package com.ujazdowski.buyitogether.repository;

import com.ujazdowski.buyitogether.domain.UserOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * Spring Data  repository for the UserOffer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserOfferRepository extends JpaRepository<UserOffer, Long> {

    @Query("select user_offer from UserOffer user_offer where user_offer.user.login = ?#{principal.username}")
    List<UserOffer> findByUserIsCurrentUser();

    @Query("select uo from UserOffer uo where 0 = (select count(c) from UserOfferChat c where c.userOffer = uo.id and c.accepted = TRUE)")
    List<UserOffer> findUserOffersWithNotAcceptedChat();

}
