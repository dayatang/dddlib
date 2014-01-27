package businesslog;

import static org.openkoala.businesslog.common.ContextQueryHelper.*;

import business.Contract;
import business.Project;
import org.junit.Test;
import org.openkoala.businesslog.BusinessLogClassNotFoundException;
import org.openkoala.businesslog.common.ContextQueryHelper;

import java.util.*;

/**
 * User: zjzhai
 * Date: 12/10/13
 * Time: 2:32 PM
 */
public class ContextQueryHelperTest {

    private String methodSignature = "findContractById(long,business.Project,String,double)";
    private String methodSignature1 = "findContractById(long)";
    private String methodSignature2 = "findContractById()";
    private String methodSignature3 =
            "findContractById(long,business.Project,String,double,boolean,float,business.Project[])";


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


        assert List.class.equals(ContextQueryHelper.getMethodParamClass("List"));
        assert Map.class.equals(ContextQueryHelper.getMethodParamClass("Map"));
        assert Set.class.equals(ContextQueryHelper.getMethodParamClass("Set"));
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
        result.add(Project[].class);


        assert result.equals(
                Arrays.asList(
                        ContextQueryHelper.getMethodParamClass(
                                ContextQueryHelper.getMethodParamTypes(methodSignature3))));
    }


    @Test
    public void testcontextQueryArgConvertStringToObject() {
        assert new Long(1).equals(
                ContextQueryHelper.contextQueryArgConvertStringToObject("1", Long.class, null));

        Map<String, Object> context = new HashMap<String, Object>();

        context.put("project", new Project("projectName"));
        context.put("float", 0.2f);

        assert new Project("projectName").
                equals(
                        ContextQueryHelper.contextQueryArgConvertStringToObject("${project}",
                                Project.class, context));

        assert "projectName".equals(
                ContextQueryHelper.contextQueryArgConvertStringToObject("${project.name}",
                        String.class, context));

        assert null == contextQueryArgConvertStringToObject("${contact}", Contract.class, context);

        assert 0.2f == (Float) contextQueryArgConvertStringToObject("${float}", float.class, context);
        assert 0.2f == (Float) contextQueryArgConvertStringToObject("0.2", float.class, context);


    }


    @Test(expected = BusinessLogClassNotFoundException.class)
    public void testgetMethodInstanceOf() {
        String methodSignature4 = "findContractById(business.xx)";
        assert new ArrayList<Class>().equals(
                Arrays.asList(
                        ContextQueryHelper.getMethodParamClass(
                                ContextQueryHelper.getMethodParamTypes(methodSignature4))));
    }


}

