
master_new :


INSERT INTO `i18n_display_page_validation_msg` ( `device`, `lang`, `message_key`, `message_value`)
VALUES
	('mobile', 'us-en', 'ERROR_ZIPCODE_EMPTY', 'Please enter your zipcode'),
	( 'mobile', 'us-en', 'ERROR_ZIPCODE_INVALID', 'Please enter a valid zipcode'),
	( 'mobile', 'us-en', 'ERROR_SSN_EMPTY', 'Please enter your SSN'),
	( 'mobile', 'us-en', 'ERROR_SSN_INVALID', 'Please enter a valid 9-digit SSN'),
	('mobile', 'us-en', 'ERROR_CUSTOMER_TYPE_EMPTY', 'Please select the Customer Type'),
	( 'mobile', 'us-en', 'ERROR_UTILITY_TYPE_EMPTY', 'Please select the Utility Type'),
	( 'mobile', 'us-en', 'ERROR_SERVICE_EMPTY', 'Please select the Service'),
	( 'mobile', 'us-en', 'ERROR_FIRST_NAME_EMPTY', 'Please enter your First Name'),
	( 'mobile', 'us-en', 'ERROR_FIRST_NAME_INVALID', 'Please enter a valid First Name'),
	( 'mobile', 'us-en', 'ERROR_LAST_NAME_EMPTY', 'Please enter your Last Name'),
	( 'mobile', 'us-en', 'ERROR_LAST_NAME_INVALID', 'Please enter a valid Last Name'),
	( 'mobile', 'us-en', 'ERROR_CONTACT_PHONE_EMPTY', 'Please enter your Contact Phone Number'),
	( 'mobile', 'us-en', 'ERROR_CONTACT_PHONE_INVALID', 'Please enter your 10-digit Contact Phone Number'),
	( 'mobile', 'us-en', 'ERROR_EMAIL_EMPTY', 'Please enter your Email'),
	( 'mobile', 'us-en', 'ERROR_EMAIL_INVALID', 'Please enter a valid Email'),
	( 'mobile', 'us-en', 'ERROR_ENERGY_ACC_EMPTY', 'Please enter your Energy Account Number'),
	( 'mobile', 'us-en', 'ERROR_ENERGY_ACC_INVALID', 'Please enter a valid Energy Account Number'),
	( 'mobile', 'us-en', 'INCOME_NOT_ELIGIBLE', 'Your household monthly income do not meet the eligibility criteria for assistance'),
	( 'mobile', 'us-en', 'ERROR_ADDRESS_EMPTY', 'Please enter your Address'),
	( 'mobile', 'us-en', 'ERROR_ADDRESS_INVALID', 'Please enter a valid Address'),
	( 'mobile', 'us-en', 'ERROR_CITY_EMPTY', 'Please enter your City'),
	( 'mobile', 'us-en', 'ERROR_CITY_INVALID', 'Please enter a valid City'),
	( 'mobile', 'us-en', 'ERROR_DOB_EMPTY', 'Please enter your DOB'),
	( 'mobile', 'us-en', 'ERROR_DOB_INVALID', 'Please enter a valid DOB'),
	( 'mobile', 'us-en', 'ERROR_LGE_ACCOUNT_EMPTY', 'Please enter your LGE Accont No#'),
	( 'mobile', 'us-en', 'ERROR_LGE_ACCOUNT_INVALID', 'Please enter a valid LGE Accont No#');




client :




INSERT INTO `i18n_display_field_labels` (`device`, `lang`, `message_key`, `message_value`)
VALUES
	( 'mobile', 'us-en', 'LOC_NO_APPTS', 'I\'m sorry, our appointment schedule is currently full, but funding is still available. New appointments are opened randomly on the hour almost every day. Please check back during the next appointment opening. Thank you.'),
	( 'mobile', 'us-en', 'LOCATION_CLOSED', 'I\'m sorry, our appointment scheduler is closed for this location. Please check back periodically when the scheduler will be opened up. Thank you.'),
	( 'mobile', 'us-en', 'ERROR_ALREADY_RECVD_FUNDING', 'Our records show you already received MSC\'s energy assistance funding this season. The new funding season begins in October of each year. You may reapply at that time. Thank you.'),
	( 'mobile', 'us-en', 'ERROR_OTHER_SOURCES_RECVD_LIHEAP', 'Our records indicate that you received a LIHEAP benefit for this year. You are not eligible to receive another grant until November. If you need further energy assistance, call your energy provider to discuss payment arrangements.'),
	( 'mobile', 'us-en', 'WARNING_RECEIVE_2ND_GRANT', 'Our records show you previously received a LIHEAP grant from MSC. Please note that you cannot receive our second grant, the PSE HELP grant, until the credit from your LIHEAP grant is at or below $600. To continue, click Next button'),
	( 'mobile', 'us-en', 'SERVICE_CLOSED_1', 'I\'m sorry, our funding for your energy source is unavailable. We will reopen in October and encourage you to check back at that time.'),
	( 'mobile', 'us-en', 'SERVICE_CLOSED_2', 'I\'m sorry, our funding for your energy source is unavailable. We will reopen in October and encourage you to check back at that time.'),
	( 'mobile', 'us-en', 'SERVICE_CLOSED_3', 'I\'m sorry, our funding for your energy source is unavailable. We will reopen in December and encourage you to check back at that time.'),
	( 'mobile', 'us-en', 'SER_NO_APPTS', 'I\'m sorry, our appointment schedule is currently full, but funding is still available. New appointments are opened randomly on the hour almost every day. Please check back during the next appointment opening. Thank you.'),
	( 'mobile', 'us-en', 'CUSTOMER_BLOCKED_MSG', 'I\'m sorry that unable to match with our records. Please contact MSC for further assistance.'),
	( 'mobile', 'us-en', 'other_heating_rcvd_liheap', 'Our records indicate that you received a LIHEAP benefit for this year. You are not eligible to receive another grant until December. If you need further energy assistance, call your energy provider to discuss payment arrangements.'),
	( 'mobile', 'us-en', 'receive_2nd_grant', 'Our records show you previously received a LIHEAP grant from MSC. Please note that you cannot receive our second grant, the PSE HELP grant, until the credit from your LIHEAP grant is at or below $600. To continue, click Next button'),
	( 'mobile', 'us-en', 'already_rcvd_funding', 'Our records show you already received MSC\'s energy assistance funding this season. The new funding season begins in October of each year. You may reapply at that time. Thank you.'),
	( 'mobile', 'us-en', 'ERROR_ZIPCODE_HOPELINK', 'The zip code you entered is served by Hopelink. Please visit <a href=\"http://hopelink.itfrontdesk.com\" target=\"_blank\">http://hopelink.itfrontdesk.com</a> to schedule your energy assistance appointment with Hopelink.'),
	( 'mobile', 'us-en', 'ERROR_ZIPCODE_CENTERSTONE', 'The zip code you entered is served by Centerstone in Seattle. Please visit <a href=\"http://centerstone.itfrontdesk.com\" target=\"_blank\">http://centerstone.itfrontdesk.com</a> to schedule your energy assistance appointment with Centerstone in Seattle.'),
	( 'mobile', 'us-en', 'ERROR_ZIPCODE_NO_MATCH', 'The zip code you entered is NOT served by Multi-Service Center. Please call your local energy company or the Community Information line at 211 to find out who serves your area for energy assistance.'),
	( 'mobile', 'us-en', 'ERROR_98070_ZIPCODE', 'The zip code you entered is NOT served by Multi-Service Center. Please call 1-800-422-1384 extension 122 and state that you live on Vashon Island. Thank you.'),
	( 'mobile', 'us-en', 'ERROR_98013_ZIPCODE', 'The zip code you entered is NOT served by Multi-Service Center. Please call 1-800-123-1234 extension 123 to schedule your appointment. Thank you.'),
	( 'mobile', 'us-en', 'WARNING_SHARED_98027', 'Multi-Service Center Serves the portion of this zip code that is Outside Issaquah City Limits and South of Interstate 90.'),
	( 'mobile', 'us-en', 'WARNING_SHARED_98056', 'Multi-Service Center Serves the portion of this zip code that is Outside Newcastle City Limits'),
	( 'mobile', 'us-en', 'WARNING_SHARED_98059', 'Multi-Service Center Serves the portion of this zip code that is Outside Newcastle City Limits'),
	( 'mobile', 'us-en', 'WARNING_SHARED_98178', 'Multi-Service Center Serves the portion of this zip code that is Outside Seattle City Limits'),
	( 'mobile', 'us-en', 'WARNING_SHARED_98106', 'Multi-Service Center Serves the portion of this zip code that is South of Roxburry Street.  (Outside Seattle City Limits)'),
	( 'mobile', 'us-en', 'CUSTOMER_NOT_FOUND', 'We are unable to match the SSN you have entered. Please make sure you have entered the same SSN you used for booking the appointment.'),
	( 'mobile', 'us-en', 'NO_BOOKED_APPTS', 'There are no future appointments you have scheduled with us. Please make sure you have entered the correct SSN.'),
	( 'mobile', 'us-en', 'DUPLICATE_APPT', 'You have previously scheduled appointment with us. In order to schedule a new appointment, you will need to cancel the existing appointment.'),
	( 'mobile', 'us-en', 'HOLD_NOT_RELEASED', 'Your record is currently locked and unable to access now. Please try after 15 minutes.'),
	( 'mobile', 'us-en', 'DB_ERROR', 'Unable to fetch info from database. Please try again later.'),
	( 'mobile', 'us-en', 'LOGIN_CONTACT_PHONE', '<font color=\"red\">*</font>Contact Phone'),
	( 'mobile', 'us-en', 'LOGIN_CONTACT_PHONE_EMPTY_ERROR', 'Please enter your Contact Phone'),
	( 'mobile', 'us-en', 'LOGIN_CONTACT_PHONE_INVALID_ERROR', 'Please enter a valid 10-digit Contact Phone'),
	( 'mobile', 'us-en', 'LOGIN_EMAIL', 'Email'),
	( 'mobile', 'us-en', 'LOGIN_EMAIL_EMPTY_ERROR', 'Please enter your Email'),
	( 'mobile', 'us-en', 'LOGIN_EMAIL_INVALID_ERROR', 'Please enter a valid Email'),
	( 'mobile', 'us-en', 'LOGIN_FIRST_NAME', '<font color=\"red\">*</font>First Name'),
	( 'mobile', 'us-en', 'LOGIN_FIRST_NAME_EMPTY_ERROR', 'Please enter your First Name'),
	( 'mobile', 'us-en', 'LOGIN_FIRST_NAME_INVALID_ERROR', 'Please enter a valid First Name'),
	( 'mobile', 'us-en', 'LOGIN_LAST_NAME', '<font color=\"red\">*</font>Last Name'),
	( 'mobile', 'us-en', 'LOGIN_LAST_NAME_EMPTY_ERROR', 'Please enter your Last Name'),
	( 'mobile', 'us-en', 'LOGIN_LAST_NAME_INVALID_ERROR', 'Please enter a valid Last Name'),
	( 'mobile', 'us-en', 'LOGIN_SSN', '<font color=\"red\">*</font>SSN'),
	( 'mobile', 'us-en', 'LOGIN_ENERGY_ACCT_EMPTY_ERROR', 'Please enter Energy Acct#. Enter 0 to skip.'),
	( 'mobile', 'us-en', 'LOGIN_ENERGY_ACCT_INVALID_ERROR', 'Energy Acct#  is Invalid'),
	( 'mobile', 'us-en', 'LOGIN_ENERGY_ACCT', '<font color=\"red\">*</font>Energy Acct#'),
	( 'mobile', 'us-en', 'LOGIN_APPT_TYPE_EMPTY_ERROR', 'Please choose Appointment Type'),
	( 'mobile', 'us-en', 'LOGIN_APPT_TYPE_INVALID_ERROR', 'Please choose Appointment Type'),
	( 'mobile', 'us-en', 'LOGIN_APPT_TYPE', '<font color=\"red\">*</font>Appointment Type'),
	( 'mobile', 'us-en', 'LOGIN_SSN_EMPTY_ERROR', 'Please enter 9 digit SSN'),
	( 'mobile', 'us-en', 'LOGIN_SSN_INVALID_ERROR', 'Please enter a valid 9 digit SSN'),
	( 'mobile', 'us-en', 'PAYMENT_LABEL', 'Pledge'),
	( 'mobile', 'us-en', 'LOGIN_ADDRESS', 'Address'),
	( 'mobile', 'us-en', 'ERROR_ADDRESS_EMPTY', 'Please enter your Address'),
	( 'mobile', 'us-en', 'ERROR_ADDRESS_INVALID', 'Please enter a valid Address'),
	( 'mobile', 'us-en', 'LOGIN_CITY', 'City'),
	( 'mobile', 'us-en', 'ERROR_CITY_EMPTY', 'Please enter your City'),
	( 'mobile', 'us-en', 'ERROR_CITY_INVALID', 'Please enter a valid City'),
	( 'mobile', 'us-en', 'LOGIN_ZIP', 'Zip Code'),
	( 'mobile', 'us-en', 'ERROR_ZIPCODE_EMPTY', 'Please enter your Zip Code'),
	( 'mobile', 'us-en', 'ERROR_ZIPCODE_INVALID', 'Please enter a valid Zip Code'),
	( 'mobile', 'us-en', 'LOGIN_DOB', 'DOB'),
	( 'mobile', 'us-en', 'ERROR_DOB_EMPTY', 'Please enter your DOB'),
	( 'mobile', 'us-en', 'ERROR_DOB_INVALID', 'Please enter a valid DOB'),
	( 'mobile', 'us-en', 'LOGIN_DOB_EMPTY_ERROR', 'Please enter your Date of Birth'),
	( 'mobile', 'us-en', 'LOGIN_DOB_INVALID_ERROR', 'Please enter a valid Date of Birth');


ALTER TABLE login_param_config add column field_name varchar(500) NOT NULL;
alter table login_param_config add column storage_size int(11) default 0;
alter table login_param_config add column storage_type varchar(50) NULL comment 'first / last  / full / prefix0 / postfix0';
alter table login_param_config add column java_reflection varchar(500) NULL;
alter table login_param_config add column validate_min_chars smallint(6) NULL;



INSERT INTO `login_param_config` (`device_type`, `display_context`, `placement`, `param_table`, `param_column`, `param_type`, `login_type`, `display_type`, `display_title`, `field_notes`, `display_format`, `display_size`, `max_chars`, `textarea_rows`, `textarea_cols`, `display_hint`, `display_tooltip`, `empty_error_msg`, `invalid_error_msg`, `validate_required`, `validation_rules`, `validate_max_chars`, `validate_min_value`, `validate_max_value`, `list_labels`, `list_values`, `list_initial_values`, `required`, `ivr_min_digits`, `ivr_max_digits`, `ivr_login_param_audio`, `ivr_login_param_tts`, `field_name`, `storage_size`, `storage_type`, `java_reflection`, `validate_min_chars`)
VALUES
	('mobile', 'last7', 1, 'customer', 'account_number', 'string', 'authenticate', 'textbox', 'LOGIN_SSN', NULL, ' ', 30, 9, 0, 0, NULL, NULL, 'LOGIN_SSN_EMPTY_ERROR', 'LOGIN_SSN_INVALID_ERROR', 'Y', 'numeric', 9, 9, 0, ' ', ' ', ' ', 'Y', NULL, NULL, NULL, NULL, 'SSN', 0, NULL, 'accountNumber', NULL),
	('mobile', '', 2, 'customer', 'first_name', 'string', 'update', 'textbox', 'LOGIN_FIRST_NAME', NULL, '', 30, 50, 0, 0, NULL, NULL, 'LOGIN_FIRST_NAME_EMPTY_ERROR', 'LOGIN_FIRST_NAME_INVALID_ERROR', 'Y', 'alpha, space, hypen, single-quote', 50, 0, 0, '', '', '', 'Y', NULL, NULL, NULL, NULL, 'First Name', 0, NULL, 'firstName', NULL),
	('mobile', '', 3, 'customer', 'last_name', 'string', 'update', 'textbox', 'LOGIN_LAST_NAME', NULL, '', 30, 50, 0, 0, NULL, NULL, 'LOGIN_LAST_NAME_EMPTY_ERROR', 'LOGIN_LAST_NAME_INVALID_ERROR', 'Y', 'alpha, space, hypen, single-quote', 50, 0, 0, '', '', '', 'Y', NULL, NULL, NULL, NULL, 'Last Name', 0, NULL, 'lastName', NULL),
	('mobile', '', 4, 'customer', 'home_phone', 'string', 'update', 'textbox-3-3-4', 'LOGIN_CONTACT_PHONE', NULL, '', 10, 15, 0, 0, NULL, NULL, 'LOGIN_CONTACT_PHONE_EMPTY_ERROR', 'LOGIN_CONTACT_PHONE_INVALID_ERROR', 'Y', 'numeric', 10, 0, 0, '', '', '', 'Y', NULL, NULL, NULL, NULL, 'Home Phone', 0, NULL, 'homePhone', NULL),
	('mobile', '', 5, 'customer', 'email', 'string', 'update', 'textbox', 'LOGIN_EMAIL', NULL, '', 30, 50, 0, 0, NULL, NULL, 'LOGIN_EMAIL_EMPTY_ERROR', 'LOGIN_EMAIL_INVALID_ERROR', 'Y', 'email', 50, 0, 0, '', '', '', 'N', NULL, NULL, NULL, NULL, 'Email', 0, NULL, 'email', NULL);


 alter table appt_sys_config add column check_assigned_resource char(1) DEFAULT 'N';


-- 22/Jul/2017

INSERT INTO `i18n_aliases` (`device`, `lang`, `message_key`, `message_value`)
VALUES
	('mobile', 'us-es', 'Phone Appts', 'Phone Appts'),
	('mobile', 'us-ru', 'Phone Appts', 'Phone Appts'),
	('mobile', 'us-en', 'PSE Customer', 'Puget Sound Energy customer'),
	('mobile', 'us-en', 'Other Customer', 'Other heating and energy sources'),
	('mobile', 'us-en', 'PSE Electric Service', 'PSE Electric Service Only'),
	('mobile', 'us-en', 'PSE Gas Service', 'PSE Gas Service');



CREATE TABLE `mobile_app_pages` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
   page_name varchar(50) NOT NULL,
  `lang` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `content_key` text COLLATE utf8_unicode_ci,
  `content_value` text COLLATE utf8_unicode_ci,
  PRIMARY KEY (`id`),
  unique (page_name)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

insert into `mobile_app_pages` (`page_name`, `lang`, `content_key`, `content_value`) values('appt_features_home_page','us-en','book|viewOrCancel|viewGrant|viewDocsToBring','Book a New Appointment|View or Cancel existing Appointment|View Grant Info|View List of Documents to Bring');
insert into `mobile_app_pages` (`page_name`, `lang`, `content_key`, `content_value`) values('icon_labels','us-en','home|locations|messages|docsToBring|logout','Home|Locations|Messages|Docs to bring|Logout');
insert into `mobile_app_pages` (`page_name`, `lang`, `content_key`, `content_value`) values('button_labels','us-en','back|next|bookit|cancel','Back|Next|Book It|Cancel');
insert into `mobile_app_pages` (`page_name`, `lang`, `content_key`, `content_value`) values('select_service_page','us-en','titleLabel|titleDesc|companyLabel|companyLabelDropDownInit|procedureLabel|procedureLabelDropDownInit|locationLabel|locationDropDownInit|departmentLabel|departmentDropDownInit|resourceLabel|resourceDropDownInit|serviceLabel|serviceDropInit|pageValidationErrorMessage','Select Service|Please select the Location, Doctor and Service and then click Next on the right corner.|Company|Select|Procedure|Select|Location|Select|Deparment|Select|Doctor|Select|Service|Select|Please select all the below select dropdowns before clicking "Next" button.');
insert into `mobile_app_pages` (`page_name`, `lang`, `content_key`, `content_value`) values('select_date_page','us-en','dateTimeTitle|available|notAvailable|noAvailableDateMessage|noDatesAvailable','Select Date & Time|Available|Not Available|The date you have selected is not available. Please try another date.|There are no timeslots available for the location/service you have chosen. New appointments are added every Monday. Please check back later. Thank you.');
insert into `mobile_app_pages` (`page_name`, `lang`, `content_key`, `content_value`) values('select_time_page','us-en','noTimesAvailableMessage','We are sorry that there are no available times for the date you have chosen. We open new timeslots every Monday morning and there could be some cancellations. Please check periodically for any new appointments availability.');

insert into `mobile_app_pages` (`page_name`, `lang`, `content_key`, `content_value`) values('customer_login_page','us-en','titleLabel|titleDesc|firstNameLabel|lastNameLabel|homePhoneLabel|emailLabel','Your Info|To make an appointment, please enter the requested information below  and client \"Next\" button at top right corner above.|FirstName|LastName|Home Phone|Email');
insert into `mobile_app_pages` (`page_name`, `lang`, `content_key`, `content_value`) values('verify_appt_page','us-en','titleLabel|titleDesc|firstNameLabel|lastNameLabel|apptDateTimeLabel|locationLabel|resourceLabel|serviceLabel|listOfDocsToBringLabel|addCommentLabel','Verify|Please verify the below information and click on the \"Book It\" button.|FirstName|LastName|AppointmentDateTime|Location|Doctor|Service|List of Documents to Bring|Add Comments');
insert into `mobile_app_pages` (`page_name`, `lang`, `content_key`, `content_value`) values('confirm_appt_page','us-en','titleLabel|titleDesc|confirmNumberLabel|firstNameLabel|lastNameLabel|apptDateTimeLabel|locationLabel|resourceLabel|serviceLabel|listOfDocsToBringLabel','Confirmation|Thank you for scheduling with us! Our Physician at Demo Medical Group look forward to working with you during your scheduled time below.|Confirmation#|FirstName|LastName|AppointmentDateTime|Location|Doctor|Service|List of Documents to Bring');
insert into `mobile_app_pages` (`page_name`, `lang`, `content_key`, `content_value`) values('existing_appt_page','us-en','apptHistoryLabel|confirmNumberLabel|ssnLabel|firstNameLabel|lastNameLabel|emailLabel|contactPhoneLabel|apptDateTimeLabel|locationLabel|departmentLabel|serviceLabel|noApptsLabel','Existing Appointments|Confirmation#|SSN|FirstName|LastName|Email|ContactPhone|AppointmentDateTime|Location|Doctor|ServiceConfirmNumber|There no existing appointments for the SSN and Contact Phone you have entered.');

INSERT INTO `i18n_aliases` (`id`, `device`, `lang`, `message_key`, `message_value`)
VALUES
	(24, 'mobile', 'us-en', 'Both PSE Electric and Gas Service', 'Have both PSE Electric and Gas Services');

INSERT INTO `i18n_aliases` (`id`, `device`, `lang`, `message_key`, `message_value`)
VALUES
	(27, 'mobile', 'us-en', 'PSE Electric', 'PSE Utility Assistance'),
	(28, 'mobile', 'us-en', 'LIHEAP', 'LIHEAP Utility Assistance');


-- service_funds_customer_utility_type utility_id is null for customer_type_id = 2