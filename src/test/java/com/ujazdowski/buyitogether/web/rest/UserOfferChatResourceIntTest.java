package com.ujazdowski.buyitogether.web.rest;

import com.ujazdowski.buyitogether.BuyItTogetherApp;

import com.ujazdowski.buyitogether.domain.UserOfferChat;
import com.ujazdowski.buyitogether.repository.UserOfferChatRepository;
import com.ujazdowski.buyitogether.service.UserOfferChatService;
import com.ujazdowski.buyitogether.service.dto.UserOfferChatDTO;
import com.ujazdowski.buyitogether.service.mapper.UserOfferChatMapper;
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
import java.util.List;


import static com.ujazdowski.buyitogether.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the UserOfferChatResource REST controller.
 *
 * @see UserOfferChatResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BuyItTogetherApp.class)
public class UserOfferChatResourceIntTest {

    private static final Boolean DEFAULT_ACCEPTED = false;
    private static final Boolean UPDATED_ACCEPTED = true;

    @Autowired
    private UserOfferChatRepository userOfferChatRepository;

    @Autowired
    private UserOfferChatMapper userOfferChatMapper;
    
    @Autowired
    private UserOfferChatService userOfferChatService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restUserOfferChatMockMvc;

    private UserOfferChat userOfferChat;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UserOfferChatResource userOfferChatResource = new UserOfferChatResource(userOfferChatService);
        this.restUserOfferChatMockMvc = MockMvcBuilders.standaloneSetup(userOfferChatResource)
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
    public static UserOfferChat createEntity(EntityManager em) {
        UserOfferChat userOfferChat = new UserOfferChat()
            .accepted(DEFAULT_ACCEPTED);
        return userOfferChat;
    }

    @Before
    public void initTest() {
        userOfferChat = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserOfferChat() throws Exception {
        int databaseSizeBeforeCreate = userOfferChatRepository.findAll().size();

        // Create the UserOfferChat
        UserOfferChatDTO userOfferChatDTO = userOfferChatMapper.toDto(userOfferChat);
        restUserOfferChatMockMvc.perform(post("/api/user-offer-chats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userOfferChatDTO)))
            .andExpect(status().isCreated());

        // Validate the UserOfferChat in the database
        List<UserOfferChat> userOfferChatList = userOfferChatRepository.findAll();
        assertThat(userOfferChatList).hasSize(databaseSizeBeforeCreate + 1);
        UserOfferChat testUserOfferChat = userOfferChatList.get(userOfferChatList.size() - 1);
        assertThat(testUserOfferChat.isAccepted()).isEqualTo(DEFAULT_ACCEPTED);
    }

    @Test
    @Transactional
    public void createUserOfferChatWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userOfferChatRepository.findAll().size();

        // Create the UserOfferChat with an existing ID
        userOfferChat.setId(1L);
        UserOfferChatDTO userOfferChatDTO = userOfferChatMapper.toDto(userOfferChat);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserOfferChatMockMvc.perform(post("/api/user-offer-chats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userOfferChatDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserOfferChat in the database
        List<UserOfferChat> userOfferChatList = userOfferChatRepository.findAll();
        assertThat(userOfferChatList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkAcceptedIsRequired() throws Exception {
        int databaseSizeBeforeTest = userOfferChatRepository.findAll().size();
        // set the field null
        userOfferChat.setAccepted(null);

        // Create the UserOfferChat, which fails.
        UserOfferChatDTO userOfferChatDTO = userOfferChatMapper.toDto(userOfferChat);

        restUserOfferChatMockMvc.perform(post("/api/user-offer-chats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userOfferChatDTO)))
            .andExpect(status().isBadRequest());

        List<UserOfferChat> userOfferChatList = userOfferChatRepository.findAll();
        assertThat(userOfferChatList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUserOfferChats() throws Exception {
        // Initialize the database
        userOfferChatRepository.saveAndFlush(userOfferChat);

        // Get all the userOfferChatList
        restUserOfferChatMockMvc.perform(get("/api/user-offer-chats?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userOfferChat.getId().intValue())))
            .andExpect(jsonPath("$.[*].accepted").value(hasItem(DEFAULT_ACCEPTED.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getUserOfferChat() throws Exception {
        // Initialize the database
        userOfferChatRepository.saveAndFlush(userOfferChat);

        // Get the userOfferChat
        restUserOfferChatMockMvc.perform(get("/api/user-offer-chats/{id}", userOfferChat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userOfferChat.getId().intValue()))
            .andExpect(jsonPath("$.accepted").value(DEFAULT_ACCEPTED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingUserOfferChat() throws Exception {
        // Get the userOfferChat
        restUserOfferChatMockMvc.perform(get("/api/user-offer-chats/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserOfferChat() throws Exception {
        // Initialize the database
        userOfferChatRepository.saveAndFlush(userOfferChat);

        int databaseSizeBeforeUpdate = userOfferChatRepository.findAll().size();

        // Update the userOfferChat
        UserOfferChat updatedUserOfferChat = userOfferChatRepository.findById(userOfferChat.getId()).get();
        // Disconnect from session so that the updates on updatedUserOfferChat are not directly saved in db
        em.detach(updatedUserOfferChat);
        updatedUserOfferChat
            .accepted(UPDATED_ACCEPTED);
        UserOfferChatDTO userOfferChatDTO = userOfferChatMapper.toDto(updatedUserOfferChat);

        restUserOfferChatMockMvc.perform(put("/api/user-offer-chats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userOfferChatDTO)))
            .andExpect(status().isOk());

        // Validate the UserOfferChat in the database
        List<UserOfferChat> userOfferChatList = userOfferChatRepository.findAll();
        assertThat(userOfferChatList).hasSize(databaseSizeBeforeUpdate);
        UserOfferChat testUserOfferChat = userOfferChatList.get(userOfferChatList.size() - 1);
        assertThat(testUserOfferChat.isAccepted()).isEqualTo(UPDATED_ACCEPTED);
    }

    @Test
    @Transactional
    public void updateNonExistingUserOfferChat() throws Exception {
        int databaseSizeBeforeUpdate = userOfferChatRepository.findAll().size();

        // Create the UserOfferChat
        UserOfferChatDTO userOfferChatDTO = userOfferChatMapper.toDto(userOfferChat);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserOfferChatMockMvc.perform(put("/api/user-offer-chats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userOfferChatDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserOfferChat in the database
        List<UserOfferChat> userOfferChatList = userOfferChatRepository.findAll();
        assertThat(userOfferChatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteUserOfferChat() throws Exception {
        // Initialize the database
        userOfferChatRepository.saveAndFlush(userOfferChat);

        int databaseSizeBeforeDelete = userOfferChatRepository.findAll().size();

        // Get the userOfferChat
        restUserOfferChatMockMvc.perform(delete("/api/user-offer-chats/{id}", userOfferChat.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<UserOfferChat> userOfferChatList = userOfferChatRepository.findAll();
        assertThat(userOfferChatList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserOfferChat.class);
        UserOfferChat userOfferChat1 = new UserOfferChat();
        userOfferChat1.setId(1L);
        UserOfferChat userOfferChat2 = new UserOfferChat();
        userOfferChat2.setId(userOfferChat1.getId());
        assertThat(userOfferChat1).isEqualTo(userOfferChat2);
        userOfferChat2.setId(2L);
        assertThat(userOfferChat1).isNotEqualTo(userOfferChat2);
        userOfferChat1.setId(null);
        assertThat(userOfferChat1).isNotEqualTo(userOfferChat2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserOfferChatDTO.class);
        UserOfferChatDTO userOfferChatDTO1 = new UserOfferChatDTO();
        userOfferChatDTO1.setId(1L);
        UserOfferChatDTO userOfferChatDTO2 = new UserOfferChatDTO();
        assertThat(userOfferChatDTO1).isNotEqualTo(userOfferChatDTO2);
        userOfferChatDTO2.setId(userOfferChatDTO1.getId());
        assertThat(userOfferChatDTO1).isEqualTo(userOfferChatDTO2);
        userOfferChatDTO2.setId(2L);
        assertThat(userOfferChatDTO1).isNotEqualTo(userOfferChatDTO2);
        userOfferChatDTO1.setId(null);
        assertThat(userOfferChatDTO1).isNotEqualTo(userOfferChatDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(userOfferChatMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(userOfferChatMapper.fromId(null)).isNull();
    }
}
