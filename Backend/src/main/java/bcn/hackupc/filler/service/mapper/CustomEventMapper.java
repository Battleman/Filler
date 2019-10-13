package bcn.hackupc.filler.service.mapper;

import bcn.hackupc.filler.domain.*;
import bcn.hackupc.filler.service.dto.CustomEventDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link CustomEvent} and its DTO {@link CustomEventDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface CustomEventMapper extends EntityMapper<CustomEventDTO, CustomEvent> {


    @Mapping(target = "removeUser", ignore = true)
    CustomEvent toEntity(CustomEventDTO customEventDTO);

    default CustomEvent fromId(Long id) {
        if (id == null) {
            return null;
        }
        CustomEvent customEvent = new CustomEvent();
        customEvent.setId(id);
        return customEvent;
    }
}
