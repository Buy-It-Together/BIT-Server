package com.ujazdowski.buyitogether.service;

import com.ujazdowski.buyitogether.service.dto.UserOfferDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing UserOffer.
 */
public interface UserOfferService {

    /**
     * Save a userOffer.
     *
     * @param userOfferDTO the entity to save
     * @return the persisted entity
     */
    UserOfferDTO save(UserOfferDTO userOfferDTO);

    /**
     * Get all the userOffers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<UserOfferDTO> findAll(Pageable pageable);


    /**
     * Get the "id" userOffer.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<UserOfferDTO> findOne(Long id);

    /**
     * Delete the "id" userOffer.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Get the current user offers
     *
     * @return the entities
     */
    List<UserOfferDTO> findByUserIsCurrentUser();
}
