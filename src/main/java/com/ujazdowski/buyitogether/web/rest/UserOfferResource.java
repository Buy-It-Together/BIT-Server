package com.ujazdowski.buyitogether.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ujazdowski.buyitogether.repository.UserOfferRepository;
import com.ujazdowski.buyitogether.service.UserOfferService;
import com.ujazdowski.buyitogether.service.UserService;
import com.ujazdowski.buyitogether.web.rest.errors.BadRequestAlertException;
import com.ujazdowski.buyitogether.web.rest.util.HeaderUtil;
import com.ujazdowski.buyitogether.web.rest.util.PaginationUtil;
import com.ujazdowski.buyitogether.service.dto.UserOfferDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing UserOffer.
 */
@RestController
@RequestMapping("/api")
public class UserOfferResource {

    private final Logger log = LoggerFactory.getLogger(UserOfferResource.class);

    private static final String ENTITY_NAME = "userOffer";

    private final UserOfferService userOfferService;

    private final UserService userService;

    public UserOfferResource(UserOfferService userOfferService, UserService userService) {
        this.userOfferService = userOfferService;
        this.userService = userService;
    }

    /**
     * POST  /user-offers : Create a new userOffer.
     *
     * @param userOfferDTO the userOfferDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userOfferDTO, or with status 400 (Bad Request) if the userOffer has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/user-offers")
    @Timed
    public ResponseEntity<UserOfferDTO> createUserOffer(@Valid @RequestBody UserOfferDTO userOfferDTO) throws URISyntaxException {
        log.debug("REST request to save UserOffer : {}", userOfferDTO);
        if (userOfferDTO.getId() != null) {
            throw new BadRequestAlertException("A new userOffer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        userOfferDTO.setUserId(userService.getUserWithAuthorities().orElseThrow(() -> new AuthenticationServiceException("User not Authenticated")).getId());
        UserOfferDTO result = userOfferService.save(userOfferDTO);
        return ResponseEntity.created(new URI("/api/user-offers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /user-offers : Updates an existing userOffer.
     *
     * @param userOfferDTO the userOfferDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userOfferDTO,
     * or with status 400 (Bad Request) if the userOfferDTO is not valid,
     * or with status 500 (Internal Server Error) if the userOfferDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/user-offers")
    @Timed
    public ResponseEntity<UserOfferDTO> updateUserOffer(@Valid @RequestBody UserOfferDTO userOfferDTO) throws URISyntaxException {
        log.debug("REST request to update UserOffer : {}", userOfferDTO);
        if (userOfferDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UserOfferDTO result = userOfferService.save(userOfferDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, userOfferDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /user-offers : get all the current user offers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of userOffers in body
     */
    @GetMapping("/user-offers")
    @Timed
    public List<UserOfferDTO>getAllCurrentUserOffers() {
        log.debug("REST request to get a page of UserOffers");
        return userOfferService.findByUserIsCurrentUser();
    }

    /**
     * GET  /user-offers/:id : get the "id" userOffer.
     *
     * @param id the id of the userOfferDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userOfferDTO, or with status 404 (Not Found)
     */
    @GetMapping("/user-offers/{id}")
    @Timed
    public ResponseEntity<UserOfferDTO> getUserOffer(@PathVariable Long id) {
        log.debug("REST request to get UserOffer : {}", id);
        Optional<UserOfferDTO> userOfferDTO = userOfferService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userOfferDTO);
    }

    /**
     * DELETE  /user-offers/:id : delete the "id" userOffer.
     *
     * @param id the id of the userOfferDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/user-offers/{id}")
    @Timed
    public ResponseEntity<Void> deleteUserOffer(@PathVariable Long id) {
        log.debug("REST request to delete UserOffer : {}", id);
        userOfferService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
