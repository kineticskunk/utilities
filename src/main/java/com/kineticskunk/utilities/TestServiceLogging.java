package com.kineticskunk.utilities;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;

/**
 * 
 * @author yodaqua
 *
 */
public class TestServiceLogging {

	private Logger logger;
	private boolean loggerEnabled;

	public TestServiceLogging(Logger logger) {
		this.logger = logger;
	}

	public TestServiceLogging(Logger logger, boolean loggerEnabled) {
		this(logger);
		this.loggerEnabled = loggerEnabled;
	}
	
	/*
	 * 
	 */
	public void enterLogger() {
		if (this.loggerEnabled) {
			this.logger.info("--------------------------------------------------------------------------------------------------------------------------------");
			this.logger.entry();
		}
	}
	
	/**
	 * 
	 * @param message
	 */	
	public void enterLogger(String message) {
		if (this.loggerEnabled) {
			this.logger.info("--------------------------------------------------------------------------------------------------------------------------------");
			this.logger.entry(message);
		}
	}
	
	/**
	 * 
	 * @param methodName
	 * @param parameters
	 */
	public void enterLogger(String methodName, String parameters) {
		if (this.loggerEnabled) {
			this.logger.info("--------------------------------------------------------------------------------------------------------------------------------");
			this.logger.entry(methodName, parameters);
		}
	}
	
	/**
	 * Log message to logger instantiated in the constructor
	 * @param marker
	 * @param methodName
	 * @param parameters
	 */
	public void enterLogger(Marker marker, String methodName, String parameters) {
		if (this.loggerEnabled) {
			this.logger.info("--------------------------------------------------------------------------------------------------------------------------------");
			this.logger.entry(marker, methodName, parameters);
		}
	}
	
	/**
	 * Log message to a different logger than the logger instantiated in the constructor
	 * @param logger
	 * @param marker
	 * @param methodName
	 * @param parameters
	 */
	public void enterLogger(Logger logger, Marker marker, String methodName, String parameters) {
		if (this.loggerEnabled) {
			logger.info("--------------------------------------------------------------------------------------------------------------------------------");
			logger.entry(marker, methodName, parameters);
		}

	}

	/**
	 * 
	 * @param methodName
	 */
	public void logMessage(Logger logger, Marker marker, Level level, String methodName) {
		if (this.loggerEnabled) {
			logger.info("--------------------------------------------------------------------------------------------------------------------------------");
			logger.log(level, marker, methodName);	
		}
	}

	/**
	 * 
	 * @param logLevel
	 * @param loggingMessage
	 */
	public void logMessage(Level logLevel, String loggingMessage) { 
		if (this.loggerEnabled) {
			this.logger.log(logLevel, loggingMessage);
		}
	}

	/**
	 * 
	 * @param exitMessage
	 */
	public void exitLogger(boolean result) {
		if (this.loggerEnabled) {
			this.logger.exit(result);
		}
	}

	/**
	 * 
	 * @param logger
	 * @param result
	 */
	public void exitLogger(Logger logger, String result) {
		if (this.loggerEnabled) {
			logger.exit(result);
		}
	}

	/**
	 * 
	 * @param methodName
	 * @param params
	 * @param ex
	 */
	public void catchException(Exception ex) {
		if (this.loggerEnabled) {
			logger.info("--------------------------------------------------------------------------------------------------------------------------------");
			logger.debug("Message = " + ex.getMessage());
			logger.debug("Cause = " + ex.getCause());
		}
	}
	
	/**
	 * 
	 * @param marker
	 * @param methodName
	 * @param params
	 * @param ex
	 */
	public void catchError(Marker marker, String methodName,  String params, Exception ex) {
		logger.entry();
		if (logger.isDebugEnabled()) {
			logger.log(Level.DEBUG, marker, "RUN {} USING {}", methodName, params);
			logger.log(Level.DEBUG, marker, "Cause = " + ex.getCause());
			logger.log(Level.DEBUG, marker, "Message = " + ex.getMessage());
			logger.log(Level.DEBUG, marker, "Local Message = " + ex.getLocalizedMessage());
			logger.catching(Level.DEBUG, ex);
		}
		logger.exit();
	}

}
