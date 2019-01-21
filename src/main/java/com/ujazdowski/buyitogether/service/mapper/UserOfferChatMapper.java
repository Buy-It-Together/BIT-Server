package com.ujazdowski.buyitogether.service.mapper;

import com.ujazdowski.buyitogether.domain.*;
import com.ujazdowski.buyitogether.service.dto.UserOfferChatDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity UserOfferChat and its DTO UserOfferChatDTO.
 */
@Mapper(componentModel = "spring", uses = {ChatMapper.class, UserOfferMapper.class})
public interface UserOfferChatMapper extends EntityMapper<UserOfferChatDTO, UserOfferChat> {

    @Mapping(source = "chat.id", target = "chatId")
    @Mapping(source = "userOffer.id", target = "userOfferId")
    UserOfferChatDTO toDto(UserOfferChat userOfferChat);

    @Mapping(source = "chatId", target = "chat")
    @Mapping(source = "userOfferId", target = "userOffer")
    UserOfferChat toEntity(UserOfferChatDTO userOfferChatDTO);

    default UserOfferChat fromId(Long id) {
        if (id == null) {
            return null;
        }
        UserOfferChat userOfferChat = new UserOfferChat();
        userOfferChat.setId(id);
        return userOfferChat;
    }
}
