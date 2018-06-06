package com.telappoint.apptdesk.service;

import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import com.telappoint.apptdesk.common.model.ExternalLoginRequest;
import com.telappoint.apptdesk.common.model.ResponseModel;
import com.telappoint.apptdesk.handlers.exception.TelAppointException;
import com.telappoint.apptdesk.model.ConfirmAppointmentRequest;
import com.telappoint.apptdesk.model.CustomerInfoRequest;
import com.telappoint.apptdesk.model.HoldAppointmentRequest;
import com.telappoint.apptdesk.model.IVRCallRequest;
import com.telappoint.apptdesk.model.NameRecordInfo;
import com.telappoint.apptdesk.model.TransScriptRequest;
import com.telappoint.apptdesk.model.UpdateCustomerInfoRequest;

/**
 * @author Balaji N
 */
public interface ApptService {
    public Logger getLogger(String clientCode, String device) throws TelAppointException;
    
    public boolean isClientAllowed(Logger logger, String clientCode) throws Exception;

    public ResponseEntity<ResponseModel> handleException(Logger logger, String clientCode, Exception tae);

    public ResponseModel populateRMDSuccessData(Logger logger, Object data);

    public Logger changeLogLevel(String clientCode, String logLevel) throws TelAppointException;

    public ResponseEntity<ResponseModel> getOnlineLandingPage(Logger logger, String clientCode, String langCode, String device) throws TelAppointException, Exception;

    public ResponseEntity<ResponseModel> getOnlinePageData(Logger logger, String clientCode, String langCode, String device) throws TelAppointException, Exception;

    public ResponseEntity<ResponseModel> getIVRFlow(Logger logger, String clientCode, String device, String langCode, String anis, String dnis) throws TelAppointException, Exception;

    public ResponseEntity<ResponseModel> getVXML(Logger logger, String clientCode, String device, String langCode, String page) throws TelAppointException, Exception;

    public ResponseEntity<ResponseModel> getProcedure(Logger logger, String clientCode, String device, String langCode) throws TelAppointException, Exception;

    public ResponseEntity<ResponseModel> getProcedureId(Logger logger, String clientCode, String device, String procedureName) throws TelAppointException, Exception;

    public ResponseEntity<ResponseModel> getProcedureNoMatch(Logger logger, String clientCode, String device, String langCode, String procedureValue) throws TelAppointException, Exception;

    public ResponseEntity<ResponseModel> getLocationPrimarySecondaryAvailability(Logger logger, String clientCode, String device, String langCode, String procedureId) throws TelAppointException, Exception;

    public ResponseEntity<ResponseModel> loginAuthenticate(Logger logger, CustomerInfoRequest customerInfoRequest) throws TelAppointException, Exception;

    public ResponseEntity<ResponseModel> authenticateForCancel(Logger logger, String clientCode, String langCode, String device, String inputParamValues) throws TelAppointException, Exception;

    public ResponseEntity<ResponseModel> getCustomerType(Logger logger, String clientCode, String device, String langCode, Long customerId, Integer locationId) throws TelAppointException, Exception;

    public ResponseEntity<ResponseModel> getUtilityType(Logger logger, String clientCode, String device, String langCode, Integer serviceFundsRcvdId, Integer customerTypeId, Integer locationId) throws TelAppointException, Exception;

    public ResponseEntity<ResponseModel> getServiceNameCustomerUtility(Logger logger, String clientCode, String device, String langCode, Integer serviceFundsRcvdId, Integer customerTypeId, Integer locationId,
                                                                       Integer utilityTypeId) throws TelAppointException, Exception;

    public ResponseEntity<ResponseModel> getServiceNameCustomerWarningPage(Logger logger, String clientCode, String device, String langCode, Integer serviceFundsRcvdId, Integer customerTypeId,
                                                                           Integer utilityTypeId, Integer serviceId) throws TelAppointException, Exception;

    public ResponseEntity<ResponseModel> getSingleServiceNotClosed(Logger logger, String clientCode, String device, String langCode, String serviceIds) throws TelAppointException, Exception;

    public ResponseEntity<ResponseModel> getServiceClosedStatus(Logger logger, String clientCode, String device, String langCode, Integer serviceId, Integer locationId) throws TelAppointException, Exception;

    public ResponseEntity<ResponseModel> getServiceTimeSlotsAvailableStatus(Logger logger, String clientCode, String device, String langCode, Integer locationId, Integer serviceId) throws TelAppointException, Exception;

    public ResponseEntity<ResponseModel> holdFirstAvailableAppointment(Logger logger, HoldAppointmentRequest holdAppointmentRequest) throws TelAppointException, Exception;

    public ResponseEntity<ResponseModel> getHouseholdMonthlyIncome(Logger logger, String clientCode, String device, String langCode, String noOfPeople, Integer serviceId) throws TelAppointException, Exception;

    public ResponseEntity<ResponseModel> confirmAppointment(Logger logger, ConfirmAppointmentRequest confirmAppointmentRequest) throws TelAppointException, Exception;

    public ResponseEntity<ResponseModel> getListOfThingsToBring(Logger logger, String clientCode, String device, String langCode, Integer serviceId) throws TelAppointException, Exception;

    public ResponseEntity<ResponseModel> updateCustomerInfo(Logger logger, UpdateCustomerInfoRequest customerInfoRequest) throws TelAppointException, Exception;

    public ResponseEntity<ResponseModel> getIVRPageFields(Logger logger, String clientCode, String langCode, String device) throws TelAppointException, Exception;

    public ResponseEntity<ResponseModel> getBookedAppointments(Logger logger, String clientCode, String device, String langCode, Long customerId) throws TelAppointException, Exception;

    public ResponseEntity<ResponseModel> updateNameRecord(Logger logger, NameRecordInfo nameRecordInfo) throws TelAppointException, Exception;

    public ResponseEntity<ResponseModel> getCustomerInfo(Logger logger, String clientCode, String device, Long customerId) throws TelAppointException, Exception;

    public ResponseEntity<ResponseModel> cancelAppointment(Logger logger, String clientCode, String device, String langCode, Long scheduleId, Long transId, String token) throws TelAppointException, Exception;

    public ResponseEntity<ResponseModel> getTransId(String clientCode, Logger logger, String device, String uuid, String ipAddress, String callerId, String userName) throws TelAppointException, Exception;

    public ResponseEntity<ResponseModel> updateTransaction(String clientCode, Logger logger, String device, Long transId, Integer pageId, Long scheduleId) throws TelAppointException, Exception;

    public ResponseEntity<ResponseModel> updateIVRCalls(Logger logger, IVRCallRequest ivrCallRequest) throws TelAppointException, Exception;

    public ResponseEntity<ResponseModel> getPageValidationMessages(Logger logger, String device) throws TelAppointException, Exception;

    public ResponseEntity<ResponseModel> releaseHoldAppointment(Logger logger, String clientCode, String device, Long scheduleId) throws TelAppointException, Exception;

    public ResponseEntity<ResponseModel> getIVRVxml(Logger logger, String clientCode, String device) throws TelAppointException, Exception;

    public ResponseEntity<ResponseModel> extendHoldTime(String clientCode, Logger logger, String device, Long scheduleId) throws TelAppointException, Exception;

    ResponseEntity<ResponseModel> getAvailableDatesCallcenter(String clientCode, Long locationId, Long departmentId, Long serviceId, Logger logger, String device, String langCode, Long transId) throws Exception;

    ResponseEntity<ResponseModel> getAvailableTimesCallcenter(String clientCode, Long locationId, Long departmentId, Long serviceId, String availDate, Logger logger, String device, String langCode, Long transId) throws Exception;

    ResponseEntity<ResponseModel> holdAppointmentCallCenter(String clientCode, Long locationId, Long procedureId, Long departmentId, Long serviceId, Long customerId, String apptDateTime, Logger logger, 
    		String device, String langCode, Long transId) throws Exception;
	
	public ResponseEntity<ResponseModel> getPledgeHistory(String clientCode, Logger logger, String device, String langCode, Long customerId, Long transId) throws Exception;

	public ResponseEntity<ResponseModel> getPledgeAwardLetter(String clientCode, Logger logger, String device, String langCode, Long transId, Long customerPledgeId) throws Exception;

	public ResponseEntity<ResponseModel> checkDuplicateAppts(Logger logger, String device, String langCode, String clientCode, Long serviceId, Long customerId, Long transId) throws Exception;

	public ResponseEntity<ResponseModel> getClientDirectAccessNumbers(Logger logger) throws Exception;

	public ResponseEntity<ResponseModel> getLocations(String clientCode, Logger logger, String device, String langCode, Long transId) throws Exception;

	public ResponseEntity<ResponseModel> getServicesCallcenter(String clientCode, Logger logger, String device, String langCode, Integer locationId, Integer departmentId, Long transId) throws Exception;

	public ResponseEntity<ResponseModel> loginAuthenticateAndUpdateForExternal(Logger logger, ExternalLoginRequest externalReq) throws Exception;

	public ResponseEntity<ResponseModel> confirmAppointmentExternalLogic(Logger logger, ConfirmAppointmentRequest confirmAppointmentRequest) throws Exception;
	
	public ResponseEntity<ResponseModel> authenticateAndUpdateCustomer(Logger logger, CustomerInfoRequest custInfoReq) throws TelAppointException, Exception;

	public ResponseEntity<ResponseModel> cancelAppointmentExternalLogic(Logger logger, String clientCode, String device, String langCode, Long scheduleId) throws Exception;

	public ResponseEntity<ResponseModel> saveTransScriptMsg(Logger logger, TransScriptRequest transScriptReq) throws Exception;

	public ResponseEntity<ResponseModel> processPendingCreateAndCancelEnrollments(Logger logger, String clientCode) throws Exception;

	public ResponseEntity<ResponseModel> loginAuthenticateForExternal(Logger logger, CustomerInfoRequest customerInfo) throws Exception;

	public ResponseEntity<ResponseModel> deleteTransScriptMsg(Logger logger, String clientCode, long transScriptMsgId) throws Exception;

    public ResponseEntity<ResponseModel> getLang(Logger logger, String clientCode, String device, String langCode) throws Exception;
    
    public ResponseEntity<ResponseModel> confirmAppointmentForFileUpload(Logger logger, ConfirmAppointmentRequest confirmApptReq) throws Exception;

	public ResponseEntity<ResponseModel> getFirstAvailableDateAnyLocation(Logger logger, String clientCode, String device, String langCode) throws Exception;

	public ResponseEntity<ResponseModel> getServiceListByLocationId(String clientCode, Logger logger, String device, String langCode, Long transId, Integer locationId) throws Exception;
	
	public ResponseEntity<ResponseModel> getPledgeHistoryWithTemplate(String clientCode, Logger logger, String device, String langCode, Long customerId, Long transId) throws Exception;

	void sendEmailInvalidPassword(String clientCode, HttpHeaders httpHeader) throws Exception;

	ResponseEntity<ResponseModel> getMobileDemoClientList(HttpHeaders httpHeader, Logger logger) throws Exception;

	ResponseEntity<ResponseModel> getClientListBySearchKey(HttpHeaders httpHeader, Logger logger, String uuid, String searchKey) throws Exception;

	ResponseEntity<ResponseModel> getClientListByClientCode(HttpHeaders httpHeader, Logger logger, String uuid, String clientCode) throws Exception;

	ResponseEntity<ResponseModel> getLanguageList(HttpHeaders httpHeader, Logger logger, String uuid, String clientCode) throws Exception;

	public ResponseEntity<ResponseModel> getClientInfo(Logger logger, String device, String clientCode, String langCode) throws Exception;

	public ResponseEntity<ResponseModel> getLocResSerInfo(Logger logger, String clientCode, String device, String langCode, String token) throws Exception;

	
	public ResponseEntity<ResponseModel> getLocationList(Logger logger, String clientCode, String device, String langCode, String procedureId, String token) throws Exception;

	public ResponseEntity<ResponseModel> getCompanyList(Logger logger, String clientCode, String device, String langCode,String token) throws Exception;

	public ResponseEntity<ResponseModel> getProcedureList(Logger logger, String clientCode, String device, String langCode, String companyId, String token) throws Exception;

	public ResponseEntity<ResponseModel> getDepartmentList(Logger logger, String clientCode, String device, String langCode, String locationId, String token) throws Exception;

	ResponseEntity<ResponseModel> getResourceList(Logger logger, String clientCode, String device, String langCode, String locationId, String departmentId, String token) throws Exception;

	public ResponseEntity<ResponseModel> getMobileHomePageInfo(Logger logger, String langCode) throws Exception;

	public ResponseEntity<ResponseModel> getMobileHomePageInitial(Logger logger, String langCode) throws Exception;

	public ResponseEntity<ResponseModel> getMobileApptsFeaturesHomePage(Logger logger, String clientCode, String langCode, String token) throws Exception;

	public ResponseEntity<ResponseModel> getMobileHomePage(Logger logger, String langCode, String uuid) throws Exception;

	public ResponseEntity<ResponseModel> getSearchFieldInitValue(Logger logger, String langCode) throws Exception;

	public ResponseEntity<ResponseModel> getMobileMenuItems(Logger logger, String langCode, String uuid) throws Exception;

	public ResponseEntity<ResponseModel> getIconAndButtonLabels(Logger logger, String clientCode, String langCode, String token) throws Exception;

	ResponseEntity<ResponseModel> getLocations(Logger logger, String clientCode, String langCode, String token) throws Exception;

	ResponseEntity<ResponseModel> getServiceList(Logger logger, String clientCode, String device, String langCode, Integer locationId, Integer resourceId, Integer departmentId, String token) throws Exception;

	public ResponseEntity<ResponseModel> getMessages(Logger logger, String clientCode, String langCode, String token) throws Exception;

	public ResponseEntity<ResponseModel> getListOfDocsTOBring(Logger logger, String clientCode, String langCode, Integer serviceId, String token) throws Exception;

	public ResponseEntity<ResponseModel> getAvailableDates(Logger logger, String clientCode, String langCode, String device, Long locationId, Long departmentId, Long resourceId, Long serviceId, String token) throws Exception;

	public ResponseEntity<ResponseModel> getAvailableTimes(String clientCode, String device, Long locationId, Long departmentId, Long resourceId, Long serviceId, String availDate, Logger logger,
			String langCode, String token) throws Exception;

	ResponseEntity<ResponseModel> loginMobileAuthenticate(Logger logger, CustomerInfoRequest custInfoReq) throws Exception;

	public ResponseEntity<ResponseModel> getMobilePageFields(Logger logger, String clientCode, String langCode, String device) throws Exception;

	public ResponseEntity<ResponseModel> holdAppointment(String clientCode, Long locationId, Long procedureId, Long departmentId, Long resourceId, Long serviceId, Long customerId,
			String apptDateTime, Logger logger, String device, String langCode, Long transId, String token) throws Exception;

	public ResponseEntity<ResponseModel> bookAppointment(Logger logger, ConfirmAppointmentRequest confirmAppointmentRequest) throws Exception;

	ResponseEntity<ResponseModel> getVerifyPageData(Logger logger, String clientCode, String device, String langCode, Long customerId, Long scheduleId, String token) throws Exception;

	public ResponseEntity<ResponseModel> getMobilePageFieldsForCancelAppts(Logger logger, String clientCode, String langCode, String device) throws Exception;

	public ResponseEntity<ResponseModel> loginAuthenticateAndUpdate(Logger logger, CustomerInfoRequest customerInfoRequest) throws Exception;

	public ResponseEntity<ResponseModel> getMyProvidesLocResSerList(HttpHeaders httpHeader, Logger logger,String device, String uuid) throws Exception;

	public ResponseEntity<ResponseModel> addMyProvidersList(HttpHeaders httpHeader, Logger logger, String clientCode, String device, String uuid) throws Exception;

	ResponseEntity<ResponseModel> deleteProvidersList(HttpHeaders httpHeader, Logger logger, String clientCode, String device, String uuid) throws Exception;

	public ResponseEntity<ResponseModel> getClientListByMobileCode(HttpHeaders httpHeader, Logger logger, String uuid, String mobileCode) throws Exception;

	ResponseEntity<ResponseModel> getMyProvidersList(HttpHeaders httpHeader, Logger logger, String device, String uuid) throws Exception;

	public ResponseEntity<ResponseModel> updateLocResSer(Logger logger, String clientCode, String uuid, Long locationId, Long resourceId, Long serviceId) throws Exception;

	public ResponseEntity<ResponseModel> getMyAccountDetails(Logger logger, String device, String uuid) throws Exception;

	ResponseEntity<ResponseModel> getPersonalInfo(Logger logger, String clientCode, String device, String langCode, Long customerId) throws Exception;

	public ResponseEntity<ResponseModel> getExistingAppointments(Logger logger, String clientCode, String device, String langCode, Long customerId, String uuid) throws Exception;

}
