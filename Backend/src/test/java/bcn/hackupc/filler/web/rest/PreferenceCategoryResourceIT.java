package bcn.hackupc.filler.web.rest;

import bcn.hackupc.filler.BackendApp;
import bcn.hackupc.filler.domain.PreferenceCategory;
import bcn.hackupc.filler.repository.PreferenceCategoryRepository;
import bcn.hackupc.filler.service.PreferenceCategoryService;
import bcn.hackupc.filler.service.dto.PreferenceCategoryDTO;
import bcn.hackupc.filler.service.mapper.PreferenceCategoryMapper;
import bcn.hackupc.filler.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static bcn.hackupc.filler.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link PreferenceCategoryResource} REST controller.
 */
@SpringBootTest(classes = BackendApp.class)
public class PreferenceCategoryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private PreferenceCategoryRepository preferenceCategoryRepository;

    @Autowired
    private PreferenceCategoryMapper preferenceCategoryMapper;

    @Autowired
    private PreferenceCategoryService preferenceCategoryService;

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

    private MockMvc restPreferenceCategoryMockMvc;

    private PreferenceCategory preferenceCategory;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PreferenceCategoryResource preferenceCategoryResource = new PreferenceCategoryResource(preferenceCategoryService);
        this.restPreferenceCategoryMockMvc = MockMvcBuilders.standaloneSetup(preferenceCategoryResource)
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
    public static PreferenceCategory createEntity(EntityManager em) {
        PreferenceCategory preferenceCategory = new PreferenceCategory()
            .name(DEFAULT_NAME);
        return preferenceCategory;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PreferenceCategory createUpdatedEntity(EntityManager em) {
        PreferenceCategory preferenceCategory = new PreferenceCategory()
            .name(UPDATED_NAME);
        return preferenceCategory;
    }

    @BeforeEach
    public void initTest() {
        preferenceCategory = createEntity(em);
    }

    @Test
    @Transactional
    public void createPreferenceCategory() throws Exception {
        int databaseSizeBeforeCreate = preferenceCategoryRepository.findAll().size();

        // Create the PreferenceCategory
        PreferenceCategoryDTO preferenceCategoryDTO = preferenceCategoryMapper.toDto(preferenceCategory);
        restPreferenceCategoryMockMvc.perform(post("/api/preference-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(preferenceCategoryDTO)))
            .andExpect(status().isCreated());

        // Validate the PreferenceCategory in the database
        List<PreferenceCategory> preferenceCategoryList = preferenceCategoryRepository.findAll();
        assertThat(preferenceCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        PreferenceCategory testPreferenceCategory = preferenceCategoryList.get(preferenceCategoryList.size() - 1);
        assertThat(testPreferenceCategory.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createPreferenceCategoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = preferenceCategoryRepository.findAll().size();

        // Create the PreferenceCategory with an existing ID
        preferenceCategory.setId(1L);
        PreferenceCategoryDTO preferenceCategoryDTO = preferenceCategoryMapper.toDto(preferenceCategory);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPreferenceCategoryMockMvc.perform(post("/api/preference-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(preferenceCategoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PreferenceCategory in the database
        List<PreferenceCategory> preferenceCategoryList = preferenceCategoryRepository.findAll();
        assertThat(preferenceCategoryList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllPreferenceCategories() throws Exception {
        // Initialize the database
        preferenceCategoryRepository.saveAndFlush(preferenceCategory);

        // Get all the preferenceCategoryList
        restPreferenceCategoryMockMvc.perform(get("/api/preference-categories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(preferenceCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    @Transactional
    public void getPreferenceCategory() throws Exception {
        // Initialize the database
        preferenceCategoryRepository.saveAndFlush(preferenceCategory);

        // Get the preferenceCategory
        restPreferenceCategoryMockMvc.perform(get("/api/preference-categories/{id}", preferenceCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(preferenceCategory.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    public void getNonExistingPreferenceCategory() throws Exception {
        // Get the preferenceCategory
        restPreferenceCategoryMockMvc.perform(get("/api/preference-categories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePreferenceCategory() throws Exception {
        // Initialize the database
        preferenceCategoryRepository.saveAndFlush(preferenceCategory);

        int databaseSizeBeforeUpdate = preferenceCategoryRepository.findAll().size();

        // Update the preferenceCategory
        PreferenceCategory updatedPreferenceCategory = preferenceCategoryRepository.findById(preferenceCategory.getId()).get();
        // Disconnect from session so that the updates on updatedPreferenceCategory are not directly saved in db
        em.detach(updatedPreferenceCategory);
        updatedPreferenceCategory
            .name(UPDATED_NAME);
        PreferenceCategoryDTO preferenceCategoryDTO = preferenceCategoryMapper.toDto(updatedPreferenceCategory);

        restPreferenceCategoryMockMvc.perform(put("/api/preference-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(preferenceCategoryDTO)))
            .andExpect(status().isOk());

        // Validate the PreferenceCategory in the database
        List<PreferenceCategory> preferenceCategoryList = preferenceCategoryRepository.findAll();
        assertThat(preferenceCategoryList).hasSize(databaseSizeBeforeUpdate);
        PreferenceCategory testPreferenceCategory = preferenceCategoryList.get(preferenceCategoryList.size() - 1);
        assertThat(testPreferenceCategory.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingPreferenceCategory() throws Exception {
        int databaseSizeBeforeUpdate = preferenceCategoryRepository.findAll().size();

        // Create the PreferenceCategory
        PreferenceCategoryDTO preferenceCategoryDTO = preferenceCategoryMapper.toDto(preferenceCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPreferenceCategoryMockMvc.perform(put("/api/preference-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(preferenceCategoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PreferenceCategory in the database
        List<PreferenceCategory> preferenceCategoryList = preferenceCategoryRepository.findAll();
        assertThat(preferenceCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePreferenceCategory() throws Exception {
        // Initialize the database
        preferenceCategoryRepository.saveAndFlush(preferenceCategory);

        int databaseSizeBeforeDelete = preferenceCategoryRepository.findAll().size();

        // Delete the preferenceCategory
        restPreferenceCategoryMockMvc.perform(delete("/api/preference-categories/{id}", preferenceCategory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PreferenceCategory> preferenceCategoryList = preferenceCategoryRepository.findAll();
        assertThat(preferenceCategoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PreferenceCategory.class);
        PreferenceCategory preferenceCategory1 = new PreferenceCategory();
        preferenceCategory1.setId(1L);
        PreferenceCategory preferenceCategory2 = new PreferenceCategory();
        preferenceCategory2.setId(preferenceCategory1.getId());
        assertThat(preferenceCategory1).isEqualTo(preferenceCategory2);
        preferenceCategory2.setId(2L);
        assertThat(preferenceCategory1).isNotEqualTo(preferenceCategory2);
        preferenceCategory1.setId(null);
        assertThat(preferenceCategory1).isNotEqualTo(preferenceCategory2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PreferenceCategoryDTO.class);
        PreferenceCategoryDTO preferenceCategoryDTO1 = new PreferenceCategoryDTO();
        preferenceCategoryDTO1.setId(1L);
        PreferenceCategoryDTO preferenceCategoryDTO2 = new PreferenceCategoryDTO();
        assertThat(preferenceCategoryDTO1).isNotEqualTo(preferenceCategoryDTO2);
        preferenceCategoryDTO2.setId(preferenceCategoryDTO1.getId());
        assertThat(preferenceCategoryDTO1).isEqualTo(preferenceCategoryDTO2);
        preferenceCategoryDTO2.setId(2L);
        assertThat(preferenceCategoryDTO1).isNotEqualTo(preferenceCategoryDTO2);
        preferenceCategoryDTO1.setId(null);
        assertThat(preferenceCategoryDTO1).isNotEqualTo(preferenceCategoryDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(preferenceCategoryMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(preferenceCategoryMapper.fromId(null)).isNull();
    }
}
