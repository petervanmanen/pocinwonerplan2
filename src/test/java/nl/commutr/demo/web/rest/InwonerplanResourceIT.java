package nl.commutr.demo.web.rest;

import static nl.commutr.demo.domain.InwonerplanAsserts.*;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link InwonerplanResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InwonerplanResourceIT {

    private static final String DEFAULT_BSN = "AAAAAAAAAA";
    private static final String UPDATED_BSN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/inwonerplans";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InwonerplanRepository inwonerplanRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInwonerplanMockMvc;

    private Inwonerplan inwonerplan;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Inwonerplan createEntity(EntityManager em) {
        Inwonerplan inwonerplan = new Inwonerplan().bsn(DEFAULT_BSN);
        return inwonerplan;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Inwonerplan createUpdatedEntity(EntityManager em) {
        Inwonerplan inwonerplan = new Inwonerplan().bsn(UPDATED_BSN);
        return inwonerplan;
    }

    @BeforeEach
    public void initTest() {
        inwonerplan = createEntity(em);
    }

    @Test
    @Transactional
    void createInwonerplan() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Inwonerplan
        var returnedInwonerplan = om.readValue(
            restInwonerplanMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inwonerplan)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Inwonerplan.class
        );

        // Validate the Inwonerplan in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertInwonerplanUpdatableFieldsEquals(returnedInwonerplan, getPersistedInwonerplan(returnedInwonerplan));
    }

    @Test
    @Transactional
    void createInwonerplanWithExistingId() throws Exception {
        // Create the Inwonerplan with an existing ID
        inwonerplan.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInwonerplanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inwonerplan)))
            .andExpect(status().isBadRequest());

        // Validate the Inwonerplan in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkBsnIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        inwonerplan.setBsn(null);

        // Create the Inwonerplan, which fails.

        restInwonerplanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inwonerplan)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInwonerplans() throws Exception {
        // Initialize the database
        inwonerplanRepository.saveAndFlush(inwonerplan);

        // Get all the inwonerplanList
        restInwonerplanMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inwonerplan.getId().intValue())))
            .andExpect(jsonPath("$.[*].bsn").value(hasItem(DEFAULT_BSN)));
    }

    @Test
    @Transactional
    void getInwonerplan() throws Exception {
        // Initialize the database
        inwonerplanRepository.saveAndFlush(inwonerplan);

        // Get the inwonerplan
        restInwonerplanMockMvc
            .perform(get(ENTITY_API_URL_ID, inwonerplan.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(inwonerplan.getId().intValue()))
            .andExpect(jsonPath("$.bsn").value(DEFAULT_BSN));
    }

    @Test
    @Transactional
    void getNonExistingInwonerplan() throws Exception {
        // Get the inwonerplan
        restInwonerplanMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInwonerplan() throws Exception {
        // Initialize the database
        inwonerplanRepository.saveAndFlush(inwonerplan);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the inwonerplan
        Inwonerplan updatedInwonerplan = inwonerplanRepository.findById(inwonerplan.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedInwonerplan are not directly saved in db
        em.detach(updatedInwonerplan);
        updatedInwonerplan.bsn(UPDATED_BSN);

        restInwonerplanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInwonerplan.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedInwonerplan))
            )
            .andExpect(status().isOk());

        // Validate the Inwonerplan in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInwonerplanToMatchAllProperties(updatedInwonerplan);
    }

    @Test
    @Transactional
    void putNonExistingInwonerplan() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inwonerplan.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInwonerplanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inwonerplan.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(inwonerplan))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inwonerplan in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInwonerplan() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inwonerplan.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInwonerplanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(inwonerplan))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inwonerplan in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInwonerplan() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inwonerplan.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInwonerplanMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inwonerplan)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Inwonerplan in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInwonerplanWithPatch() throws Exception {
        // Initialize the database
        inwonerplanRepository.saveAndFlush(inwonerplan);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the inwonerplan using partial update
        Inwonerplan partialUpdatedInwonerplan = new Inwonerplan();
        partialUpdatedInwonerplan.setId(inwonerplan.getId());

        restInwonerplanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInwonerplan.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInwonerplan))
            )
            .andExpect(status().isOk());

        // Validate the Inwonerplan in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInwonerplanUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedInwonerplan, inwonerplan),
            getPersistedInwonerplan(inwonerplan)
        );
    }

    @Test
    @Transactional
    void fullUpdateInwonerplanWithPatch() throws Exception {
        // Initialize the database
        inwonerplanRepository.saveAndFlush(inwonerplan);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the inwonerplan using partial update
        Inwonerplan partialUpdatedInwonerplan = new Inwonerplan();
        partialUpdatedInwonerplan.setId(inwonerplan.getId());

        partialUpdatedInwonerplan.bsn(UPDATED_BSN);

        restInwonerplanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInwonerplan.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInwonerplan))
            )
            .andExpect(status().isOk());

        // Validate the Inwonerplan in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInwonerplanUpdatableFieldsEquals(partialUpdatedInwonerplan, getPersistedInwonerplan(partialUpdatedInwonerplan));
    }

    @Test
    @Transactional
    void patchNonExistingInwonerplan() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inwonerplan.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInwonerplanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, inwonerplan.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(inwonerplan))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inwonerplan in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInwonerplan() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inwonerplan.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInwonerplanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(inwonerplan))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inwonerplan in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInwonerplan() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inwonerplan.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInwonerplanMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(inwonerplan)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Inwonerplan in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInwonerplan() throws Exception {
        // Initialize the database
        inwonerplanRepository.saveAndFlush(inwonerplan);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the inwonerplan
        restInwonerplanMockMvc
            .perform(delete(ENTITY_API_URL_ID, inwonerplan.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return inwonerplanRepository.count();
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

    protected Inwonerplan getPersistedInwonerplan(Inwonerplan inwonerplan) {
        return inwonerplanRepository.findById(inwonerplan.getId()).orElseThrow();
    }

    protected void assertPersistedInwonerplanToMatchAllProperties(Inwonerplan expectedInwonerplan) {
        assertInwonerplanAllPropertiesEquals(expectedInwonerplan, getPersistedInwonerplan(expectedInwonerplan));
    }

    protected void assertPersistedInwonerplanToMatchUpdatableProperties(Inwonerplan expectedInwonerplan) {
        assertInwonerplanAllUpdatablePropertiesEquals(expectedInwonerplan, getPersistedInwonerplan(expectedInwonerplan));
    }
}
