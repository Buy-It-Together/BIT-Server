package com.ujazdowski.buyitogether.service.impl;

import com.ujazdowski.buyitogether.domain.UserOffer;
import com.ujazdowski.buyitogether.repository.UserOfferRepository;
import com.ujazdowski.buyitogether.service.UserOfferService;
import com.ujazdowski.buyitogether.service.dto.UserOfferDTO;
import com.ujazdowski.buyitogether.service.mapper.UserOfferMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing UserOffer.
 */
@Service
@Transactional
public class UserOfferServiceImpl implements UserOfferService {

    private final Logger log = LoggerFactory.getLogger(UserOfferServiceImpl.class);

    private UserOfferRepository userOfferRepository;

    private UserOfferMapper userOfferMapper;

    public UserOfferServiceImpl(UserOfferRepository userOfferRepository, UserOfferMapper userOfferMapper) {
        this.userOfferRepository = userOfferRepository;
        this.userOfferMapper = userOfferMapper;
    }

    /**
     * Save a userOffer.
     *
     * @param userOfferDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public UserOfferDTO save(UserOfferDTO userOfferDTO) {
        log.debug("Request to save UserOffer : {}", userOfferDTO);

        UserOffer userOffer = userOfferMapper.toEntity(userOfferDTO);
        userOffer = userOfferRepository.save(userOffer);
        return userOfferMapper.toDto(userOffer);
    }

    /**
     * Get all the userOffers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UserOfferDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserOffers");
        return userOfferRepository.findAll(pageable)
            .map(userOfferMapper::toDto);
    }


    /**
     * Get one userOffer by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<UserOfferDTO> findOne(Long id) {
        log.debug("Request to get UserOffer : {}", id);
        return userOfferRepository.findById(id)
            .map(userOfferMapper::toDto);
    }

    /**
     * Delete the userOffer by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserOffer : {}", id);
        userOfferRepository.deleteById(id);
    }

    @Override
    public List<UserOfferDTO> findByUserIsCurrentUser() {
        return userOfferRepository.findByUserIsCurrentUser().stream().map(userOfferMapper::toDto).collect(Collectors.toList());
    }
}
