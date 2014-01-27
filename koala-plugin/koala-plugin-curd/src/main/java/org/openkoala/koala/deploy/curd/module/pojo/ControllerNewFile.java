package org.openkoala.koala.deploy.curd.module.pojo; 

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

import org.apache.velocity.VelocityContext;
import org.openkoala.koala.action.velocity.VelocityUtil;
import org.openkoala.koala.deploy.curd.generator.VelocityContextUtils;
import org.openkoala.koala.deploy.curd.module.core.EntityModel;
import org.openkoala.koala.deploy.curd.module.ui.model.EntityViewUI;
import org.openkoala.koala.pojo.MavenProject;

/**
 * 创建Controller类
 * @author zyb
 * @since 2013-07-29 10:05
 *
 */
public class ControllerNewFile extends NewFile {
	
	private static final String TEMPLATE_PATH = "templates/controllerTemplate.vm";
	
	private EntityModel entityModel;

	public ControllerNewFile(String name, List<MavenProject> projects, NewFileType type, EntityViewUI entityViewUI) {
		super(name, projects, type);
		entityModel = entityViewUI.getEntityModel();
	}

    @Override
	public String getPath() {
		String[] temparr = entityModel.getClassName().split("\\.");
        String lastPackageName = temparr[temparr.length - 2];
        String targetPath = MessageFormat.format("{0}/src/main/java/{1}/{2}/{3}.java", projectPath, //
                getPackageName().replaceAll("\\.", "/"), lastPackageName, getName());
        return targetPath;
	}
	
    @Override
    public String getPackageName() {
    	return super.getPackageName() + ".controller";
    }
    
	@Override
	public void process() {
        VelocityContext context = VelocityContextUtils.getVelocityContext();
        context.put("controllerClass", this);
        try {
            VelocityUtil.vmToFile(context, TEMPLATE_PATH, getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

}
