package org.sdrc.lactation.controller;

import java.util.List;

import org.sdrc.lactation.domain.LactationUser;
import org.sdrc.lactation.domain.LogFeed;
import org.sdrc.lactation.service.SynchronizationService;
import org.sdrc.lactation.utils.SynchronizationModel;
import org.sdrc.lactation.utils.SynchronizationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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

	@RequestMapping(value = "/sync", method = RequestMethod.GET)
	public LogFeed sync() {
		return new LogFeed();
	}

	@RequestMapping(value = "/registerUser", method = RequestMethod.GET)
	public LactationUser registerUser(@RequestParam("firstName") String firstName,
			@RequestParam("password") String password) {
		return synchronizationService.registerUser(firstName, password);
	}

	@RequestMapping(value = "/sync", method = RequestMethod.POST)
	public SynchronizationResult synchronize(@RequestBody List<SynchronizationModel> synchronizationModels) {
		return synchronizationService.synchronizeForms(synchronizationModels, null);
	}
}
