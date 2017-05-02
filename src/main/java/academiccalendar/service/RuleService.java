package academiccalendar.service;

import academiccalendar.model.DbRule;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RuleService {

    @Autowired
    private RuleRepository ruleRepository;

    public DbRule findById(Long ruleId) {
        return ruleRepository.findOne(ruleId);
    }

    public void update(DbRule dbRule) {
        ruleRepository.save(dbRule);
    }

    public List<DbRule> getAllRules() {
        return Lists.newArrayList(ruleRepository.findAll());
    }

    public void delete(Long dbRuleId) {
        ruleRepository.delete(dbRuleId);
    }

    public void save(DbRule rule) {
        ruleRepository.save(rule);
    }
}
