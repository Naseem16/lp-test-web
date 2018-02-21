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
import org.sdrc.lactation.domain.LogBreastFeedingPostDischarge;
import org.sdrc.lactation.domain.LogBreastFeedingSupportivePractice;
import org.sdrc.lactation.domain.LogExpressionBreastFeed;
import org.sdrc.lactation.domain.LogFeed;
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
import org.sdrc.lactation.utils.FailureBFExpression;
import org.sdrc.lactation.utils.FailureBFPD;
import org.sdrc.lactation.utils.FailureBFSP;
import org.sdrc.lactation.utils.FailureFeedExpression;
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
			List<LactationUser> users = new ArrayList<LactationUser>();
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
					users.add(lUser);
					
				}
			});
			lactationUserRepository.save(users);
			
			syncResult.setFailureUsers(faliureUsers);
		}

		
		//getting patients from database
		Map<String, Patient> patientMap = new HashMap<String, Patient>();
		List<String> babyCodeList = new ArrayList<String>();
		
		if (syncModels.getPatients() != null
				&& !syncModels.getPatients().isEmpty()) {
			syncModels.getPatients().forEach(patient -> babyCodeList.add(patient.getBabyCode()));
		}
		if (syncModels.getBfExpressions() != null
				&& !syncModels.getBfExpressions().isEmpty()) {
			syncModels.getBfExpressions().forEach(bfExpression -> babyCodeList.add(bfExpression.getBabyCode()));					
		}
		
		if (syncModels.getFeedExpressions() != null
				&& !syncModels.getFeedExpressions().isEmpty()) {
			syncModels.getFeedExpressions().forEach(feedExpression -> babyCodeList.add(feedExpression.getBabyCode()));					
		}
		
		if (syncModels.getBfsps() != null
				&& !syncModels.getBfsps().isEmpty()) {
			syncModels.getBfsps().forEach(bfsp -> babyCodeList.add(bfsp.getBabyCode()));					
		}
		
		if (syncModels.getBfpds() != null
				&& !syncModels.getBfpds().isEmpty()) {
			syncModels.getBfpds().forEach(bfpd -> babyCodeList.add(bfpd.getBabyCode()));					
		}
		if(!babyCodeList.isEmpty()){
			List<Patient> existingPatients = patientRepository.findByINBabyCode(babyCodeList);
			existingPatients.forEach(patient->patientMap.put(patient.getBabyCode(), patient));
		}
		
		
		//Saving patients
		if (syncModels.getPatients() != null
				&& !syncModels.getPatients().isEmpty()) {
			
			List<Patient> patients = new ArrayList<Patient>();
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
					existingPatient.setDeliveryDateAndTime(getTimestampFromDateAndTime(patient.getDeliveryDate(), patient.getDeliveryTime()));
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
					patients.add(existingPatient);
					
				}else {
					Patient newPatient = new Patient();
					newPatient.setBabyCode(patient.getBabyCode());
					newPatient.setAdmissionDateForOutdoorPatients(date);
					newPatient.setBabyAdmittedTo(typeDetailsMap.get(patient.getBabyAdmittedTo()));
					newPatient.setBabyCodeHospital(patient.getBabyCodeHospital());
					newPatient.setBabyOf(patient.getBabyOf());
					newPatient.setBabyWeight(patient.getBabyWeight());
					newPatient.setDeliveryDateAndTime(getTimestampFromDateAndTime(patient.getDeliveryDate(), patient.getDeliveryTime()));
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
					patients.add(newPatient);
					
				}
			});
			List<Patient> savedPatient = patientRepository.save(patients);
			savedPatient.forEach(patient->{
				patientMap.put(patient.getBabyCode(), patient);
			});
			
			List<FailurePatient> faliurePatients = new ArrayList<>();
			syncResult.setFailurePatients(faliurePatients);
		}
		
		
		//Saving BF expression
		if (syncModels.getBfExpressions() != null
				&& !syncModels.getBfExpressions().isEmpty()) {
			
			List<String> uniqueIdList = new ArrayList<String>();
			
			syncModels.getBfExpressions().forEach(bfExpression -> uniqueIdList.add(bfExpression.getId()));
			List<LogExpressionBreastFeed> existingBFEXpressions = logExpressionBreastFeedRepository.findByINId(uniqueIdList);
			Map<String, LogExpressionBreastFeed> bFEXpressionMap = new HashMap<String, LogExpressionBreastFeed>();
			existingBFEXpressions.forEach(bFEXpression->bFEXpressionMap.put(bFEXpression.getUniqueFormId(), bFEXpression));
			
			List<LogExpressionBreastFeed> bfExpressions = new ArrayList<LogExpressionBreastFeed>();
			
			syncModels.getBfExpressions().forEach(bFEXpression -> {
				LogExpressionBreastFeed existingBFEXpression = bFEXpressionMap.get(bFEXpression.getId());
				if(existingBFEXpression != null){
					existingBFEXpression.setPatientId(patientMap.get(bFEXpression.getBabyCode()));
					existingBFEXpression.setDateAndTimeOfExpression(getTimestampFromDateAndTime(bFEXpression.getDateOfExpression(),
							bFEXpression.getTimeOfExpression()));
					existingBFEXpression.setMethodOfExpression(typeDetailsMap.get(bFEXpression.getMethodOfExpression()));
					existingBFEXpression.setExpressionOccuredLocation(typeDetailsMap.get(bFEXpression.getLocationOfExpression()));
					existingBFEXpression.setMilkExpressedFromLeftAndRightBreast(bFEXpression.getVolOfMilkExpressedFromLR());
					existingBFEXpression.setUpdatedBy(bFEXpression.getUserId());
					existingBFEXpression.setUniqueFormId(bFEXpression.getId());
					bfExpressions.add(existingBFEXpression);
					
				}else{
					LogExpressionBreastFeed newBFEXpression = new LogExpressionBreastFeed();
					newBFEXpression.setPatientId(patientMap.get(bFEXpression.getBabyCode()));
					newBFEXpression.setDateAndTimeOfExpression(getTimestampFromDateAndTime(bFEXpression.getDateOfExpression(),
							bFEXpression.getTimeOfExpression()));
					newBFEXpression.setMethodOfExpression(typeDetailsMap.get(bFEXpression.getMethodOfExpression()));
					newBFEXpression.setExpressionOccuredLocation(typeDetailsMap.get(bFEXpression.getLocationOfExpression()));
					newBFEXpression.setMilkExpressedFromLeftAndRightBreast(bFEXpression.getVolOfMilkExpressedFromLR());
					newBFEXpression.setCreatedBy(bFEXpression.getUserId());
					newBFEXpression.setUniqueFormId(bFEXpression.getId());
					bfExpressions.add(newBFEXpression);											
				}
			});
			logExpressionBreastFeedRepository.save(bfExpressions);
			
			List<FailureBFExpression> failureBFExpressions = new ArrayList<>();
			syncResult.setFailureBFExpressions(failureBFExpressions);
			
		}
		
		//Saving feed expression
		if (syncModels.getFeedExpressions() != null
				&& !syncModels.getFeedExpressions().isEmpty()) {
			
			List<String> uniqueIdList = new ArrayList<String>();
			syncModels.getFeedExpressions().forEach(feedExpression -> uniqueIdList.add(feedExpression.getId()));
			List<LogFeed> existingFeeds = logFeedRepository.findByINId(uniqueIdList);
			Map<String, LogFeed> logFeedMap = new HashMap<String, LogFeed>();
			existingFeeds.forEach(logFeed->logFeedMap.put(logFeed.getUniqueFormId(), logFeed));
			List<LogFeed> feeds = new ArrayList<LogFeed>();
			syncModels.getFeedExpressions().forEach(logFeed -> {
				LogFeed existingFeed = logFeedMap.get(logFeed.getId());
				if(existingFeed != null){
					existingFeed.setPatientId(patientMap.get(logFeed.getBabyCode()));
					existingFeed.setDateAndTimeOfFeed(getTimestampFromDateAndTime(logFeed.getDateOfFeed(), logFeed.getTimeOfFeed()));
					existingFeed.setFeedMethod(typeDetailsMap.get(logFeed.getMethodOfFeed()));
					existingFeed.setOmmVolume(logFeed.getOmmVolume());
					existingFeed.setDhmVolume(logFeed.getDhmVolume());
					existingFeed.setFormulaVolume(logFeed.getFormulaVolume());
					existingFeed.setAnimalMilkVolume(logFeed.getAnimalMilkVolume());
					existingFeed.setOtherVolume(logFeed.getOtherVolume());
					existingFeed.setLocationOfFeeding(typeDetailsMap.get(logFeed.getLocationOfFeeding()));
					existingFeed.setWeightOfBaby(logFeed.getBabyWeight());
					existingFeed.setUpdatedBy(logFeed.getUserId());
					existingFeed.setUniqueFormId(logFeed.getId());
					feeds.add(existingFeed);
				}else{
					LogFeed newFeed = new LogFeed();
					newFeed.setPatientId(patientMap.get(logFeed.getBabyCode()));
					newFeed.setDateAndTimeOfFeed(getTimestampFromDateAndTime(logFeed.getDateOfFeed(), logFeed.getTimeOfFeed()));
					newFeed.setFeedMethod(typeDetailsMap.get(logFeed.getMethodOfFeed()));
					newFeed.setOmmVolume(logFeed.getOmmVolume());
					newFeed.setDhmVolume(logFeed.getDhmVolume());
					newFeed.setFormulaVolume(logFeed.getFormulaVolume());
					newFeed.setAnimalMilkVolume(logFeed.getAnimalMilkVolume());
					newFeed.setOtherVolume(logFeed.getOtherVolume());
					newFeed.setLocationOfFeeding(typeDetailsMap.get(logFeed.getLocationOfFeeding()));
					newFeed.setWeightOfBaby(logFeed.getBabyWeight());
					newFeed.setCreatedBy(logFeed.getUserId());
					newFeed.setUniqueFormId(logFeed.getId());
					feeds.add(newFeed);					
				}
			});
			logFeedRepository.save(feeds);
			List<FailureFeedExpression> failureFeedExpressions = new ArrayList<>();
			syncResult.setFailureFeedExpressions(failureFeedExpressions);
			
		}
		
		//Saving BFSP
		if (syncModels.getBfsps() != null
				&& !syncModels.getBfsps().isEmpty()) {
			List<String> uniqueIdList = new ArrayList<String>();
			syncModels.getBfsps().forEach(bfsp -> uniqueIdList.add(bfsp.getId()));
			
			List<LogBreastFeedingSupportivePractice> existingBFSPs = logBreastFeedingSupportivePracticeRepository.findByINId(uniqueIdList);
			Map<String, LogBreastFeedingSupportivePractice> bFSPMap = new HashMap<String, LogBreastFeedingSupportivePractice>();
			existingBFSPs.forEach(bFSP->bFSPMap.put(bFSP.getUniqueFormId(), bFSP));
			List<LogBreastFeedingSupportivePractice> bFSPs = new ArrayList<LogBreastFeedingSupportivePractice>();
			syncModels.getBfsps().forEach(bFSP -> {
				LogBreastFeedingSupportivePractice existingBFSP = bFSPMap.get(bFSP.getId());
				if(existingBFSP != null){
					existingBFSP.setPatientId(patientMap.get(bFSP.getBabyCode()));
					existingBFSP.setDateAndTimeOfBFSP(getTimestampFromDateAndTime(bFSP.getDateOfFeed(), bFSP.getTimeOfFeed()));
					existingBFSP.setBfspPerformed(typeDetailsMap.get(bFSP.getSpPerformed()));
					existingBFSP.setPersonWhoPerformedBFSP(typeDetailsMap.get(bFSP.getPersonPerformed()));
					existingBFSP.setBfspDuration(bFSP.getDuration());
					existingBFSP.setUpdatedBy(bFSP.getUserId());
					existingBFSP.setUniqueFormId(bFSP.getId());
					bFSPs.add(existingBFSP);
				}else{
					LogBreastFeedingSupportivePractice newBFSP = new LogBreastFeedingSupportivePractice();
					newBFSP.setPatientId(patientMap.get(bFSP.getBabyCode()));
					newBFSP.setDateAndTimeOfBFSP(getTimestampFromDateAndTime(bFSP.getDateOfFeed(), bFSP.getTimeOfFeed()));
					newBFSP.setBfspPerformed(typeDetailsMap.get(bFSP.getSpPerformed()));
					newBFSP.setPersonWhoPerformedBFSP(typeDetailsMap.get(bFSP.getPersonPerformed()));
					newBFSP.setBfspDuration(bFSP.getDuration());
					newBFSP.setCreatedBy(bFSP.getUserId());
					newBFSP.setUniqueFormId(bFSP.getId());
					bFSPs.add(newBFSP);					
				}
			});
			logBreastFeedingSupportivePracticeRepository.save(bFSPs);
			List<FailureBFSP> failureBFSPs = new ArrayList<>();
			syncResult.setFailureBFSPs(failureBFSPs);
		}
		
		//Saving BFPD
		if (syncModels.getBfpds() != null
				&& !syncModels.getBfpds().isEmpty()) {
			
			List<String> uniqueIdList = new ArrayList<String>();
			syncModels.getBfpds().forEach(bfpd -> uniqueIdList.add(bfpd.getId()));
			List<LogBreastFeedingPostDischarge> existingBFPDs = logBreastFeedingPostDischargeRepository.findByINId(uniqueIdList);
			Map<String, LogBreastFeedingPostDischarge> bFPDMap = new HashMap<String, LogBreastFeedingPostDischarge>();
			existingBFPDs.forEach(bFPD->bFPDMap.put(bFPD.getUniqueFormId(), bFPD));
			List<LogBreastFeedingPostDischarge> bFPDs = new ArrayList<LogBreastFeedingPostDischarge>();
			syncModels.getBfpds().forEach(bFPD -> {
				LogBreastFeedingPostDischarge existingBFPD = bFPDMap.get(bFPD.getId());
				if(existingBFPD != null){
					existingBFPD.setPatientId(patientMap.get(bFPD.getBabyCode()));
					existingBFPD.setDateOfBreastFeeding(getTimestampFromDateAndTime(bFPD.getDateOfFeed(), "00:00"));
					existingBFPD.setTimeOfBreastFeeding(typeDetailsMap.get(bFPD.getTimeOfFeed()));
					existingBFPD.setBreastFeedingStatus(typeDetailsMap.get(bFPD.getStatus()));
					existingBFPD.setUpdatedBy(bFPD.getUserId());
					existingBFPD.setUniqueFormId(bFPD.getId());
					bFPDs.add(existingBFPD);
				}else{
					LogBreastFeedingPostDischarge newBFPD = new LogBreastFeedingPostDischarge();
					newBFPD.setPatientId(patientMap.get(bFPD.getBabyCode()));
					newBFPD.setDateOfBreastFeeding(getTimestampFromDateAndTime(bFPD.getDateOfFeed(), "00:00"));
					newBFPD.setTimeOfBreastFeeding(typeDetailsMap.get(bFPD.getTimeOfFeed()));
					newBFPD.setBreastFeedingStatus(typeDetailsMap.get(bFPD.getStatus()));
					newBFPD.setUpdatedBy(bFPD.getUserId());
					newBFPD.setUniqueFormId(bFPD.getId());
					bFPDs.add(newBFPD);					
				}
			});
			logBreastFeedingPostDischargeRepository.save(bFPDs);
			List<FailureBFPD> failureBFPDs = new ArrayList<>();
			syncResult.setFailureBFPDs(failureBFPDs);
		}			
		return syncResult;
	}
	
	private Timestamp getTimestampFromDateAndTime(String date, String time){
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		try {
			return new Timestamp(sdf.parse(date+ " " + time).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

}
