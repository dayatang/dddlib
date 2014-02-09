package org.openkoala.koala.deploy.ejb.exception;

import org.openkoala.koala.exception.KoalaException;
/**
 * 接口复制过程中的异常类
 * @author lingen
 *
 */
public class InterfaceCopyException extends KoalaException {

	public InterfaceCopyException(String message) {
		super(message);
	}

	private static final long serialVersionUID = 5368647285668408918L;

}
