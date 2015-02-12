package com.intel.bigdata.analysis.dataload;

import com.intel.bigdata.analysis.dataload.source.Record;

public class RecordWrapper {
  private Record record;
  private FileInfo file;

  public RecordWrapper(Record record, FileInfo file) {
    this.record = record;
    this.file = file;
  }

  public Record getRecord() {
    return record;
  }

  public FileInfo getFile() {
    return file;
  }
}
