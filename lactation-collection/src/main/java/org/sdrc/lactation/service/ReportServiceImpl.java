package org.sdrc.lactation.service;

import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.sdrc.lactation.domain.LactationUser;
import org.sdrc.lactation.domain.LogBreastFeedingPostDischarge;
import org.sdrc.lactation.domain.LogBreastFeedingSupportivePractice;
import org.sdrc.lactation.domain.LogExpressionBreastFeed;
import org.sdrc.lactation.domain.LogFeed;
import org.sdrc.lactation.domain.Patient;
import org.sdrc.lactation.repository.LactationUserRepository;
import org.sdrc.lactation.repository.LogBreastFeedingPostDischargeRepository;
import org.sdrc.lactation.repository.LogBreastFeedingSupportivePracticeRepository;
import org.sdrc.lactation.repository.LogExpressionBreastFeedRepository;
import org.sdrc.lactation.repository.LogFeedRepository;
import org.sdrc.lactation.repository.PatientRepository;
import org.sdrc.lactation.utils.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *@author Naseem Akhtar - (naseem@sdrc.co.in) on 5th April 2018 11:30
 *@category - Report
 *
 *This service class contains the method which is written for internal daily report generation.
 */

@Service
public class ReportServiceImpl {

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
	private EmailService emailService;
	
	@Autowired
	private LactationUserRepository lactationUserRepository;
	
	private SimpleDateFormat sdfDateOnly = new SimpleDateFormat("yyyy-MM-dd");
	
	private SimpleDateFormat sdfDateTimeWithSeconds = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private SimpleDateFormat sdfDateInteger = new SimpleDateFormat("ddMMyyyyHHmmssSSS");
	
	
	/**
	 * @author - Naseem Akhtar - (naseem@sdrc.co.in) on 5th April 2018 11:30
	 *  
	 * This method will run every day at 12:00 AM and send an email with excel file as an attachment 
	 * to the internal Lactation project group of SDRC.
	 *
	 * The excel file contains the following :-
	 * 1. Cumulative list of all the babies in the database
	 * 2. Babies registered on the previous day (w.r.t report generation date)
	 * 3. Users who have synced on the previous day (w.r.t report generation date)
	 */
	@Scheduled(
//			cron="*/10 * * * * *"
			cron="0 0 0 * * *"
			)
	@Transactional(readOnly = true)
	public void generateReport(){
		
		// setting file path
		String filePath = "/opt/lactation/dailyReport/daily_report_"+ sdfDateInteger.format(new Date()) + ".xlsx";
		
		//creating a workbook using try with resources, by doing so we do not need to close the workbook manually in the finally block.
		try(XSSFWorkbook workbook = new XSSFWorkbook(); FileOutputStream fileOut = new FileOutputStream(filePath);){
			
			// for fetching previous day date
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.add(Calendar.DATE, -1);
			
			//setting start and end date for querying the DB.
			Timestamp startDate = new Timestamp(sdfDateTimeWithSeconds.parse((sdfDateOnly.format(calendar.getTime()) + " 00:00:00")).getTime());
			Timestamp endDate = new Timestamp(sdfDateTimeWithSeconds.parse(sdfDateOnly.format(calendar.getTime()) + " 23:59:59").getTime());
			
			//creating two sheets in the workbook - 1. will contain baby details and 2. will contain user details.
			XSSFSheet babyReportSheet = workbook.createSheet("baby_list");
			XSSFSheet userReportSheet = workbook.createSheet("user_list");
			
			//creating cell style for this workbook
			XSSFCellStyle style = workbook.createCellStyle();
			
			//creating font for this particular workbook
			XSSFFont font = workbook.createFont();
			font.setBold(true);
			
			//setting the font on to the created cell style
			style.setFont(font);
			
			//setting heading of the patient columns which are being written in excel sheet
			int rowNum = 0;
	        int headingCol = 0;
	        Row headingRow = babyReportSheet.createRow(rowNum);
	        
	        Cell slNoHeadingCell = headingRow.createCell(headingCol); slNoHeadingCell.setCellValue("Sl no.");
	        slNoHeadingCell.setCellStyle(style); babyReportSheet.autoSizeColumn(headingCol); headingCol++;
	        
	        Cell babyListHeadingCell = headingRow.createCell(headingCol); babyListHeadingCell.setCellValue("Cumulative list of baby code");
	        babyListHeadingCell.setCellStyle(style); babyReportSheet.autoSizeColumn(headingCol); headingCol++;
	        
	        Cell previousDayBabyListHeadingCell = headingRow.createCell(headingCol);
	        previousDayBabyListHeadingCell.setCellValue("Babies created on - " + sdfDateOnly.format(calendar.getTime())); 
	        previousDayBabyListHeadingCell.setCellStyle(style); babyReportSheet.autoSizeColumn(headingCol); headingCol++;
	        
	        //finding all the babies in the DB for cumulative baby column in the excel sheet
			List<Patient> patientList = patientRepository.findAll();
			
			//iterating through patient entries and putting baby code in the sheet.
			rowNum++;
	        int slNo = 1;
			for(Patient patient : patientList){
				int col = 0;
				Row row = babyReportSheet.createRow(rowNum);
				
				row.createCell(col).setCellValue(slNo); col++;
				row.createCell(col).setCellValue(patient.getBabyCode()); slNo++; rowNum++;
			}
			
			//For keeping unique list of users who have synced the previous day (w.r.t report generation date)
			Set<String> lastDaySyncedUsers = new HashSet<>();
			
			//finding babies who have been registered the previous day (w.r.t report generation date)
			List<Patient> previousDayBabyList = patientRepository.findByCreatedDateBetween(startDate, endDate);
			
			/**
			 * Iterating through the baby entries which have been created yesterday and writing the baby code to the excel sheet, also keeping track of
			 * the users who have synced the previous day. 
			 */
			rowNum = 1;
			for(Patient patient : previousDayBabyList){
				int colNum = 2;
				Row row = babyReportSheet.getRow(rowNum);
				row.createCell(colNum).setCellValue(patient.getBabyCode());
				
				lastDaySyncedUsers.add(patient.getUpdatedBy()); rowNum++;
			}
			
			//The following queries to the 4 different tables is to find out the users who have synced the previous day (w.r.t report generation date) 
			List<LogExpressionBreastFeed> bfExpressions = logExpressionBreastFeedRepository.findByUpdatedDateBetween(startDate, endDate);
			List<LogBreastFeedingSupportivePractice> bfspList = logBreastFeedingSupportivePracticeRepository.findByUpdatedDateBetween(startDate, endDate);
			List<LogFeed> feedList = logFeedRepository.findByUpdatedDateBetween(startDate, endDate);
			List<LogBreastFeedingPostDischarge> bfpdList = logBreastFeedingPostDischargeRepository.findByUpdatedDateBetween(startDate, endDate);
			
			
			/**
			 * Keeping track of the users who have synced the previous day.
			 */ 
			bfExpressions.forEach(d -> lastDaySyncedUsers.add(d.getUpdatedBy()));
			bfspList.forEach(d -> lastDaySyncedUsers.add(d.getUpdatedBy()));
			feedList.forEach(d -> lastDaySyncedUsers.add(d.getUpdatedBy()));
			bfpdList.forEach(d -> lastDaySyncedUsers.add(d.getUpdatedBy()));
			
			rowNum = 0;
			headingCol = 0;
			
			Row userSheetHeading = userReportSheet.createRow(rowNum);
			
			Cell slNoHeading = userSheetHeading.createCell(headingCol); userReportSheet.autoSizeColumn(headingCol);
			slNoHeading.setCellValue("Sl no."); slNoHeading.setCellStyle(style); headingCol++;
			
			Cell cumulativeUsersHeading = userSheetHeading.createCell(headingCol); userReportSheet.autoSizeColumn(headingCol);
			cumulativeUsersHeading.setCellValue("Cumulative list of users"); cumulativeUsersHeading.setCellStyle(style); headingCol++;
			
			Cell lastDaySyncedUsersHeadingCell = userSheetHeading.createCell(headingCol); userReportSheet.autoSizeColumn(headingCol);
			lastDaySyncedUsersHeadingCell.setCellValue("List of users who synced on - " + sdfDateOnly.format(calendar.getTime()));
			lastDaySyncedUsersHeadingCell.setCellStyle(style);
			
			List<LactationUser> users = lactationUserRepository.findAll();
			
			rowNum = 1;
			slNo = 1;
			
			for(LactationUser user : users){
				int col = 0;
				Row row = userReportSheet.createRow(rowNum);
				row.createCell(col).setCellValue(slNo); col++; slNo++;
				row.createCell(col).setCellValue(user.getEmail());
				
				rowNum++;
			}
			
			for(String userName : lastDaySyncedUsers){
				int colNum = 2;
				Row row = userReportSheet.getRow(rowNum);
				row.createCell(colNum).setCellValue(userName); rowNum++;
			}
			
			workbook.write(fileOut);
			
			emailService.sendEmail("lactationproject@sdrc.co.in", null, "Lactation Project - Daily Report", "Please find attached daily submission report", filePath);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
