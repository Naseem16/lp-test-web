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
	private String dateOfBFSP;
	private String timeOfBFSP;
	private int bfspPerformed;
	private int personWhoPerformedBFSP;
	private int bfspDuration;
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
	public String getDateOfBFSP() {
		return dateOfBFSP;
	}
	public void setDateOfBFSP(String dateOfBFSP) {
		this.dateOfBFSP = dateOfBFSP;
	}
	public String getTimeOfBFSP() {
		return timeOfBFSP;
	}
	public void setTimeOfBFSP(String timeOfBFSP) {
		this.timeOfBFSP = timeOfBFSP;
	}
	public int getBfspPerformed() {
		return bfspPerformed;
	}
	public void setBfspPerformed(int bfspPerformed) {
		this.bfspPerformed = bfspPerformed;
	}
	public int getPersonWhoPerformedBFSP() {
		return personWhoPerformedBFSP;
	}
	public void setPersonWhoPerformedBFSP(int personWhoPerformedBFSP) {
		this.personWhoPerformedBFSP = personWhoPerformedBFSP;
	}
	public int getBfspDuration() {
		return bfspDuration;
	}
	public void setBfspDuration(int bfspDuration) {
		this.bfspDuration = bfspDuration;
	}	
	
}
