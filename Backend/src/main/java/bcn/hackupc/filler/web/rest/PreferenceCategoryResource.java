package bcn.hackupc.filler.web.rest;

import bcn.hackupc.filler.service.PreferenceCategoryService;
import bcn.hackupc.filler.web.rest.errors.BadRequestAlertException;
import bcn.hackupc.filler.service.dto.PreferenceCategoryDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

/**
 * REST controller for managing {@link bcn.hackupc.filler.domain.PreferenceCategory}.
 */
@RestController
@RequestMapping("/api")
public class PreferenceCategoryResource {

    private final Logger log = LoggerFactory.getLogger(PreferenceCategoryResource.class);

    private static final String ENTITY_NAME = "preferenceCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PreferenceCategoryService preferenceCategoryService;

    public PreferenceCategoryResource(PreferenceCategoryService preferenceCategoryService) {
        this.preferenceCategoryService = preferenceCategoryService;
    }

    /**
     * {@code POST  /preference-categories} : Create a new preferenceCategory.
     *
     * @param preferenceCategoryDTO the preferenceCategoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new preferenceCategoryDTO, or with status {@code 400 (Bad Request)} if the preferenceCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/preference-categories")
    public ResponseEntity<PreferenceCategoryDTO> createPreferenceCategory(@RequestBody PreferenceCategoryDTO preferenceCategoryDTO) throws URISyntaxException {
        log.debug("REST request to save PreferenceCategory : {}", preferenceCategoryDTO);
        if (preferenceCategoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new preferenceCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PreferenceCategoryDTO result = preferenceCategoryService.save(preferenceCategoryDTO);
        return ResponseEntity.created(new URI("/api/preference-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /preference-categories} : Updates an existing preferenceCategory.
     *
     * @param preferenceCategoryDTO the preferenceCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated preferenceCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the preferenceCategoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the preferenceCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/preference-categories")
    public ResponseEntity<PreferenceCategoryDTO> updatePreferenceCategory(@RequestBody PreferenceCategoryDTO preferenceCategoryDTO) throws URISyntaxException {
        log.debug("REST request to update PreferenceCategory : {}", preferenceCategoryDTO);
        if (preferenceCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PreferenceCategoryDTO result = preferenceCategoryService.save(preferenceCategoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, preferenceCategoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /preference-categories} : get all the preferenceCategories.
     *

     * @param pageable the pagination information.

     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of preferenceCategories in body.
     */
    @GetMapping("/preference-categories")
    public ResponseEntity<List<PreferenceCategoryDTO>> getAllPreferenceCategories(Pageable pageable, @RequestParam(required = false) String filter) {
        log.debug("REST request to get a page of PreferenceCategories");
        Page<PreferenceCategoryDTO> page = preferenceCategoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /preference-categories/:id} : get the "id" preferenceCategory.
     *
     * @param id the id of the preferenceCategoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the preferenceCategoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/preference-categories/{id}")
    public ResponseEntity<PreferenceCategoryDTO> getPreferenceCategory(@PathVariable Long id) {
        log.debug("REST request to get PreferenceCategory : {}", id);
        Optional<PreferenceCategoryDTO> preferenceCategoryDTO = preferenceCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(preferenceCategoryDTO);
    }

    /**
     * {@code DELETE  /preference-categories/:id} : delete the "id" preferenceCategory.
     *
     * @param id the id of the preferenceCategoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/preference-categories/{id}")
    public ResponseEntity<Void> deletePreferenceCategory(@PathVariable Long id) {
        log.debug("REST request to delete PreferenceCategory : {}", id);
        preferenceCategoryService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
