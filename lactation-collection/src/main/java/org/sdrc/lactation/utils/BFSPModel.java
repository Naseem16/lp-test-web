package org.sdrc.lactation.utils;

/**
 * 
 * @author Ratikanta
 *
 */
public class BFSPModel {
	private String id;
	private String babyCode;
	private String userId;
	private String syncFailureMessage;
	private Boolean isSynced;
	private String dateOfFeed;
	private String timeOfFeed;
	private int spPerformed;
	private int personPerformed;
	private Double duration;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBabyCode() {
		return babyCode;
	}
	public void setBabyCode(String babyCode) {
		this.babyCode = babyCode;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getSyncFailureMessage() {
		return syncFailureMessage;
	}
	public void setSyncFailureMessage(String syncFailureMessage) {
		this.syncFailureMessage = syncFailureMessage;
	}
	public Boolean getIsSynced() {
		return isSynced;
	}
	public void setIsSynced(Boolean isSynced) {
		this.isSynced = isSynced;
	}
	public String getDateOfFeed() {
		return dateOfFeed;
	}
	public void setDateOfFeed(String dateOfFeed) {
		this.dateOfFeed = dateOfFeed;
	}
	public String getTimeOfFeed() {
		return timeOfFeed;
	}
	public void setTimeOfFeed(String timeOfFeed) {
		this.timeOfFeed = timeOfFeed;
	}
	public int getSpPerformed() {
		return spPerformed;
	}
	public void setSpPerformed(int spPerformed) {
		this.spPerformed = spPerformed;
	}
	public int getPersonPerformed() {
		return personPerformed;
	}
	public void setPersonPerformed(int personPerformed) {
		this.personPerformed = personPerformed;
	}
	public Double getDuration() {
		return duration;
	}
	public void setDuration(Double duration) {
		this.duration = duration;
	}	
	
}
