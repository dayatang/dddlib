package org.openkoala.koala.auth.ss3adapter;

/**
 * 邮箱未找到异常
 * @author zhuyuanbiao
 * @date 2014年1月6日 上午11:08:33
 *
 */
public class EmailNotFoundExction extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3598969735212878275L;

	public EmailNotFoundExction() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EmailNotFoundExction(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public EmailNotFoundExction(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public EmailNotFoundExction(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
