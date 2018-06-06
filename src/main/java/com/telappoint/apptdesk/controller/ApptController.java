package com.telappoint.apptdesk.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.telappoint.apptdesk.common.constants.CommonApptDeskConstants;
import com.telappoint.apptdesk.common.constants.ErrorConstants;
import com.telappoint.apptdesk.common.constants.PropertiesConstants;
import com.telappoint.apptdesk.common.logger.AsynchLogger;
import com.telappoint.apptdesk.common.logger.LoggerPool;
import com.telappoint.apptdesk.common.model.BaseResponse;
import com.telappoint.apptdesk.common.model.ExternalLoginRequest;
import com.telappoint.apptdesk.common.model.ResponseModel;
import com.telappoint.apptdesk.common.utils.CoreUtils;
import com.telappoint.apptdesk.common.utils.PropertyUtils;
import com.telappoint.apptdesk.handlers.exception.TelAppointException;
import com.telappoint.apptdesk.model.ConfirmAppointmentRequest;
import com.telappoint.apptdesk.model.CustomerInfoRequest;
import com.telappoint.apptdesk.model.HoldAppointmentRequest;
import com.telappoint.apptdesk.model.IVRCallRequest;
import com.telappoint.apptdesk.model.NameRecordInfo;
import com.telappoint.apptdesk.model.TransScriptRequest;
import com.telappoint.apptdesk.model.UpdateCustomerInfoRequest;
import com.telappoint.apptdesk.service.ApptService;

/**
 * @author Balaji N
 */
@RestController
@RequestMapping("/service")
public class ApptController {

	/**
	 * This will be auto wired while component scan.
	 */
	@Autowired
	public ApptService apptService;

	public ApptController() {
	}

	public ApptController(ApptService apptService) {
		this.apptService = apptService;
	}

	@RequestMapping(method = RequestMethod.GET, value = "getOnlineLandingPage", produces = "application/json")
	public ResponseEntity<ResponseModel> getOnlineLandingPage(HttpServletRequest request, @RequestParam("clientCode") String clientCode,
			@RequestParam(value = "langCode", required = false) String langCode, @RequestParam("device") String device) {
		Logger logger = null;

		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			
			logger = apptService.getLogger(clientCode, "INFO");
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, logger, ipAddress);
			}
			
			return apptService.getOnlineLandingPage(logger, clientCode, langCode, device);
		} catch (TelAppointException e) {
			return apptService.handleException(logger, clientCode, e);
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "getOnlinePageData", produces = "application/json")
	public ResponseEntity<ResponseModel> getOnlinePageData(HttpServletRequest request, @RequestParam("clientCode") String clientCode,
			@RequestParam("langCode") String langCode, @RequestParam("device") String device) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			
			logger = apptService.getLogger(clientCode, "INFO");
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, logger, ipAddress);
			}
			return apptService.getOnlinePageData(logger, clientCode, langCode, device);
		} catch (TelAppointException e) {
			return apptService.handleException(logger, clientCode, e);
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}

	// ONLINE only
	@RequestMapping(method = RequestMethod.GET, value = "getPageValidationMessages", produces = "application/json")
	public ResponseEntity<ResponseModel> getPageValidationMessages(HttpServletRequest request, @RequestParam("device") String device) {

		Logger logger = null;
		try {
			logger = getDefaultLog();
			return apptService.getPageValidationMessages(logger, device);
		} catch (TelAppointException e) {
			return apptService.handleException(logger, null, e);
		} catch (Exception e) {
			return apptService.handleException(logger, null, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "getIVRPageFields", produces = "application/json")
	public ResponseEntity<ResponseModel> getIVRPageFields(HttpServletRequest request, @RequestParam("clientCode") String clientCode,
			@RequestParam("langCode") String langCode) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			
			logger = apptService.getLogger(clientCode, "INFO");
			String device = CommonApptDeskConstants.IVRAUDIO.getValue();
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, logger, ipAddress);
			}
			return apptService.getIVRPageFields(logger, clientCode, langCode, device);
		} catch (TelAppointException e) {
			return apptService.handleException(logger, clientCode, e);
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "getIVRFlow", produces = "application/json")
	public ResponseEntity<ResponseModel> getIVRFlow(HttpServletRequest request, @RequestParam("clientCode") String clientCode,
			@RequestParam(value = "anis", required = false) String anis, @RequestParam(value = "dnis", required = false) String dnis) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			
			logger = apptService.getLogger(clientCode, "INFO");
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, logger, ipAddress);
			}
			return apptService.getIVRFlow(logger, clientCode, CommonApptDeskConstants.IVRAUDIO.getValue(), "us-en", anis, dnis);
		} catch (TelAppointException e) {
			return apptService.handleException(logger, clientCode, e);
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "getVXML", produces = "application/json")
	public ResponseEntity<ResponseModel> getVXML(HttpServletRequest request, @RequestParam("clientCode") String clientCode,
			@RequestParam(value = "langCode", required = false, defaultValue = "us-en") String langCode, @RequestParam("page") String page) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			
			logger = apptService.getLogger(clientCode, "INFO");
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, logger, ipAddress);
			}
			return apptService.getVXML(logger, clientCode, CommonApptDeskConstants.IVRAUDIO.getValue(), langCode, page);
		} catch (TelAppointException e) {
			return apptService.handleException(logger, clientCode, e);
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "getIVRVxml", produces = "application/json")
	public ResponseEntity<ResponseModel> getIVRVxml(HttpServletRequest request, @RequestParam("clientCode") String clientCode) {
		Logger logger = null;
		try {
			
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			
			logger = apptService.getLogger(clientCode, "INFO");
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, logger, ipAddress);
			}
			return apptService.getIVRVxml(logger, clientCode, CommonApptDeskConstants.IVRAUDIO.getValue());
		} catch (TelAppointException e) {
			return apptService.handleException(logger, clientCode, e);
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}

	// IVR only
	@RequestMapping(method = RequestMethod.POST, value = "updateNameRecord", produces = "application/json")
	public ResponseEntity<ResponseModel> updateNameRecord(HttpServletRequest request, @RequestBody NameRecordInfo nameRecordInfo) {
		String clientCode = nameRecordInfo.getClientCode();
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			logger = apptService.getLogger(nameRecordInfo.getClientCode(), "INFO");
			nameRecordInfo.setDevice(CommonApptDeskConstants.IVRAUDIO.getValue());
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(nameRecordInfo.getClientCode(), logger, ipAddress);
			}
			return apptService.updateNameRecord(logger, nameRecordInfo);
		} catch (TelAppointException e) {
			return apptService.handleException(logger, clientCode, e);
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}

	// IVR only
	@RequestMapping(method = RequestMethod.POST, value = "updateIVRCallLogs", produces = "application/json")
	public ResponseEntity<ResponseModel> updateIVRCallLogs(HttpServletRequest request, @RequestBody IVRCallRequest ivrCallRequest) {
		String clientCode = ivrCallRequest.getClientCode();
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			logger = apptService.getLogger(ivrCallRequest.getClientCode(), "INFO");
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(ivrCallRequest.getClientCode(), logger, ipAddress);
			}
			return apptService.updateIVRCalls(logger, ivrCallRequest);
		} catch (TelAppointException tae) {
			return apptService.handleException(logger, clientCode, tae);
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "getProcedureId", produces = "application/json")
	public ResponseEntity<ResponseModel> getProcedureId(HttpServletRequest request, @RequestParam("clientCode") String clientCode,
			@RequestParam("device") String device, @RequestParam("procedureName") String procedureName) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			
			logger = apptService.getLogger(clientCode, "INFO");
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, logger, ipAddress);
			}
			return apptService.getProcedureId(logger, clientCode, device, procedureName);
		} catch (TelAppointException e) {
			return apptService.handleException(logger, clientCode, e);
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "getProcedure", produces = "application/json")
	public ResponseEntity<ResponseModel> getProcedure(HttpServletRequest request, @RequestParam("clientCode") String clientCode,
			@RequestParam("langCode") String langCode, @RequestParam("device") String device) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			
			logger = apptService.getLogger(clientCode, "INFO");
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, logger, ipAddress);
			}
			return apptService.getProcedure(logger, clientCode, device, langCode);
		} catch (TelAppointException e) {
			return apptService.handleException(logger, clientCode, e);
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "getProcedureNoMatch", produces = "application/json")
	public ResponseEntity<ResponseModel> getProcedureNoMatch(HttpServletRequest request, @RequestParam("clientCode") String clientCode,
			@RequestParam("langCode") String langCode, @RequestParam("device") String device, @RequestParam("procedureValue") String procedureValue) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			
			logger = apptService.getLogger(clientCode, "INFO");
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, logger, ipAddress);
			}
			return apptService.getProcedureNoMatch(logger, clientCode, device, langCode, procedureValue);
		} catch (TelAppointException e) {
			return apptService.handleException(logger, clientCode, e);
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "getLocationPrimarySecondaryAvailability", produces = "application/json")
	public ResponseEntity<ResponseModel> getLocationPrimarySecondaryAvailability(HttpServletRequest request, @RequestParam("clientCode") String clientCode,
			@RequestParam("langCode") String langCode, @RequestParam("device") String device, @RequestParam("procedureId") String procedureId) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			
			logger = apptService.getLogger(clientCode, "INFO");
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, logger, ipAddress);
			}
			return apptService.getLocationPrimarySecondaryAvailability(logger, clientCode, device, langCode, procedureId);
		} catch (TelAppointException e) {
			return apptService.handleException(logger, clientCode, e);
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "loginAuthenticate", produces = "application/json", consumes = "application/json")
	public ResponseEntity<ResponseModel> loginAuthenticate(HttpServletRequest request, @RequestBody CustomerInfoRequest customerInfoRequest) {
		String clientCode = customerInfoRequest.getClientCode();
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			logger = apptService.getLogger(customerInfoRequest.getClientCode(), "INFO");
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(customerInfoRequest.getClientCode(), logger, ipAddress);
			}
			return apptService.loginAuthenticate(logger, customerInfoRequest);
		} catch (TelAppointException e) {
			return apptService.handleException(logger, clientCode, e);
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "authenticateAndUpdateCustomer", produces = "application/json", consumes="application/json")
	public ResponseEntity<ResponseModel> authenticateAndUpdateCustomer(HttpServletRequest request, @RequestBody CustomerInfoRequest customerInfoRequest) {
		String clientCode = customerInfoRequest.getClientCode();
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			logger = apptService.getLogger(customerInfoRequest.getClientCode(), "INFO");
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(customerInfoRequest.getClientCode(), logger, ipAddress);
			}
			return apptService.authenticateAndUpdateCustomer(logger, customerInfoRequest);
		} catch (TelAppointException e) {
			return apptService.handleException(logger, clientCode, e);
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "loginAuthenticateAndUpdateForExternal", produces = "application/json", consumes="application/json")
	public ResponseEntity<ResponseModel> loginAuthenticateAndUpdateForExternal(HttpServletRequest request, @RequestBody ExternalLoginRequest externalInfo) {
		String clientCode = externalInfo.getClientCode();
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			logger = apptService.getLogger(externalInfo.getClientCode(), "INFO");
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(externalInfo.getClientCode(), logger, ipAddress);
			}
			return apptService.loginAuthenticateAndUpdateForExternal(logger, externalInfo);
		} catch (TelAppointException e) {
			return apptService.handleException(logger, clientCode, e);
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "loginAuthenticateForExternal", produces = "application/json", consumes="application/json")
	public ResponseEntity<ResponseModel> loginAuthenticateForExternal(HttpServletRequest request, @RequestBody CustomerInfoRequest customerInfo) {
		String clientCode = customerInfo.getClientCode();
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			logger = apptService.getLogger(customerInfo.getClientCode(), "INFO");
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(customerInfo.getClientCode(), logger, ipAddress);
			}
			return apptService.loginAuthenticateForExternal(logger, customerInfo);
		} catch (TelAppointException e) {
			return apptService.handleException(logger, clientCode, e);
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "confirmAppointmentExternalLogic", produces = "application/json")
	public ResponseEntity<ResponseModel> confirmAppointmentExternalLogic(HttpServletRequest request, @RequestBody ConfirmAppointmentRequest confirmAppointmentRequest) {
		String clientCode = confirmAppointmentRequest.getClientCode();
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			logger = apptService.getLogger(confirmAppointmentRequest.getClientCode(), "INFO");
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(confirmAppointmentRequest.getClientCode(), logger, ipAddress);
			}
			return apptService.confirmAppointmentExternalLogic(logger, confirmAppointmentRequest);
		} catch (TelAppointException e) {
			return apptService.handleException(logger, clientCode, e);
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "confirmAppointmentForFileUpload", produces = "application/json")
	public ResponseEntity<ResponseModel> confirmAppointmentFileUpload(HttpServletRequest request, @RequestBody ConfirmAppointmentRequest confirmAppointmentRequest) {
		String clientCode = confirmAppointmentRequest.getClientCode();
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			logger = apptService.getLogger(confirmAppointmentRequest.getClientCode(), "INFO");
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(confirmAppointmentRequest.getClientCode(), logger, ipAddress);
			}
			return apptService.confirmAppointmentForFileUpload(logger, confirmAppointmentRequest);
		} catch (TelAppointException e) {
			return apptService.handleException(logger, clientCode, e);
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "authenticateForCancel", produces = "application/json")
	public ResponseEntity<ResponseModel> authenticateForCancel(HttpServletRequest request, @RequestParam String clientCode, @RequestParam String device,
			@RequestParam String langCode, @RequestParam String inputParamValues) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			logger = apptService.getLogger(clientCode, "INFO");
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, logger, ipAddress);
			}
			return apptService.authenticateForCancel(logger, clientCode, langCode, device, inputParamValues);
		} catch (TelAppointException e) {
			return apptService.handleException(logger, clientCode, e);
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "getCustomerType", produces = "application/json")
	public ResponseEntity<ResponseModel> getCustomerType(HttpServletRequest request, @RequestParam("clientCode") String clientCode,
			@RequestParam("device") String device, @RequestParam("langCode") String langCode, @RequestParam("customerId") Long customerId,
			@RequestParam("locationId") Integer locationId) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			logger = apptService.getLogger(clientCode, "INFO");
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, logger, ipAddress);
			}
			return apptService.getCustomerType(logger, clientCode, device, langCode, customerId, locationId);
		} catch (TelAppointException e) {
			return apptService.handleException(logger, clientCode, e);
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "getUtilityType", produces = "application/json")
	public ResponseEntity<ResponseModel> getUtilityType(HttpServletRequest request, @RequestParam("clientCode") String clientCode,
			@RequestParam("device") String device, @RequestParam("langCode") String langCode, @RequestParam("serviceFundsRcvdId") Integer serviceFundsRcvdId,
			@RequestParam("customerTypeId") Integer customerTypeId,@RequestParam("locationId") Integer locationId) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			logger = apptService.getLogger(clientCode, "INFO");
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, logger, ipAddress);
			}
			return apptService.getUtilityType(logger, clientCode, device, langCode, serviceFundsRcvdId, customerTypeId, locationId);
		} catch (TelAppointException e) {
			return apptService.handleException(logger, clientCode, e);
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "getServiceNameCustomerUtility", produces = "application/json")
	public ResponseEntity<ResponseModel> getServiceNameCustomerUtility(HttpServletRequest request, @RequestParam("clientCode") String clientCode,
			@RequestParam("device") String device, @RequestParam("langCode") String langCode, @RequestParam("serviceFundsRcvdId") Integer serviceFundsRcvdId,
			@RequestParam("customerTypeId") Integer customerTypeId, @RequestParam("locationId") Integer locationId, @RequestParam("utilityTypeId") Integer utilityTypeId) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			logger = apptService.getLogger(clientCode, "INFO");
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, logger, ipAddress);
			}
			return apptService.getServiceNameCustomerUtility(logger, clientCode, device, langCode, serviceFundsRcvdId, customerTypeId, locationId, utilityTypeId);
		} catch (TelAppointException e) {
			return apptService.handleException(logger, clientCode, e);
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "getServiceNameCustomerWarningPage", produces = "application/json")
	public ResponseEntity<ResponseModel> getServiceNameCustomerWarningPage(HttpServletRequest request, @RequestParam("clientCode") String clientCode,
			@RequestParam("device") String device, @RequestParam("langCode") String langCode, @RequestParam("serviceFundsRcvdId") Integer serviceFundsRcvdId,
			@RequestParam("customerTypeId") Integer customerTypeId, @RequestParam("utilityTypeId") Integer utilityTypeId, @RequestParam("serviceId") Integer serviceId) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			logger = apptService.getLogger(clientCode, "INFO");
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, logger, ipAddress);
			}
			return apptService.getServiceNameCustomerWarningPage(logger, clientCode, device, langCode, serviceFundsRcvdId, customerTypeId, utilityTypeId, serviceId);
		} catch (TelAppointException e) {
			return apptService.handleException(logger, clientCode, e);
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "getSingleServiceNotClosed", produces = "application/json")
	public ResponseEntity<ResponseModel> getSingleServiceNotClosed(HttpServletRequest request, @RequestParam("clientCode") String clientCode,
			@RequestParam("device") String device, @RequestParam("langCode") String langCode, @RequestParam("serviceIds") String serviceIds) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			logger = apptService.getLogger(clientCode, "INFO");
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, logger, ipAddress);
			}
			return apptService.getSingleServiceNotClosed(logger, clientCode, device, langCode, serviceIds);
		} catch (TelAppointException e) {
			return apptService.handleException(logger, clientCode, e);
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "getServiceClosedStatus", produces = "application/json")
	public ResponseEntity<ResponseModel> getServiceClosedStatus(HttpServletRequest request, @RequestParam("clientCode") String clientCode,
			@RequestParam("device") String device, @RequestParam("langCode") String langCode, @RequestParam("serviceId") Integer serviceId, @RequestParam("locationId") Integer locationId) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			logger = apptService.getLogger(clientCode, "INFO");
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, logger, ipAddress);
			}
			return apptService.getServiceClosedStatus(logger, clientCode, device, langCode, serviceId, locationId);
		} catch (TelAppointException e) {
			return apptService.handleException(logger, clientCode, e);
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "getServiceTimeSlotsAvailableStatus", produces = "application/json")
	public ResponseEntity<ResponseModel> getServiceTimeSlotsAvailableStatus(HttpServletRequest request, @RequestParam("clientCode") String clientCode,
			@RequestParam("device") String device, @RequestParam("langCode") String langCode, @RequestParam("locationId") Integer locationId,
			@RequestParam("serviceId") Integer serviceId) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			logger = apptService.getLogger(clientCode, "INFO");
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, logger, ipAddress);
			}
			return apptService.getServiceTimeSlotsAvailableStatus(logger, clientCode, device, langCode, locationId, serviceId);
		} catch (TelAppointException e) {
			return apptService.handleException(logger, clientCode, e);
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "holdFirstAvailableAppointment", produces = "application/json")
	public ResponseEntity<ResponseModel> holdFirstAvailableAppointment(HttpServletRequest request, @RequestBody HoldAppointmentRequest holdAppointmentRequest) {
		String clientCode = holdAppointmentRequest.getClientCode();
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			logger = apptService.getLogger(holdAppointmentRequest.getClientCode(), "INFO");
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(holdAppointmentRequest.getClientCode(), logger, ipAddress);
			}
			return apptService.holdFirstAvailableAppointment(logger, holdAppointmentRequest);
		} catch (TelAppointException e) {
			return apptService.handleException(logger, clientCode, e);
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "confirmAppointment", produces = "application/json")
	public ResponseEntity<ResponseModel> confirmAppointment(HttpServletRequest request, @RequestBody ConfirmAppointmentRequest confirmAppointmentRequest) {
		String clientCode = confirmAppointmentRequest.getClientCode();
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			logger = apptService.getLogger(confirmAppointmentRequest.getClientCode(), "INFO");
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(confirmAppointmentRequest.getClientCode(), logger, ipAddress);
			}
			return apptService.confirmAppointment(logger, confirmAppointmentRequest);
		} catch (TelAppointException e) {
			return apptService.handleException(logger, clientCode, e);
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "getListOfThingsToBring", produces = "application/json")
	public ResponseEntity<ResponseModel> getListOfThingsToBring(HttpServletRequest request, @RequestParam("clientCode") String clientCode,
			@RequestParam("device") String device, @RequestParam("langCode") String langCode, @RequestParam("serviceId") Integer serviceId) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			logger = apptService.getLogger(clientCode, "INFO");
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, logger, ipAddress);
			}
			return apptService.getListOfThingsToBring(logger, clientCode, device, langCode, serviceId);
		} catch (TelAppointException e) {
			return apptService.handleException(logger, clientCode, e);
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "getHouseholdMonthlyIncome", produces = "application/json")
	public ResponseEntity<ResponseModel> getHouseholdMonthlyIncome(HttpServletRequest request, @RequestParam("clientCode") String clientCode,
			@RequestParam("device") String device, @RequestParam("langCode") String langCode, @RequestParam("noOfPeople") String noOfPeople,
			@RequestParam("serviceId") Integer serviceId) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			logger = apptService.getLogger(clientCode, "INFO");
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, logger, ipAddress);
			}
			return apptService.getHouseholdMonthlyIncome(logger, clientCode, device, langCode, noOfPeople, serviceId);
		} catch (TelAppointException e) {
			return apptService.handleException(logger, clientCode, e);
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "updateCustomerInfo", produces = "application/json")
	public ResponseEntity<ResponseModel> updateCustomerInfo(HttpServletRequest request, @RequestBody UpdateCustomerInfoRequest customerInfoRequest) {
		Logger logger = null;
		String clientCode = customerInfoRequest.getClientCode();
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			logger = apptService.getLogger(customerInfoRequest.getClientCode(), "INFO");
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(customerInfoRequest.getClientCode(), logger, ipAddress);
			}
			return apptService.updateCustomerInfo(logger, customerInfoRequest);
		} catch (TelAppointException e) {
			return apptService.handleException(logger, clientCode, e);
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "getBookedAppointments", produces = "application/json")
	public ResponseEntity<ResponseModel> getBookedAppointments(HttpServletRequest request, @RequestParam String clientCode, @RequestParam String device,
			@RequestParam String langCode, @RequestParam Long customerId) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			logger = apptService.getLogger(clientCode, "INFO");
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, logger, ipAddress);
			}
			return apptService.getBookedAppointments(logger, clientCode, device, langCode, customerId);
		} catch (TelAppointException e) {
			return apptService.handleException(logger, clientCode, e);
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}
	
	// mobile main menu list.
	@RequestMapping(method = RequestMethod.GET, value = "getExistingAppointments", produces = "application/json")
	public ResponseEntity<ResponseModel> getExistingAppointments(HttpServletRequest request, @RequestParam(value="clientCode", required=false) String clientCode , @RequestParam String device,
			@RequestParam String langCode, @RequestParam(value="customerId", required=false) Long customerId, @RequestParam String uuid) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode) && clientCode != null && !"".equals(clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
				logger = apptService.getLogger(clientCode, "INFO");
	        }
			
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, logger, ipAddress);
			}
			return apptService.getExistingAppointments(logger, clientCode, device, langCode, customerId, uuid);
		} catch (TelAppointException e) {
			return apptService.handleException(logger, clientCode, e);
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "getCustomerInfo", produces = "application/json")
	public ResponseEntity<ResponseModel> getCustomerInfo(HttpServletRequest request, @RequestParam String clientCode, @RequestParam String device,
			@RequestParam Long customerId) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			logger = apptService.getLogger(clientCode, "INFO");
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, logger, ipAddress);
			}
			return apptService.getCustomerInfo(logger, clientCode, device, customerId);
		} catch (TelAppointException e) {
			return apptService.handleException(logger, clientCode, e);
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "cancelAppointment", produces = "application/json")
	public ResponseEntity<ResponseModel> cancelAppointment(HttpServletRequest request, @RequestParam String clientCode, @RequestParam String device,
			@RequestParam String langCode, @RequestParam Long scheduleId, @RequestParam("transId") Long transId, @RequestParam(value="token", required=false) String token) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			logger = apptService.getLogger(clientCode, "INFO");
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, logger, ipAddress);
			}
			return apptService.cancelAppointment(logger, clientCode, device, langCode, scheduleId, transId, token);
		} catch (TelAppointException e) {
			return apptService.handleException(logger, clientCode, e);
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "cancelAppointmentExternalLogic", produces = "application/json")
	public ResponseEntity<ResponseModel> cancelAppointmentExternalLogic(HttpServletRequest request, @RequestParam String clientCode, @RequestParam String device,
			@RequestParam String langCode, @RequestParam Long scheduleId) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			logger = apptService.getLogger(clientCode, "INFO");
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, logger, ipAddress);
			}
			return apptService.cancelAppointmentExternalLogic(logger, clientCode, device, langCode, scheduleId);
		} catch (TelAppointException e) {
			return apptService.handleException(logger, clientCode, e);
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "releaseHoldAppointment", produces = "application/json")
	public ResponseEntity<ResponseModel> releaseHoldAppointment(HttpServletRequest request, @RequestParam String clientCode, @RequestParam String device,
			@RequestParam Long scheduleId) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			logger = apptService.getLogger(clientCode, "INFO");
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, logger, ipAddress);
			}
			return apptService.releaseHoldAppointment(logger, clientCode, device, scheduleId);
		} catch (TelAppointException e) {
			return apptService.handleException(logger, clientCode, e);
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "changeLogLevel", produces = "application/json")
	public ResponseEntity<ResponseModel> changeLogLevel(HttpServletRequest request, @RequestParam("clientCode") String clientCode,
			@RequestParam("logLevel") String logLevel) throws Exception {
		Logger logger = null;
		try {
			logger = apptService.changeLogLevel(clientCode, logLevel);
			return new ResponseEntity<ResponseModel>(apptService.populateRMDSuccessData(logger, new BaseResponse()), HttpStatus.OK);
		} catch (TelAppointException tae) {
			return apptService.handleException(logger, clientCode, tae);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "getTransId", produces = "application/json")
	public ResponseEntity<ResponseModel> getTransId(HttpServletRequest request, @RequestParam("clientCode") String clientCode, @RequestParam("device") String device,
			@RequestParam(value = "uuid", required = false) String uuid, @RequestParam(value = "ipAddress", required = false) String ipAddress,
			@RequestParam(value = "callerId", required = false) String callerId, @RequestParam(value = "userName", required = false) String userName) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			logger = apptService.getLogger(clientCode, "INFO");
			String ipAddressStr = request.getRemoteAddr();
			if (checkIP(ipAddressStr)) {
				sendEmailIPNotAllowed(clientCode, logger, ipAddress);
			}
			return apptService.getTransId(clientCode, logger, device, uuid, ipAddress, callerId, userName);
		} catch (TelAppointException tae) {
			return apptService.handleException(logger, clientCode, tae);
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "updateTransaction", produces = "application/json")
	public ResponseEntity<ResponseModel> updateTransaction(HttpServletRequest request, @RequestParam("clientCode") String clientCode,
			@RequestParam("device") String device, @RequestParam("transId") Long transId, @RequestParam("pageId") Integer pageId,
			@RequestParam(value = "scheduleId", required = false) Long scheduleId) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			logger = apptService.getLogger(clientCode, "INFO");
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, logger, ipAddress);
			}
			return apptService.updateTransaction(clientCode, logger, device, transId, pageId, scheduleId);
		} catch (TelAppointException tae) {
			return apptService.handleException(logger, clientCode, tae);
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "extendHoldTime", produces = "application/json")
	public ResponseEntity<ResponseModel> extendHoldTime(HttpServletRequest request, @RequestParam("clientCode") String clientCode,
			@RequestParam("device") String device, @RequestParam("scheduleId") Long scheduleId) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			logger = apptService.getLogger(clientCode, "INFO");
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, logger, ipAddress);
			}
			return apptService.extendHoldTime(clientCode, logger, device, scheduleId);
		} catch (TelAppointException tae) {
			return apptService.handleException(logger, clientCode, tae);
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "getAvailableDatesCallcenter", produces = "application/json")
	public ResponseEntity<ResponseModel> getAvailableDatesCallcenter(HttpServletRequest request, @RequestParam("clientCode") String clientCode,
			@RequestParam("device") String device, @RequestParam(value="langCode", required=false) String langCode, @RequestParam("locationId") Long locationId, @RequestParam("departmentId") Long departmentId,
			@RequestParam("serviceId") Long serviceId, @RequestParam("transId") Long transId,@RequestParam(value="token", required=false) String token) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			logger = apptService.getLogger(clientCode, "INFO");
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, logger, ipAddress);
			}
			 
			return apptService.getAvailableDatesCallcenter(clientCode, locationId, departmentId, serviceId, logger, device, langCode, transId);
		} catch (TelAppointException tae) {
			return apptService.handleException(logger, clientCode, tae);
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "getAvailableTimesCallcenter", produces = "application/json")
	public ResponseEntity<ResponseModel> getAvailableTimesCallcenter(HttpServletRequest request, @RequestParam("clientCode") String clientCode,
			@RequestParam("device") String device, @RequestParam(value="langCode", required=false) String langCode, @RequestParam("locationId") Long locationId, @RequestParam("departmentId") Long departmentId,
			@RequestParam("serviceId") Long serviceId, @RequestParam("availDate") String availDate, @RequestParam("transId") Long transId,@RequestParam(value="token", required=false) String token) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			logger = apptService.getLogger(clientCode, "INFO");
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, logger, ipAddress);
			}
			 
			return apptService.getAvailableTimesCallcenter(clientCode, locationId, departmentId, serviceId, availDate, logger, device, langCode, transId);
		} catch (TelAppointException tae) {
			return apptService.handleException(logger, clientCode, tae);
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "holdAppointmentCallCenter", produces = "application/json")
	public ResponseEntity<ResponseModel> holdAppointmentCallCenter(HttpServletRequest request, @RequestParam("clientCode") String clientCode,
			@RequestParam("device") String device, @RequestParam(value="langCode", required=false) String langCode, @RequestParam("locationId") Long locationId, @RequestParam("procedureId") Long procedureId,
			@RequestParam("departmentId") Long departmentId, @RequestParam("serviceId") Long serviceId, @RequestParam("customerId") Long customerId,
			@RequestParam("apptDateTime") String apptDateTime, @RequestParam("transId") Long transId,@RequestParam(value="token", required=false) String token) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			logger = apptService.getLogger(clientCode, "INFO");
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, logger, ipAddress);
			}
			 
			return apptService.holdAppointmentCallCenter(clientCode, locationId, procedureId, departmentId, serviceId, customerId, apptDateTime, logger, device, langCode, transId);
		} catch (TelAppointException tae) {
			return apptService.handleException(logger, clientCode, tae);
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "checkDuplicateAppts", produces = "application/json")
	public ResponseEntity<ResponseModel> checkDuplicateAppts(HttpServletRequest request, @RequestParam("clientCode") String clientCode,
			@RequestParam("device") String device, @RequestParam("langCode") String langCode,  @RequestParam("serviceId") Long serviceId, @RequestParam("customerId") Long customerId,
			@RequestParam("transId") Long transId) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			logger = apptService.getLogger(clientCode, "INFO");
			String ipAddress = request.getRemoteAddr();
			
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, logger, ipAddress);
			}
			 
			return apptService.checkDuplicateAppts(logger, device, langCode, clientCode, serviceId, customerId, transId);
		} catch (TelAppointException tae) {
			return apptService.handleException(logger, clientCode, tae);
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}
	
	

	@RequestMapping(method = RequestMethod.GET, value = "getPledgeHistory", produces = "application/json")
	public ResponseEntity<ResponseModel> getPledgeHistory(HttpServletRequest request, @RequestParam("clientCode") String clientCode,
			@RequestParam("device") String device, @RequestParam("langCode") String langCode, @RequestParam("customerId") Long customerId, @RequestParam("transId") Long transId) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			logger = apptService.getLogger(clientCode, "INFO");
			String ipAddress = request.getRemoteAddr();

			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, logger, ipAddress);
			}

			return apptService.getPledgeHistory(clientCode, logger, device, langCode, customerId, transId);
		} catch (TelAppointException tae) {
			return apptService.handleException(logger, clientCode, tae);
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getPledgeHistoryWithTemplate", produces = "application/json")
	public ResponseEntity<ResponseModel> getPledgeHistoryWithTemplate(HttpServletRequest request, @RequestParam("clientCode") String clientCode,
			@RequestParam("device") String device, @RequestParam("langCode") String langCode, @RequestParam("customerId") Long customerId, @RequestParam("transId") Long transId) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			logger = apptService.getLogger(clientCode, "INFO");
			String ipAddress = request.getRemoteAddr();

			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, logger, ipAddress);
			}

			return apptService.getPledgeHistoryWithTemplate(clientCode, logger, device, langCode, customerId, transId);
		} catch (TelAppointException tae) {
			return apptService.handleException(logger, clientCode, tae);
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/getPledgeAwardLetter", produces = "application/json")
	public ResponseEntity<ResponseModel> getPledgeAwardLetter(@RequestParam("clientCode") String clientCode, HttpServletRequest request,
			@RequestParam("device") String device, @RequestParam("langCode") String langCode, @RequestParam("transId") Long transId, @RequestParam("customerPledgeId") Long customerPledgeId) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			logger = apptService.getLogger(clientCode, "INFO");
			String ipAddress = request.getRemoteAddr();

			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, logger, ipAddress);
			}
			return apptService.getPledgeAwardLetter(clientCode, logger, device, langCode, transId, customerPledgeId);
		} catch (Exception tae) {
			return apptService.handleException(logger, clientCode, tae);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/getLocations", produces = "application/json")
	public ResponseEntity<ResponseModel> getLocations(@RequestParam("clientCode") String clientCode, HttpServletRequest request,
			@RequestParam("device") String device, @RequestParam("langCode") String langCode, @RequestParam("transId") Long transId) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			logger = apptService.getLogger(clientCode, "INFO");
			String ipAddress = request.getRemoteAddr();

			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, logger, ipAddress);
			}
			return apptService.getLocations(clientCode, logger, device, langCode, transId);
		} catch (Exception tae) {
			return apptService.handleException(logger, clientCode, tae);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/getServiceListByLocationId", produces = "application/json")
	public ResponseEntity<ResponseModel> getServiceListByLocationId(@RequestParam("clientCode") String clientCode, HttpServletRequest request,
			@RequestParam("device") String device, @RequestParam("langCode") String langCode, @RequestParam("transId") Long transId, @RequestParam("locationId") Integer locationId) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			logger = apptService.getLogger(clientCode, "INFO");
			String ipAddress = request.getRemoteAddr();

			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, logger, ipAddress);
			}
			return apptService.getServiceListByLocationId(clientCode, logger, device, langCode, transId, locationId);
		} catch (Exception tae) {
			return apptService.handleException(logger, clientCode, tae);
		}
	}
	
	
	@RequestMapping(method = RequestMethod.GET, value = "/getServicesCallcenter", produces = "application/json")
	public ResponseEntity<ResponseModel> getServicesCallcenter(@RequestParam("clientCode") String clientCode, HttpServletRequest request,
			@RequestParam("device") String device, @RequestParam("langCode") String langCode, @RequestParam("locationId") Integer locationId,
			@RequestParam(value="departmentId" , required=false) Integer departmentId, @RequestParam("transId") Long transId,
			@RequestParam(value="token", required=false) String token) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			logger = apptService.getLogger(clientCode, "INFO");
			String ipAddress = request.getRemoteAddr();

			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, logger, ipAddress);
			}
			return apptService.getServicesCallcenter(clientCode, logger, device, langCode, locationId, departmentId, transId);
		} catch (Exception tae) {
			return apptService.handleException(logger, clientCode, tae);
		}
	}
	
	
	@RequestMapping(method = RequestMethod.POST, value = "/saveTransScriptMsg", produces = "application/json", consumes="application/json")
	public ResponseEntity<ResponseModel> saveTransScriptMsg(HttpServletRequest request, @RequestBody TransScriptRequest transScriptReq) {
		Logger logger = null;
		String clientCode = transScriptReq.getClientCode();
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			logger = apptService.getLogger(transScriptReq.getClientCode(), "INFO");
			String ipAddress = request.getRemoteAddr();

			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(transScriptReq.getClientCode(), logger, ipAddress);
			}
			return apptService.saveTransScriptMsg(logger, transScriptReq);
		} catch (Exception tae) {
			return apptService.handleException(logger, transScriptReq.getClientCode(), tae);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/deleteTransScriptMsg", produces = "application/json")
	public ResponseEntity<ResponseModel> deleteTransScriptMsg(HttpServletRequest request, @RequestParam String clientCode, @RequestParam long transScriptMsgId) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			logger = apptService.getLogger(clientCode, "INFO");
			String ipAddress = request.getRemoteAddr();

			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, logger, ipAddress);
			}
			return apptService.deleteTransScriptMsg(logger, clientCode, transScriptMsgId);
		} catch (Exception tae) {
			return apptService.handleException(logger, clientCode, tae);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/processCreateAndCancelEnrollments", produces = "application/json")
	public ResponseEntity<ResponseModel> processCreateAndCancelEnrollments(HttpServletRequest request, @RequestParam("clientCode") String clientCode) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			logger = apptService.getLogger(clientCode, "INFO");
			String ipAddress = request.getRemoteAddr();

			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, logger, ipAddress);
			}
			return apptService.processPendingCreateAndCancelEnrollments(logger, clientCode);
		} catch (Exception tae) {
			return apptService.handleException(logger, clientCode, tae);
		}
	}
	
	
	@RequestMapping(method = RequestMethod.GET, value = "getClientDirectAccessNumbers", produces = "application/json")
	public ResponseEntity<ResponseModel> getClientDirectAccessNumbers(HttpServletRequest request) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			return apptService.getClientDirectAccessNumbers(logger);
		} catch (Exception e) {
			return apptService.handleException(logger, null, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "getLang", produces = "application/json")
	public ResponseEntity<ResponseModel> getLang(HttpServletRequest request, @RequestParam("clientCode") String clientCode, @RequestParam String langCode) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if (isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);
			}
			logger = apptService.getLogger(clientCode, "INFO");
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, logger, ipAddress);
			}
			return apptService.getLang(logger, clientCode, CommonApptDeskConstants.IVRAUDIO.getValue(), langCode);
		} catch (TelAppointException e) {
			return apptService.handleException(logger, clientCode, e);
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getFirstAvailableDateAnyLocation", produces = "application/json")
	public ResponseEntity<ResponseModel> getFirstAvailableDateAnyLocation(HttpServletRequest request, @RequestParam("clientCode") String clientCode,
			@RequestParam String langCode, @RequestParam String device) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if (isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);
			}
			logger = apptService.getLogger(clientCode, "INFO");
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, logger, ipAddress);
			}
			return apptService.getFirstAvailableDateAnyLocation(logger, clientCode, device, langCode);
		} catch (TelAppointException e) {
			return apptService.handleException(logger, clientCode, e);
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}
	
	//iphone apis - start
	@RequestMapping(method = RequestMethod.GET, value = "getMobileDemoClientList", produces = "application/json")
	public ResponseEntity<ResponseModel> getMobileDemoClientList(@RequestHeader HttpHeaders httpHeader) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			return apptService.getMobileDemoClientList(httpHeader, logger);
		} catch (Exception e) {
			return apptService.handleException(logger, "Master", e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getClientListBySearchKey", produces = "application/json")
	public ResponseEntity<ResponseModel> getClientListBySearchKey(@RequestHeader HttpHeaders httpHeader, @RequestParam String uuid, @RequestParam String searchKey) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			return apptService.getClientListBySearchKey(httpHeader, logger, uuid, searchKey);
		} catch (Exception e) {
			return apptService.handleException(logger, "Master", e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getClientListByClientCode", produces = "application/json")
	public ResponseEntity<ResponseModel> getClientListByClientCode(@RequestHeader HttpHeaders httpHeader, @RequestParam String uuid, @RequestParam String clientCode) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			return apptService.getClientListByClientCode(httpHeader, logger, uuid, clientCode);
		} catch (Exception e) {
			return apptService.handleException(logger, "Master", e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getClientListByMobileCode", produces = "application/json")
	public ResponseEntity<ResponseModel> getClientListByMobileCode(@RequestHeader HttpHeaders httpHeader, @RequestParam String uuid, @RequestParam String mobileCode) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			return apptService.getClientListByMobileCode(httpHeader, logger, uuid, mobileCode);
		} catch (Exception e) {
			return apptService.handleException(logger, "Master", e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getLanguageList", produces = "application/json")
	public ResponseEntity<ResponseModel> getLanguageList(@RequestHeader HttpHeaders httpHeader, @RequestParam String uuid, @RequestParam String clientCode) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			return apptService.getLanguageList(httpHeader, logger, uuid, clientCode);
		} catch (Exception e) {
			return apptService.handleException(logger, "MasterDB", e);
		}
	}
	
	@Deprecated
	@RequestMapping(method = RequestMethod.GET, value = "getMobileHomePageInfo ", produces = "application/json")
	public ResponseEntity<ResponseModel> getMobileHomePageInfo (@RequestParam String langCode) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			return apptService.getMobileHomePageInfo(logger, langCode);
		} catch (Exception e) {
			return apptService.handleException(logger, "MasterDB", e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getMobileHomePage ", produces = "application/json")
	public ResponseEntity<ResponseModel> getMobileHomePage (@RequestParam String langCode,@RequestParam String uuid) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			return apptService.getMobileHomePage(logger, langCode, uuid);
		} catch (Exception e) {
			return apptService.handleException(logger, "MasterDB", e);
		}
	}
	
	@Deprecated
	@RequestMapping(method = RequestMethod.GET, value = "getMobileHomePageInitial", produces = "application/json")
	public ResponseEntity<ResponseModel> getMobileHomePageInitial(@RequestParam String langCode) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			return apptService.getMobileHomePageInitial(logger, langCode);
		} catch (Exception e) {
			return apptService.handleException(logger, "MasterDb", e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getSearchFieldInitValue", produces = "application/json")
	public ResponseEntity<ResponseModel> getSearchFieldInitValue(@RequestParam String langCode) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			return apptService.getSearchFieldInitValue(logger, langCode);
		} catch (Exception e) {
			return apptService.handleException(logger, "MasterDb", e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getMobileMenuItems", produces = "application/json")
	public ResponseEntity<ResponseModel> getMobileMenuItems(@RequestParam String langCode, @RequestParam String uuid) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			return apptService.getMobileMenuItems(logger, langCode, uuid);
		} catch (Exception e) {
			return apptService.handleException(logger, "MasterDb", e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getIconAndButtonLabels", produces = "application/json")
	public ResponseEntity<ResponseModel> getIconAndButtonLabels(@RequestParam String clientCode, @RequestParam String langCode, @RequestParam("transId") Long transId, @RequestParam String token) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			logger = apptService.getLogger(clientCode, "INFO");
			return apptService.getIconAndButtonLabels(logger, clientCode, langCode, token);
		} catch (Exception e) {
			return apptService.handleException(logger, "MasterDb", e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getMobileApptsFeaturesHomePage", produces = "application/json")
	public ResponseEntity<ResponseModel> getMobileApptsFeaturesHomePage(@RequestParam String clientCode, @RequestParam String langCode, @RequestParam("transId") Long transId,@RequestParam String token) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			logger = apptService.getLogger(clientCode, "INFO");
			return apptService.getMobileApptsFeaturesHomePage(logger, clientCode, langCode, token);
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}
	
	// only for mobile.
	@RequestMapping(method = RequestMethod.GET, value = "getBasicLocations", produces = "application/json")
	public ResponseEntity<ResponseModel> getBasicLocations(@RequestParam String clientCode, @RequestParam String langCode, @RequestParam("transId") Long transId,@RequestParam String token) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			logger = apptService.getLogger(clientCode, "INFO");
			return apptService.getLocations(logger, clientCode, langCode, token);
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getClientInfo", produces = "application/json")
	public ResponseEntity<ResponseModel> getClientInfo(@RequestParam String clientCode,  @RequestParam String device, @RequestParam String langCode,@RequestParam("transId") Long transId) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			logger = apptService.getLogger(clientCode, "INFO");
			return apptService.getClientInfo(logger, clientCode, device, langCode);
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getLocResSerInfo", produces = "application/json")
	public ResponseEntity<ResponseModel> getLocResSerInfo(@RequestParam String clientCode, @RequestParam String device,  @RequestParam String langCode, @RequestParam("transId") Long transId,@RequestParam String token) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			logger = apptService.getLogger(clientCode, "INFO");
			return apptService.getLocResSerInfo(logger, clientCode, device, langCode, token);
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getCompanyList", produces = "application/json")
	public ResponseEntity<ResponseModel> getCompanyList(@RequestParam String clientCode, @RequestParam String device, @RequestParam String langCode,
			@RequestParam("transId") Long transId,@RequestParam String token) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			logger = apptService.getLogger(clientCode, "INFO");
			return apptService.getCompanyList(logger, clientCode, device, langCode, token);
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getProcedureList", produces = "application/json")
	public ResponseEntity<ResponseModel> getProcedureList(@RequestParam String clientCode, @RequestParam String device, @RequestParam String langCode,
			@RequestParam String companyId, @RequestParam("transId") Long transId,@RequestParam String token) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			logger = apptService.getLogger(clientCode, "INFO");
			return apptService.getProcedureList(logger, clientCode, device, langCode, companyId, token);
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getLocationList", produces = "application/json")
	public ResponseEntity<ResponseModel> getLocationList(@RequestParam String clientCode, @RequestParam String device, @RequestParam String langCode,
			@RequestParam String procedureId, @RequestParam("transId") Long transId,@RequestParam String token) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			logger = apptService.getLogger(clientCode, "INFO");
			return apptService.getLocationList(logger, clientCode, device, langCode, procedureId, token);
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getDepartmentList", produces = "application/json")
	public ResponseEntity<ResponseModel> getDepartmentList(@RequestParam String clientCode, @RequestParam String device, @RequestParam String langCode,
			@RequestParam String locationId,@RequestParam("transId") Long transId, @RequestParam String token) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			logger = apptService.getLogger(clientCode, "INFO");
			return apptService.getDepartmentList(logger, clientCode, device, langCode, locationId, token);
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getResourceList", produces = "application/json")
	public ResponseEntity<ResponseModel> getResourceList(@RequestParam String clientCode, @RequestParam String device, @RequestParam String langCode,
			@RequestParam String locationId,@RequestParam String departmentId, @RequestParam("transId") Long transId,@RequestParam String token) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			logger = apptService.getLogger(clientCode, "INFO");
			return apptService.getResourceList(logger, clientCode, device, langCode, locationId,departmentId, token);
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getServiceList", produces = "application/json")
	public ResponseEntity<ResponseModel> getServiceList(@RequestParam String clientCode, @RequestParam String device, @RequestParam String langCode,
			@RequestParam Integer locationId, @RequestParam Integer resourceId,@RequestParam Integer departmentId, @RequestParam("transId") Long transId, @RequestParam String token) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			logger = apptService.getLogger(clientCode, "INFO");
			return apptService.getServiceList(logger, clientCode, device, langCode, locationId, resourceId, departmentId, token);
			
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}
	
	
	
	@RequestMapping(method = RequestMethod.GET, value = "getMessages", produces = "application/json")
	public ResponseEntity<ResponseModel> getMessages(@RequestParam String clientCode, @RequestParam String langCode,
			@RequestParam("transId") Long transId,@RequestParam String token) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			logger = apptService.getLogger(clientCode, "INFO");
			return apptService.getMessages(logger, clientCode, langCode, token);
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getListOfDocsTOBring", produces = "application/json")
	public ResponseEntity<ResponseModel> getListOfDocsTOBring(@RequestParam String clientCode, @RequestParam String langCode,
			@RequestParam Integer serviceId, @RequestParam("transId") Long transId,@RequestParam String token) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			logger = apptService.getLogger(clientCode, "INFO");;
			return apptService.getListOfDocsTOBring(logger, clientCode, langCode, serviceId, token);
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getAvailableDates", produces = "application/json")
	public ResponseEntity<ResponseModel> getAvailableDates(@RequestParam String clientCode, @RequestParam String langCode, @RequestParam(value="device", required=false) String device,
			    @RequestParam("locationId") Long locationId, @RequestParam("departmentId") Long departmentId,
			    @RequestParam("resourceId") Long resourceId, @RequestParam("serviceId") Long serviceId, @RequestParam("transId") Long transId,@RequestParam String token) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			logger = apptService.getLogger(clientCode, "INFO");;
			return apptService.getAvailableDates(logger, clientCode, langCode, device, locationId, departmentId, resourceId, serviceId, token);
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getAvailableTimes", produces = "application/json")
	public ResponseEntity<ResponseModel> getAvailableTimes(HttpServletRequest request, @RequestParam("clientCode") String clientCode,
			@RequestParam(value="device", required=false) String device,
			@RequestParam(value="langCode", required=false) String langCode, @RequestParam("locationId") Long locationId, @RequestParam("departmentId") Long departmentId,
			@RequestParam("resourceId") Long resourceId,
			@RequestParam("serviceId") Long serviceId, @RequestParam("availDate") String availDate,@RequestParam("transId") Long transId,@RequestParam String token) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			logger = apptService.getLogger(clientCode, "INFO");
			return apptService.getAvailableTimes(clientCode, device, locationId, departmentId, resourceId, serviceId, availDate, logger, langCode, token);
		} catch (TelAppointException tae) {
			return apptService.handleException(logger, clientCode, tae);
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getMobilePageFields", produces = "application/json")
	public ResponseEntity<ResponseModel> getMobilePageFields(HttpServletRequest request, @RequestParam("clientCode") String clientCode,
			@RequestParam("langCode") String langCode, @RequestParam("transId") Long transId,@RequestParam String token) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			
			logger = apptService.getLogger(clientCode, "INFO");
			String device = CommonApptDeskConstants.MOBILE.getValue();
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, logger, ipAddress);
			}
			return apptService.getMobilePageFields(logger, clientCode, langCode, device);
		} catch (TelAppointException e) {
			return apptService.handleException(logger, clientCode, e);
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getMobilePageFieldsForCancelAppts", produces = "application/json")
	public ResponseEntity<ResponseModel> getMobilePageFieldsForCancelAppts(HttpServletRequest request, @RequestParam("clientCode") String clientCode,
			@RequestParam("langCode") String langCode, @RequestParam("transId") Long transId,@RequestParam String token) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			
			logger = apptService.getLogger(clientCode, "INFO");
			String device = CommonApptDeskConstants.MOBILE.getValue();
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, logger, ipAddress);
			}
			return apptService.getMobilePageFieldsForCancelAppts(logger, clientCode, langCode, device);
		} catch (TelAppointException e) {
			return apptService.handleException(logger, clientCode, e);
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "loginMobileAuthenticate", produces = "application/json", consumes = "application/json")
	public ResponseEntity<ResponseModel> loginMobileAuthenticate(HttpServletRequest request, @RequestBody CustomerInfoRequest customerInfoRequest) {
		String clientCode = customerInfoRequest.getClientCode();
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			logger = apptService.getLogger(customerInfoRequest.getClientCode(), "INFO");
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(customerInfoRequest.getClientCode(), logger, ipAddress);
			}
			return apptService.loginMobileAuthenticate(logger, customerInfoRequest);
		} catch (TelAppointException e) {
			return apptService.handleException(logger, clientCode, e);
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "holdAppointment", produces = "application/json")
	public ResponseEntity<ResponseModel> holdAppointment(HttpServletRequest request, @RequestParam("clientCode") String clientCode,
			@RequestParam("device") String device, @RequestParam(value="langCode", required=false) String langCode, @RequestParam("locationId") Long locationId, @RequestParam("procedureId") Long procedureId,
			@RequestParam("departmentId") Long departmentId, @RequestParam("resourceId") Long resourceId, @RequestParam("serviceId") Long serviceId, @RequestParam("customerId") Long customerId,
			@RequestParam("apptDateTime") String apptDateTime, @RequestParam("transId") Long transId, @RequestParam("token") String token) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			logger = apptService.getLogger(clientCode, "INFO");
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, logger, ipAddress);
			}
			 
			return apptService.holdAppointment(clientCode, locationId, procedureId, departmentId, resourceId, serviceId, customerId, apptDateTime, logger, device, langCode, transId, token);
		} catch (TelAppointException tae) {
			return apptService.handleException(logger, clientCode, tae);
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "bookAppointment", produces = "application/json")
	public ResponseEntity<ResponseModel> bookAppointment(HttpServletRequest request, @RequestBody ConfirmAppointmentRequest confirmAppointmentRequest) {
		String clientCode = confirmAppointmentRequest.getClientCode();
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			logger = apptService.getLogger(confirmAppointmentRequest.getClientCode(), "INFO");
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(confirmAppointmentRequest.getClientCode(), logger, ipAddress);
			}
			return apptService.bookAppointment(logger, confirmAppointmentRequest);
		} catch (TelAppointException e) {
			return apptService.handleException(logger, clientCode, e);
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getVerifyPageData", produces = "application/json")
	public ResponseEntity<ResponseModel> getVerifyPageData(HttpServletRequest request, @RequestParam String clientCode,@RequestParam("device") String device, @RequestParam String langCode, 
			@RequestParam Long customerId, @RequestParam Long scheduleId, @RequestParam("transId") Long transId,@RequestParam String token) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			logger = apptService.getLogger(clientCode, "INFO");
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, logger, ipAddress);
			}
			return apptService.getVerifyPageData(logger, clientCode, device, langCode, customerId, scheduleId, token);
		} catch (TelAppointException e) {
			return apptService.handleException(logger, clientCode, e);
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "updateLocResSer ", produces = "application/json")
	public ResponseEntity<ResponseModel> updateLocResSer (HttpServletRequest request, @RequestParam("clientCode") String clientCode,@RequestParam("uuid") String uuid, @RequestParam Long locationId,
			@RequestParam Long resourceId, @RequestParam Long serviceId) {
		
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			logger = apptService.getLogger(clientCode, "INFO");
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, logger, ipAddress);
			}
			return apptService.updateLocResSer(logger, clientCode, uuid, locationId, resourceId, serviceId);
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getPersonalInfo", produces = "application/json")
	public ResponseEntity<ResponseModel> getPersonalInfo(HttpServletRequest request, @RequestParam String clientCode, @RequestParam String device,@RequestParam String langCode,
			@RequestParam Long customerId) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			logger = apptService.getLogger(clientCode, "INFO");
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, logger, ipAddress);
			}
			return apptService.getPersonalInfo(logger, clientCode, device, langCode, customerId);
		} catch (TelAppointException e) {
			return apptService.handleException(logger, clientCode, e);
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getMyAccountDetails", produces = "application/json")
	public ResponseEntity<ResponseModel> getMyAccountDetails(@RequestParam String device, @RequestParam String uuid) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			return apptService.getMyAccountDetails(logger, device, uuid);
		} catch (Exception e) {
			return apptService.handleException(logger, "Master", e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getMyProvidersList", produces = "application/json")
	public ResponseEntity<ResponseModel> getMyProvidersList(@RequestHeader HttpHeaders httpHeader, @RequestParam String device, @RequestParam String uuid) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			return apptService.getMyProvidersList(httpHeader, logger, device, uuid);
		} catch (Exception e) {
			return apptService.handleException(logger, "Master", e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getMyProvidesLocResSerList", produces = "application/json")
	public ResponseEntity<ResponseModel> getMyProvidesLocResSerList(@RequestHeader HttpHeaders httpHeader, @RequestParam String device, @RequestParam String uuid) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			return apptService.getMyProvidesLocResSerList(httpHeader, logger, device, uuid);
		} catch (Exception e) {
			return apptService.handleException(logger, "Master", e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "addMyProvidersList", produces = "application/json")
	public ResponseEntity<ResponseModel> addMyProvidersList(@RequestHeader HttpHeaders httpHeader, @RequestParam String clientCode, @RequestParam String device, @RequestParam String uuid) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			return apptService.addMyProvidersList(httpHeader, logger, clientCode, device, uuid);
		} catch (Exception e) {
			return apptService.handleException(logger, "Master", e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "deleteProvidersList", produces = "application/json")
	public ResponseEntity<ResponseModel> deleteProvidersList(@RequestHeader HttpHeaders httpHeader, @RequestParam String clientCode, @RequestParam String device, @RequestParam String uuid) {
		Logger logger = null;
		try {
			logger = getDefaultLog();
			return apptService.deleteProvidersList(httpHeader, logger, clientCode, device, uuid);
		} catch (Exception e) {
			return apptService.handleException(logger, "Master", e); 	
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "loginAuthenticateAndUpdate", produces = "application/json", consumes="application/json")
	public ResponseEntity<ResponseModel> loginAuthenticateAndUpdate(HttpServletRequest request, @RequestBody CustomerInfoRequest customerInfoRequest) {
		String clientCode = customerInfoRequest.getClientCode();
		Logger logger = null;
		try {
			logger = getDefaultLog();
			if(isClientCodeNullEmptyNotAllowed(logger, clientCode)) {
				sendEmailClientNotAllowed(clientCode, logger);   
	        }
			logger = apptService.getLogger(customerInfoRequest.getClientCode(), "INFO");
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(customerInfoRequest.getClientCode(), logger, ipAddress);
			}
			return apptService.loginAuthenticateAndUpdate(logger, customerInfoRequest);
		} catch (TelAppointException e) {
			return apptService.handleException(logger, clientCode, e);
		} catch (Exception e) {
			return apptService.handleException(logger, clientCode, e);
		}
	}
	
	public void sendEmailIPNotAllowed(String clientCode, Logger logger, String ipaddress) throws TelAppointException {
		throw new TelAppointException(ErrorConstants.ERROR_2995.getCode(), ErrorConstants.ERROR_2995.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, "IP Not Allowed:" + ipaddress,
				null);
	}
	
	public void sendEmailClientNotAllowed(String clientCode, Logger logger) throws TelAppointException {
		throw new TelAppointException(ErrorConstants.ERROR_3001.getCode(), ErrorConstants.ERROR_3001.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, "ClientCode Not Allowed:" + clientCode,
				null);
	}

	public Logger getDefaultLog() throws TelAppointException, Exception {
		Logger asynchLogger = null;
		try {
			String logFileLocation = PropertyUtils.getValueFromProperties("LOG_LOCATION_PATH", PropertiesConstants.APPT_SERVICE_REST_WS_PROP.getPropertyFileName());
			if (LoggerPool.getHandleToLogger("defaultApptServiceLog") == null) {
				asynchLogger = (new AsynchLogger(logFileLocation + "/defaultApptServiceLog.log", Level.INFO)).getLogger("defaultApptServiceLog");
				LoggerPool.addLogger("defaultApptServiceLog", asynchLogger);
			}
			return LoggerPool.getHandleToLogger("defaultApptServiceLog");
		} catch (IOException ioe) {
			throw new TelAppointException(ErrorConstants.ERROR_2999.getCode(), ErrorConstants.ERROR_2999.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, ioe.getMessage(), null);
		}
	}

	private final String iptoCheck = "127.0.0.1";
	public boolean checkIP(String ipAddress) throws TelAppointException, Exception {
		try {
			String allowAnyIp = PropertyUtils.getValueFromProperties("ALLOW_ANY_IP", PropertiesConstants.APPT_SERVICE_REST_WS_PROP.getPropertyFileName());
			if ("true".equals(allowAnyIp)) {
				return false;
			}
			return !ipAddress.equals(iptoCheck);
		} catch (IOException ioe) {
			throw new TelAppointException(ErrorConstants.ERROR_3000.getCode(), ErrorConstants.ERROR_3000.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, ioe.getMessage(), null);
		}
	}
	
	private boolean isClientCodeNullEmptyNotAllowed(Logger logger, String clientCode) throws Exception {
		if(clientCode == null || "".equals(clientCode)) {
			return true;
		}
		if(clientCode.length() > 8) {
			return true;
		}
		if(!CoreUtils.allowOnlyAlphanumeric(clientCode)) {
			return true;
		}
		return apptService.isClientAllowed(logger, clientCode)?false:true;
	}
	
	
	
}
