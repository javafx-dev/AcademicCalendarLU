package academiccalendar.service;

import academiccalendar.model.DbRule;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
interface RuleRepository extends CrudRepository<DbRule, Long> {
}
