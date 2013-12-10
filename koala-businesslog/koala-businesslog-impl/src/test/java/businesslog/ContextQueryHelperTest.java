package businesslog;

import business.Project;
import org.junit.Ignore;
import org.junit.Test;
import org.openkoala.businesslog.common.ContextQueryHelper;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: zjzhai
 * Date: 12/10/13
 * Time: 2:32 PM
 */
public class ContextQueryHelperTest {

    private String methodSignature = "findContractById(long,business.Project,String,double)";
    private String methodSignature1 = "findContractById(long)";
    private String methodSignature2 = "findContractById()";
    private String methodSignature3 = "findContractById(long,business.Project,String,double,boolean,float)";


    @Test
    public void testGetMethodParamTypes() throws Exception {
        List<String> result = new ArrayList<String>();
        result.add("long");
        result.add("business.Project");
        result.add("String");
        result.add("double");
        assert result.equals(
                ContextQueryHelper.getMethodParamTypes(methodSignature));


        List<String> result1 = new ArrayList<String>();
        result1.add("long");
        assert result1.equals(
                ContextQueryHelper.getMethodParamTypes(methodSignature1));


        List<String> result2 = new ArrayList<String>();
        assert result2.equals(
                ContextQueryHelper.getMethodParamTypes(methodSignature2));
    }

    @Test
    public void testGetMethodName() {
        assert "findContractById".equals(ContextQueryHelper.getMethodName(methodSignature));
        assert "findContractById".equals(ContextQueryHelper.getMethodName(methodSignature1));
        assert "findContractById".equals(ContextQueryHelper.getMethodName(methodSignature2));
    }

    @Test
    public void testgetMethodParamClasses() {

        List<Class> result = new ArrayList<Class>();
        result.add(long.class);
        result.add(Project.class);
        result.add(String.class);
        result.add(double.class);
        result.add(boolean.class);
        result.add(float.class);

        assert result.equals(
                Arrays.asList(
                        ContextQueryHelper.getMethodParamClasses(
                                ContextQueryHelper.getMethodParamTypes(methodSignature3))));
    }

    @Ignore
    @Test
    public void testgetMethodInstanceOf() throws ClassNotFoundException {
        Method method = ContextQueryHelper.getMethodInstanceOf("business.ContractApplicationImpl", "findByContractIdAndProject", ContextQueryHelper.getMethodParamClasses(
                ContextQueryHelper.getMethodParamTypes("findByContractIdAndProject(long, business.Project)")));
       assert method.getName().equals("findByContractIdAndProject");
    }


}

