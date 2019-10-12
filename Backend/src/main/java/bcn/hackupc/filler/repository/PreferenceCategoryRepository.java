package bcn.hackupc.filler.repository;
import bcn.hackupc.filler.domain.PreferenceCategory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the PreferenceCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PreferenceCategoryRepository extends JpaRepository<PreferenceCategory, Long> {

}
