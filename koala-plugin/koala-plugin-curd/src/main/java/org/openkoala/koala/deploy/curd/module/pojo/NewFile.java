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
package org.openkoala.koala.deploy.curd.module.pojo;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.openkoala.koala.pojo.MavenProject;

/**
 * 类    名：NewFile.java
 *   
 * 功能描述：生成的新文件的描述
 *  
 * 创建日期：2013-1-23下午5:06:52     
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
public abstract class NewFile {
    
    /*创建的新文件的名称*/
    protected String name;

    /*创建的项目所在的路径*/
    protected String projectPath;

    protected List<String> projectList;

    /*项目的包名*/
    private String packageName;

    private NewFileType type;

    public abstract void process();
    /**
     * @param name
     * @param projectPath
     * @param moduleName
     */
    public NewFile(String name, List<MavenProject> projects, NewFileType type) {
        super();
        this.name = name;
        this.projectPath = projects.get(0).getPath();
        this.packageName = projects.get(0).getProperties().get("base.package");
        if (projects.size() > 1) {
            projectList = new ArrayList<String>();
            for (MavenProject project : projects) {
                projectList.add(project.getPath());
            }
        }
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProjectPath() {
        return projectPath;
    }

    public void setProjectPath(String projectPath) {
        this.projectPath = projectPath;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
    
	
	public NewFileType getType() {
        return type;
    }
    /**
     * 获取文件路径
     * @return
     */
    protected String getPath() {
    	return MessageFormat.format("{0}/src/main/java/{1}/{2}.java", projectPath, //
                getPackageName().replaceAll("\\.", "/"), getName());
    }
    
    public String getPackagePath() {
    	String result = "";
    	String fullPath = getPath();
    	if (fullPath.startsWith(getProjectPath())) {
    		result = fullPath.replaceFirst(getProjectPath(), "");
    	}
    	return result;
    }
    
    public boolean isFileExists(){
        if(new File(getPath()).exists()){
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "CreateNewFile [name=" + name + ", projectPath=" + projectPath + ", packageName=" + packageName + "]";
    }
}
