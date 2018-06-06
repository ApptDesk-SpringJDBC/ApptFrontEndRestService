package com.telappoint.apptdesk.common.model;

import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonAutoDetect
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class AvailableDateTimes extends BaseResponse {
    private String availableDates;
    private List<String> availableDatesArray;
    private List<String> holidayListArray;
    private List<String> closedDayList;
    
	private String isSlotAvailableMessage;
	private String isNotAvailableMessage;
	private String isHolidayMessage;
	private String isClosedMessage;
 
	private String availableTimes;
    private List<String> availableTimesArray;
    private String displayTimeList;
    private String warningFlag;
    private String warningMessage;
    private String errorFlag;
	private String errorMessage;
	private String timeZone;
	
	// mobile
	private Map<String,String> pageData;

	public String getAvailableDates() {
		return availableDates;
	}

	public void setAvailableDates(String availableDates) {
		this.availableDates = availableDates;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getAvailableTimes() {
		return availableTimes;
	}

	public void setAvailableTimes(String availableTimes) {
		this.availableTimes = availableTimes;
	}

	public String getDisplayTimeList() {
		return displayTimeList;
	}

	public void setDisplayTimeList(String displayTimeList) {
		this.displayTimeList = displayTimeList;
	}
	
	public AvailableDateTimes() {
		
	}
	
	public AvailableDateTimes(String availableDates, String errorMessage) {
		this.availableDates = availableDates;
		this.errorMessage = errorMessage;
	}
	
	public AvailableDateTimes(String availableDates, boolean status, String errorMessage) {
		this.status=status;
		this.availableDates = availableDates;
		this.errorMessage = errorMessage;
	}

	public AvailableDateTimes(String availableDates, String availableTimes, String errorMessage) {
		this.availableDates = availableDates;
		this.availableTimes = availableTimes;
		this.errorMessage = errorMessage;
	}

	public AvailableDateTimes(String availableDates, String availableTimes, String displayTimeList, String errorMessage) {
		this.availableDates = availableDates;
		this.availableTimes = availableTimes;
		this.displayTimeList = displayTimeList;
		this.errorMessage = errorMessage;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public String getWarningFlag() {
		return warningFlag;
	}

	public void setWarningFlag(String warningFlag) {
		this.warningFlag = warningFlag;
	}

	public String getWarningMessage() {
		return warningMessage;
	}

	public void setWarningMessage(String warningMessage) {
		this.warningMessage = warningMessage;
	}

	public Map<String,String> getPageData() {
		return pageData;
	}

	public void setPageData(Map<String,String> pageData) {
		this.pageData = pageData;
	}

	public String getErrorFlag() {
		return errorFlag;
	}

	public void setErrorFlag(String errorFlag) {
		this.errorFlag = errorFlag;
	}

	public List<String> getAvailableDatesArray() {
		return availableDatesArray;
	}

	public void setAvailableDatesArray(List<String> availableDatesArray) {
		this.availableDatesArray = availableDatesArray;
	}

	public List<String> getAvailableTimesArray() {
		return availableTimesArray;
	}

	public void setAvailableTimesArray(List<String> availableTimesArray) {
		this.availableTimesArray = availableTimesArray;
	}

	public List<String> getHolidayListArray() {
		return holidayListArray;
	}

	public void setHolidayListArray(List<String> holidayListArray) {
		this.holidayListArray = holidayListArray;
	}

	public List<String> getClosedDayList() {
		return closedDayList;
	}

	public void setClosedDayList(List<String> closedDayList) {
		this.closedDayList = closedDayList;
	}
	
	 public String getIsSlotAvailableMessage() {
		return isSlotAvailableMessage;
	}

	public void setIsSlotAvailableMessage(String isSlotAvailableMessage) {
		this.isSlotAvailableMessage = isSlotAvailableMessage;
	}

	public String getIsNotAvailableMessage() {
		return isNotAvailableMessage;
	}

	public void setIsNotAvailableMessage(String isNotAvailableMessage) {
		this.isNotAvailableMessage = isNotAvailableMessage;
	}

	public String getIsHolidayMessage() {
		return isHolidayMessage;
	}

	public void setIsHolidayMessage(String isHolidayMessage) {
		this.isHolidayMessage = isHolidayMessage;
	}

	public String getIsClosedMessage() {
		return isClosedMessage;
	}

	public void setIsClosedMessage(String isClosedMessage) {
		this.isClosedMessage = isClosedMessage;
	}
}
