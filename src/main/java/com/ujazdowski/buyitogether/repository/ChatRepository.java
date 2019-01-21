package com.ujazdowski.buyitogether.repository;

import com.ujazdowski.buyitogether.domain.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;


/**
 * Spring Data  repository for the Chat entity.
 */
@SuppressWarnings("unused")
@Transactional
@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Query("select chat from Chat chat where :id in (select uoc.userOffer.id from UserOfferChat uoc where uoc.chat.id = chat.id)")
    Set<Chat> findByUserOfferId(@Param("id") Long userOfferId);

}
