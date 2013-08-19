package org.openkoala.koala.widget;

import java.io.Serializable;

import org.openkoala.koala.annotation.ListFunctionCreate;
import org.openkoala.koala.annotation.ObjectFunctionCreate;

@ObjectFunctionCreate
public class Jsf implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8223649492988988987L;
	
	@ListFunctionCreate
	private boolean sample;

	public boolean isSample() {
		return sample;
	}

	public void setSample(boolean sample) {
		this.sample = sample;
	}

}
