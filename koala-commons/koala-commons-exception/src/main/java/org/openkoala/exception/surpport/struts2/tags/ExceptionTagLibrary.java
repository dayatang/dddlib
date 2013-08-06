package org.openkoala.exception.surpport.struts2.tags;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.views.TagLibrary;
import org.apache.struts2.views.velocity.components.HeadDirective;

import com.opensymphony.xwork2.util.ValueStack;

public class ExceptionTagLibrary implements TagLibrary {

	  public Object getFreemarkerModels(ValueStack stack, HttpServletRequest req, HttpServletResponse res)
	  {
	    return new JqueryModels(stack, req, res);
	  }

	  @SuppressWarnings("unchecked")
	  public List<Class> getVelocityDirectiveClasses()
	  {
	    Class[] directives = new Class[] {
	        HeadDirective.class
	    };
	    return Arrays.asList(directives);
	  }
}

