package com.telappoint.apptdesk.common.externallogin;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.CollectionType;
import org.codehaus.jackson.map.type.TypeFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * @author Balaji N
 * 
 */
public class ExternalLoginRestClient {

	public static Login login(Logger logger, String url, String payload) {
		HttpHeaders requestHeaders = createHttpHeader("*/*", "UTF-8", "application/json");
		HttpEntity<String> requestEntity = new HttpEntity<String>(payload, requestHeaders);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> responseJSON = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
	
		Gson gson = new GsonBuilder().create();
		Login loginResponse = gson.fromJson(responseJSON.getBody(), new TypeToken<Login>() {
			private static final long serialVersionUID = 1L;
		}.getType());
		return loginResponse;
	}
	
	
	public static List<Participant> getParticipant(Logger logger, String url, String token) throws IOException {
		System.out.println("url:"+url);
		HttpHeaders requestHeaders = createHttpHeader("*/*", "UTF-8", "application/json");
		requestHeaders.set("Authorization","Bearer "+token);
		HttpEntity<String> requestEntity = new HttpEntity<String>(requestHeaders);
		RestTemplate restTemplate = new RestTemplate();
		long startTime = System.currentTimeMillis();
		ResponseEntity<String> responseJSON = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
		long endTime = System.currentTimeMillis();
		System.out.println("Time Taken for getParticipant: "+(endTime-startTime)+" ms");
		logger.info("Time Taken for getParticipant: "+(endTime-startTime)+" ms");
		String response = responseJSON.getBody();
		System.out.println("getParticipant response from backend: "+response);
		List<Participant> participantResponse=null;
		if(response != null) {
			StringBuilder body = new StringBuilder(response);
			if(!"".equals(body.toString())) {
				ObjectMapper mapper = new ObjectMapper();
				try {
					TypeFactory typeFactory = mapper.getTypeFactory();
			        CollectionType collectionType = typeFactory.constructCollectionType(
			                                            List.class, Participant.class);
					participantResponse = mapper.readValue(body.toString(), collectionType);
				} catch (JsonGenerationException | JsonParseException  e) {
					logger.error("Error: "+e,e);
				} 
			}
		}
		
		return participantResponse;
	}
	
	public static Eligibility getEligibilityCheck(Logger logger, String url,  String token, long participantId, int programId, long programInstanceId, long houseHoldId, String date) throws IOException {
		HttpHeaders requestHeaders = createHttpHeader("*/*", "UTF-8", "application/json");
		requestHeaders.set("Authorization","Bearer "+token);
		HttpEntity<String> requestEntity = new HttpEntity<String>(requestHeaders);
		RestTemplate restTemplate = new RestTemplate();
		
		StringBuilder backendURL = new StringBuilder();
		backendURL.append(url).append("?ParticipantID=").append(participantId).append("&ProgramID=").append(programId);
		backendURL.append("&ProgramInstanceID=").append(programInstanceId).append("&HouseholdID=").append(houseHoldId);
		backendURL.append("&EnrollmentStartDate=").append(date);
		backendURL.append("&EnrollmentEndDate=").append(date);
		
		System.out.println("backendURL:"+backendURL.toString());
		Eligibility eligibility=null;
		try {
			long startTime = System.currentTimeMillis();
			ResponseEntity<String> responseJSON = restTemplate.exchange(backendURL.toString(), HttpMethod.POST, requestEntity, String.class);
			long endTime = System.currentTimeMillis();
			logger.info("Time Taken for EligiblilityCheck: "+(endTime-startTime)+" ms");
			System.out.println("Time Taken for EligiblilityCheck: "+(endTime-startTime)+" ms");
			String response = responseJSON.getBody();
			System.out.println("getParticipant response from backend: "+response);
			
			if(response != null) {
				if(!"".equals(response)) {
					ObjectMapper mapper = new ObjectMapper();
					try {
				        eligibility = mapper.readValue(response.toString(), Eligibility.class);
					} catch (JsonGenerationException | JsonParseException  e) {
						logger.error("Error: "+e,e);
					} 
				}
			}
		} catch(Exception e) {
			logger.error("Error while getEligibilityCheck:"+e,e);
		}
		return eligibility;
	}
	
	public static EnrollementResponse createEnrollment(Logger logger, String url,  String token, String payload) throws IOException {
		HttpHeaders requestHeaders = createHttpHeader("*/*", "UTF-8", "application/json");
		requestHeaders.set("Authorization","Bearer "+token);
		System.out.println(token);
		HttpEntity<String> requestEntity = new HttpEntity<String>(payload, requestHeaders);
		RestTemplate restTemplate = new RestTemplate();
		long startTime = System.currentTimeMillis();
		ResponseEntity<String> responseJSON = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
		long endTime = System.currentTimeMillis();
		System.out.println("Time Taken for createEnrollment: "+(endTime-startTime)+" ms");
		logger.info("Time Taken for createEnrollment: "+(endTime-startTime)+" ms");
		EnrollementResponse enrollmentRes=null;
		if(responseJSON != null) {
			if(!"".equals(responseJSON.toString())) {
				ObjectMapper mapper = new ObjectMapper();
				StringBuilder response = new StringBuilder(responseJSON.getBody());
				try {
					enrollmentRes = mapper.readValue(response.toString(), EnrollementResponse.class);
				} catch (JsonGenerationException | JsonParseException  e) {
					logger.error("Error: "+e,e);
				} 
			}
		}
		return enrollmentRes;
	}
	
	public static boolean cancelEnrollment(Logger logger, String url,  String token) throws IOException {
		HttpHeaders requestHeaders = createHttpHeader("*/*", "UTF-8", "application/json");
		requestHeaders.set("Authorization","Bearer "+token);
		HttpEntity<String> requestEntity = new HttpEntity<String>("{}",requestHeaders);
		RestTemplate restTemplate = new RestTemplate();
		long startTime = System.currentTimeMillis();
		ResponseEntity<String> responseJSON = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
		System.out.println(responseJSON);
		logger.info("cancelEnrollment Response: "+responseJSON);
		long endTime = System.currentTimeMillis();
		System.out.println("Time Taken for cancelEnrollment: "+(endTime-startTime)+" ms");
		logger.info("Time Taken for cancelEnrollment: "+(endTime-startTime)+" ms");
		// get Enrollment ID;
		return true;
	}
	
	public static void main(String[] args) throws Exception {
		String jsonData = "{\"consumerId\": 2,\"secret\": \"c8e2a3e1-b35c-430e-8241-a57745578868\"}";
		String url = "http://devapi.empoworbycsst.com/api/v1/Logins/Login";

		Login login = login(null, url, jsonData);
		String token = login.getToken();
		System.out.println(token);
//		url = "http://devapi.empoworbycsst.com/api/v1/Participants/GetParticipants/Abbatangelo/5410/03-17-1963";
//		System.out.println(getParticipant(null, url, token).toString());
//		
//		url="http://devapi.empoworbycsst.com/api/v1/Enrollments/EligibilityCheck";
//		System.out.println(getEligibilityCheck(null,  url, token, 7690, 19, 39, 3268, "12/12/2017"));
//		
//		
//		url="http://devapi.empoworbycsst.com/api/v1/Enrollments/CreateEnrollment";
//		String payload="{\"participantId\": 5559,\"programId\": 25,\"programInstanceId\": 46,\"householdId\": 2395,\"appointmentDate\": \"2016-09-09\",\"firstName\": \"Judith\",\"lastName\": \"Abbatangelo\",\"ssn\": \"5410\",\"phoneNumber\": \"8569999999\",\"note\": \"test\",\"transaction\": \"12345\",prospectiveClientID\": \"0\",\"dob\": \"2063-03-17\"}";
//		System.out.println(createEnrollment(null, url, token, payload));
		
		
		url="http://devapi.empoworbycsst.com/api/v1/Enrollments/CancelEnrollment/3835";
		
		System.out.println(cancelEnrollment(null, url, token));
		
	}

	/**
	 * create HTTP headers.
	 * 
	 * @return
	 */
	private static HttpHeaders createHttpHeader(String acceptMediaType, String acceptCharset, String contentType) {
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.set("Accept", acceptMediaType);
		requestHeaders.set("Accept-Charset", acceptCharset);
		requestHeaders.set("Content-Type", contentType);
		return requestHeaders;
	}
}
