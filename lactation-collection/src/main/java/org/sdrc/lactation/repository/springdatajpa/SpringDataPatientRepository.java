package org.sdrc.lactation.repository.springdatajpa;

import java.util.List;

import org.sdrc.lactation.domain.Patient;
import org.sdrc.lactation.repository.PatientRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;

@RepositoryDefinition(domainClass=Patient.class , idClass=Integer.class)
public interface SpringDataPatientRepository extends PatientRepository {
	
	
	@Override
	@Query("SELECT p FROM Patient p where p.babyCode in :babyCodeList")
	public List<Patient> findByINBababyCode(@Param("babyCodeList") List<String> babyCodeList);

}
