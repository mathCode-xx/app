package cn.scut.app.mapper;

import cn.scut.app.entity.Topic;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
@Slf4j
public class ITopicMapperTest {
    @Autowired
    private ITopicMapper topicMapper;

    @Test
    void insertTest() {
        Topic topic = new Topic();
        topic.setContent("非常推荐预习高数使用：汤家凤高数0基础班，B站网址如下“https://www.bilibili.com/video/BV1wW4y1S7gd");
        topic.setTime(LocalDateTime.now());
        topic.setUserId("201930035227");
        topic.setTypeId(1);
        int count = topicMapper.insert(topic);
        log.info(""+count);
    }


}
