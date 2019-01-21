package com.ujazdowski.buyitogether.service.mapper;

import com.ujazdowski.buyitogether.domain.*;
import com.ujazdowski.buyitogether.service.dto.ChatDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Chat and its DTO ChatDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ChatMapper extends EntityMapper<ChatDTO, Chat> {


    @Mapping(target = "users", ignore = true)
    @Mapping(target = "messages", ignore = true)
    Chat toEntity(ChatDTO chatDTO);

    default Chat fromId(Long id) {
        if (id == null) {
            return null;
        }
        Chat chat = new Chat();
        chat.setId(id);
        return chat;
    }
}
