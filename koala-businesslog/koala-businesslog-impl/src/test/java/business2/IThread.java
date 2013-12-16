package business2;

import com.dayatang.domain.InstanceFactory;

/**
 * User: zjzhai
 * Date: 12/16/13
 * Time: 11:14 AM
 */
public class IThread extends Thread {

    @Override
    public void run() {
        System.out.println("InstanceFactory.isInitialized() is " + InstanceFactory.isInitialized());
    }
}
