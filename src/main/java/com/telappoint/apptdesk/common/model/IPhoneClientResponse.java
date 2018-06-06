package com.telappoint.apptdesk.common.model;

import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import com.telappoint.apptdesk.model.Language;

/**
 * 
 * @author Balaji
 * 
 */
@JsonSerialize(include = Inclusion.NON_NULL)
public class IPhoneClientResponse {
	public String clientCode;
	private List<Language> Languages;

	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}

	public String getClientCode() {
		return clientCode;
	}

	public List<Language> getLanguages() {
		return Languages;
	}

	public void setLanguages(List<Language> languages) {
		Languages = languages;
	}
}
