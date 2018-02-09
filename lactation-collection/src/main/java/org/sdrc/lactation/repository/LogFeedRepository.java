package org.sdrc.lactation.repository;

import org.sdrc.lactation.domain.LogFeed;

/**
 * 
 * @author Naseem Akhtar (naseem@sdrc.co.in) on 9th February 2018 17:10. This
 *         repository will be used to store data coming from LogFeed form.
 */

public interface LogFeedRepository {

	void save(Iterable<LogFeed> logFeedList);

}
