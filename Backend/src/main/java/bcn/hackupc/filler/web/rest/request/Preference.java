package bcn.hackupc.filler.web.rest.request;

public class Preference {
        private String preferenceType;
    private String preference;

    public String getPreferenceType() {
        return preferenceType;
    }

    public void setPreferenceType(String preferenceType) {
        this.preferenceType = preferenceType;
    }

    public String getPreference() {
        return preference;
    }

    public void setPreference(String preference) {
        this.preference = preference;
    }

    @Override
    public String toString() {
        return "Preference{" +
            "preferenceType='" + preferenceType + '\'' +
            ", preference='" + preference + '\'' +
            '}';
    }
}
