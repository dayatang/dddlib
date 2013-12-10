package org.openkoala.koala.dbinit;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openkoala.auth.application.ResourceApplication;

import com.dayatang.domain.InstanceFactory;

public class SecurityDBInit extends HttpServlet {


	private ResourceApplication resourceApplication;
	
	public ResourceApplication getResourceApplication(){
		if(resourceApplication==null){
			resourceApplication = InstanceFactory.getInstance(ResourceApplication.class);
		}
		return resourceApplication;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -268354658112935661L;

	
	private List<String> inits;
	
	private String type;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}

	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		getResourceApplication().initMenus(type,inits);
		//resp.sendRedirect(req.getContextPath()+"/pages/common/dbInit.jsp");
		PrintWriter out = resp.getWriter();
		out.write("success");
		out.flush();
		out.close();
	}
	
	private static final String INIT_ORGANIATION="organization";
	
	  public void init() throws ServletException{  
		  type = this.getInitParameter("type");   
		  inits = new ArrayList<String>();
		  if(this.getInitParameter(INIT_ORGANIATION)!=null){
			  inits.add(INIT_ORGANIATION);
		  }
		  
       }   
	
	
}
