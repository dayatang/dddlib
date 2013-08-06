package org.openkoala.koala.deploy.curd.generator; 

import java.util.List;

import org.apache.velocity.VelocityContext;
import org.openkoala.koala.deploy.curd.module.analysis.CURDCoreAnalysis;
import org.openkoala.koala.deploy.curd.module.analysis.CURDDefaultUIView;
import org.openkoala.koala.deploy.curd.module.analysis.CURDFileCreateAnalysis;
import org.openkoala.koala.deploy.curd.module.core.EntityModel;
import org.openkoala.koala.deploy.curd.module.pojo.NewFile;
import org.openkoala.koala.deploy.curd.module.ui.model.EntityViewUI;
import org.openkoala.koala.deploy.curd.module.util.CURDConfigUpdate;
import org.openkoala.koala.pojo.MavenProject;

/**
 * 
 * 类    名：CodeGenerator.java
 *   
 * 功能描述：代码生成器
 *  
 * 创建日期：2013-1-23上午11:24:54
 * 
 * 版本信息：
 * 
 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved
 * 
 * 作    者：Ken (rejoy2008@126.com)
 * 
 * 修改记录： 
 * 修 改 者    修改日期     文件版本   修改说明
 */
public class CodeGenerator {
    
    /**
     * 生成代码（VO、application、applicationImpl、页面）
     * @param javaPath
     * @throws Exception
     */
    public void generateCode(String javaPath) throws Exception {
        CURDCoreAnalysis util = CURDCoreAnalysis.getInstance();
        EntityModel entity = util.analysis(javaPath);
        VelocityContext context = VelocityContextUtils.getVelocityContext();
        context.put("entity", entity);
        EntityViewUI entityViewUI = CURDDefaultUIView.getDefaultEntityViewUI(entity);
        List<NewFile> createFiles = CURDFileCreateAnalysis.getCreateFileList(util.getProject(), entityViewUI);
        
        for (NewFile newFile : createFiles){
            newFile.process();
        }
    }
    
    @Deprecated
    public static void generateCode(EntityViewUI entityViewUI, MavenProject project) throws Exception {
    	VelocityContext context = VelocityContextUtils.getVelocityContext();
    	context.put("entity", entityViewUI.getEntityModel());
    	List<NewFile> createFiles = CURDFileCreateAnalysis.getCreateFileList(project, entityViewUI);
    	for (NewFile newFile : createFiles){
    		newFile.process();
    	}
    	//更新配置文件
    	CURDConfigUpdate.UpdateDBConfig(entityViewUI.getEntityModel(), project);
    }
    
    /**
     * 获取需要生产的JAVA文件列表
     * @param entityViewUI
     * @param project
     * @return
     */
    public static List<NewFile> getGenerateFiles(EntityViewUI entityViewUI, MavenProject project){
        VelocityContext context = VelocityContextUtils.getVelocityContext();
        context.put("entity", entityViewUI.getEntityModel());
        List<NewFile> createFiles = CURDFileCreateAnalysis.getCreateFileList(project, entityViewUI);
        return createFiles;
    }
    
    /**
     * 生成代码
     * @param entityViewUI
     * @param project
     * @param createFiles
     * @throws Exception
     */
    public static void generateCode(EntityViewUI entityViewUI, MavenProject project,List<NewFile> createFiles) throws Exception {
        for (NewFile newFile : createFiles){
            newFile.process();
        }
        //更新配置文件
        CURDConfigUpdate.UpdateDBConfig(entityViewUI.getEntityModel(), project);
        
        //MavenExcuter.runMaven(project.getPath());
    }
    

    
}
