package com.telappoint.apptdesk.common.utils;



import org.apache.log4j.Logger;

import com.telappoint.apptdesk.common.components.ConnectionPoolUtil;
import com.telappoint.apptdesk.common.model.Client;
public class ThreadPool implements Runnable {
	
	private ConnectionPoolUtil pool;
	private Logger logger;
	private Client client;

	@Override
	public void run() {
		
		try {
			pool.getJdbcCustomTemplate(logger, client).getJdbcTemplate().queryForInt("select 1 from dual");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ConnectionPoolUtil getPool() {
		return pool;
	}

	public void setPool(ConnectionPoolUtil pool) {
		this.pool = pool;
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	
	
}
