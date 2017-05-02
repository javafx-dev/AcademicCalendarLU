package academiccalendar.service;

import academiccalendar.model.DbTerm;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
interface TermRepository extends CrudRepository<DbTerm, Long> {

    DbTerm findByName(String name);

    boolean existsByName(String name);
}
