package bcn.hackupc.filler.service.dto;
import bcn.hackupc.filler.domain.Preference;

import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the {@link bcn.hackupc.filler.domain.CustomEvent} entity.
 */
public class CustomEventDTO implements Serializable {

    private Long id;

    private String name;

    private String subject;

    private Double price;

    private String latitude;

    private String longitude;

    private ZonedDateTime startDate;

    private ZonedDateTime endDate;

    private String description;

    private Boolean repeat;


    private Set<UserDTO> users = new HashSet<>();

    private Set<Preference> preferences = new HashSet<>();

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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public ZonedDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public ZonedDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isRepeat() {
        return repeat;
    }

    public void setRepeat(Boolean repeat) {
        this.repeat = repeat;
    }

    public Set<UserDTO> getUsers() {
        return users;
    }

    public void setUsers(Set<UserDTO> users) {
        this.users = users;
    }

    public Set<Preference> getPreferences() {
        return preferences;
    }

    public void setPreferences(Set<Preference> preferences) {
        this.preferences = preferences;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CustomEventDTO customEventDTO = (CustomEventDTO) o;
        if (customEventDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), customEventDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CustomEventDTO{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", subject='" + subject + '\'' +
            ", price=" + price +
            ", latitude='" + latitude + '\'' +
            ", longitude='" + longitude + '\'' +
            ", startDate=" + startDate +
            ", endDate=" + endDate +
            ", description='" + description + '\'' +
            ", repeat=" + repeat +
            ", users=" + users +
            ", preferences=" + preferences +
            '}';
    }
}
