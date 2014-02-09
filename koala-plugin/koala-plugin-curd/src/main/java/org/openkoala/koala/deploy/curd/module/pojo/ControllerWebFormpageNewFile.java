package org.openkoala.koala.deploy.curd.module.pojo;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

import org.apache.velocity.VelocityContext;
import org.openkoala.koala.action.velocity.VelocityUtil;
import org.openkoala.koala.deploy.curd.generator.VelocityContextUtils;
import org.openkoala.koala.deploy.curd.module.core.EntityModel;
import org.openkoala.koala.deploy.curd.module.ui.UIWidget;
import org.openkoala.koala.deploy.curd.module.ui.model.AddModel;
import org.openkoala.koala.deploy.curd.module.ui.model.EntityViewUI;
import org.openkoala.koala.deploy.curd.module.ui.model.UpdateModel;
import org.openkoala.koala.pojo.MavenProject;

/**
 * 生成SpringMVC增加、修改页面
 * @author zyb
 * @since 2013-7-29 下午2:13:04
 */
public class ControllerWebFormpageNewFile extends NewFile {

    private static final String TEMPLATE_PATH = "templates/controllerFormPageTemplate.vm";
    
    /**
     * 关联的领域实体模型
     */
    private EntityModel entityModel;
    
    /**
     * 新增模型
     */
    private AddModel addModel;
    
    /**
     * 更新模型
     */
    private UpdateModel updateModel;
    
    /**
     * @param name
     * @param projects
     * @param type
     */
    public ControllerWebFormpageNewFile(String name, List<MavenProject> projects,
            NewFileType type,EntityViewUI entityUI) {
        super(name, projects, type);
        addModel = entityUI.getAddModel();
        updateModel = entityUI.getUpdateModel();
        entityModel = entityUI.getEntityModel();
    }
    
    @Override
	public String getPath() {
        String targetPath = MessageFormat.format("{0}/src/main/webapp/pages/{1}/{2}-{3}", 
                projectPath,entityModel.getLastPackageName(),entityModel.getName(),getName());
        return targetPath;
	}
    
    /*
     *@see org.openkoala.koala.deploy.curd.module.pojo.NewFile#process()
     */
    @Override
    public void process() {
        VelocityContext context = VelocityContextUtils.getVelocityContext();
        context.put("pageParams", this);
        try {
            VelocityUtil.vmToFile(context, TEMPLATE_PATH, getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public EntityModel getEntityModel() {
        return entityModel;
    }

    public void setEntityModel(EntityModel entityModel) {
        this.entityModel = entityModel;
    }

    public AddModel getAddModel() {
        return addModel;
    }


    public void setAddModel(AddModel addModel) {
        this.addModel = addModel;
    }


    public UpdateModel getUpdateModel() {
        return updateModel;
    }


    public void setUpdateModel(UpdateModel updateModel) {
        this.updateModel = updateModel;
    }
    
    public List<UIWidget> getFormWidgets() {
        if(getIsAddForm()){
            return addModel.getViews();
        }else{
            return updateModel.getViews();
        }
    }
    
    public boolean getIsAddForm(){
        return NewFileType.WEB_ADD.equals(getType());
    }

}
