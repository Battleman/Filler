package bcn.hackupc.filler.web.rest;

import bcn.hackupc.filler.BackendApp;
import bcn.hackupc.filler.domain.CustomEvent;
import bcn.hackupc.filler.repository.CustomEventRepository;
import bcn.hackupc.filler.service.CustomEventService;
import bcn.hackupc.filler.service.dto.CustomEventDTO;
import bcn.hackupc.filler.service.mapper.CustomEventMapper;
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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static bcn.hackupc.filler.web.rest.TestUtil.sameInstant;
import static bcn.hackupc.filler.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link CustomEventResource} REST controller.
 */
@SpringBootTest(classes = BackendApp.class)
public class CustomEventResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SUBJECT = "AAAAAAAAAA";
    private static final String UPDATED_SUBJECT = "BBBBBBBBBB";

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;

    private static final String DEFAULT_LATITUDE = "AAAAAAAAAA";
    private static final String UPDATED_LATITUDE = "BBBBBBBBBB";

    private static final String DEFAULT_LONGITUDE = "AAAAAAAAAA";
    private static final String UPDATED_LONGITUDE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_START_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_END_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_END_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_REPEAT = false;
    private static final Boolean UPDATED_REPEAT = true;

    @Autowired
    private CustomEventRepository customEventRepository;

    @Autowired
    private CustomEventMapper customEventMapper;

    @Autowired
    private CustomEventService customEventService;

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

    private MockMvc restCustomEventMockMvc;

    private CustomEvent customEvent;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CustomEventResource customEventResource = new CustomEventResource(customEventService);
        this.restCustomEventMockMvc = MockMvcBuilders.standaloneSetup(customEventResource)
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
    public static CustomEvent createEntity(EntityManager em) {
        CustomEvent customEvent = new CustomEvent()
            .name(DEFAULT_NAME)
            .subject(DEFAULT_SUBJECT)
            .price(DEFAULT_PRICE)
            .latitude(DEFAULT_LATITUDE)
            .longitude(DEFAULT_LONGITUDE)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .description(DEFAULT_DESCRIPTION)
            .repeat(DEFAULT_REPEAT);
        return customEvent;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomEvent createUpdatedEntity(EntityManager em) {
        CustomEvent customEvent = new CustomEvent()
            .name(UPDATED_NAME)
            .subject(UPDATED_SUBJECT)
            .price(UPDATED_PRICE)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .description(UPDATED_DESCRIPTION)
            .repeat(UPDATED_REPEAT);
        return customEvent;
    }

    @BeforeEach
    public void initTest() {
        customEvent = createEntity(em);
    }

    @Test
    @Transactional
    public void createCustomEvent() throws Exception {
        int databaseSizeBeforeCreate = customEventRepository.findAll().size();

        // Create the CustomEvent
        CustomEventDTO customEventDTO = customEventMapper.toDto(customEvent);
        restCustomEventMockMvc.perform(post("/api/custom-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customEventDTO)))
            .andExpect(status().isCreated());

        // Validate the CustomEvent in the database
        List<CustomEvent> customEventList = customEventRepository.findAll();
        assertThat(customEventList).hasSize(databaseSizeBeforeCreate + 1);
        CustomEvent testCustomEvent = customEventList.get(customEventList.size() - 1);
        assertThat(testCustomEvent.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCustomEvent.getSubject()).isEqualTo(DEFAULT_SUBJECT);
        assertThat(testCustomEvent.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testCustomEvent.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testCustomEvent.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testCustomEvent.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testCustomEvent.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testCustomEvent.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCustomEvent.isRepeat()).isEqualTo(DEFAULT_REPEAT);
    }

    @Test
    @Transactional
    public void createCustomEventWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = customEventRepository.findAll().size();

        // Create the CustomEvent with an existing ID
        customEvent.setId(1L);
        CustomEventDTO customEventDTO = customEventMapper.toDto(customEvent);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomEventMockMvc.perform(post("/api/custom-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customEventDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CustomEvent in the database
        List<CustomEvent> customEventList = customEventRepository.findAll();
        assertThat(customEventList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllCustomEvents() throws Exception {
        // Initialize the database
        customEventRepository.saveAndFlush(customEvent);

        // Get all the customEventList
        restCustomEventMockMvc.perform(get("/api/custom-events?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customEvent.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE)))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(sameInstant(DEFAULT_START_DATE))))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(sameInstant(DEFAULT_END_DATE))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].repeat").value(hasItem(DEFAULT_REPEAT.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getCustomEvent() throws Exception {
        // Initialize the database
        customEventRepository.saveAndFlush(customEvent);

        // Get the customEvent
        restCustomEventMockMvc.perform(get("/api/custom-events/{id}", customEvent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(customEvent.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.subject").value(DEFAULT_SUBJECT))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE))
            .andExpect(jsonPath("$.startDate").value(sameInstant(DEFAULT_START_DATE)))
            .andExpect(jsonPath("$.endDate").value(sameInstant(DEFAULT_END_DATE)))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.repeat").value(DEFAULT_REPEAT.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCustomEvent() throws Exception {
        // Get the customEvent
        restCustomEventMockMvc.perform(get("/api/custom-events/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCustomEvent() throws Exception {
        // Initialize the database
        customEventRepository.saveAndFlush(customEvent);

        int databaseSizeBeforeUpdate = customEventRepository.findAll().size();

        // Update the customEvent
        CustomEvent updatedCustomEvent = customEventRepository.findById(customEvent.getId()).get();
        // Disconnect from session so that the updates on updatedCustomEvent are not directly saved in db
        em.detach(updatedCustomEvent);
        updatedCustomEvent
            .name(UPDATED_NAME)
            .subject(UPDATED_SUBJECT)
            .price(UPDATED_PRICE)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .description(UPDATED_DESCRIPTION)
            .repeat(UPDATED_REPEAT);
        CustomEventDTO customEventDTO = customEventMapper.toDto(updatedCustomEvent);

        restCustomEventMockMvc.perform(put("/api/custom-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customEventDTO)))
            .andExpect(status().isOk());

        // Validate the CustomEvent in the database
        List<CustomEvent> customEventList = customEventRepository.findAll();
        assertThat(customEventList).hasSize(databaseSizeBeforeUpdate);
        CustomEvent testCustomEvent = customEventList.get(customEventList.size() - 1);
        assertThat(testCustomEvent.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCustomEvent.getSubject()).isEqualTo(UPDATED_SUBJECT);
        assertThat(testCustomEvent.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testCustomEvent.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testCustomEvent.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testCustomEvent.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testCustomEvent.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testCustomEvent.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCustomEvent.isRepeat()).isEqualTo(UPDATED_REPEAT);
    }

    @Test
    @Transactional
    public void updateNonExistingCustomEvent() throws Exception {
        int databaseSizeBeforeUpdate = customEventRepository.findAll().size();

        // Create the CustomEvent
        CustomEventDTO customEventDTO = customEventMapper.toDto(customEvent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomEventMockMvc.perform(put("/api/custom-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customEventDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CustomEvent in the database
        List<CustomEvent> customEventList = customEventRepository.findAll();
        assertThat(customEventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCustomEvent() throws Exception {
        // Initialize the database
        customEventRepository.saveAndFlush(customEvent);

        int databaseSizeBeforeDelete = customEventRepository.findAll().size();

        // Delete the customEvent
        restCustomEventMockMvc.perform(delete("/api/custom-events/{id}", customEvent.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CustomEvent> customEventList = customEventRepository.findAll();
        assertThat(customEventList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomEvent.class);
        CustomEvent customEvent1 = new CustomEvent();
        customEvent1.setId(1L);
        CustomEvent customEvent2 = new CustomEvent();
        customEvent2.setId(customEvent1.getId());
        assertThat(customEvent1).isEqualTo(customEvent2);
        customEvent2.setId(2L);
        assertThat(customEvent1).isNotEqualTo(customEvent2);
        customEvent1.setId(null);
        assertThat(customEvent1).isNotEqualTo(customEvent2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomEventDTO.class);
        CustomEventDTO customEventDTO1 = new CustomEventDTO();
        customEventDTO1.setId(1L);
        CustomEventDTO customEventDTO2 = new CustomEventDTO();
        assertThat(customEventDTO1).isNotEqualTo(customEventDTO2);
        customEventDTO2.setId(customEventDTO1.getId());
        assertThat(customEventDTO1).isEqualTo(customEventDTO2);
        customEventDTO2.setId(2L);
        assertThat(customEventDTO1).isNotEqualTo(customEventDTO2);
        customEventDTO1.setId(null);
        assertThat(customEventDTO1).isNotEqualTo(customEventDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(customEventMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(customEventMapper.fromId(null)).isNull();
    }
}
