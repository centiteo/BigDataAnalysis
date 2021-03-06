package com.cloudera.bigdata.analysis.index.search;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A set of {@link com.intel.its.server.search.IndexRange}, it's generated by
 * {@link com.intel.its.server.search.SearchStrategyDecider} and used by
 * {@link com.intel.its.server.search.IndexBasedSearchStrategy} to search index
 * entries.
 */
public class IndexRangeSet implements Iterable<IndexRange> {

  private static final Logger LOG = LoggerFactory
      .getLogger(IndexRangeSet.class);

  /** use tree set to keep index ranges ordered. */
  private Set<IndexRange> indexRanges = new TreeSet<IndexRange>();

  /*-------------------------------------------   Major Logic Methods   --------------------------------------------*/

  public static IndexRangeSet and(IndexRangeSet leftSet, IndexRangeSet rightSet) {
    if ((leftSet != null && leftSet.isNotEmpty())
        && (rightSet == null || rightSet.isEmpty())) {
      return leftSet.clone();
    } else if ((rightSet != null && rightSet.isNotEmpty())
        && (leftSet == null || leftSet.isEmpty())) {
      return rightSet.clone();
    } else if ((leftSet != null && leftSet.isNotEmpty())
        && (rightSet != null && rightSet.isNotEmpty())) {
      // do and operation!
      IndexRangeSet resultSet = new IndexRangeSet();
      for (IndexRange leftIndexRange : leftSet) {
        for (IndexRange rightIndexRange : rightSet) {
          IndexRange andMergedIndexRange = IndexRange.and(leftIndexRange,
              rightIndexRange);
          resultSet.add(andMergedIndexRange);
        }
      }
      return resultSet;
    } else {
      return new IndexRangeSet();
    }
  }

  public static IndexRangeSet or(IndexRangeSet leftSet, IndexRangeSet rightSet) {
    // if (LOG.isDebugEnabled()){
    // LOG.debug("OR: left RangeSet:"+leftSet);
    // LOG.debug("OR: right RangeSet:"+rightSet);
    // LOG.debug("left hash code:" + leftSet.hashCode());
    // LOG.debug("right hash code:" + rightSet.hashCode());
    // LOG.debug("left == right?" + leftSet.equals(rightSet));
    //
    // for (IndexRange left : leftSet) {
    // for (IndexRange right : rightSet) {
    // if(left.compareTo(right)==0){
    // LOG.debug("left compareTo right == 0" + left +"||"+right);
    // }
    // }
    // }
    // }
    if ((leftSet != null && leftSet.isNotEmpty())
        && (rightSet == null || rightSet.isEmpty())) {
      return leftSet.clone();
    } else if ((rightSet != null && rightSet.isNotEmpty())
        && (leftSet == null || leftSet.isEmpty())) {
      return rightSet.clone();
    } else if ((leftSet != null && leftSet.isNotEmpty())
        && (rightSet != null && rightSet.isNotEmpty())) {
      // do or operation!
      IndexRangeSet resultSet = new IndexRangeSet();
      resultSet.add(leftSet.clone());
      resultSet.add(rightSet.clone());
      // if (LOG.isDebugEnabled()){
      // LOG.debug("OR: Merged RangeSet:"+resultSet);
      // }
      return resultSet;
    } else {
      return new IndexRangeSet();
    }
  }

  public void add(IndexRange indexRange) {
    indexRanges.add(indexRange);
  }

  public void add(IndexRangeSet searchRanges) {
    this.indexRanges.addAll(searchRanges.getIndexRanges());
  }

  public boolean isEmpty() {
    return CollectionUtils.isEmpty(indexRanges);
  }

  public boolean isNotEmpty() {
    return CollectionUtils.isNotEmpty(indexRanges);
  }

  public IndexRangeSet clone() {
    IndexRangeSet indexRangeSet = new IndexRangeSet();
    for (IndexRange indexRange : indexRanges) {
      indexRangeSet.add(indexRange.clone());
    }
    return indexRangeSet;
  }

  /*--------------------------------------------     Common Methods    ---------------------------------------------*/

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("{");
    for (IndexRange indexRange : indexRanges) {
      sb.append(indexRange).append(",");
    }
    sb.append("}");
    return "IndexRangeSet{" + "indexRanges=" + sb.toString() + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    IndexRangeSet that = (IndexRangeSet) o;
    if (indexRanges.size() != that.indexRanges.size())
      return false;
    Iterator<IndexRange> thisIterator = indexRanges.iterator();
    Iterator<IndexRange> thatIterator = that.indexRanges.iterator();
    while (thisIterator.hasNext()) {
      if (!thisIterator.next().equals(thatIterator.next())) {
        return false;
      }
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 17;
    for (IndexRange indexRange : indexRanges) {
      hashCode = 31 * hashCode + indexRange.hashCode();
    }
    return hashCode;
  }

  @Override
  public Iterator<IndexRange> iterator() {
    return new Iterator<IndexRange>() {
      private Iterator<IndexRange> indexRangeSetIter = indexRanges.iterator();

      @Override
      public boolean hasNext() {
        return indexRangeSetIter.hasNext();
      }

      @Override
      public IndexRange next() {
        return indexRangeSetIter.next();
      }

      @Override
      public void remove() {
        indexRangeSetIter.remove();
      }
    };
  }

  /*--------------------------------------------    Getters/Setters    ---------------------------------------------*/

  public Set<IndexRange> getIndexRanges() {
    return indexRanges;
  }
}
