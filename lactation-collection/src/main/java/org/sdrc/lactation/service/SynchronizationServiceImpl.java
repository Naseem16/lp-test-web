package org.sdrc.lactation.service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

//	@Autowired
//	private MessageDigestPasswordEncoder messageDigestPasswordEncoder;

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
		Map<Integer, Area> areaMap = new HashMap<>();
		areaRepository.findAll().forEach(area->areaMap.put(area.getId(), area));
		
		//getting type details
		Map<Integer, TypeDetails> typeDetailsMap = new HashMap<>();
		typeDetailsRepository.findAll().forEach(typeDetails->typeDetailsMap.put(typeDetails.getId(), typeDetails));
		
		
		//Saving users
		if (syncModels.getUsers() != null
				&& !syncModels.getUsers().isEmpty()) {
					
			List<FailureUser> faliureUsers = new ArrayList<>();
			List<LactationUser> users = new ArrayList<>();
			syncModels.getUsers().forEach(user -> {
				LactationUser existingUser = lactationUserRepository.findByEmail(user.getEmail());
				if(existingUser != null){
					FailureUser failureUser = new FailureUser();
					failureUser.setUserId(user.getEmail());
					failureUser.setReasonOfFailure("User exists!");
					faliureUsers.add(failureUser);
				}else{
					users.add(user);
				}
			});
			lactationUserRepository.save(users);
			
			syncResult.setFailureUsers(faliureUsers);
		}

		
		//getting patients from database
		Map<String, Patient> patientMap = new HashMap<>();
		List<String> babyCodeList = new ArrayList<>();
		
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
			
			List<Patient> patients = new ArrayList<>();
			syncModels.getPatients().forEach(patient -> {
				Patient existingPatient = patientMap.get(patient.getBabyCode());
				
				if(existingPatient != null){
					existingPatient.setAdmissionDateForOutdoorPatients((patient.getAdmissionDateForOutdoorPatients() == null 
							|| patient.getAdmissionDateForOutdoorPatients() == "") ? null :	getDateFromString(patient.getAdmissionDateForOutdoorPatients()));
					existingPatient.setBabyAdmittedTo(patient.getBabyAdmittedTo() == null ? null : new TypeDetails(patient.getBabyAdmittedTo()));
					existingPatient.setBabyCodeHospital((patient.getBabyCodeHospital() == null || patient.getBabyCodeHospital() == "") ? null : patient.getBabyCodeHospital());
					existingPatient.setBabyOf((patient.getBabyOf() == null || patient.getBabyOf() == "") ? null : patient.getBabyOf());
					existingPatient.setBabyWeight(patient.getBabyWeight() == null ? null :patient.getBabyWeight());
					existingPatient.setDeliveryDateAndTime(getTimestampFromDateAndTime(patient.getDeliveryDate(), patient.getDeliveryTime()));
					existingPatient.setDeliveryMethod(patient.getDeliveryMethod() == null ? null : new TypeDetails(patient.getDeliveryMethod()));
					existingPatient.setDischargeDate((patient.getDischargeDate() == null || patient.getDischargeDate() == "") ? null : getDateFromString(patient.getDischargeDate()));
					existingPatient.setGestationalAgeInWeek(patient.getGestationalAgeInWeek() == null ? null : patient.getGestationalAgeInWeek());
					existingPatient.setInpatientOrOutPatient(patient.getInpatientOrOutPatient() == null ? null : new TypeDetails(patient.getInpatientOrOutPatient()));
					existingPatient.setMothersAge(patient.getMothersAge() == null ? null : patient.getMothersAge());
					existingPatient.setMothersPrenatalIntent(patient.getMothersPrenatalIntent() == null ? null : new TypeDetails(patient.getMothersPrenatalIntent()));
					
					if(patient.getNicuAdmissionReason() != null && patient.getNicuAdmissionReason().length != 0){
						existingPatient.setNicuAdmissionReason(arrayToString(patient.getNicuAdmissionReason()));
					}
					
					existingPatient.setParentsKnowledgeOnHmAndLactation(patient.getParentsKnowledgeOnHmAndLactation() == null ? null : 
						new TypeDetails(patient.getParentsKnowledgeOnHmAndLactation()));
					
					if((patient.getTimeTillFirstExpressionInHour() != null && patient.getTimeTillFirstExpressionInHour() != "") && 
							(patient.getTimeTillFirstExpressionInMinute() != null && patient.getTimeTillFirstExpressionInMinute() != ""))
						existingPatient.setTimeTillFirstExpression(patient.getTimeTillFirstExpressionInHour() + ":" + patient.getTimeTillFirstExpressionInMinute());
					
					existingPatient.setUpdatedBy(patient.getUserId());
					existingPatient.setUpdatedDate(getTimestampFromString(patient.getUpdatedDate()));
					existingPatient.setDischargeDate(patient.getDischargeDate() != null && patient.getDischargeDate() != "" ? getDateFromString(patient.getDischargeDate()) : null);
				}else {
					Patient newPatient = new Patient();
					
					newPatient.setAdmissionDateForOutdoorPatients((patient.getAdmissionDateForOutdoorPatients() == null 
							|| patient.getAdmissionDateForOutdoorPatients() == "") ? null :	getDateFromString(patient.getAdmissionDateForOutdoorPatients()));
					newPatient.setBabyAdmittedTo(patient.getBabyAdmittedTo() == null ? null : new TypeDetails(patient.getBabyAdmittedTo()));
					newPatient.setBabyCode(patient.getBabyCode());
					newPatient.setBabyCodeHospital((patient.getBabyCodeHospital() == null || patient.getBabyCodeHospital() == "") ? null : patient.getBabyCodeHospital());
					newPatient.setBabyOf((patient.getBabyOf() == null || patient.getBabyOf() == "") ? null : patient.getBabyOf());
					newPatient.setBabyWeight(patient.getBabyWeight() == null ? null :patient.getBabyWeight());
					newPatient.setCreatedBy(patient.getUserId());
					newPatient.setCreatedDate(getTimestampFromString(patient.getCreatedDate()));
					newPatient.setDeliveryDateAndTime(getTimestampFromDateAndTime(patient.getDeliveryDate(), patient.getDeliveryTime()));
					newPatient.setDeliveryMethod(patient.getDeliveryMethod() == null ? null : new TypeDetails(patient.getDeliveryMethod()));
					newPatient.setDischargeDate((patient.getDischargeDate() == null || patient.getDischargeDate() == "") ? null : getDateFromString(patient.getDischargeDate()));
					newPatient.setGestationalAgeInWeek(patient.getGestationalAgeInWeek() == null ? null : patient.getGestationalAgeInWeek());
					newPatient.setInpatientOrOutPatient(patient.getInpatientOrOutPatient() == null ? null : new TypeDetails(patient.getInpatientOrOutPatient()));
					newPatient.setMothersAge(patient.getMothersAge() == null ? null : patient.getMothersAge());
					newPatient.setMothersPrenatalIntent(patient.getMothersPrenatalIntent() == null ? null : new TypeDetails(patient.getMothersPrenatalIntent()));
					
					if(patient.getNicuAdmissionReason() != null && patient.getNicuAdmissionReason().length != 0){
						newPatient.setNicuAdmissionReason(arrayToString(patient.getNicuAdmissionReason()));
					}
					
					newPatient.setParentsKnowledgeOnHmAndLactation(patient.getParentsKnowledgeOnHmAndLactation() == null ? null : 
						new TypeDetails(patient.getParentsKnowledgeOnHmAndLactation()));
					
					if((patient.getTimeTillFirstExpressionInHour() != null && patient.getTimeTillFirstExpressionInHour() != "") && 
							(patient.getTimeTillFirstExpressionInMinute() != null && patient.getTimeTillFirstExpressionInMinute() != ""))
						newPatient.setTimeTillFirstExpression(patient.getTimeTillFirstExpressionInHour() + ":" + patient.getTimeTillFirstExpressionInMinute());
					
					patients.add(newPatient);
				}
			});
			List<Patient> savedPatient = patientRepository.save(patients);
			savedPatient.forEach(patient-> patientMap.put(patient.getBabyCode(), patient));
			
			List<FailurePatient> faliurePatients = new ArrayList<>();
			syncResult.setFailurePatients(faliurePatients);
		}
		
		
		//Saving BF expression
		if (syncModels.getBfExpressions() != null
				&& !syncModels.getBfExpressions().isEmpty()) {
			
			List<String> uniqueIdList = new ArrayList<>();
			Map<String, LogExpressionBreastFeed> bFEXpressionMap = new HashMap<>();
			syncModels.getBfExpressions().forEach(bfExpression -> uniqueIdList.add(bfExpression.getId()));
			
			List<LogExpressionBreastFeed> existingBFEXpressions = logExpressionBreastFeedRepository.findByINId(uniqueIdList);
			existingBFEXpressions.forEach(bFEXpression->bFEXpressionMap.put(bFEXpression.getUniqueFormId(), bFEXpression));
			
			List<LogExpressionBreastFeed> bfExpressions = new ArrayList<>();
			
			syncModels.getBfExpressions().forEach(bFEXpression -> {
				LogExpressionBreastFeed existingBFEXpression = bFEXpressionMap.get(bFEXpression.getId());
				if(existingBFEXpression != null){
					existingBFEXpression.setPatientId(patientMap.get(bFEXpression.getBabyCode()));
					existingBFEXpression.setDateAndTimeOfExpression(getTimestampFromDateAndTime(bFEXpression.getDateOfExpression(),
							bFEXpression.getTimeOfExpression()));
					existingBFEXpression.setMethodOfExpression(bFEXpression.getMethodOfExpression() == null ? null : new TypeDetails(bFEXpression.getMethodOfExpression()));
					existingBFEXpression.setExpressionOccuredLocation(bFEXpression.getLocationOfExpression() == null ? null : new TypeDetails(bFEXpression.getLocationOfExpression()));
					existingBFEXpression.setMilkExpressedFromLeftAndRightBreast(bFEXpression.getVolOfMilkExpressedFromLR() == null ? null : bFEXpression.getVolOfMilkExpressedFromLR());
					existingBFEXpression.setUniqueFormId(bFEXpression.getId());
					existingBFEXpression.setUpdatedDate(getTimestampFromString((bFEXpression.getUpdatedDate())));
					existingBFEXpression.setUpdatedBy(bFEXpression.getUserId());
				}else{
					LogExpressionBreastFeed newBFEXpression = new LogExpressionBreastFeed();
					
					newBFEXpression.setPatientId(patientMap.get(bFEXpression.getBabyCode()));
					newBFEXpression.setDateAndTimeOfExpression(getTimestampFromDateAndTime(bFEXpression.getDateOfExpression(),
							bFEXpression.getTimeOfExpression()));
					newBFEXpression.setMethodOfExpression(bFEXpression.getMethodOfExpression() == null ? null : new TypeDetails(bFEXpression.getMethodOfExpression()));
					newBFEXpression.setExpressionOccuredLocation(bFEXpression.getLocationOfExpression() == null ? null : new TypeDetails(bFEXpression.getLocationOfExpression()));
					newBFEXpression.setMilkExpressedFromLeftAndRightBreast(bFEXpression.getVolOfMilkExpressedFromLR() == null ? null : bFEXpression.getVolOfMilkExpressedFromLR());
					newBFEXpression.setUniqueFormId(bFEXpression.getId());
					newBFEXpression.setCreatedDate(getTimestampFromString(bFEXpression.getCreatedDate()));
					newBFEXpression.setCreatedBy(bFEXpression.getUserId());
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
			
			List<LogFeed> feeds = new ArrayList<>();
			List<String> uniqueIdList = new ArrayList<>();
			Map<String, LogFeed> logFeedMap = new HashMap<>();
			syncModels.getFeedExpressions().forEach(feedExpression -> uniqueIdList.add(feedExpression.getId()));
			
			List<LogFeed> existingFeeds = logFeedRepository.findByINId(uniqueIdList);
			existingFeeds.forEach(logFeed->logFeedMap.put(logFeed.getUniqueFormId(), logFeed));
			
			syncModels.getFeedExpressions().forEach(logFeed -> {
				LogFeed existingFeed = logFeedMap.get(logFeed.getId());
				if(existingFeed != null){
					existingFeed.setPatientId(patientMap.get(logFeed.getBabyCode()));
					existingFeed.setDateAndTimeOfFeed(getTimestampFromDateAndTime(logFeed.getDateOfFeed(), logFeed.getTimeOfFeed()));
					existingFeed.setFeedMethod(logFeed.getMethodOfFeed() == null ? null : new TypeDetails(logFeed.getMethodOfFeed()));
					existingFeed.setOmmVolume(logFeed.getOmmVolume() == null ? null :logFeed.getOmmVolume());
					existingFeed.setDhmVolume(logFeed.getDhmVolume() == null ? null : logFeed.getDhmVolume());
					existingFeed.setFormulaVolume(logFeed.getFormulaVolume() == null ? null : logFeed.getFormulaVolume());
					existingFeed.setAnimalMilkVolume(logFeed.getAnimalMilkVolume() == null ? null : logFeed.getAnimalMilkVolume());
					existingFeed.setOtherVolume(logFeed.getOtherVolume() == null ? null : logFeed.getOtherVolume());
					existingFeed.setLocationOfFeeding(logFeed.getLocationOfFeeding() == null ? null : new TypeDetails(logFeed.getLocationOfFeeding()));
					existingFeed.setWeightOfBaby(logFeed.getBabyWeight() == null ? null : logFeed.getBabyWeight());
					existingFeed.setUpdatedBy(logFeed.getUserId());
					existingFeed.setUniqueFormId(logFeed.getId());
					existingFeed.setUpdatedDate(getTimestampFromString(logFeed.getUpdatedDate()));
				}else{
					LogFeed newFeed = new LogFeed();
					
					newFeed.setPatientId(patientMap.get(logFeed.getBabyCode()));
					newFeed.setDateAndTimeOfFeed(getTimestampFromDateAndTime(logFeed.getDateOfFeed(), logFeed.getTimeOfFeed()));
					newFeed.setFeedMethod(logFeed.getMethodOfFeed() == null ? null : new TypeDetails(logFeed.getMethodOfFeed()));
					newFeed.setOmmVolume(logFeed.getOmmVolume() == null ? null :logFeed.getOmmVolume());
					newFeed.setDhmVolume(logFeed.getDhmVolume() == null ? null : logFeed.getDhmVolume());
					newFeed.setFormulaVolume(logFeed.getFormulaVolume() == null ? null : logFeed.getFormulaVolume());
					newFeed.setAnimalMilkVolume(logFeed.getAnimalMilkVolume() == null ? null : logFeed.getAnimalMilkVolume());
					newFeed.setOtherVolume(logFeed.getOtherVolume() == null ? null : logFeed.getOtherVolume());
					newFeed.setLocationOfFeeding(logFeed.getLocationOfFeeding() == null ? null : new TypeDetails(logFeed.getLocationOfFeeding()));
					newFeed.setWeightOfBaby(logFeed.getBabyWeight() == null ? null : logFeed.getBabyWeight());
					newFeed.setCreatedBy(logFeed.getUserId());
					newFeed.setUniqueFormId(logFeed.getId());
					newFeed.setCreatedDate(getTimestampFromString(logFeed.getCreatedDate()));
					
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
			List<String> uniqueIdList = new ArrayList<>();
			List<LogBreastFeedingSupportivePractice> bFSPs = new ArrayList<>();
			Map<String, LogBreastFeedingSupportivePractice> bFSPMap = new HashMap<>();
			
			syncModels.getBfsps().forEach(bfsp -> uniqueIdList.add(bfsp.getId()));
			List<LogBreastFeedingSupportivePractice> existingBFSPs = logBreastFeedingSupportivePracticeRepository.findByINId(uniqueIdList);
			existingBFSPs.forEach(bFSP->bFSPMap.put(bFSP.getUniqueFormId(), bFSP));
			
			syncModels.getBfsps().forEach(bFSP -> {
				LogBreastFeedingSupportivePractice existingBFSP = bFSPMap.get(bFSP.getId());
				if(existingBFSP != null){
					existingBFSP.setPatientId(patientMap.get(bFSP.getBabyCode()));
					existingBFSP.setDateAndTimeOfBFSP(getTimestampFromDateAndTime(bFSP.getDateOfBFSP(), bFSP.getTimeOfBFSP()));
					existingBFSP.setBfspPerformed(bFSP.getBfspPerformed() == null ? null : new TypeDetails(bFSP.getBfspPerformed()));
					existingBFSP.setPersonWhoPerformedBFSP(bFSP.getPersonWhoPerformedBFSP() == null ? null : new TypeDetails(bFSP.getPersonWhoPerformedBFSP()));
					existingBFSP.setBfspDuration(bFSP.getBfspDuration() == null ? null : bFSP.getBfspDuration());
					existingBFSP.setUpdatedBy(bFSP.getUserId());
					existingBFSP.setUniqueFormId(bFSP.getId());
					existingBFSP.setUpdatedDate(getTimestampFromString(bFSP.getUpdatedDate()));
				}else{
					LogBreastFeedingSupportivePractice newBFSP = new LogBreastFeedingSupportivePractice();
					
					newBFSP.setPatientId(patientMap.get(bFSP.getBabyCode()));
					newBFSP.setDateAndTimeOfBFSP(getTimestampFromDateAndTime(bFSP.getDateOfBFSP(), bFSP.getTimeOfBFSP()));
					newBFSP.setBfspPerformed(bFSP.getBfspPerformed() == null ? null : new TypeDetails(bFSP.getBfspPerformed()));
					newBFSP.setPersonWhoPerformedBFSP(bFSP.getPersonWhoPerformedBFSP() == null ? null : new TypeDetails(bFSP.getPersonWhoPerformedBFSP()));
					newBFSP.setBfspDuration(bFSP.getBfspDuration() == null ? null : bFSP.getBfspDuration());
					newBFSP.setCreatedBy(bFSP.getUserId());
					newBFSP.setUniqueFormId(bFSP.getId());
					newBFSP.setCreatedDate(getTimestampFromString(bFSP.getCreatedDate()));
					
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
			
			List<String> uniqueIdList = new ArrayList<>();
			Map<String, LogBreastFeedingPostDischarge> bFPDMap = new HashMap<>();
			List<LogBreastFeedingPostDischarge> bFPDs = new ArrayList<>();
			
			syncModels.getBfpds().forEach(bfpd -> uniqueIdList.add(bfpd.getId()));
			List<LogBreastFeedingPostDischarge> existingBFPDs = logBreastFeedingPostDischargeRepository.findByINId(uniqueIdList);
			existingBFPDs.forEach(bFPD->bFPDMap.put(bFPD.getUniqueFormId(), bFPD));
			
			syncModels.getBfpds().forEach(bFPD -> {
				LogBreastFeedingPostDischarge existingBFPD = bFPDMap.get(bFPD.getId());
				if(existingBFPD != null){
					existingBFPD.setPatientId(patientMap.get(bFPD.getBabyCode()));
					existingBFPD.setDateOfBreastFeeding(getTimestampFromDateAndTime(bFPD.getDateOfBreastFeeding(), "00:00"));
					existingBFPD.setTimeOfBreastFeeding(bFPD.getTimeOfBreastFeeding() == null ? null : new TypeDetails(bFPD.getTimeOfBreastFeeding()));
					existingBFPD.setBreastFeedingStatus(bFPD.getBreastFeedingStatus() == null ? null : new TypeDetails(bFPD.getBreastFeedingStatus()));
					existingBFPD.setUpdatedBy(bFPD.getUserId());
					existingBFPD.setUniqueFormId(bFPD.getId());
					existingBFPD.setUpdatedDate(getTimestampFromString(bFPD.getUpdatedDate()));
				}else{
					LogBreastFeedingPostDischarge newBFPD = new LogBreastFeedingPostDischarge();
					
					newBFPD.setPatientId(patientMap.get(bFPD.getBabyCode()));
					newBFPD.setDateOfBreastFeeding(getTimestampFromDateAndTime(bFPD.getDateOfBreastFeeding(), "00:00"));
					newBFPD.setTimeOfBreastFeeding(bFPD.getTimeOfBreastFeeding() == null ? null : new TypeDetails(bFPD.getTimeOfBreastFeeding()));
					newBFPD.setBreastFeedingStatus(bFPD.getBreastFeedingStatus() == null ? null : new TypeDetails(bFPD.getBreastFeedingStatus()));
					newBFPD.setUpdatedBy(bFPD.getUserId());
					newBFPD.setUniqueFormId(bFPD.getId());
					newBFPD.setCreatedDate(getTimestampFromString(bFPD.getCreatedDate()));
					
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
	
	private Timestamp getTimestampFromString(String date){
		return Timestamp.valueOf(date);
	}
	
	private Date getDateFromString(String date){
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		try{
			return new Date(sdf.parse(date).getTime());
		} catch(ParseException e){
			e.printStackTrace();
			return null;
		}
	}
	
	private String arrayToString(Integer[] integerArray){
		StringBuilder arrayAsString = new StringBuilder();
		for (int i = 0; i < integerArray.length; i++) {
			arrayAsString.append(integerArray[i].toString()+",");
		}
		return arrayAsString.substring(0, arrayAsString.length() - 1);
	}

}
