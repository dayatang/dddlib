package org.openkoala.koala.jbpm.servlet;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.openkoala.jbpm.application.JBPMApplication;

import com.dayatang.domain.InstanceFactory;

public class ProcessImageServlet extends HttpServlet {

	private static final long serialVersionUID = 8246510620131052146L;

	private static final String CONTENT_TYPE = "image/gif";

	@Inject
	private JBPMApplication jbpmApplication;
	
	public JBPMApplication getJBPMApplication(){
		if(jbpmApplication==null){
			jbpmApplication = InstanceFactory.getInstance(JBPMApplication.class);
		}
		return jbpmApplication;
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			response.setContentType(CONTENT_TYPE);
			String processId = request
					.getParameter("processId");
			byte[] bytes = null;
			if(processId!=null){
			 bytes = getJBPMApplication().getProcessImage(processId);
			}else{
				//processInstanceId
				bytes = getJBPMApplication().getPorcessImageStream(Long.parseLong(request
					.getParameter("processInstanceId")));
			}
			
			response.setContentLength(bytes.length);
			ServletOutputStream op = response.getOutputStream();
			op.write(bytes);
			op.flush();
			op.close();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
