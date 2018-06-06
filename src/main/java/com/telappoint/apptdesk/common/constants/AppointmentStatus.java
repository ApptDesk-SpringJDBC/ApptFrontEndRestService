package com.telappoint.apptdesk.common.constants;


/**
 * @author Rajin
 * 
 *
 */

public enum AppointmentStatus {
	
	NOT_OPEN(-1,"Not Opened"),
	OPEN(0,"Opened"),
	
	NONE(0,"None"),
	HOLD(1,"Hold"),
	RELEASE_HOLD(2,"Release Hold"), 
	
	// ALL CONFIRM APPT STATUS RESERVERED FROM 11 TO 19
	CONFIRM(11,"Confirm"),
	REMIND_CONFIRM(12,"Remind Cancel"),
	
	// ALL CANCEL APPT STATUS RESERVERED FROM 21 TO 30
	CANCEL(21,"Cancel"),
	REMIND_CANCEL(22,"Remaind Cancel"),
	RESCHEDULE_CANCEL(23,"Reschedule Cancel"),
	DISPLACEMENT_CANCEL(24,"Displacement Cancel"),
	
	// OTHER APPT STATUS
	NO_SHOW(31,"No Show"),
	DISPLACED(32,"Displaced"), 
	OVERBOOKED(33,"OverBooked"),
	RESCHEDULE(34,"Reschedule"),
	MISSING_DOC(35,"Missing documents"),
	
	EMERGENCY_APPT(37,"Emergency appointment"),
	BLOCK_FOR_THIS_SEASON(50,"Block for this season");
	
	private int status;
	private String description;
	
	private AppointmentStatus(int status,String description) {
		this.status = status;
		this.description = description;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public static class AppointmentStatusDescription {
		public static String getAppointmentStatusDescription(int status) {
			AppointmentStatus[] keys = AppointmentStatus.values();
			int _status = 0;
			for (AppointmentStatus key : keys) {
				_status = key.getStatus();
				if (_status == status) {
					return key.getDescription();
				}
			}
			return "";
		}
	}
	
	public static void main(String[] args) {
		String result = AppointmentStatusDescription.getAppointmentStatusDescription(1);
		System.out.println(result);
	}
}
