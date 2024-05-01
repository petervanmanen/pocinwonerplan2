package nl.commutr.demo.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import nl.commutr.demo.domain.Ontwikkelwens;
import nl.commutr.demo.repository.OntwikkelwensRepository;
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
 * REST controller for managing {@link nl.commutr.demo.domain.Ontwikkelwens}.
 */
@RestController
@RequestMapping("/api/ontwikkelwens")
@Transactional
public class OntwikkelwensResource {

    private final Logger log = LoggerFactory.getLogger(OntwikkelwensResource.class);

    private static final String ENTITY_NAME = "ontwikkelwens";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OntwikkelwensRepository ontwikkelwensRepository;

    public OntwikkelwensResource(OntwikkelwensRepository ontwikkelwensRepository) {
        this.ontwikkelwensRepository = ontwikkelwensRepository;
    }

    /**
     * {@code POST  /ontwikkelwens} : Create a new ontwikkelwens.
     *
     * @param ontwikkelwens the ontwikkelwens to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ontwikkelwens, or with status {@code 400 (Bad Request)} if the ontwikkelwens has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Ontwikkelwens> createOntwikkelwens(@RequestBody Ontwikkelwens ontwikkelwens) throws URISyntaxException {
        log.debug("REST request to save Ontwikkelwens : {}", ontwikkelwens);
        if (ontwikkelwens.getId() != null) {
            throw new BadRequestAlertException("A new ontwikkelwens cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ontwikkelwens = ontwikkelwensRepository.save(ontwikkelwens);
        return ResponseEntity.created(new URI("/api/ontwikkelwens/" + ontwikkelwens.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, ontwikkelwens.getId().toString()))
            .body(ontwikkelwens);
    }

    /**
     * {@code PUT  /ontwikkelwens/:id} : Updates an existing ontwikkelwens.
     *
     * @param id the id of the ontwikkelwens to save.
     * @param ontwikkelwens the ontwikkelwens to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ontwikkelwens,
     * or with status {@code 400 (Bad Request)} if the ontwikkelwens is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ontwikkelwens couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Ontwikkelwens> updateOntwikkelwens(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Ontwikkelwens ontwikkelwens
    ) throws URISyntaxException {
        log.debug("REST request to update Ontwikkelwens : {}, {}", id, ontwikkelwens);
        if (ontwikkelwens.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ontwikkelwens.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ontwikkelwensRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ontwikkelwens = ontwikkelwensRepository.save(ontwikkelwens);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, ontwikkelwens.getId().toString()))
            .body(ontwikkelwens);
    }

    /**
     * {@code PATCH  /ontwikkelwens/:id} : Partial updates given fields of an existing ontwikkelwens, field will ignore if it is null
     *
     * @param id the id of the ontwikkelwens to save.
     * @param ontwikkelwens the ontwikkelwens to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ontwikkelwens,
     * or with status {@code 400 (Bad Request)} if the ontwikkelwens is not valid,
     * or with status {@code 404 (Not Found)} if the ontwikkelwens is not found,
     * or with status {@code 500 (Internal Server Error)} if the ontwikkelwens couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Ontwikkelwens> partialUpdateOntwikkelwens(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Ontwikkelwens ontwikkelwens
    ) throws URISyntaxException {
        log.debug("REST request to partial update Ontwikkelwens partially : {}, {}", id, ontwikkelwens);
        if (ontwikkelwens.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ontwikkelwens.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ontwikkelwensRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Ontwikkelwens> result = ontwikkelwensRepository
            .findById(ontwikkelwens.getId())
            .map(existingOntwikkelwens -> {
                if (ontwikkelwens.getCode() != null) {
                    existingOntwikkelwens.setCode(ontwikkelwens.getCode());
                }
                if (ontwikkelwens.getNaam() != null) {
                    existingOntwikkelwens.setNaam(ontwikkelwens.getNaam());
                }
                if (ontwikkelwens.getOmschrijving() != null) {
                    existingOntwikkelwens.setOmschrijving(ontwikkelwens.getOmschrijving());
                }
                if (ontwikkelwens.getActief() != null) {
                    existingOntwikkelwens.setActief(ontwikkelwens.getActief());
                }

                return existingOntwikkelwens;
            })
            .map(ontwikkelwensRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, ontwikkelwens.getId().toString())
        );
    }

    /**
     * {@code GET  /ontwikkelwens} : get all the ontwikkelwens.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ontwikkelwens in body.
     */
    @GetMapping("")
    public List<Ontwikkelwens> getAllOntwikkelwens() {
        log.debug("REST request to get all Ontwikkelwens");
        return ontwikkelwensRepository.findAll();
    }

    /**
     * {@code GET  /ontwikkelwens/:id} : get the "id" ontwikkelwens.
     *
     * @param id the id of the ontwikkelwens to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ontwikkelwens, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Ontwikkelwens> getOntwikkelwens(@PathVariable("id") Long id) {
        log.debug("REST request to get Ontwikkelwens : {}", id);
        Optional<Ontwikkelwens> ontwikkelwens = ontwikkelwensRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(ontwikkelwens);
    }

    /**
     * {@code DELETE  /ontwikkelwens/:id} : delete the "id" ontwikkelwens.
     *
     * @param id the id of the ontwikkelwens to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOntwikkelwens(@PathVariable("id") Long id) {
        log.debug("REST request to delete Ontwikkelwens : {}", id);
        ontwikkelwensRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
