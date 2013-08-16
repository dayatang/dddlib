package org.openkoala.koala.monitor.jwebap;

public class StackTrace extends Trace {
	private static final long serialVersionUID = 1L;
	protected Exception traceException;

	public StackTrace() {
		traceException = new Exception();
	}

	public StackTrace(Trace parent) {
		super(parent);
	}

	public void setStack(Exception e) {
		traceException = e;
	}

	public StackTraceElement[] getStackTraces() {
		if (traceException != null)
			return traceException.getStackTrace();
		else
			return new StackTraceElement[0];
	}

	public String getStackTracesDetails() {
		return getStackTracesDetails(0, getStackTraces().length);
	}

	public String getStackTracesDetails(int end) {
		return getStackTracesDetails(0, end);
	}

	public String getStackTracesDetails(int begin, int end) {
		StringBuffer log = new StringBuffer();
		StackTraceElement traces[] = getStackTraces();
		for (int i = begin; i < traces.length && i < end; i++)
			log.append(traces[i].toString()).append("\n");

		return log.toString();
	}
}
