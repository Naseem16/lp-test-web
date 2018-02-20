package org.sdrc.lactation.utils;

/**
 * 
 * @author Ratikanta
 *
 */
public class BFExpressionModel {
	private String id;
	private String babyCode;
	private String userId;
	private String syncFailureMessage;
	private Boolean isSynced;
	private String dateOfExpression;
	private String timeOfExpression;
	private int methodOfExpression;
	private int locationOfExpression;
	private Double volOfMilkExpressedFromLR;// 0-300ml
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
	public String getDateOfExpression() {
		return dateOfExpression;
	}
	public void setDateOfExpression(String dateOfExpression) {
		this.dateOfExpression = dateOfExpression;
	}
	public String getTimeOfExpression() {
		return timeOfExpression;
	}
	public void setTimeOfExpression(String timeOfExpression) {
		this.timeOfExpression = timeOfExpression;
	}
	public int getMethodOfExpression() {
		return methodOfExpression;
	}
	public void setMethodOfExpression(int methodOfExpression) {
		this.methodOfExpression = methodOfExpression;
	}
	public int getLocationOfExpression() {
		return locationOfExpression;
	}
	public void setLocationOfExpression(int locationOfExpression) {
		this.locationOfExpression = locationOfExpression;
	}
	public Double getVolOfMilkExpressedFromLR() {
		return volOfMilkExpressedFromLR;
	}
	public void setVolOfMilkExpressedFromLR(Double volOfMilkExpressedFromLR) {
		this.volOfMilkExpressedFromLR = volOfMilkExpressedFromLR;
	}
	
}
