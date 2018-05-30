package org.sdrc.lactation.service;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.sdrc.lactation.domain.LogBreastFeedingPostDischarge;
import org.sdrc.lactation.domain.LogBreastFeedingSupportivePractice;
import org.sdrc.lactation.domain.LogExpressionBreastFeed;
import org.sdrc.lactation.domain.LogFeed;
import org.sdrc.lactation.domain.Patient;
import org.sdrc.lactation.domain.TypeDetails;
import org.sdrc.lactation.repository.LogBreastFeedingPostDischargeRepository;
import org.sdrc.lactation.repository.LogBreastFeedingSupportivePracticeRepository;
import org.sdrc.lactation.repository.LogExpressionBreastFeedRepository;
import org.sdrc.lactation.repository.LogFeedRepository;
import org.sdrc.lactation.repository.PatientRepository;
import org.sdrc.lactation.repository.TypeDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LegacyDataImportServiceImpl implements LegacyDataImportService {

	private SimpleDateFormat sdfDateOnly = new SimpleDateFormat("dd-MM-yyyy");
	//
	// private SimpleDateFormat sdfTimeOnly = new SimpleDateFormat("HH:mm");
	//
	private SimpleDateFormat sdfDateTimeWithSeconds = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	//
	private SimpleDateFormat sdfDateInteger = new SimpleDateFormat("ddMMyyyyHHmmssSSS");

	private static final String FILE_NAME = "/opt/lactation/data_dump/LactationProject_LegacyDataTemplate_r7.xlsx";
	private static final Logger log = LogManager.getLogger(SynchronizationServiceImpl.class);

	@Autowired
	private TypeDetailsRepository typeDetailsRepository;

	@Autowired
	private PatientRepository patientRepository;

	@Autowired
	private LogExpressionBreastFeedRepository logExpressionBreastFeedRepository;
	
	@Autowired
	private LogBreastFeedingSupportivePracticeRepository logBreastFeedingSupportivePracticeRepository;
	
	@Autowired
	private LogFeedRepository logFeedRepository;
	
	@Autowired
	private LogBreastFeedingPostDischargeRepository logBreastFeedingPostDischargeRepository;

	@Override
	public Boolean importLegacyData() {
		final String username = "lactation_kem@medela.co.in";
		final String uuid = "Legacy Data";
		final String sysOutFragment = "Inserting row ---> ";

		try (FileInputStream excelFile = new FileInputStream(new File(FILE_NAME));
				XSSFWorkbook workbook = new XSSFWorkbook(excelFile);) {

			// getting type details
			Map<String, TypeDetails> typeDetailsMapPatient = new HashMap<>();
			Map<String, TypeDetails> typeDetailsMapBfExp = new HashMap<>();
			Map<String, TypeDetails> typeDetailsMapBfsp = new HashMap<>();
			Map<String, TypeDetails> typeDetailsMapLogFeed = new HashMap<>();
			Map<String, TypeDetails> typeDetailsMapBfpd = new HashMap<>();
			
			typeDetailsRepository.findAll().forEach(typeDetails -> {
				if(typeDetails.getTypeId().getId() < 7)
					typeDetailsMapPatient.put(typeDetails.getName(), typeDetails);
				
				if(typeDetails.getTypeId().getId() == 7 || typeDetails.getTypeId().getId() == 8)
					typeDetailsMapBfExp.put(typeDetails.getName(), typeDetails);
				
				if(typeDetails.getTypeId().getId() == 9 || typeDetails.getTypeId().getId() == 10)
					typeDetailsMapBfsp.put(typeDetails.getName(), typeDetails);
				
				if(typeDetails.getTypeId().getId() == 5 || typeDetails.getTypeId().getId() == 11)
					typeDetailsMapLogFeed.put(typeDetails.getName(), typeDetails);
				
				if(typeDetails.getTypeId().getId() == 12 || typeDetails.getTypeId().getId() == 13)
					typeDetailsMapBfpd.put(typeDetails.getName(), typeDetails);
			});
			
			Map<String, Patient> patientMap = new HashMap<>();

			XSSFSheet patientSheet = workbook.getSheetAt(0);
			XSSFSheet logExpressionBfSheet = workbook.getSheetAt(1);
			XSSFSheet bfspSheet = workbook.getSheetAt(2);
			XSSFSheet logFeedSheet = workbook.getSheetAt(3);
			XSSFSheet bfpdSheet = workbook.getSheetAt(4);

			importPatientData(patientSheet, typeDetailsMapPatient, username, uuid, sysOutFragment);
			patientRepository.findAll().forEach(patient -> patientMap.put(patient.getBabyCode(), patient));
			
			importLogExpBfData(logExpressionBfSheet, typeDetailsMapBfExp, patientMap, username, uuid, sysOutFragment);
			importBfspData(bfspSheet, typeDetailsMapBfsp, patientMap, username, uuid, sysOutFragment);
			importLogFeedData(logFeedSheet, typeDetailsMapLogFeed, patientMap, username, uuid, sysOutFragment);
			importBfpdData(bfpdSheet, typeDetailsMapBfpd, patientMap, username, uuid, sysOutFragment);

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}

		return true;
	}

	private void importBfpdData(XSSFSheet bfpdSheet, Map<String, TypeDetails> typeDetailsMapBfpd,
			Map<String, Patient> patientMap, String username, String uuid, String sysOutFragment) throws InterruptedException, ParseException {
		
		List<LogBreastFeedingPostDischarge> bfpdList = new ArrayList<>();
		
		for (int row = 1; row <= bfpdSheet.getLastRowNum(); row++) {
			Thread.sleep(100);
			System.out.println(sysOutFragment + (row+1) + " for BFPD");
			
			LogBreastFeedingPostDischarge bfpd = new LogBreastFeedingPostDischarge();
			bfpd.setCreatedBy(username);
			bfpd.setUpdatedBy(username);
			bfpd.setUuidNumber(uuid);
			bfpd.setUpdatedDate(new Timestamp(new Date().getTime()));
			bfpd.setCreatedDate(new Timestamp(new Date().getTime()));
			
			XSSFRow xssfRow = bfpdSheet.getRow(row);
			for (int cols = 0; cols < xssfRow.getLastCellNum(); cols++) {
				Cell cell = xssfRow.getCell(cols);

				if (cell != null) {
					switch (cols) {
					case 0:
						bfpd.setPatientId(patientMap.get(cell.getStringCellValue()));
						System.out.println(sysOutFragment + (row+1) + " column --> PatientId");
						break;
					case 1:
						bfpd.setDateOfBreastFeeding(getTimestampFromString(cell.getStringCellValue()));
						System.out.println(sysOutFragment + (row+1) + " column --> DateOfBreastFeeding");
						break;
					case 2:
						bfpd.setTimeOfBreastFeeding(typeDetailsMapBfpd.get(cell.getStringCellValue()));
						System.out.println(sysOutFragment + (row+1) + " column --> TimeOfBreastFeeding");
						break;
					case 3:
						bfpd.setBreastFeedingStatus(typeDetailsMapBfpd.get(cell.getStringCellValue()));
						System.out.println(sysOutFragment + (row+1) + " column --> BreastFeedingStatus");
						break;
					default:
						log.error("Error occured in switch case in BFPD in row no --> " + (row+1) + " column no --> " + (cols+1));
						break;
					}
				}
			}
			
			if(bfpd.getPatientId() != null && bfpd.getDateOfBreastFeeding() != null && bfpd.getTimeOfBreastFeeding() != null) {
				bfpd.setUniqueFormId(bfpd.getPatientId().getBabyCode() + "bfpd" + sdfDateInteger.format(new Date()));
				bfpdList.add(bfpd);
			}else {
				log.warn("BFPD --> patient id or date and time of exp is missing or incorrect for row no --->>>" + (row+1));
			}
		}
		
		if(bfpdList != null && !bfpdList.isEmpty())
			logBreastFeedingPostDischargeRepository.save(bfpdList);
	}

	private void importLogFeedData(XSSFSheet logFeedSheet, Map<String, TypeDetails> typeDetailsMapLogFeed,
			Map<String, Patient> patientMap, String username, String uuid, String sysOutFragment) throws InterruptedException, ParseException {
		
		List<LogFeed> logFeedList = new ArrayList<>();
		
		for (int row = 1; row <= logFeedSheet.getLastRowNum(); row++) {
			Thread.sleep(100);
			System.out.println(sysOutFragment + (row+1) + " for Log Feed");
			
			LogFeed logFeed = new LogFeed();
			logFeed.setCreatedBy(username);
			logFeed.setUpdatedBy(username);
			logFeed.setUuidNumber(uuid);
			logFeed.setUpdatedDate(new Timestamp(new Date().getTime()));
			logFeed.setCreatedDate(new Timestamp(new Date().getTime()));
			XSSFRow xssfRow = logFeedSheet.getRow(row);

			for (int cols = 0; cols < xssfRow.getLastCellNum(); cols++) {
				Cell cell = xssfRow.getCell(cols);

				if (cell != null) {
					switch (cols) {
					case 0:
						logFeed.setPatientId(patientMap.get(cell.getStringCellValue()));
						System.out.println(sysOutFragment + (row+1) + " column --> PatientId");
						break;
					case 3:
						logFeed.setDateAndTimeOfFeed(getTimestampFromString(cell.getStringCellValue()));
						System.out.println(sysOutFragment + (row+1) + " column --> Date and time");
						break;
					case 4:
						logFeed.setFeedMethod(typeDetailsMapLogFeed.get(cell.getStringCellValue()));
						System.out.println(sysOutFragment + (row+1) + " column --> feed method");
						break;
					case 5:
						logFeed.setOmmVolume(cell.getNumericCellValue());
						System.out.println(sysOutFragment + (row+1) + " column --> omm volume");
						break;
					case 6:
						logFeed.setDhmVolume(cell.getNumericCellValue());
						System.out.println(sysOutFragment + (row+1) + " column --> dhm volume");
						break;
					case 7:
						logFeed.setFormulaVolume(cell.getNumericCellValue());
						System.out.println(sysOutFragment + (row+1) + " column --> formula volume");
						break;
					case 8:
						logFeed.setAnimalMilkVolume(cell.getNumericCellValue());
						System.out.println(sysOutFragment + (row+1) + " column --> animal milk volume");
						break;
					case 9:
						logFeed.setOtherVolume(cell.getNumericCellValue());
						System.out.println(sysOutFragment + (row+1) + " column --> other volume");
						break;
					case 10:
						logFeed.setLocationOfFeeding(typeDetailsMapLogFeed.get(cell.getStringCellValue()));
						System.out.println(sysOutFragment + (row+1) + " column --> location of feeding");
						break;
					case 11:
						logFeed.setWeightOfBaby(cell.getNumericCellValue());
						System.out.println(sysOutFragment + (row+1) + " column --> weight of baby");
						break;
					default:
						log.error("Error occured in switch case in LogFeed in row no --> " + (row+1) + " column no --> " + (cols+1));
						break;
					}
				}
			}
			
			if(logFeed.getPatientId() != null && logFeed.getDateAndTimeOfFeed() != null){
				logFeed.setUniqueFormId(logFeed.getPatientId().getBabyCode() + "feid" + sdfDateInteger.format(new Date()));
				logFeedList.add(logFeed);
			}else {
				log.warn("LogFeed --> patient id or date and time of exp is missing or incorrect for row no --->>>" + (row+1));
			}
		}
		
		if(logFeedList != null && !logFeedList.isEmpty())
			logFeedRepository.save(logFeedList);
	}

	private void importBfspData(XSSFSheet bfspSheet, Map<String, TypeDetails> typeDetailsMap,
			Map<String, Patient> patientMap, String username, String uuid, String sysOutFragment) throws InterruptedException, ParseException {
		
		List<LogBreastFeedingSupportivePractice> bfspList = new ArrayList<>();
		
		for (int row = 1; row <= bfspSheet.getLastRowNum(); row++) {
			Thread.sleep(100);
			System.out.println(sysOutFragment + (row+1) + " for BFSP");
			
			LogBreastFeedingSupportivePractice bfsp = new LogBreastFeedingSupportivePractice();
			bfsp.setCreatedBy(username);
			bfsp.setUpdatedBy(username);
			bfsp.setUuidNumber(uuid);
			bfsp.setUpdatedDate(new Timestamp(new Date().getTime()));
			bfsp.setCreatedDate(new Timestamp(new Date().getTime()));
			XSSFRow xssfRow = bfspSheet.getRow(row);

			for (int cols = 0; cols < xssfRow.getLastCellNum(); cols++) {
				Cell cell = xssfRow.getCell(cols);

				if (cell != null) {
					switch (cols) {
					case 0:
						bfsp.setPatientId(patientMap.get(cell.getStringCellValue()));
						System.out.println(sysOutFragment + (row+1) + " column --> PatientId");
						break;
					case 3:
						bfsp.setDateAndTimeOfBFSP(getTimestampFromString(cell.getStringCellValue()));
						System.out.println(sysOutFragment + (row+1) + " column --> DateAndTimeOfBFSP");
						break;
					case 4:
						bfsp.setBfspPerformed(typeDetailsMap.get(cell.getStringCellValue()));
						System.out.println(sysOutFragment + (row+1) + " column --> BfspPerformed");
						break;
					case 5:
						bfsp.setPersonWhoPerformedBFSP(typeDetailsMap.get(cell.getStringCellValue()));
						System.out.println(sysOutFragment + (row+1) + " column --> PersonWhoPerformedBFSP");
						break;
					case 6:
						bfsp.setBfspDuration((int) cell.getNumericCellValue());
						System.out.println(sysOutFragment + (row+1) + " column --> BfspDuration");
						break;
					default:
						log.error("Error occured in switch case in BFSP in row no --> " + (row+1) + " column no --> " + (cols+1));
						break;
					}
				}
			}
			if(bfsp.getPatientId() != null && bfsp.getDateAndTimeOfBFSP() != null) {
				bfsp.setUniqueFormId(bfsp.getPatientId().getBabyCode() + "bfps" + sdfDateInteger.format(new Date()));
				bfspList.add(bfsp);
			}else {
				log.warn("BFSP --> patient id or date and time of exp is missing or incorrect for row no --->>>" + (row+1));
			}
		}
		
		if(bfspList != null && !bfspList.isEmpty())
			logBreastFeedingSupportivePracticeRepository.save(bfspList);
	}

	private void importLogExpBfData(XSSFSheet logExpressionBfSheet, Map<String, TypeDetails> typeDetailsMap,
			Map<String, Patient> patientMap, String username, String uuid, String sysOutFragment) throws ParseException, InterruptedException {
		List<LogExpressionBreastFeed> bfExps = new ArrayList<>();
		for (int row = 1; row <= logExpressionBfSheet.getLastRowNum(); row++) {
			Thread.sleep(100);
			System.out.println(sysOutFragment + (row+1) + " for BF Expressions");
			LogExpressionBreastFeed bfExp = new LogExpressionBreastFeed();
			bfExp.setCreatedBy(username);
			bfExp.setUpdatedBy(username);
			bfExp.setUuidNumber(uuid);
			bfExp.setUpdatedDate(new Timestamp(new Date().getTime()));
			bfExp.setCreatedDate(new Timestamp(new Date().getTime()));
			XSSFRow xssfRow = logExpressionBfSheet.getRow(row);

			for (int cols = 0; cols < xssfRow.getLastCellNum(); cols++) {
				Cell cell = xssfRow.getCell(cols);

				if (cell != null) {
					switch (cols) {
					case 0:
						bfExp.setPatientId(patientMap.get(cell.getStringCellValue()));
						System.out.println(sysOutFragment + (row+1) + " column --> patient id");
						break;
					case 3:
						bfExp.setDateAndTimeOfExpression(getTimestampFromString(cell.getStringCellValue()));
						System.out.println(sysOutFragment + (row+1) + " column --> DateAndTimeOfExpression");
						break;
					case 4:
						bfExp.setMethodOfExpression(typeDetailsMap.get(cell.getStringCellValue()));
						System.out.println(sysOutFragment + (row+1) + " column --> MethodOfExpression");
						break;
					case 5:
						bfExp.setMethodOfExpressionOthers(cell.getStringCellValue());
						System.out.println(sysOutFragment + (row+1) + " column --> MethodOfExpressionOthers");
						break;
					case 6:
						bfExp.setExpressionOccuredLocation(typeDetailsMap.get(cell.getStringCellValue()));
						System.out.println(sysOutFragment + (row+1) + " column --> ExpressionOccuredLocation");
						break;
					case 7:
						bfExp.setMilkExpressedFromLeftAndRightBreast(cell.getNumericCellValue());
						System.out.println(sysOutFragment + (row+1) + " column --> MilkExpressedFromLeftAndRightBreast");
						break;
					}
				}
			}

			if (bfExp.getPatientId() != null && bfExp.getDateAndTimeOfExpression() != null) {
				bfExp.setUniqueFormId(bfExp.getPatientId().getBabyCode() + "bfid" + sdfDateInteger.format(new Date()));
				bfExps.add(bfExp);
			} else {
				log.warn("BFExpression: patient id or date and time of exp is missing or incorrect for row no --->>>" + (row+1));
			}
		}

		if(bfExps != null && !bfExps.isEmpty())
			logExpressionBreastFeedRepository.save(bfExps);
	}

	private void importPatientData(XSSFSheet patientSheet, Map<String, TypeDetails> typeDetailsMap, String username, String uuid, String sysOutFragment)
			throws ParseException {
		List<Patient> patientList = new ArrayList<>();
		for (int row = 1; row <= patientSheet.getLastRowNum(); row++) {
			
			System.out.println(sysOutFragment + (row+1));
			
			Patient patient = new Patient();
			patient.setCreatedBy(username);
			patient.setUpdatedBy(username);
			patient.setUuidNumber(uuid);
			patient.setUpdatedDate(new Timestamp(new Date().getTime()));
			
			XSSFRow xssfRow = patientSheet.getRow(row);

			for (int cols = 0; cols < xssfRow.getLastCellNum(); cols++) {
				Cell cell = xssfRow.getCell(cols);

				if (cell != null) {
					switch (cols) {
					case 1:
						patient.setCreatedDate(getTimestampFromString(cell.getStringCellValue()));
						System.out.println(sysOutFragment + (row+1) + " column --> created date");
						break;
					case 6:
						patient.setBabyCode(cell.getStringCellValue());
						System.out.println(sysOutFragment + (row+1) + " column --> baby code");
						break;
					case 7:
						patient.setBabyCodeHospital(String.valueOf((int) cell.getNumericCellValue()));
						System.out.println(sysOutFragment + (row+1) + " column --> baby code hospital");
						break;
					case 8:
						patient.setBabyOf(cell.getStringCellValue());
						System.out.println(sysOutFragment + (row+1) + " column --> baby of");
						break;
					case 9:
						patient.setMothersAge((int) cell.getNumericCellValue());
						System.out.println(sysOutFragment + (row+1) + " column --> mothers age");
						break;
					case 12:
						patient.setDeliveryDateAndTime(getTimestampFromString(cell.getStringCellValue()));
						System.out.println(sysOutFragment + (row+1) + " column --> deilvery date and time");
						break;
					case 13:
						patient.setDeliveryMethod(typeDetailsMap.get(cell.getStringCellValue()));
						System.out.println(sysOutFragment + (row+1) + " column --> deilvery method");
						break;
					case 14:
						patient.setBabyWeight(cell.getNumericCellValue());
						System.out.println(sysOutFragment + (row+1) + " column --> baby weight");
						break;
					case 15:
						patient.setGestationalAgeInWeek((int) cell.getNumericCellValue());
						System.out.println(sysOutFragment + (row+1) + " column --> gestational age in weeks");
						break;
					case 16:
						patient.setMothersPrenatalIntent(typeDetailsMap.get(cell.getStringCellValue()));
						System.out.println(sysOutFragment + (row+1) + " column --> mothers prenatal intent");
						break;
					case 17:
						patient.setParentsKnowledgeOnHmAndLactation(typeDetailsMap.get(cell.getStringCellValue()));
						System.out.println(
								sysOutFragment + (row+1) + " column --> Parents Knowledge On Hm And Lactation");
						break;
					case 18:
						patient.setTimeTillFirstExpression(String.valueOf(cell.getNumericCellValue()));
						System.out.println(cell.getNumericCellValue());
						System.out.println(sysOutFragment + (row+1) + " column --> Time till first expression");
						break;
					case 19:
						patient.setInpatientOrOutPatient(typeDetailsMap.get(cell.getStringCellValue()));
						System.out.println(sysOutFragment + (row+1) + " column --> Inpatient or outpatient");
						break;
					case 20:
						if (patient.getInpatientOrOutPatient().getId() == 13) {
							patient.setAdmissionDateForOutdoorPatients(
									getTimestampFromString(cell.getStringCellValue()));
							System.out.println(
									sysOutFragment + (row+1) + " column --> admission date for outdoor patients");
						}
						break;
					case 21:
						patient.setBabyAdmittedTo(typeDetailsMap.get(cell.getStringCellValue()));
						System.out.println(sysOutFragment + (row+1) + " column --> baby admitted to");
						break;
					case 22:
						patient.setNicuAdmissionReason(reasonsToIds(cell.getStringCellValue(), typeDetailsMap));
						System.out.println(sysOutFragment + (row+1) + " column --> nicu admission reason");
						break;
					case 23:
						patient.setDischargeDate(getTimestampFromString(cell.getStringCellValue()));
						System.out.println(sysOutFragment + (row+1) + " column --> discharge date");
						break;
					default:
						log.error("Patient: error occured in switch case for row --> " + (row+1) + " column ---> " + (cols+1));
					}
				}
			}

			patientList.add(patient);
		}

		if(patientList != null && !patientList.isEmpty())
			patientRepository.save(patientList);
		System.out.println("Add patient import completed");
	}

	private Timestamp getTimestampFromString(String date) throws ParseException {
		return new Timestamp(sdfDateOnly.parse(date).getTime());
	}

	private String reasonsToIds(String reasons, Map<String, TypeDetails> typeDetailsMap) {
		StringBuilder arrayAsString = new StringBuilder();
		String[] reasonArray = reasons.split(",");

		for (int i = 0; i < reasonArray.length; i++) {
			arrayAsString.append(typeDetailsMap.get(reasonArray[i]).getId() + ",");
		}
		return arrayAsString.substring(0, arrayAsString.length() - 1);
	}

}
