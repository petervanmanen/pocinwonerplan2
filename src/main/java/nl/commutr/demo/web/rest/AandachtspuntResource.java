package nl.commutr.demo.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import nl.commutr.demo.domain.Aandachtspunt;
import nl.commutr.demo.repository.AandachtspuntRepository;
import nl.commutr.demo.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link nl.commutr.demo.domain.Aandachtspunt}.
 */
@RestController
@RequestMapping("/api/aandachtspunts")
@Transactional
public class AandachtspuntResource {

    private final Logger log = LoggerFactory.getLogger(AandachtspuntResource.class);

    private static final String ENTITY_NAME = "aandachtspunt";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AandachtspuntRepository aandachtspuntRepository;

    public AandachtspuntResource(AandachtspuntRepository aandachtspuntRepository) {
        this.aandachtspuntRepository = aandachtspuntRepository;
    }

    /**
     * {@code POST  /aandachtspunts} : Create a new aandachtspunt.
     *
     * @param aandachtspunt the aandachtspunt to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aandachtspunt, or with status {@code 400 (Bad Request)} if the aandachtspunt has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Aandachtspunt> createAandachtspunt(@RequestBody Aandachtspunt aandachtspunt) throws URISyntaxException {
        log.debug("REST request to save Aandachtspunt : {}", aandachtspunt);
        if (aandachtspunt.getId() != null) {
            throw new BadRequestAlertException("A new aandachtspunt cannot already have an ID", ENTITY_NAME, "idexists");
        }
        aandachtspunt = aandachtspuntRepository.save(aandachtspunt);
        return ResponseEntity.created(new URI("/api/aandachtspunts/" + aandachtspunt.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, aandachtspunt.getId().toString()))
            .body(aandachtspunt);
    }

    /**
     * {@code PUT  /aandachtspunts/:id} : Updates an existing aandachtspunt.
     *
     * @param id the id of the aandachtspunt to save.
     * @param aandachtspunt the aandachtspunt to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aandachtspunt,
     * or with status {@code 400 (Bad Request)} if the aandachtspunt is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aandachtspunt couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Aandachtspunt> updateAandachtspunt(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Aandachtspunt aandachtspunt
    ) throws URISyntaxException {
        log.debug("REST request to update Aandachtspunt : {}, {}", id, aandachtspunt);
        if (aandachtspunt.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aandachtspunt.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aandachtspuntRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        aandachtspunt = aandachtspuntRepository.save(aandachtspunt);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, aandachtspunt.getId().toString()))
            .body(aandachtspunt);
    }

    /**
     * {@code PATCH  /aandachtspunts/:id} : Partial updates given fields of an existing aandachtspunt, field will ignore if it is null
     *
     * @param id the id of the aandachtspunt to save.
     * @param aandachtspunt the aandachtspunt to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aandachtspunt,
     * or with status {@code 400 (Bad Request)} if the aandachtspunt is not valid,
     * or with status {@code 404 (Not Found)} if the aandachtspunt is not found,
     * or with status {@code 500 (Internal Server Error)} if the aandachtspunt couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Aandachtspunt> partialUpdateAandachtspunt(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Aandachtspunt aandachtspunt
    ) throws URISyntaxException {
        log.debug("REST request to partial update Aandachtspunt partially : {}, {}", id, aandachtspunt);
        if (aandachtspunt.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aandachtspunt.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aandachtspuntRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Aandachtspunt> result = aandachtspuntRepository
            .findById(aandachtspunt.getId())
            .map(existingAandachtspunt -> {
                if (aandachtspunt.getCode() != null) {
                    existingAandachtspunt.setCode(aandachtspunt.getCode());
                }
                if (aandachtspunt.getNaam() != null) {
                    existingAandachtspunt.setNaam(aandachtspunt.getNaam());
                }
                if (aandachtspunt.getOmschrijving() != null) {
                    existingAandachtspunt.setOmschrijving(aandachtspunt.getOmschrijving());
                }
                if (aandachtspunt.getActief() != null) {
                    existingAandachtspunt.setActief(aandachtspunt.getActief());
                }

                return existingAandachtspunt;
            })
            .map(aandachtspuntRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, aandachtspunt.getId().toString())
        );
    }

    /**
     * {@code GET  /aandachtspunts} : get all the aandachtspunts.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aandachtspunts in body.
     */
    @GetMapping("")
    public List<Aandachtspunt> getAllAandachtspunts(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get all Aandachtspunts");
        if (eagerload) {
            return aandachtspuntRepository.findAllWithEagerRelationships();
        } else {
            return aandachtspuntRepository.findAll();
        }
    }

    /**
     * {@code GET  /aandachtspunts/:id} : get the "id" aandachtspunt.
     *
     * @param id the id of the aandachtspunt to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aandachtspunt, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Aandachtspunt> getAandachtspunt(@PathVariable("id") Long id) {
        log.debug("REST request to get Aandachtspunt : {}", id);
        Optional<Aandachtspunt> aandachtspunt = aandachtspuntRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(aandachtspunt);
    }

    /**
     * {@code DELETE  /aandachtspunts/:id} : delete the "id" aandachtspunt.
     *
     * @param id the id of the aandachtspunt to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAandachtspunt(@PathVariable("id") Long id) {
        log.debug("REST request to delete Aandachtspunt : {}", id);
        aandachtspuntRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
