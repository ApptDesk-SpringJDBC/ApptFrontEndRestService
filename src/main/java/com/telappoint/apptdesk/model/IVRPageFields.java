package com.telappoint.apptdesk.model;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

@JsonSerialize(include = Inclusion.NON_NULL)
public class IVRPageFields {
	private int fieldId;
	private String pageName;
	private String fieldName;
	private String loginType;
	private String paramType;
	private String paramTable;
	private String paramColumn;
	private String javaRef;
	private String storageType;
	private int storageSize;
	private String encrypt;
	private int ivrMinDigits;
	private int ivrMaxDigits;
	private String ivrLoginParamAudio;
	private String ivrLoginParamTts;
	
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	
	public int getFieldId() {
		return fieldId;
	}
	public void setFieldId(int fieldId) {
		this.fieldId = fieldId;
	}
	public String getParamTable() {
		return paramTable;
	}
	public void setParamTable(String paramTable) {
		this.paramTable = paramTable;
	}
	public String getParamColumn() {
		return paramColumn;
	}
	public void setParamColumn(String paramColumn) {
		this.paramColumn = paramColumn;
	}
	public String getLoginType() {
		return loginType;
	}
	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}
	public String getStorageType() {
		return storageType;
	}
	public void setStorageType(String storageType) {
		this.storageType = storageType;
	}
	public int getStorageSize() {
		return storageSize;
	}
	public void setStorageSize(int storageSize) {
		this.storageSize = storageSize;
	}
	public String getParamType() {
		return paramType;
	}
	public void setParamType(String paramType) {
		this.paramType = paramType;
	}
	public String getEncrypt() {
		return encrypt;
	}
	public void setEncrypt(String encrypt) {
		this.encrypt = encrypt;
	}
	public String getPageName() {
		return pageName;
	}
	public void setPageName(String pageName) {
		this.pageName = pageName;
	}
	public String getJavaRef() {
		return javaRef;
	}
	public void setJavaRef(String javaRef) {
		this.javaRef = javaRef;
	}
	public int getIvrMinDigits() {
		return ivrMinDigits;
	}
	public void setIvrMinDigits(int ivrMinDigits) {
		this.ivrMinDigits = ivrMinDigits;
	}
	public int getIvrMaxDigits() {
		return ivrMaxDigits;
	}
	public void setIvrMaxDigits(int ivrMaxDigits) {
		this.ivrMaxDigits = ivrMaxDigits;
	}
	public String getIvrLoginParamAudio() {
		return ivrLoginParamAudio;
	}
	public void setIvrLoginParamAudio(String ivrLoginParamAudio) {
		this.ivrLoginParamAudio = ivrLoginParamAudio;
	}
	public String getIvrLoginParamTts() {
		return ivrLoginParamTts;
	}
	public void setIvrLoginParamTts(String ivrLoginParamTts) {
		this.ivrLoginParamTts = ivrLoginParamTts;
	}
}
