package bcn.hackupc.filler.service.impl;

import bcn.hackupc.filler.service.PreferenceCategoryService;
import bcn.hackupc.filler.domain.PreferenceCategory;
import bcn.hackupc.filler.repository.PreferenceCategoryRepository;
import bcn.hackupc.filler.service.dto.PreferenceCategoryDTO;
import bcn.hackupc.filler.service.mapper.PreferenceCategoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link PreferenceCategory}.
 */
@Service
@Transactional
public class PreferenceCategoryServiceImpl implements PreferenceCategoryService {

    private final Logger log = LoggerFactory.getLogger(PreferenceCategoryServiceImpl.class);

    private final PreferenceCategoryRepository preferenceCategoryRepository;

    private final PreferenceCategoryMapper preferenceCategoryMapper;

    public PreferenceCategoryServiceImpl(PreferenceCategoryRepository preferenceCategoryRepository, PreferenceCategoryMapper preferenceCategoryMapper) {
        this.preferenceCategoryRepository = preferenceCategoryRepository;
        this.preferenceCategoryMapper = preferenceCategoryMapper;
    }

    /**
     * Save a preferenceCategory.
     *
     * @param preferenceCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public PreferenceCategoryDTO save(PreferenceCategoryDTO preferenceCategoryDTO) {
        log.debug("Request to save PreferenceCategory : {}", preferenceCategoryDTO);
        PreferenceCategory preferenceCategory = preferenceCategoryMapper.toEntity(preferenceCategoryDTO);
        preferenceCategory = preferenceCategoryRepository.save(preferenceCategory);
        return preferenceCategoryMapper.toDto(preferenceCategory);
    }

    /**
     * Get all the preferenceCategories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PreferenceCategoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PreferenceCategories");
        return preferenceCategoryRepository.findAll(pageable)
            .map(preferenceCategoryMapper::toDto);
    }


    /**
     * Get one preferenceCategory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PreferenceCategoryDTO> findOne(Long id) {
        log.debug("Request to get PreferenceCategory : {}", id);
        return preferenceCategoryRepository.findById(id)
            .map(preferenceCategoryMapper::toDto);
    }

    /**
     * Delete the preferenceCategory by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete PreferenceCategory : {}", id);
        preferenceCategoryRepository.deleteById(id);
    }
}
