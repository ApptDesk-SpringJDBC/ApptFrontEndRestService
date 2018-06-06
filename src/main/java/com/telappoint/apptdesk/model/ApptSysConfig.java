package com.telappoint.apptdesk.model;

/**
 * 
 * @author Balaji N
 *
 */
public class ApptSysConfig {
	private String displayCompany;
	private String displayProcedure;
	private String displayLocation;
	private String displayDepartment;
	private String displayResource;
	private String displayService;
	
	private String enforceLogin;
	private String loginFirst;
	private String sendConfirmEmail;
	private String sendCancelEmail;
	private String schedulerClosed;
	private String runPhoneTypeLookup;
	//ivr
	private String ivrTimeBatchSize;
	private String recordMsg;
	private String noFunding;
	private String displayComments;
	private String commentsNoOfRows;
	private String commentsNoOfCols;

	private Integer maxApptDurationDays;
	private Integer apptDelayTimeDays;
	private Integer apptDelayTimeHrs;
	private String restrictApptWindow; 
	private String apptStartDate; 
	private String apptEndDate; 
	private String restrictLocApptWindow; 
	private String restrictSerApptWindow; 
	private String restrictLocSerApptWindow;
	private String onlineDateTimeFormat;
	private String ccConfirmEmails;
	private String ccCancalEmails;
	private String sendReschdEmail;
	private String allowAnyDoctor;
	private String checkAssignedResource;
	
	public String getDisplayCompany() {
		return displayCompany;
	}

	public void setDisplayCompany(String displayCompany) {
		this.displayCompany = displayCompany;
	}

	public String getDisplayProcedure() {
		return displayProcedure;
	}

	public void setDisplayProcedure(String displayProcedure) {
		this.displayProcedure = displayProcedure;
	}

	public String getDisplayLocation() {
		return displayLocation;
	}

	public void setDisplayLocation(String displayLocation) {
		this.displayLocation = displayLocation;
	}

	public String getDisplayDepartment() {
		return displayDepartment;
	}

	public void setDisplayDepartment(String displayDepartment) {
		this.displayDepartment = displayDepartment;
	}

	public String getEnforceLogin() {
		return enforceLogin;
	}

	public void setEnforceLogin(String enforceLogin) {
		this.enforceLogin = enforceLogin;
	}

	public String getLoginFirst() {
		return loginFirst;
	}

	public void setLoginFirst(String loginFirst) {
		this.loginFirst = loginFirst;
	}

	public String getSendConfirmEmail() {
		return sendConfirmEmail;
	}

	public void setSendConfirmEmail(String sendConfirmEmail) {
		this.sendConfirmEmail = sendConfirmEmail;
	}

	public String getSendCancelEmail() {
		return sendCancelEmail;
	}

	public void setSendCancelEmail(String sendCancelEmail) {
		this.sendCancelEmail = sendCancelEmail;
	}

	public String getIvrTimeBatchSize() {
		return ivrTimeBatchSize;
	}

	public void setIvrTimeBatchSize(String ivrTimeBatchSize) {
		this.ivrTimeBatchSize = ivrTimeBatchSize;
	}

	public String getRecordMsg() {
		return recordMsg;
	}

	public void setRecordMsg(String recordMsg) {
		this.recordMsg = recordMsg;
	}

	public String getSchedulerClosed() {
		return schedulerClosed;
	}

	public void setSchedulerClosed(String schedulerClosed) {
		this.schedulerClosed = schedulerClosed;
	}

	public String getNoFunding() {
		return noFunding;
	}

	public void setNoFunding(String noFunding) {
		this.noFunding = noFunding;
	}

	public String getDisplayComments() {
		return displayComments;
	}

	public void setDisplayComments(String displayComments) {
		this.displayComments = displayComments;
	}

	public String getCommentsNoOfRows() {
		return commentsNoOfRows;
	}

	public void setCommentsNoOfRows(String commentsNoOfRows) {
		this.commentsNoOfRows = commentsNoOfRows;
	}

	public String getCommentsNoOfCols() {
		return commentsNoOfCols;
	}

	public void setCommentsNoOfCols(String commentsNoOfCols) {
		this.commentsNoOfCols = commentsNoOfCols;
	}

	public String getRunPhoneTypeLookup() {
		return runPhoneTypeLookup;
	}

	public void setRunPhoneTypeLookup(String runPhoneTypeLookup) {
		this.runPhoneTypeLookup = runPhoneTypeLookup;
	}
	
	public Integer getApptDelayTimeDays() {
		return apptDelayTimeDays;
	}

	public void setApptDelayTimeDays(Integer apptDelayTimeDays) {
		this.apptDelayTimeDays = apptDelayTimeDays;
	}

	public Integer getApptDelayTimeHrs() {
		return apptDelayTimeHrs;
	}

	public void setApptDelayTimeHrs(Integer apptDelayTimeHrs) {
		this.apptDelayTimeHrs = apptDelayTimeHrs;
	}

	public String getRestrictApptWindow() {
		return restrictApptWindow;
	}

	public void setRestrictApptWindow(String restrictApptWindow) {
		this.restrictApptWindow = restrictApptWindow;
	}

	public String getApptStartDate() {
		return apptStartDate;
	}

	public void setApptStartDate(String apptStartDate) {
		this.apptStartDate = apptStartDate;
	}

	public String getApptEndDate() {
		return apptEndDate;
	}

	public void setApptEndDate(String apptEndDate) {
		this.apptEndDate = apptEndDate;
	}

	public String getRestrictLocApptWindow() {
		return restrictLocApptWindow;
	}

	public void setRestrictLocApptWindow(String restrictLocApptWindow) {
		this.restrictLocApptWindow = restrictLocApptWindow;
	}

	public String getRestrictSerApptWindow() {
		return restrictSerApptWindow;
	}

	public void setRestrictSerApptWindow(String restrictSerApptWindow) {
		this.restrictSerApptWindow = restrictSerApptWindow;
	}

	public String getRestrictLocSerApptWindow() {
		return restrictLocSerApptWindow;
	}

	public void setRestrictLocSerApptWindow(String restrictLocSerApptWindow) {
		this.restrictLocSerApptWindow = restrictLocSerApptWindow;
	}

	public void setMaxApptDurationDays(Integer maxApptDurationDays) {
		this.maxApptDurationDays = maxApptDurationDays;
	}
	
	public Integer getMaxApptDurationDays() {
		return maxApptDurationDays;
	}

	public String getOnlineDateTimeFormat() {
		return onlineDateTimeFormat;
	}

	public void setOnlineDateTimeFormat(String onlineDateTimeFormat) {
		this.onlineDateTimeFormat = onlineDateTimeFormat;
	}

	public String getCcConfirmEmails() {
		return ccConfirmEmails;
	}

	public void setCcConfirmEmails(String ccConfirmEmails) {
		this.ccConfirmEmails = ccConfirmEmails;
	}

	public String getCcCancalEmails() {
		return ccCancalEmails;
	}

	public void setCcCancalEmails(String ccCancalEmails) {
		this.ccCancalEmails = ccCancalEmails;
	}

	public String getSendReschdEmail() {
		return sendReschdEmail;
	}

	public void setSendReschdEmail(String sendReschdEmail) {
		this.sendReschdEmail = sendReschdEmail;
	}

	public String getDisplayResource() {
		return displayResource;
	}

	public void setDisplayResource(String displayResource) {
		this.displayResource = displayResource;
	}

	public String getDisplayService() {
		return displayService;
	}

	public void setDisplayService(String displayService) {
		this.displayService = displayService;
	}

	public String getAllowAnyDoctor() {
		return allowAnyDoctor;
	}

	public void setAllowAnyDoctor(String allowAnyDoctor) {
		this.allowAnyDoctor = allowAnyDoctor;
	}

	public String getCheckAssignedResource() {
		return checkAssignedResource;
	}

	public void setCheckAssignedResource(String checkAssignedResource) {
		this.checkAssignedResource = checkAssignedResource;
	}
}
