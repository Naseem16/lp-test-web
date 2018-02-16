package org.sdrc.lactation.service;

import org.sdrc.lactation.domain.LactationUser;
import org.sdrc.lactation.domain.Patient;
import org.sdrc.lactation.repository.LactationUserRepository;
import org.sdrc.lactation.repository.LogBreastFeedingPostDischargeRepository;
import org.sdrc.lactation.repository.LogBreastFeedingSupportivePracticeRepository;
import org.sdrc.lactation.repository.LogExpressionBreastFeedRepository;
import org.sdrc.lactation.repository.LogFeedRepository;
import org.sdrc.lactation.repository.PatientRepository;
import org.sdrc.lactation.utils.SynchronizationModel;
import org.sdrc.lactation.utils.SynchronizationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author Naseem Akhtar (naseem@sdrc.co.in) on 9th February 2018 17:10. This
 *         service will be used to write synchronization logic which will handle
 *         data coming from the mobile.
 */

@Service
public class SynchronizationServiceImpl implements SynchronizationService {

	@Autowired
	private MessageDigestPasswordEncoder messageDigestPasswordEncoder;

	@Autowired
	private LactationUserRepository lactationUserRepository;

	@Autowired
	private PatientRepository patientRepository;

	@Autowired
	private LogExpressionBreastFeedRepository logExpressionBreastFeedRepository;

	@Autowired
	private LogBreastFeedingPostDischargeRepository logBreastFeedingPostDischargeRepository;

	@Autowired
	private LogFeedRepository logFeedRepository;

	@Autowired
	private LogBreastFeedingSupportivePracticeRepository logBreastFeedingSupportivePracticeRepository;

	/**
	 * @author Naseem Akhtar (naseem@sdrc.co.in) on 12th February 2018 1548.
	 *         This method will receive the forms in form of
	 *         SynchronizationModel. This method will accept List of
	 *         SynchronizationModel which would contain list of users for
	 *         registration purpose and list of data related to a particular
	 *         baby.
	 */
	@Override
	@Transactional
	public SynchronizationResult synchronizeForms(SynchronizationModel synchronizationModels, HttpRequest httpRequest) {

		SynchronizationResult synchronizationResult = new SynchronizationResult();

		try {
			if (synchronizationModels.getLactationUserList() != null
					&& !synchronizationModels.getLactationUserList().isEmpty()) {
								
				synchronizationModels.getLactationUserList().forEach(user -> {
					LactationUser existingUser = lactationUserRepository.findByEmail(user.getEmail());
					if(existingUser != null && !user.getIsSynced()){
						existingUser.setCountry(user.getCountry());
						existingUser.setDistrict(user.getDistrict());
						existingUser.setFirstName(user.getFirstName());
						existingUser.setInstitutionName(user.getInstitutionName());
						existingUser.setLastName(user.getLastName());
						existingUser.setState(user.getState());
					}else if(!user.getIsSynced()){
						user.setPassword(messageDigestPasswordEncoder.encodePassword(user.getEmail(), user.getPassword()));
						lactationUserRepository.save(user);
					}
				});
			}

			if (synchronizationModels.getPatientSynchronizationModelList() != null
					&& !synchronizationModels.getPatientSynchronizationModelList().isEmpty()) {
				synchronizationModels.getPatientSynchronizationModelList().forEach(patientData -> {
					Patient baby = patientRepository.findByBabyCode(patientData.getPatient().getBabyCode());
					if (baby != null && !baby.getIsSynced()) {
						baby.setAdmissionDateForOutdoorPatients(
								patientData.getPatient().getAdmissionDateForOutdoorPatients());
						baby.setBabyAdmittedTo(patientData.getPatient().getBabyAdmittedTo());
						baby.setBabyCodeHospital(patientData.getPatient().getBabyCodeHospital());
						baby.setBabyOf(patientData.getPatient().getBabyOf());
						baby.setBabyWeight(patientData.getPatient().getBabyWeight());
						baby.setDeliveryDateAndTime(patientData.getPatient().getDeliveryDateAndTime());
						baby.setDeliveryMethod(patientData.getPatient().getDeliveryMethod());
						baby.setGestationalAgeInWeek(patientData.getPatient().getGestationalAgeInWeek());
						baby.setDeviceId(synchronizationModels.getDeviceId());
						baby.setInpatientOrOutPatient(patientData.getPatient().getInpatientOrOutPatient());
						baby.setMothersAge(patientData.getPatient().getMothersAge());
						baby.setMothersPrenatalIntent(patientData.getPatient().getMothersPrenatalIntent());
						baby.setNicuAdmissionReason(patientData.getPatient().getNicuAdmissionReason());
						baby.setParentsKnowledgeOnHmAndLactation(
								patientData.getPatient().getParentsKnowledgeOnHmAndLactation());
						baby.setTimeTillFirstExpression(patientData.getPatient().getTimeTillFirstExpression());
						baby.setUpdatedBy(patientData.getPatient().getUpdatedBy());
					}
					Patient babyFromDb = baby == null ? patientRepository.save(patientData.getPatient()) : baby;

					if (patientData.getLogBreastFeedingPostDischargeList() != null
							&& !patientData.getLogBreastFeedingPostDischargeList().isEmpty()) {
						patientData.getLogBreastFeedingPostDischargeList().forEach(logExpBfPostDischarge -> {
							logExpBfPostDischarge.setPatientId(new Patient(babyFromDb.getPatientId()));
							logExpBfPostDischarge.setBabyCode(babyFromDb.getBabyCode());
							logExpBfPostDischarge.setDeviceId(synchronizationModels.getDeviceId());
						});
						logBreastFeedingPostDischargeRepository
								.save(patientData.getLogBreastFeedingPostDischargeList());
					}

					if (patientData.getLogBreastFeedingSupportivePracticeList() != null
							&& !patientData.getLogBreastFeedingSupportivePracticeList().isEmpty()) {
						patientData.getLogBreastFeedingSupportivePracticeList().forEach(logBfSuppPractice -> {
							logBfSuppPractice.setPatientId(new Patient(babyFromDb.getPatientId()));
							logBfSuppPractice.setBabyCode(babyFromDb.getBabyCode());
							logBfSuppPractice.setDeviceId(synchronizationModels.getDeviceId());
						});
						logBreastFeedingSupportivePracticeRepository
								.save(patientData.getLogBreastFeedingSupportivePracticeList());
					}

					if (patientData.getLogExpressionBreastFeedList() != null
							&& !patientData.getLogExpressionBreastFeedList().isEmpty()) {
						patientData.getLogExpressionBreastFeedList().forEach(logExpBf -> {
							logExpBf.setPatientId(new Patient(babyFromDb.getPatientId()));
							logExpBf.setBabyCode(babyFromDb.getBabyCode());
							logExpBf.setDeviceId(synchronizationModels.getDeviceId());
						});
						logExpressionBreastFeedRepository.save(patientData.getLogExpressionBreastFeedList());
					}

					if (patientData.getLogFeedList() != null && !patientData.getLogFeedList().isEmpty()) {
						patientData.getLogFeedList().forEach(logFeed -> {
							logFeed.setPatientId(new Patient(babyFromDb.getPatientId()));
							logFeed.setBabyCode(babyFromDb.getBabyCode());
							logFeed.setDeviceId(synchronizationModels.getDeviceId());
						});
						logFeedRepository.save(patientData.getLogFeedList());
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		synchronizationResult.setBfExpressionFailed(0);
		synchronizationResult.setBfExpressionSynced(1);
		synchronizationResult.setBfPostDischargeFailed(0);
		synchronizationResult.setBfPostDischargeSynced(1);
		synchronizationResult.setBfSupportivePracticeFailed(0);
		synchronizationResult.setBfSupportivePracticeSynced(1);
		synchronizationResult.setLogFeedFailed(0);
		synchronizationResult.setLogFeedSynced(1);
		synchronizationResult.setMessage("Successfull");
		synchronizationResult.setPatientsFailed(0);
		synchronizationResult.setPatientsSynced(1);
		synchronizationResult.setRejectedUserList(null);
		synchronizationResult.setStatusCode(1);
		synchronizationResult.setUsersFailed(0);
		synchronizationResult.setUsersSynced(1);

		return synchronizationResult;
	}

}
