package bcn.hackupc.filler.service;

import bcn.hackupc.filler.service.dto.PreferenceCategoryDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link bcn.hackupc.filler.domain.PreferenceCategory}.
 */
public interface PreferenceCategoryService {

    /**
     * Save a preferenceCategory.
     *
     * @param preferenceCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    PreferenceCategoryDTO save(PreferenceCategoryDTO preferenceCategoryDTO);

    /**
     * Get all the preferenceCategories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PreferenceCategoryDTO> findAll(Pageable pageable);

    /**
     * Get the "id" preferenceCategory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PreferenceCategoryDTO> findOne(Long id);

    /**
     * Delete the "id" preferenceCategory.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
