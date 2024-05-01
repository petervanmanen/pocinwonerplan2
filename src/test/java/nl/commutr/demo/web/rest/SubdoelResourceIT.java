package nl.commutr.demo.web.rest;

import static nl.commutr.demo.domain.SubdoelAsserts.*;
import static nl.commutr.demo.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import nl.commutr.demo.IntegrationTest;
import nl.commutr.demo.domain.Subdoel;
import nl.commutr.demo.repository.SubdoelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SubdoelResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SubdoelResourceIT {

    private static final Integer DEFAULT_CODE = 1;
    private static final Integer UPDATED_CODE = 2;

    private static final String DEFAULT_NAAM = "AAAAAAAAAA";
    private static final String UPDATED_NAAM = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIEF = false;
    private static final Boolean UPDATED_ACTIEF = true;

    private static final String ENTITY_API_URL = "/api/subdoels";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SubdoelRepository subdoelRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSubdoelMockMvc;

    private Subdoel subdoel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Subdoel createEntity(EntityManager em) {
        Subdoel subdoel = new Subdoel().code(DEFAULT_CODE).naam(DEFAULT_NAAM).actief(DEFAULT_ACTIEF);
        return subdoel;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Subdoel createUpdatedEntity(EntityManager em) {
        Subdoel subdoel = new Subdoel().code(UPDATED_CODE).naam(UPDATED_NAAM).actief(UPDATED_ACTIEF);
        return subdoel;
    }

    @BeforeEach
    public void initTest() {
        subdoel = createEntity(em);
    }

    @Test
    @Transactional
    void createSubdoel() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Subdoel
        var returnedSubdoel = om.readValue(
            restSubdoelMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subdoel)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Subdoel.class
        );

        // Validate the Subdoel in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertSubdoelUpdatableFieldsEquals(returnedSubdoel, getPersistedSubdoel(returnedSubdoel));
    }

    @Test
    @Transactional
    void createSubdoelWithExistingId() throws Exception {
        // Create the Subdoel with an existing ID
        subdoel.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubdoelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subdoel)))
            .andExpect(status().isBadRequest());

        // Validate the Subdoel in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSubdoels() throws Exception {
        // Initialize the database
        subdoelRepository.saveAndFlush(subdoel);

        // Get all the subdoelList
        restSubdoelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subdoel.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].naam").value(hasItem(DEFAULT_NAAM)))
            .andExpect(jsonPath("$.[*].actief").value(hasItem(DEFAULT_ACTIEF.booleanValue())));
    }

    @Test
    @Transactional
    void getSubdoel() throws Exception {
        // Initialize the database
        subdoelRepository.saveAndFlush(subdoel);

        // Get the subdoel
        restSubdoelMockMvc
            .perform(get(ENTITY_API_URL_ID, subdoel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(subdoel.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.naam").value(DEFAULT_NAAM))
            .andExpect(jsonPath("$.actief").value(DEFAULT_ACTIEF.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingSubdoel() throws Exception {
        // Get the subdoel
        restSubdoelMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSubdoel() throws Exception {
        // Initialize the database
        subdoelRepository.saveAndFlush(subdoel);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the subdoel
        Subdoel updatedSubdoel = subdoelRepository.findById(subdoel.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSubdoel are not directly saved in db
        em.detach(updatedSubdoel);
        updatedSubdoel.code(UPDATED_CODE).naam(UPDATED_NAAM).actief(UPDATED_ACTIEF);

        restSubdoelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSubdoel.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedSubdoel))
            )
            .andExpect(status().isOk());

        // Validate the Subdoel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSubdoelToMatchAllProperties(updatedSubdoel);
    }

    @Test
    @Transactional
    void putNonExistingSubdoel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subdoel.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubdoelMockMvc
            .perform(put(ENTITY_API_URL_ID, subdoel.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subdoel)))
            .andExpect(status().isBadRequest());

        // Validate the Subdoel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSubdoel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subdoel.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubdoelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(subdoel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Subdoel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSubdoel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subdoel.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubdoelMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subdoel)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Subdoel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSubdoelWithPatch() throws Exception {
        // Initialize the database
        subdoelRepository.saveAndFlush(subdoel);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the subdoel using partial update
        Subdoel partialUpdatedSubdoel = new Subdoel();
        partialUpdatedSubdoel.setId(subdoel.getId());

        partialUpdatedSubdoel.naam(UPDATED_NAAM).actief(UPDATED_ACTIEF);

        restSubdoelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubdoel.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSubdoel))
            )
            .andExpect(status().isOk());

        // Validate the Subdoel in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSubdoelUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedSubdoel, subdoel), getPersistedSubdoel(subdoel));
    }

    @Test
    @Transactional
    void fullUpdateSubdoelWithPatch() throws Exception {
        // Initialize the database
        subdoelRepository.saveAndFlush(subdoel);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the subdoel using partial update
        Subdoel partialUpdatedSubdoel = new Subdoel();
        partialUpdatedSubdoel.setId(subdoel.getId());

        partialUpdatedSubdoel.code(UPDATED_CODE).naam(UPDATED_NAAM).actief(UPDATED_ACTIEF);

        restSubdoelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubdoel.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSubdoel))
            )
            .andExpect(status().isOk());

        // Validate the Subdoel in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSubdoelUpdatableFieldsEquals(partialUpdatedSubdoel, getPersistedSubdoel(partialUpdatedSubdoel));
    }

    @Test
    @Transactional
    void patchNonExistingSubdoel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subdoel.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubdoelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, subdoel.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(subdoel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Subdoel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSubdoel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subdoel.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubdoelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(subdoel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Subdoel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSubdoel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subdoel.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubdoelMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(subdoel)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Subdoel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSubdoel() throws Exception {
        // Initialize the database
        subdoelRepository.saveAndFlush(subdoel);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the subdoel
        restSubdoelMockMvc
            .perform(delete(ENTITY_API_URL_ID, subdoel.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return subdoelRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Subdoel getPersistedSubdoel(Subdoel subdoel) {
        return subdoelRepository.findById(subdoel.getId()).orElseThrow();
    }

    protected void assertPersistedSubdoelToMatchAllProperties(Subdoel expectedSubdoel) {
        assertSubdoelAllPropertiesEquals(expectedSubdoel, getPersistedSubdoel(expectedSubdoel));
    }

    protected void assertPersistedSubdoelToMatchUpdatableProperties(Subdoel expectedSubdoel) {
        assertSubdoelAllUpdatablePropertiesEquals(expectedSubdoel, getPersistedSubdoel(expectedSubdoel));
    }
}
