package com.ujazdowski.buyitogether.service;

import com.ujazdowski.buyitogether.domain.Chat;
import com.ujazdowski.buyitogether.domain.Message;
import com.ujazdowski.buyitogether.domain.UserOfferChat;
import com.ujazdowski.buyitogether.service.dto.ChatDTO;
import com.ujazdowski.buyitogether.service.dto.CoordinatesDTO;
import com.ujazdowski.buyitogether.service.dto.MessageDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Chat.
 */
public interface ChatService {

    /**
     * Save a chat.
     *
     * @param chatDTO the entity to save
     * @return the persisted entity
     */
    ChatDTO save(ChatDTO chatDTO);

    /**
     * Get all the chats.
     *
     * @return the list of entities
     */
    List<ChatDTO> findAll();


    /**
     * Get the "id" chat.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ChatDTO> findOne(Long id);

    /**
     * Delete the "id" chat.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Save proposed chats
     *
     * @param chats list of chats
     */
    void saveChats(List<Chat> chats);

    /**
     * Calculate coordinates of meting place
     *
     * @param chat the chat
     * @return Coordinates of meting lace (Latitude, Longitiude)
     */
    CoordinatesDTO calculatePlaceToMeet(Chat chat);

    /**
     * Accept the "id" userOfferChat
     * @param id the id of the entity
     */
    UserOfferChat accept(Long id);

    /**
     * Save message from user to chat
     *
     * @param chatId the "id" of chat
     * @param content the "content" of message
     * @return
     */
    Message sendMessage(Long chatId, String content);
}
