package org.sdrc.lactation.controller;

import org.sdrc.lactation.service.SynchronizationService;
import org.sdrc.lactation.utils.SyncModel;
import org.sdrc.lactation.utils.SyncResult;
import org.springframework.beans.factory.annotation.Autowired;
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
}
