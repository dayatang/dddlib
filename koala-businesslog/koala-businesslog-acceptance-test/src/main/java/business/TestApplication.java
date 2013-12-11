package business;

import java.util.Map;

public interface TestApplication {

    void testAdd(int i, String str);

    void testLogMethod(int arg1);

    void testAdd(TestApplication testApplication);

    void testAdd(Map<String, Object> map);

    void testAdd(String... args);

}
