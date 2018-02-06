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
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 
 * @author Naseem Akhtar (naseem@sdrc.co.in) on 6th February 2018 13:10. This
 *         domain will be used for capturing data filled up in breast feeding
 *         supportive practice form, this is for a particular baby.
 */

@Entity
@JsonAutoDetect
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LogBreastFeedingSupportivePractice {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn
	private Patient babyId;

	private Timestamp dateAndTimeOfBFSP;

	@ManyToOne
	@JoinColumn
	private TypeDetails bfspPerformed;

	@ManyToOne
	@JoinColumn
	private TypeDetails personWhoPerformedBFSP;

	@Digits(integer = 4, fraction = 2)
	private Double bfspDuration;

	@CreationTimestamp
	private Timestamp createdDate;

	@UpdateTimestamp
	private Timestamp updatedDate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Patient getBabyId() {
		return babyId;
	}

	public void setBabyId(Patient babyId) {
		this.babyId = babyId;
	}

	public Timestamp getDateAndTimeOfBFSP() {
		return dateAndTimeOfBFSP;
	}

	public void setDateAndTimeOfBFSP(Timestamp dateAndTimeOfBFSP) {
		this.dateAndTimeOfBFSP = dateAndTimeOfBFSP;
	}

	public TypeDetails getBfspPerformed() {
		return bfspPerformed;
	}

	public void setBfspPerformed(TypeDetails bfspPerformed) {
		this.bfspPerformed = bfspPerformed;
	}

	public TypeDetails getPersonWhoPerformedBFSP() {
		return personWhoPerformedBFSP;
	}

	public void setPersonWhoPerformedBFSP(TypeDetails personWhoPerformedBFSP) {
		this.personWhoPerformedBFSP = personWhoPerformedBFSP;
	}

	public Double getBsfpDuration() {
		return bfspDuration;
	}

	public void setBsfpDuration(Double bfspDuration) {
		this.bfspDuration = bfspDuration;
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
