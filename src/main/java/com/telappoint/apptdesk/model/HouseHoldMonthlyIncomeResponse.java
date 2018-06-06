package com.telappoint.apptdesk.model;

import com.telappoint.apptdesk.common.model.BaseResponse;

public class HouseHoldMonthlyIncomeResponse extends BaseResponse {
	private String monthlyIncome;
	private String monthlyIncomeTts;
	private String monthlyIncomeAudio;

	public String getMonthlyIncome() {
		return monthlyIncome;
	}

	public void setMonthlyIncome(String monthlyIncome) {
		this.monthlyIncome = monthlyIncome;
	}

	public String getMonthlyIncomeTts() {
		return monthlyIncomeTts;
	}

	public void setMonthlyIncomeTts(String monthlyIncomeTts) {
		this.monthlyIncomeTts = monthlyIncomeTts;
	}

	public String getMonthlyIncomeAudio() {
		return monthlyIncomeAudio;
	}

	public void setMonthlyIncomeAudio(String monthlyIncomeAudio) {
		this.monthlyIncomeAudio = monthlyIncomeAudio;
	}
}
