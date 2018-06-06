package com.telappoint.apptdesk.model;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

@JsonSerialize(include = Inclusion.NON_NULL)
public class LoginPageFields {
	private int fieldId;
	private String pageName;
	private String fieldName;
	private String loginType;
	private String paramType;
	private String paramTable;
	private String paramColumn;
	private String paramColumnForQuery;
	private String displayType;
	private String display;
	private String emptyErrorMsg;
	private String invalidErrorMsg;
	private String required;
	private String validateRequired;
	private String validationRules;
	private String validationRuleAPI;
	private String validateMaxChars;
	private String validateMinChars;
	private String initialValues;
	private String javaRef;
	private String storageType;
	private int storageSize;
	private String listLabels;
	private String listValues;
	private String listInitialValues;
	private String encrypt;
	
	
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
	
	public String getEmptyErrorMsg() {
		return emptyErrorMsg;
	}
	public void setEmptyErrorMsg(String emptyErrorMsg) {
		this.emptyErrorMsg = emptyErrorMsg;
	}
	public String getInvalidErrorMsg() {
		return invalidErrorMsg;
	}
	public void setInvalidErrorMsg(String invalidErrorMsg) {
		this.invalidErrorMsg = invalidErrorMsg;
	}
	public String getRequired() {
		return required;
	}
	public void setRequired(String required) {
		this.required = required;
	}
	public String getValidateRequired() {
		return validateRequired;
	}
	public void setValidateRequired(String validateRequired) {
		this.validateRequired = validateRequired;
	}
	public String getValidationRules() {
		return validationRules;
	}
	public void setValidationRules(String validationRules) {
		this.validationRules = validationRules;
	}
	public String getValidationRuleAPI() {
		return validationRuleAPI;
	}
	public void setValidationRuleAPI(String validationRuleAPI) {
		this.validationRuleAPI = validationRuleAPI;
	}
	public String getValidateMaxChars() {
		return validateMaxChars;
	}
	public void setValidateMaxChars(String validateMaxChars) {
		this.validateMaxChars = validateMaxChars;
	}
	public String getValidateMinChars() {
		return validateMinChars;
	}
	public void setValidateMinChars(String validateMinChars) {
		this.validateMinChars = validateMinChars;
	}
	public String getInitialValues() {
		return initialValues;
	}
	public void setInitialValues(String initialValues) {
		this.initialValues = initialValues;
	}
	public String getDisplayType() {
		return displayType;
	}
	public void setDisplayType(String displayType) {
		this.displayType = displayType;
	}
	public String getDisplay() {
		return display;
	}
	public void setDisplay(String display) {
		this.display = display;
	}
	public String getParamColumnForQuery() {
		return paramColumnForQuery;
	}
	public void setParamColumnForQuery(String paramColumnForQuery) {
		this.paramColumnForQuery = paramColumnForQuery;
	}
	public String getListLabels() {
		return listLabels;
	}
	public void setListLabels(String listLabels) {
		this.listLabels = listLabels;
	}
	public String getListValues() {
		return listValues;
	}
	public void setListValues(String listValues) {
		this.listValues = listValues;
	}
	public String getListInitialValues() {
		return listInitialValues;
	}
	public void setListInitialValues(String listInitialValues) {
		this.listInitialValues = listInitialValues;
	}
}
