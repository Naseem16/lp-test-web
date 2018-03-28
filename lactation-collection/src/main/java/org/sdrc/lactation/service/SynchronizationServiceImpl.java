package org.sdrc.lactation.service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.json.simple.JSONObject;
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
import org.sdrc.lactation.utils.BFExpressionModel;
import org.sdrc.lactation.utils.BFPDModel;
import org.sdrc.lactation.utils.BFSPModel;
import org.sdrc.lactation.utils.FeedExpressionModel;
import org.sdrc.lactation.utils.PatientModel;
import org.sdrc.lactation.utils.SyncModel;
import org.sdrc.lactation.utils.SyncResult;
import org.sdrc.lactation.utils.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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
	
	@Autowired
	private MessageSource messageSource;
	
	private SimpleDateFormat sdfDateOnly = new SimpleDateFormat("dd-MM-yyyy");
	
	private SimpleDateFormat sdfTimeOnly = new SimpleDateFormat("HH:mm");

	/**
	 * @author Naseem Akhtar (naseem@sdrc.co.in) on 12th February 2018 1548.
	 *         This method will receive the forms in form of
	 *         SynchronizationModel. This method will accept List of
	 *         SynchronizationModel which would contain list of users for
	 *         registration purpose and list of data related to a particular
	 *         baby.
	 *  @author Ratikanta
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public SyncResult synchronizeForms(SyncModel syncModel, HttpRequest httpRequest) {

		SyncResult syncResult = new SyncResult();

		//getting area 
		Map<Integer, Area> areaMap = new HashMap<>();
		areaRepository.findAll().forEach(area->areaMap.put(area.getId(), area));
		
		//getting type details
		Map<Integer, TypeDetails> typeDetailsMap = new HashMap<>();
		typeDetailsRepository.findAll().forEach(typeDetails->typeDetailsMap.put(typeDetails.getId(), typeDetails));
		
		// for changing values inside lambda expressions atomic boolean is used.
		AtomicBoolean userFromDifferentInstitution = new AtomicBoolean(false);
		
		//for storing users fetched by institution id
		List<LactationUser> userByInstitutionId;
		
		List<String> userNameByInstitution = new ArrayList<>();
		
		JSONObject inconsistentUser = new JSONObject();
		
		
		//Saving users
		if (syncModel.getUsers() != null
				&& !syncModel.getUsers().isEmpty()) {
			
			List<LactationUser> users = new ArrayList<>();
			syncModel.getUsers().forEach(user -> {
				LactationUser existingUser = lactationUserRepository.findByEmail(user.getEmail());
				if(existingUser != null && existingUser.getInstitution().getId() != user.getInstitution().getId()){
					inconsistentUser.put("name", existingUser.getFirstName());
					inconsistentUser.put("oldState", existingUser.getState().getName());
					inconsistentUser.put("oldDistrict", existingUser.getDistrict().getName());
					inconsistentUser.put("oldInstitute", existingUser.getInstitution().getName());
					inconsistentUser.put("newState", areaMap.get(user.getState().getId()).getName());
					inconsistentUser.put("newDistrict", areaMap.get(user.getDistrict().getId()).getName());
					inconsistentUser.put("newInstitute", areaMap.get(user.getInstitution().getId()).getName());
					userFromDifferentInstitution.set(true);
				}else if(existingUser == null && !userFromDifferentInstitution.get()){
					users.add(user);
				}
			});
			
			if(!userFromDifferentInstitution.get() && !users.isEmpty()){
				lactationUserRepository.save(users);
			}
			
//			syncResult.setFailureUsers(faliureUsers);
		}
		
		
		if(!userFromDifferentInstitution.get()){
			
			List<UserModel> userByInstitutionList = new ArrayList<>();
			// Fetching user institution wise
			userByInstitutionId = lactationUserRepository.findByInstitutionId(syncModel.getInstituteId());
			
			for (LactationUser user : userByInstitutionId) {
				UserModel userModel = new UserModel();
				userModel.setCountry(user.getCountry().getId());
				userModel.setDistrict(user.getDistrict().getId());
				userModel.setEmail(user.getEmail());
				userModel.setFirstName(user.getFirstName());
				userModel.setInstitution(user.getInstitution().getId());
				userModel.setIsSynced(true);
				userModel.setLastName(user.getLastName());
				userModel.setState(user.getState().getId());
				
				userNameByInstitution.add(user.getEmail());
				
				userByInstitutionList.add(userModel);
			}
			
			//getting patients from database
			Map<String, Patient> patientMap = new HashMap<>();
			Set<String> babyCodeList = new HashSet<>();
			
			if (syncModel.getPatients() != null
					&& !syncModel.getPatients().isEmpty()) {
				syncModel.getPatients().forEach(patient -> babyCodeList.add(patient.getBabyCode()));
			}
			if (syncModel.getBfExpressions() != null
					&& !syncModel.getBfExpressions().isEmpty()) {
				syncModel.getBfExpressions().forEach(bfExpression -> babyCodeList.add(bfExpression.getBabyCode()));					
			}
			
			if (syncModel.getFeedExpressions() != null
					&& !syncModel.getFeedExpressions().isEmpty()) {
				syncModel.getFeedExpressions().forEach(feedExpression -> babyCodeList.add(feedExpression.getBabyCode()));					
			}
			
			if (syncModel.getBfsps() != null
					&& !syncModel.getBfsps().isEmpty()) {
				syncModel.getBfsps().forEach(bfsp -> babyCodeList.add(bfsp.getBabyCode()));					
			}
			
			if (syncModel.getBfpds() != null
					&& !syncModel.getBfpds().isEmpty()) {
				syncModel.getBfpds().forEach(bfpd -> babyCodeList.add(bfpd.getBabyCode()));					
			}
			if(!babyCodeList.isEmpty()){
				List<Patient> existingPatients = patientRepository.findByBabyCodeIn(babyCodeList);
				existingPatients.forEach(patient->patientMap.put(patient.getBabyCode(), patient));
			}
			
			
			//Saving patients
			if (syncModel.getPatients() != null
					&& !syncModel.getPatients().isEmpty()) {
				
				List<Patient> patients = new ArrayList<>();
				syncModel.getPatients().forEach(patient -> {
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
						
						if(patient.getNicuAdmissionReason() != null && patient.getNicuAdmissionReason().length != 0) {
							existingPatient.setNicuAdmissionReason(arrayToString(patient.getNicuAdmissionReason()));
						}else {
							existingPatient.setNicuAdmissionReason(null);
						}
						
						existingPatient.setParentsKnowledgeOnHmAndLactation(patient.getParentsKnowledgeOnHmAndLactation() == null ? null : 
							new TypeDetails(patient.getParentsKnowledgeOnHmAndLactation()));
						
						if((patient.getTimeTillFirstExpressionInHour() != null && patient.getTimeTillFirstExpressionInHour() != "") && 
								(patient.getTimeTillFirstExpressionInMinute() != null && patient.getTimeTillFirstExpressionInMinute() != "")) {
							existingPatient.setTimeTillFirstExpression(patient.getTimeTillFirstExpressionInHour() + ":" + patient.getTimeTillFirstExpressionInMinute());
						}else{
							existingPatient.setTimeTillFirstExpression(null);
						}
						
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
				
//				List<FailurePatient> faliurePatients = new ArrayList<>();
//				syncResult.setFailurePatients(faliurePatients);
			}
			
			
			//Saving BF expression
			if (syncModel.getBfExpressions() != null
					&& !syncModel.getBfExpressions().isEmpty()) {
				
				List<String> uniqueIdList = new ArrayList<>();
				Map<String, LogExpressionBreastFeed> bFEXpressionMap = new HashMap<>();
				syncModel.getBfExpressions().forEach(bfExpression -> uniqueIdList.add(bfExpression.getId()));
				
				List<LogExpressionBreastFeed> existingBFEXpressions = logExpressionBreastFeedRepository.findByINId(uniqueIdList);
				existingBFEXpressions.forEach(bFEXpression->bFEXpressionMap.put(bFEXpression.getUniqueFormId(), bFEXpression));
				
				List<LogExpressionBreastFeed> bfExpressions = new ArrayList<>();
				
				syncModel.getBfExpressions().forEach(bFEXpression -> {
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
				
//				List<FailureBFExpression> failureBFExpressions = new ArrayList<>();
//				syncResult.setFailureBFExpressions(failureBFExpressions);
				
			}
			
			//Saving feed expression
			if (syncModel.getFeedExpressions() != null
					&& !syncModel.getFeedExpressions().isEmpty()) {
				
				List<LogFeed> feeds = new ArrayList<>();
				List<String> uniqueIdList = new ArrayList<>();
				Map<String, LogFeed> logFeedMap = new HashMap<>();
				syncModel.getFeedExpressions().forEach(feedExpression -> uniqueIdList.add(feedExpression.getId()));
				
				List<LogFeed> existingFeeds = logFeedRepository.findByINId(uniqueIdList);
				existingFeeds.forEach(logFeed->logFeedMap.put(logFeed.getUniqueFormId(), logFeed));
				
				syncModel.getFeedExpressions().forEach(logFeed -> {
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
//				List<FailureFeedExpression> failureFeedExpressions = new ArrayList<>();
//				syncResult.setFailureFeedExpressions(failureFeedExpressions);
				
			}
			
			//Saving BFSP
			if (syncModel.getBfsps() != null
					&& !syncModel.getBfsps().isEmpty()) {
				List<String> uniqueIdList = new ArrayList<>();
				List<LogBreastFeedingSupportivePractice> bFSPs = new ArrayList<>();
				Map<String, LogBreastFeedingSupportivePractice> bFSPMap = new HashMap<>();
				
				syncModel.getBfsps().forEach(bfsp -> uniqueIdList.add(bfsp.getId()));
				List<LogBreastFeedingSupportivePractice> existingBFSPs = logBreastFeedingSupportivePracticeRepository.findByINId(uniqueIdList);
				existingBFSPs.forEach(bFSP->bFSPMap.put(bFSP.getUniqueFormId(), bFSP));
				
				syncModel.getBfsps().forEach(bFSP -> {
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
//				List<FailureBFSP> failureBFSPs = new ArrayList<>();
//				syncResult.setFailureBFSPs(failureBFSPs);
			}
			
			//Saving BFPD
			if (syncModel.getBfpds() != null
					&& !syncModel.getBfpds().isEmpty()) {
				
				List<String> uniqueIdList = new ArrayList<>();
				Map<String, LogBreastFeedingPostDischarge> bFPDMap = new HashMap<>();
				List<LogBreastFeedingPostDischarge> bFPDs = new ArrayList<>();
				
				syncModel.getBfpds().forEach(bfpd -> uniqueIdList.add(bfpd.getId()));
				List<LogBreastFeedingPostDischarge> existingBFPDs = logBreastFeedingPostDischargeRepository.findByINId(uniqueIdList);
				existingBFPDs.forEach(bFPD->bFPDMap.put(bFPD.getUniqueFormId(), bFPD));
				
				syncModel.getBfpds().forEach(bFPD -> {
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
//				List<FailureBFPD> failureBFPDs = new ArrayList<>();
//				syncResult.setFailureBFPDs(failureBFPDs);
			}
			
//=================================================================================================================================================================//
			
			/*
			 * @author - Naseem Akhtar on 22nd March 2018 2045
			 * From here the reverse sync logic starts
			 */
			
			List<Patient> patientByInstituteId = patientRepository.findByCreatedByIn(userNameByInstitution);
			List<PatientModel> patientByInstituteIdList = new ArrayList<>();
			
			patientByInstituteId.forEach(patient -> {
				PatientModel patientModel = new PatientModel();
				patientModel.setAdmissionDateForOutdoorPatients(patient.getAdmissionDateForOutdoorPatients() == null ? null : patient.getAdmissionDateForOutdoorPatients().toString());
				patientModel.setBabyAdmittedTo(patient.getBabyAdmittedTo() == null ? null : patient.getBabyAdmittedTo().getId());
				patientModel.setBabyCode(patient.getBabyCode());
				patientModel.setBabyCodeHospital(patient.getBabyCodeHospital() == null ? null : patient.getBabyCodeHospital());
				patientModel.setBabyOf(patient.getBabyOf() == null ? null : patient.getBabyOf());
				patientModel.setBabyWeight(patient.getBabyWeight() == null ? null : patient.getBabyWeight());
				patientModel.setDeliveryDate(sdfDateOnly.format(patient.getDeliveryDateAndTime()));
				patientModel.setDeliveryMethod(patient.getDeliveryMethod() == null ? null : patient.getDeliveryMethod().getId());
				patientModel.setDeliveryTime(sdfTimeOnly.format(patient.getDeliveryDateAndTime()));
				patientModel.setDischargeDate(patient.getDischargeDate() == null ? null : patient.getDischargeDate().toString());
				patientModel.setGestationalAgeInWeek(patient.getGestationalAgeInWeek() == null ? null : patient.getGestationalAgeInWeek());
				patientModel.setInpatientOrOutPatient(patient.getInpatientOrOutPatient() == null ? null : patient.getInpatientOrOutPatient().getId());
				patientModel.setIsSynced(true);
				patientModel.setMothersAge(patient.getMothersAge() == null ? null : patient.getMothersAge());
				patientModel.setMothersPrenatalIntent(patient.getMothersPrenatalIntent() == null ? null : patient.getMothersPrenatalIntent().getId());
				
				String[] nicuReasons = patient.getNicuAdmissionReason() == null ? new String[0] : (patient.getNicuAdmissionReason().split(","));
				Integer[] nicuAdmissionReasons = new Integer[nicuReasons.length];
				
				for (int i = 0; i < nicuReasons.length; i++) {
					nicuAdmissionReasons[i] = Integer.parseInt(nicuReasons[i]);
				}
				
				patientModel.setNicuAdmissionReason(nicuAdmissionReasons);
				patientModel.setParentsKnowledgeOnHmAndLactation(patient.getParentsKnowledgeOnHmAndLactation() == null ? null : patient.getParentsKnowledgeOnHmAndLactation().getId());
				if(patient.getTimeTillFirstExpression() != null){
					patientModel.setTimeTillFirstExpressionInHour(patient.getTimeTillFirstExpression().split(":")[0]);
					patientModel.setTimeTillFirstExpressionInMinute(patient.getTimeTillFirstExpression().split(":")[1]);
				}
				patientModel.setUserId(patient.getCreatedBy());
				
				patientByInstituteIdList.add(patientModel);
			});
			
			List<LogExpressionBreastFeed> bfExpressionByInstitute = logExpressionBreastFeedRepository.findByCreatedByIn(userNameByInstitution);
			List<BFExpressionModel> bfExpressionByInstituteList = new ArrayList<>();
			
			bfExpressionByInstitute.forEach(bfExp -> {
				BFExpressionModel bfExpressionModel = new BFExpressionModel();
				bfExpressionModel.setBabyCode(bfExp.getPatientId().getBabyCode());
				bfExpressionModel.setDateOfExpression(sdfDateOnly.format(bfExp.getDateAndTimeOfExpression()));
				bfExpressionModel.setId(bfExp.getUniqueFormId());
				bfExpressionModel.setIsSynced(true);
				bfExpressionModel.setLocationOfExpression(bfExp.getExpressionOccuredLocation() == null ? null : bfExp.getExpressionOccuredLocation().getId());
				bfExpressionModel.setMethodOfExpression(bfExp.getMethodOfExpression() == null ? null : bfExp.getMethodOfExpression().getId());
				bfExpressionModel.setTimeOfExpression(sdfTimeOnly.format(bfExp.getDateAndTimeOfExpression()));
				bfExpressionModel.setUserId(bfExp.getCreatedBy());
				bfExpressionModel.setVolOfMilkExpressedFromLR(bfExp.getMilkExpressedFromLeftAndRightBreast() == null ? null : bfExp.getMilkExpressedFromLeftAndRightBreast());
				
				bfExpressionByInstituteList.add(bfExpressionModel);
			});
			
			List<LogBreastFeedingPostDischarge> bfpdByInstitute = logBreastFeedingPostDischargeRepository.findByCreatedByIn(userNameByInstitution);
			List<BFPDModel> bfpdByInstituteList = new ArrayList<>();
			
			bfpdByInstitute.forEach(bfpd -> {
				BFPDModel bfpdModel = new BFPDModel();
				bfpdModel.setBabyCode(bfpd.getPatientId().getBabyCode());
				bfpdModel.setBreastFeedingStatus(bfpd.getBreastFeedingStatus() == null ? null : bfpd.getBreastFeedingStatus().getId());
				bfpdModel.setDateOfBreastFeeding(bfpd.getDateOfBreastFeeding().toString());
				bfpdModel.setId(bfpd.getUniqueFormId());
				bfpdModel.setIsSynced(true);
				bfpdModel.setTimeOfBreastFeeding(bfpd.getTimeOfBreastFeeding() == null ? null : bfpd.getTimeOfBreastFeeding().getId());
				bfpdModel.setUserId(bfpd.getCreatedBy());
				
				bfpdByInstituteList.add(bfpdModel);
			});
			
			List<LogBreastFeedingSupportivePractice> bfspByInstitute = logBreastFeedingSupportivePracticeRepository.findByCreatedByIn(userNameByInstitution);
			List<BFSPModel> bfspByInstituteList = new ArrayList<>();
			
			bfspByInstitute.forEach(bfsp -> {
				BFSPModel bfspModel = new BFSPModel();
				bfspModel.setBabyCode(bfsp.getPatientId().getBabyCode());
				bfspModel.setBfspDuration(bfsp.getBfspDuration() == null ? null : bfsp.getBfspDuration());
				bfspModel.setBfspPerformed(bfsp.getBfspPerformed() == null ? null : bfsp.getBfspPerformed().getId());
				bfspModel.setDateOfBFSP(sdfDateOnly.format(bfsp.getDateAndTimeOfBFSP()));
				bfspModel.setId(bfsp.getUniqueFormId());
				bfspModel.setIsSynced(true);
				bfspModel.setPersonWhoPerformedBFSP(bfsp.getPersonWhoPerformedBFSP() == null ? null : bfsp.getPersonWhoPerformedBFSP().getId());
				bfspModel.setTimeOfBFSP(sdfTimeOnly.format(bfsp.getDateAndTimeOfBFSP()));
				bfspModel.setUserId(bfsp.getCreatedBy());
				
				bfspByInstituteList.add(bfspModel);
			});
			
			List<LogFeed> feedByInstitute = logFeedRepository.findByCreatedByIn(userNameByInstitution);
			List<FeedExpressionModel> feedByInstituteList = new ArrayList<>();
			
			feedByInstitute.forEach(feed -> {
				FeedExpressionModel feedModel = new FeedExpressionModel();
				feedModel.setAnimalMilkVolume(feed.getAnimalMilkVolume() == null ? null : feed.getAnimalMilkVolume());
				feedModel.setBabyCode(feed.getPatientId().getBabyCode());
				feedModel.setBabyWeight(feed.getWeightOfBaby() == null ? null : feed.getWeightOfBaby());
				feedModel.setDateOfFeed(sdfDateOnly.format(feed.getDateAndTimeOfFeed()));
				feedModel.setDhmVolume(feed.getDhmVolume() == null ? null : feed.getDhmVolume());
				feedModel.setFormulaVolume(feed.getFormulaVolume() == null ? null : feed.getFormulaVolume());
				feedModel.setId(feed.getUniqueFormId());
				feedModel.setIsSynced(true);
				feedModel.setLocationOfFeeding(feed.getLocationOfFeeding() == null ? null : feed.getLocationOfFeeding().getId());
				feedModel.setMethodOfFeed(feed.getFeedMethod() == null ? null : feed.getFeedMethod().getId());
				feedModel.setOmmVolume(feed.getOmmVolume() == null ? null : feed.getOmmVolume());
				feedModel.setOtherVolume(feed.getOtherVolume() == null ? null : feed.getOtherVolume());
				feedModel.setTimeOfFeed(sdfTimeOnly.format(feed.getDateAndTimeOfFeed()));
				feedModel.setUserId(feed.getCreatedBy());
				
				feedByInstituteList.add(feedModel);
			});
			
			
			
			syncResult.setBfExpressions(bfExpressionByInstituteList);
			syncResult.setBfpds(bfpdByInstituteList);
			syncResult.setBfsps(bfspByInstituteList);
			syncResult.setFeedExpressions(feedByInstituteList);
			syncResult.setPatients(patientByInstituteIdList);
			syncResult.setUsers(userByInstitutionList);
			syncResult.setSyncStatus(1);
			
		}else{
			//-----------------------------Send email to lactation group, conveying that existing user trying to register with different insitute----------------------//
			
			syncResult.setSyncStatus(-1);
			
			Thread thread = new Thread(){
				public void run(){
					/*final String username = messageSource.getMessage("lactation.email", null, null);
					final String password = messageSource.getMessage("lactation.password", null, null);
					final String to = messageSource.getMessage("lactation.sentTo", null, null);*/
					
					final String username = "lactation.medella@gmail.com";
					final String password = "lactation@2018";
					final String to = "naseem@sdrc.co.in";

					Properties props = new Properties();
					props.put("mail.smtp.auth", "true");
					props.put("mail.smtp.starttls.enable", "true");
					props.put("mail.smtp.host", "smtp.gmail.com");
					props.put("mail.smtp.port", "587");

					Session session = Session.getInstance(props,
					  new javax.mail.Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(username, password);
						}
					  });

					try {

						Message message = new MimeMessage(session);
						message.setFrom(new InternetAddress(username));
						message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
						message.setSubject("Lactation Project - Alert");
						message.setText("A user - "+ inconsistentUser.get("name") +", registered with "+ inconsistentUser.get("oldInstitute") + 
								" (" +inconsistentUser.get("oldState") + ", " + inconsistentUser.get("oldDistrict") + "), is trying to register "+ 
								"himself again with "+ inconsistentUser.get("newInstitute") + " (" + inconsistentUser.get("newState") + ", " + inconsistentUser.get("newDistrict")
								+ ")");
						Transport.send(message);

					} catch (MessagingException e) {
						e.printStackTrace();
					}
				}
			};
			
			thread.start();
			
			
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
