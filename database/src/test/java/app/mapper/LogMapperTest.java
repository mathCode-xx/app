package app.mapper;

import app.entity.OptLogDTO;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Select;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@Slf4j
public class LogMapperTest {
    @Resource
    ILogMapper logMapper;

    @Test
    public void insert() {
        for (int i = 0; i < 100; i++) {
            OptLogDTO optLogDTO = new OptLogDTO();
            optLogDTO.setId(IdUtil.getSnowflakeNextId());
            optLogDTO.setOptType(i % 2 == 0 ? "操作成功" : "操作失败");
            optLogDTO.setDescription(RandomUtil.randomString(6));
            optLogDTO.setParams(RandomUtil.randomString(10));
            optLogDTO.setOperatorId("201930035228");
            optLogDTO.setOptTime(LocalDateTime.now());
            logMapper.insert(optLogDTO);
        }
    }

    @Test
    public void select() {
        List<OptLogDTO> all = logMapper.findAll();
        for (OptLogDTO o :
                all) {
            log.info(o.toString());
        }
    }

}
