package com.telappoint.apptdesk.service.impl;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;

import com.telappoint.apptdesk.common.clientdb.dao.ApptDAO;
import com.telappoint.apptdesk.common.components.CacheComponent;
import com.telappoint.apptdesk.common.components.CommonComponent;
import com.telappoint.apptdesk.common.components.ConnectionPoolUtil;
import com.telappoint.apptdesk.common.components.EmailComponent;
import com.telappoint.apptdesk.common.constants.AppointmentMethod;
import com.telappoint.apptdesk.common.constants.AppointmentStatus;
import com.telappoint.apptdesk.common.constants.CommonApptDeskConstants;
import com.telappoint.apptdesk.common.constants.CommonDateContants;
import com.telappoint.apptdesk.common.constants.DesignTemplateConstants;
import com.telappoint.apptdesk.common.constants.DisplayFieldLabelConstants;
import com.telappoint.apptdesk.common.constants.DisplayPageContentConstants;
import com.telappoint.apptdesk.common.constants.EmailTemplateConstants;
import com.telappoint.apptdesk.common.constants.ErrorConstants;
import com.telappoint.apptdesk.common.constants.IVRVxmlConstants;
import com.telappoint.apptdesk.common.constants.PropertiesConstants;
import com.telappoint.apptdesk.common.externallogin.Eligibility;
import com.telappoint.apptdesk.common.externallogin.EnrollementResponse;
import com.telappoint.apptdesk.common.externallogin.ExternalLoginRestClient;
import com.telappoint.apptdesk.common.externallogin.HouseHold;
import com.telappoint.apptdesk.common.externallogin.Login;
import com.telappoint.apptdesk.common.externallogin.Participant;
import com.telappoint.apptdesk.common.masterdb.dao.MasterDAO;
import com.telappoint.apptdesk.common.masterdb.dao.impl.ProvidersLocResSer;
import com.telappoint.apptdesk.common.model.AvailableDateTimes;
import com.telappoint.apptdesk.common.model.BaseResponse;
import com.telappoint.apptdesk.common.model.Client;
import com.telappoint.apptdesk.common.model.ClientCustomerInfo;
import com.telappoint.apptdesk.common.model.ClientDeploymentConfig;
import com.telappoint.apptdesk.common.model.ClientInfo;
import com.telappoint.apptdesk.common.model.ClientInfoResponse;
import com.telappoint.apptdesk.common.model.CompanyResponse;
import com.telappoint.apptdesk.common.model.Department;
import com.telappoint.apptdesk.common.model.DepartmentResponse;
import com.telappoint.apptdesk.common.model.EmailRequest;
import com.telappoint.apptdesk.common.model.ExternalLogicResponse;
import com.telappoint.apptdesk.common.model.ExternalLoginRequest;
import com.telappoint.apptdesk.common.model.FirstAvailableDateTime;
import com.telappoint.apptdesk.common.model.HoldAppt;
import com.telappoint.apptdesk.common.model.HomePageResponse;
import com.telappoint.apptdesk.common.model.IPhoneClientInfo;
import com.telappoint.apptdesk.common.model.IPhoneClientResponse;
import com.telappoint.apptdesk.common.model.IvrLang;
import com.telappoint.apptdesk.common.model.JdbcCustomTemplate;
import com.telappoint.apptdesk.common.model.LocResSerResponse;
import com.telappoint.apptdesk.common.model.LocationResourceServiceInfo;
import com.telappoint.apptdesk.common.model.LocationResponse;
import com.telappoint.apptdesk.common.model.MobileAppPage;
import com.telappoint.apptdesk.common.model.MobileClientResponse;
import com.telappoint.apptdesk.common.model.PersonalInfo;
import com.telappoint.apptdesk.common.model.PersonalInfoResponse;
import com.telappoint.apptdesk.common.model.ProcedureListResponse;
import com.telappoint.apptdesk.common.model.ProviderResponse;
import com.telappoint.apptdesk.common.model.Resource;
import com.telappoint.apptdesk.common.model.ResourceResponse;
import com.telappoint.apptdesk.common.model.ResponseModel;
import com.telappoint.apptdesk.common.model.ServiceCallCenterResponse;
import com.telappoint.apptdesk.common.model.ServiceOption;
import com.telappoint.apptdesk.common.model.ServiceStatus;
import com.telappoint.apptdesk.common.model.TempHoldAppt;
import com.telappoint.apptdesk.common.model.TimeSlots;
import com.telappoint.apptdesk.common.model.VerifyPageData;
import com.telappoint.apptdesk.common.model.VerifyPageResponse;
import com.telappoint.apptdesk.common.utils.CoreUtils;
import com.telappoint.apptdesk.common.utils.DateUtils;
import com.telappoint.apptdesk.common.utils.PropertyUtils;
import com.telappoint.apptdesk.handlers.exception.TelAppointException;
import com.telappoint.apptdesk.model.AppointmentDetails;
import com.telappoint.apptdesk.model.AppointmentsDataResponse;
import com.telappoint.apptdesk.model.ApptSysConfig;
import com.telappoint.apptdesk.model.AuthResponse;
import com.telappoint.apptdesk.model.CancelAppointResponse;
import com.telappoint.apptdesk.model.Company;
import com.telappoint.apptdesk.model.ConfirmAppointmentRequest;
import com.telappoint.apptdesk.model.ConfirmAppointmentResponse;
import com.telappoint.apptdesk.model.Customer;
import com.telappoint.apptdesk.model.CustomerInfo;
import com.telappoint.apptdesk.model.CustomerInfoRequest;
import com.telappoint.apptdesk.model.CustomerPledge;
import com.telappoint.apptdesk.model.CustomerPledgeData;
import com.telappoint.apptdesk.model.CustomerPledgeResponse;
import com.telappoint.apptdesk.model.CustomerPledgeWithTemplateResponse;
import com.telappoint.apptdesk.model.DirectAccessNumberResponse;
import com.telappoint.apptdesk.model.DuplicateApptResponse;
import com.telappoint.apptdesk.model.ExternalLoginEnrollment;
import com.telappoint.apptdesk.model.FirstAvailableDateAnyLocationResponse;
import com.telappoint.apptdesk.model.HoldAppointmentRequest;
import com.telappoint.apptdesk.model.HoldAppointmentResponse;
import com.telappoint.apptdesk.model.HouseHoldMonthlyIncomeResponse;
import com.telappoint.apptdesk.model.IVRCallRequest;
import com.telappoint.apptdesk.model.IVRCallResponse;
import com.telappoint.apptdesk.model.IVRFlow;
import com.telappoint.apptdesk.model.IVRPageFields;
import com.telappoint.apptdesk.model.IVRPageFieldsReponse;
import com.telappoint.apptdesk.model.IVRVxmlData;
import com.telappoint.apptdesk.model.IVRVxmlFlowData;
import com.telappoint.apptdesk.model.IVRXml;
import com.telappoint.apptdesk.model.LandingPageInfo;
import com.telappoint.apptdesk.model.Language;
import com.telappoint.apptdesk.model.ListOfDocsTOBringResponse;
import com.telappoint.apptdesk.model.ListOfThingsResponse;
import com.telappoint.apptdesk.model.Location;
import com.telappoint.apptdesk.model.LocationPriSecAvailable;
import com.telappoint.apptdesk.model.LoginPageFields;
import com.telappoint.apptdesk.model.MessageResponse;
import com.telappoint.apptdesk.model.MobilePageFieldsReponse;
import com.telappoint.apptdesk.model.NameRecordInfo;
import com.telappoint.apptdesk.model.OnlineFlow;
import com.telappoint.apptdesk.model.OnlinePageContent;
import com.telappoint.apptdesk.model.OnlinePageData;
import com.telappoint.apptdesk.model.OnlinePageFields;
import com.telappoint.apptdesk.model.Options;
import com.telappoint.apptdesk.model.PageValidationResponse;
import com.telappoint.apptdesk.model.PendingEnrollment;
import com.telappoint.apptdesk.model.PledgeLetterData;
import com.telappoint.apptdesk.model.Procedure;
import com.telappoint.apptdesk.model.ProcedureResponse;
import com.telappoint.apptdesk.model.ProvidersLocResSerResponse;
import com.telappoint.apptdesk.model.Schedule;
import com.telappoint.apptdesk.model.SchedulerClosedNoFunding;
import com.telappoint.apptdesk.model.ServiceFundsResponse;
import com.telappoint.apptdesk.model.ServiceResponse;
import com.telappoint.apptdesk.model.ServiceTimeSlotsAvailableStatus;
import com.telappoint.apptdesk.model.SingleServiceNotClosed;
import com.telappoint.apptdesk.model.TransScript;
import com.telappoint.apptdesk.model.TransScriptEmailData;
import com.telappoint.apptdesk.model.TransScriptRequest;
import com.telappoint.apptdesk.model.UpdateCustomerInfoRequest;
import com.telappoint.apptdesk.service.ApptService;


/**
 * @author Balaji N
 */
@Service
public class ApptServiceImpl implements ApptService {

	@Autowired
	private MasterDAO masterDAO;

	@Autowired
	private ApptDAO apptDAO;

	@Autowired
	private ConnectionPoolUtil connectionPoolUtil;

	@Autowired
	private CacheComponent cacheComponent;

	@Autowired
	private CommonComponent commonComponent;

	@Autowired
	private EmailComponent emailComponent;

	private Object lock = new Object();

	@Override
	public boolean isClientAllowed(Logger logger, String clientCode) throws Exception {
		Client client = cacheComponent.getClient(logger, clientCode, true);
		if (client == null) {
			logger.error("ClientCode is null");
			return false;
		}
		return true;
	}

	public ResponseEntity<ResponseModel> handleException(Logger logger, String clientCode, Exception tae) {
		String clientName = "";
		try {
			if (clientCode != null && !"".equals(clientCode)) {
				Client client = cacheComponent.getClient(logger, clientCode, true);
				if (client != null) {
					clientName = client.getClientName();
				}
			}
		} catch (Exception e) {
			logger.error("Error: " + e, e);
		}
		return ResponseModel.exceptionResponse(logger, clientName, tae, emailComponent);

	}

	@Override
	public Logger getLogger(String clientCode, String logLevel) throws TelAppointException {
		return commonComponent.getLogger(clientCode, logLevel);
	}

	@Override
	public Logger changeLogLevel(String clientCode, String logLevel) throws TelAppointException {
		return commonComponent.changeLogLevel(clientCode, logLevel);
	}

	public ResponseModel populateRMDSuccessData(Logger logger, Object data) {
		return commonComponent.populateRMDData(logger, data);
	}

	public String getClientName(Logger logger, String clientCode) throws TelAppointException, Exception {
		Client client = cacheComponent.getClient(logger, clientCode, true);
		return client.getClientCode();
	}

	@Override
	public ResponseEntity<ResponseModel> getOnlineLandingPage(Logger logger, String clientCode, String langCode, String device) throws TelAppointException, Exception {
		LandingPageInfo landingPageInfo = new LandingPageInfo();
		Client client = cacheComponent.getClient(logger, clientCode, true);
		boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		populateClientData(client, landingPageInfo);
		populateDesignTemplate(jdbcCustomTemplate, logger, landingPageInfo, device, cache);
		if (langCode == null || "".equals(langCode)) {
			Language language = apptDAO.getDefaultLangCode(jdbcCustomTemplate, logger);
			langCode = language != null ? language.getLangCode() : "us-en";
			landingPageInfo.setDefaultLangCode(langCode);
		}
		populatePageContent(jdbcCustomTemplate, logger, landingPageInfo, device, langCode, cache);
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, landingPageInfo), HttpStatus.OK);
	}

	/**
	 * It is used to get the onlineFlow data, onlinePageContent data and
	 * onlinePageFields. Used to call to to fetch the data from database. Here
	 * we can use cache but not used because of front module will cache after
	 * the first hit.
	 *
	 * @return ResponseEntity<ResponseModel> throws {@link TelAppointException}
	 */
	@Override
	public ResponseEntity<ResponseModel> getOnlinePageData(Logger logger, String clientCode, String langCode, String device) throws TelAppointException, Exception {
		OnlinePageData onlinePageData = new OnlinePageData();
		Client client = cacheComponent.getClient(logger, clientCode, true);
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);

		Map<String, OnlineFlow> onlineFlows = new LinkedHashMap<String, OnlineFlow>();
		Map<String, List<OnlinePageContent>> onlinePageContents = new HashMap<String, List<OnlinePageContent>>();
		Map<String, List<String>> onlinePageContentMapIds = new HashMap<String, List<String>>();
		apptDAO.getOnlineFlow(logger, jdbcCustomTemplate, onlineFlows);
		apptDAO.getOnlinePageContent(logger, jdbcCustomTemplate, langCode, onlinePageContents, onlinePageContentMapIds);
		Map<String, List<OnlinePageFields>> onlinePageFieldMap = new LinkedHashMap<String, List<OnlinePageFields>>();

		apptDAO.getOnlinePageFields(logger, jdbcCustomTemplate, langCode, onlinePageFieldMap);
		onlinePageData.setOnlineFlows(onlineFlows);
		onlinePageData.setOnlinePageContentMap(onlinePageContents);
		onlinePageData.setOnlinePageFieldsMap(onlinePageFieldMap);
		SchedulerClosedNoFunding schedulerClosedNoFunding = new SchedulerClosedNoFunding();
		getSchedulerClosedNoFunding(logger, jdbcCustomTemplate, device, langCode, schedulerClosedNoFunding);
		onlinePageData.setSchedulerClosedNoFunding(schedulerClosedNoFunding);
		onlinePageData.setOnlinePageContentMapIds(onlinePageContentMapIds);
		Map<String, String> contentMap = cacheComponent.getDisplayPageContentsMap(jdbcCustomTemplate, logger, device, langCode, true);
		if (contentMap != null) {
			onlinePageData.setLogoutURL(contentMap.get(DisplayFieldLabelConstants.LOGOUT_URL.getValue()));
		}
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, onlinePageData), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseModel> getIVRPageFields(Logger logger, String clientCode, String langCode, String device) throws TelAppointException, Exception {
		IVRPageFieldsReponse ivrPageFieldResponse = new IVRPageFieldsReponse();
		Client client = cacheComponent.getClient(logger, clientCode, true);
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		List<IVRPageFields> ivrPageFields = new ArrayList<IVRPageFields>();
		apptDAO.getIVRPageFields(logger, jdbcCustomTemplate, langCode, ivrPageFields);
		ivrPageFieldResponse.setIvrPageFields(ivrPageFields);
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, ivrPageFieldResponse), HttpStatus.OK);
	}

	/**
	 * It is used to get the ivrFlow data, Used to call to fetch the data from
	 * data base. Here we can use cache but not used because of front module
	 * will cache after the first hit.
	 *
	 * @return ResponseEntity<ResponseModel> throws {@link TelAppointException,
	 *         Exception}
	 */
	@Override
	public ResponseEntity<ResponseModel> getIVRFlow(Logger logger, String clientCode, String device, String langCode, String anis, String dnis) throws TelAppointException,
			Exception {
		IVRVxmlFlowData ivrVxmlData = new IVRVxmlFlowData();
		Client client = cacheComponent.getClient(logger, clientCode, true);
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		List<IVRFlow> ivrFlows = new ArrayList<IVRFlow>();
		apptDAO.getIVRFlow(logger, jdbcCustomTemplate, ivrFlows);
		Language language = apptDAO.getDefaultLangCode(jdbcCustomTemplate, logger);
		ivrVxmlData.setLangCode(language != null ? language.getLangCode() : "us-en");
		ivrVxmlData.setVoiceName(language != null ? language.getVoiceName() : "");
		ivrVxmlData.setIvrFlows(ivrFlows);
		SchedulerClosedNoFunding schedulerClosedNoFunding = new SchedulerClosedNoFunding();
		getSchedulerClosedNoFunding(logger, jdbcCustomTemplate, device, langCode, schedulerClosedNoFunding);
		ivrVxmlData.setSchedulerClosedNoFunding(schedulerClosedNoFunding);
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, ivrVxmlData), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseModel> getVXML(Logger logger, String clientCode, String device, String langCode, String pageName) throws TelAppointException, Exception {
		BaseResponse baseResponse = new BaseResponse();
		Client client = cacheComponent.getClient(logger, clientCode, true);
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
		cacheComponent.loadVXML(jdbcCustomTemplate, logger, baseResponse, pageName, langCode, cache);
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, baseResponse), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseModel> getIVRVxml(Logger logger, String clientCode, String device) throws TelAppointException, Exception {
		IVRVxmlData ivrVXMLData = new IVRVxmlData();
		Client client = cacheComponent.getClient(logger, clientCode, true);
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		Map<String, IVRXml> ivrVXmlMap = new HashMap<String, IVRXml>();
		apptDAO.getIVRVxml(jdbcCustomTemplate, logger, ivrVXmlMap);
		ivrVXMLData.setIvrVxmlMap(ivrVXmlMap);
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, ivrVXmlMap), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseModel> getProcedure(Logger logger, String clientCode, String device, String langCode) throws TelAppointException, Exception {
		ProcedureListResponse procListRes = new ProcedureListResponse();
		Client client = cacheComponent.getClient(logger, clientCode, true);
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
		List<Options> optionsList = new ArrayList<Options>();

		Map<String, String> aliasMap = cacheComponent.getDisplayAliasMap(jdbcCustomTemplate, logger, device, langCode, cache);
		Map<String, String> labelMap = cacheComponent.getDisplayFieldLabelsMap(jdbcCustomTemplate, logger, device, langCode, cache);
		apptDAO.getProcedure(jdbcCustomTemplate, logger, device, optionsList, aliasMap, labelMap);
		if (CoreUtils.isIVR(device)) {
			String pageName = (optionsList.size() == 1) ? IVRVxmlConstants.SINGLE_PROCEDURE.getPageValue() : IVRVxmlConstants.PROCEDURE.getPageValue();
			cacheComponent.loadVXML(jdbcCustomTemplate, logger, procListRes, pageName, langCode, cache);
		}
		procListRes.setProcedureList(optionsList);
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, procListRes), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseModel> getProcedureId(Logger logger, String clientCode, String device, String procedureName) throws TelAppointException, Exception {
		ProcedureResponse procedureResponse = new ProcedureResponse();
		Client client = cacheComponent.getClient(logger, clientCode, true);
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		Procedure procedure = apptDAO.getProcedureByName(jdbcCustomTemplate, logger, procedureName, device);
		if (procedure != null && procedure.getProcedureId() != null) {
			procedureResponse.setProcedureId(procedure.getProcedureId());
		} else if (procedure == null || procedure.getProcedureId() == null) {
			procedureResponse.setStatus(false);
		}
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, procedureResponse), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseModel> getProcedureNoMatch(Logger logger, String clientCode, String device, String langCode, String procedureValue) throws TelAppointException,
			Exception {
		BaseResponse baseResponse = new BaseResponse();
		Client client = cacheComponent.getClient(logger, clientCode, true);
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
		Map<String, String> labelMap = cacheComponent.getDisplayFieldLabelsMap(jdbcCustomTemplate, logger, device, langCode, cache);

		Map<String, String> procedureMap = cacheComponent.getProcedureNoMatch(jdbcCustomTemplate, logger, langCode, cache);
		String procedureMsg = procedureMap.get(procedureValue);
		if (procedureMsg == null || "".equals(procedureMsg)) {
			procedureMsg = procedureMap.get("");
		}

		if (labelMap != null) {
			procedureMsg = labelMap.get(procedureMsg);
		} else {
			logger.error("alias data empty for device:" + device);
		}
		baseResponse.setMessage(procedureMsg);
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, baseResponse), HttpStatus.OK);
	}

	/**
	 * Based on procedure id and lookup the primary location time slots
	 * availability. if not available - it will loop through all secondary
	 * locations for that procedureId. if non are available - sends back first
	 * locationId and error message back.
	 */
	@Override
	public ResponseEntity<ResponseModel> getLocationPrimarySecondaryAvailability(Logger logger, String clientCode, String device, String langCode, String procedureId)
			throws TelAppointException, Exception {
		LocationPriSecAvailable locPriSecAvail = new LocationPriSecAvailable();
		Client client = cacheComponent.getClient(logger, clientCode, true);
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
		ClientDeploymentConfig cdConfig = cacheComponent.getClientDeploymentConfig(logger, clientCode, cache);
		List<Integer> locationIds = new ArrayList<Integer>();
		apptDAO.getLocationIdList(jdbcCustomTemplate, logger, procedureId, locationIds);

		if (!locationIds.isEmpty()) {
			locPriSecAvail.setLocationId(locationIds.get(0));
		} else {
			locPriSecAvail.setErrorFlag("Y");
			locPriSecAvail.setErrorMessage(ErrorConstants.ERROR_2994.getMessage());
			return new ResponseEntity<>(commonComponent.populateRMDData(logger, locPriSecAvail), HttpStatus.OK);
		}

		for (Integer locationId : locationIds) {
			Location location = apptDAO.getLocationById(jdbcCustomTemplate, logger, device, locationId);
			if ("N".equals(location.getClosed())) {
				apptDAO.getLocationAvailability(jdbcCustomTemplate, logger, cdConfig.getTimeZone(), locationId, cdConfig, locPriSecAvail);
				if (!locPriSecAvail.isTimeSlotsAvailable()) {
					logger.debug("Time slots are not available in locationId: " + location.getLocationId() + " trying next location.");
					continue;
				}
				locPriSecAvail.setLocationId(locationId);
				locPriSecAvail.setTimeSlotsAvailable(locPriSecAvail.isTimeSlotsAvailable());
				break;
			} else {
				logger.debug("Location closed, LocationId: " + location.getLocationId());
				locPriSecAvail.setTimeSlotsAvailable(false);
				continue;
			}
		}

		Map<String, String> labelMap = cacheComponent.getDisplayFieldLabelsMap(jdbcCustomTemplate, logger, device, langCode, cache);
		Location location = apptDAO.getLocationById(jdbcCustomTemplate, logger, device, locPriSecAvail.getLocationId());
		locPriSecAvail.setLocationClosed(location.getClosed());

		if (CoreUtils.isOnline(device) || CoreUtils.isMobile(device)) {
			// location closed check
			if ("Y".equals(location.getClosed())) {
				locPriSecAvail.setLocationErrorCode(location.getLocationErrorCode());
				locPriSecAvail.setLocationClosedDisplayMessage((labelMap != null && labelMap.get(location.getLocationErrorCode()) == null) ? "" : labelMap.get(location
						.getLocationErrorCode()));
				return new ResponseEntity<>(commonComponent.populateRMDData(logger, locPriSecAvail), HttpStatus.OK);
			}

			// location availability check
			if (!locPriSecAvail.isTimeSlotsAvailable()) {
				locPriSecAvail.setErrorFlag("Y");
				if (labelMap != null) {
					locPriSecAvail.setErrorMessage(labelMap.get(DisplayFieldLabelConstants.LOC_NO_APPTS.getValue()) == null ? ErrorConstants.ERROR_2996.getMessage() : labelMap
							.get(DisplayFieldLabelConstants.LOC_NO_APPTS.getValue()));
				} else {
					locPriSecAvail.setErrorMessage(ErrorConstants.ERROR_2996.getMessage());
				}
				return new ResponseEntity<>(commonComponent.populateRMDData(logger, locPriSecAvail), HttpStatus.OK);
			}

			// fill location details
			Map<String, String> aliasMap = cacheComponent.getDisplayAliasMap(jdbcCustomTemplate, logger, device, langCode, cache);
			if (aliasMap != null) {
				locPriSecAvail.setLocationName(aliasMap.get(location.getLocationName()));
			} else {
				locPriSecAvail.setLocationName(location.getLocationName());
			}
			locPriSecAvail.setLocationAddress(location.getAddress());
			locPriSecAvail.setLocationGoogleMap(location.getLocationGoogleMap());
			locPriSecAvail.setLocationGoogleMapLink(location.getLocationGoogleMapLink());
			locPriSecAvail.setCity(location.getCity());
			locPriSecAvail.setState(location.getState());
			locPriSecAvail.setZip(location.getZip());
			locPriSecAvail.setWorkPhone(location.getWorkPhone());
		} else if (CoreUtils.isIVR(device)) {
			// location closed - tts and audio
			if ("Y".equals(location.getClosed())) {
				locPriSecAvail.setLocationClosedTts(location.getClosedTts());
				locPriSecAvail.setLocationClosedAudio(location.getClosedAudio());
				return new ResponseEntity<>(commonComponent.populateRMDData(logger, locPriSecAvail), HttpStatus.OK);
			}
			// availability check
			if (!locPriSecAvail.isTimeSlotsAvailable()) {
				locPriSecAvail.setErrorFlag("Y");
				locPriSecAvail.setPageName("loc_no_appts");
				return new ResponseEntity<>(commonComponent.populateRMDData(logger, locPriSecAvail), HttpStatus.OK);
			}

			locPriSecAvail.setLocationNameTts(location.getLocationNameIvrTts());
			locPriSecAvail.setLocationNameAudio(location.getLocationNameIvrAudio());
		}
		if (location.getTimeZone() == null || "".equals(location.getTimeZone())) {
			locPriSecAvail.setTimeZone(location.getTimeZone());
		} else {
			locPriSecAvail.setTimeZone(cdConfig.getTimeZone());
		}
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, locPriSecAvail), HttpStatus.OK);
	}

	/**
	 * Used to authenticate the customer, if not exist, it will create the new
	 * customer. loginParam data format: fieldId1~fieldValue| etc
	 *
	 * @throws TelAppointException
	 *             , Exception
	 */
	@Override
	public ResponseEntity<ResponseModel> loginAuthenticate(Logger logger, CustomerInfoRequest custInfoReq) throws TelAppointException, Exception {
		String clientCode = custInfoReq.getClientCode();
		Client client = cacheComponent.getClient(logger, clientCode, true);
		boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		AuthResponse authResponse = loginAuthenticateInternal(logger,jdbcCustomTemplate, custInfoReq, cache);
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, authResponse), HttpStatus.CREATED);
	}
	
	
	/**
	 * Used to authenticate the customer, if not exist, it will create the new
	 * customer. loginParam data format: fieldId1~fieldValue| etc
	 *
	 * @throws TelAppointException
	 *             , Exception
	 */
	@Override
	public ResponseEntity<ResponseModel> loginMobileAuthenticate(Logger logger, CustomerInfoRequest custInfoReq) throws Exception {
		String clientCode = custInfoReq.getClientCode();
		Client client = cacheComponent.getClient(logger, clientCode, true);
		boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		AuthResponse authResponse = loginAuthenticateInternal(logger, jdbcCustomTemplate, custInfoReq, cache);
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, authResponse), HttpStatus.CREATED);
	}
	
	
	private AuthResponse loginAuthenticateInternal(Logger logger, JdbcCustomTemplate jdbcCustomTemplate, CustomerInfoRequest custInfoReq, boolean cache)  throws Exception {
		AuthResponse authResponse = new AuthResponse();
		String clientCode = custInfoReq.getClientCode();
		String device = custInfoReq.getDevice();
		String langCode = custInfoReq.getLangCode();
		ClientDeploymentConfig cdConfig = cacheComponent.getClientDeploymentConfig(logger, clientCode, cache);
		List<Integer> fieldIds = new ArrayList<>();
		List<String> fieldValues = new ArrayList<>();
		parseInputParamValues(custInfoReq.getInputParamValues(), fieldIds, fieldValues);
		Map<String, String> labelMap = cacheComponent.getDisplayFieldLabelsMap(jdbcCustomTemplate, logger, device, langCode, cache);
		List<OnlinePageFields> onlinePageFields = null;
		List<IVRPageFields> ivrPageFields = null;
		List<LoginPageFields> mobilePageFields = null;
		int size=0;
		if (CoreUtils.isOnline(device)) {
			onlinePageFields = apptDAO.getOnlinePageFieldsByPageFieldIds(jdbcCustomTemplate, logger, fieldIds);
			size = onlinePageFields.size();
		} else if(CoreUtils.isIVR(device)) {
			ivrPageFields = apptDAO.getIVRPageFieldsByPageFieldIds(jdbcCustomTemplate, logger, fieldIds);
			size = ivrPageFields.size();
		} else if(CoreUtils.isMobile(device)) {
			mobilePageFields = apptDAO.getMobilePageFieldsByPageFieldIds(jdbcCustomTemplate, logger, fieldIds);
			size = mobilePageFields.size();
		} else {
			
		}

		int inputArraySize = fieldIds.size();
		if (inputArraySize != size) {
			logger.error("Invalid input parameters passed from device: " + device);
			throw new TelAppointException(ErrorConstants.ERROR_2002.getCode(), ErrorConstants.ERROR_2002.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,
					"Invalid input parameters passed from device: " + device, custInfoReq.toString());
		}

		ApptSysConfig apptSysConfig = cacheComponent.getApptSysConfig(jdbcCustomTemplate, logger, cache);
		Class<OnlinePageFields> onlineClassType = OnlinePageFields.class;
		Class<IVRPageFields> ivrClassType = IVRPageFields.class;
		Class<LoginPageFields> mobileClassType = LoginPageFields.class;
		if (CoreUtils.isOnline(device)) {
			apptDAO.authenticateCustomer(jdbcCustomTemplate, logger, device, fieldIds, fieldValues, onlinePageFields, onlineClassType, authResponse, labelMap);
		} else if (CoreUtils.isIVR(device)) {
			apptDAO.authenticateCustomer(jdbcCustomTemplate, logger, device, fieldIds, fieldValues, ivrPageFields, ivrClassType, authResponse, labelMap);
		} else if (CoreUtils.isMobile(device)) {
			apptDAO.authenticateCustomer(jdbcCustomTemplate, logger, device, fieldIds, fieldValues, mobilePageFields, mobileClassType, authResponse, labelMap);
		}

		boolean noLogin = "N".equals(apptSysConfig.getEnforceLogin());
		if (CoreUtils.isAdmin(device)) {
			noLogin = true;
		}

		Customer customer;
		if (authResponse.isAuthSuccess() == false && noLogin) {
			customer = new Customer();

			if (CoreUtils.isOnline(device)) {
				populateCustomer(logger, device, customer, onlinePageFields, onlineClassType, fieldIds, fieldValues, "both");
			} else if (CoreUtils.isIVR(device)) {
				populateCustomer(logger, device, customer, ivrPageFields, ivrClassType, fieldIds, fieldValues, "both");
			} else if (CoreUtils.isMobile(device)) {
				populateCustomer(logger, device, customer, mobilePageFields, mobileClassType, fieldIds, fieldValues, "both");
			}

			if (customer.getFirstName() == null) {
				customer.setFirstName("");
			}
			if (customer.getLastName() == null) {
				customer.setLastName("");
			}
			long customerId = apptDAO.saveCustomer(jdbcCustomTemplate, logger, customer, cdConfig);
			if (CoreUtils.isIVR(device)) {
				customer.setCustomerId(customerId);
				customer.setFirstName("NEW");
				customer.setLastName("Customer-" + customerId);
				boolean isUpdated = apptDAO.updateCustomerForIVR(jdbcCustomTemplate, logger, customer, cdConfig);
				if (!isUpdated) {
					logger.error("IVR firstName and lastName not updated!");
				}
			}
			authResponse.setCustomerId(customerId);
			
			if("Y".equals(apptSysConfig.getCheckAssignedResource())) {
				authResponse.setResourceId(apptDAO.getResourceIdFromCustomer(jdbcCustomTemplate, customerId));
			}
		}

		if (authResponse.getCustomerId() <= 0) {
			authResponse.setAuthSuccess(false);
			authResponse.setAuthMessage((labelMap !=null && !"".equals(labelMap.get("CUSTOMER_NOT_FOUND")))?labelMap.get("CUSTOMER_NOT_FOUND"):ErrorConstants.ERROR_2996.getMessage());
		} else {
			customer = apptDAO.getCustomer(jdbcCustomTemplate, logger, authResponse.getCustomerId());
			authResponse.setAuthSuccess(true);
		}
		return authResponse;
	}

	/**
	 * Used to authenticate the customer, if not exist, it will create the new
	 * customer. loginParam data format: fieldId1~fieldValue| etc
	 *
	 * @throws TelAppointException
	 *             , Exception
	 */
	@Override
	public ResponseEntity<ResponseModel> authenticateAndUpdateCustomer(Logger logger, CustomerInfoRequest custInfoReq) throws TelAppointException, Exception {
		AuthResponse authResponse = new AuthResponse();
		String clientCode = custInfoReq.getClientCode();
		String device = custInfoReq.getDevice();
		String langCode = custInfoReq.getLangCode();
		Client client = cacheComponent.getClient(logger, clientCode, true);
		boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
		ClientDeploymentConfig cdConfig = cacheComponent.getClientDeploymentConfig(logger, clientCode, cache);
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);

		List<Integer> fieldIds = new ArrayList<Integer>();
		List<String> fieldValues = new ArrayList<String>();

		logger.info("authenticateAndUpdateCustomer API [ Input parameters] -  " + custInfoReq.getInputParamValues());
		parseInputParamValues(custInfoReq.getInputParamValues(), fieldIds, fieldValues);
		Map<String, String> labelMap = cacheComponent.getDisplayFieldLabelsMap(jdbcCustomTemplate, logger, device, langCode, cache);
		List<OnlinePageFields> onlinePageFields = null;
		List<IVRPageFields> ivrPageFields = null;
		List<LoginPageFields> mobilePageFields = null;
		int size=0;
		if (CoreUtils.isOnline(device)) {
			onlinePageFields = apptDAO.getOnlinePageFieldsByPageFieldIds(jdbcCustomTemplate, logger, fieldIds);
			size = onlinePageFields.size();
		} else if(CoreUtils.isIVR(device)){
			ivrPageFields = apptDAO.getIVRPageFieldsByPageFieldIds(jdbcCustomTemplate, logger, fieldIds);
			size = ivrPageFields.size();
		} else if(CoreUtils.isMobile(device)) {
			mobilePageFields = apptDAO.getMobilePageFieldsByPageFieldIds(jdbcCustomTemplate, logger, fieldIds);
			size = mobilePageFields.size();
		} else {
			//handle it
		}

		int inputArraySize = fieldIds.size();
		if (inputArraySize != size) {
			logger.error("Invalid input parameters passed from device: " + device);
			throw new TelAppointException(ErrorConstants.ERROR_2002.getCode(), ErrorConstants.ERROR_2002.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,
					"Invalid input parameters passed from device: " + device, custInfoReq.toString());
		}

		ApptSysConfig apptSysConfig = cacheComponent.getApptSysConfig(jdbcCustomTemplate, logger, cache);
		Class<OnlinePageFields> onlineClassType = OnlinePageFields.class;
		Class<IVRPageFields> ivrClassType = IVRPageFields.class;
		Class<LoginPageFields> mobileClassType = LoginPageFields.class;
		if (CoreUtils.isOnline(device)) {
			apptDAO.authenticateCustomer(jdbcCustomTemplate, logger, device, fieldIds, fieldValues, onlinePageFields, onlineClassType, authResponse, labelMap);
		} else if (CoreUtils.isIVR(device)) {
			apptDAO.authenticateCustomer(jdbcCustomTemplate, logger, device, fieldIds, fieldValues, ivrPageFields, ivrClassType, authResponse, labelMap);
		} else if (CoreUtils.isMobile(device)) {
			apptDAO.authenticateCustomer(jdbcCustomTemplate, logger, device, fieldIds, fieldValues, mobilePageFields, mobileClassType, authResponse, labelMap);
		}

		boolean noLogin = "N".equals(apptSysConfig.getEnforceLogin());
		if (CoreUtils.isAdmin(device)) {
			noLogin = true;
		}

		Customer customer;
		if (authResponse.isAuthSuccess() == false && noLogin) {
			customer = new Customer();
			if (CoreUtils.isOnline(device)) {
				populateCustomer(logger, device, customer, onlinePageFields, onlineClassType, fieldIds, fieldValues, "both");
			} else if (CoreUtils.isIVR(device)) {
				populateCustomer(logger, device, customer, ivrPageFields, ivrClassType, fieldIds, fieldValues, "both");
			} else if (CoreUtils.isMobile(device)) {
				populateCustomer(logger, device, customer, mobilePageFields, mobileClassType, fieldIds, fieldValues, "both");
			}

			if (customer.getFirstName() == null) {
				customer.setFirstName("");
			}
			if (customer.getLastName() == null) {
				customer.setLastName("");
			}
			long customerId = apptDAO.saveCustomer(jdbcCustomTemplate, logger, customer, cdConfig);
			if (CoreUtils.isIVR(device)) {
				customer.setCustomerId(customerId);
				customer.setFirstName("NEW");
				customer.setLastName("Customer-" + customerId);
				boolean isUpdated = apptDAO.updateCustomerForIVR(jdbcCustomTemplate, logger, customer, cdConfig);
				if (!isUpdated) {
					logger.error("IVR firstName and lastName not updated!");
				}
			}
			authResponse.setCustomerId(customerId);
			if("Y".equals(apptSysConfig.getCheckAssignedResource())) {
				authResponse.setResourceId(apptDAO.getResourceIdFromCustomer(jdbcCustomTemplate, customerId));
			}
		}

		if (authResponse.getCustomerId() <= 0) {
			authResponse.setAuthSuccess(false);
			authResponse.setAuthMessage((labelMap !=null && !"".equals(labelMap.get("CUSTOMER_NOT_FOUND")))?labelMap.get("CUSTOMER_NOT_FOUND"):ErrorConstants.ERROR_2996.getMessage());
		} else {
			customer = apptDAO.getCustomer(jdbcCustomTemplate, logger, authResponse.getCustomerId());
			StringBuilder sql = new StringBuilder("update customer set id=:customerId ");
			MapSqlParameterSource paramSource = new MapSqlParameterSource();
			if (CoreUtils.isOnline(device)) {
				populateCustomerQuery(logger, device, onlinePageFields, fieldValues, "update", sql, paramSource, authResponse.getCustomerId());
			} else if (CoreUtils.isIVR(device)) {
				populateCustomerQuery(logger, device, ivrPageFields, fieldValues, "update", sql, paramSource, authResponse.getCustomerId());
			} else if (CoreUtils.isMobile(device)) {
				populateCustomerQuery(logger, device, mobilePageFields, fieldValues, "update", sql, paramSource, authResponse.getCustomerId());
			}
			apptDAO.updateCustomer(jdbcCustomTemplate, logger, sql, paramSource, cdConfig);
			authResponse.setAuthSuccess(true);
		}
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, authResponse), HttpStatus.CREATED);
	}

	/**
	 * Used to authenticate the customer, if not exist, it will return back with
	 * Customer not found message. loginParam data format: fieldId1~fieldValue|
	 * etc
	 *
	 * @throws TelAppointException
	 *             , Exception
	 */
	@Override
	public ResponseEntity<ResponseModel> authenticateForCancel(Logger logger, String clientCode, String langCode, String device, String inputParamValues)
			throws TelAppointException, Exception {
		AuthResponse authResponse = new AuthResponse();
		Client client = cacheComponent.getClient(logger, clientCode, true);
		boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		List<Integer> fieldIds = new ArrayList<Integer>();
		List<String> fieldValues = new ArrayList<String>();
		parseInputParamValues(inputParamValues, fieldIds, fieldValues);
		Map<String, String> labelMap = cacheComponent.getDisplayFieldLabelsMap(jdbcCustomTemplate, logger, device, langCode, cache);
		List<OnlinePageFields> onlinePageFields = null;
		List<IVRPageFields> ivrPageFields = null;
		List<LoginPageFields> mobilePageFields = null;
		int size=0;
		if (CoreUtils.isOnline(device)) {
			onlinePageFields = apptDAO.getOnlinePageFieldsByPageFieldIds(jdbcCustomTemplate, logger, fieldIds);
			size = onlinePageFields.size();
		} else if(CoreUtils.isIVR(device)) {
			ivrPageFields = apptDAO.getIVRPageFieldsByPageFieldIds(jdbcCustomTemplate, logger, fieldIds);
			size = ivrPageFields.size();
		} else if(CoreUtils.isMobile(device)) {
			mobilePageFields = apptDAO.getMobilePageFieldsByPageFieldIds(jdbcCustomTemplate, logger, fieldIds);
			size = mobilePageFields.size();
		}

		int inputArraySize = fieldIds.size();
		if (inputArraySize != size) {
			logger.error("Invalid input parameters passed from device: " + device);
			throw new TelAppointException(ErrorConstants.ERROR_2002.getCode(), ErrorConstants.ERROR_2002.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,
					"Invalid input parameters passed from device: " + device, "Data: " + inputParamValues);
		}

		ApptSysConfig apptSysConfig = cacheComponent.getApptSysConfig(jdbcCustomTemplate, logger, cache);
		Class<OnlinePageFields> onlineClassType = OnlinePageFields.class;
		Class<IVRPageFields> ivrClassType = IVRPageFields.class;
		Class<LoginPageFields> mobileClassType = LoginPageFields.class;
		if (CoreUtils.isOnline(device)) {
			apptDAO.authenticateCustomer(jdbcCustomTemplate, logger, device, fieldIds, fieldValues, onlinePageFields, onlineClassType, authResponse, labelMap);
		} else if (CoreUtils.isIVR(device)) {
			apptDAO.authenticateCustomer(jdbcCustomTemplate, logger, device, fieldIds, fieldValues, ivrPageFields, ivrClassType, authResponse, labelMap);
		} else if (CoreUtils.isMobile(device)) {
			apptDAO.authenticateCustomer(jdbcCustomTemplate, logger, device, fieldIds, fieldValues, mobilePageFields, mobileClassType, authResponse, labelMap);
		}

		boolean noLogin = "N".equals(apptSysConfig.getEnforceLogin());
		if (CoreUtils.isAdmin(device)) {
			noLogin = true;
		}
		Customer customer;
		if (!authResponse.isAuthSuccess() && noLogin) {
			if (authResponse.getCustomerId() <= 0) {
				authResponse.setAuthSuccess(false);
				if (labelMap != null && (CoreUtils.isOnline(device) || CoreUtils.isMobile(device)) ) {
					authResponse.setAuthMessage(labelMap.get("CUSTOMER_NOT_FOUND") == null ? ErrorConstants.ERROR_2996.getMessage() : labelMap.get("CUSTOMER_NOT_FOUND"));
				}
			}
		} else {
			customer = apptDAO.getCustomer(jdbcCustomTemplate, logger, authResponse.getCustomerId());
			if (CoreUtils.isIVR(device)) {
				customer.setLastName("Customer-" + authResponse.getCustomerId());
			}
			authResponse.setAuthSuccess(true);
		}
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, authResponse), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseModel> getCustomerType(Logger logger, String clientCode, String device, String langCode, Long customerId, Integer locationId)
			throws TelAppointException, Exception {
		ServiceFundsResponse serviceFundsResponse = new ServiceFundsResponse();
		Client client = cacheComponent.getClient(logger, clientCode, true);
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
		Map<String, String> aliasMap = cacheComponent.getDisplayAliasMap(jdbcCustomTemplate, logger, device, langCode, cache);
		Map<String, String> labelMap = cacheComponent.getDisplayFieldLabelsMap(jdbcCustomTemplate, logger, device, langCode, cache);
		apptDAO.getCustomerType(jdbcCustomTemplate, logger, device, customerId, locationId, serviceFundsResponse, aliasMap, labelMap);
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, serviceFundsResponse), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseModel> getUtilityType(Logger logger, String clientCode, String device, String langCode, Integer serviceFundsRcvdId, Integer customerTypeId,
			Integer locationId) throws TelAppointException, Exception {
		ServiceFundsResponse serviceFundsResponse = new ServiceFundsResponse();
		Client client = cacheComponent.getClient(logger, clientCode, true);
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
		Map<String, String> aliasMap = cacheComponent.getDisplayAliasMap(jdbcCustomTemplate, logger, device, langCode, cache);
		Map<String, String> labelMap = cacheComponent.getDisplayFieldLabelsMap(jdbcCustomTemplate, logger, device, langCode, cache);
		apptDAO.getUtilityType(jdbcCustomTemplate, logger, device, serviceFundsRcvdId, customerTypeId, locationId, serviceFundsResponse, aliasMap, labelMap);
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, serviceFundsResponse), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseModel> getServiceNameCustomerUtility(Logger logger, String clientCode, String device, String langCode, Integer serviceFundsRcvdId,
			Integer customerTypeId, Integer locationId, Integer utilityTypeId) throws TelAppointException, Exception {
		ServiceFundsResponse serviceFundsResponse = new ServiceFundsResponse();
		Client client = cacheComponent.getClient(logger, clientCode, true);
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
		Map<String, String> aliasMap = cacheComponent.getDisplayAliasMap(jdbcCustomTemplate, logger, device, langCode, cache);
		Map<String, String> labelMap = cacheComponent.getDisplayFieldLabelsMap(jdbcCustomTemplate, logger, device, langCode, cache);
		apptDAO.getServiceNameCustomerUtility(jdbcCustomTemplate, logger, device, serviceFundsRcvdId, customerTypeId, locationId, utilityTypeId, serviceFundsResponse, aliasMap,
				labelMap);
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, serviceFundsResponse), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseModel> getServiceNameCustomerWarningPage(Logger logger, String clientCode, String device, String langCode, Integer serviceFundsRcvdId,
			Integer customerTypeId, Integer utilityTypeId, Integer serviceId) throws TelAppointException, Exception {
		ServiceFundsResponse serviceFundsResponse = new ServiceFundsResponse();
		Client client = cacheComponent.getClient(logger, clientCode, true);
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
		Map<String, String> labelMap = cacheComponent.getDisplayFieldLabelsMap(jdbcCustomTemplate, logger, device, langCode, cache);
		apptDAO.getServiceNameCustomerWarningPage(jdbcCustomTemplate, logger, device, serviceFundsRcvdId, customerTypeId, utilityTypeId, serviceId, serviceFundsResponse, labelMap);
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, serviceFundsResponse), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseModel> getSingleServiceNotClosed(Logger logger, String clientCode, String device, String langCode, String serviceIds) throws TelAppointException,
			Exception {
		SingleServiceNotClosed singleServiceNotClosed = new SingleServiceNotClosed();
		Client client = cacheComponent.getClient(logger, clientCode, true);
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		Map<String, com.telappoint.apptdesk.model.Service> serviceMap = new HashMap<String, com.telappoint.apptdesk.model.Service>();
		apptDAO.getServices(jdbcCustomTemplate, logger, serviceMap);

		if (serviceIds != null && !"".equals(serviceIds)) {
			String[] serviceIdArray = serviceIds.trim().split("\\,");
			for (int i = 0; i < serviceIdArray.length; i++) {
				if (i == 0) {
					singleServiceNotClosed.setServiceId(Integer.valueOf(serviceIdArray[i]));
				}
				com.telappoint.apptdesk.model.Service service = serviceMap.get(serviceIdArray[i]);
				if ("N".equals(service.getClosed())) {
					singleServiceNotClosed.setServiceId(Integer.valueOf(serviceIdArray[i]));
					break;
				}
			}
		} else {
			throw new TelAppointException(ErrorConstants.ERROR_1066.getCode(), ErrorConstants.ERROR_1066.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,
					"ServiceIds not passed from front end", "ServiceIds: " + serviceIds);
		}

		return new ResponseEntity<>(commonComponent.populateRMDData(logger, singleServiceNotClosed), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseModel> getServiceClosedStatus(Logger logger, String clientCode, String device, String langCode, Integer serviceId, Integer locationId) throws TelAppointException,
			Exception {
		ServiceStatus serviceStatus = new ServiceStatus();
		Client client = cacheComponent.getClient(logger, clientCode, true);
		boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		Map<String, String> aliasLabel = cacheComponent.getDisplayFieldLabelsMap(jdbcCustomTemplate, logger, device, langCode, cache);
		Map<String, com.telappoint.apptdesk.model.Service> serviceMap = new HashMap<String, com.telappoint.apptdesk.model.Service>();
		apptDAO.getServices(jdbcCustomTemplate, logger, serviceMap);
		com.telappoint.apptdesk.model.Service service = serviceMap.get("" + serviceId);
		if (service != null && "Y".equals(service.getClosed())) {
			if(service.getClosedLocationIds() == null || "".equals(service.getClosedLocationIds()) || service.getClosedLocationIds().contains(String.valueOf(locationId))) {
				serviceStatus.setClosed(true);
				if (CoreUtils.isOnline(device)) {
					if (aliasLabel == null) {
						serviceStatus.setClosedMessage(ErrorConstants.ERROR_2996.getMessage());
					} else {
						serviceStatus.setClosedMessage(aliasLabel.get(service.getClosedMessage()) == null ? ErrorConstants.ERROR_2996.getMessage() : aliasLabel.get(service
								.getClosedMessage()));
					}
				} else if (CoreUtils.isIVR(device)) {
					serviceStatus.setClosedAudio(service.getClosedAudio().toLowerCase());
					serviceStatus.setClosedTts(service.getClosedTts().toLowerCase());
				}
			}
		}
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, serviceStatus), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseModel> getServiceTimeSlotsAvailableStatus(Logger logger, String clientCode, String device, String langCode, Integer locationId, Integer serviceId)
			throws TelAppointException, Exception {
		ServiceTimeSlotsAvailableStatus serviceTimeSlotsAvailableStatus = new ServiceTimeSlotsAvailableStatus();
		Client client = cacheComponent.getClient(logger, clientCode, true);
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
		ClientDeploymentConfig cdConfig = cacheComponent.getClientDeploymentConfig(logger, clientCode, cache);
		Map<String, String> labelMap = cacheComponent.getDisplayFieldLabelsMap(jdbcCustomTemplate, logger, device, langCode, cache);
		apptDAO.getServiceAvailability(jdbcCustomTemplate, logger, locationId, serviceId, cdConfig, serviceTimeSlotsAvailableStatus);
		if (!serviceTimeSlotsAvailableStatus.isAvailable()) {
			if (CoreUtils.isOnline(device) || CoreUtils.isMobile(device)) {
				if (labelMap != null) {
					serviceTimeSlotsAvailableStatus.setMessage(labelMap.get("SER_NO_APPTS") == null ? ErrorConstants.ERROR_2996.getMessage() : labelMap.get("SER_NO_APPTS"));
				} else {
					serviceTimeSlotsAvailableStatus.setMessage(ErrorConstants.ERROR_2996.getMessage());
				}
			} else if (CoreUtils.isIVR(device)) {
				serviceTimeSlotsAvailableStatus.setPageName("ser_no_appts");
			}
		}
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, serviceTimeSlotsAvailableStatus), HttpStatus.OK);
	}

	/**
	 * Used the hold the first Available time slot and return back with date and
	 * time. Holding will be done with three steps i) GetFirstAvailableDateTime.
	 * ii) TempHold the time slot. iii) Insert the appointment data with hold
	 * status in schedule table.
	 *
	 * @throws TelAppointException
	 *             , Exception
	 */
	@Override
	public ResponseEntity<ResponseModel> holdFirstAvailableAppointment(Logger logger, HoldAppointmentRequest holdAppointmentRequest) throws TelAppointException, Exception {
		HoldAppointmentResponse holdAppointmentResponse = new HoldAppointmentResponse();
		String device = holdAppointmentRequest.getDevice();
		logger.info(holdAppointmentRequest.toString());
		if (holdAppointmentRequest.getCustomerId() == null || holdAppointmentRequest.getCustomerId() == 0) {
			throw new TelAppointException(ErrorConstants.ERROR_1048.getCode(), ErrorConstants.ERROR_1048.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,
					"Customer Id is null or Zero", null);
		}

		if (holdAppointmentRequest.getLocationId() == null || holdAppointmentRequest.getLocationId() == 0) {
			throw new TelAppointException(ErrorConstants.ERROR_1059.getCode(), ErrorConstants.ERROR_1059.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,
					"LocationId is null or Zero", null);
		}

		if (holdAppointmentRequest.getServiceId() == null || holdAppointmentRequest.getServiceId() == 0) {
			throw new TelAppointException(ErrorConstants.ERROR_1060.getCode(), ErrorConstants.ERROR_1060.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,
					"ServiceId is null or Zero", null);
		}
		Client client = cacheComponent.getClient(logger, holdAppointmentRequest.getClientCode(), true);
		boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		Map<String, String> labelMap = cacheComponent.getDisplayFieldLabelsMap(jdbcCustomTemplate, logger, holdAppointmentRequest.getDevice(),
				holdAppointmentRequest.getLangCode(), cache);
		ClientDeploymentConfig cdConfig = cacheComponent.getClientDeploymentConfig(logger, holdAppointmentRequest.getClientCode(), cache);

		boolean isOutLoop = false;
		TempHoldAppt tempHoldAppt = null;
		int count = 0;
		while (count <= 2) {
			count = count + 1;
			logger.debug("Calling getFirstAvailableDateTime storedProcedure");
			FirstAvailableDateTime firstAvailableDateTime = apptDAO.getApptFirstAvailDateTimeCallCenter(jdbcCustomTemplate, logger, holdAppointmentRequest, cdConfig);
			// Return data as
			// '2015-05-24|1-10:00,10:15,16:00,16:15~2-09:30,11:15|3-08:30'
			String spResponse = firstAvailableDateTime.getAvailableDateTime();
			logger.info("get_first_avail_date_times_callcenter_sp response : " + spResponse);

			if (spResponse != null && "LOC_NO_APPTS".equals(spResponse)) {
				holdAppointmentResponse.setErrorFlag("Y");
				if (CoreUtils.isOnline(device)) {
					if (labelMap != null) {
						holdAppointmentResponse.setErrorMessage(labelMap.get(spResponse.trim()) == null ? ErrorConstants.ERROR_2996.getMessage() : labelMap.get(spResponse));
					} else {
						holdAppointmentResponse.setErrorMessage(ErrorConstants.ERROR_2996.getMessage());
					}
				} else if (CoreUtils.isIVR(device)) {
					holdAppointmentResponse.setPageName(spResponse.trim().toLowerCase());
				}
				return new ResponseEntity<>(commonComponent.populateRMDData(logger, holdAppointmentResponse), HttpStatus.CREATED);
			}

			if (!"".equals(spResponse.trim())) {
				// Return data as
				// '2015-05-24|1-10:00,10:15,16:00,16:15~2-09:30,11:15~3-03:30'
				// for loop resource Id's
				// inside of for loop for time slots.
				// inside call temp hold stored procedure.

				String[] resArray = spResponse.split("\\|");
				if (resArray.length >= 2) {
					String date = resArray[0];
					for (int i = 1; i < resArray.length; i++) {
						String times = resArray[i];
						List<TimeSlots> timeList = getSortedFirstAvailDate(logger, times);
						logger.info("Sorted times of get_first_avail_date_times_callcenter_sp - " + timeList.toString());
						for (TimeSlots timeSlots : timeList) {
							String dateTime = date + " " + timeSlots.getTime();
							synchronized (lock) {
								holdAppointmentRequest.setResourceId(Integer.valueOf(timeSlots.getResourceId()));
								tempHoldAppt = apptDAO.holdAppt(jdbcCustomTemplate, logger, Integer.valueOf(timeSlots.getResourceId()), dateTime, holdAppointmentRequest.getServiceId(),
										holdAppointmentRequest.getCustomerId(), cdConfig);
							}
							if (tempHoldAppt != null && tempHoldAppt.getHoldId().longValue() < -1) {
								apptDAO.holdFirstAvailableAppointment(jdbcCustomTemplate, logger, holdAppointmentRequest, dateTime, tempHoldAppt, cdConfig, holdAppointmentResponse);
								holdAppointmentResponse.setApptDateTime(dateTime);
								isOutLoop = true;
								break;
							} else {
								if (tempHoldAppt == null) {
									logger.error("temp hold SP response is null, So trying to hold next time slot");
								}
								continue;
							}
						}

						if (isOutLoop) {
							break;
						}
					}
				} else {
					logger.error("Error while process the hold appointment.");
					break;
				}
			} else {
				logger.error("getFirstAvailableDate time stored procedure response:" + spResponse);
				break;
			}

			if (isOutLoop) {
				break;
			}

		}

		if (!isOutLoop) {
			holdAppointmentResponse.setErrorFlag("Y");
			if (CoreUtils.isOnline(holdAppointmentRequest.getDevice())) {
				if (labelMap != null) {
					holdAppointmentResponse.setErrorMessage(labelMap.get(tempHoldAppt.getErrorMessage()) == null ? ErrorConstants.ERROR_2996.getMessage() : labelMap
							.get(tempHoldAppt.getErrorMessage()));
				} else {
					holdAppointmentResponse.setErrorMessage(ErrorConstants.ERROR_2996.getMessage());
				}
			} else if (CoreUtils.isIVR(holdAppointmentRequest.getDevice())) {
				holdAppointmentResponse.setPageName(tempHoldAppt.getErrorMessage().toLowerCase());
			}
		}

		return new ResponseEntity<>(commonComponent.populateRMDData(logger, holdAppointmentResponse), HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<ResponseModel> getCustomerInfo(Logger logger, String clientCode, String device, Long customerId) throws TelAppointException, Exception {
		CustomerInfo customerInfo = new CustomerInfo();
		Client client = cacheComponent.getClient(logger, clientCode, true);
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		apptDAO.getCustomerInfo(jdbcCustomTemplate, logger, device, customerId, customerInfo);
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, customerInfo), HttpStatus.OK);
	}

	/**
	 * Used to confirm the appointment, It will do with two steps. 1. Confirm
	 * the appointment using book appointment stored procedure. 2. Send an email
	 * with Asynchronous call. if any error it will log it without throwing the
	 * exception.
	 *
	 * @throws TelAppointException
	 *             , Exception
	 * 
	 */
	@Override
	public ResponseEntity<ResponseModel> confirmAppointment(Logger logger, ConfirmAppointmentRequest confirmApptReq) throws TelAppointException, Exception {
		Client client = cacheComponent.getClient(logger, confirmApptReq.getClientCode(), true);
		boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		ClientDeploymentConfig cdConfig = cacheComponent.getClientDeploymentConfig(logger, confirmApptReq.getClientCode(), cache);
		ConfirmAppointmentResponse confirmApptRes = bookppointment(jdbcCustomTemplate, logger, confirmApptReq, cdConfig);
		String errorMessage = confirmApptRes.getMessage();
		Map<String, String> emailData = new HashMap<String, String>();
		String emailType = "confirm";
		sendConfirmEmail(logger, errorMessage, jdbcCustomTemplate, client, cdConfig, confirmApptReq, confirmApptRes, emailData, emailType);
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, confirmApptRes), HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<ResponseModel> confirmAppointmentForFileUpload(Logger logger, ConfirmAppointmentRequest confirmApptReq) throws TelAppointException, Exception {
		Client client = cacheComponent.getClient(logger, confirmApptReq.getClientCode(), true);
		boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		ClientDeploymentConfig cdConfig = cacheComponent.getClientDeploymentConfig(logger, confirmApptReq.getClientCode(), cache);
		ConfirmAppointmentResponse confirmApptRes = bookppointment(jdbcCustomTemplate, logger, confirmApptReq, cdConfig);
		String errorMessage = confirmApptRes.getMessage();

		Map<String, String> emailData = new HashMap<String, String>();
		fillTransScriptFilesPlaceHolder(jdbcCustomTemplate, logger, confirmApptReq.getScheduleId(), emailData);
		String emailType = "other";
		sendConfirmEmail(logger, errorMessage, jdbcCustomTemplate, client, cdConfig, confirmApptReq, confirmApptRes, emailData, emailType);
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, confirmApptRes), HttpStatus.CREATED);
	}

	private void fillTransScriptFilesPlaceHolder(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, long scheduleId, Map<String, String> emailData) {
		try {
			List<TransScript> transScriptList = apptDAO.getTransScriptMsgs(jdbcCustomTemplate, logger, scheduleId);
			StringBuilder fileNames = new StringBuilder();
			for (TransScript transScript : transScriptList) {
				fileNames.append(transScript.getFileName()).append("<br>");
			}
			emailData.put("%FILE_UPLOAD_FILENAMES%", fileNames.toString());
			logger.info("TransScriptFileNames: " + fileNames.toString());
		} catch (Exception e) {
			logger.error("Error - You can ignore this error if non trans script clients :" + e, e);
			emailData.put("%FILE_UPLOAD_FILENAMES%", "");
		}
	}

	private ConfirmAppointmentResponse bookppointment(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, ConfirmAppointmentRequest confirmApptReq,
			ClientDeploymentConfig cdConfig) throws TelAppointException, Exception {
		ConfirmAppointmentResponse confirmApptRes = new ConfirmAppointmentResponse();
		String device = confirmApptReq.getDevice();
		Integer apptMethod;
		if (CoreUtils.isIVR(device)) {
			apptMethod = AppointmentMethod.IVR.getMethod();
		} else if (CoreUtils.isOnline(device)) {
			apptMethod = AppointmentMethod.ONLINE.getMethod();
		} else if (CoreUtils.isMobile(device)) {
			apptMethod = AppointmentMethod.MOBILE.getMethod();
		}  else {
			apptMethod = AppointmentMethod.ADMIN.getMethod();
		}
		apptDAO.bookAppointment(jdbcCustomTemplate, logger, confirmApptReq.getScheduleId(), confirmApptReq.getDevice(), confirmApptReq.getLangCode(), apptMethod, cdConfig,
				confirmApptRes);
		
		return confirmApptRes;
	}

	@Override
	public ResponseEntity<ResponseModel> getBookedAppointments(Logger logger, String clientCode, String device, String langCode, Long customerId) throws TelAppointException,
			Exception {
		AppointmentsDataResponse apptDataResponse = new AppointmentsDataResponse();
		Client client = cacheComponent.getClient(logger, clientCode, true);
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;

		List<AppointmentDetails>  apptList = getBookedAppts(logger, jdbcCustomTemplate, cache, device, langCode, customerId);
		apptDataResponse.setBookedAppts(apptList);
		apptDataResponse.setClientName(client.getClientName());
		if (apptList.isEmpty()) {
			Map<String, String> labelMap = cacheComponent.getDisplayFieldLabelsMap(jdbcCustomTemplate, logger, device, langCode, cache);
			if (labelMap != null) {
				if(CoreUtils.isOnline(device) || CoreUtils.isMobile(device)) {
					apptDataResponse.setMessage(labelMap.get(DisplayFieldLabelConstants.NO_BOOKED_APPTS.getValue()) == null ? ErrorConstants.ERROR_2996.getMessage() : labelMap
						.get(DisplayFieldLabelConstants.NO_BOOKED_APPTS.getValue()));
				}
			}
		} 
		
		if(CoreUtils.isMobile(device)) {
			MobileAppPage mobileAppPage = apptDAO.getMobileAppPages(jdbcCustomTemplate, "existing_appt_page");
			apptDataResponse.setPageData(getMobileAppPages(mobileAppPage));
		}
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, apptDataResponse), HttpStatus.OK);
	}
	
	
	private List<AppointmentDetails> getBookedAppts(Logger logger, JdbcCustomTemplate jdbcCustomTemplate, boolean cache, String device, String langCode, Long customerId) throws Exception {
		
		List<AppointmentDetails> apptList = new ArrayList<>();
		
		ClientDeploymentConfig cdConfig = cacheComponent.getClientDeploymentConfig(logger, jdbcCustomTemplate.getClientCode(), cache);
		Map<String, String> aliasMap = cacheComponent.getDisplayAliasMap(jdbcCustomTemplate, logger, device, langCode, cache);

		String displayForamt = null;
		if (CoreUtils.isOnline(device)) {
			ApptSysConfig apptSysConfig = apptDAO.getApptSysConfig(jdbcCustomTemplate, logger);
			displayForamt = apptSysConfig.getOnlineDateTimeFormat();
		} else {
			displayForamt ="%m/%d/%Y %h:%i %p";
		}

		apptDAO.getBookedAppointments(jdbcCustomTemplate, logger, device, customerId, cdConfig, displayForamt, apptList, aliasMap);
		return apptList;
	}

	@Override
	public ResponseEntity<ResponseModel> getListOfThingsToBring(Logger logger, String clientCode, String device, String langCode, Integer serviceId) throws TelAppointException,
			Exception {
		ListOfThingsResponse listOfThingsResponse = new ListOfThingsResponse();
		Client client = cacheComponent.getClient(logger, clientCode, true);
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		apptDAO.getListOfThingsToBring(jdbcCustomTemplate, logger, device, langCode, serviceId, listOfThingsResponse);
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, listOfThingsResponse), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseModel> getHouseholdMonthlyIncome(Logger logger, String clientCode, String device, String langCode, String noOfPeople, Integer serviceId)
			throws TelAppointException, Exception {
		HouseHoldMonthlyIncomeResponse houseHoldMonthlyIncome = new HouseHoldMonthlyIncomeResponse();
		Client client = cacheComponent.getClient(logger, clientCode, true);
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		apptDAO.getHouseholdMonthlyIncome(jdbcCustomTemplate, logger, device, noOfPeople, serviceId, houseHoldMonthlyIncome);
		if (houseHoldMonthlyIncome.getMonthlyIncome() == null) {
			logger.error("No Monthly income fetching for noOfPeople - " + noOfPeople + ", serviceId - " + serviceId);
			return new ResponseEntity<>(commonComponent.populateRMDData(logger, houseHoldMonthlyIncome), HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, houseHoldMonthlyIncome), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseModel> updateCustomerInfo(Logger logger, UpdateCustomerInfoRequest customerInfoRequest) throws TelAppointException, Exception {
		Client client = cacheComponent.getClient(logger, customerInfoRequest.getClientCode(), true);
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		String clientCode = customerInfoRequest.getClientCode();
		String device = customerInfoRequest.getDevice();
		boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
		ClientDeploymentConfig cdConfig = cacheComponent.getClientDeploymentConfig(logger, clientCode, cache);

		List<Integer> fieldIds = new ArrayList<Integer>();
		List<String> fieldValues = new ArrayList<String>();
		List<OnlinePageFields> onlinePageFields = null;
		List<IVRPageFields> ivrPageFields = null;
		List<LoginPageFields> mobilePageFields = null;

		parseInputParamValues(customerInfoRequest.getInputParamValues(), fieldIds, fieldValues);
		int size = 0;
		if (CoreUtils.isOnline(device)) {
			onlinePageFields = apptDAO.getOnlinePageFieldsByPageFieldIds(jdbcCustomTemplate, logger, fieldIds);
			onlinePageFields = getOnlinePageOptionFields(onlinePageFields);
			size = onlinePageFields.size();
		} else if (CoreUtils.isIVR(device)) {
			ivrPageFields = apptDAO.getIVRPageFieldsByPageFieldIds(jdbcCustomTemplate, logger, fieldIds);
			ivrPageFields = getIVRPageOptionFields(ivrPageFields);
			size = ivrPageFields.size();
		} else if (CoreUtils.isMobile(device)) {
			mobilePageFields = apptDAO.getMobilePageFieldsByPageFieldIds(jdbcCustomTemplate, logger, fieldIds);
			mobilePageFields = getMobilePageOptionFields(mobilePageFields);
			size = mobilePageFields.size();
		}

		int inputArraySize = fieldIds.size();
		if (inputArraySize != size) {
			logger.error("Invalid input parameters passed from device: " + device);
			throw new TelAppointException(ErrorConstants.ERROR_2002.getCode(), ErrorConstants.ERROR_2002.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,
					"Invalid input parameters passed from device: " + device, customerInfoRequest.toString());
		}

		StringBuilder sql = new StringBuilder("update customer set id=:customerId ");
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		if (CoreUtils.isOnline(device)) {
			populateCustomerQuery(logger, customerInfoRequest.getDevice(), onlinePageFields, fieldValues, "update", sql, paramSource, customerInfoRequest.getCustomerId());
		} else if (CoreUtils.isIVR(device)) {
			populateCustomerQuery(logger, customerInfoRequest.getDevice(), ivrPageFields, fieldValues, "update", sql, paramSource, customerInfoRequest.getCustomerId());
		} else if (CoreUtils.isMobile(device)) {
			populateCustomerQuery(logger, customerInfoRequest.getDevice(), mobilePageFields, fieldValues, "update", sql, paramSource, customerInfoRequest.getCustomerId());
		}
		apptDAO.updateCustomer(jdbcCustomTemplate, logger, sql, paramSource, cdConfig);
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, new BaseResponse()), HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<ResponseModel> updateNameRecord(Logger logger, NameRecordInfo nameRecordInfo) throws TelAppointException, Exception {
		Client client = cacheComponent.getClient(logger, nameRecordInfo.getClientCode(), true);
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		apptDAO.updateNameRecord(jdbcCustomTemplate, logger, nameRecordInfo);
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, new BaseResponse()), HttpStatus.CREATED);
	}

	private void parseInputParamValues(String inputParamValues, List<Integer> fieldIds, List<String> fieldValues) {
		String[] fields = inputParamValues.split("\\|", -1);
		for (String field : fields) {
			String[] inField = field.split("\\~", -1);
			if (inField.length == 2) {
				fieldIds.add(Integer.valueOf(inField[0]));
				fieldValues.add(inField[1]);
			}
		}
	}

	private List<OnlinePageFields> getOnlinePageOptionFields(List<OnlinePageFields> onlinePageFields) {
		List<OnlinePageFields> filteredList = new ArrayList<OnlinePageFields>();
		for (OnlinePageFields onlinePageField : onlinePageFields) {
			if ("update".equals(onlinePageField.getLoginType())) {
				filteredList.add(onlinePageField);
			}
		}
		return filteredList;
	}

	private List<LoginPageFields> getMobilePageOptionFields(List<LoginPageFields> mobilePageFields) {
		List<LoginPageFields> filteredList = new ArrayList<LoginPageFields>();
		for (LoginPageFields onlinePageField : mobilePageFields) {
			if ("update".equals(onlinePageField.getLoginType())) {
				filteredList.add(onlinePageField);
			}
		}
		return filteredList;
	}

	private List<IVRPageFields> getIVRPageOptionFields(List<IVRPageFields> ivrPageFields) {
		List<IVRPageFields> filteredList = new ArrayList<IVRPageFields>();
		for (IVRPageFields ivrPageField : ivrPageFields) {
			if ("update".equals(ivrPageField.getLoginType())) {
				filteredList.add(ivrPageField);
			}
		}
		return filteredList;
	}

	private void getSchedulerClosedNoFunding(Logger logger, JdbcCustomTemplate jdbcCustomTemplate, String device, String langCode, SchedulerClosedNoFunding schedulerClosedNoFunding)
			throws TelAppointException, Exception {
		ApptSysConfig apptSysConfig = apptDAO.getApptSysConfig(jdbcCustomTemplate, logger);
		schedulerClosedNoFunding.setSchedulerClosed(apptSysConfig.getSchedulerClosed());
		schedulerClosedNoFunding.setNoFunding(apptSysConfig.getNoFunding());
		if ("Y".equalsIgnoreCase(apptSysConfig.getSchedulerClosed())) {
			Map<String, String> pageContent = cacheComponent.getDisplayPageContentsMap(jdbcCustomTemplate, logger, device, langCode, true);
			if (pageContent != null) {
				schedulerClosedNoFunding.setHeader(pageContent.get(DisplayPageContentConstants.SCHEDULER_CLOSED_HEADER.getValue()));
				schedulerClosedNoFunding.setBody(pageContent.get(DisplayPageContentConstants.SCHEDULER_CLOSED_BODY.getValue()));

			} else {
				logger.error("No data fetched from pageContent table");
			}
		} else if ("Y".equalsIgnoreCase(apptSysConfig.getNoFunding())) {
			Map<String, String> pageContent = cacheComponent.getDisplayPageContentsMap(jdbcCustomTemplate, logger, device, langCode, true);
			if (pageContent != null) {
				schedulerClosedNoFunding.setHeader(pageContent.get(DisplayPageContentConstants.NO_FUNDING_HEADER.getValue()));
				schedulerClosedNoFunding.setBody(pageContent.get(DisplayPageContentConstants.NO_FUNDING_BODY.getValue()));
			} else {
				logger.error("No data fetched from pageContent table");
			}
		}
	}

	private void populateDesignTemplate(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, LandingPageInfo landingPageInfo, String device, boolean cache)
			throws TelAppointException, Exception {
		Map<String, String> designTemplates = cacheComponent.getDesignTemplatesMap(jdbcCustomTemplate, logger, device, cache);
		landingPageInfo.setCssFileName(designTemplates.get(DesignTemplateConstants.CSS_FILENAME.getValue()));
		landingPageInfo.setLogoFileName(designTemplates.get(DesignTemplateConstants.LOGO_TAG.getValue()));
		landingPageInfo.setFooterContent(designTemplates.get(DesignTemplateConstants.FOOTER_CONTENT.getValue()));
		landingPageInfo.setFooterLinks(designTemplates.get(DesignTemplateConstants.FOOTER_LINKS.getValue()));
	}

	private void populatePageContent(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, LandingPageInfo landingPageInfo, String device, String langCode, boolean cache)
			throws TelAppointException, Exception {
		Map<String, String> pageContent = cacheComponent.getDisplayPageContentsMap(jdbcCustomTemplate, logger, device, langCode, cache);
		landingPageInfo.setLandingPageText(pageContent.get(DisplayPageContentConstants.LANDING_PAGE_CONTENT.getValue()));
	}

	private void populateClientData(Client client, LandingPageInfo landingPageInfo) {
		landingPageInfo.setClientCode(client.getClientCode());
		landingPageInfo.setClientName(client.getClientName());
	}

	private <T> void populateCustomer(Logger logger, String device, Customer customer, List<T> pageFields, Class<T> classType, List<Integer> fieldIds, List<String> fieldValues,
			String type) throws TelAppointException, Exception {
		T inPageFields = null;
		logger.debug("populating the customer");
		try {
			for (int i = 0; i < pageFields.size(); i++) {
				inPageFields = pageFields.get(i);
				String javaRef = (String) CoreUtils.getPropertyValue(inPageFields, "javaRef");
				String loginType = (String) CoreUtils.getPropertyValue(inPageFields, "loginType");
				String paramColumn = (String) CoreUtils.getPropertyValue(inPageFields, "paramColumn");
				int storageSize = (Integer) CoreUtils.getPropertyValue(inPageFields, "storageSize");
				String storageType = (String) CoreUtils.getPropertyValue(inPageFields, "storageType");

				if (type.equals(loginType) || "both".equals(type)) {
					String paramValue = fieldValues.get(i) == null ? "" : fieldValues.get(i);
					if ("contact_phone".equalsIgnoreCase(paramColumn)) {
						customer.setContactPhone(paramValue);
					} else if ("home_phone".equalsIgnoreCase(paramColumn)) {
						customer.setHomePhone(paramValue);
					} else if ("work_phone".equalsIgnoreCase(paramColumn)) {
						customer.setWorkPhone(paramValue);
					} else if ("account_number".equalsIgnoreCase(paramColumn)) {
						int paramValueLength = paramValue.length();
						if (storageSize > 0 && paramValueLength >= storageSize) {
							if ("last".equalsIgnoreCase(storageType)) {
								paramValue = paramValue.substring(paramValueLength - storageSize);
							} else if ("first".equalsIgnoreCase(storageType)) {
								paramValue = paramValue.substring(0, storageSize);
							} else if ("prefix0".equalsIgnoreCase(storageType)) {
								paramValue = "0" + paramValue;
							} else if ("postfix0".equalsIgnoreCase(storageType)) {
								paramValue = paramValue + "0";
							}
						}
						customer.setAccountNumber(paramValue == null ? "0" : paramValue);
					} else if ("email".equalsIgnoreCase(paramColumn)) {
						customer.setEmail(paramValue);
					} else if ("dob".equalsIgnoreCase(paramColumn)) {
						if (paramValue == null || "".equals(paramValue)) {
							logger.error("DOB value is : " + paramValue);
							continue;
						}
						paramValue = DateUtils.convertMMDDYYYY_TO_YYYYMMDDFormat(paramValue);
						CoreUtils.setPropertyValue(customer, javaRef, paramValue);
					} else {
						if (CoreUtils.isOnline(device)) {
							if ("first_name".equalsIgnoreCase(paramColumn) || "last_name".equalsIgnoreCase(paramColumn) || "middle_name".equalsIgnoreCase(paramColumn)) {
								paramValue = CoreUtils.capitalizeString(paramValue);
							}
							CoreUtils.setPropertyValue(customer, javaRef, paramValue);
						} else {
							CoreUtils.setPropertyValue(customer, javaRef, paramValue);
						}
					}
				}
			}

			if ("0".equals(customer.getAccountNumber()) || customer.getAccountNumber() == null) {
				customer.setAccountNumber(customer.getHomePhone());
			}

			if (customer.getAccountNumber() == null) {
				customer.setAccountNumber(customer.getContactPhone());
			}
		} catch (NoSuchFieldException | IllegalAccessException | InvocationTargetException | IllegalArgumentException | IntrospectionException e) {
			String data = "FieldIds: " + fieldIds.toString() + " , fieldValues:" + fieldValues.toString();
			throw new TelAppointException(ErrorConstants.ERROR_1040.getCode(), ErrorConstants.ERROR_1040.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), data);
		}
	}

	private <T> void populateCustomerQuery(Logger logger, String device, List<T> pageFields, List<String> fieldValues, String type, StringBuilder sql,
			MapSqlParameterSource paramSource, Long customerId) throws TelAppointException, Exception {
		T inPageField = null;
		StringBuilder sqlIn = new StringBuilder();
		try {
			int j=0;
			for (int i = 0; i < pageFields.size(); i++) {
				inPageField = pageFields.get(i);
				String javaRef = (String) CoreUtils.getPropertyValue(inPageField, "javaRef");
				String loginType = (String) CoreUtils.getPropertyValue(inPageField, "loginType");
				String paramColumn = (String) CoreUtils.getPropertyValue(inPageField, "paramColumn");
				int storageSize = (Integer) CoreUtils.getPropertyValue(inPageField, "storageSize");
				String storageType = (String) CoreUtils.getPropertyValue(inPageField, "storageType");

				if (type.equals(loginType)) {
					
					if(j==0) {
						sqlIn.append(",");
						++j;
					}
					
					String paramValue = fieldValues.get(i);
					if (CoreUtils.isOnline(device) || CoreUtils.isMobile(device) ) {
						if ("first_name".equalsIgnoreCase(paramColumn) || "last_name".equalsIgnoreCase(paramColumn) || "middle_name".equalsIgnoreCase(paramColumn)) {
							paramValue = CoreUtils.capitalizeString(paramValue);
						}
					} else if ("account_number".equalsIgnoreCase(paramColumn)) {
						int paramValueLength = paramValue.length();
						if (storageSize > 0 && paramValueLength >= storageSize) {
							if ("last".equalsIgnoreCase(storageType)) {
								paramValue = paramValue.substring(paramValueLength - storageSize);
							} else if ("first".equalsIgnoreCase(storageType)) {
								paramValue = paramValue.substring(0, storageSize);
							} else if ("prefix0".equalsIgnoreCase(storageType)) {
								paramValue = "0" + paramValue;
							} else if ("postfix0".equalsIgnoreCase(storageType)) {
								paramValue = paramValue + "0";
							}
						}
						paramValue = paramValue == null ? "0" : paramValue;
					}

					if ("dob".equalsIgnoreCase(paramColumn)) {
						if (paramValue == null || "".equals(paramValue)) {
							logger.error("DOB value is : " + paramValue);
							continue;
						}

						paramValue = DateUtils.convertMMDDYYYY_TO_YYYYMMDDFormat(paramValue);
						Date dob = DateUtils.getDateObject(paramValue);
						sqlIn.append(" ").append(paramColumn).append("=:").append(javaRef);
						if (i + 1 != pageFields.size()) {
							sqlIn.append(",");
						}
						paramSource.addValue(javaRef, dob);
						continue;
					}

					sqlIn.append(" ").append(paramColumn).append("=:").append(javaRef);
					if (i + 1 != pageFields.size()) {
						sqlIn.append(",");
					}
					paramSource.addValue(javaRef, paramValue);
				}
			}

			String sqlStr = sqlIn.toString();
			if (sqlStr.endsWith(",")) {
				sql.append(sqlStr.substring(0, sqlStr.length() - 1));
			} else {
				sql.append(sqlIn);
			}
			sql.append(" where id=:customerId");
			paramSource.addValue("customerId", customerId);
		} catch (NoSuchFieldException nsfe) {
			throw new TelAppointException(ErrorConstants.ERROR_1040.getCode(), ErrorConstants.ERROR_1040.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, nsfe.getMessage(),
					"CustomerId: " + customerId);
		}
	}

	@Override
	public ResponseEntity<ResponseModel> cancelAppointment(Logger logger, String clientCode, String device, String langCode, Long scheduleId, Long transId, String token) throws TelAppointException, Exception {
		CancelAppointResponse cancelAppointResponse = cancelAppt(logger, clientCode, device, langCode, scheduleId, token);
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, cancelAppointResponse), HttpStatus.OK);
	}
	
	private CancelAppointResponse cancelAppt(Logger logger, String clientCode, String device, String langCode, Long scheduleId, String token) throws Exception {
		CancelAppointResponse cancelAppointResponse = new CancelAppointResponse();
		Client client = cacheComponent.getClient(logger, clientCode, true);
		boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		
		ClientDeploymentConfig cdConfig = cacheComponent.getClientDeploymentConfig(logger, clientCode, cache);
		if(CoreUtils.isMobile(device) && !isTokenValid(jdbcCustomTemplate, clientCode, token)) {
			cancelAppointResponse.setStatus(false);
			cancelAppointResponse.setMessage(PropertyUtils.getValueFromProperties("INVALID_TOKEN_MESSAGE", PropertiesConstants.APPT_SERVICE_REST_WS_PROP.getPropertyFileName()));
			return cancelAppointResponse;
		}
		
		Integer cancelMethod;
		if (CoreUtils.isIVR(device)) {
			cancelMethod = AppointmentMethod.IVR.getMethod();
		} else if (CoreUtils.isOnline(device)) {
			cancelMethod = AppointmentMethod.ONLINE.getMethod();
		} else if (CoreUtils.isMobile(device)) {
			cancelMethod = AppointmentMethod.MOBILE.getMethod();
		} else {
			cancelMethod = AppointmentMethod.ADMIN.getMethod();
		}
		apptDAO.cancelAppointment(jdbcCustomTemplate, logger, scheduleId, cancelMethod, langCode, cdConfig, cancelAppointResponse);
		String errorMessage = cancelAppointResponse.getMessage();
		if(("".equals(errorMessage) || errorMessage == null)) {
			if(CoreUtils.isMobile(device)) {
				Map<String, String> aliasMap = cacheComponent.getDisplayAliasMap(jdbcCustomTemplate, logger, device, langCode, cache);
				VerifyPageData verifyPageData = apptDAO.getVerfiyPageData(jdbcCustomTemplate, logger, device, langCode, scheduleId, aliasMap);
				String displayValues = cancelAppointResponse.getDisplayValues();
				String confirmNumber = (displayValues != null && !"".equals(displayValues))?displayValues.split("\\|")[0]:"";
				cancelAppointResponse.setCancelApptDetails(verifyPageData);
				verifyPageData.setConfNumber(confirmNumber);
				cancelAppointResponse.setDisplayKeys(null);
				cancelAppointResponse.setDisplayValues(null);
				MobileAppPage mobileAppPage = apptDAO.getMobileAppPages(jdbcCustomTemplate, "cancel_appt_page");
				cancelAppointResponse.setPageData(getMobileAppPages(mobileAppPage));
			}
		} 
		
		if(CoreUtils.isOnline(device) || CoreUtils.isMobile(device)) {
			cancelAppointResponse.setErrorMessage(errorMessage);
		}
		
		if(cancelAppointResponse.isCancelled()) {
			EmailRequest emailRequest = new EmailRequest();
			Map<String, String> emailData = new HashMap<String, String>();
			sendCancelEmail(logger, errorMessage, jdbcCustomTemplate, client, cdConfig, cancelAppointResponse, scheduleId, langCode, emailRequest, emailData);
		}
		return cancelAppointResponse;
	}
	
	

	@Override
	public ResponseEntity<ResponseModel> getTransId(String clientCode, Logger logger, String device, String uuid, String ipAddress, String callerId, String userName)
			throws TelAppointException, Exception {
		BaseResponse baseResponse = new BaseResponse();
		Client client = cacheComponent.getClient(logger, clientCode, true);
		boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		ClientDeploymentConfig cdConfig = cacheComponent.getClientDeploymentConfig(logger, clientCode, cache);
		Long transId = apptDAO.getTransId(jdbcCustomTemplate, logger, device, cdConfig, uuid, ipAddress, callerId, userName);
		baseResponse.setTransId(transId);
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, baseResponse), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseModel> updateTransaction(String clientCode, Logger logger, String device, Long transId, Integer pageId, Long scheduleId)
			throws TelAppointException, Exception {
		BaseResponse baseResponse = new BaseResponse();
		Client client = cacheComponent.getClient(logger, clientCode, true);
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		apptDAO.updateTransaction(jdbcCustomTemplate, logger, device, transId, pageId, scheduleId);
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, baseResponse), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseModel> extendHoldTime(String clientCode, Logger logger, String device, Long scheduleId) throws TelAppointException, Exception {
		BaseResponse baseResponse = new BaseResponse();
		Client client = cacheComponent.getClient(logger, clientCode, true);
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		apptDAO.extendHoldTime(jdbcCustomTemplate, logger, device, scheduleId);
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, baseResponse), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseModel> updateIVRCalls(Logger logger, IVRCallRequest ivrCallRequest) throws TelAppointException, Exception {
		IVRCallResponse ivrCallResponse = new IVRCallResponse();
		Client client = cacheComponent.getClient(logger, ivrCallRequest.getClientCode(), true);
		boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		ClientDeploymentConfig cdConfig = cacheComponent.getClientDeploymentConfig(logger, ivrCallRequest.getClientCode(), cache);
		apptDAO.updateIVRCallLog(jdbcCustomTemplate, logger, cdConfig, ivrCallRequest, ivrCallResponse);
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, ivrCallResponse), HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<ResponseModel> getPageValidationMessages(Logger logger, String device) throws TelAppointException, Exception {
		PageValidationResponse pageValidationResponse = new PageValidationResponse();
		Map<String, String> validationMsgMap = new HashMap<String, String>();
		masterDAO.getPageValidationMessages(device, validationMsgMap);
		pageValidationResponse.setPageValidationMap(validationMsgMap);
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, pageValidationResponse), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseModel> releaseHoldAppointment(Logger logger, String clientCode, String device, Long scheduleId) throws TelAppointException, Exception {
		Client client = cacheComponent.getClient(logger, clientCode, true);
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		BaseResponse baseResponse = apptDAO.releaseHoldAppointment(jdbcCustomTemplate, logger, device, scheduleId);
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, baseResponse), HttpStatus.OK);
	}

	private List<TimeSlots> getSortedFirstAvailDate(Logger logger, String resTimes) throws TelAppointException, Exception {
		// Data format => 1-10:00,10:15,10:30~2-10:00,10:15,10:30~3-10:00 etc
		String resTimeArray[] = resTimes.split("\\~");
		String resourceId;
		String times[];
		List<TimeSlots> resTimesList = new ArrayList<>();
		for (int i = 0; i < resTimeArray.length; i++) {
			// splitting ==>
			// 1-10:00,10:15,10:30, 2-10:00,10:15 etc in loop one by one.
			String resTimeArrayTemp[] = resTimeArray[i].split("\\-");
			if (resTimeArrayTemp.length == 2) {
				resourceId = resTimeArrayTemp[0].trim();
				if (resourceId == null || "".equals(resourceId)) {
					throw new TelAppointException(ErrorConstants.ERROR_1069.getCode(), ErrorConstants.ERROR_1069.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,
							ErrorConstants.ERROR_1069.getMessage(), resTimeArray[i].toString());
				}
			} else {
				throw new TelAppointException(ErrorConstants.ERROR_1069.getCode(), ErrorConstants.ERROR_1069.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,
						ErrorConstants.ERROR_1069.getMessage(), resTimeArray[i].toString());
			}
			// splitting ==> 10:00,10:15,10:30
			times = resTimeArrayTemp[1].trim().split(",");
			TimeSlots timeSlots = null;
			for (String time : times) {
				timeSlots = new TimeSlots();
				timeSlots.setResourceId(resourceId);
				timeSlots.setTime(time);
				resTimesList.add(timeSlots);
			}
		}

		if (!resTimesList.isEmpty()) {
			Collections.sort(resTimesList, new Comparator<TimeSlots>() {
			@Override
			public int compare(TimeSlots o1, TimeSlots o2) {
				try {
					return new SimpleDateFormat("hh:mm:ss").parse(o1.getTime()).compareTo(new SimpleDateFormat("hh:mm:ss").parse(o2.getTime()));
				} catch (ParseException e) {
					return 0;
				}
			}
		});
		}
		return resTimesList;
	}

	/**
	 * Used to prepare the email place holder data.
	 *
	 * @param logger
	 * @param emailData
	 * @param cdConfig
	 * @param langCode
	 * @param displayKey
	 * @param displayValues
	 * @throws TelAppointException
	 *             , Exception
	 */
	private void populateDataForEmail(Logger logger, Map<String, String> emailData, ClientDeploymentConfig cdConfig, String langCode, String displayKey, String displayValues)
			throws TelAppointException, Exception {
		try {
			String displayKeyArray[] = displayKey != null ? displayKey.split("\\|", -1) : null;
			String displayValuesArray[] = displayKey != null ? displayValues.split("\\|", -1) : null;
			if (displayKeyArray != null && displayValuesArray != null) {
				String rFirstName = null;
				String rLastName = null;
				String rPrefix = null;

				for (int i = 0; i < displayKeyArray.length; i++) {
					String key = displayKeyArray[i];
					String value = displayValuesArray[i] == null ? "" : displayValuesArray[i];
					if (key.contains("appt_date_time_display")) {
						String dateTime = displayValuesArray[i];
						emailData.put("%APPTDATE%", dateTime.substring(0, dateTime.length() - 8).trim());
						emailData.put("%APPTTIME%", dateTime.substring(dateTime.length() - 8, dateTime.length()));
						emailData.put("%SHORTAPPTDATE%", (dateTime.split(","))[1]);
					}

					if (key.equals("s.appt_date_time")) {
						String dateTime = value;
						emailData.put("%DB_DATE_TIME%", dateTime);
					}

					if (key.equals("s.appt_date_time_start")) {
						String dateTime = value;
						emailData.put("%STARTDATE%",
								emailComponent.getDate(logger, cdConfig.getTimeZone(), langCode, dateTime, CommonDateContants.DATETIME_FORMAT_YYYYMMDDHHMMSS_CAP.getValue()));
					}

					if (key.equals("s.appt_date_time_end")) {
						String dateTime = value;
						emailData.put("%ENDDATE%",
								emailComponent.getDate(logger, cdConfig.getTimeZone(), langCode, dateTime, CommonDateContants.DATETIME_FORMAT_YYYYMMDDHHMMSS_CAP.getValue()));
					}

					if (key.equals("r.first_name")) {
						rFirstName = value;
					}

					if (key.equals("r.last_name")) {
						rLastName = value;
					}

					if (key.equals("r.email")) {
						emailData.put("%RESOURCEEMAIL%", value);
					}

					if (key.equals("c.first_name")) {
						emailData.put("%FIRSTNAME%", value);
					}

					if (key.equals("c.last_name")) {
						emailData.put("%LASTNAME%", value);
					}

					if (key.contains("location_name_online")) {
						emailData.put("%LOCATION%", value);
					}

					if (key.equals("l.address")) {
						emailData.put("%LOCATIONADDRESS%", value);
					}

					if (key.equals("l.city")) {
						emailData.put("%LOCATIONCITY%", value);
					}

					if (key.equals("l.state")) {
						emailData.put("%LOCATIONSTATE%", value);
					}

					if (key.equals("l.zip")) {
						emailData.put("%LOCATIONZIP%", value);
					}

					if (key.equals("l.location_google_map")) {
						emailData.put("%LOCATIONGOOGLEMAP%", value);
					}

					if (key.equals("l.location_google_map_link")) {
						emailData.put("%LOCATIONGOOGLEMAPLINK%", value);
					}

					if (key.equals("p.procedure_name_online")) {
						emailData.put("%PROCEDURE%", value);
					}

					if (key.equals("ia.message_value.service")) {
						emailData.put("%SERVICE%", value);
					}

					if (key.equals("d.department_name_online")) {
						emailData.put("%DEPARTMENT%", value);
					}

					if (key.equals("c.account_number")) {
						emailData.put("%ACCOUNTNUMBER%", value);
					}

					if (key.equals("c.contact_phone")) {
						emailData.put("%CONTACTPHONE%", value);
					}

					if (key.equals("c.home_phone")) {
						emailData.put("%HOMEPHONE%", value);
					}

					if (key.equals("c.work_phone")) {
						emailData.put("%WORKPHONE%", value);
					}

					if (key.equals("c.cell_phone")) {
						emailData.put("%CELLPHONE%", value);
					}

					if (key.equals("c.email")) {
						emailData.put("%CMAIL%", value);
					}

					if (key.equals("c.attrib1")) {
						emailData.put("%ATTRIB1%", value);
					}

					if (key.equals("c.attrib2")) {
						emailData.put("%ATTRIB2%", value);
					}

					if (key.equals("c.attrib3")) {
						emailData.put("%ATTRIB3%", value);
					}

					if (key.equals("c.attrib4")) {
						emailData.put("%ATTRIB4%", value);
					}

					if (key.equals("c.attrib5")) {
						emailData.put("%ATTRIB5%", value);
					}

					if (key.equals("c.attrib6")) {
						emailData.put("%ATTRIB6%", value);
					}

					if (key.equals("c.attrib7")) {
						emailData.put("%ATTRIB7%", value);
					}

					if (key.equals("c.attrib8")) {
						emailData.put("%ATTRIB8%", value);
					}

					if (key.equals("c.attrib9")) {
						emailData.put("%ATTRIB9%", value);
					}

					if (key.equals("c.attrib10")) {
						emailData.put("%ATTRIB10%", value);
					}

					if (key.equals("doc.display_text")) {
						emailData.put("%DISPLAYTEXT%", value);
					}

					if (key.contains("conf_number")) {
						emailData.put("%CONFNUM%", value);
					}

					if (key.contains("l.time_zone")) {
						emailData.put("%TIMEZONE%", value);
					}
				}
				emailData.put("%RESOURCE%", rPrefix + " " + rLastName + " " + rFirstName);
			} else {
				logger.error("DispalyKeys and DisplayValues should not be empty!");
			}
		} catch (ArrayIndexOutOfBoundsException aiob) {
			logger.info("displayKeys: "+displayKey+", size: "+displayKey.split("\\|").length);
			logger.info("displayValues: "+displayValues+", size: "+displayValues.split("\\|").length);
			logger.error("Error while preparing data for email.- "+aiob,aiob);
			throw new TelAppointException(ErrorConstants.ERROR_2003.getCode(), ErrorConstants.ERROR_2003.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, aiob.getMessage(),
					"DisplayKeys or Display values format not valid.");
		} catch(Exception e) {
			logger.error("Error while preparing data for email.- "+e,e);
		}
	}

	@Override
	public ResponseEntity<ResponseModel> getAvailableDatesCallcenter(String clientCode, Long locationId, Long departmentId, Long serviceId, Logger logger, String device,
			String langCode, Long transId) throws Exception {
		Client client = cacheComponent.getClient(logger, clientCode, true);
		ClientDeploymentConfig cdConfig = cacheComponent.getClientDeploymentConfig(logger, clientCode, true);
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		AvailableDateTimes availableDates = apptDAO.getAvailableDatesCallcenter(jdbcCustomTemplate, logger, cdConfig.getTimeZone(), locationId, departmentId, serviceId,
				Long.valueOf(cdConfig.getBlockTimeInMins()));
		availableDates.setTimeZone(cdConfig.getTimeZone());
		
		if("".equals(availableDates.getAvailableDates())) {
			availableDates.setErrorFlag("Y");
			if(CoreUtils.isOnline(device)) {
				availableDates.setErrorMessage("No Available Dates");
			} else if(CoreUtils.isIVR(device)) {
				availableDates.setPageName("loc_no_appts");
			}
		}
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, availableDates), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseModel> getAvailableTimesCallcenter(String clientCode, Long locationId, Long departmentId, Long serviceId, String availDate, Logger logger,
			String device, String langCode, Long transId) throws Exception {
		Client client = cacheComponent.getClient(logger, clientCode, true);
		ClientDeploymentConfig cdConfig = cacheComponent.getClientDeploymentConfig(logger, clientCode, true);
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		AvailableDateTimes availableTimes = apptDAO.getAvailableTimesCallcenter(jdbcCustomTemplate, logger, locationId, departmentId, serviceId, availDate,
				Long.valueOf(cdConfig.getBlockTimeInMins()));

		if (CoreUtils.isOnline(device)) {
			String errorMsg = availableTimes.getErrorMessage();
			if (errorMsg != null && "NO_AVAIL_TIMESLOTS".equalsIgnoreCase(errorMsg.toString())) {
				availableTimes.setWarningFlag("Y");
				if (langCode == null || "".equals(langCode)) {
					langCode = "us-en";
				}
				Map<String, String> labelMap = cacheComponent.getDisplayFieldLabelsMap(jdbcCustomTemplate, logger, device, langCode, true);
				if (labelMap != null) {
					availableTimes.setWarningMessage(labelMap.get("NO_AVAIL_TIMESLOTS"));
				}
			}
		}
		availableTimes.setTimeZone(cdConfig.getTimeZone());
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, availableTimes), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseModel> holdAppointmentCallCenter(String clientCode, Long locationId, Long procedureId, Long departmentId, Long serviceId, Long customerId,
			String apptDateTime, Logger logger, String device, String langCode, Long transId) throws Exception {
		Client client = cacheComponent.getClient(logger, clientCode, true);
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		ClientDeploymentConfig cdConfig = cacheComponent.getClientDeploymentConfig(logger, clientCode, true);
		HoldAppt holdAppt;

		synchronized (lock) {
			holdAppt = apptDAO.holdAppointmentCallCenter(jdbcCustomTemplate, logger, device, locationId, procedureId, departmentId, serviceId, customerId, apptDateTime, cdConfig,
					transId);
		}

		String errorMsg = holdAppt.getErrorMessage();
		if (CoreUtils.isOnline(device)) {
			if (errorMsg != null && "DUPLICATE_APPT".equalsIgnoreCase(errorMsg.toString())) {

				holdAppt.setErrorFlag("Y");
				if (langCode == null || "".equals(langCode)) {
					langCode = "us-en";
				}
				Map<String, String> labelMap = cacheComponent.getDisplayFieldLabelsMap(jdbcCustomTemplate, logger, device, langCode, true);
				if (labelMap != null) {
					holdAppt.setErrorMessage(labelMap.get("DUPLICATE_APPT"));
				}
			} else if (errorMsg != null && "SELECTED_DATE_TIME_NOT_AVAILABLE".equalsIgnoreCase(errorMsg.toString())) {
				holdAppt.setErrorFlag("Y");
				if (langCode == null || "".equals(langCode)) {
					langCode = "us-en";
				}
				Map<String, String> labelMap = cacheComponent.getDisplayFieldLabelsMap(jdbcCustomTemplate, logger, device, langCode, true);
				if (labelMap != null) {
					holdAppt.setErrorMessage(labelMap.get("SELECTED_DATE_TIME_NOT_AVAILABLE"));
				}
			} else if (errorMsg != null && "HOLD_NOT_RELEASED".equalsIgnoreCase(errorMsg.toString())) {
				holdAppt.setErrorFlag("Y");
				if (langCode == null || "".equals(langCode)) {
					langCode = "us-en";
				}
				Map<String, String> labelMap = cacheComponent.getDisplayFieldLabelsMap(jdbcCustomTemplate, logger, device, langCode, true);
				if (labelMap != null) {
					holdAppt.setErrorMessage(labelMap.get("HOLD_NOT_RELEASED"));
				}
			} else if (errorMsg != null) {
				logger.error("Error from hold appointment call center stored procedure: " + errorMsg);
			}
		} else if (CoreUtils.isIVR(device)) {
			if (errorMsg != null) {
				holdAppt.setErrorFlag("Y");
				holdAppt.setPageName(errorMsg.toLowerCase());
			}
		}
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, holdAppt), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseModel> getPledgeHistory(String clientCode, Logger logger, String device, String langCode, Long customerId, Long transId) throws Exception {
		CustomerPledgeResponse pledgeRes = new CustomerPledgeResponse();
		Client client = cacheComponent.getClient(logger, clientCode, true);
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		apptDAO.getPledgeHistory(jdbcCustomTemplate, logger, device, langCode, customerId, pledgeRes);
		List<CustomerPledge> customerPledgeList = pledgeRes.getCustomerPledgeList();
		if (customerPledgeList != null && customerPledgeList.isEmpty()) {
			Map<String, String> labelMap = cacheComponent.getDisplayFieldLabelsMap(jdbcCustomTemplate, logger, device, langCode, true);
			if (labelMap != null) {
				pledgeRes.setErrorFlag("Y");
				pledgeRes.setErrorMessage(labelMap.get("NO_PLEDGE_HISTORY"));
			}
		}
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, pledgeRes), HttpStatus.OK);
	}
	
	
	@Override
	public ResponseEntity<ResponseModel> getPledgeHistoryWithTemplate(String clientCode, Logger logger, String device, String langCode, Long customerId, Long transId) throws Exception {
		CustomerPledgeWithTemplateResponse pledgeTemplateRes = new CustomerPledgeWithTemplateResponse();
		Client client = cacheComponent.getClient(logger, clientCode, true);
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		Map<String, CustomerPledgeData> pleCustomerPledgeDataMap = apptDAO.getPledgeHistoryWithTemplate(jdbcCustomTemplate, logger, device, langCode, customerId);
		
		Map<String, String> customerPledgeFundName = new HashMap<>();
		Map<String, String> pageContent = cacheComponent.getDisplayPageContentsMap(jdbcCustomTemplate, logger, device, langCode, true);
		String pledgeVendorTemplate = getPledgeVendarTemplate(logger, pageContent, pleCustomerPledgeDataMap, customerPledgeFundName);
		pledgeTemplateRes.setCustomerPledgeFundName(customerPledgeFundName);
		pledgeTemplateRes.setCustomerPledgeTemplate(pledgeVendorTemplate);
	
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, pledgeTemplateRes), HttpStatus.OK);
	}

	private String getPledgeVendarTemplate(Logger logger, Map<String, String> pageContent, Map<String,CustomerPledgeData> pleCustomerPledgeDataMap, Map<String, String> customerPledgeFundName) {
		
		if(pleCustomerPledgeDataMap.isEmpty()) {	
			return pageContent.get("PLEDGE_HISTORY_NOT_FUND");
		}
		Map<String, String> placeHolderData = new HashMap<>();
		boolean isLiHeap = pleCustomerPledgeDataMap.containsKey("LIHEAP");
		boolean isPSEHelp = pleCustomerPledgeDataMap.containsKey("PSE HELP");
		String template= null;
		if(isLiHeap && isPSEHelp) {
			CustomerPledgeData liHeapcpd = pleCustomerPledgeDataMap.get("LIHEAP");
			CustomerPledgeData pseCpd = pleCustomerPledgeDataMap.get("PSE HELP");
			int liHeapVendorCount = liHeapcpd.getVendorNames().length;
			int pseHelpVendorCount = pseCpd.getVendorNames().length;
			customerPledgeFundName.put("LIHEAP", liHeapcpd.getCustomerPledgeId());
			customerPledgeFundName.put("PSEHELP", pseCpd.getCustomerPledgeId());
			
			if(liHeapVendorCount == 1 && pseHelpVendorCount == 1) {
				template =  pageContent.get("PLEDGE_HISTORY_ONE_PSEHELP_ONE_LIHEAP_VENDOR_HTML");
			} else if(liHeapVendorCount == 2 && pseHelpVendorCount == 1) {
				template =pageContent.get("PLEDGE_HISTORY_ONE_PSEHELP_TWO_LIHEAP_VENDOR_HTML");
			} else if(liHeapVendorCount == 3 && pseHelpVendorCount == 1) {
				template = pageContent.get("PLEDGE_HISTORY_ONE_PSEHELP_THREE_LIHEAP_VENDOR_HTML");
			} else if(liHeapVendorCount == 1 && pseHelpVendorCount == 2) {
				template = pageContent.get("PLEDGE_HISTORY_TWO_PSEHELP_ONE_LIHEAP_VENDOR_HTML");
			} else if(liHeapVendorCount == 1 && pseHelpVendorCount == 3) {
				template = pageContent.get("PLEDGE_HISTORY_THREE_PSEHELP_ONE_LIHEAP_VENDOR_HTML");
			} else if(liHeapVendorCount == 2 && pseHelpVendorCount == 3) {
				template = pageContent.get("PLEDGE_HISTORY_THREE_PSEHELP_TWO_LIHEAP_VENDOR_HTML");
			} else if(liHeapVendorCount == 2 && pseHelpVendorCount == 2) {
				template = pageContent.get("PLEDGE_HISTORY_TWO_PSEHELP_TWO_LIHEAP_VENDOR_HTML");
			} else if(liHeapVendorCount == 3 && pseHelpVendorCount == 2) {
				template = pageContent.get("PLEDGE_HISTORY_TWO_PSEHELP_THREE_LIHEAP_VENDOR_HTML");
			} else if(liHeapVendorCount == 3 && pseHelpVendorCount == 3) {
				template = pageContent.get("PLEDGE_HISTORY_THREE_PSEHELP_THREE_LIHEAP_VENDOR_HTML");
			}
			
			if(template == null) template ="";
			
			populatePlaceHolderData(liHeapcpd, placeHolderData);
			template = CoreUtils.replaceAllPlaceHolders(logger, template, placeHolderData);
			placeHolderData.clear();
			populatePlaceHolderData(pseCpd, placeHolderData);
			template = CoreUtils.replaceAllPlaceHolders(logger, template, placeHolderData);
		} else if(isLiHeap) {
			CustomerPledgeData liHeapcpd = pleCustomerPledgeDataMap.get("LIHEAP");
			int liHeapVendorCount = liHeapcpd.getVendorNames().length;
			customerPledgeFundName.put("LIHEAP", liHeapcpd.getCustomerPledgeId());
			
			if(liHeapVendorCount == 1) {
				template=pageContent.get("PLEDGE_HISTORY_LIHEAP_ONE_VENDOR_HTML");
			} else if(liHeapVendorCount == 2) {
				template=pageContent.get("PLEDGE_HISTORY_LIHEAP_TWO_VENDOR_HTML");
			} else if(liHeapVendorCount == 3) {
				template=pageContent.get("PLEDGE_HISTORY_LIHEAP_THREE_VENDOR_HTML");
			}
			populatePlaceHolderData(liHeapcpd, placeHolderData);
			template = CoreUtils.replaceAllPlaceHolders(logger, template, placeHolderData);
		} else if(isPSEHelp) {
			CustomerPledgeData pseCpd = pleCustomerPledgeDataMap.get("PSE HELP");
			int pseHelpVendorCount = pseCpd.getVendorNames().length;
			customerPledgeFundName.put("PSEHELP", pseCpd.getCustomerPledgeId());
			
			if(pseHelpVendorCount == 1) {
				template = pageContent.get("PLEDGE_HISTORY_PSE_HELP_ONE_VENDOR_HTML");
			} else if(pseHelpVendorCount == 2) {
				template=pageContent.get("PLEDGE_HISTORY_PSE_HELP_TWO_VENDOR_HTML");
			} else if(pseHelpVendorCount == 3) {
				template=pageContent.get("PLEDGE_HISTORY_PSE_HELP_THREE_VENDOR_HTML");
			} 
			populatePlaceHolderData(pseCpd, placeHolderData);
			template = CoreUtils.replaceAllPlaceHolders(logger, template, placeHolderData);
		}
		return template;
	}

	private void populatePlaceHolderData(CustomerPledgeData pleCustomerPledgeData, Map<String, String> placeHolderData) {
		

		String type = "";
		if ("PSE HELP".equals(pleCustomerPledgeData.getFundName())) {
			type = "PSE";
		} else {
			type = "LIHEAP";
		}
		
		placeHolderData.put("%"+type+"_PLEDGE_DATE%", pleCustomerPledgeData.getPledateDate());
		placeHolderData.put("%"+type+"_TOTALAMOUNT%", pleCustomerPledgeData.getTotalPledgeAmount());
		placeHolderData.put("%"+type+"_GRANT_TYPE%", pleCustomerPledgeData.getFundName());

		
		if (pleCustomerPledgeData.getVendorNames().length > 0) {
			placeHolderData.put("%" + type + "_VENDOR1NAME%", pleCustomerPledgeData.getVendorNames()[0]);
		}

		if (pleCustomerPledgeData.getVendorNames().length > 1) {
			placeHolderData.put("%" + type + "_VENDOR2NAME%", pleCustomerPledgeData.getVendorNames()[1]);
		}

		if (pleCustomerPledgeData.getVendorNames().length > 2) {
			placeHolderData.put("%" + type + "_VENDOR3NAME%", pleCustomerPledgeData.getVendorNames()[2]);
		}

		if (pleCustomerPledgeData.getVendorPledgePayments().length > 0) {
			placeHolderData.put("%" + type + "_VENDOR1AMOUNT%", pleCustomerPledgeData.getVendorPledgePayments()[0]);
		}

		if (pleCustomerPledgeData.getVendorPledgePayments().length > 1) {
			placeHolderData.put("%" + type + "_VENDOR2AMOUNT%", pleCustomerPledgeData.getVendorPledgePayments()[1]);
		}

		if (pleCustomerPledgeData.getVendorPledgePayments().length > 2) {
			placeHolderData.put("%" + type + "_VENDOR3AMOUNT%", pleCustomerPledgeData.getVendorPledgePayments()[2]);
		}

	}

	@Override
	public ResponseEntity<ResponseModel> getPledgeAwardLetter(String clientCode, Logger logger, String device, String langCode, Long transId, Long customerPledgeId)
			throws Exception {
		PledgeLetterData pledgeLetterData = new PledgeLetterData();
		Client client = cacheComponent.getClient(logger, clientCode, true);
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		Map<String, String> pageContent = cacheComponent.getDisplayPageContentsMap(jdbcCustomTemplate, logger, device, langCode, true);
		if (pageContent != null) {
			String pledgeLetterBody = "";
			Map<String, String> placeHolderData = new HashMap<String, String>();
			CustomerPledge customerPledge = apptDAO.getCustomerPledgeById(jdbcCustomTemplate, logger, device, langCode, customerPledgeId);

			if (customerPledge != null) {
				if (customerPledge.getFundName().contains("LIHEAP")) {
					if (customerPledge.getVendorCount() == 1) {
						if("Direct Pay to Applicant".equals(customerPledge.getVendor1Name()) ||  "Direct Pay to Applicant".equals(customerPledge.getVendor2Name()) || "Direct Pay to Applicant".equals(customerPledge.getVendor3Name())) {
							pledgeLetterBody = pageContent.get("LIHEAP_PLEDGE_AWARD_LETTER_DIRECT_PAY_HTML");
						} else {
							pledgeLetterBody = pageContent.get("LIHEAP_PLEDGE_AWARD_LETTER_ONE_VENDOR_HTML");
						}
					} else if (customerPledge.getVendorCount() == 2) {
						pledgeLetterBody = pageContent.get("LIHEAP_PLEDGE_AWARD_LETTER_TWO_VENDOR_HTML");
					} else if (customerPledge.getVendorCount() == 3) {
						pledgeLetterBody = pageContent.get("LIHEAP_PLEDGE_AWARD_LETTER_THREE_VENDOR_HTML");
					} else {
						pledgeLetterBody = pageContent.get("LIHEAP_PLEDGE_AWARD_LETTER_ONE_VENDOR_HTML");
					}

				} else if (customerPledge.getFundName().contains("PSE HELP")) {
					if (customerPledge.getVendorCount() == 1) {
						if("Direct Pay to Applicant".equals(customerPledge.getVendor1Name()) ||  "Direct Pay to Applicant".equals(customerPledge.getVendor2Name()) || "Direct Pay to Applicant".equals(customerPledge.getVendor3Name())) {
							pledgeLetterBody = pageContent.get("PSE_PLEDGE_AWARD_LETTER_DIRECT_PAY_HTML");
						} else {
							pledgeLetterBody = pageContent.get("PSE_PLEDGE_AWARD_LETTER_ONE_VENDOR_HTML");
						}
					} else if (customerPledge.getVendorCount() == 2) {
						pledgeLetterBody = pageContent.get("PSE_PLEDGE_AWARD_LETTER_TWO_VENDOR_HTML");
					} else if (customerPledge.getVendorCount() == 3) {
						pledgeLetterBody = pageContent.get("PSE_PLEDGE_AWARD_LETTER_THREE_VENDOR_HTML");
					} else {
						pledgeLetterBody = pageContent.get("PSE_PLEDGE_AWARD_LETTER_ONE_VENDOR_HTML");
					}

				}

				placeHolderData.put("%FIRSTNAME%", customerPledge.getFirstName());
				placeHolderData.put("%LASTNAME%", customerPledge.getLastName());
				placeHolderData.put("%ADDRESS%", customerPledge.getAddress());
				placeHolderData.put("%CITY%", customerPledge.getCity());
				placeHolderData.put("%STATE%", customerPledge.getState());
				placeHolderData.put("%ZIP%", customerPledge.getZipPostal());

				placeHolderData.put("%PLEDGEDATE%", customerPledge.getPledgeDateTime());
				placeHolderData.put("%TOTALAMOUNT%", customerPledge.getTotalPledgeAmt());

				placeHolderData.put("%VENDOR1NAME%", customerPledge.getVendor1Name() == null ? "" : customerPledge.getVendor1Name());
				placeHolderData.put("%VENDOR2NAME%", customerPledge.getVendor2Name() == null ? "" : customerPledge.getVendor2Name());
				placeHolderData.put("%VENDOR3NAME%", customerPledge.getVendor3Name() == null ? "" : customerPledge.getVendor3Name());

				placeHolderData.put("%VENDOR1AMOUNT%", customerPledge.getVendor1Payment() == null ? "" : customerPledge.getVendor1Payment());
				placeHolderData.put("%VENDOR2AMOUNT%", customerPledge.getVendor2Payment() == null ? "" : customerPledge.getVendor2Payment());
				placeHolderData.put("%VENDOR3AMOUNT%", customerPledge.getVendor3Payment() == null ? "" : customerPledge.getVendor3Payment());

				pledgeLetterBody = CoreUtils.replaceAllPlaceHolders(logger, pledgeLetterBody, placeHolderData);
				pledgeLetterData.setPledgeLetterBody(pledgeLetterBody);
			} else {
				logger.error("No award letter for the customer pledge id: " + customerPledgeId);
			}
		}
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, pledgeLetterData), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseModel> checkDuplicateAppts(Logger logger, String device, String langCode, String clientCode, Long serviceId, Long customerId, Long transId)
			throws Exception {
		DuplicateApptResponse duplicateApptResponse = new DuplicateApptResponse();
		Client client = cacheComponent.getClient(logger, clientCode, true);
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		apptDAO.checkDuplicateAppts(jdbcCustomTemplate, logger, serviceId, customerId, duplicateApptResponse);
		if (duplicateApptResponse.isDuplicate()) {
			Map<String, String> labelMap = cacheComponent.getDisplayFieldLabelsMap(jdbcCustomTemplate, logger, device, langCode, true);
			duplicateApptResponse.setErrorMessage(labelMap.get("DUPLICATE_APPT"));
		}
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, duplicateApptResponse), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseModel> getClientDirectAccessNumbers(Logger logger) throws Exception {
		DirectAccessNumberResponse directAccessNumbers = new DirectAccessNumberResponse();
		directAccessNumbers.setAccessNumber(masterDAO.getClientDirectAccessNumbers());
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, directAccessNumbers), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseModel> getLocations(String clientCode, Logger logger, String device, String langCode, Long transId) throws Exception {
		LocationResponse locationResponse = new LocationResponse();
		Client client = cacheComponent.getClient(logger, clientCode, true);
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		List<Location> locations = apptDAO.getLocations(jdbcCustomTemplate, logger, device, langCode);
		locationResponse.setLocations(locations);
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, locationResponse), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseModel> getServicesCallcenter(String clientCode, Logger logger, String device, String langCode, Integer locationId, Integer departmentId, Long transId) throws Exception {
		ServiceCallCenterResponse serviceCallCenter = new ServiceCallCenterResponse();
		Client client = cacheComponent.getClient(logger, clientCode, true);
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		List<com.telappoint.apptdesk.model.Service> services = apptDAO.getServicesCallcenter(jdbcCustomTemplate, logger, device, langCode, locationId, departmentId);
		serviceCallCenter.setServices(services);
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, serviceCallCenter), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseModel> loginAuthenticateForExternal(Logger logger, CustomerInfoRequest customerInfo) throws Exception {
		AuthResponse authResponse = new AuthResponse();
		String clientCode = customerInfo.getClientCode();
		Client client = cacheComponent.getClient(logger, clientCode, true);
		boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		Participant participant = authenticateAndGetParticipant(logger, jdbcCustomTemplate, client.getExtLoginId(), client.getExtLoginPassword(), customerInfo);
		
		if (participant != null) {
			logger.info("participant data: "+participant.toString());
			long participantId = participant.getParticipantID();
			Customer customerFromDB = apptDAO.findByParticipantId(jdbcCustomTemplate, logger, participantId);
			if (customerFromDB != null) {
				authResponse.setAuthSuccess(true);
				authResponse.setCustomerId(customerFromDB.getCustomerId());
			}
		} else {
			authResponse.setAuthSuccess(false);
			Map<String, String> labelMap = cacheComponent.getDisplayFieldLabelsMap(jdbcCustomTemplate, logger, customerInfo.getDevice(), customerInfo.getDevice(), cache);
			authResponse.setAuthMessage((labelMap !=null && !"".equals(labelMap.get("CUSTOMER_NOT_FOUND")))?labelMap.get("CUSTOMER_NOT_FOUND"):ErrorConstants.ERROR_2996.getMessage());
		}

		return new ResponseEntity<>(commonComponent.populateRMDData(logger, authResponse), HttpStatus.CREATED);
	}

	private Participant authenticateAndGetParticipant(Logger logger, JdbcCustomTemplate jdbcCustomTemplate, int extLoginId, String extLoginPassword,
			CustomerInfoRequest customerInfo) throws Exception {
		String device = customerInfo.getDevice();
		List<Integer> fieldIds = new ArrayList<Integer>();
		List<String> fieldValues = new ArrayList<String>();
		parseInputParamValues(customerInfo.getInputParamValues(), fieldIds, fieldValues);
		List<OnlinePageFields> onlinePageFields = null;
		List<IVRPageFields> ivrPageFields = null;
		int size;
		if (CoreUtils.isOnline(device)) {
			onlinePageFields = apptDAO.getOnlinePageFieldsByPageFieldIds(jdbcCustomTemplate, logger, fieldIds);
			size = onlinePageFields.size();
		} else {
			ivrPageFields = apptDAO.getIVRPageFieldsByPageFieldIds(jdbcCustomTemplate, logger, fieldIds);
			size = ivrPageFields.size();
		}

		int inputArraySize = fieldIds.size();
		if (inputArraySize != size) {
			logger.error("Invalid input parameters passed from device: " + device);
			throw new TelAppointException(ErrorConstants.ERROR_2002.getCode(), ErrorConstants.ERROR_2002.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,
					"Invalid input parameters passed from device: " + device, customerInfo.toString());
		}

		Class<OnlinePageFields> onlineClassType = OnlinePageFields.class;
		Class<IVRPageFields> ivrClassType = IVRPageFields.class;

		String loginURL = PropertyUtils.getValueFromProperties("EXT_LOGIN_URL", PropertiesConstants.APPT_SERVICE_REST_WS_PROP.getPropertyFileName());
		StringBuilder payload = new StringBuilder("{\"consumerId\":").append(extLoginId).append(",\"secret\":").append("\"").append(extLoginPassword).append("\"").append("}");
		Login login = ExternalLoginRestClient.login(logger, loginURL, payload.toString());
		String participantURL = "";
		Customer customer = new Customer();
		if (CoreUtils.isOnline(device)) {
			populateCustomer(logger, device, customer, onlinePageFields, onlineClassType, fieldIds, fieldValues, "both");
			String lastName = customer.getLastName();
			String last4DigitSSN = customer.getAccountNumber();
			String dob = customer.getDob();
			dob = DateUtils.convertYYYYMMDD_TO_MMDDYYYYFormat(dob);
			participantURL = PropertyUtils.getValueFromProperties("EXT_GET_PARTICIPANT_URL", PropertiesConstants.APPT_SERVICE_REST_WS_PROP.getPropertyFileName());
			participantURL = participantURL.replaceAll("@LASTNAME@", lastName);
			participantURL = participantURL.replaceAll("@LAST4DIGITSSN@", last4DigitSSN);
			participantURL = participantURL.replaceAll("@DOB_MM-dd-YYYY@", dob);
		} else if (CoreUtils.isIVR(device)) {
			populateCustomer(logger, device, customer, ivrPageFields, ivrClassType, fieldIds, fieldValues, "both");
			String last4DigitSSN = customer.getAccountNumber();
			String dob = customer.getDob();
			dob = DateUtils.convertYYYYMMDD_TO_MMDDYYYYFormat(dob);
			participantURL = PropertyUtils.getValueFromProperties("EXT_GET_PARTICIPANT_URL", PropertiesConstants.APPT_SERVICE_REST_WS_PROP.getPropertyFileName());
			participantURL = participantURL.replaceAll("@LASTNAME@", "");
			participantURL = participantURL.replaceAll("@LAST4DIGITSSN@", last4DigitSSN);
			participantURL = participantURL.replaceAll("@DOB_MM-dd-YYYY@", dob);
		}
		customer.setDob(customer.getDob());

		Participant participant = null;
		if (login != null && login.getToken() != null) {
			List<Participant> participants = ExternalLoginRestClient.getParticipant(logger, participantURL, login.getToken());
			if (participants != null && !participants.isEmpty()) {
				participant = participants.get(0);
			} else {
				logger.error("Participant not fetched");
				// TODO: As per anantha. we will handle this scenario later.
			}
		}
		return participant;
	}

	@Override
	public ResponseEntity<ResponseModel> loginAuthenticateAndUpdateForExternal(Logger logger, ExternalLoginRequest externalReq) throws Exception {
		logger.info("loginAuthenticateAndUpdateForExternal - start");
		String clientCode = externalReq.getClientCode();
		String device = externalReq.getDevice();

		Client client = cacheComponent.getClient(logger, clientCode, true);
		boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
		ClientDeploymentConfig cdConfig = cacheComponent.getClientDeploymentConfig(logger, clientCode, cache);
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);

		List<Integer> fieldIds = new ArrayList<Integer>();
		List<String> fieldValues = new ArrayList<String>();
		parseInputParamValues(externalReq.getInputParamValues(), fieldIds, fieldValues);
		List<OnlinePageFields> onlinePageFields = null;
		List<IVRPageFields> ivrPageFields = null;
		int size;
		if (CoreUtils.isOnline(device)) {
			onlinePageFields = apptDAO.getOnlinePageFieldsByPageFieldIds(jdbcCustomTemplate, logger, fieldIds);
			size = onlinePageFields.size();
		} else {
			ivrPageFields = apptDAO.getIVRPageFieldsByPageFieldIds(jdbcCustomTemplate, logger, fieldIds);
			size = ivrPageFields.size();
		}

		int inputArraySize = fieldIds.size();
		if (inputArraySize != size) {
			logger.error("Invalid input parameters passed from device: " + device);
			throw new TelAppointException(ErrorConstants.ERROR_2002.getCode(), ErrorConstants.ERROR_2002.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,
					"Invalid input parameters passed from device: " + device, externalReq.toString());
		}

		Class<OnlinePageFields> onlineClassType = OnlinePageFields.class;
		Class<IVRPageFields> ivrClassType = IVRPageFields.class;
		
		Long programInstanceId = apptDAO.getProgramInstanceId(jdbcCustomTemplate, logger,externalReq.getLocationId(), externalReq.getServiceId());
		Long programId = apptDAO.getProgramId(jdbcCustomTemplate, logger,externalReq.getLocationId(), externalReq.getServiceId());
		
		ExternalLogicResponse external = null;
		if(CoreUtils.isOnline(device)) {
			external = authenticateCustomerEmpowor(jdbcCustomTemplate, logger, device, programInstanceId, programId, onlinePageFields,onlineClassType, fieldIds, fieldValues, client, cdConfig);
			//external = authenticateCustomerInternal(jdbcCustomTemplate, logger, device, programInstanceId, externalReq.getServiceId(), onlinePageFields,onlineClassType, fieldIds, fieldValues, cdConfig);
		} else if(CoreUtils.isIVR(device)) {
			external = authenticateCustomerEmpowor(jdbcCustomTemplate, logger, device, programInstanceId, programId, ivrPageFields,ivrClassType, fieldIds, fieldValues, client, cdConfig);
			//external = authenticateCustomerInternal(jdbcCustomTemplate, logger, device, programInstanceId, externalReq.getServiceId(), ivrPageFields,ivrClassType, fieldIds, fieldValues, cdConfig);
		}
		
		Map<String, String> labelMap = cacheComponent.getDisplayFieldLabelsMap(jdbcCustomTemplate, logger, device, "us-en", cache);
		AuthResponse authResponse = null;
		if(external != null) {
			authResponse = external.getAuthResponse();
			if (authResponse != null && authResponse.isAuthSuccess()) {
				Long participantId = external.getParticipantId();
				if(participantId == null) {
					logger.error("getParticipant API returned empty data, so continue with participantId : 0");
					participantId = (long)0;
				}
				apptDAO.updateParticipantId(jdbcCustomTemplate, logger, external.getParticipantId(), authResponse.getCustomerId());
				if("N".equals(authResponse.getEligible()))  {
					if (CoreUtils.isOnline(device)) {
						authResponse.setEligibleMessage((labelMap != null && labelMap.get("NOT_ELIGIBLE") == null) ? "" : labelMap.get("NOT_ELIGIBLE"));
					} else if (CoreUtils.isIVR(device)) {
						authResponse.setPageName("customer_not_eligible");
					}
				}
			} else {
				authResponse.setAuthSuccess(false);
				authResponse.setAuthMessage((labelMap !=null && !"".equals(labelMap.get("CUSTOMER_NOT_FOUND")))?labelMap.get("CUSTOMER_NOT_FOUND"):ErrorConstants.ERROR_2996.getMessage());
			}
		} else {
			authResponse = new AuthResponse();
			authResponse.setAuthSuccess(false);
			authResponse.setAuthMessage((labelMap !=null && !"".equals(labelMap.get("CUSTOMER_NOT_FOUND")))?labelMap.get("CUSTOMER_NOT_FOUND"):ErrorConstants.ERROR_2996.getMessage());
		}
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, authResponse), HttpStatus.CREATED);
	}

	private <T> ExternalLogicResponse authenticateCustomerEmpowor(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String device, Long programInstanceId, Long programId,
			List<T> pageFields, Class<T> classType, List<Integer> fieldIds, List<String> fieldValues,  Client client, ClientDeploymentConfig cdConfig) throws Exception {
		
		ExternalLogicResponse externalLoginResponse = new ExternalLogicResponse();
		String loginURL = PropertyUtils.getValueFromProperties("EXT_LOGIN_URL", PropertiesConstants.APPT_SERVICE_REST_WS_PROP.getPropertyFileName());
		StringBuilder payload = new StringBuilder("{\"consumerId\":").append(client.getExtLoginId()).append(",\"secret\":").append("\"").append(client.getExtLoginPassword()).append("\"").append("}");
		Login login = ExternalLoginRestClient.login(logger, loginURL, payload.toString());
		String participantURL="";
	    Customer customer = new Customer();
		populateCustomer(logger, device, customer, pageFields, classType, fieldIds, fieldValues, "both");
		String lastName = customer.getLastName();
		String last4DigitSSN = customer.getAccountNumber();
		String dob = customer.getDob();
		dob = DateUtils.convertYYYYMMDD_TO_MMDDYYYYFormat(dob);
		participantURL = PropertyUtils.getValueFromProperties("EXT_GET_PARTICIPANT_URL", PropertiesConstants.APPT_SERVICE_REST_WS_PROP.getPropertyFileName());
		if(CoreUtils.isOnline(device)) {
			participantURL = participantURL.replaceAll("@LASTNAME@", lastName);
		} else {
			participantURL = participantURL.replaceAll("@LASTNAME@", "");
		}
	    participantURL = participantURL.replaceAll("@LAST4DIGITSSN@", last4DigitSSN);
	    participantURL= participantURL.replaceAll("@DOB_MM-dd-YYYY@", dob);
		customer.setDob(customer.getDob());
		Participant participant = null;
		if(login != null && login.getToken() != null) {		    
			logger.info("Participant URL:"+participantURL.toString());
			List<Participant> participants = ExternalLoginRestClient.getParticipant(logger, participantURL, login.getToken());
			if(participants != null && !participants.isEmpty()) {
				participant = participants.get(0);
			} else {
				logger.error("Participant: not fetched");
				//TODO: As per anantha. we will handle this scenario later.
			}
			
			HouseHold houseHold = null;
			if(participant != null) {
				String eligibilityURL = PropertyUtils.getValueFromProperties("EXT_ELIGIBILITY_URL", PropertiesConstants.APPT_SERVICE_REST_WS_PROP.getPropertyFileName());		
				logger.info("EligibilityURL:"+eligibilityURL);
				long participantId = participant.getParticipantID();
				List<HouseHold> houseHolds = participant.getHouseholdList();
				long houseHoldId=0;
				if(houseHolds != null && !houseHolds.isEmpty()) {
					houseHold = houseHolds.get(0);
					houseHoldId = houseHold.getHouseholdID().longValue();
				} else {
					logger.error("no HouseHold details fetched from external login service.");
				}
				String date = DateUtils.getCurrentDateMMDDYYYY(cdConfig.getTimeZone());
				Eligibility eligibility =  ExternalLoginRestClient.getEligibilityCheck(logger, eligibilityURL, login.getToken(), participantId, programId.intValue(), programInstanceId, houseHoldId, date);
				logger.info("Eligibility Response: "+eligibility.toString());
				externalLoginResponse = authenticateCustomerEmpower(jdbcCustomTemplate, logger, device, pageFields, classType, fieldIds, fieldValues, cdConfig, participant);
				logger.info("externalLoginResponse: "+externalLoginResponse);
				logger.info("auth res: "+externalLoginResponse.getAuthResponse());
				// overriding the external 
				externalLoginResponse.getAuthResponse().setEligible((eligibility != null && eligibility.isEligible())?"Y":"N");
				externalLoginResponse.setParticipantId(participantId);
				externalLoginResponse.setHouseHoldId(houseHoldId);
			} else {
				externalLoginResponse = authenticateCustomerInternal(jdbcCustomTemplate, logger, device, pageFields, classType, fieldIds, fieldValues, cdConfig);
			}
		}
		return externalLoginResponse;
	}
	private <T> ExternalLogicResponse authenticateCustomerEmpower(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String device,
			List<T> pageFields, Class<T> classType, List<Integer> fieldIds, List<String> fieldValues, ClientDeploymentConfig cdConfig,Participant participant) throws Exception {
		ExternalLogicResponse externalLoginResponse = new ExternalLogicResponse();
		AuthResponse authResponse = authenticateAndUpdateEmpowor(jdbcCustomTemplate, logger, device, pageFields, classType, fieldIds, fieldValues, cdConfig, participant);
		if(authResponse != null && authResponse.isAuthSuccess()) {
			long customerId = authResponse.getCustomerId();
			Customer customer = apptDAO.getCustomer(jdbcCustomTemplate, logger, customerId);
			authResponse.setEligible("N".equals(customer.getLiHeapFund())?"Y":"N");
			externalLoginResponse.setAuthResponse(authResponse);
			externalLoginResponse.setParticipantId(customer.getHouseHoldId());
			externalLoginResponse.setHouseHoldId(customer.getHouseHoldId());
		} 
		return externalLoginResponse;
	}
	
	private <T> AuthResponse authenticateAndUpdateEmpowor(JdbcCustomTemplate jdbcCustomTemplate, Logger logger,String device, List<T> pageFields, Class<T> classType, List<Integer> fieldIds,
			List<String> fieldValues, ClientDeploymentConfig cdConfig,Participant participant) throws Exception {
		AuthResponse authResponse = new AuthResponse();
		Map<String, String> labelMap = cacheComponent.getDisplayFieldLabelsMap(jdbcCustomTemplate, logger, device, "us-en", true);
		
		long participantId = participant.getParticipantID();
		HouseHold houseHold = null;
		Customer customer = apptDAO.findByParticipantId(jdbcCustomTemplate, logger, participantId);
		if(participantId > 0 && customer != null) {
			logger.info("participantsId exists --- "+customer.getCustomerId());
			List<HouseHold> houseHolds = participant.getHouseholdList();
			authResponse.setCustomerId(customer.getCustomerId());
			long houseHoldId=0;
			if(houseHolds != null && !houseHolds.isEmpty()) {
				houseHold = houseHolds.get(0);
				houseHoldId = houseHold.getHouseholdID().longValue();
				customer.setHouseHoldId(houseHoldId);
			} else {
				logger.error("no HouseHold details fetched from external login service. ");
				houseHoldId = apptDAO.getHouseHoldValue(jdbcCustomTemplate);
			}
	
			customer.setCustomerId(authResponse.getCustomerId());
			StringBuilder sql = new StringBuilder("update customer set  id=:customerId ");
			MapSqlParameterSource paramSource = new MapSqlParameterSource();
			
			if(CoreUtils.isIVR(device)) {
				sql.append(",first_name=:firstName");
				sql.append(",last_name=:lastName");
				paramSource.addValue("firstName", participant.getFirstName());
				paramSource.addValue("lastName", participant.getLastName());
				if(houseHolds != null && !houseHolds.isEmpty()) {
					houseHold = houseHolds.get(0);
					sql.append(",address=:address");
					sql.append(",city=:city");
					sql.append(",state=:state");
					sql.append(",zip_postal=:zipCode ");
					
					paramSource.addValue("address", houseHold.getAddress1()==null?"":houseHold.getAddress1());
					paramSource.addValue("city", houseHold.getCity()==null?"":houseHold.getCity());
					paramSource.addValue("state", houseHold.getState()==null?"":houseHold.getState());
					paramSource.addValue("zipCode", houseHold.getPostalCode()==null?"":houseHold.getPostalCode());
				}
			}
			logger.info("update customer sql: "+sql.toString());
			populateCustomerQuery(logger, device, pageFields, fieldValues, "update", sql, paramSource, authResponse.getCustomerId());
			apptDAO.updateCustomer(jdbcCustomTemplate, logger, sql, paramSource, cdConfig);
			authResponse.setAuthSuccess(true);
		} else {
			customer = new Customer();
			apptDAO.authenticateCustomer(jdbcCustomTemplate, logger, device, fieldIds, fieldValues, pageFields, classType, authResponse, labelMap);
			if (!authResponse.isAuthSuccess()) {
				populateCustomer(logger, device, customer, pageFields, classType, fieldIds, fieldValues, "both");
				customer.setFirstName(participant.getFirstName());
				customer.setLastName(participant.getLastName());
				customer.setDob(participant.getDob());
				customer.setAccountNumber(participant.getSsn());
				customer.setParticipantId(participant.getParticipantID());
				List<HouseHold> houseHolds = participant.getHouseholdList();
				long houseHoldId=0;
				
				if(houseHolds != null && !houseHolds.isEmpty()) {
					houseHold = houseHolds.get(0);
					houseHoldId = houseHold.getHouseholdID().longValue();
					customer.setHouseHoldId(houseHoldId);
				} else {
					logger.error("no HouseHold details fetched from external login service.");
				}
				
				long customerId = apptDAO.saveCustomer(jdbcCustomTemplate, logger, customer, cdConfig);
				if (CoreUtils.isIVR(device)) {
					customer.setCustomerId(customerId);
					if(participantId > 0) {
						customer.setLastName(participant.getLastName());
						customer.setFirstName(participant.getFirstName());
					} else {
						customer.setFirstName("NEW");
						customer.setLastName("Customer-" + customerId);
					}
					boolean isUpdated = apptDAO.updateCustomerForIVR(jdbcCustomTemplate, logger, customer, cdConfig);
					if (!isUpdated) {
						logger.error("IVR firstName and lastName not updated!");
					}
				}
				authResponse.setCustomerId(customerId);
				authResponse.setAuthSuccess(true);
			} else {
				customer.setCustomerId(authResponse.getCustomerId());
				StringBuilder sql = new StringBuilder("update customer set  id=:customerId ");
				MapSqlParameterSource paramSource = new MapSqlParameterSource();
				populateCustomerQuery(logger, device, pageFields, fieldValues, "update", sql, paramSource, authResponse.getCustomerId());
				apptDAO.updateCustomer(jdbcCustomTemplate, logger, sql, paramSource, cdConfig);
				authResponse.setAuthSuccess(true);
			}
		}
		return authResponse;
	}
	
	private <T> ExternalLogicResponse authenticateCustomerInternal(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String device,
			List<T> pageFields, Class<T> classType, List<Integer> fieldIds, List<String> fieldValues, ClientDeploymentConfig cdConfig) throws Exception {
		ExternalLogicResponse externalLoginResponse = new ExternalLogicResponse();
		AuthResponse authResponse = authenticateAndUpdate(jdbcCustomTemplate, logger, device, pageFields, classType, fieldIds, fieldValues, cdConfig);
		if(authResponse.isAuthSuccess()) {
			long customerId = authResponse.getCustomerId();
			Customer customer = apptDAO.getCustomer(jdbcCustomTemplate, logger, customerId);
			authResponse.setEligible("N".equals(customer.getLiHeapFund())?"Y":"N");
			externalLoginResponse.setAuthResponse(authResponse);
			externalLoginResponse.setParticipantId(customer.getHouseHoldId());
			externalLoginResponse.setHouseHoldId(customer.getHouseHoldId());
		} 
		return externalLoginResponse;
	}
	
	private <T> AuthResponse authenticateAndUpdate(JdbcCustomTemplate jdbcCustomTemplate, Logger logger,String device, List<T> pageFields, Class<T> classType, List<Integer> fieldIds,
			List<String> fieldValues, ClientDeploymentConfig cdConfig) throws Exception {
		AuthResponse authResponse = new AuthResponse();
		Map<String, String> labelMap = cacheComponent.getDisplayFieldLabelsMap(jdbcCustomTemplate, logger, device, "us-en", true);
		Customer customer = new Customer();
		apptDAO.authenticateCustomer(jdbcCustomTemplate, logger, device, fieldIds, fieldValues, pageFields, classType, authResponse, labelMap);
		if (!authResponse.isAuthSuccess()) {
			populateCustomer(logger, device, customer, pageFields, classType, fieldIds, fieldValues, "both");
			if(CoreUtils.isIVR(device)) {
				customer.setFirstName("");
				customer.setLastName("");
			}
			long customerId = apptDAO.saveCustomer(jdbcCustomTemplate, logger, customer, cdConfig);
			if (CoreUtils.isIVR(device)) {
				customer.setCustomerId(customerId);
				customer.setFirstName("NEW");
				customer.setLastName("Customer-" + customerId);
				boolean isUpdated = apptDAO.updateCustomerForIVR(jdbcCustomTemplate, logger, customer, cdConfig);
				if (!isUpdated) {
					logger.error("IVR firstName and lastName not updated!");
				}
			}
			authResponse.setCustomerId(customerId);
			ApptSysConfig apptSysConfig = apptDAO.getApptSysConfig(jdbcCustomTemplate, logger);
			if("Y".equals(apptSysConfig.getCheckAssignedResource())) {
				authResponse.setResourceId(apptDAO.getResourceIdFromCustomer(jdbcCustomTemplate, customerId));
			}
			authResponse.setAuthSuccess(true);
		} else {
			customer.setCustomerId(authResponse.getCustomerId());
			StringBuilder sql = new StringBuilder("update customer set  id=:customerId ");
			MapSqlParameterSource paramSource = new MapSqlParameterSource();
			populateCustomerQuery(logger, device, pageFields, fieldValues, "update", sql, paramSource, authResponse.getCustomerId());
			apptDAO.updateCustomer(jdbcCustomTemplate, logger, sql, paramSource, cdConfig);
			authResponse.setAuthSuccess(true);
		}
		return authResponse;
	}

	@Override
	public ResponseEntity<ResponseModel> confirmAppointmentExternalLogic(Logger logger, ConfirmAppointmentRequest confirmApptReq) throws TelAppointException, Exception {
		Client client = cacheComponent.getClient(logger, confirmApptReq.getClientCode(), true);
		boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		ClientDeploymentConfig cdConfig = cacheComponent.getClientDeploymentConfig(logger, confirmApptReq.getClientCode(), cache);
		Map<String, String> emailData = new HashMap<String, String>();
		ConfirmAppointmentResponse confirmApptRes = bookppointment(jdbcCustomTemplate, logger, confirmApptReq, cdConfig);
		String errorMessage = confirmApptRes.getMessage();
		if (confirmApptRes.isStatus() && errorMessage == null) {
			String enrollmentId = enrollExternalLoginEnrollment(jdbcCustomTemplate, client, logger, confirmApptReq, confirmApptRes);
			if(enrollmentId != null && enrollmentId.contains("p")) {
				emailData.put("ENROLLMENTID_CONTAINS_P", "Y");
			}
		} else {
			logger.error("ConfirmAppointment has been failed.");
		}

		
		sendConfirmEmail(logger, errorMessage, jdbcCustomTemplate, client, cdConfig, confirmApptReq, confirmApptRes, emailData, "confirm");
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, confirmApptRes), HttpStatus.CREATED);
	}

	private String enrollExternalLoginEnrollment(JdbcCustomTemplate jdbcCustomTemplate, Client client, Logger logger, ConfirmAppointmentRequest confirmApptReq,
			ConfirmAppointmentResponse confirmApptRes) {
		String enrollmentPayload = null;
		try {
			String loginURL = PropertyUtils.getValueFromProperties("EXT_LOGIN_URL", PropertiesConstants.APPT_SERVICE_REST_WS_PROP.getPropertyFileName());
			StringBuilder payload = new StringBuilder("{\"consumerId\":").append(client.getExtLoginId()).append(",\"secret\":").append("\"").append(client.getExtLoginPassword())
					.append("\"").append("}");
			Login login = ExternalLoginRestClient.login(logger, loginURL, payload.toString());
			String enrollURL = PropertyUtils.getValueFromProperties("EXT_CREATE_ENROLLMENT_URL", PropertiesConstants.APPT_SERVICE_REST_WS_PROP.getPropertyFileName());

			ExternalLoginEnrollment externalLoginEntrollment = new ExternalLoginEnrollment();
			prepareExternalLoginEnrollement(jdbcCustomTemplate, logger, confirmApptReq.getScheduleId(),confirmApptReq.getTransId(), confirmApptRes, externalLoginEntrollment);
			enrollmentPayload = getPayloadJSON(logger, externalLoginEntrollment);
			logger.info("create Enrollment Req: "+enrollmentPayload);
			EnrollementResponse enrollmentRes = null;
			if (enrollmentPayload != null) {
				enrollmentRes = ExternalLoginRestClient.createEnrollment(logger, enrollURL, login.getToken(), enrollmentPayload);
				logger.info("Create Enrollment response:"+enrollmentRes.toString());
				if (enrollmentRes != null && !"".equals(enrollmentRes.getEnrollmentId())) {
					boolean status = apptDAO.updateEnrollemntId(jdbcCustomTemplate, logger, enrollmentRes.getEnrollmentId(), confirmApptReq.getScheduleId());
					if (!status) {
						logger.error("update enrollement id to appointment table failed.!");
					}
					return enrollmentRes.getEnrollmentId();
				} else {
					logger.error("create enrollement has been failed.!");
					saveToPendingEnrollment(jdbcCustomTemplate, logger, "createEnrollment", enrollmentPayload, confirmApptReq.getScheduleId());
				}
			} else {
				logger.error("JSON convertion failure!.");
			}
			
		} catch (Exception e) {
			try {
				saveToPendingEnrollment(jdbcCustomTemplate, logger, "createEnrollment", enrollmentPayload, confirmApptReq.getScheduleId());
			} catch (TelAppointException te) {
				logger.error("create PendingEnrollement save to db failed.!");
			}
			logger.error("Error:" + e, e);

			String sendErrorEmail = "N";
			try {
				sendErrorEmail = PropertyUtils.getValueFromProperties("error.mail.send", PropertiesConstants.APPT_SERVICE_REST_WS_PROP.getPropertyFileName());
				if ("Y".equalsIgnoreCase(sendErrorEmail)) {
					String subject = "Error in ApptFrontEndRestService for ClientName - " + client.getClientName();
					StringBuilder errorMsg = new StringBuilder();
					errorMsg.append("InputParameters: ").append("ScheduleId: " + confirmApptReq.getScheduleId());
					errorMsg.append("<br/> ");
					errorMsg.append(CoreUtils.getMethodAndClassName(e));
					errorMsg.append("<br/>");
					errorMsg.append("Root Cause of Exception:").append("External logic - CreateEnrollment to external system failure");
					errorMsg.append("<br/><br/>");
					errorMsg.append("Stack Trace:");
					errorMsg.append("<br/>");
					errorMsg.append("<br/>" + CoreUtils.getStackTrace(e));
					EmailRequest emailRequest = new EmailRequest();
					emailRequest.setEmailBody(errorMsg.toString());
					emailRequest.setSubject(subject);
					emailRequest.setEmailType("error");
					emailComponent.setErrorMailServerPreference(logger, emailRequest);
					emailComponent.sendEmail(logger, emailRequest, null);
				}
			} catch (Exception ioe) {
				logger.error("Error while sending an error email ::" + ioe, ioe);
			}
		}
		return null;
	}

	private void saveToPendingEnrollment(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String enrollmentType, String payLoad, Long scheduleId) throws TelAppointException {
		PendingEnrollment pendingEnrollment = new PendingEnrollment();
		pendingEnrollment.setScheduleId(scheduleId);
		pendingEnrollment.setApiName(enrollmentType);
		pendingEnrollment.setJsonPayLoad(payLoad);
		apptDAO.savePendingCreateOrCancelEnrollments(jdbcCustomTemplate, logger, pendingEnrollment);
	}

	private void prepareExternalLoginEnrollement(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, Long scheduleId, Long transId, ConfirmAppointmentResponse confirmApptRes,
			ExternalLoginEnrollment externalLoginEnrollment) throws TelAppointException, Exception {
		Schedule schedule = apptDAO.getSchedule(jdbcCustomTemplate, logger, scheduleId);
		Customer customer = apptDAO.getCustomer(jdbcCustomTemplate, logger, schedule.getCustomerId());
		Long programInstanceId = apptDAO.getProgramInstanceId(jdbcCustomTemplate, logger, schedule.getLocationId(), schedule.getServiceId());
		Long programId = apptDAO.getProgramId(jdbcCustomTemplate, logger, schedule.getLocationId(), schedule.getServiceId());
		Long participantId = customer.getParticipantId();
		if (participantId.longValue() > 1000000) {
			externalLoginEnrollment.setParticipantId(0);
			externalLoginEnrollment.setHouseholdId(0);
		} else {
			externalLoginEnrollment.setParticipantId(participantId);
			externalLoginEnrollment.setHouseholdId(customer.getHouseHoldId());
		}
		externalLoginEnrollment.setProgramId(programId.intValue());
		externalLoginEnrollment.setProgramInstanceId(programInstanceId);	
		externalLoginEnrollment.setAppointmentDate(schedule.getApptDateTime());
		externalLoginEnrollment.setFirstName(customer.getFirstName());
		externalLoginEnrollment.setLastName(customer.getLastName());
		externalLoginEnrollment.setSsn(customer.getAccountNumber());
		externalLoginEnrollment.setDob(customer.getDob());
		externalLoginEnrollment.setTransaction(""+transId);
		externalLoginEnrollment.setNote("test");
		externalLoginEnrollment.setPhoneNumber(customer.getContactPhone());
		externalLoginEnrollment.setEnrollmentStartDate(schedule.getApptDateTime().split("T")[0]);
		externalLoginEnrollment.setEnrollmentEndDate(schedule.getApptDateTime().split("T")[0]);
	}

	private String getPayloadJSON(Logger logger, ExternalLoginEnrollment externalLoginEntrollment) {
		ObjectMapper mapper = new ObjectMapper();
		String json = null;
		try {
			json = mapper.writeValueAsString(externalLoginEntrollment);
		} catch (IOException e) {
			logger.error("Json convertion is failed.");
		}
		return json;
	}

	@Override
	public ResponseEntity<ResponseModel> cancelAppointmentExternalLogic(Logger logger, String clientCode, String device, String langCode, Long scheduleId) throws Exception {
		logger.info("CancelAppointment using external logic - start");
		CancelAppointResponse cancelAppointResponse = new CancelAppointResponse();
		Client client = cacheComponent.getClient(logger, clientCode, true);
		boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		ClientDeploymentConfig cdConfig = cacheComponent.getClientDeploymentConfig(logger, clientCode, cache);
		Integer cancelMethod;

		if (CoreUtils.isIVR(device)) {
			cancelMethod = AppointmentMethod.IVR.getMethod();
		} else if (CoreUtils.isOnline(device)) {
			cancelMethod = AppointmentMethod.ONLINE.getMethod();
		}  else if (CoreUtils.isMobile(device)) {
			cancelMethod = AppointmentMethod.MOBILE.getMethod();
		} else {
			cancelMethod = AppointmentMethod.ADMIN.getMethod();
		}
		apptDAO.cancelAppointment(jdbcCustomTemplate, logger, scheduleId, cancelMethod, langCode, cdConfig, cancelAppointResponse);
		String errorMessage = cancelAppointResponse.getMessage();
		String enrollmentId = null;
		if (cancelAppointResponse.isCancelled() && errorMessage == null) {
			enrollmentId = apptDAO.getEnrollementId(jdbcCustomTemplate, logger, scheduleId);
			cancelEnrollment(jdbcCustomTemplate, logger, scheduleId, client, enrollmentId);
		}
		EmailRequest emailRequest = new EmailRequest();
		Map<String, String> emailData = new HashMap<String, String>();
		
		if(enrollmentId != null && enrollmentId.contains("p")) {
			emailData.put("ENROLLMENTID_CONTAINS_P", "Y");
		}
		sendCancelEmail(logger, errorMessage, jdbcCustomTemplate, client, cdConfig, cancelAppointResponse, scheduleId, langCode, emailRequest, emailData);
		logger.info("CancelAppointment using external logic - end");
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, cancelAppointResponse), HttpStatus.OK);
	}

	private void sendConfirmEmail(Logger logger, String errorMessage, JdbcCustomTemplate jdbcCustomTemplate, Client client, ClientDeploymentConfig cdConfig,
			ConfirmAppointmentRequest confirmApptReq, ConfirmAppointmentResponse confirmApptRes, Map<String, String> emailData, String emailType) {
		try {
			if (errorMessage == null) {
				apptDAO.updateTransId(jdbcCustomTemplate, logger, confirmApptReq.getScheduleId(), confirmApptReq.getTransId());

				EmailRequest emailRequest = new EmailRequest();
				ApptSysConfig apptSysConfig = apptDAO.getApptSysConfig(jdbcCustomTemplate, logger);
				String ccConfirmEmail[] = null;
				String ccAddressStr="";
				if (apptSysConfig != null && apptSysConfig.getCcConfirmEmails() != null && apptSysConfig.getCcConfirmEmails().trim().length() > 0) {
					ccAddressStr = apptSysConfig.getCcConfirmEmails();
				}
				
				if(emailData.get("ENROLLMENTID_CONTAINS_P")!= null && "Y".equals(emailData.get("ENROLLMENTID_CONTAINS_P"))) {
					String ccAddress = PropertyUtils.getValueFromProperties("EXT_LOGIN_NOTFOUND_EMAIL", PropertiesConstants.APPT_SERVICE_REST_WS_PROP.getPropertyFileName());
					if(ccAddress != null &&  !"".equals(ccAddress)) {
						ccAddressStr = ccAddressStr !=null && !"".equals(ccAddressStr) ?ccAddressStr+","+ccAddress:ccAddress;
					} 
				}
				ccConfirmEmail = ccAddressStr.split(",");
				if (ccConfirmEmail != null) {
					emailRequest.setCcAddresses(ccConfirmEmail);
				}

				String langCode = confirmApptReq.getLangCode();
				emailData.put("%CLIENTAPPTLINK%", client.getApptLink() == null ? "" : client.getApptLink());
				emailData.put("%CLIENTNAME%", client.getClientName() == null ? "" : client.getClientName());
				emailData.put("%CLIENTADDRESS%", client.getAddress() == null ? "" : client.getAddress());
				populateDataForEmail(logger, emailData, cdConfig, confirmApptReq.getLangCode(), confirmApptRes.getDisplayKeys(), confirmApptRes.getDisplayValues());
				emailData.put("%SCHEDULEID%", "" + confirmApptReq.getScheduleId());
				String email = emailData.get("%CMAIL%");
				if (email != null && !"".equals(email)) {
					Map<String, String> emailTemplateMap = cacheComponent.getEmailTemplateMap(jdbcCustomTemplate, logger, langCode, false);
					if (emailTemplateMap != null) {
						String emailSubjectTemplate = (String) emailTemplateMap.get(EmailTemplateConstants.EMAIL_APPT_CONFIRM_SUBJECT.getValue());
						String emailBodyTemplate = (String) emailTemplateMap.get(EmailTemplateConstants.EMAIL_APPT_CONFIRM_BODY.getValue());
						
						String emailSubject = emailComponent.getEmailSubject(logger, emailSubjectTemplate, emailData);
						String emailBody = emailComponent.getEmailBody(logger, emailBodyTemplate, emailData);
						emailRequest.setSubject(emailSubject);
						emailRequest.setEmailBody(emailBody);
						logger.info("Email Body: " + emailBody);
						emailRequest.setToAddress(email);
						emailRequest.setMethod("REQUEST");
						emailRequest.setStatus("CONFIRMED");
						emailRequest.setEmailType(emailType);
						
						emailComponent.setMailServerPreference(logger, emailRequest);
						emailComponent.sendEmail(logger, emailRequest, emailData);
					} else {
						logger.error("Email templates not configured properly");
					}
				} else {
					logger.warn("Customer email address not available. So email not sending!!!");
				}
			} else {
				logger.error("Book appointment failed. Response recieved from book appointment stored procedure ::" + errorMessage);
			}
		} catch (Exception e) {
			logger.error("Confirmation email failed to send. " + e, e);
		}
	}

	private void sendCancelEmail(Logger logger, String errorMessage, JdbcCustomTemplate jdbcCustomTemplate, Client client, ClientDeploymentConfig cdConfig,
			CancelAppointResponse cancelAppointResponse, long scheduleId, String langCode, EmailRequest emailRequest, Map<String, String> emailData) {
		try {
			if (errorMessage == null || "".equals(errorMessage)) {
				String ccAddressStr="";
				ApptSysConfig apptSysConfig = apptDAO.getApptSysConfig(jdbcCustomTemplate, logger);
				String ccConfirmEmail[] = null;
				if (apptSysConfig != null && apptSysConfig.getCcConfirmEmails() != null && apptSysConfig.getCcConfirmEmails().trim().length() > 0) {
					ccAddressStr = apptSysConfig.getCcConfirmEmails();
				}
				
				if(emailData.get("ENROLLMENTID_CONTAINS_P")!= null && "Y".equals(emailData.get("ENROLLMENTID_CONTAINS_P"))) {
					String ccAddress = PropertyUtils.getValueFromProperties("EXT_LOGIN_NOTFOUND_EMAIL", PropertiesConstants.APPT_SERVICE_REST_WS_PROP.getPropertyFileName());
					if(ccAddress != null &&  !"".equals(ccAddress)) {
						ccAddressStr = ccAddressStr !=null && !"".equals(ccAddressStr) ?ccAddressStr+","+ccAddress:ccAddress;
					} 
				}
				ccConfirmEmail = ccAddressStr.split(",");
				if (ccConfirmEmail != null) {
					emailRequest.setCcAddresses(ccConfirmEmail);
				}
				emailData.put("%CLIENTAPPTLINK%", client.getApptLink() == null ? "" : client.getApptLink());
				emailData.put("%CLIENTNAME%", client.getClientName() == null ? "" : client.getClientName());
				emailData.put("%CLIENTADDRESS%", client.getAddress() == null ? "" : client.getAddress());
				populateDataForEmail(logger, emailData, cdConfig, langCode, cancelAppointResponse.getDisplayKeys(), cancelAppointResponse.getDisplayValues());
				emailData.put("%SCHEDULEID%", "" + scheduleId);
				String email = emailData.get("%CMAIL%");
				if (email != null && !"".equals(email)) {
					Map<String, String> emailTemplateMap = cacheComponent.getEmailTemplateMap(jdbcCustomTemplate, logger, langCode, false);
					if (emailTemplateMap != null) {
						String emailSubjectTemplate = (String) emailTemplateMap.get(EmailTemplateConstants.EMAIL_APPT_CANCEL_SUBJECT.getValue());
						String emailBodyTemplate = (String) emailTemplateMap.get(EmailTemplateConstants.EMAIL_APPT_CANCEL_BODY.getValue());
						if ((emailSubjectTemplate != null && !"".equals(emailSubjectTemplate)) || (emailBodyTemplate != null && !"".equals(emailBodyTemplate))) {
							String emailSubject = emailComponent.getEmailSubject(logger, emailSubjectTemplate, emailData);
							String emailBody = emailComponent.getEmailBody(logger, emailBodyTemplate, emailData);
							emailRequest.setSubject(emailSubject);
							emailRequest.setEmailBody(emailBody);
							emailRequest.setToAddress(email);
							emailRequest.setMethod("REQUEST");
							emailRequest.setStatus("CANCELLED");
							emailRequest.setEmailType("cancel");
							emailComponent.setMailServerPreference(logger, emailRequest);
							emailComponent.sendEmail(logger, emailRequest, emailData);
						}
					} else {
						logger.error("Email templates not configured properly");
					}
				} else {
					logger.warn("Customer email address not available. So email not sending!!!");
				}
			} else {
				logger.error("Cancel appointment failed. Response recieved from cancel appointment stored procedure ::" + errorMessage);
			}
		} catch (Exception e) {
			logger.error("Cancellation email failed to send. " + e, e);
		}
	}

	private void cancelEnrollment(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, Long scheduleId, Client client, String enrollmentId ) {
		String cancelPayload = null;
		try {
			if (enrollmentId != null && !"".equals(enrollmentId)) {
				String loginURL = PropertyUtils.getValueFromProperties("EXT_LOGIN_URL", PropertiesConstants.APPT_SERVICE_REST_WS_PROP.getPropertyFileName());
				StringBuilder payload = new StringBuilder("{\"consumerId\":").append(client.getExtLoginId()).append(",\"secret\":").append("\"")
						.append(client.getExtLoginPassword()).append("\"").append("}");
				Login login = ExternalLoginRestClient.login(logger, loginURL, payload.toString());
				String cancelURL = PropertyUtils.getValueFromProperties("EXT_CANCEL_ENROLLMENT_URL", PropertiesConstants.APPT_SERVICE_REST_WS_PROP.getPropertyFileName());
			    cancelURL= cancelURL.replaceAll("@ENROLLMENTID@", ""+enrollmentId);
			    logger.info("Cancel URL: "+cancelURL);
				boolean cancelEnrollemnt = ExternalLoginRestClient.cancelEnrollment(logger, cancelURL, login.getToken());
				logger.info("CancelAppointment: "+cancelEnrollemnt);
				if (!cancelEnrollemnt) {
					logger.error("cancelEnrollment is failed!");
				}
			} else {
				saveToPendingEnrollment(jdbcCustomTemplate, logger, "cancelEnrollment", cancelPayload, scheduleId);
			}
		} catch (Exception e) {
			logger.error("Error:" + e, e);
			try {
				saveToPendingEnrollment(jdbcCustomTemplate, logger, "cancelEnrollment", cancelPayload, scheduleId);
			} catch (TelAppointException te) {
				logger.error("cancel PendingEnrollement save to db failed.!");
			}
			String sendErrorEmail = "N";
			try {
				sendErrorEmail = PropertyUtils.getValueFromProperties("error.mail.send", PropertiesConstants.APPT_SERVICE_REST_WS_PROP.getPropertyFileName());
				if ("Y".equalsIgnoreCase(sendErrorEmail)) {
					String subject = "Error in ApptFrontEndRestService for ClientName - " + client.getClientName();
					StringBuilder errorMsg = new StringBuilder();
					errorMsg.append("InputParameters: ").append("ScheduleId: " + scheduleId);
					errorMsg.append("<br/> ");
					errorMsg.append(CoreUtils.getMethodAndClassName(e));
					errorMsg.append("<br/>");
					errorMsg.append("Root Cause of Exception:").append("External logic - CancelEnrollment to external system failure");
					errorMsg.append("<br/><br/>");
					errorMsg.append("Stack Trace:");
					errorMsg.append("<br/>");
					errorMsg.append("<br/>" + CoreUtils.getStackTrace(e));
					EmailRequest emailRequest = new EmailRequest();
					emailRequest.setEmailBody(errorMsg.toString());
					emailRequest.setSubject(subject);
					emailRequest.setEmailType("error");
					emailComponent.setErrorMailServerPreference(logger, emailRequest);
					emailComponent.sendEmail(logger, emailRequest, null);
				}
			} catch (Exception ioe) {
				logger.error("Error while sending an error email ::" + ioe, ioe);
			}
		}

	}

	

	@Override
	public ResponseEntity<ResponseModel> saveTransScriptMsg(Logger logger, TransScriptRequest transScriptReq) throws Exception {
		BaseResponse baseResponse = new BaseResponse();
		Client client = cacheComponent.getClient(logger, transScriptReq.getClientCode(), true);
		boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		boolean savedToDB = apptDAO.saveTransScriptMsg(jdbcCustomTemplate, logger, transScriptReq);
		if (!savedToDB) {
			baseResponse.setStatus(false);
			baseResponse.setMessage("Failed to save the TransScript to database.");
		} else {
			if (transScriptReq.isSendEmail()) {
				ClientDeploymentConfig cdConfig = cacheComponent.getClientDeploymentConfig(logger, transScriptReq.getClientCode(), cache);
				Map<String, String> emailData = new HashMap<String, String>();
				emailData.put("%CLIENTAPPTLINK%", client.getApptLink() == null ? "" : client.getApptLink());
				emailData.put("%CLIENTNAME%", client.getClientName() == null ? "" : client.getClientName());
				emailData.put("%CLIENTADDRESS%", client.getAddress() == null ? "" : client.getAddress());
				fillTransScriptFilesPlaceHolder(jdbcCustomTemplate, logger, transScriptReq.getScheduleId(), emailData);
				sendFileUploadTransScriptEmail(jdbcCustomTemplate, logger, cdConfig, emailData, transScriptReq);
			}
		}
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, baseResponse), HttpStatus.CREATED);
	}

	private void sendFileUploadTransScriptEmail(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, ClientDeploymentConfig cdConfig, Map<String, String> emailData,
			TransScriptRequest transScriptReq) {
		try {
			EmailRequest emailRequest = new EmailRequest();
			ApptSysConfig apptSysConfig = apptDAO.getApptSysConfig(jdbcCustomTemplate, logger);
			String ccConfirmEmail[] = null;
			if (apptSysConfig != null && apptSysConfig.getCcConfirmEmails() != null && apptSysConfig.getCcConfirmEmails().trim().length() > 0) {
				ccConfirmEmail = apptSysConfig.getCcConfirmEmails().split(",");
			}
			if (ccConfirmEmail != null) {
				emailRequest.setCcAddresses(ccConfirmEmail);
			}
			String langCode = transScriptReq.getLangCode();
			long scheduleId = transScriptReq.getScheduleId();
			if (langCode == null) {
				langCode = "us-en";
			}
			TransScriptEmailData transUploadEmailData = apptDAO.getFileUploadConfirmEmaildata(jdbcCustomTemplate, logger, langCode, scheduleId);
			populateDataForEmail(logger, emailData, cdConfig, langCode, transUploadEmailData.getDisplayKeys(), transUploadEmailData.getDisplayValues());
			emailData.put("%SCHEDULEID%", "" + transScriptReq.getScheduleId());
			String email = emailData.get("%CMAIL%");
			if (email != null && !"".equals(email)) {
				Map<String, String> emailTemplateMap = cacheComponent.getEmailTemplateMap(jdbcCustomTemplate, logger, transScriptReq.getLangCode(), false);
				if (emailTemplateMap != null) {
					String emailSubjectTemplate = (String) emailTemplateMap.get(EmailTemplateConstants.EMAIL_ADDITIONAL_FILEUPLOAD_SUBJECT.getValue());
					String emailBodyTemplate = (String) emailTemplateMap.get(EmailTemplateConstants.EMAIL_ADDITIONAL_FILEUPLOAD_BODY.getValue());
					String emailSubject = emailComponent.getEmailSubject(logger, emailSubjectTemplate, emailData);
					String emailBody = emailComponent.getEmailBody(logger, emailBodyTemplate, emailData);
					emailRequest.setSubject(emailSubject);
					emailRequest.setEmailBody(emailBody);
					logger.info("Upload Additional TransScript Email Body: " + emailBody);
					emailRequest.setToAddress(email);
					emailRequest.setEmailType("other");
					emailComponent.setMailServerPreference(logger, emailRequest);
					emailComponent.sendEmail(logger, emailRequest, emailData);
				} else {
					logger.error("Email templates not configured properly");
				}
			} else {
				logger.warn("Customer email address not available. So email not sending!!!");
			}
		} catch (Exception e) {
			logger.error("Error while sending transscript file upload confirm email");
		}
	}

	@Override
	public ResponseEntity<ResponseModel> processPendingCreateAndCancelEnrollments(Logger logger, String clientCode) throws Exception {
		BaseResponse baseResponse = new BaseResponse();
		Client client = cacheComponent.getClient(logger, clientCode, true);
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		List<PendingEnrollment> enrollments = apptDAO.getPendingEnrollments(jdbcCustomTemplate, logger);
		String token = "";
		if (enrollments != null && !enrollments.isEmpty()) {
			String loginURL = PropertyUtils.getValueFromProperties("EXT_LOGIN_URL", PropertiesConstants.APPT_SERVICE_REST_WS_PROP.getPropertyFileName());
			StringBuilder payload = new StringBuilder("{\"consumerId\":").append(client.getExtLoginId()).append(",\"secret\":").append("\"").append(client.getExtLoginPassword())
					.append("\"").append("}");
			Login login = ExternalLoginRestClient.login(logger, loginURL, payload.toString());
			token = login.getToken();
		}
		String CREATE_ENROLLMENT_APINAME = "createEnrollment";
		String CANCEL_ENROLLMENT_APINAME = "cancelEnrollment";
		for (PendingEnrollment enrollment : enrollments) {
			if (CREATE_ENROLLMENT_APINAME.equals(enrollment.getApiName())) {
				String enrollURL = PropertyUtils.getValueFromProperties("EXT_CREATE_ENROLLMENT_URL", PropertiesConstants.APPT_SERVICE_REST_WS_PROP.getPropertyFileName());
				String enrollmentPayload = enrollment.getJsonPayLoad();
				System.out.println("enrollmentPayload: " + enrollmentPayload);
				ExternalLoginRestClient.createEnrollment(logger, enrollURL, token, enrollment.getJsonPayLoad());
				EnrollementResponse enrollmentRes = ExternalLoginRestClient.createEnrollment(logger, enrollURL, token, enrollmentPayload);
				if (enrollmentRes != null && !"".equals(enrollmentRes.getEnrollmentId())) {
					boolean status = apptDAO.updateEnrollemntId(jdbcCustomTemplate, logger, enrollmentRes.getEnrollmentId(), enrollment.getScheduleId());
					if (status) {
						apptDAO.updateEnrollmentProcessStatus(jdbcCustomTemplate, logger, CREATE_ENROLLMENT_APINAME, enrollment.getScheduleId());
					} else {
						logger.error("update enrollement id to appointment table failed.!");
					}
				} else {
					logger.error("create enrollement has been failed.!");
				}
			} else if (CANCEL_ENROLLMENT_APINAME.equals(enrollment.getApiName())) {
				String enrollemntId = apptDAO.getEnrollementId(jdbcCustomTemplate, logger, enrollment.getScheduleId());
				String cancelURL = PropertyUtils.getValueFromProperties("EXT_CANCEL_ENROLLMENT_URL", PropertiesConstants.APPT_SERVICE_REST_WS_PROP.getPropertyFileName());
				String cancelPayload = enrollment.getJsonPayLoad();
				cancelPayload = cancelPayload.replaceAll("@ENROLLMENTID@", enrollemntId);
				System.out.println("CancelEnrollmentPayLoad: " + cancelPayload);
				boolean cancelEnrollment = ExternalLoginRestClient.cancelEnrollment(logger, cancelURL, token);
				System.out.println("CancelEnrollment Status: " + cancelEnrollment);
				if (cancelEnrollment) {
					apptDAO.updateEnrollmentProcessStatus(jdbcCustomTemplate, logger, CREATE_ENROLLMENT_APINAME, enrollment.getScheduleId());
				} else {
					logger.error("cancel enrollement has been failed.!");
				}
			}
		}
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, baseResponse), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseModel> deleteTransScriptMsg(Logger logger, String clientCode, long transScriptMsgId) throws Exception {
		BaseResponse baseResponse = new BaseResponse();
		Client client = cacheComponent.getClient(logger, clientCode, true);
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		apptDAO.deleteTransScriptMsg(jdbcCustomTemplate, logger, transScriptMsgId);
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, baseResponse), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseModel> getLang(Logger logger, String clientCode, String device, String langCode) throws Exception {
		IvrLang response = new IvrLang();
		Client client = cacheComponent.getClient(logger, clientCode, true);
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		response = apptDAO.getLang(jdbcCustomTemplate, logger, langCode);
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, response), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseModel> getFirstAvailableDateAnyLocation(Logger logger, String clientCode, String device, String langCode) throws TelAppointException, Exception {
		FirstAvailableDateAnyLocationResponse firstAvailableDate = new FirstAvailableDateAnyLocationResponse();
		Client client = cacheComponent.getClient(logger, clientCode, true);
		boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		ClientDeploymentConfig cdConfig = cacheComponent.getClientDeploymentConfig(logger, clientCode, cache);
		apptDAO.getApptFirstAvailDateTimeAnyLocation(jdbcCustomTemplate, logger, device, cdConfig, firstAvailableDate);
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, firstAvailableDate), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseModel> getServiceListByLocationId(String clientCode, Logger logger, String device, String langCode, Long transId, Integer locationId)
			throws Exception {
		ServiceResponse serviceResponse = new ServiceResponse();
		Client client = cacheComponent.getClient(logger, clientCode, true);
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		List<ServiceOption> serviceList = apptDAO.getServiceListByLocationId(jdbcCustomTemplate, device, locationId);
		serviceResponse.setServiceList(serviceList);
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, serviceResponse), HttpStatus.OK);
	}
	
	@Override
	public void sendEmailInvalidPassword(String clientCode, HttpHeaders httpHeader) throws Exception {
		List<String> passwordList = httpHeader.get("password");
		String password ="";
		if(passwordList != null && !passwordList.isEmpty()) {
			password = passwordList.get(0);
		}
		throw new TelAppointException(ErrorConstants.ERROR_1083.getCode(), ErrorConstants.ERROR_1083.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, "Invalid Password ["+password+"] - clientCode:"+clientCode);
	}

	@Override
	public ResponseEntity<ResponseModel> getMobileDemoClientList(HttpHeaders httpHeader, Logger logger) throws Exception {
		MobileClientResponse mobileClientRes = new MobileClientResponse();
		boolean isPasswordValid = CoreUtils.isValidPassword(httpHeader);
		if(isPasswordValid == false) {
			sendEmailInvalidPassword("MasterDB", httpHeader);
		}
		mobileClientRes.setIphoneClientInfo(masterDAO.getMobileDemoClientList());
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, mobileClientRes), HttpStatus.OK);
	}
	
	@Override
	public ResponseEntity<ResponseModel> getClientListBySearchKey(HttpHeaders httpHeader, Logger logger, String uuid, String searchKey) throws Exception {
		MobileClientResponse mobileClientRes = new MobileClientResponse();
		boolean isPasswordValid = CoreUtils.isValidPassword(httpHeader);
		if(isPasswordValid == false) {
			sendEmailInvalidPassword("MasterDB", httpHeader);
		}
		masterDAO.logRequest(CommonApptDeskConstants.MOBILE.getValue(),uuid, searchKey);
		List<IPhoneClientInfo> iphoneClientInfo = masterDAO.getClientListBySearchKey(searchKey);
		mobileClientRes.setIphoneClientInfo(iphoneClientInfo);
		
		for(IPhoneClientInfo iphoneInfo : iphoneClientInfo) {
			String clientCode = iphoneInfo.getClientCode();
			Client client = cacheComponent.getClient(logger, clientCode, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
			Map<String, String> designTemplates = cacheComponent.getDesignTemplatesMap(jdbcCustomTemplate, logger, CommonApptDeskConstants.MOBILE.getValue(), cache);
			if(designTemplates != null) {
				iphoneInfo.setLogoFileName(designTemplates.get(DesignTemplateConstants.LOGO_TAG.getValue()));
			}
		}
		
		MobileAppPage mobileAppPage = masterDAO.getMobileAppPages("search_result_by_searchkey");
		mobileClientRes.setPageData(getMobileAppPages(mobileAppPage));
		
		
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, mobileClientRes), HttpStatus.OK);
	}
	
	@Override
	public ResponseEntity<ResponseModel> getLanguageList(HttpHeaders httpHeader, Logger logger, String uuid, String clientCode) throws Exception {
		IPhoneClientResponse iphoneClientRes = new IPhoneClientResponse();
		boolean isPasswordValid = CoreUtils.isValidPassword(httpHeader);
		if(isPasswordValid == false) {
			sendEmailInvalidPassword("MasterDB", httpHeader);
		}
		
		masterDAO.logRequest(CommonApptDeskConstants.MOBILE.getValue(), uuid, CommonApptDeskConstants.EMPTY_STRING.getValue());
		Client client = cacheComponent.getClient(logger, clientCode, true);
		boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		List<Language> languages = cacheComponent.getLangList(jdbcCustomTemplate, logger, cache);
		iphoneClientRes.setClientCode(clientCode);
		iphoneClientRes.setLanguages(languages);
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, iphoneClientRes), HttpStatus.OK);
	}
	

	@Override
	public ResponseEntity<ResponseModel> getClientListByClientCode(HttpHeaders httpHeader, Logger logger, String uuid, String clientCode) throws Exception {
		MobileClientResponse mobileClientRes = new MobileClientResponse();
		boolean isPasswordValid = CoreUtils.isValidPassword(httpHeader);
		if(isPasswordValid == false) {
			sendEmailInvalidPassword("MasterDB", httpHeader);
		}
		masterDAO.logRequest(CommonApptDeskConstants.MOBILE.getValue(),uuid, clientCode);
		List<IPhoneClientInfo> iphoneClientInfo = masterDAO.getClientListByClientCode(clientCode);
		mobileClientRes.setIphoneClientInfo(iphoneClientInfo);
		for(IPhoneClientInfo iphoneInfo : iphoneClientInfo) {
			clientCode = iphoneInfo.getClientCode();
			Client client = cacheComponent.getClient(logger, clientCode, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
			Map<String, String> designTemplates = cacheComponent.getDesignTemplatesMap(jdbcCustomTemplate, logger, CommonApptDeskConstants.MOBILE.getValue(), cache);
			if(designTemplates != null) {
				iphoneInfo.setLogoFileName(designTemplates.get(DesignTemplateConstants.LOGO_TAG.getValue()));
			}
		}
		MobileAppPage mobileAppPage = masterDAO.getMobileAppPages("clientcode_search_result_page");
		mobileClientRes.setPageData(getMobileAppPages(mobileAppPage));
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, mobileClientRes), HttpStatus.OK);
	}
	
	@Override
	public ResponseEntity<ResponseModel> getClientListByMobileCode(HttpHeaders httpHeader, Logger logger, String uuid, String mobileCode) throws Exception {
		MobileClientResponse mobileClientRes = new MobileClientResponse();
		boolean isPasswordValid = CoreUtils.isValidPassword(httpHeader);
		if(isPasswordValid == false) {
			sendEmailInvalidPassword("MasterDB", httpHeader);
		}
		masterDAO.logRequest(CommonApptDeskConstants.MOBILE.getValue(),uuid, mobileCode);
		List<IPhoneClientInfo> iphoneClientInfo = masterDAO.getClientListByMobileCode(mobileCode);
		mobileClientRes.setIphoneClientInfo(iphoneClientInfo);
		for(IPhoneClientInfo iphoneInfo : iphoneClientInfo) {
			String clientCode = iphoneInfo.getClientCode();
			Client client = cacheComponent.getClient(logger, clientCode, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
			Map<String, String> designTemplates = cacheComponent.getDesignTemplatesMap(jdbcCustomTemplate, logger, CommonApptDeskConstants.MOBILE.getValue(), cache);
			if(designTemplates != null) {
				iphoneInfo.setLogoFileName(designTemplates.get(DesignTemplateConstants.LOGO_TAG.getValue()));
			}
		}
		MobileAppPage mobileAppPage = masterDAO.getMobileAppPages("mobilecode_search_result_page");
		mobileClientRes.setPageData(getMobileAppPages(mobileAppPage));
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, mobileClientRes), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseModel> getClientInfo(Logger logger, String clientCode, String device, String langCode) throws Exception {
		ClientInfoResponse response = new ClientInfoResponse();
		Client client = cacheComponent.getClient(logger, clientCode, true);
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		ApptSysConfig apptSysConfig = apptDAO.getApptSysConfig(jdbcCustomTemplate, logger);
		ClientInfo clientInfo = new ClientInfo();
		clientInfo.setLoginFirst(apptSysConfig.getLoginFirst());
		String expiryTokenStr = PropertyUtils.getValueFromProperties("TOKEN_EXPIRY_SEC", PropertiesConstants.APPT_SERVICE_REST_WS_PROP.getPropertyFileName());
		if(expiryTokenStr == null) expiryTokenStr="300";
		clientInfo.setToken(apptDAO.insertTokenAndGet(jdbcCustomTemplate, clientCode, Integer.valueOf(expiryTokenStr)));
		response.setClientInfo(clientInfo);
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, response), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseModel> getLocResSerInfo(Logger logger, String clientCode, String device, String langCode, String token) throws Exception {
		LocResSerResponse response = new LocResSerResponse();
		Client client = cacheComponent.getClient(logger, clientCode, true);
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		if(CoreUtils.isMobile(device) && !isTokenValid(jdbcCustomTemplate, clientCode, token)) {
			response.setStatus(false);
			response.setMessage(PropertyUtils.getValueFromProperties("INVALID_TOKEN_MESSAGE", PropertiesConstants.APPT_SERVICE_REST_WS_PROP.getPropertyFileName()));
			return new ResponseEntity<>(commonComponent.populateRMDData(logger, response), HttpStatus.OK);
		}
		boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
		ApptSysConfig apptSysConfig = apptDAO.getApptSysConfig(jdbcCustomTemplate, logger);
		LocationResourceServiceInfo locationResSerInfo = response.getLocResSerInfo();
		locationResSerInfo.setDisplayCompany(apptSysConfig.getDisplayCompany());
		locationResSerInfo.setDisplayProcedure(apptSysConfig.getDisplayProcedure());
		locationResSerInfo.setDisplayLocation(apptSysConfig.getDisplayLocation());
		locationResSerInfo.setDisplayDepartment(apptSysConfig.getDisplayDepartment());
		locationResSerInfo.setDisplayResource(apptSysConfig.getDisplayResource());
		locationResSerInfo.setDisplayService(apptSysConfig.getDisplayService());
		ClientDeploymentConfig cdConfig = cacheComponent.getClientDeploymentConfig(logger, clientCode, cache);
		locationResSerInfo.setCallCenterLogic(cdConfig.getCallCenterLogic());
		
		if(CoreUtils.isMobile(device)) {
			MobileAppPage mobileAppPage = apptDAO.getMobileAppPages(jdbcCustomTemplate, "select_service_page");
			response.setPageData(getMobileAppPages(mobileAppPage));
		} else if(CoreUtils.isIVR(device)) {
			//TODO: 
		}
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, response), HttpStatus.OK);
	}
	
	@Override
	public ResponseEntity<ResponseModel> getCompanyList(Logger logger, String clientCode, String device, String langCode, String token) throws Exception {
		CompanyResponse response = new CompanyResponse();
		Client client = cacheComponent.getClient(logger, clientCode, true);
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		if(CoreUtils.isMobile(device) && !isTokenValid(jdbcCustomTemplate, clientCode, token)) {
			response.setStatus(false);
			response.setMessage(PropertyUtils.getValueFromProperties("INVALID_TOKEN_MESSAGE", PropertiesConstants.APPT_SERVICE_REST_WS_PROP.getPropertyFileName()));
			return new ResponseEntity<>(commonComponent.populateRMDData(logger, response), HttpStatus.OK);
		}
		boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
		List<Company> companyList = apptDAO.getCompanyList(jdbcCustomTemplate, logger); 
		Map<String, String> aliasMap = cacheComponent.getDisplayAliasMap(jdbcCustomTemplate, logger, device, langCode, cache);
		
		for(Company company : companyList) {
			String companyName = aliasMap != null && aliasMap.get(company.getCompanyNameOnline()) != null ? aliasMap.get(company.getCompanyNameOnline()):company.getCompanyNameOnline();
			company.setCompanyNameOnline(companyName);
			if(CoreUtils.isIVR(device)) {
				String companyNameTts = aliasMap != null && aliasMap.get(company.getCompanyNameTts()) != null ? aliasMap.get(company.getCompanyNameTts()):company.getCompanyNameTts();
				String companyNameAudio = aliasMap != null && aliasMap.get(company.getCompanyNameAudio()) != null ? aliasMap.get(company.getCompanyNameAudio()):company.getCompanyNameAudio();
				company.setCompanyNameTts(companyNameTts);
				company.setCompanyNameAudio(companyNameAudio);
			} else {
				company.setCompanyNameTts(null);
				company.setCompanyNameAudio(null);
			}
		}
		response.setCompanyList(companyList);
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, response), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseModel> getProcedureList(Logger logger, String clientCode, String device, String langCode, String companyId, String token) throws Exception {
		ProcedureResponse response = new ProcedureResponse();
		Client client = cacheComponent.getClient(logger, clientCode, true);
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		if(CoreUtils.isMobile(device) && !isTokenValid(jdbcCustomTemplate, clientCode, token)) {
			response.setStatus(false);
			response.setMessage(PropertyUtils.getValueFromProperties("INVALID_TOKEN_MESSAGE", PropertiesConstants.APPT_SERVICE_REST_WS_PROP.getPropertyFileName()));
			return new ResponseEntity<>(commonComponent.populateRMDData(logger, response), HttpStatus.OK);
		}
		boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
		List<Procedure> procedureList = apptDAO.getProcedureList(jdbcCustomTemplate, logger, companyId); 
		Map<String, String> aliasMap = cacheComponent.getDisplayAliasMap(jdbcCustomTemplate, logger, device, langCode, cache);
		
		for(Procedure procedure : procedureList) {
			String procedureName = aliasMap != null && aliasMap.get(procedure.getProcedureNameOnline()) != null?aliasMap.get(procedure.getProcedureNameOnline()):procedure.getProcedureNameOnline();
			procedure.setProcedureNameOnline(procedureName);
			if(CoreUtils.isIVR(device)) {
				String procedureTts = aliasMap != null && aliasMap.get(procedure.getProcedureNameTts()) != null?aliasMap.get(procedure.getProcedureNameTts()):procedure.getProcedureNameTts();
				String procedureAudio = aliasMap != null && aliasMap.get(procedure.getProcedureNameAudio()) != null?aliasMap.get(procedure.getProcedureNameAudio()):procedure.getProcedureNameAudio();
				procedure.setProcedureNameTts(procedureTts);
				procedure.setProcedureNameAudio(procedureAudio);
			} else {
				procedure.setProcedureNameTts(null);
				procedure.setProcedureNameAudio(null);
			}
		}
		response.setProcedureList(procedureList);
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, response), HttpStatus.OK);
	}
	
	@Override
	public ResponseEntity<ResponseModel> getLocationList(Logger logger, String clientCode, String device, String langCode, String procedureId, String token) throws Exception {
		LocationResponse response = new LocationResponse();
		Client client = cacheComponent.getClient(logger, clientCode, true);
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		if(CoreUtils.isMobile(device) && !isTokenValid(jdbcCustomTemplate, clientCode, token)) {
			response.setStatus(false);
			response.setMessage(PropertyUtils.getValueFromProperties("INVALID_TOKEN_MESSAGE", PropertiesConstants.APPT_SERVICE_REST_WS_PROP.getPropertyFileName()));
			return new ResponseEntity<>(commonComponent.populateRMDData(logger, response), HttpStatus.OK);
		}
		boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
		List<Location> locationList = apptDAO.getLocationList(jdbcCustomTemplate, logger, procedureId); 
		Map<String, String> aliasMap = cacheComponent.getDisplayAliasMap(jdbcCustomTemplate, logger, device, langCode, cache);
		
		for(Location location : locationList) {
			String locationName = aliasMap != null && aliasMap.get(location.getLocationName())!=null?aliasMap.get(location.getLocationName()):location.getLocationName();
			location.setLocationName(locationName);
			if(CoreUtils.isIVR(device)) {
				String locationNameTts = aliasMap != null && aliasMap.get(location.getLocationNameIvrTts())!=null?aliasMap.get(location.getLocationNameIvrTts()):location.getLocationNameIvrTts();
				String locationNameAudio = aliasMap != null && aliasMap.get(location.getLocationNameIvrAudio())!=null?aliasMap.get(location.getLocationNameIvrAudio()):location.getLocationNameIvrAudio();
				location.setLocationNameIvrTts(locationNameTts);
				location.setLocationNameIvrAudio(locationNameAudio);
				location.setAddress(null);
				location.setCity(null);
				location.setState(null);
				location.setZip(null);
			} else {
				location.setLocationNameIvrTts(null);
				location.setLocationNameIvrAudio(null);
			}
			location.setEnable(null);
		}
		response.setLocations(locationList);
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, response), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseModel> getDepartmentList(Logger logger, String clientCode, String device, String langCode, String locationId, String token) throws Exception {
		DepartmentResponse response = new DepartmentResponse();
		Client client = cacheComponent.getClient(logger, clientCode, true);
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		if(CoreUtils.isMobile(device) && !isTokenValid(jdbcCustomTemplate, clientCode, token)) {
			response.setStatus(false);
			response.setMessage(PropertyUtils.getValueFromProperties("INVALID_TOKEN_MESSAGE", PropertiesConstants.APPT_SERVICE_REST_WS_PROP.getPropertyFileName()));
			return new ResponseEntity<>(commonComponent.populateRMDData(logger, response), HttpStatus.OK);
		}
		boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
		List<Department> departmentList = apptDAO.getDepartmentList(jdbcCustomTemplate, logger, locationId); 
		Map<String, String> aliasMap = cacheComponent.getDisplayAliasMap(jdbcCustomTemplate, logger, device, langCode, cache);
		
		for(Department department : departmentList) {
			String departmentName = aliasMap != null && aliasMap.get(department.getDepartmentName())!=null?aliasMap.get(department.getDepartmentName()):department.getDepartmentName();
			department.setDepartmentName(departmentName);
			
			if(CoreUtils.isIVR(device)) {
				String departmentNameTts = aliasMap != null && aliasMap.get(department.getDepartmentNameIvrTts())!=null?aliasMap.get(department.getDepartmentNameIvrTts()):department.getDepartmentNameIvrTts();
				String departmentNameAudio = aliasMap != null && aliasMap.get(department.getDepartmentNameIvrAudio())!=null?aliasMap.get(department.getDepartmentNameIvrAudio()):department.getDepartmentNameIvrAudio();
				department.setDepartmentNameIvrTts(departmentNameTts);
				department.setDepartmentNameIvrAudio(departmentNameAudio);
			} else {
				department.setDepartmentNameIvrTts(null);
				department.setDepartmentNameIvrAudio(null);
			}
		}
		response.setDepartmentList(departmentList);
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, response), HttpStatus.OK);
	}
	
	
	@Override
	public ResponseEntity<ResponseModel> getResourceList(Logger logger, String clientCode, String device,  String langCode, String locationId, String departmentId, String token) throws Exception {
		ResourceResponse response = new ResourceResponse();
		Client client = cacheComponent.getClient(logger, clientCode, true);
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		if(CoreUtils.isMobile(device) && !isTokenValid(jdbcCustomTemplate, clientCode, token)) {
			response.setStatus(false);
			response.setMessage(PropertyUtils.getValueFromProperties("INVALID_TOKEN_MESSAGE", PropertiesConstants.APPT_SERVICE_REST_WS_PROP.getPropertyFileName()));
			return new ResponseEntity<>(commonComponent.populateRMDData(logger, response), HttpStatus.OK);
		}
		boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
		List<Resource> resourceList = apptDAO.getResourceList(jdbcCustomTemplate, logger, locationId, departmentId); 
		Map<String, String> aliasMap = cacheComponent.getDisplayAliasMap(jdbcCustomTemplate, logger, device, langCode, cache);
		
		for(Resource resource : resourceList) {
			String resourceName = aliasMap != null && aliasMap.get(resource.getResourceName())!=null?aliasMap.get(resource.getResourceName()):resource.getResourceName();
			resource.setResourceName(resourceName);
			if(CoreUtils.isIVR(device)) {
				String resourceNameAudio = aliasMap != null && aliasMap.get(resource.getResourceAudio())!=null?aliasMap.get(resource.getResourceAudio()):resource.getResourceAudio();
				resource.setResourceAudio(resourceNameAudio);
			} else {
				resource.setResourceAudio(null);
			}
		}
		response.setResourceList(resourceList);
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, response), HttpStatus.OK);
	}
	
	@Override
	public ResponseEntity<ResponseModel> getServiceList(Logger logger, String clientCode, String device, String langCode, Integer locationId, Integer resourceId, Integer departmentId, String token) throws Exception {
		ServiceResponse response = new ServiceResponse();
		Client client = cacheComponent.getClient(logger, clientCode, true);
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		if(CoreUtils.isMobile(device) && !isTokenValid(jdbcCustomTemplate, clientCode, token)) {
			response.setStatus(false);
			response.setMessage(PropertyUtils.getValueFromProperties("INVALID_TOKEN_MESSAGE", PropertiesConstants.APPT_SERVICE_REST_WS_PROP.getPropertyFileName()));
			return new ResponseEntity<>(commonComponent.populateRMDData(logger, response), HttpStatus.OK);
		}
		boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
		ClientDeploymentConfig cdConfig = cacheComponent.getClientDeploymentConfig(logger, clientCode, cache);
		List<com.telappoint.apptdesk.model.Service> services = null;
		if("Y".equalsIgnoreCase(cdConfig.getCallCenterLogic())) {
			services = apptDAO.getServicesCallcenter(jdbcCustomTemplate, logger, device, langCode, locationId, departmentId);
		} else {
			services = apptDAO.getServicesNonCallcenter(jdbcCustomTemplate, logger, device, langCode, locationId, resourceId, departmentId); 
		}
		Map<String, String> aliasMap = cacheComponent.getDisplayAliasMap(jdbcCustomTemplate, logger, device, langCode, cache);

		for(com.telappoint.apptdesk.model.Service service : services) {
			String serviceName = aliasMap != null && aliasMap.get(service.getServiceNameOnline())!=null?aliasMap.get(service.getServiceNameOnline()):service.getServiceNameOnline();
			service.setServiceNameOnline(serviceName);
		}
		response.setServices(services);
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, response), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseModel> getMobileHomePage(Logger logger, String langCode, String uuid) throws Exception {
		HomePageResponse homePageRes = new HomePageResponse();
		MobileAppPage mobileAppPage = masterDAO.getMobileAppPages("home_page");
		homePageRes.setPageData(getMobileAppPages(mobileAppPage));
		homePageRes.setClientList(masterDAO.getMyProvidersList(uuid));
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, homePageRes), HttpStatus.OK);
	}
	
	private Map<String,String> getMobileAppPages(MobileAppPage mobileAppPage) {
		Map<String, String> data = new LinkedHashMap<>();
		if(mobileAppPage != null) {
			if(mobileAppPage.getContentKey() != null && !"".equals(mobileAppPage.getContentKey())) {
				String [] keys = mobileAppPage.getContentKey().split("\\|");
				String [] values = mobileAppPage.getContentValue().split("\\|");
				for(int i=0;i<keys.length;i++) {
					data.put(keys[i], values[i]);
				}
			}
		}
		return data;
	}

	@Override
	@Deprecated
	public ResponseEntity<ResponseModel> getMobileHomePageInfo(Logger logger, String langCode) throws Exception {
		HomePageResponse homePageRes = new HomePageResponse();
		MobileAppPage mobileAppPage = masterDAO.getMobileAppPages("home_page_info");
		homePageRes.setPageData(getMobileAppPages(mobileAppPage));
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, homePageRes), HttpStatus.OK);
	}

	@Override
	@Deprecated
	public ResponseEntity<ResponseModel> getMobileHomePageInitial(Logger logger, String langCode) throws Exception {
		HomePageResponse homePageRes = new HomePageResponse();
		MobileAppPage mobileAppPage = masterDAO.getMobileAppPages("home_page_init");
		homePageRes.setPageData(getMobileAppPages(mobileAppPage));
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, homePageRes), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseModel> getMobileApptsFeaturesHomePage(Logger logger, String clientCode, String langCode, String token) throws Exception {
		HomePageResponse response = new HomePageResponse();
		Client client = cacheComponent.getClient(logger, clientCode, true);
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		if(!isTokenValid(jdbcCustomTemplate, clientCode, token)) {
			response.setStatus(false);
			response.setMessage(PropertyUtils.getValueFromProperties("INVALID_TOKEN_MESSAGE", PropertiesConstants.APPT_SERVICE_REST_WS_PROP.getPropertyFileName()));
			return new ResponseEntity<>(commonComponent.populateRMDData(logger, response), HttpStatus.OK);
		}
		MobileAppPage mobileAppPage = apptDAO.getMobileAppPages(jdbcCustomTemplate, "appt_features_home_page");
		response.setPageData(getMobileAppPages(mobileAppPage));
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, response), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseModel> getSearchFieldInitValue(Logger logger, String langCode) throws Exception {
		HomePageResponse homePageRes = new HomePageResponse();
		MobileAppPage mobileAppPage = masterDAO.getMobileAppPages("search_field_init_value");
		homePageRes.setPageData(getMobileAppPages(mobileAppPage));
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, homePageRes), HttpStatus.OK);
	}
	
	public ResponseEntity<ResponseModel> getMobileMenuItems(Logger logger, String langCode, String uuid) throws Exception {
		HomePageResponse homePageRes = new HomePageResponse();
		
		MobileAppPage mobileAppPage = masterDAO.getMobileAppPages("menu_items");
		homePageRes.setPageData(getMobileAppPages(mobileAppPage));
		List<Long> customerIdList = masterDAO.getCustomerIdFromMyProvidersByUuid(logger, uuid);
		Map<String,String> displayFlags = new HashMap<>();
		displayFlags.put("displayNotificationPreference", "N"); //TODO we will do later.
		String clientCode = masterDAO.getClientCodeByUuid(uuid);
		if(clientCode != null) {
			displayFlags.put("displayMyProviders","Y");
		} else {
			displayFlags.put("displayMyProviders","N");
		}
		
		if(customerIdList == null || customerIdList.isEmpty() ) {
			displayFlags.put("displayMyAccount", "N");
			displayFlags.put("displayExistingAppointments", "N");
			homePageRes.setFirstName("");
			homePageRes.setLastName("");
		} else {
			Long customerId = customerIdList.get(0);
			displayFlags.put("displayMyAccount", "Y");
			if(clientCode != null) {
				Client client = cacheComponent.getClient(logger, clientCode, true);
				JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
				CustomerInfo customerInfo = new CustomerInfo();
				apptDAO.getCustomerInfo(jdbcCustomTemplate, logger, CommonApptDeskConstants.MOBILE.getValue(), customerId, customerInfo);
				homePageRes.setFirstName(customerInfo.getCustomer() ==null?"":customerInfo.getCustomer().getFirstName());
				homePageRes.setLastName(customerInfo.getCustomer()==null?"":customerInfo.getCustomer().getLastName());
				boolean isApptsExist = false;
				for(Long cusId : customerIdList) {
					isApptsExist = apptDAO.isAppointmentsExist(jdbcCustomTemplate, logger, cusId);
					if(isApptsExist) {
						break;
					}
				}
				
				if(isApptsExist) {
					displayFlags.put("displayExistingAppointments", "Y");
				} else {
					displayFlags.put("displayExistingAppointments", "N");
				}
				
			} else {
				displayFlags.put("displayExistingAppointments", "N");
			}
		}
		homePageRes.setDisplayFlags(displayFlags);
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, homePageRes), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseModel> getIconAndButtonLabels(Logger logger, String clientCode, String langCode, String token) throws Exception {
		HomePageResponse response = new HomePageResponse();
		Client client = cacheComponent.getClient(logger, clientCode, true);
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		if(!isTokenValid(jdbcCustomTemplate, clientCode, token)) {
			response.setStatus(false);
			response.setMessage(PropertyUtils.getValueFromProperties("INVALID_TOKEN_MESSAGE", PropertiesConstants.APPT_SERVICE_REST_WS_PROP.getPropertyFileName()));
			return new ResponseEntity<>(commonComponent.populateRMDData(logger, response), HttpStatus.OK);
		}
		MobileAppPage iconLabels = apptDAO.getMobileAppPages(jdbcCustomTemplate, "icon_labels");
		MobileAppPage buttonLabels = apptDAO.getMobileAppPages(jdbcCustomTemplate, "button_labels");
		Map<String,String> map = getMobileAppPages(iconLabels);
		map.putAll(getMobileAppPages(buttonLabels));
		response.setPageData(map);
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, response), HttpStatus.OK);
	}
	
	@Override
	public ResponseEntity<ResponseModel> getLocations(Logger logger, String clientCode, String langCode, String token) throws Exception {
		LocationResponse response = new LocationResponse();
		Client client = cacheComponent.getClient(logger, clientCode, true);
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		if(!isTokenValid(jdbcCustomTemplate, clientCode, token)) {
			response.setStatus(false);
			response.setMessage(PropertyUtils.getValueFromProperties("INVALID_TOKEN_MESSAGE", PropertiesConstants.APPT_SERVICE_REST_WS_PROP.getPropertyFileName()));
			return new ResponseEntity<>(commonComponent.populateRMDData(logger, response), HttpStatus.OK);
		}
		response.setLocations(apptDAO.getLocationList(jdbcCustomTemplate, true));
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, response), HttpStatus.OK);
	}
	
	public ResponseEntity<ResponseModel> getMessages(Logger logger, String clientCode, String langCode, String token) throws Exception {
		MessageResponse response = new MessageResponse();
		Client client = cacheComponent.getClient(logger, clientCode, true);
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		if(!isTokenValid(jdbcCustomTemplate, clientCode, token)) {
			response.setStatus(false);
			response.setMessage(PropertyUtils.getValueFromProperties("INVALID_TOKEN_MESSAGE", PropertiesConstants.APPT_SERVICE_REST_WS_PROP.getPropertyFileName()));
			return new ResponseEntity<>(commonComponent.populateRMDData(logger, response), HttpStatus.OK);
		}
		response.setMessages(apptDAO.getMessages(jdbcCustomTemplate));
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, response), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseModel> getListOfDocsTOBring(Logger logger, String clientCode, String langCode, Integer serviceId, String token) throws Exception {
		ListOfDocsTOBringResponse response = new ListOfDocsTOBringResponse();
		Client client = cacheComponent.getClient(logger, clientCode, true);
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		if(!isTokenValid(jdbcCustomTemplate, clientCode, token)) {
			response.setStatus(false);
			response.setMessage(PropertyUtils.getValueFromProperties("INVALID_TOKEN_MESSAGE", PropertiesConstants.APPT_SERVICE_REST_WS_PROP.getPropertyFileName()));
			return new ResponseEntity<>(commonComponent.populateRMDData(logger, response), HttpStatus.OK);
		}
		response.setListOfDocs(apptDAO.getListOfDocsToBring(jdbcCustomTemplate, serviceId));
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, response), HttpStatus.OK);
	}
	
	private boolean isTokenValid(JdbcCustomTemplate jdbcCustomTemplate, String clientCode, String token) throws Exception {
		boolean isValidToken = apptDAO.isTokenValid(jdbcCustomTemplate, clientCode, token);
		return isValidToken;
	}

	@Override
	public ResponseEntity<ResponseModel> getAvailableDates(Logger logger, String clientCode, String langCode, String device, Long locationId, Long departmentId, Long resourceId, Long serviceId, String token)
			throws Exception {
		AvailableDateTimes availableDates = new AvailableDateTimes();
		Client client = cacheComponent.getClient(logger, clientCode, true);
		ClientDeploymentConfig cdConfig = cacheComponent.getClientDeploymentConfig(logger, clientCode, true);
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		if(CoreUtils.isMobile(device) && !isTokenValid(jdbcCustomTemplate, clientCode, token)) {
			availableDates.setStatus(false);
			availableDates.setMessage(PropertyUtils.getValueFromProperties("INVALID_TOKEN_MESSAGE", PropertiesConstants.APPT_SERVICE_REST_WS_PROP.getPropertyFileName()));
			return new ResponseEntity<>(commonComponent.populateRMDData(logger, availableDates), HttpStatus.OK);
		}
		 
		if("Y".equals(cdConfig.getCallCenterLogic())) {
			availableDates = apptDAO.getAvailableDatesCallcenter(jdbcCustomTemplate, logger, cdConfig.getTimeZone(), locationId, departmentId, serviceId,Long.valueOf(cdConfig.getBlockTimeInMins()));
		} else {
			if(resourceId <= 0) {
				logger.info("resourceId passed from front end: ["+resourceId+"], It should be greater than or equals to zero because of non call center logic.");
				availableDates.setStatus(true);
				return new ResponseEntity<>(commonComponent.populateRMDData(logger, availableDates), HttpStatus.OK);
			}
			availableDates = apptDAO.getAvailableDates(jdbcCustomTemplate, logger, cdConfig.getTimeZone(), locationId, departmentId, resourceId, serviceId,Long.valueOf(cdConfig.getBlockTimeInMins()));
		}
		
		availableDates.setTimeZone(cdConfig.getTimeZone());
		if(CoreUtils.isMobile(device)) {
		    List<String> holidayList = apptDAO.getHolidaysMap(jdbcCustomTemplate, cdConfig.getTimeZone());
		    List<String> closedDayList = apptDAO.getClosedDaysMap(jdbcCustomTemplate, locationId, cdConfig.getTimeZone());
		    availableDates.setHolidayListArray(holidayList);
		    availableDates.setClosedDayList(closedDayList);
		    availableDates.setIsSlotAvailableMessage("Available");
		    availableDates.setIsNotAvailableMessage("Not Available");
		    availableDates.setIsHolidayMessage("Holiday");
		    availableDates.setIsClosedMessage("Closed");
			MobileAppPage labels = apptDAO.getMobileAppPages(jdbcCustomTemplate, "select_date_page");
			availableDates.setPageData(getMobileAppPages(labels));
		}
		String dates = availableDates.getAvailableDates();
		if(dates != null && !"".equals(dates)) {
			if(CoreUtils.isMobile(device) || CoreUtils.isIVR(device)) {
				availableDates.setAvailableDatesArray(Arrays.asList(dates.split(",")));
				availableDates.setAvailableDates(null);
			} 
		}
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, availableDates), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseModel> getAvailableTimes(String clientCode, String device, Long locationId, Long departmentId, Long resourceId, Long serviceId, String availDate, Logger logger, String langCode, String token) throws Exception {
		Client client = cacheComponent.getClient(logger, clientCode, true);
		ClientDeploymentConfig cdConfig = cacheComponent.getClientDeploymentConfig(logger, clientCode, true);
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		AvailableDateTimes availableTimes = new AvailableDateTimes();
		if(CoreUtils.isMobile(device) && !isTokenValid(jdbcCustomTemplate, clientCode, token)) {
			availableTimes.setStatus(false);
			availableTimes.setMessage(PropertyUtils.getValueFromProperties("INVALID_TOKEN_MESSAGE", PropertiesConstants.APPT_SERVICE_REST_WS_PROP.getPropertyFileName()));
			return new ResponseEntity<>(commonComponent.populateRMDData(logger, availableTimes), HttpStatus.OK);
		}
		if("Y".equals(cdConfig.getCallCenterLogic())) {
			availableTimes = apptDAO.getAvailableTimesCallcenter(jdbcCustomTemplate, logger, locationId, departmentId, serviceId, availDate,Long.valueOf(cdConfig.getBlockTimeInMins()));
		} else {
			if(resourceId > 0) {
				availableTimes = apptDAO.getAvailableTimes(jdbcCustomTemplate, logger, locationId, departmentId, resourceId, serviceId, availDate,Long.valueOf(cdConfig.getBlockTimeInMins()));
			} else {
				logger.info("resourceId passed from front end: ["+resourceId+"], It should be greater than or equals to zero because of non call center logic.");
				availableTimes.setStatus(true);
				return new ResponseEntity<>(commonComponent.populateRMDData(logger, availableTimes), HttpStatus.OK);
			}
		}

		availableTimes.setTimeZone(cdConfig.getTimeZone());
		String times = availableTimes.getAvailableTimes();
		if(times != null && !"".equals(times)) {
			if(CoreUtils.isMobile(device)) {
				availableTimes.setAvailableTimesArray(Arrays.asList(times.split(",")));
				availableTimes.setAvailableTimes(null);
				if(CoreUtils.isMobile(device)) {
					MobileAppPage labels = apptDAO.getMobileAppPages(jdbcCustomTemplate, "select_time_page");
					availableTimes.setPageData(getMobileAppPages(labels));
				}
			} else if(CoreUtils.isIVR(device)) {
				//TODO
			}
		}
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, availableTimes), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseModel> getMobilePageFields(Logger logger, String clientCode, String langCode, String device) throws Exception {
		MobilePageFieldsReponse mobilePageFieldResponse = new MobilePageFieldsReponse();
		Client client = cacheComponent.getClient(logger, clientCode, true);
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		List<LoginPageFields> mobilePageFields = new ArrayList<>();
		
		Map<String, String> labelMap = cacheComponent.getDisplayFieldLabelsMap(jdbcCustomTemplate, logger, device, langCode, true);
		apptDAO.getMobilePageFields(logger, jdbcCustomTemplate, langCode, mobilePageFields, labelMap, "all");
	
		mobilePageFieldResponse.setMobilePageFields(mobilePageFields);
		MobileAppPage mobileAppPage = apptDAO.getMobileAppPages(jdbcCustomTemplate, "customer_login_page");
		mobilePageFieldResponse.setPageData(getMobileAppPages(mobileAppPage));
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, mobilePageFieldResponse), HttpStatus.OK);
	}
	
	@Override
	public ResponseEntity<ResponseModel> getMobilePageFieldsForCancelAppts(Logger logger, String clientCode, String langCode, String device) throws Exception {
		MobilePageFieldsReponse mobilePageFieldResponse = new MobilePageFieldsReponse();
		Client client = cacheComponent.getClient(logger, clientCode, true);
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		List<LoginPageFields> mobilePageFields = new ArrayList<>();
		
		Map<String, String> labelMap = cacheComponent.getDisplayFieldLabelsMap(jdbcCustomTemplate, logger, device, langCode, true);
		apptDAO.getMobilePageFields(logger, jdbcCustomTemplate, langCode, mobilePageFields, labelMap, "authenticate");
		mobilePageFieldResponse.setMobilePageFields(mobilePageFields);
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, mobilePageFieldResponse), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseModel> holdAppointment(String clientCode, Long locationId, Long procedureId, Long departmentId, Long resourceId, Long serviceId, Long customerId,
			String apptDateTime, Logger logger, String device, String langCode, Long transId, String token) throws Exception {
		Client client = cacheComponent.getClient(logger, clientCode, true);
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		ClientDeploymentConfig cdConfig = cacheComponent.getClientDeploymentConfig(logger, clientCode, true);
		HoldAppt holdAppt= new HoldAppt();
		boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
		
		if(CoreUtils.isMobile(device) && !isTokenValid(jdbcCustomTemplate, clientCode, token)) {
			holdAppt.setStatus(false);
			holdAppt.setMessage(PropertyUtils.getValueFromProperties("INVALID_TOKEN_MESSAGE", PropertiesConstants.APPT_SERVICE_REST_WS_PROP.getPropertyFileName()));
			return new ResponseEntity<>(commonComponent.populateRMDData(logger, holdAppt), HttpStatus.OK);
		}
		
		synchronized (lock) {
			if("Y".equals(cdConfig.getCallCenterLogic())) {
				holdAppt = apptDAO.holdAppointmentCallCenter(jdbcCustomTemplate, logger, device, locationId, procedureId, departmentId, serviceId, customerId, apptDateTime, cdConfig,
						transId);
			} else {
				if(resourceId <= 0) {
					logger.info("resourceId passed from front end: ["+resourceId+"], It should be greater than or equals to zero because of non call center logic.");
					holdAppt.setStatus(false);
					holdAppt.setErrorFlag("Y");
					holdAppt.setErrorMessage("resourceId passed from front end: ["+resourceId+"], It should be greater than or equals to zero because of non call center logic.");
					return new ResponseEntity<>(commonComponent.populateRMDData(logger, holdAppt), HttpStatus.OK);
				}
				holdAppt = apptDAO.holdAppointment(logger,jdbcCustomTemplate, device, locationId, procedureId, departmentId, resourceId, serviceId, customerId, apptDateTime, cdConfig, transId);
			}
		}

		String errorMsg = holdAppt.getErrorMessage();
		if (CoreUtils.isOnline(device) || CoreUtils.isMobile(device)) {
			if (langCode == null || "".equals(langCode)) {
				langCode = "us-en";
			}
			if (errorMsg != null && "DUPLICATE_APPT".equalsIgnoreCase(errorMsg.toString())) {
				holdAppt.setErrorFlag("Y");
				Map<String, String> labelMap = cacheComponent.getDisplayFieldLabelsMap(jdbcCustomTemplate, logger, device, langCode, cache);
				if (labelMap != null) {
					holdAppt.setErrorMessage(labelMap.get("DUPLICATE_APPT"));
				}
			} else if (errorMsg != null && "SELECTED_DATE_TIME_NOT_AVAILABLE".equalsIgnoreCase(errorMsg.toString())) {
				holdAppt.setErrorFlag("Y");
				if (langCode == null || "".equals(langCode)) {
					langCode = "us-en";
				}
				Map<String, String> labelMap = cacheComponent.getDisplayFieldLabelsMap(jdbcCustomTemplate, logger, device, langCode, cache);
				if (labelMap != null) {
					holdAppt.setErrorMessage(labelMap.get("SELECTED_DATE_TIME_NOT_AVAILABLE"));
				}
			} else if (errorMsg != null && "HOLD_NOT_RELEASED".equalsIgnoreCase(errorMsg.toString())) {
				holdAppt.setErrorFlag("Y");
				if (langCode == null || "".equals(langCode)) {
					langCode = "us-en";
				}
				Map<String, String> labelMap = cacheComponent.getDisplayFieldLabelsMap(jdbcCustomTemplate, logger, device, langCode, cache);
				if (labelMap != null) {
					holdAppt.setErrorMessage(labelMap.get("HOLD_NOT_RELEASED"));
				}
			} else if (errorMsg != null) {
				logger.error("Error from hold appointment call center stored procedure: " + errorMsg);
			}
		} else if (CoreUtils.isIVR(device)) {
			if (errorMsg != null) {
				holdAppt.setErrorFlag("Y");
				holdAppt.setPageName(errorMsg.toLowerCase());
			}
		}
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, holdAppt), HttpStatus.OK);
	}
	
	@Override
	public ResponseEntity<ResponseModel> getVerifyPageData(Logger logger, String clientCode, String device, String langCode,  Long customerId, Long scheduleId, String token) throws Exception {
		VerifyPageResponse verifyPageResponse = new VerifyPageResponse();
		Client client = cacheComponent.getClient(logger, clientCode, true);
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
		
		if(CoreUtils.isMobile(device) && !isTokenValid(jdbcCustomTemplate, clientCode, token)) {
			verifyPageResponse.setStatus(false);
			verifyPageResponse.setMessage(PropertyUtils.getValueFromProperties("INVALID_TOKEN_MESSAGE", PropertiesConstants.APPT_SERVICE_REST_WS_PROP.getPropertyFileName()));
			return new ResponseEntity<>(commonComponent.populateRMDData(logger, verifyPageResponse), HttpStatus.OK);
		}
		
		ApptSysConfig apptSysConfig = cacheComponent.getApptSysConfig(jdbcCustomTemplate, logger, cache);
		if("N".equals(apptSysConfig.getLoginFirst())) {
			if(customerId.longValue() <= 0) {
				throw new TelAppointException(ErrorConstants.ERROR_1087.getCode(), ErrorConstants.ERROR_1087.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, ErrorConstants.ERROR_1085.getMessage(),
						"getVerifyPage input params: customerId:"+customerId + ",ScheduleId:"+scheduleId);
			} 
		}
		if("N".equals(apptSysConfig.getLoginFirst())) {
			apptDAO.updateCustomerIdInSchedule(jdbcCustomTemplate, logger,customerId, scheduleId);
		}
		Map<String, String> aliasMap = cacheComponent.getDisplayAliasMap(jdbcCustomTemplate, logger, device, langCode, cache);
		verifyPageResponse.setVerifyPageData(apptDAO.getVerfiyPageData(jdbcCustomTemplate, logger, device, langCode, scheduleId, aliasMap));
		
		if(CoreUtils.isMobile(device)) {
			MobileAppPage mobileAppPage = apptDAO.getMobileAppPages(jdbcCustomTemplate, "verify_appt_page");
			verifyPageResponse.setPageData(getMobileAppPages(mobileAppPage));
		}
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, verifyPageResponse), HttpStatus.CREATED);
	}
	
	
	/**
	 * Used to confirm the appointment, It will do with two steps. 1. Confirm
	 * the appointment using book appointment stored procedure. 2. Send an email
	 * with Asynchronous call. if any error it will log it without throwing the
	 * exception.
	 *
	 * @throws TelAppointException
	 *             , Exception
	 * 
	 */
	@Override
	public ResponseEntity<ResponseModel> bookAppointment(Logger logger, ConfirmAppointmentRequest confirmApptReq) throws TelAppointException, Exception {	
		ConfirmAppointmentResponse confirmApptRes = new ConfirmAppointmentResponse();
		String device = confirmApptReq.getDevice();
		Client client = cacheComponent.getClient(logger, confirmApptReq.getClientCode(), true);
		boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		ClientDeploymentConfig cdConfig = cacheComponent.getClientDeploymentConfig(logger, confirmApptReq.getClientCode(),cache);
		
		if(!isTokenValid(jdbcCustomTemplate, confirmApptReq.getClientCode(), confirmApptReq.getToken())) {
			confirmApptRes.setStatus(false);
			confirmApptRes.setMessage(PropertyUtils.getValueFromProperties("INVALID_TOKEN_MESSAGE", PropertiesConstants.APPT_SERVICE_REST_WS_PROP.getPropertyFileName()));
			return new ResponseEntity<>(commonComponent.populateRMDData(logger, confirmApptRes), HttpStatus.OK);
		}
		
		Schedule schedule = apptDAO.getSchedule(jdbcCustomTemplate, logger, confirmApptReq.getScheduleId());
		if(schedule != null && schedule.getStatus() != AppointmentStatus.HOLD.getStatus() || schedule.getStatus() != AppointmentStatus.RELEASE_HOLD.getStatus()) {
			confirmApptRes.setStatus(false);
			confirmApptRes.setMessage("Appointment is not in Hold/Release state. Rejecting to book an appointment.");
			return new ResponseEntity<>(commonComponent.populateRMDData(logger, confirmApptRes), HttpStatus.OK);
		}
		
		ApptSysConfig apptSysConfig = cacheComponent.getApptSysConfig(jdbcCustomTemplate, logger, cache);
		if("N".equals(apptSysConfig.getLoginFirst())) {
			if(confirmApptReq.getCustomerId() <= 0) {
				throw new TelAppointException(ErrorConstants.ERROR_1085.getCode(), ErrorConstants.ERROR_1085.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, ErrorConstants.ERROR_1085.getMessage(),
						"confirmApptReq input params: " + confirmApptReq.toString());
			} 
		}
		confirmApptRes = bookppointment(jdbcCustomTemplate, logger, confirmApptReq, cdConfig);
		String errorMessage = confirmApptRes.getMessage();
		if(confirmApptRes.isStatus() && ("".equals(errorMessage) || errorMessage == null)) {
			if("N".equals(apptSysConfig.getLoginFirst())) {
				apptDAO.updateCustomerIdInSchedule(jdbcCustomTemplate, logger,confirmApptReq.getCustomerId(), confirmApptReq.getScheduleId());
			}
			
			if(CoreUtils.isMobile(device)) {
				Map<String, String> aliasMap = cacheComponent.getDisplayAliasMap(jdbcCustomTemplate, logger, device, confirmApptReq.getLangCode(), cache);
				VerifyPageData verifyPageData = apptDAO.getVerfiyPageData(jdbcCustomTemplate, logger, device, confirmApptReq.getLangCode(), confirmApptReq.getScheduleId(), aliasMap);
				String displayValues = confirmApptRes.getDisplayValues();
				String confirmNumber = (displayValues != null && !"".equals(displayValues))?displayValues.split("\\|")[0]:"";
			    confirmApptRes.setConfirmApptData(verifyPageData);
				verifyPageData.setConfNumber(confirmNumber);
				confirmApptRes.setDisplayKeys(null);
				confirmApptRes.setDisplayValues(null);
				MobileAppPage mobileAppPage = apptDAO.getMobileAppPages(jdbcCustomTemplate, "confirm_appt_page");
				confirmApptRes.setPageData(getMobileAppPages(mobileAppPage));
			} 
			
			Map<String, String> emailData = new HashMap<String, String>();
			String emailType = "confirm";
			sendConfirmEmail(logger, errorMessage, jdbcCustomTemplate, client, cdConfig, confirmApptReq, confirmApptRes, emailData, emailType);
		}
		
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, confirmApptRes), HttpStatus.CREATED);
	}
	
	
	/**
	 * Used to authenticate the customer, if not exist, it will create the new
	 * customer. loginParam data format: fieldId1~fieldValue| etc
	 *
	 * @throws TelAppointException
	 *             , Exception
	 */
	@Override
	public ResponseEntity<ResponseModel> loginAuthenticateAndUpdate(Logger logger, CustomerInfoRequest custInfoReq) throws TelAppointException, Exception {
		AuthResponse authResponse = new AuthResponse();
		String clientCode = custInfoReq.getClientCode();
		String device = custInfoReq.getDevice();
		String langCode = custInfoReq.getLangCode();
		Client client = cacheComponent.getClient(logger, clientCode, true);
		boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
		ClientDeploymentConfig cdConfig = cacheComponent.getClientDeploymentConfig(logger, clientCode, cache);
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);

		List<Integer> fieldIds = new ArrayList<Integer>();
		List<String> fieldValues = new ArrayList<String>();

		logger.info("authenticateAndUpdateCustomer API [ Input parameters] -  " + custInfoReq.getInputParamValues());
		parseInputParamValues(custInfoReq.getInputParamValues(), fieldIds, fieldValues);
		Map<String, String> labelMap = cacheComponent.getDisplayFieldLabelsMap(jdbcCustomTemplate, logger, device, langCode, cache);
		List<OnlinePageFields> onlinePageFields = null;
		List<IVRPageFields> ivrPageFields = null;
		List<LoginPageFields> mobilePageFields = null;
		int size=0;
		if (CoreUtils.isOnline(device)) {
			onlinePageFields = apptDAO.getOnlinePageFieldsByPageFieldIds(jdbcCustomTemplate, logger, fieldIds);
			size = onlinePageFields.size();
		} else if(CoreUtils.isIVR(device)){
			ivrPageFields = apptDAO.getIVRPageFieldsByPageFieldIds(jdbcCustomTemplate, logger, fieldIds);
			size = ivrPageFields.size();
		} else if(CoreUtils.isMobile(device)) {
			mobilePageFields = apptDAO.getMobilePageFieldsByPageFieldIds(jdbcCustomTemplate, logger, fieldIds);
			size = mobilePageFields.size();
		} else {
			//handle it
		}

		int inputArraySize = fieldIds.size();
		if (inputArraySize != size) {
			logger.error("Invalid input parameters passed from device: " + device);
			throw new TelAppointException(ErrorConstants.ERROR_2002.getCode(), ErrorConstants.ERROR_2002.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,
					"Invalid input parameters passed from device: " + device, custInfoReq.toString());
		}

		ApptSysConfig apptSysConfig = cacheComponent.getApptSysConfig(jdbcCustomTemplate, logger, cache);
		Class<OnlinePageFields> onlineClassType = OnlinePageFields.class;
		Class<IVRPageFields> ivrClassType = IVRPageFields.class;
		Class<LoginPageFields> mobileClassType = LoginPageFields.class;
		if (CoreUtils.isOnline(device)) {
			apptDAO.authenticateCustomer(jdbcCustomTemplate, logger, device, fieldIds, fieldValues, onlinePageFields, onlineClassType, authResponse, labelMap);
		} else if (CoreUtils.isIVR(device)) {
			apptDAO.authenticateCustomer(jdbcCustomTemplate, logger, device, fieldIds, fieldValues, ivrPageFields, ivrClassType, authResponse, labelMap);
		} else if (CoreUtils.isMobile(device)) {
			apptDAO.authenticateCustomer(jdbcCustomTemplate, logger, device, fieldIds, fieldValues, mobilePageFields, mobileClassType, authResponse, labelMap);
		}

		boolean noLogin = "N".equals(apptSysConfig.getEnforceLogin());
		if (CoreUtils.isAdmin(device)) {
			noLogin = true;
		}

		Customer customer;
		if (authResponse.isAuthSuccess() == false && noLogin) {
			customer = new Customer();
			if (CoreUtils.isOnline(device)) {
				populateCustomer(logger, device, customer, onlinePageFields, onlineClassType, fieldIds, fieldValues, "both");
			} else if (CoreUtils.isIVR(device)) {
				populateCustomer(logger, device, customer, ivrPageFields, ivrClassType, fieldIds, fieldValues, "both");
			} else if (CoreUtils.isMobile(device)) {
				populateCustomer(logger, device, customer, mobilePageFields, mobileClassType, fieldIds, fieldValues, "both");
			}

			if (customer.getFirstName() == null) {
				customer.setFirstName("");
			}
			if (customer.getLastName() == null) {
				customer.setLastName("");
			}
			long customerId = apptDAO.saveCustomer(jdbcCustomTemplate, logger, customer, cdConfig);
			if (CoreUtils.isIVR(device)) {
				customer.setCustomerId(customerId);
				customer.setFirstName("NEW");
				customer.setLastName("Customer-" + customerId);
				boolean isUpdated = apptDAO.updateCustomerForIVR(jdbcCustomTemplate, logger, customer, cdConfig);
				if (!isUpdated) {
					logger.error("IVR firstName and lastName not updated!");
				}
			}
			authResponse.setCustomerId(customerId);
			
			if("Y".equals(apptSysConfig.getCheckAssignedResource())) {
				authResponse.setResourceId(apptDAO.getResourceIdFromCustomer(jdbcCustomTemplate, customerId));
			}
		}

		if (authResponse.getCustomerId() <= 0) {
			authResponse.setAuthSuccess(false);
			authResponse.setAuthMessage((labelMap !=null && !"".equals(labelMap.get("CUSTOMER_NOT_FOUND")))?labelMap.get("CUSTOMER_NOT_FOUND"):ErrorConstants.ERROR_2996.getMessage());
		} else {
			customer = apptDAO.getCustomer(jdbcCustomTemplate, logger, authResponse.getCustomerId());
			StringBuilder sql = new StringBuilder("update customer set id=:customerId ");
			MapSqlParameterSource paramSource = new MapSqlParameterSource();
			if (CoreUtils.isOnline(device)) {
				populateCustomerQuery(logger, device, onlinePageFields, fieldValues, "update", sql, paramSource, authResponse.getCustomerId());
			} else if (CoreUtils.isIVR(device)) {
				populateCustomerQuery(logger, device, ivrPageFields, fieldValues, "update", sql, paramSource, authResponse.getCustomerId());
			} else if (CoreUtils.isMobile(device)) {
				populateCustomerQuery(logger, device, mobilePageFields, fieldValues, "update", sql, paramSource, authResponse.getCustomerId());
				masterDAO.updateCustomerIdInMyProviderList(clientCode, custInfoReq.getUuid(), authResponse.getCustomerId());
			}
			apptDAO.updateCustomer(jdbcCustomTemplate, logger, sql, paramSource, cdConfig);
			authResponse.setAuthSuccess(true);
		}
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, authResponse), HttpStatus.CREATED);
	}
	
	
	

	@Override
	public ResponseEntity<ResponseModel> getMyProvidersList(HttpHeaders httpHeader, Logger logger,String device, String uuid) throws Exception {
		ClientInfoResponse clientInfoRes = new ClientInfoResponse();
		List<ClientInfo> clientInfoList = masterDAO.getMyProvidersList(uuid);
		clientInfoRes.setClientList(clientInfoList);
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, clientInfoRes), HttpStatus.OK);
	}
	
	@Override
	public ResponseEntity<ResponseModel> getMyProvidesLocResSerList(HttpHeaders httpHeader, Logger logger,String device, String uuid) throws Exception {
		ProvidersLocResSerResponse providersLocResSer = new ProvidersLocResSerResponse();
		List<ProvidersLocResSer> providerList = masterDAO.getMyProvidersLocResSerList(uuid);
		if(!providerList.isEmpty()) {
			providersLocResSer.setProvidersLocResSer(providerList.get(0));
		} else {
			providersLocResSer.setProvidersLocResSer(new ProvidersLocResSer());
		}
		
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, providersLocResSer), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseModel> addMyProvidersList(HttpHeaders httpHeader, Logger logger, String clientCode, String device, String uuid) throws Exception {
		ProviderResponse providerRes = new ProviderResponse();
		boolean isExist = masterDAO.getProviderExist(clientCode, uuid);
		if(isExist) {
			providerRes.setErrorFlag("Y");
			providerRes.setErrorMessage("Provider already exist!");
			return new ResponseEntity<>(commonComponent.populateRMDData(logger, providerRes), HttpStatus.OK);
		}
		Long providerId = masterDAO.addMyProvidersList(clientCode, device, uuid);
		providerRes.setProviderId(providerId);
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, providerRes), HttpStatus.OK);
	}

	
	@Override
	public ResponseEntity<ResponseModel> deleteProvidersList(HttpHeaders httpHeader, Logger logger, String clientCode, String device, String uuid) throws Exception {
		ProviderResponse providerRes = new ProviderResponse();
		masterDAO.deleteMyProvidersList(clientCode, device, uuid);
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, providerRes), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseModel> updateLocResSer(Logger logger, String clientCode, String uuid, Long locationId, Long resourceId, Long serviceId) throws Exception {
		masterDAO.updateLocResSer(logger, clientCode, uuid, locationId, resourceId, serviceId);
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, new BaseResponse()), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseModel> getMyAccountDetails(Logger logger, String device, String uuid) throws Exception {
		PersonalInfoResponse personalInfoRes = new PersonalInfoResponse();
		List<ClientCustomerInfo> clientCustomerInfo = masterDAO.getClientInfoFromMyProviders(logger, uuid);
		List<PersonalInfo> personalInfoList = new ArrayList<>();
		
		for(ClientCustomerInfo clientCustomer : clientCustomerInfo) {
			String clientCode = clientCustomer.getClientCode();
			String clientName = clientCustomer.getClientName();
			Long customerId = clientCustomer.getCustomerId();
			if(customerId != null && customerId.intValue() > 0 ) {
				String langCode="us-en";
				Client client = cacheComponent.getClient(logger, clientCode, true);
				JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
				PersonalInfo personalInfo = getPersonalInfo(logger, jdbcCustomTemplate, device, langCode);
				CustomerInfo customerInfo = new CustomerInfo();
				apptDAO.getCustomerInfo(jdbcCustomTemplate, logger, device, customerId, customerInfo);
				personalInfo.setClientCode(clientCode);
				personalInfo.setClientName(clientName);
				personalInfo.setCustomer(customerInfo.getCustomer());
				personalInfoList.add(personalInfo);
				
			}
		}
		personalInfoRes.setPersonalInfoList(personalInfoList);
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, personalInfoRes), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseModel> getPersonalInfo(Logger logger, String clientCode, String device, String langCode, Long customerId) throws Exception {
		PersonalInfoResponse personalInfoRes = new PersonalInfoResponse();
		Client client = cacheComponent.getClient(logger, clientCode, true);
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
		PersonalInfo personalInfo = getPersonalInfo(logger, jdbcCustomTemplate, device, langCode);
		CustomerInfo customerInfo = new CustomerInfo();
		apptDAO.getCustomerInfo(jdbcCustomTemplate, logger, device, customerId, customerInfo);
		personalInfoRes.setPersonalInfo(personalInfo);
		personalInfo.setCustomer(customerInfo.getCustomer());
		return new ResponseEntity<>(commonComponent.populateRMDData(logger, personalInfoRes), HttpStatus.OK);
	}
	
	private PersonalInfo getPersonalInfo(Logger logger,JdbcCustomTemplate jdbcCustomTemplate, String device, String langCode) throws Exception {
		PersonalInfo personalInfo = new PersonalInfo();
		List<LoginPageFields> mobilePageFields = new ArrayList<>();
		Map<String, String> labelMap = cacheComponent.getDisplayFieldLabelsMap(jdbcCustomTemplate, logger, device, langCode, true);
		apptDAO.getMobilePageFields(logger, jdbcCustomTemplate, langCode, mobilePageFields, labelMap, "all");
	
		personalInfo.setMobilePageFields(mobilePageFields);
		MobileAppPage mobileAppPage = apptDAO.getMobileAppPages(jdbcCustomTemplate, "customer_login_page");
		personalInfo.setPageData(getMobileAppPages(mobileAppPage));
		return personalInfo;
	}

	@Override
	public ResponseEntity<ResponseModel> getExistingAppointments(Logger logger, String clientCode, String device, String langCode, Long customerId, String uuid) throws Exception {
		AppointmentsDataResponse apptDataResponse = new AppointmentsDataResponse();
		if(clientCode == null || customerId == null || customerId.longValue() == 0) {
			List<MobileMenuExistingAppt> mobileMainMenuExistAppts = new ArrayList<>();
			MobileMenuExistingAppt mobileMenuExitingAppts= null;
			List<ClientCustomerInfo> clientCustomerInfo = masterDAO.getClientInfoFromMyProviders(logger, uuid);
			JdbcCustomTemplate jdbcCustomTemplate = null;
			boolean cache = false;
			for(ClientCustomerInfo clientCustomer : clientCustomerInfo) {
				clientCode = clientCustomer.getClientCode();
				customerId = clientCustomer.getCustomerId();
				if(clientCode != null && customerId != null && customerId.longValue() > 0) {
					mobileMenuExitingAppts = new  MobileMenuExistingAppt();
					Client client = cacheComponent.getClient(logger, clientCode, true);
					jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(logger, client);
					cache = "Y".equals(client.getCacheEnabled()) ? true : false;
					mobileMenuExitingAppts.setClientName(client.getClientName());
					mobileMenuExitingAppts.setApptList(getBookedAppts(logger, jdbcCustomTemplate, cache, device, langCode, customerId));
					mobileMainMenuExistAppts.add(mobileMenuExitingAppts);
				}
			}
			apptDataResponse.setMainMenuApptDetails(mobileMainMenuExistAppts);
			if (apptDataResponse.getMainMenuApptDetails().isEmpty()) {
				Map<String, String> labelMap = cacheComponent.getDisplayFieldLabelsMap(jdbcCustomTemplate, logger, device, langCode, cache);
				if (labelMap != null) {
					if(CoreUtils.isOnline(device) || CoreUtils.isMobile(device)) {
						apptDataResponse.setMessage(labelMap.get(DisplayFieldLabelConstants.NO_BOOKED_APPTS.getValue()) == null ? ErrorConstants.ERROR_2996.getMessage() : labelMap
							.get(DisplayFieldLabelConstants.NO_BOOKED_APPTS.getValue()));
					}
				}
			} 
			if(CoreUtils.isMobile(device)) {
				MobileAppPage mobileAppPage = apptDAO.getMobileAppPages(jdbcCustomTemplate, "existing_appt_page");
				apptDataResponse.setPageData(getMobileAppPages(mobileAppPage));
			}
			return new ResponseEntity<>(commonComponent.populateRMDData(logger, apptDataResponse), HttpStatus.OK);
			
		} else {
			return getBookedAppointments(logger, clientCode, device, langCode, customerId);
		}
	}
}
