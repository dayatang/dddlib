package org.dayatang.dddlib.event.api;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * 事件处理器装载器。在指定的包中扫描事件处理器，并将其注册到事件总线。
 *
 * Created by yyang on 15/4/10.
 */
public class EventListenerLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventListenerLoader.class);

    //事件总线，用于注册事件监听器
    private EventBus eventBus;

    //包的数组，从这些包中寻找事件监听器，注册到事件总线
    private String[] packages;

    public EventListenerLoader(EventBus eventBus, String[] packages) {
        this.eventBus = eventBus;
        this.packages = packages;
    }

    /**
     * 在指定的包中扫描事件处理器，并将其注册到事件总线。
     */
    public void execute() {
        for (String each : packages) {
            Reflections reflections = new Reflections(each);
            Set<Class<? extends AbstractEventListener>> handlers = reflections.getSubTypesOf(AbstractEventListener.class);
            for (Class<? extends EventListener> handler : handlers) {
                try {
                    eventBus.register(handler.newInstance());
                } catch (InstantiationException e) {
                    e.printStackTrace();
                    LOGGER.error("Handler " + handler + " create failed!", e);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    LOGGER.error("Handler " + handler + " create failed!", e);
                }
            }
        }
    }
}
