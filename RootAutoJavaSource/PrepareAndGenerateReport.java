package com.rootauto.report;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import com.rootauto.utils.RootAutoUtils;
import com.rootauto.models.RootAutoReportRecord;
import com.rootauto.models.RootAutoTrip;
/*
 *  This class includes all methods to process Driver and Trip records and create data structures to generate a correct report.
 */

public class PrepareAndGenerateReport {
	private Set<RootAutoTrip> valid_trips = new HashSet<>();
	private ConcurrentHashMap<String, Set<RootAutoTrip>> driver_to_trips = new ConcurrentHashMap<>(); // Take all defaults
	private List<RootAutoReportRecord> report_records = new ArrayList<>(); 
	
	public PrepareAndGenerateReport() {};
	
public Set<RootAutoTrip> createSetOfValidTrips ( List<String> records) {
	
	  for ( String recs : records ) {
		if ( recs.startsWith("Driver"))  continue;   // skip 'Driver" record 
		String[] fields = recs.split("\\s+");
		// validate drive times format HH:MM 
		  try {
			  if (!RootAutoUtils.isValid_TimeHHSS(fields[2]) || !RootAutoUtils.isValid_TimeHHSS(fields[3])) {
				  System.err.println("ERROR: Invalid Trip record HH:SS field is incorrect-> " + recs);
				  continue; 
			  }
		  } catch(Exception e) {
			  e.printStackTrace();
			  continue; 
		 	  }
		  if (fields[3].equals("00:00")) {  // special case adjustment 
			  fields[3] = "23:59"; // midnight for end time driven changed , so we can process a midnight to midnight drive.
			  // Handle start HH:SS 00:00 to end HH:SS 00:00 use cases 
			  System.out.println("INFO: End time for midnight adjusted to 23:59 for Trip record -> " + recs);
			  }
		  long[] durations; 
		  try {
		      durations = RootAutoUtils.calculate_DriveTimes(fields[2],fields[3]);
		      if (durations[1] <= 0) { // I will use seconds of time driven. 
		    	  // negative or zero value means start time is greater than end time or both times are equal. This is not valid. 
		    	  System.err.println("ERROR: Invalid Trip record - start time is greater than end time or both times are equal -> " + recs);
		    	  continue; 
		      }
		      //  HH:SS does not have leading zero 
		  } catch(Exception e) {
			  System.err.println("ERROR: Invalid Trip record HH:SS must have leading zero -> " + recs);
			  continue; 
		  }
		      try {
		      if (!RootAutoUtils.isValid_MilesDriven(fields[4])) {
		    	  System.err.println("ERROR: Invalid Trip record miles driven exceeds 999.9 -or- format is not correct valid is (ddd.d) -> " + recs);
		    	  continue; 
		      }
		      } catch(Exception e) {
				  e.printStackTrace();
				  continue; 
			 	  }
		      try {
		    	   if (!RootAutoUtils.isValid_DriverName(fields[1])) {
		    	  System.err.println("ERROR: Invalid Trip record driver name not valid-> " + recs);
		    	  continue;
		    	  }
		      } catch(Exception e) {
				  e.printStackTrace();
				  continue; 
			 	  } 
		     
		      RootAutoTrip trip = new RootAutoTrip(); 
		      trip.setDriver(fields[1]);       // set driver id 
		      trip.setDrive_time(durations[1]); // set time driven in seconds 
		      trip.setMiles(Double.parseDouble(fields[4])); // set miles driven. 
		      trip.setEnd_HHSS(fields[3]);
		      trip.setStart_HHSS(fields[2]);
		      trip.setMph(RootAutoUtils.calculate_MilesPerHour(Double.parseDouble(fields[4]), durations[1])); // SET MILES PER HOUR FOR THIS TRIP. 
		      
		      if (!valid_trips.add(trip)) {
		    	  System.err.println ("ERROR: UNEXPECTED DUPLICATE TRIP OBJECT DETECTED NOT ADDED TO SET: TRIP -> " + trip.toString()); 
		      }
		      
		      
		      
		  }
		     return valid_trips; 
		
}
public ConcurrentHashMap<String, Set<RootAutoTrip>> generate_Driver_To_TripsAssociationMap(Set<RootAutoTrip> trips, List<String> records) {
	// 1st process "Driver" commands use driver name as key and instiate an empty Set as value for each key. 
	// I do not assume ANY ORDER of Driver or Trip records. e.g. a Trip record for driver "Dan" can appear before the associated Driver command record.
	// In later processing a "Driver" with an empty set of Trip objects represents a Driver with no "Trip" command records.
	
	    List<String> entries_to_remove = new ArrayList<>();
	    
	    for (String rec : records) {
	    	if (!rec.startsWith("Driver")) continue;
	    	String[] fields = rec.split("\\s+");
	    	 try {
		    	   if (!RootAutoUtils.isValid_DriverName(fields[1])) {
		    	  System.err.println("ERROR: Invalid Driver record driver name not valid-> " + rec);
		    	  continue;
		    	  }
		      } catch(Exception e) {
		    	  System.err.println("ERROR: Invalid Driver record driver name not valid-> " + rec);
				  e.printStackTrace();
				  continue; 
			 	  } 
	    	driver_to_trips.putIfAbsent(fields[1], new HashSet<RootAutoTrip>()); 
	    }
	    // Now process "Trip" Objects  
	    for (RootAutoTrip trip : trips) {
	    		    	
	    	if (driver_to_trips.containsKey(trip.getDriver())) {
	    		Set<RootAutoTrip> value = driver_to_trips.get(trip.getDriver());
	    	// Do not modify e.g. add to map Set original value directly use replace function.  	
	    		Set<RootAutoTrip> new_value = new HashSet<>();
	    		new_value.addAll(value); 
	    		new_value.add(trip); 
	    		driver_to_trips.replace(trip.getDriver(), new_value); 
	    		
	    	} else {
	    		System.err.println("ERROR: Invalid Trip record - no related Driver record registered: Name = " + trip.getDriver());
	    	}
	    }
	    // Now DISCARD trips for a SPECIFIC driver that average a speed of less than 5 MPH or greater than 100 MPH. 
	    // We will simply remove the Map entry if this condition is true. 
	    
	    for (Map.Entry<String, Set<RootAutoTrip>> entry : driver_to_trips.entrySet()) {
	    	String driver = entry.getKey();
	    	Set<RootAutoTrip> trips2 = entry.getValue();
	    	
	    	if (trips2.isEmpty()) continue;  
	    	
	    	   double average = trips2.stream()
               .mapToDouble(e -> e.getMph())
               .average()
               .getAsDouble();
	    	   
	    	   if ( average < 5.0d || average > 100.0d) {
	    		   System.out.println("INFO: DISCARDING DUE TO AVG MPH CONSTRAINT TRIPS FOR DRIVER = " + driver + " AVG = " + average);
	    		   entries_to_remove.add(driver);
	    		   // DO NOT REMOVE ENTRIES WHILE ITERATING THE MAP. 
	    	   }
	    	
	    }  // end Map loop 
	    
	    // Remove entries to discard 
	       for (String key : entries_to_remove) {
	    	   driver_to_trips.remove(key);
	       }
	       // Here we go.... 
	       detectOverlappingTimes(driver_to_trips);
	       
	    return driver_to_trips; 
}
public void detectOverlappingTimes (ConcurrentHashMap<String, Set<RootAutoTrip>> map) {

	SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy, HH:mm");
    formatter.setLenient(false);
    
for (Map.Entry<String, Set<RootAutoTrip>> entry : map.entrySet()) {
	
	Set<RootAutoTrip> trips = entry.getValue();
	int num = trips.size(); 
	RootAutoTrip arr[] = new RootAutoTrip[num]; 
	
	System.arraycopy(trips.toArray(), 0, arr, 0, num);	
	
	try {
		
	for ( int i = 0; i < arr.length; i++ ) {	

		  String timeA =  new String("01.01.2021, " + arr[i].getStart_HHSS());
          String timeB =  new String ("01.01.2021, " + arr[i].getEnd_HHSS());
          
          java.util.Date old1 = formatter.parse(timeA);
	      java.util.Date old2 = formatter.parse(timeB);
	      
	      long one1 = old1.getTime();
	      long one2 = old2.getTime();
	      
	      Timestamp time1 = new Timestamp(one1);
	      Timestamp time2 = new Timestamp(one2);
          
		for ( int n = i + 1; n < arr.length; n++) {	
			
			  
	          
	          String time2A =  new String("01.01.2021, " + arr[n].getStart_HHSS());
	          String time2B = new String("01.01.2021, " + arr[n].getEnd_HHSS());
	          
	          
		      java.util.Date old3 = formatter.parse(time2A);
		      java.util.Date old4 = formatter.parse(time2B);
		      
		     
		      long one3 = old3.getTime();
		      long one4 = old4.getTime();
		      
		      
		      Timestamp time3 = new Timestamp(one3);
		      Timestamp time4 = new Timestamp(one4);
		      
		      boolean b =  !time1.after(time4)
		 	         && !time2.before(time3);
		      if (b) {
		    	  System.err.println("WARNING: Overlapping HH:SS times for Trip " + arr[i].getDriver() + " " + arr[i].getStart_HHSS() + " " + arr[i].getEnd_HHSS());
		    	  System.err.println("WARNING: Overlapping HH:SS times for Trip " + arr[n].getDriver() + " " + arr[n].getStart_HHSS() + " " + arr[n].getEnd_HHSS());
		      }
			}
		
	}
	} catch (Exception e ) {
		System.err.println("ERROR: Unexpected error while detecting overlapping HH:SS time for trip records- report processing continues");
		e.printStackTrace();
		return; 
	}
	
}
  return; 
	
}
public List<RootAutoReportRecord> generateReportRecord(ConcurrentHashMap<String, Set<RootAutoTrip>> map) {
	

	for (Map.Entry<String, Set<RootAutoTrip>> entry : map.entrySet()) {
    	String driver = entry.getKey();
    	Set<RootAutoTrip> trips = entry.getValue();
    	
    	if (trips.isEmpty()) {
    		RootAutoReportRecord report_record_zeros = new RootAutoReportRecord();
    		   report_record_zeros.setDriver(driver);
	    	   report_record_zeros.setTotal_miles(0L); 
	    	   report_record_zeros.setAverage_speed(0.0d);
	    	   report_records.add(report_record_zeros);
	    	   continue; 
    		
    	}
    	
    	double total_mph = 0.0d; 
    	long total_entries = 0L; 
    	long total_miles = 0L; 
    	for ( RootAutoTrip trip : trips) {
    		
	    		total_mph += trip.getMph();
	    		total_miles += trip.getMiles();
	    		total_entries += 1L; 
	    	} // end Set loop 
	    	    
	    	   RootAutoReportRecord report_record = new RootAutoReportRecord(); 
	    	   double average = total_mph / total_entries; 
	    	   report_record.setDriver(driver);
	    	   report_record.setTotal_miles(total_miles); 
	    	   report_record.setAverage_speed(average);
	    	   report_records.add(report_record);
    	} // end map loop 
	        // use new feature of Java 8 + to sort report records descending by total miles driven. 
        	Comparator<RootAutoReportRecord> reportRecordComparator 
            = Comparator.comparingLong(RootAutoReportRecord::getTotal_miles);
        	
        	Comparator<RootAutoReportRecord>  reportRecordComparatorDesc
        	 = reportRecordComparator.reversed();
        	
        	report_records.sort(reportRecordComparatorDesc);
        	
        	// remove commented code below for debugging prior to printing report 
//        	for ( RootAutoReportRecord xxx : report_records) {
//        		System.out.println(xxx.toString());
//        	}
        	
        	return report_records; 
    	
	 
}
public void printReport ( List<RootAutoReportRecord> records) {
	 
	        System.out.println("***********************************START REPORT ***************************************");
	       
	        for ( RootAutoReportRecord record : records) {
	        	
	        	if (record.getTotal_miles() == 0L) {
	        		System.out.println(record.getDriver() + ": " + record.getTotal_miles() + " miles" );
	        	continue; 
	        }
System.out.println(record.getDriver() + ": " + record.getTotal_miles() + " miles " + "@ " + RootAutoUtils.roundToNearestInt(record.getAverage_speed()) +" mph" );
	        }
	        
	        System.out.println("*********************************** END REPORT ***************************************");
}
}
      


