package com.telappoint.apptdesk.common.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import com.telappoint.apptdesk.common.clientdb.dao.ApptDAO;
import com.telappoint.apptdesk.common.constants.CacheConstants;
import com.telappoint.apptdesk.common.constants.ErrorConstants;
import com.telappoint.apptdesk.common.masterdb.dao.MasterDAO;
import com.telappoint.apptdesk.common.model.BaseRequest;
import com.telappoint.apptdesk.common.model.BaseResponse;
import com.telappoint.apptdesk.common.model.Client;
import com.telappoint.apptdesk.common.model.ClientDeploymentConfig;
import com.telappoint.apptdesk.common.model.JdbcCustomTemplate;
import com.telappoint.apptdesk.handlers.exception.TelAppointException;
import com.telappoint.apptdesk.model.ApptSysConfig;
import com.telappoint.apptdesk.model.IVRXml;
import com.telappoint.apptdesk.model.Language;

/**
 * 
 * @author Balaji Nandarapu
 *
 */
@Component
public class CacheComponent {

	private static Map<String, Client> clientCacheMap = new HashMap<String, Client>();
	private static Map<String, Object> cacheObject = new HashMap<String, Object>();
	private static final Object lock = new Object();
	private static Map<String, Map<String, IVRXml>> cacheMap = new HashMap<String, Map<String, IVRXml>>();

	@Autowired
	private MasterDAO masterDAO;
	
	@Autowired
	private ApptDAO apptDAO;
	
	public Client getClient(Logger logger, String clientCode, boolean cache) throws TelAppointException, Exception {
		StringBuilder key = new StringBuilder();
		key.append(CacheConstants.CLIENT.getValue()).append("|").append(clientCode);
		Client client = clientCacheMap.get(key.toString());
		if (client != null && cache) {
			if (logger != null) {
				logger.debug("Client object returned from cache.");
			}
			return client;
		} else {
			if (logger != null) {
				logger.debug("Client object returned from DB.");
			}
			synchronized (lock) {
				masterDAO.getClients(CacheConstants.CLIENT.getValue(), clientCacheMap);
			}
			client = clientCacheMap.get(key.toString());
			if (client == null) {
				if(logger != null) {
					logger.info("Client is not available to process - [clientCode:" + clientCode+"]");
				}
				BaseRequest baseRequest = new BaseRequest();
				baseRequest.setClientCode(clientCode);
				throw new TelAppointException(ErrorConstants.ERROR_2998.getCode(), ErrorConstants.ERROR_2998.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, "client code is not valid.", baseRequest.toString());
			}
			return client;
		}
	}

	public ClientDeploymentConfig getClientDeploymentConfig(Logger logger, String clientCode, boolean cache) throws TelAppointException, Exception  {
		Client client = getClient(logger, clientCode, true);
		StringBuilder key = new StringBuilder();
		key.append(CacheConstants.CLIENT_DEPLOYMENT_CONFIG.getValue()).append("|").append(clientCode);

		ClientDeploymentConfig clientDeploymentConfig = (ClientDeploymentConfig) cacheObject.get(key.toString());
		if (clientDeploymentConfig != null && cache) {
			logger.debug("ClientDeploymentConfig object returned from cache.");
			return clientDeploymentConfig;
		} else {
			logger.debug("ClientDeploymentConfig object returned from DB.");
			synchronized (lock) {
				masterDAO.getClientDeploymentConfig(CacheConstants.CLIENT_DEPLOYMENT_CONFIG.getValue(), clientCode, client.getClientId(), cacheObject);
			}
			clientDeploymentConfig = (ClientDeploymentConfig) cacheObject.get(key.toString());
			return clientDeploymentConfig;
		}
	}
	

	private Map<String, Map<String, String>> getDesignTemplatesMapCache(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String mainKey, boolean cache) throws TelAppointException, Exception {
		Object obj = cacheObject.get(mainKey);
		if (obj != null && cache) {
			logger.debug("DesignTemplates returned from cache.");
			Map<String, Map<String, String>> map = (Map<String, Map<String, String>>) obj;
			return map;
		} else {
			logger.debug("DesignTemplates returned from DB.");
			Map<String, Map<String, String>> subMap = new HashMap<String, Map<String, String>>();
			apptDAO.getI18nDesignTemplatesMap(jdbcCustomTemplate, logger, subMap);
			synchronized (lock) {
				cacheObject.put(mainKey, subMap);
			}
			return subMap;
		}
	}

	public Map<String, String> getDesignTemplatesMap(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String device, boolean cache) throws TelAppointException, Exception {
		StringBuilder key = new StringBuilder();
		key.append(CacheConstants.DISPLAY_TEMPLATE.getValue()).append("|").append(jdbcCustomTemplate.getClientCode());

		Map<String, Map<String, String>> map = getDesignTemplatesMapCache(jdbcCustomTemplate, logger, key.toString(), cache);
		key = new StringBuilder();
		key.append(device);
		return map.get(key.toString());
	}

	private Map<String, Map<String, String>> getDisplayFieldLabelsMap(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String mainKey, boolean cache) throws TelAppointException, Exception {
		Object obj = cacheObject.get(mainKey);
		if (obj != null && cache) {
			logger.debug("DisplayFieldLabels returned from cache.");
			Map<String, Map<String, String>> map = (Map<String, Map<String, String>>) obj;
			return map;
		} else {
			logger.debug("DisplayFieldLabels returned from DB.");
			Map<String, Map<String, String>> subMap = new HashMap<String, Map<String, String>>();
			apptDAO.getI18nDisplayFieldLabelsMap(jdbcCustomTemplate, logger, subMap);
			synchronized (lock) {
				cacheObject.put(mainKey, subMap);
			}
			return subMap;
		}
	}
	
	public Map<String, String> getDisplayFieldLabelsMap(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String device, String langCode, boolean cache) throws TelAppointException, Exception {
		StringBuilder key = new StringBuilder();
		key.append(CacheConstants.DISPLAY_FIELD_LABEL.getValue()).append("|").append(jdbcCustomTemplate.getClientCode());

		Map<String, Map<String, String>> map = getDisplayFieldLabelsMap(jdbcCustomTemplate, logger, key.toString(), cache);
		key = new StringBuilder();
		key.append(device).append("|").append(langCode);
		return map.get(key.toString());
	}
	
	private Map<String, Map<String, String>> getProcedureNoMatchMap(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String mainKey, boolean cache) throws TelAppointException, Exception {
		Object obj = cacheObject.get(mainKey);
		if (obj != null && cache) {
			logger.debug("getProcedureNoMatchMap returned from cache.");
			Map<String, Map<String, String>> map = (Map<String, Map<String, String>>) obj;
			return map;
		} else {
			logger.debug("getProcedureNoMatchMap returned from DB.");
			Map<String, Map<String, String>> subMap = new HashMap<String, Map<String, String>>();
			apptDAO.getProcedureNoMatchMap(jdbcCustomTemplate, logger, subMap);
			synchronized (lock) {
				cacheObject.put(mainKey, subMap);
			}
			return subMap;
		}
	}
	
	public Map<String, String> getProcedureNoMatch(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String langCode, boolean cache) throws TelAppointException, Exception {
		StringBuilder key = new StringBuilder();
		key.append(CacheConstants.PROCEDURE_NO_MATCH.getValue()).append("|").append(jdbcCustomTemplate.getClientCode());
		Map<String, Map<String, String>> map = getProcedureNoMatchMap(jdbcCustomTemplate, logger, key.toString(), cache);
		key = new StringBuilder();
		key.append(langCode);
		return map.get(key.toString());
	}
	

	private Map<String, Map<String, String>> getDisplayPageContentsMap(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String mainKey, boolean cache) throws TelAppointException, Exception {
		Object obj = cacheObject.get(mainKey);
		if (obj != null && cache) {
			logger.debug("DisplayPageContents returned from cache.");
			Map<String, Map<String, String>> map = (Map<String, Map<String, String>>) obj;
			return map;
		} else {
			logger.debug("DisplayPageContents returned from DB.");
			Map<String, Map<String, String>> subMap = new HashMap<String, Map<String, String>>();
			apptDAO.getI18nPageContentMap(jdbcCustomTemplate, logger, subMap);
			synchronized (lock) {
				cacheObject.put(mainKey, subMap);
			}
			return subMap;
		}
	}
	
	public Map<String, String> getDisplayPageContentsMap(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String device, String langCode, boolean cache) throws TelAppointException, Exception {
		StringBuilder key = new StringBuilder();
		key.append(CacheConstants.DISPLAY_PAGE_CONTENT.getValue()).append("|").append(jdbcCustomTemplate.getClientCode());

		Map<String, Map<String, String>> map = getDisplayPageContentsMap(jdbcCustomTemplate, logger, key.toString(), cache);
		key = new StringBuilder();
		key.append(device).append("|").append(langCode);
		return map.get(key.toString());
	}
	
	
	private Map<String, Map<String, String>> getI18nEmailTemplateMap(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String mainKey, boolean cache) throws TelAppointException, Exception {
		Object obj = cacheObject.get(mainKey);
		if (obj != null && cache) {
			logger.debug("EmailTemplateMap returned from cache.");
			Map<String, Map<String, String>> map = (Map<String, Map<String, String>>) obj;
			return map;
		} else {
			logger.debug("EmailTemplateMap returned from DB.");
			Map<String, Map<String, String>> subMap = new HashMap<String, Map<String, String>>();
			apptDAO.getI18nEmailTemplateMap(jdbcCustomTemplate, logger, subMap);
			synchronized (lock) {
				cacheObject.put(mainKey, subMap);
			}
			return subMap;
		}
	}

	public Map<String, String> getEmailTemplateMap(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String langCode, boolean cache) throws TelAppointException, Exception {
		StringBuilder key = new StringBuilder();
		key.append(CacheConstants.EMAIL_TEMPLATE.getValue()).append("|").append(jdbcCustomTemplate.getClientCode());

		Map<String, Map<String, String>> map = getI18nEmailTemplateMap(jdbcCustomTemplate, logger, key.toString(), cache);
		key.setLength(0);
		key.append(langCode);
		return map.get(key.toString());
	}


	private Map<String, Map<String, String>> getDisplayButtonsMap(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String mainKey, boolean cache) throws TelAppointException, Exception {
		Object obj = cacheObject.get(mainKey);
		if (obj != null && cache) {
			logger.debug("DisplayButtons returned from cache.");
			Map<String, Map<String, String>> map = (Map<String, Map<String, String>>) obj;
			return map;
		} else {
			logger.debug("DisplayButtons returned from DB.");
			Map<String, Map<String, String>> subMap = new HashMap<String, Map<String, String>>();
			apptDAO.getI18nButtonsMap(jdbcCustomTemplate, logger, subMap);
			synchronized (lock) {
				cacheObject.put(mainKey, subMap);
			}
			return subMap;
		}
	}

	public Map<String, String> getDisplayButtonsMap(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String device, String langCode, boolean cache) throws TelAppointException, Exception {
		StringBuilder key = new StringBuilder();
		key.append(CacheConstants.DISPLAY_BUTTON_NAMES.getValue()).append("|").append(jdbcCustomTemplate.getClientCode());
		Map<String, Map<String, String>> map = getDisplayButtonsMap(jdbcCustomTemplate, logger, key.toString(), cache);
		key.setLength(0);
		key.append(device).append("|").append(langCode);
		return map.get(key.toString());
	}

	private Map<String, Map<String, String>> getDisplayAliasMap(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String mainKey, boolean cache) throws TelAppointException, Exception {
		Object obj = cacheObject.get(mainKey);
		if (obj != null && cache) {
			logger.debug("DisplayAlias returned from cache.");
			Map<String, Map<String, String>> map = (Map<String, Map<String, String>>) obj;
			return map;
		} else {
			logger.debug("DisplayAlias returned from DB.");
			Map<String, Map<String, String>> subMap = new HashMap<String, Map<String, String>>();
			apptDAO.getI18nAliasesMap(jdbcCustomTemplate, logger, subMap);
			synchronized (lock) {
				cacheObject.put(mainKey, subMap);
			}
			return subMap;
		}
	}

	public Map<String, String> getDisplayAliasMap(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String device, String langCode, boolean cache) throws TelAppointException, Exception {
		StringBuilder key = new StringBuilder();
		key.append(CacheConstants.DISPLAY_ALIAES.getValue()).append("|").append(jdbcCustomTemplate.getClientCode());

		Map<String, Map<String, String>> map = getDisplayAliasMap(jdbcCustomTemplate, logger, key.toString(), cache);
		key.setLength(0);
		key.append(device).append("|").append(langCode);
		return map.get(key.toString());
	}

	public List<Language> getLangList(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, boolean cache) throws Exception {
		StringBuilder key = new StringBuilder();
		key.append(CacheConstants.DISPLAY_LANG.getValue()).append("|").append(jdbcCustomTemplate.getClientCode());

		Object obj = cacheObject.get(key.toString());
		if (obj != null && cache) {
			logger.debug("LanguageList returned from cache.");
			return (List<Language>) ((ArrayList<Language>) obj).clone();
		} else {
			logger.debug("LanguageList returned from DB.");
			List<Language> languageList = new ArrayList<Language>();
			apptDAO.getLangDetails(jdbcCustomTemplate, logger, languageList);
			synchronized (lock) {
				cacheObject.put(key.toString(), languageList);
			}
			return languageList;
		}
	}

	private Map<String, IVRXml> getVXML(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String mainKey, boolean cache) throws TelAppointException, Exception {
		Map<String, IVRXml> map = cacheMap.get(mainKey);
		if (map != null && map.size() > 0 && cache) {
			logger.debug("VXML returned from cache.");
			return map;
		} else {
			logger.debug("VXML returned from DB.");
			synchronized (lock) {
				apptDAO.loadVXML(jdbcCustomTemplate, mainKey, cacheMap);
			}
			return cacheMap.get(mainKey);
		}
	}

	public void loadVXML(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, BaseResponse baseResponse, String pageName, String langCode, boolean cache) throws TelAppointException, Exception {
		StringBuilder key = new StringBuilder();
		key.append(CacheConstants.IVR_VXML.getValue()).append("|").append(jdbcCustomTemplate.getClientCode());

		Map<String, IVRXml> map = getVXML(jdbcCustomTemplate, logger, key.toString(), cache);
		key= new StringBuilder();
		key.append(pageName).append("|").append(langCode);
		IVRXml ivrxml = map.get(key.toString());

		baseResponse.setPageName(ivrxml == null || ivrxml.getPageName() == null ? "" : ivrxml.getPageName());
		baseResponse.setVxml(ivrxml == null || ivrxml.getVxml() == null ? "" : ivrxml.getVxml());
		baseResponse.setPageAudio(ivrxml == null || ivrxml.getPageAudio() == null ? "" : ivrxml.getPageAudio());
		baseResponse.setPageTTS(ivrxml == null || ivrxml.getPageTTS() == null ? "" : ivrxml.getPageTTS());
	}
	
	public ApptSysConfig getApptSysConfig(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, boolean cache) throws TelAppointException, Exception {
		StringBuilder key = new StringBuilder();
		key.append(CacheConstants.APPT_SYS_CONFIG.getValue()).append("|").append(jdbcCustomTemplate.getClientCode());

		Object obj = cacheObject.get(key.toString());
		if (obj != null && cache) {
			logger.debug("ResvSysConfig object returned from cache");
			return (ApptSysConfig) obj;
		} else {
			logger.debug("ResvSysConfig object returned from DB.");
			ApptSysConfig resvSysConfig = apptDAO.getApptSysConfig(jdbcCustomTemplate, logger);
			synchronized (lock) {
				cacheObject.put(key.toString(), resvSysConfig);
			}
			return resvSysConfig;
		}
	}
}
