package app.config;

import app.event.OptLogListener;
import app.service.impl.LogServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogConfig {
    //自动配置日志监听器组件
    @Bean
    @ConditionalOnMissingBean
    public OptLogListener sysLogListener(LogServiceImpl logServiceImpl){
        return new OptLogListener(logServiceImpl::saveLog);
    }
}
