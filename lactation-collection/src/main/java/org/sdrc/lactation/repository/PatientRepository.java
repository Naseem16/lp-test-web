package org.sdrc.lactation.repository;

import org.sdrc.lactation.domain.Patient;

/**
 * 
 * @author Naseem Akhtar (naseem@sdrc.co.in) on 9th February 2018 17:10. This
 *         repository will be used to store patient registration data.
 */

public interface PatientRepository {

	Patient save(Patient baby);
	
	Patient findByBabyCode(String babyId);

}
