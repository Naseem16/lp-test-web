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
	private String dateOfBreastFeeding;
	private int timeOfBreastFeeding;
	private int breastFeedingStatus;
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
	public String getDateOfBreastFeeding() {
		return dateOfBreastFeeding;
	}
	public void setDateOfBreastFeeding(String dateOfBreastFeeding) {
		this.dateOfBreastFeeding = dateOfBreastFeeding;
	}
	public int getTimeOfBreastFeeding() {
		return timeOfBreastFeeding;
	}
	public void setTimeOfBreastFeeding(int timeOfBreastFeeding) {
		this.timeOfBreastFeeding = timeOfBreastFeeding;
	}
	public int getBreastFeedingStatus() {
		return breastFeedingStatus;
	}
	public void setBreastFeedingStatus(int breastFeedingStatus) {
		this.breastFeedingStatus = breastFeedingStatus;
	}
	
	
	
}
