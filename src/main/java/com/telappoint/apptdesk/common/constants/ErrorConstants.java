package com.telappoint.apptdesk.common.constants;

/**
 * 
 * @author Balaji N
 *
 */
public enum ErrorConstants {
	// DB layer error codes
	ERROR_1000("1000", "Error in getClient fetch."), 
	ERROR_1001("1001", "Error in getClientDeploymentConfig fetch."), 
	ERROR_1002("1002", "Error while generating the transId"),
	ERROR_1003("1003", "Error while updating the ivr calls"),
	ERROR_1004("1004", "Error while saving the ivr calls"),
	ERROR_1005("1005", "Error while saving the transaction state"),
	ERROR_1006("1006", "Error while fetching the page validation message.."),
	ERROR_1016("1016", "Error while fetching ApptSysConfig details."),
	ERROR_1024("1024", "Error while TransId not saved to DB!"),
	ERROR_1025("1025", "Error while fetching the onlineFlowData"),
	ERROR_1026("1026", "Error while fetching the onlinePageContent data"),
	ERROR_1027("1027", "Error while fetching the onlinePageFields data"),
	ERROR_1028("1028", "Error while fetching the ivr vxml data"),
	ERROR_1029("1029", "Error while fetching the procedure table data."),
	ERROR_1030("1030", "Error while prepare the options aliases data."),
	ERROR_1031("1031", "Error while getLocationById."),
	ERROR_1032("1032", "Error while fetching onlinePageFields by contentId"),
	ERROR_1033("1033", "Error while matching authenticate fields in backend - {Order mismatched}"),
	ERROR_1034("1034", "Error while authenticate the login"),
	ERROR_1035("1035", "Error while fetching the customer"),
	ERROR_1036("1036", "Error while saving the customer to DB"),
	ERROR_1037("1037", "Error while updating the customer to DB"),
	ERROR_1038("1038", "Error while validating the customer"),
	ERROR_1039("1039", "Error while get procedureId by procedure Name"),
	ERROR_1040("1040", "Error while fetch the data by field name in authenticate method"),
	ERROR_1041("1041", "Error while fetch ServiceFundReceivedIds"),
	ERROR_1042("1042", "Error while fetch errorPage from serviceFundsCustomerUtilityTypes"),
	ERROR_1043("1043", "Error while fetching the services"),
	ERROR_1044("1044", "Error while fetching the monthly income"),
	ERROR_1045("1045", "Error while fetching the Ivr page field data"),
	ERROR_1046("1046", "Error while update the resource calendar for hold appointment"),
	ERROR_1047("1047", "Error while saving the schedule table for hold appointment"),
	ERROR_1048("1048", "Error while holdingFirstAvailableTime - CustomerId should not be null or zero"),
	ERROR_1049("1049", "InvalidDataAccessApiUsage error in temp hold storedprocedure"),
	ERROR_1050("1050", "Error while fetching the FirstAvailableTime from first_available_time_sp call."),
	ERROR_1051("1051", "Error while fetching appointment details."),
	ERROR_1052("1052", "Error while saving NameRecord details."),
	ERROR_1053("1053", "Error while getCustomer information."),
	ERROR_1054("1054", "Error while calling first_available_time_sp"),
	ERROR_1055("1055", "Error while calling temp hold appointment"),
	ERROR_1056("1056", "Error while calling book appointment stored procedure."),
	ERROR_1057("1057", "Error while calling cancel appointment stored procedure."),
	ERROR_1058("1058", "Error while calling get location availability stored procedure."),
	ERROR_1059("1059", "Error while holdingFirstAvailableTime - LocationId should not be null or zero"),
	ERROR_1060("1060", "Error while holdingFirstAvailableTime - ServiceId should not be null or zero"),
	ERROR_1061("1061", "Error while procedure no match"),
	ERROR_1062("1062", "Error while fetching getCustomerType."),
	ERROR_1063("1063", "Error while fetching getUtilityType."),
	ERROR_1064("1064", "Error while fetching getServiceNameCustomerUtility."),
	ERROR_1065("1065", "Error while fetching getServiceNameCustomerWarningPage."),
	ERROR_1066("1066", "ServiceIds not passed from front end."),
	ERROR_1067("1067", "Error while releaseHoldAppointment."),
	ERROR_1068("1068", "Error while fetching language details."),
	ERROR_1069("1069","getFirstAvailableDateTime data format is invalid from stored procedure."),
	ERROR_1070("1070", "Error while calling get service availability stored procedure."),
	ERROR_1071("1071", "Error while fetching design template details."),
	ERROR_1072("1072", "Error while populate Map."),
	ERROR_1073("1073", "Error while fetching getLangDetails."),
	ERROR_1074("1074", "Error while extending hold time."),
	ERROR_1075("1075", "Error while fetching the pledge details."),
	ERROR_1076("1076", "Error while holding the time."),
	ERROR_1077("1077", "Selected date and time is not available."),
	ERROR_1078("1078", "Error while fetching the direct access numbers."),
	ERRRO_1079("1079", "Error while fetching the schedule table data."),
	ERROR_1080("1080", "Error while calling first_available_time_any_location_sp"),
	ERROR_1081("1081", "Services are empty"),
	ERROR_1082("1082", "Error while fetching getServiceListByLocationId."),
	ERROR_1086("1086", "Error while fetching the mobile page field data"),
	
	
	// iphone codes
	ERROR_1083("1083", "Iphone invalid password."),
	ERROR_1084("1084", "Error while fetching loginparamConfig data"),
	ERROR_1085("1085", "Customer Id not passed while booking an appointment"),
	ERROR_1087("1087", "Customer Id not passed while getting the verify appointment"),
	
	// Service layer error codes
	ERROR_2001("2001", "Error while prepare the connection pool"),
	ERROR_2002("2002", "Invalid input parameters passed from front end"),
	ERROR_2003("2003","Error while prepare the confirm email data."),
	ERROR_2994("2994", "Unable to fetch info from database. Please try again later."),
	ERROR_2995("2995", "IP Not Allowed"),
	ERROR_2996("2996","Unable to fetch Error/Warning Message from database"),
	ERROR_2997("2997","Error while reading properties"),
	ERROR_2998("2998", "Error while getClient information."),
	ERROR_2999("2999", "Error while getLogger instance."),
	ERROR_3000("3000", "Error while IP checking."),
	ERROR_3001("3001", "ClientCode Not Allowed"),
	ERROR_9999("9999", "Generic Error");
	
	private String code;
	private String message;

	private ErrorConstants(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
