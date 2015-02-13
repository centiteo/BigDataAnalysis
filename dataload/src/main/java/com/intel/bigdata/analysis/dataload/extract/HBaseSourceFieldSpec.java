package com.intel.bigdata.analysis.dataload.extract;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import com.intel.bigdata.analysis.dataload.Constants;
import com.intel.bigdata.analysis.dataload.io.ConfigReader;

public class HBaseSourceFieldSpec {
  private Properties props;
  private String hbaseSourceTableName;
  private String toBeCleanedRowkeyRangeSpec;
  private String toBeCleanedCellSpec;
  private ArrayList<String> toBeCleanedCellNames = null;
  private Map<String, String> toBeCleanedCellMap = null;

  public String getHbaseSourceTableName() {
    return hbaseSourceTableName;
  }

  public String getToBeCleanedRowkeyRangeSpec() {
    return toBeCleanedRowkeyRangeSpec;
  }

  public String getToBeCleanedCellSpec() {
    return toBeCleanedCellSpec;
  }

  public ArrayList<String> getToBeCleanedCellNames() {
    return toBeCleanedCellNames;
  }

  public Map<String, String> getToBeCleanedCellMap() {
    return toBeCleanedCellMap;
  }
  
  public HBaseSourceFieldSpec(Properties props){
	  this.props = props;
	  readHBaseSourceFieldSpecs();
  }

  public void readHBaseSourceFieldSpecs() {

    hbaseSourceTableName = props.getProperty(Constants.HBASE_SOURCE_TABLE_NAME);
    
    toBeCleanedRowkeyRangeSpec = (!props
        .containsKey(Constants.TO_BE_CLEANED_ROWKEY_RANGE_SPEC) ? toBeCleanedRowkeyRangeSpec
        : props.getProperty(Constants.TO_BE_CLEANED_ROWKEY_RANGE_SPEC));
    
    toBeCleanedCellSpec = (!props
        .containsKey(Constants.TO_BE_CLEANED_CELL_SPEC) ? toBeCleanedCellSpec
        : props.getProperty(Constants.TO_BE_CLEANED_CELL_SPEC));

    String[] toBeCleanedCellSpecs = props
        .getProperty(Constants.TO_BE_CLEANED_CELL_SPEC).trim()
        .split(Constants.CELL_SPLIT_CHARACTER);

    ArrayList<String> theToBeCleanedCellNames = new ArrayList<String>();
    Map<String, String> theToBeCleanedCellMap = new HashMap<String, String>();

    toBeCleanedCellNames = theToBeCleanedCellNames;
    toBeCleanedCellMap = theToBeCleanedCellMap;

    for (String toBeCleanedCellSpec : toBeCleanedCellSpecs) {
      theToBeCleanedCellNames.add(toBeCleanedCellSpec);
      String[] familyQualifierPairs = StringUtils
          .splitByWholeSeparatorPreserveAllTokens(toBeCleanedCellSpec,
              Constants.FAMILY_QUALIFIER_SPLIT_CHARACTER);
      theToBeCleanedCellMap.put(familyQualifierPairs[1],
          familyQualifierPairs[0]);
    }
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("HBaseSourceTableName:").append(hbaseSourceTableName)
        .append("\n");
    sb.append("ToBeCleanedCellSpec:").append(toBeCleanedCellSpec).append("\n");
    sb.append("ToBeCleanedRowkeyRangeSpec:").append(toBeCleanedRowkeyRangeSpec)
        .append("\n");
    sb.append("ToBeCleanedCellMap:").append(toBeCleanedCellMap).append("\n");
    sb.append("ToBeCleanedCellNames:").append(toBeCleanedCellNames);
    return sb.toString();
  }

  public static void main(String[] args) throws Exception {
	  Properties props = new Properties();
	  props.load(new FileInputStream("etl-hbase2hbase-conf.properties"));
    HBaseSourceFieldSpec hsfs = new HBaseSourceFieldSpec(props);
    System.out.println(hsfs.toString());
  }
}
