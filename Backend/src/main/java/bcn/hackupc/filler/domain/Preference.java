package bcn.hackupc.filler.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    @ManyToOne
    @JsonIgnoreProperties("preferences")
    private CustomEvent customEvent;

    @ManyToOne
    @JsonIgnoreProperties("preferences")
    private User user;

    @OneToMany(mappedBy = "preference")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PreferenceCategory> preferenceCategories = new HashSet<>();

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

    public CustomEvent getCustomEvent() {
        return customEvent;
    }

    public Preference customEvent(CustomEvent customEvent) {
        this.customEvent = customEvent;
        return this;
    }

    public void setCustomEvent(CustomEvent customEvent) {
        this.customEvent = customEvent;
    }

    public User getUser() {
        return user;
    }

    public Preference user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<PreferenceCategory> getPreferenceCategories() {
        return preferenceCategories;
    }

    public Preference preferenceCategories(Set<PreferenceCategory> preferenceCategories) {
        this.preferenceCategories = preferenceCategories;
        return this;
    }

    public Preference addPreferenceCategory(PreferenceCategory preferenceCategory) {
        this.preferenceCategories.add(preferenceCategory);
        preferenceCategory.setPreference(this);
        return this;
    }

    public Preference removePreferenceCategory(PreferenceCategory preferenceCategory) {
        this.preferenceCategories.remove(preferenceCategory);
        preferenceCategory.setPreference(null);
        return this;
    }

    public void setPreferenceCategories(Set<PreferenceCategory> preferenceCategories) {
        this.preferenceCategories = preferenceCategories;
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
