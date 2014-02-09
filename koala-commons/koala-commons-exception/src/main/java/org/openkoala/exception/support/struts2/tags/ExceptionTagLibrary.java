package org.openkoala.exception.support.struts2.tags;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.views.DefaultTagLibrary;
import org.apache.struts2.views.TagLibraryDirectiveProvider;
import org.apache.struts2.views.TagLibraryModelProvider;
import org.apache.struts2.views.velocity.components.HeadDirective;

import com.opensymphony.xwork2.util.ValueStack;

public class ExceptionTagLibrary implements TagLibraryDirectiveProvider, TagLibraryModelProvider {

    @Override
    public List<Class> getDirectiveClasses() {
        return Arrays.asList(new Class[] {HeadDirective.class});
    }

    @Override
    public Object getModels(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
        return new JqueryModels(stack, req, res);
    }
}

