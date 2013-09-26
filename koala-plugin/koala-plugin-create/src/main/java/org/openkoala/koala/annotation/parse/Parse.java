package org.openkoala.koala.annotation.parse;

import java.util.List;

public interface Parse {

	public void initParms(List params,String name,Object fieldVal);
	
	public void process() throws Exception;
}
