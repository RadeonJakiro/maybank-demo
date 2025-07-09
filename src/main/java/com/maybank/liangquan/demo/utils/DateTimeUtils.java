package com.maybank.liangquan.demo.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateTimeUtils {

	public static LocalDateTime convertDateToLocalDateTime(Date dateToConvert) {
		Instant instant = dateToConvert.toInstant();
		ZoneId systemZone = ZoneId.systemDefault();
		return LocalDateTime.ofInstant(instant, systemZone);
	}

}
