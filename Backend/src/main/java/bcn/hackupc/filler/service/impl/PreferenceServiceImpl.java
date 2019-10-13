package bcn.hackupc.filler.service.impl;

import bcn.hackupc.filler.domain.Preference;
import bcn.hackupc.filler.domain.PreferenceCategory;
import bcn.hackupc.filler.repository.PreferenceCategoryRepository;
import bcn.hackupc.filler.repository.PreferenceRepository;
import bcn.hackupc.filler.service.PreferenceService;
import bcn.hackupc.filler.service.dto.PreferenceDTO;
import bcn.hackupc.filler.service.mapper.PreferenceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Preference}.
 */
@Service
@Transactional
public class PreferenceServiceImpl implements PreferenceService {

    private final Logger log = LoggerFactory.getLogger(PreferenceServiceImpl.class);

    private final PreferenceRepository preferenceRepository;

    private final PreferenceCategoryRepository preferenceCategoryRepository;

    private final PreferenceMapper preferenceMapper;

    public PreferenceServiceImpl(PreferenceRepository preferenceRepository, PreferenceCategoryRepository preferenceCategoryRepository, PreferenceMapper preferenceMapper) {
        this.preferenceRepository = preferenceRepository;
        this.preferenceCategoryRepository = preferenceCategoryRepository;
        this.preferenceMapper = preferenceMapper;
    }

    /**
     * Save a preference.
     *
     * @param preferenceDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public PreferenceDTO save(PreferenceDTO preferenceDTO) {
        log.debug("Request to save Preference : {}", preferenceDTO);
        Preference preference = preferenceMapper.toEntity(preferenceDTO);
        preference = preferenceRepository.save(preference);
        return preferenceMapper.toDto(preference);
    }

    /**
     * Get all the preferences.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PreferenceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Preferences");
        return preferenceRepository.findAll(pageable)
            .map(preferenceMapper::toDto);
    }

    /**
     * Get all the preferences with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<PreferenceDTO> findAllWithEagerRelationships(Pageable pageable) {
        return preferenceRepository.findAllWithEagerRelationships(pageable).map(preferenceMapper::toDto);
    }


    /**
     * Get one preference by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PreferenceDTO> findOne(Long id) {
        log.debug("Request to get Preference : {}", id);
        return preferenceRepository.findOneWithEagerRelationships(id)
            .map(preferenceMapper::toDto);
    }

    /**
     * Delete the preference by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Preference : {}", id);
        preferenceRepository.deleteById(id);
    }

//    @PostConstruct
//    public void fillDatabase() {
//        PreferenceCategory preferenceCategoryMusic = new PreferenceCategory();
//        preferenceCategoryMusic.setName("Music");
//        preferenceCategoryMusic = preferenceCategoryRepository.save(preferenceCategoryMusic);
//        PreferenceCategory preferenceCategoryArt = new PreferenceCategory();
//        preferenceCategoryArt.setName("Art");
//        preferenceCategoryArt = preferenceCategoryRepository.save(preferenceCategoryArt);
//        PreferenceCategory preferenceCategoryConference = new PreferenceCategory();
//        preferenceCategoryConference.setName("Conference");
//        preferenceCategoryConference = preferenceCategoryRepository.save(preferenceCategoryConference);
//
//        Preference preferenceRock = new Preference();
//        preferenceRock.setName("rock");
//        preferenceRock.setPreferenceCategory(preferenceCategoryMusic);
//        Preference preferencemetal = new Preference();
//        preferencemetal.setName("metal");
//        preferencemetal.setPreferenceCategory(preferenceCategoryMusic);
//        Preference preferencejazz = new Preference();
//        preferencejazz.setName("jazz");
//        preferencejazz.setPreferenceCategory(preferenceCategoryMusic);
//        Preference preferencerenaissance = new Preference();
//        preferencerenaissance.setName("renaissance");
//        preferencerenaissance.setPreferenceCategory(preferenceCategoryArt);
//        Preference preferenceai = new Preference();
//        preferenceai.setName("ai");
//        preferenceai.setPreferenceCategory(preferenceCategoryConference);
//        Preference preferencedatabase = new Preference();
//        preferencedatabase.setName("database");
//        preferencedatabase.setPreferenceCategory(preferenceCategoryConference);
//        Preference preferenceclimate = new Preference();
//        preferenceclimate.setName("climate");
//        preferenceclimate.setPreferenceCategory(preferenceCategoryConference);
//
//        preferenceRepository.saveAndFlush(preferenceRock);
//        preferenceRepository.saveAndFlush(preferencemetal);
//        preferenceRepository.saveAndFlush(preferencejazz);
//        preferenceRepository.saveAndFlush(preferencerenaissance);
//        preferenceRepository.saveAndFlush(preferenceai);
//        preferenceRepository.saveAndFlush(preferencedatabase);
//        preferenceRepository.saveAndFlush(preferenceclimate);
//    }
}
