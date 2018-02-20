package org.sdrc.lactation.utils;

/**
 * 
 * @author Ratikanta
 *
 */
public class BFPDModel {

	private String id;
	private String babyCode;
	private String userId;
	private String syncFailureMessage;
	private Boolean isSynced;
	private String dateOfFeed;
	private int timeOfFeed;
	private int status;
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
	public int getTimeOfFeed() {
		return timeOfFeed;
	}
	public void setTimeOfFeed(int timeOfFeed) {
		this.timeOfFeed = timeOfFeed;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
}
