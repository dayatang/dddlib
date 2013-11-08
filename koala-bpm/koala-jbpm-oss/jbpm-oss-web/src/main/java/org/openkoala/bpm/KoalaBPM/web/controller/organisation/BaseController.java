package org.openkoala.bpm.KoalaBPM.web.controller.organisation;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.openkoala.organisation.application.BaseApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

/**
 * 基础Controller，提供一些通用的功能
 * @author xmfang
 *
 */
public class BaseController {

	@Autowired
	private BaseApplication baseApplication;

	BaseApplication getBaseApplication() {
		return baseApplication;
	}
	
	//数据绑定  
    @InitBinder    
    public void initBinder(WebDataBinder binder) {  
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");    
        dateFormat.setLenient(false);    
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));    
        //CustomDateEditor 可以换成自己定义的编辑器。  
        //注册一个Date 类型的绑定器 。  
    }   
	
}
