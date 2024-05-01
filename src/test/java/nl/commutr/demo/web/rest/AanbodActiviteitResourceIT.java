package nl.commutr.demo.web.rest;

import static nl.commutr.demo.domain.AanbodActiviteitAsserts.*;
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
 * Integration tests for the {@link AanbodActiviteitResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AanbodActiviteitResourceIT {

    private static final String DEFAULT_NAAM = "AAAAAAAAAA";
    private static final String UPDATED_NAAM = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIEF = false;
    private static final Boolean UPDATED_ACTIEF = true;

    private static final Integer DEFAULT_AFHANDELTERMIJN = 1;
    private static final Integer UPDATED_AFHANDELTERMIJN = 2;

    private static final String ENTITY_API_URL = "/api/aanbod-activiteits";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AanbodActiviteitRepository aanbodActiviteitRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAanbodActiviteitMockMvc;

    private AanbodActiviteit aanbodActiviteit;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AanbodActiviteit createEntity(EntityManager em) {
        AanbodActiviteit aanbodActiviteit = new AanbodActiviteit()
            .naam(DEFAULT_NAAM)
            .actief(DEFAULT_ACTIEF)
            .afhandeltermijn(DEFAULT_AFHANDELTERMIJN);
        return aanbodActiviteit;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AanbodActiviteit createUpdatedEntity(EntityManager em) {
        AanbodActiviteit aanbodActiviteit = new AanbodActiviteit()
            .naam(UPDATED_NAAM)
            .actief(UPDATED_ACTIEF)
            .afhandeltermijn(UPDATED_AFHANDELTERMIJN);
        return aanbodActiviteit;
    }

    @BeforeEach
    public void initTest() {
        aanbodActiviteit = createEntity(em);
    }

    @Test
    @Transactional
    void createAanbodActiviteit() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AanbodActiviteit
        var returnedAanbodActiviteit = om.readValue(
            restAanbodActiviteitMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aanbodActiviteit)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AanbodActiviteit.class
        );

        // Validate the AanbodActiviteit in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAanbodActiviteitUpdatableFieldsEquals(returnedAanbodActiviteit, getPersistedAanbodActiviteit(returnedAanbodActiviteit));
    }

    @Test
    @Transactional
    void createAanbodActiviteitWithExistingId() throws Exception {
        // Create the AanbodActiviteit with an existing ID
        aanbodActiviteitRepository.saveAndFlush(aanbodActiviteit);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAanbodActiviteitMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aanbodActiviteit)))
            .andExpect(status().isBadRequest());

        // Validate the AanbodActiviteit in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAanbodActiviteits() throws Exception {
        // Initialize the database
        aanbodActiviteitRepository.saveAndFlush(aanbodActiviteit);

        // Get all the aanbodActiviteitList
        restAanbodActiviteitMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aanbodActiviteit.getId().toString())))
            .andExpect(jsonPath("$.[*].naam").value(hasItem(DEFAULT_NAAM)))
            .andExpect(jsonPath("$.[*].actief").value(hasItem(DEFAULT_ACTIEF.booleanValue())))
            .andExpect(jsonPath("$.[*].afhandeltermijn").value(hasItem(DEFAULT_AFHANDELTERMIJN)));
    }

    @Test
    @Transactional
    void getAanbodActiviteit() throws Exception {
        // Initialize the database
        aanbodActiviteitRepository.saveAndFlush(aanbodActiviteit);

        // Get the aanbodActiviteit
        restAanbodActiviteitMockMvc
            .perform(get(ENTITY_API_URL_ID, aanbodActiviteit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(aanbodActiviteit.getId().toString()))
            .andExpect(jsonPath("$.naam").value(DEFAULT_NAAM))
            .andExpect(jsonPath("$.actief").value(DEFAULT_ACTIEF.booleanValue()))
            .andExpect(jsonPath("$.afhandeltermijn").value(DEFAULT_AFHANDELTERMIJN));
    }

    @Test
    @Transactional
    void getNonExistingAanbodActiviteit() throws Exception {
        // Get the aanbodActiviteit
        restAanbodActiviteitMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAanbodActiviteit() throws Exception {
        // Initialize the database
        aanbodActiviteitRepository.saveAndFlush(aanbodActiviteit);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the aanbodActiviteit
        AanbodActiviteit updatedAanbodActiviteit = aanbodActiviteitRepository.findById(aanbodActiviteit.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAanbodActiviteit are not directly saved in db
        em.detach(updatedAanbodActiviteit);
        updatedAanbodActiviteit.naam(UPDATED_NAAM).actief(UPDATED_ACTIEF).afhandeltermijn(UPDATED_AFHANDELTERMIJN);

        restAanbodActiviteitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAanbodActiviteit.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAanbodActiviteit))
            )
            .andExpect(status().isOk());

        // Validate the AanbodActiviteit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAanbodActiviteitToMatchAllProperties(updatedAanbodActiviteit);
    }

    @Test
    @Transactional
    void putNonExistingAanbodActiviteit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aanbodActiviteit.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAanbodActiviteitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aanbodActiviteit.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(aanbodActiviteit))
            )
            .andExpect(status().isBadRequest());

        // Validate the AanbodActiviteit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAanbodActiviteit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aanbodActiviteit.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAanbodActiviteitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(aanbodActiviteit))
            )
            .andExpect(status().isBadRequest());

        // Validate the AanbodActiviteit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAanbodActiviteit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aanbodActiviteit.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAanbodActiviteitMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aanbodActiviteit)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AanbodActiviteit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAanbodActiviteitWithPatch() throws Exception {
        // Initialize the database
        aanbodActiviteitRepository.saveAndFlush(aanbodActiviteit);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the aanbodActiviteit using partial update
        AanbodActiviteit partialUpdatedAanbodActiviteit = new AanbodActiviteit();
        partialUpdatedAanbodActiviteit.setId(aanbodActiviteit.getId());

        partialUpdatedAanbodActiviteit.actief(UPDATED_ACTIEF);

        restAanbodActiviteitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAanbodActiviteit.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAanbodActiviteit))
            )
            .andExpect(status().isOk());

        // Validate the AanbodActiviteit in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAanbodActiviteitUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAanbodActiviteit, aanbodActiviteit),
            getPersistedAanbodActiviteit(aanbodActiviteit)
        );
    }

    @Test
    @Transactional
    void fullUpdateAanbodActiviteitWithPatch() throws Exception {
        // Initialize the database
        aanbodActiviteitRepository.saveAndFlush(aanbodActiviteit);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the aanbodActiviteit using partial update
        AanbodActiviteit partialUpdatedAanbodActiviteit = new AanbodActiviteit();
        partialUpdatedAanbodActiviteit.setId(aanbodActiviteit.getId());

        partialUpdatedAanbodActiviteit.naam(UPDATED_NAAM).actief(UPDATED_ACTIEF).afhandeltermijn(UPDATED_AFHANDELTERMIJN);

        restAanbodActiviteitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAanbodActiviteit.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAanbodActiviteit))
            )
            .andExpect(status().isOk());

        // Validate the AanbodActiviteit in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAanbodActiviteitUpdatableFieldsEquals(
            partialUpdatedAanbodActiviteit,
            getPersistedAanbodActiviteit(partialUpdatedAanbodActiviteit)
        );
    }

    @Test
    @Transactional
    void patchNonExistingAanbodActiviteit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aanbodActiviteit.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAanbodActiviteitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, aanbodActiviteit.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(aanbodActiviteit))
            )
            .andExpect(status().isBadRequest());

        // Validate the AanbodActiviteit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAanbodActiviteit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aanbodActiviteit.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAanbodActiviteitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(aanbodActiviteit))
            )
            .andExpect(status().isBadRequest());

        // Validate the AanbodActiviteit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAanbodActiviteit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aanbodActiviteit.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAanbodActiviteitMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(aanbodActiviteit)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AanbodActiviteit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAanbodActiviteit() throws Exception {
        // Initialize the database
        aanbodActiviteitRepository.saveAndFlush(aanbodActiviteit);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the aanbodActiviteit
        restAanbodActiviteitMockMvc
            .perform(delete(ENTITY_API_URL_ID, aanbodActiviteit.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return aanbodActiviteitRepository.count();
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

    protected AanbodActiviteit getPersistedAanbodActiviteit(AanbodActiviteit aanbodActiviteit) {
        return aanbodActiviteitRepository.findById(aanbodActiviteit.getId()).orElseThrow();
    }

    protected void assertPersistedAanbodActiviteitToMatchAllProperties(AanbodActiviteit expectedAanbodActiviteit) {
        assertAanbodActiviteitAllPropertiesEquals(expectedAanbodActiviteit, getPersistedAanbodActiviteit(expectedAanbodActiviteit));
    }

    protected void assertPersistedAanbodActiviteitToMatchUpdatableProperties(AanbodActiviteit expectedAanbodActiviteit) {
        assertAanbodActiviteitAllUpdatablePropertiesEquals(
            expectedAanbodActiviteit,
            getPersistedAanbodActiviteit(expectedAanbodActiviteit)
        );
    }
}
