package bcn.hackupc.filler.service.mapper;

import bcn.hackupc.filler.domain.*;
import bcn.hackupc.filler.service.dto.PreferenceDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Preference} and its DTO {@link PreferenceDTO}.
 */
@Mapper(componentModel = "spring", uses = {PreferenceCategoryMapper.class, UserMapper.class, CustomEventMapper.class})
public interface PreferenceMapper extends EntityMapper<PreferenceDTO, Preference> {

    @Mapping(source = "preferenceCategory.id", target = "preferenceCategoryId")
    PreferenceDTO toDto(Preference preference);

    @Mapping(source = "preferenceCategoryId", target = "preferenceCategory")
    @Mapping(target = "removeUser", ignore = true)
    @Mapping(target = "removeCustomEvent", ignore = true)
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
