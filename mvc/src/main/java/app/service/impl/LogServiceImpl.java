package app.service.impl;

import app.context.UserHolder;
import app.entity.OptLogDTO;
import app.entity.R;
import app.entity.User;
import app.mapper.ILogMapper;
import app.service.ILogService;
import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class LogServiceImpl implements ILogService {
    @Resource
    ILogMapper logMapper;

    public void saveLog(OptLogDTO optLogDTO){
        //此处只是将日志信息进行输出，实际项目中可以将日志信息保存到数据库
        optLogDTO.setId(IdUtil.getSnowflakeNextId());
        logMapper.insert(optLogDTO);
    }

    @Override
    public R findAllLogs() {
        //只有系统管理员可以查看操作日志
        if (UserHolder.getUser().getPermission() != User.SYSTEM_MANAGER) {
            return R.fail("权限不足");
        }
        List<OptLogDTO> all = logMapper.findAll();
        return R.success().addData("logs", all);
    }
}
