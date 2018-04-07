package org.sdrc.lactation.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

/**
 * 
 * @author Naseem Akhtar (naseem@sdrc.co.in)
 *
 */

public interface DataDumpService {

	String exportDataInExcel(HttpServletRequest request, HttpServletResponse response);

	JSONObject exportDataInJson(HttpServletRequest request, HttpServletResponse response);

}
