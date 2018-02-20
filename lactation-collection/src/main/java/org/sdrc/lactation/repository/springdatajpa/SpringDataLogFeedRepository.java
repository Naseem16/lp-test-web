package org.sdrc.lactation.repository.springdatajpa;

import java.util.List;

import org.sdrc.lactation.domain.LogFeed;
import org.sdrc.lactation.repository.LogFeedRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;

@RepositoryDefinition(domainClass = LogFeed.class, idClass = Integer.class)
public interface SpringDataLogFeedRepository extends LogFeedRepository {
	@Override
	@Query("SELECT l FROM LogFeed l where l.uniqueFormId in :uniqueIdList")
	public List<LogFeed> findByINId(@Param("uniqueIdList") List<String> uniqueIdList);
}
