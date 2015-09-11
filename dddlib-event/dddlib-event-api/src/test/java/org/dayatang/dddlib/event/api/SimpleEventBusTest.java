package org.dayatang.dddlib.event.api;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.StrictAssertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


/**
 * Created by yyang on 15/9/11.
 */
public class SimpleEventBusTest {

    private SimpleEventBus instance;

    @Mock
    private EventStore eventStore;

    private EmployeeRetiredEventListener employeeRetiredEventListener = new EmployeeRetiredEventListener();

    private PostCreatedEventListener postCreatedEventListener = new PostCreatedEventListener();

    @Mock
    private EventListener eventListener3;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        instance = new SimpleEventBus(eventStore);
    }

    @Test
    public void test() throws Exception {
        EmployeeRetiredEvent employeeRetired = new EmployeeRetiredEvent(new Date(), 1);
        PostCreatedEvent postCreated = new PostCreatedEvent(new Date(), 2);

        instance.register(employeeRetiredEventListener, postCreatedEventListener);
        assertThat(employeeRetiredEventListener.getCount()).isEqualTo(0);
        assertThat(postCreatedEventListener.getCount()).isEqualTo(0);

        instance.post(employeeRetired);
        assertThat(employeeRetiredEventListener.getCount()).isEqualTo(1);
        assertThat(postCreatedEventListener.getCount()).isEqualTo(0);
        verify(eventStore, times(1)).store(employeeRetired);

        instance.post(postCreated);
        assertThat(employeeRetiredEventListener.getCount()).isEqualTo(1);
        assertThat(postCreatedEventListener.getCount()).isEqualTo(1);
        verify(eventStore, times(1)).store(postCreated);

        instance.unregister(employeeRetiredEventListener, postCreatedEventListener);
        instance.post(postCreated);
        instance.post(employeeRetired);
        assertThat(employeeRetiredEventListener.getCount()).isEqualTo(1);
        assertThat(postCreatedEventListener.getCount()).isEqualTo(1);
        verify(eventStore, times(2)).store(employeeRetired);
        verify(eventStore, times(2)).store(postCreated);
    }

    @Test
    public void createWithListeners() {
        List<EventListener> listeners = new ArrayList<EventListener>();
        listeners.add(employeeRetiredEventListener);
        listeners.add(postCreatedEventListener);
        instance = new SimpleEventBus(eventStore, listeners);
        assertThat(instance.getListeners()).isEqualTo(listeners);
    }
}