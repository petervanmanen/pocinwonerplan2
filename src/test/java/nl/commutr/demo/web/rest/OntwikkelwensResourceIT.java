package nl.commutr.demo.web.rest;

import static nl.commutr.demo.domain.OntwikkelwensAsserts.*;
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
import nl.commutr.demo.domain.Ontwikkelwens;
import nl.commutr.demo.repository.OntwikkelwensRepository;
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
 * Integration tests for the {@link OntwikkelwensResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class OntwikkelwensResourceIT {

    private static final Integer DEFAULT_CODE = 1;
    private static final Integer UPDATED_CODE = 2;

    private static final String DEFAULT_NAAM = "AAAAAAAAAA";
    private static final String UPDATED_NAAM = "BBBBBBBBBB";

    private static final String DEFAULT_OMSCHRIJVING = "AAAAAAAAAA";
    private static final String UPDATED_OMSCHRIJVING = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIEF = false;
    private static final Boolean UPDATED_ACTIEF = true;

    private static final String ENTITY_API_URL = "/api/ontwikkelwens";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OntwikkelwensRepository ontwikkelwensRepository;

    @Mock
    private OntwikkelwensRepository ontwikkelwensRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOntwikkelwensMockMvc;

    private Ontwikkelwens ontwikkelwens;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ontwikkelwens createEntity(EntityManager em) {
        Ontwikkelwens ontwikkelwens = new Ontwikkelwens()
            .code(DEFAULT_CODE)
            .naam(DEFAULT_NAAM)
            .omschrijving(DEFAULT_OMSCHRIJVING)
            .actief(DEFAULT_ACTIEF);
        return ontwikkelwens;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ontwikkelwens createUpdatedEntity(EntityManager em) {
        Ontwikkelwens ontwikkelwens = new Ontwikkelwens()
            .code(UPDATED_CODE)
            .naam(UPDATED_NAAM)
            .omschrijving(UPDATED_OMSCHRIJVING)
            .actief(UPDATED_ACTIEF);
        return ontwikkelwens;
    }

    @BeforeEach
    public void initTest() {
        ontwikkelwens = createEntity(em);
    }

    @Test
    @Transactional
    void createOntwikkelwens() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Ontwikkelwens
        var returnedOntwikkelwens = om.readValue(
            restOntwikkelwensMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ontwikkelwens)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Ontwikkelwens.class
        );

        // Validate the Ontwikkelwens in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertOntwikkelwensUpdatableFieldsEquals(returnedOntwikkelwens, getPersistedOntwikkelwens(returnedOntwikkelwens));
    }

    @Test
    @Transactional
    void createOntwikkelwensWithExistingId() throws Exception {
        // Create the Ontwikkelwens with an existing ID
        ontwikkelwens.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOntwikkelwensMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ontwikkelwens)))
            .andExpect(status().isBadRequest());

        // Validate the Ontwikkelwens in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOntwikkelwens() throws Exception {
        // Initialize the database
        ontwikkelwensRepository.saveAndFlush(ontwikkelwens);

        // Get all the ontwikkelwensList
        restOntwikkelwensMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ontwikkelwens.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].naam").value(hasItem(DEFAULT_NAAM)))
            .andExpect(jsonPath("$.[*].omschrijving").value(hasItem(DEFAULT_OMSCHRIJVING)))
            .andExpect(jsonPath("$.[*].actief").value(hasItem(DEFAULT_ACTIEF.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOntwikkelwensWithEagerRelationshipsIsEnabled() throws Exception {
        when(ontwikkelwensRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOntwikkelwensMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(ontwikkelwensRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOntwikkelwensWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(ontwikkelwensRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOntwikkelwensMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(ontwikkelwensRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getOntwikkelwens() throws Exception {
        // Initialize the database
        ontwikkelwensRepository.saveAndFlush(ontwikkelwens);

        // Get the ontwikkelwens
        restOntwikkelwensMockMvc
            .perform(get(ENTITY_API_URL_ID, ontwikkelwens.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ontwikkelwens.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.naam").value(DEFAULT_NAAM))
            .andExpect(jsonPath("$.omschrijving").value(DEFAULT_OMSCHRIJVING))
            .andExpect(jsonPath("$.actief").value(DEFAULT_ACTIEF.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingOntwikkelwens() throws Exception {
        // Get the ontwikkelwens
        restOntwikkelwensMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOntwikkelwens() throws Exception {
        // Initialize the database
        ontwikkelwensRepository.saveAndFlush(ontwikkelwens);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ontwikkelwens
        Ontwikkelwens updatedOntwikkelwens = ontwikkelwensRepository.findById(ontwikkelwens.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOntwikkelwens are not directly saved in db
        em.detach(updatedOntwikkelwens);
        updatedOntwikkelwens.code(UPDATED_CODE).naam(UPDATED_NAAM).omschrijving(UPDATED_OMSCHRIJVING).actief(UPDATED_ACTIEF);

        restOntwikkelwensMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOntwikkelwens.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedOntwikkelwens))
            )
            .andExpect(status().isOk());

        // Validate the Ontwikkelwens in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOntwikkelwensToMatchAllProperties(updatedOntwikkelwens);
    }

    @Test
    @Transactional
    void putNonExistingOntwikkelwens() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ontwikkelwens.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOntwikkelwensMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ontwikkelwens.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ontwikkelwens))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ontwikkelwens in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOntwikkelwens() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ontwikkelwens.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOntwikkelwensMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ontwikkelwens))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ontwikkelwens in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOntwikkelwens() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ontwikkelwens.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOntwikkelwensMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ontwikkelwens)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ontwikkelwens in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOntwikkelwensWithPatch() throws Exception {
        // Initialize the database
        ontwikkelwensRepository.saveAndFlush(ontwikkelwens);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ontwikkelwens using partial update
        Ontwikkelwens partialUpdatedOntwikkelwens = new Ontwikkelwens();
        partialUpdatedOntwikkelwens.setId(ontwikkelwens.getId());

        partialUpdatedOntwikkelwens.naam(UPDATED_NAAM);

        restOntwikkelwensMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOntwikkelwens.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOntwikkelwens))
            )
            .andExpect(status().isOk());

        // Validate the Ontwikkelwens in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOntwikkelwensUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedOntwikkelwens, ontwikkelwens),
            getPersistedOntwikkelwens(ontwikkelwens)
        );
    }

    @Test
    @Transactional
    void fullUpdateOntwikkelwensWithPatch() throws Exception {
        // Initialize the database
        ontwikkelwensRepository.saveAndFlush(ontwikkelwens);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ontwikkelwens using partial update
        Ontwikkelwens partialUpdatedOntwikkelwens = new Ontwikkelwens();
        partialUpdatedOntwikkelwens.setId(ontwikkelwens.getId());

        partialUpdatedOntwikkelwens.code(UPDATED_CODE).naam(UPDATED_NAAM).omschrijving(UPDATED_OMSCHRIJVING).actief(UPDATED_ACTIEF);

        restOntwikkelwensMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOntwikkelwens.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOntwikkelwens))
            )
            .andExpect(status().isOk());

        // Validate the Ontwikkelwens in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOntwikkelwensUpdatableFieldsEquals(partialUpdatedOntwikkelwens, getPersistedOntwikkelwens(partialUpdatedOntwikkelwens));
    }

    @Test
    @Transactional
    void patchNonExistingOntwikkelwens() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ontwikkelwens.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOntwikkelwensMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ontwikkelwens.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ontwikkelwens))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ontwikkelwens in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOntwikkelwens() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ontwikkelwens.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOntwikkelwensMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ontwikkelwens))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ontwikkelwens in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOntwikkelwens() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ontwikkelwens.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOntwikkelwensMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(ontwikkelwens)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ontwikkelwens in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOntwikkelwens() throws Exception {
        // Initialize the database
        ontwikkelwensRepository.saveAndFlush(ontwikkelwens);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the ontwikkelwens
        restOntwikkelwensMockMvc
            .perform(delete(ENTITY_API_URL_ID, ontwikkelwens.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return ontwikkelwensRepository.count();
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

    protected Ontwikkelwens getPersistedOntwikkelwens(Ontwikkelwens ontwikkelwens) {
        return ontwikkelwensRepository.findById(ontwikkelwens.getId()).orElseThrow();
    }

    protected void assertPersistedOntwikkelwensToMatchAllProperties(Ontwikkelwens expectedOntwikkelwens) {
        assertOntwikkelwensAllPropertiesEquals(expectedOntwikkelwens, getPersistedOntwikkelwens(expectedOntwikkelwens));
    }

    protected void assertPersistedOntwikkelwensToMatchUpdatableProperties(Ontwikkelwens expectedOntwikkelwens) {
        assertOntwikkelwensAllUpdatablePropertiesEquals(expectedOntwikkelwens, getPersistedOntwikkelwens(expectedOntwikkelwens));
    }
}
