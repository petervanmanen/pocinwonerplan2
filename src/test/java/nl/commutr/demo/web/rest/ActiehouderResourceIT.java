package nl.commutr.demo.web.rest;

import static nl.commutr.demo.domain.ActiehouderAsserts.*;
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
 * Integration tests for the {@link ActiehouderResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ActiehouderResourceIT {

    private static final String DEFAULT_NAAM = "AAAAAAAAAA";
    private static final String UPDATED_NAAM = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/actiehouders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ActiehouderRepository actiehouderRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restActiehouderMockMvc;

    private Actiehouder actiehouder;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Actiehouder createEntity(EntityManager em) {
        Actiehouder actiehouder = new Actiehouder().naam(DEFAULT_NAAM);
        return actiehouder;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Actiehouder createUpdatedEntity(EntityManager em) {
        Actiehouder actiehouder = new Actiehouder().naam(UPDATED_NAAM);
        return actiehouder;
    }

    @BeforeEach
    public void initTest() {
        actiehouder = createEntity(em);
    }

    @Test
    @Transactional
    void createActiehouder() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Actiehouder
        var returnedActiehouder = om.readValue(
            restActiehouderMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(actiehouder)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Actiehouder.class
        );

        // Validate the Actiehouder in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertActiehouderUpdatableFieldsEquals(returnedActiehouder, getPersistedActiehouder(returnedActiehouder));
    }

    @Test
    @Transactional
    void createActiehouderWithExistingId() throws Exception {
        // Create the Actiehouder with an existing ID
        actiehouderRepository.saveAndFlush(actiehouder);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restActiehouderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(actiehouder)))
            .andExpect(status().isBadRequest());

        // Validate the Actiehouder in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllActiehouders() throws Exception {
        // Initialize the database
        actiehouderRepository.saveAndFlush(actiehouder);

        // Get all the actiehouderList
        restActiehouderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(actiehouder.getId().toString())))
            .andExpect(jsonPath("$.[*].naam").value(hasItem(DEFAULT_NAAM)));
    }

    @Test
    @Transactional
    void getActiehouder() throws Exception {
        // Initialize the database
        actiehouderRepository.saveAndFlush(actiehouder);

        // Get the actiehouder
        restActiehouderMockMvc
            .perform(get(ENTITY_API_URL_ID, actiehouder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(actiehouder.getId().toString()))
            .andExpect(jsonPath("$.naam").value(DEFAULT_NAAM));
    }

    @Test
    @Transactional
    void getNonExistingActiehouder() throws Exception {
        // Get the actiehouder
        restActiehouderMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingActiehouder() throws Exception {
        // Initialize the database
        actiehouderRepository.saveAndFlush(actiehouder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the actiehouder
        Actiehouder updatedActiehouder = actiehouderRepository.findById(actiehouder.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedActiehouder are not directly saved in db
        em.detach(updatedActiehouder);
        updatedActiehouder.naam(UPDATED_NAAM);

        restActiehouderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedActiehouder.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedActiehouder))
            )
            .andExpect(status().isOk());

        // Validate the Actiehouder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedActiehouderToMatchAllProperties(updatedActiehouder);
    }

    @Test
    @Transactional
    void putNonExistingActiehouder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        actiehouder.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActiehouderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, actiehouder.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(actiehouder))
            )
            .andExpect(status().isBadRequest());

        // Validate the Actiehouder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchActiehouder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        actiehouder.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActiehouderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(actiehouder))
            )
            .andExpect(status().isBadRequest());

        // Validate the Actiehouder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamActiehouder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        actiehouder.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActiehouderMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(actiehouder)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Actiehouder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateActiehouderWithPatch() throws Exception {
        // Initialize the database
        actiehouderRepository.saveAndFlush(actiehouder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the actiehouder using partial update
        Actiehouder partialUpdatedActiehouder = new Actiehouder();
        partialUpdatedActiehouder.setId(actiehouder.getId());

        partialUpdatedActiehouder.naam(UPDATED_NAAM);

        restActiehouderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedActiehouder.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedActiehouder))
            )
            .andExpect(status().isOk());

        // Validate the Actiehouder in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertActiehouderUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedActiehouder, actiehouder),
            getPersistedActiehouder(actiehouder)
        );
    }

    @Test
    @Transactional
    void fullUpdateActiehouderWithPatch() throws Exception {
        // Initialize the database
        actiehouderRepository.saveAndFlush(actiehouder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the actiehouder using partial update
        Actiehouder partialUpdatedActiehouder = new Actiehouder();
        partialUpdatedActiehouder.setId(actiehouder.getId());

        partialUpdatedActiehouder.naam(UPDATED_NAAM);

        restActiehouderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedActiehouder.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedActiehouder))
            )
            .andExpect(status().isOk());

        // Validate the Actiehouder in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertActiehouderUpdatableFieldsEquals(partialUpdatedActiehouder, getPersistedActiehouder(partialUpdatedActiehouder));
    }

    @Test
    @Transactional
    void patchNonExistingActiehouder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        actiehouder.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActiehouderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, actiehouder.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(actiehouder))
            )
            .andExpect(status().isBadRequest());

        // Validate the Actiehouder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchActiehouder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        actiehouder.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActiehouderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(actiehouder))
            )
            .andExpect(status().isBadRequest());

        // Validate the Actiehouder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamActiehouder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        actiehouder.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActiehouderMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(actiehouder)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Actiehouder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteActiehouder() throws Exception {
        // Initialize the database
        actiehouderRepository.saveAndFlush(actiehouder);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the actiehouder
        restActiehouderMockMvc
            .perform(delete(ENTITY_API_URL_ID, actiehouder.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return actiehouderRepository.count();
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

    protected Actiehouder getPersistedActiehouder(Actiehouder actiehouder) {
        return actiehouderRepository.findById(actiehouder.getId()).orElseThrow();
    }

    protected void assertPersistedActiehouderToMatchAllProperties(Actiehouder expectedActiehouder) {
        assertActiehouderAllPropertiesEquals(expectedActiehouder, getPersistedActiehouder(expectedActiehouder));
    }

    protected void assertPersistedActiehouderToMatchUpdatableProperties(Actiehouder expectedActiehouder) {
        assertActiehouderAllUpdatablePropertiesEquals(expectedActiehouder, getPersistedActiehouder(expectedActiehouder));
    }
}
