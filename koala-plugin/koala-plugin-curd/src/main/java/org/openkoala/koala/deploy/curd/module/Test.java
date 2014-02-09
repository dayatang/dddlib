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
package org.openkoala.koala.deploy.curd.module;

import java.util.List;

import org.openkoala.koala.deploy.curd.module.analysis.CURDCoreAnalysis;
import org.openkoala.koala.deploy.curd.module.analysis.CURDDefaultUIView;
import org.openkoala.koala.deploy.curd.module.analysis.CURDFileCreateAnalysis;
import org.openkoala.koala.deploy.curd.module.core.EntityModel;
import org.openkoala.koala.deploy.curd.module.pojo.NewFile;
import org.openkoala.koala.deploy.curd.module.ui.model.EntityViewUI;

/**
 * 类    名：Test.java
 *   
 * 功能描述：具体功能做描述。	
 *  
 * 创建日期：2013-1-23下午6:45:34     
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
public class Test {
    /**
     * @param args
     */
    public static void main(String[] args) {
        //第一步，选中领域对象，获得到此领域对象的绝对路径
        String path = "G:/project/foss-project/src/KoalaSecurity/core/src/main/java/org/openkoala/koala/auth/core/domain/User.java";
        //分析获得到此领域对象的建模
        CURDCoreAnalysis util = CURDCoreAnalysis.getInstance();
        EntityModel entity = util.analysis(path);
        
        //分析获得到此领域对象的默认UI显示
        EntityViewUI entityViewUI = CURDDefaultUIView.getDefaultEntityViewUI(entity);
       
        //分析获取到此领域对象需要生成的对象列表
        List<NewFile> createFiles = CURDFileCreateAnalysis.getCreateFileList(util.getProject(), entityViewUI);
        
        for(NewFile newFile:createFiles){
            newFile.process();
        }
        //下一步
        //根据UI显示，及对象列表结果，进行生成
        //TODO 生成VO列表
        
        //TODO 生成APPLICATION接口
        
        //TODO 生成IMPL实现
        
        //TODO 生成页面
        
        //FINISHED
    }
}
