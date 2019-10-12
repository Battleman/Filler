package bcn.hackupc.filler.service.mapper;

import bcn.hackupc.filler.domain.*;
import bcn.hackupc.filler.service.dto.PreferenceDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Preference} and its DTO {@link PreferenceDTO}.
 */
@Mapper(componentModel = "spring", uses = {CustomEventMapper.class, UserMapper.class})
public interface PreferenceMapper extends EntityMapper<PreferenceDTO, Preference> {

    @Mapping(source = "customEvent.id", target = "customEventId")
    @Mapping(source = "user.id", target = "userId")
    PreferenceDTO toDto(Preference preference);

    @Mapping(source = "customEventId", target = "customEvent")
    @Mapping(source = "userId", target = "user")
    @Mapping(target = "preferenceCategories", ignore = true)
    @Mapping(target = "removePreferenceCategory", ignore = true)
    Preference toEntity(PreferenceDTO preferenceDTO);

    default Preference fromId(Long id) {
        if (id == null) {
            return null;
        }
        Preference preference = new Preference();
        preference.setId(id);
        return preference;
    }
}
