package com.rootauto.utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/*
 *  This class should contain static general purpose "utility" methods only.
 */

public class RootAutoUtils {


public static boolean isValid_TimeHHSS(String time) throws Exception {
	
	 String regex = "([01]?[0-9]|2[0-3]):[0-5][0-9]"; 
	
	 Pattern p = Pattern.compile(regex); 

    // If the time is empty 
    // throw Exception 
    if (time == null) { 
        throw new IllegalArgumentException();  
    } 

     Matcher match = p.matcher(time); 

     return match.matches(); 
} 
public static boolean isValid_DriverName(String name) throws Exception {
	// Allow only letters and 1st character must me upper case. There is no length restriction. 
	String regex = "[A-Z][a-z]*";
	
	if (name == null) {
		 throw new IllegalArgumentException();  
		    } 
	return name.matches(regex);
}
public static boolean isValid_MilesDriven (String milesDriven) throws Exception {
	
	// We will allow at most 3 digits before decimal and 1 digit after e.g. 123.4. The maximum value is 999.9.  
	
	String regex = "\\d{1,3}\\.\\d{1,1}";
	
	if (milesDriven == null) {
		 throw new IllegalArgumentException();  
    } 
	return milesDriven.matches(regex);
	}

public static long[] calculate_DriveTimes(String start, String end) throws DateTimeParseException {
	// Best to provide all reasonable time durations! 
	// 
	// A DATE FORMAT OF "dd-MM-yyyy" + HH:mm must be provided or these methods will throw an exception.
	// ANY ARBRITARY VALID DATE OF FORMAT "dd-MM-yyyy" AS LONG AS THEY ARE THE SAME WILL WORK. I CHOSE "01-01-2021". 
	
	long[] durations = new long[4];
	
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

	LocalDateTime time_start= LocalDateTime.parse("01-01-2021 " + start, formatter);
	LocalDateTime time_end= LocalDateTime.parse("01-01-2021 " + end, formatter);

	long Milliseconds = Duration.between(time_start, time_end).toMillis();
	long Seconds = Duration.between(time_start, time_end).getSeconds();
	long Minutes = Duration.between(time_start, time_end).toMinutes();
	long Hours =   Duration.between(time_start, time_end).toHours();
	
	durations[0] = Milliseconds;
	durations[1] = Seconds; 
	durations[2] = Minutes;
	durations[3] = Hours; 
	return durations;
	
}
public static double calculate_MilesPerHour(double distance_miles, long time_seconds ) {
	// arguments already validated for proper format prior to invoking this method! 
	double meters =  distance_miles * 1609.34d; 
	double kph = ( meters/1000.0d ) / ( time_seconds/3600.0d ); // Do kilometers per hour also. 
	double mph = kph / 1.609d;  // convert kilometers per hours to miles per hour 
	return mph;
}
public static int roundToNearestInt(double num) {
	//  ** The rounding factor is .5 ** and this method will handle negatives also. 
	//  46.51234567  will round to 47 
	//  46.4123456   will round to 46 
	//  -46.4 will round to 46. 
    double d = Math.abs(num);
    int i = (int) d;
    double result = d - (double) i;
    if(result<0.5){
        return num<0 ?i : i;            
    }else{
    	return num<0 ?-(i+1) : i+1;     
    }
}
}


