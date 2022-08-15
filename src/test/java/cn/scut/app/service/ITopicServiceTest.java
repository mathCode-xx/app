package cn.scut.app.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.scut.app.entity.Topic;
import cn.scut.app.mapper.ITopicMapper;
import cn.scut.app.service.impl.TopicServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@SpringBootTest
@Slf4j
public class ITopicServiceTest {

    @Resource
    ITopicMapper iTopicMapper;

    @Test
    public void relese() {

        Topic topic = new Topic();
        topic.setTime(LocalDateTime.now());
        topic.setUpdateTime(LocalDateTime.now());
        topic.setUserId("201930035228");
        topic.setTypeId(1);

        for (int i = 0; i < 1000; i++) {
            topic.setId(IdUtil.getSnowflakeNextId());
            topic.setContent(StrUtil.uuid());
            iTopicMapper.insert(topic);
        }
    }

}
