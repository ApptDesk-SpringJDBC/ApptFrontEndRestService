package com.telappoint.apptdesk.model;

import java.util.ArrayList;
import java.util.List;

public class IVRVxmlFlowData {
	private List<IVRFlow> ivrFlows = new ArrayList<IVRFlow>();
	private SchedulerClosedNoFunding schedulerClosedNoFunding;
	private String langCode;
	private String voiceName;
	
	public List<IVRFlow> getIvrFlows() {
		return ivrFlows;
	}
	public void setIvrFlows(List<IVRFlow> ivrFlows) {
		this.ivrFlows = ivrFlows;
	}
	public SchedulerClosedNoFunding getSchedulerClosedNoFunding() {
		return schedulerClosedNoFunding;
	}
	public void setSchedulerClosedNoFunding(SchedulerClosedNoFunding schedulerClosedNoFunding) {
		this.schedulerClosedNoFunding = schedulerClosedNoFunding;
	}
	public String getLangCode() {
		return langCode;
	}
	public void setLangCode(String langCode) {
		this.langCode = langCode;
	}
	public String getVoiceName() {
		return voiceName;
	}
	public void setVoiceName(String voiceName) {
		this.voiceName = voiceName;
	}
}
