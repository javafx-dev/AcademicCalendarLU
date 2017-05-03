package academiccalendar.service;

import academiccalendar.model.DbColorGroup;
import com.google.common.collect.Lists;
import javafx.scene.paint.Color;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ColorGroupService {

    @Autowired
    private ColorGroupRepository colorGroupRepository;

    public List<DbColorGroup> findAll() {
        return Lists.newArrayList(colorGroupRepository.findAll());
    }

    public void updateColor(Long groupId, Color color) {
        DbColorGroup group = colorGroupRepository.findOne(groupId);
        group.setColor(getRGB(color));
        colorGroupRepository.save(group);
    }

    private String getRGB(Color c) {
        return Integer.toString((int) (c.getRed() * 255)) + "-"
                + Integer.toString((int) (c.getGreen() * 255)) + "-"
                + Integer.toString((int) (c.getBlue() * 255));
    }

    @Transactional
    public void save(String name, Color color) {
        DbColorGroup dbColorGroup = new DbColorGroup();
        dbColorGroup.setName(name);
        dbColorGroup.setColor(getRGB(color));
        dbColorGroup.setVisible(true);
        colorGroupRepository.save(dbColorGroup);
    }
}
