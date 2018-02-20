package org.sdrc.lactation.utils;
/**
 * 
 * @author Ratikanta
 *
 */
public class FeedExpressionModel {

	private String id;
	private String babyCode;
	private String userId;
	private String dateOfFeed;
	private String timeOfFeed;
	private int methodOfFeed;
	private int OMMVolume;
	private int DHMVolume;
	private int formulaVolume;
	private int animalMilkVolume;
	private int otherVolume;
	private int locationOfFeeding;
	private Double babyWeight;
	private String syncFailureMessage;
	private Boolean isSynced;
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
	public int getMethodOfFeed() {
		return methodOfFeed;
	}
	public void setMethodOfFeed(int methodOfFeed) {
		this.methodOfFeed = methodOfFeed;
	}
	public int getOMMVolume() {
		return OMMVolume;
	}
	public void setOMMVolume(int oMMVolume) {
		OMMVolume = oMMVolume;
	}
	public int getDHMVolume() {
		return DHMVolume;
	}
	public void setDHMVolume(int dHMVolume) {
		DHMVolume = dHMVolume;
	}
	public int getFormulaVolume() {
		return formulaVolume;
	}
	public void setFormulaVolume(int formulaVolume) {
		this.formulaVolume = formulaVolume;
	}
	public int getAnimalMilkVolume() {
		return animalMilkVolume;
	}
	public void setAnimalMilkVolume(int animalMilkVolume) {
		this.animalMilkVolume = animalMilkVolume;
	}
	public int getOtherVolume() {
		return otherVolume;
	}
	public void setOtherVolume(int otherVolume) {
		this.otherVolume = otherVolume;
	}
	public int getLocationOfFeeding() {
		return locationOfFeeding;
	}
	public void setLocationOfFeeding(int locationOfFeeding) {
		this.locationOfFeeding = locationOfFeeding;
	}
	public Double getBabyWeight() {
		return babyWeight;
	}
	public void setBabyWeight(Double babyWeight) {
		this.babyWeight = babyWeight;
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
	
}
