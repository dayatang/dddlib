package businesslog;

import freemarker.template.TemplateException;
import org.junit.Test;
import org.openkoala.businesslog.BusinessLogRender;
import org.openkoala.businesslog.impl.BusinessLogFreemarkerDefaultRender;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * User: zjzhai
 * Date: 11/29/13
 * Time: 3:36 PM
 */
public class BusinessLogRenderTest {


    @Test
    public void testRender() throws IOException, TemplateException {
        String template = "添加合同:${contractName}";
        Map<String, Object> context = new HashMap<String, Object>();
        context.put("contractName", "xx");
        BusinessLogRender render = new BusinessLogFreemarkerDefaultRender();
        assert "添加合同:xx".equals(render.render(context, template).build());
    }


}
