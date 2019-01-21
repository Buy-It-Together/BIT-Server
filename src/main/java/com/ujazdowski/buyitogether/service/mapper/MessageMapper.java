package com.ujazdowski.buyitogether.service.mapper;

import com.ujazdowski.buyitogether.domain.*;
import com.ujazdowski.buyitogether.service.dto.MessageDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Message and its DTO MessageDTO.
 */
@Mapper(componentModel = "spring", uses = {ChatMapper.class, UserMapper.class})
public interface MessageMapper extends EntityMapper<MessageDTO, Message> {

    @Mapping(source = "chat.id", target = "chatId")
    @Mapping(source = "user.id", target = "userId")
    MessageDTO toDto(Message message);

    @Mapping(source = "chatId", target = "chat")
    @Mapping(source = "userId", target = "user")
    Message toEntity(MessageDTO messageDTO);

    default Message fromId(Long id) {
        if (id == null) {
            return null;
        }
        Message message = new Message();
        message.setId(id);
        return message;
    }
}
