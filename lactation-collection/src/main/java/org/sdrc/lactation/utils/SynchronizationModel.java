package org.sdrc.lactation.utils;

import java.util.List;

import org.sdrc.lactation.domain.LactationUser;

/**
 * 
 * @author Naseem Akhtar (naseem@sdrc.co.in) on 9th February 2018 19:10. This
 *         model will be used to receive data from the mobile.
 */

public class SynchronizationModel {

	private List<LactationUser> lactationUserList;
	private List<PatientSynchronizationModel> patientSynchronizationModelList;
	private String deviceId;

	public List<LactationUser> getLactationUserList() {
		return lactationUserList;
	}

	public void setLactationUserList(List<LactationUser> lactationUserList) {
		this.lactationUserList = lactationUserList;
	}

	public List<PatientSynchronizationModel> getPatientSynchronizationModelList() {
		return patientSynchronizationModelList;
	}

	public void setPatientSynchronizationModelList(List<PatientSynchronizationModel> patientSynchronizationModelList) {
		this.patientSynchronizationModelList = patientSynchronizationModelList;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

}
