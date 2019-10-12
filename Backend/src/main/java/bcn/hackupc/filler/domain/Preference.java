package bcn.hackupc.filler.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Preference.
 */
@Entity
@Table(name = "preference")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Preference implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToOne
    @JoinColumn(unique = true)
    private PreferenceCategory preferenceCategory;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "preference_user",
               joinColumns = @JoinColumn(name = "preference_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private Set<User> users = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "preference_custom_event",
               joinColumns = @JoinColumn(name = "preference_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "custom_event_id", referencedColumnName = "id"))
    private Set<CustomEvent> customEvents = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Preference name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PreferenceCategory getPreferenceCategory() {
        return preferenceCategory;
    }

    public Preference preferenceCategory(PreferenceCategory preferenceCategory) {
        this.preferenceCategory = preferenceCategory;
        return this;
    }

    public void setPreferenceCategory(PreferenceCategory preferenceCategory) {
        this.preferenceCategory = preferenceCategory;
    }

    public Set<User> getUsers() {
        return users;
    }

    public Preference users(Set<User> users) {
        this.users = users;
        return this;
    }

    public Preference addUser(User user) {
        this.users.add(user);
        return this;
    }

    public Preference removeUser(User user) {
        this.users.remove(user);
        return this;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Set<CustomEvent> getCustomEvents() {
        return customEvents;
    }

    public Preference customEvents(Set<CustomEvent> customEvents) {
        this.customEvents = customEvents;
        return this;
    }

    public Preference addCustomEvent(CustomEvent customEvent) {
        this.customEvents.add(customEvent);
        customEvent.getPreferences().add(this);
        return this;
    }

    public Preference removeCustomEvent(CustomEvent customEvent) {
        this.customEvents.remove(customEvent);
        customEvent.getPreferences().remove(this);
        return this;
    }

    public void setCustomEvents(Set<CustomEvent> customEvents) {
        this.customEvents = customEvents;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Preference)) {
            return false;
        }
        return id != null && id.equals(((Preference) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Preference{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
