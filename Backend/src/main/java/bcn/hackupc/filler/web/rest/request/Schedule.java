package bcn.hackupc.filler.web.rest.request;

import java.time.LocalDateTime;

public class Schedule {

    private LocalDateTime start;

    private LocalDateTime end;
    private Boolean recurring;
    private Location location;

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public Boolean getRecurring() {
        return recurring;
    }

    public void setRecurring(Boolean recurring) {
        this.recurring = recurring;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Schedule{" +
            "start=" + start +
            ", end=" + end +
            ", recurring=" + recurring +
            ", location=" + location +
            '}';
    }
}
