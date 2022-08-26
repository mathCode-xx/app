package app.aspect;

import app.annotation.OptLog;
import app.context.UserHolder;
import app.entity.OptLogDTO;
import app.entity.R;
import app.entity.User;
import app.event.OptLogEvent;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;


@Aspect
@Slf4j
public class OptLogAspect {

    private static final ThreadLocal<OptLogDTO> THREAD_LOCAL = new ThreadLocal<>();
    @Autowired
    private ApplicationContext applicationContext;

    private OptLogDTO get() {
        OptLogDTO sysLog = THREAD_LOCAL.get();
        if (sysLog == null) {
            return new OptLogDTO();
        }
        return sysLog;
    }

    /***
     * 定义controller切入点拦截规则，拦截OptLog注解的方法
     */
    @Pointcut("@annotation(app.annotation.OptLog)")
    public void sysLogAspect() {

    }

    @Before(value = "sysLogAspect()")
    public void recordLog(JoinPoint joinPoint) {
        OptLogDTO optLogDTO = get();
        //log.info(UserHolder);

        User user = UserHolder.getUser();
        if (user != null) {
            optLogDTO.setOperatorId(user.getId());
        } else {
            optLogDTO.setOperatorId("未登录用户进行的操作");
        }
        optLogDTO.setDescription(getControllerMethodDescription(joinPoint));

        Object[] args = joinPoint.getArgs();

        String strArgs = "";
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        try {
            if (!request.getContentType().contains("multipart/form-data")) {
                strArgs = JSONUtil.toJsonStr(args);
            }
        } catch (Exception e) {
            try {
                strArgs = Arrays.toString(args);
            } catch (Exception ex) {
                log.warn("解析参数异常", ex);
            }
        }
        optLogDTO.setParams(getText(strArgs));

        THREAD_LOCAL.set(optLogDTO);
    }

    /**
     * 返回通知
     */
    @AfterReturning(returning = "ret", pointcut = "sysLogAspect()")
    public void doAfterReturning(Object ret) {
        R r = Convert.convert(R.class, ret);
        OptLogDTO optLogDTO = get();

        if (r.getStatusCode() == R.SUCCESS_CODE) {
            optLogDTO.setOptType(OptLogDTO.OPT_SUCCESS_MSG);
        } else {
            optLogDTO.setOptType(OptLogDTO.OPT_FAIL_MSG);
        }

        publishEvent(optLogDTO);
    }

    private void publishEvent(OptLogDTO optLogDTO) {
        optLogDTO.setOptTime(LocalDateTime.now());
        applicationContext.publishEvent(new OptLogEvent(optLogDTO));
        THREAD_LOCAL.remove();
    }

    public String getControllerMethodDescription(JoinPoint point) {
        try {
            // 获取连接点目标类名
            String targetName = point.getTarget().getClass().getName();
            // 获取连接点签名的方法名
            String methodName = point.getSignature().getName();
            //获取连接点参数
            Object[] args = point.getArgs();
            //根据连接点类的名字获取指定类
            Class targetClass = Class.forName(targetName);
            //获取类里面的方法
            Method[] methods = targetClass.getMethods();
            String description = "";
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    Class[] clazzs = method.getParameterTypes();
                    if (clazzs.length == args.length) {
                        description = method.getAnnotation(OptLog.class).value();
                        break;
                    }
                }
            }
            return description;
        } catch (Exception e) {
            return "";
        }
    }

    private String getText(String val) {
        return StrUtil.sub(val, 0, 65535);
    }
}
