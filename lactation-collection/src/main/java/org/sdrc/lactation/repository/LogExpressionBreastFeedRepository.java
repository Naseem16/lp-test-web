package org.sdrc.lactation.repository;

import java.util.List;

import org.sdrc.lactation.domain.LogExpressionBreastFeed;

/**
 * 
 * @author Naseem Akhtar (naseem@sdrc.co.in) on 9th February 2018 17:10. This
 *         repository will be used to store data coming from
 *         LogExpressionBreastFeed forms.
 */

public interface LogExpressionBreastFeedRepository {

	void save(Iterable<LogExpressionBreastFeed> logExpressionBreastFeedList);
	
	List<LogExpressionBreastFeed> findByINId(List<String> uniqueIdList);

	List<LogExpressionBreastFeed> findByCreatedByIn(List<String> userNameByInstitution);

	List<LogExpressionBreastFeed> findByUniqueFormIdIsNull();

	List<LogExpressionBreastFeed> findAll();

}
