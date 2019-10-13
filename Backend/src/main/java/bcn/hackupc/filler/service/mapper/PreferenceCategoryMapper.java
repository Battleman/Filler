package bcn.hackupc.filler.service.mapper;

import bcn.hackupc.filler.domain.*;
import bcn.hackupc.filler.service.dto.PreferenceCategoryDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link PreferenceCategory} and its DTO {@link PreferenceCategoryDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PreferenceCategoryMapper extends EntityMapper<PreferenceCategoryDTO, PreferenceCategory> {


    @Mapping(target = "preferences", ignore = true)
    @Mapping(target = "removePreference", ignore = true)
    PreferenceCategory toEntity(PreferenceCategoryDTO preferenceCategoryDTO);

    default PreferenceCategory fromId(Long id) {
        if (id == null) {
            return null;
        }
        PreferenceCategory preferenceCategory = new PreferenceCategory();
        preferenceCategory.setId(id);
        return preferenceCategory;
    }
}
