package com.ujazdowski.buyitogether.service.impl;

import com.ujazdowski.buyitogether.domain.UserOfferChat;
import com.ujazdowski.buyitogether.repository.UserOfferChatRepository;
import com.ujazdowski.buyitogether.service.UserOfferChatService;
import com.ujazdowski.buyitogether.service.dto.UserOfferChatDTO;
import com.ujazdowski.buyitogether.service.mapper.UserOfferChatMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

/**
 * Service Implementation for managing UserOfferChat.
 */
@Service
@Transactional
public class UserOfferChatServiceImpl implements UserOfferChatService {

    private final Logger log = LoggerFactory.getLogger(UserOfferChatServiceImpl.class);

    private UserOfferChatRepository userOfferChatRepository;

    private UserOfferChatMapper userOfferChatMapper;

    public UserOfferChatServiceImpl(UserOfferChatRepository userOfferChatRepository, UserOfferChatMapper userOfferChatMapper) {
        this.userOfferChatRepository = userOfferChatRepository;
        this.userOfferChatMapper = userOfferChatMapper;
    }

    /**
     * Save a userOfferChat.
     *
     * @param userOfferChatDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public UserOfferChatDTO save(UserOfferChatDTO userOfferChatDTO) {
        log.debug("Request to save UserOfferChat : {}", userOfferChatDTO);

        UserOfferChat userOfferChat = userOfferChatMapper.toEntity(userOfferChatDTO);
        userOfferChat = userOfferChatRepository.save(userOfferChat);
        return userOfferChatMapper.toDto(userOfferChat);
    }

    /**
     * Get all the userOfferChats.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UserOfferChatDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserOfferChats");
        return userOfferChatRepository.findAll(pageable)
            .map(userOfferChatMapper::toDto);
    }


    /**
     * Get one userOfferChat by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<UserOfferChatDTO> findOne(Long id) {
        log.debug("Request to get UserOfferChat : {}", id);
        return userOfferChatRepository.findById(id)
            .map(userOfferChatMapper::toDto);
    }

    /**
     * Delete the userOfferChat by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserOfferChat : {}", id);
        userOfferChatRepository.deleteById(id);
    }
}
