package org.sdrc.lactation.domain;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Digits;

/**
 * 
 * @author Naseem Akhtar (naseem@sdrc.co.in) on 5th February 2018 17:10. This
 *         domain will be used for new patient registration.
 */

@Entity
@Table(uniqueConstraints = {
		@UniqueConstraint(columnNames = { "babyCode", "createdBy" }, name = "uniqueBabyCodeCreatedById") })
public class Patient {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer patientId;

	private String babyCode;

	private String babyCodeHospital;

	private Integer mothersAge;

	private String babyOf;

	private Timestamp deliveryDateAndTime;

	@Transient
	private String deliveryDate;

	@Transient
	private String deliveryTime;

	@ManyToOne
	@JoinColumn(name = "delivery_method")
	private TypeDetails deliveryMethod;

	@Digits(integer = 4, fraction = 2)
	private Double babyWeight;

	private Integer gestationalAgeInWeek;

	@ManyToOne
	@JoinColumn
	private TypeDetails mothersPrenatalIntent;

	@ManyToOne
	@JoinColumn
	private TypeDetails parentsKnowledgeOnHmAndLactation;

	private String timeTillFirstExpression;

	@Transient
	private String timeTillFirstExpressionInHour;

	@Transient
	private String timeTillFirstExpressionInMinute;

	@ManyToOne
	@JoinColumn
	private TypeDetails inpatientOrOutPatient;

	@Transient
	private String admissionDateForOutdoorPatients;

	private Date admissionDateForOutdoorPatientsM;

	@ManyToOne
	@JoinColumn
	private TypeDetails babyAdmittedTo;

	private String nicuAdmissionReasonDb;

	@Transient
	private Integer[] nicuAdmissionReason;

	@Transient
	private String createdDate;

	@Transient
	private String updatedDate;

	private Timestamp createdDateM;

	private Timestamp updatedDateM;

	private String createdBy;

	private String updatedBy;

	@Transient
	private String userId;

	@Transient
	private String dischargeDate;

	private Date dischargeDateM;

	public Patient() {

	}

	public Patient(Integer patientId) {
		this.patientId = patientId;
	}

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

	public Integer getMothersAge() {
		return mothersAge;
	}

	public void setMothersAge(Integer mothersAge) {
		this.mothersAge = mothersAge;
	}

	public String getBabyOf() {
		return babyOf;
	}

	public void setBabyOf(String babyOf) {
		this.babyOf = babyOf;
	}

	public Timestamp getDeliveryDateAndTime() {
		return deliveryDateAndTime;
	}

	public void setDeliveryDateAndTime(Timestamp deliveryDateAndTime) {
		this.deliveryDateAndTime = deliveryDateAndTime;
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

	public TypeDetails getDeliveryMethod() {
		return deliveryMethod;
	}

	public void setDeliveryMethod(TypeDetails deliveryMethod) {
		this.deliveryMethod = deliveryMethod;
	}

	public Double getBabyWeight() {
		return babyWeight;
	}

	public void setBabyWeight(Double babyWeight) {
		this.babyWeight = babyWeight;
	}

	public Integer getGestationalAgeInWeek() {
		return gestationalAgeInWeek;
	}

	public void setGestationalAgeInWeek(Integer gestationalAgeInWeek) {
		this.gestationalAgeInWeek = gestationalAgeInWeek;
	}

	public TypeDetails getMothersPrenatalIntent() {
		return mothersPrenatalIntent;
	}

	public void setMothersPrenatalIntent(TypeDetails mothersPrenatalIntent) {
		this.mothersPrenatalIntent = mothersPrenatalIntent;
	}

	public TypeDetails getParentsKnowledgeOnHmAndLactation() {
		return parentsKnowledgeOnHmAndLactation;
	}

	public void setParentsKnowledgeOnHmAndLactation(TypeDetails parentsKnowledgeOnHmAndLactation) {
		this.parentsKnowledgeOnHmAndLactation = parentsKnowledgeOnHmAndLactation;
	}

	public String getTimeTillFirstExpression() {
		return timeTillFirstExpression;
	}

	public void setTimeTillFirstExpression(String timeTillFirstExpression) {
		this.timeTillFirstExpression = timeTillFirstExpression;
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

	public TypeDetails getInpatientOrOutPatient() {
		return inpatientOrOutPatient;
	}

	public void setInpatientOrOutPatient(TypeDetails inpatientOrOutPatient) {
		this.inpatientOrOutPatient = inpatientOrOutPatient;
	}

	public String getAdmissionDateForOutdoorPatients() {
		return admissionDateForOutdoorPatients;
	}

	public void setAdmissionDateForOutdoorPatients(String admissionDateForOutdoorPatients) {
		this.admissionDateForOutdoorPatients = admissionDateForOutdoorPatients;
	}

	public Date getAdmissionDateForOutdoorPatientsM() {
		return admissionDateForOutdoorPatientsM;
	}

	public void setAdmissionDateForOutdoorPatientsM(Date admissionDateForOutdoorPatientsM) {
		this.admissionDateForOutdoorPatientsM = admissionDateForOutdoorPatientsM;
	}

	public TypeDetails getBabyAdmittedTo() {
		return babyAdmittedTo;
	}

	public void setBabyAdmittedTo(TypeDetails babyAdmittedTo) {
		this.babyAdmittedTo = babyAdmittedTo;
	}

	public String getNicuAdmissionReasonDb() {
		return nicuAdmissionReasonDb;
	}

	public void setNicuAdmissionReasonDb(String nicuAdmissionReasonDb) {
		this.nicuAdmissionReasonDb = nicuAdmissionReasonDb;
	}

	public Integer[] getNicuAdmissionReason() {
		return nicuAdmissionReason;
	}

	public void setNicuAdmissionReason(Integer[] nicuAdmissionReason) {
		this.nicuAdmissionReason = nicuAdmissionReason;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Timestamp getCreatedDateM() {
		return createdDateM;
	}

	public void setCreatedDateM(Timestamp createdDateM) {
		this.createdDateM = createdDateM;
	}

	public Timestamp getUpdatedDateM() {
		return updatedDateM;
	}

	public void setUpdatedDateM(Timestamp updatedDateM) {
		this.updatedDateM = updatedDateM;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public String getDischargeDate() {
		return dischargeDate;
	}

	public void setDischargeDate(String dischargeDate) {
		this.dischargeDate = dischargeDate;
	}

	public Date getDischargeDateM() {
		return dischargeDateM;
	}

	public void setDischargeDateM(Date dischargeDateM) {
		this.dischargeDateM = dischargeDateM;
	}

}
