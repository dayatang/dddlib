package org.openkoala.exception.support.struts2.tags;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.opensymphony.xwork2.util.ValueStack;

public class JqueryModels {
  protected HeadModel          head;
  private ValueStack           stack;
  private HttpServletRequest   req;
  private HttpServletResponse  res;

  public JqueryModels(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
    this.stack = stack;
    this.req = req;
    this.res = res;
  }

  public HeadModel getHead()
  {
    if (head == null)
    {
      head = new HeadModel(stack, req, res);
    }

    return head;
  }

}

