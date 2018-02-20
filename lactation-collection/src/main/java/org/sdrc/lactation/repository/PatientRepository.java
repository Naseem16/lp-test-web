package org.sdrc.lactation.repository;

import java.util.List;

import org.sdrc.lactation.domain.Patient;

/**
 * 
 * @author Naseem Akhtar (naseem@sdrc.co.in) on 9th February 2018 17:10. This
 *         repository will be used to store patient registration data.
 */

public interface PatientRepository {
	
	void save(Iterable<Patient> patients);
	
	Patient findByBabyCode(String babyId);

	List<Patient> findByINBabyCode(List<String> babyCodeList);

}
