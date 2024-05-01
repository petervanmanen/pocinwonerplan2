package nl.commutr.demo.web.rest;

import static nl.commutr.demo.domain.AanbodAsserts.*;
import static nl.commutr.demo.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import nl.commutr.demo.IntegrationTest;
import nl.commutr.demo.domain.Aanbod;
import nl.commutr.demo.repository.AanbodRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AanbodResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AanbodResourceIT {

    private static final String DEFAULT_NAAM = "AAAAAAAAAA";
    private static final String UPDATED_NAAM = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/aanbods";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AanbodRepository aanbodRepository;

    @Mock
    private AanbodRepository aanbodRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAanbodMockMvc;

    private Aanbod aanbod;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Aanbod createEntity(EntityManager em) {
        Aanbod aanbod = new Aanbod().naam(DEFAULT_NAAM);
        return aanbod;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Aanbod createUpdatedEntity(EntityManager em) {
        Aanbod aanbod = new Aanbod().naam(UPDATED_NAAM);
        return aanbod;
    }

    @BeforeEach
    public void initTest() {
        aanbod = createEntity(em);
    }

    @Test
    @Transactional
    void createAanbod() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Aanbod
        var returnedAanbod = om.readValue(
            restAanbodMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aanbod)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Aanbod.class
        );

        // Validate the Aanbod in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAanbodUpdatableFieldsEquals(returnedAanbod, getPersistedAanbod(returnedAanbod));
    }

    @Test
    @Transactional
    void createAanbodWithExistingId() throws Exception {
        // Create the Aanbod with an existing ID
        aanbod.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAanbodMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aanbod)))
            .andExpect(status().isBadRequest());

        // Validate the Aanbod in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAanbods() throws Exception {
        // Initialize the database
        aanbodRepository.saveAndFlush(aanbod);

        // Get all the aanbodList
        restAanbodMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aanbod.getId().intValue())))
            .andExpect(jsonPath("$.[*].naam").value(hasItem(DEFAULT_NAAM)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAanbodsWithEagerRelationshipsIsEnabled() throws Exception {
        when(aanbodRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAanbodMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(aanbodRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAanbodsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(aanbodRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAanbodMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(aanbodRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getAanbod() throws Exception {
        // Initialize the database
        aanbodRepository.saveAndFlush(aanbod);

        // Get the aanbod
        restAanbodMockMvc
            .perform(get(ENTITY_API_URL_ID, aanbod.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(aanbod.getId().intValue()))
            .andExpect(jsonPath("$.naam").value(DEFAULT_NAAM));
    }

    @Test
    @Transactional
    void getNonExistingAanbod() throws Exception {
        // Get the aanbod
        restAanbodMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAanbod() throws Exception {
        // Initialize the database
        aanbodRepository.saveAndFlush(aanbod);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the aanbod
        Aanbod updatedAanbod = aanbodRepository.findById(aanbod.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAanbod are not directly saved in db
        em.detach(updatedAanbod);
        updatedAanbod.naam(UPDATED_NAAM);

        restAanbodMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAanbod.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAanbod))
            )
            .andExpect(status().isOk());

        // Validate the Aanbod in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAanbodToMatchAllProperties(updatedAanbod);
    }

    @Test
    @Transactional
    void putNonExistingAanbod() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aanbod.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAanbodMockMvc
            .perform(put(ENTITY_API_URL_ID, aanbod.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aanbod)))
            .andExpect(status().isBadRequest());

        // Validate the Aanbod in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAanbod() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aanbod.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAanbodMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(aanbod))
            )
            .andExpect(status().isBadRequest());

        // Validate the Aanbod in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAanbod() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aanbod.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAanbodMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aanbod)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Aanbod in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAanbodWithPatch() throws Exception {
        // Initialize the database
        aanbodRepository.saveAndFlush(aanbod);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the aanbod using partial update
        Aanbod partialUpdatedAanbod = new Aanbod();
        partialUpdatedAanbod.setId(aanbod.getId());

        partialUpdatedAanbod.naam(UPDATED_NAAM);

        restAanbodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAanbod.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAanbod))
            )
            .andExpect(status().isOk());

        // Validate the Aanbod in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAanbodUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedAanbod, aanbod), getPersistedAanbod(aanbod));
    }

    @Test
    @Transactional
    void fullUpdateAanbodWithPatch() throws Exception {
        // Initialize the database
        aanbodRepository.saveAndFlush(aanbod);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the aanbod using partial update
        Aanbod partialUpdatedAanbod = new Aanbod();
        partialUpdatedAanbod.setId(aanbod.getId());

        partialUpdatedAanbod.naam(UPDATED_NAAM);

        restAanbodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAanbod.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAanbod))
            )
            .andExpect(status().isOk());

        // Validate the Aanbod in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAanbodUpdatableFieldsEquals(partialUpdatedAanbod, getPersistedAanbod(partialUpdatedAanbod));
    }

    @Test
    @Transactional
    void patchNonExistingAanbod() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aanbod.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAanbodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, aanbod.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(aanbod))
            )
            .andExpect(status().isBadRequest());

        // Validate the Aanbod in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAanbod() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aanbod.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAanbodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(aanbod))
            )
            .andExpect(status().isBadRequest());

        // Validate the Aanbod in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAanbod() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aanbod.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAanbodMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(aanbod)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Aanbod in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAanbod() throws Exception {
        // Initialize the database
        aanbodRepository.saveAndFlush(aanbod);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the aanbod
        restAanbodMockMvc
            .perform(delete(ENTITY_API_URL_ID, aanbod.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return aanbodRepository.count();
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

    protected Aanbod getPersistedAanbod(Aanbod aanbod) {
        return aanbodRepository.findById(aanbod.getId()).orElseThrow();
    }

    protected void assertPersistedAanbodToMatchAllProperties(Aanbod expectedAanbod) {
        assertAanbodAllPropertiesEquals(expectedAanbod, getPersistedAanbod(expectedAanbod));
    }

    protected void assertPersistedAanbodToMatchUpdatableProperties(Aanbod expectedAanbod) {
        assertAanbodAllUpdatablePropertiesEquals(expectedAanbod, getPersistedAanbod(expectedAanbod));
    }
}
