package org.sdrc.lactation.utils;

import java.util.List;

/**
 * 
 * @author Naseem Akhtar (naseem@sdrc.co.in) on 13th February 2018 1601.
 * This class will help us to send the response back to the mobile after synchronization process is complete or interrupted.
 * This will send response about the no. of forms synced, what is the status of the http request made etc.
 *
 */

public class SynchronizationResult {

	private Integer numberOfPatientSynced;
	private Integer numberOfPatientFailed;
	private Boolean status;
	private Integer statusCode;
	private String message;
	private List<String> babyCode;

	public Integer getNumberOfPatientSynced() {
		return numberOfPatientSynced;
	}

	public void setNumberOfPatientSynced(Integer numberOfPatientSynced) {
		this.numberOfPatientSynced = numberOfPatientSynced;
	}

	public Integer getNumberOfPatientFailed() {
		return numberOfPatientFailed;
	}

	public void setNumberOfPatientFailed(Integer numberOfPatientFailed) {
		this.numberOfPatientFailed = numberOfPatientFailed;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
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

	public List<String> getBabyCode() {
		return babyCode;
	}

	public void setBabyCode(List<String> babyCode) {
		this.babyCode = babyCode;
	}

}
