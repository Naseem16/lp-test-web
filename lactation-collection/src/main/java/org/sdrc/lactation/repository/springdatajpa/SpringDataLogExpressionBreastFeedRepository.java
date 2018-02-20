package org.sdrc.lactation.repository.springdatajpa;

import java.util.List;

import org.sdrc.lactation.domain.LogExpressionBreastFeed;
import org.sdrc.lactation.repository.LogExpressionBreastFeedRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;

@RepositoryDefinition(domainClass = LogExpressionBreastFeed.class, idClass = Integer.class)
public interface SpringDataLogExpressionBreastFeedRepository extends LogExpressionBreastFeedRepository {

	@Override
	@Query("SELECT l FROM LogExpressionBreastFeed l where l.uniqueFormId in :uniqueIdList")
	public List<LogExpressionBreastFeed> findByINId(@Param("uniqueIdList") List<String> uniqueIdList);
}
