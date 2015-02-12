package com.intel.bigdata.analysis.custom.datagen;

import com.intel.bigdata.analysis.datagen.CustomFieldGenerator;
import com.intel.bigdata.analysis.datagen.RecordGenerator;
import com.intel.bigdata.analysis.generated.ColumnSpec;

public class ServiceTypeGenerator extends CustomFieldGenerator {

  public ServiceTypeGenerator(RecordGenerator recordGenerator, ColumnSpec columnSpec){
    super(recordGenerator, columnSpec);
  }
  
  @Override
  public String generate() {
    return "DummyServiceType";
  }

}
