ALTER TABLE `customer`
	ADD COLUMN `participant_id` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0' AFTER `id`;

CREATE TABLE `trans_script_msg` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Unique Id - auto_increment',
  `schedule_id` bigint(20) NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `customer_id` bigint(20) unsigned NOT NULL COMMENT 'Customer Id linked to customer table',
  `display_flag` char(1) DEFAULT 'Y',
  `file_path` varchar(500) NOT NULL DEFAULT '/ivr_recordings/mp3/ivrappt',
  `file_name` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `schedule_id` (`schedule_id`),
  KEY `customer_id` (`customer_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

create table ext_login_process (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Unique Id - auto_increment',
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `schedule_id` bigint(20) NOT NULL,
  `api_name` varchar(200) NOT NULL,
  `json_payload` varchar(500) NOT NULL,
  `status` char(1) DEFAULT 'N' comment 'Y - Completed, N- Not completed',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

ALTER TABLE `ext_login_process`
	ALTER `api_name` DROP DEFAULT,
	ALTER `json_payload` DROP DEFAULT;
ALTER TABLE `ext_login_process`
	CHANGE COLUMN `api_name` `api_name` VARCHAR(200) NULL AFTER `schedule_id`,
	CHANGE COLUMN `json_payload` `json_payload` VARCHAR(500) NULL AFTER `api_name`;



alter table appt_sys_config add column cc_emails varchar(200) NULL;
alter table appt_sys_config drop column cc_emails;

alter table appointment add column cancel_timestamp timestamp NULL;
ALTER TABLE appointment add column cancel_method tinyint(4) not null;
alter table appointment add column cancel_type tinyint(4) not null;


-- added by balaji.
insert into i18n_display_field_labels(device,lang,message_key,message_value) values ('online','us-en','SELECTED_DATE_TIME_NOT_AVAILABLE','No Time slots available');

alter table service_location add column program_instance_id int(10) NOT NULL;
ALTER TABLE `service_location`
	ADD COLUMN `program_id` INT(10) NOT NULL AFTER `service_id`;

CREATE TABLE `sequence_data` (
    `sequence_name` varchar(100) NOT NULL,
    `sequence_increment` int(11) unsigned NOT NULL DEFAULT 1,
    `sequence_min_value` int(11) unsigned NOT NULL DEFAULT 1,
    `sequence_max_value` bigint(20) unsigned NOT NULL DEFAULT 18446744073709551615,
    `sequence_cur_value` bigint(20) unsigned DEFAULT 10000001,
    `sequence_cycle` boolean NOT NULL DEFAULT FALSE,
    PRIMARY KEY (`sequence_name`)
) ENGINE=MyISAM;

ALTER TABLE `sequence_data`
 COLLATE='utf8_general_ci',
 CONVERT TO CHARSET utf8;

 
INSERT INTO sequence_data
    (sequence_name)
VALUE
    ('sq_my_sequence')
;


-- only for development purpose
DELIMITER $$

DROP FUNCTION IF EXISTS `nextval`$$

CREATE FUNCTION `nextval`(`seq_name` varchar(100)) RETURNS bigint(20)
BEGIN
    DECLARE cur_val bigint(20);
    set cur_val=1000000;
 
    RETURN cur_val;
END$$

DELIMITER ;

DELIMITER $$
 
CREATE FUNCTION `nextval` (`seq_name` varchar(100))
RETURNS bigint(20) NOT DETERMINISTIC
BEGIN
    DECLARE cur_val bigint(20);
 
    SELECT
        sequence_cur_value INTO cur_val
    FROM
        sequence_data
    WHERE
        sequence_name = seq_name
    ;
 
    IF cur_val IS NOT NULL THEN
        UPDATE
            sequence_data
        SET
            sequence_cur_value = IF (
                (sequence_cur_value + sequence_increment) > sequence_max_value,
                IF (
                    sequence_cycle = TRUE,
                    sequence_min_value,
                    NULL
                ),
                sequence_cur_value + sequence_increment
            )
        WHERE
            sequence_name = seq_name
        ;
    END IF;
 
    RETURN cur_val;
END$$

alter table sequence_data convert to character set utf8 collate utf8_general_ci;

alter table appointment modify column cancel_timestamp timestamp NULL;
ALTER TABLE appointment modify column cancel_method tinyint(4) not null;
alter table appointment modify column cancel_type tinyint(4) not null;

-- balaji
-- Date: 26th Feb 2017
-- label for mobile device.
insert  into `i18n_display_field_labels`(`device`,`lang`,`message_key`,`message_value`) values ('mobile','us-en','DISPLAY_COMPANY_LABEL','Company');
insert  into `i18n_display_field_labels`(`device`,`lang`,`message_key`,`message_value`) values ('mobile','us-en','DISPLAY_PROCEDURE_LABEL','Procedure');
insert  into `i18n_display_field_labels`(`device`,`lang`,`message_key`,`message_value`) values ('mobile','us-en','DISPLAY_LOCATION_LABEL','Location');
insert  into `i18n_display_field_labels`(`device`,`lang`,`message_key`,`message_value`) values ('mobile','us-en','DISPLAY_DEPARTMENT_LABEL','Department');
insert  into `i18n_display_field_labels`(`device`,`lang`,`message_key`,`message_value`) values ('mobile','us-en','DISPLAY_RESOURCE_LABEL','Resource');
insert  into `i18n_display_field_labels`(`device`,`lang`,`message_key`,`message_value`) values ('mobile','us-en','DISPLAY_SERVICE_LABEL','Service');
insert  into `i18n_display_field_labels`(`device`,`lang`,`message_key`,`message_value`) values ('mobile','us-en','DISPLAY_DATE_TIME_LABEL','DateTime');
insert  into `i18n_display_field_labels`(`device`,`lang`,`message_key`,`message_value`) values ('mobile','us-en','DISPLAY_COMMENTS_LABEL','Comments');


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




CREATE TABLE `messages` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `effective_date` DATE NULL,
  `expiry_date` DATE NULL,
  `delete_flag` char(1) NOT NULL DEFAULT 'N',
   message text,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

ALTER TABLE login_param_config add column field_name varchar(500) NOT NULL;

insert into `mobile_app_pages` (`page_name`, `lang`, `content_key`, `content_value`) values('customer_login_page','us-en','titleLabel|titleDesc|firstNameLabel|lastNameLabel|homePhoneLabel|emailLabel','Your Info|To make an appointment, please enter the requested information below  and client \"Next\" button at top right corner above.|FirstName|LastName|Home Phone|Email');
insert into `mobile_app_pages` (`page_name`, `lang`, `content_key`, `content_value`) values('verify_appt_page','us-en','titleLabel|titleDesc|firstNameLabel|lastNameLabel|apptDateTimeLabel|locationLabel|resourceLabel|serviceLabel|listOfDocsToBringLabel|addCommentLabel','Verify|Please verify the below information and click on the \"Book It\" button.|FirstName|LastName|AppointmentDateTime|Location|Doctor|Service|List of Documents to Bring|Add Comments');
insert into `mobile_app_pages` (`page_name`, `lang`, `content_key`, `content_value`) values('confirm_appt_page','us-en','titleLabel|titleDesc|confirmNumberLabel|firstNameLabel|lastNameLabel|apptDateTimeLabel|locationLabel|resourceLabel|serviceLabel|listOfDocsToBringLabel','Confirmation|Thank you for scheduling with us! Our Physician at Demo Medical Group look forward to working with you during your scheduled time below.|Confirmation#|FirstName|LastName|AppointmentDateTime|Location|Doctor|Service|List of Documents to Bring');
insert into `mobile_app_pages` (`page_name`, `lang`, `content_key`, `content_value`) values('existing_appt_page','us-en','apptHistoryLabel|confirmNumberLabel|ssnLabel|firstNameLabel|lastNameLabel|emailLabel|contactPhoneLabel|apptDateTimeLabel|locationLabel|departmentLabel|serviceLabel|noApptsLabel','Existing Appointments|Confirmation#|SSN|FirstName|LastName|Email|ContactPhone|AppointmentDateTime|Location|Doctor|ServiceConfirmNumber|There no existing appointments for the SSN and Contact Phone you have entered.');

alter table appointment add column enrollment_id varchar(50) null;
alter table appointmentstatus add column denied char(1) default 'N';

-- added by balaji.
insert into i18n_display_field_labels(device,lang,message_key,message_value) values ('mobile','us-en','SELECTED_DATE_TIME_NOT_AVAILABLE','We are sorry that the date and time you have chosen is no longer available. Please select another date/time. Thankyou.');
insert into i18n_display_field_labels(device,lang,message_key,message_value) values ('mobile','us-en','DUPLICATE_APPT','You have previously scheduled appointment with us. In order to schedule a new appointment, you will need to cancel the existing appointment.');
insert into i18n_display_field_labels(device,lang,message_key,message_value) values ('mobile','us-en','HOLD_NOT_RELEASED','Your record is currently locked and unable to access now. Please try after 15 minutes.');


alter table login_param_config add column storage_size int(11) default 0;
alter table login_param_config add column storage_type varchar(50) NULL comment 'first / last  / full / prefix0 / postfix0';
alter table login_param_config add column java_reflection varchar(500) NULL;
alter table login_param_config add column validate_min_chars smallint(6) NULL;

alter table list_of_things_bring add column list_of_docs_tts varchar(100) NULL;
alter table list_of_things_bring add column list_of_docs_audio varchar(100) NULL;

insert  into `i18n_display_field_labels`(`device`,`lang`,`message_key`,`message_value`) values ('online','us-en','CUSTOMER_NOT_FOUND','We are unable to match your login credentials with our system. Please try again or contact your provider. Thankyou.');
insert  into `i18n_display_field_labels`(`device`,`lang`,`message_key`,`message_value`) values ('mobile','us-en','CUSTOMER_NOT_FOUND','We are unable to match your login credentials with our system. Please try again or contact your provider. Thankyou.');

-- balaji
-- date 26th Apr 2017
insert  into `i18n_display_field_labels`(`device`,`lang`,`message_key`,`message_value`) values ('online','us-en','NOT_ELIGIBLE','Not eligible.');

insert into `i18n_display_field_labels` (`device`, `lang`, `message_key`, `message_value`) values('mobile','us-en','NO_BOOKED_APPTS','There are no future appointments you have scheduled with us. Please make sure you have entered the correct SSN.');
insert into `i18n_display_field_labels` (`device`, `lang`, `message_key`, `message_value`) values('mobile','us-en','DUPLICATE_APPT','You have previously scheduled appointment with us. In order to schedule a new appointment, you will need to cancel the existing appointment.');
insert into `i18n_display_field_labels` (`device`, `lang`, `message_key`, `message_value`) values('mobile','us-en','HOLD_NOT_RELEASED','Your record is currently locked and unable to access now. Please try after 15 minutes.');




alter table appt_sys_config add column online_datetime_display char(1) DEFAULT 'N';

ALTER TABLE department CHANGE COLUMN `location_name_ivr_tts` `department_name_ivr_tts` VARCHAR(100) NOT NULL;
-- ALTER TABLE department CHANGE COLUMN  `department_name_ivr_tts` `location_name_ivr_tts` VARCHAR(100) NOT NULL;

ALTER TABLE `i18n_aliases`
 COLLATE='utf8_general_ci',
 CONVERT TO CHARSET utf8;
 
 
 alter table appt_sys_config add column check_assigned_resource char(1) DEFAULT 'N';
 alter table customer add column resource_id int(11) NULL;
