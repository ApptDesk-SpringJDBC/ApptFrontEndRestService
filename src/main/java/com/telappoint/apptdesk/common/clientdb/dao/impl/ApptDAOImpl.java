package com.telappoint.apptdesk.common.clientdb.dao.impl;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.telappoint.apptdesk.common.model.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.SqlReturnResultSet;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.telappoint.apptdesk.common.clientdb.dao.ApptDAO;
import com.telappoint.apptdesk.common.constants.AppointmentStatus;
import com.telappoint.apptdesk.common.constants.CommonApptDeskConstants;
import com.telappoint.apptdesk.common.constants.ErrorConstants;
import com.telappoint.apptdesk.common.sp.constants.SPConstants;
import com.telappoint.apptdesk.common.utils.CoreUtils;
import com.telappoint.apptdesk.common.utils.DateUtils;
import com.telappoint.apptdesk.handlers.exception.TelAppointException;
import com.telappoint.apptdesk.model.AppointmentDetails;
import com.telappoint.apptdesk.model.ApptSysConfig;
import com.telappoint.apptdesk.model.Company;
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
import com.telappoint.apptdesk.model.IVRFlow;
import com.telappoint.apptdesk.model.IVRPageFields;
import com.telappoint.apptdesk.model.IVRXml;
import com.telappoint.apptdesk.model.Language;
import com.telappoint.apptdesk.model.ListOfDocs;
import com.telappoint.apptdesk.model.ListOfThingsResponse;
import com.telappoint.apptdesk.model.Location;
import com.telappoint.apptdesk.model.LocationPriSecAvailable;
import com.telappoint.apptdesk.model.LoginPageFields;
import com.telappoint.apptdesk.model.NameRecordInfo;
import com.telappoint.apptdesk.model.OnlineFlow;
import com.telappoint.apptdesk.model.OnlinePageContent;
import com.telappoint.apptdesk.model.OnlinePageFields;
import com.telappoint.apptdesk.model.Options;
import com.telappoint.apptdesk.model.PendingEnrollment;
import com.telappoint.apptdesk.model.Procedure;
import com.telappoint.apptdesk.model.Schedule;
import com.telappoint.apptdesk.model.Service;
import com.telappoint.apptdesk.model.ServiceFundsResponse;
import com.telappoint.apptdesk.model.ServiceTimeSlotsAvailableStatus;
import com.telappoint.apptdesk.model.TransScript;
import com.telappoint.apptdesk.model.TransScriptEmailData;
import com.telappoint.apptdesk.model.TransScriptRequest;


/**
 * @author Balaji N
 */
@Repository
public  class ApptDAOImpl extends AbstractDAOImpl implements ApptDAO {

    @Override
    public void getOnlineFlow(Logger logger, JdbcCustomTemplate jdbcCustomTemplate, final Map<String, OnlineFlow> onlineFlowMap) throws TelAppointException, Exception {
        String sql = "select * from online_flow order by page_id, placement asc";
        try {
            jdbcCustomTemplate.getJdbcTemplate().query(sql, new ResultSetExtractor<Long>() {
                @Override
                public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
                    OnlineFlow onlineFlow;
                    while (rs.next()) {
                        onlineFlow = new OnlineFlow();
                        onlineFlow.setPageId(rs.getInt("page_id"));
                        String field = rs.getString("field");
                        onlineFlow.setField(field);
                        onlineFlow.setDisplay(rs.getString("display"));
                        onlineFlow.setSubmitAPI(rs.getString("submit_api"));
                        onlineFlow.setPageRedirect(rs.getString("page_redirect"));
                        onlineFlow.setLogic1(rs.getString("logic_1"));
                        onlineFlow.setPage1(rs.getString("page_1"));
                        onlineFlow.setLogic2(rs.getString("logic_2"));
                        onlineFlow.setPage2(rs.getString("page_2"));
                        onlineFlow.setLogic3(rs.getString("logic_3"));
                        onlineFlow.setPage3(rs.getString("page_3"));
                        onlineFlow.setLogic4(rs.getString("logic_4"));
                        onlineFlow.setPage4(rs.getString("page_4"));
                        onlineFlow.setLogic5(rs.getString("logic_5"));
                        onlineFlow.setPage5(rs.getString("page_5"));
                        onlineFlowMap.put(field, onlineFlow);
                    }
                    return (long) 0;
                }
            });
        } catch (DataAccessException dae) {
            throw new TelAppointException(ErrorConstants.ERROR_1025.getCode(), ErrorConstants.ERROR_1025.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, dae.getMessage(), null);
        }
    }

    @Override
    public void getOnlinePageContent(Logger logger, JdbcCustomTemplate jdbcCustomTemplate, String langCode, final Map<String, List<OnlinePageContent>> onlinePageContentMap, Map<String, List<String>> onlinePageContentMapIds)
            throws TelAppointException, Exception {
        String sql = "select id, appt_method, header, left_side_content, script, right_side_content from online_page_content where lang_code=? and delete_flag='N' order by placement";
        try {
            jdbcCustomTemplate.getJdbcTemplate().query(sql, new Object[]{langCode}, new ResultSetExtractor<Long>() {
                @Override
                public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
                    OnlinePageContent onlinePageContent;
                    while (rs.next()) {
                        onlinePageContent = new OnlinePageContent();
                        String apptMethod = rs.getString("appt_method");
                        onlinePageContent.setPageContentId(rs.getInt("id"));
                        onlinePageContent.setApptMethod(apptMethod);
                        onlinePageContent.setHeader(rs.getString("header"));
                        onlinePageContent.setScript(rs.getString("script"));
                        onlinePageContent.setLeftSideContent(rs.getString("left_side_content"));
                        onlinePageContent.setRightSideContent(rs.getString("right_side_content"));
                        if (onlinePageContentMap.containsKey(apptMethod)) {
                            List<OnlinePageContent> onlinePageContents = onlinePageContentMap.get(apptMethod);
                            onlinePageContents.add(onlinePageContent);
                            onlinePageContentMap.put(apptMethod, onlinePageContents);
                            List<String> onlineContentId = onlinePageContentMapIds.get(apptMethod);
                            onlineContentId.add(Integer.toString(onlinePageContent.getPageContentId()));
                            onlinePageContentMapIds.put(apptMethod, onlineContentId);
                        } else {
                            List<OnlinePageContent> onlinePageContents = new ArrayList<OnlinePageContent>();
                            List<String> onlinePageContentIdList = new ArrayList<String>();
                            onlinePageContents.add(onlinePageContent);
                            onlinePageContentIdList.add(Integer.toString(onlinePageContent.getPageContentId()));
                            onlinePageContentMap.put(apptMethod, onlinePageContents);
                            onlinePageContentMapIds.put(apptMethod, onlinePageContentIdList);
                        }
                    }
                    return (long) 0;
                }
            });
        } catch (DataAccessException dae) {
            throw new TelAppointException(ErrorConstants.ERROR_1026.getCode(), ErrorConstants.ERROR_1026.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, dae.getMessage(), null);
        }
    }

    @Override
    public void getOnlinePageFields(Logger logger, JdbcCustomTemplate jdbcCustomTemplate, String langCode, final Map<String, List<OnlinePageFields>> pageFieldMap) throws TelAppointException, Exception {
        String sql = "select * from online_page_fields order by placement";
        try {
            jdbcCustomTemplate.getJdbcTemplate().query(sql, new ResultSetExtractor<Long>() {
                @Override
                public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
                    OnlinePageFields onlinePageFields = null;
                    while (rs.next()) {
                        onlinePageFields = new OnlinePageFields();
                        int fieldId = rs.getInt("id");
                        int onlinePageContentId = rs.getInt("online_page_content_id");
                        onlinePageFields.setOnlinePageContentId(onlinePageContentId);
                        onlinePageFields.setFieldName(rs.getString("field_name"));
                        onlinePageFields.setDisplayType(rs.getString("display_type"));
                        onlinePageFields.setDisplay(rs.getString("display"));
                        onlinePageFields.setShowData(rs.getString("show_data"));
                        onlinePageFields.setEmptyErrorMsg(rs.getString("empty_error_msg"));
                        onlinePageFields.setInvalidErrorMsg(rs.getString("invalid_error_msg"));
                        onlinePageFields.setRequired(rs.getString("required"));
                        onlinePageFields.setValidateRequired(rs.getString("validate_required"));
                        onlinePageFields.setValidationRuleAPI(rs.getString("validation_rules_api"));
                        onlinePageFields.setValidationRules(rs.getString("validation_rules"));
                        onlinePageFields.setValidateMinChars(rs.getString("validate_min_chars"));
                        onlinePageFields.setValidateMaxChars(rs.getString("validate_max_chars"));
                        onlinePageFields.setInitialValues(rs.getString("initial_values"));
                        onlinePageFields.setStorageSize(rs.getInt("storage_size"));
                        onlinePageFields.setStorageType(rs.getString("storage_type"));
                        onlinePageFields.setParamColumn(rs.getString("param_column"));
                        onlinePageFields.setParamTable(rs.getString("param_table"));
                        onlinePageFields.setParamType(rs.getString("param_type"));
                        onlinePageFields.setLoginType(rs.getString("login_type"));
                        onlinePageFields.setFieldId(fieldId);

                        if (pageFieldMap.containsKey(Long.toString(onlinePageContentId))) {
                            List<OnlinePageFields> onlinePageFieldList = pageFieldMap.get(Long.toString(onlinePageContentId));
                            onlinePageFieldList.add(onlinePageFields);
                            pageFieldMap.put(Long.toString(onlinePageContentId), onlinePageFieldList);
                        } else {
                            List<OnlinePageFields> onlinePageFieldList = new ArrayList<OnlinePageFields>();
                            onlinePageFieldList.add(onlinePageFields);
                            pageFieldMap.put(Long.toString(onlinePageContentId), onlinePageFieldList);
                        }
                    }
                    return (long) 0;
                }
            });
        } catch (DataAccessException dae) {
            throw new TelAppointException(ErrorConstants.ERROR_1027.getCode(), ErrorConstants.ERROR_1027.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, dae.getMessage(), null);
        }
    }

    @Override
    public void getIVRPageFields(Logger logger, JdbcCustomTemplate jdbcCustomTemplate, String langCode, List<IVRPageFields> ivrPageFields) throws TelAppointException, Exception {
        String sql = "select * from ivr_page_fields order by placement";
        try {
            jdbcCustomTemplate.getJdbcTemplate().query(sql, new ResultSetExtractor<Long>() {
                @Override
                public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
                    IVRPageFields ivrPageField;
                    while (rs.next()) {
                        ivrPageField = new IVRPageFields();
                        ivrPageField.setFieldId(rs.getInt("id"));
                        ivrPageField.setPageName(rs.getString("page_name"));
                        ivrPageField.setFieldName(rs.getString("field_name"));
                        ivrPageField.setLoginType(rs.getString("login_type"));
                        ivrPageField.setParamType(rs.getString("param_type"));
                        ivrPageField.setParamTable(rs.getString("param_table"));
                        ivrPageField.setParamColumn(rs.getString("param_column"));
                        ivrPageField.setJavaRef(rs.getString("java_reflection"));
                        ivrPageField.setStorageSize(rs.getInt("storage_size"));
                        ivrPageField.setStorageType(rs.getString("storage_type"));
                        ivrPageField.setEncrypt(rs.getString("encrypt"));
                        ivrPageField.setIvrMinDigits(rs.getInt("ivr_min_digits"));
                        ivrPageField.setIvrMaxDigits(rs.getInt("ivr_max_digits"));
                        ivrPageField.setIvrLoginParamAudio(rs.getString("ivr_login_param_audio"));
                        ivrPageField.setIvrLoginParamTts(rs.getString("ivr_login_param_tts"));
                        ivrPageFields.add(ivrPageField);
                    }
                    return (long) 0;
                }
            });
        } catch (DataAccessException dae) {
            throw new TelAppointException(ErrorConstants.ERROR_1045.getCode(), ErrorConstants.ERROR_1045.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, dae.getMessage(), null);
        }
    }
    
    @Override
    public void getMobilePageFields(Logger logger, JdbcCustomTemplate jdbcCustomTemplate, String langCode, List<LoginPageFields> mobilePageFields, Map<String, String> contentMap, String loginType) throws Exception {
    	
        String sql = "select * from login_param_config where device_type='mobile'"+(!"all".equals(loginType)?" and login_type='"+loginType+"'":" ")+" order by placement";
        try {
            jdbcCustomTemplate.getJdbcTemplate().query(sql, new ResultSetExtractor<Long>() {
                @Override
                public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
                    LoginPageFields mobilePageField;
                    while (rs.next()) {
                        mobilePageField = new LoginPageFields();
                        mobilePageField.setFieldId(rs.getInt("id"));
                        mobilePageField.setFieldName(rs.getString("field_name"));
                        mobilePageField.setLoginType(rs.getString("login_type"));
                        mobilePageField.setParamType(rs.getString("param_type"));
                        mobilePageField.setParamTable(rs.getString("param_table"));
                        mobilePageField.setParamColumn(rs.getString("param_column"));
                        mobilePageField.setDisplayType(rs.getString("display_type"));
                        mobilePageField.setDisplay(rs.getString("display_context"));
                        String emptyErrorMsg= ((contentMap != null && contentMap.get(rs.getString("empty_error_msg")) !=null)? contentMap.get(rs.getString("empty_error_msg")):"Empty error message not configured for the key: "+rs.getString("empty_error_msg"));
                        String invalidErrorMsg = ((contentMap != null && contentMap.get(rs.getString("invalid_error_msg")) !=null)? contentMap.get(rs.getString("invalid_error_msg")):"Invalid Error message not configured for the key:"+rs.getString("invalid_error_msg"));
                        mobilePageField.setEmptyErrorMsg(emptyErrorMsg);
                        mobilePageField.setInvalidErrorMsg(invalidErrorMsg);
                        
                        mobilePageField.setRequired(rs.getString("required"));
                        mobilePageField.setValidateRequired(rs.getString("validate_required"));
                        mobilePageField.setValidationRules(rs.getString("validation_rules"));
                        mobilePageField.setValidateMinChars(rs.getString("validate_min_chars"));
                        mobilePageField.setValidateMaxChars(rs.getString("validate_max_chars"));
                       
                        mobilePageField.setStorageSize(rs.getInt("storage_size"));
                        mobilePageField.setStorageType(rs.getString("storage_type"));
                        mobilePageField.setJavaRef(rs.getString("java_reflection"));
                        mobilePageField.setListLabels(rs.getString("list_labels"));
                        mobilePageField.setListValues(rs.getString("list_values"));
                        mobilePageField.setInitialValues(rs.getString("list_initial_values"));
                        mobilePageFields.add(mobilePageField);
                    }
                    return (long) 0;
                }
            });
        } catch (DataAccessException dae) {
            throw new TelAppointException(ErrorConstants.ERROR_1086.getCode(), ErrorConstants.ERROR_1086.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, dae.getMessage(), null);
        }

    }

    @Override
    public void getIVRFlow(Logger logger, JdbcCustomTemplate jdbcCustomTemplate, final List<IVRFlow> ivrFlowList) throws TelAppointException, Exception {
        String sql = "select * from ivr_flow order by placement asc";
        try {
            jdbcCustomTemplate.getJdbcTemplate().query(sql, new ResultSetExtractor<Long>() {
                @Override
                public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
                    IVRFlow ivrFlow = null;
                    while (rs.next()) {
                        ivrFlow = new IVRFlow();
                        ivrFlow.setPage(rs.getString("page"));
                        ivrFlow.setDisplay(rs.getString("display"));
                        ivrFlow.setSubmitAPI(rs.getString("submit_api"));
                        ivrFlow.setPageRedirect(rs.getString("page_redirect"));
                        ivrFlow.setLogic1(rs.getString("logic_1"));
                        ivrFlow.setPage1(rs.getString("page_1"));
                        ivrFlow.setLogic2(rs.getString("logic_2"));
                        ivrFlow.setPage2(rs.getString("page_2"));
                        ivrFlow.setLogic3(rs.getString("logic_3"));
                        ivrFlow.setPage3(rs.getString("page_3"));
                        ivrFlow.setLogic4(rs.getString("logic_4"));
                        ivrFlow.setPage4(rs.getString("page_4"));
                        ivrFlow.setLogic5(rs.getString("logic_5"));
                        ivrFlow.setPage5(rs.getString("page_5"));
                        ivrFlowList.add(ivrFlow);
                    }
                    return (long) 0;
                }
            });
        } catch (DataAccessException dae) {
            throw new TelAppointException(ErrorConstants.ERROR_1025.getCode(), ErrorConstants.ERROR_1025.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, dae.getMessage(), null);
        }
    }
    
    @Override
    public boolean isAppointmentsExist(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, Long customerId) throws Exception {
    	StringBuilder sql = new StringBuilder();
    	sql.append("select count(*) from schedule sc");
    	sql.append(" where sc.appt_date_time > CONVERT_TZ(now(),'US/Central','US/Central')");
        sql.append(" and sc.status=").append(AppointmentStatus.CONFIRM.getStatus());
        sql.append(" and sc.customer_id=?");
        return jdbcCustomTemplate.getJdbcTemplate().queryForObject(sql.toString(), new Object[]{customerId}, Integer.class) !=0;
    }

    @Override
    public void getBookedAppointments(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String device, Long customerId, ClientDeploymentConfig cdConfig, String onlineDateTimeForamt, List<AppointmentDetails> apptList, Map<String, String> aliasMap)
            throws TelAppointException, Exception {
        StringBuilder sql = new StringBuilder();
        sql.append("select DISTINCT cu.account_number, cu.first_name, cu.last_name, cu.contact_phone, cu.home_phone, cu.cell_phone, cu.work_phone,cu.email, cu.attrib1, cu.attrib2,");
        sql.append("cu.attrib3, cu.attrib4,cu.attrib5, cu.attrib6,cu.attrib7, cu.attrib8,cu.attrib9, cu.attrib10,");
        sql.append("cu.attrib11, cu.attrib12,cu.attrib13, cu.attrib14,cu.attrib15, cu.attrib16,cu.attrib17, cu.attrib18,cu.attrib19, cu.attrib20,");
        sql.append("l.location_name_online, l.address, l.city, l.state, l.zip,l.time_zone, r.resource_audio, s.service_name_ivr_tts, s.service_name_ivr_audio,l.location_name_ivr_tts,l.location_name_ivr_audio,");
        sql.append("CONCAT(r.first_name,'',r.last_name) as resourceName, s.service_name_online,");
        if (CoreUtils.isOnline(device) || CoreUtils.isMobile(device)) {
            sql.append("DATE_FORMAT(sc.appt_date_time,").append("'").append(onlineDateTimeForamt).append("'").append(") as apptDateTime,");
        } else if (CoreUtils.isIVR(device)) {
            sql.append("sc.appt_date_time as apptDateTime,");
        }

        sql.append(" sc.id as scheduleId, a.conf_number from customer cu, schedule sc, appointment a, location l, resource r, service s");
        sql.append(" where 1=1");
        sql.append(" and sc.appt_date_time > CONVERT_TZ(now(),'US/Central','").append(cdConfig.getTimeZone()).append("')");
        sql.append(" and sc.status=").append(AppointmentStatus.CONFIRM.getStatus());
        sql.append(" and cu.id=? and cu.id=sc.customer_id and sc.id=a.schedule_id and sc.resource_id=r.id and l.id=sc.location_id and s.id=sc.service_id order by sc.appt_date_time asc");
        logger.debug("getBookedAppointment query: " + sql.toString());
        try {
            jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[]{customerId}, new ResultSetExtractor<Long>() {
                @Override
                public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
                    AppointmentDetails apptDetails = null;
                    while (rs.next()) {
                        apptDetails = new AppointmentDetails();
                        apptDetails.setAccountNumber(rs.getString("account_number"));
                        apptDetails.setFirstName(rs.getString("first_name"));
                        apptDetails.setLastName(rs.getString("last_name"));
                        apptDetails.setHomePhone(rs.getString("home_phone"));
                        apptDetails.setContactPhone(rs.getString("contact_phone"));
                        apptDetails.setCellPhone(rs.getString("cell_phone"));
                        apptDetails.setWorkPhone(rs.getString("work_phone"));
                        apptDetails.setEmail(rs.getString("email"));

                        if (CoreUtils.isIVR(device)) {
                            apptDetails.setTimeZone(rs.getString("time_zone") == null ? cdConfig.getTimeZone() : rs.getString("time_zone"));
                            apptDetails.setServiceNameAudio(rs.getString("service_name_ivr_audio"));
                            apptDetails.setServiceNameTts(rs.getString("service_name_ivr_tts"));
                            apptDetails.setResourceNameAudio(rs.getString("resource_audio"));
                            apptDetails.setLocationNameAudio(rs.getString("location_name_ivr_audio"));
                            apptDetails.setLocationNameTts(rs.getString("location_name_ivr_tts"));
                        } else if (CoreUtils.isOnline(device) || CoreUtils.isMobile(device)) {
                            apptDetails.setLocationAddress(rs.getString("address"));
                            apptDetails.setCity(rs.getString("city"));
                            apptDetails.setState(rs.getString("state"));
                            apptDetails.setZip(rs.getString("zip"));
                            if (aliasMap != null) {
                                apptDetails.setLocationName(aliasMap.get(rs.getString("location_name_online")) == null?rs.getString("location_name_online"):aliasMap.get(rs.getString("location_name_online")));
                                apptDetails.setServiceName(aliasMap.get(rs.getString("service_name_online")) == null?rs.getString("service_name_online"):aliasMap.get(rs.getString("service_name_online")));
                            }
                        }
                        apptDetails.setAttrib1(rs.getString("attrib1"));
                        apptDetails.setAttrib2(rs.getString("attrib2"));
                        apptDetails.setAttrib3(rs.getString("attrib3"));
                        apptDetails.setAttrib4(rs.getString("attrib4"));
                        apptDetails.setAttrib5(rs.getString("attrib5"));
                        apptDetails.setAttrib6(rs.getString("attrib6"));
                        apptDetails.setAttrib7(rs.getString("attrib7"));
                        apptDetails.setAttrib8(rs.getString("attrib8"));
                        apptDetails.setAttrib9(rs.getString("attrib9"));
                        apptDetails.setAttrib10(rs.getString("attrib10"));
                        apptDetails.setAttrib11(rs.getString("attrib11"));
                        apptDetails.setAttrib12(rs.getString("attrib12"));
                        apptDetails.setAttrib13(rs.getString("attrib13"));
                        apptDetails.setAttrib14(rs.getString("attrib14"));
                        apptDetails.setAttrib15(rs.getString("attrib15"));
                        apptDetails.setAttrib16(rs.getString("attrib16"));
                        apptDetails.setAttrib17(rs.getString("attrib17"));
                        apptDetails.setAttrib18(rs.getString("attrib18"));
                        apptDetails.setAttrib19(rs.getString("attrib19"));
                        apptDetails.setAttrib20(rs.getString("attrib20"));
                        apptDetails.setResourceName(rs.getString("resourceName"));
                        apptDetails.setApptDateTime(rs.getString("apptDateTime"));
                        apptDetails.setScheduleId(rs.getLong("scheduleId"));
                        apptDetails.setConfirmationNumber(rs.getLong("conf_number"));
                        apptDetails.setListDocsScript(getTransScriptMsgs(jdbcCustomTemplate, logger, rs.getLong("scheduleId")));
                        apptList.add(apptDetails);
                    }
                    return (long) 0;
                }
            });
        } catch (DataAccessException dae) {
            throw new TelAppointException(ErrorConstants.ERROR_1051.getCode(), ErrorConstants.ERROR_1051.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, dae.getMessage(), "CustomerId:[" + customerId + "]");
        }
    }

    @Override
    public void getI18nAliasesMap(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, final Map<String, Map<String, String>> map) throws TelAppointException, Exception {
        String sql = "select device,lang,message_key,message_value from i18n_aliases";
        populateMap(jdbcCustomTemplate, logger, sql, map);
    }

    private void populateMap(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String sql, final Map<String, Map<String, String>> map) throws TelAppointException, Exception {
        try {
            jdbcCustomTemplate.getJdbcTemplate().query(sql, new ResultSetExtractor<Map<String, Map<String, String>>>() {
                @Override
                public Map<String, Map<String, String>> extractData(ResultSet rs) throws SQLException, DataAccessException {
                    Map<String, String> subMap;
                    StringBuilder key = new StringBuilder();
                    while (rs.next()) {
                        key.append(rs.getString("device")).append("|").append(rs.getString("lang"));
                        if (map.containsKey(key.toString())) {
                            subMap = map.get(key.toString());
                            subMap.put(rs.getString("message_key"), rs.getString("message_value"));
                        } else {
                            subMap = new HashMap<String, String>();
                            subMap.put(rs.getString("message_key"), rs.getString("message_value"));
                            map.put(key.toString(), subMap);
                        }
                        key.setLength(0);
                    }
                    return map;
                }
            });
        } catch (DataAccessException dae) {
            throw new TelAppointException(ErrorConstants.ERROR_1072.getCode(), ErrorConstants.ERROR_1072.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, dae.getMessage(), "");
        }
    }

    @Override
    public void getI18nDesignTemplatesMap(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, final Map<String, Map<String, String>> map) throws TelAppointException {
        String sql = "select device,message_key,message_value from i18n_design_templates";
        try {
            jdbcCustomTemplate.getJdbcTemplate().query(sql, new ResultSetExtractor<Map<String, Map<String, String>>>() {
                @Override
                public Map<String, Map<String, String>> extractData(ResultSet rs) throws SQLException, DataAccessException {
                    Map<String, String> subMap = null;
                    StringBuilder key = new StringBuilder();
                    while (rs.next()) {
                        key.append(rs.getString("device"));
                        if (map.containsKey(key.toString())) {
                            subMap = map.get(key.toString());
                            subMap.put(rs.getString("message_key"), rs.getString("message_value"));
                        } else {
                            subMap = new HashMap<String, String>();
                            subMap.put(rs.getString("message_key"), rs.getString("message_value"));
                            map.put(key.toString(), subMap);
                        }
                        key.setLength(0);
                    }
                    return map;
                }
            });
        } catch (DataAccessException dae) {
            throw new TelAppointException(ErrorConstants.ERROR_1071.getCode(), ErrorConstants.ERROR_1071.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, dae.getMessage(), "");
        }
    }

    @Override
    public void getI18nButtonsMap(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, final Map<String, Map<String, String>> map) throws TelAppointException, Exception {
        String sql = "select device,lang,message_key,message_value from i18n_display_button_names";
        populateMap(jdbcCustomTemplate, logger, sql, map);
    }

    @Override
    public void getI18nDisplayFieldLabelsMap(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, final Map<String, Map<String, String>> map) throws TelAppointException, Exception {
        String sql = "select device,lang,message_key,message_value from i18n_display_field_labels";
        populateMap(jdbcCustomTemplate, logger, sql, map);
    }

    @Override
    public void getI18nPageContentMap(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, final Map<String, Map<String, String>> map) throws TelAppointException, Exception {
        String sql = "select device,lang,message_key,message_value from i18n_display_page_content";
        populateMap(jdbcCustomTemplate, logger, sql, map);
    }

    @Override
    public void getI18nEmailTemplateMap(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, final Map<String, Map<String, String>> map) throws TelAppointException, Exception {
        String sql = "select lang,message_key,message_value from i18n_email_templates";
        jdbcCustomTemplate.getJdbcTemplate().query(sql, new ResultSetExtractor<Map<String, Map<String, String>>>() {
            @Override
            public Map<String, Map<String, String>> extractData(ResultSet rs) throws SQLException, DataAccessException {
                Map<String, String> subMap = null;
                StringBuilder key = new StringBuilder();
                while (rs.next()) {
                    key.append(rs.getString("lang"));
                    if (map.containsKey(key.toString())) {
                        subMap = map.get(key.toString());
                        subMap.put(rs.getString("message_key"), rs.getString("message_value"));
                    } else {
                        subMap = new HashMap<String, String>();
                        subMap.put(rs.getString("message_key"), rs.getString("message_value"));
                        map.put(key.toString(), subMap);
                    }
                    key.setLength(0);
                }
                return map;
            }
        });
    }

    @Override
    public List<Language> getLangDetails(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, final List<Language> languageList) throws TelAppointException, Exception {
        String sql = "select * from i18n_lang ";
        try {
            return jdbcCustomTemplate.getJdbcTemplate().query(sql, new ResultSetExtractor<List<Language>>() {
                @Override
                public List<Language> extractData(ResultSet rs) throws SQLException, DataAccessException {
                    Language language;
                    while (rs.next()) {
                        language = new Language(Integer.toString(rs.getInt("id")),
                                rs.getString("lang_code"), rs.getString("language"), rs.getString("default_lang").charAt(0), rs
                                .getString("lang_link"), rs.getString("voice_name"));
                        languageList.add(language);
                    }
                    return languageList;
                }
            });
        } catch (DataAccessException dae) {
            throw new TelAppointException(ErrorConstants.ERROR_1073.getCode(), ErrorConstants.ERROR_1073.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, dae.getMessage(), null);
        }
    }

    @Override
    public Language getDefaultLangCode(JdbcCustomTemplate jdbcCustomTemplate, Logger logger) throws TelAppointException, Exception {
        String sql = "select * from i18n_lang where default_lang = 'Y'";
        try {
            return jdbcCustomTemplate.getJdbcTemplate().query(sql, new ResultSetExtractor<Language>() {
                @Override
                public Language extractData(ResultSet rs) throws SQLException, DataAccessException {
                    Language language = null;
                    if (rs.next()) {
                        language = new Language(Integer.toString(rs.getInt("id")),
                                rs.getString("lang_code"), rs.getString("language"), rs.getString("default_lang").charAt(0), rs
                                .getString("lang_link"), rs.getString("voice_name"));

                    }
                    return language;
                }
            });
        } catch (DataAccessException dae) {
            throw new TelAppointException(ErrorConstants.ERROR_1068.getCode(), ErrorConstants.ERROR_1068.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, dae.getMessage(), null);
        }
    }

    @Override
    public List<Options> getProcedure(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, final String device, final List<Options> procedureOptions, Map<String, String> aliasMap, Map<String, String> labelMap) throws TelAppointException, Exception {
        StringBuilder sql = new StringBuilder("select id,procedure_name_online,procedure_name_mobile, procedure_name_ivr_tts,procedure_name_ivr_audio, warning_msg,");
        sql.append("warning_msg_tts, warning_msg_audio, error_msg, error_msg_tts, error_msg_audio");
        sql.append(" from `procedure` where id > 0 ");
        sql.append("admin".equals(device) ? " " : " and delete_flag = 'N'");
        sql.append(" order by placement asc");
        logger.debug("getProcedureList SQL: " + sql.toString());
        try {
            return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new ResultSetExtractor<List<Options>>() {
                @Override
                public List<com.telappoint.apptdesk.model.Options> extractData(ResultSet rs) throws SQLException, DataAccessException {
                    Options e;
                    while (rs.next()) {
                        e = new com.telappoint.apptdesk.model.Options();
                        e.setOptionKey(Integer.toString(rs.getInt("id")));
                        if (CoreUtils.isOnline(device)) {
                            e.setOptionValue(rs.getString("procedure_name_online"));
                            if (labelMap != null) {
                                e.setWarningMsg(labelMap.get(rs.getString("warning_msg")));
                                e.setErrorMsg(labelMap.get(rs.getString("error_msg")));
                            } else {
                                e.setWarningMsg(rs.getString("warning_msg"));
                                e.setErrorMsg(rs.getString("error_msg"));
                            }
                        } else if (CoreUtils.isIVR(device)) {
                            e.setOptionTTS(rs.getString("procedure_name_ivr_tts") == null ? "" : rs.getString("procedure_name_ivr_tts"));
                            e.setOptionAudio(rs.getString("procedure_name_ivr_audio") == null ? "" : rs.getString("procedure_name_ivr_audio"));
                            e.setWarningMsgAudio(rs.getString("warning_msg_audio") == null ? "" : rs.getString("warning_msg_audio"));
                            e.setWarningMsgTTS(rs.getString("warning_msg_tts") == null ? "" : rs.getString("warning_msg_tts"));
                            e.setErrorMsgAudio(rs.getString("error_msg_audio") == null ? "" : rs.getString("error_msg_audio"));
                            e.setErrorMsgTts(rs.getString("error_msg_tts") == null ? "" : rs.getString("error_msg_tts"));
                        } else if (CoreUtils.isMobile(device)) {
                            e.setOptionValue(rs.getString("procedure_name_mobile"));
                            e.setWarningMsg(rs.getString("warning_msg"));
                            e.setErrorMsg(rs.getString("error_msg"));

                        }
                        procedureOptions.add(e);
                    }
                    return procedureOptions;
                }
            });
        } catch (DataAccessException e) {
            throw new TelAppointException(ErrorConstants.ERROR_1029.getCode(), ErrorConstants.ERROR_1029.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    @Override
    public Procedure  getProcedureByName(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String procedureName, String device) throws TelAppointException, Exception {
        StringBuilder sql = new StringBuilder("select id from `procedure` where procedure_name_online = ?");
        if (CoreUtils.isMobile(device)) {
            sql = new StringBuilder("select id from `procedure` where procedure_name_mobile = ?");
        }

        try {
            return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[]{procedureName}, new ResultSetExtractor<Procedure>() {
                @Override
                public Procedure extractData(ResultSet rs) throws SQLException, DataAccessException {
                    Procedure procedure = null;
                    while (rs.next()) {
                        procedure = new Procedure();
                        procedure.setProcedureId(rs.getInt("id"));
                    }
                    return procedure;
                }
            });


        } catch (DataAccessException dae) {
            throw new TelAppointException(ErrorConstants.ERROR_1029.getCode(), ErrorConstants.ERROR_1029.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, dae.getMessage(), null);
        }
    }

    @Override
    public void loadVXML(final JdbcCustomTemplate jdbcCustomTemplate, final String mainKey, final Map<String, Map<String, IVRXml>> cacheMap) throws TelAppointException, Exception {
        String sql = "select id, page_name,lang_code,vxml from ivr_vxml where enabled='Y'";

        try {
            jdbcCustomTemplate.getJdbcTemplate().query(sql, new ResultSetExtractor<Map<String, Map<String, IVRXml>>>() {
                @Override
                public Map<String, Map<String, IVRXml>> extractData(ResultSet rs) throws SQLException, DataAccessException {
                    Map<String, IVRXml> map = new HashMap<String, IVRXml>();
                    StringBuilder key = new StringBuilder();
                    String pageName;
                    String langCode;
                    IVRXml ivrXML;
                    while (rs.next()) {
                        ivrXML = new IVRXml();
                        pageName = rs.getString("page_name");
                        langCode = rs.getString("lang_code");
                        ivrXML.setPageName(pageName);
                        ivrXML.setVxml(rs.getString("vxml"));
                        ivrXML.setIvrXmlId(rs.getLong("id"));
                        key.append(pageName).append("|").append(langCode);
                        map.put(key.toString(), ivrXML);
                        key.setLength(0);
                    }
                    cacheMap.put(mainKey, map);
                    return cacheMap;
                }
            });
            return;
        } catch (DataAccessException dae) {
            throw new TelAppointException(ErrorConstants.ERROR_1028.getCode(), ErrorConstants.ERROR_1028.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, dae.getMessage(), null);
        }
    }

    @Override
    public void getIVRVxml(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, Map<String, IVRXml> ivrxmlMap) throws TelAppointException, Exception {
        String sql = "select id,page_name,lang_code,vxml from ivr_vxml where enabled='Y' order by placement";
        try {
            jdbcCustomTemplate.getJdbcTemplate().query(sql, new ResultSetExtractor<Map<String, IVRXml>>() {
                @Override
                public Map<String, IVRXml> extractData(ResultSet rs) throws SQLException, DataAccessException {
                    long id = 0;
                    String pageName;
                    String langCode;
                    IVRXml ivrXML;
                    while (rs.next()) {
                        ivrXML = new IVRXml();
                        id = rs.getLong("id");
                        pageName = rs.getString("page_name");
                        langCode = rs.getString("lang_code");
                        ivrXML.setIvrXmlId(id);
                        ivrXML.setPageName(pageName);
                        ivrXML.setLangCode(langCode);
                        ivrXML.setVxml(rs.getString("vxml"));
                        ivrxmlMap.put(pageName, ivrXML);
                    }
                    return ivrxmlMap;
                }
            });
            return;
        } catch (DataAccessException dae) {
            throw new TelAppointException(ErrorConstants.ERROR_1028.getCode(), ErrorConstants.ERROR_1028.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, dae.getMessage(), null);
        }
    }

    @Override
    public void getProcedureNoMatchMap(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, Map<String, Map<String, String>> map) throws TelAppointException, Exception {
        String sql = "select lang_code,procedure_name,message from procedure_no_match";
        try {
            jdbcCustomTemplate.getJdbcTemplate().query(sql, new ResultSetExtractor<Map<String, Map<String, String>>>() {
                @Override
                public Map<String, Map<String, String>> extractData(ResultSet rs) throws SQLException, DataAccessException {
                    Map<String, String> subMap;
                    StringBuilder key = new StringBuilder();
                    while (rs.next()) {
                        key.append(rs.getString("lang_code"));
                        if (map.containsKey(key.toString())) {
                            subMap = map.get(key.toString());
                            subMap.put(rs.getString("procedure_name"), rs.getString("message"));
                        } else {
                            subMap = new HashMap<String, String>();
                            subMap.put(rs.getString("procedure_name"), rs.getString("message"));
                            map.put(key.toString(), subMap);
                        }
                        key.setLength(0);
                    }
                    return map;
                }
            });
        } catch (DataAccessException dae) {
            throw new TelAppointException(ErrorConstants.ERROR_1061.getCode(), ErrorConstants.ERROR_1061.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, dae.getMessage(), null);
        }
    }

    @Override
    public void getLocationIdList(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String procedureId, List<Integer> locationIds) throws TelAppointException, Exception {
        String sql = "select distinct location_id from procedure_location where procedure_id=? order by placement asc";
        jdbcCustomTemplate.getJdbcTemplate().query(sql, new Object[]{Integer.valueOf(procedureId)}, new ResultSetExtractor<Long>() {
            @Override
            public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
                while (rs.next()) {
                    locationIds.add(rs.getInt("location_id"));
                }
                return (long) 0;
            }
        });
    }

    @Override
    public Location getLocationById(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String device, Integer locationId) throws TelAppointException {
        StringBuilder sql = new StringBuilder("select id,location_name_online,location_name_mobile, location_name_ivr_tts,location_name_ivr_audio,address,");
        sql.append("city,state,zip,work_phone,time_zone, closed,closed_message, enable, location_google_map, location_google_map_link, closed_audio, closed_tts ");
        sql.append(" from location");
        sql.append(" where id=?");
        logger.debug("getLocationById query: " + sql.toString());
        try {
            return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[]{locationId}, new ResultSetExtractor<Location>() {
                @Override
                public Location extractData(ResultSet rs) throws SQLException, DataAccessException {
                    Location location = null;
                    if (rs.next()) {
                        location = new Location();
                        location.setLocationId(rs.getInt("id"));
                        location.setLocationName(rs.getString("location_name_online"));
                        location.setTimeZone(rs.getString("time_zone"));
                        location.setAddress(rs.getString("address"));
                        location.setClosed(rs.getString("closed"));

                        if (CoreUtils.isOnline(device)) {
                            location.setLocationErrorCode(rs.getString("closed_message"));
                            location.setLocationGoogleMap(rs.getString("location_google_map"));
                            location.setLocationGoogleMapLink(rs.getString("location_google_map_link"));
                            location.setCity(rs.getString("city"));
                            location.setState(rs.getString("state"));
                            location.setWorkPhone(rs.getString("work_phone"));
                            location.setZip(rs.getString("zip"));
                        } else if (CoreUtils.isIVR(device)) {
                            location.setLocationNameIvrTts(rs.getString("location_name_ivr_tts"));
                            location.setLocationNameIvrAudio(rs.getString("location_name_ivr_audio"));
                            location.setClosedTts(rs.getString("closed_tts"));
                            location.setClosedAudio(rs.getString("closed_audio"));
                        } else if (CoreUtils.isMobile(device)) {
                            location.setLocationName(rs.getString("location_name_mobile"));
                            location.setCity(rs.getString("city"));
                            location.setState(rs.getString("state"));
                            location.setWorkPhone(rs.getString("work_phone"));
                            location.setZip(rs.getString("zip"));
                        } else {
                            location.setEnable(rs.getString("enable"));
                            location.setEnable(null);
                        }
                    }
                    return location;
                }
            });
        } catch (DataAccessException dae) {
            throw new TelAppointException(ErrorConstants.ERROR_1031.getCode(), ErrorConstants.ERROR_1031.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, dae.getMessage(), null);
        }
    }

    @Override
    public List<OnlinePageFields> getOnlinePageFieldsByPageFieldIds(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, List<Integer> fieldIds) throws TelAppointException, Exception {
        String sql = "select * from online_page_fields where id in (:fieldIds) order by placement";
        try {
            MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
            mapSqlParameterSource.addValue("fieldIds", fieldIds);
            return jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql, mapSqlParameterSource, new ResultSetExtractor<List<OnlinePageFields>>() {
                final List<OnlinePageFields> onlinePageFieldList = new ArrayList<OnlinePageFields>();

                @Override
                public final List<OnlinePageFields> extractData(ResultSet rs) throws SQLException, DataAccessException {
                    OnlinePageFields onlinePageFields;
                    while (rs.next()) {
                        onlinePageFields = new OnlinePageFields();
                        int onlinePageContentId = rs.getInt("online_page_content_id");
                        onlinePageFields.setFieldId(rs.getInt("id"));
                        onlinePageFields.setOnlinePageContentId(onlinePageContentId);
                        onlinePageFields.setFieldName(rs.getString("field_name"));
                        onlinePageFields.setDisplayType(rs.getString("display_type"));
                        onlinePageFields.setDisplay(rs.getString("display"));
                        onlinePageFields.setJavaRef(rs.getString("java_reflection"));
                        onlinePageFields.setLoginType(rs.getString("login_type"));
                        onlinePageFields.setParamColumn(rs.getString("param_column"));
                        onlinePageFields.setStorageSize(rs.getInt("storage_size"));
                        onlinePageFields.setStorageType(rs.getString("storage_type"));
                        onlinePageFields.setShowData(rs.getString("show_data"));
                        onlinePageFields.setEmptyErrorMsg(rs.getString("empty_error_msg"));
                        onlinePageFields.setInvalidErrorMsg(rs.getString("invalid_error_msg"));
                        onlinePageFields.setRequired(rs.getString("required"));
                        onlinePageFields.setValidateRequired(rs.getString("validate_required"));
                        onlinePageFields.setValidationRuleAPI(rs.getString("validation_rules_api"));
                        onlinePageFields.setValidateMinChars(rs.getString("validate_min_chars"));
                        onlinePageFields.setValidateMaxChars(rs.getString("validate_max_chars"));
                        onlinePageFields.setInitialValues(rs.getString("initial_values"));
                        onlinePageFieldList.add(onlinePageFields);
                    }
                    return onlinePageFieldList;
                }
            });
        } catch (DataAccessException dae) {
            throw new TelAppointException(ErrorConstants.ERROR_1032.getCode(), ErrorConstants.ERROR_1032.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, dae.getMessage(), "FieldIds::" + fieldIds.toString());
        }
    }

    @Override
    public List<IVRPageFields> getIVRPageFieldsByPageFieldIds(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, List<Integer> fieldIds) throws TelAppointException, Exception {
        String sql = "select * from ivr_page_fields where id in (:fieldIds) order by placement";
        try {
            MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
            mapSqlParameterSource.addValue("fieldIds", fieldIds);
            return jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql, mapSqlParameterSource, new ResultSetExtractor<List<IVRPageFields>>() {
                final List<IVRPageFields> ivrPageFieldList = new ArrayList<IVRPageFields>();

                @Override
                public final List<IVRPageFields> extractData(ResultSet rs) throws SQLException, DataAccessException {
                    IVRPageFields ivrPageFields = null;
                    while (rs.next()) {
                        ivrPageFields = new IVRPageFields();
                        ivrPageFields.setFieldId(rs.getInt("id"));
                        ivrPageFields.setFieldName(rs.getString("field_name"));
                        ivrPageFields.setLoginType(rs.getString("login_type"));
                        ivrPageFields.setParamColumn(rs.getString("param_column"));
                        ivrPageFields.setJavaRef(rs.getString("java_reflection"));
                        ivrPageFields.setStorageSize(rs.getInt("storage_size"));
                        ivrPageFields.setStorageType(rs.getString("storage_type"));
                        ivrPageFieldList.add(ivrPageFields);
                    }
                    return ivrPageFieldList;
                }
            });
        } catch (DataAccessException dae) {
            throw new TelAppointException(ErrorConstants.ERROR_1032.getCode(), ErrorConstants.ERROR_1032.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, dae.getMessage(), "FieldIds::" + fieldIds.toString());
        }
    }
    
    
    @Override
    public List<LoginPageFields> getMobilePageFieldsByPageFieldIds(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, List<Integer> fieldIds) throws Exception {
        String sql = "select * from login_param_config where id in (:fieldIds) and device_type=:deviceType order by placement";
        try {
            MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
            mapSqlParameterSource.addValue("fieldIds", fieldIds);
            mapSqlParameterSource.addValue("deviceType", CommonApptDeskConstants.MOBILE.getValue());
            return jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql, mapSqlParameterSource, new ResultSetExtractor<List<LoginPageFields>>() {
                final List<LoginPageFields> mobilePageFieldList = new ArrayList<>();

                @Override
                public final List<LoginPageFields> extractData(ResultSet rs) throws SQLException, DataAccessException {
                	LoginPageFields mobilePageFields = null;
                    while (rs.next()) {
                    	mobilePageFields = new LoginPageFields();
                    	mobilePageFields.setFieldId(rs.getInt("id"));
                    	mobilePageFields.setFieldName(rs.getString("field_name"));
                    	mobilePageFields.setLoginType(rs.getString("login_type"));
                    	mobilePageFields.setParamColumn(rs.getString("param_column"));
                    	mobilePageFields.setStorageSize(rs.getInt("storage_size"));
                    	mobilePageFields.setStorageType(rs.getString("storage_type"));
                    	mobilePageFields.setJavaRef(rs.getString("java_reflection"));
                    	mobilePageFieldList.add(mobilePageFields);
                    }
                    return mobilePageFieldList;
                }
            });
        } catch (DataAccessException dae) {
            throw new TelAppointException(ErrorConstants.ERROR_1032.getCode(), ErrorConstants.ERROR_1032.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, dae.getMessage(), "FieldIds::" + fieldIds.toString());
        }
    }

    private Integer getServiceFundReceivedIdByCustomer(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, Long customerId) throws TelAppointException, Exception {
        StringBuilder sql = new StringBuilder("select sfr.id from service_funds_received sfr, customer cu where cu.id = ?");
        sql.append(" and sfr.liheap_fund = cu.liheap_fund");
        sql.append(" and sfr.psehelp_fund = cu.psehelp_fund");
        try {
            return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[]{customerId}, new ResultSetExtractor<Integer>() {
                int serviceFundsRecievedId = 0;

                @Override
                public final Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
                    if (rs.next()) {
                        serviceFundsRecievedId = rs.getInt("id");
                    }
                    return serviceFundsRecievedId;
                }
            });
        } catch (DataAccessException dae) {
            throw new TelAppointException(ErrorConstants.ERROR_1041.getCode(), ErrorConstants.ERROR_1041.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, dae.getMessage(), "CustomerId:" + customerId);
        }
    }

    private void fillErrorPageData(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String device, String sql, MapSqlParameterSource mapSQLParameterSource,
                                   ServiceFundsResponse serviceFundsResponse, Map<String, String> labelMap) throws TelAppointException, Exception {
        try {
            jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), mapSQLParameterSource, new ResultSetExtractor<String>() {
                @Override
                public final String extractData(ResultSet rs) throws SQLException, DataAccessException {
                    while (rs.next()) {
                        String errorPage = rs.getString("error_page");
                        if (!"".equals(errorPage)) {
                            serviceFundsResponse.setErrorFlag("Y");
                            if (CoreUtils.isOnline(device) || CoreUtils.isMobile(device)) {
                                if (labelMap != null) {
                                    serviceFundsResponse.setErrorPage(labelMap.get(errorPage) == null ? ErrorConstants.ERROR_2996.getMessage() : labelMap.get(errorPage));
                                } else {
                                    serviceFundsResponse.setErrorPage(ErrorConstants.ERROR_2996.getMessage());
                                }
                            } else if (CoreUtils.isIVR(device)) {
                                serviceFundsResponse.setErrorPage(errorPage);
                            }
                            break;
                        }
                    }
                    return "";
                }
            });
        } catch (DataAccessException dae) {
            throw new TelAppointException(ErrorConstants.ERROR_1042.getCode(), ErrorConstants.ERROR_1042.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, dae.getMessage(), null);
        }
    }

    private void fillWarningPageData(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String device, String sql, MapSqlParameterSource mapSQLParameterSource,
                                     ServiceFundsResponse serviceFundsResponse, Map<String, String> labelMap) throws TelAppointException, Exception {
        try {
            jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), mapSQLParameterSource, new ResultSetExtractor<String>() {
                @Override
                public final String extractData(ResultSet rs) throws SQLException, DataAccessException {
                    while (rs.next()) {
                        String warningPage = rs.getString("warning_page");
                        if (!"".equals(warningPage)) {
                            serviceFundsResponse.setWarningFlag("Y");
                            if (CoreUtils.isOnline(device)) {
                                if (labelMap != null) {
                                    serviceFundsResponse.setWarningPage(labelMap.get(warningPage) == null ? ErrorConstants.ERROR_2996.getMessage() : labelMap.get(warningPage));
                                } else {
                                    serviceFundsResponse.setWarningPage(ErrorConstants.ERROR_2996.getMessage());
                                }
                            } else if (CoreUtils.isIVR(device)) {
                                serviceFundsResponse.setWarningPage(warningPage);
                            }
                            break;
                        }
                    }
                    return "";
                }
            });
        } catch (DataAccessException dae) {
            throw new TelAppointException(ErrorConstants.ERROR_1042.getCode(), ErrorConstants.ERROR_1042.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, dae.getMessage(), null);
        }
    }

    private void prepareCustomerTypeData(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String device, Long customerId, ServiceFundsResponse customerTypeResponse, Map<String, String> aliasMap)
            throws TelAppointException, Exception {
        Integer serviceFundsRcvdId = getServiceFundReceivedIdByCustomer(jdbcCustomTemplate, logger, customerId);
        customerTypeResponse.setServiceFundsRcvdId(serviceFundsRcvdId);
        StringBuilder sql = new StringBuilder();
        sql.append("select id,item, ivr_tts as tts,ivr_audio as audio from service_customer_type where id in ");
        sql.append("(").append("select distinct customer_type_id from service_funds_customer_utility_type");
        sql.append(" where service_funds_rcvd_id = ?").append(") order by id asc");
        try {
            jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[]{serviceFundsRcvdId}, new ResultSetExtractor<String>() {
                @Override
                public final String extractData(ResultSet rs) throws SQLException, DataAccessException {
                    List<Integer> ids = new ArrayList<Integer>();
                    List<String> values = new ArrayList<String>();
                    List<String> audioList = null;
                    List<String> ttsList = null;
                    if (CoreUtils.isIVR(device)) {
                        ttsList = new ArrayList<String>();
                        audioList = new ArrayList<String>();
                    }
                    while (rs.next()) {
                        ids.add(rs.getInt("id"));
                        if (aliasMap != null) {
                            values.add(aliasMap.get(rs.getString("item")));
                        } else {
                            values.add(rs.getString("item"));
                        }

                        if (CoreUtils.isIVR(device)) {
                            ttsList.add(rs.getString("tts"));
                            audioList.add(rs.getString("audio"));
                        }
                    }
                    customerTypeResponse.setIds(ids);
                    customerTypeResponse.setValues(values);
                    customerTypeResponse.setAudioList(audioList);
                    customerTypeResponse.setTtsList(ttsList);
                    customerTypeResponse.setCategoryType("customer_type");
                    return "";
                }
            });
        } catch (DataAccessException dae) {
            throw new TelAppointException(ErrorConstants.ERROR_1042.getCode(), ErrorConstants.ERROR_1042.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, dae.getMessage(), "CustomerId:" + customerId);
        }
    }

    private void prepareUtilityTypeData(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String device, String sql, MapSqlParameterSource mapSqlParameterSource,
                                        ServiceFundsResponse customerTypeResponse, Map<String, String> aliasMap) throws TelAppointException, Exception {
        try {
            logger.debug("PrepareUtilityTypeData sql: " + sql.toString());
            jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), mapSqlParameterSource, new ResultSetExtractor<String>() {

                @Override
                public final String extractData(ResultSet rs) throws SQLException, DataAccessException {
                    List<Integer> ids = new ArrayList<Integer>();
                    List<String> values = new ArrayList<String>();
                    List<String> audioList = null;
                    List<String> ttsList = null;
                    if (CoreUtils.isIVR(device)) {
                        ttsList = new ArrayList<String>();
                        audioList = new ArrayList<String>();
                    }

                    while (rs.next()) {
                        ids.add(rs.getInt("id"));
                        if (aliasMap != null) {
                            values.add(aliasMap.get(rs.getString("item")));
                        } else {
                            values.add(rs.getString("item"));
                        }

                        if (CoreUtils.isIVR(device)) {
                            ttsList.add(rs.getString("tts"));
                            audioList.add(rs.getString("audio"));
                        }
                    }
                    customerTypeResponse.setIds(ids);
                    customerTypeResponse.setValues(values);
                    customerTypeResponse.setAudioList(audioList);
                    customerTypeResponse.setTtsList(ttsList);
                    customerTypeResponse.setCategoryType("utility_type");
                    return "";
                }
            });
        } catch (DataAccessException dae) {
            throw new TelAppointException(ErrorConstants.ERROR_1042.getCode(), ErrorConstants.ERROR_1042.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, dae.getMessage(), null);
        }
    }

    public void prepareServiceTypeData(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String device, String sql, MapSqlParameterSource mapSqlSource,
                                       ServiceFundsResponse serviceFundsReponse, Map<String, String> aliasMap) throws TelAppointException, Exception {
        try {
        	
            jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), mapSqlSource, new ResultSetExtractor<String>() {
                @Override
                public final String extractData(ResultSet rs) throws SQLException, DataAccessException {
                    List<Integer> ids = new ArrayList<Integer>();
                    List<String> values = new ArrayList<String>();
                    List<String> audioList = null;
                    List<String> ttsList = null;
                    if (CoreUtils.isIVR(device)) {
                        ttsList = new ArrayList<String>();
                        audioList = new ArrayList<String>();
                    }
                    while (rs.next()) {
                        ids.add(rs.getInt("id"));
                        if (aliasMap != null) {
                            values.add(aliasMap.get(rs.getString("item")));
                        } else {
                            values.add(rs.getString("item"));
                        }

                        if (CoreUtils.isIVR(device)) {
                            ttsList.add(rs.getString("tts"));
                            audioList.add(rs.getString("audio"));
                        }
                    }
                    serviceFundsReponse.setIds(ids);
                    serviceFundsReponse.setValues(values);
                    serviceFundsReponse.setAudioList(audioList);
                    serviceFundsReponse.setTtsList(ttsList);
                    serviceFundsReponse.setCategoryType("service");
                    return "";
                }
            });
        } catch (DataAccessException dae) {
            throw new TelAppointException(ErrorConstants.ERROR_1042.getCode(), ErrorConstants.ERROR_1042.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, dae.getMessage(), null);
        }
    }

    @Override
    public void getCustomerType(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String device, Long customerId, Integer locationId, ServiceFundsResponse serviceFundsResponse, Map<String, String> aliasMap, Map<String, String> labelMap) throws TelAppointException, Exception {
        prepareCustomerTypeData(jdbcCustomTemplate, logger, device, customerId, serviceFundsResponse, aliasMap);
        try {
            List<Integer> ids = serviceFundsResponse.getIds();
            if (ids != null && ids.isEmpty()) {
                Integer serviceFundsRcvdId = getServiceFundReceivedIdByCustomer(jdbcCustomTemplate, logger, customerId);
                serviceFundsResponse.setServiceFundsRcvdId(serviceFundsRcvdId);
                StringBuilder sql = new StringBuilder();
                sql.append("select id,item as item, ivr_tts as tts,ivr_audio as audio from service_utility_type where id in ");
                sql.append("(").append("select distinct utility_type_id from service_funds_customer_utility_type");
                sql.append(" where service_funds_rcvd_id = :serviceFundsRcvdId").append(")");
                MapSqlParameterSource mapSource = new MapSqlParameterSource();
                mapSource.addValue("serviceFundsRcvdId", serviceFundsRcvdId);
                mapSource.addValue("locationId", locationId);
                prepareUtilityTypeData(jdbcCustomTemplate, logger, device, sql.toString(), mapSource, serviceFundsResponse, aliasMap);

                ids = serviceFundsResponse.getIds();
                if (ids != null && ids.isEmpty()) {
                    sql = new StringBuilder();
                    sql.append("select id,service_name_online as item, service_name_ivr_tts as tts, service_name_ivr_audio as audio from service where id in ");
                    sql.append("(").append("select distinct service_id from service_funds_customer_utility_type");
                    sql.append(" where service_funds_rcvd_id = :serviceFundsRcvdId").append(")");
                    sql.append(" and id in (select distinct ser.id from service ser, resource res, resource_service rs where res.location_id = :locationId and res.delete_flag = 'N'");
                    sql.append(" and res.`enable` = 'Y' and res.allow_selfservice = 'Y' and res.id = rs.resource_id and rs.`enable` = 'Y' and rs.allow_selfservice = 'Y'");
                    sql.append(" and rs.service_id = ser.id and ser.delete_flag = 'N' and ser.enable = 'Y'").append(")");
                    sql.append(" order by id");
                    prepareServiceTypeData(jdbcCustomTemplate, logger, device, sql.toString(), mapSource, serviceFundsResponse, aliasMap);
                    ids = serviceFundsResponse.getIds();
                    if (ids != null && ids.isEmpty()) {
                        sql = new StringBuilder("select distinct error_page from service_funds_customer_utility_type where service_funds_rcvd_id = :serviceFundsRcvd");
                        sql.append(" and error_page IS NOT NULL");
                        mapSource = new MapSqlParameterSource();
                        mapSource.addValue("serviceFundsRcvd", serviceFundsRcvdId);
                        fillErrorPageData(jdbcCustomTemplate, logger, device, sql.toString(), mapSource, serviceFundsResponse, labelMap);
                    } else {
                        logger.debug("service information not found");
                        return;
                    }
                } else {
                    logger.debug("utility information not found");
                    return;
                }
            } else {
                logger.debug("customer type information not found");
                return;
            }
        } catch (DataAccessException dae) {
            StringBuilder data = new StringBuilder();
            data.append("CustomerId:").append(customerId);
            throw new TelAppointException(ErrorConstants.ERROR_1062.getCode(), ErrorConstants.ERROR_1062.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, dae.getMessage(), data.toString());
        }
    }

    @Override
    public void getUtilityType(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String device, Integer serviceFundsRcvdId, Integer customerTypeId, Integer locationId, ServiceFundsResponse serviceFundsResponse, Map<String, String> aliasMap,
                               Map<String, String> labelMap) throws TelAppointException, Exception {
        serviceFundsResponse.setServiceFundsRcvdId(serviceFundsRcvdId);
        serviceFundsResponse.setCustomerTypeId(customerTypeId);
        StringBuilder sql = new StringBuilder();
        sql.append("select id,item as item, ivr_tts as tts,ivr_audio as audio from service_utility_type where id in ");
        sql.append("(").append("select distinct utility_type_id from service_funds_customer_utility_type");
        sql.append(" where service_funds_rcvd_id = :serviceFundsRcvdId and customer_type_id=:customerTypeId").append(")");
        sql.append(" order by id asc");
        try {
            MapSqlParameterSource mapSource = new MapSqlParameterSource();
            mapSource.addValue("serviceFundsRcvdId", serviceFundsRcvdId);
            mapSource.addValue("customerTypeId", customerTypeId);
            mapSource.addValue("locationId", locationId);
            
            prepareUtilityTypeData(jdbcCustomTemplate, logger, device, sql.toString(), mapSource, serviceFundsResponse, aliasMap);
            if (serviceFundsResponse.getIds().isEmpty()) {
                sql = new StringBuilder();
                sql.append("select id,service_name_online as item, service_name_ivr_tts as tts, service_name_ivr_audio as audio from service where id in ");
                sql.append("(").append("select distinct service_id from service_funds_customer_utility_type");
                sql.append(" where service_funds_rcvd_id = :serviceFundsRcvdId and customer_type_id=:customerTypeId").append(")");
                sql.append(" and id in (select distinct ser.id from service ser, resource res, resource_service rs where res.location_id = :locationId and res.delete_flag = 'N'");
                sql.append(" and res.`enable` = 'Y' and res.allow_selfservice = 'Y' and res.id = rs.resource_id and rs.`enable` = 'Y' and rs.allow_selfservice = 'Y'");
                sql.append(" and rs.service_id = ser.id and ser.delete_flag = 'N' and ser.enable = 'Y'").append(")");
                sql.append(" order by id asc");
                prepareServiceTypeData(jdbcCustomTemplate, logger, device, sql.toString(), mapSource, serviceFundsResponse, aliasMap);
                if (serviceFundsResponse.getIds().isEmpty()) {
                    sql = new StringBuilder("select distinct error_page from service_funds_customer_utility_type where service_funds_rcvd_id = :serviceFundsRcvd");
                    sql.append(" and customer_type_id=:customerTypeId");
                    sql.append(" and error_page IS NOT NULL");
                    mapSource = new MapSqlParameterSource();
                    mapSource.addValue("serviceFundsRcvd", serviceFundsRcvdId);
                    mapSource.addValue("customerTypeId", customerTypeId);
                    fillErrorPageData(jdbcCustomTemplate, logger, device, sql.toString(), mapSource, serviceFundsResponse, labelMap);
                } else {
                    logger.debug("service information found");
                    return;
                }
            } else {
                logger.debug("utility information found");
                return;
            }
        } catch (DataAccessException dae) {
            StringBuilder data = new StringBuilder();
            data.append("ServiceFundsRcvdId:").append(serviceFundsRcvdId);
            data.append("CustomerTypeId:").append(customerTypeId);
            throw new TelAppointException(ErrorConstants.ERROR_1063.getCode(), ErrorConstants.ERROR_1063.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, dae.getMessage(), data.toString());
        }
    }
    
    @Override
    public void getServiceNameCustomerUtility(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String device, Integer serviceFundsRcvdId, Integer customerTypeId, Integer locationId, Integer utilityTypeId,
                                              ServiceFundsResponse serviceFundsResponse, Map<String, String> aliasMap, Map<String, String> labelMap) throws TelAppointException, Exception {
        serviceFundsResponse.setServiceFundsRcvdId(serviceFundsRcvdId);
        serviceFundsResponse.setCustomerTypeId(customerTypeId);
        serviceFundsResponse.setUtilityTypeId(utilityTypeId);
        try {
            MapSqlParameterSource mapSource = new MapSqlParameterSource();
            mapSource.addValue("serviceFundsRcvdId", serviceFundsRcvdId);
            mapSource.addValue("customerTypeId", customerTypeId);
            mapSource.addValue("utilityTypeId", utilityTypeId);
            mapSource.addValue("locationId", locationId);
            StringBuilder sql = new StringBuilder();
            sql.append("select id,service_name_online as item, service_name_ivr_tts as tts, service_name_ivr_audio as audio from service where id in ");
            sql.append("(").append("select distinct service_id from service_funds_customer_utility_type");
            sql.append(" where service_funds_rcvd_id = :serviceFundsRcvdId and customer_type_id=:customerTypeId and utility_type_id=:utilityTypeId").append(")");
            sql.append(" and id in (select distinct ser.id from service ser, resource res, resource_service rs where res.location_id = :locationId and res.delete_flag = 'N'");
            sql.append(" and res.`enable` = 'Y' and res.allow_selfservice = 'Y' and res.id = rs.resource_id and rs.`enable` = 'Y' and rs.allow_selfservice = 'Y'");
            sql.append(" and rs.service_id = ser.id and ser.delete_flag = 'N' and ser.enable = 'Y'").append(")");
            sql.append(" order by placement");
        
            prepareServiceTypeData(jdbcCustomTemplate, logger, device, sql.toString(), mapSource, serviceFundsResponse, aliasMap);
            if (serviceFundsResponse.getIds().isEmpty()) {
                sql = new StringBuilder("select distinct error_page from service_funds_customer_utility_type where service_funds_rcvd_id = :serviceFundsRcvd");
                sql.append(" and customer_type_id=:customerTypeId");
                sql.append(" and utility_type_id=:utilityTypeId");
                sql.append(" and error_page IS NOT NULL");

                mapSource = new MapSqlParameterSource();
                mapSource.addValue("serviceFundsRcvd", serviceFundsRcvdId);
                mapSource.addValue("customerTypeId", customerTypeId);
                mapSource.addValue("utilityTypeId", utilityTypeId);
                fillErrorPageData(jdbcCustomTemplate, logger, device, sql.toString(), mapSource, serviceFundsResponse, labelMap);
            } else {
                logger.debug("service information found");

                if ("N".equals(serviceFundsResponse.getErrorFlag())) {
                    if (serviceFundsResponse.getIds().size() == 1) {
                        serviceFundsResponse.setServiceId(serviceFundsResponse.getIds().get(0));
                    }
                }
                return;
            }
        } catch (DataAccessException dae) {
            StringBuilder data = new StringBuilder();
            data.append("ServiceFundsRcvdId:").append(serviceFundsRcvdId);
            data.append("CustomerTypeId:").append(customerTypeId);
            data.append("utilityTypeId:").append(utilityTypeId);
            throw new TelAppointException(ErrorConstants.ERROR_1064.getCode(), ErrorConstants.ERROR_1064.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, dae.getMessage(), data.toString());
        }
    }

    @Override
    public void getServiceNameCustomerWarningPage(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String device, Integer serviceFundsRcvdId, Integer customerTypeId, Integer utilityTypeId,
                                                  Integer serviceId, ServiceFundsResponse serviceFundsResponse, Map<String, String> labelMap) throws TelAppointException, Exception {
        serviceFundsResponse.setServiceFundsRcvdId(serviceFundsRcvdId);
        serviceFundsResponse.setCustomerTypeId(customerTypeId);
        serviceFundsResponse.setUtilityTypeId(utilityTypeId);
        serviceFundsResponse.setServiceId(serviceId);
        try {
            MapSqlParameterSource mapSource = new MapSqlParameterSource();
            mapSource.addValue("serviceFundsRcvd", serviceFundsRcvdId);

            StringBuilder sql = new StringBuilder("select distinct warning_page from service_funds_customer_utility_type");
            sql.append(" where 1=1");
            sql.append(" and service_funds_rcvd_id = :serviceFundsRcvd");

            if (customerTypeId != null) {
                mapSource.addValue("customerTypeId", customerTypeId);
                sql.append(" and customer_type_id=:customerTypeId");
            }

            if (utilityTypeId != null) {
                sql.append(" and utility_type_id=:utilityTypeId");
                mapSource.addValue("utilityTypeId", utilityTypeId);
            }

            if (serviceId != null) {
                sql.append(" and service_id=:serviceId");
                mapSource.addValue("serviceId", serviceId);
            }

            sql.append(" and warning_page IS NOT NULL");
            fillWarningPageData(jdbcCustomTemplate, logger, device, sql.toString(), mapSource, serviceFundsResponse, labelMap);
        } catch (DataAccessException dae) {
            StringBuilder data = new StringBuilder();
            data.append("ServiceFundsRcvdId:").append(serviceFundsRcvdId);
            data.append("CustomerTypeId:").append(customerTypeId);
            data.append("utilityTypeId:").append(utilityTypeId);
            data.append("ServiceId:").append(serviceId);
            throw new TelAppointException(ErrorConstants.ERROR_1065.getCode(), ErrorConstants.ERROR_1065.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, dae.getMessage(), data.toString());
        }
    }

    @Override
    public void getServices(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, Map<String, Service> serviceMap) throws TelAppointException, Exception {
        StringBuilder sql = new StringBuilder();
        sql.append("select * from service where delete_flag = 'N' order by placement");

        try {
            jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new ResultSetExtractor<String>() {
                @Override
                public final String extractData(ResultSet rs) throws SQLException, DataAccessException {
                    Service service;
                    while (rs.next()) {
                        service = new Service();
                        service.setServiceId(rs.getInt("id"));
                        service.setServiceNameOnline(rs.getString("service_name_online"));
                        service.setServiceNameIvrAudio(rs.getString("service_name_ivr_audio"));
                        service.setServiceNameIvrTts(rs.getString("service_name_ivr_tts"));
                        service.setBlocks(rs.getInt("blocks"));
                        service.setBuffer(rs.getInt("buffer"));
                        service.setAllowDuplAppt(rs.getString("allow_duplicate_appt"));
                        service.setSkipDateTime(rs.getString("skip_date_time"));
                        service.setClosed(rs.getString("closed"));
                        service.setClosedMessage(rs.getString("closed_message"));
                        service.setClosedAudio(rs.getString("closed_audio"));
                        service.setClosedTts(rs.getString("closed_tts"));
                        service.setClosedLocationIds(rs.getString("closed_location_ids"));
                        service.setSunOpen(rs.getString("is_sun_open"));
                        service.setMonOpen(rs.getString("is_mon_open"));
                        service.setTueOpen(rs.getString("is_tue_open"));
                        service.setWedOpen(rs.getString("is_wed_open"));
                        service.setThuOpen(rs.getString("is_thu_open"));
                        service.setFriOpen(rs.getString("is_fri_open"));
                        service.setSatOpen(rs.getString("is_sat_open"));
                        service.setApptStartDate(rs.getString("appt_start_date"));
                        service.setApptEndDate(rs.getString("appt_end_date"));
                        serviceMap.put(Integer.toString(rs.getInt("id")), service);
                    }
                    return "";
                }
            });
        } catch (DataAccessException dae) {
            throw new TelAppointException(ErrorConstants.ERROR_1043.getCode(), ErrorConstants.ERROR_1043.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, dae.getMessage(), null);
        }
    }

    @Override
    public void getListOfThingsToBring(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String device, String langCode, Integer serviceId, final ListOfThingsResponse listOfThingsResponse)
            throws TelAppointException, Exception {
        StringBuilder sql = new StringBuilder("select display_text, list_of_docs_tts, list_of_docs_audio");
        sql.append(" from list_of_things_bring where service_id= ? and lang= ?");
        try {
            jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[]{serviceId, langCode}, new ResultSetExtractor<ListOfThingsResponse>() {
                @Override
                public com.telappoint.apptdesk.model.ListOfThingsResponse extractData(ResultSet rs) throws SQLException, DataAccessException {
                    if (rs.next()) {
                        if (CoreUtils.isOnline(device) || CoreUtils.isMobile(device) ) {
                            listOfThingsResponse.setDeplayText(rs.getString("display_text"));
                        } else if (CoreUtils.isIVR(device)) {
                            listOfThingsResponse.setListOfDocsTts(rs.getString("list_of_docs_tts") == null ? "" : rs.getString("list_of_docs_tts"));
                            listOfThingsResponse.setListOfDocsAudio(rs.getString("list_of_docs_audio") == null ? "" : rs.getString("list_of_docs_audio"));
                        }
                    }
                    return listOfThingsResponse;
                }
            });
        } catch (DataAccessException dae) {
            throw new TelAppointException(ErrorConstants.ERROR_1043.getCode(), ErrorConstants.ERROR_1043.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, dae.getMessage(), "ServiceId:" + serviceId);
        }
    }

    @Override
    public void getHouseholdMonthlyIncome(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String device, String noOfPeople, Integer serviceId,
                                          HouseHoldMonthlyIncomeResponse houseHoldMonthlyIncome) throws TelAppointException, Exception {
        StringBuilder sql = new StringBuilder("select eligible_monthly_income, eligible_mon_income_tts, eligible_mon_income_audio");
        sql.append(" from service_monthly_income_eligibility where service_id=? and no_of_people_household=?");
        try {
            jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[]{serviceId, noOfPeople}, new ResultSetExtractor<Long>() {
                @Override
                public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
                    if (rs.next()) {
                        houseHoldMonthlyIncome.setMonthlyIncome(rs.getString("eligible_monthly_income"));
                        if (CoreUtils.isIVR(device)) {
                            houseHoldMonthlyIncome.setMonthlyIncomeTts(rs.getString("eligible_mon_income_tts"));
                            houseHoldMonthlyIncome.setMonthlyIncomeAudio(rs.getString("eligible_mon_income_audio"));
                        }
                    }
                    return (long)0;
                }
            });
        } catch (DataAccessException dae) {
            StringBuilder inputData = new StringBuilder();
            inputData.append("noOfPeople:").append(noOfPeople).append(",").append("serviceId:" + serviceId);
            throw new TelAppointException(ErrorConstants.ERROR_1044.getCode(), ErrorConstants.ERROR_1044.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, dae.getMessage(), inputData.toString());
        }
    }

    @Override
    public void updateNameRecord(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, NameRecordInfo nameRecordInfo) throws TelAppointException, Exception {
        StringBuilder sql = new StringBuilder();
        sql.append("insert into voice_msg (schedule_id, timestamp, customer_id, display_flag, file_path, ");
        sql.append("file_name, encrypted, record_duration) values (?, now(), ?, 'Y', ?, ?, 'N', ?)");
        try {
            jdbcCustomTemplate.getJdbcTemplate().update(sql.toString(),
                    new Object[]{nameRecordInfo.getScheduleId(), nameRecordInfo.getCustomerId(), nameRecordInfo.getMp3FilePath(), nameRecordInfo.getFileName(),
                            nameRecordInfo.getDuration()});
        } catch (DataAccessException dae) {
            throw new TelAppointException(ErrorConstants.ERROR_1052.getCode(), ErrorConstants.ERROR_1052.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, dae.getMessage(), nameRecordInfo.toString());
        }
    }

    @Override
    public void getCustomerInfo(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String device, Long customerId, CustomerInfo customerInfo) throws TelAppointException, Exception {
    	String spName = "get_customer_info_sp";
    	if(CoreUtils.isMobile(device)) {
    	 spName = "get_mobile_customer_info_sp";
    	}
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcCustomTemplate.getJdbcTemplate()).withProcedureName(spName);
       
        Customer customer = new Customer();
        call.declareParameters(new SqlReturnResultSet("result", new ResultSetExtractor<String>() {
            @Override
            public String extractData(ResultSet rs) throws SQLException, DataAccessException {
                if (rs.next()) {
                    String keys = rs.getString("Key");
                    String javaRef = rs.getString("JavaRef");
                    String fields = rs.getString("Field");
                    String displayType = rs.getString("DisplayType");
                    customerInfo.setJavaRef(javaRef);
                    customerInfo.setFieldNames(fields);
                    customerInfo.setDisplayType(displayType);
                    String[] keyArray = keys.split(",");
                    String[] javaArray = javaRef.split(",");
                    if (keyArray.length == javaArray.length) {
                        for (int i = 0; i < keyArray.length; i++) {
                            try {
                            	if("dob".equals(javaArray[i])) {
                            		String dob = rs.getString(keyArray[i]) == null ? "" : rs.getString(keyArray[i]);
                            		if(dob != null && "0000-00-00".equals(dob)) {
                            			dob="";
                            		} else if(dob == null || "".equals(dob)) {
                            			dob="";
                            		} else {
                            			String yyyy = dob.substring(0,4);
                            			String MM = dob.substring(5,7);
                            			String dd = dob.substring(8);
                            			dob=MM+"/"+dd+"/"+yyyy;
                            		}
                            		CoreUtils.setPropertyValue(customer, javaArray[i], dob);
                            	} else {
                            		CoreUtils.setPropertyValue(customer, javaArray[i], rs.getString(keyArray[i]) == null ? "" : rs.getString(keyArray[i]));
                            	}
                            } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException | IntrospectionException e) {
                                logger.error("Error while populating the customer bean");
                            }
                        }
                    } else {
                        logger.error("Key column and JavaRef fields not a same. ");
                    }
                    customer.setCustomerId(customerId);
                    customerInfo.setCustomer(customer);
                }
                return "";
            }
        }));
        call.declareParameters(new SqlParameter(SPConstants.CUSTOMER_ID.getValue(), Types.BIGINT));

        try {
        	long startTime = System.currentTimeMillis();
            call.execute(customerId);
            long endTime = System.currentTimeMillis();
            logTimeTaken(spName, startTime, endTime);
        } catch (DataAccessException dae) {
            throw new TelAppointException(ErrorConstants.ERROR_1053.getCode(), ErrorConstants.ERROR_1053.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, dae.getMessage(), customerInfo.toString());
        }
    }

    @Override
    public void getLocationAvailability(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String timeZone, Integer locationId, ClientDeploymentConfig cdConfig, LocationPriSecAvailable locPriSecAvail)
            throws TelAppointException, Exception {
        String data = "TimeZone" + timeZone + ", locationId:" + locationId;
        try {
        	String spName = "get_location_availability_sp";
            SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcCustomTemplate.getJdbcTemplate()).withProcedureName(spName);
            Map<String, Object> inParameters = new HashMap<String, Object>();
            inParameters.put(SPConstants.TIME_ZONE.getValue(), timeZone);
            inParameters.put(SPConstants.LOCATION_ID.getValue(), locationId);
            inParameters.put(SPConstants.BLOCK_TIME_IN_MINS.getValue(), cdConfig.getBlockTimeInMins());
            long startTime = System.currentTimeMillis();
            Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(inParameters);
            long endTime = System.currentTimeMillis();
            logTimeTaken(spName, startTime, endTime);
            Object availability = simpleJdbcCallResult.get(SPConstants.AVAILABILITY.getValue());
            Object errorMsg = simpleJdbcCallResult.get(SPConstants.ERROR_MESSAGE.getValue());
            if (errorMsg != null && !"".equals(errorMsg)) {
                logger.error("Error Message from get_location_availability_sp is: " + errorMsg);
            }
            locPriSecAvail.setTimeSlotsAvailable((availability != null && "Y".equals((String) availability)) ? true : false);
        } catch (DataAccessException dae) {
            throw new TelAppointException(ErrorConstants.ERROR_1058.getCode(), ErrorConstants.ERROR_1058.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, dae.getMessage(), data);
        }
    }

    @Override
    public void getServiceAvailability(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, Integer locationId, Integer serviceId, ClientDeploymentConfig cdConfig, ServiceTimeSlotsAvailableStatus serviceAvail)
            throws TelAppointException, Exception {
        String data = "TimeZone: " + cdConfig.getTimeZone() + ", locationId: " + locationId + ", serviceId: " + serviceId;
        try {
        	String spName = "get_service_availability_callcenter_sp";
            SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcCustomTemplate.getJdbcTemplate()).withProcedureName(spName);
            Map<String, Object> inParameters = new HashMap<String, Object>();
            inParameters.put(SPConstants.TIME_ZONE.getValue(), cdConfig.getTimeZone());

            inParameters.put(SPConstants.LOCATION_ID.getValue(), locationId);
            inParameters.put(SPConstants.SERVICE_ID.getValue(), serviceId);
            inParameters.put(SPConstants.BLOCK_TIME_IN_MINS.getValue(), cdConfig.getBlockTimeInMins());

            long startTime = System.currentTimeMillis();
            Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(inParameters);
            long endTime = System.currentTimeMillis();
            logTimeTaken(spName, startTime, endTime);
            Object availability = simpleJdbcCallResult.get(SPConstants.AVAILABILITY.getValue());
            Object errorMsg = simpleJdbcCallResult.get(SPConstants.ERROR_MESSAGE.getValue());
            if (errorMsg != null && !"".equals(errorMsg)) {
                logger.error("Error Message from get_service_availability_sp is: " + errorMsg);
            }
            serviceAvail.setAvailable(availability != null && "Y".equals((String) availability) ? true : false);
        } catch (DataAccessException dae) {
            throw new TelAppointException(ErrorConstants.ERROR_1070.getCode(), ErrorConstants.ERROR_1070.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, dae.getMessage(), data);
        }
    }

    @Override
    public Long getTransId(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, final String device, final ClientDeploymentConfig cdConfig, final String uuid, final String ipAddress, final String callerId, String userName) throws TelAppointException, Exception {
        final String sql = "insert into main (timestamp,device,uuid,ip_address,caller_id,username) values (?,?,?,?,?,?)";
        DataSourceTransactionManager dsTransactionManager = jdbcCustomTemplate.getDataSourceTransactionManager();
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = dsTransactionManager.getTransaction(def);
        KeyHolder holder = new GeneratedKeyHolder();

        try {
            jdbcCustomTemplate.getJdbcTemplate().update(new PreparedStatementCreator() {
                int i = 1;

                @Override
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    PreparedStatement ps = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
                    ps.setTimestamp(i++, new Timestamp(System.currentTimeMillis()));
                    ps.setString(i++, device);
                    ps.setString(i++, (uuid == null) ? "" : uuid);
                    ps.setString(i++, ipAddress == null ? "" : ipAddress);
                    ps.setString(i++, callerId == null ? "" : callerId);
                    ps.setString(i++, userName == null ? "" : userName);
                    return ps;
                }
            }, holder);
            Long id = holder.getKey().longValue();
            if (CoreUtils.isIVR(device)) {
                saveIVRCallLog(jdbcCustomTemplate, logger, cdConfig, id);
            }
            dsTransactionManager.commit(status);
            return id;
        } catch (DataAccessException dae) {
            dsTransactionManager.rollback(status);
            StringBuilder inputData = new StringBuilder();
            inputData.append("UUID:").append((uuid == null) ? "" : uuid).append(" , ").append("IpAddress:").append(ipAddress == null ? "" : ipAddress);
            inputData.append("callerId:").append((callerId == null) ? "" : callerId).append(",").append("userName:").append(userName == null ? "" : userName);
            throw new TelAppointException(ErrorConstants.ERROR_1002.getCode(), ErrorConstants.ERROR_1002.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, dae.getMessage(), inputData.toString());
        }
    }

    private void saveIVRCallLog(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, final ClientDeploymentConfig cdConfig, final Long transId) throws DataAccessException {
        final StringBuilder sql = new StringBuilder("insert into ivr_calls (trans_id, start_time, end_time, seconds)");
        sql.append(" values (?,DATE_ADD(CONVERT_TZ(now(),'US/Central','").append(cdConfig.getTimeZone()).append("'),INTERVAL -30 SECOND)");
        sql.append(",CONVERT_TZ(now(),'US/Central','").append(cdConfig.getTimeZone()).append("')");
        sql.append(",30)");
        jdbcCustomTemplate.getJdbcTemplate().update(sql.toString(), new Object[]{transId});
    }

    @Override
    public void updateIVRCallLog(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, final ClientDeploymentConfig cdConfig, final IVRCallRequest ivrCallRequest, IVRCallResponse ivrCallResponse) throws TelAppointException, Exception {
        StringBuilder sql = new StringBuilder("update ivr_calls set ");
        sql.append(" end_time= CONVERT_TZ(now(),'US/Central','").append(cdConfig.getTimeZone()).append("')");
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        try {
            if (ivrCallRequest.getCustomerId() != null && ivrCallRequest.getCustomerId() > 0) {
                sql.append(",customer_id=:customerId");
                mapSqlParameterSource.addValue("customerId", ivrCallRequest.getCustomerId());
            }

            if (ivrCallRequest.getResourceId() != null && ivrCallRequest.getResourceId() > 0) {
                sql.append(",resource_id=:resourceId");
                mapSqlParameterSource.addValue("resourceId", ivrCallRequest.getResourceId());
            }

            if (ivrCallRequest.getLocationId() != null && ivrCallRequest.getLocationId() > 0) {
                sql.append(",location_id=:locationId");
                mapSqlParameterSource.addValue("locationId", ivrCallRequest.getLocationId());
            }

            if (ivrCallRequest.getServiceId() != null && ivrCallRequest.getServiceId() > 0) {
                sql.append(",service_id=:serviceId");
                mapSqlParameterSource.addValue("serviceId", ivrCallRequest.getServiceId());
            }

            if (ivrCallRequest.getConfNumber() != null && ivrCallRequest.getConfNumber() > 0) {
                sql.append(",conf_num=:confirmNumber");
                mapSqlParameterSource.addValue("confirmNumber", ivrCallRequest.getConfNumber());
            }

            if (ivrCallRequest.getApptType() != null && ivrCallRequest.getApptType() > 0) {
                sql.append(",appt_type=:apptType");
                mapSqlParameterSource.addValue("apptType", ivrCallRequest.getApptType());
            }

            sql.append(",seconds=TIMESTAMPDIFF(SECOND,start_time,CONVERT_TZ(now(),'US/Central','").append(cdConfig.getTimeZone()).append("')) where trans_id=:transId");
            mapSqlParameterSource.addValue("transId", ivrCallRequest.getTransId());

            int count = jdbcCustomTemplate.getNameParameterJdbcTemplate().update(sql.toString(), mapSqlParameterSource);
            if (count == 0) {
                throw new TelAppointException(ErrorConstants.ERROR_1003.getCode(), ErrorConstants.ERROR_1003.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, "IVR call upadation failed.", ivrCallRequest.toString());
            }
        } catch (DataAccessException dae) {
            throw new TelAppointException(ErrorConstants.ERROR_1003.getCode(), ErrorConstants.ERROR_1003.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, dae.getMessage(), ivrCallRequest.toString());
        }

    }

    public int updateSeconds(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, final ClientDeploymentConfig cdConfig, long ivrCallId) {
        int seconds = cdConfig.getLagTimeInSeconds() + cdConfig.getLeadTimeInSeconds();
        String sql = "update ivr_calls set seconds=CEILING((seconds+" + seconds + ")/60)*60 where id=?";
        return jdbcCustomTemplate.getJdbcTemplate().update(sql, new Object[]{ivrCallId});
    }

    @Override
    public void updateTransaction(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String device, Long transId, Integer pageId, Long scheduleId) throws TelAppointException, Exception {
        String sql = "insert into trans_state (trans_id, timestamp, state) values (?, now(), ?)";
        try {
            jdbcCustomTemplate.getJdbcTemplate().update(sql, new Object[]{transId, pageId});
            if (scheduleId != null && scheduleId.longValue() > 1) {
                jdbcCustomTemplate.getJdbcTemplate().update("update schedule set timestamp=now() where id=?", new Object[]{scheduleId});
            }
        } catch (DataAccessException dae) {
            String inputData = "TransId: " + transId + " ,PageId:" + pageId + ", ScheduleId: " + scheduleId;
            throw new TelAppointException(ErrorConstants.ERROR_1005.getCode(), ErrorConstants.ERROR_1005.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, dae.getMessage(), inputData.toString());
        }

    }

    @Override
    public void extendHoldTime(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String device, Long scheduleId) throws TelAppointException, Exception {
        try {
            if (scheduleId != null && scheduleId.longValue() > 0) {
                jdbcCustomTemplate.getJdbcTemplate().update("update schedule set timestamp=now() where id=?", new Object[]{scheduleId});
            }
        } catch (DataAccessException dae) {
            String inputData = "ScheduleId:" + scheduleId;
            throw new TelAppointException(ErrorConstants.ERROR_1074.getCode(), ErrorConstants.ERROR_1074.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, dae.getMessage(), inputData.toString());
        }
    }

    @Override
    public BaseResponse releaseHoldAppointment(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String device, Long scheduleId) throws TelAppointException, Exception {
        try {
            BaseResponse baseResponse = new BaseResponse();

            logger.info("releaseHoldAppointment input params: scheduleId: " + scheduleId);
            SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcCustomTemplate.getJdbcTemplate()).withProcedureName("release_hold_appt_sp");
            Map<String, Object> inParameters = new HashMap<String, Object>();
            inParameters.put(SPConstants.SCHEDULE_ID.getValue(), scheduleId);
            Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(inParameters);
            Object statusResult = simpleJdbcCallResult.get(SPConstants.STATUS_RESULT.getValue());
            Object errorMsg = simpleJdbcCallResult.get(SPConstants.ERROR_MESSAGE.getValue());
            if (statusResult != null && "N".equals((String) statusResult)) {
                logger.error("ReleaseHoldAppointment failed!");
                baseResponse.setMessage("ReleaseHoldAppointment failed!");
                baseResponse.setStatus(false);
            }
            if (errorMsg != null && !"".equals(errorMsg)) {
                logger.error("Error Message from release hold appt is: " + errorMsg);
                baseResponse.setMessage("Error Message from release hold appt is: " + errorMsg);
                baseResponse.setStatus(false);
            }
            return baseResponse;
        } catch (DataAccessException dae) {
            throw new TelAppointException(ErrorConstants.ERROR_1067.getCode(), ErrorConstants.ERROR_1067.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, dae.getMessage(), "ScheduleId: " + scheduleId);
        }
    }

    @Override
    public AvailableDateTimes getAvailableDatesCallcenter(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String timeZone,
                                                          Long locationId, Long departmentId, Long serviceId, Long blockTimeMins) throws Exception {
        Map<String, Object> inParameters = new HashMap<>();
        try {
        	String spName = "get_available_dates_callcenter_sp";
            SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcCustomTemplate.getJdbcTemplate()).withProcedureName(spName);

            inParameters.put(SPConstants.TIME_ZONE.getValue(), timeZone);
            inParameters.put(SPConstants.LOCATION_ID.getValue(), locationId);
            inParameters.put(SPConstants.DEPARTMENT_ID.getValue(), departmentId);
            inParameters.put(SPConstants.SERVICE_ID.getValue(), serviceId);
            inParameters.put(SPConstants.BLOCK_TIME_IN_MINS.getValue(), blockTimeMins);

            logger.info("getAvailableDatesCallcenter input params = " + inParameters);
            long startTime = System.currentTimeMillis();
            Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(inParameters);
            long endTime = System.currentTimeMillis();
            logTimeTaken(spName, startTime, endTime);
            
            Object statusResult = simpleJdbcCallResult.get(SPConstants.AVAILABLE_DATES.getValue());
            Object errorMsg = simpleJdbcCallResult.get(SPConstants.ERROR_MESSAGE.getValue());
            return new AvailableDateTimes(statusResult == null ? "" : statusResult.toString(), errorMsg == null ? null : errorMsg.toString());
        } catch (DataAccessException dae) {
            throw new TelAppointException(ErrorConstants.ERROR_1069.getCode(), ErrorConstants.ERROR_1069.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, dae.getMessage(), "getAvailableDatesCallcenter input params = " + inParameters);
        }

    }
    
    @Override
    public AvailableDateTimes getAvailableDates(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String timeZone,
                                                          Long locationId, Long departmentId, Long resourceId, Long serviceId, Long blockTimeMins) throws Exception {
        Map<String, Object> inParameters = new HashMap<>();
        try {
        	String spName = "get_available_dates_sp";
            SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcCustomTemplate.getJdbcTemplate()).withProcedureName(spName);

            inParameters.put(SPConstants.TIME_ZONE.getValue(), timeZone);
            inParameters.put(SPConstants.LOCATION_ID.getValue(), locationId);
            inParameters.put(SPConstants.DEPARTMENT_ID.getValue(), departmentId);
            inParameters.put(SPConstants.RESOURCE_ID.getValue(), resourceId);
            inParameters.put(SPConstants.SERVICE_ID.getValue(), serviceId);
            inParameters.put(SPConstants.BLOCK_TIME_IN_MINS.getValue(), blockTimeMins);

            logger.info("getAvailableDatesCallcenter input params = " + inParameters);
            long startTime = System.currentTimeMillis();
            Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(inParameters);
            long endTime = System.currentTimeMillis();
            logTimeTaken(spName, startTime, endTime);
            
            Object statusResult = simpleJdbcCallResult.get(SPConstants.AVAILABLE_DATES.getValue());
            Object errorMsg = simpleJdbcCallResult.get(SPConstants.ERROR_MESSAGE.getValue());
            return new AvailableDateTimes(statusResult == null ? "" : statusResult.toString(), errorMsg == null ? null : errorMsg.toString());
        } catch (DataAccessException dae) {
            throw new TelAppointException(ErrorConstants.ERROR_1069.getCode(), ErrorConstants.ERROR_1069.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, dae.getMessage(), "getAvailableDatesCallcenter input params = " + inParameters);
        }

    }

    @Override
    public FirstAvailableDateTime getApptFirstAvailDateTimeCallCenter(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, HoldAppointmentRequest holdAppt, ClientDeploymentConfig cdConfig) throws Exception {
        return super.getApptFirstAvailDateTimeCallCenter(jdbcCustomTemplate, logger, holdAppt, cdConfig);
    }
    
    @Override
    public void getApptFirstAvailDateTimeAnyLocation(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String device, ClientDeploymentConfig cdConfig, FirstAvailableDateAnyLocationResponse firstAvailDateAnyLoc) throws TelAppointException, Exception {
        super.getApptFirstAvailDateTimeAnyLocation(jdbcCustomTemplate, logger, device, cdConfig, firstAvailDateAnyLoc);
    }

    @Override
    public AvailableDateTimes getAvailableTimesCallcenter(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, Long locationId,
                                                       Long departmentId, Long serviceId, String availDate, Long blockTimeMins) throws Exception {
        Map<String, Object> inParameters = new HashMap<String, Object>();
        try {
        	String spName="get_avail_times_callcenter_sp";
            SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcCustomTemplate.getJdbcTemplate()).withProcedureName("get_avail_times_callcenter_sp");

            inParameters.put(SPConstants.LOCATION_ID.getValue(), locationId);
            inParameters.put(SPConstants.DEPARTMENT_ID.getValue(), departmentId);
            inParameters.put(SPConstants.SERVICE_ID.getValue(), serviceId);
            inParameters.put(SPConstants.BLOCK_TIME_IN_MINS.getValue(), blockTimeMins);
            inParameters.put(SPConstants.AVAILABLE_DATE.getValue(), new java.sql.Date((new SimpleDateFormat("yyyy-MM-dd").parse(availDate)).getTime()));
            logger.info("getAvailableTimesCallcenter input params = " + inParameters);
            long startTime = System.currentTimeMillis();
            Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(inParameters);
            long endTime = System.currentTimeMillis();
            logTimeTaken(spName, startTime, endTime);
            
            Object availDateTimes = simpleJdbcCallResult.get(SPConstants.AVAILABLE_DATE_TIMES.getValue());
            Object errorMsg = simpleJdbcCallResult.get(SPConstants.ERROR_MESSAGE.getValue());
            String sortedUniqueTime = null;
            String displayTimeList = null;
            if(availDateTimes != null) {
            	String availDateTimeStr = (String)availDateTimes;
            	if(!"".equals(availDateTimeStr)) {
            		 logger.info("AvailableDateTimes: "+(String)availDateTimes);
                     sortedUniqueTime = getSortedUniqueTime(availDateTimes);
                     logger.info("sortedUniqueTimes: "+sortedUniqueTime);
                     displayTimeList = getDisplayTimeList(sortedUniqueTime);	  
            	} 
            } 
            return new AvailableDateTimes(null, sortedUniqueTime == null ? null : sortedUniqueTime, displayTimeList == null ? null : displayTimeList,  errorMsg == null ? null : errorMsg.toString());
        } catch (DataAccessException dae) {
            throw new TelAppointException(ErrorConstants.ERROR_1069.getCode(), ErrorConstants.ERROR_1069.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, dae.getMessage(), "getAvailableTimesCallcenter input params = " + inParameters);
        }
    }

    //'2015-05-24|1-10:00,10:15,16:00,16:15|2-09:30,11:15'
    private String getSortedUniqueTime(Object avail_date_times) {
        List<String> timeList = new ArrayList<>();
        if (avail_date_times != null) {
            String avail_times_with_date = avail_date_times.toString();
            if (avail_times_with_date.contains("|")) {
                String[] times = avail_times_with_date.split("\\|");
                
                // 1-10:00,10:15,16:00,16:15|2-09:30,11:15  - doing from 1st array. 
                for (int i = 1; i < times.length; i++) {
                    String onlyTimesWithResourceId = times[i];
                    String[] split = onlyTimesWithResourceId.split("-");
                    String timesWithComma = split[1];
                    String[] timeString = timesWithComma.split(",");

                    for (int j = 0; j < timeString.length; j++) {
                        if (!timeList.contains(timeString[j])) {
                            timeList.add(timeString[j]);
                        }
                    }
                }
            }
        }
        Collections.sort(timeList, new TimeStringComparator());
        return StringUtils.join(timeList, ',');
    }

    private String getDisplayTimeList(String sortedUniqueTime) {
        List<String> displayTimeList = new ArrayList<>();
        String[] timeArray = sortedUniqueTime.split(",");
        for (String time : timeArray) {
            try {
                if (time != null) {
                    displayTimeList.add(DateUtils.convert24To12HoursFormatSingleH(time));
                }
            } catch (ParseException e) {

            }
        }
        return StringUtils.join(displayTimeList, ',');
    }

    @Override
    public void holdFirstAvailableAppointment(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, HoldAppointmentRequest holdApptReq, String dateTime, TempHoldAppt tempHoldAppt, ClientDeploymentConfig cdConfig, HoldAppointmentResponse holdAppointmentResponse) throws Exception {
        super.holdFirstAvailableAppointment(jdbcCustomTemplate, logger, holdApptReq, dateTime, tempHoldAppt, cdConfig, holdAppointmentResponse);
    }

    @Override
    public TempHoldAppt holdAppt(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, Integer resourceId, String dateTime, Integer serviceId, Long customerId, ClientDeploymentConfig cdConfig) throws Exception {
        return super.holdAppt(jdbcCustomTemplate, logger, resourceId, dateTime, serviceId, customerId, cdConfig);
    }

	@Override
	public HoldAppt holdAppointmentCallCenter(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String device, Long locationId, Long procedureId, Long departmentId,
			Long serviceId, Long customerId, String apptDateTime, ClientDeploymentConfig cdConfig, Long transId) throws Exception {
		Map<String, Object> inParameters = new HashMap<>();
		try {
			String spName = "hold_appointment_callcenter_sp";
			SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcCustomTemplate.getJdbcTemplate()).withProcedureName(spName);

			inParameters.put(SPConstants.APPT_DATE_TIME.getValue(), apptDateTime);
			inParameters.put(SPConstants.LOCATION_ID.getValue(), locationId);
			inParameters.put(SPConstants.PROCEDURE_ID.getValue(), procedureId);
			inParameters.put(SPConstants.DEPARTMENT_ID.getValue(), departmentId);
			inParameters.put(SPConstants.SERVICE_ID.getValue(), serviceId);
			inParameters.put(SPConstants.CUSTOMER_ID.getValue(), customerId);
			inParameters.put(SPConstants.BLOCK_TIME_IN_MINS.getValue(), cdConfig.getBlockTimeInMins());
			inParameters.put(SPConstants.TRANS_ID.getValue(), transId);
			inParameters.put(SPConstants.DEVICE.getValue(), device);

			logger.info("holdAppointmentCallCenter input params: " + inParameters);
			long startTime = System.currentTimeMillis();
			Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(inParameters);
			long endTime = System.currentTimeMillis();
			logTimeTaken(spName, startTime, endTime);

			Object schedule_id = simpleJdbcCallResult.get(SPConstants.RETURN_SCHEDULE_ID.getValue());
			Object errorMsg = simpleJdbcCallResult.get(SPConstants.ERROR_MESSAGE.getValue());
			Object display_datetime = simpleJdbcCallResult.get(SPConstants.DISPLAY_DATETIME.getValue());
			
			if (schedule_id != null && display_datetime != null) {
				return new HoldAppt(Long.parseLong(schedule_id.toString()), display_datetime.toString(), errorMsg == null ? null : errorMsg.toString());
			} else {
				return new HoldAppt(null, null, errorMsg == null ? null : errorMsg.toString(), false);
			}
		} catch (DataAccessException dae) {
			throw new TelAppointException(ErrorConstants.ERROR_1076.getCode(), ErrorConstants.ERROR_1076.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, dae.getMessage(),
					"holdAppointmentCallCenter input params: " + inParameters);
		}
	}
	
	
	@Override
	public Map<String, CustomerPledgeData> getPledgeHistoryWithTemplate(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String device, String langCode, Long customerId) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT cp.id as cpId, f.fund_name, f.fund_name_tts, f.fund_name_audio, ");
		sql.append("group_concat(cv.vendor_name_tts ORDER BY cv.id) as vendorNameTts,");
		sql.append("group_concat(cv.vendor_name_audio ORDER BY cv.id) as vendorNameAudio,");
        sql.append("cp.total_amount,group_concat(cv.vendor_name ORDER BY cpv.id) as vendorNames,group_concat(cpv.vendor_pledge_amount ORDER BY cpv.id) as vendorAmounts,");
        if(CoreUtils.isOnline(device)) {
        	sql.append(" DATE_FORMAT(cp.pledge_datetime,'%M %d, %Y') as pledge_datetime");
        } else if(CoreUtils.isIVR(device)) {
        	sql.append(" DATE_FORMAT(cp.pledge_datetime,'%Y-%m-%d') as pledge_datetime");
        }
        sql.append(" FROM  customer c ");
        sql.append(" LEFT OUTER JOIN customer_pledge cp ON c.id = cp.customer_id ");
        sql.append(" LEFT OUTER JOIN customer_pledge_fund_source f ON cp.fund_id = f.id"); 
        sql.append(" LEFT OUTER JOIN customer_pledge_status cps ON cp.pledge_status_id = cps.id ");
        sql.append(" LEFT OUTER JOIN customer_pledge_vendor cpv ON cp.id = cpv.customer_pledge_id ");
        sql.append(" LEFT OUTER JOIN customer_vendor cv ON cpv.vendor_id = cv.id");
        sql.append(" LEFT OUTER JOIN `schedule` s ON cp.schedule_id = s.id");
        sql.append(" WHERE c.id IN (SELECT c2.id FROM customer c2 WHERE c2.household_id = (SELECT c1.household_id FROM customer c1 WHERE c1.id = ?))");
        sql.append(" and cp.total_amount > 0.00 group by c.id,cp.id ORDER BY cp.id,cp.pledge_datetime DESC");
        logger.debug("getPledgeHistoryList ::: SQL  ::: " + sql.toString());
        try {
            return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(),new Object[]{customerId}, new ResultSetExtractor<Map<String, CustomerPledgeData>>() {
                @Override
                public Map<String,CustomerPledgeData> extractData(ResultSet rs) throws SQLException, DataAccessException {
                	Map<String, CustomerPledgeData> customerPledgeDataMap = new HashMap<>();
                	CustomerPledgeData customerPledgeData = null;
                    while (rs.next()) {
                    	customerPledgeData = new CustomerPledgeData();
                    	customerPledgeData.setPledateDate(rs.getString("pledge_datetime"));
                    	customerPledgeData.setCustomerPledgeId(""+rs.getLong("cpId"));
                    	customerPledgeData.setFundName(rs.getString("fund_name"));
                    	
						try {
							
	                    	customerPledgeData.setTotalPledgeAmount(String.format("%.2f",rs.getDouble("total_amount")));
							String vendorNames = rs.getString("vendorNames");
							if (vendorNames != null) {
								String vendorNameArr[] = vendorNames.split(",");
								customerPledgeData.setVendorNames(vendorNameArr);
							}
							
							
							if(CoreUtils.isIVR(device)) {
								customerPledgeData.setFundNameTts(rs.getString("fund_name_tts"));
								customerPledgeData.setFundNameAudio(rs.getString("fund_name_audio"));
								String vendorNameTts = rs.getString("vendorNameTts");
								if (vendorNameTts != null) {
									String vendorNameTtsArr[] = vendorNameTts.split(",");
									customerPledgeData.setVendorNameTtsList(vendorNameTtsArr);
								}
								
								String vendorNameAudio = rs.getString("vendorNameAudio");
								if (vendorNameAudio != null) {
									String vendorNameAudioArr[] = vendorNameAudio.split(",");
									customerPledgeData.setVendorNameAudioList(vendorNameAudioArr);
								}
							}

							String vendorPAmounts = rs.getString("vendorAmounts");
							if (vendorPAmounts != null) {
								String pledgeAmountsArr[] = vendorPAmounts.split(",");
								customerPledgeData.setVendorPledgePayments(pledgeAmountsArr);
							}
						} catch (Exception e) {
							logger.error("Error: "+e,e);
						}
						if(!customerPledgeDataMap.containsKey(customerPledgeData.getFundName())) {
							customerPledgeDataMap.put(customerPledgeData.getFundName(), customerPledgeData);
						}
                    } 
                    return customerPledgeDataMap;
                }
            });
        } catch (DataAccessException dae) {
            throw new TelAppointException(ErrorConstants.ERROR_1075.getCode(), ErrorConstants.ERROR_1075.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, dae.getMessage(), null);
        }
		
	}

	

	@Override
	public void getPledgeHistory(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String device, String langCode, Long customerId, CustomerPledgeResponse pledgeRes) throws TelAppointException {
		StringBuilder sql = new StringBuilder("SELECT cp.id as cpId, c.id as cId, c.household_id, RIGHT(c.account_number,4) as account_number, c.first_name, c.last_name, DATE_FORMAT(s.appt_date_time, '%m/%d/%y %l:%i %p') as apptDateTime, f.id as fid, ");
		sql.append(" f.fund_name, f.fund_name_tts, f.fund_name_audio,");
		sql.append("group_concat(cv.vendor_name_tts ORDER BY cv.id) as vendorNameTts,");
		sql.append("group_concat(cv.vendor_name_audio ORDER BY cv.id) as vendorNameAudio,");
        sql.append("cp.total_amount,group_concat(cpv.vendor_id ORDER BY cpv.id) as vendorIds,group_concat(cv.vendor_name ORDER BY cpv.id) as vendorNames,group_concat(cpv.vendor_pledge_amount ORDER BY cpv.id) as vendorAmounts,");
        if(CoreUtils.isOnline(device)) {
        	sql.append(" DATE_FORMAT(cp.pledge_datetime,'%M %d, %Y') as pledge_datetime,");
        } else if(CoreUtils.isIVR(device)) {
        	sql.append(" DATE_FORMAT(cp.pledge_datetime,'%Y-%m-%d') as pledge_datetime,");
        }
        sql.append("cps.`status`, CASE c.liheap_fund WHEN 'Y' THEN 'No' WHEN 'N' THEN 'Yes' ELSE 'Yes' END as liheapFund, CASE c.psehelp_fund WHEN 'Y' THEN 'No' WHEN 'N' THEN 'Yes' ELSE 'Yes' END as psehelpFund, s.id as scheduleId ");
        sql.append(" FROM  customer c ");
        sql.append(" LEFT OUTER JOIN customer_pledge cp ON c.id = cp.customer_id ");
        sql.append(" LEFT OUTER JOIN customer_pledge_fund_source f ON cp.fund_id = f.id"); 
        sql.append(" LEFT OUTER JOIN customer_pledge_status cps ON cp.pledge_status_id = cps.id ");
        sql.append(" LEFT OUTER JOIN customer_pledge_vendor cpv ON cp.id = cpv.customer_pledge_id ");
        sql.append(" LEFT OUTER JOIN customer_vendor cv ON cpv.vendor_id = cv.id");
        sql.append(" LEFT OUTER JOIN `schedule` s ON cp.schedule_id = s.id");
        sql.append(" WHERE c.id IN (SELECT c2.id FROM customer c2 WHERE c2.household_id = (SELECT c1.household_id FROM customer c1 WHERE c1.id = ?))");
        sql.append(" and cp.total_amount > 0.00 group by c.id,cp.id ORDER BY cp.id,cp.pledge_datetime DESC");
        logger.debug("getPledgeHistoryList ::: SQL  ::: " + sql.toString());
        try {
        	List<CustomerPledge> customerPledgeList = new ArrayList<CustomerPledge>();
            jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(),new Object[]{customerId}, new ResultSetExtractor<Long>() {
                @Override
                public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
                	CustomerPledge customerPledge;
                    while (rs.next()) {
                    	customerPledge = new CustomerPledge();
                    	customerPledge.setCustomerPledgeId(""+rs.getLong("cpId"));
                    	customerPledge.setHouseHoldId(rs.getLong("household_id"));
                    	customerPledge.setAccountNumber(rs.getString("account_number"));
                    	customerPledge.setFirstName(rs.getString("first_name"));
                    	customerPledge.setLastName(rs.getString("last_name"));
                    	customerPledge.setApptDateTime(rs.getString("apptDateTime"));
                    	customerPledge.setPledgeDateTime(rs.getString("pledge_datetime"));
                    	customerPledge.setCustomerId(rs.getLong("cId"));
                    	customerPledge.setFundName(rs.getString("fund_name"));
						try {
							
							if(CoreUtils.isOnline(device)) {
		                    	customerPledge.setFundId(""+rs.getLong("fid"));
		                    	customerPledge.setTotalPledgeAmt(String.format("%.2f",rs.getDouble("total_amount")));
		                    	customerPledge.setPledgeStatus(rs.getString("status"));
		                    	customerPledge.setLiheapFund(rs.getString("liheapFund"));
		                    	customerPledge.setPseHelpFund(rs.getString("psehelpFund"));
		                    	customerPledge.setScheduleId(""+rs.getLong("scheduleId"));
		                    	customerPledge.setPledgeStatus(rs.getString("status"));
		                    	
								String vendorIds = rs.getString("vendorIds");
								if (vendorIds != null) {
									String vendorIdArr[] = vendorIds.split(",");
									for (int index = 0; index < vendorIdArr.length; index++) {
										int id = index + 1;
										CoreUtils.setPropertyValue(customerPledge, "vendor" + (id)+"Id", vendorIdArr[index]);
									}
								}
	
								String vendorNames = rs.getString("vendorNames");
								if (vendorNames != null) {
									String vendorNameArr[] = vendorNames.split(",");
									for (int index = 0; index < vendorNameArr.length; index++) {
										int id = index + 1;
										CoreUtils.setPropertyValue(customerPledge, "vendor" + (id)+"Name", vendorNameArr[index]);
									}
								}
							}
							
							if(CoreUtils.isIVR(device)) {
								customerPledge.setFundNameTts(rs.getString("fund_name_tts"));
								customerPledge.setFundNameAudio(rs.getString("fund_name_audio"));
								String vendorNameTts = rs.getString("vendorNameTts");
								if (vendorNameTts != null) {
									String vendorNameArr[] = vendorNameTts.split(",");
									for (int index = 0; index < vendorNameArr.length; index++) {
										int id = index + 1;
										CoreUtils.setPropertyValue(customerPledge, "vendor" + (id)+"NameTts", vendorNameArr[index]);
									}
								}
								
								String vendorNameAudio = rs.getString("vendorNameAudio");
								if (vendorNameAudio != null) {
									String vendorNameArr[] = vendorNameAudio.split(",");
									for (int index = 0; index < vendorNameArr.length; index++) {
										int id = index + 1;
										CoreUtils.setPropertyValue(customerPledge, "vendor" + (id)+"NameAudio", vendorNameArr[index]);
									}
								}
							}

							String vendorPAmounts = rs.getString("vendorAmounts");
							if (vendorPAmounts != null) {
								String pledgeAmountsArr[] = vendorPAmounts.split(",");
								for (int index = 0; index < pledgeAmountsArr.length; index++) {
									int id = index + 1;
									if(pledgeAmountsArr[index] != null) {
										CoreUtils.setPropertyValue(customerPledge, "vendor" + id + "Payment", pledgeAmountsArr[index]);
									} else {
										CoreUtils.setPropertyValue(customerPledge, "vendor" + id + "Payment", "");
									}
								}
							}
						} catch (Exception e) {
							logger.error("Error: "+e,e);
						}
                    	customerPledgeList.add(customerPledge);
                    } 
                    pledgeRes.setCustomerPledgeList(customerPledgeList);
                    return (long) 0;
                }
            });
        } catch (DataAccessException dae) {
            throw new TelAppointException(ErrorConstants.ERROR_1075.getCode(), ErrorConstants.ERROR_1075.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, dae.getMessage(), null);
        }
		
	}

	@Override
	public void checkDuplicateAppts(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, Long serviceId, Long customerId, DuplicateApptResponse duplicateApptResponse)
			throws TelAppointException {
		String sql = "select count(s.`id`) from `schedule` s, customer c where c.`id` = ? and s.customer_id = c.`id` and s.appt_date_time > now() and s.`status` = 11";
		Long dupsApptCount = jdbcCustomTemplate.getJdbcTemplate().queryForObject(sql, new Object[]{customerId}, Long.class);
		sql = "select allow_duplicate_appt from service where id=?";
		String dupsAppt = jdbcCustomTemplate.getJdbcTemplate().query(sql, new Object[]{serviceId},new ResultSetExtractor<String>() {
            @Override
            public String extractData(ResultSet rs) throws SQLException, DataAccessException {
                if (rs.next()) {
                	return rs.getString("allow_duplicate_appt");
                } 
                return "";
            }
        });		
		if("N".equals(dupsAppt) && dupsApptCount.longValue() > 0) {
			duplicateApptResponse.setDuplicate(true);
			duplicateApptResponse.setErrorFlag("Y");
		}
	}

	@Override
	public CustomerPledge getCustomerPledgeById(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String device, String langCode, Long customerPledgeId) throws TelAppointException {
		StringBuilder sql = new StringBuilder();
		sql.append("select cp.total_amount,");
		if(CoreUtils.isOnline(device)) {
			sql.append("DATE_FORMAT(cp.pledge_datetime,'%M %d, %Y') as pledge_datetime,");
		} else if(CoreUtils.isIVR(device)){
			sql.append(" DATE_FORMAT(cp.pledge_datetime,'%Y-%m-%d') as pledge_datetime,");
		}
		 
		sql.append("c.first_name, c.last_name, c.address, c.state, c.city, c.zip_postal, f.fund_name, ");
		sql.append(" group_concat(cv.vendor_name ORDER BY cpv.id) as vendorNames, ");
		sql.append(" group_concat(cpv.vendor_pledge_amount ORDER BY cpv.id) vendorAmounts");
		sql.append(",CASE c.liheap_fund WHEN 'Y' THEN 'No' WHEN 'N' THEN 'Yes' ELSE 'Yes' END as liheapFund, ");
		sql.append( "CASE c.psehelp_fund WHEN 'Y' THEN 'No' WHEN 'N' THEN 'Yes' ELSE 'Yes' END as psehelpFund");
		sql.append(" from customer_pledge cp LEFT OUTER JOIN customer_pledge_vendor cpv ON cp.id=cpv.customer_pledge_id LEFT OUTER JOIN customer_vendor cv ON cpv.vendor_id = cv.id ");
		 sql.append(" LEFT OUTER JOIN customer_pledge_fund_source f ON cp.fund_id = f.id"); 
		sql.append(" LEFT OUTER JOIN customer c ON cp.customer_id = c.id where cp.id=");
		sql.append(customerPledgeId).append(" group by cp.id order by cpv.id");
		
		return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new ResultSetExtractor<CustomerPledge>() {
            @Override
            public CustomerPledge extractData(ResultSet rs) throws SQLException, DataAccessException {
            	CustomerPledge customerPledge =null;
                if (rs.next()) {
                	customerPledge = new CustomerPledge();
                	customerPledge.setFirstName(rs.getString("first_name"));
                	customerPledge.setLastName(rs.getString("last_name"));
                	customerPledge.setAddress(rs.getString("address"));
                	customerPledge.setCity(rs.getString("city"));
                	customerPledge.setState(rs.getString("state"));
                	customerPledge.setZipPostal(rs.getString("zip_postal"));
                	customerPledge.setPledgeDateTime(rs.getString("pledge_datetime"));
                	customerPledge.setTotalPledgeAmt(String.format("%.2f",rs.getDouble("total_amount")));
                	customerPledge.setFundName(rs.getString("fund_name"));
                		
					try {
						String vendorNames = rs.getString("vendorNames");
						if (vendorNames != null) {
							String vendorNameArr[] = vendorNames.split(",");
							customerPledge.setVendorCount(vendorNameArr.length);
							for (int index = 0; index < vendorNameArr.length; index++) {
								int id = index + 1;
								CoreUtils.setPropertyValue(customerPledge, "vendor" + (id)+"Name", vendorNameArr[index]);
							}
						}

						String vendorPAmounts = rs.getString("vendorAmounts");
						if (vendorPAmounts != null) {
							String pledgeAmountsArr[] = vendorPAmounts.split(",");
							for (int index = 0; index < pledgeAmountsArr.length; index++) {
								int id = index + 1;
								if(pledgeAmountsArr[index] != null) {
									CoreUtils.setPropertyValue(customerPledge, "vendor" + id + "Payment", pledgeAmountsArr[index]);
								} else {
									CoreUtils.setPropertyValue(customerPledge, "vendor" + id + "Payment", "");
								}
							}
						}
					} catch (Exception e) {
						logger.error("Error: "+e,e);
					}
                } 
                return customerPledge;
            }
        });
	}
	
	
	@Override
	public List<Location> getLocations(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String device, String langCode) throws TelAppointException {
		String sql = "select * from location where delete_flag='N' and enable='Y' order by placement";
		return jdbcCustomTemplate.getJdbcTemplate().query(sql, new ResultSetExtractor<List<Location>>() {
			List<Location> locations = new ArrayList<Location>();
            @Override
            public List<Location> extractData(ResultSet rs) throws SQLException, DataAccessException {
            	Location location;
            	while(rs.next()) {
            		location = new Location();
            		location.setLocationId(rs.getInt("id"));
            		location.setLocationName(rs.getString("location_name_online"));
            		if(CoreUtils.isIVR(device)) {
            			location.setLocationNameIvrTts(rs.getString("location_name_ivr_tts"));
            			location.setLocationNameIvrAudio(rs.getString("location_name_ivr_audio"));
            		}
            		locations.add(location);	
            	}
            	return locations;
            }
		 });		
	}
	
	@Override
	public List<Service> getServicesCallcenter(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String device, String langCode, Integer locationId, Integer departmentId) throws TelAppointException {
		StringBuilder sql = new StringBuilder();
		sql.append("select distinct s.id,s.service_name_online,s.service_name_ivr_tts,s.service_name_ivr_audio from location l, department d, location_department_resource ldr, resource r, resource_service rs, service s ");
		sql.append(" where l.id = ? and d.id = ? and ldr.location_id = l.id and ldr.department_id = d.id and ldr.`enable` = 'Y' and ldr.resource_id = r.id and r.`enable` = 'Y' and r.delete_flag = 'N' ");
		sql.append(" and rs.resource_id = r.id and rs.service_id = s.id and rs.`enable` = 'Y' and s.`enable` = 'Y' and s.delete_flag = 'N' order by s.placement");
		return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[]{locationId, departmentId}, new ResultSetExtractor<List<Service>>() {
			List<Service> services = new ArrayList<Service>();
            @Override
            public List<Service> extractData(ResultSet rs) throws SQLException, DataAccessException {
            	Service service;
            	while(rs.next()) {
            		service = new Service();
            		service.setServiceId(rs.getInt("id"));
            		service.setServiceNameOnline(rs.getString("service_name_online"));
            		if(CoreUtils.isIVR(device)) {
            			service.setServiceNameIvrTts(rs.getString("service_name_ivr_tts"));
            			service.setServiceNameIvrAudio(rs.getString("service_name_ivr_audio"));
            		}
            		services.add(service);
            	}
            	return services;
            }
		 });		
	}
	
	@Override
	public List<Service> getServicesNonCallcenter(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String device, String langCode, Integer locationId, Integer resourceId, Integer departmentId) throws TelAppointException {
		StringBuilder sql = new StringBuilder();
		sql.append("select distinct s.id, s.service_name_online from location l, department d, location_department_resource ldr, resource r, resource_service rs, service s ");
		sql.append("where l.id = ? and d.id = ? and r.id=? and ldr.location_id = l.id and ldr.department_id = d.id and ldr.`enable` = 'Y' and ldr.resource_id = r.id and r.`enable` = 'Y' and r.delete_flag = 'N'");
		sql.append("and rs.resource_id = r.id and rs.service_id = s.id and rs.`enable` = 'Y' and s.`enable` = 'Y' and s.delete_flag = 'N' order by s.placement");
		return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[]{locationId, departmentId, resourceId}, new ResultSetExtractor<List<Service>>() {
			List<Service> services = new ArrayList<Service>();
            @Override
            public List<Service> extractData(ResultSet rs) throws SQLException, DataAccessException {
            	Service service;
            	while(rs.next()) {
            		service = new Service();
            		service.setServiceId(rs.getInt("id"));
            		service.setServiceNameOnline(rs.getString("service_name_online"));
            		if(CoreUtils.isIVR(device)) {
            			service.setServiceNameIvrTts(rs.getString("service_name_ivr_tts"));
            			service.setServiceNameIvrAudio(rs.getString("service_name_ivr_audio"));
            		}
            		services.add(service);
            	}
            	return services;
            }
		 });		
	}
	
	public Schedule getSchedule(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, Long scheduleId) throws TelAppointException {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("select s.id, s.location_id, s.service_id, CONCAT(DATE_FORMAT(s.appt_date_time, '%Y-%m-%d'), 'T', TIME_FORMAT(s.appt_date_time, '%H:%i:%s')) as apptDateTime, s.customer_id ");
			sql.append(" , s.status");
			sql.append(" from schedule s where s.id=?");
			logger.debug("scheduleId:" + scheduleId);

			return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[] {scheduleId }, new ResultSetExtractor<Schedule>() {
				@Override
				public Schedule extractData(ResultSet rs) throws SQLException, DataAccessException {
					Schedule schedule = null;
					if (rs.next()) {
						schedule = new Schedule();
						schedule.setScheduleId(rs.getLong("id"));
						schedule.setLocationId(rs.getInt("location_id"));
						schedule.setServiceId(rs.getInt("service_id"));
						schedule.setApptDateTime(rs.getString("apptDateTime"));
						schedule.setCustomerId(rs.getLong("customer_id"));
						schedule.setStatus(rs.getInt("status"));
						
					}
					return schedule;
				}
			});
		} catch (DataAccessException  dae) {
			throw new TelAppointException(ErrorConstants.ERRRO_1079.getCode(), ErrorConstants.ERRRO_1079.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, dae.getMessage(), null);
		}
	}

	@Override
	public Long getProgramInstanceId(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, int locationId, int serviceId) throws TelAppointException {
		String sql = "select program_instance_id from service_location where service_id=? and location_id=?";
		return jdbcCustomTemplate.getJdbcTemplate().queryForObject(sql, new Object[]{serviceId, locationId}, Long.class);
	}
	
	@Override
	public Long getProgramId(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, int locationId, int serviceId) throws TelAppointException {
		String sql = "select program_id from service_location where service_id=? and location_id=?";
		return jdbcCustomTemplate.getJdbcTemplate().queryForObject(sql, new Object[]{serviceId, locationId}, Long.class);
	}

	@Override
	public Customer findByParticipantId(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, long participantID) throws TelAppointException {
		Customer customer = getCustomerByParticipantId(jdbcCustomTemplate, logger, participantID);
		return customer;
	}

	@Override
	public boolean updateEnrollemntId(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String enrollmentId, long scheduleId) throws TelAppointException {
		String sql = "update appointment set enrollment_id=? where schedule_id=?";
		jdbcCustomTemplate.getJdbcTemplate().update(sql, new Object[]{enrollmentId, scheduleId});
		return true;
	}

	@Override
	public boolean saveTransScriptMsg(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, TransScriptRequest transScriptReq) throws TelAppointException {
		StringBuilder sql = new StringBuilder("insert into trans_script_msg (schedule_id,timestamp,customer_id, display_flag,");
		if(transScriptReq.getFilePath() != null && !"".equals(transScriptReq.getFilePath())) {
			sql.append("file_path,");
		}
		sql.append("file_name) values (:scheduleId,now(),:customerId,:displayFlag,");
		if(transScriptReq.getFilePath() != null && !"".equals(transScriptReq.getFilePath())) {
			sql.append(":filePath,");
		}
		sql.append(":fileName)");
		
		List<SqlParameterSource> list = new ArrayList<SqlParameterSource>();
		MapSqlParameterSource mapSQLParameterSource;
		String fileNames[] = transScriptReq.getFileName();
		for (String fileName : fileNames) {
			mapSQLParameterSource = new MapSqlParameterSource();
			mapSQLParameterSource.addValue("scheduleId", transScriptReq.getScheduleId());
			mapSQLParameterSource.addValue("customerId", transScriptReq.getCustomerId());
			mapSQLParameterSource.addValue("displayFlag", "Y");
			if(transScriptReq.getFilePath() != null && !"".equals(transScriptReq.getFilePath())) {
				mapSQLParameterSource.addValue("filePath", transScriptReq.getFilePath());
			}
			mapSQLParameterSource.addValue("fileName", fileName);
			list.add(mapSQLParameterSource);
		}
		SqlParameterSource mapArray[] = new SqlParameterSource[list.size()];
		SqlParameterSource batchArray[] = list.toArray(mapArray);
		jdbcCustomTemplate.getNameParameterJdbcTemplate().batchUpdate(sql.toString(), batchArray);
		return true;
	}

	public List<TransScript> getTransScriptMsgs(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, long scheduleId) {
		StringBuilder sql = new StringBuilder("select * from trans_script_msg where schedule_id=?");
		try {
			return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[]{scheduleId}, new ResultSetExtractor<List<TransScript>>() {
	            @Override 
	            public List<TransScript> extractData(ResultSet rs) throws SQLException, DataAccessException {
	               List<TransScript> filePaths = new ArrayList<TransScript>();
	               TransScript transScript;
	                while (rs.next()) {
	                	transScript = new TransScript();
	                	transScript.setFileName(rs.getString("file_name"));
	                	transScript.setTransScriptId(rs.getLong("id"));
	                	filePaths.add(transScript);
	                }
	                return filePaths;
	            }
	        });
		} catch(DataAccessException dae) {
			logger.error("Error: "+dae,dae);
		}
		return null;
	}

	@Override
	public List<PendingEnrollment> getPendingEnrollments(JdbcCustomTemplate jdbcCustomTemplate, Logger logger) throws TelAppointException {
		StringBuilder sql = new StringBuilder("select * from ext_login_process where status='N' order by timestamp");
		return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new ResultSetExtractor<List<PendingEnrollment>>() {
            @Override 
            public List<PendingEnrollment> extractData(ResultSet rs) throws SQLException, DataAccessException {
               List<PendingEnrollment> pendingEnrollmentList = new ArrayList<PendingEnrollment>();
                PendingEnrollment pendingEnrollment;
                while (rs.next()) {
                	pendingEnrollment = new PendingEnrollment();
                	pendingEnrollment.setApiName(rs.getString("api_name"));
                	pendingEnrollment.setScheduleId(rs.getLong("schedule_id"));
                	pendingEnrollment.setJsonPayLoad(rs.getString("json_payload"));
                	pendingEnrollmentList.add(pendingEnrollment);
                }
                return pendingEnrollmentList;
            }
        });
	}
	
	@Override
	public void savePendingCreateOrCancelEnrollments(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, PendingEnrollment pendingEnrollment) throws TelAppointException {
		StringBuilder sql = new StringBuilder("insert into  ext_login_process (schedule_id, api_name, json_payload) values (?,?,?)");
		jdbcCustomTemplate.getJdbcTemplate().update(sql.toString(), new Object[]{pendingEnrollment.getScheduleId(), pendingEnrollment.getApiName(), pendingEnrollment.getJsonPayLoad()});
	}

	@Override
	public String getEnrollementId(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, long scheduleId) throws TelAppointException {
		String sql = "select enrollment_id from appointment where schedule_id=?";
		return jdbcCustomTemplate.getJdbcTemplate().queryForObject(sql, String.class, new Object[]{scheduleId});
	}

	@Override
	public void updateEnrollmentProcessStatus(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String apiName, long scheduleId) throws TelAppointException {
		String sql = "update ext_login_process set status='Y' where schedule_id=?";
		jdbcCustomTemplate.getJdbcTemplate().update(sql, new Object[]{scheduleId});
	}

	@Override
	public void deleteTransScriptMsg(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, long transScriptMsgId) throws TelAppointException {
		String sql = "delete from trans_script_msg where id=?";
		jdbcCustomTemplate.getJdbcTemplate().update(sql, new Object[]{transScriptMsgId});
	}
	public void updateTransId(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, Long scheduleId, Long transId) throws Exception {
		String sql = "update appointment set trans_id=? where schedule_id=?";
		if(transId != null && transId.longValue() > 0 ) {
			jdbcCustomTemplate.getJdbcTemplate().update(sql, new Object[]{transId, scheduleId});
		}
	}

    @Override
    public IvrLang getLang(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String langCode) {
        StringBuilder sql = new StringBuilder("select * from i18n_lang where lang_code=?");
        try {
            return jdbcCustomTemplate.getJdbcTemplate().queryForObject(sql.toString(), new Object[]{langCode}, new BeanPropertyRowMapper<IvrLang>(IvrLang.class));
        } catch(DataAccessException dae) {
            logger.error("Error: "+dae,dae);
        }
        return new IvrLang(false);
    }

	@Override
	public TransScriptEmailData getFileUploadConfirmEmaildata(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String langCode, long scheduleId) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" select ").append("'").append("s.appt_date_time|s.appt_date_time_display|r.prefix|r.first_name|r.last_name|r.email|l.location_name_online|l.address|l.city|l.state|l.zip|l.location_google_map|l.location_google_map_link|l.time_zone|p.procedure_name_online|d.department_name_online|ia.message_value.service|c.account_number|c.first_name|c.last_name|c.contact_phone|c.home_phone|c.work_phone|c.cell_phone|c.email|c.attrib1|c.attrib2|c.attrib3|c.attrib4|c.attrib5|c.attrib6|c.attrib7|c.attrib8|c.attrib9|c.attrib10|doc.display_text|r.resource_tts|r.resource_audio|l.location_name_ivr_tts|l.location_name_ivr_audio|p.procedure_name_ivr_tts|p.procedure_name_ivr_audio|d.location_name_ivr_tts|d.department_name_ivr_audio|ser.service_name_ivr_tts|ser.service_name_ivr_audio|doc.list_of_docs_tts|doc.list_of_docs_audio").append("' as displayKeys").append(",");
		sql.append("CONCAT_WS('|',s.appt_date_time,DATE_FORMAT(s.appt_date_time, sys.online_datetime_display),");
		sql.append(" IFNULL(r.prefix,''),IFNULL(r.first_name,''),IFNULL(r.last_name,''),IFNULL(r.email,''),IFNULL(l.location_name_online,''),IFNULL(l.address,''),IFNULL(l.city,''),IFNULL(l.state,''),IFNULL(l.zip,''),");
		sql.append("IFNULL(l.location_google_map,''),IFNULL(l.location_google_map_link,''),IFNULL(l.time_zone,''),IFNULL(p.procedure_name_online,''),IFNULL(d.department_name_online,''),IFNULL(ia.message_value,''),");
		sql.append("IFNULL(c.account_number,''),IFNULL(c.first_name,''),IFNULL(c.last_name,''),IFNULL(c.contact_phone,''),IFNULL(c.home_phone,''),IFNULL(c.work_phone,''),IFNULL(c.cell_phone,''),IFNULL(c.email,''),");
		sql.append("IFNULL(c.attrib1,''),IFNULL(c.attrib2,''),IFNULL(c.attrib3,''),IFNULL(c.attrib4,''),IFNULL(c.attrib5,''),IFNULL(c.attrib6,''),IFNULL(c.attrib7,''),IFNULL(c.attrib8,''),IFNULL(c.attrib9,''),");
		sql.append("IFNULL(c.attrib10,''),IFNULL(doc.display_text,''),CONCAT(IFNULL(r.first_name,''),' ',IFNULL(r.last_name,'')),IFNULL(r.resource_audio,''),IFNULL(l.location_name_ivr_tts,''),IFNULL(l.location_name_ivr_audio,''),");
		sql.append("IFNULL(p.procedure_name_ivr_tts,''),IFNULL(p.procedure_name_ivr_audio,''),IFNULL(d.location_name_ivr_tts,''),IFNULL(d.department_name_ivr_audio,''),IFNULL(ser.service_name_ivr_tts,''),");
		sql.append("IFNULL(ser.service_name_ivr_audio,''),IFNULL(doc.list_of_docs_tts,''),IFNULL(doc.list_of_docs_audio,'')) as displayValues from `schedule` s LEFT OUTER JOIN appointment a ON s.id = a.schedule_id ");
		sql.append("LEFT OUTER JOIN resource r ON s.resource_id = r.id LEFT OUTER JOIN location l ON s.location_id = l.id LEFT OUTER JOIN `procedure` p ON s.procedure_id = p.id LEFT OUTER JOIN department d ON s.department_id = d.id ");
		sql.append(" LEFT OUTER JOIN service ser ON s.service_id = ser.id LEFT OUTER JOIN customer c ON s.customer_id = c.id LEFT OUTER JOIN list_of_things_bring doc ON s.service_id = doc.service_id and ");
		sql.append(" doc.lang = ? LEFT OUTER JOIN i18n_aliases ia ON ser.service_name_online = ia.message_key and ia.device = 'online' ");
		sql.append(" and ia.lang = ? JOIN appt_sys_config sys where s.id = ?");
		return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[]{langCode, langCode, scheduleId},new ResultSetExtractor<TransScriptEmailData>() {
            @Override 
            public TransScriptEmailData extractData(ResultSet rs) throws SQLException, DataAccessException {
            	TransScriptEmailData transScriptEmailData = null;
                if (rs.next()) {
                	transScriptEmailData = new TransScriptEmailData();
                	transScriptEmailData.setDisplayKeys(rs.getString("displayKeys"));
                	transScriptEmailData.setDisplayValues(rs.getString("displayValues"));
                }
                return transScriptEmailData;
            }
        });
	}

	@Override
	public List<ServiceOption> getServiceListByLocationId(JdbcCustomTemplate jdbcCustomTemplate, String device, Integer locationId) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select s.id,s.service_name_online, sl.id as serviceLocationId, s.service_name_ivr_tts, s.service_name_ivr_audio from service s, service_location sl where sl.location_id=? and ");
		sql.append(" sl.service_id=s.id and s.delete_flag='N' and s.enable='Y'");
		try {
			return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[]{locationId}, serviceOptionMapper(device));
		} catch (DataAccessException dae) {
			throw new TelAppointException(ErrorConstants.ERROR_1082.getCode(), ErrorConstants.ERROR_1082.getMessage(), INTERNAL_SERVER_ERROR, dae.getMessage(), "LocationId: "+locationId);
		}
	}

	public static RowMapper<ServiceOption> serviceOptionMapper(final String device) {	
        return (rs, rowNum) -> {
        	ServiceOption service = new ServiceOption();
            service.setServiceId(rs.getInt("id"));
            service.setServiceNameOnline(rs.getString("service_name_online"));
            //service.setServiceLocationId(rs.getInt("serviceLocationId"));
            if(CoreUtils.isIVR(device)) {
            	service.setServiceNameIvrTts(rs.getString("service_name_ivr_tts"));
            	service.setServiceNameIvrAudio(rs.getString("service_name_ivr_audio"));
            }
            return service;
        };
    }
	
	@Override
    public List<String> getHolidaysMap(JdbcCustomTemplate jdbcCustomTemplate, String timeZone) throws Exception {
    	StringBuilder sql = new StringBuilder();
        sql.append("select distinct(`date`) as `date` from holidays");
        sql.append(" where `date` >= CONVERT_TZ(now(),'US/Central','").append(timeZone).append("')");
        sql.append(" order by date");
        return  jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(),  noOfClosedMpapper());
    }
	
	@Override
    public List<String> getClosedDaysMap(JdbcCustomTemplate jdbcCustomTemplate,Long locationId, String timeZone) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select distinct(`date`) as `date` from closed_days");
		sql.append(" where `date` >= CONVERT_TZ(now(),'US/Central','").append(timeZone).append("')");
		sql.append(" and location_id=:locationId order by `date`");
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
	    paramSource.addValue("locationId", locationId);
	    return  jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), paramSource, noOfClosedMpapper());
    }

	 public static RowMapper<String> noOfClosedMpapper() {	
	        return (rs, rowNum) -> {
	            return rs.getString("date");
	        };
	 }
	@Override
	public boolean updateParticipantId(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, Long participantId, Long customerId) throws Exception {
		final StringBuilder sql = new StringBuilder();
		sql.append("update customer set participant_id=? where id=?");
		int count = jdbcCustomTemplate.getJdbcTemplate().update(sql.toString(), new Object[]{participantId, customerId});	
		return count != 0;
	}
	
	@Override
	public String insertTokenAndGet(JdbcCustomTemplate jdbcCustomTemplate, String clientCode, int expiryInSec) throws Exception {
		StringBuilder sql = new StringBuilder();
		String timeStr = intToStringDuration(expiryInSec);
		System.out.println(timeStr);
		sql.append("insert into tokens (client_code, expiry_stamp, token) values (:clientCode, ADDTIME(now(), '"+timeStr+"'), :token) ");
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("clientCode", clientCode);
		String token = getToken(clientCode);
		paramSource.addValue("token", token);
		KeyHolder holder = new GeneratedKeyHolder();
		jdbcCustomTemplate.getNameParameterJdbcTemplate().update(sql.toString(), paramSource, holder);
		if(holder.getKey().longValue() > 0) return token;
		return "";
	}
	
	private static String intToStringDuration(int expiryInSecs) {
	    String result = "";
	    int hours = 0, minutes = 0, seconds = 0;
	    hours = expiryInSecs / 3600;
	    minutes = (expiryInSecs - hours * 3600) / 60;
	    seconds = (expiryInSecs - (hours * 3600 + minutes * 60));
	    result = String.format("%02d:%02d:%02d", hours, minutes, seconds);
	    return result;
	}
	
	public String getToken(String clientCode) {
		StringBuilder appendedStr = new StringBuilder();
		appendedStr.append(clientCode);
		appendedStr.append(new Timestamp(System.currentTimeMillis()));
		return CoreUtils.getToken(appendedStr.toString());
	}

	@Override
	public List<Company> getCompanyList(JdbcCustomTemplate jdbcCustomTemplate, Logger logger) throws Exception {
		String sql = "select id, company_name_online,company_name_ivr_tts,company_name_ivr_audio from company where delete_flag='N'";
		return jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), companyMapper());
	}

	private RowMapper<Company> companyMapper() {
		 return (rs, rowNum) -> {
			 Company company = new Company();
			 company.setCompanyId(rs.getInt("id"));
			 company.setCompanyNameOnline(rs.getString("company_name_online"));
			 company.setCompanyNameTts(rs.getString("company_name_ivr_tts"));
			 company.setCompanyNameAudio(rs.getString("company_name_ivr_audio"));
			 return company;
		 };
	}

	@Override
	public List<Procedure> getProcedureList(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String companyId) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" select p.id, p.procedure_name_online,p.procedure_name_ivr_tts,p.procedure_name_ivr_audio from `procedure` p");
		sql.append(" LEFT OUTER JOIN company_procedure cp on cp.procedure_id=p.id");
		sql.append(" where cp.company_id=:companyId");
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("companyId", companyId);
		return jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), paramSource, procedureMapper());
	}

	private RowMapper<Procedure> procedureMapper() {
		return (rs, rowNum) -> {
			 Procedure procedure = new Procedure();
			 procedure.setProcedureId(rs.getInt("id"));
			 procedure.setProcedureNameOnline(rs.getString("procedure_name_online"));
			 procedure.setProcedureNameTts(rs.getString("procedure_name_ivr_tts"));
			 procedure.setProcedureNameAudio(rs.getString("procedure_name_ivr_audio"));
			 return procedure;
		 };
	}
	
	@Override
	public List<Location> getLocationList(JdbcCustomTemplate jdbcCustomTemplate, boolean isActiveList) throws Exception {
		StringBuilder sql = new StringBuilder("select * from location");
		sql.append(" where delete_flag ='N' ");
		if (isActiveList) {
			sql.append(" and enable ='Y'");
		}
		sql.append(" order by placement asc");
		return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), locationMapperWithBasic());
		
	}

	@Override
	public List<Location> getLocationList(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String procedureId) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select l.id, l.location_name_online,l.location_name_ivr_tts,l.location_name_ivr_audio,l.address, l.city, l.state, l.zip,  ");
		sql.append("CONCAT(LEFT(IF(l.work_phone IS NULL,'', l.work_phone),3),'-',mid(IF(l.work_phone IS NULL,'', l.work_phone),4,3),'-', RIGHT(IF(l.work_phone IS NULL,'', l.work_phone),4)) as workPhone");
		sql.append(" from `location` l");
		sql.append(" LEFT OUTER JOIN procedure_location pl on pl.location_id=l.id");
		sql.append(" where pl.procedure_id=:procedureId and l.delete_flag='N' and l.enable='Y'");
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("procedureId", procedureId);
		return jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), paramSource, locationMapper());
	}
	
	private RowMapper<Location> locationMapperWithBasic() {
		return (rs, rowNum) -> {
			 Location location = new Location();
			 location.setLocationId(rs.getInt("id"));
			 location.setLocationName(rs.getString("location_name_online"));
			 location.setAddress(rs.getString("address"));
             location.setCity(rs.getString("city"));
             location.setState(rs.getString("state"));
             location.setZip(rs.getString("zip"));
             location.setEnable(null);
             location.setLocationGoogleMap(rs.getString("location_google_map"));
			 return location;
		 };
	}
	
	private RowMapper<Location> locationMapper() {
		return (rs, rowNum) -> {
			 Location location = new Location();
			 location.setLocationId(rs.getInt("id"));
			 location.setLocationName(rs.getString("location_name_online"));
			 location.setLocationNameIvrTts(rs.getString("location_name_ivr_tts"));
			 location.setLocationNameIvrAudio(rs.getString("location_name_ivr_audio"));
			 location.setAddress(rs.getString("address"));
             location.setCity(rs.getString("city"));
             location.setState(rs.getString("state"));
             location.setZip(rs.getString("zip"));
             location.setWorkPhone(rs.getString("workPhone"));
			 return location;
		 };
	}

	@Override
	public List<Department> getDepartmentList(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String locationId) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select distinct d.id, d.department_name_online,d.department_name_ivr_tts,d.department_name_ivr_audio from department d");
		sql.append(" LEFT OUTER JOIN location_department_resource lds on lds.department_id=d.id");
		sql.append(" where lds.location_id=:locationId and d.delete_flag='N'");
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("locationId", locationId);
		return jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), paramSource, departmentMapper());
	}
	
	private RowMapper<Department> departmentMapper() {
		return (rs, rowNum) -> {
			 Department department = new Department();
			 department.setDepartmentId(rs.getInt("id"));
			 department.setDepartmentName(rs.getString("department_name_online"));
			 department.setDepartmentNameIvrTts(rs.getString("department_name_ivr_tts"));
			 department.setDepartmentNameIvrAudio(rs.getString("department_name_ivr_audio"));
			 return department;
		 };
	}
	
	public List<Resource> getResourceList(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String locationId, String departmentId) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select distinct r.id, CONCAT( r.prefix,'',r.first_name,' ',r.last_name) as resourceName, r.resource_audio from resource r");
		sql.append(" LEFT OUTER JOIN resource_service rs on rs.resource_id=r.id");
		sql.append(" LEFT OUTER JOIN location_department_resource lds on lds.resource_id=r.id");
		sql.append(" where r.location_id=lds.location_id and rs.resource_id=lds.resource_id and r.location_id=1 and lds.department_id=1 and r.delete_flag='N' and r.enable='Y'");
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("locationId", locationId);
		paramSource.addValue("departmentId", departmentId);
		List<Resource> resourceList = jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), paramSource,resourceMapper());
		ApptSysConfig apptSysConfig = getApptSysConfig(jdbcCustomTemplate, logger);
		if(apptSysConfig != null && "Y".equalsIgnoreCase(apptSysConfig.getAllowAnyDoctor())) {
			Resource resource = new Resource();
			resource.setResourceId(-2);
			resource.setResourceName("Any Doctor");
			if(resourceList != null) resourceList.add(0, resource);
		}
		return resourceList;
	}
	
	private RowMapper<Resource> resourceMapper() {
		return (rs, rowNum) -> {
			Resource resource = new Resource();
			 resource.setResourceId(rs.getInt("id"));
			 resource.setResourceName(rs.getString("resourceName"));
			 resource.setResourceAudio(rs.getString("resource_audio"));
			 return resource;
		 };
	}
	
	@Override
	public List<ServiceOption> getServiceList(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String resourceId) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select s.id, s.service_name_online from service s");
		sql.append(" LEFT OUTER JOIN resource_service rs on rs.service_id=s.id");
		sql.append(" where rs.resource_id=:resourceId and s.delete_flag='N' and s.enable='Y'");
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("resourceId", resourceId);
		return jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), paramSource,serviceMapper());
	}
	
	private RowMapper<ServiceOption> serviceMapper() {
		return (rs, rowNum) -> {
			ServiceOption service = new ServiceOption();
			service.setServiceId(rs.getInt("id"));
			service.setServiceNameOnline(rs.getString("service_name_online"));
			return service;
		};
	}
	
	@Override 
	public MobileAppPage getMobileAppPages(JdbcCustomTemplate jdbcCustomerTemplate, String pageName) throws Exception {
	   String sql = "select * from mobile_app_pages where page_name=?";
	   return jdbcCustomerTemplate.getJdbcTemplate().query(sql, new Object[]{pageName}, new ResultSetExtractor<MobileAppPage>() {
           @Override
           public MobileAppPage extractData(ResultSet rs) throws SQLException, DataAccessException {
        	   MobileAppPage mobileAppPage = new MobileAppPage();
               if (rs.next()) {
            	  mobileAppPage.setContentKey(rs.getString("content_key"));
            	  mobileAppPage.setContentValue(rs.getString("content_value"));
               }
               return mobileAppPage;
           }
       });
	}

	@Override
	public List<String> getMessages(JdbcCustomTemplate jdbcCustomTemplate) throws Exception {
		String sql = "select message from messages where effective_date <= DATE(now()) and expiry_date >= DATE(now()) and delete_flag = 'N'";
		return jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), messageMapper());
	}
	
	private RowMapper<String> messageMapper() {
		return (rs, rowNum) -> {
			return rs.getString("message");
		};
	}
	
	@Override
	public ListOfDocs getListOfDocsToBring(JdbcCustomTemplate jdbcCustomTemplate, Integer serviceId) throws Exception {
		StringBuilder sql = new StringBuilder("select IF(s.service_name_online IS NULL,'AllServices',s.service_name_online) as service_name_online, ltb.display_text from list_of_things_bring ltb");
		sql.append(" LEFT OUTER JOIN service s on s.id=ltb.service_id where lang='us-en' and device='mobile' and ltb.service_id=?");;
		 return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[]{serviceId}, new ResultSetExtractor<ListOfDocs>() {
	           @Override
	           public ListOfDocs extractData(ResultSet rs) throws SQLException, DataAccessException {
	        	   ListOfDocs listOfDocs = new ListOfDocs();
	               if (rs.next()) {
	            	   listOfDocs.setServiceName(rs.getString("service_name_online"));
	            	   listOfDocs.setDisplayText(rs.getString("display_text"));
	            	 
	               }
	               return listOfDocs;
	           }
	       });
	}
	
	
	@Override
	public boolean isTokenValid(JdbcCustomTemplate jdbcCustomTemplate, String clientCode, String token) throws Exception {
		String sql = "select count(1) from tokens where client_code=:clientCode and expiry_stamp > now() and token=:token";
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("clientCode", clientCode);
		paramSource.addValue("token", token);
		return jdbcCustomTemplate.getNameParameterJdbcTemplate().queryForObject(sql, paramSource, Integer.class) > 0;
	}

	@Override
	public AvailableDateTimes getAvailableTimes(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, Long locationId, Long departmentId, Long resourceId, Long serviceId,
			String availDate, Long blockTimeInMins) throws Exception {
		Map<String, Object> inParameters = new HashMap<String, Object>();
        try {
        	String spName="get_avail_times_sp";
            SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcCustomTemplate.getJdbcTemplate()).withProcedureName("get_avail_times_sp");

            inParameters.put(SPConstants.LOCATION_ID.getValue(), locationId);
            inParameters.put(SPConstants.DEPARTMENT_ID.getValue(), departmentId);
            inParameters.put(SPConstants.SERVICE_ID.getValue(), serviceId);
            inParameters.put(SPConstants.BLOCK_TIME_IN_MINS.getValue(), blockTimeInMins);
            inParameters.put(SPConstants.RESOURCE_ID.getValue(), resourceId);
            inParameters.put(SPConstants.AVAILABLE_DATE.getValue(), new java.sql.Date((new SimpleDateFormat("yyyy-MM-dd").parse(availDate)).getTime()));
            logger.info("getAvailableTimesCallcenter input params = " + inParameters);
            long startTime = System.currentTimeMillis();
            Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(inParameters);
            long endTime = System.currentTimeMillis();
            logTimeTaken(spName, startTime, endTime);
            
            Object availDateTimes = simpleJdbcCallResult.get(SPConstants.AVAILABLE_DATE_TIMES.getValue());
            Object errorMsg = simpleJdbcCallResult.get(SPConstants.ERROR_MESSAGE.getValue());
            String sortedUniqueTime = null;
            String displayTimeList = null;
            if(availDateTimes != null) {
            	String availDateTimeStr = (String)availDateTimes;
            	if(!"".equals(availDateTimeStr)) {
            		 logger.info("AvailableDateTimes: "+(String)availDateTimes);
                     sortedUniqueTime = getSortedUniqueTime(availDateTimes);
                     logger.info("sortedUniqueTimes: "+sortedUniqueTime);
                     displayTimeList = getDisplayTimeList(sortedUniqueTime);	  
            	} 
            } 
            return new AvailableDateTimes(null, sortedUniqueTime == null ? null : sortedUniqueTime, displayTimeList == null ? null : displayTimeList,  errorMsg == null ? null : errorMsg.toString());
        } catch (DataAccessException dae) {
            throw new TelAppointException(ErrorConstants.ERROR_1069.getCode(), ErrorConstants.ERROR_1069.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, dae.getMessage(), "getAvailableTimesCallcenter input params = " + inParameters);
        }
	}
	
	@Override
	public HoldAppt holdAppointment(Logger logger, JdbcCustomTemplate jdbcCustomTemplate, String device, Long locationId, Long resourceId, Long procedureId, Long departmentId,
			Long serviceId, Long customerId, String apptDateTime, ClientDeploymentConfig cdConfig, Long transId) throws Exception {
		Map<String, Object> inParameters = new HashMap<String, Object>();
		try {
			String spName = "hold_appointment_sp";
			SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcCustomTemplate.getJdbcTemplate()).withProcedureName(spName);

			inParameters.put(SPConstants.APPT_DATE_TIME.getValue(), apptDateTime);
			inParameters.put(SPConstants.LOCATION_ID.getValue(), locationId);
			inParameters.put(SPConstants.RESOURCE_ID.getValue(), resourceId);
			inParameters.put(SPConstants.PROCEDURE_ID.getValue(), procedureId);
			inParameters.put(SPConstants.DEPARTMENT_ID.getValue(), departmentId);
			inParameters.put(SPConstants.SERVICE_ID.getValue(), serviceId);
			inParameters.put(SPConstants.CUSTOMER_ID.getValue(), customerId);
			inParameters.put(SPConstants.BLOCK_TIME_IN_MINS.getValue(), cdConfig.getBlockTimeInMins());
			inParameters.put(SPConstants.TRANS_ID.getValue(), transId);
			inParameters.put(SPConstants.DEVICE.getValue(), device);

			logger.info("holdAppointment input params: " + inParameters);
			long startTime = System.currentTimeMillis();
			Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(inParameters);
			long endTime = System.currentTimeMillis();
			logTimeTaken(spName, startTime, endTime);

			Object schedule_id = simpleJdbcCallResult.get(SPConstants.RETURN_SCHEDULE_ID.getValue());
			Object errorMsg = simpleJdbcCallResult.get(SPConstants.ERROR_MESSAGE.getValue());
			Object display_datetime = simpleJdbcCallResult.get(SPConstants.DISPLAY_DATETIME.getValue());
			
			if (schedule_id != null && display_datetime != null) {
				return new HoldAppt(Long.parseLong(schedule_id.toString()), display_datetime.toString(), errorMsg == null ? null : errorMsg.toString());
			} else {
				return new HoldAppt(null, null, errorMsg == null ? null : errorMsg.toString(), false);
			}
		} catch (DataAccessException dae) {
			throw new TelAppointException(ErrorConstants.ERROR_1076.getCode(), ErrorConstants.ERROR_1076.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, dae.getMessage(),
					"holdAppointmentCallCenter input params: " + inParameters);
		}
	}

	@Override
	public boolean updateCustomerIdInSchedule(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, Long customerId, Long scheduleId) throws Exception {
		String sql = "update schedule set customer_id=? where id=?";
		return jdbcCustomTemplate.getJdbcTemplate().update(sql, new Object[]{customerId, scheduleId})!=0;
	}
	
	private List<LoginPageFields> getLoginPageFields(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String device) {
		StringBuilder sql = new StringBuilder("select distinct param_column, java_reflection");
		if(CoreUtils.isMobile(device)) {
			 sql.append(" from login_param_config where device_type='mobile'");
		} else if(CoreUtils.isOnline(device)) {
			sql.append(" from online_page_fields where login_type in ('authenticate','update') ");
		} else if(CoreUtils.isIVR(device)) {
			sql.append(" from ivr_page_fields ");
		}
		
		return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new ResultSetExtractor<List<LoginPageFields>>() {
            final List<LoginPageFields> loginPageFieldList = new ArrayList<>();
            @Override
            public final List<LoginPageFields> extractData(ResultSet rs) throws SQLException, DataAccessException {
            	LoginPageFields loginPageFields = null;
                while (rs.next()) {
                	loginPageFields = new LoginPageFields();
                	String paramColumn = rs.getString("param_column");
                	loginPageFields.setParamColumn(paramColumn);
                	loginPageFields.setParamColumnForQuery("c."+paramColumn);
                	if("contact_phone".equals(paramColumn)) {
                		paramColumn = "CONCAT(LEFT(c.contact_phone,3),'-',MID(c.contact_phone,4,3),'-',RIGHT(c.contact_phone,4)) as contact_phone";
                		loginPageFields.setParamColumnForQuery(paramColumn);
                	}
                	
                	if("home_phone".equals(paramColumn)) {
                		paramColumn = "CONCAT(LEFT(c.home_phone,3),'-',MID(c.home_phone,4,3),'-',RIGHT(c.home_phone,4)) as home_phone";
                		loginPageFields.setParamColumnForQuery(paramColumn);
                	}
                	
                	loginPageFields.setJavaRef(rs.getString("java_reflection"));
                	loginPageFieldList.add(loginPageFields);
                }
                return loginPageFieldList;
            }
        });
 	}
	
	@Override
	public VerifyPageData getVerfiyPageData(JdbcCustomTemplate jdbcCustomTemplate, Logger logger, String device, String langCode, Long scheduleId,Map<String, String> aliasMap) throws Exception {
		StringBuilder sql = new StringBuilder();
		List<LoginPageFields> loginPageFields = getLoginPageFields(jdbcCustomTemplate, logger, device);
		List<String> paramColumns = loginPageFields.stream().distinct().map(LoginPageFields::getParamColumnForQuery).collect(Collectors.toList());
		
		sql.append(" select DATE_FORMAT(sc.appt_date_time,'%m/%d/%Y %h:%i %p') as appt_date_time, concat(r.prefix,' ', r.first_name,' ', r.last_name) as resourceName ");
		sql.append(",s.service_name_online, l.location_name_online, lb.display_text, IF(p.procedure_name_online IS NULL,'',p.procedure_name_online) as procedure_name_online");
		sql.append(",IF(de.department_name_online IS NULL,'',de.department_name_online) as department_name_online, l.time_zone");
		if(!loginPageFields.isEmpty()) {
			sql.append(","+StringUtils.join(paramColumns, ","));
		}
		sql.append(" from schedule sc");
		sql.append(" left outer join customer c on c.id=sc.customer_id");
		sql.append(" left outer join service s on s.id=sc.service_id");
		sql.append(" left outer join resource r on r.id=sc.resource_id");
		sql.append(" left outer join location l on l.id=sc.location_id");
		sql.append(" left outer join `procedure` p on p.id=sc.procedure_id");
		sql.append(" left outer join department de on de.id=sc.department_id");
		sql.append(" left outer join list_of_things_bring lb on lb.service_id=sc.service_id" );
		sql.append(" where sc.id=:scheduleId and lb.lang=:langCode");

		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		
		paramSource.addValue("scheduleId", scheduleId);
		paramSource.addValue("langCode", langCode);
		
		List<VerifyPageData> verifyPageData = jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), paramSource, verifyPageMapper(logger,loginPageFields, aliasMap));
		if(!verifyPageData.isEmpty()) {
			return verifyPageData.get(0);
		}
		return null;
	}

	private RowMapper<VerifyPageData> verifyPageMapper(Logger logger, List<LoginPageFields> loginPageFields, Map<String,String> aliasMap) throws Exception {
		return (rs, num) -> {
			VerifyPageData verifyPageData = new VerifyPageData();
			verifyPageData.setCustomerId(null);
			verifyPageData.setApptDateTime(rs.getString("appt_date_time"));
			verifyPageData.setResourceName(rs.getString("resourceName"));
			//verifyPageData.setContactPhone(rs.getString("contact_phone"));
			//verifyPageData.setAccountNumber(rs.getString("account_number"));
			
			 if (aliasMap != null) {
				 verifyPageData.setLocationName(aliasMap.get(rs.getString("location_name_online")) ==null?rs.getString("location_name_online"):aliasMap.get(rs.getString("location_name_online")));
				 verifyPageData.setServiceName(aliasMap.get(rs.getString("service_name_online"))==null?rs.getString("service_name_online"):aliasMap.get(rs.getString("service_name_online")));
				 verifyPageData.setProcedureName(aliasMap.get(rs.getString("procedure_name_online"))==null?rs.getString("procedure_name_online"):aliasMap.get(rs.getString("procedure_name_online")));
				 verifyPageData.setDepartmentName(aliasMap.get(rs.getString("department_name_online"))==null?rs.getString("department_name_online"):aliasMap.get(rs.getString("department_name_online")));
				 verifyPageData.setTimeZone(aliasMap.get(rs.getString("time_zone"))==null?rs.getString("time_zone"):aliasMap.get(rs.getString("time_zone")));
             } else {
            	 verifyPageData.setServiceName(rs.getString("service_name_online"));
     			 verifyPageData.setLocationName(rs.getString("location_name_online"));
     			 verifyPageData.setProcedureName(rs.getString("procedure_name_online"));
     			 verifyPageData.setDepartmentName(rs.getString("department_name_online"));
     			 verifyPageData.setTimeZone(rs.getString("time_zone"));
             }
			
			verifyPageData.setListOfDocsToBring(rs.getString("display_text"));
			for(LoginPageFields loginPageField : loginPageFields) {
				try {
					CoreUtils.setPropertyValue(verifyPageData, loginPageField.getJavaRef(), rs.getString(loginPageField.getParamColumn()));
				} catch (Exception e) {
					logger.error("Customer data population failed!.");
				}
			}
			return verifyPageData;
		};
	}

	public Long getHouseHoldValue(JdbcCustomTemplate jdbcCustomTemplate)  {
		String sql = "SELECT nextval('sq_my_sequence') as next_sequence";
		return jdbcCustomTemplate.getJdbcTemplate().queryForObject(sql, Long.class);
	}
	
	public Integer getResourceIdFromCustomer(JdbcCustomTemplate jdbcCustomTemplate, long customerId) throws Exception {
		String sql = "select resource_id from customer where id=?";
		try {
			return jdbcCustomTemplate.getJdbcTemplate().queryForObject(sql, new Object[]{customerId},Integer.class);
		} catch(EmptyResultDataAccessException erda) {
			return null;
		}
	}
}


class TimeStringComparator implements Comparator<String> {
    private DateFormat primaryFormat = new SimpleDateFormat("HH:mm:ss");
    private DateFormat secondaryFormat = new SimpleDateFormat("H:mm:ss");

    @Override
    public int compare(String time1, String time2) {
        try {
            return timeInMillis(time1) - timeInMillis(time2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int timeInMillis(String time) throws ParseException {
        return timeInMillis(time, primaryFormat);
    }

    private int timeInMillis(String time, DateFormat format) throws ParseException {
        try {
            Date date = format.parse(time);
            return (int) date.getTime();
        } catch (ParseException e) {
            if (format != secondaryFormat) {
                return timeInMillis(time, secondaryFormat);
            } else {
                throw e;
            }
        }
    }
    
    
}