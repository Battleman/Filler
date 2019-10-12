package bcn.hackupc.filler.service.dto;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the {@link bcn.hackupc.filler.domain.Preference} entity.
 */
public class PreferenceDTO implements Serializable {

    private Long id;

    private String name;


    private Long preferenceCategoryId;

    private Set<UserDTO> users = new HashSet<>();

    private Set<CustomEventDTO> customEvents = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPreferenceCategoryId() {
        return preferenceCategoryId;
    }

    public void setPreferenceCategoryId(Long preferenceCategoryId) {
        this.preferenceCategoryId = preferenceCategoryId;
    }

    public Set<UserDTO> getUsers() {
        return users;
    }

    public void setUsers(Set<UserDTO> users) {
        this.users = users;
    }

    public Set<CustomEventDTO> getCustomEvents() {
        return customEvents;
    }

    public void setCustomEvents(Set<CustomEventDTO> customEvents) {
        this.customEvents = customEvents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PreferenceDTO preferenceDTO = (PreferenceDTO) o;
        if (preferenceDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), preferenceDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PreferenceDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", preferenceCategory=" + getPreferenceCategoryId() +
            "}";
    }
}
