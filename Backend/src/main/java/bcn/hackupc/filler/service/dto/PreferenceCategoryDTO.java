package bcn.hackupc.filler.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link bcn.hackupc.filler.domain.PreferenceCategory} entity.
 */
public class PreferenceCategoryDTO implements Serializable {

    private Long id;

    private String name;


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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PreferenceCategoryDTO preferenceCategoryDTO = (PreferenceCategoryDTO) o;
        if (preferenceCategoryDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), preferenceCategoryDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PreferenceCategoryDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
