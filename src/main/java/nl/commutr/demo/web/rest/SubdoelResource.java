package nl.commutr.demo.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import nl.commutr.demo.domain.Subdoel;
import nl.commutr.demo.repository.SubdoelRepository;
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
 * REST controller for managing {@link nl.commutr.demo.domain.Subdoel}.
 */
@RestController
@RequestMapping("/api/subdoels")
@Transactional
public class SubdoelResource {

    private final Logger log = LoggerFactory.getLogger(SubdoelResource.class);

    private static final String ENTITY_NAME = "subdoel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubdoelRepository subdoelRepository;

    public SubdoelResource(SubdoelRepository subdoelRepository) {
        this.subdoelRepository = subdoelRepository;
    }

    /**
     * {@code POST  /subdoels} : Create a new subdoel.
     *
     * @param subdoel the subdoel to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new subdoel, or with status {@code 400 (Bad Request)} if the subdoel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Subdoel> createSubdoel(@RequestBody Subdoel subdoel) throws URISyntaxException {
        log.debug("REST request to save Subdoel : {}", subdoel);
        if (subdoel.getId() != null) {
            throw new BadRequestAlertException("A new subdoel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        subdoel = subdoelRepository.save(subdoel);
        return ResponseEntity.created(new URI("/api/subdoels/" + subdoel.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, subdoel.getId().toString()))
            .body(subdoel);
    }

    /**
     * {@code PUT  /subdoels/:id} : Updates an existing subdoel.
     *
     * @param id the id of the subdoel to save.
     * @param subdoel the subdoel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subdoel,
     * or with status {@code 400 (Bad Request)} if the subdoel is not valid,
     * or with status {@code 500 (Internal Server Error)} if the subdoel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Subdoel> updateSubdoel(@PathVariable(value = "id", required = false) final Long id, @RequestBody Subdoel subdoel)
        throws URISyntaxException {
        log.debug("REST request to update Subdoel : {}, {}", id, subdoel);
        if (subdoel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subdoel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subdoelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        subdoel = subdoelRepository.save(subdoel);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, subdoel.getId().toString()))
            .body(subdoel);
    }

    /**
     * {@code PATCH  /subdoels/:id} : Partial updates given fields of an existing subdoel, field will ignore if it is null
     *
     * @param id the id of the subdoel to save.
     * @param subdoel the subdoel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subdoel,
     * or with status {@code 400 (Bad Request)} if the subdoel is not valid,
     * or with status {@code 404 (Not Found)} if the subdoel is not found,
     * or with status {@code 500 (Internal Server Error)} if the subdoel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Subdoel> partialUpdateSubdoel(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Subdoel subdoel
    ) throws URISyntaxException {
        log.debug("REST request to partial update Subdoel partially : {}, {}", id, subdoel);
        if (subdoel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subdoel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subdoelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Subdoel> result = subdoelRepository
            .findById(subdoel.getId())
            .map(existingSubdoel -> {
                if (subdoel.getCode() != null) {
                    existingSubdoel.setCode(subdoel.getCode());
                }
                if (subdoel.getNaam() != null) {
                    existingSubdoel.setNaam(subdoel.getNaam());
                }
                if (subdoel.getActief() != null) {
                    existingSubdoel.setActief(subdoel.getActief());
                }

                return existingSubdoel;
            })
            .map(subdoelRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, subdoel.getId().toString())
        );
    }

    /**
     * {@code GET  /subdoels} : get all the subdoels.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subdoels in body.
     */
    @GetMapping("")
    public List<Subdoel> getAllSubdoels() {
        log.debug("REST request to get all Subdoels");
        return subdoelRepository.findAll();
    }

    /**
     * {@code GET  /subdoels/:id} : get the "id" subdoel.
     *
     * @param id the id of the subdoel to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the subdoel, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Subdoel> getSubdoel(@PathVariable("id") Long id) {
        log.debug("REST request to get Subdoel : {}", id);
        Optional<Subdoel> subdoel = subdoelRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(subdoel);
    }

    /**
     * {@code DELETE  /subdoels/:id} : delete the "id" subdoel.
     *
     * @param id the id of the subdoel to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubdoel(@PathVariable("id") Long id) {
        log.debug("REST request to delete Subdoel : {}", id);
        subdoelRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
