package org.sdrc.lactation.utils;

import java.util.List;

import org.sdrc.lactation.domain.LogBreastFeedingPostDischarge;
import org.sdrc.lactation.domain.LogBreastFeedingSupportivePractice;
import org.sdrc.lactation.domain.LogExpressionBreastFeed;
import org.sdrc.lactation.domain.LogFeed;
import org.sdrc.lactation.domain.Patient;

/**
 * 
 * @author Naseem Akhtar (naseem@sdrc.co.in) on 9th February 2018 19:10. This
 *         model will be used to receive data from the mobile.
 */

public class SynchronizationModel {

	private Patient patient;
	private List<LogExpressionBreastFeed> logExpressionBreastFeedList;
	private List<LogFeed> logFeedList;
	private List<LogBreastFeedingSupportivePractice> logBreastFeedingSupportivePracticeList;
	private List<LogBreastFeedingPostDischarge> logBreastFeedingPostDischargeList;
	private String deviceId;

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public List<LogExpressionBreastFeed> getLogExpressionBreastFeedList() {
		return logExpressionBreastFeedList;
	}

	public void setLogExpressionBreastFeedList(List<LogExpressionBreastFeed> logExpressionBreastFeedList) {
		this.logExpressionBreastFeedList = logExpressionBreastFeedList;
	}

	public List<LogFeed> getLogFeedList() {
		return logFeedList;
	}

	public void setLogFeedList(List<LogFeed> logFeedList) {
		this.logFeedList = logFeedList;
	}

	public List<LogBreastFeedingSupportivePractice> getLogBreastFeedingSupportivePracticeList() {
		return logBreastFeedingSupportivePracticeList;
	}

	public void setLogBreastFeedingSupportivePracticeList(
			List<LogBreastFeedingSupportivePractice> logBreastFeedingSupportivePracticeList) {
		this.logBreastFeedingSupportivePracticeList = logBreastFeedingSupportivePracticeList;
	}

	public List<LogBreastFeedingPostDischarge> getLogBreastFeedingPostDischargeList() {
		return logBreastFeedingPostDischargeList;
	}

	public void setLogBreastFeedingPostDischargeList(
			List<LogBreastFeedingPostDischarge> logBreastFeedingPostDischargeList) {
		this.logBreastFeedingPostDischargeList = logBreastFeedingPostDischargeList;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

}
