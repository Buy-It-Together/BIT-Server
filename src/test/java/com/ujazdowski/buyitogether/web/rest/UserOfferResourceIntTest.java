package com.ujazdowski.buyitogether.web.rest;

import com.ujazdowski.buyitogether.BuyItTogetherApp;

import com.ujazdowski.buyitogether.domain.UserOffer;
import com.ujazdowski.buyitogether.repository.UserOfferRepository;
import com.ujazdowski.buyitogether.service.UserOfferService;
import com.ujazdowski.buyitogether.service.UserService;
import com.ujazdowski.buyitogether.service.dto.UserOfferDTO;
import com.ujazdowski.buyitogether.service.mapper.UserOfferMapper;
import com.ujazdowski.buyitogether.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;


import static com.ujazdowski.buyitogether.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the UserOfferResource REST controller.
 *
 * @see UserOfferResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BuyItTogetherApp.class)
public class UserOfferResourceIntTest {

    private static final String DEFAULT_LINK = "AAAAAAAAAA";
    private static final String UPDATED_LINK = "BBBBBBBBBB";

    private static final Double DEFAULT_LONGITUDE = 1D;
    private static final Double UPDATED_LONGITUDE = 2D;

    private static final Double DEFAULT_LATITUDE = 1D;
    private static final Double UPDATED_LATITUDE = 2D;

    private static final Double DEFAULT_DISTANCE = 1D;
    private static final Double UPDATED_DISTANCE = 2D;

    private static final Integer DEFAULT_COUNT_OF_ITEMS = 1;
    private static final Integer UPDATED_COUNT_OF_ITEMS = 2;

    private static final Integer DEFAULT_COUNT_OF_ITEMS_TO_GET_BONUS = 1;
    private static final Integer UPDATED_COUNT_OF_ITEMS_TO_GET_BONUS = 2;

    private static final Instant DEFAULT_EXPIRATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPIRATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private UserOfferRepository userOfferRepository;

    @Autowired
    private UserOfferMapper userOfferMapper;
    
    @Autowired
    private UserOfferService userOfferService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restUserOfferMockMvc;

    private UserOffer userOffer;

    @Autowired
    private UserService userService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UserOfferResource userOfferResource = new UserOfferResource(userOfferService, userService);
        this.restUserOfferMockMvc = MockMvcBuilders.standaloneSetup(userOfferResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserOffer createEntity(EntityManager em) {
        UserOffer userOffer = new UserOffer()
            .link(DEFAULT_LINK)
            .longitude(DEFAULT_LONGITUDE)
            .latitude(DEFAULT_LATITUDE)
            .distance(DEFAULT_DISTANCE)
            .countOfItems(DEFAULT_COUNT_OF_ITEMS)
            .countOfItemsToGetBonus(DEFAULT_COUNT_OF_ITEMS_TO_GET_BONUS)
            .expirationDate(DEFAULT_EXPIRATION_DATE);
        return userOffer;
    }

    @Before
    public void initTest() {
        userOffer = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserOffer() throws Exception {
        int databaseSizeBeforeCreate = userOfferRepository.findAll().size();

        // Create the UserOffer
        UserOfferDTO userOfferDTO = userOfferMapper.toDto(userOffer);
        restUserOfferMockMvc.perform(post("/api/user-offers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userOfferDTO)))
            .andExpect(status().isCreated());

        // Validate the UserOffer in the database
        List<UserOffer> userOfferList = userOfferRepository.findAll();
        assertThat(userOfferList).hasSize(databaseSizeBeforeCreate + 1);
        UserOffer testUserOffer = userOfferList.get(userOfferList.size() - 1);
        assertThat(testUserOffer.getLink()).isEqualTo(DEFAULT_LINK);
        assertThat(testUserOffer.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testUserOffer.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testUserOffer.getDistance()).isEqualTo(DEFAULT_DISTANCE);
        assertThat(testUserOffer.getCountOfItems()).isEqualTo(DEFAULT_COUNT_OF_ITEMS);
        assertThat(testUserOffer.getCountOfItemsToGetBonus()).isEqualTo(DEFAULT_COUNT_OF_ITEMS_TO_GET_BONUS);
        assertThat(testUserOffer.getExpirationDate()).isEqualTo(DEFAULT_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    public void createUserOfferWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userOfferRepository.findAll().size();

        // Create the UserOffer with an existing ID
        userOffer.setId(1L);
        UserOfferDTO userOfferDTO = userOfferMapper.toDto(userOffer);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserOfferMockMvc.perform(post("/api/user-offers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userOfferDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserOffer in the database
        List<UserOffer> userOfferList = userOfferRepository.findAll();
        assertThat(userOfferList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkLinkIsRequired() throws Exception {
        int databaseSizeBeforeTest = userOfferRepository.findAll().size();
        // set the field null
        userOffer.setLink(null);

        // Create the UserOffer, which fails.
        UserOfferDTO userOfferDTO = userOfferMapper.toDto(userOffer);

        restUserOfferMockMvc.perform(post("/api/user-offers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userOfferDTO)))
            .andExpect(status().isBadRequest());

        List<UserOffer> userOfferList = userOfferRepository.findAll();
        assertThat(userOfferList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLongitudeIsRequired() throws Exception {
        int databaseSizeBeforeTest = userOfferRepository.findAll().size();
        // set the field null
        userOffer.setLongitude(null);

        // Create the UserOffer, which fails.
        UserOfferDTO userOfferDTO = userOfferMapper.toDto(userOffer);

        restUserOfferMockMvc.perform(post("/api/user-offers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userOfferDTO)))
            .andExpect(status().isBadRequest());

        List<UserOffer> userOfferList = userOfferRepository.findAll();
        assertThat(userOfferList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLatitudeIsRequired() throws Exception {
        int databaseSizeBeforeTest = userOfferRepository.findAll().size();
        // set the field null
        userOffer.setLatitude(null);

        // Create the UserOffer, which fails.
        UserOfferDTO userOfferDTO = userOfferMapper.toDto(userOffer);

        restUserOfferMockMvc.perform(post("/api/user-offers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userOfferDTO)))
            .andExpect(status().isBadRequest());

        List<UserOffer> userOfferList = userOfferRepository.findAll();
        assertThat(userOfferList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDistanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = userOfferRepository.findAll().size();
        // set the field null
        userOffer.setDistance(null);

        // Create the UserOffer, which fails.
        UserOfferDTO userOfferDTO = userOfferMapper.toDto(userOffer);

        restUserOfferMockMvc.perform(post("/api/user-offers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userOfferDTO)))
            .andExpect(status().isBadRequest());

        List<UserOffer> userOfferList = userOfferRepository.findAll();
        assertThat(userOfferList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCountOfItemsIsRequired() throws Exception {
        int databaseSizeBeforeTest = userOfferRepository.findAll().size();
        // set the field null
        userOffer.setCountOfItems(null);

        // Create the UserOffer, which fails.
        UserOfferDTO userOfferDTO = userOfferMapper.toDto(userOffer);

        restUserOfferMockMvc.perform(post("/api/user-offers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userOfferDTO)))
            .andExpect(status().isBadRequest());

        List<UserOffer> userOfferList = userOfferRepository.findAll();
        assertThat(userOfferList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCountOfItemsToGetBonusIsRequired() throws Exception {
        int databaseSizeBeforeTest = userOfferRepository.findAll().size();
        // set the field null
        userOffer.setCountOfItemsToGetBonus(null);

        // Create the UserOffer, which fails.
        UserOfferDTO userOfferDTO = userOfferMapper.toDto(userOffer);

        restUserOfferMockMvc.perform(post("/api/user-offers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userOfferDTO)))
            .andExpect(status().isBadRequest());

        List<UserOffer> userOfferList = userOfferRepository.findAll();
        assertThat(userOfferList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkExpirationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = userOfferRepository.findAll().size();
        // set the field null
        userOffer.setExpirationDate(null);

        // Create the UserOffer, which fails.
        UserOfferDTO userOfferDTO = userOfferMapper.toDto(userOffer);

        restUserOfferMockMvc.perform(post("/api/user-offers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userOfferDTO)))
            .andExpect(status().isBadRequest());

        List<UserOffer> userOfferList = userOfferRepository.findAll();
        assertThat(userOfferList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUserOffers() throws Exception {
        // Initialize the database
        userOfferRepository.saveAndFlush(userOffer);

        // Get all the userOfferList
        restUserOfferMockMvc.perform(get("/api/user-offers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userOffer.getId().intValue())))
            .andExpect(jsonPath("$.[*].link").value(hasItem(DEFAULT_LINK.toString())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].distance").value(hasItem(DEFAULT_DISTANCE.doubleValue())))
            .andExpect(jsonPath("$.[*].countOfItems").value(hasItem(DEFAULT_COUNT_OF_ITEMS)))
            .andExpect(jsonPath("$.[*].countOfItemsToGetBonus").value(hasItem(DEFAULT_COUNT_OF_ITEMS_TO_GET_BONUS)))
            .andExpect(jsonPath("$.[*].expirationDate").value(hasItem(DEFAULT_EXPIRATION_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getUserOffer() throws Exception {
        // Initialize the database
        userOfferRepository.saveAndFlush(userOffer);

        // Get the userOffer
        restUserOfferMockMvc.perform(get("/api/user-offers/{id}", userOffer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userOffer.getId().intValue()))
            .andExpect(jsonPath("$.link").value(DEFAULT_LINK.toString()))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.doubleValue()))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.doubleValue()))
            .andExpect(jsonPath("$.distance").value(DEFAULT_DISTANCE.doubleValue()))
            .andExpect(jsonPath("$.countOfItems").value(DEFAULT_COUNT_OF_ITEMS))
            .andExpect(jsonPath("$.countOfItemsToGetBonus").value(DEFAULT_COUNT_OF_ITEMS_TO_GET_BONUS))
            .andExpect(jsonPath("$.expirationDate").value(DEFAULT_EXPIRATION_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUserOffer() throws Exception {
        // Get the userOffer
        restUserOfferMockMvc.perform(get("/api/user-offers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserOffer() throws Exception {
        // Initialize the database
        userOfferRepository.saveAndFlush(userOffer);

        int databaseSizeBeforeUpdate = userOfferRepository.findAll().size();

        // Update the userOffer
        UserOffer updatedUserOffer = userOfferRepository.findById(userOffer.getId()).get();
        // Disconnect from session so that the updates on updatedUserOffer are not directly saved in db
        em.detach(updatedUserOffer);
        updatedUserOffer
            .link(UPDATED_LINK)
            .longitude(UPDATED_LONGITUDE)
            .latitude(UPDATED_LATITUDE)
            .distance(UPDATED_DISTANCE)
            .countOfItems(UPDATED_COUNT_OF_ITEMS)
            .countOfItemsToGetBonus(UPDATED_COUNT_OF_ITEMS_TO_GET_BONUS)
            .expirationDate(UPDATED_EXPIRATION_DATE);
        UserOfferDTO userOfferDTO = userOfferMapper.toDto(updatedUserOffer);

        restUserOfferMockMvc.perform(put("/api/user-offers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userOfferDTO)))
            .andExpect(status().isOk());

        // Validate the UserOffer in the database
        List<UserOffer> userOfferList = userOfferRepository.findAll();
        assertThat(userOfferList).hasSize(databaseSizeBeforeUpdate);
        UserOffer testUserOffer = userOfferList.get(userOfferList.size() - 1);
        assertThat(testUserOffer.getLink()).isEqualTo(UPDATED_LINK);
        assertThat(testUserOffer.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testUserOffer.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testUserOffer.getDistance()).isEqualTo(UPDATED_DISTANCE);
        assertThat(testUserOffer.getCountOfItems()).isEqualTo(UPDATED_COUNT_OF_ITEMS);
        assertThat(testUserOffer.getCountOfItemsToGetBonus()).isEqualTo(UPDATED_COUNT_OF_ITEMS_TO_GET_BONUS);
        assertThat(testUserOffer.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingUserOffer() throws Exception {
        int databaseSizeBeforeUpdate = userOfferRepository.findAll().size();

        // Create the UserOffer
        UserOfferDTO userOfferDTO = userOfferMapper.toDto(userOffer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserOfferMockMvc.perform(put("/api/user-offers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userOfferDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserOffer in the database
        List<UserOffer> userOfferList = userOfferRepository.findAll();
        assertThat(userOfferList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteUserOffer() throws Exception {
        // Initialize the database
        userOfferRepository.saveAndFlush(userOffer);

        int databaseSizeBeforeDelete = userOfferRepository.findAll().size();

        // Get the userOffer
        restUserOfferMockMvc.perform(delete("/api/user-offers/{id}", userOffer.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<UserOffer> userOfferList = userOfferRepository.findAll();
        assertThat(userOfferList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserOffer.class);
        UserOffer userOffer1 = new UserOffer();
        userOffer1.setId(1L);
        UserOffer userOffer2 = new UserOffer();
        userOffer2.setId(userOffer1.getId());
        assertThat(userOffer1).isEqualTo(userOffer2);
        userOffer2.setId(2L);
        assertThat(userOffer1).isNotEqualTo(userOffer2);
        userOffer1.setId(null);
        assertThat(userOffer1).isNotEqualTo(userOffer2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserOfferDTO.class);
        UserOfferDTO userOfferDTO1 = new UserOfferDTO();
        userOfferDTO1.setId(1L);
        UserOfferDTO userOfferDTO2 = new UserOfferDTO();
        assertThat(userOfferDTO1).isNotEqualTo(userOfferDTO2);
        userOfferDTO2.setId(userOfferDTO1.getId());
        assertThat(userOfferDTO1).isEqualTo(userOfferDTO2);
        userOfferDTO2.setId(2L);
        assertThat(userOfferDTO1).isNotEqualTo(userOfferDTO2);
        userOfferDTO1.setId(null);
        assertThat(userOfferDTO1).isNotEqualTo(userOfferDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(userOfferMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(userOfferMapper.fromId(null)).isNull();
    }
}
