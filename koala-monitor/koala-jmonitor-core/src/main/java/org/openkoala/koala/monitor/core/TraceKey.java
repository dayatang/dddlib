package org.openkoala.koala.monitor.core;


public interface TraceKey
{

    public abstract Object getInvokeKey();

    public abstract Object getThreadKey();
}
