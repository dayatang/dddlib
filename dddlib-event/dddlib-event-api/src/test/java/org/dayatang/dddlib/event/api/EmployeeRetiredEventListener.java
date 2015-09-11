package org.dayatang.dddlib.event.api;

/**
 * Created by yyang on 15/9/11.
 */
public class EmployeeRetiredEventListener extends AbstractEventListener<EmployeeRetiredEvent> {

    private int count = 0;

    public int getCount() {
        return count;
    }

    @Override
    public void handle(EmployeeRetiredEvent event) {
        count ++;
    }
}
