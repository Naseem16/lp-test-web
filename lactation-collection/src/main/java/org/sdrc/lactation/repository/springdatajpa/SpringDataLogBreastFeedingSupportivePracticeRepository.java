package org.sdrc.lactation.repository.springdatajpa;

import java.util.List;

import org.sdrc.lactation.domain.LogBreastFeedingSupportivePractice;
import org.sdrc.lactation.repository.LogBreastFeedingSupportivePracticeRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;

@RepositoryDefinition(domainClass = LogBreastFeedingSupportivePractice.class, idClass = Integer.class)
public interface SpringDataLogBreastFeedingSupportivePracticeRepository extends LogBreastFeedingSupportivePracticeRepository {
	@Override
	@Query("SELECT l FROM LogBreastFeedingSupportivePractice l where l.uniqueFormId in :uniqueIdList")
	public List<LogBreastFeedingSupportivePractice> findByINId(@Param("uniqueIdList") List<String> uniqueIdList);
}
