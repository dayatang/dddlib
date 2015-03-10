package org.dayatang.domain.event;

/**
 * Created by yyang on 15/2/10.
 */
public interface EventListener {

    /**
     * Returns {@code true} if the listener instance can process the specified event object, {@code false} otherwise.
     * @param event the event object to test
     * @return {@code true} if the listener instance can process the specified event object, {@code false} otherwise.
     */
    boolean accepts(DomainEvent event);

    /**
     * The class of event
     * @return
     */
    Class getEventType();

    /**
     * Handles the specified event.  Again, as this interface is an implementation concept, implementations of this
     * method will likely dispatch the event to a 'real' processor (e.g. method).
     *
     * @param event the event to handle.
     */
    void onEvent(DomainEvent event);

}
