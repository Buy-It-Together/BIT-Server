package com.ujazdowski.buyitogether.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ujazdowski.buyitogether.domain.*;
import com.ujazdowski.buyitogether.repository.ChatRepository;
import com.ujazdowski.buyitogether.service.ChatService;
import com.ujazdowski.buyitogether.service.UserService;
import com.ujazdowski.buyitogether.service.dto.ChatDTO;
import com.ujazdowski.buyitogether.service.dto.CoordinatesDTO;
import com.ujazdowski.buyitogether.web.rest.errors.BadRequestAlertException;
import com.ujazdowski.buyitogether.web.rest.util.HeaderUtil;
import com.ujazdowski.buyitogether.web.rest.vm.*;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * REST controller for managing Chat.
 */
@RestController
@RequestMapping("/api")
public class ChatResource {

    private static final String ENTITY_NAME = "chat";
    private final Logger log = LoggerFactory.getLogger(ChatResource.class);
    private final ChatService chatService;
    private final ChatRepository chatRepository;
    private final UserService userService;

    @Autowired
    public ChatResource(ChatService chatService, ChatRepository chatRepository, UserService userService) {
        this.chatService = chatService;
        this.chatRepository = chatRepository;
        this.userService = userService;
    }

    /**
     * POST  /chats : Create a new chat.
     *
     * @param chatDTO the chatDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new chatDTO, or with status 400 (Bad Request) if the chat has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/chats")
    @Timed
    public ResponseEntity<ChatDTO> createChat(@RequestBody ChatDTO chatDTO) throws URISyntaxException {
        log.debug("REST request to save Chat : {}", chatDTO);
        if (chatDTO.getId() != null) {
            throw new BadRequestAlertException("A new chat cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ChatDTO result = chatService.save(chatDTO);
        return ResponseEntity.created(new URI("/api/chats/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /chats : Updates an existing chat.
     *
     * @param chatDTO the chatDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated chatDTO,
     * or with status 400 (Bad Request) if the chatDTO is not valid,
     * or with status 500 (Internal Server Error) if the chatDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/chats")
    @Timed
    public ResponseEntity<ChatDTO> updateChat(@RequestBody ChatDTO chatDTO) throws URISyntaxException {
        log.debug("REST request to update Chat : {}", chatDTO);
        if (chatDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ChatDTO result = chatService.save(chatDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, chatDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /chats : get all the chats.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of chats in body
     */
    @GetMapping("/chats")
    @Timed
    public List<ChatDTO> getAllChats() {
        log.debug("REST request to get all Chats");
        return chatService.findAll();
    }

    /**
     * GET  /chats/:id : get the "id" chat.
     *
     * @param id the id of the chatDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the chatDTO, or with status 404 (Not Found)
     */
    @GetMapping("/chats/{id}")
    @Timed
    public ResponseEntity<ChatDTO> getChat(@PathVariable Long id) {
        log.debug("REST request to get Chat : {}", id);
        Optional<ChatDTO> chatDTO = chatService.findOne(id);
        return ResponseUtil.wrapOrNotFound(chatDTO);
    }

    /**
     * DELETE  /chats/:id : delete the "id" chat.
     *
     * @param id the id of the chatDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/chats/{id}")
    @Timed
    public ResponseEntity<Void> deleteChat(@PathVariable Long id) {
        log.debug("REST request to delete Chat : {}", id);
        chatService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * GET  /chats-by-user-offer/:id : get the chat by "id" of user offer.
     *
     * @param id the id of the UserOfferDTO to retrieve chat set
     * @return the ResponseEntity with status 200 (OK) and with body the chatDTO, or with status 404 (Not Found)
     */
    @GetMapping("/chats-by-user-offer/{id}")
    @Timed
    public ResponseEntity getChatByUserOffer(@PathVariable Long id) {
        log.debug("REST request to get Chat : {}", id);
        Set<ChatVM> chats = chatRepository.findByUserOfferId(id).stream().map(this::toVm).collect(Collectors.toSet());
        return ResponseEntity.ok(chats);
    }

    @GetMapping("/chat/accept/{id}")
    public ResponseEntity acceptChat(@PathVariable Long id) {
        return ResponseEntity.ok(userOfferChatToVM(chatService.accept(id), userService.getUserWithAuthorities().get()));
    }

    @PostMapping("/chat/message/{chat}")
    public ResponseEntity sendMessage(@PathVariable Long chat, @RequestBody MessageContentVM message) {
        MessageVM messageVM = messageToVm(chatService.sendMessage(chat, message.getMessage()));
        return ResponseEntity.ok(messageVM);
    }

    private MessageVM messageToVm(Message message) {
        return new MessageVM(message.getUser().getEmail(), message.getMessage(), message.getDate());
    }

    private ChatVM toVm(Chat chat) {
        CoordinatesDTO coordinatesDTO = chatService.calculatePlaceToMeet(chat);
        return new ChatVM(chat.getId(), chat.getUsers().stream().map(userOfferChat -> userOfferChatToVM(userOfferChat, userService.getUserWithAuthorities().get()))
            .collect(Collectors.toSet()), chat.getMessages().stream().map(this::messageToVm).sorted((o1, o2) -> o1.getDate().getNano() - o2.getDate().getNano()).collect(Collectors.toList()),
            coordinatesDTO.getLatitude(), coordinatesDTO.getLongitude());
    }

    private UserOfferChatVM userOfferChatToVM(UserOfferChat userOfferChat, User current) {
        return new UserOfferChatVM(userOfferChat.getId(), userOfferChat.isAccepted(), userOfferToVM(userOfferChat.getUserOffer()), current.getId().equals(userOfferChat.getUserOffer().getUser().getId()));
    }

    private UserOfferVM userOfferToVM(UserOffer userOffer) {
        return new UserOfferVM(userOffer.getLongitude(), userOffer.getLatitude(), userOffer.getDistance(),
            userOffer.getCountOfItems(), userOffer.getCountOfItemsToGetBonus(), userOffer.getExpirationDate());
    }
}
