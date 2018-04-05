/**
 * @author Naseem Akhtar (naseem@sdrc.co.in) on 12th February 2018 2057.
 * This service will handle the request coming from mobile to SynchronizationController.
 */
package org.sdrc.lactation.service;

import org.sdrc.lactation.utils.SyncModel;
import org.sdrc.lactation.utils.SyncResult;
import org.springframework.http.HttpRequest;

public interface SynchronizationService {

	SyncResult synchronizeForms(SyncModel synchronizationModels, HttpRequest httpRequest);

	Boolean setUniqueId();
	
}
