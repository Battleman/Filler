package bcn.hackupc.filler.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link bcn.hackupc.filler.domain.Preference} entity.
 */
public class PreferenceDTO implements Serializable {

    private Long id;

    private String name;


    private Long customEventId;

    private Long userId;

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

    public Long getCustomEventId() {
        return customEventId;
    }

    public void setCustomEventId(Long customEventId) {
        this.customEventId = customEventId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
            ", customEvent=" + getCustomEventId() +
            ", user=" + getUserId() +
            "}";
    }
}
