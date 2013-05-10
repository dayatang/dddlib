package com.dayatang.utils;

import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

public class Slf4jLoggerTest {

	private Slf4jLogger instance;
	private Logger logger;
	private String msg = "Hello, {}, {}, and {}";
	private String errorMsg = "Oh, No!!!";
	private Object[] args = new Object[] {"A", "B", "C"};
	private Throwable t = new RuntimeException("error!");
	
	@Before
	public void setUp() throws Exception {
		logger = mock(Logger.class);
		instance = new Slf4jLogger(logger);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testDebugEnable() {
		when(logger.isDebugEnabled()).thenReturn(true);
		instance.debug(msg, "A", "B", "C");
		verify(logger).debug(msg, args);
	}

	@Test
	public void testDebugDisable() {
		when(logger.isDebugEnabled()).thenReturn(false);
		instance.debug(msg, "A", "B", "C");
		verify(logger, never()).debug(msg, args);
	}

	@Test
	public void testDebugEnabledException() {
		when(logger.isDebugEnabled()).thenReturn(true);
		instance.debug(errorMsg, t);
		verify(logger).debug(errorMsg, t);
	}

	@Test
	public void testDebugDisabledException() {
		when(logger.isDebugEnabled()).thenReturn(false);
		instance.debug(errorMsg, t);
		verify(logger, never()).debug(errorMsg, t);
	}

	@Test
	public void testInfoEnable() {
		when(logger.isInfoEnabled()).thenReturn(true);
		instance.info(msg, "A", "B", "C");
		verify(logger).info(msg, args);
	}

	@Test
	public void testInfoDisable() {
		when(logger.isInfoEnabled()).thenReturn(false);
		instance.info(msg, "A", "B", "C");
		verify(logger, never()).info(msg, args);
	}

	@Test
	public void testInfoEnabledException() {
		when(logger.isInfoEnabled()).thenReturn(true);
		instance.info(errorMsg, t);
		verify(logger).info(errorMsg, t);
	}

	@Test
	public void testInfoDisabledException() {
		when(logger.isInfoEnabled()).thenReturn(false);
		instance.info(errorMsg, t);
		verify(logger, never()).info(errorMsg, t);
	}

	@Test
	public void testTraceEnable() {
		when(logger.isTraceEnabled()).thenReturn(true);
		instance.trace(msg, "A", "B", "C");
		verify(logger).trace(msg, args);
	}

	@Test
	public void testTraceDisable() {
		when(logger.isTraceEnabled()).thenReturn(false);
		instance.trace(msg, "A", "B", "C");
		verify(logger, never()).trace(msg, args);
	}

	@Test
	public void testTraceEnabledException() {
		when(logger.isTraceEnabled()).thenReturn(true);
		instance.trace(errorMsg, t);
		verify(logger).trace(errorMsg, t);
	}

	@Test
	public void testTraceDisabledException() {
		when(logger.isTraceEnabled()).thenReturn(false);
		instance.trace(errorMsg, t);
		verify(logger, never()).trace(errorMsg, t);
	}

	@Test
	public void testWarnEnable() {
		when(logger.isWarnEnabled()).thenReturn(true);
		instance.warn(msg, "A", "B", "C");
		verify(logger).warn(msg, args);
	}

	@Test
	public void testWarnDisable() {
		when(logger.isWarnEnabled()).thenReturn(false);
		instance.warn(msg, "A", "B", "C");
		verify(logger, never()).warn(msg, args);
	}

	@Test
	public void testWarnEnabledException() {
		when(logger.isWarnEnabled()).thenReturn(true);
		instance.warn(errorMsg, t);
		verify(logger).warn(errorMsg, t);
	}

	@Test
	public void testWarnDisabledException() {
		when(logger.isWarnEnabled()).thenReturn(false);
		instance.warn(errorMsg, t);
		verify(logger, never()).warn(errorMsg, t);
	}

	@Test
	public void testErrorEnable() {
		when(logger.isErrorEnabled()).thenReturn(true);
		instance.error(msg, "A", "B", "C");
		verify(logger).error(msg, args);
	}

	@Test
	public void testErrorDisable() {
		when(logger.isErrorEnabled()).thenReturn(false);
		instance.error(msg, "A", "B", "C");
		verify(logger, never()).error(msg, args);
	}

	@Test
	public void testErrorEnabledException() {
		when(logger.isErrorEnabled()).thenReturn(true);
		instance.error(errorMsg, t);
		verify(logger).error(errorMsg, t);
	}

	@Test
	public void testErrorDisabledException() {
		when(logger.isErrorEnabled()).thenReturn(false);
		instance.error(errorMsg, t);
		verify(logger, never()).error(errorMsg, t);
	}

}
