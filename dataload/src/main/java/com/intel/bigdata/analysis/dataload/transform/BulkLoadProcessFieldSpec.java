package com.intel.bigdata.analysis.dataload.transform;

import java.util.Properties;

import com.cloudera.bigdata.analysis.util.Util;
import com.intel.bigdata.analysis.dataload.Constants;
import com.intel.bigdata.analysis.exception.ETLException;

public class BulkLoadProcessFieldSpec {
  private Properties props = null;
  private boolean buildIndex;
  private String regionQuantity = null;
  private String indexConfFileName = null;
  private String hbaseCoprocessorLocation = null;
  private boolean onlyGenerateSplitKeySpec = false;
  private boolean preCreateRegions = true;
  private String rowkeyPrefix = null;
  private String recordsNumPerRegion = null;
  private String inputSplitSize = null;
  private String extendedHBaseRowConverterClass = null;
  private String importDate = null;
  private String validatorClass = "com.intel.bigdata.analysis.dataload.exception.RecordValidator";
  private boolean createMalformedTable = false;
  private boolean nativeTaskEnabled = true;

  public boolean isBuildIndex() {
    return buildIndex;
  }

  public boolean isOnlyGenerateSplitKeySpec() {
    return onlyGenerateSplitKeySpec;
  }

  public boolean isPreCreateRegions() {
    return preCreateRegions;
  }

  public String getRegionQuantity() {
    return regionQuantity;
  }

  public void setRegionQuantity(String regionQuantity) {
    this.regionQuantity = regionQuantity;
  }

  public String getHBaseCoprocessorLocation() {
    return hbaseCoprocessorLocation;
  }

  public void setHBaseCoprocessorLocation(String hbaseCoprocessorLocation) {
    this.hbaseCoprocessorLocation = hbaseCoprocessorLocation;
  }

  public String getIndexConfFileName() {
    return indexConfFileName;
  }

  public void setIndexConfFileName(String indexConfFileName) {
    this.indexConfFileName = indexConfFileName;
  }

  public String getRowkeyPrefix() {
    return rowkeyPrefix;
  }

  public void setRowkeyPrefix(String rowkeyPrefix) {
    this.rowkeyPrefix = rowkeyPrefix;
  }

  public String getRecordsNumPerRegion() {
    return recordsNumPerRegion;
  }

  public void setRecordsNumPerRegion(String recordsNumPerRegion) {
    this.recordsNumPerRegion = recordsNumPerRegion;
  }

  public String getInputSplitSize() {
    return inputSplitSize;
  }

  public void setInputSplitSize(String inputSplitSize) {
    this.inputSplitSize = inputSplitSize;
  }

  public String getExtendedHBaseRowConverterClass() {
    return extendedHBaseRowConverterClass;
  }

  public void setExtendedHBaseRowConverterClass(
      String extendedHBaseRowConverterClass) {
    this.extendedHBaseRowConverterClass = extendedHBaseRowConverterClass;
  }

  public String getImportDate() {
    return importDate;
  }

  public void setImportDate(String importDate) {
    this.importDate = importDate;
  }

  public String getValidatorClass() {
    return validatorClass;
  }

  public void setValidatorClass(String validatorClass) {
    this.validatorClass = validatorClass;
  }

  public boolean isCreateMalformedTable() {
    return createMalformedTable;
  }

  public void setcreateMalformedTable(boolean createMalformedTable) {
    this.createMalformedTable = createMalformedTable;
  }

  public boolean isNativeTaskEnabled() {
    return nativeTaskEnabled;
  }

  public void setNativeTaskEnabled(boolean nativeTaskEnabled) {
    this.nativeTaskEnabled = nativeTaskEnabled;
  }
  
  public BulkLoadProcessFieldSpec(Properties props){
	  this.props = props;
	  readAndCheckBulkLoadProcessFieldSpecs();
  }

  private void readAndCheckBulkLoadProcessFieldSpecs() {
    buildIndex = (!props.containsKey(Constants.BUILD_INDEX) ? Constants.DEFAULT_BUILD_INDEX
        : Boolean.parseBoolean(props.getProperty(Constants.BUILD_INDEX)));
    
    regionQuantity = props.getProperty(Constants.REGION_QUANTITY);
    
    indexConfFileName = props.getProperty(Constants.INDEX_CONF_FILE_NAME);
    
    hbaseCoprocessorLocation = props.getProperty(Constants.HBASE_COPROCESSOR_LOCATION);

    onlyGenerateSplitKeySpec = (!props
        .containsKey(Constants.ONLY_GENERATE_SPLITKEYSPEC) ? Constants.DEFAULT_ONLY_GENERATE_SPLITKEYSPEC
        : Boolean.parseBoolean(props.getProperty(Constants.ONLY_GENERATE_SPLITKEYSPEC)));
    
    preCreateRegions = (!props.containsKey(Constants.PRE_CREATE_REGIONS) ? Constants.DEFAULT_PRE_CREATE_REGIONS
        : Boolean.parseBoolean(props.getProperty(Constants.PRE_CREATE_REGIONS)));
    
    rowkeyPrefix = props.getProperty(Constants.ROWKEY_PREFIX);
    
    recordsNumPerRegion = props.getProperty(Constants.RECORDS_NUM_PER_REGION);
    
    inputSplitSize = props.getProperty(Constants.INPUT_SPLIT_SIZE);

    extendedHBaseRowConverterClass = props.getProperty(Constants.EXTENDEDHBASEROWCONVERTER_CLASS_KEY);

    importDate = props.getProperty(Constants.IMPORT_DATE);
    
    validatorClass = props.getProperty(Constants.VALIDATOR_CLASS);
    
    createMalformedTable = (!props
        .containsKey(Constants.CREATE_MALFORMED_TABLE) ? Constants.DEFAULT_CREATE_MALFORMED_TABLE
        : Boolean.parseBoolean(props.getProperty(Constants.CREATE_MALFORMED_TABLE)));
    
    nativeTaskEnabled = (!props.containsKey(Constants.NATIVETASK_ENABLED) ? Constants.DEFAULT_NATIVETASK_ENABLED
        : Boolean.parseBoolean(props.getProperty(Constants.NATIVETASK_ENABLED)));
    
    if(buildIndex){
    	if(Util.checkIsEmpty(regionQuantity)
    			||Util.checkIsEmpty(indexConfFileName)){
        ETLException.handle("Need to specify value for "
            + Constants.REGION_QUANTITY + " and "
            + Constants.INDEX_CONF_FILE_NAME + " when buildIndex is true.");
    	}
    }
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("buildIndex:").append(buildIndex).append("\n");
    sb.append("regionQuantity:").append(regionQuantity).append("\n");
    sb.append("indexConfFileName:").append(indexConfFileName).append("\n");
    sb.append("hbaseCoprocessorLocation:").append(hbaseCoprocessorLocation)
        .append("\n");

    sb.append("onlyGenerateSplitKeySpec:").append(preCreateRegions)
        .append("\n");
    sb.append("preCreateRegions:").append(preCreateRegions).append("\n");
    sb.append("rowkeyPrefix:").append(rowkeyPrefix).append("\n");
    sb.append("recordsNumPerRegion:").append(recordsNumPerRegion).append("\n");
    sb.append("extendedHBaseRowConverterClass:")
        .append(extendedHBaseRowConverterClass).append("\n");
    sb.append("inputSplitSize:").append(inputSplitSize).append("\n");
    sb.append("importDate:").append(importDate).append("\n");
    sb.append("validatorClass:").append(validatorClass).append("\n");
    sb.append("createMalformedTable:").append(createMalformedTable)
        .append("\n");
    sb.append("nativeTaskEnabled:").append(nativeTaskEnabled);

    return sb.toString();
  }
}
