/**
 * @author Naseem Akhtar (naseem@sdrc.co.in) on 12th February 2018 2057.
 * This service will handle the request coming from mobile to SynchronizationController.
 */
package org.sdrc.lactation.service;

import java.util.List;

import org.sdrc.lactation.domain.LactationUser;
import org.sdrc.lactation.utils.SynchronizationModel;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;

public interface SynchronizationService {

	LactationUser registerUser(String firstName, String password);

	ResponseEntity<?> synchronizeForms(List<SynchronizationModel> synchronizationModels, HttpRequest httpRequest);

}
