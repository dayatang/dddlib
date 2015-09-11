package org.dayatang.dddlib.event.api;

/**
 * Created by yyang on 15/9/11.
 */
public class PostCreatedEventListener extends AbstractEventListener<PostCreatedEvent> {

    private int count = 0;

    public int getCount() {
        return count;
    }

    @Override
    public void handle(PostCreatedEvent event) {
        count ++;
    }
}
