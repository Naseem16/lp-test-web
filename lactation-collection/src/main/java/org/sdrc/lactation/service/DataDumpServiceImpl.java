package org.sdrc.lactation.service;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
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
public class DataDumpServiceImpl implements DataDumpService {

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
	
	@Autowired
	private TypeDetailsRepository typeDetailsRepository;

	private SimpleDateFormat sdfDateInteger = new SimpleDateFormat("ddMMyyyyHHmmssSSS");
	
	private SimpleDateFormat sdfDateOnly = new SimpleDateFormat("yyyy-MM-dd");
	
	private SimpleDateFormat sdfDateTimeWithSeconds = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public String exportDataToExcel() {

		Map<Integer, TypeDetails> typeDetailsMap = new HashMap<>();
		typeDetailsRepository.findAll().forEach(typeDetails->typeDetailsMap.put(typeDetails.getId(), typeDetails));
		
		String filePath = "/opt/lactation/data_dump/dataDump_" + sdfDateInteger.format(new Date()) + ".xlsx";

		try (XSSFWorkbook workbook = new XSSFWorkbook(); FileOutputStream fileOut = new FileOutputStream(filePath);) {
			List<Patient> patients = patientRepository.findAll();
			List<LogExpressionBreastFeed> bfExpressions = logExpressionBreastFeedRepository.findAll();
			List<LogBreastFeedingSupportivePractice> bfsps = logBreastFeedingSupportivePracticeRepository.findAll();
			List<LogFeed> feeds = logFeedRepository.findAll();
			List<LogBreastFeedingPostDischarge> bfpds = logBreastFeedingPostDischargeRepository.findAll();

			XSSFSheet patientSheet = workbook.createSheet("Patients");
			XSSFSheet bfExpressionsSheet = workbook.createSheet("Bf Expressions");
			XSSFSheet bfspSheet = workbook.createSheet("BFSP");
			XSSFSheet logFeedSheet = workbook.createSheet("Log Feed");
			XSSFSheet bfpdSheet = workbook.createSheet("BFPD");

			// for styling the first row of every sheet
			XSSFFont font = workbook.createFont();
			font.setBold(true);
			font.setItalic(false);

			CellStyle cellStyle = workbook.createCellStyle();
			cellStyle.setFont(font);

			int slNo = 1;
			int rowNum = 0;
			Row headingRow = patientSheet.createRow(rowNum++);
			int headingCol = 0;

			headingRow.setRowStyle(cellStyle);
			// setting heading of patient sheet
			headingRow.createCell(headingCol).setCellValue("Sl no.");
			headingCol++;
			headingRow.createCell(headingCol).setCellValue("Baby Code");
			headingCol++;
			headingRow.createCell(headingCol).setCellValue("Baby code hospital");
			headingCol++;
			headingRow.createCell(headingCol).setCellValue("Baby of");
			headingCol++;
			headingRow.createCell(headingCol).setCellValue("Baby weight");
			headingCol++;
			headingRow.createCell(headingCol).setCellValue("Baby admitted to");
			headingCol++;
			headingRow.createCell(headingCol).setCellValue("Admission date for outdoor patients");
			headingCol++;
			headingRow.createCell(headingCol).setCellValue("Created by");
			headingCol++;
			headingRow.createCell(headingCol).setCellValue("Discharge date");
			headingCol++;
			headingRow.createCell(headingCol).setCellValue("Nicu admission reason");
			headingCol++;
			headingRow.createCell(headingCol).setCellValue("Time till first expression");
			headingCol++;
			headingRow.createCell(headingCol).setCellValue("Updated by");
			headingCol++;
			headingRow.createCell(headingCol).setCellValue("UUID");
			headingCol++;
			headingRow.createCell(headingCol).setCellValue("Created date");
			headingCol++;
			headingRow.createCell(headingCol).setCellValue("Delivery date and time");
			headingCol++;
			headingRow.createCell(headingCol).setCellValue("Gestational age in weeks");
			headingCol++;
			headingRow.createCell(headingCol).setCellValue("Mother's age");
			headingCol++;
			headingRow.createCell(headingCol).setCellValue("Updated date");
			headingCol++;
			headingRow.createCell(headingCol).setCellValue("Delivert method");
			headingCol++;
			headingRow.createCell(headingCol).setCellValue("Inpatient / Outpatient");
			headingCol++;
			headingRow.createCell(headingCol).setCellValue("Mother's prenatal intent");
			headingCol++;
			headingRow.createCell(headingCol).setCellValue("Parents knowledge on hm and lactation");
			headingCol = 0;

			// Iterating patient records and writing in the excel sheet
			for (Patient patient : patients) {
				Row row = patientSheet.createRow(rowNum++);
				int colNum = 0;

				row.createCell(colNum).setCellValue(slNo++);
				colNum++;
				row.createCell(colNum).setCellValue(patient.getBabyCode());
				colNum++;
				row.createCell(colNum).setCellValue(patient.getBabyCodeHospital() == null ? "" : patient.getBabyCodeHospital());
				colNum++;
				row.createCell(colNum).setCellValue(patient.getBabyOf() == null ? "" : patient.getBabyOf());
				colNum++;
				row.createCell(colNum).setCellValue(patient.getBabyWeight() == null ? "" : patient.getBabyWeight().toString());
				colNum++;
				row.createCell(colNum).setCellValue(patient.getBabyAdmittedTo() == null ? "" : patient.getBabyAdmittedTo().getName());
				colNum++;
				row.createCell(colNum).setCellValue(patient.getAdmissionDateForOutdoorPatients() ==  null ? "" : sdfDateOnly.format(patient.getAdmissionDateForOutdoorPatients()));
				colNum++;
				row.createCell(colNum).setCellValue(patient.getCreatedBy());
				colNum++;
				row.createCell(colNum).setCellValue(patient.getDischargeDate() == null ? "" : sdfDateOnly.format(patient.getDischargeDate()));
				colNum++;
				row.createCell(colNum).setCellValue(patient.getNicuAdmissionReason().length() == 0 ? "" : arrayToString(patient.getNicuAdmissionReason(), typeDetailsMap));
				colNum++;
				row.createCell(colNum).setCellValue(patient.getTimeTillFirstExpression() == null ? "" : patient.getTimeTillFirstExpression());
				colNum++;
				row.createCell(colNum).setCellValue(patient.getUpdatedBy());
				colNum++;
				row.createCell(colNum).setCellValue(patient.getUuidNumber() == null ? "" : patient.getUuidNumber());
				colNum++;
				row.createCell(colNum).setCellValue(sdfDateTimeWithSeconds.format(patient.getCreatedDate()));
				colNum++;
				row.createCell(colNum).setCellValue(sdfDateTimeWithSeconds.format(patient.getDeliveryDateAndTime()));
				colNum++;
				row.createCell(colNum).setCellValue(patient.getGestationalAgeInWeek() == null ? "" : patient.getGestationalAgeInWeek().toString());
				colNum++;
				row.createCell(colNum).setCellValue(patient.getMothersAge() == null ? "" : patient.getMothersAge().toString());
				colNum++;
				row.createCell(colNum).setCellValue(sdfDateTimeWithSeconds.format(patient.getUpdatedDate()));
				colNum++;
				row.createCell(colNum).setCellValue(patient.getDeliveryMethod() == null ? "" : patient.getDeliveryMethod().getName());
				colNum++;
				row.createCell(colNum).setCellValue(patient.getInpatientOrOutPatient() == null ? "" : patient.getInpatientOrOutPatient().getName());
				colNum++;
				row.createCell(colNum).setCellValue(patient.getMothersPrenatalIntent() == null ? "" : patient.getMothersPrenatalIntent().getName());
				colNum++;
				row.createCell(colNum).setCellValue(patient.getParentsKnowledgeOnHmAndLactation() == null ? "" : patient.getParentsKnowledgeOnHmAndLactation().getName());
			}

			rowNum = 0;
			slNo = 1;
			headingRow = bfExpressionsSheet.createRow(rowNum++);

			// setting heading of breastfeed expression sheet
			headingRow.createCell(headingCol).setCellValue("Sl no.");
			headingCol++;
			headingRow.createCell(headingCol).setCellValue("Created by");
			headingCol++;
			headingRow.createCell(headingCol).setCellValue("Unique form id");
			headingCol++;
			headingRow.createCell(headingCol).setCellValue("Updated by");
			headingCol++;
			headingRow.createCell(headingCol).setCellValue("UUID");
			headingCol++;
			headingRow.createCell(headingCol).setCellValue("Created date");
			headingCol++;
			headingRow.createCell(headingCol).setCellValue("Date and time of expression");
			headingCol++;
			headingRow.createCell(headingCol).setCellValue("Milk expressed from left and right breast");
			headingCol++;
			headingRow.createCell(headingCol).setCellValue("Updated date");
			headingCol++;
			headingRow.createCell(headingCol).setCellValue("Location where expression occured");
			headingCol++;
			headingRow.createCell(headingCol).setCellValue("Method of expression");
			headingCol++;
			headingRow.createCell(headingCol).setCellValue("Patient id");
			headingCol = 0;

			// iterating through breastfeed expressions
			for (LogExpressionBreastFeed bfExpression : bfExpressions) {
				Row row = bfExpressionsSheet.createRow(rowNum++);
				int colNum = 0;

				row.createCell(colNum).setCellValue(slNo++);
				colNum++;
				row.createCell(colNum).setCellValue(bfExpression.getCreatedBy());
				colNum++;
				row.createCell(colNum).setCellValue(bfExpression.getUniqueFormId());
				colNum++;
				row.createCell(colNum).setCellValue(bfExpression.getUpdatedBy());
				colNum++;
				row.createCell(colNum).setCellValue(bfExpression.getUuidNumber() == null ? "" : bfExpression.getUuidNumber());
				colNum++;
				row.createCell(colNum).setCellValue(sdfDateTimeWithSeconds.format(bfExpression.getCreatedDate()));
				colNum++;
				row.createCell(colNum).setCellValue(sdfDateTimeWithSeconds.format(bfExpression.getDateAndTimeOfExpression()));
				colNum++;
				row.createCell(colNum).setCellValue(bfExpression.getMilkExpressedFromLeftAndRightBreast() == null ? "" : bfExpression.getMilkExpressedFromLeftAndRightBreast().toString());
				colNum++;
				row.createCell(colNum).setCellValue(sdfDateTimeWithSeconds.format(bfExpression.getUpdatedDate()));
				colNum++;
				row.createCell(colNum).setCellValue(bfExpression.getExpressionOccuredLocation() == null ? "" : bfExpression.getExpressionOccuredLocation().getName());
				colNum++;
				row.createCell(colNum).setCellValue(bfExpression.getMethodOfExpression() == null ? "" : bfExpression.getMethodOfExpression().getName());
				colNum++;
				row.createCell(colNum).setCellValue(bfExpression.getPatientId().getBabyCode());
			}

			rowNum = 0;
			slNo = 1;
			headingRow = bfspSheet.createRow(rowNum++);

			// setting heading of bfsp sheet
			headingRow.createCell(headingCol).setCellValue("Sl no.");
			headingCol++;
			headingRow.createCell(headingCol).setCellValue("Created by");
			headingCol++;
			headingRow.createCell(headingCol).setCellValue("Unique form id");
			headingCol++;
			headingRow.createCell(headingCol).setCellValue("Updated by");
			headingCol++;
			headingRow.createCell(headingCol).setCellValue("UUID");
			headingCol++;
			headingRow.createCell(headingCol).setCellValue("Bfsp duration");
			headingCol++;
			headingRow.createCell(headingCol).setCellValue("Created date");
			headingCol++;
			headingRow.createCell(headingCol).setCellValue("Date and time of bfsp");
			headingCol++;
			headingRow.createCell(headingCol).setCellValue("Updated date");
			headingCol++;
			headingRow.createCell(headingCol).setCellValue("Bfsp performed");
			headingCol++;
			headingRow.createCell(headingCol).setCellValue("Patient id");
			headingCol++;
			headingRow.createCell(headingCol).setCellValue("Personn who performed BFSP");
			headingCol = 0;

			// iterating through bfsp entries and writing them in excel
			for (LogBreastFeedingSupportivePractice bfsp : bfsps) {
				Row row = bfspSheet.createRow(rowNum++);
				int colNum = 0;

				row.createCell(colNum).setCellValue(slNo++);
				colNum++;
				row.createCell(colNum).setCellValue(bfsp.getCreatedBy());
				colNum++;
				row.createCell(colNum).setCellValue(bfsp.getUniqueFormId());
				colNum++;
				row.createCell(colNum).setCellValue(bfsp.getUpdatedBy());
				colNum++;
				row.createCell(colNum).setCellValue(bfsp.getUuidNumber() == null ? "" : bfsp.getUuidNumber());
				colNum++;
				row.createCell(colNum).setCellValue(bfsp.getBfspDuration() == null ? "" : bfsp.getBfspDuration().toString());
				colNum++;
				row.createCell(colNum).setCellValue(sdfDateTimeWithSeconds.format(bfsp.getCreatedDate()));
				colNum++;
				row.createCell(colNum).setCellValue(sdfDateTimeWithSeconds.format(bfsp.getDateAndTimeOfBFSP()));
				colNum++;
				row.createCell(colNum).setCellValue(sdfDateTimeWithSeconds.format(bfsp.getUpdatedDate()));
				colNum++;
				row.createCell(colNum).setCellValue(bfsp.getBfspPerformed() == null ? "" : bfsp.getBfspPerformed().getName());
				colNum++;
				row.createCell(colNum).setCellValue(bfsp.getPatientId().getBabyCode());
				colNum++;
				row.createCell(colNum).setCellValue(bfsp.getPersonWhoPerformedBFSP() == null ? "" : bfsp.getPersonWhoPerformedBFSP().getName());
			}

			rowNum = 0;
			slNo = 1;
			headingRow = logFeedSheet.createRow(rowNum++);

			// setting heading of feed sheet
			headingRow.createCell(headingCol).setCellValue("Sl no.");
			headingCol++;
			headingRow.createCell(headingCol).setCellValue("Created by");
			headingCol++;
			headingRow.createCell(headingCol).setCellValue("Unique form id");
			headingCol++;
			headingRow.createCell(headingCol).setCellValue("Updated by");
			headingCol++;
			headingRow.createCell(headingCol).setCellValue("UUID");
			headingCol++;
			headingRow.createCell(headingCol).setCellValue("Animal milk volume");
			headingCol++;
			headingRow.createCell(headingCol).setCellValue("Created date");
			headingCol++;
			headingRow.createCell(headingCol).setCellValue("Date and time of feed");
			headingCol++;
			headingRow.createCell(headingCol).setCellValue("DHM volume");
			headingCol++;
			headingRow.createCell(headingCol).setCellValue("Formula volume");
			headingCol++;
			headingRow.createCell(headingCol).setCellValue("OMM volume");
			headingCol++;
			headingRow.createCell(headingCol).setCellValue("Other volume");
			headingCol++;
			headingRow.createCell(headingCol).setCellValue("Updated date");
			headingCol++;
			headingRow.createCell(headingCol).setCellValue("Weight of baby");
			headingCol++;
			headingRow.createCell(headingCol).setCellValue("Feed method");
			headingCol++;
			headingRow.createCell(headingCol).setCellValue("Location of feeding");
			headingCol++;
			headingRow.createCell(headingCol).setCellValue("Patient id");
			headingCol = 0;

			// iterating through feed entries and writing them in excel
			for (LogFeed feed : feeds) {
				Row row = logFeedSheet.createRow(rowNum++);
				int colNum = 0;

				row.createCell(colNum).setCellValue(slNo++);
				colNum++;
				row.createCell(colNum).setCellValue(feed.getCreatedBy());
				colNum++;
				row.createCell(colNum).setCellValue(feed.getUniqueFormId());
				colNum++;
				row.createCell(colNum).setCellValue(feed.getUpdatedBy());
				colNum++;
				row.createCell(colNum).setCellValue(feed.getUuidNumber() == null ? "" : feed.getUuidNumber());
				colNum++;
				row.createCell(colNum)
						.setCellValue(feed.getAnimalMilkVolume() == null ? "" : feed.getAnimalMilkVolume().toString());
				colNum++;
				row.createCell(colNum).setCellValue(sdfDateTimeWithSeconds.format(feed.getCreatedDate()));
				colNum++;
				row.createCell(colNum).setCellValue(sdfDateTimeWithSeconds.format(feed.getDateAndTimeOfFeed()));
				colNum++;
				row.createCell(colNum).setCellValue(feed.getDhmVolume() == null ? "" : feed.getDhmVolume().toString());
				colNum++;
				row.createCell(colNum)
						.setCellValue(feed.getFormulaVolume() == null ? "" : feed.getFormulaVolume().toString());
				colNum++;
				row.createCell(colNum).setCellValue(feed.getOmmVolume() == null ? "" : feed.getOmmVolume().toString());
				colNum++;
				row.createCell(colNum)
						.setCellValue(feed.getOtherVolume() == null ? "" : feed.getOtherVolume().toString());
				colNum++;
				row.createCell(colNum).setCellValue(sdfDateTimeWithSeconds.format(feed.getUpdatedDate()));
				colNum++;
				row.createCell(colNum).setCellValue(feed.getWeightOfBaby() == null ? "" : feed.getWeightOfBaby().toString());
				colNum++;
				row.createCell(colNum).setCellValue(feed.getFeedMethod() == null ? "" : feed.getFeedMethod().getName());
				colNum++;
				row.createCell(colNum).setCellValue(feed.getLocationOfFeeding() == null ? "" : feed.getLocationOfFeeding().getName());
				colNum++;
				row.createCell(colNum).setCellValue(feed.getPatientId().getBabyCode());
			}

			rowNum = 0;
			slNo = 1;
			headingRow = bfpdSheet.createRow(rowNum++);

			// setting heading of bfpd sheet
			headingRow.createCell(headingCol).setCellValue("Sl no.");
			headingCol++;
			headingRow.createCell(headingCol).setCellValue("Created by");
			headingCol++;
			headingRow.createCell(headingCol).setCellValue("Unique form id");
			headingCol++;
			headingRow.createCell(headingCol).setCellValue("Updated by");
			headingCol++;
			headingRow.createCell(headingCol).setCellValue("UUID");
			headingCol++;
			headingRow.createCell(headingCol).setCellValue("Created date");
			headingCol++;
			headingRow.createCell(headingCol).setCellValue("Date of breastfeeding");
			headingCol++;
			headingRow.createCell(headingCol).setCellValue("Updated date");
			headingCol++;
			headingRow.createCell(headingCol).setCellValue("Breastfeeding status");
			headingCol++;
			headingRow.createCell(headingCol).setCellValue("Patient id");
			headingCol++;
			headingRow.createCell(headingCol).setCellValue("Time of breastfeeding");
			headingCol = 0;

			// iterating through bfpd entries and writing them in excel
			for (LogBreastFeedingPostDischarge bfpd : bfpds) {
				Row row = bfpdSheet.createRow(rowNum++);
				int colNum = 0;

				row.createCell(colNum).setCellValue(slNo++);
				colNum++;
				row.createCell(colNum).setCellValue(bfpd.getCreatedBy());
				colNum++;
				row.createCell(colNum).setCellValue(bfpd.getUniqueFormId());
				colNum++;
				row.createCell(colNum).setCellValue(bfpd.getUpdatedBy());
				colNum++;
				row.createCell(colNum).setCellValue(bfpd.getUuidNumber() == null ? "" : bfpd.getUuidNumber());
				colNum++;
				row.createCell(colNum).setCellValue(sdfDateTimeWithSeconds.format(bfpd.getCreatedDate()));
				colNum++;
				row.createCell(colNum).setCellValue(sdfDateOnly.format(bfpd.getDateOfBreastFeeding()));
				colNum++;
				row.createCell(colNum).setCellValue(sdfDateTimeWithSeconds.format(bfpd.getUpdatedDate()));
				colNum++;
				row.createCell(colNum).setCellValue(bfpd.getBreastFeedingStatus() == null ? "" : bfpd.getBreastFeedingStatus().getName());
				colNum++;
				row.createCell(colNum).setCellValue(bfpd.getPatientId().getBabyCode());
				colNum++;
				row.createCell(colNum).setCellValue(bfpd.getTimeOfBreastFeeding().getName());
			}

			workbook.write(fileOut);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return filePath;
	}
	
	private String arrayToString(String admissionReason, Map<Integer, TypeDetails> typeDetailsMap){
		String[] nicuAdmissionReasons = admissionReason.split(",");
		StringBuilder reasonNameList = new StringBuilder();
		for(String reason : nicuAdmissionReasons){
			reasonNameList.append(typeDetailsMap.get(Integer.parseInt(reason)).getName() + ",");
		}
		return reasonNameList.substring(0, reasonNameList.length() - 1);
	}

}
