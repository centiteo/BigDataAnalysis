package com.intel.bigdata.analysis.dataload.exception;

public interface RecordValidator {

  public void validate(String recordLine) throws FormatException;

}
