package org.sdrc.lactation.repository.springdatajpa;

import java.util.List;

import org.sdrc.lactation.domain.LogBreastFeedingPostDischarge;
import org.sdrc.lactation.repository.LogBreastFeedingPostDischargeRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;

@RepositoryDefinition(domainClass = LogBreastFeedingPostDischarge.class, idClass = Integer.class)
public interface SpringDataLogBreastFeedingPostDischargeRepository extends LogBreastFeedingPostDischargeRepository {
	@Override
	@Query("SELECT l FROM LogBreastFeedingPostDischarge l where l.uniqueFormId in :uniqueIdList")
	public List<LogBreastFeedingPostDischarge> findByINId(@Param("uniqueIdList") List<String> uniqueIdList);
}
