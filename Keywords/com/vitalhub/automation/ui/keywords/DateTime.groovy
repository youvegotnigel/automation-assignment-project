package com.vitalhub.automation.ui.keywords

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.TimeZone

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.keyword.datetime.DateTimeUtility

import groovy.transform.CompileStatic
import internal.GlobalVariable

public class DateTime  extends DateTimeUtility{

	private static String timeZone = GlobalVariable.TIME_ZONE
	private static String dateFormat = GlobalVariable.DATE_FORMAT

	/**
	 * Get date time formatted value
	 *
	 * @param text String to check if it's a date time to get Date/Time
	 * @return the Date/Time value
	 */
	@CompileStatic
	@Keyword
	public static String formatIfDateTime(String text) {

		switch (text) {
			case 'today':
				return getCurrentDateTime(timeZone, dateFormat)

			case 'current-day':
				return getCurrentDateTime(timeZone, 'yyyy-MM-dd')

			case 'next-day':
				return getFutureDateTime(1, timeZone, 'yyyy-MM-dd')

			case 'previous':
				return getPreviousDateTime(1, timeZone, 'dd MMM yyyy')

			case 'yesterday':
				return getPreviousDateTime(1, timeZone, 'yyyy-MM-dd')

			case 'last year':
				return getPreviousYear(1);

			case ~/^yesterday\+\d{1,2}$/:
				def date = text.split("\\+")
				return getPreviousDateTime(date[1] as int, timeZone, 'yyyy-MM-dd')

			case ~/^today\-\d{1,3}$/:
				def date = text.split("\\-")
				return getPreviousDateTime(date[1] as int, timeZone, dateFormat)

			case 'NQ_Day':
				return getPreviousDateTime(1, timeZone, 'EEEE')

			case 'future':
				return getFutureDateTime(1, timeZone, 'dd MMM yyyy')

			case ~/^today\+\d{1,3}$/:
				def futuredate = text.split("\\+")
				return getFutureDateTime(futuredate[1] as int, timeZone, dateFormat)

			case 'today yyyy MM dd':
				return getCurrentDateTime(timeZone, 'yyyy MM dd')

			case 'today yyyy-MM-dd HH:mm':
				return getCurrentDateTime(timeZone, 'yyyy-MM-dd HH:mm')

			case 'today d':
				return getCurrentDateTime(timeZone, 'd')

			case 'now HH:mm:ss':
				return getCurrentDateTime(timeZone, 'HH:00:00')

			case 'now':
				return getCurrentDateTime(timeZone, 'hh:mm a')

			case 'now h:mm':
				return getCurrentDateTime(timeZone, 'h:mm')

			case 'today+now':
				return getCurrentDateTime(timeZone, 'dd MMM yyyy HH:mm')

			case 'today+now+time':
				return getCurrentDateTime(timeZone, 'dd MMM yyyy hh:mm a')

			case 'now HH':
				return getCurrentDateTime(timeZone, 'HH')

			case ~/^now HH\+\d$/:
				def change = text.split('now HH')
				return getFutureTime((change[1] as int), 0, 'HH')

			case ~/^now HH\-\d$/:
				def change = text.split('now HH')
				return getPastTime((change[1] as int), 0, 'HH')

			case ~/^current_time\+\d{1,2}$/:
				def change = text.split('current_time')
				return getFutureTime((change[1] as int), 0, 'YYYYMMDDhhmmss')

			case ~/^current_time\-\d{1,2}$/:
				def change = text.split('current_time')
				return getPastTime((change[1] as int), 0, 'YYYYMMDDhhmmss')

			case 'now hh':
				return getCurrentDateTime(timeZone, 'hh')

			case 'now mm':
				return getCurrentDateTime(timeZone, 'mm')

			case 'now ss':
				return getCurrentDateTime(timeZone, 'ss')

			case ~/^now mm\+\d$/:
				def change = text.split('now mm')
				return getFutureTime(0, (change[1] as int), 'mm')

			case ~/^now mm\-\d$/:
				def change = text.split('now mm')
				return getPastTime(0, (change[1] as int), 'mm')

			case ~/^Month Year\+\d$/:
				def change = text.split('Month Year')
				return getFutureDate(timeZone, (change[1] as int), 0, 'MMM yyyy')

			case ~/^Month Year\-\d$/:
				def change = text.split('Month Year')
				return getPastDate(timeZone, (change[1] as int), 0, 'MMM yyyy')

			case 'now m0':
				return getCurrentDateTime(timeZone, 'm0')

			case 'today_date':
				return getWeekMonthFormat(timeZone)

			case 'last_wednessday_date':
				return getLastWeekDate(timeZone, 'dd MMM yyyy', Calendar.WEDNESDAY)

			case 'first day current month':
				return getFirstDateOfCurrentMonth(timeZone, 'yyyy-MM-dd')

			case 'first day this month':
				return getFirstDateOfCurrentMonth(timeZone, 'dd MMM yyyy')

			case 'first day previous month':
				return getFirstDateOfPreviousMonth(timeZone, 'dd MMM yyyy')

			case 'last day previous month':
				return getLastDateOfPreviousMonth(timeZone, 'dd MMM yyyy')

			case 'last day last month':
				return getLastDateOfPreviousMonth(timeZone, 'dd MMM yyyy')

			case 'first day current year':
				return getFirstDateOfCurrentYear(timeZone, 'yyyy-MM-dd');

			case 'first day this year':
				return getFirstDateOfCurrentYear(timeZone, 'dd MMM yyyy')

			case 'first day previous year':
				return getFirstDateOfPreviousYear(timeZone, 'yyyy-MM-dd')

			case 'last day previous year':
				return getLastDateOfPreviousYear(timeZone, 'yyyy-MM-dd')

			case 'last day last year':
				return getLastDateOfPreviousYear(timeZone, 'dd MMM yyyy')

			case 'last 12 months date':
				return getFirstDateOfLastNMonths(timeZone, 'dd MMM yyyy', 12)

			case 'last 12 month':
				return getFirstDateOfLastNMonths(timeZone, 'MMM yyyy', 12)

			case 'last 24 months date':
				return getFirstDateOfLastNMonths(timeZone, 'dd MMM yyyy', 24)

			case 'last 24 month':
				return getFirstDateOfLastNMonths(timeZone, 'MMM yyyy', 24)

			case 'last 36 months date':
				return getFirstDateOfLastNMonths(timeZone, 'dd MMM yyyy', 36)

			case 'last 36 month':
				return getFirstDateOfLastNMonths(timeZone, 'MMM yyyy', 36)

			default:
				return text
		}
	}

	/**
	 * Get date time formatted value
	 *
	 * @param text String to check if it's a date time to get Date/Time
	 * @param format format for date time to get Date/Time
	 * @return the Date/Time value
	 */
	@CompileStatic
	@Keyword
	public static String formatIfDateTime(String text, String format, String timeZoneId=null) {
		switch (text) {
			case 'today+now':
				return getCurrentDateTime(timeZoneId, format)
			default:
				return getCurrentDateTime(timeZoneId, format)
		}
	}

	/**
	 * Get first date of current month
	 *
	 * @param timeZone time zone used in the application
	 * @param format the date time format. If the dateTimeFormat is null, the default format set to "dd-MM-yyyy hh:mm:ss a"
	 * @return the first date of current month
	 */
	@CompileStatic
	@Keyword
	public static String getFirstDateOfCurrentMonth(String timeZone, String format) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format)
		ZoneId zoneId = ZoneId.of(timeZone)
		LocalDateTime now = LocalDateTime.now(zoneId).withDayOfMonth(1)
		return dtf.format(now)
	}

	/**
	 * Get first date of previous month
	 *
	 * @param timeZone time zone used in the application
	 * @param format the date time format. If the dateTimeFormat is null, the default format set to "dd-MM-yyyy hh:mm:ss a"
	 * @return the first date of previous month
	 */
	@CompileStatic
	@Keyword
	public static String getFirstDateOfPreviousMonth(String timeZone, String format) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format)
		ZoneId zoneId = ZoneId.of(timeZone)
		YearMonth yearMonthNow = YearMonth.now(zoneId)
		YearMonth yearMonthPrevious = yearMonthNow.minusMonths(1)
		LocalDate firstOfMonth = yearMonthPrevious.atDay(1)

		return dtf.format(firstOfMonth)
	}

	/**
	 * Get first date of current year
	 *
	 * @param timeZone time zone used in the application
	 * @param format the date time format. If the dateTimeFormat is null, the default format set to "dd-MM-yyyy hh:mm:ss a"
	 * @return the first date of current year
	 */
	@CompileStatic
	@Keyword
	public static String getFirstDateOfCurrentYear(String timeZone, String format) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format)
		ZoneId zoneId = ZoneId.of(timeZone)
		LocalDateTime now = LocalDateTime.now(zoneId).withDayOfYear(1)
		return dtf.format(now)
	}

	/**
	 * Get first date of previous year
	 *
	 * @param timeZone time zone used in the application
	 * @param format the date time format. If the dateTimeFormat is null, the default format set to "dd-MM-yyyy hh:mm:ss a"
	 * @return the first date of previous year
	 */
	@CompileStatic
	@Keyword
	public static String getFirstDateOfPreviousYear(String timeZone, String format) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format)
		ZoneId zoneId = ZoneId.of(timeZone)
		YearMonth yearMonthNow = YearMonth.now(zoneId)
		YearMonth yearMonthPrevious = yearMonthNow
		LocalDate firstOfYear = yearMonthPrevious.atDay(1)

		return dtf.format(firstOfYear)
	}

	/**
	 * Get last date of previous year
	 *
	 * @param timeZone timeZone used in the application
	 * @param format the date time format. If the dateTimeFormat is null, the default format set to "dd-MM-yyyy hh:mm:ss a"
	 * @return the last date of previous year
	 */
	@CompileStatic
	@Keyword
	public static String getLastDateOfPreviousYear(String timeZone, String format) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format)
		ZoneId zoneId = ZoneId.of(timeZone)
		YearMonth yearMonthNow = YearMonth.now(zoneId)
		YearMonth yearMonthPrevious = yearMonthNow.minusYears(1)
		LocalDate firstOfYear = yearMonthPrevious.atEndOfMonth()

		return dtf.format(firstOfYear)
	}

	/**
	 * Get last date of previous month
	 *
	 * @param timeZone time zone used in the application
	 * @param format the date time format. If the dateTimeFormat is null, the default format set to "dd-MM-yyyy hh:mm:ss a"
	 * @return the last date of previous month
	 */
	@CompileStatic
	@Keyword
	public static String getLastDateOfPreviousMonth(String timeZone, String format) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format)
		ZoneId zoneId = ZoneId.of(timeZone)
		YearMonth yearMonthNow = YearMonth.now(zoneId)
		YearMonth yearMonthPrevious = yearMonthNow.minusMonths(1)
		LocalDate lastOfMonth  = yearMonthPrevious.atEndOfMonth()

		return dtf.format(lastOfMonth)
	}

	/**
	 * Get first date of last n months
	 *
	 * @param timeZone time zone used in the application
	 * @param format the date time format. If the dateTimeFormat is null, the default format set to "dd-MM-yyyy hh:mm:ss a"
	 * @return the first date of last n months
	 */
	@CompileStatic
	@Keyword
	public static String getFirstDateOfLastNMonths(String timeZone, String format, int n) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format)
		ZoneId zoneId = ZoneId.of(timeZone)
		YearMonth yearMonthNow = YearMonth.now(zoneId)
		YearMonth yearMonthPrevious = yearMonthNow.minusMonths(n)
		LocalDate firstOfMonth  = yearMonthPrevious.atDay(1)

		return dtf.format(firstOfMonth)
	}

	/**
	 * Get suffix of the day of month
	 *
	 * @param n day
	 * @return the suffix of the day of month
	 */
	@CompileStatic
	@Keyword
	public static String getDayOfMonthSuffix(final int n) {

		if (n >= 11 && n <= 13) {
			return n+"th"
		}
		switch (n % 10) {
			case 1:  return n+"st"
			case 2:  return n+"nd"
			case 3:  return n+"rd"
			default: return n+"th"
		}
	}


	/**
	 * Get getWeekMonthFormat
	 *
	 * @param timeZone time zone used in the application
	 * @return date with suffix of the day of month
	 */
	@CompileStatic
	@Keyword
	public static String getWeekMonthFormat(String timeZone){

		DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("dd")
		DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("EEE dd MMM")
		ZoneId zoneId = ZoneId.of(timeZone)

		LocalDateTime ldt_day = LocalDateTime.now(zoneId)
		LocalDateTime ldt_date = LocalDateTime.now(zoneId)

		String suffix = getDayOfMonthSuffix(Integer.parseInt(dtf1.format(ldt_day)))

		String s = dtf2.format(ldt_date)

		return s.replaceAll("\\d{1,2}", suffix)
	}

	/**
	 * Get getLastWeekDate
	 *
	 * @param timeZone time zone used in the application
	 * @param format the date time format
	 * @return dow day of the week
	 */
	@CompileStatic
	@Keyword
	public static String getLastWeekDate(String timeZone, String format, int dow){

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format)
		ZoneId zoneId = ZoneId.of(timeZone)
		Calendar date = Calendar.getInstance(TimeZone.getTimeZone(timeZone))

		int diff = dow - date.get(Calendar.DAY_OF_WEEK)

		if (diff >= 0) {
			diff -= 7
		}
		date.add(Calendar.DAY_OF_WEEK, diff)

		LocalDateTime ldt = LocalDateTime.ofInstant(date.toInstant(), zoneId)

		return dtf.format(ldt)
	}


	/**
	 * Get previous year
	 *
	 * @return previous year
	 */
	@CompileStatic
	@Keyword
	public static String getPreviousYear(int noOfYears) {
		Calendar now = Calendar.getInstance()
		return String.valueOf(now.get(1) - noOfYears)
	}

	@Keyword
	public static String getFutureTime(int hrs, int minute, String format) {
		ZoneId zoneId = ZoneId.of(timeZone)
		KeywordUtil.logInfo("The current TimeZone ID is: " + zoneId.getId())
		def time = LocalDateTime.now(zoneId).plusHours(hrs).plusMinutes(minute).format(DateTimeFormatter.ofPattern(format))
		return time
	}

	@Keyword
	public static String getPastTime(int hrs, int minute, String format) {
		ZoneId zoneId = ZoneId.of(timeZone)
		KeywordUtil.logInfo("The current TimeZone ID is: " + zoneId.getId())
		def time = LocalDateTime.now(zoneId).minusHours(hrs).minusMinutes(minute).format(DateTimeFormatter.ofPattern(format))
		return time
	}

	@Keyword
	public static String getFutureDate(String timeZone, int months, int days, String format) {
		ZoneId zoneId = ZoneId.of(timeZone)
		def date = LocalDate.now(zoneId).plusMonths(months).plusDays(days).format(DateTimeFormatter.ofPattern(format))
		return date
	}

	@Keyword
	public static String getPastDate(String timeZone, int months, int days, String format) {
		ZoneId zoneId = ZoneId.of(timeZone)
		def date = LocalDate.now(zoneId).minusMonths(Math.abs(months)).minusDays(Math.abs(days)).format(DateTimeFormatter.ofPattern(format))
		return date
	}

	public static String getCurrentDateTime(String timeZoneId, String dateTimeFormat) {
		SimpleDateFormat format = new SimpleDateFormat(dateTimeFormat, Locale.US)
		setTimeZone(format, timeZoneId);
		Date date = new Date();
		return ((SimpleDateFormat) format).format(date);
	}
}
