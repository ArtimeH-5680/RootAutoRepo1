WHATS INCLUDED IN THE TAR BALL:
      1) THIS README FILE.
      2) Directory "RootAutoExec" - includes all compiled classes - run instructions included below. 
      3) Direcrory "RootAutoJavaSource" - includes all *.java source files. 
      4) Executable JAR file "rootautoexecutable.jar" - You can use this file to run the project instructions are included below. 
      5) Input file with "Driver" and "Trip" records "Input1.txt" - An over all and final test case file. 

GENERAL INFORMATION AND REQUIREMNTS FOR CORRECT USAGE:

1) MUST use Java version 8 OR above compiler and runtime environment. 
   The code uses features and Apis introduced in Java 8 such as Streams, Lambdas etc.... 
2) This code was created and tested with both Oracle's and IBM's Java 8 / update 168 distribution. 
   Please do not use any "Open" JDK distribution. 
3) MUST provide an input file / valid file path as the ONLY (1) argument to the 'java' CLI command. 
   If you provide no argument or more than one a INFO level message is sent to console. if you provide 
   and invalid file name an exception will occur an logged to console run terminates with status return code 1.
4) To keep it as simple as posssible I did not rely on ANY external framework or libraries. 
5) All logging (INFO,ERROR,WARNING) levels and the actual report messages are sent to STDOUT and STDERR.
6) I used Eclipse IDE ( Oxygen 2 ) release, but did not include an exported eclipse project in the TAR ball. 
7) I included an executable jar file + directory containing all 6 java source files + a directory containing compiled
   class files + one input file I used for a complete test scenario. 
8) You simply can compile and run OR just run the exercise from a Unix/Linux shell CLI or a Windows command prompt console
   as per instructions provide below. 
9) Due to the nature of this exercise I did not code abstractions ( no abstract classes or interfaces ) definitions.  
   I did code using the traditional object oriented approach. Due to the fact that related classes required no
   abstract type class to implement a "template" pattern and all classes where related I felt no need to define interface definitions.
10) I did not include Author : name comments on the top of class files. 
   
DEVELOPMENT AND TESTING METHODOLOGY: 

   This was a classic "batch" use case with no external network connections or resource managers etc. I used a 
   simple TDD "test driven development" approach. I coded multiple simple test cases and made sure test cases failed
   at first then fixed code and repeated this process. I used NO external unit tsting libraries or framework.
   I used various input files each suited to a specific test case,then finaly used 1 input file to test all cases.  
       
CODING DETAILS AND VALIDATIONS:
I used double-precision 64-bit IEEE 754 floating point primitives and 64-bit long primitive numbers to perform 
total miles, total average miles driven, average speed calculations. I ONLY ROUNDED TO NEAREST INTEGER FOR  
THE REPORT OUTPUT / ROUNDING FACTOR = .5 
Exmaple: 
         Average speed before rounding = 75.45678923455... 
		 Rounded average speed = 75 
		 Average speed before rounding = 75.545678923455... 
		 Rounded average speed = 76 
		 
		 I do print report records info average speed ( not rounded ) etc prior to printing the report. 
		 Simply edit file "PrepareAndGenerateReport.java" 
		 UNCOMMENT lines 276 to 278 compile and run. 
		 Remove "//" comment. 
		         	// remove commented code below for debugging prior to printing report 
//        	for ( RootAutoReportRecord xxx : report_records) {
//        		System.out.println(xxx.toString());
//        	}
		 
		 
1) HH:SS in Trip records MUST have leading 0s. A record with "7:45" for HH:SS is logged as an error
   and discarded from further procesing. Further more "HH:SS" time stamps are validated for correct 
   format and 24 HOUR MILATARY contraints.    
   As an example "05.10" is an error ALSO "24:00" is an error. The END TIME must be greater than START TIME
   if not an error message is logged and the record will be discarded from further processing. 
2) Trip records with start time and end time are equal are logged as en error and are discarded from 
   further processing. e.g. 05:10 05:10 is an error. 
3) Records MUST exactly start in column 1 with "Driver" or "Trip" words case sensitive. If not they are 
   treated as COMMENT lines. 
4) There is no restriction on the order of "Driver" and "Trip" records. They can appear in any order.
5) Your allowed to include duplicate driver names on multiple "Driver" records. The logic will 
   process these records as if you had only 1 Driver record.
6) Trip records with no associated "Driver" record will be logged as an error and discarded from further processing.   
7) Both "Trip" and  "Driver" records are validated for exact number of fields immediately after they are read.
   Records with incorrect number of expected fields will be logged as errors and discarded from further processing.
   The fields in the records can be separated by several blanks. 
8) For both "Driver" and "Trip" records the NAME field will be validated. These records must have a name 
   that start with a capital letter, include ONLY letters ( no numerics or special characters ). This validation
   does not handle multi national names, so any English name is valid. There is no restriction on the length.   
   Records that fail validation will be logged as an error and discarded from further processing. 
9) For "Trip" records containing END TIMES of "00:00" (midnight) my logic converts to "23:59", so 
   calculations and processing can proceed. 
    Example Trip record: 00:00 00:00 
	The END time will be adjusted to "23:59" , so it's a midnight to midnight trip duration. 
10)Trip records MILES DRIVEN field are validated as follows:
   Format MUST be "DDD.D" at most 3 digits before decimal and at most 1 digit after decimal.
   The maximum value cannot exceed "999.9" 
   It will ve validated for numerics only , one decimal point. 
   e.g. 1000.0 is not valid , 80.20 is not valid, 001.1 is valid. 
   All invalid Trip records will be logged as an error with details about the condition. 
11) AS PER exercise instructions: Discard any trips that average a speed of less than 5 mph or greater than 100 mph.
    The trip records will be discarded from further processing and a INFO level message is logged.
	NOTE:  SINCE THIS AN AVERAGE VALUE FOR A SET OF TRIP RECORDS RELATED TO A SPECIFIC DRIVER, 
	       I REMOVE THE DRIVER FROM REGISTRATION. THIS SPECIFIC DRIVER DOES NOT APPEAR IN THE REPORT.

12) Driver records with no associated "Trip" records will be included in the report as 0 miles driven.
    e.g. -> Jumi: 0 miles
13) BONUS VALIDATION: Trip records related to a specific Driver with OVERLAPPING start and end times are detected and logged 
    as WARNING level messages to STDERR. These records are NOT discarded and are included in futher processing.
	Example: 
	         Trip Joe 13:30 16:30 70.0 
			 Trip Joe 13:50 15:00 20.5 
	These records will be logged WARNING level 
			 
    
**** INSTRUCTIONS TO RUN OR COMPILE THEN RUN PROJECT:

*** To compile included java source files copy all *.java files from the tar ball "RootAutoSource" directory to any other dirctory of your choice.
*** Change working directory to where you copied all (6) *.java files. 
*** Use the following CLI command:
    javac *.java -d [Target directory name where you want all *.class files to go]
	
	
*** To run with already included compiled class files.
*** Change working directory to what you specified with the "-d" option in the javac command
*** OR you can use the already compiled code that I have included in directory "RootAutoExec" in the TAR ball. 
*** Use the following CLI command:

    java com.rootauto.main.RootAutoMain [path to input file]
	
	NOTE: If you plan to run the project using a LARGE (3,000 + lines) input file. I recommend 
	      you specify a maximum heap size value other than the JVM default. 
		  
		  Use the following command line and increase max heap size "-Xmx" as appropriate to prevent
		  JVM "OutOfMemoryException" Exceptions. 
		  
		  java -Xmx4096M com.rootauto.main.RootAutoMain [path to input file]
		  
*** To run with the executable jar file "rootautoexecutable.jar" I included within the TAR. 
*** There is no need to compile using this method. 
*** Use the following CLI command:

    java -Xmx4096M -jar rootautoexecutable.jar  [path to input file]
