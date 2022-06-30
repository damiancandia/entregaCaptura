package com.javatechie.spring.mongo.api.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Document(collection = "accidentes")
public class Accidente {
	
	@Id
	private String id;
	
	


	@Field("Severity")
	public String severity;
	
	@Field("Start_Time")
	private String starttime;
	
	@Field("End_Time")
	private String endtime;
	
	@Field("Start_Lat")
	private String startLat;
	
	@Field("Start_Lng")
	private String startLng;
	
	@Field("End_Lat")
	private String endlat;
	
	@Field("End_Lng")
	private String endLng;
	
	@Field("Weather_Condition")
	private String weatherCondition;
	
	@Field("Civil_Twilight")
	private String civilTwilight;
	
	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public String getStartLat() {
		return startLat;
	}

	public void setStartLat(String startLat) {
		this.startLat = startLat;
	}

	public String getStartLng() {
		return startLng;
	}

	public void setStartLng(String startLng) {
		this.startLng = startLng;
	}

	public String getEndlat() {
		return endlat;
	}

	public void setEndlat(String endlat) {
		this.endlat = endlat;
	}


	public String getEndLng() {
		return endLng;
	}


	public void setEndLng(String endLng) {
		this.endLng = endLng;
	}


	
	public String getId() {
		return id;
	}


	public String getSeverity() {
		return severity;
	}


	public void setSeverity(String value) {
		severity = value;
	}

	public String getStarttime() {
		return starttime;
	}


	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}
	
	public String getWeatherCondition() {
		return weatherCondition;
	}

	public void setWeatherCondition(String weatherCondition) {
		this.weatherCondition = weatherCondition;
	}

	public String getCivilTwilight() {
		return civilTwilight;
	}

	public void setCivilTwilight(String civilTwilight) {
		this.civilTwilight = civilTwilight;
	}

}
