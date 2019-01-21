package com.ujazdowski.buyitogether.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ujazdowski.buyitogether.service.UserOfferChatService;
import com.ujazdowski.buyitogether.web.rest.errors.BadRequestAlertException;
import com.ujazdowski.buyitogether.web.rest.util.HeaderUtil;
import com.ujazdowski.buyitogether.web.rest.util.PaginationUtil;
import com.ujazdowski.buyitogether.service.dto.UserOfferChatDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing UserOfferChat.
 */
@RestController
@RequestMapping("/api")
public class UserOfferChatResource {

    private final Logger log = LoggerFactory.getLogger(UserOfferChatResource.class);

    private static final String ENTITY_NAME = "userOfferChat";

    private UserOfferChatService userOfferChatService;

    public UserOfferChatResource(UserOfferChatService userOfferChatService) {
        this.userOfferChatService = userOfferChatService;
    }

    /**
     * POST  /user-offer-chats : Create a new userOfferChat.
     *
     * @param userOfferChatDTO the userOfferChatDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userOfferChatDTO, or with status 400 (Bad Request) if the userOfferChat has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/user-offer-chats")
    @Timed
    public ResponseEntity<UserOfferChatDTO> createUserOfferChat(@Valid @RequestBody UserOfferChatDTO userOfferChatDTO) throws URISyntaxException {
        log.debug("REST request to save UserOfferChat : {}", userOfferChatDTO);
        if (userOfferChatDTO.getId() != null) {
            throw new BadRequestAlertException("A new userOfferChat cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserOfferChatDTO result = userOfferChatService.save(userOfferChatDTO);
        return ResponseEntity.created(new URI("/api/user-offer-chats/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /user-offer-chats : Updates an existing userOfferChat.
     *
     * @param userOfferChatDTO the userOfferChatDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userOfferChatDTO,
     * or with status 400 (Bad Request) if the userOfferChatDTO is not valid,
     * or with status 500 (Internal Server Error) if the userOfferChatDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/user-offer-chats")
    @Timed
    public ResponseEntity<UserOfferChatDTO> updateUserOfferChat(@Valid @RequestBody UserOfferChatDTO userOfferChatDTO) throws URISyntaxException {
        log.debug("REST request to update UserOfferChat : {}", userOfferChatDTO);
        if (userOfferChatDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UserOfferChatDTO result = userOfferChatService.save(userOfferChatDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, userOfferChatDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /user-offer-chats : get all the userOfferChats.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of userOfferChats in body
     */
    @GetMapping("/user-offer-chats")
    @Timed
    public ResponseEntity<List<UserOfferChatDTO>> getAllUserOfferChats(Pageable pageable) {
        log.debug("REST request to get a page of UserOfferChats");
        Page<UserOfferChatDTO> page = userOfferChatService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/user-offer-chats");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /user-offer-chats/:id : get the "id" userOfferChat.
     *
     * @param id the id of the userOfferChatDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userOfferChatDTO, or with status 404 (Not Found)
     */
    @GetMapping("/user-offer-chats/{id}")
    @Timed
    public ResponseEntity<UserOfferChatDTO> getUserOfferChat(@PathVariable Long id) {
        log.debug("REST request to get UserOfferChat : {}", id);
        Optional<UserOfferChatDTO> userOfferChatDTO = userOfferChatService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userOfferChatDTO);
    }

    /**
     * DELETE  /user-offer-chats/:id : delete the "id" userOfferChat.
     *
     * @param id the id of the userOfferChatDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/user-offer-chats/{id}")
    @Timed
    public ResponseEntity<Void> deleteUserOfferChat(@PathVariable Long id) {
        log.debug("REST request to delete UserOfferChat : {}", id);
        userOfferChatService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
