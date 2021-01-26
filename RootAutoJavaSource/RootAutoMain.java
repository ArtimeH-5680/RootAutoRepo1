package com.rootauto.main;
import com.rootauto.io.ProcessInputFile;
import com.rootauto.models.RootAutoReportRecord;
import com.rootauto.models.RootAutoTrip;
import com.rootauto.report.PrepareAndGenerateReport;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
public class RootAutoMain {

	public static void main(String[] args) {

// Ensure that input file name is included on the command line and only one argument 

//====================================================================================		
		if (args.length == 0 || args.length > 1 ) {
			System.err.println("ERROR: Please provide a valid file name/path on command line" );
			System.exit(1);
		}
// Process input file invalid records will be discarded 
		ProcessInputFile inputRecords = new ProcessInputFile(args[0]);
		
		Optional<List<String>> opt = inputRecords.readInputFile_discardBadRecords();
		
		if (!opt.isPresent()) {
			System.err.println("ERROR: No valid input records detected!");
			System.exit(1); // set any status other than 0.
		}
		List<String> records = opt.get();
		
		PrepareAndGenerateReport prepare = new PrepareAndGenerateReport(); 
		
		// phase 1 is to validate trip records and create a Set containing valid objects of type 'RootAutoTrip" 
		Set<RootAutoTrip> trips = prepare.createSetOfValidTrips(records);
		
		if (trips.isEmpty()) {
			System.err.println("ERROR: No valid input trip records detected!");
		}
		ConcurrentHashMap<String, Set<RootAutoTrip>> map_of_trips = prepare.generate_Driver_To_TripsAssociationMap(trips, records);
		
		 List<RootAutoReportRecord> report_records = prepare.generateReportRecord(map_of_trips); 
		 // Output the report 
		 prepare.printReport(report_records);
		 System.exit(0); // set return status code to 0 
		 
		
	}

}
