package bcn.hackupc.filler.service;

import bcn.hackupc.filler.service.dto.CustomEventDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link bcn.hackupc.filler.domain.CustomEvent}.
 */
public interface CustomEventService {

    /**
     * Save a customEvent.
     *
     * @param customEventDTO the entity to save.
     * @return the persisted entity.
     */
    CustomEventDTO save(CustomEventDTO customEventDTO);

    /**
     * Get all the customEvents.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CustomEventDTO> findAll(Pageable pageable);


    /**
     * Get the "id" customEvent.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CustomEventDTO> findOne(Long id);

    /**
     * Delete the "id" customEvent.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<CustomEventDTO> findAllBetwwen(ZonedDateTime startDate, ZonedDateTime endDate);
}
