package com.telappoint.apptdesk.common.components;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.loader.StringResourceLoader;
import org.apache.velocity.runtime.resource.util.StringResourceRepository;
import org.springframework.beans.MethodInvocationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import com.telappoint.apptdesk.common.clientdb.dao.ApptDAO;
import com.telappoint.apptdesk.common.constants.ErrorConstants;
import com.telappoint.apptdesk.common.constants.PropertiesConstants;
import com.telappoint.apptdesk.common.logger.AsynchLogger;
import com.telappoint.apptdesk.common.logger.LoggerPool;
import com.telappoint.apptdesk.common.model.ResponseModel;
import com.telappoint.apptdesk.common.utils.PropertyUtils;
import com.telappoint.apptdesk.handlers.exception.TelAppointException;

/**
 * 
 * @author Balaji N
 *
 */
@Component
public class CommonComponent {

	@Autowired
	private ApptDAO apptDAO;

	private VelocityEngine ve = null;
	
	public CommonComponent() {
		ve = new VelocityEngine();
		Properties properties = new Properties();
		String logFileLocation = null;
		try {
			logFileLocation = PropertyUtils.getValueFromProperties("LOG_LOCATION_PATH", PropertiesConstants.APPT_SERVICE_REST_WS_PROP.getPropertyFileName());
		} catch (Exception e) {
			if (logFileLocation == null) {
				logFileLocation = "/opt/tomcat8/logs";
			}
		}
		properties.setProperty("runtime.log", logFileLocation + "/velocity.log");
		properties.setProperty("resource.loader", "string");
		properties.setProperty("string.resource.loader.class", "org.apache.velocity.runtime.resource.loader.StringResourceLoader");
		ve.init(properties);
	}

	public Logger getLogger(String clientCode, String logLevel) throws TelAppointException {
		Logger asynchLogger = null;
		Level level = getLogLevel(logLevel);
		String logFileLocation = null;
		try {
			logFileLocation = PropertyUtils.getValueFromProperties("LOG_LOCATION_PATH", PropertiesConstants.APPT_SERVICE_REST_WS_PROP.getPropertyFileName());
			if (LoggerPool.getHandleToLogger(clientCode) == null) {
				asynchLogger = (new AsynchLogger(logFileLocation + "/" + clientCode+".log", level)).getLogger(clientCode);
				LoggerPool.addLogger(clientCode, asynchLogger);
			}
			return LoggerPool.getHandleToLogger(clientCode);
		} catch (IOException ioe) {
			throw new TelAppointException(ErrorConstants.ERROR_2999.getCode(), ErrorConstants.ERROR_2999.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, "Logger not initialized","Data: clientCode:"+clientCode);
		} catch (Exception e) {
			throw new TelAppointException(ErrorConstants.ERROR_2999.getCode(), ErrorConstants.ERROR_2999.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,"Logger not initialized","Data: clientCode:"+clientCode);
		}
	}

	public Logger changeLogLevel(String clientCode, String logLevel) throws TelAppointException {
		Logger asynchLogger = null;
		Level level = getLogLevel(logLevel);
		try {
			String logFileLocation = PropertyUtils.getValueFromProperties("LOG_LOCATION_PATH", PropertiesConstants.APPT_SERVICE_REST_WS_PROP.getPropertyFileName());
			if (LoggerPool.getHandleToLogger(clientCode) != null) {
				asynchLogger = (new AsynchLogger(logFileLocation + "/" + clientCode, level)).setLoggerProperties(LoggerPool.getHandleToLogger(clientCode), level);
				LoggerPool.addLogger(clientCode, asynchLogger);
			}
			return LoggerPool.getHandleToLogger(clientCode);
		} catch (Exception e) {
			throw new TelAppointException(ErrorConstants.ERROR_2999.getCode(), ErrorConstants.ERROR_2999.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, "Failed to switch the log level to "+logLevel, null);
		}
	}

	public StringWriter processTemplate(String templateName, String templateContent, Object data) throws ResourceNotFoundException, ParseErrorException, MethodInvocationException {
		StringWriter writer = new StringWriter();
		VelocityContext context = new VelocityContext();
		StringResourceRepository repo = StringResourceLoader.getRepository();
		repo.putStringResource(templateName, templateContent);
		if(data instanceof Map) {
			Map<String, Object> map = (Map<String, Object>)data; 
			 for ( Map.Entry<String, Object> e : map.entrySet() ) {
			        context.put(e.getKey(), e.getValue() ==null?"":e.getValue());
			 }
		} else {
			context.put("dynamicData", data);
		}
		Template t = ve.getTemplate(templateName);
		t.merge(context, writer);

		return writer;
	}
	
	public ResponseModel populateRMDSuccessData(Logger logger, Object data) {
		return populateRMDSuccessData(logger, data, true);
	}

	public ResponseModel populateRMDData(Logger logger, Object data) {
		return populateRMDSuccessData(logger, data, true);
	}

	public ResponseModel populateRMDSuccessData(Logger logger, Object data, boolean logging) {
		if (logging == false) {
			// TODO: log it
		}

		ResponseModel responseModel = new ResponseModel();
		responseModel.setData(data);
		return responseModel;
	}

	private Level getLogLevel(String logLevel) {
		Level level = null;
		if (logLevel == null) {
			level = Level.INFO;
		} else {
			if (logLevel.equalsIgnoreCase("TRACE")) {
				level = Level.TRACE;
			} else if (logLevel.equalsIgnoreCase("DEBUG")) {
				level = Level.DEBUG;
			} else {
				level = Level.INFO;
			}
		}
		return level;
	}
}
