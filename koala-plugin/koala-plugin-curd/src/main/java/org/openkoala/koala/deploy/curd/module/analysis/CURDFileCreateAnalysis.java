/*
 * Copyright (c) OpenKoala 2011 All Rights Reserved
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.openkoala.koala.deploy.curd.module.analysis;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openkoala.koala.deploy.curd.module.core.EntityModel;
import org.openkoala.koala.deploy.curd.module.core.model.RelationFieldModel;
import org.openkoala.koala.deploy.curd.module.pojo.ActionNewFile;
import org.openkoala.koala.deploy.curd.module.pojo.ApplicationNewFile;
import org.openkoala.koala.deploy.curd.module.pojo.ControllerNewFile;
import org.openkoala.koala.deploy.curd.module.pojo.ControllerWebFormpageNewFile;
import org.openkoala.koala.deploy.curd.module.pojo.ControllerWebListpageNewFile;
import org.openkoala.koala.deploy.curd.module.pojo.ControllerWebViewpageNewFile;
import org.openkoala.koala.deploy.curd.module.pojo.ImplNewFile;
import org.openkoala.koala.deploy.curd.module.pojo.NewFile;
import org.openkoala.koala.deploy.curd.module.pojo.NewFileType;
import org.openkoala.koala.deploy.curd.module.pojo.DTONewFile;
import org.openkoala.koala.deploy.curd.module.pojo.WebFormpageNewFile;
import org.openkoala.koala.deploy.curd.module.pojo.WebListpageNewFile;
import org.openkoala.koala.deploy.curd.module.pojo.WebViewpageNewFile;
import org.openkoala.koala.deploy.curd.module.ui.UIWidget;
import org.openkoala.koala.deploy.curd.module.ui.model.AddModel;
import org.openkoala.koala.deploy.curd.module.ui.model.EntityViewUI;
import org.openkoala.koala.deploy.curd.module.ui.model.ListModel;
import org.openkoala.koala.deploy.curd.module.ui.model.QueryModel;
import org.openkoala.koala.deploy.curd.module.ui.model.TabModel;
import org.openkoala.koala.deploy.curd.module.ui.model.TabViewUI;
import org.openkoala.koala.deploy.curd.module.ui.model.UpdateModel;
import org.openkoala.koala.deploy.curd.module.ui.model.ViewModel;
import org.openkoala.koala.deploy.curd.module.ui.view.RelativeReadOnlyView;
import org.openkoala.koala.exception.KoalaException;
import org.openkoala.koala.pojo.MavenProject;
import org.openkoala.koala.pojo.ModuleType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类    名：CURDFileCreateAnalysis.java
 *   
 * 功能描述：根据领域对象实体，分析产生的
 *  
 * 创建日期：2013-1-23下午3:31:31     
 * 
 * 版本信息：
 * 
 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved
 * 
 * 作    者：lingen(lingen.liu@gmail.com)
 * 
 * 修改记录： 
 * 修 改 者    修改日期     文件版本   修改说明	
 */
public class CURDFileCreateAnalysis {
    
    private static final Logger logger = LoggerFactory.getLogger(CURDFileCreateAnalysis.class);
    
    public static List<NewFile> getCreateFileList(MavenProject project,EntityViewUI entityUI){
        List<NewFile> newFiles = new ArrayList<NewFile>();
        List<MavenProject> applicationProjects = new ArrayList<MavenProject>();
        List<MavenProject> implProjects = new ArrayList<MavenProject>();
        List<MavenProject> webProject = new ArrayList<MavenProject>();
        
        addChildProject(project, applicationProjects, implProjects, webProject);
        handleFault(applicationProjects, implProjects, webProject);
        createSelfDtoFile(project, entityUI, entityUI.getEntityModel(), newFiles, applicationProjects);
        createRelationDtoFile(project, entityUI, entityUI.getEntityModel(), newFiles, applicationProjects);
        createApplicationFile(entityUI, entityUI.getEntityModel(), newFiles, applicationProjects);
        createApplicationImplFile(entityUI, entityUI.getEntityModel(), newFiles, implProjects);
        createActionIfNeed(project, entityUI, entityUI.getEntityModel(), newFiles, webProject);
        createControllerIfNeed(project, entityUI, entityUI.getEntityModel(), newFiles, webProject);
        
        return newFiles;
    }

    /**
     * 添加子项目 
     * @param project
     * @param applicationProjects
     * @param implProjects
     * @param webProject
     */
	private static void addChildProject(MavenProject project,
			List<MavenProject> applicationProjects,
			List<MavenProject> implProjects, List<MavenProject> webProject) {
		for (MavenProject child : project.getChilds()) {
            if (child.getType().equals(ModuleType.Application)) {
            	applicationProjects.add(child);
            }
            if (child.getType().equals(ModuleType.Impl)) {
            	implProjects.add(child);
            }
            if (child.getType().equals(ModuleType.War)) {
            	webProject.add(child);
            }
        }
	}

    /**
     * 处理错误
     * @param applicationProjects
     * @param implProjects
     * @param webProject
     */
	private static void handleFault(List<MavenProject> applicationProjects, 
			List<MavenProject> implProjects, List<MavenProject> webProject) {
		if (applicationProjects.size() == 0) {
        	throw new KoalaException("找不到应用层接口");
        }
        
        if (implProjects.size() == 0) {
        	throw new KoalaException("找不到应用层实现");
        }
        
        if (webProject.size() == 0) {
        	throw new KoalaException("找不到Web项目");
        }
	}

    /**
     * 创建Controller的Java文件 
     * @param project
     * @param entityUI
     * @param entity
     * @param newFiles
     * @param webProject
     */
	private static void createControllerIfNeed(MavenProject project,
			EntityViewUI entityUI, EntityModel entity, List<NewFile> newFiles,
			List<MavenProject> webProject) {
		if ("SpringMVC".equals(project.getMvcType())) {
        	newFiles.add(new ControllerNewFile(MessageFormat.format("{0}Controller", entity.getName()),  //
        			webProject, NewFileType.WEB_CONTROLLER, entityUI));
        	newFiles.add(new ControllerWebListpageNewFile("list.jsp", webProject,NewFileType.WEB_LIST, entityUI));
        	newFiles.add(new ControllerWebViewpageNewFile("view.jsp", webProject,NewFileType.WEB_VIEW, entityUI));
        	newFiles.add(new ControllerWebFormpageNewFile("add.jsp", webProject,NewFileType.WEB_ADD, entityUI));
        	newFiles.add(new ControllerWebFormpageNewFile("update.jsp", webProject, NewFileType.WEB_MODIFY, entityUI));
        }
	}

    /**
     * 创建Action的Java文件 
     * @param project
     * @param entityUI
     * @param entity
     * @param newFiles
     * @param webProject
     */
	private static void createActionIfNeed(MavenProject project,
			EntityViewUI entityUI, EntityModel entity, List<NewFile> newFiles,
			List<MavenProject> webProject) {
		if ("Struts2MVC".equals(project.getMvcType())) {
	        newFiles.add(new ActionNewFile(MessageFormat.format("{0}Action", entity.getName()),  //
	        		webProject, NewFileType.WEB_ACTION, entityUI));
	        
	        //分析需要产生哪些页面
	        newFiles.add(new WebListpageNewFile("list.jsp", webProject,NewFileType.WEB_LIST, entityUI));
	        newFiles.add(new WebViewpageNewFile("view.jsp", webProject,NewFileType.WEB_VIEW, entityUI));
	        newFiles.add(new WebFormpageNewFile("add.jsp", webProject,NewFileType.WEB_ADD, entityUI));
	        newFiles.add(new WebFormpageNewFile("update.jsp", webProject, NewFileType.WEB_MODIFY, entityUI));
        }
	}

    /**
     * 创建ApplicationImpl的Java文件 
     * @param entityUI
     * @param entity
     * @param newFiles
     * @param implProjects
     */
	private static void createApplicationImplFile(EntityViewUI entityUI,
			EntityModel entity, List<NewFile> newFiles,
			List<MavenProject> implProjects) {
		ImplNewFile impl = new ImplNewFile(getApplicationImplClassName(entity), implProjects,NewFileType.IMPL, entity, entityUI);
        impl.setReliations(getRelationModel(entityUI));
        newFiles.add(impl);
	}

    /**
     * 创建Application的Java文件
     * @param entityUI
     * @param entity
     * @param newFiles
     * @param applicationProjects
     */
	private static void createApplicationFile(EntityViewUI entityUI,
			EntityModel entity, List<NewFile> newFiles,
			List<MavenProject> applicationProjects) {
		ApplicationNewFile file = new ApplicationNewFile(getApplicationClassName(entity), applicationProjects, NewFileType.APPLICATION, entity);
        file.setRelations(getRelationModel(entityUI));
        newFiles.add(file);
	}

    /**
     * 获取ApplicationImpl的类名
     * @param entity
     * @return
     */
	private static String getApplicationImplClassName(EntityModel entity) {
		return entity.getName() + "ApplicationImpl";
	}

    /**
     * 获取Application的类名
     * @param entity
     * @return
     */
	private static String getApplicationClassName(EntityModel entity) {
		return entity.getName() + "Application";
	}

    /**
     * 创建关联对象的DTO文件
     * @param project
     * @param entityUI
     * @param entity
     * @param newFiles
     * @param applicationProjects
     */
	private static void createRelationDtoFile(MavenProject project,
			EntityViewUI entityUI, EntityModel entity, List<NewFile> newFiles,
			List<MavenProject> applicationProjects) {
		for(RelationFieldModel reliation:entity.getRelationFieldModel()){
            if (reliation.getRelationModel().getEntityModel().getName().equals(entity.getName())) {
            	continue;
            }
            DTONewFile relationDtoNewFile = new DTONewFile(getRelationDtoClassName(reliation), applicationProjects, NewFileType.DTO, project, entity);
            for (TabViewUI ui:entityUI.getViewModel().getTabs()) {
                if (ui.getRelationFieldModel().equals(reliation)) {
                	relationDtoNewFile.setFieldMap(getVoParams(ui.getListModel()));
                }
            }
            newFiles.add(relationDtoNewFile);
        }
	}

    /**
     * 获取关联属性DTO的类名
     * @param reliation
     * @return
     */
	private static String getRelationDtoClassName(RelationFieldModel reliation) {
		return reliation.getRelationModel().getEntityModel().getName() + "DTO";
	}

    /**
     * 创建自身的DTO
     * @param project
     * @param entityUI
     * @param entity
     * @param newFiles
     * @param applicationProjects
     */
	private static void createSelfDtoFile(MavenProject project,
			EntityViewUI entityUI, EntityModel entity, List<NewFile> newFiles,
			List<MavenProject> applicationProjects) {
		DTONewFile dtoNewFile = new DTONewFile(getDtoClassName(entity), applicationProjects,NewFileType.DTO, project, entity);
        dtoNewFile.setFieldMap(getVoParams(entityUI, dtoNewFile.getRelativeTypes()));
        newFiles.add(dtoNewFile);
	}

    /**
     * 获取DTO的类名
     * @param entity
     * @return
     */
	private static String getDtoClassName(EntityModel entity) {
		return entity.getName() + "DTO";
	}
    
    private static List<RelationFieldModel> getRelationModel(EntityViewUI entityUI){
        List<RelationFieldModel>  relations = new ArrayList<RelationFieldModel>();
        List<TabViewUI> tabs = entityUI.getViewModel().getTabs();
        for(TabViewUI tab:tabs){
            relations.add(tab.getRelationFieldModel());
        }
        return relations;
    }
    
    private static Map<String,String> getVoParams(EntityViewUI entityUI, List<String> relativeTypes){
        Map<String,String> params = new HashMap<String,String>();
        QueryModel queryModel  = entityUI.getQueryModel();
        for(UIWidget view:queryModel.getViews()){
            UIWidget widget = (UIWidget)view;
            params.put(widget.getExpress(), widget.getType());
        }
        ListModel listModel = entityUI.getListModel();
        for(UIWidget view:listModel.getViews()){
            if (view instanceof RelativeReadOnlyView){
                RelativeReadOnlyView relativeView = (RelativeReadOnlyView)view;
                params.put(relativeView.getRelative() + relativeView.getExpress().substring(0,1).toUpperCase() + relativeView.getExpress().substring(1), relativeView.getType());
                relativeTypes.add(relativeView.getRelative() + relativeView.getExpress().substring(0,1).toUpperCase() + relativeView.getExpress().substring(1));
            } else {
                UIWidget widget = (UIWidget)view;
                params.put(widget.getExpress(), widget.getType());
            }
        }
        
        AddModel addModel = entityUI.getAddModel();
        for(UIWidget view:addModel.getViews()){
            UIWidget widget = (UIWidget)view;
            params.put(widget.getExpress(), widget.getType());
        }
        
        UpdateModel updateModel = entityUI.getUpdateModel();
        for(UIWidget view:updateModel.getViews()){
            UIWidget widget = (UIWidget)view;
            params.put(widget.getExpress(), widget.getType());
        }
        
        ViewModel viewMoel = entityUI.getViewModel();
        for(UIWidget view:viewMoel.getViews()){
            if(view instanceof RelativeReadOnlyView){
                RelativeReadOnlyView relativeView = (RelativeReadOnlyView)view;
                params.put(relativeView.getRelative()+relativeView.getExpress().substring(0,1).toUpperCase()+relativeView.getExpress().substring(1), relativeView.getType());
            }else{
                UIWidget widget = (UIWidget)view;
                params.put(widget.getExpress(), widget.getType());
            }
        }
        logger.info(params.toString());
        return params;
    }
    
    private static Map<String,String> getVoParams(TabModel tabModel){
        Map<String,String> params = new HashMap<String,String>();
        for(UIWidget view:tabModel.getViews()){
            if(view instanceof RelativeReadOnlyView){
                RelativeReadOnlyView relativeView = (RelativeReadOnlyView)view;
                params.put(relativeView.getRelative().toLowerCase()+relativeView.getExpress().substring(0,1).toUpperCase()+relativeView.getExpress().substring(1), relativeView.getType());
            }else{
                UIWidget widget = (UIWidget)view;
                params.put(widget.getExpress(), widget.getType());
            }
        }
        logger.info(params.toString());
        return params;
    }
    
}