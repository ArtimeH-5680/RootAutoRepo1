package com.rootauto.models;

public class RootAutoReportRecord {
  private String driver; 
  private long total_miles; 
  private double average_speed;
  
  public RootAutoReportRecord(long total_miles, double average_speed, String driver) {
	  this.total_miles = total_miles;
	  this.average_speed = average_speed;
	  this.driver = driver; 
  }
  public RootAutoReportRecord() {}

public long getTotal_miles() {
	return total_miles;
}

public void setTotal_miles(long total_miles) {
	this.total_miles = total_miles;
}

public double getAverage_speed() {
	return average_speed;
}

public void setAverage_speed(double average_speed) {
	this.average_speed = average_speed;
}

public String getDriver() {
	return driver;
}
public void setDriver(String driver) {
	this.driver = driver;
}
@Override
public String toString() {
	return "RootAutoReportRecord [driver=" + driver + ", total_miles=" + total_miles + ", average_speed="
			+ average_speed + "]";
}

  
}
