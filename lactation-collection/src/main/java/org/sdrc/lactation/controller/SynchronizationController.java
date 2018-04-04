package org.sdrc.lactation.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.sdrc.lactation.service.SynchronizationService;
import org.sdrc.lactation.utils.SyncModel;
import org.sdrc.lactation.utils.SyncResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * @author Naseem Akhtar (naseem@sdrc.co.in) on 9th February 2018 17:10. This
 *         controller will be used to handle all the requests coming from the
 *         mobile application.
 */

@RestController
public class SynchronizationController {

	@Autowired
	private SynchronizationService synchronizationService;

	@CrossOrigin
	@RequestMapping(value = "/sync", method = RequestMethod.POST)
	public SyncResult synchronize(@RequestBody SyncModel synchronizationModel) {
		return synchronizationService.synchronizeForms(synchronizationModel, null);
	}

	@CrossOrigin
	@RequestMapping(value = "/serverStatus", method = RequestMethod.GET)
	public Boolean serverStatus() {
		return true;
	}
	
	@RequestMapping(value = "/setUniqueId", method = RequestMethod.GET)
	public Boolean setUniqueId() {
		return synchronizationService.setUniqueId();
	}
	
	@RequestMapping(value = "/downloadFile", method=RequestMethod.GET)
	public void downLoad(HttpServletResponse response) throws IOException {
		String name1 = synchronizationService.exportDataToExcel();
		InputStream inputStream;
		String fileName = "";
		try {
			fileName=name1.replaceAll("%3A", ":").replaceAll("%2F", "/")
						 .replaceAll("%5C", "/").replaceAll("%2C",",")
						 .replaceAll("\\+", " ").replaceAll("%22", "")
						 .replaceAll("%3F", "?").replaceAll("%3D", "=");
			inputStream = new FileInputStream(fileName);
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"",
					new java.io.File(fileName).getName());
			response.setHeader(headerKey, headerValue);
			response.setContentType("application/octet-stream"); //for all file type
			ServletOutputStream outputStream = response.getOutputStream();
			FileCopyUtils.copy(inputStream, outputStream);
			outputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
//			new File(fileName).delete();
		}
	}

	
}
