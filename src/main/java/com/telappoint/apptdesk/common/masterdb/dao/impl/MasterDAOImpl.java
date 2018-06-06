package com.telappoint.apptdesk.common.masterdb.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.telappoint.apptdesk.common.constants.ErrorConstants;
import com.telappoint.apptdesk.common.masterdb.dao.MasterDAO;
import com.telappoint.apptdesk.common.model.Client;
import com.telappoint.apptdesk.common.model.ClientCustomerInfo;
import com.telappoint.apptdesk.common.model.ClientDeploymentConfig;
import com.telappoint.apptdesk.common.model.ClientInfo;
import com.telappoint.apptdesk.common.model.IPhoneClientInfo;
import com.telappoint.apptdesk.common.model.MobileAppPage;
import com.telappoint.apptdesk.handlers.exception.TelAppointException;

/**
 * 
 * @author Balaji N
 *
 */
@Repository
public class MasterDAOImpl implements MasterDAO {

	@Autowired
	private JdbcTemplate masterJdbcTemplate;
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	/**
	 * This will injected from spring content.
	 * @param jdbcTemplate
	 */
	public MasterDAOImpl(JdbcTemplate jdbcTemplate) {
		this.masterJdbcTemplate = jdbcTemplate;
	}

	public MasterDAOImpl() {
	}

	@Override
	public void getClients(final String key, final Map<String, Client> clientCacheMap) throws TelAppointException, Exception {
		String query = "select * from client c where delete_flag='N'";
		try {
			masterJdbcTemplate.query(query.toString(), new ResultSetExtractor<Map<String, Client>>() {
				@Override
				public Map<String, Client> extractData(ResultSet rs) throws SQLException, DataAccessException {
					Client client;
					String clientCode;
					while (rs.next()) {
						clientCode = rs.getString("client_code");
						client = new Client();
						client.setClientId(rs.getInt("id"));
						client.setClientCode(clientCode);
						client.setClientName(rs.getString("client_name"));
						client.setWebsite(rs.getString("website"));
						client.setContactEmail(rs.getString("contact_email"));
						client.setFax(rs.getString("fax"));
						client.setAddress(rs.getString("address"));
						client.setAddress2(rs.getString("address2"));
						client.setCity(rs.getString("city"));
						client.setState(rs.getString("state"));
						client.setZip(rs.getString("zip"));
						client.setCountry(rs.getString("country"));
						client.setDbName(rs.getString("db_name"));
						client.setDbServer(rs.getString("db_server"));
						client.setCacheEnabled(rs.getString("cache_enabled"));
						client.setApptLink(rs.getString("appt_link"));
						client.setDirectAccessNumber(rs.getString("direct_access_number"));
						client.setAppcode(rs.getString("appcode"));
						client.setExtLoginId(rs.getInt("ext_login_id"));
						client.setExtLoginPassword(rs.getString("ext_login_password"));
						clientCacheMap.put(key + "|" + clientCode, client);
					}
					return clientCacheMap;
				}
			});
		} catch (DataAccessException dae) {
			throw new TelAppointException(ErrorConstants.ERROR_1000.getCode(), ErrorConstants.ERROR_1000.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, dae.getMessage(), "");
		}
	}

	@Override
	public void getClientDeploymentConfig(final String key, final String clientCode, int clientId, final Map<String, Object> cacheObjectMap) throws TelAppointException, Exception {
		String query = "select * from client_deployment_config c where client_id = ?";

		try {
			masterJdbcTemplate.query(query.toString(), new Object[] { clientId }, new ResultSetExtractor<Map<String, Object>>() {
				@Override
				public Map<String, Object> extractData(ResultSet rs) throws SQLException, DataAccessException {
					ClientDeploymentConfig clientDeploymentConfig;
					if (rs.next()) {
						clientDeploymentConfig = new ClientDeploymentConfig();
						clientDeploymentConfig.setTimeZone(rs.getString("time_zone"));
						clientDeploymentConfig.setDateFormat(rs.getString("date_format"));
						clientDeploymentConfig.setTimeFormat(rs.getString("time_format"));
						clientDeploymentConfig.setDateyyyyFormat(rs.getString("date_yyyy_format"));
						clientDeploymentConfig.setFullDateFormat(rs.getString("full_date_format"));
						clientDeploymentConfig.setFullDatetimeFormat(rs.getString("full_datetime_format"));
						clientDeploymentConfig.setFullTextualdayFormat(rs.getString("full_textualday_format"));
						clientDeploymentConfig.setPhoneFormat(rs.getString("phone_format"));
						clientDeploymentConfig.setPopupCalendardateFormat(rs.getString("popup_calendardate_format"));
						clientDeploymentConfig.setLeadTimeInSeconds(rs.getInt("notify_phone_lead_time"));
						clientDeploymentConfig.setLagTimeInSeconds(rs.getInt("notify_phone_lag_time"));
						clientDeploymentConfig.setBlockTimeInMins(rs.getInt("block_time_in_mins"));
						clientDeploymentConfig.setCallCenterLogic(rs.getString("call_center_logic"));
						cacheObjectMap.put(key + "|" + clientCode, clientDeploymentConfig);
					}
					return cacheObjectMap;
				}
			});
		} catch (DataAccessException dae) {
			throw new TelAppointException(ErrorConstants.ERROR_1001.getCode(), ErrorConstants.ERROR_1001.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, dae.getMessage(), "");
		}
	}

	@Override
	public void getPageValidationMessages(String device, final Map<String, String> validationMsgMap) throws TelAppointException, Exception {
		String query = "select * from i18n_display_page_validation_msg c where device = ?";
		try {
			masterJdbcTemplate.query(query.toString(), new Object[] { device }, new ResultSetExtractor<Map<String, String>>() {
				@Override
				public Map<String, String> extractData(ResultSet rs) throws SQLException, DataAccessException {
					while (rs.next()) {
						String lang = rs.getString("lang");
						String messageKey = rs.getString("message_key");
						String messageValue = rs.getString("message_value");
						validationMsgMap.put(lang + "|" + messageKey, messageValue);
					}
					return validationMsgMap;
				}
			});
		} catch (DataAccessException dae) {
			throw new TelAppointException(ErrorConstants.ERROR_1006.getCode(), ErrorConstants.ERROR_1006.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, dae.getMessage(), "");
		}
	}
	
	@Override
	public List<String> getClientDirectAccessNumbers() throws TelAppointException, Exception {
		String sql = "select distinct direct_access_number from client where 1=1 and direct_access_number is not null";
		try {
		    List<String> list = masterJdbcTemplate.query(sql, (rs, rowNum) -> {
		        return rs.getString("direct_access_number");
		    });
		    return list == null || list.isEmpty() ? new ArrayList<String>() : list;
		} catch (DataAccessException dae) {
			throw new TelAppointException(ErrorConstants.ERROR_1006.getCode(), ErrorConstants.ERROR_1006.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, dae.getMessage(), "");
		}
	}
	
	@Override
    public List<IPhoneClientInfo> getMobileDemoClientList() {      
        StringBuilder sql = new StringBuilder();
        sql.append(" select c.client_location_google_map,c.business_type,c.city,c.state,c.zip,c.address, ");
        sql.append(" c.client_name, IF(c.contact_phone IS NULL OR c.contact_phone = '','',CONCAT(LEFT(c.contact_phone,3),'-',MID(c.contact_phone,4,3),'-',RIGHT(c.contact_phone, 4))) as phone from client c");
        sql.append(" where c.delete_flag = 'N' and c.active = 'Y' and c.id in (select distinct mad.client_id from mobile_app_demos mad)");
        return namedParameterJdbcTemplate.query(sql.toString(), iphoneRowMapper());
    }

	private RowMapper<IPhoneClientInfo> iphoneRowMapper() {
		return (rs, rowNum) -> {
			IPhoneClientInfo iPhoneClientInfo = new IPhoneClientInfo();
			iPhoneClientInfo.setClientCode(rs.getString("client_code"));
			iPhoneClientInfo.setClientType(rs.getString("business_type"));
			iPhoneClientInfo.setName(rs.getString("client_name"));
			iPhoneClientInfo.setPhoneNumber(rs.getString("phone"));
			iPhoneClientInfo.setCity(rs.getString("city"));
			iPhoneClientInfo.setState(rs.getString("state"));
			iPhoneClientInfo.setZip(rs.getString("zip"));
			iPhoneClientInfo.setAddress(rs.getString("address"));
			iPhoneClientInfo.setClientLocationGoogleMap(rs.getString("client_location_google_map")==null?"":rs.getString("client_location_google_map"));
			return iPhoneClientInfo;
		};
	}
	
	@Override
	public List<IPhoneClientInfo> getClientListBySearchKey(String searchKey) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select c.client_code, c.client_location_google_map, c.business_type,c.city,c.state,c.zip,c.address, c.client_name, IF(c.contact_phone IS NULL OR c.contact_phone = '','',CONCAT(LEFT(c.contact_phone,3),'-',MID(c.contact_phone,4,3),'-',RIGHT(c.contact_phone, 4))) as phone");
		sql.append(" from client c");
		sql.append(" where upper(c.client_name) like upper(:searchKey) and c.delete_flag = 'N' and c.active = 'Y' and c.business_type = 'mobile' ");
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("searchKey", "%" + searchKey + "%");
		return namedParameterJdbcTemplate.query(sql.toString(), paramSource, iphoneRowMapper());
	}
	
	@Override
	public List<IPhoneClientInfo> getClientListByClientCode(String clientCode) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select c.client_code,c.client_location_google_map,c.business_type,c.city,c.state,c.zip,c.address, c.client_name, IF(c.contact_phone IS NULL OR c.contact_phone = '','',CONCAT(LEFT(c.contact_phone,3),'-',MID(c.contact_phone,4,3),'-',RIGHT(c.contact_phone, 4))) as phone");
		sql.append(" from client c");
		sql.append(" where upper(c.client_code) like upper(:clientCode) and c.delete_flag = 'N' and c.active = 'Y' and c.business_type = 'mobile' ");
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("clientCode", clientCode);
		return namedParameterJdbcTemplate.query(sql.toString(), paramSource, iphoneRowMapper());
	}
	
	@Override
	public List<IPhoneClientInfo> getClientListByMobileCode(String mobileCode) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select c.client_code,c.client_location_google_map,c.business_type,c.city,c.state,c.zip,c.address, c.client_name, IF(c.contact_phone IS NULL OR c.contact_phone = '','',CONCAT(LEFT(c.contact_phone,3),'-',MID(c.contact_phone,4,3),'-',RIGHT(c.contact_phone, 4))) as phone");
		sql.append(" from client c");
		sql.append(" where upper(c.mobile_code) like upper(:mobileCode) and c.delete_flag = 'N' and c.active = 'Y' and c.business_type = 'mobile' ");
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("mobileCode", mobileCode);
		return namedParameterJdbcTemplate.query(sql.toString(), paramSource, iphoneRowMapper());
	}
	
	@Override
    public boolean logRequest(String device, String uuid, String searchTerm) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append("insert into request_log (uuid, mobile_search_term, timestamp, device) values (:uuid, :mobilesearchterm, now(), :device) ");
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("uuid", uuid);
        paramSource.addValue("mobilesearchterm", searchTerm);
        paramSource.addValue("device", device);
        return namedParameterJdbcTemplate.update(sql.toString(), paramSource) != 0;
    }
	
	@Override 
	public MobileAppPage getMobileAppPages(String pageName) throws Exception {
	   String sql = "select * from mobile_app_pages where page_name=?";
	   return masterJdbcTemplate.query(sql, new Object[]{pageName}, new ResultSetExtractor<MobileAppPage>() {
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
	public List<ClientInfo> getMyProvidersList(String uuid) throws Exception {
		String sql ="select c.client_name,mpl.client_code from my_providers_list mpl, client c where mpl.uuid = :uuid and mpl.client_code = c.client_code order by mpl.id desc";
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("uuid", uuid);
		return namedParameterJdbcTemplate.query(sql.toString(), paramSource , clientInfoRowMapper());
	}
	
	private RowMapper<ClientInfo> clientInfoRowMapper() {
		return (rs, rowNum) -> {
			ClientInfo clientInfo = new ClientInfo();
			clientInfo.setClientCode(rs.getString("client_code"));
			clientInfo.setClientName(rs.getString("client_name"));
			return clientInfo;
		};
	}
	
	@Override
	public List<ProvidersLocResSer> getMyProvidersLocResSerList(String uuid) throws Exception {
		String sql ="select mpl.company_id, mpl.procedure_id, mpl.location_id, mpl.department_id, mpl.resource_id, mpl.service_id from my_providers_list mpl, client c where mpl.uuid = :uuid and mpl.client_code = c.client_code order by mpl.id desc limit 1";
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("uuid", uuid);
		return namedParameterJdbcTemplate.query(sql.toString(), paramSource , locResSerRowMapper());
	}
	
	private RowMapper<ProvidersLocResSer> locResSerRowMapper() {
		return (rs, rowNum) -> {
			ProvidersLocResSer providerLocResSer = new ProvidersLocResSer();
			providerLocResSer.setCompanyId(rs.getLong("company_id"));
			providerLocResSer.setProcedureId(rs.getLong("procedure_id"));
			providerLocResSer.setLocationId(rs.getLong("location_id"));
			providerLocResSer.setDepartmentId(rs.getLong("department_id"));
			providerLocResSer.setResourceId(rs.getLong("resource_id"));
			providerLocResSer.setServiceId(rs.getLong("service_id"));
			return providerLocResSer;
		};
	}

	@Override
	public Long addMyProvidersList(String clientCode, String device, String uuid) throws Exception {
		StringBuilder sql = new StringBuilder();
        sql.append("insert into my_providers_list (client_code,uuid) values (:client_code,:uuid) ");
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("uuid", uuid);
        paramSource.addValue("client_code", clientCode);
        KeyHolder holder = new GeneratedKeyHolder();
        return (long) namedParameterJdbcTemplate.update(sql.toString(), paramSource, holder);
	}
	
	@Override
	public boolean deleteMyProvidersList(String clientCode, String device, String uuid) throws Exception {
		StringBuilder sql = new StringBuilder();
        sql.append("delete from my_providers_list where client_code = :client_code and uuid = :uuid");
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("uuid", uuid);
        paramSource.addValue("client_code", clientCode);
        return namedParameterJdbcTemplate.update(sql.toString(), paramSource) != 0;
	}

	@Override
	public boolean getProviderExist(String clientCode, String uuid) throws Exception {
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("uuid", uuid);
        paramSource.addValue("clientCode", clientCode);
		return namedParameterJdbcTemplate.queryForObject("select count(*) from my_providers_list where client_code = :clientCode and uuid = :uuid",paramSource, Integer.class) !=0;
	}

	@Override
	public String getClientCodeByUuid(String uuid) throws Exception {
		String sql = "select client_code from my_providers_list where uuid=:uuid ";
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("uuid", uuid);
		List<String> clientCodeList = namedParameterJdbcTemplate.query(sql.toString(), paramSource , clientCodeInfoRowMapper());
		if(clientCodeList!=null && !clientCodeList.isEmpty()) return clientCodeList.get(0);
		return null;
	}
	
	private RowMapper<String> clientCodeInfoRowMapper() {
		return (rs, rowNum) -> {
			return rs.getString("client_code");
		};
	}

	@Override
	public void updateLocResSer(Logger logger, String clientCode, String uuid, Long locationId, Long resourceId, Long serviceId) throws Exception {
		String sql ="update my_providers_list set location_id=?,resource_id=?,service_id=? where uuid = ? and client_code = ?";
		masterJdbcTemplate.update(sql, new Object[]{locationId, resourceId, serviceId, uuid, clientCode});
	}

	@Override
	public void updateCustomerIdInMyProviderList(String clientCode, String uuid, Long customerId) throws Exception {
		String sql ="update my_providers_list set customer_id=? where uuid = ? and client_code = ?";
		masterJdbcTemplate.update(sql, new Object[]{customerId, uuid, clientCode});
	}

	@Override
	public List<Long> getCustomerIdFromMyProvidersByUuid(Logger logger, String uuid) throws Exception {
		String sql = "select customer_id from my_providers_list where uuid=? and customer_id IS NOT NULL order by id desc limit 1";
		List<Long> list = masterJdbcTemplate.query(sql, new Object[]{uuid},(rs, num) -> {return rs.getLong("customer_id");});
		return list;
	}

	@Override
	public List<ClientCustomerInfo> getClientInfoFromMyProviders(Logger logger, String uuid) throws Exception {
		String sql ="select mpl.client_code,c.client_name,mpl.customer_id from my_providers_list mpl, client c where mpl.uuid = :uuid and mpl.client_code = c.client_code order by mpl.id desc";
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("uuid", uuid);
		return namedParameterJdbcTemplate.query(sql.toString(), paramSource , clientInfoMyProviderRowMapper());
	}
	
	private RowMapper<ClientCustomerInfo> clientInfoMyProviderRowMapper() {
		return (rs, rowNum) -> {
			ClientCustomerInfo clientCustomerInfo = new ClientCustomerInfo();
			clientCustomerInfo.setClientCode(rs.getString("client_code"));
			clientCustomerInfo.setCustomerId(rs.getLong("customer_id"));
			clientCustomerInfo.setClientName(rs.getString("client_name"));
			return clientCustomerInfo;
		};
	}
}
