package com.rootauto.io;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/* Author: Henry Artime 
 * This class cannot be sub classed. 
 * Records with invalid commands or invalid number of fields will be prevented from further processing
*/ 
final  public  class ProcessInputFile {
	private String fileName;
	
	public ProcessInputFile(String filename) {
		this.fileName = filename;
		
	}
	
	public Optional<List<String>> readInputFile_discardBadRecords() {
		
		List<String> inRecords = new ArrayList<>();
		List<String> records = null;
		Optional<List<String>> opt; 
		
		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {

            //1. filter records that are not "Driver or Trip" commands 
			//2. Covert to a List of records 
            inRecords = stream
                    .filter(record -> record.startsWith("Driver") || record.startsWith("Trip"))
                    .collect(Collectors.toList());

        } catch (IOException e) {
        	System.err.println("ERROR: IO error while trying to open or reading input file - File name path = " + this.getFileName());
            e.printStackTrace();
            System.exit(1);
        }
		records = validateCorrect_NumberOfFields_PerRecord(inRecords);
		 
		 if (!records.isEmpty()) {  
		 opt = Optional.of(records);
		 } else {
		 opt = Optional.empty();
		 }
	return opt; 
	}
	private List<String> validateCorrect_NumberOfFields_PerRecord(List<String> recs) {
		List<String> okayRecords = new ArrayList<>();
		
		for (String rec : recs) {
			// Following logic will allow for more than a single space between fields!
			// Following logic will also flag invalid HH:SS e.g. HH SS. 
			String[] fields = rec.split("\\s+");
			
			if (fields[0].startsWith("Driver") && fields.length == 2 ) {
			okayRecords.add(rec);
			} else {
			if (fields[0].startsWith("Trip") && fields.length == 5 ) {
			okayRecords.add(rec);
			} else {
				System.err.println("ERROR: Invalid record missing field(s) -or- incorrect number of fields -> " + rec);
			}
			}
		}
		return okayRecords; 
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public String toString() {
		return "ProcessInputFile [fileName=" + fileName + "]";
	}
	
	
}

