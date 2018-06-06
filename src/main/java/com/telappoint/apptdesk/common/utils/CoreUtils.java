package com.telappoint.apptdesk.common.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;

import com.telappoint.apptdesk.common.constants.CommonApptDeskConstants;
import com.telappoint.apptdesk.common.constants.CommonDateContants;
import com.telappoint.apptdesk.common.constants.PropertiesConstants;

/**
 * 
 * @author Balaji Nandarapu
 *
 */
public class CoreUtils {
	private static DateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	
	private static final ThreadLocal<DateFormat> tldfyyyyMMddHHmm = new ThreadLocal<DateFormat>() {
		@Override
		protected DateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm");
		}
	};
	//tldfyyyyMMddHHmm.get().parse(source);
	
	public static DateFormat getSimpleDateFormatYYYYMMDDHHMM() {
		return tldfyyyyMMddHHmm.get();
	}
	//tldfyyyyMMddHHmm.get().parse(source);
	
	public static String replaceAllPlaceHolders(Logger logger, String emailMessage, Map<String, String> emailData) {
		if (emailData != null) {
			Set<String> keySet = emailData.keySet();
			Iterator<String> it = keySet.iterator();
			while (it.hasNext()) {
				String key = it.next();
				String value = emailData.get(key);
				emailMessage = emailMessage.replace(key, value == null ? "" : value);
			}
		} else {
			logger.error("EmailPlaceHolder data is empty.!");
		}
		return emailMessage;
	}
	
	public static boolean allowOnlyAlphanumeric(String clientCode) {
		clientCode = clientCode.replaceAll("/", "");
		String regex = "^[a-zA-Z0-9]+$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(clientCode);
		return matcher.matches();
	}
	
	public static void main(String[] args) throws Exception {
		List<String> a = new ArrayList<String>();
		a.add("Blaji");
		a.add("VVV");
		a.add("ddddd");
		a.add(0,"Balaji");
		
		System.out.println(a.toString());
		
	}
	
	private static String intToStringDuration(int aDuration) {
	    String result = "";

	    int hours = 0, minutes = 0, seconds = 0;

	    hours = aDuration / 3600;
	    minutes = (aDuration - hours * 3600) / 60;
	    seconds = (aDuration - (hours * 3600 + minutes * 60));

	    result = String.format("%02d:%02d:%02d", hours, minutes, seconds);
	    return result;
	}
	
	public static String getToken(String clientCode, String device) {
		StringBuilder sb = new StringBuilder();
		sb.append(clientCode);
		sb.append(device);
		sb.append(formatter.format(new Date()));
		sb.append(GenerateRandomToken.getRandomToken(6, "N"));
		return UUID.nameUUIDFromBytes(sb.toString().getBytes()).toString();
	}
	
	public static boolean isValidEmailAddress(String email) {
		if (email == null || "".equals(email))
			return false;
		java.util.regex.Pattern p = java.util.regex.Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-']+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
		java.util.regex.Matcher m = p.matcher(email);
		return m.matches();
	}

	public static Object getPropertyValue(Object object, String fieldName) throws NoSuchFieldException {
		try {
			BeanInfo info = Introspector.getBeanInfo(object.getClass());
			for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
				if (pd.getName().equals(fieldName)) {
					Method getter = pd.getReadMethod();
					if (getter != null) {
						getter.setAccessible(true);
						return getter.invoke(object, null);
					}

				}
			}
		} catch (Exception e) {
			throw new NoSuchFieldException(object.getClass() + " has no field " + fieldName);
		}
		return "";
	}
	
	/**
	 * Used to split the message to multiple parts because of message length is
	 * exceeded 160 characters.
	 */
	public static List<String> splitMessage(String message) {
		ArrayList<String> messages = new ArrayList<String>();
		char[] sAr = message.toCharArray();
		int start = 0;
		for (int i = 160; i < sAr.length; i--) {
			if (sAr[i] == ' ') {
				messages.add(message.substring(start, i));
				start = i + 1;
				i += 160;
			}
		}
		messages.add(message.substring(start));
		return messages;
	}

	public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
		for (Entry<T, E> entry : map.entrySet()) {
			if (value.equals(entry.getValue())) {
				return entry.getKey();
			}
		}
		return null;
	}

	public static String removeNonDigits(final String str) {
		if (str == null || str.length() == 0) {
			return "";
		}
		return str.replaceAll("\\D+", "");
	}
	
	public static String removeErrorNumber(String stMethodName) {
		int lastIndex = stMethodName.lastIndexOf(":");
		return stMethodName.substring(0, lastIndex);
	}
	
	public static String getStackTrace(Throwable t) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		t.printStackTrace(pw);
		String stackTrace = sw.toString();
		return stackTrace.replace(System.getProperty("line.separator"), "<br/>\n");
	}
	
	public static String getMethodAndClassName(final Throwable cause) {
	     Throwable rootCause = cause;
	     while(rootCause.getCause() != null &&  rootCause.getCause() != rootCause) {
	          rootCause = rootCause.getCause();
	     }

	    StringBuilder sb = new StringBuilder();
	    sb.append("<br>").append("ClassName: ").append(rootCause.getStackTrace()[0].getClassName()); 
	    sb.append("<br>").append("MethodName: ").append(rootCause.getStackTrace()[0].getMethodName()); 
	    return sb.toString();
	}

	
	public static boolean isStringEqual(String str1, String str2) {
		if (str1 == null || str2 == null)
			return false;
		
		str1 = str1.trim();
		str2 = str2.trim();
		if (str1.length() == 0 || str2.length() == 0) {
			return false;
		}
		if (str1.equals(str2)) {
			return true;
		}
		return false;
	}

	public static String getNotifyStartDate(int hours, String timeZone) {
		GregorianCalendar cal = new GregorianCalendar(TimeZone.getTimeZone(timeZone));
		cal.add(Calendar.HOUR_OF_DAY, hours);
		ThreadLocal<DateFormat> df = DateUtils.getSimpleDateFormat(CommonDateContants.DATETIME_FORMAT_YYYYMMDDHHMMSS_CAP.getValue());
		df.get().setTimeZone(java.util.TimeZone.getTimeZone(timeZone));
		return df.get().format(cal.getTime());
	}

	public static void setPropertyValue(Object object, String propertyName, Object propertyValue) throws IllegalAccessException, InvocationTargetException, IllegalArgumentException, IntrospectionException {
			BeanInfo bi = Introspector.getBeanInfo(object.getClass());
			PropertyDescriptor pds[] = bi.getPropertyDescriptors();
			for (PropertyDescriptor pd : pds) {
				if (pd.getName().equals(propertyName)) {
					Method setter = pd.getWriteMethod();
					if (setter != null) {
						setter.invoke(object, new Object[] { propertyValue });
					}
				}
			}
		
	}

	public static Object getInitCaseValue(Object value) {
		String name = (String) value;
		StringBuilder nameBuilder = new StringBuilder();
		String[] nameStrs = name.split("\\s+");
		if (nameStrs != null && nameStrs.length > 0) {
			for (String nameStr : nameStrs) {
				if (nameStr != null && !" ".equals(nameStr) && nameStr.length() > 0) {
					nameBuilder.append(nameStr.substring(0, 1) != null ? nameStr.substring(0, 1).toUpperCase() : "");
					nameBuilder.append(nameStr.substring(1));
					nameBuilder.append(" ");
				}
			}
		}
		if (nameBuilder.toString() != null && !"".equals(nameBuilder.toString().trim())) {
			value = nameBuilder.toString().trim();
		}
		return value;
	}

	public static String capitalizeString(String string) {
		char[] chars = string.toLowerCase().toCharArray();
		StringBuilder result = new StringBuilder();
		boolean found = false;
		for (int i = 0; i < chars.length; i++) {
			if (!found && Character.isLetter(chars[i])) {
				result.append(Character.toUpperCase(chars[i]));
				found = true;
			} else if ("'".equals(String.valueOf(chars[i]))) {
				result.append("");
				found = false;
			} else if (Character.isWhitespace(chars[i]) || chars[i] == '-') {
				result.append(chars[i]);
				found = false;
			} else if (Character.isLetter(chars[i])) {
				result.append(chars[i]);
			}
		}
		return result.toString();
	}
	
	public static Date addMinsToCurrentTime(int mins) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, mins);
		return calendar.getTime();
	}

	public static String removeDigitsAndNonAlpha(final String str) {
		if (str == null || str.length() == 0) {
			return "";
		}
		return str.replaceAll("[^A-Za-z]", "");
	}

	private static final String IPADDRESS_PATTERN = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
			+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

	public static boolean validateIP(final String ip) {
		Pattern pattern = Pattern.compile(IPADDRESS_PATTERN);
		Matcher matcher = pattern.matcher(ip);
		return matcher.matches();
	}

	public static boolean isOnline(String device) {
		return CommonApptDeskConstants.ONLINE.getValue().equals(device) ? true : false;
	}


	public static boolean isMobile(String device) {
		return CommonApptDeskConstants.MOBILE.getValue().equals(device) ? true : false;
	}

	public static boolean isIVR(String device) {
		return CommonApptDeskConstants.IVRAUDIO.getValue().equals(device) ? true : false;
	}
	
	public static boolean isAdmin(String device) {
		return CommonApptDeskConstants.ADMIN.getValue().equals(device) ? true : false;
	}
	
	public static boolean isValidPassword(HttpHeaders requestHeader) throws Exception {
		List<String> passwordList = requestHeader.get("password");
		if(passwordList != null && !passwordList.isEmpty()) {
		  return getSystemPassword(PropertyUtils.getValueFromProperties("IPHONE_API_PASSWORD", PropertiesConstants.APPT_SERVICE_REST_WS_PROP.getPropertyFileName())).equals(passwordList.get(0));
		}
		return true;
	}
	
	public static String getSystemPassword(String systemMobilePassword) throws Exception {
		return MD5Sum(todaysDate("US/Eastern")+systemMobilePassword);
	}
	
	public static String getToken(String appendedStr) {
		String token = UUID.nameUUIDFromBytes(appendedStr.getBytes()).toString();
		return token;
	}
	
	public static String MD5Sum(String pass) throws Exception {
		MessageDigest m = MessageDigest.getInstance("MD5");
		byte[] data = pass.getBytes();
		m.update(data, 0, data.length);
		BigInteger i = new BigInteger(1, m.digest());
		String output = i.toString(16);
		boolean leadingZero = false;
		while (output.length() < 32){
			leadingZero = true;
			output = "0"+output;
	    }
		if(leadingZero) {
			System.out.println(output);
			System.out.println("date: "+pass+" , md5sum leadingzero password:"+output);
		}
		return output;
	}

	public static String todaysDate(String timeZone) throws Exception {
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		dateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
		Calendar cal = Calendar.getInstance();
		return (dateFormat.format(cal.getTime()));
	}
}
