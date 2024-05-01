package nl.commutr.demo.web.rest;

import static nl.commutr.demo.domain.HoofddoelAsserts.*;
import static nl.commutr.demo.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import nl.commutr.demo.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link HoofddoelResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HoofddoelResourceIT {

    private static final LocalDate DEFAULT_BEGINDATUM = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BEGINDATUM = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_NAAM = "AAAAAAAAAA";
    private static final String UPDATED_NAAM = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/hoofddoels";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private HoofddoelRepository hoofddoelRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHoofddoelMockMvc;

    private Hoofddoel hoofddoel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Hoofddoel createEntity(EntityManager em) {
        Hoofddoel hoofddoel = new Hoofddoel().begindatum(DEFAULT_BEGINDATUM).naam(DEFAULT_NAAM);
        return hoofddoel;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Hoofddoel createUpdatedEntity(EntityManager em) {
        Hoofddoel hoofddoel = new Hoofddoel().begindatum(UPDATED_BEGINDATUM).naam(UPDATED_NAAM);
        return hoofddoel;
    }

    @BeforeEach
    public void initTest() {
        hoofddoel = createEntity(em);
    }

    @Test
    @Transactional
    void createHoofddoel() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Hoofddoel
        var returnedHoofddoel = om.readValue(
            restHoofddoelMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hoofddoel)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Hoofddoel.class
        );

        // Validate the Hoofddoel in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertHoofddoelUpdatableFieldsEquals(returnedHoofddoel, getPersistedHoofddoel(returnedHoofddoel));
    }

    @Test
    @Transactional
    void createHoofddoelWithExistingId() throws Exception {
        // Create the Hoofddoel with an existing ID
        hoofddoel.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHoofddoelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hoofddoel)))
            .andExpect(status().isBadRequest());

        // Validate the Hoofddoel in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllHoofddoels() throws Exception {
        // Initialize the database
        hoofddoelRepository.saveAndFlush(hoofddoel);

        // Get all the hoofddoelList
        restHoofddoelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hoofddoel.getId().intValue())))
            .andExpect(jsonPath("$.[*].begindatum").value(hasItem(DEFAULT_BEGINDATUM.toString())))
            .andExpect(jsonPath("$.[*].naam").value(hasItem(DEFAULT_NAAM)));
    }

    @Test
    @Transactional
    void getHoofddoel() throws Exception {
        // Initialize the database
        hoofddoelRepository.saveAndFlush(hoofddoel);

        // Get the hoofddoel
        restHoofddoelMockMvc
            .perform(get(ENTITY_API_URL_ID, hoofddoel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(hoofddoel.getId().intValue()))
            .andExpect(jsonPath("$.begindatum").value(DEFAULT_BEGINDATUM.toString()))
            .andExpect(jsonPath("$.naam").value(DEFAULT_NAAM));
    }

    @Test
    @Transactional
    void getNonExistingHoofddoel() throws Exception {
        // Get the hoofddoel
        restHoofddoelMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHoofddoel() throws Exception {
        // Initialize the database
        hoofddoelRepository.saveAndFlush(hoofddoel);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hoofddoel
        Hoofddoel updatedHoofddoel = hoofddoelRepository.findById(hoofddoel.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHoofddoel are not directly saved in db
        em.detach(updatedHoofddoel);
        updatedHoofddoel.begindatum(UPDATED_BEGINDATUM).naam(UPDATED_NAAM);

        restHoofddoelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedHoofddoel.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedHoofddoel))
            )
            .andExpect(status().isOk());

        // Validate the Hoofddoel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedHoofddoelToMatchAllProperties(updatedHoofddoel);
    }

    @Test
    @Transactional
    void putNonExistingHoofddoel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hoofddoel.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHoofddoelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hoofddoel.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hoofddoel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Hoofddoel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHoofddoel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hoofddoel.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHoofddoelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hoofddoel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Hoofddoel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHoofddoel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hoofddoel.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHoofddoelMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hoofddoel)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Hoofddoel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHoofddoelWithPatch() throws Exception {
        // Initialize the database
        hoofddoelRepository.saveAndFlush(hoofddoel);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hoofddoel using partial update
        Hoofddoel partialUpdatedHoofddoel = new Hoofddoel();
        partialUpdatedHoofddoel.setId(hoofddoel.getId());

        restHoofddoelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHoofddoel.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHoofddoel))
            )
            .andExpect(status().isOk());

        // Validate the Hoofddoel in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHoofddoelUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedHoofddoel, hoofddoel),
            getPersistedHoofddoel(hoofddoel)
        );
    }

    @Test
    @Transactional
    void fullUpdateHoofddoelWithPatch() throws Exception {
        // Initialize the database
        hoofddoelRepository.saveAndFlush(hoofddoel);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hoofddoel using partial update
        Hoofddoel partialUpdatedHoofddoel = new Hoofddoel();
        partialUpdatedHoofddoel.setId(hoofddoel.getId());

        partialUpdatedHoofddoel.begindatum(UPDATED_BEGINDATUM).naam(UPDATED_NAAM);

        restHoofddoelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHoofddoel.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHoofddoel))
            )
            .andExpect(status().isOk());

        // Validate the Hoofddoel in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHoofddoelUpdatableFieldsEquals(partialUpdatedHoofddoel, getPersistedHoofddoel(partialUpdatedHoofddoel));
    }

    @Test
    @Transactional
    void patchNonExistingHoofddoel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hoofddoel.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHoofddoelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, hoofddoel.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hoofddoel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Hoofddoel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHoofddoel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hoofddoel.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHoofddoelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hoofddoel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Hoofddoel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHoofddoel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hoofddoel.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHoofddoelMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(hoofddoel)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Hoofddoel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHoofddoel() throws Exception {
        // Initialize the database
        hoofddoelRepository.saveAndFlush(hoofddoel);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the hoofddoel
        restHoofddoelMockMvc
            .perform(delete(ENTITY_API_URL_ID, hoofddoel.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return hoofddoelRepository.count();
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

    protected Hoofddoel getPersistedHoofddoel(Hoofddoel hoofddoel) {
        return hoofddoelRepository.findById(hoofddoel.getId()).orElseThrow();
    }

    protected void assertPersistedHoofddoelToMatchAllProperties(Hoofddoel expectedHoofddoel) {
        assertHoofddoelAllPropertiesEquals(expectedHoofddoel, getPersistedHoofddoel(expectedHoofddoel));
    }

    protected void assertPersistedHoofddoelToMatchUpdatableProperties(Hoofddoel expectedHoofddoel) {
        assertHoofddoelAllUpdatablePropertiesEquals(expectedHoofddoel, getPersistedHoofddoel(expectedHoofddoel));
    }
}
