package bcn.hackupc.filler.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A PreferenceCategory.
 */
@Entity
@Table(name = "preference_category")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PreferenceCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "preferenceCategory")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Preference> preferences = new HashSet<>();

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

    public PreferenceCategory name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Preference> getPreferences() {
        return preferences;
    }

    public PreferenceCategory preferences(Set<Preference> preferences) {
        this.preferences = preferences;
        return this;
    }

    public PreferenceCategory addPreference(Preference preference) {
        this.preferences.add(preference);
        preference.setPreferenceCategory(this);
        return this;
    }

    public PreferenceCategory removePreference(Preference preference) {
        this.preferences.remove(preference);
        preference.setPreferenceCategory(null);
        return this;
    }

    public void setPreferences(Set<Preference> preferences) {
        this.preferences = preferences;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PreferenceCategory)) {
            return false;
        }
        return id != null && id.equals(((PreferenceCategory) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "PreferenceCategory{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
