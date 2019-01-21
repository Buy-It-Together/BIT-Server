package com.ujazdowski.buyitogether.service.mapper;

import com.ujazdowski.buyitogether.domain.*;
import com.ujazdowski.buyitogether.service.dto.UserOfferDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity UserOffer and its DTO UserOfferDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface UserOfferMapper extends EntityMapper<UserOfferDTO, UserOffer> {

    @Mapping(source = "user.id", target = "userId")
    UserOfferDTO toDto(UserOffer userOffer);

    @Mapping(target = "userOfferChats", ignore = true)
    @Mapping(source = "userId", target = "user")
    UserOffer toEntity(UserOfferDTO userOfferDTO);

    default UserOffer fromId(Long id) {
        if (id == null) {
            return null;
        }
        UserOffer userOffer = new UserOffer();
        userOffer.setId(id);
        return userOffer;
    }
}
