package org.sdrc.lactation.utils;

import java.util.List;

/**
 * 
 * @author Naseem Akhtar (naseem@sdrc.co.in) on 13th February 2018 1601. This
 *         class will help us to send the response back to the mobile after
 *         synchronization process is complete or interrupted. This will send
 *         response about the no. of forms synced, what is the status of the
 *         http request made etc.
 *
 */

public class SynchronizationResult {

	private Integer usersSynced;
	private Integer usersFailed;
	private Integer patientsSynced;
	private Integer patientsFailed;
	private Integer bfExpressionSynced;
	private Integer bfExpressionFailed;
	private Integer logFeedSynced;
	private Integer logFeedFailed;
	private Integer bfSupportivePracticeSynced;
	private Integer bfSupportivePracticeFailed;
	private Integer bfPostDischargeSynced;
	private Integer bfPostDischargeFailed;
	private Integer statusCode;
	private String message;
	private List<String> rejectedUserList;

	public Integer getPatientsSynced() {
		return patientsSynced;
	}

	public void setPatientsSynced(Integer patientsSynced) {
		this.patientsSynced = patientsSynced;
	}

	public Integer getPatientsFailed() {
		return patientsFailed;
	}

	public void setPatientsFailed(Integer patientsFailed) {
		this.patientsFailed = patientsFailed;
	}

	public Integer getBfExpressionSynced() {
		return bfExpressionSynced;
	}

	public void setBfExpressionSynced(Integer bfExpressionSynced) {
		this.bfExpressionSynced = bfExpressionSynced;
	}

	public Integer getBfExpressionFailed() {
		return bfExpressionFailed;
	}

	public void setBfExpressionFailed(Integer bfExpressionFailed) {
		this.bfExpressionFailed = bfExpressionFailed;
	}

	public Integer getLogFeedSynced() {
		return logFeedSynced;
	}

	public void setLogFeedSynced(Integer logFeedSynced) {
		this.logFeedSynced = logFeedSynced;
	}

	public Integer getLogFeedFailed() {
		return logFeedFailed;
	}

	public void setLogFeedFailed(Integer logFeedFailed) {
		this.logFeedFailed = logFeedFailed;
	}

	public Integer getBfSupportivePracticeSynced() {
		return bfSupportivePracticeSynced;
	}

	public void setBfSupportivePracticeSynced(Integer bfSupportivePracticeSynced) {
		this.bfSupportivePracticeSynced = bfSupportivePracticeSynced;
	}

	public Integer getBfSupportivePracticeFailed() {
		return bfSupportivePracticeFailed;
	}

	public void setBfSupportivePracticeFailed(Integer bfSupportivePracticeFailed) {
		this.bfSupportivePracticeFailed = bfSupportivePracticeFailed;
	}

	public Integer getBfPostDischargeSynced() {
		return bfPostDischargeSynced;
	}

	public void setBfPostDischargeSynced(Integer bfPostDischargeSynced) {
		this.bfPostDischargeSynced = bfPostDischargeSynced;
	}

	public Integer getBfPostDischargeFailed() {
		return bfPostDischargeFailed;
	}

	public void setBfPostDischargeFailed(Integer bfPostDischargeFailed) {
		this.bfPostDischargeFailed = bfPostDischargeFailed;
	}

	public Integer getUsersSynced() {
		return usersSynced;
	}

	public void setUsersSynced(Integer usersSynced) {
		this.usersSynced = usersSynced;
	}

	public Integer getUsersFailed() {
		return usersFailed;
	}

	public void setUsersFailed(Integer usersFailed) {
		this.usersFailed = usersFailed;
	}

	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<String> getRejectedUserList() {
		return rejectedUserList;
	}

	public void setRejectedUserList(List<String> rejectedUserList) {
		this.rejectedUserList = rejectedUserList;
	}

}
