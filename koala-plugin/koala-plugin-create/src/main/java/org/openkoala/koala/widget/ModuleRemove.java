package org.openkoala.koala.widget;

import java.io.Serializable;
import java.util.List;
/**
 * 删除功能XML对应的POJO解析类
 * @author lingen.liu
 *
 */
public class ModuleRemove implements Serializable {

	private static final long serialVersionUID = 6113484633874313300L;

	private String funType;
	
	private List<String> files;

	public String getFunType() {
		return funType;
	}

	public void setFunType(String funType) {
		this.funType = funType;
	}

	public List<String> getFiles() {
		return files;
	}

	public void setFiles(List<String> files) {
		this.files = files;
	}
	
}
