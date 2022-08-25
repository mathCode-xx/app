package app.event;

import app.entity.OptLogDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;

import java.util.function.Consumer;

@Slf4j
@AllArgsConstructor
public class OptLogListener {
    private Consumer<OptLogDTO> consumer;

    @Async
    @Order
    @EventListener(OptLogEvent.class)
    public void saveSysLog(OptLogEvent event) {
        OptLogDTO optLog = (OptLogDTO) event.getSource();
        consumer.accept(optLog);
    }
}
