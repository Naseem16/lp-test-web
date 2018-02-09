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
