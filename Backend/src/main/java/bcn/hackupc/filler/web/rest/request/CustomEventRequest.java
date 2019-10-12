package bcn.hackupc.filler.web.rest.request;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CustomEventRequest {

    private List<Preference> preferences = new ArrayList<>();

    private LocalDateTime startDate;

    private LocalDateTime endDate;
    private List<Schedule> schedule = new ArrayList<>();

    public List<Preference> getPreferences() {
        return preferences;
    }

    public void setPreferences(List<Preference> preferences) {
        this.preferences = preferences;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public List<Schedule> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<Schedule> schedule) {
        this.schedule = schedule;
    }

    @Override
    public String toString() {
        return "CustomEventRequest{" +
            "preferences=" + preferences +
            ", startDate=" + startDate +
            ", endDate=" + endDate +
            ", schedules=" + schedule +
            '}';
    }
}
