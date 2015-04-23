package org.dayatang.dddlib.event;

import com.google.common.reflect.TypeToken;

/**
 * 抽象事件处理器
 * @param <T> 事件类型
 */
public abstract class AbstractEventListener<T extends DomainEvent> implements EventListener<T> {

    //获取事件类型
    @SuppressWarnings("serial")
    private final TypeToken<T> token = new TypeToken<T>(getClass()) {};

    public boolean supports(DomainEvent event) {
        return token.isAssignableFrom(event.getClass());
    }

    @Override
    public void onEvent(DomainEvent event) {
        if (!supports(event)) {
            return;
        }
        handle((T) event);
    }

    /**
     * 处理事件
     * @param event 要处理的事件
     */
    public abstract void handle(T event);
}
