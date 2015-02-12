package com.intel.bigdata.analysis.hbase.query;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterBase;
import org.apache.hadoop.hbase.filter.ParseFilter;
import org.apache.hadoop.hbase.io.HbaseObjectWritable;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.DoubleWritable;

import com.google.common.base.Preconditions;

public class MultiColumnValueFilter extends FilterBase{
	static final Log LOG = LogFactory.getLog(MultiColumnValueFilter.class);
	  protected byte [] columnFamily;
	  protected byte [] columnQualifier;
	  private CompareOp compareOp;
	//  private WritableByteArrayComparable comparator;
	 // ptrivate DoubleWritable.Comparator comparaor;
	  private DoubleWritable doubleWritable;
	  private byte[] split;
	  private boolean foundColumn = false;
	  private boolean matchedColumn = false;
	  private boolean filterIfMissing = false;
	  private boolean latestVersionOnly = true;

	  /**
	   * Writable constructor, do not use.
	   */
	  public MultiColumnValueFilter() {
	  
	  }

	  /**
	   * Constructor for binary compare of the value of a single column.  If the
	   * column is found and the condition passes, all columns of the row will be
	   * emitted.  If the condition fails, the row will not be emitted.
	   * <p>
	   * Use the filterIfColumnMissing flag to set whether the rest of the columns
	   * in a row will be emitted if the specified column to check is not found in
	   * the row.
	   *
	   * @param family name of column family
	   * @param qualifier name of column qualifier
	   * @param compareOp operator
	   * @param value value to compare column values against
	   */
	  public MultiColumnValueFilter(final byte [] family, final byte [] qualifier,
	      final CompareOp compareOp, final byte[] value,final byte[] split) {
	    this(family, qualifier, compareOp, new DoubleWritable(Bytes.toDouble(value)),split);
	  }

	  /**
	   * Constructor for binary compare of the value of a single column.  If the
	   * column is found and the condition passes, all columns of the row will be
	   * emitted.  If the condition fails, the row will not be emitted.
	   * <p>
	   * Use the filterIfColumnMissing flag to set whether the rest of the columns
	   * in a row will be emitted if the specified column to check is not found in
	   * the row.
	   *
	   * @param family name of column family
	   * @param qualifier name of column qualifier
	   * @param compareOp operator
	   * @param comparator Comparator to use.
	   */
	  public MultiColumnValueFilter(final byte [] family, final byte [] qualifier,
	      final CompareOp compareOp, final DoubleWritable doubleWritable,final byte[] split) {
	    this.columnFamily = family;
	    this.columnQualifier = qualifier;
	    this.compareOp = compareOp;
	    this.doubleWritable = doubleWritable;
	    this.split = split;
	  }

	  /**
	   * @return operator
	   */
	  public CompareOp getOperator() {
	    return compareOp;
	  }

	  /**
	   * @return the doubleWritable
	   */
	  public DoubleWritable getDoubleWritable() {
	    return doubleWritable;
	  }

	  /**
	   * @return the family
	   */
	  public byte[] getFamily() {
	    return columnFamily;
	  }

	  /**
	   * @return the qualifier
	   */
	  public byte[] getQualifier() {
	    return columnQualifier;
	  }

	  public ReturnCode filterKeyValue(KeyValue keyValue) {
	    // System.out.println("REMOVE KEY=" + keyValue.toString() + ", value=" + Bytes.toString(keyValue.getValue()));
	    if (this.matchedColumn) {
	      // We already found and matched the single column, all keys now pass
	      return ReturnCode.INCLUDE;
	    } else if (this.latestVersionOnly && this.foundColumn) {
	      // We found but did not match the single column, skip to next row
	      return ReturnCode.NEXT_ROW;
	    }
	    if (!keyValue.matchingColumn(this.columnFamily, this.columnQualifier)) {
	      return ReturnCode.INCLUDE;
	    }
	    foundColumn = true;
	    
	    if (filterColumnValue(keyValue.getValue(),
	        keyValue.getValueOffset(), keyValue.getValueLength())) {
	    	//LOG.info(Bytes.toString(keyValue.getValue()));
	    	//LOG.info(Bytes.toString(keyValue.getQualifier()));
	      return this.latestVersionOnly? ReturnCode.NEXT_ROW: ReturnCode.INCLUDE;
	    }
	    this.matchedColumn = true;
	    return ReturnCode.INCLUDE;
	  }

	  private boolean filterColumnValue(final byte [] data, final int offset,
	      final int length) {
		
		//byte [] qualifier = Arrays.copyOfRange(data, offset,
	//			  length + offset);
		double result = dataTransfer(data);
	    //int compareResult = this.comparator.compareTo(result , 0, result.length);
		int compareResult = new DoubleWritable(result).compareTo(doubleWritable);
	    //LOG.info(compareResult);
		switch (this.compareOp) {
	    case LESS:
	      return compareResult >= 0;
	    case LESS_OR_EQUAL:
	      return compareResult > 0;
	    case EQUAL:
	      return compareResult != 0;
	    case NOT_EQUAL:
	      return compareResult == 0;
	    case GREATER_OR_EQUAL:
	      return compareResult < 0;
	    case GREATER:
	      return compareResult <= 0;
	    default:
	      throw new RuntimeException("Unknown Compare op " + compareOp.name());
	    }
	  }

	  private double dataTransfer(byte[] data){
		  String s = Bytes.toString(data);
		  //LOG.info("================="+s);
		  String splitStr = Bytes.toString(this.split);
		  String[] array = s.split("\\"+splitStr);
		  double result = 0;
		  for(String str:array){
			  //LOG.info("str===="+str);
			  result+=Double.parseDouble(str);
		  }
		  //LOG.info("result========"+result);
		  return result;
	  }
	  
	  public boolean filterRow() {
	    // If column was found, return false if it was matched, true if it was not
	    // If column not found, return true if we filter if missing, false if not
	    return this.foundColumn? !this.matchedColumn: this.filterIfMissing;
	  }

	  public void reset() {
	    foundColumn = false;
	    matchedColumn = false;
	  }

	  /**
	   * Get whether entire row should be filtered if column is not found.
	   * @return true if row should be skipped if column not found, false if row
	   * should be let through anyways
	   */
	  public boolean getFilterIfMissing() {
	    return filterIfMissing;
	  }

	  /**
	   * Set whether entire row should be filtered if column is not found.
	   * <p>
	   * If true, the entire row will be skipped if the column is not found.
	   * <p>
	   * If false, the row will pass if the column is not found.  This is default.
	   * @param filterIfMissing flag
	   */
	  public void setFilterIfMissing(boolean filterIfMissing) {
	    this.filterIfMissing = filterIfMissing;
	  }

	  /**
	   * Get whether only the latest version of the column value should be compared.
	   * If true, the row will be returned if only the latest version of the column
	   * value matches. If false, the row will be returned if any version of the
	   * column value matches. The default is true.
	   * @return return value
	   */
	  public boolean getLatestVersionOnly() {
	    return latestVersionOnly;
	  }

	  /**
	   * Set whether only the latest version of the column value should be compared.
	   * If true, the row will be returned if only the latest version of the column
	   * value matches. If false, the row will be returned if any version of the
	   * column value matches. The default is true.
	   * @param latestVersionOnly flag
	   */
	  public void setLatestVersionOnly(boolean latestVersionOnly) {
	    this.latestVersionOnly = latestVersionOnly;
	  }

	  public static Filter createFilterFromArguments(ArrayList<byte []> filterArguments) {
	    Preconditions.checkArgument(filterArguments.size() == 5 || filterArguments.size() == 7,
	                                "Expected 4 or 6 but got: %s", filterArguments.size());
	    byte [] family = ParseFilter.removeQuotesFromByteArray(filterArguments.get(0));
	    byte [] qualifier = ParseFilter.removeQuotesFromByteArray(filterArguments.get(1));
	    CompareOp compareOp = ParseFilter.createCompareOp(filterArguments.get(2));
	    //WritableByteArrayComparable comparator = ParseFilter.createComparator(
	    //  ParseFilter.removeQuotesFromByteArray(filterArguments.get(3)));
	    byte [] dwvalue = ParseFilter.removeQuotesFromByteArray(filterArguments.get(3));
	    DoubleWritable dw = new DoubleWritable(Bytes.toDouble(dwvalue));
	    byte [] split = ParseFilter.removeQuotesFromByteArray(filterArguments.get(4));
	    
	   // if (comparator instanceof RegexStringComparator ||
	   //     comparator instanceof SubstringComparator) {
	    //  if (compareOp != CompareOp.EQUAL &&
	    //      compareOp != CompareOp.NOT_EQUAL) {
	    //    throw new IllegalArgumentException ("A regexstring comparator and substring comparator " +
	    //                                        "can only be used with EQUAL and NOT_EQUAL");
	     // }
	   // }

	    MultiColumnValueFilter filter = new MultiColumnValueFilter(family, qualifier,
	                                                                 compareOp, dw,split);

	    if (filterArguments.size() == 7) {
	      boolean filterIfMissing = ParseFilter.convertByteArrayToBoolean(filterArguments.get(5));
	      boolean latestVersionOnly = ParseFilter.convertByteArrayToBoolean(filterArguments.get(6));
	      filter.setFilterIfMissing(filterIfMissing);
	      filter.setLatestVersionOnly(latestVersionOnly);
	    }
	    return filter;
	  }

	  public void readFields(final DataInput in) throws IOException {
	    this.columnFamily = Bytes.readByteArray(in);
	    if(this.columnFamily.length == 0) {
	      this.columnFamily = null;
	    }
	    this.columnQualifier = Bytes.readByteArray(in);
	    if(this.columnQualifier.length == 0) {
	      this.columnQualifier = null;
	    }
	    this.compareOp = CompareOp.valueOf(in.readUTF());
	    this.doubleWritable = (DoubleWritable)HbaseObjectWritable.readObject(in, null);
	      //(WritableByteArrayComparable)HbaseObjectWritable.readObject(in, null);
	    this.split = Bytes.readByteArray(in);
	    if(this.split.length == 0){
	    	this.split = null;
	    }
	    this.foundColumn = in.readBoolean();
	    this.matchedColumn = in.readBoolean();
	    this.filterIfMissing = in.readBoolean();
	    this.latestVersionOnly = in.readBoolean();
	  }

	  public void write(final DataOutput out) throws IOException {
	    Bytes.writeByteArray(out, this.columnFamily);
	    Bytes.writeByteArray(out, this.columnQualifier);
	    out.writeUTF(compareOp.name());
	   //HbaseObjectWritable.writeObject(out, comparator,
	    //    WritableByteArrayComparable.class, null);
	    //this.doubleWritable.write(out);
	    HbaseObjectWritable.writeObject(out, doubleWritable, DoubleWritable.class, null);
	    Bytes.writeByteArray(out, this.split);
	    out.writeBoolean(foundColumn);
	    out.writeBoolean(matchedColumn);
	    out.writeBoolean(filterIfMissing);
	    out.writeBoolean(latestVersionOnly);
	  }

	  @Override
	  public String toString() {
	    return String.format("%s (%s, %s, %s, %s)",
	        this.getClass().getSimpleName(), Bytes.toStringBinary(this.columnFamily),
	        Bytes.toStringBinary(this.columnQualifier), this.compareOp.name(),
	        //Bytes.toStringBinary(this.comparator.getValue()));
	        Bytes.toStringBinary(Bytes.toBytes(this.doubleWritable.get())));
	  }
}
