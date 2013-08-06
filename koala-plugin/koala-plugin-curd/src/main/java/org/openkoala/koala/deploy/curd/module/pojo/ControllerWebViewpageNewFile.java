package org.openkoala.koala.deploy.curd.module.pojo;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.velocity.VelocityContext;
import org.openkoala.koala.action.velocity.VelocityUtil;
import org.openkoala.koala.deploy.curd.generator.VelocityContextUtils;
import org.openkoala.koala.deploy.curd.module.core.EntityModel;
import org.openkoala.koala.deploy.curd.module.ui.UIWidget;
import org.openkoala.koala.deploy.curd.module.ui.model.EntityViewUI;
import org.openkoala.koala.deploy.curd.module.ui.model.TabViewUI;
import org.openkoala.koala.deploy.curd.module.ui.model.ViewModel;
import org.openkoala.koala.pojo.MavenProject;

/**
 * 生成SpringMVC查看页面
 * @author zyb
 * @since 2013-7-29 下午2:10:54
 */
public class ControllerWebViewpageNewFile extends NewFile {

    private static final String TEMPLATE_PATH = "templates/controllerViewPageTemplate.vm";
    
    /**
     * 关联的领域实体模型
     */
    private EntityModel entityModel;
    
    /**
     * VIEW模型
     */
    private ViewModel viewModel;
    
    /**
     * @param name
     * @param projects
     * @param type
     */
    public ControllerWebViewpageNewFile(String name, List<MavenProject> projects,
            NewFileType type,EntityViewUI entityUI) {
        super(name, projects, type);
        viewModel = entityUI.getViewModel();
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

    public ViewModel getViewModel() {
        return viewModel;
    }

    public void setViewModel(ViewModel viewModel) {
        this.viewModel = viewModel;
    }
    
    public List<UIWidget> getTab1Widgets(){
        List<UIWidget> ws = viewModel.getViews();
        if(ws == null)ws = new ArrayList<UIWidget>();
        return ws;
    }
    
    public List<TabViewUI> getTabs(){
        List<TabViewUI> tabs = viewModel.getTabs();
        if(tabs == null)tabs = new ArrayList<TabViewUI>();
        return tabs;
    }
    
    public boolean getHaveTab(){
        return getTabs().size()>0;
    }
}
