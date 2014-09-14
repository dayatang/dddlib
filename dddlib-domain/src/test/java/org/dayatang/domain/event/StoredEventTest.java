package org.dayatang.domain.event;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.dayatang.utils.DateUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;

/**
 * Created by yyang on 14-9-14.
 */
public class StoredEventTest {

    private StoredEvent instance;

    private String typeName = DomainEventSub.class.getName();

    private DomainEventSub event = new DomainEventSub();

    @Mock
    private EventSerializer serializer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(serializer.deserialize("anEventBody", DomainEventSub.class)).thenReturn(event);
    }

    @Test
    public void constructorWithoutEventId() throws ClassNotFoundException {
        Date occurredOn = new Date();
        instance = new StoredEvent(typeName, occurredOn, "anEventBody");
        instance.setSerializer(serializer);
        assertThat(instance.getEventBody(), is("anEventBody"));
        assertThat(instance.getOccurredOn(), is(occurredOn));
        DomainEventSub theEvent = instance.toDomainEvent();
        assertThat(theEvent, is(event));
    }

    @Test
    public void constructorWithEventId() {
        Date occurredOn = new Date();
        instance = new StoredEvent(typeName, occurredOn, "anEventBody", "anId");
        instance.setSerializer(serializer);
        assertThat(instance.getEventBody(), is("anEventBody"));
        assertThat(instance.getOccurredOn(), is(occurredOn));
        assertThat(instance.getEventId(), is("anId"));
        DomainEventSub theEvent = instance.toDomainEvent();
        assertThat(theEvent, is(event));
    }

    @Test
    public void toDomainEvent() {
        Date occurredOn = DateUtils.date(2002, 4, 11);
        String eventBody = "{\"prop1\":\"abc\",\"prop2\":null,\"id\":\"anId\",\"occurredOn\":\"1018454400000\",\"version\":1}";
        event = new DomainEventSub(occurredOn, 1);
        event.setProp1("abc");
        instance = new StoredEvent(typeName, occurredOn, eventBody, "anId");
        DomainEventSub result = instance.toDomainEvent();
        assertThat(result.getOccurredOn(), is(event.getOccurredOn()));
        assertThat(result.getProp1(), is(event.getProp1()));
        assertThat(result.getVersion(), is(event.getVersion()));
        assertNull(result.getProp2());
    }

    @Test
    public void fromDomainEvent() {
        Date occurredOn = DateUtils.date(2002, 4, 11);
        event = new DomainEventSub(occurredOn, 1);
        event.setId("anId");
        event.setProp1("abc");
        when(serializer.serialize(event)).thenReturn("theEventBody");
        instance = StoredEvent.fromDomainEvent(event, serializer);
        assertThat(instance.getEventId(), is(event.getId()));
        assertThat(instance.getOccurredOn(), is(event.getOccurredOn()));
        assertThat(instance.getTypeName(), is(event.getClass().getName()));
        assertThat(instance.getEventBody(), is("theEventBody"));
    }
}
