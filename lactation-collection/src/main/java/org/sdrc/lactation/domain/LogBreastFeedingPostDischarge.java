package org.sdrc.lactation.domain;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 
 * @author Naseem Akhtar (naseem@sdrc.co.in) on 6th February 2018 13:10. This
 *         domain will be used for capturing data from log breast
 *         feeding post discharge form, this form is for a particular baby.
 */

@Entity
@JsonAutoDetect
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LogBreastFeedingPostDischarge {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private Timestamp dateAndTimingOfBreastFeeding;

	@ManyToOne
	@JoinColumn
	private TypeDetails breastFeedingStatus;

	public Integer getId() {
		return id;
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

}
