package com.ujazdowski.buyitogether.service.impl;

import com.ujazdowski.buyitogether.domain.*;
import com.ujazdowski.buyitogether.repository.ChatRepository;
import com.ujazdowski.buyitogether.repository.MessageRepository;
import com.ujazdowski.buyitogether.repository.UserOfferChatRepository;
import com.ujazdowski.buyitogether.service.ChatService;
import com.ujazdowski.buyitogether.service.UserService;
import com.ujazdowski.buyitogether.service.dto.ChatDTO;
import com.ujazdowski.buyitogether.service.dto.CoordinatesDTO;
import com.ujazdowski.buyitogether.service.exceptions.EntityNotFountException;
import com.ujazdowski.buyitogether.service.mapper.ChatMapper;
import com.ujazdowski.buyitogether.web.rest.vm.MessageVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Chat.
 */
@Service
@Transactional
public class ChatServiceImpl implements ChatService {

    private final static String AUTHENTICATION_CREDENTIALS_NOT_FOUND_MESSAGE = "Authentication credentials not found!";

    private final Logger log = LoggerFactory.getLogger(ChatServiceImpl.class);

    private final ChatRepository chatRepository;

    private final ChatMapper chatMapper;

    private final UserOfferChatRepository userOfferChatRepository;

    private final UserService userService;

    private final MessageRepository messageRepository;

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public ChatServiceImpl(ChatRepository chatRepository, ChatMapper chatMapper, UserOfferChatRepository userOfferChatRepository, UserService userService, MessageRepository messageRepository, SimpMessagingTemplate simpMessagingTemplate) {
        this.chatRepository = chatRepository;
        this.chatMapper = chatMapper;
        this.userOfferChatRepository = userOfferChatRepository;
        this.userService = userService;
        this.messageRepository = messageRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    /**
     * Save a chat.
     *
     * @param chatDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ChatDTO save(ChatDTO chatDTO) {
        log.debug("Request to save Chat : {}", chatDTO);

        Chat chat = chatMapper.toEntity(chatDTO);
        chat = chatRepository.save(chat);
        return chatMapper.toDto(chat);
    }

    /**
     * Get all the chats.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ChatDTO> findAll() {
        log.debug("Request to get all Chats");
        return chatRepository.findAll().stream()
            .map(chatMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one chat by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ChatDTO> findOne(Long id) {
        log.debug("Request to get Chat : {}", id);
        return chatRepository.findById(id)
            .map(chatMapper::toDto);
    }

    /**
     * Delete the chat by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Chat : {}", id);
        chatRepository.deleteById(id);
    }

    @Override
    public void saveChats(List<Chat> chats) {
        for (Iterator<Chat> iterator = chats.iterator(); iterator.hasNext(); ) {
            Chat chat = iterator.next();
            Set<Long> ids = chat.getUsers().stream().mapToLong(value -> value.getUserOffer().getId()).boxed().collect(Collectors.toSet());
            if (userOfferChatRepository.existsUserOfferChatsByUserOffers(ids, Long.valueOf(ids.size()))) {
                iterator.remove();
            }
        }
        chatRepository.saveAll(chats);
    }

    /**
     * @param chat
     * @return
     * @link http://www.geomidpoint.com/calculation.html
     */
    @Override
    public CoordinatesDTO calculatePlaceToMeet(Chat chat) {
        List<UserOffer> offers = chat.getUsers().stream().map(UserOfferChat::getUserOffer).collect(Collectors.toList());

        Double X = 0.0, Y = 0.0, Z = 0.0, totalWeight = 0.0;

        for (UserOffer userOffer : offers) {
            Double lat = Math.toRadians(userOffer.getLatitude());
            Double lon = Math.toRadians(userOffer.getLongitude());
            Double weight = 1.0/userOffer.getDistance();

            X += Math.cos(lat) * Math.cos(lon) * weight;
            Y += Math.cos(lat) * Math.sin(lon) * weight;
            Z += Math.sin(lat) * weight;

            totalWeight += weight;
        }

        X /= totalWeight;
        Y /= totalWeight;
        Z /= totalWeight;

        if (Math.abs(X) < Math.pow(10, -9) &&
            Math.abs(Y) < Math.pow(10, -9) &&
            Math.abs(Z) < Math.pow(10, -9)) {
            return new CoordinatesDTO(0.0, 0.0);
        }

        return new CoordinatesDTO(
            Math.toDegrees(Math.atan2(Z, Math.sqrt(X * X + Y * Y))),
            Math.toDegrees(Math.atan2(Y, X)));
    }

    @Override
    public UserOfferChat accept(Long id) {
        Chat chat = chatRepository.findById(id).orElseThrow(() -> new EntityNotFountException(Chat.class, id));
        User user = userService.getUserWithAuthorities().orElseThrow(() -> new AuthenticationCredentialsNotFoundException(AUTHENTICATION_CREDENTIALS_NOT_FOUND_MESSAGE));
        UserOfferChat offerChat = chat.getUsers().stream().filter(userOfferChat -> userOfferChat.getUserOffer().getUser().getId().equals(user.getId()))
            .findFirst().orElseThrow(() -> new UnsupportedOperationException());
        offerChat.setAccepted(true);
        userOfferChatRepository.save(offerChat);
        return offerChat;
    }

    @Override
    public Message sendMessage(Long chatId, String content) {
        User user = userService.getUserWithAuthorities().orElseThrow(() -> new AuthenticationCredentialsNotFoundException(AUTHENTICATION_CREDENTIALS_NOT_FOUND_MESSAGE));
        Chat chat = chatRepository.getOne(chatId);
        if (!chat.getUsers().stream().anyMatch(userOfferChat -> userOfferChat.getUserOffer().getUser().getId().equals(user.getId()))) {
            throw new PermissionDeniedDataAccessException(new StringBuilder().append("User: ").append(user.getEmail()).append(" has no permission to these data").toString(), null);
        }
        Message message = messageRepository.save(new Message().message(content.trim()).date(Instant.now()).chat(chat).user(user));
        simpMessagingTemplate.convertAndSend(new StringBuilder().append("/topic/chat-messages/").append(chatId).toString(),
            new MessageVM(user.getEmail(), message.getMessage(), message.getDate()));
        return message;
    }
}
