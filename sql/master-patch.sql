alter table client add column ext_login_id int(4) DEFAULT NULL;
alter table client add column ext_login_password varchar(100) DEFAULT NULL;

insert  into `i18n_display_page_validation_msg`(`device`,`lang`,`message_key`,`message_value`) values 
('online','us-en','ERROR_LGE_ACCOUNT_EMPTY','Please enter your LGE Accont No#'),
('online','us-en','ERROR_LGE_ACCOUNT_INVALID','Please enter a valid LGE Accont No#');

-- 27th Feb 
-- Balaji 
CREATE TABLE `mobile_app_pages` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
   page_name varchar(50) NOT NULL,
  `lang` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `content_key` text COLLATE utf8_unicode_ci,
  `content_value` text COLLATE utf8_unicode_ci,
  PRIMARY KEY (`id`),
  unique (page_name)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- insert into `mobile_app_pages` (`page_name`, `lang`, `content_key`, `content_value`) values('home_page_init','us-en','language|clientCodeSearchLabel|goLabel|homePageInitBody|emptyErrorMessage|invalidErrorMessage','Language|Enter Client Code|Go|Page content|Please enter the client code|Please enter a valid client code');
insert into `mobile_app_pages` (`page_name`, `lang`, `content_key`, `content_value`) values('home_page','us-en','languauage|clientCodeLabel|goLabel|homePageInfo|emptyErrorMessage|invalidErrorMessage|serviceProviderLabel|poweredByLabel','Languauage|Enter Client Code|Go|Please enter the <b>Client Code</b> of your provider and click \"Go\" button. You may contact your provider, if you do not have the code. Also you may search your provider using above search icon at top right corner.|Please enter the client code|Please enter a valid client code|My Service Provider|Powered by IT FrontDesk');
-- insert into `mobile_app_pages` (`page_name`, `lang`, `content_key`, `content_value`) values('home_page_info','us-en','homePageInfo','Home Page content');
insert into `mobile_app_pages` (`page_name`, `lang`, `content_key`, `content_value`) values('clientcode_search_result_page','us-en','searchResultLabel|notFoundMessage','Search Result|We are sorry that we are unable to match the client code you have entered. Please try with correct client code or contact your provider.');
insert into `mobile_app_pages` (`page_name`, `lang`, `content_key`, `content_value`) values('search_result_by_searchkey','us-en','searchResultLabel|notFoundMessage','Search Result|We are sorry that we are unable to match your search. Please try with different search key or contact your provider.');
insert into `mobile_app_pages` (`page_name`, `lang`, `content_key`, `content_value`) values('search_field_init_value','us-en','searchFieldInitValue','Please enter business name');
insert into `mobile_app_pages` (`page_name`, `lang`, `content_key`, `content_value`) values('menu_items','us-en','home|myAccount|existAppointments|notificationPref|serviceProviders|close','Home|My Account|Existing Appointments|My Notification Preference|My Service Providers|Exit/Close');
alter table client add column client_location_google_map text DEFAULT NULL;

CREATE TABLE `my_providers_list` (
`id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'Unique id for each row',
timestamp timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
`client_code` varchar(10) NOT NULL COMMENT 'Unique Client Code - 8 character',
uuid varchar(200) NULL,
PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=1;

-- 14th May 2017
ALTER TABLE `client`
	ADD COLUMN `mobile_code` VARCHAR(100) NULL DEFAULT '' AFTER `client_code`;
	
insert into `mobile_app_pages` (`page_name`, `lang`, `content_key`, `content_value`) values('mobilecode_search_result_page','us-en','searchResultLabel|notFoundMessage','Search Result|We are sorry that we are unable to match the mobile code you have entered. Please try with correct mobile code or contact your provider.');


-- 20th May 2017
ALTER TABLE my_providers_list ADD CONSTRAINT clientCodeUuid UNIQUE (client_code, `uuid`);


ALTER TABLE `my_providers_list`
	ADD COLUMN `customer_id` BIGINT(20) UNSIGNED NULL AFTER `uuid`,
	ADD COLUMN `company_id` INT(10) NULL AFTER `customer_id`,
	ADD COLUMN `procedure_id` INT(10) NULL AFTER `company_id`,
	ADD COLUMN `location_id` INT(10) NULL AFTER `procedure_id`,
	ADD COLUMN `department_id` INT(10) NULL AFTER `location_id`,
	ADD COLUMN `resource_id` INT(10) NULL AFTER `department_id`,
	ADD COLUMN `service_id` INT(10) NULL AFTER `resource_id`;