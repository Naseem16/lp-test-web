package org.sdrc.lactation.repository;

import org.sdrc.lactation.domain.LogBreastFeedingPostDischarge;

/**
 * 
 * @author Naseem Akhtar (naseem@sdrc.co.in) on 9th February 2018 17:10. This
 *         repository will be used to store data of the
 *         LogBreastFeedingPostDischarge form.
 */

public interface LogBreastFeedingPostDischargeRepository {

	void save(Iterable<LogBreastFeedingPostDischarge> logBreastFeedingPostDischargeList);

}
