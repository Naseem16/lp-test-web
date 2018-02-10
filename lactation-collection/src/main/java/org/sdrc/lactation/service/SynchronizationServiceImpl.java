package org.sdrc.lactation.service;

import java.util.List;

import org.sdrc.lactation.domain.LactationUser;
import org.sdrc.lactation.domain.Patient;
import org.sdrc.lactation.repository.LactationUserRepository;
import org.sdrc.lactation.repository.LogBreastFeedingPostDischargeRepository;
import org.sdrc.lactation.repository.LogBreastFeedingSupportivePracticeRepository;
import org.sdrc.lactation.repository.LogExpressionBreastFeedRepository;
import org.sdrc.lactation.repository.LogFeedRepository;
import org.sdrc.lactation.repository.PatientRepository;
import org.sdrc.lactation.utils.SynchronizationModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.stereotype.Service;

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

	@Override
	public LactationUser registerUser(String firstName, String password) {
		LactationUser user = new LactationUser();
		user.setEmail(firstName + "@gmail.com");
		user.setFirstName(firstName);
		user.setPassword(messageDigestPasswordEncoder.encodePassword(user.getEmail(), password));

		lactationUserRepository.save(user);

		return user;
	}

	/**
	 * @naseem this method needs internal comments
	 */
	@Override
	public ResponseEntity<?> synchronizeForms(List<SynchronizationModel> synchronizationModels,
			HttpRequest httpRequest) {

		synchronizationModels.forEach(patientData -> {
			Patient baby = patientRepository.findByBabyCode(patientData.getPatient().getBabyCode());
			Patient babyFromDb = baby == null ? patientRepository.save(baby) : baby;

			if (patientData.getLogBreastFeedingPostDischargeList() != null
					&& !patientData.getLogBreastFeedingPostDischargeList().isEmpty()) {
				patientData.getLogBreastFeedingPostDischargeList().forEach(logExpBfPostDischarge -> {
					logExpBfPostDischarge.setPatientId(new Patient(babyFromDb.getPatientId()));
					logExpBfPostDischarge.setBabyCode(babyFromDb.getBabyCode());
				});
				logBreastFeedingPostDischargeRepository.save(patientData.getLogBreastFeedingPostDischargeList());
			}

			if (patientData.getLogBreastFeedingSupportivePracticeList() != null
					&& !patientData.getLogBreastFeedingSupportivePracticeList().isEmpty()) {
				patientData.getLogBreastFeedingSupportivePracticeList().forEach(logBfSuppPractice -> {
					logBfSuppPractice.setPatientId(new Patient(babyFromDb.getPatientId()));
					logBfSuppPractice.setBabyCode(babyFromDb.getBabyCode());
				});
				logBreastFeedingSupportivePracticeRepository
						.save(patientData.getLogBreastFeedingSupportivePracticeList());
			}

			if (patientData.getLogExpressionBreastFeedList() != null
					&& !patientData.getLogExpressionBreastFeedList().isEmpty()) {
				patientData.getLogExpressionBreastFeedList().forEach(logExpBf -> {
					logExpBf.setPatientId(new Patient(babyFromDb.getPatientId()));
					logExpBf.setBabyCode(babyFromDb.getBabyCode());
				});
				logExpressionBreastFeedRepository.save(patientData.getLogExpressionBreastFeedList());
			}

			if (patientData.getLogFeedList() != null && !patientData.getLogFeedList().isEmpty()) {
				patientData.getLogFeedList().forEach(logFeed -> {
					logFeed.setPatientId(new Patient(babyFromDb.getPatientId()));
					logFeed.setBabyCode(babyFromDb.getBabyCode());
				});
				logFeedRepository.save(patientData.getLogFeedList());
			}
		});
		return null;
	}

}
