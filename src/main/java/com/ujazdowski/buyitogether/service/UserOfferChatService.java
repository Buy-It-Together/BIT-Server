package com.ujazdowski.buyitogether.service;

import com.ujazdowski.buyitogether.service.dto.UserOfferChatDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing UserOfferChat.
 */
public interface UserOfferChatService {

    /**
     * Save a userOfferChat.
     *
     * @param userOfferChatDTO the entity to save
     * @return the persisted entity
     */
    UserOfferChatDTO save(UserOfferChatDTO userOfferChatDTO);

    /**
     * Get all the userOfferChats.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<UserOfferChatDTO> findAll(Pageable pageable);


    /**
     * Get the "id" userOfferChat.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<UserOfferChatDTO> findOne(Long id);

    /**
     * Delete the "id" userOfferChat.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
