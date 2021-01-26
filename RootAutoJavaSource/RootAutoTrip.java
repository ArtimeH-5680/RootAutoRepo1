package com.rootauto.models;

import java.util.UUID;
/*
 *  This class represents a unique "Trip" instance. 
 */

public class RootAutoTrip {
	UUID unique_id;   // Version 4-to ensure uniqueness when used in HashSet or as key in HashMap.
	private String driver;
	private long drive_time; // time in seconds 
	private double miles;
	private double mph; 
	private String end_HHSS;
	private String start_HHSS;
	
	public RootAutoTrip ( String driver, long drive_time, double miles, double mph,String start_HHSS, String end_HHSS) {
		this.driver = driver;
		this.drive_time = drive_time;
		this.miles = miles;
		this.mph = mph;
		this.start_HHSS = start_HHSS;
		this.end_HHSS = end_HHSS;
		setUnique_id(UUID.randomUUID());
	}
		
	public RootAutoTrip() {setUnique_id(UUID.randomUUID());};	
	
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}

	public double getMiles() {
		return miles;
	}
	public void setMiles(double miles) {
		this.miles = miles;
	}

	public long getDrive_time() {
		return drive_time;
	}

	public void setDrive_time(long drive_time) {
		this.drive_time = drive_time;
	}

	public double getMph() {
		return mph;
	}

	public void setMph(double mph) {
		this.mph = mph;
	}

	private UUID getUnique_id() {
		return unique_id;
	}

	private void setUnique_id(UUID unique_id) {
		this.unique_id = unique_id;
	}
	
    public String getEnd_HHSS() {
		return end_HHSS;
	}

	public void setEnd_HHSS(String end_HHSS) {
		this.end_HHSS = end_HHSS;
	}

	public String getStart_HHSS() {
		return start_HHSS;
	}

	public void setStart_HHSS(String start_HHSS) {
		this.start_HHSS = start_HHSS;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (drive_time ^ (drive_time >>> 32));
		result = prime * result + ((driver == null) ? 0 : driver.hashCode());
		result = prime * result + ((end_HHSS == null) ? 0 : end_HHSS.hashCode());
		long temp;
		temp = Double.doubleToLongBits(miles);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(mph);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((start_HHSS == null) ? 0 : start_HHSS.hashCode());
		result = prime * result + ((unique_id == null) ? 0 : unique_id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		RootAutoTrip other = (RootAutoTrip) obj;
		if (drive_time != other.drive_time) {
			return false;
		}
		if (driver == null) {
			if (other.driver != null) {
				return false;
			}
		} else if (!driver.equals(other.driver)) {
			return false;
		}
		if (end_HHSS == null) {
			if (other.end_HHSS != null) {
				return false;
			}
		} else if (!end_HHSS.equals(other.end_HHSS)) {
			return false;
		}
		if (Double.doubleToLongBits(miles) != Double.doubleToLongBits(other.miles)) {
			return false;
		}
		if (Double.doubleToLongBits(mph) != Double.doubleToLongBits(other.mph)) {
			return false;
		}
		if (start_HHSS == null) {
			if (other.start_HHSS != null) {
				return false;
			}
		} else if (!start_HHSS.equals(other.start_HHSS)) {
			return false;
		}
		if (unique_id == null) {
			if (other.unique_id != null) {
				return false;
			}
		} else if (!unique_id.equals(other.unique_id)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "RootAutoTrip [unique_id=" + unique_id + ", driver=" + driver + ", drive_time=" + drive_time + ", miles="
				+ miles + ", mph=" + mph + ", end_HHSS=" + end_HHSS + ", start_HHSS=" + start_HHSS + "]";
	}

	
	}

	
