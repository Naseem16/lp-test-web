package org.sdrc.lactation.domain;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
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
	@JoinColumn
	private Patient babyId;

	private Timestamp timeOfExpression;

	@ManyToOne
	@JoinColumn
	private TypeDetails methodOfExpression;

	@ManyToOne
	@JoinColumn
	private TypeDetails expressionOccuredLocation;

	@Min(0)
	@Max(300)
	private Integer milkExpressedFromRightBreast;

	@Min(0)
	@Max(300)
	private Integer milkExpressedFromLeftBreast;

	@CreationTimestamp
	private Timestamp createdDate;

	@UpdateTimestamp
	private Timestamp updatedDate;

	public Integer getId() {
		return id;
	}

	public Patient getBabyId() {
		return babyId;
	}

	public void setBabyId(Patient babyId) {
		this.babyId = babyId;
	}

	public Timestamp getTimeOfExpression() {
		return timeOfExpression;
	}

	public void setTimeOfExpression(Timestamp timeOfExpression) {
		this.timeOfExpression = timeOfExpression;
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

	public Integer getMilkExpressedFromRightBreast() {
		return milkExpressedFromRightBreast;
	}

	public void setMilkExpressedFromRightBreast(Integer milkExpressedFromRightBreast) {
		this.milkExpressedFromRightBreast = milkExpressedFromRightBreast;
	}

	public Integer getMilkExpressedFromLeftBreast() {
		return milkExpressedFromLeftBreast;
	}

	public void setMilkExpressedFromLeftBreast(Integer milkExpressedFromLeftBreast) {
		this.milkExpressedFromLeftBreast = milkExpressedFromLeftBreast;
	}

}
