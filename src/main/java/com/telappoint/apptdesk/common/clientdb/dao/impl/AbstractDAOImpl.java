package com.telappoint.apptdesk.common.clientdb.dao.impl;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.telappoint.apptdesk.common.constants.AppointmentStatus;
import com.telappoint.apptdesk.common.constants.ErrorConstants;
import com.telappoint.apptdesk.common.model.ClientDeploymentConfig;
import com.telappoint.apptdesk.common.model.FirstAvailableDateTime;
import com.telappoint.apptdesk.common.model.JdbcCustomTemplate;
import com.telappoint.apptdesk.common.model.TempHoldAppt;
import com.telappoint.apptdesk.common.sp.constants.SPConstants;
import com.telappoint.apptdesk.common.utils.CoreUtils;
import com.telappoint.apptdesk.common.utils.DateUtils;
import com.telappoint.apptdesk.handlers.exception.TelAppointException;
import com.telappoint.apptdesk.model.ApptSysConfig;
import com.telappoint.apptdesk.model.AuthResponse;
import com.telappoint.apptdesk.model.CancelAppointResponse;
import com.telappoint.apptdesk.model.ConfirmAppointmentResponse;
import com.telappoint.apptdesk.model.Customer;
import com.telappoint.apptdesk.model.FirstAvailableDateAnyLocationResponse;
import com.telappoint.apptdesk.model.HoldAppointmentRequest;
import com.telappoint.apptdesk.model.HoldAppointmentResponse;
import com.telappoint.apptdesk.model.Location;
import com.telappoint.apptdesk.model.OnlinePageFields;
import com.telappoint.apptdesk.model.Options;
import com.telappoint.apptdesk.model.Service;

/**
 * 
 * @author Balaji N
 *
 */
public abstract class AbstractDAOImpl {
	public ApptSysConfig getApptSysConfig(JdbcCustomTemplate jdbcCustomTemplate, Logger logger) throws TelAppointException, Exception {
		StringBuilder sql = new StringBuilder("select max_appt_duration_days,display_company,display_procedure,display_location,display_department,display_resource,display_service,");
		sql.append(" login_first,enforce_login,send_conf_email,send_cancel_email,display_comments, comments_rows, comments_cols,");
		sql.append("scheduler_closed, no_funding,run_phone_type_lookup, ");
		sql.append("appt_delay_time_days, appt_delay_time_hrs, restrict_appt_window, appt_start_date, appt_end_date,");
		sql.append("restrict_loc_appt_window, restrict_ser_appt_window, restrict_loc_ser_appt_window, online_datetime_display,send_reschd_email,cc_confirm_email,cc_cancel_email, allow_any_resource, check_assigned_resource ");
		sql.append(" from appt_sys_config ");

		final ApptSysConfig apptSysConfig = new ApptSysConfig();
		try {
			return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new ResultSetExtractor<ApptSysConfig>() {
				@Override
				public ApptSysConfig extractData(ResultSet rs) throws SQLException, DataAccessException {
					while (rs.next()) {
						apptSysConfig.setDisplayCompany(rs.getString("display_company"));
						apptSysConfig.setDisplayProcedure(rs.getString("display_procedure"));
						apptSysConfig.setDisplayLocation(rs.getString("display_location"));
						apptSysConfig.setDisplayDepartment(rs.getString("display_department"));
						apptSysConfig.setDisplayResource(rs.getString("display_resource"));
						apptSysConfig.setDisplayService(rs.getString("display_service"));
						apptSysConfig.setLoginFirst(rs.getString("login_first"));
						apptSysConfig.setEnforceLogin(rs.getString("enforce_login"));
						apptSysConfig.setSendConfirmEmail(rs.getString("send_conf_email"));
						apptSysConfig.setSendCancelEmail(rs.getString("send_cancel_email"));
						apptSysConfig.setSchedulerClosed(rs.getString("scheduler_closed"));
						apptSysConfig.setNoFunding(rs.getString("no_funding"));
						apptSysConfig.setDisplayComments(rs.getString("display_comments"));
						apptSysConfig.setCommentsNoOfCols(Integer.toString(rs.getInt("comments_cols")));
						apptSysConfig.setCommentsNoOfRows(Integer.toString(rs.getInt("comments_rows")));
						apptSysConfig.setMaxApptDurationDays(rs.getInt("max_appt_duration_days"));
						apptSysConfig.setRunPhoneTypeLookup(rs.getString("run_phone_type_lookup"));
						apptSysConfig.setApptDelayTimeDays(rs.getInt("appt_delay_time_days"));
						apptSysConfig.setApptDelayTimeHrs(rs.getInt("appt_delay_time_hrs"));
						apptSysConfig.setRestrictApptWindow(rs.getString("restrict_appt_window"));
						apptSysConfig.setApptStartDate(rs.getString("appt_start_date"));
						apptSysConfig.setApptEndDate(rs.getString("appt_end_date"));
						apptSysConfig.setRestrictLocApptWindow(rs.getString("restrict_loc_appt_window"));
						apptSysConfig.setRestrictLocSerApptWindow(rs.getString("restrict_loc_ser_appt_window"));
						apptSysConfig.setRestrictSerApptWindow(rs.getString("restrict_ser_appt_window"));
						apptSysConfig.setOnlineDateTimeFormat(rs.getString("online_datetime_display"));
						apptSysConfig.setCcConfirmEmails(rs.getString("cc_confirm_email"));
						apptSysConfig.setCcCancalEmails(rs.getString("cc_cancel_email"));
						apptSysConfig.setSendReschdEmail(rs.getString("send_reschd_email"));
						apptSysConfig.setAllowAnyDoctor(rs.getString("allow_any_resource"));
						apptSysConfig.setCheckAssignedResource(rs.getString("check_assigned_resource"));
						
						
					}
					return apptSysConfig;
				}
			});
		} catch(DataAccessException dae) {
			throw new TelAppointException(ErrorConstants.ERROR_1016.getCode(), ErrorConstants.ERROR_1016.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,dae.getMessage(), null);
		}
	}
	
	public void getApptFirstAvailDateTimeAnyLocation(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String device, ClientDeploymentConfig cdConfig, FirstAvailableDateAnyLocationResponse firstAvailDateAnyLoc) throws TelAppointException, Exception {
		try {
			String spName = "get_first_avail_date_times_any_location_sp";
			SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcCustomTemplate.getJdbcTemplate()).withProcedureName(spName);
			StringBuilder inputParams = new StringBuilder("get_first_avail_date_times_any_location_sp input values: ");
			inputParams.append("TimeZone:").append(cdConfig.getTimeZone()).append(",");
			logger.info(inputParams.toString());
			
			Map<String,Object> inParameters = new HashMap<String,Object>();
			inParameters.put(SPConstants.TIME_ZONE.getValue(), cdConfig.getTimeZone());
			long startTime = System.currentTimeMillis();
			Map<String, Object> result = simpleJdbcCall.execute(inParameters);
			long endTime = System.currentTimeMillis();
			logTimeTaken(spName, startTime,endTime);
			
			Object response = result.get(SPConstants.RESULT_STR.getValue());
			Object errorMsg = result.get(SPConstants.ERROR_MESSAGE.getValue());
			if(response != null && errorMsg == null) {
				String resultStr = (String)response;
				String resultStrArray[] = resultStr.split("\\|");
				int i=0;
				if(resultStrArray.length == 16) {
					// DBDateTime|displayDateTime|ivrDate|ivrTime|locationname|address|city|zip|locationIVRAudio|locationIVRTts|resourceId|serviceId|serviceName|serviceTts|serviceAudio
					Location location = firstAvailDateAnyLoc.getLocation();
					Service service = firstAvailDateAnyLoc.getService();
					if(CoreUtils.isOnline(device)) {
						String dbDateTime = resultStrArray[i++];
						String dbDateTimeArray[] = dbDateTime.split(" ");
						if(dbDateTimeArray.length == 2) {
							firstAvailDateAnyLoc.setDate(dbDateTimeArray[0]);
							firstAvailDateAnyLoc.setTime(dbDateTimeArray[1]);
						} else {
							firstAvailDateAnyLoc.setDate("");
							firstAvailDateAnyLoc.setTime("");
							logger.error(device+" dateTime format is wrong! - "+dbDateTime);
						}
						firstAvailDateAnyLoc.setDisplayDateTime(resultStrArray[i++]);
						i=i+2;
						location.setLocationId(Integer.valueOf(resultStrArray[i++]));
						location.setLocationName(resultStrArray[i++]);
						location.setAddress(resultStrArray[i++]);
						location.setCity(resultStrArray[i++]);
						location.setZip(resultStrArray[i++]);
						i = i + 3; // skip location ivr audit and tts and resource_id
						logger.info("resourceId: "+resultStrArray[i-1]);
						System.out.println("resourceId: "+resultStrArray[i-1]);
						service.setServiceId(Integer.valueOf(resultStrArray[i++]));
						service.setServiceNameOnline(resultStrArray[i++]);
						i = i + 2;
					} else if (CoreUtils.isIVR(device)) {
						i=i+2;
						firstAvailDateAnyLoc.setDate(resultStrArray[i++]);
						firstAvailDateAnyLoc.setTime(resultStrArray[i++]);
						location.setLocationId(Integer.valueOf(resultStrArray[i++]));
						location.setLocationName(resultStrArray[i++]);
						i = i + 3; 
						location.setLocationNameIvrTts(resultStrArray[i++]);
						location.setLocationNameIvrAudio(resultStrArray[i++]);
						i=i+1;
						service.setServiceId(Integer.valueOf(resultStrArray[i++]));
						service.setServiceNameOnline(resultStrArray[i++]);
						service.setServiceNameIvrTts(resultStrArray[i++]);
						service.setServiceNameIvrAudio(resultStrArray[i++]);
						
					} else {
						throw new Exception("Pass the device name - online / ivr");
					}
					location.setEnable(null);
					firstAvailDateAnyLoc.setLocation(location);
					firstAvailDateAnyLoc.setService(service);
				}
			} else {
				logger.error("Error response recieved from get_first_avail_date_times_any_location_sp");
				firstAvailDateAnyLoc.setStatus(false);
				firstAvailDateAnyLoc.setMessage("System error");
			}
		} catch(DataAccessException dae) {
			throw new TelAppointException(ErrorConstants.ERROR_1080.getCode(), ErrorConstants.ERROR_1080.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,dae.getMessage(), null);
		}
	}
	
	public void logTimeTaken(String spName, long startTime, long endTime) {
		long timeTaken = (endTime - startTime)/1000;
		if(timeTaken >= 3) {
			System.out.println(spName+" Time token:: "+timeTaken);
		}
	}

	public FirstAvailableDateTime getApptFirstAvailDateTimeCallCenter(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, HoldAppointmentRequest holdAppt, ClientDeploymentConfig cdConfig) throws TelAppointException, Exception {
		try {
			String spName = "get_first_avail_date_times_callcenter_sp";
			SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcCustomTemplate.getJdbcTemplate()).withProcedureName(spName);
			StringBuilder inputParams = new StringBuilder("get_first_avail_date_times_sp input values: ");
			inputParams.append("TimeZone:").append(cdConfig.getTimeZone()).append(",");
			inputParams.append("LocationId:").append(holdAppt.getLocationId()).append(",");
			inputParams.append("departmentId:").append(holdAppt.getDepartmentId() == null?1:holdAppt.getDepartmentId()).append(",");
			inputParams.append("serviceId:").append(holdAppt.getServiceId()).append(",");
			inputParams.append("BlockTimeInMins:").append(cdConfig.getBlockTimeInMins());
			logger.info(inputParams.toString());
			
			Map<String,Object> inParameters = new HashMap<String,Object>();
			inParameters.put(SPConstants.TIME_ZONE.getValue(), cdConfig.getTimeZone());
			inParameters.put(SPConstants.LOCATION_ID.getValue(), holdAppt.getLocationId());
			inParameters.put(SPConstants.DEPARTMENT_ID.getValue(), holdAppt.getDepartmentId() == null?1:holdAppt.getDepartmentId());
			inParameters.put(SPConstants.SERVICE_ID.getValue(), holdAppt.getServiceId());
			inParameters.put(SPConstants.BLOCK_TIME_IN_MINS.getValue(), cdConfig.getBlockTimeInMins());
			long startTime = System.currentTimeMillis();
			Map<String, Object> result = simpleJdbcCall.execute(inParameters);
			long endTime = System.currentTimeMillis();
			logTimeTaken(spName, startTime, endTime);
			
			FirstAvailableDateTime firstAvailableDateTime = new FirstAvailableDateTime();
			Object response = result.get(SPConstants.AVAILABLE_DATE_TIMES.getValue());
			Object errorMsg = result.get(SPConstants.ERROR_MESSAGE.getValue());
			firstAvailableDateTime.setAvailableDateTime((response!=null)?(String)response:"");
			firstAvailableDateTime.setErrorMessage((errorMsg!=null)?(String)errorMsg:"");
			return firstAvailableDateTime;
		} catch(DataAccessException dae) {
			throw new TelAppointException(ErrorConstants.ERROR_1054.getCode(), ErrorConstants.ERROR_1054.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,dae.getMessage(), holdAppt.toString());
		}
	}
	
	public TempHoldAppt holdAppt(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, Integer resourceId, String dateTime, Integer serviceId, Long customerId, ClientDeploymentConfig cdConfig) throws TelAppointException, Exception {		
		StringBuilder inputParams = new StringBuilder("temp_hold_appointment_sp input values: ");
		inputParams.append("resourceId:").append(resourceId == null?0:resourceId).append(",");
		inputParams.append("apptDateTime:").append(dateTime).append(",");
		inputParams.append("serviceId:").append(serviceId).append(",");
		inputParams.append("customerId:").append(customerId).append(",");
		inputParams.append("BlockTimeInMins:").append(cdConfig.getBlockTimeInMins());
		logger.debug("Input parameters of Hold Appointment SP:"+inputParams.toString());
		try {
			String spName = "temp_hold_appointment_sp";
			SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcCustomTemplate.getJdbcTemplate()).withProcedureName(spName);
			Map<String,Object> inParameters = new HashMap<String,Object>();
			inParameters.put(SPConstants.RESOURCE_ID.getValue(), resourceId == null ? 0 : resourceId);
			inParameters.put(SPConstants.APPT_DATE_TIME.getValue(), dateTime);
			inParameters.put(SPConstants.BLOCK_TIME_IN_MINS.getValue(), cdConfig.getBlockTimeInMins());
			inParameters.put(SPConstants.SERVICE_ID.getValue(), serviceId);
			inParameters.put(SPConstants.CUSTOMER_ID.getValue(), customerId);
			
			long startTime = System.currentTimeMillis();
			Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(inParameters);
			long endTime = System.currentTimeMillis();
			logTimeTaken(spName, startTime, endTime);
			
			
			Object holdIdObj = simpleJdbcCallResult.get(SPConstants.HOLD_ID.getValue());
			Object errorMsg = simpleJdbcCallResult.get(SPConstants.ERROR_MESSAGE.getValue());
			Object displayDateTime = simpleJdbcCallResult.get(SPConstants.DISPLAY_DATETIME.getValue());
			
			TempHoldAppt tempHoldAppt = new TempHoldAppt();
			tempHoldAppt.setHoldId((Long)holdIdObj);
			tempHoldAppt.setErrorMessage((errorMsg!=null)?(String)errorMsg:"");
			tempHoldAppt.setDisplayDateTime((displayDateTime!=null)?(String)displayDateTime:"");
			return tempHoldAppt;
		} catch(DataAccessException dae) {
			throw new TelAppointException(ErrorConstants.ERROR_1055.getCode(), ErrorConstants.ERROR_1055.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,dae.getMessage(), inputParams.toString());
		}
	}
	
	public void bookAppointment(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, Long scheduleId, String device, String langCode, Integer apptMethod, ClientDeploymentConfig cdConfig, ConfirmAppointmentResponse confirmAppointmentResponse) throws TelAppointException, Exception {
		try {
			String spName = "book_appointment_sp";
			SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcCustomTemplate.getJdbcTemplate()).withProcedureName(spName);
			logger.info("book_appointment_sp input params: ScheduleId: "+scheduleId+", BlockedTimeInMins:"+cdConfig.getBlockTimeInMins());
			
			Map<String,Object> inParameters = new HashMap<>();
			inParameters.put(SPConstants.SCHEDULE_ID.getValue(), scheduleId);
			inParameters.put(SPConstants.LANG_CODE.getValue(), langCode);
			inParameters.put(SPConstants.DEVICE.getValue(), device);
			inParameters.put(SPConstants.APPT_METHOD.getValue(), apptMethod);
			inParameters.put(SPConstants.BLOCK_TIME_IN_MINS.getValue(), cdConfig.getBlockTimeInMins());
			long startTime = System.currentTimeMillis();
			Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(inParameters);
			long endTime = System.currentTimeMillis();
			logTimeTaken(spName, startTime, endTime);
			
			Object errorMsg = simpleJdbcCallResult.get(SPConstants.ERROR_MESSAGE.getValue());
			Object displayKeys = simpleJdbcCallResult.get(SPConstants.DISPLAY_KEYS.getValue());
			Object displayValues = simpleJdbcCallResult.get(SPConstants.DISPLAY_VALUES.getValue());
			String errorMessage = (errorMsg!=null)?(String)errorMsg:"";
			
			if(displayValues !=null && !"".equals(displayValues)) {
				confirmAppointmentResponse.setDisplayKeys((displayKeys!=null)?(String)displayKeys:"");
				confirmAppointmentResponse.setDisplayValues(displayValues!=null?(String)displayValues:"");
			} else {
				logger.error("Error from book appointment storedprocedure: "+errorMessage);
				confirmAppointmentResponse.setMessage(errorMessage);
				confirmAppointmentResponse.setStatus(false);
			}
		}  catch(DataAccessException dae) {
			StringBuilder inputData = new StringBuilder();
			inputData.append("scheduleId: [").append(scheduleId).append("]").append(",");
			inputData.append("langCode:[").append(langCode).append("]");
			throw new TelAppointException(ErrorConstants.ERROR_1056.getCode(), ErrorConstants.ERROR_1056.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,dae.getMessage(), inputData.toString());
		}
	}
	
	public void cancelAppointment(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, Long scheduleId, Integer cancelMethod, String langCode,ClientDeploymentConfig cdConfig, CancelAppointResponse cancelAppointResponse) throws TelAppointException, Exception {
		try {
			String spName = "cancel_appointment_sp";
			SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcCustomTemplate.getJdbcTemplate()).withProcedureName(spName);
			logger.info("book_appointment_sp input params: ScheduleId: "+scheduleId+" , BlockedTimeInMins:"+cdConfig.getBlockTimeInMins());
			Map<String,Object> inParameters = new HashMap<String,Object>();
			inParameters.put(SPConstants.SCHEDULE_ID.getValue(), scheduleId);
			inParameters.put(SPConstants.CANCEL_METHOD.getValue(), cancelMethod);
			inParameters.put(SPConstants.LANG_CODE.getValue(), langCode);
			inParameters.put(SPConstants.BLOCK_TIME_IN_MINS.getValue(), cdConfig.getBlockTimeInMins());
			long startTime = System.currentTimeMillis();
			Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(inParameters);
			long endTime = System.currentTimeMillis();
			logTimeTaken(spName, startTime, endTime);
			
			Object success = simpleJdbcCallResult.get(SPConstants.SUCCESS.getValue());
			Object displayKeys = simpleJdbcCallResult.get(SPConstants.DISPLAY_KEYS.getValue());
			Object displayValues = simpleJdbcCallResult.get(SPConstants.DISPLAY_VALUES.getValue());
			Object errorMsg = simpleJdbcCallResult.get(SPConstants.ERROR_MESSAGE.getValue());
			String errorMessage = (errorMsg!=null)?(String)errorMsg:"";
			if(success != null) {
				if("Y".equals((String)success)) {
					cancelAppointResponse.setCancelled(true);
					cancelAppointResponse.setDisplayKeys((displayKeys!=null)?(String)displayKeys:"");
					cancelAppointResponse.setDisplayValues(displayValues!=null?(String)displayValues:"");
				} else {
					cancelAppointResponse.setCancelled(false);
					cancelAppointResponse.setMessage(errorMessage);
				}
			} else {
				logger.error("Error from book appointment storedprocedure:"+errorMessage);
			}
		}  catch(DataAccessException dae) {
			StringBuilder inputData = new StringBuilder();
			inputData.append("scheduleId: [ ").append(scheduleId).append(" ] ").append(",");
			inputData.append("langCode:[ ").append(langCode).append(" ]");
			throw new TelAppointException(ErrorConstants.ERROR_1057.getCode(), ErrorConstants.ERROR_1057.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,dae.getMessage(), inputData.toString());
		}
	}
		
	public void holdFirstAvailableAppointment(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, HoldAppointmentRequest holdApptReq,
			String dateTime, TempHoldAppt tempHoldAppt, ClientDeploymentConfig cdConfig, HoldAppointmentResponse holdAppointmentResponse) throws TelAppointException, Exception {
		DataSourceTransactionManager dsTransactionManager = jdbcCustomTemplate.getDataSourceTransactionManager();
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = dsTransactionManager.getTransaction(def);
		try {
			final StringBuilder sql = new StringBuilder();
			sql.append("insert into schedule (timestamp,status,trans_id,appt_date_time,blocks,");
			/*if (holdApptReq.getCompanyId() !=null && holdApptReq.getCompanyId() > 0) {
				sql.append("company_id,");
			}*/
			if (holdApptReq.getProcedureId() != null && holdApptReq.getProcedureId() > 0) {
				sql.append("procedure_id,");
			}

			if (holdApptReq.getDepartmentId() != null && holdApptReq.getDepartmentId() > 0) {
				sql.append("department_id,");
			}
			sql.append("location_id,resource_id,");
			sql.append(" service_id,customer_id) values (");
			sql.append(" CONVERT_TZ(now(),'US/Central','").append(cdConfig.getTimeZone()).append("')");
			sql.append(",?,?,?,(select blocks from service where id=?),");

			/*if (holdApptReq.getCompanyId() != null && holdApptReq.getCompanyId() > 0) {
				sql.append("?,");
			}*/
			if (holdApptReq.getProcedureId() != null && holdApptReq.getProcedureId() > 0) {
				sql.append("?,");
			}

			if (holdApptReq.getDepartmentId() != null && holdApptReq.getDepartmentId() > 0) {
				sql.append("?,");
			}

			sql.append("?,?,?,?)");

			KeyHolder holder = new GeneratedKeyHolder();
			jdbcCustomTemplate.getJdbcTemplate().update(new PreparedStatementCreator() {
				int i = 1;

				@Override
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
					ps.setInt(i++, AppointmentStatus.HOLD.getStatus()); 
					ps.setLong(i++, holdApptReq.getTransId()==null || "".equals(holdApptReq.getTransId())?1:holdApptReq.getTransId());
					ps.setString(i++, dateTime);
					ps.setInt(i++, holdApptReq.getServiceId());
					/*if (holdApptReq.getCompanyId() != null && holdApptReq.getCompanyId() > 0) {
						ps.setInt(i++, holdApptReq.getCompanyId());
					}*/

					if (holdApptReq.getProcedureId() != null && holdApptReq.getProcedureId() > 0) {
						ps.setLong(i++, holdApptReq.getProcedureId());
					}

					if (holdApptReq.getDepartmentId() != null && holdApptReq.getDepartmentId() > 0) {
						ps.setLong(i++, holdApptReq.getDepartmentId());
					}
					ps.setInt(i++, holdApptReq.getLocationId().intValue());
					ps.setInt(i++, holdApptReq.getResourceId() == null?0:holdApptReq.getResourceId().intValue());
					ps.setInt(i++, holdApptReq.getServiceId().intValue());
					ps.setLong(i++, holdApptReq.getCustomerId().longValue());
					return ps;
				}
			}, holder);
			Long scheduleId = holder.getKey().longValue();
			sql.setLength(0);
			updateResourceCalendar(jdbcCustomTemplate, logger, scheduleId, tempHoldAppt.getHoldId());
			holdAppointmentResponse.setScheduleId(scheduleId);
			holdAppointmentResponse.setDisplayDateTime(tempHoldAppt.getDisplayDateTime());
			dsTransactionManager.commit(status);
		} catch(DataAccessException dae) {
			dsTransactionManager.rollback(status);
			throw new TelAppointException(ErrorConstants.ERROR_1047.getCode(), ErrorConstants.ERROR_1047.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,dae.getMessage(), holdApptReq.toString());
		}
	}

	private void updateResourceCalendar(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, Long scheduleId, Long holdId) throws TelAppointException, Exception {
		StringBuilder sql = new StringBuilder();
		StringBuilder inputParams = new StringBuilder("ScheduleId:[").append(scheduleId).append("], tempHoldId:["+holdId).append("]");
		sql.append("update resource_calendar set schedule_id=? where schedule_id=?");
		try {
			int count = jdbcCustomTemplate.getJdbcTemplate().update(sql.toString(), new Object[]{scheduleId, holdId});
			if(count == 0) {
				throw new TelAppointException(ErrorConstants.ERROR_1046.getCode(), ErrorConstants.ERROR_1046.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,"No rows updated in resource calendar", inputParams.toString());
			}
		} catch(DataAccessException dae) {
			throw new TelAppointException(ErrorConstants.ERROR_1046.getCode(), ErrorConstants.ERROR_1046.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, dae.getMessage(), inputParams.toString());
		}
	}

	public void populateAlias(Map<String, String> aliasMap, List<Options> options, String fieldName) throws TelAppointException, Exception {
		try {
			for (Options option : options) {
				Object key = CoreUtils.getPropertyValue(option, fieldName);
				if (aliasMap != null) {
					String aliasValue = aliasMap.get((String) key);
					if (key != null && aliasValue != null) {
						CoreUtils.setPropertyValue(option, fieldName, aliasValue.trim());
					}
				}
			}
		} catch (IllegalAccessException |InvocationTargetException | IllegalArgumentException | IntrospectionException | NoSuchFieldException e) {
			throw new TelAppointException(ErrorConstants.ERROR_1030.getCode(), ErrorConstants.ERROR_1030.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
		}
	}

	public <T> void authenticateCustomer(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String device, List<Integer> fieldIds, List<String> fieldValues,
			final List<T> fieldList, final Class<T> classType, final Object response, Map<String, String> labelMap) throws TelAppointException, Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select id,first_name,last_name,blocked_flag from customer where 1=1 ");
		List<String> valueList = new ArrayList<String>();
		try {
			for (int i = 0; i < fieldList.size(); i++) {
				T pageFieldObject = fieldList.get(i);
				int fieldId = (Integer)CoreUtils.getPropertyValue(pageFieldObject, "fieldId");
				String loginType = (String)CoreUtils.getPropertyValue(pageFieldObject, "loginType");
				String paramColumn = (String)CoreUtils.getPropertyValue(pageFieldObject, "paramColumn");
				String storageType = (String)CoreUtils.getPropertyValue(pageFieldObject, "storageType");
				int storageSize = (Integer)CoreUtils.getPropertyValue(pageFieldObject, "storageSize");
				int inFieldId = fieldIds.get(i);
				if (fieldId == inFieldId && "authenticate".equals(loginType)) {
					String paramValue = fieldValues.get(i);
					sql.append(" and ").append(paramColumn).append("=").append("?");
					if ("first_name".equals(paramColumn) || "last_name".equals(paramColumn)) {
						paramValue = (paramValue.length() > 0) ? CoreUtils.capitalizeString(paramValue) : "";
					}
					if ("account_number".equals(paramColumn)) {
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
					}
					
					if ("dob".equalsIgnoreCase(paramColumn)) {
						if (paramValue == null || "".equals(paramValue)) {
							logger.error("DOB value is : " + paramValue);
							continue;
						}
						paramValue = DateUtils.convertMMDDYYYY_TO_YYYYMMDDFormat(paramValue);
					}
					
					valueList.add(paramValue);
				} 
				
				
				/*else {
					StringBuilder inputData = new StringBuilder();
					inputData.append("FieldIds:").append(fieldIds.toString()).append(",");
					inputData.append("FieldValues:").append(fieldValues.toString());
					throw new TelAppointException(ErrorConstants.ERROR_1033.getCode(), ErrorConstants.ERROR_1033.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,"Invalid fields length", inputData.toString());
				}*/
			}
		} catch(NoSuchFieldException nsfe) {
			StringBuilder inputData = new StringBuilder();
			inputData.append("FieldIds:").append(fieldIds.toString()).append(",");
			inputData.append("FieldValues:").append(fieldValues.toString());
			throw new TelAppointException(ErrorConstants.ERROR_1040.getCode(), ErrorConstants.ERROR_1040.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,nsfe.getMessage(), inputData.toString());
		}

		sql.append(" and delete_flag ='N' order by id desc");
		logger.debug("authenticate Customer SQL: " + sql.toString());
		try {
			jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), valueList.toArray(), new ResultSetExtractor<Object>() {
				@Override
				public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						if (response instanceof AuthResponse) {
							AuthResponse authResponse = (AuthResponse) response;
							authResponse.setAuthSuccess(true);
							authResponse.setCustomerId(rs.getLong("id"));
							authResponse.setBlocked(rs.getString("blocked_flag"));
							if("Y".equals(authResponse.getBlocked())) {
								if(CoreUtils.isOnline(device)) {
									authResponse.setBlockedMessage((labelMap == null)? "Unable to get customer blocked message from database.":(labelMap.get("CUSTOMER_BLOCKED_MSG")==null?"Unable to get customer blocked message from database.":labelMap.get("CUSTOMER_BLOCKED_MSG")));
								} else if(CoreUtils.isIVR(device)) {
									authResponse.setPageName("cust_blocked");
								}
							}
							return authResponse;
						}
					} else {
						if (response instanceof AuthResponse) {
							AuthResponse authResponse = (AuthResponse) response;
							authResponse.setAuthSuccess(false);
							return authResponse;
						}
					}
					return response;
				}
			});
		} catch (DataAccessException dae) {
			throw new TelAppointException(ErrorConstants.ERROR_1034.getCode(), ErrorConstants.ERROR_1034.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,dae.getMessage(), null);
		}
	}

	
	public boolean validateCustomer(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String device, List<OnlinePageFields> onlinePageFields, List<Integer> fieldIds,
			List<String> fieldValues) throws TelAppointException, Exception {
			StringBuilder sql = new StringBuilder();
			sql.append("select count(1) from customer where 1=1 ");
			List<String> valueList = new ArrayList<String>();
			for (int i = 0; i < onlinePageFields.size(); i++) {
				OnlinePageFields onlinePageField = onlinePageFields.get(i);
				int fieldId = onlinePageField.getFieldId();
				int inFieldId = fieldIds.get(i);

				if (fieldId == inFieldId && "authenticate".equals(onlinePageField.getLoginType())) {
					String paramColumn = onlinePageField.getParamColumn();
					String paramValue = fieldValues.get(i);
					sql.append(" and ").append(paramColumn).append("=").append("?");
					if ("first_name".equals(paramColumn) || "last_name".equals(paramColumn)) {
						paramValue = (paramValue.length() > 0) ? CoreUtils.capitalizeString(paramValue) : "";
					}
					if ("account_number".equals(paramColumn)) {
						int storageSize = onlinePageField.getStorageSize();
						String storageType = onlinePageField.getStorageType();
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
					}
					valueList.add(paramValue);
				} else {
					throw new TelAppointException(ErrorConstants.ERROR_1033.getCode(), ErrorConstants.ERROR_1033.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,"Invalid fields length", null);
				}
			}

			sql.append(" and delete_flag ='N' order by id desc");
			logger.debug("authenticate Customer SQL: " + sql.toString());
			try {
				int count = jdbcCustomTemplate.getJdbcTemplate().queryForInt(sql.toString(), valueList.toArray());
				boolean isValid = true;
				if (count > 1) {
					isValid = false;
				}
				return isValid;
			} catch(DataAccessException dae) {
				throw new TelAppointException(ErrorConstants.ERROR_1033.getCode(), ErrorConstants.ERROR_1033.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, dae.getMessage(), null);
			}
	}

	public long saveCustomer(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, final Customer customer, ClientDeploymentConfig cdConfig) throws TelAppointException, Exception {
		final StringBuilder sql = new StringBuilder();
		sql.append("insert into customer (account_number, first_name, middle_name,  last_name,  ");
		sql.append("home_phone, work_phone, contact_phone, email,address, city, zip_postal, create_datetime, update_datetime, attrib1,");
		sql.append(" attrib2, attrib3, attrib4, attrib5, attrib6, attrib7, attrib8, attrib9, attrib10, ");
		sql.append(" attrib11, attrib12, attrib13, attrib14, attrib15, attrib16, attrib17, attrib18, attrib19, attrib20");
		if(customer.getDob() != null && !"".equals(customer.getDob())) {
			sql.append(",dob");
		}
		sql.append(") ");
		sql.append(" values (?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,");
		sql.append("CONVERT_TZ(now(),'US/Central','").append(cdConfig.getTimeZone()).append("')").append(",");
		sql.append("CONVERT_TZ(now(),'US/Central','").append(cdConfig.getTimeZone()).append("')");
		sql.append(",?, ?, ?, ?, ?,  ?, ?, ?, ?, ?,  ?, ?, ?, ?, ?,  ?,?, ?, ?,?");
		
		if(customer.getDob() != null && !"".equals(customer.getDob())) {
			sql.append(",?");
		}
		
		sql.append(")");
		logger.debug("saveCustomer SQL: " + sql.toString());
		KeyHolder holder = new GeneratedKeyHolder();
		try {
			jdbcCustomTemplate.getJdbcTemplate().update(new PreparedStatementCreator() {
				int i = 1;

				@Override
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
					ps.setString(i++, customer.getAccountNumber());
					ps.setString(i++, customer.getFirstName());
					ps.setString(i++, customer.getMiddleName());
					ps.setString(i++, customer.getLastName());
					ps.setString(i++, customer.getHomePhone());
					ps.setString(i++, customer.getWorkPhone());
					ps.setString(i++, customer.getContactPhone());
					ps.setString(i++, customer.getEmail());
					ps.setString(i++, customer.getAddress());
					ps.setString(i++, customer.getCity());
					ps.setString(i++, customer.getZipPostal());
					ps.setString(i++, customer.getAttrib1());
					ps.setString(i++, customer.getAttrib2());
					ps.setString(i++, customer.getAttrib3());
					ps.setString(i++, customer.getAttrib4());
					ps.setString(i++, customer.getAttrib5());
					ps.setString(i++, customer.getAttrib6());
					ps.setString(i++, customer.getAttrib7());
					ps.setString(i++, customer.getAttrib8());
					ps.setString(i++, customer.getAttrib9());
					ps.setString(i++, customer.getAttrib10());
					ps.setString(i++, customer.getAttrib11());
					ps.setString(i++, customer.getAttrib12());
					ps.setString(i++, customer.getAttrib13());
					ps.setString(i++, customer.getAttrib14());
					ps.setString(i++, customer.getAttrib15());
					ps.setString(i++, customer.getAttrib16());
					ps.setString(i++, customer.getAttrib17());
					ps.setString(i++, customer.getAttrib18());
					ps.setString(i++, customer.getAttrib19());
					ps.setString(i++, customer.getAttrib20());
					
					if(customer.getDob() != null && !"".equals(customer.getDob())) {
						ps.setString(i++, customer.getDob());
					}
					return ps;
				}
			}, holder);
			Long customerId = holder.getKey().longValue();
			
			//below added by Anantha on August 5th, 2016
			final StringBuilder sql2 = new StringBuilder();
			sql2.append("update customer ");
			if(customer.getHouseHoldId() != null && customer.getHouseHoldId().longValue() > 0) {
				sql2.append(" set household_id =  "+customer.getHouseHoldId());
			} else {
				sql2.append(" set household_id = (SELECT nextval('sq_my_sequence') as next_sequence) ");
			}
			Object columnValue[];
			if(customer.getParticipantId() == null) {
				columnValue = new Object[] { customerId };
			} else {
				sql2.append(",participant_id=? ");
				columnValue = new Object[] { customer.getParticipantId(), customerId };
			}
			sql2.append(" where id=?");
			int count = jdbcCustomTemplate.getJdbcTemplate().update(sql2.toString(), columnValue);				
			
			return customerId;
		} catch (DataAccessException dae) {
			throw new TelAppointException(ErrorConstants.ERROR_1036.getCode(), ErrorConstants.ERROR_1036.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, dae.getMessage(), "CustomerData:"+customer.toString());
		}
	}
	
	
	
	public boolean updateCustomerForIVR(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, Customer customer, ClientDeploymentConfig cdConfig) throws TelAppointException, Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("update customer set first_name= ?, last_name=?, ");
		sql.append(" update_datetime = ");
		sql.append("CONVERT_TZ(now(),'US/Central','").append(cdConfig.getTimeZone()).append("')");
		sql.append(" where id = ?");
		logger.debug("updateCustomer SQL: " + sql.toString());
		Object columnValue[] = new Object[] { customer.getFirstName(), customer.getLastName(),customer.getCustomerId() };
		try {
			int count = jdbcCustomTemplate.getJdbcTemplate().update(sql.toString(), columnValue);
			if (count > 0) {
				return true;
			}
			return false;
		} catch (DataAccessException dae) {
			throw new TelAppointException(ErrorConstants.ERROR_1037.getCode(), ErrorConstants.ERROR_1037.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,dae.getMessage(), null);
		}
	}
		
	public boolean updateCustomer(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, StringBuilder sql, MapSqlParameterSource paramSource, ClientDeploymentConfig cdConfig) throws TelAppointException, Exception {
		logger.debug("updateCustomer SQL: " + sql.toString());
		try {
			int count = jdbcCustomTemplate.getNameParameterJdbcTemplate().update(sql.toString(), paramSource);
			if (count > 0) {
				return true;
			}
			return false;
		} catch (DataAccessException dae) {
			throw new TelAppointException(ErrorConstants.ERROR_1037.getCode(), ErrorConstants.ERROR_1037.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,dae.getMessage(), "Data: "+paramSource.toString());
		}
	}

	public Customer getCustomer(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, Long customerId) throws TelAppointException, Exception {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("select *,  DATE_FORMAT(customer.dob,'%m-%d-%Y') as dobDB from customer customer where customer.id=");
			sql.append(customerId);
			sql.append(" and customer.delete_flag ='N' ");
			logger.debug("getCustomer SQL: " + sql.toString());
			return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new ResultSetExtractor<Customer>() {
				@Override
				public Customer extractData(ResultSet rs) throws SQLException, DataAccessException {
					Customer customer = null;
					if (rs.next()) {
						customer = new Customer();
						customer.setCustomerId(rs.getLong("id"));
						customer.setAccountNumber(rs.getString("account_number"));
						customer.setFirstName(rs.getString("first_name"));
						customer.setMiddleName(rs.getString("middle_name"));
						customer.setLastName(rs.getString("last_name"));
						customer.setHomePhone(rs.getString("home_phone"));
						customer.setWorkPhone(rs.getString("work_phone"));
						customer.setContactPhone(rs.getString("contact_phone"));
						customer.setEmail(rs.getString("email"));
						customer.setParticipantId(rs.getLong("participant_id"));
						customer.setHouseHoldId(rs.getLong("household_id"));
						customer.setLiHeapFund(rs.getString("liheap_fund"));
						customer.setCustomerBlocked(rs.getString("blocked_flag"));
						customer.setDob(rs.getString("dobDB"));
						
					}
					return customer;
				}
			});
		} catch (DataAccessException dae) {
			throw new TelAppointException(ErrorConstants.ERROR_1035.getCode(), ErrorConstants.ERROR_1035.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, dae.getMessage(), "CustomerId: ["+customerId+"]");
		}
	}
	
	public Customer getCustomerByParticipantId(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, Long participantId) throws TelAppointException {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("select * from customer customer where customer.participant_id=");
			sql.append(participantId);
			sql.append(" and customer.delete_flag ='N' ");
			logger.debug("getCustomer SQL: " + sql.toString());
			return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new ResultSetExtractor<Customer>() {
				@Override
				public Customer extractData(ResultSet rs) throws SQLException, DataAccessException {
					Customer customer = null;
					if (rs.next()) {
						customer = new Customer();
						customer.setCustomerId(rs.getLong("id"));
						customer.setParticipantId(participantId);
						customer.setAccountNumber(rs.getString("account_number"));
						customer.setFirstName(rs.getString("first_name"));
						customer.setMiddleName(rs.getString("middle_name"));
						customer.setLastName(rs.getString("last_name"));
						customer.setHomePhone(rs.getString("home_phone"));
						customer.setWorkPhone(rs.getString("work_phone"));
						customer.setContactPhone(rs.getString("contact_phone"));
						customer.setEmail(rs.getString("email"));
						customer.setCustomerBlocked(rs.getString("blocked_flag"));
					}
					return customer;
				}
			});
		} catch (DataAccessException dae) {
			throw new TelAppointException(ErrorConstants.ERROR_1035.getCode(), ErrorConstants.ERROR_1035.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, dae.getMessage(), "ParticipantId: ["+participantId+"]");
		}
	}
}
