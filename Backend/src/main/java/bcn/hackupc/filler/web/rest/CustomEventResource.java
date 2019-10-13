package bcn.hackupc.filler.web.rest;

import bcn.hackupc.filler.service.CustomEventService;
import bcn.hackupc.filler.service.dto.CustomEventDTO;
import bcn.hackupc.filler.web.rest.errors.BadRequestAlertException;
import bcn.hackupc.filler.web.rest.request.CustomEventRequest;
import bcn.hackupc.filler.web.rest.request.Schedule;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link bcn.hackupc.filler.domain.CustomEvent}.
 */
@RestController
@RequestMapping("/api")
public class CustomEventResource {

    private final Logger log = LoggerFactory.getLogger(CustomEventResource.class);

    private static final String ENTITY_NAME = "customEvent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CustomEventService customEventService;

    public CustomEventResource(CustomEventService customEventService) {
        this.customEventService = customEventService;
    }

    /**
     * {@code POST  /custom-events} : Create a new customEvent.
     *
     * @param customEventDTO the customEventDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new customEventDTO, or with status {@code 400 (Bad Request)} if the customEvent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/custom-events")
    public ResponseEntity<CustomEventDTO> createCustomEvent(@RequestBody CustomEventDTO customEventDTO) throws URISyntaxException {
        log.debug("REST request to save CustomEvent : {}", customEventDTO);
        if (customEventDTO.getId() != null) {
            throw new BadRequestAlertException("A new customEvent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CustomEventDTO result = customEventService.save(customEventDTO);
        return ResponseEntity.created(new URI("/api/custom-events/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /custom-events} : Updates an existing customEvent.
     *
     * @param customEventDTO the customEventDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customEventDTO,
     * or with status {@code 400 (Bad Request)} if the customEventDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the customEventDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/custom-events")
    public ResponseEntity<CustomEventDTO> updateCustomEvent(@RequestBody CustomEventDTO customEventDTO) throws URISyntaxException {
        log.debug("REST request to update CustomEvent : {}", customEventDTO);
        if (customEventDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CustomEventDTO result = customEventService.save(customEventDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, customEventDTO.getId().toString()))
            .body(result);
    }

//    /**
//     * {@code GET  /custom-events} : get all the customEvents.
//     *
//
//     * @param pageable the pagination information.
//
//     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of customEvents in body.
//     */
//    @GetMapping("/custom-events")
//    public ResponseEntity<List<CustomEventDTO>> getAllCustomEvents(Pageable pageable) {
//        log.debug("REST request to get a page of CustomEvents");
//        Page<CustomEventDTO> page = customEventService.findAll(pageable);
//        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
//        return ResponseEntity.ok().headers(headers).body(page.getContent());
//    }

    @PostMapping("/create-custom-events")
    public ResponseEntity<List<CustomEventDTO>> getAllCustomEvents(@RequestBody CustomEventRequest customEventRequest) {

        ZonedDateTime startDate = ZonedDateTime.of(customEventRequest.getStartDate(), ZoneId.systemDefault());
        ZonedDateTime endDate = ZonedDateTime.of(customEventRequest.getEndDate(), ZoneId.systemDefault());

        List<CustomEventDTO> customEventDTOS = customEventService.findAllBetwwen(startDate, endDate);

        for (int i = 0; i < customEventDTOS.size(); i++) {
            CustomEventDTO ce = customEventDTOS.get(i);
            for (int j = 0; j < customEventRequest.getSchedule().size(); j++) {
                Schedule schedule = customEventRequest.getSchedule().get(j);
                if (ce.getStartDate().isBefore(ZonedDateTime.of(schedule.getEnd(), ZoneId.systemDefault())) &&
                    ce.getStartDate().isAfter(ZonedDateTime.of(schedule.getStart(), ZoneId.systemDefault()))) {
                    customEventDTOS.remove(i);
                    i--;
                    continue;
                }

                if (ce.getEndDate().isBefore(ZonedDateTime.of(schedule.getEnd(), ZoneId.systemDefault())) &&
                    ce.getEndDate().isAfter(ZonedDateTime.of(schedule.getStart(), ZoneId.systemDefault()))) {
                    customEventDTOS.remove(i);
                    i--;
                }
            }
        }

        return ResponseEntity.ok(customEventDTOS);
    }

    /**
     * {@code GET  /custom-events/:id} : get the "id" customEvent.
     *
     * @param id the id of the customEventDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the customEventDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/custom-events/{id}")
    public ResponseEntity<CustomEventDTO> getCustomEvent(@PathVariable Long id) {
        log.debug("REST request to get CustomEvent : {}", id);
        Optional<CustomEventDTO> customEventDTO = customEventService.findOne(id);
        return ResponseUtil.wrapOrNotFound(customEventDTO);
    }

    /**
     * {@code DELETE  /custom-events/:id} : delete the "id" customEvent.
     *
     * @param id the id of the customEventDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/custom-events/{id}")
    public ResponseEntity<Void> deleteCustomEvent(@PathVariable Long id) {
        log.debug("REST request to delete CustomEvent : {}", id);
        customEventService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
