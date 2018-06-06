package com.telappoint.apptdesk.service.impl;

import com.telappoint.apptdesk.common.constants.ErrorConstants;
import com.telappoint.apptdesk.common.model.TempHoldAppt;
import com.telappoint.apptdesk.common.utils.CoreUtils;
import com.telappoint.apptdesk.handlers.exception.TelAppointException;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Koti on 7/28/2016.
 */
public class ApptServiceImplTest {


    public static void main(String[] args) throws Exception {

        String spResponse = "2016-07-27|2-09:00:00,09:30:00,10:00:00,10:30:00,11:00:00,11:30:00,13:00:00,13:30:00,14:00:00,14:30:00,15:00:00,15:30:00,16:00:00|11-09:00:00,09:30:00,10:00:00,10:30:00,11:00:00,11:30:00,13:00:00,13:30:00,14:00:00,14:30:00,15:00:00,15:30:00,16:00:00|12-09:00:00,09:30:00,10:00:00,10:30:00,11:00:00,11:30:00,13:00:00,13:30:00,14:00:00,14:30:00,15:00:00,15:30:00,16:00:00|13-09:00:00,09:30:00,10:00:00,10:30:00,11:00:00,11:30:00,13:00:00,13:30:00,14:00:00,14:30:00,15:00:00,15:30:00,16:00:00|14-09:00:00,09:30:00,10:00:00,10:30:00,11:00:00,11:30:00,13:00:00,13:30:00,14:00:00,14:30:00,15:00:00,15:30:00,16:00:00";
        if (!"".equals(spResponse.trim())) {
            //Return data as '2015-05-24|1-10:00,10:15,16:00,16:15|2-09:30,11:15'
            // for loop resource Id's
            // inside of for loop for time slots.
            // inside call temp hold stored procedure.

            String[] resArray = spResponse.split("\\|");
            if (resArray.length >= 2) {
                String date = resArray[0];
                for (int i = 1; i < resArray.length; i++) {
                    String times = resArray[i];
                    List<String> timeList = getSortedFirstAvailDate(times);
                    System.out.println("timeList = " + timeList);
                    System.out.println("======================");
                    for (String time : timeList) {
                        String resTime[] = time.split("\\-");
                        String dateTime = date + " " + resTime[1];
                        System.out.println("dateTime = " + dateTime);

                    }
                }
            }
        }

    }

    private static List<String> getSortedFirstAvailDate(String resTimes) throws TelAppointException, Exception {
        // Data format => 1-10:00,10:15,10:30~2-10:00,10:15,10:30~3-10:00 etc
        List<String> resTimesList = new ArrayList<String>();

//        for (String resTimesString : resTimes.split("\\|")){
        String resTimeArray[] = resTimes.split("\\~");
        String resourceId;
        String times[];
        for (int i = 0; i < resTimeArray.length; i++) {
            // splitting ==>
            // 1-10:00,10:15,10:30, 2-10:00,10:15 etc in loop one by one.
            String resTimeArrayTemp[] = resTimeArray[i].split("\\-");
            if (resTimeArrayTemp.length == 2) {
                resourceId = resTimeArrayTemp[0].trim();
                if (resourceId == null || "".equals(resourceId)) {
                    throw new TelAppointException(ErrorConstants.ERROR_1069.getCode(), ErrorConstants.ERROR_1069.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,
                            ErrorConstants.ERROR_1069.getMessage(), resTimeArray[i].toString());
                }
            } else {
                throw new TelAppointException(ErrorConstants.ERROR_1069.getCode(), ErrorConstants.ERROR_1069.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,
                        ErrorConstants.ERROR_1069.getMessage(), resTimeArray[i].toString());
            }
            // splitting ==> 10:00,10:15,10:30
            times = resTimeArrayTemp[1].trim().split(",");
            for (String time : times) {
                resTimesList.add(resourceId + "-" + time);
            }
        }
//        }


        if (!resTimesList.isEmpty()) {
            Collections.sort(resTimesList, new Comparator<String>() {
                public int compare(String str1, String str2) {
                    String substr1 = str1.substring(2);
                    String substr2 = str2.substring(2);
                    return substr1.compareTo(substr2);
                }
            });
        }
        return resTimesList;
    }

}