package org.sdrc.lactation.repository;

import java.util.List;
import java.util.Set;

import org.sdrc.lactation.domain.Patient;

/**
 * 
 * @author Naseem Akhtar (naseem@sdrc.co.in) on 9th February 2018 17:10. This
 *         repository will be used to store patient registration data.
 */

public interface PatientRepository {
	
	List<Patient> save(Iterable<Patient> patients);
	
	Patient findByBabyCode(String babyId);

	List<Patient> findByINBabyCode(List<String> babyCodeList);

	List<Patient> findByBabyCodeIn(Set<String> babyCodeList);

	List<Patient> findByCreatedByIn(List<String> userNameByInstitution);

	List<Patient> findByUuidNumberIsNull();

}
