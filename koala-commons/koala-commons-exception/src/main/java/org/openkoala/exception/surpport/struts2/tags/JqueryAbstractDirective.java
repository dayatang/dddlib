package org.openkoala.exception.support.struts2.tags;

import org.apache.struts2.views.velocity.components.AbstractDirective;

/**
 * Overwrite name prefix
 * 
 */
public abstract class JqueryAbstractDirective extends AbstractDirective {
  public String getName()
  {
    return "sj" + getBeanName();
  }
}