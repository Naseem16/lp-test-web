package org.sdrc.lactation.domain;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Digits;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 
 * @author Naseem Akhtar (naseem@sdrc.co.in) on 5th February 2018 17:10. This
 *         domain will be used for new patient registration.
 */

@Entity
public class Patient {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer patientId;

	@Column(nullable = false, unique = true)
	private String babyCode;

	private Integer mothersAge;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
	private Timestamp deliveryDateAndTime;

	@ManyToOne
	@JoinColumn(name = "delivery_method")
	private TypeDetails deliveryMethod;

	@Digits(integer = 4, fraction = 2)
	private Double babyWeight;

	private Integer gestationalAgeInWeek;

	@ManyToOne
	@JoinColumn
	private TypeDetails mothersParentalIntent;

	@ManyToOne
	@JoinColumn
	private TypeDetails parentsKnowledgeOnHmAndLactation;

	private Double timeTillFirstExpression;

	@ManyToOne
	@JoinColumn
	private TypeDetails inpatientOrOutPatient;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date admissionDateForOutdoorPatients;

	@ManyToOne
	@JoinColumn
	private TypeDetails babyAdmittedTo;

	@ManyToOne
	@JoinColumn
	private TypeDetails nicuAdmissionReason;

	@CreationTimestamp
	private Timestamp createdDate;

	@UpdateTimestamp
	private Timestamp updatedDate;

	private String createdBy;

	private String updatedBy;

	public Patient() {

	}

	public Patient(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getPatientId() {
		return patientId;
	}
	
	public String getBabyCode() {
		return babyCode;
	}

	public void setBabyCode(String babyCode) {
		this.babyCode = babyCode;
	}

	public Integer getMothersAge() {
		return mothersAge;
	}

	public void setMothersAge(Integer mothersAge) {
		this.mothersAge = mothersAge;
	}

	public Timestamp getDeliveryDateAndTime() {
		return deliveryDateAndTime;
	}

	public void setDeliveryDateAndTime(Timestamp deliveryDateAndTime) {
		this.deliveryDateAndTime = deliveryDateAndTime;
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

	public TypeDetails getMothersParentalIntent() {
		return mothersParentalIntent;
	}

	public void setMothersParentalIntent(TypeDetails mothersParentalIntent) {
		this.mothersParentalIntent = mothersParentalIntent;
	}

	public TypeDetails getParentsKnowledgeOnHmAndLactation() {
		return parentsKnowledgeOnHmAndLactation;
	}

	public void setParentsKnowledgeOnHmAndLactation(TypeDetails parentsKnowledgeOnHmAndLactation) {
		this.parentsKnowledgeOnHmAndLactation = parentsKnowledgeOnHmAndLactation;
	}

	public Double getTimeTillFirstExpression() {
		return timeTillFirstExpression;
	}

	public void setTimeTillFirstExpression(Double timeTillFirstExpression) {
		this.timeTillFirstExpression = timeTillFirstExpression;
	}

	public TypeDetails getInpatientOrOutPatient() {
		return inpatientOrOutPatient;
	}

	public void setInpatientOrOutPatient(TypeDetails inpatientOrOutPatient) {
		this.inpatientOrOutPatient = inpatientOrOutPatient;
	}

	public Date getAdmissionDateForOutdoorPatients() {
		return admissionDateForOutdoorPatients;
	}

	public void setAdmissionDateForOutdoorPatients(Date admissionDateForOutdoorPatients) {
		this.admissionDateForOutdoorPatients = admissionDateForOutdoorPatients;
	}

	public TypeDetails getBabyAdmittedTo() {
		return babyAdmittedTo;
	}

	public void setBabyAdmittedTo(TypeDetails babyAdmittedTo) {
		this.babyAdmittedTo = babyAdmittedTo;
	}

	public TypeDetails getNicuAdmissionReason() {
		return nicuAdmissionReason;
	}

	public void setNicuAdmissionReason(TypeDetails nicuAdmissionReason) {
		this.nicuAdmissionReason = nicuAdmissionReason;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public Timestamp getUpdatedDate() {
		return updatedDate;
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

}
