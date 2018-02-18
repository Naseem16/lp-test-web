package org.sdrc.lactation.utils;

import java.util.List;


/**
 * 
 * @author Naseem Akhtar (naseem@sdrc.co.in) on 9th February 2018 19:10. This
 *         model will be used to receive data from the mobile.
 *  @author Ratikanta 
 */

public class SyncModel {

	private List<UserModel> users;
	private List<PatientModel> patients;

	public List<UserModel> getUsers() {
		return users;
	}

	public void setUsers(List<UserModel> users) {
		this.users = users;
	}

	public List<PatientModel> getPatients() {
		return patients;
	}

	public void setPatients(List<PatientModel> patients) {
		this.patients = patients;
	}	
	
}
