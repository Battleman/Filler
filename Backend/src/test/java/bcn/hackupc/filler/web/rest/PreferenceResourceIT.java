package bcn.hackupc.filler.web.rest;

import bcn.hackupc.filler.BackendApp;
import bcn.hackupc.filler.domain.Preference;
import bcn.hackupc.filler.repository.PreferenceRepository;
import bcn.hackupc.filler.service.PreferenceService;
import bcn.hackupc.filler.service.dto.PreferenceDTO;
import bcn.hackupc.filler.service.mapper.PreferenceMapper;
import bcn.hackupc.filler.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static bcn.hackupc.filler.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link PreferenceResource} REST controller.
 */
@SpringBootTest(classes = BackendApp.class)
public class PreferenceResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private PreferenceRepository preferenceRepository;

    @Mock
    private PreferenceRepository preferenceRepositoryMock;

    @Autowired
    private PreferenceMapper preferenceMapper;

    @Mock
    private PreferenceService preferenceServiceMock;

    @Autowired
    private PreferenceService preferenceService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restPreferenceMockMvc;

    private Preference preference;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PreferenceResource preferenceResource = new PreferenceResource(preferenceService);
        this.restPreferenceMockMvc = MockMvcBuilders.standaloneSetup(preferenceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Preference createEntity(EntityManager em) {
        Preference preference = new Preference()
            .name(DEFAULT_NAME);
        return preference;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Preference createUpdatedEntity(EntityManager em) {
        Preference preference = new Preference()
            .name(UPDATED_NAME);
        return preference;
    }

    @BeforeEach
    public void initTest() {
        preference = createEntity(em);
    }

    @Test
    @Transactional
    public void createPreference() throws Exception {
        int databaseSizeBeforeCreate = preferenceRepository.findAll().size();

        // Create the Preference
        PreferenceDTO preferenceDTO = preferenceMapper.toDto(preference);
        restPreferenceMockMvc.perform(post("/api/preferences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(preferenceDTO)))
            .andExpect(status().isCreated());

        // Validate the Preference in the database
        List<Preference> preferenceList = preferenceRepository.findAll();
        assertThat(preferenceList).hasSize(databaseSizeBeforeCreate + 1);
        Preference testPreference = preferenceList.get(preferenceList.size() - 1);
        assertThat(testPreference.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createPreferenceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = preferenceRepository.findAll().size();

        // Create the Preference with an existing ID
        preference.setId(1L);
        PreferenceDTO preferenceDTO = preferenceMapper.toDto(preference);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPreferenceMockMvc.perform(post("/api/preferences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(preferenceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Preference in the database
        List<Preference> preferenceList = preferenceRepository.findAll();
        assertThat(preferenceList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllPreferences() throws Exception {
        // Initialize the database
        preferenceRepository.saveAndFlush(preference);

        // Get all the preferenceList
        restPreferenceMockMvc.perform(get("/api/preferences?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(preference.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllPreferencesWithEagerRelationshipsIsEnabled() throws Exception {
        PreferenceResource preferenceResource = new PreferenceResource(preferenceServiceMock);
        when(preferenceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restPreferenceMockMvc = MockMvcBuilders.standaloneSetup(preferenceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restPreferenceMockMvc.perform(get("/api/preferences?eagerload=true"))
        .andExpect(status().isOk());

        verify(preferenceServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllPreferencesWithEagerRelationshipsIsNotEnabled() throws Exception {
        PreferenceResource preferenceResource = new PreferenceResource(preferenceServiceMock);
            when(preferenceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restPreferenceMockMvc = MockMvcBuilders.standaloneSetup(preferenceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restPreferenceMockMvc.perform(get("/api/preferences?eagerload=true"))
        .andExpect(status().isOk());

            verify(preferenceServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getPreference() throws Exception {
        // Initialize the database
        preferenceRepository.saveAndFlush(preference);

        // Get the preference
        restPreferenceMockMvc.perform(get("/api/preferences/{id}", preference.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(preference.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    public void getNonExistingPreference() throws Exception {
        // Get the preference
        restPreferenceMockMvc.perform(get("/api/preferences/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePreference() throws Exception {
        // Initialize the database
        preferenceRepository.saveAndFlush(preference);

        int databaseSizeBeforeUpdate = preferenceRepository.findAll().size();

        // Update the preference
        Preference updatedPreference = preferenceRepository.findById(preference.getId()).get();
        // Disconnect from session so that the updates on updatedPreference are not directly saved in db
        em.detach(updatedPreference);
        updatedPreference
            .name(UPDATED_NAME);
        PreferenceDTO preferenceDTO = preferenceMapper.toDto(updatedPreference);

        restPreferenceMockMvc.perform(put("/api/preferences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(preferenceDTO)))
            .andExpect(status().isOk());

        // Validate the Preference in the database
        List<Preference> preferenceList = preferenceRepository.findAll();
        assertThat(preferenceList).hasSize(databaseSizeBeforeUpdate);
        Preference testPreference = preferenceList.get(preferenceList.size() - 1);
        assertThat(testPreference.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingPreference() throws Exception {
        int databaseSizeBeforeUpdate = preferenceRepository.findAll().size();

        // Create the Preference
        PreferenceDTO preferenceDTO = preferenceMapper.toDto(preference);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPreferenceMockMvc.perform(put("/api/preferences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(preferenceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Preference in the database
        List<Preference> preferenceList = preferenceRepository.findAll();
        assertThat(preferenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePreference() throws Exception {
        // Initialize the database
        preferenceRepository.saveAndFlush(preference);

        int databaseSizeBeforeDelete = preferenceRepository.findAll().size();

        // Delete the preference
        restPreferenceMockMvc.perform(delete("/api/preferences/{id}", preference.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Preference> preferenceList = preferenceRepository.findAll();
        assertThat(preferenceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Preference.class);
        Preference preference1 = new Preference();
        preference1.setId(1L);
        Preference preference2 = new Preference();
        preference2.setId(preference1.getId());
        assertThat(preference1).isEqualTo(preference2);
        preference2.setId(2L);
        assertThat(preference1).isNotEqualTo(preference2);
        preference1.setId(null);
        assertThat(preference1).isNotEqualTo(preference2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PreferenceDTO.class);
        PreferenceDTO preferenceDTO1 = new PreferenceDTO();
        preferenceDTO1.setId(1L);
        PreferenceDTO preferenceDTO2 = new PreferenceDTO();
        assertThat(preferenceDTO1).isNotEqualTo(preferenceDTO2);
        preferenceDTO2.setId(preferenceDTO1.getId());
        assertThat(preferenceDTO1).isEqualTo(preferenceDTO2);
        preferenceDTO2.setId(2L);
        assertThat(preferenceDTO1).isNotEqualTo(preferenceDTO2);
        preferenceDTO1.setId(null);
        assertThat(preferenceDTO1).isNotEqualTo(preferenceDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(preferenceMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(preferenceMapper.fromId(null)).isNull();
    }
}
