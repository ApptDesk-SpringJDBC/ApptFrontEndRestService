package com.telappoint.apptdesk.common.constants;

public enum AppointmentMethod {
		ONLINE(1,"Online"), 
		IVR(2,"IVR"), 
		ADMIN(3,"Admin"), 
		MOBILE(4,"Mobile"), 
		ANDROID(5,"Android"), 
		IPAD(6,"IPAD"), 
		SMS(7,"SMS");
		
		private int method;
		private String description;
		
		private AppointmentMethod(int method,String description) {
			this.method = method;
			this.description = description;
		}

		/**
		 * @return the method
		 */
		public int getMethod() {
			return method;
		}

		/**
		 * @param method the method to set
		 */
		public void setMethod(int method) {
			this.method = method;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}
		
			
		public static class AppointmentMethodDescription {
			public static String getAppointmentMethodDescription(int method) {
				AppointmentMethod[] keys = AppointmentMethod.values();
				int _method = 0;
				for (AppointmentMethod key : keys) {
					_method = key.getMethod();
					if (_method == method) {
						return key.getDescription();
					}
				}
				return "";
			}
		}
		
		public static void main(String[] args) {
			String result = AppointmentMethodDescription.getAppointmentMethodDescription(1);
			System.out.println(result);
		}
}
