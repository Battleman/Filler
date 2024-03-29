package bcn.hackupc.filler.repository;
import bcn.hackupc.filler.domain.CustomEvent;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Spring Data  repository for the CustomEvent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomEventRepository extends JpaRepository<CustomEvent, Long> {

    List<CustomEvent> findAllByStartDateGreaterThanAndEndDateLessThan(ZonedDateTime startDate, ZonedDateTime endDate);

}
