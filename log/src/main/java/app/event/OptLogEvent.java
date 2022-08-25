package app.event;


import org.springframework.context.ApplicationEvent;

public class OptLogEvent extends ApplicationEvent {
    public OptLogEvent(Object source) {
        super(source);
    }
}
