package bcn.hackupc.filler.repository;
import bcn.hackupc.filler.domain.Preference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Preference entity.
 */
@Repository
public interface PreferenceRepository extends JpaRepository<Preference, Long> {

    @Query(value = "select distinct preference from Preference preference left join fetch preference.users left join fetch preference.customEvents",
        countQuery = "select count(distinct preference) from Preference preference")
    Page<Preference> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct preference from Preference preference left join fetch preference.users left join fetch preference.customEvents")
    List<Preference> findAllWithEagerRelationships();

    @Query("select preference from Preference preference left join fetch preference.users left join fetch preference.customEvents where preference.id =:id")
    Optional<Preference> findOneWithEagerRelationships(@Param("id") Long id);

}
