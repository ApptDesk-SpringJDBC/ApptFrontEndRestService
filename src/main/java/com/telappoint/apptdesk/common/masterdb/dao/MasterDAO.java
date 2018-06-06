package com.telappoint.apptdesk.common.masterdb.dao;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.telappoint.apptdesk.common.masterdb.dao.impl.ProvidersLocResSer;
import com.telappoint.apptdesk.common.model.Client;
import com.telappoint.apptdesk.common.model.ClientCustomerInfo;
import com.telappoint.apptdesk.common.model.ClientInfo;
import com.telappoint.apptdesk.common.model.IPhoneClientInfo;
import com.telappoint.apptdesk.common.model.MobileAppPage;
import com.telappoint.apptdesk.handlers.exception.TelAppointException;

public interface MasterDAO {
	public  void getClients(final String key, final Map<String, Client> clientCacheMap) throws TelAppointException, Exception;
	public void getClientDeploymentConfig(final String key, String clientCode, int clientId,final Map<String, Object> cacheMap) throws TelAppointException, Exception;
	public void getPageValidationMessages(String device, final Map<String, String> validationMsgMap) throws TelAppointException, Exception;
	public List<String> getClientDirectAccessNumbers() throws TelAppointException, Exception;
	List<IPhoneClientInfo> getMobileDemoClientList() throws Exception;
	List<IPhoneClientInfo> getClientListBySearchKey(String searchKey) throws Exception;
	List<IPhoneClientInfo> getClientListByClientCode(String clientCode) throws Exception;
	public List<IPhoneClientInfo> getClientListByMobileCode(String mobileCode) throws Exception;
	boolean logRequest(String device, String uuid, String searchTerm) throws Exception;
	MobileAppPage getMobileAppPages(String pageName) throws Exception;
	List<ClientInfo> getMyProvidersList(String uuid) throws Exception;
	public Long addMyProvidersList(String clientCode, String device, String uuid) throws Exception;
	boolean deleteMyProvidersList(String clientCode, String device, String uuid) throws Exception;
	boolean getProviderExist(String clientCode, String uuid) throws Exception;
	public String getClientCodeByUuid(String uuid) throws Exception;
	List<ProvidersLocResSer> getMyProvidersLocResSerList(String uuid) throws Exception;
	public void updateLocResSer(Logger logger, String clientCode, String uuid, Long locationId, Long resourceId, Long serviceId) throws Exception;
	public void updateCustomerIdInMyProviderList(String clientCode, String uuid, Long customerId) throws Exception;
	public List<Long> getCustomerIdFromMyProvidersByUuid(Logger logger, String uuid) throws Exception;
	public List<ClientCustomerInfo> getClientInfoFromMyProviders(Logger logger, String uuid) throws Exception;
}
