package org.openkoala.koala.deploy.curd.module.pojo;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

import org.apache.velocity.VelocityContext;
import org.openkoala.koala.action.velocity.VelocityUtil;
import org.openkoala.koala.deploy.curd.generator.VelocityContextUtils;
import org.openkoala.koala.deploy.curd.module.core.EntityModel;
import org.openkoala.koala.deploy.curd.module.ui.model.EntityViewUI;
import org.openkoala.koala.deploy.curd.module.ui.model.ListModel;
import org.openkoala.koala.deploy.curd.module.ui.model.QueryModel;
import org.openkoala.koala.pojo.MavenProject;

/**
 * 生成SpringMVC的列表
 * @author zyb
 * @since 2013-7-29 下午2:06:10
 */
public class ControllerWebListpageNewFile extends NewFile {

    private static final String TEMPLATE_PATH = "templates/controllerListPageTemplate.vm";
    
    /**
     * 关联的领域实体模型
     */
    private EntityModel entityModel;
    
    /**
     * 当前实体的查询模型
     */
    private QueryModel queryModel;
    
    /**
     * List模型
     */
    private ListModel listModel;
    
    /**
     * @param name
     * @param projects
     * @param type
     */
    public ControllerWebListpageNewFile(String name, List<MavenProject> projects,
            NewFileType type,EntityViewUI entityUI) {
        super(name, projects, type);
        queryModel = entityUI.getQueryModel();
        listModel = entityUI.getListModel();
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


    public QueryModel getQueryModel() {
        return queryModel;
    }


    public void setQueryModel(QueryModel queryModel) {
        this.queryModel = queryModel;
    }


    public ListModel getListModel() {
        return listModel;
    }


    public void setListModel(ListModel listModel) {
        this.listModel = listModel;
    }
    
}
