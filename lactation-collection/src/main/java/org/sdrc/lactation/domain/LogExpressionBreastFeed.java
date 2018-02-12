package org.sdrc.lactation.domain;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 
 * @author Naseem Akhtar (naseem@sdrc.co.in) on 6th February 2018 13:10. This
 *         domain will be used for capturing data from log expression breast
 *         feed form, this form is for a particular baby.
 */

@Entity
@JsonAutoDetect
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LogExpressionBreastFeed {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(nullable = false)
	private Patient patientId;

	private String babyCode;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
	private Timestamp dateAndTimeOfExpression;

	@Max(300)
	@Digits(integer = 3, fraction = 2)
	private Double durationOfExpression;

	@ManyToOne
	@JoinColumn
	private TypeDetails methodOfExpression;

	@ManyToOne
	@JoinColumn
	private TypeDetails expressionOccuredLocation;

	@Max(300)
	@Digits(integer = 3, fraction = 2)
	private Double milkExpressedFromRightBreast;

	@Max(300)
	@Digits(integer = 3, fraction = 2)
	private Double milkExpressedFromLeftBreast;

	@CreationTimestamp
	private Timestamp createdDate;

	@UpdateTimestamp
	private Timestamp updatedDate;

	private String createdBy;

	private String updatedBy;

	private String imeiNumberUniqueDeviceId;

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

	public Timestamp getDateAndTimeOfExpression() {
		return dateAndTimeOfExpression;
	}

	public void setDateAndTimeOfExpression(Timestamp dateAndTimeOfExpression) {
		this.dateAndTimeOfExpression = dateAndTimeOfExpression;
	}

	public Double getDurationOfExpression() {
		return durationOfExpression;
	}

	public void setDurationOfExpression(Double durationOfExpression) {
		this.durationOfExpression = durationOfExpression;
	}

	public TypeDetails getMethodOfExpression() {
		return methodOfExpression;
	}

	public void setMethodOfExpression(TypeDetails methodOfExpression) {
		this.methodOfExpression = methodOfExpression;
	}

	public TypeDetails getExpressionOccuredLocation() {
		return expressionOccuredLocation;
	}

	public void setExpressionOccuredLocation(TypeDetails expressionOccuredLocation) {
		this.expressionOccuredLocation = expressionOccuredLocation;
	}

	public Double getMilkExpressedFromRightBreast() {
		return milkExpressedFromRightBreast;
	}

	public void setMilkExpressedFromRightBreast(Double milkExpressedFromRightBreast) {
		this.milkExpressedFromRightBreast = milkExpressedFromRightBreast;
	}

	public Double getMilkExpressedFromLeftBreast() {
		return milkExpressedFromLeftBreast;
	}

	public void setMilkExpressedFromLeftBreast(Double milkExpressedFromLeftBreast) {
		this.milkExpressedFromLeftBreast = milkExpressedFromLeftBreast;
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

	public String getImeiNumberUniqueDeviceId() {
		return imeiNumberUniqueDeviceId;
	}

	public void setImeiNumberUniqueDeviceId(String imeiNumberUniqueDeviceId) {
		this.imeiNumberUniqueDeviceId = imeiNumberUniqueDeviceId;
	}

}
