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
 *         domain will be used for capturing data from log feed form, this form
 *         is for a particular baby.
 */

@Entity
@JsonAutoDetect
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LogFeed {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private Timestamp dateAndTimeOfFeed;

	@ManyToOne
	@JoinColumn
	private TypeDetails feedMethod;

	private Integer ommVolume;
	private Integer dhmVolume;
	private Integer formulaVolume;
	private Integer otherVolume;

	public Integer getId() {
		return id;
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

	public Integer getOmmVolume() {
		return ommVolume;
	}

	public void setOmmVolume(Integer ommVolume) {
		this.ommVolume = ommVolume;
	}

	public Integer getDhmVolume() {
		return dhmVolume;
	}

	public void setDhmVolume(Integer dhmVolume) {
		this.dhmVolume = dhmVolume;
	}

	public Integer getFormulaVolume() {
		return formulaVolume;
	}

	public void setFormulaVolume(Integer formulaVolume) {
		this.formulaVolume = formulaVolume;
	}

	public Integer getOtherVolume() {
		return otherVolume;
	}

	public void setOtherVolume(Integer otherVolume) {
		this.otherVolume = otherVolume;
	}

}
