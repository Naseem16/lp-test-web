package org.sdrc.lactation.utils;

public class PatientModel {

	private String babyCode;
	private String babyCodeHospital;
	private String babyOf;
	private int mothersAge;
	private String deliveryDate;
	private String deliveryTime;
	private int deliveryMethod;
	private double babyWeight;
	private int gestationalAgeInWeek;
	private int mothersPrenatalIntent;
	private int parentsKnowledgeOnHmAndLactation;
	private String timeTillFirstExpressionInHour;
	private String timeTillFirstExpressionInMinute;
	private int inpatientOrOutPatient;
	private String admissionDateForOutdoorPatients;
	private int babyAdmittedTo;
	private int nicuAdmissionReason;
	private Boolean isSynced;
	private String syncFailureMessage;
	private String userId;

	public String getBabyCode() {
		return babyCode;
	}

	public void setBabyCode(String babyCode) {
		this.babyCode = babyCode;
	}

	public String getBabyCodeHospital() {
		return babyCodeHospital;
	}

	public void setBabyCodeHospital(String babyCodeHospital) {
		this.babyCodeHospital = babyCodeHospital;
	}

	public String getBabyOf() {
		return babyOf;
	}

	public void setBabyOf(String babyOf) {
		this.babyOf = babyOf;
	}

	public int getMothersAge() {
		return mothersAge;
	}

	public void setMothersAge(int mothersAge) {
		this.mothersAge = mothersAge;
	}

	public String getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public String getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(String deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public int getDeliveryMethod() {
		return deliveryMethod;
	}

	public void setDeliveryMethod(int deliveryMethod) {
		this.deliveryMethod = deliveryMethod;
	}

	public double getBabyWeight() {
		return babyWeight;
	}

	public void setBabyWeight(double babyWeight) {
		this.babyWeight = babyWeight;
	}

	public int getGestationalAgeInWeek() {
		return gestationalAgeInWeek;
	}

	public void setGestationalAgeInWeek(int gestationalAgeInWeek) {
		this.gestationalAgeInWeek = gestationalAgeInWeek;
	}

	public int getMothersPrenatalIntent() {
		return mothersPrenatalIntent;
	}

	public void setMothersPrenatalIntent(int mothersPrenatalIntent) {
		this.mothersPrenatalIntent = mothersPrenatalIntent;
	}

	public int getParentsKnowledgeOnHmAndLactation() {
		return parentsKnowledgeOnHmAndLactation;
	}

	public void setParentsKnowledgeOnHmAndLactation(int parentsKnowledgeOnHmAndLactation) {
		this.parentsKnowledgeOnHmAndLactation = parentsKnowledgeOnHmAndLactation;
	}

	public String getTimeTillFirstExpressionInHour() {
		return timeTillFirstExpressionInHour;
	}

	public void setTimeTillFirstExpressionInHour(String timeTillFirstExpressionInHour) {
		this.timeTillFirstExpressionInHour = timeTillFirstExpressionInHour;
	}

	public String getTimeTillFirstExpressionInMinute() {
		return timeTillFirstExpressionInMinute;
	}

	public void setTimeTillFirstExpressionInMinute(String timeTillFirstExpressionInMinute) {
		this.timeTillFirstExpressionInMinute = timeTillFirstExpressionInMinute;
	}

	public int getInpatientOrOutPatient() {
		return inpatientOrOutPatient;
	}

	public void setInpatientOrOutPatient(int inpatientOrOutPatient) {
		this.inpatientOrOutPatient = inpatientOrOutPatient;
	}

	public String getAdmissionDateForOutdoorPatients() {
		return admissionDateForOutdoorPatients;
	}

	public void setAdmissionDateForOutdoorPatients(String admissionDateForOutdoorPatients) {
		this.admissionDateForOutdoorPatients = admissionDateForOutdoorPatients;
	}

	public int getBabyAdmittedTo() {
		return babyAdmittedTo;
	}

	public void setBabyAdmittedTo(int babyAdmittedTo) {
		this.babyAdmittedTo = babyAdmittedTo;
	}

	public int getNicuAdmissionReason() {
		return nicuAdmissionReason;
	}

	public void setNicuAdmissionReason(int nicuAdmissionReason) {
		this.nicuAdmissionReason = nicuAdmissionReason;
	}

	public Boolean getIsSynced() {
		return isSynced;
	}

	public void setIsSynced(Boolean isSynced) {
		this.isSynced = isSynced;
	}

	public String getSyncFailureMessage() {
		return syncFailureMessage;
	}

	public void setSyncFailureMessage(String syncFailureMessage) {
		this.syncFailureMessage = syncFailureMessage;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
