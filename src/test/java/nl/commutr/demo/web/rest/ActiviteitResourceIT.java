package nl.commutr.demo.web.rest;

import static nl.commutr.demo.domain.ActiviteitAsserts.*;
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
import nl.commutr.demo.domain.Activiteit;
import nl.commutr.demo.repository.ActiviteitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ActiviteitResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ActiviteitResourceIT {

    private static final Integer DEFAULT_CODE = 1;
    private static final Integer UPDATED_CODE = 2;

    private static final String DEFAULT_NAAM = "AAAAAAAAAA";
    private static final String UPDATED_NAAM = "BBBBBBBBBB";

    private static final String DEFAULT_ACTIEHOUDER = "AAAAAAAAAA";
    private static final String UPDATED_ACTIEHOUDER = "BBBBBBBBBB";

    private static final Integer DEFAULT_AFHANDELTERMIJN = 1;
    private static final Integer UPDATED_AFHANDELTERMIJN = 2;

    private static final Boolean DEFAULT_ACTIEF = false;
    private static final Boolean UPDATED_ACTIEF = true;

    private static final String ENTITY_API_URL = "/api/activiteits";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ActiviteitRepository activiteitRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restActiviteitMockMvc;

    private Activiteit activiteit;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Activiteit createEntity(EntityManager em) {
        Activiteit activiteit = new Activiteit()
            .code(DEFAULT_CODE)
            .naam(DEFAULT_NAAM)
            .actiehouder(DEFAULT_ACTIEHOUDER)
            .afhandeltermijn(DEFAULT_AFHANDELTERMIJN)
            .actief(DEFAULT_ACTIEF);
        return activiteit;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Activiteit createUpdatedEntity(EntityManager em) {
        Activiteit activiteit = new Activiteit()
            .code(UPDATED_CODE)
            .naam(UPDATED_NAAM)
            .actiehouder(UPDATED_ACTIEHOUDER)
            .afhandeltermijn(UPDATED_AFHANDELTERMIJN)
            .actief(UPDATED_ACTIEF);
        return activiteit;
    }

    @BeforeEach
    public void initTest() {
        activiteit = createEntity(em);
    }

    @Test
    @Transactional
    void createActiviteit() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Activiteit
        var returnedActiviteit = om.readValue(
            restActiviteitMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(activiteit)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Activiteit.class
        );

        // Validate the Activiteit in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertActiviteitUpdatableFieldsEquals(returnedActiviteit, getPersistedActiviteit(returnedActiviteit));
    }

    @Test
    @Transactional
    void createActiviteitWithExistingId() throws Exception {
        // Create the Activiteit with an existing ID
        activiteit.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restActiviteitMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(activiteit)))
            .andExpect(status().isBadRequest());

        // Validate the Activiteit in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllActiviteits() throws Exception {
        // Initialize the database
        activiteitRepository.saveAndFlush(activiteit);

        // Get all the activiteitList
        restActiviteitMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(activiteit.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].naam").value(hasItem(DEFAULT_NAAM)))
            .andExpect(jsonPath("$.[*].actiehouder").value(hasItem(DEFAULT_ACTIEHOUDER)))
            .andExpect(jsonPath("$.[*].afhandeltermijn").value(hasItem(DEFAULT_AFHANDELTERMIJN)))
            .andExpect(jsonPath("$.[*].actief").value(hasItem(DEFAULT_ACTIEF.booleanValue())));
    }

    @Test
    @Transactional
    void getActiviteit() throws Exception {
        // Initialize the database
        activiteitRepository.saveAndFlush(activiteit);

        // Get the activiteit
        restActiviteitMockMvc
            .perform(get(ENTITY_API_URL_ID, activiteit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(activiteit.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.naam").value(DEFAULT_NAAM))
            .andExpect(jsonPath("$.actiehouder").value(DEFAULT_ACTIEHOUDER))
            .andExpect(jsonPath("$.afhandeltermijn").value(DEFAULT_AFHANDELTERMIJN))
            .andExpect(jsonPath("$.actief").value(DEFAULT_ACTIEF.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingActiviteit() throws Exception {
        // Get the activiteit
        restActiviteitMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingActiviteit() throws Exception {
        // Initialize the database
        activiteitRepository.saveAndFlush(activiteit);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the activiteit
        Activiteit updatedActiviteit = activiteitRepository.findById(activiteit.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedActiviteit are not directly saved in db
        em.detach(updatedActiviteit);
        updatedActiviteit
            .code(UPDATED_CODE)
            .naam(UPDATED_NAAM)
            .actiehouder(UPDATED_ACTIEHOUDER)
            .afhandeltermijn(UPDATED_AFHANDELTERMIJN)
            .actief(UPDATED_ACTIEF);

        restActiviteitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedActiviteit.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedActiviteit))
            )
            .andExpect(status().isOk());

        // Validate the Activiteit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedActiviteitToMatchAllProperties(updatedActiviteit);
    }

    @Test
    @Transactional
    void putNonExistingActiviteit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        activiteit.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActiviteitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, activiteit.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(activiteit))
            )
            .andExpect(status().isBadRequest());

        // Validate the Activiteit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchActiviteit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        activiteit.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActiviteitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(activiteit))
            )
            .andExpect(status().isBadRequest());

        // Validate the Activiteit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamActiviteit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        activiteit.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActiviteitMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(activiteit)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Activiteit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateActiviteitWithPatch() throws Exception {
        // Initialize the database
        activiteitRepository.saveAndFlush(activiteit);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the activiteit using partial update
        Activiteit partialUpdatedActiviteit = new Activiteit();
        partialUpdatedActiviteit.setId(activiteit.getId());

        partialUpdatedActiviteit.code(UPDATED_CODE).actiehouder(UPDATED_ACTIEHOUDER).actief(UPDATED_ACTIEF);

        restActiviteitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedActiviteit.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedActiviteit))
            )
            .andExpect(status().isOk());

        // Validate the Activiteit in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertActiviteitUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedActiviteit, activiteit),
            getPersistedActiviteit(activiteit)
        );
    }

    @Test
    @Transactional
    void fullUpdateActiviteitWithPatch() throws Exception {
        // Initialize the database
        activiteitRepository.saveAndFlush(activiteit);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the activiteit using partial update
        Activiteit partialUpdatedActiviteit = new Activiteit();
        partialUpdatedActiviteit.setId(activiteit.getId());

        partialUpdatedActiviteit
            .code(UPDATED_CODE)
            .naam(UPDATED_NAAM)
            .actiehouder(UPDATED_ACTIEHOUDER)
            .afhandeltermijn(UPDATED_AFHANDELTERMIJN)
            .actief(UPDATED_ACTIEF);

        restActiviteitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedActiviteit.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedActiviteit))
            )
            .andExpect(status().isOk());

        // Validate the Activiteit in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertActiviteitUpdatableFieldsEquals(partialUpdatedActiviteit, getPersistedActiviteit(partialUpdatedActiviteit));
    }

    @Test
    @Transactional
    void patchNonExistingActiviteit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        activiteit.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActiviteitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, activiteit.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(activiteit))
            )
            .andExpect(status().isBadRequest());

        // Validate the Activiteit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchActiviteit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        activiteit.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActiviteitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(activiteit))
            )
            .andExpect(status().isBadRequest());

        // Validate the Activiteit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamActiviteit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        activiteit.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActiviteitMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(activiteit)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Activiteit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteActiviteit() throws Exception {
        // Initialize the database
        activiteitRepository.saveAndFlush(activiteit);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the activiteit
        restActiviteitMockMvc
            .perform(delete(ENTITY_API_URL_ID, activiteit.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return activiteitRepository.count();
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

    protected Activiteit getPersistedActiviteit(Activiteit activiteit) {
        return activiteitRepository.findById(activiteit.getId()).orElseThrow();
    }

    protected void assertPersistedActiviteitToMatchAllProperties(Activiteit expectedActiviteit) {
        assertActiviteitAllPropertiesEquals(expectedActiviteit, getPersistedActiviteit(expectedActiviteit));
    }

    protected void assertPersistedActiviteitToMatchUpdatableProperties(Activiteit expectedActiviteit) {
        assertActiviteitAllUpdatablePropertiesEquals(expectedActiviteit, getPersistedActiviteit(expectedActiviteit));
    }
}
