package academiccalendar.service;

import academiccalendar.model.DbColorGroup;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
interface ColorGroupRepository extends CrudRepository<DbColorGroup, Long> {
    DbColorGroup findByColor(String defaultColor);
}
