package org.sdrc.lactation.service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sdrc.lactation.domain.Area;
import org.sdrc.lactation.domain.LactationUser;
import org.sdrc.lactation.domain.Patient;
import org.sdrc.lactation.domain.TypeDetails;
import org.sdrc.lactation.repository.AreaRepository;
import org.sdrc.lactation.repository.LactationUserRepository;
import org.sdrc.lactation.repository.LogBreastFeedingPostDischargeRepository;
import org.sdrc.lactation.repository.LogBreastFeedingSupportivePracticeRepository;
import org.sdrc.lactation.repository.LogExpressionBreastFeedRepository;
import org.sdrc.lactation.repository.LogFeedRepository;
import org.sdrc.lactation.repository.PatientRepository;
import org.sdrc.lactation.repository.TypeDetailsRepository;
import org.sdrc.lactation.utils.FailurePatient;
import org.sdrc.lactation.utils.FailureUser;
import org.sdrc.lactation.utils.SyncModel;
import org.sdrc.lactation.utils.SyncResult;
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
 * @author Ratikanta        
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
	private AreaRepository areaRepository;
	
	@Autowired
	private TypeDetailsRepository typeDetailsRepository;
	
	private SimpleDateFormat sdfddMMyyyy = new SimpleDateFormat("dd-MM-yyyy");

	@Autowired
	private LogBreastFeedingSupportivePracticeRepository logBreastFeedingSupportivePracticeRepository;

	/**
	 * @author Naseem Akhtar (naseem@sdrc.co.in) on 12th February 2018 1548.
	 *         This method will receive the forms in form of
	 *         SynchronizationModel. This method will accept List of
	 *         SynchronizationModel which would contain list of users for
	 *         registration purpose and list of data related to a particular
	 *         baby.
	 *  @author Ratikanta       
	 */
	@Override
	@Transactional
	public SyncResult synchronizeForms(SyncModel syncModels, HttpRequest httpRequest) {

		SyncResult syncResult = new SyncResult();

		try {
			
			//getting area 
			Map<Integer, Area> areaMap = new HashMap<Integer, Area>();
			areaRepository.findAll().forEach(area->areaMap.put(area.getId(), area));
			
			//getting type details
			Map<Integer, TypeDetails> typeDetailsMap = new HashMap<Integer, TypeDetails>();
			typeDetailsRepository.findAll().forEach(typeDetails->typeDetailsMap.put(typeDetails.getId(), typeDetails));
			
			
			//Saving users
			if (syncModels.getUsers() != null
					&& !syncModels.getUsers().isEmpty()) {
						
				List<FailureUser> faliureUsers = new ArrayList<>();
				syncModels.getUsers().forEach(user -> {
					LactationUser existingUser = lactationUserRepository.findByEmail(user.getEmail());
					if(existingUser != null){
						FailureUser failureUser = new FailureUser();
						failureUser.setUserId(user.getEmail());
						failureUser.setReasonOfFailure("User exists!");
						faliureUsers.add(failureUser);
					}else{
						LactationUser lUser = new LactationUser();
						lUser.setFirstName(user.getFirstName());
						lUser.setLastName(user.getLastName());
						lUser.setEmail(user.getEmail());
						lUser.setCountry(areaMap.get(user.getCountry()));
						lUser.setState(areaMap.get(user.getState()));
						lUser.setDistrict(areaMap.get(user.getDistrict()));
						lUser.setInstitutionName((areaMap.get(user.getInstitution())));
						lactationUserRepository.save(lUser);
					}
				});
				
				syncResult.setFailureUsers(faliureUsers);
			}

			//Saving patients
			
			
			if (syncModels.getPatients() != null
					&& !syncModels.getPatients().isEmpty()) {
			
				//getting patients from database
				List<String> babyCodeList = new ArrayList<String>();
				syncModels.getPatients().forEach(patient -> babyCodeList.add(patient.getBabyCode()));
				List<Patient> existingPatients = patientRepository.findByINBababyCode(babyCodeList);
				Map<String, Patient> patientMap = new HashMap<String, Patient>();
				existingPatients.forEach(patient->patientMap.put(patient.getBabyCode(), patient));
				
				
				
				syncModels.getPatients().forEach(patient -> {
					Patient existingPatient = patientMap.get(patient.getBabyCode());
					java.sql.Date date = null;
					try{
						if(patient.getAdmissionDateForOutdoorPatients() != null){
							date = new java.sql.Date(sdfddMMyyyy.parse(patient.getAdmissionDateForOutdoorPatients()).getTime());
						}
					} catch(ParseException e){
						e.printStackTrace();
					}
					if(existingPatient != null){
						
						existingPatient.setAdmissionDateForOutdoorPatients(date);
						existingPatient.setBabyAdmittedTo(typeDetailsMap.get(patient.getBabyAdmittedTo()));
						existingPatient.setBabyCodeHospital(patient.getBabyCodeHospital());
						existingPatient.setBabyOf(patient.getBabyOf());
						existingPatient.setBabyWeight(patient.getBabyWeight());
						existingPatient.setDeliveryDateAndTime(getDeliveryDateAndTime(patient.getDeliveryDate(), patient.getDeliveryTime()));
						existingPatient.setDeliveryMethod(typeDetailsMap.get(patient.getDeliveryMethod()));
						existingPatient.setGestationalAgeInWeek(patient.getGestationalAgeInWeek());
						existingPatient.setInpatientOrOutPatient(typeDetailsMap.get(patient.getInpatientOrOutPatient()));
						existingPatient.setMothersAge(patient.getMothersAge());
						existingPatient.setMothersPrenatalIntent(typeDetailsMap.get(patient.getMothersPrenatalIntent()));
						existingPatient.setNicuAdmissionReason(typeDetailsMap.get(patient.getNicuAdmissionReason()));
						existingPatient.setParentsKnowledgeOnHmAndLactation(
								typeDetailsMap.get(patient.getParentsKnowledgeOnHmAndLactation()));
						existingPatient.setTimeTillFirstExpression(patient.getTimeTillFirstExpression());
						existingPatient.setUpdatedBy(patient.getUserId());
						patientRepository.save(existingPatient);
						
					}else {
						Patient newPatient = new Patient();
						newPatient.setBabyCode(patient.getBabyCode());
						newPatient.setAdmissionDateForOutdoorPatients(date);
						newPatient.setBabyAdmittedTo(typeDetailsMap.get(patient.getBabyAdmittedTo()));
						newPatient.setBabyCodeHospital(patient.getBabyCodeHospital());
						newPatient.setBabyOf(patient.getBabyOf());
						newPatient.setBabyWeight(patient.getBabyWeight());
						newPatient.setDeliveryDateAndTime(getDeliveryDateAndTime(patient.getDeliveryDate(), patient.getDeliveryTime()));
						newPatient.setDeliveryMethod(typeDetailsMap.get(patient.getDeliveryMethod()));
						newPatient.setGestationalAgeInWeek(patient.getGestationalAgeInWeek());
						newPatient.setInpatientOrOutPatient(typeDetailsMap.get(patient.getInpatientOrOutPatient()));
						newPatient.setMothersAge(patient.getMothersAge());
						newPatient.setMothersPrenatalIntent(typeDetailsMap.get(patient.getMothersPrenatalIntent()));
						newPatient.setNicuAdmissionReason(typeDetailsMap.get(patient.getNicuAdmissionReason()));
						newPatient.setParentsKnowledgeOnHmAndLactation(
								typeDetailsMap.get(patient.getParentsKnowledgeOnHmAndLactation()));
						newPatient.setTimeTillFirstExpression(patient.getTimeTillFirstExpression());
						newPatient.setCreatedBy(patient.getUserId());
						patientRepository.save(newPatient);
					}
				});
				
				List<FailurePatient> faliurePatients = new ArrayList<>();
				syncResult.setFailurePatients(faliurePatients);
			}
			/*
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

					

					

					

					
				});
			}
			
			//Saving BF expression
			if (patientData.getLogExpressionBreastFeedList() != null
					&& !patientData.getLogExpressionBreastFeedList().isEmpty()) {
				patientData.getLogExpressionBreastFeedList().forEach(logExpBf -> {
					logExpBf.setPatientId(new Patient(babyFromDb.getPatientId()));
					logExpBf.setBabyCode(babyFromDb.getBabyCode());
					logExpBf.setDeviceId(synchronizationModels.getDeviceId());
				});
				logExpressionBreastFeedRepository.save(patientData.getLogExpressionBreastFeedList());
			}
			
			//Saving feed expression
			if (patientData.getLogFeedList() != null && !patientData.getLogFeedList().isEmpty()) {
				patientData.getLogFeedList().forEach(logFeed -> {
					logFeed.setPatientId(new Patient(babyFromDb.getPatientId()));
					logFeed.setBabyCode(babyFromDb.getBabyCode());
					logFeed.setDeviceId(synchronizationModels.getDeviceId());
				});
				logFeedRepository.save(patientData.getLogFeedList());
			}
			
			
			
			//Saving BFSP
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
			
			//Saving BFPD
			if (patientData.getLogBreastFeedingPostDischargeList() != null
					&& !patientData.getLogBreastFeedingPostDischargeList().isEmpty()) {
				patientData.getLogBreastFeedingPostDischargeList().forEach(logExpBfPostDischarge -> {
					logExpBfPostDischarge.setPatientId(new Patient(babyFromDb.getPatientId()));
					logExpBfPostDischarge.setBabyCode(babyFromDb.getBabyCode());
					logExpBfPostDischarge.setDeviceId(synchronizationModels.getDeviceId());
				});
				logBreastFeedingPostDischargeRepository
						.save(patientData.getLogBreastFeedingPostDischargeList());
			}*/
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return syncResult;
	}
	
	private Timestamp getDeliveryDateAndTime(String date, String time){
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		try {
			return new Timestamp(sdf.parse(date+ " " + time).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

}
