package app.mapper;

import app.entity.Topic;
import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
@Slf4j
public class TopicMapperTest {

    @Resource
    ITopicMapper topicMapper;

    @Test
    public void select() {
        List<Topic> allTopic = topicMapper.getAllTopic();
        if (CollectionUtil.isEmpty(allTopic)) {
            return;
        }
        log.info(allTopic.get(0).toString());
    }

}
