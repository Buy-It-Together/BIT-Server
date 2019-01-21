package com.ujazdowski.buyitogether.repository;

import com.ujazdowski.buyitogether.domain.UserOfferChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;


/**
 * Spring Data  repository for the UserOfferChat entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserOfferChatRepository extends JpaRepository<UserOfferChat, Long> {
    // H2: select count(u.*) from  USER_OFFER_CHAT u where u.chat_id in (select uoc.chat_id from USER_OFFER_CHAT uoc where uoc.user_offer_id in (Select * from (VALUES (100), (101), (102), (104))) group by uoc.chat_id having count(uoc.chat_id) = 4)
    @Query("select case when (count(u) > 0)  then true else false end from UserOfferChat u where u.chat.id in (select uoc.chat.id from UserOfferChat uoc where uoc.userOffer.id in (:ids) group by uoc.chat.id having count(uoc.chat.id) = :size)")
    Boolean existsUserOfferChatsByUserOffers(@Param("ids") Set<Long> ids, @Param("size") Long size);
}
