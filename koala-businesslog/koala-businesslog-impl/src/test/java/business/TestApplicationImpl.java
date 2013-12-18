package business;

import business.TestApplication;

import java.util.Map;

public class TestApplicationImpl implements TestApplication {

    public void testAdd(int i, String str) {
        System.out.println("run the testLogMethod!!!" + i + "----" + str);

    }

    public void testLogMethod(int arg1) {
        System.out.println("run the testLogMethod!!!");
    }

    @Override
    public void testAdd(TestApplication testApplication) {
        System.out.println("run testApplication add method");
    }

    public void testAdd(Map<String, Object> map) {
        System.out.println("run testApplication addmap method");

    }

    @Override
    public void testAdd(String... args) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

}
