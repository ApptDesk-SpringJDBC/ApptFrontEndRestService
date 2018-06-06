package com.telappoint.apptdesk.model;

import java.util.HashMap;
import java.util.Map;

public class IVRVxmlData {
	private Map<String, IVRXml> ivrVxmlMap = new HashMap<String,IVRXml>();

	public Map<String, IVRXml> getIvrVxmlMap() {
		return ivrVxmlMap;
	}

	public void setIvrVxmlMap(Map<String, IVRXml> ivrVxmlMap) {
		this.ivrVxmlMap = ivrVxmlMap;
	}
}
