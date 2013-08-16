package org.jwebap.plugin.tracer.jdbc;

import java.sql.Connection;

public interface ConnectionEventListener 
{
   
   /**
    * @param conn
    * @roseuid 45597DA5001F
    */
   public void fire(Connection conn);
}
