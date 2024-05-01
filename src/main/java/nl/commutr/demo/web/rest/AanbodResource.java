package nl.commutr.demo.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import nl.commutr.demo.domain.Aanbod;
import nl.commutr.demo.repository.AanbodRepository;
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
 * REST controller for managing {@link nl.commutr.demo.domain.Aanbod}.
 */
@RestController
@RequestMapping("/api/aanbods")
@Transactional
public class AanbodResource {

    private final Logger log = LoggerFactory.getLogger(AanbodResource.class);

    private static final String ENTITY_NAME = "aanbod";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AanbodRepository aanbodRepository;

    public AanbodResource(AanbodRepository aanbodRepository) {
        this.aanbodRepository = aanbodRepository;
    }

    /**
     * {@code POST  /aanbods} : Create a new aanbod.
     *
     * @param aanbod the aanbod to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aanbod, or with status {@code 400 (Bad Request)} if the aanbod has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Aanbod> createAanbod(@RequestBody Aanbod aanbod) throws URISyntaxException {
        log.debug("REST request to save Aanbod : {}", aanbod);
        if (aanbod.getId() != null) {
            throw new BadRequestAlertException("A new aanbod cannot already have an ID", ENTITY_NAME, "idexists");
        }
        aanbod = aanbodRepository.save(aanbod);
        return ResponseEntity.created(new URI("/api/aanbods/" + aanbod.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, aanbod.getId().toString()))
            .body(aanbod);
    }

    /**
     * {@code PUT  /aanbods/:id} : Updates an existing aanbod.
     *
     * @param id the id of the aanbod to save.
     * @param aanbod the aanbod to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aanbod,
     * or with status {@code 400 (Bad Request)} if the aanbod is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aanbod couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Aanbod> updateAanbod(@PathVariable(value = "id", required = false) final Long id, @RequestBody Aanbod aanbod)
        throws URISyntaxException {
        log.debug("REST request to update Aanbod : {}, {}", id, aanbod);
        if (aanbod.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aanbod.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aanbodRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        aanbod = aanbodRepository.save(aanbod);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, aanbod.getId().toString()))
            .body(aanbod);
    }

    /**
     * {@code PATCH  /aanbods/:id} : Partial updates given fields of an existing aanbod, field will ignore if it is null
     *
     * @param id the id of the aanbod to save.
     * @param aanbod the aanbod to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aanbod,
     * or with status {@code 400 (Bad Request)} if the aanbod is not valid,
     * or with status {@code 404 (Not Found)} if the aanbod is not found,
     * or with status {@code 500 (Internal Server Error)} if the aanbod couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Aanbod> partialUpdateAanbod(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Aanbod aanbod
    ) throws URISyntaxException {
        log.debug("REST request to partial update Aanbod partially : {}, {}", id, aanbod);
        if (aanbod.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aanbod.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aanbodRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Aanbod> result = aanbodRepository
            .findById(aanbod.getId())
            .map(existingAanbod -> {
                if (aanbod.getNaam() != null) {
                    existingAanbod.setNaam(aanbod.getNaam());
                }

                return existingAanbod;
            })
            .map(aanbodRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, aanbod.getId().toString())
        );
    }

    /**
     * {@code GET  /aanbods} : get all the aanbods.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aanbods in body.
     */
    @GetMapping("")
    public List<Aanbod> getAllAanbods(@RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload) {
        log.debug("REST request to get all Aanbods");
        if (eagerload) {
            return aanbodRepository.findAllWithEagerRelationships();
        } else {
            return aanbodRepository.findAll();
        }
    }

    /**
     * {@code GET  /aanbods/:id} : get the "id" aanbod.
     *
     * @param id the id of the aanbod to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aanbod, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Aanbod> getAanbod(@PathVariable("id") Long id) {
        log.debug("REST request to get Aanbod : {}", id);
        Optional<Aanbod> aanbod = aanbodRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(aanbod);
    }

    /**
     * {@code DELETE  /aanbods/:id} : delete the "id" aanbod.
     *
     * @param id the id of the aanbod to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAanbod(@PathVariable("id") Long id) {
        log.debug("REST request to delete Aanbod : {}", id);
        aanbodRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
