package org.sdrc.lactation.domain;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Digits;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 
 * @author Naseem Akhtar (naseem@sdrc.co.in) on 6th February 2018 13:10. This
 *         domain will be used for capturing data from log feed form, this form
 *         is for a particular baby.
 */

@Entity
@JsonAutoDetect
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LogFeed {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	private Integer id;

	@ManyToOne
	@JoinColumn(nullable = false)
	private Patient patientId;

	private String babyCode;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
	private Timestamp dateAndTimeOfFeed;

	@ManyToOne
	@JoinColumn
	private TypeDetails feedMethod;

	@Digits(integer = 4, fraction = 2)
	private Double ommVolume;

	@Digits(integer = 4, fraction = 2)
	private Double dhmVolume;

	@Digits(integer = 4, fraction = 2)
	private Double formulaVolume;

	@Digits(integer = 4, fraction = 2)
	private Double animalMilkVolume;

	@Digits(integer = 4, fraction = 2)
	private Double otherVolume;

	@Digits(integer = 5, fraction = 2)
	private Double weightOfBaby;

	@CreationTimestamp
	private Timestamp createdDate;

	@UpdateTimestamp
	private Timestamp updatedDate;

	private String createdBy;

	private String updatedBy;

	private String deviceId;

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

	public Timestamp getDateAndTimeOfFeed() {
		return dateAndTimeOfFeed;
	}

	public void setDateAndTimeOfFeed(Timestamp dateAndTimeOfFeed) {
		this.dateAndTimeOfFeed = dateAndTimeOfFeed;
	}

	public TypeDetails getFeedMethod() {
		return feedMethod;
	}

	public void setFeedMethod(TypeDetails feedMethod) {
		this.feedMethod = feedMethod;
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

	public Double getOtherVolume() {
		return otherVolume;
	}

	public void setOtherVolume(Double otherVolume) {
		this.otherVolume = otherVolume;
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

	public Double getAnimalMilkVolume() {
		return animalMilkVolume;
	}

	public void setAnimalMilkVolume(Double animalMilkVolume) {
		this.animalMilkVolume = animalMilkVolume;
	}

	public Double getWeightOfBaby() {
		return weightOfBaby;
	}

	public void setWeightOfBaby(Double weightOfBaby) {
		this.weightOfBaby = weightOfBaby;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

}
