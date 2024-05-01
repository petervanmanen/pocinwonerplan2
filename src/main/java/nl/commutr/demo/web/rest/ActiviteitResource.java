package nl.commutr.demo.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import nl.commutr.demo.domain.Activiteit;
import nl.commutr.demo.repository.ActiviteitRepository;
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
 * REST controller for managing {@link nl.commutr.demo.domain.Activiteit}.
 */
@RestController
@RequestMapping("/api/activiteits")
@Transactional
public class ActiviteitResource {

    private final Logger log = LoggerFactory.getLogger(ActiviteitResource.class);

    private static final String ENTITY_NAME = "activiteit";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ActiviteitRepository activiteitRepository;

    public ActiviteitResource(ActiviteitRepository activiteitRepository) {
        this.activiteitRepository = activiteitRepository;
    }

    /**
     * {@code POST  /activiteits} : Create a new activiteit.
     *
     * @param activiteit the activiteit to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new activiteit, or with status {@code 400 (Bad Request)} if the activiteit has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Activiteit> createActiviteit(@RequestBody Activiteit activiteit) throws URISyntaxException {
        log.debug("REST request to save Activiteit : {}", activiteit);
        if (activiteit.getId() != null) {
            throw new BadRequestAlertException("A new activiteit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        activiteit = activiteitRepository.save(activiteit);
        return ResponseEntity.created(new URI("/api/activiteits/" + activiteit.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, activiteit.getId().toString()))
            .body(activiteit);
    }

    /**
     * {@code PUT  /activiteits/:id} : Updates an existing activiteit.
     *
     * @param id the id of the activiteit to save.
     * @param activiteit the activiteit to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated activiteit,
     * or with status {@code 400 (Bad Request)} if the activiteit is not valid,
     * or with status {@code 500 (Internal Server Error)} if the activiteit couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Activiteit> updateActiviteit(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Activiteit activiteit
    ) throws URISyntaxException {
        log.debug("REST request to update Activiteit : {}, {}", id, activiteit);
        if (activiteit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, activiteit.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!activiteitRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        activiteit = activiteitRepository.save(activiteit);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, activiteit.getId().toString()))
            .body(activiteit);
    }

    /**
     * {@code PATCH  /activiteits/:id} : Partial updates given fields of an existing activiteit, field will ignore if it is null
     *
     * @param id the id of the activiteit to save.
     * @param activiteit the activiteit to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated activiteit,
     * or with status {@code 400 (Bad Request)} if the activiteit is not valid,
     * or with status {@code 404 (Not Found)} if the activiteit is not found,
     * or with status {@code 500 (Internal Server Error)} if the activiteit couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Activiteit> partialUpdateActiviteit(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Activiteit activiteit
    ) throws URISyntaxException {
        log.debug("REST request to partial update Activiteit partially : {}, {}", id, activiteit);
        if (activiteit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, activiteit.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!activiteitRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Activiteit> result = activiteitRepository
            .findById(activiteit.getId())
            .map(existingActiviteit -> {
                if (activiteit.getCode() != null) {
                    existingActiviteit.setCode(activiteit.getCode());
                }
                if (activiteit.getNaam() != null) {
                    existingActiviteit.setNaam(activiteit.getNaam());
                }
                if (activiteit.getActiehouder() != null) {
                    existingActiviteit.setActiehouder(activiteit.getActiehouder());
                }
                if (activiteit.getAfhandeltermijn() != null) {
                    existingActiviteit.setAfhandeltermijn(activiteit.getAfhandeltermijn());
                }
                if (activiteit.getActief() != null) {
                    existingActiviteit.setActief(activiteit.getActief());
                }

                return existingActiviteit;
            })
            .map(activiteitRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, activiteit.getId().toString())
        );
    }

    /**
     * {@code GET  /activiteits} : get all the activiteits.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of activiteits in body.
     */
    @GetMapping("")
    public List<Activiteit> getAllActiviteits() {
        log.debug("REST request to get all Activiteits");
        return activiteitRepository.findAll();
    }

    /**
     * {@code GET  /activiteits/:id} : get the "id" activiteit.
     *
     * @param id the id of the activiteit to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the activiteit, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Activiteit> getActiviteit(@PathVariable("id") Long id) {
        log.debug("REST request to get Activiteit : {}", id);
        Optional<Activiteit> activiteit = activiteitRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(activiteit);
    }

    /**
     * {@code DELETE  /activiteits/:id} : delete the "id" activiteit.
     *
     * @param id the id of the activiteit to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActiviteit(@PathVariable("id") Long id) {
        log.debug("REST request to delete Activiteit : {}", id);
        activiteitRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
