package org.sdrc.lactation.utils;

import java.util.List;

/**
 * 
 * @author Naseem Akhtar (naseem@sdrc.co.in) on 13th February 2018 1601. This
 *         class will help us to send the response back to the mobile after
 *         synchronization process is complete or interrupted. This will send
 *         response about the no. of forms synced, what is the status of the
 *         http request made etc.
 * @author Ratikanta
 */

public class SyncResult {

	private List<FailureUser> failureUsers;
	private List<FailurePatient> failurePatients;
	private List<FailureBFExpression> failureBFExpressions;
	private List<FailureFeedExpression> failureFeedExpressions;
	private List<FailureBFSP> failureBFSPs;
	private List<FailureBFPD> failureBFPDs;

	public List<FailureUser> getFailureUsers() {
		return failureUsers;
	}

	public void setFailureUsers(List<FailureUser> failureUsers) {
		this.failureUsers = failureUsers;
	}

	public List<FailurePatient> getFailurePatients() {
		return failurePatients;
	}

	public void setFailurePatients(List<FailurePatient> failurePatients) {
		this.failurePatients = failurePatients;
	}

	public List<FailureBFExpression> getFailureBFExpressions() {
		return failureBFExpressions;
	}

	public void setFailureBFExpressions(List<FailureBFExpression> failureBFExpressions) {
		this.failureBFExpressions = failureBFExpressions;
	}

	public List<FailureFeedExpression> getFailureFeedExpressions() {
		return failureFeedExpressions;
	}

	public void setFailureFeedExpressions(List<FailureFeedExpression> failureFeedExpressions) {
		this.failureFeedExpressions = failureFeedExpressions;
	}

	public List<FailureBFSP> getFailureBFSPs() {
		return failureBFSPs;
	}

	public void setFailureBFSPs(List<FailureBFSP> failureBFSPs) {
		this.failureBFSPs = failureBFSPs;
	}

	public List<FailureBFPD> getFailureBFPDs() {
		return failureBFPDs;
	}

	public void setFailureBFPDs(List<FailureBFPD> failureBFPDs) {
		this.failureBFPDs = failureBFPDs;
	}

}
