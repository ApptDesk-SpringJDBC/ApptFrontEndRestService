DELIMITER $$

DROP PROCEDURE IF EXISTS `temp_hold_appointment_sp`$$

CREATE PROCEDURE `temp_hold_appointment_sp`(IN `res_id` INT(10), IN `appt_date_time` VARCHAR(60), IN `block_time_in_mins` INT(11), IN `ser_id` INT(10), IN `cust_id` BIGINT(20), OUT `hold_id` BIGINT(20), OUT `error_msg` VARCHAR(2000), OUT `display_datetime` VARCHAR(200))
BEGIN
	DECLARE rows_count INT DEFAULT 0;
	DECLARE no_blocks INT DEFAULT 0;
	DECLARE dups_appt CHAR(1) DEFAULT 'N'; -- no duplicates by default
	DECLARE dups_appt_count INT DEFAULT 0;
	DECLARE dups_hold_count INT DEFAULT 0;
	
	START TRANSACTION;
	
	select allow_duplicate_appt INTO dups_appt from service where id=ser_id;
	
	select count(s.`id`) INTO dups_appt_count from `schedule` s, customer c where c.`id` = cust_id and s.customer_id = c.`id` and s.appt_date_time > now() and s.`status` = 11;
	
	select count(s.`id`) INTO dups_hold_count from `schedule` s, customer c where c.`id` = cust_id and s.customer_id = c.`id` and s.appt_date_time > now() and  NOT(s.appt_date_time = appt_date_time and s.resource_id = res_id) and s.`status` = 1;
	
	IF(dups_appt = 'N' and dups_appt_count > 0) THEN
		SET error_msg = 'DUPLICATE_APPT';
	ELSE
		IF(dups_appt = 'N' and dups_hold_count > 0) THEN
			SET error_msg = 'HOLD_NOT_RELEASED';
		ELSE
			select `blocks` INTO no_blocks from service where id=ser_id;
	
			-- find out timeslots are still available for that date/time and resourceId
			select count(1) INTO rows_count from resource_calendar where date_time >= appt_date_time and date_time < DATE_ADD(appt_date_time, INTERVAL (no_blocks * block_time_in_mins) MINUTE) and resource_id=res_id and schedule_id=0;
	
			IF(rows_count = no_blocks) THEN
				-- fetch the hold_id and use it to block the timeslots
				SELECT rc.id INTO hold_id from resource_calendar rc where rc.date_time=appt_date_time and resource_id=res_id and schedule_id=0;
	
				SET hold_id = hold_id * -1;
				update resource_calendar set schedule_id=hold_id where date_time >= appt_date_time and date_time < DATE_ADD(appt_date_time, INTERVAL (no_blocks * block_time_in_mins) MINUTE) and resource_id=res_id;
				COMMIT;
	
				select DATE_FORMAT(appt_date_time, online_datetime_display) INTO display_datetime from appt_sys_config;
			ELSE 
				SET hold_id = -1;
				SET error_msg = 'SELECTED_DATE_TIME_NOT_AVAILABLE';
			END IF;
		END IF;
	END IF;
END$$
DELIMITER ;

DELIMITER $$

DROP PROCEDURE IF EXISTS `hold_appointment_callcenter_sp`$$

CREATE PROCEDURE `hold_appointment_callcenter_sp`(IN `appt_date_time` VARCHAR(60), IN `block_time_in_mins` INT(11), IN `loc_id` INT(10), IN proc_id INT(10), IN dept_id INT(10), IN `ser_id` INT(10), IN `cust_id` BIGINT(20), IN `trans_id` BIGINT(20), IN device VARCHAR(10), OUT `sched_id` BIGINT(20), OUT `error_msg` VARCHAR(2000), OUT `display_datetime` VARCHAR(200))
BEGIN
	DECLARE done INT DEFAULT 0;
	DECLARE rows_count INT DEFAULT 0;
	DECLARE no_blocks INT DEFAULT 0;
	DECLARE dups_appt CHAR(1) DEFAULT 'N'; -- no duplicates by default
	DECLARE dups_appt_count INT DEFAULT 0;
	DECLARE dups_hold_count INT DEFAULT 0;
	DECLARE sp_res_id INT(10);

	DECLARE cur_resource_list CURSOR  FOR select distinct r.id from location_department_resource ldr, resource r, resource_service rs, service s where ldr.location_id = loc_id and ldr.department_id = dept_id and ldr.enable = 'Y' and ldr. resource_id = r.id and r.enable = 'Y' and r.delete_flag = 'N' and r.allow_selfservice = 'Y' and r.id = rs.resource_id and rs.service_id = ser_id and rs.enable = 'Y' and rs.allow_selfservice = 'Y' and rs.service_id = s.id and s.enable = 'Y' and s.delete_flag = 'N' order by r.placement;
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
	
	START TRANSACTION;
	
	select allow_duplicate_appt INTO dups_appt from service where id=ser_id;
	
	select count(s.`id`) INTO dups_appt_count from `schedule` s, customer c where c.`id` = cust_id and s.customer_id = c.`id` and s.appt_date_time > now() and s.`status` = 11;
	
	select count(s.`id`) INTO dups_hold_count from `schedule` s, customer c where c.`id` = cust_id and s.customer_id = c.`id` and s.appt_date_time > now() and  s.`status` = 1;
	
	
	
	SET sched_id = 1;
	
	IF(dept_id = NULL or dept_id = 0) THEN
		SET dept_id = 1;
	END IF;
	IF(proc_id = NULL or proc_id = 0) THEN
		SET proc_id = 1;
	END IF;
	
	IF(dups_appt = 'N' and dups_appt_count > 0 and cust_id > 0) THEN
		SET error_msg = 'DUPLICATE_APPT';
	ELSE
		IF(dups_appt = 'N' and dups_hold_count > 0 and cust_id > 0) THEN
			SET error_msg = 'HOLD_NOT_RELEASED';
		ELSE
			SET done = 0;
			OPEN cur_resource_list;
			REPEAT
				FETCH cur_resource_list INTO sp_res_id;
				IF (done=0) THEN


					select `blocks` INTO no_blocks from service where id=ser_id;
			
					-- find out timeslots are still available for that date/time and resourceId
					select count(1) INTO rows_count from resource_calendar where date_time >= appt_date_time and date_time < DATE_ADD(appt_date_time, INTERVAL (no_blocks * block_time_in_mins) MINUTE) and resource_id=sp_res_id and schedule_id=0;
			
					IF(rows_count = no_blocks) THEN
						INSERT INTO schedule (timestamp,status,appt_date_time,blocks,trans_id,procedure_id,location_id,department_id,resource_id,service_id,customer_id,updated_by) values (CONVERT_TZ(now(),'US/Central','US/Pacific'),1,appt_date_time,no_blocks,trans_id,proc_id,loc_id,dept_id,sp_res_id,ser_id,cust_id,concat('book:',lower(device),'@',DATE_FORMAT(CONVERT_TZ(now(),'US/Central','US/Pacific'),'%m/%d/%Y %h:%i %p')));
						SELECT LAST_INSERT_ID() INTO sched_id;
						update resource_calendar set schedule_id=sched_id where date_time >= appt_date_time and date_time < DATE_ADD(appt_date_time, INTERVAL (no_blocks * block_time_in_mins) MINUTE) and resource_id=sp_res_id;
						COMMIT;
			
						select DATE_FORMAT(appt_date_time, online_datetime_display) INTO display_datetime from appt_sys_config;
						SET done = 1;
					END IF;
				END IF;
			UNTIL done END REPEAT;
			close cur_resource_list;
			
			IF(sched_id = 1) THEN
				SET error_msg = 'SELECTED_DATE_TIME_NOT_AVAILABLE';
			END IF;
					
		END IF;
	END IF;
END$$
DELIMITER ;



DELIMITER $$

DROP PROCEDURE IF EXISTS `release_hold_appt_sp`$$

CREATE PROCEDURE `release_hold_appt_sp`(IN `sched_id` BIGINT(20), OUT `status_result` VARCHAR(200), OUT `error_msg` VARCHAR(2000))
BEGIN
	DECLARE status_sp INT DEFAULT 0;
	
	select status INTO status_sp from schedule WHERE `id` = sched_id;

	IF(sched_id > 0 and status_sp = 1) THEN
		UPDATE schedule SET status = 2 WHERE `id` = sched_id;
		UPDATE resource_calendar SET schedule_id = 0 WHERE schedule_id = sched_id;
		COMMIT;
		SET status_result = 'Y';
	ELSE
		SET status_result = 'N';
	END IF;

END$$
DELIMITER ;


DELIMITER $$
DROP PROCEDURE IF EXISTS `get_first_avail_date_times_callcenter_sp`$$
CREATE PROCEDURE `get_first_avail_date_times_callcenter_sp`(IN time_zone VARCHAR(50), IN loc_id INT(10), IN dept_id INT(10), IN ser_id INT(10), IN block_time_in_mins INT(11), OUT avail_date_times TEXT, OUT error_msg VARCHAR(2000))
BEGIN
	-- This stored procedure fetches the first available date and its timeslots for that date using below filters:
	-- * select available dates > now() and only within the appointment window dates/time
	-- * remove holidays & closed days
	-- * remove days (Sun - Sat) for which service is not opened
	-- * remove timeslots reserved for the resource_id for that date
	-- * remove non-consecutive timeslots
	-- * Return data in format 'DATE|RES_ID1-TIME1,TIME2,TIME3|RES_ID2-TIME1,TIME2,TIME3'
	-- * Return data as '2015-05-24|1-10:00,10:15,16:00,16:15|2-09:30,11:15'
	
	DECLARE sp_start_date timestamp;
	DECLARE sp_end_date date;
	DECLARE cur_avail_dates date;
	
	DECLARE sp_max_appt_duration_days TINYINT(4);
	DECLARE sp_appt_delay_time_days TINYINT(4);
	DECLARE sp_appt_delay_time_hrs TINYINT(4);
	DECLARE sp_restrict_appt_window CHAR(1);
	DECLARE sp_appt_start_date DATE;
	DECLARE sp_appt_end_date DATE;
	DECLARE sp_restrict_loc_appt_window CHAR(1);
	DECLARE sp_restrict_ser_appt_window CHAR(1);
	DECLARE sp_restrict_loc_ser_appt_window CHAR(1);
	DECLARE sp_cur_ser_blocks TINYINT(4);
	DECLARE sp_cur_avail_dates DATE;
	DECLARE sp_cur_avail_times TIME;
	DECLARE timeArrayOfValue TEXT;
	DECLARE sp_res_id INT(10);
	DECLARE TIMESLOT_FOUND TINYINT(4) DEFAULT 0;
	DECLARE temp_avail_date_times TEXT DEFAULT '';
	DECLARE avail_date_times_sp TEXT DEFAULT '';
	
	-- fetch all appt window configuration settings from appt_sys_config table
	DECLARE cur_appt_sys_config CURSOR FOR select max_appt_duration_days, appt_delay_time_days, appt_delay_time_hrs, restrict_appt_window, appt_start_date, appt_end_date, restrict_loc_appt_window, restrict_ser_appt_window, restrict_loc_ser_appt_window from appt_sys_config;
	DECLARE cur_ser_blocks CURSOR FOR select blocks from service where id = ser_id;
	-- fetch distinct available dates filtered by holidays & closed days, and service availability days	
	DECLARE cur_avail_dates CURSOR  FOR select rc.date from (select DATE(date_time) "date" from resource_calendar rc  where date_time > DATE_ADD(CURDATE(), INTERVAL 1 day) and date_time >= sp_start_date and date_time < DATE_ADD(sp_end_date, INTERVAL 1 day) and resource_id IN (select distinct r.id from location_department_resource ldr, resource r, resource_service rs, service s where ldr.location_id = loc_id and ldr.department_id = dept_id and ldr.enable = 'Y' and ldr. resource_id = r.id and r.enable = 'Y' and r.delete_flag = 'N' and r.allow_selfservice = 'Y' and r.id = rs.resource_id and rs.service_id = ser_id and rs.enable = 'Y' and rs.allow_selfservice = 'Y' and rs.service_id = s.id and s.enable = 'Y' and s.delete_flag = 'N') and schedule_id = 0 group by DATE(date_time) ) rc where rc.date NOT IN (select h.date from holidays h where date >= DATE(sp_start_date) and date <= sp_end_date union select c.date from closed_days c where date >= DATE(sp_start_date) and date <= sp_end_date and location_id = loc_id) and 'Y' = 
	(
	SELECT CASE DATE_FORMAT(rc.date,'%w')
	WHEN 0 THEN is_sun_open
	WHEN 1 THEN is_mon_open
	WHEN 2 THEN is_tue_open
	WHEN 3 THEN is_wed_open
	WHEN 4 THEN is_thu_open
	WHEN 5 THEN is_fri_open
	WHEN 6 THEN is_sat_open
	END AS days
	FROM service where id = ser_id
	) order by rc.date;
	DECLARE cur_resource_list CURSOR  FOR select distinct r.id from location_department_resource ldr, resource r, resource_service rs, service s where ldr.location_id = loc_id and ldr.department_id = dept_id and ldr.enable = 'Y' and ldr. resource_id = r.id and r.enable = 'Y' and r.delete_flag = 'N' and r.allow_selfservice = 'Y' and r.id = rs.resource_id and rs.service_id = ser_id and rs.enable = 'Y' and rs.allow_selfservice = 'Y' and rs.service_id = s.id and s.enable = 'Y' and s.delete_flag = 'N' order by r.placement;
	-- fetch available times for that date filtered by resource_time_off (reserved) times
	DECLARE cur_avail_times CURSOR  FOR select TIME(rc.date_time) from resource_calendar rc where rc.resource_id = sp_res_id and DATE(rc.date_time) = sp_cur_avail_dates and schedule_id = 0 and  (select count(1) from resource_time_off rt where rc.resource_id = rt.resource_id and TIME(rc.date_time) >= start_time and TIME(rc.date_time) < end_time and DATE(rc.date_time) = rt.start_date) = 0 order by TIME(rc.date_time);
	
	IF(dept_id = NULL or dept_id = 0) THEN
		SET dept_id = 1;
	END IF;
	
	OPEN cur_appt_sys_config;
		FETCH cur_appt_sys_config INTO sp_max_appt_duration_days,sp_appt_delay_time_days,sp_appt_delay_time_hrs,sp_restrict_appt_window,sp_appt_start_date,sp_appt_end_date,sp_restrict_loc_appt_window,sp_restrict_ser_appt_window,sp_restrict_loc_ser_appt_window;
	CLOSE cur_appt_sys_config;
	
	-- fetch number of blocks for the service_id
	OPEN cur_ser_blocks;
		FETCH cur_ser_blocks INTO sp_cur_ser_blocks;
	CLOSE cur_ser_blocks;
	
	
	-- determine how to apply the appointment window based on basic window, location specific, service specific, location-service specific, or global settings
	IF(sp_restrict_appt_window = 'Y') THEN
		SET sp_start_date = sp_appt_start_date;
		SET sp_end_date = sp_appt_end_date;
	ELSEIF(loc_id > 0 and sp_restrict_loc_appt_window = 'Y') THEN
		select appt_start_date, appt_end_date INTO sp_start_date, sp_end_date from location where id = loc_id;
	ELSEIF(ser_id > 0 and sp_restrict_ser_appt_window = 'Y') THEN
		select appt_start_date, appt_end_date INTO sp_start_date, sp_end_date from service where id = ser_id;
	ELSEIF(loc_id > 0 and ser_id > 0 and sp_restrict_loc_ser_appt_window = 'Y') THEN
		select start_date, end_date INTO sp_start_date, sp_end_date from service_location where location_id = loc_id and service_id = ser_id;
	ELSE
		select CONVERT_TZ(DATE_ADD(NOW(),INTERVAL concat(appt_delay_time_days,',',appt_delay_time_hrs) DAY_HOUR),'US/Central',time_zone) INTO sp_start_date from appt_sys_config;
		select DATE(CONVERT_TZ(DATE_ADD(NOW(),INTERVAL max_appt_duration_days DAY),'US/Central',time_zone)) INTO sp_end_date from appt_sys_config;
	END IF;
	
	SET avail_date_times = '';
	BLOCK1: begin
		declare no_more_rows1 boolean DEFAULT FALSE;
		declare continue handler for not found set no_more_rows1 := TRUE;
		OPEN cur_avail_dates;
		-- iterate through all available dates
		LOOP1: loop
			FETCH cur_avail_dates INTO sp_cur_avail_dates;
			if no_more_rows1 then
				close cur_avail_dates;
				leave LOOP1;
			end if;
				
				BLOCK2: begin
					declare no_more_rows2 boolean DEFAULT FALSE;
					declare continue handler for not found set no_more_rows2 := TRUE;
				
						OPEN cur_resource_list;
						SET sp_res_id = 0;
						LOOP2: loop
							FETCH cur_resource_list INTO sp_res_id;
							if no_more_rows2 then
								close cur_resource_list;
								leave LOOP2;
							end if;
						
							BLOCK3: begin
								declare no_more_rows3 boolean DEFAULT FALSE;
								declare continue handler for not found set no_more_rows3 := TRUE;
								
									OPEN cur_avail_times;
									-- iterate through all available times and remove non-consecutive timeslots
									SET timeArrayOfValue = '';
									LOOP3: loop
										FETCH cur_avail_times INTO sp_cur_avail_times;
										if no_more_rows3 then
											close cur_avail_times;
											leave LOOP3;
										end if;
										
										SET timeArrayOfValue = CONCAT(timeArrayOfValue, ',', CAST(sp_cur_avail_times as CHAR(8)));
									end loop LOOP3;
							
									SET timeArrayOfValue = CONCAT(SUBSTRING(timeArrayOfValue FROM 2),',');
									-- set time values as string array - '10:00,10:15,10:30,16:00,16:15,16:30,'
				
									SET temp_avail_date_times = '';
									-- filter only consecutive timeslots
									WHILE (LOCATE(',', timeArrayOfValue) > 0)
									DO
										SET @timeStr = SUBSTRING(timeArrayOfValue, 1, LOCATE(',',timeArrayOfValue)-1); -- fetch first time '10:00'
										SET timeArrayOfValue = SUBSTRING(timeArrayOfValue, LOCATE(',', timeArrayOfValue) + 1); -- remaining values '10:15,10:30,16:00,16:15,16:30,'
													
										SET @sp_required_consective_time_str = @timeStr; -- first time it will be '10:00'
										SET @sp_given_consective_time_str = @timeStr; -- first time it will be '10:00'
										SET @timeStrTemp = timeArrayOfValue; -- first time it will be '10:15,10:30,16:00,16:15,16:30,'
										SET @sp_consecutive_time_iteration = 1;
										-- creates of string of required consecutive times for the given time (timeStr)
										
										WHILE (@sp_consecutive_time_iteration < sp_cur_ser_blocks)
										DO
											SET @sp_given_consective_time_str = concat(@sp_given_consective_time_str, ',', SUBSTRING(@timeStrTemp, 1, LOCATE(',',@timeStrTemp)-1)); -- will set to '10:00,10:15'
											SET @timeStrTemp = SUBSTRING(@timeStrTemp, LOCATE(',', @timeStrTemp) + 1); -- remaining values '10:30,16:00,16:15,16:30,'
							
											SET @sp_required_consective_time_str= concat(@sp_required_consective_time_str, ',', TIME(DATE_ADD(CONCAT('1900-01-01 ' , @timeStr), INTERVAL (@sp_consecutive_time_iteration * block_time_in_mins) MINUTE)));
														
											SET @sp_consecutive_time_iteration = @sp_consecutive_time_iteration + 1;
										END WHILE;
													
										IF (@sp_required_consective_time_str = @sp_given_consective_time_str) THEN
											SET temp_avail_date_times = CONCAT(temp_avail_date_times, ',', @timeStr);
										END IF;
													
									END WHILE;
					
									IF (LENGTH(temp_avail_date_times) > 1) THEN
										SET TIMESLOT_FOUND = TIMESLOT_FOUND + 1;
										IF (TIMESLOT_FOUND = 1) THEN
											SET avail_date_times_sp = CONCAT(sp_cur_avail_dates, '|', sp_res_id, '-', SUBSTRING(temp_avail_date_times, 2));
										ELSE
											SET avail_date_times_sp = CONCAT(avail_date_times_sp, '~', sp_res_id, '-', SUBSTRING(temp_avail_date_times, 2));
										END IF;
									END IF;
							end BLOCK3;	
						end loop LOOP2;
				end BLOCK2;		
				IF (LENGTH(avail_date_times_sp) > 1) THEN
					close cur_avail_dates;
					leave LOOP1;
				END IF;
		end loop LOOP1;
	end BLOCK1;		

				
	IF (LENGTH(avail_date_times_sp) > 1) THEN
		SET avail_date_times = avail_date_times_sp;
	ELSE
		SET avail_date_times = 'LOC_NO_APPTS';
	END IF;
	-- will return as '2015-05-24|1-10:00,10:15,16:00,16:15|2-09:30,11:15'
END$$
DELIMITER ;


DELIMITER $$
DROP PROCEDURE IF EXISTS `get_available_dates_callcenter_sp`$$
CREATE PROCEDURE `get_available_dates_callcenter_sp`(IN time_zone VARCHAR(50), IN loc_id INT(10), IN dept_id INT(10), IN ser_id INT(10), IN block_time_in_mins INT(11), OUT avail_dates TEXT, OUT error_msg VARCHAR(2000))
BEGIN
	-- This stored procedure fetches the available dates using below filters:
	-- * select available dates > now() and only within the appointment window dates/time
	-- * remove holidays & closed days
	-- * remove days (Sun - Sat) for which service is not opened
	DECLARE done INT DEFAULT 0;
	DECLARE sp_start_date timestamp;
	DECLARE sp_end_date date;
	DECLARE cur_avail_dates date;
	
	DECLARE sp_max_appt_duration_days TINYINT(4);
	DECLARE sp_appt_delay_time_days TINYINT(4);
	DECLARE sp_appt_delay_time_hrs TINYINT(4);
	DECLARE sp_restrict_appt_window CHAR(1);
	DECLARE sp_appt_start_date DATE;
	DECLARE sp_appt_end_date DATE;
	DECLARE sp_restrict_loc_appt_window CHAR(1);
	DECLARE sp_restrict_ser_appt_window CHAR(1);
	DECLARE sp_restrict_loc_ser_appt_window CHAR(1);
	DECLARE sp_cur_avail_dates DATE;
	
	-- fetch all appt window configuration settings from appt_sys_config table
	DECLARE cur_appt_sys_config CURSOR FOR select max_appt_duration_days, appt_delay_time_days, appt_delay_time_hrs, restrict_appt_window, appt_start_date, appt_end_date, restrict_loc_appt_window, restrict_ser_appt_window, restrict_loc_ser_appt_window from appt_sys_config;
	DECLARE cur_ser_blocks CURSOR FOR select blocks from service where id = ser_id;
	-- fetch distinct available dates filtered by holidays & closed days, and service availability days	
	DECLARE cur_avail_dates CURSOR  FOR select rc.date from (select DATE(date_time) "date" from resource_calendar rc  where date_time > DATE_ADD(CURDATE(), INTERVAL 1 day) and date_time >= sp_start_date and date_time < DATE_ADD(sp_end_date, INTERVAL 1 day) and resource_id IN (select distinct r.id from location_department_resource ldr, resource r, resource_service rs, service s where ldr.location_id = loc_id and ldr.department_id = dept_id and ldr.enable = 'Y' and ldr. resource_id = r.id and r.enable = 'Y' and r.delete_flag = 'N' and r.allow_selfservice = 'Y' and r.id = rs.resource_id and rs.service_id = ser_id and rs.enable = 'Y' and rs.allow_selfservice = 'Y' and rs.service_id = s.id and s.enable = 'Y' and s.delete_flag = 'N') and schedule_id = 0 group by DATE(date_time) ) rc where rc.date NOT IN (select h.date from holidays h where date >= DATE(sp_start_date) and date <= sp_end_date union select c.date from closed_days c where date >= DATE(sp_start_date) and date <= sp_end_date and location_id = loc_id) and 'Y' = 
	(
	SELECT CASE DATE_FORMAT(rc.date,'%w')
	WHEN 0 THEN is_sun_open
	WHEN 1 THEN is_mon_open
	WHEN 2 THEN is_tue_open
	WHEN 3 THEN is_wed_open
	WHEN 4 THEN is_thu_open
	WHEN 5 THEN is_fri_open
	WHEN 6 THEN is_sat_open
	END AS days
	FROM service where id = ser_id
	) order by rc.date;
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
	
	IF(dept_id = NULL or dept_id = 0 or dept_id = -1) THEN
		SET dept_id = 1;
	END IF;
	
	OPEN cur_appt_sys_config;
		FETCH cur_appt_sys_config INTO sp_max_appt_duration_days,sp_appt_delay_time_days,sp_appt_delay_time_hrs,sp_restrict_appt_window,sp_appt_start_date,sp_appt_end_date,sp_restrict_loc_appt_window,sp_restrict_ser_appt_window,sp_restrict_loc_ser_appt_window;
	CLOSE cur_appt_sys_config;
	
	
	-- determine how to apply the appointment window based on basic window, location specific, service specific, location-service specific, or global settings
	IF(sp_restrict_appt_window = 'Y') THEN
		SET sp_start_date = sp_appt_start_date;
		SET sp_end_date = sp_appt_end_date;
	ELSEIF(loc_id > 0 and sp_restrict_loc_appt_window = 'Y') THEN
		select appt_start_date, appt_end_date INTO sp_start_date, sp_end_date from location where id = loc_id;
	ELSEIF(ser_id > 0 and sp_restrict_ser_appt_window = 'Y') THEN
		select appt_start_date, appt_end_date INTO sp_start_date, sp_end_date from service where id = ser_id;
	ELSEIF(loc_id > 0 and ser_id > 0 and sp_restrict_loc_ser_appt_window = 'Y') THEN
		select start_date, end_date INTO sp_start_date, sp_end_date from service_location where location_id = loc_id and service_id = ser_id;
	ELSE
		select CONVERT_TZ(DATE_ADD(NOW(),INTERVAL concat(appt_delay_time_days,',',appt_delay_time_hrs) DAY_HOUR),'US/Central',time_zone) INTO sp_start_date from appt_sys_config;
		select DATE(CONVERT_TZ(DATE_ADD(NOW(),INTERVAL max_appt_duration_days DAY),'US/Central',time_zone)) INTO sp_end_date from appt_sys_config;
	END IF;
	
	SET done = 0;
	SET avail_dates = '';
	OPEN cur_avail_dates;
		-- iterate through all available dates
		REPEAT
			FETCH cur_avail_dates INTO sp_cur_avail_dates;
			IF (done=0) THEN
				SET avail_dates = CONCAT(avail_dates, ',', sp_cur_avail_dates);
			END IF;	
		UNTIL done END REPEAT;
		SET avail_dates = SUBSTRING(avail_dates,2);
		-- will return as 'mm/dd/yyyy' format
	close cur_avail_dates;
END$$
DELIMITER ;


DELIMITER $$
DROP PROCEDURE IF EXISTS `get_available_dates_sp`$$
CREATE PROCEDURE `get_available_dates_sp`(IN time_zone VARCHAR(50), IN loc_id INT(10), IN dept_id INT(10),IN res_id INT(10), IN ser_id INT(10), IN block_time_in_mins INT(11), OUT avail_dates TEXT, OUT error_msg VARCHAR(2000))
BEGIN
	-- This stored procedure fetches the available dates using below filters:
	-- * select available dates > now() and only within the appointment window dates/time
	-- * remove holidays & closed days
	-- * remove days (Sun - Sat) for which service is not opened
	DECLARE done INT DEFAULT 0;
	DECLARE sp_start_date timestamp;
	DECLARE sp_end_date date;
	DECLARE cur_avail_dates date;
	
	DECLARE sp_max_appt_duration_days TINYINT(4);
	DECLARE sp_appt_delay_time_days TINYINT(4);
	DECLARE sp_appt_delay_time_hrs TINYINT(4);
	DECLARE sp_restrict_appt_window CHAR(1);
	DECLARE sp_appt_start_date DATE;
	DECLARE sp_appt_end_date DATE;
	DECLARE sp_restrict_loc_appt_window CHAR(1);
	DECLARE sp_restrict_ser_appt_window CHAR(1);
	DECLARE sp_restrict_loc_ser_appt_window CHAR(1);
	DECLARE sp_cur_avail_dates DATE;
	
	-- fetch all appt window configuration settings from appt_sys_config table
	DECLARE cur_appt_sys_config CURSOR FOR select max_appt_duration_days, appt_delay_time_days, appt_delay_time_hrs, restrict_appt_window, appt_start_date, appt_end_date, restrict_loc_appt_window, restrict_ser_appt_window, restrict_loc_ser_appt_window from appt_sys_config;
	DECLARE cur_ser_blocks CURSOR FOR select blocks from service where id = ser_id;
	-- fetch distinct available dates filtered by holidays & closed days, and service availability days	
	DECLARE cur_avail_dates CURSOR  FOR select rc.date from (select DATE(date_time) "date" from resource_calendar rc  where date_time > DATE_ADD(CURDATE(), INTERVAL 1 day) and date_time >= sp_start_date and date_time < DATE_ADD(sp_end_date, INTERVAL 1 day) and resource_id=res_id and schedule_id = 0 group by DATE(date_time) ) rc where rc.date NOT IN (select h.date from holidays h where date >= DATE(sp_start_date) and date <= sp_end_date union select c.date from closed_days c where date >= DATE(sp_start_date) and date <= sp_end_date and location_id = loc_id) and 'Y' = 
	(
	SELECT CASE DATE_FORMAT(rc.date,'%w')
	WHEN 0 THEN is_sun_open
	WHEN 1 THEN is_mon_open
	WHEN 2 THEN is_tue_open
	WHEN 3 THEN is_wed_open
	WHEN 4 THEN is_thu_open
	WHEN 5 THEN is_fri_open
	WHEN 6 THEN is_sat_open
	END AS days
	FROM service where id = ser_id
	) order by rc.date;
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
	
	IF(dept_id = NULL or dept_id = 0 or dept_id = -1) THEN
		SET dept_id = 1;
	END IF;
	
	OPEN cur_appt_sys_config;
		FETCH cur_appt_sys_config INTO sp_max_appt_duration_days,sp_appt_delay_time_days,sp_appt_delay_time_hrs,sp_restrict_appt_window,sp_appt_start_date,sp_appt_end_date,sp_restrict_loc_appt_window,sp_restrict_ser_appt_window,sp_restrict_loc_ser_appt_window;
	CLOSE cur_appt_sys_config;
	
	
	-- determine how to apply the appointment window based on basic window, location specific, service specific, location-service specific, or global settings
	IF(sp_restrict_appt_window = 'Y') THEN
		SET sp_start_date = sp_appt_start_date;
		SET sp_end_date = sp_appt_end_date;
	ELSEIF(loc_id > 0 and sp_restrict_loc_appt_window = 'Y') THEN
		select appt_start_date, appt_end_date INTO sp_start_date, sp_end_date from location where id = loc_id;
	ELSEIF(ser_id > 0 and sp_restrict_ser_appt_window = 'Y') THEN
		select appt_start_date, appt_end_date INTO sp_start_date, sp_end_date from service where id = ser_id;
	ELSEIF(loc_id > 0 and ser_id > 0 and sp_restrict_loc_ser_appt_window = 'Y') THEN
		select start_date, end_date INTO sp_start_date, sp_end_date from service_location where location_id = loc_id and service_id = ser_id;
	ELSE
		select CONVERT_TZ(DATE_ADD(NOW(),INTERVAL concat(appt_delay_time_days,',',appt_delay_time_hrs) DAY_HOUR),'US/Central',time_zone) INTO sp_start_date from appt_sys_config;
		select DATE(CONVERT_TZ(DATE_ADD(NOW(),INTERVAL max_appt_duration_days DAY),'US/Central',time_zone)) INTO sp_end_date from appt_sys_config;
	END IF;
	
	SET done = 0;
	SET avail_dates = '';
	OPEN cur_avail_dates;
		-- iterate through all available dates
		REPEAT
			FETCH cur_avail_dates INTO sp_cur_avail_dates;
			IF (done=0) THEN
				SET avail_dates = CONCAT(avail_dates, ',', sp_cur_avail_dates);
			END IF;	
		UNTIL done END REPEAT;
		SET avail_dates = SUBSTRING(avail_dates,2);
		-- will return as 'mm/dd/yyyy' format
	close cur_avail_dates;
END$$
DELIMITER ;

DELIMITER $$
DROP PROCEDURE IF EXISTS `get_avail_times_callcenter_sp`$$
CREATE PROCEDURE `get_avail_times_callcenter_sp`(IN loc_id INT(10), IN dept_id INT(10), IN ser_id INT(10), IN block_time_in_mins INT(11), IN avail_date DATE, OUT avail_date_times TEXT, OUT error_msg VARCHAR(2000))
BEGIN
	-- This stored procedure fetches the available timeslots for supplied date
	-- * remove timeslots reserved for the resource_id for that date
	-- * remove non-consecutive timeslots
	-- * Return data in format 'DATE|RES_ID1-TIME1,TIME2,TIME3|RES_ID2-TIME1,TIME2,TIME3'
	-- * Return data as '2015-05-24|1-10:00,10:15,16:00,16:15|2-09:30,11:15'
	
	DECLARE sp_cur_ser_blocks TINYINT(4);
	DECLARE sp_cur_avail_times TIME;
	DECLARE timeArrayOfValue TEXT;
	DECLARE sp_res_id INT(10);
	DECLARE length_consective_time TINYINT(4);
	DECLARE TIMESLOT_FOUND TINYINT(4) DEFAULT 0;
	DECLARE temp_avail_date_times TEXT DEFAULT '';
	DECLARE avail_date_times_sp TEXT DEFAULT '';
	
	-- fetch all appt window configuration settings from appt_sys_config table
	DECLARE cur_appt_sys_config CURSOR FOR select max_appt_duration_days, appt_delay_time_days, appt_delay_time_hrs, restrict_appt_window, appt_start_date, appt_end_date, restrict_loc_appt_window, restrict_ser_appt_window, restrict_loc_ser_appt_window from appt_sys_config;
	DECLARE cur_ser_blocks CURSOR FOR select blocks from service where id = ser_id;
	-- fetch distinct available dates filtered by holidays & closed days, and service availability days	
	DECLARE cur_resource_list CURSOR  FOR select distinct r.id from location_department_resource ldr, resource r, resource_service rs, service s where ldr.location_id = loc_id and ldr.department_id = dept_id and ldr.enable = 'Y' and ldr. resource_id = r.id and r.enable = 'Y' and r.delete_flag = 'N' and r.allow_selfservice = 'Y' and r.id = rs.resource_id and rs.service_id = ser_id and rs.enable = 'Y' and rs.allow_selfservice = 'Y' and rs.service_id = s.id and s.enable = 'Y' and s.delete_flag = 'N' order by r.placement;
	-- fetch available times for that date filtered by resource_time_off (reserved) times
	DECLARE cur_avail_times CURSOR  FOR select TIME(rc.date_time) from resource_calendar rc where rc.resource_id = sp_res_id and DATE(rc.date_time) = avail_date and schedule_id = 0 and  (select count(1) from resource_time_off rt where rc.resource_id = rt.resource_id and TIME(rc.date_time) >= start_time and TIME(rc.date_time) < end_time and DATE(rc.date_time) = rt.start_date) = 0 order by TIME(rc.date_time);
	
	IF(dept_id = NULL or dept_id = 0) THEN
		SET dept_id = 1;
	END IF;
	
	-- fetch number of blocks for the service_id
	OPEN cur_ser_blocks;
		FETCH cur_ser_blocks INTO sp_cur_ser_blocks;
	CLOSE cur_ser_blocks;
	
	
	BLOCK1: begin
		declare no_more_rows1 boolean DEFAULT FALSE;
		declare continue handler for not found set no_more_rows1 := TRUE;
		OPEN cur_resource_list;
		
		LOOP1: loop
			FETCH cur_resource_list INTO sp_res_id;
			if no_more_rows1 then
				close cur_resource_list;
				leave LOOP1;
			end if;
			
				BLOCK2: begin
					declare no_more_rows2 boolean DEFAULT FALSE;
					declare continue handler for not found set no_more_rows2 := TRUE;
						OPEN cur_avail_times;
						-- iterate through all available times and remove non-consecutive timeslots
						SET timeArrayOfValue = '';
					
						LOOP2: loop
							FETCH cur_avail_times INTO sp_cur_avail_times;
							if no_more_rows2 then
								close cur_avail_times;
								leave LOOP2;
							end if;
							
							SET timeArrayOfValue = CONCAT(timeArrayOfValue, ',', CAST(sp_cur_avail_times as CHAR(8)));
						end loop LOOP2;	
						
						SET timeArrayOfValue = CONCAT(SUBSTRING(timeArrayOfValue FROM 2),',');
						-- set time values as string array - '10:00,10:15,10:30,16:00,16:15,16:30,'
						
						SET temp_avail_date_times = '';
						SET length_consective_time = 1;
						-- filter only consecutive timeslots
						WHILE (LOCATE(',', timeArrayOfValue) > 0)
						DO
							SET timeArrayOfValue = SUBSTRING(timeArrayOfValue, length_consective_time);
							SET @timeStr = SUBSTRING(timeArrayOfValue, 1, LOCATE(',',timeArrayOfValue)-1); -- fetch first time '10:00'
							SET timeArrayOfValue = SUBSTRING(timeArrayOfValue, LOCATE(',', timeArrayOfValue) + 1); -- remaining values '10:15,10:30,16:00,16:15,16:30,'
										
							SET @sp_required_consective_time_str = @timeStr; -- first time it will be '10:00'
							SET @sp_given_consective_time_str = @timeStr; -- first time it will be '10:00'
							SET @timeStrTemp = timeArrayOfValue; -- first time it will be '10:15,10:30,16:00,16:15,16:30,'
							SET @sp_consecutive_time_iteration = 1;
							-- creates of string of required consecutive times for the given time (timeStr)
							
							SET length_consective_time = 1;
							WHILE (@sp_consecutive_time_iteration < sp_cur_ser_blocks)
							DO
								SET @sp_given_consective_time_str = concat(@sp_given_consective_time_str, ',', SUBSTRING(@timeStrTemp, 1, LOCATE(',',@timeStrTemp)-1)); -- will set to '10:00,10:15'
								SET @timeStrTemp = SUBSTRING(@timeStrTemp, LOCATE(',', @timeStrTemp) + 1); -- remaining values '10:30,16:00,16:15,16:30,'
				
								SET @sp_required_consective_time_str= concat(@sp_required_consective_time_str, ',', TIME(DATE_ADD(CONCAT('1900-01-01 ' , @timeStr), INTERVAL (@sp_consecutive_time_iteration * block_time_in_mins) MINUTE)));
											
								SET @sp_consecutive_time_iteration = @sp_consecutive_time_iteration + 1;
							END WHILE;
										
							IF (@sp_required_consective_time_str = @sp_given_consective_time_str) THEN
								IF (@sp_consecutive_time_iteration > 1) THEN
									SET length_consective_time = (@sp_consecutive_time_iteration - 1) * 9;
								END IF;
								SET temp_avail_date_times = CONCAT(temp_avail_date_times, ',', @timeStr);
							END IF;
										
						END WHILE;

						IF (LENGTH(temp_avail_date_times) > 1) THEN
							SET TIMESLOT_FOUND = TIMESLOT_FOUND + 1;
							IF (TIMESLOT_FOUND = 1) THEN
								SET avail_date_times_sp = CONCAT(avail_date, '|', sp_res_id, '-', SUBSTRING(temp_avail_date_times, 2));
							ELSE
								SET avail_date_times_sp = CONCAT(avail_date_times_sp, '|', sp_res_id, '-', SUBSTRING(temp_avail_date_times, 2));
							END IF;
						END IF;
				end BLOCK2;		
		end loop LOOP1;
	end BLOCK1;		
	
	IF (LENGTH(avail_date_times_sp) > 1) THEN
		SET avail_date_times = avail_date_times_sp;
	ELSE
		SET avail_date_times = 'NO_AVAIL_TIMESLOTS';
	END IF;

END$$
DELIMITER ;



DELIMITER $$

DROP PROCEDURE IF EXISTS `book_appointment_sp`$$
CREATE PROCEDURE `book_appointment_sp`(IN `sched_id` BIGINT(20), IN `block_time_in_mins` INT(11), IN `appt_method` INT(4), IN `device` VARCHAR(50), IN `lang_code` VARCHAR(5), OUT `display_keys` TEXT, OUT `display_values` TEXT, OUT `error_msg` VARCHAR(2000))
	LANGUAGE SQL
	NOT DETERMINISTIC
	CONTAINS SQL
	SQL SECURITY DEFINER
	COMMENT ''
BEGIN
	DECLARE sched_rows_count INT DEFAULT 0;
	DECLARE zero_rows_count INT DEFAULT 0;
	DECLARE sched_status INT DEFAULT 0;
	DECLARE hold_status INT DEFAULT 0;
	DECLARE appt_date_time_value datetime;
	DECLARE no_blocks int(4);
	DECLARE res_id int(10);
	DECLARE cur_sched_items CURSOR  FOR select `status`, appt_date_time, `blocks`, resource_id FROM `schedule` WHERE `id`=sched_id;
	DECLARE cur_display_items_online CURSOR  FOR select 'a.conf_number|s.appt_date_time|s.appt_date_time_start|s.appt_date_time_end|s.appt_date_time_display|r.prefix|r.first_name|r.last_name|r.email|l.location_name_online|l.address|l.city|l.state|l.zip|l.location_google_map|l.location_google_map_link|l.time_zone|p.procedure_name_online|d.department_name_online|ia.message_value.service|c.account_number|c.first_name|c.last_name|c.contact_phone|c.home_phone|c.work_phone|c.cell_phone|c.email|c.attrib1|c.attrib2|c.attrib3|c.attrib4|c.attrib5|c.attrib6|c.attrib7|c.attrib8|c.attrib9|c.attrib10|doc.display_text|r.resource_tts|r.resource_audio|l.location_name_ivr_tts|l.location_name_ivr_audio|p.procedure_name_ivr_tts|p.procedure_name_ivr_audio|d.department_name_ivr_tts|d.department_name_ivr_audio|ser.service_name_ivr_tts|ser.service_name_ivr_audio|doc.list_of_docs_tts|doc.list_of_docs_audio', CONCAT_WS('|',IFNULL(a.conf_number,''),s.appt_date_time,appt_date_time_value,DATE_ADD(appt_date_time_value, INTERVAL (no_blocks * block_time_in_mins) MINUTE),DATE_FORMAT(s.appt_date_time, sys.online_datetime_display),IFNULL(r.prefix,''),IFNULL(r.first_name,''),IFNULL(r.last_name,''),IFNULL(r.email,''),IFNULL(l.location_name_online,''),IFNULL(l.address,''),IFNULL(l.city,''),IFNULL(l.state,''),IFNULL(l.zip,''),IFNULL(l.location_google_map,''),IFNULL(l.location_google_map_link,''),IFNULL(l.time_zone,''),IFNULL(p.procedure_name_online,''),IFNULL(d.department_name_online,''),IFNULL(ia.message_value,''),IFNULL(c.account_number,''),IFNULL(c.first_name,''),IFNULL(c.last_name,''),IFNULL(c.contact_phone,''),IFNULL(c.home_phone,''),IFNULL(c.work_phone,''),IFNULL(c.cell_phone,''),IFNULL(c.email,''),IFNULL(c.attrib1,''),IFNULL(c.attrib2,''),IFNULL(c.attrib3,''),IFNULL(c.attrib4,''),IFNULL(c.attrib5,''),IFNULL(c.attrib6,''),IFNULL(c.attrib7,''),IFNULL(c.attrib8,''),IFNULL(c.attrib9,''),IFNULL(c.attrib10,''),IFNULL(doc.display_text,''),CONCAT(IFNULL(r.first_name,''),' ',IFNULL(r.last_name,'')),IFNULL(r.resource_audio,''),IFNULL(l.location_name_ivr_tts,''),IFNULL(l.location_name_ivr_audio,''),IFNULL(p.procedure_name_ivr_tts,''),IFNULL(p.procedure_name_ivr_audio,''),IFNULL(d.department_name_ivr_tts,''),IFNULL(d.department_name_ivr_audio,''),IFNULL(ser.service_name_ivr_tts,''),IFNULL(ser.service_name_ivr_audio,''),IFNULL(doc.list_of_docs_tts,''),IFNULL(doc.list_of_docs_audio,'')) from `schedule` s LEFT OUTER JOIN appointment a ON s.id = a.schedule_id LEFT OUTER JOIN resource r ON s.resource_id = r.id LEFT OUTER JOIN location l ON s.location_id = l.id LEFT OUTER JOIN `procedure` p ON s.procedure_id = p.id LEFT OUTER JOIN department d ON s.department_id = d.id LEFT OUTER JOIN service ser ON s.service_id = ser.id LEFT OUTER JOIN customer c ON s.customer_id = c.id LEFT OUTER JOIN list_of_things_bring doc ON s.service_id = doc.service_id and doc.lang = lang_code COLLATE utf8_general_ci LEFT OUTER JOIN i18n_aliases ia ON ser.service_name_online = ia.message_key and ia.device = 'online' and ia.lang = lang_code COLLATE utf8_general_ci JOIN appt_sys_config sys where s.id = sched_id;
  
	START TRANSACTION;
  
	OPEN cur_sched_items;
		FETCH cur_sched_items INTO sched_status, appt_date_time_value, no_blocks, res_id;
	CLOSE cur_sched_items;
  
	-- find out timeslots are still in hold state for that schedule_id
	select count(1) INTO sched_rows_count from resource_calendar where date_time >= appt_date_time_value and date_time < DATE_ADD(appt_date_time_value, INTERVAL (no_blocks * block_time_in_mins) MINUTE) and resource_id=res_id and schedule_id=sched_id;
	select count(1) INTO zero_rows_count from resource_calendar where date_time >= appt_date_time_value and date_time < DATE_ADD(appt_date_time_value, INTERVAL (no_blocks * block_time_in_mins) MINUTE) and resource_id=res_id and schedule_id=0;
  
	IF(sched_rows_count = no_blocks and sched_status = 1) THEN
		SET hold_status=1;
	ELSEIF(zero_rows_count = no_blocks and sched_status = 2) THEN
		UPDATE resource_calendar set schedule_id=sched_id where date_time >= appt_date_time_value and date_time < DATE_ADD(appt_date_time_value, INTERVAL (no_blocks * block_time_in_mins) MINUTE) and resource_id=res_id;
		UPDATE `schedule` SET `status`=1 WHERE `id`=sched_id;
		COMMIT;
		SET hold_status=1;
	END IF;
	
	IF(hold_status = 1) THEN
		UPDATE schedule SET status = 11 where id=sched_id;
		INSERT INTO appointment(trans_id,schedule_id,timestamp,appt_method,appt_type,outlook_google_sync) SELECT trans_id,id, now(), appt_method, 1, 'N' from schedule where id=sched_id;
		INSERT INTO notify(timestamp,campaign_id,call_now,emergency_notify,broadcast_mode,notify_status,resource_id,location_id,service_id,customer_id,schedule_id, first_name,last_name,home_phone,work_phone,cell_phone,email,notify_by_phone,notify_by_phone_confirm,notify_by_sms,notify_by_sms_confirm,notify_by_email, notify_by_email_confirm,notify_phone_status,notify_sms_status,notify_email_status,due_date_time,attrib1,attrib2,attrib3,attrib4,attrib5,attrib6,attrib7, attrib8,attrib9,attrib10,attrib11,attrib12,attrib13,attrib14,attrib15,attrib16,attrib17,attrib18,attrib19,attrib20) SELECT now(),cp.`id`,'N','N','N',1,s.resource_id,s.location_id,s.service_id,s.customer_id,s.`id`,c.first_name,c.last_name,IFNULL(c.home_phone,c.contact_phone),c.work_phone,c.cell_phone,c.email,cp.notify_by_phone,cp.notify_by_phone_confirm,cp.notify_by_sms,cp.notify_by_sms_confirm,cp.notify_by_email,cp.notify_by_email_confirm,0,0,0,s.appt_date_time,c.attrib1,c.attrib2,c.attrib3,c.attrib4,c.attrib5,c.attrib6,c.attrib7,c.attrib8,c.attrib9,c.attrib10,c.attrib11,c.attrib12,c.attrib13,c.attrib14,c.attrib15,c.attrib16,c.attrib17,c.attrib18,c.attrib19,c.attrib20 from `schedule` s, customer c, campaign cp where s.`id`=sched_id and s.customer_id = c.`id` and cp.`name` = 'Appt Reminder';
		COMMIT;
	  
		OPEN cur_display_items_online;
			FETCH cur_display_items_online INTO display_keys,display_values;
		CLOSE cur_display_items_online;
	ELSE
		SET error_msg = 'Unable to book the appointment.';
	END IF;
END$$
DELIMITER ;

DELIMITER $$
DROP PROCEDURE IF EXISTS `cancel_appointment_sp`$$
CREATE PROCEDURE `cancel_appointment_sp`(IN `sched_id` BIGINT(20), IN `block_time_in_mins` INT(11), IN `cancel_method` INT(4), IN `lang_code` VARCHAR(5), OUT `success` CHAR(1),  OUT `display_keys` TEXT, OUT `display_values` TEXT, OUT `error_msg` VARCHAR(2000))
BEGIN
  DECLARE sched_rows_count INT DEFAULT 0;
  DECLARE sched_status INT DEFAULT 0;
  DECLARE appt_date_time_value datetime;
  DECLARE no_blocks int(4);DECLARE res_id int(10);
  DECLARE cur_sched_items CURSOR  FOR select `status`, appt_date_time, `blocks`, resource_id FROM `schedule` WHERE `id`=sched_id;
  DECLARE cur_display_items_online CURSOR  FOR select 'a.conf_number|s.appt_date_time|s.appt_date_time_start|s.appt_date_time_end|s.appt_date_time_display|r.prefix|r.first_name|r.last_name|r.email|l.location_name_online|l.address|l.city|l.state|l.zip|l.location_google_map|l.location_google_map_link|l.time_zone|p.procedure_name_online|d.department_name_online|ia.message_value.service|c.account_number|c.first_name|c.last_name|c.contact_phone|c.home_phone|c.work_phone|c.cell_phone|c.email|c.attrib1|c.attrib2|c.attrib3|c.attrib4|c.attrib5|c.attrib6|c.attrib7|c.attrib8|c.attrib9|c.attrib10|doc.display_text|r.resource_tts|r.resource_audio|l.location_name_ivr_tts|l.location_name_ivr_audio|p.procedure_name_ivr_tts|p.procedure_name_ivr_audio|d.department_name_ivr_tts|d.department_name_ivr_audio|ser.service_name_ivr_tts|ser.service_name_ivr_audio|doc.list_of_docs_tts|doc.list_of_docs_audio', CONCAT_WS('|',IFNULL(a.conf_number,''),s.appt_date_time,appt_date_time_value,DATE_ADD(appt_date_time_value, INTERVAL (no_blocks * block_time_in_mins) MINUTE),DATE_FORMAT(s.appt_date_time, sys.online_datetime_display),IFNULL(r.prefix,''),IFNULL(r.first_name,''),IFNULL(r.last_name,''),IFNULL(r.email,''),IFNULL(l.location_name_online,''),IFNULL(l.address,''),IFNULL(l.city,''),IFNULL(l.state,''),IFNULL(l.zip,''),IFNULL(l.location_google_map,''),IFNULL(l.location_google_map_link,''),IFNULL(l.time_zone,''),IFNULL(p.procedure_name_online,''),IFNULL(d.department_name_online,''),IFNULL(ia.message_value,''),IFNULL(c.account_number,''),IFNULL(c.first_name,''),IFNULL(c.last_name,''),IFNULL(c.contact_phone,''),IFNULL(c.home_phone,''),IFNULL(c.work_phone,''),IFNULL(c.cell_phone,''),IFNULL(c.email,''),IFNULL(c.attrib1,''),IFNULL(c.attrib2,''),IFNULL(c.attrib3,''),IFNULL(c.attrib4,''),IFNULL(c.attrib5,''),IFNULL(c.attrib6,''),IFNULL(c.attrib7,''),IFNULL(c.attrib8,''),IFNULL(c.attrib9,''),IFNULL(c.attrib10,''),IFNULL(doc.display_text,''),CONCAT(IFNULL(r.first_name,''),' ',IFNULL(r.last_name,'')),IFNULL(r.resource_audio,''),IFNULL(l.location_name_ivr_tts,''),IFNULL(l.location_name_ivr_audio,''),IFNULL(p.procedure_name_ivr_tts,''),IFNULL(p.procedure_name_ivr_audio,''),IFNULL(d.location_name_ivr_tts,''),IFNULL(d.department_name_ivr_audio,''),IFNULL(ser.service_name_ivr_tts,''),IFNULL(ser.service_name_ivr_audio,''),IFNULL(doc.list_of_docs_tts,''),IFNULL(doc.list_of_docs_audio,'')) from `schedule` s LEFT OUTER JOIN appointment a ON s.id = a.schedule_id LEFT OUTER JOIN resource r ON s.resource_id = r.id LEFT OUTER JOIN location l ON s.location_id = l.id LEFT OUTER JOIN `procedure` p ON s.procedure_id = p.id LEFT OUTER JOIN department d ON s.department_id = d.id LEFT OUTER JOIN service ser ON s.service_id = ser.id LEFT OUTER JOIN customer c ON s.customer_id = c.id LEFT OUTER JOIN list_of_things_bring doc ON s.service_id = doc.service_id and doc.lang = lang_code COLLATE utf8_general_ci LEFT OUTER JOIN i18n_aliases ia ON ser.service_name_online = ia.message_key and ia.device = 'online' and ia.lang = lang_code COLLATE utf8_general_ci JOIN appt_sys_config sys where s.id = sched_id;

	START TRANSACTION;
	OPEN cur_sched_items;
		FETCH cur_sched_items INTO sched_status, appt_date_time_value, no_blocks, res_id;
	CLOSE cur_sched_items;
	
	SET success = 'N';
	-- find out timeslots are still in hold state for that schedule_id
	select count(1) INTO sched_rows_count from resource_calendar where date_time >= appt_date_time_value and date_time < DATE_ADD(appt_date_time_value, INTERVAL (no_blocks * block_time_in_mins) MINUTE) and resource_id=res_id and schedule_id=sched_id;
	IF(sched_rows_count = no_blocks and sched_status = 11) THEN
		UPDATE resource_calendar SET schedule_id = 0 where date_time >= appt_date_time_value and date_time < DATE_ADD(appt_date_time_value, INTERVAL (no_blocks * block_time_in_mins) MINUTE) and resource_id=res_id and schedule_id=sched_id;
		UPDATE schedule SET status = 21, updated_by = CONCAT(updated_by,',cancel:',(CASE cancel_method WHEN 1 THEN 'online' WHEN 2 THEN 'ivr' WHEN 3 THEN 'admin' END)) where id=sched_id;
		-- kept appt_type = 2 for backward compatibility purpose
		UPDATE appointment SET cancel_timestamp = now(), appt_type = 2, cancel_method = cancel_method, cancel_type = 2, outlook_google_sync = 'N' where schedule_id = sched_id;
		UPDATE notify SET do_not_notify = 'Y', delete_flag = 'Y' where schedule_id = sched_id;
		COMMIT;
	
		OPEN cur_display_items_online;
			FETCH cur_display_items_online INTO display_keys,display_values;
		CLOSE cur_display_items_online;
		
		SET success = 'Y';
	ELSE
		SET error_msg = 'Unable to cancel this appointment. Please try again later.';
	END IF;
END$$
DELIMITER ;


DELIMITER $$
DROP PROCEDURE IF EXISTS `get_customer_info_sp`$$
CREATE PROCEDURE `get_customer_info_sp`(IN `cust_id` BIGINT(20))
BEGIN
	DECLARE myval VARCHAR(100);
	DECLARE done INT DEFAULT 0;
	DECLARE sp_param_column varchar(1000);
	DECLARE sp_java_reflection varchar(1000);
	DECLARE sp_param_type varchar(1000);
	DECLARE sp_field_name varchar(1000);
	DECLARE sp_display_type varchar(1000);
	DECLARE cur_online_page_fields CURSOR  FOR select `param_column`, `java_reflection`, `param_type`, `field_name`, `display_type` FROM `online_page_fields` WHERE login_type = 'update' and param_table = 'customer' order by placement;
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
	
	SET @cust_sql = '';
	SET @cust_sql_java_refl = '';
	SET @cust_sql_col_type = '';
	SET @cust_sql_field_name = '';
	SET @cust_sql_display_type = '';
	SET done = 0;
	
	OPEN cur_online_page_fields;
	REPEAT
	FETCH cur_online_page_fields INTO sp_param_column, sp_java_reflection, sp_param_type, sp_field_name, sp_display_type;
	IF (done=0) THEN
		SET @cust_sql = CONCAT(@cust_sql, ',', sp_param_column);
		SET @cust_sql_java_refl = CONCAT(@cust_sql_java_refl, ',', sp_java_reflection);
		SET @cust_sql_col_type = CONCAT(@cust_sql_col_type, ',', sp_param_type);
		SET @cust_sql_field_name = CONCAT(@cust_sql_field_name, ',', sp_field_name);
		SET @cust_sql_display_type = CONCAT(@cust_sql_display_type, ',', sp_display_type);
	END IF;
	UNTIL done END REPEAT;
	CLOSE cur_online_page_fields;
	
	SET @cust_sql = SUBSTRING(@cust_sql, 2);
	SET @cust_sql_java_refl = SUBSTRING(@cust_sql_java_refl, 2);
	SET @cust_sql_col_type = SUBSTRING(@cust_sql_col_type, 2);
	SET @cust_sql_field_name = SUBSTRING(@cust_sql_field_name, 2);
	SET @cust_sql_display_type = SUBSTRING(@cust_sql_display_type, 2);
	SET @s = CONCAT('select ''',@cust_sql,''' AS "Key",''',@cust_sql_java_refl,''' AS "JavaRef",''',@cust_sql_col_type,''' AS "Type",''',@cust_sql_field_name,''' AS "Field",''',@cust_sql_display_type,''' AS "DisplayType",',@cust_sql,' from customer where id = ',cust_id);
	
	PREPARE stmt1 FROM @s;
	EXECUTE stmt1;
	DEALLOCATE PREPARE stmt1;
END$$
DELIMITER ;



DELIMITER $$
DROP PROCEDURE IF EXISTS `get_location_availability_sp`$$
CREATE PROCEDURE `get_location_availability_sp`(IN time_zone VARCHAR(50), IN `block_time_in_mins` INT(11), IN loc_id INT(10), OUT availability CHAR(1), OUT error_msg VARCHAR(2000))
BEGIN
	DECLARE sp_start_date timestamp;DECLARE sp_end_date date;
	DECLARE cur_avail_dates date;
	DECLARE sp_max_appt_duration_days TINYINT(4);
	DECLARE sp_appt_delay_time_days TINYINT(4);
	DECLARE sp_appt_delay_time_hrs TINYINT(4);
	DECLARE sp_restrict_appt_window CHAR(1);
	DECLARE sp_appt_start_date DATE;
	DECLARE sp_appt_end_date DATE;
	DECLARE sp_restrict_loc_appt_window CHAR(1);
	DECLARE sp_restrict_ser_appt_window CHAR(1);
	DECLARE sp_restrict_loc_ser_appt_window CHAR(1);
	DECLARE sp_cur_ser_blocks TINYINT(4);
	DECLARE sp_cur_avail_dates DATE;
	DECLARE sp_cur_avail_times TIME;
	DECLARE timeArrayOfValue TEXT;
	DECLARE sp_res_id INT(10);
	DECLARE sp_service_id INT(10) DEFAULT 0;
	DECLARE sp_loc_not_allowed TINYINT(4) DEFAULT 0;
	DECLARE res_id int(10) DEFAULT 0;
	DECLARE avail_date_times TEXT DEFAULT '';
	-- fetch all appt window configuration settings from appt_sys_config table
	DECLARE cur_loc_not_allowed CURSOR FOR select count(1) from location where id = loc_id and (delete_flag = 'Y' or `enable` = 'N' or closed = 'Y');
	DECLARE cur_appt_sys_config CURSOR FOR select max_appt_duration_days, appt_delay_time_days, appt_delay_time_hrs, restrict_appt_window, appt_start_date, appt_end_date, restrict_loc_appt_window, restrict_ser_appt_window, restrict_loc_ser_appt_window from appt_sys_config;
	DECLARE cur_ser_blocks CURSOR FOR select ser.id, MIN(ser.blocks) from service ser, resource res, resource_service rs where res.location_id = loc_id and res.delete_flag = 'N' and res.`enable` = 'Y' and res.allow_selfservice = 'Y' and res.id = rs.resource_id and rs.`enable` = 'Y' and rs.allow_selfservice = 'Y' and rs.service_id = ser.id and ser.delete_flag = 'N' and ser.enable = 'Y';
	-- fetch distinct available dates filtered by holidays & closed days, and service availability days	
	DECLARE cur_avail_dates CURSOR  FOR select rc.date from (select DATE(date_time) "date" from resource_calendar rc  where date_time > DATE_ADD(CURDATE(), INTERVAL 1 day) and date_time >= sp_start_date and date_time < DATE_ADD(sp_end_date, INTERVAL 1 day) and resource_id = res_id and schedule_id = 0 group by DATE(date_time) ) rc where rc.date NOT IN (select h.date from holidays h where date >= DATE(sp_start_date) and date <= sp_end_date union select c.date from closed_days c where date >= DATE(sp_start_date) and date <= sp_end_date and location_id = loc_id) order by rc.date;
	-- fetch available times for that date filtered by resource_time_off (reserved) times
	DECLARE cur_avail_times CURSOR  FOR select TIME(rc.date_time) from resource_calendar rc where rc.resource_id = res_id and DATE(rc.date_time) = sp_cur_avail_dates and schedule_id = 0 and  (select count(1) from resource_time_off rt where rc.resource_id = rt.resource_id and TIME(rc.date_time) >= start_time and TIME(rc.date_time) < end_time and DATE(rc.date_time) = rt.start_date) = 0 order by TIME(rc.date_time);
	DECLARE cur_resource_list CURSOR  FOR select distinct res.id from resource res, resource_service rs where res.location_id = loc_id and res.delete_flag = 'N' and res.`enable` = 'Y' and res.allow_selfservice = 'Y' and res.id = rs.resource_id and rs.`enable` = 'Y' and rs.allow_selfservice = 'Y' order by res.placement;
	SET availability = 'N';
	
	OPEN cur_loc_not_allowed;
		FETCH cur_loc_not_allowed INTO sp_loc_not_allowed;
	CLOSE cur_loc_not_allowed;
	
	IF(sp_loc_not_allowed = 0) THEN
	
		OPEN cur_appt_sys_config;
			FETCH cur_appt_sys_config INTO sp_max_appt_duration_days,sp_appt_delay_time_days,sp_appt_delay_time_hrs,sp_restrict_appt_window,sp_appt_start_date,sp_appt_end_date,sp_restrict_loc_appt_window,sp_restrict_ser_appt_window,sp_restrict_loc_ser_appt_window;
			-- fetch number of blocks for the service_id
		CLOSE cur_appt_sys_config;
		OPEN cur_ser_blocks;
			FETCH cur_ser_blocks INTO sp_service_id,sp_cur_ser_blocks;
		CLOSE cur_ser_blocks;
		-- determine how to apply the appointment window based on basic window, location specific, service specific, location-service specific, or global settings
		
		IF(sp_restrict_appt_window = 'Y') THEN
			SET sp_start_date = sp_appt_start_date;
			SET sp_end_date = sp_appt_end_date;
		ELSEIF(sp_restrict_loc_appt_window = 'Y') THEN
			select appt_start_date, appt_end_date INTO sp_start_date, sp_end_date from location where id = loc_id;
		ELSEIF(sp_restrict_ser_appt_window = 'Y') THEN
			select appt_start_date, appt_end_date INTO sp_start_date, sp_end_date from service where id = sp_service_id;
		ELSEIF(sp_restrict_loc_ser_appt_window = 'Y') THEN
			select start_date, end_date INTO sp_start_date, sp_end_date from service_location where location_id = loc_id and service_id = sp_service_id;
		ELSE
			select CONVERT_TZ(DATE_ADD(NOW(),INTERVAL concat(appt_delay_time_days,',',appt_delay_time_hrs) DAY_HOUR),'US/Central',time_zone) INTO sp_start_date from appt_sys_config;
			select DATE(CONVERT_TZ(DATE_ADD(NOW(),INTERVAL max_appt_duration_days DAY),'US/Central',time_zone)) INTO sp_end_date from appt_sys_config;
		END IF;
		
		BLOCK1: begin
			declare no_more_rows1 boolean DEFAULT FALSE;
			declare continue handler for not found set no_more_rows1 := TRUE;
			OPEN cur_resource_list;
			LOOP1: loop
				FETCH cur_resource_list INTO sp_res_id;
				if no_more_rows1 then
					close cur_resource_list;
					leave LOOP1;
				end if;
				
				SET res_id = sp_res_id;
				BLOCK2: begin
					declare no_more_rows2 boolean DEFAULT FALSE;
					declare continue handler for not found set no_more_rows2 := TRUE;
					
					OPEN cur_avail_dates;
					LOOP2: loop
						FETCH cur_avail_dates INTO sp_cur_avail_dates;
						if no_more_rows2 then
							close cur_avail_dates;
							leave LOOP2;
						end if;
						
						BLOCK3: begin
							declare no_more_rows3 boolean DEFAULT FALSE;
							declare continue handler for not found set no_more_rows3 := TRUE;
						
							OPEN cur_avail_times;
							SET timeArrayOfValue = '';
							LOOP3: loop
								FETCH cur_avail_times INTO sp_cur_avail_times;
								if no_more_rows3 then
									close cur_avail_times;
									leave LOOP3;
								end if;
								
								SET timeArrayOfValue = CONCAT(timeArrayOfValue, ',', CAST(sp_cur_avail_times as CHAR(8)));
							end loop LOOP3;
							
								SET timeArrayOfValue = CONCAT(SUBSTRING(timeArrayOfValue FROM 2),',');
								-- set time values as string array - '10:00,10:15,10:30,16:00,16:15,16:30,'
								
								-- filter only consecutive timeslots
								WHILE ((LOCATE(',', timeArrayOfValue) > 0) and (availability = 'N') )
								DO
									SET @timeStr = SUBSTRING(timeArrayOfValue, 1, LOCATE(',',timeArrayOfValue)-1);
									-- fetch first time '10:00'
									SET timeArrayOfValue = SUBSTRING(timeArrayOfValue, LOCATE(',', timeArrayOfValue) + 1);
									-- remaining values '10:15,10:30,16:00,16:15,16:30,'
												
									SET @sp_required_consective_time_str = @timeStr;
									-- first time it will be '10:00'
									SET @sp_given_consective_time_str = @timeStr;
									-- first time it will be '10:00'
									SET @timeStrTemp = timeArrayOfValue;
									-- first time it will be '10:15,10:30,16:00,16:15,16:30,'
									SET @sp_consecutive_time_iteration = 1;
									-- creates of string of required consecutive times for the given time (timeStr)
									
									WHILE (@sp_consecutive_time_iteration < sp_cur_ser_blocks)
									DO
										SET @sp_given_consective_time_str = concat(@sp_given_consective_time_str, ',', SUBSTRING(@timeStrTemp, 1, LOCATE(',',@timeStrTemp)-1));
										-- will set to '10:00,10:15'
										SET @timeStrTemp = SUBSTRING(@timeStrTemp, LOCATE(',', @timeStrTemp) + 1);
										-- remaining values '10:30,16:00,16:15,16:30,'
						
										SET @sp_required_consective_time_str= concat(@sp_required_consective_time_str, ',', TIME(DATE_ADD(CONCAT('1900-01-01 ' , @timeStr), INTERVAL (@sp_consecutive_time_iteration * block_time_in_mins) MINUTE)));
										SET @sp_consecutive_time_iteration = @sp_consecutive_time_iteration + 1;
									END WHILE;
									
									IF (@sp_required_consective_time_str = @sp_given_consective_time_str) THEN
										SET availability = 'Y';
									END IF;
								END WHILE;
								
								
						end BLOCK3;	
					end loop LOOP2;
				end BLOCK2;		
			end loop LOOP1;
		end BLOCK1;		
	END IF;
END$$
DELIMITER ;

DELIMITER $$
DROP PROCEDURE IF EXISTS `get_service_availability_callcenter_sp`$$
CREATE PROCEDURE `get_service_availability_callcenter_sp`(IN time_zone VARCHAR(50), IN `block_time_in_mins` INT(11), IN loc_id INT(10), IN ser_id INT(10), OUT availability CHAR(1), OUT error_msg VARCHAR(2000))
BEGIN
	DECLARE sp_start_date timestamp;DECLARE sp_end_date date;
	DECLARE cur_avail_dates date;
	DECLARE sp_max_appt_duration_days TINYINT(4);
	DECLARE sp_appt_delay_time_days TINYINT(4);
	DECLARE sp_appt_delay_time_hrs TINYINT(4);
	DECLARE sp_restrict_appt_window CHAR(1);
	DECLARE sp_appt_start_date DATE;
	DECLARE sp_appt_end_date DATE;
	DECLARE sp_restrict_loc_appt_window CHAR(1);
	DECLARE sp_restrict_ser_appt_window CHAR(1);
	DECLARE sp_restrict_loc_ser_appt_window CHAR(1);
	DECLARE sp_cur_ser_blocks TINYINT(4);
	DECLARE sp_cur_avail_dates DATE;
	DECLARE sp_cur_avail_times TIME;
	DECLARE timeArrayOfValue TEXT;
	DECLARE sp_res_id INT(10);
	DECLARE sp_service_id INT(10) DEFAULT 0;
	DECLARE res_id int(10) DEFAULT 0;
	DECLARE avail_date_times TEXT DEFAULT '';
	-- fetch all appt window configuration settings from appt_sys_config table
	DECLARE cur_appt_sys_config CURSOR FOR select max_appt_duration_days, appt_delay_time_days, appt_delay_time_hrs, restrict_appt_window, appt_start_date, appt_end_date, restrict_loc_appt_window, restrict_ser_appt_window, restrict_loc_ser_appt_window from appt_sys_config;
	DECLARE cur_ser_blocks CURSOR FOR select ser.id, ser.blocks from service ser where ser.id = ser_id;
	-- fetch distinct available dates filtered by holidays & closed days, and service availability days	
	DECLARE cur_avail_dates CURSOR  FOR select rc.date from (select DATE(date_time) "date" from resource_calendar rc  where date_time > DATE_ADD(CURDATE(), INTERVAL 1 day) and date_time >= sp_start_date and date_time < DATE_ADD(sp_end_date, INTERVAL 1 day) and resource_id = res_id and schedule_id = 0 group by DATE(date_time) ) rc where rc.date NOT IN (select h.date from holidays h where date >= DATE(sp_start_date) and date <= sp_end_date union select c.date from closed_days c where date >= DATE(sp_start_date) and date <= sp_end_date and location_id = loc_id) order by rc.date;
	-- fetch available times for that date filtered by resource_time_off (reserved) times
	DECLARE cur_avail_times CURSOR  FOR select TIME(rc.date_time) from resource_calendar rc where rc.resource_id = res_id and DATE(rc.date_time) = sp_cur_avail_dates and schedule_id = 0 and  (select count(1) from resource_time_off rt where rc.resource_id = rt.resource_id and TIME(rc.date_time) >= start_time and TIME(rc.date_time) < end_time and DATE(rc.date_time) = rt.start_date) = 0 order by TIME(rc.date_time);
	DECLARE cur_resource_list CURSOR  FOR select distinct res.id from resource res, resource_service rs where res.location_id = loc_id and res.delete_flag = 'N' and res.`enable` = 'Y' and res.allow_selfservice = 'Y' and res.id = rs.resource_id and rs.`enable` = 'Y' and rs.allow_selfservice = 'Y' and rs.service_id = sp_service_id order by res.placement;
	
	SET availability = 'N';
	OPEN cur_appt_sys_config;
		FETCH cur_appt_sys_config INTO sp_max_appt_duration_days,sp_appt_delay_time_days,sp_appt_delay_time_hrs,sp_restrict_appt_window,sp_appt_start_date,sp_appt_end_date,sp_restrict_loc_appt_window,sp_restrict_ser_appt_window,sp_restrict_loc_ser_appt_window;
	CLOSE cur_appt_sys_config;
	
	-- fetch number of blocks for the service_id
	OPEN cur_ser_blocks;
		FETCH cur_ser_blocks INTO sp_service_id,sp_cur_ser_blocks;
	CLOSE cur_ser_blocks;
	
	-- determine how to apply the appointment window based on basic window, location specific, service specific, location-service specific, or global settings
	IF(sp_restrict_appt_window = 'Y') THEN
		SET sp_start_date = sp_appt_start_date;
		SET sp_end_date = sp_appt_end_date;
	ELSEIF(sp_restrict_loc_appt_window = 'Y') THEN
		select appt_start_date, appt_end_date INTO sp_start_date, sp_end_date from location where id = loc_id;
	ELSEIF(sp_restrict_ser_appt_window = 'Y') THEN
		select appt_start_date, appt_end_date INTO sp_start_date, sp_end_date from service where id = sp_service_id;
	ELSEIF(sp_restrict_loc_ser_appt_window = 'Y') THEN
		select start_date, end_date INTO sp_start_date, sp_end_date from service_location where location_id = loc_id and service_id = sp_service_id;
	ELSE
		select CONVERT_TZ(DATE_ADD(NOW(),INTERVAL concat(appt_delay_time_days,',',appt_delay_time_hrs) DAY_HOUR),'US/Central',time_zone) INTO sp_start_date from appt_sys_config;
		select DATE(CONVERT_TZ(DATE_ADD(NOW(),INTERVAL max_appt_duration_days DAY),'US/Central',time_zone)) INTO sp_end_date from appt_sys_config;
	END IF;
	
	BLOCK1: begin
		declare no_more_rows1 boolean DEFAULT FALSE;
		declare continue handler for not found set no_more_rows1 := TRUE;
		OPEN cur_resource_list;
		LOOP1: loop
			FETCH cur_resource_list INTO sp_res_id;
			if no_more_rows1 then
				close cur_resource_list;
				leave LOOP1;
			end if;
			
			SET res_id = sp_res_id;
			BLOCK2: begin
				declare no_more_rows2 boolean DEFAULT FALSE;
				declare continue handler for not found set no_more_rows2 := TRUE;
				
				OPEN cur_avail_dates;
				LOOP2: loop
					FETCH cur_avail_dates INTO sp_cur_avail_dates;
					if no_more_rows2 then
						close cur_avail_dates;
						leave LOOP2;
					end if;
					
					BLOCK3: begin
						declare no_more_rows3 boolean DEFAULT FALSE;
						declare continue handler for not found set no_more_rows3 := TRUE;
					
						OPEN cur_avail_times;
						-- iterate through all available times and remove non-consecutive timeslots
						SET timeArrayOfValue = '';
						LOOP3: loop
							FETCH cur_avail_times INTO sp_cur_avail_times;
							if no_more_rows3 then
								close cur_avail_times;
								leave LOOP3;
							end if;
							
							SET timeArrayOfValue = CONCAT(timeArrayOfValue, ',', CAST(sp_cur_avail_times as CHAR(8)));
						end loop LOOP3;
				
						SET timeArrayOfValue = CONCAT(SUBSTRING(timeArrayOfValue FROM 2),',');
						-- set time values as string array - '10:00,10:15,10:30,16:00,16:15,16:30,'
						
						-- filter only consecutive timeslots
						WHILE ((LOCATE(',', timeArrayOfValue) > 0) and (availability = 'N') )
						DO
							SET @timeStr = SUBSTRING(timeArrayOfValue, 1, LOCATE(',',timeArrayOfValue)-1);
							-- fetch first time '10:00'
							SET timeArrayOfValue = SUBSTRING(timeArrayOfValue, LOCATE(',', timeArrayOfValue) + 1);
							-- remaining values '10:15,10:30,16:00,16:15,16:30,'
										
							SET @sp_required_consective_time_str = @timeStr;
							-- first time it will be '10:00'
							SET @sp_given_consective_time_str = @timeStr;
							-- first time it will be '10:00'
							SET @timeStrTemp = timeArrayOfValue;
							-- first time it will be '10:15,10:30,16:00,16:15,16:30,'
							SET @sp_consecutive_time_iteration = 1;
							-- creates of string of required consecutive times for the given time (timeStr)
							
							WHILE (@sp_consecutive_time_iteration < sp_cur_ser_blocks)
							DO
								SET @sp_given_consective_time_str = concat(@sp_given_consective_time_str, ',', SUBSTRING(@timeStrTemp, 1, LOCATE(',',@timeStrTemp)-1));
								-- will set to '10:00,10:15'
								SET @timeStrTemp = SUBSTRING(@timeStrTemp, LOCATE(',', @timeStrTemp) + 1);
								-- remaining values '10:30,16:00,16:15,16:30,'
				
								SET @sp_required_consective_time_str= concat(@sp_required_consective_time_str, ',', TIME(DATE_ADD(CONCAT('1900-01-01 ' , @timeStr), INTERVAL (@sp_consecutive_time_iteration * block_time_in_mins) MINUTE)));
								SET @sp_consecutive_time_iteration = @sp_consecutive_time_iteration + 1;
							END WHILE;
							
							IF (@sp_required_consective_time_str = @sp_given_consective_time_str) THEN
								SET availability = 'Y';
							END IF;
						END WHILE;
							
					end BLOCK3;	
				end loop LOOP2;
			end BLOCK2;		
		end loop LOOP1;
	end BLOCK1;		
END$$
DELIMITER ;


DELIMITER $$
DROP PROCEDURE IF EXISTS `get_first_avail_date_times_any_location_sp`$$
CREATE PROCEDURE `get_first_avail_date_times_any_location_sp`(IN time_zone VARCHAR(50), OUT result_str TEXT, OUT error_msg VARCHAR(2000))
BEGIN
	-- This stored procedure fetches the first available date and its timeslots for that date using below filters:
	-- * select available dates > now() and only within the appointment window dates/time
	-- * remove holidays & closed days
	-- * remove days (Sun - Sat) for which service is not opened
	-- * remove timeslots reserved for the resource_id for that date
	-- * remove non-consecutive timeslots
	-- * Return data in format '2016-11-01 08:15:00|Tuesday, November 1 2016 at 8:15 AM|11/01/2016|08:15 AM|NW Neighborhood Place at the Academy of Shawnee|4018 W. Market Street|Louisville|40212|NW Neighborhood Place at the Academy of Shawnee|NW Neighborhood Place at the Academy of Shawnee|19|LIHEAP Subsidy|LIHEAP|liheap_utility_assistance'
	
	DECLARE sp_start_date timestamp;
	DECLARE sp_end_date date;
	DECLARE cur_avail_dates date;
	DECLARE ser_id TINYINT(4);
	
	DECLARE sp_max_appt_duration_days TINYINT(4);
	DECLARE sp_appt_delay_time_days TINYINT(4);
	DECLARE sp_appt_delay_time_hrs TINYINT(4);
	DECLARE sp_restrict_appt_window CHAR(1);
	DECLARE sp_appt_start_date DATE;
	DECLARE sp_appt_end_date DATE;
	DECLARE sp_restrict_loc_appt_window CHAR(1);
	DECLARE sp_restrict_ser_appt_window CHAR(1);
	DECLARE sp_restrict_loc_ser_appt_window CHAR(1);
	
	-- fetch all appt window configuration settings from appt_sys_config table
	DECLARE cur_appt_sys_config CURSOR FOR select max_appt_duration_days, appt_delay_time_days, appt_delay_time_hrs, restrict_appt_window, appt_start_date, appt_end_date, restrict_loc_appt_window, restrict_ser_appt_window, restrict_loc_ser_appt_window from appt_sys_config;
	-- fetch distinct available dates filtered by holidays & closed days, and service availability days	
	DECLARE cur_first_avail_date CURSOR  FOR select CONCAT_WS('|',date_time,DATE_FORMAT(date_time, '%W, %M %e %Y at %l:%i %p'), DATE_FORMAT(date_time, '%m/%d/%Y'), DATE_FORMAT(date_time, '%h:%i %p'), l.id, l.location_name_online, IFNULL(l.address, ''), IFNULL(l.city, ''), IFNULL(l.zip,''), l.location_name_ivr_tts, l.location_name_ivr_audio, rc.resource_id, s.id, s.service_name_online, s.service_name_ivr_tts, s.service_name_ivr_audio) from resource_calendar rc LEFT OUTER JOIN resource r ON rc.resource_id = r.id LEFT OUTER JOIN location l ON r.location_id = l.id LEFT OUTER JOIN service s ON s.id = 1 LEFT OUTER JOIN resource_service rs ON rs.resource_id = r.id and rs.service_id = s.id where rc.date_time > DATE_ADD(CURDATE(), INTERVAL 1 day) and rc.date_time >= sp_start_date and rc.date_time < DATE_ADD(sp_end_date, INTERVAL 1 day) and rc.schedule_id = 0 and r.enable = 'Y' and r.delete_flag = 'N' and r.allow_selfservice = 'Y' and rs.enable = 'Y' and rs.allow_selfservice = 'Y' and s.enable = 'Y' and s.delete_flag = 'N' and DATE(rc.date_time) NOT IN (select h.date from holidays h where date >= DATE(sp_start_date) and date <= sp_end_date union select c.date from closed_days c where date >= DATE(sp_start_date) and date <= sp_end_date and location_id = l.id) and 'Y' = 
	(
	SELECT CASE DATE_FORMAT(rc.date_time,'%w')
	WHEN 0 THEN is_sun_open
	WHEN 1 THEN is_mon_open
	WHEN 2 THEN is_tue_open
	WHEN 3 THEN is_wed_open
	WHEN 4 THEN is_thu_open
	WHEN 5 THEN is_fri_open
	WHEN 6 THEN is_sat_open
	END AS days
	FROM service where id = ser_id
	) order by rc.date_time LIMIT 1;
	
	
	SET ser_id = 1;
	
	OPEN cur_appt_sys_config;
		FETCH cur_appt_sys_config INTO sp_max_appt_duration_days,sp_appt_delay_time_days,sp_appt_delay_time_hrs,sp_restrict_appt_window,sp_appt_start_date,sp_appt_end_date,sp_restrict_loc_appt_window,sp_restrict_ser_appt_window,sp_restrict_loc_ser_appt_window;
	CLOSE cur_appt_sys_config;
	
	-- determine how to apply the appointment window based on basic window, location specific, service specific, location-service specific, or global settings
	IF(sp_restrict_appt_window = 'Y') THEN
		SET sp_start_date = sp_appt_start_date;
		SET sp_end_date = sp_appt_end_date;
	ELSEIF(sp_restrict_loc_appt_window = 'Y') THEN
		select appt_start_date, appt_end_date INTO sp_start_date, sp_end_date from location where id = 1;
	ELSEIF(ser_id > 0 and sp_restrict_ser_appt_window = 'Y') THEN
		select appt_start_date, appt_end_date INTO sp_start_date, sp_end_date from service where id = ser_id;
	ELSEIF(ser_id > 0 and sp_restrict_loc_ser_appt_window = 'Y') THEN
		select start_date, end_date INTO sp_start_date, sp_end_date from service_location where location_id = 1 and service_id = ser_id;
	ELSE
		select CONVERT_TZ(DATE_ADD(NOW(),INTERVAL concat(appt_delay_time_days,',',appt_delay_time_hrs) DAY_HOUR),'US/Central',time_zone) INTO sp_start_date from appt_sys_config;
		select DATE(CONVERT_TZ(DATE_ADD(NOW(),INTERVAL max_appt_duration_days DAY),'US/Central',time_zone)) INTO sp_end_date from appt_sys_config;
	END IF;
	
	OPEN cur_first_avail_date;
		FETCH cur_first_avail_date INTO result_str;
	close cur_first_avail_date;
END$$
DELIMITER ;



DELIMITER $$
DROP PROCEDURE IF EXISTS `hold_appointment_sp`$$
CREATE PROCEDURE `hold_appointment_sp`(IN `appt_date_time` VARCHAR(60), IN `block_time_in_mins` INT(11), IN `loc_id` INT(10), IN `res_id` INT(10), IN proc_id INT(10), IN dept_id INT(10), IN `ser_id` INT(10), IN `cust_id` BIGINT(20), IN `trans_id` BIGINT(20), IN device VARCHAR(10), OUT `sched_id` BIGINT(20), OUT `error_msg` VARCHAR(2000), OUT `display_datetime` VARCHAR(200))
BEGIN
	DECLARE done INT DEFAULT 0;
	DECLARE rows_count INT DEFAULT 0;
	DECLARE no_blocks INT DEFAULT 0;
	DECLARE dups_appt CHAR(1) DEFAULT 'N'; -- no duplicates by default
	DECLARE dups_appt_count INT DEFAULT 0;
	DECLARE dups_hold_count INT DEFAULT 0;
	DECLARE sp_res_id INT(10);

	DECLARE cur_resource_list CURSOR  FOR select distinct r.id from location_department_resource ldr, resource r, resource_service rs, service s where r.id=res_id and ldr.location_id = loc_id and ldr.department_id = dept_id and ldr.enable = 'Y' and ldr. resource_id = r.id and r.enable = 'Y' and r.delete_flag = 'N' and r.allow_selfservice = 'Y' and r.id = rs.resource_id and rs.service_id = ser_id and rs.enable = 'Y' and rs.allow_selfservice = 'Y' and rs.service_id = s.id and s.enable = 'Y' and s.delete_flag = 'N' order by r.placement;
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
	
	START TRANSACTION;
	
	select allow_duplicate_appt INTO dups_appt from service where id=ser_id;
	
	select count(s.`id`) INTO dups_appt_count from `schedule` s, customer c where c.`id` = cust_id and s.customer_id = c.`id` and s.appt_date_time > now() and s.`status` = 11;
	
	select count(s.`id`) INTO dups_hold_count from `schedule` s, customer c where c.`id` = cust_id and s.customer_id = c.`id` and s.appt_date_time > now() and  s.`status` = 1;
	
	SET sched_id = 1;
	
	IF(dept_id = NULL or dept_id = 0) THEN
		SET dept_id = 1;
	END IF;
	IF(proc_id = NULL or proc_id = 0) THEN
		SET proc_id = 1;
	END IF;
	
	IF(dups_appt = 'N' and dups_appt_count > 0 and cust_id > 0) THEN
		SET error_msg = 'DUPLICATE_APPT';
	ELSE
		 IF(dups_appt = 'N' and dups_hold_count > 0 and cust_id > 0) THEN
		 	SET error_msg = 'HOLD_NOT_RELEASED';
		 ELSE
			SET done = 0;
			OPEN cur_resource_list;
			REPEAT
				FETCH cur_resource_list INTO sp_res_id;
				IF (done=0) THEN
					select `blocks` INTO no_blocks from service where id=ser_id;
			
					-- find out timeslots are still available for that date/time and resourceId
					select count(1) INTO rows_count from resource_calendar where date_time >= appt_date_time and date_time < DATE_ADD(appt_date_time, INTERVAL (no_blocks * block_time_in_mins) MINUTE) and resource_id=sp_res_id and schedule_id=0;
			
					IF(rows_count = no_blocks) THEN
						INSERT INTO schedule (timestamp,status,appt_date_time,blocks,trans_id,procedure_id,location_id,department_id,resource_id,service_id,customer_id,updated_by) values (CONVERT_TZ(now(),'US/Central','US/Pacific'),1,appt_date_time,no_blocks,trans_id,proc_id,loc_id,dept_id,sp_res_id,ser_id,cust_id,concat('book:',lower(device),'@',DATE_FORMAT(CONVERT_TZ(now(),'US/Central','US/Pacific'),'%m/%d/%Y %h:%i %p')));
						SELECT LAST_INSERT_ID() INTO sched_id;
						update resource_calendar set schedule_id=sched_id where date_time >= appt_date_time and date_time < DATE_ADD(appt_date_time, INTERVAL (no_blocks * block_time_in_mins) MINUTE) and resource_id=sp_res_id;
						COMMIT;
			
						select DATE_FORMAT(appt_date_time, online_datetime_display) INTO display_datetime from appt_sys_config;
						SET done = 1;
					END IF;
				END IF;
			UNTIL done END REPEAT;
			close cur_resource_list;
			
			IF(sched_id = 1) THEN
				SET error_msg = 'SELECTED_DATE_TIME_NOT_AVAILABLE';
			END IF;
					
	  END IF;
   END IF;
END$$
DELIMITER ;



DELIMITER $$
DROP PROCEDURE IF EXISTS `get_avail_times_sp`$$
CREATE PROCEDURE `get_avail_times_sp`(IN loc_id INT(10), IN dept_id INT(10),res_id INT(10), IN ser_id INT(10), IN block_time_in_mins INT(11), IN avail_date DATE, OUT avail_date_times TEXT, OUT error_msg VARCHAR(2000))
BEGIN
	-- This stored procedure fetches the available timeslots for supplied date
	-- * remove timeslots reserved for the resource_id for that date
	-- * remove non-consecutive timeslots
	-- * Return data in format 'DATE|RES_ID1-TIME1,TIME2,TIME3|RES_ID2-TIME1,TIME2,TIME3'
	-- * Return data as '2015-05-24|1-10:00,10:15,16:00,16:15|2-09:30,11:15'
	
	DECLARE sp_cur_ser_blocks TINYINT(4);
	DECLARE sp_cur_avail_times TIME;
	DECLARE timeArrayOfValue TEXT;
	DECLARE sp_res_id INT(10);
	DECLARE length_consective_time TINYINT(4);
	DECLARE TIMESLOT_FOUND TINYINT(4) DEFAULT 0;
	DECLARE temp_avail_date_times TEXT DEFAULT '';
	DECLARE avail_date_times_sp TEXT DEFAULT '';
	
	-- fetch all appt window configuration settings from appt_sys_config table
	DECLARE cur_appt_sys_config CURSOR FOR select max_appt_duration_days, appt_delay_time_days, appt_delay_time_hrs, restrict_appt_window, appt_start_date, appt_end_date, restrict_loc_appt_window, restrict_ser_appt_window, restrict_loc_ser_appt_window from appt_sys_config;
	DECLARE cur_ser_blocks CURSOR FOR select blocks from service where id = ser_id;
	-- fetch distinct available dates filtered by holidays & closed days, and service availability days	
	DECLARE cur_resource_list CURSOR  FOR select distinct r.id from location_department_resource ldr, resource r, resource_service rs, service s where ldr.location_id = loc_id and r.id=res_id and ldr.department_id = dept_id and ldr.enable = 'Y' and ldr. resource_id = r.id and r.enable = 'Y' and r.delete_flag = 'N' and r.allow_selfservice = 'Y' and r.id = rs.resource_id and rs.service_id = ser_id and rs.enable = 'Y' and rs.allow_selfservice = 'Y' and rs.service_id = s.id and s.enable = 'Y' and s.delete_flag = 'N' order by r.placement;
	-- fetch available times for that date filtered by resource_time_off (reserved) times
	DECLARE cur_avail_times CURSOR  FOR select TIME(rc.date_time) from resource_calendar rc where rc.resource_id = sp_res_id and DATE(rc.date_time) = avail_date and schedule_id = 0 and  (select count(1) from resource_time_off rt where rc.resource_id = rt.resource_id and TIME(rc.date_time) >= start_time and TIME(rc.date_time) < end_time and DATE(rc.date_time) = rt.start_date) = 0 order by TIME(rc.date_time);
	
	IF(dept_id = NULL or dept_id = 0) THEN
		SET dept_id = 1;
	END IF;
	
	-- fetch number of blocks for the service_id
	OPEN cur_ser_blocks;
		FETCH cur_ser_blocks INTO sp_cur_ser_blocks;
	CLOSE cur_ser_blocks;
	
	
	BLOCK1: begin
		declare no_more_rows1 boolean DEFAULT FALSE;
		declare continue handler for not found set no_more_rows1 := TRUE;
		OPEN cur_resource_list;
		
		LOOP1: loop
			FETCH cur_resource_list INTO sp_res_id;
			if no_more_rows1 then
				close cur_resource_list;
				leave LOOP1;
			end if;
			
				BLOCK2: begin
					declare no_more_rows2 boolean DEFAULT FALSE;
					declare continue handler for not found set no_more_rows2 := TRUE;
						OPEN cur_avail_times;
						-- iterate through all available times and remove non-consecutive timeslots
						SET timeArrayOfValue = '';
					
						LOOP2: loop
							FETCH cur_avail_times INTO sp_cur_avail_times;
							if no_more_rows2 then
								close cur_avail_times;
								leave LOOP2;
							end if;
							
							SET timeArrayOfValue = CONCAT(timeArrayOfValue, ',', CAST(sp_cur_avail_times as CHAR(8)));
						end loop LOOP2;	
						
						SET timeArrayOfValue = CONCAT(SUBSTRING(timeArrayOfValue FROM 2),',');
						-- set time values as string array - '10:00,10:15,10:30,16:00,16:15,16:30,'
						
						SET temp_avail_date_times = '';
						SET length_consective_time = 1;
						-- filter only consecutive timeslots
						WHILE (LOCATE(',', timeArrayOfValue) > 0)
						DO
							SET timeArrayOfValue = SUBSTRING(timeArrayOfValue, length_consective_time);
							SET @timeStr = SUBSTRING(timeArrayOfValue, 1, LOCATE(',',timeArrayOfValue)-1); -- fetch first time '10:00'
							SET timeArrayOfValue = SUBSTRING(timeArrayOfValue, LOCATE(',', timeArrayOfValue) + 1); -- remaining values '10:15,10:30,16:00,16:15,16:30,'
										
							SET @sp_required_consective_time_str = @timeStr; -- first time it will be '10:00'
							SET @sp_given_consective_time_str = @timeStr; -- first time it will be '10:00'
							SET @timeStrTemp = timeArrayOfValue; -- first time it will be '10:15,10:30,16:00,16:15,16:30,'
							SET @sp_consecutive_time_iteration = 1;
							-- creates of string of required consecutive times for the given time (timeStr)
							
							SET length_consective_time = 1;
							WHILE (@sp_consecutive_time_iteration < sp_cur_ser_blocks)
							DO
								SET @sp_given_consective_time_str = concat(@sp_given_consective_time_str, ',', SUBSTRING(@timeStrTemp, 1, LOCATE(',',@timeStrTemp)-1)); -- will set to '10:00,10:15'
								SET @timeStrTemp = SUBSTRING(@timeStrTemp, LOCATE(',', @timeStrTemp) + 1); -- remaining values '10:30,16:00,16:15,16:30,'
				
								SET @sp_required_consective_time_str= concat(@sp_required_consective_time_str, ',', TIME(DATE_ADD(CONCAT('1900-01-01 ' , @timeStr), INTERVAL (@sp_consecutive_time_iteration * block_time_in_mins) MINUTE)));
											
								SET @sp_consecutive_time_iteration = @sp_consecutive_time_iteration + 1;
							END WHILE;
										
							IF (@sp_required_consective_time_str = @sp_given_consective_time_str) THEN
								IF (@sp_consecutive_time_iteration > 1) THEN
									SET length_consective_time = (@sp_consecutive_time_iteration - 1) * 9;
								END IF;
								SET temp_avail_date_times = CONCAT(temp_avail_date_times, ',', @timeStr);
							END IF;
										
						END WHILE;

						IF (LENGTH(temp_avail_date_times) > 1) THEN
							SET TIMESLOT_FOUND = TIMESLOT_FOUND + 1;
							IF (TIMESLOT_FOUND = 1) THEN
								SET avail_date_times_sp = CONCAT(avail_date, '|', sp_res_id, '-', SUBSTRING(temp_avail_date_times, 2));
							ELSE
								SET avail_date_times_sp = CONCAT(avail_date_times_sp, '|', sp_res_id, '-', SUBSTRING(temp_avail_date_times, 2));
							END IF;
						END IF;
				end BLOCK2;		
		end loop LOOP1;
	end BLOCK1;		
	
	IF (LENGTH(avail_date_times_sp) > 1) THEN
		SET avail_date_times = avail_date_times_sp;
	ELSE
		SET avail_date_times = 'NO_AVAIL_TIMESLOTS';
	END IF;

END$$
DELIMITER ;


DELIMITER $$
DROP PROCEDURE IF EXISTS `get_mobile_customer_info_sp`$$
CREATE PROCEDURE `get_mobile_customer_info_sp`(IN `cust_id` BIGINT(20))
BEGIN
	DECLARE myval VARCHAR(100);
	DECLARE done INT DEFAULT 0;
	DECLARE sp_param_column varchar(1000);
	DECLARE sp_java_reflection varchar(1000);
	DECLARE sp_param_type varchar(1000);
	DECLARE sp_field_name varchar(1000);
	DECLARE sp_display_type varchar(1000);
	DECLARE cur_online_page_fields CURSOR  FOR select `param_column`, `java_reflection`, `param_type`, `field_name`, `display_type` FROM `login_param_config` WHERE device_type='mobile' and param_table = 'customer' order by placement;
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
	
	SET @cust_sql = '';
	SET @cust_sql_java_refl = '';
	SET @cust_sql_col_type = '';
	SET @cust_sql_field_name = '';
	SET @cust_sql_display_type = '';
	SET done = 0;
	
	OPEN cur_online_page_fields;
	REPEAT
	FETCH cur_online_page_fields INTO sp_param_column, sp_java_reflection, sp_param_type, sp_field_name, sp_display_type;
	IF (done=0) THEN
		SET @cust_sql = CONCAT(@cust_sql, ',', sp_param_column);
		SET @cust_sql_java_refl = CONCAT(@cust_sql_java_refl, ',', sp_java_reflection);
		SET @cust_sql_col_type = CONCAT(@cust_sql_col_type, ',', sp_param_type);
		SET @cust_sql_field_name = CONCAT(@cust_sql_field_name, ',', sp_field_name);
		SET @cust_sql_display_type = CONCAT(@cust_sql_display_type, ',', sp_display_type);
	END IF;
	UNTIL done END REPEAT;
	CLOSE cur_online_page_fields;
	
	SET @cust_sql = SUBSTRING(@cust_sql, 2);
	SET @cust_sql_java_refl = SUBSTRING(@cust_sql_java_refl, 2);
	SET @cust_sql_col_type = SUBSTRING(@cust_sql_col_type, 2);
	SET @cust_sql_field_name = SUBSTRING(@cust_sql_field_name, 2);
	SET @cust_sql_display_type = SUBSTRING(@cust_sql_display_type, 2);
	SET @s = CONCAT('select ''',@cust_sql,''' AS "Key",''',@cust_sql_java_refl,''' AS "JavaRef",''',@cust_sql_col_type,''' AS "Type",''',@cust_sql_field_name,''' AS "Field",''',@cust_sql_display_type,''' AS "DisplayType",',@cust_sql,' from customer where id = ',cust_id);
	
	PREPARE stmt1 FROM @s;
	EXECUTE stmt1;
	DEALLOCATE PREPARE stmt1;
END$$
DELIMITER ;


