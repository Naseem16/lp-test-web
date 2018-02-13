package org.sdrc.lactation.domain;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 
 * @author Naseem Akhtar (naseem@sdrc.co.in) on 6th February 2018 13:10. This
 *         domain will be used for capturing data from log breast feeding post
 *         discharge form, this form is for a particular baby.
 */

@Entity
@JsonAutoDetect
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LogBreastFeedingPostDischarge {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(nullable = false)
	private Patient patientId;

	private String babyCode;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
	private Timestamp dateAndTimingOfBreastFeeding;

	@ManyToOne
	@JoinColumn
	private TypeDetails breastFeedingStatus;

	private String imeiNumberUniqueDeviceId;

	@CreationTimestamp
	private Timestamp createdDate;

	@UpdateTimestamp
	private Timestamp updatedDate;

	private String createdBy;

	private String updatedBy;

	public Patient getPatientId() {
		return patientId;
	}

	public void setPatientId(Patient patientId) {
		this.patientId = patientId;
	}

	public String getBabyCode() {
		return babyCode;
	}

	public void setBabyCode(String babyCode) {
		this.babyCode = babyCode;
	}

	public Timestamp getDateAndTimingOfBreastFeeding() {
		return dateAndTimingOfBreastFeeding;
	}

	public void setDateAndTimingOfBreastFeeding(Timestamp dateAndTimingOfBreastFeeding) {
		this.dateAndTimingOfBreastFeeding = dateAndTimingOfBreastFeeding;
	}

	public TypeDetails getBreastFeedingStatus() {
		return breastFeedingStatus;
	}

	public void setBreastFeedingStatus(TypeDetails breastFeedingStatus) {
		this.breastFeedingStatus = breastFeedingStatus;
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

	public Integer getId() {
		return id;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public Timestamp getUpdatedDate() {
		return updatedDate;
	}

	public String getImeiNumberUniqueDeviceId() {
		return imeiNumberUniqueDeviceId;
	}

	public void setImeiNumberUniqueDeviceId(String imeiNumberUniqueDeviceId) {
		this.imeiNumberUniqueDeviceId = imeiNumberUniqueDeviceId;
	}

}
