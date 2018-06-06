package com.telappoint.apptdesk.model;

import com.telappoint.apptdesk.common.masterdb.dao.impl.ProvidersLocResSer;
import com.telappoint.apptdesk.common.model.BaseResponse;

public class ProvidersLocResSerResponse extends BaseResponse  {
	private ProvidersLocResSer providersLocResSer;

	public ProvidersLocResSer getProvidersLocResSer() {
		return providersLocResSer;
	}

	public void setProvidersLocResSer(ProvidersLocResSer providersLocResSer) {
		this.providersLocResSer = providersLocResSer;
	}
	
}
