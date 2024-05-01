package nl.commutr.demo.web.rest;

import static nl.commutr.demo.domain.AandachtspuntAsserts.*;
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
import nl.commutr.demo.domain.Aandachtspunt;
import nl.commutr.demo.repository.AandachtspuntRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AandachtspuntResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AandachtspuntResourceIT {

    private static final Integer DEFAULT_CODE = 1;
    private static final Integer UPDATED_CODE = 2;

    private static final String DEFAULT_NAAM = "AAAAAAAAAA";
    private static final String UPDATED_NAAM = "BBBBBBBBBB";

    private static final String DEFAULT_OMSCHRIJVING = "AAAAAAAAAA";
    private static final String UPDATED_OMSCHRIJVING = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIEF = false;
    private static final Boolean UPDATED_ACTIEF = true;

    private static final String ENTITY_API_URL = "/api/aandachtspunts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AandachtspuntRepository aandachtspuntRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAandachtspuntMockMvc;

    private Aandachtspunt aandachtspunt;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Aandachtspunt createEntity(EntityManager em) {
        Aandachtspunt aandachtspunt = new Aandachtspunt()
            .code(DEFAULT_CODE)
            .naam(DEFAULT_NAAM)
            .omschrijving(DEFAULT_OMSCHRIJVING)
            .actief(DEFAULT_ACTIEF);
        return aandachtspunt;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Aandachtspunt createUpdatedEntity(EntityManager em) {
        Aandachtspunt aandachtspunt = new Aandachtspunt()
            .code(UPDATED_CODE)
            .naam(UPDATED_NAAM)
            .omschrijving(UPDATED_OMSCHRIJVING)
            .actief(UPDATED_ACTIEF);
        return aandachtspunt;
    }

    @BeforeEach
    public void initTest() {
        aandachtspunt = createEntity(em);
    }

    @Test
    @Transactional
    void createAandachtspunt() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Aandachtspunt
        var returnedAandachtspunt = om.readValue(
            restAandachtspuntMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aandachtspunt)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Aandachtspunt.class
        );

        // Validate the Aandachtspunt in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAandachtspuntUpdatableFieldsEquals(returnedAandachtspunt, getPersistedAandachtspunt(returnedAandachtspunt));
    }

    @Test
    @Transactional
    void createAandachtspuntWithExistingId() throws Exception {
        // Create the Aandachtspunt with an existing ID
        aandachtspunt.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAandachtspuntMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aandachtspunt)))
            .andExpect(status().isBadRequest());

        // Validate the Aandachtspunt in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAandachtspunts() throws Exception {
        // Initialize the database
        aandachtspuntRepository.saveAndFlush(aandachtspunt);

        // Get all the aandachtspuntList
        restAandachtspuntMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aandachtspunt.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].naam").value(hasItem(DEFAULT_NAAM)))
            .andExpect(jsonPath("$.[*].omschrijving").value(hasItem(DEFAULT_OMSCHRIJVING)))
            .andExpect(jsonPath("$.[*].actief").value(hasItem(DEFAULT_ACTIEF.booleanValue())));
    }

    @Test
    @Transactional
    void getAandachtspunt() throws Exception {
        // Initialize the database
        aandachtspuntRepository.saveAndFlush(aandachtspunt);

        // Get the aandachtspunt
        restAandachtspuntMockMvc
            .perform(get(ENTITY_API_URL_ID, aandachtspunt.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(aandachtspunt.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.naam").value(DEFAULT_NAAM))
            .andExpect(jsonPath("$.omschrijving").value(DEFAULT_OMSCHRIJVING))
            .andExpect(jsonPath("$.actief").value(DEFAULT_ACTIEF.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingAandachtspunt() throws Exception {
        // Get the aandachtspunt
        restAandachtspuntMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAandachtspunt() throws Exception {
        // Initialize the database
        aandachtspuntRepository.saveAndFlush(aandachtspunt);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the aandachtspunt
        Aandachtspunt updatedAandachtspunt = aandachtspuntRepository.findById(aandachtspunt.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAandachtspunt are not directly saved in db
        em.detach(updatedAandachtspunt);
        updatedAandachtspunt.code(UPDATED_CODE).naam(UPDATED_NAAM).omschrijving(UPDATED_OMSCHRIJVING).actief(UPDATED_ACTIEF);

        restAandachtspuntMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAandachtspunt.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAandachtspunt))
            )
            .andExpect(status().isOk());

        // Validate the Aandachtspunt in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAandachtspuntToMatchAllProperties(updatedAandachtspunt);
    }

    @Test
    @Transactional
    void putNonExistingAandachtspunt() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aandachtspunt.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAandachtspuntMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aandachtspunt.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(aandachtspunt))
            )
            .andExpect(status().isBadRequest());

        // Validate the Aandachtspunt in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAandachtspunt() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aandachtspunt.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAandachtspuntMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(aandachtspunt))
            )
            .andExpect(status().isBadRequest());

        // Validate the Aandachtspunt in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAandachtspunt() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aandachtspunt.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAandachtspuntMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aandachtspunt)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Aandachtspunt in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAandachtspuntWithPatch() throws Exception {
        // Initialize the database
        aandachtspuntRepository.saveAndFlush(aandachtspunt);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the aandachtspunt using partial update
        Aandachtspunt partialUpdatedAandachtspunt = new Aandachtspunt();
        partialUpdatedAandachtspunt.setId(aandachtspunt.getId());

        partialUpdatedAandachtspunt.code(UPDATED_CODE).naam(UPDATED_NAAM).actief(UPDATED_ACTIEF);

        restAandachtspuntMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAandachtspunt.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAandachtspunt))
            )
            .andExpect(status().isOk());

        // Validate the Aandachtspunt in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAandachtspuntUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAandachtspunt, aandachtspunt),
            getPersistedAandachtspunt(aandachtspunt)
        );
    }

    @Test
    @Transactional
    void fullUpdateAandachtspuntWithPatch() throws Exception {
        // Initialize the database
        aandachtspuntRepository.saveAndFlush(aandachtspunt);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the aandachtspunt using partial update
        Aandachtspunt partialUpdatedAandachtspunt = new Aandachtspunt();
        partialUpdatedAandachtspunt.setId(aandachtspunt.getId());

        partialUpdatedAandachtspunt.code(UPDATED_CODE).naam(UPDATED_NAAM).omschrijving(UPDATED_OMSCHRIJVING).actief(UPDATED_ACTIEF);

        restAandachtspuntMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAandachtspunt.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAandachtspunt))
            )
            .andExpect(status().isOk());

        // Validate the Aandachtspunt in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAandachtspuntUpdatableFieldsEquals(partialUpdatedAandachtspunt, getPersistedAandachtspunt(partialUpdatedAandachtspunt));
    }

    @Test
    @Transactional
    void patchNonExistingAandachtspunt() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aandachtspunt.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAandachtspuntMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, aandachtspunt.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(aandachtspunt))
            )
            .andExpect(status().isBadRequest());

        // Validate the Aandachtspunt in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAandachtspunt() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aandachtspunt.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAandachtspuntMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(aandachtspunt))
            )
            .andExpect(status().isBadRequest());

        // Validate the Aandachtspunt in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAandachtspunt() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aandachtspunt.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAandachtspuntMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(aandachtspunt)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Aandachtspunt in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAandachtspunt() throws Exception {
        // Initialize the database
        aandachtspuntRepository.saveAndFlush(aandachtspunt);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the aandachtspunt
        restAandachtspuntMockMvc
            .perform(delete(ENTITY_API_URL_ID, aandachtspunt.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return aandachtspuntRepository.count();
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

    protected Aandachtspunt getPersistedAandachtspunt(Aandachtspunt aandachtspunt) {
        return aandachtspuntRepository.findById(aandachtspunt.getId()).orElseThrow();
    }

    protected void assertPersistedAandachtspuntToMatchAllProperties(Aandachtspunt expectedAandachtspunt) {
        assertAandachtspuntAllPropertiesEquals(expectedAandachtspunt, getPersistedAandachtspunt(expectedAandachtspunt));
    }

    protected void assertPersistedAandachtspuntToMatchUpdatableProperties(Aandachtspunt expectedAandachtspunt) {
        assertAandachtspuntAllUpdatablePropertiesEquals(expectedAandachtspunt, getPersistedAandachtspunt(expectedAandachtspunt));
    }
}
