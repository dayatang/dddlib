package org.openkoala.koala.annotation.parse;

import java.util.List;

public interface Parse {

	void initParms(List params,String name,Object fieldVal);
	
	void process() throws Exception;
}
