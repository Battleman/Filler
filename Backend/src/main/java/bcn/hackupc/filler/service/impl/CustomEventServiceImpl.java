package bcn.hackupc.filler.service.impl;

import bcn.hackupc.filler.service.CustomEventService;
import bcn.hackupc.filler.domain.CustomEvent;
import bcn.hackupc.filler.repository.CustomEventRepository;
import bcn.hackupc.filler.service.dto.CustomEventDTO;
import bcn.hackupc.filler.service.mapper.CustomEventMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link CustomEvent}.
 */
@Service
@Transactional
public class CustomEventServiceImpl implements CustomEventService {

    private final Logger log = LoggerFactory.getLogger(CustomEventServiceImpl.class);

    private final CustomEventRepository customEventRepository;

    private final CustomEventMapper customEventMapper;

    public CustomEventServiceImpl(CustomEventRepository customEventRepository, CustomEventMapper customEventMapper) {
        this.customEventRepository = customEventRepository;
        this.customEventMapper = customEventMapper;
    }

    /**
     * Save a customEvent.
     *
     * @param customEventDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public CustomEventDTO save(CustomEventDTO customEventDTO) {
        log.debug("Request to save CustomEvent : {}", customEventDTO);
        CustomEvent customEvent = customEventMapper.toEntity(customEventDTO);
        customEvent = customEventRepository.save(customEvent);
        return customEventMapper.toDto(customEvent);
    }

    /**
     * Get all the customEvents.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CustomEventDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CustomEvents");
        return customEventRepository.findAll(pageable)
            .map(customEventMapper::toDto);
    }


    /**
     * Get one customEvent by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CustomEventDTO> findOne(Long id) {
        log.debug("Request to get CustomEvent : {}", id);
        return customEventRepository.findById(id)
            .map(customEventMapper::toDto);
    }

    /**
     * Delete the customEvent by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CustomEvent : {}", id);
        customEventRepository.deleteById(id);
    }

    @Override
    public List<CustomEventDTO> findAllBetwwen(ZonedDateTime startDate, ZonedDateTime endDate) {
        return customEventRepository.findAllByStartDateLessThanAndEndDateGreaterThan(startDate, endDate).stream()
            .map(customEventMapper::toDto).collect(Collectors.toList());
    }
}
