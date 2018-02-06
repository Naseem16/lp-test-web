package org.sdrc.lactation.domain;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * 
 * @author Naseem Akhtar (naseem@sdrc.co.in) on 5th February 2018 17:10. This
 *         domain will be used for new patient registration.
 */

@Entity
public class Patient {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false, unique = true)
	private String babyId;
	
	private Integer mothersAge;
	
	private Timestamp deliveryDate;
	
	private Time deliveryTime;
	
	@ManyToOne
	@JoinColumn(name = "delivery_method")
	private TypeDetails deliveryMethod;
	
	private Integer babyWeight;
	
	private Integer gestationalAgeInWeek;
	
	private Integer mothersParentalIntent;
	
	@ManyToOne
	@JoinColumn(name = "parents_knowledge_on_hm_lactation")
	private TypeDetails parentsKnowledgeOnHmAndLactation;
	
	private Double timeTillFirstExpression;
	
	@ManyToOne
	@JoinColumn(name = "inpatient_or_outpatient")
	private TypeDetails inpatientOrOutPatient;
	
	private Date admissionDateForOutdoorPatients;
	
	@ManyToOne
	@JoinColumn(name = "baby_admitted_to")
	private TypeDetails babyAdmittedTo;
	
	@ManyToOne
	@JoinColumn(name = "nicu_admission_reason")
	private TypeDetails nicuAdmissionReason;
	
	@CreationTimestamp
	private Timestamp createdDate;
	
	@UpdateTimestamp
	private Timestamp updatedDate;

	public Integer getId() {
		return id;
	}

	public String getBabyId() {
		return babyId;
	}

	public void setBabyId(String babyId) {
		this.babyId = babyId;
	}

	public Integer getMothersAge() {
		return mothersAge;
	}

	public void setMothersAge(Integer mothersAge) {
		this.mothersAge = mothersAge;
	}

	public Timestamp getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Timestamp deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public Time getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(Time deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public TypeDetails getDeliveryMethod() {
		return deliveryMethod;
	}

	public void setDeliveryMethod(TypeDetails deliveryMethod) {
		this.deliveryMethod = deliveryMethod;
	}

	public Integer getBabyWeight() {
		return babyWeight;
	}

	public void setBabyWeight(Integer babyWeight) {
		this.babyWeight = babyWeight;
	}

	public Integer getGestationalAgeInWeek() {
		return gestationalAgeInWeek;
	}

	public void setGestationalAgeInWeek(Integer gestationalAgeInWeek) {
		this.gestationalAgeInWeek = gestationalAgeInWeek;
	}

	public Integer getMothersParentalIntent() {
		return mothersParentalIntent;
	}

	public void setMothersParentalIntent(Integer mothersParentalIntent) {
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

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public Timestamp getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}

}
