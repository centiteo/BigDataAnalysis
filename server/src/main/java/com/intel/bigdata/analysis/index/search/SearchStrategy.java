package com.intel.bigdata.analysis.index.search;

import org.apache.hadoop.hbase.regionserver.HRegion;

import com.intel.bigdata.analysis.query.Query;
import com.intel.bigdata.analysis.query.QueryResult;

/**
 * The interface of all search strategy.
 * 
 * @see com.intel.its.server.search.IndexBasedSearchStrategy
 * @see FullTableScanBasedSearchStrategy
 */
public interface SearchStrategy {
  /**
   * Do search job for given query on given region.
   * 
   * @param region
   *          the given region
   * @param query
   *          the query object
   * @return the query result.
   */
  QueryResult doSearch(HRegion region, Query query);
}
