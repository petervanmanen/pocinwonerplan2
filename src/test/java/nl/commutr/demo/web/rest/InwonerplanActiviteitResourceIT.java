package nl.commutr.demo.web.rest;

import static nl.commutr.demo.domain.InwonerplanActiviteitAsserts.*;
import static nl.commutr.demo.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.UUID;
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
 * Integration tests for the {@link InwonerplanActiviteitResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InwonerplanActiviteitResourceIT {

    private static final String DEFAULT_NAAM = "AAAAAAAAAA";
    private static final String UPDATED_NAAM = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIEF = false;
    private static final Boolean UPDATED_ACTIEF = true;

    private static final Integer DEFAULT_AFHANDELTERMIJN = 1;
    private static final Integer UPDATED_AFHANDELTERMIJN = 2;

    private static final String ENTITY_API_URL = "/api/inwonerplan-activiteits";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InwonerplanActiviteitRepository inwonerplanActiviteitRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInwonerplanActiviteitMockMvc;

    private InwonerplanActiviteit inwonerplanActiviteit;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InwonerplanActiviteit createEntity(EntityManager em) {
        InwonerplanActiviteit inwonerplanActiviteit = new InwonerplanActiviteit()
            .naam(DEFAULT_NAAM)
            .actief(DEFAULT_ACTIEF)
            .afhandeltermijn(DEFAULT_AFHANDELTERMIJN);
        return inwonerplanActiviteit;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InwonerplanActiviteit createUpdatedEntity(EntityManager em) {
        InwonerplanActiviteit inwonerplanActiviteit = new InwonerplanActiviteit()
            .naam(UPDATED_NAAM)
            .actief(UPDATED_ACTIEF)
            .afhandeltermijn(UPDATED_AFHANDELTERMIJN);
        return inwonerplanActiviteit;
    }

    @BeforeEach
    public void initTest() {
        inwonerplanActiviteit = createEntity(em);
    }

    @Test
    @Transactional
    void createInwonerplanActiviteit() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the InwonerplanActiviteit
        var returnedInwonerplanActiviteit = om.readValue(
            restInwonerplanActiviteitMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inwonerplanActiviteit)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            InwonerplanActiviteit.class
        );

        // Validate the InwonerplanActiviteit in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertInwonerplanActiviteitUpdatableFieldsEquals(
            returnedInwonerplanActiviteit,
            getPersistedInwonerplanActiviteit(returnedInwonerplanActiviteit)
        );
    }

    @Test
    @Transactional
    void createInwonerplanActiviteitWithExistingId() throws Exception {
        // Create the InwonerplanActiviteit with an existing ID
        inwonerplanActiviteitRepository.saveAndFlush(inwonerplanActiviteit);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInwonerplanActiviteitMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inwonerplanActiviteit)))
            .andExpect(status().isBadRequest());

        // Validate the InwonerplanActiviteit in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllInwonerplanActiviteits() throws Exception {
        // Initialize the database
        inwonerplanActiviteitRepository.saveAndFlush(inwonerplanActiviteit);

        // Get all the inwonerplanActiviteitList
        restInwonerplanActiviteitMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inwonerplanActiviteit.getId().toString())))
            .andExpect(jsonPath("$.[*].naam").value(hasItem(DEFAULT_NAAM)))
            .andExpect(jsonPath("$.[*].actief").value(hasItem(DEFAULT_ACTIEF.booleanValue())))
            .andExpect(jsonPath("$.[*].afhandeltermijn").value(hasItem(DEFAULT_AFHANDELTERMIJN)));
    }

    @Test
    @Transactional
    void getInwonerplanActiviteit() throws Exception {
        // Initialize the database
        inwonerplanActiviteitRepository.saveAndFlush(inwonerplanActiviteit);

        // Get the inwonerplanActiviteit
        restInwonerplanActiviteitMockMvc
            .perform(get(ENTITY_API_URL_ID, inwonerplanActiviteit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(inwonerplanActiviteit.getId().toString()))
            .andExpect(jsonPath("$.naam").value(DEFAULT_NAAM))
            .andExpect(jsonPath("$.actief").value(DEFAULT_ACTIEF.booleanValue()))
            .andExpect(jsonPath("$.afhandeltermijn").value(DEFAULT_AFHANDELTERMIJN));
    }

    @Test
    @Transactional
    void getNonExistingInwonerplanActiviteit() throws Exception {
        // Get the inwonerplanActiviteit
        restInwonerplanActiviteitMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInwonerplanActiviteit() throws Exception {
        // Initialize the database
        inwonerplanActiviteitRepository.saveAndFlush(inwonerplanActiviteit);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the inwonerplanActiviteit
        InwonerplanActiviteit updatedInwonerplanActiviteit = inwonerplanActiviteitRepository
            .findById(inwonerplanActiviteit.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedInwonerplanActiviteit are not directly saved in db
        em.detach(updatedInwonerplanActiviteit);
        updatedInwonerplanActiviteit.naam(UPDATED_NAAM).actief(UPDATED_ACTIEF).afhandeltermijn(UPDATED_AFHANDELTERMIJN);

        restInwonerplanActiviteitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInwonerplanActiviteit.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedInwonerplanActiviteit))
            )
            .andExpect(status().isOk());

        // Validate the InwonerplanActiviteit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInwonerplanActiviteitToMatchAllProperties(updatedInwonerplanActiviteit);
    }

    @Test
    @Transactional
    void putNonExistingInwonerplanActiviteit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inwonerplanActiviteit.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInwonerplanActiviteitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inwonerplanActiviteit.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(inwonerplanActiviteit))
            )
            .andExpect(status().isBadRequest());

        // Validate the InwonerplanActiviteit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInwonerplanActiviteit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inwonerplanActiviteit.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInwonerplanActiviteitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(inwonerplanActiviteit))
            )
            .andExpect(status().isBadRequest());

        // Validate the InwonerplanActiviteit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInwonerplanActiviteit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inwonerplanActiviteit.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInwonerplanActiviteitMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inwonerplanActiviteit)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InwonerplanActiviteit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInwonerplanActiviteitWithPatch() throws Exception {
        // Initialize the database
        inwonerplanActiviteitRepository.saveAndFlush(inwonerplanActiviteit);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the inwonerplanActiviteit using partial update
        InwonerplanActiviteit partialUpdatedInwonerplanActiviteit = new InwonerplanActiviteit();
        partialUpdatedInwonerplanActiviteit.setId(inwonerplanActiviteit.getId());

        restInwonerplanActiviteitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInwonerplanActiviteit.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInwonerplanActiviteit))
            )
            .andExpect(status().isOk());

        // Validate the InwonerplanActiviteit in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInwonerplanActiviteitUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedInwonerplanActiviteit, inwonerplanActiviteit),
            getPersistedInwonerplanActiviteit(inwonerplanActiviteit)
        );
    }

    @Test
    @Transactional
    void fullUpdateInwonerplanActiviteitWithPatch() throws Exception {
        // Initialize the database
        inwonerplanActiviteitRepository.saveAndFlush(inwonerplanActiviteit);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the inwonerplanActiviteit using partial update
        InwonerplanActiviteit partialUpdatedInwonerplanActiviteit = new InwonerplanActiviteit();
        partialUpdatedInwonerplanActiviteit.setId(inwonerplanActiviteit.getId());

        partialUpdatedInwonerplanActiviteit.naam(UPDATED_NAAM).actief(UPDATED_ACTIEF).afhandeltermijn(UPDATED_AFHANDELTERMIJN);

        restInwonerplanActiviteitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInwonerplanActiviteit.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInwonerplanActiviteit))
            )
            .andExpect(status().isOk());

        // Validate the InwonerplanActiviteit in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInwonerplanActiviteitUpdatableFieldsEquals(
            partialUpdatedInwonerplanActiviteit,
            getPersistedInwonerplanActiviteit(partialUpdatedInwonerplanActiviteit)
        );
    }

    @Test
    @Transactional
    void patchNonExistingInwonerplanActiviteit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inwonerplanActiviteit.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInwonerplanActiviteitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, inwonerplanActiviteit.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(inwonerplanActiviteit))
            )
            .andExpect(status().isBadRequest());

        // Validate the InwonerplanActiviteit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInwonerplanActiviteit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inwonerplanActiviteit.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInwonerplanActiviteitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(inwonerplanActiviteit))
            )
            .andExpect(status().isBadRequest());

        // Validate the InwonerplanActiviteit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInwonerplanActiviteit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inwonerplanActiviteit.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInwonerplanActiviteitMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(inwonerplanActiviteit)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InwonerplanActiviteit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInwonerplanActiviteit() throws Exception {
        // Initialize the database
        inwonerplanActiviteitRepository.saveAndFlush(inwonerplanActiviteit);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the inwonerplanActiviteit
        restInwonerplanActiviteitMockMvc
            .perform(delete(ENTITY_API_URL_ID, inwonerplanActiviteit.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return inwonerplanActiviteitRepository.count();
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

    protected InwonerplanActiviteit getPersistedInwonerplanActiviteit(InwonerplanActiviteit inwonerplanActiviteit) {
        return inwonerplanActiviteitRepository.findById(inwonerplanActiviteit.getId()).orElseThrow();
    }

    protected void assertPersistedInwonerplanActiviteitToMatchAllProperties(InwonerplanActiviteit expectedInwonerplanActiviteit) {
        assertInwonerplanActiviteitAllPropertiesEquals(
            expectedInwonerplanActiviteit,
            getPersistedInwonerplanActiviteit(expectedInwonerplanActiviteit)
        );
    }

    protected void assertPersistedInwonerplanActiviteitToMatchUpdatableProperties(InwonerplanActiviteit expectedInwonerplanActiviteit) {
        assertInwonerplanActiviteitAllUpdatablePropertiesEquals(
            expectedInwonerplanActiviteit,
            getPersistedInwonerplanActiviteit(expectedInwonerplanActiviteit)
        );
    }
}
