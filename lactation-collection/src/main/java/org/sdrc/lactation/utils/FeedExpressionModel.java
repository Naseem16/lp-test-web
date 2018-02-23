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
	private Double ommVolume;
	private Double dhmVolume;
	private Double formulaVolume;
	private Double animalMilkVolume;
	private Double otherVolume;
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
	
	public Double getOmmVolume() {
		return ommVolume;
	}
	public void setOmmVolume(Double ommVolume) {
		this.ommVolume = ommVolume;
	}
	public Double getDhmVolume() {
		return dhmVolume;
	}
	public void setDhmVolume(Double dhmVolume) {
		this.dhmVolume = dhmVolume;
	}
	public Double getFormulaVolume() {
		return formulaVolume;
	}
	public void setFormulaVolume(Double formulaVolume) {
		this.formulaVolume = formulaVolume;
	}
	public Double getAnimalMilkVolume() {
		return animalMilkVolume;
	}
	public void setAnimalMilkVolume(Double animalMilkVolume) {
		this.animalMilkVolume = animalMilkVolume;
	}
	public Double getOtherVolume() {
		return otherVolume;
	}
	public void setOtherVolume(Double otherVolume) {
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
