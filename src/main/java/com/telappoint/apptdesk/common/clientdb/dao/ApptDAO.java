package com.telappoint.apptdesk.common.clientdb.dao;

import java.util.List;
import java.util.Map;

import com.telappoint.apptdesk.common.model.*;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import com.telappoint.apptdesk.handlers.exception.TelAppointException;
import com.telappoint.apptdesk.model.AppointmentDetails;
import com.telappoint.apptdesk.model.ApptSysConfig;
import com.telappoint.apptdesk.model.CancelAppointResponse;
import com.telappoint.apptdesk.model.Company;
import com.telappoint.apptdesk.model.ConfirmAppointmentResponse;
import com.telappoint.apptdesk.model.Customer;
import com.telappoint.apptdesk.model.CustomerInfo;
import com.telappoint.apptdesk.model.CustomerPledge;
import com.telappoint.apptdesk.model.CustomerPledgeData;
import com.telappoint.apptdesk.model.CustomerPledgeResponse;
import com.telappoint.apptdesk.model.DuplicateApptResponse;
import com.telappoint.apptdesk.model.FirstAvailableDateAnyLocationResponse;
import com.telappoint.apptdesk.model.HoldAppointmentRequest;
import com.telappoint.apptdesk.model.HoldAppointmentResponse;
import com.telappoint.apptdesk.model.HouseHoldMonthlyIncomeResponse;
import com.telappoint.apptdesk.model.IVRCallRequest;
import com.telappoint.apptdesk.model.IVRCallResponse;
import com.telappoint.apptdesk.model.ListOfDocs;
import com.telappoint.apptdesk.model.ListOfThingsResponse;
import com.telappoint.apptdesk.model.LocationPriSecAvailable;
import com.telappoint.apptdesk.model.LoginPageFields;
import com.telappoint.apptdesk.model.NameRecordInfo;
import com.telappoint.apptdesk.model.PendingEnrollment;
import com.telappoint.apptdesk.model.Schedule;
import com.telappoint.apptdesk.model.Service;
import com.telappoint.apptdesk.model.ServiceFundsResponse;
import com.telappoint.apptdesk.model.IVRFlow;
import com.telappoint.apptdesk.model.IVRPageFields;
import com.telappoint.apptdesk.model.IVRXml;
import com.telappoint.apptdesk.model.Language;
import com.telappoint.apptdesk.model.Location;
import com.telappoint.apptdesk.model.OnlineFlow;
import com.telappoint.apptdesk.model.OnlinePageContent;
import com.telappoint.apptdesk.model.OnlinePageFields;
import com.telappoint.apptdesk.model.Options;
import com.telappoint.apptdesk.model.Procedure;
import com.telappoint.apptdesk.model.ServiceTimeSlotsAvailableStatus;
import com.telappoint.apptdesk.model.TransScript;
import com.telappoint.apptdesk.model.TransScriptEmailData;
import com.telappoint.apptdesk.model.TransScriptRequest;

/**
 * Interface for DAO layer.
 * 
 * @author Balaji
 *
 */
public interface ApptDAO {

	public void getI18nAliasesMap(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, final Map<String, Map<String, String>> map) throws TelAppointException, Exception;

	public void getI18nDesignTemplatesMap(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, final Map<String, Map<String, String>> map) throws TelAppointException, Exception;

	public void getI18nButtonsMap(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, final Map<String, Map<String, String>> map) throws TelAppointException, Exception;

	public void getI18nDisplayFieldLabelsMap(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, final Map<String, Map<String, String>> map) throws TelAppointException, Exception;

	public void getI18nPageContentMap(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, final Map<String, Map<String, String>> map) throws TelAppointException, Exception;

	public void getI18nEmailTemplateMap(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, final Map<String, Map<String, String>> map) throws TelAppointException, Exception;

	public List<Language> getLangDetails(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, final List<Language> languageList) throws TelAppointException, Exception;

	public Language getDefaultLangCode(JdbcCustomTemplate jdbcCustomTemplate, Logger logger) throws TelAppointException, Exception;

	public void populateAlias(Map<String, String> aliasMap, List<Options> options, String fieldName) throws TelAppointException, Exception;

	public void loadVXML(final JdbcCustomTemplate jdbcCustomTemplate, final String mainKey, final Map<String, Map<String, IVRXml>> cacheMap) throws TelAppointException, Exception;

	public ApptSysConfig getApptSysConfig(JdbcCustomTemplate jdbcCustomTemplate, Logger logger) throws TelAppointException, Exception;

	public void getOnlineFlow(Logger logger, JdbcCustomTemplate jdbcCustomTemplate, final Map<String, OnlineFlow> onlineFlowMap) throws TelAppointException, Exception;

	public void getOnlinePageContent(Logger logger, JdbcCustomTemplate jdbcCustomTemplate, String langCode, final Map<String, List<OnlinePageContent>> onlinePageContentMap,
			Map<String, List<String>> onlinePageContentMapIds) throws TelAppointException, Exception;

	public void getOnlinePageFields(Logger logger, JdbcCustomTemplate jdbcCustomTemplate, String langCode, Map<String, List<OnlinePageFields>> pageFieldMap)
			throws TelAppointException, Exception;

	public void getIVRFlow(Logger logger, JdbcCustomTemplate jdbcCustomTemplate, List<IVRFlow> ivrFlows) throws TelAppointException, Exception;

	public List<Options> getProcedure(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, final String device, final List<Options> procedureOptions,
			Map<String, String> aliasMap, Map<String, String> labelMap) throws TelAppointException, Exception;

	public Procedure getProcedureByName(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String procedureName, String device) throws TelAppointException, Exception;

	public void getProcedureNoMatchMap(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, Map<String, Map<String, String>> subMap) throws TelAppointException, Exception;

	public void getLocationIdList(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String procedureId, List<Integer> locationIds) throws TelAppointException, Exception;

	public Location getLocationById(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String device, Integer locationId) throws TelAppointException, Exception;

	public List<OnlinePageFields> getOnlinePageFieldsByPageFieldIds(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, List<Integer> fieldIds) throws TelAppointException, Exception;

	public <T> void authenticateCustomer(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String device, List<Integer> fieldIds, List<String> fieldValues,
			final List<T> fieldList, final Class<T> classType, final Object response, Map<String, String> labelMap) throws TelAppointException, Exception;

	public long saveCustomer(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, Customer customer, ClientDeploymentConfig cdConfig) throws TelAppointException, Exception;

	public Customer getCustomer(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, Long customerId) throws TelAppointException, Exception;

	public boolean updateCustomer(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, StringBuilder sql, MapSqlParameterSource paramSource, ClientDeploymentConfig cdConfig)
			throws TelAppointException, Exception;

	public boolean updateCustomerForIVR(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, Customer customer, ClientDeploymentConfig cdConfig) throws TelAppointException, Exception;

	public List<IVRPageFields> getIVRPageFieldsByPageFieldIds(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, List<Integer> fieldIds) throws TelAppointException, Exception;

	public void getCustomerType(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String device, Long customerId, Integer locationId, ServiceFundsResponse customerTypeResponse,
			Map<String, String> aliasMap, Map<String, String> labelMap) throws TelAppointException, Exception;

	public void getUtilityType(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String device, Integer serviceFundsRcvdId, Integer customerTypeId, Integer locationId,
			ServiceFundsResponse serviceFundsResponse, Map<String, String> aliasMap, Map<String, String> labelMap) throws TelAppointException, Exception;

	public void getServiceNameCustomerUtility(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String device, Integer serviceFundsRcvdId, Integer customerTypeId, Integer locationId,
			Integer utilityTypeId, ServiceFundsResponse serviceFundsResponse, Map<String, String> aliasMap, Map<String, String> labelMap) throws TelAppointException, Exception;

	public void getServiceNameCustomerWarningPage(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String device, Integer serviceFundsRcvdId, Integer customerTypeId,
			Integer utilityTypeId, Integer serviceId, ServiceFundsResponse serviceFundsResponse, Map<String, String> aliasMap) throws TelAppointException, Exception;

	public void getServices(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, Map<String, Service> serviceMap) throws TelAppointException, Exception;

	public void getListOfThingsToBring(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String device, String langCode, Integer serviceId,
			final ListOfThingsResponse listOfThingsResponse) throws TelAppointException, Exception;

	public void getHouseholdMonthlyIncome(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String device, String noOfPeople, Integer serviceId,
			HouseHoldMonthlyIncomeResponse houseHoldMonthlyIncome) throws TelAppointException, Exception;

	public void holdFirstAvailableAppointment(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, HoldAppointmentRequest holdAppointmentRequest, String dateTime,
			TempHoldAppt tempHoldAppt, ClientDeploymentConfig cdConfig, HoldAppointmentResponse holdAppointmentResponse) throws TelAppointException, Exception;

	public FirstAvailableDateTime getApptFirstAvailDateTimeCallCenter(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, HoldAppointmentRequest holdAppt,
			ClientDeploymentConfig cdConfig) throws TelAppointException, Exception;

	public TempHoldAppt holdAppt(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, Integer resourceId, String dateTime, Integer serviceId, Long customerId,
			ClientDeploymentConfig cdConfig) throws TelAppointException, Exception;

	public void getIVRPageFields(Logger logger, JdbcCustomTemplate jdbcCustomTemplate, String langCode, List<IVRPageFields> ivrPageFields) throws TelAppointException, Exception;

	public void bookAppointment(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, Long scheduleId, String device, String langCode, Integer apptMethod,
			ClientDeploymentConfig cdConfig, ConfirmAppointmentResponse confirmAppointmentResponse) throws TelAppointException, Exception;

	public void getBookedAppointments(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String device, Long customerId, ClientDeploymentConfig cdConfig,
			String onlineDateTimeFormat, List<AppointmentDetails> apptList, Map<String, String> aliasMap) throws TelAppointException, Exception;

	public void updateNameRecord(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, NameRecordInfo nameRecordInfo) throws TelAppointException, Exception;

	public void getCustomerInfo(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String device, Long customerId, CustomerInfo customerInfo) throws TelAppointException, Exception;

	public void cancelAppointment(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, Long scheduleId, Integer cancelMethod, String langCode, ClientDeploymentConfig cdConfig,
			CancelAppointResponse cancelAppointResponse) throws TelAppointException, Exception;

	public void getLocationAvailability(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String timeZone, Integer locationId, ClientDeploymentConfig cdConfig,
			LocationPriSecAvailable locPriSecAvail) throws TelAppointException, Exception;

	public void getServiceAvailability(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, Integer locationId, Integer serviceId, ClientDeploymentConfig cdConfig,
			ServiceTimeSlotsAvailableStatus serviceAvail) throws TelAppointException, Exception;

	public Long getTransId(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, final String device, final ClientDeploymentConfig cdConfig, final String uuid,
			final String ipAddress, String callerId, String userName) throws TelAppointException, Exception;

	public void updateIVRCallLog(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, final ClientDeploymentConfig cdConfig, final IVRCallRequest ivrCallRequest,
			IVRCallResponse ivrCallResponse) throws TelAppointException, Exception;

	public void updateTransaction(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String device, Long transId, Integer pageId, Long scheduleId) throws TelAppointException, Exception;

	public BaseResponse releaseHoldAppointment(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String device, Long scheduleId) throws TelAppointException, Exception;
	
	public void getIVRVxml(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, Map<String, IVRXml> ivrxmlMap) throws TelAppointException, Exception;
	
	public void extendHoldTime(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String device, Long scheduleId) throws TelAppointException, Exception;

    public AvailableDateTimes getAvailableDatesCallcenter(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String timeZone,
												   Long locationId, Long departmentId, Long serviceId, Long blockTimeMins) throws Exception;

	public AvailableDateTimes getAvailableTimesCallcenter(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, Long locationId,
												   Long departmentId, Long serviceId, String availDate, Long blockTimeMins) throws Exception;

	public HoldAppt holdAppointmentCallCenter(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String device, Long locationId, Long procedureId,
									   Long departmentId, Long serviceId, Long customerId, String apptDateTime, ClientDeploymentConfig cdConfig, Long transId) throws Exception;

	public void getPledgeHistory(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String device, String langCode, Long customerId, CustomerPledgeResponse pledgeRes) throws TelAppointException;
	
	public CustomerPledge getCustomerPledgeById(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String device, String langCode, Long customerPledgeId) throws TelAppointException;

	public void checkDuplicateAppts(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, Long serviceId, Long customerId, DuplicateApptResponse duplicateApptResponse) throws TelAppointException;

	public List<Location> getLocations(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String device, String langCode) throws TelAppointException;

	public List<com.telappoint.apptdesk.model.Service> getServicesCallcenter(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String device, String langCode, Integer locationId, Integer departmentId) throws TelAppointException;

	public Long getProgramInstanceId(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, int locationId, int serviceId) throws TelAppointException;

	public Customer findByParticipantId(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, long participantID) throws TelAppointException;

	public boolean updateEnrollemntId(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String enrollmentId, long scheduleId) throws TelAppointException;
	
	public Schedule getSchedule(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, Long scheduleId) throws TelAppointException;

	public boolean saveTransScriptMsg(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, TransScriptRequest transScriptReq) throws TelAppointException;

	public List<PendingEnrollment> getPendingEnrollments(JdbcCustomTemplate jdbcCustomTemplate, Logger logger) throws TelAppointException;

	public String getEnrollementId(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, long scheduleId) throws TelAppointException;

	public void updateEnrollmentProcessStatus(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String apiName, long scheduleId) throws TelAppointException;
	
	public void savePendingCreateOrCancelEnrollments(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, PendingEnrollment pendingEnrollment) throws TelAppointException;

	public void deleteTransScriptMsg(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, long transScriptMsgId) throws TelAppointException;

	public void updateTransId(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, Long scheduleId, Long transId) throws Exception;

    public IvrLang getLang(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String langCode) throws Exception;
    
    public List<TransScript> getTransScriptMsgs(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, long scheduleId);

    public TransScriptEmailData getFileUploadConfirmEmaildata(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String langCode, long scheduleId) throws Exception;
    
    public void getApptFirstAvailDateTimeAnyLocation(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String device, ClientDeploymentConfig cdConfig, FirstAvailableDateAnyLocationResponse firstAvailDateAnyLoc) throws TelAppointException, Exception;

    public List<ServiceOption> getServiceListByLocationId(JdbcCustomTemplate jdbcCustomTemplate, String device, Integer locationId) throws Exception;

	public boolean updateParticipantId(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, Long participantId, Long customerId) throws Exception;
	
	public Map<String, CustomerPledgeData> getPledgeHistoryWithTemplate(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String device, String langCode, Long customerId) throws Exception;

	public String insertTokenAndGet(JdbcCustomTemplate jdbcCustomTemplate, String clientCode,int expiryInMins) throws Exception;

	public List<Company> getCompanyList(JdbcCustomTemplate jdbcCustomTemplate, Logger logger) throws Exception;

	public List<Procedure> getProcedureList(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String companyId) throws Exception;

	public List<Location> getLocationList(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String procedureId) throws Exception;

	public List<Department> getDepartmentList(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String locationId) throws Exception;

	public List<Resource> getResourceList(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String locationId, String departmentId) throws Exception;
	
	public MobileAppPage getMobileAppPages(JdbcCustomTemplate jdbcCustomerTemplate, String pageName) throws Exception;
	
	public List<Location> getLocationList(JdbcCustomTemplate jdbcCustomTemplate, boolean isActiveList) throws Exception;

	List<ServiceOption> getServiceList(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String resourceId) throws Exception;

	public List<String> getMessages(JdbcCustomTemplate jdbcCustomTemplate) throws Exception;

	boolean isTokenValid(JdbcCustomTemplate jdbcCustomTemplate, String clientCode, String token) throws Exception;
	
	public AvailableDateTimes getAvailableDates(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String timeZone,
			   Long locationId, Long departmentId, Long resourceId, Long serviceId, Long blockTimeMins) throws Exception;

	public AvailableDateTimes getAvailableTimes(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, Long locationId, Long departmentId, Long resourceId, Long serviceId,
			String availDate, Long blockTimeInMins) throws Exception;

	List<LoginPageFields> getMobilePageFieldsByPageFieldIds(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, List<Integer> fieldIds) throws Exception;

	void getMobilePageFields(Logger logger, JdbcCustomTemplate jdbcCustomTemplate, String langCode, List<LoginPageFields> mobilePageFields,Map<String, String> contentMap, String loginType) throws Exception;

	HoldAppt holdAppointment(Logger logger, JdbcCustomTemplate jdbcCustomTemplate, String device, Long locationId, Long resourceId, Long procedureId, Long departmentId,
			Long serviceId, Long customerId, String apptDateTime, ClientDeploymentConfig cdConfig, Long transId) throws Exception;

	public boolean updateCustomerIdInSchedule(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, Long customerId, Long scheduleId) throws Exception;

	VerifyPageData getVerfiyPageData(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String device, String langCode, Long scheduleId, Map<String, String> aliasMap) throws Exception;

	Long getProgramId(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, int locationId, int serviceId) throws TelAppointException;

	ListOfDocs getListOfDocsToBring(JdbcCustomTemplate jdbcCustomTemplate, Integer serviceId) throws Exception;

	List<Service> getServicesNonCallcenter(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String device, String langCode, Integer locationId, Integer resourceId,
			Integer departmentId) throws TelAppointException;

	public Long getHouseHoldValue(JdbcCustomTemplate jdbcCustomTemplate);

	List<String> getHolidaysMap(JdbcCustomTemplate jdbcCustomTemplate, String timeZone) throws Exception;

	List<String> getClosedDaysMap(JdbcCustomTemplate jdbcCustomTemplate, Long locationId, String timeZone) throws Exception;

	boolean isAppointmentsExist(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, Long customerId) throws Exception;

	public Integer getResourceIdFromCustomer(JdbcCustomTemplate jdbcCustomTemplate, long customerId) throws Exception;

}
