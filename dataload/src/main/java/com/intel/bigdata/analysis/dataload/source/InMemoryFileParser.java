package com.intel.bigdata.analysis.dataload.source;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBElement;

import org.apache.hadoop.conf.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.bigdata.analysis.dataload.Constants;
import com.intel.bigdata.analysis.dataload.exception.ParseException;
import com.intel.bigdata.analysis.dataload.io.FileObject;
import com.intel.bigdata.analysis.dataload.jaxb.SchemaUnmarshaller;
import com.intel.bigdata.analysis.dataload.util.ByteArray;
import com.intel.bigdata.analysis.dataload.util.RandomByteIterator;
import com.intel.bigdata.analysis.generated.FieldPattern;
import com.intel.bigdata.analysis.generated.InMemoryColumnFamilyType;
import com.intel.bigdata.analysis.generated.InMemoryQualifierType;
import com.intel.bigdata.analysis.generated.InMemoryRecordType;
import com.intel.bigdata.analysis.generated.InMemoryRowKeyFieldType;
import com.intel.bigdata.analysis.generated.MultiInMemoryQualifierType;

public class InMemoryFileParser extends FileParser {
  private static final Logger LOG = LoggerFactory
      .getLogger(InMemoryFileParser.class);

  private Configuration conf;
  private InMemoryRecordType recordType;
  private List<InMemoryRowKeyFieldType> rowKeyFieldTypes;
  private String rowKeySeparator;

  private HTableDefinition cachedDefinition;
  private Map<String, HTableDefinition> definitionMap;
  private List<InMemoryColumnFamilyType> cfSpecList;

  private long recordsPerFile;
  private long counter = 0L;

  public InMemoryFileParser() {
    definitionMap = new HashMap<String, HTableDefinition>();
  }

  @Override
  public void init(InputStream is, FileObject file, Configuration configuration)
      throws IOException {
    this.conf = configuration;
    this.counter = 0L;
    String instanceDoc = null;
    if (Constants.DATALOAD_CLIENT_MODE.equalsIgnoreCase(conf
        .get(Constants.DATALOAD_MODE))) {
      instanceDoc = conf.get(Constants.PROPERTY_FOLDER_KEY) 
          + "/" 
          + conf.get(Constants.INSTANCE_DOC_PATH_KEY);
    }else{
      instanceDoc = conf.get(Constants.INSTANCE_DOC_PATH_KEY);
    }
    LOG.info("instanceDocPath: " + instanceDoc);
    if(instanceDoc==null){
      throw new IOException("Cannot find instanceDoc");
    }
    recordType = ((JAXBElement<InMemoryRecordType>) SchemaUnmarshaller
        .getInstance().unmarshallDocument(InMemoryRecordType.class,
            instanceDoc)).getValue();
    cfSpecList = recordType.getColumnFamilySpec();
    String tableName = conf.get(Constants.HBASE_TARGET_TABLE_NAME);
    cachedDefinition = definitionMap.get(tableName);
    if (cachedDefinition == null) {
      String splitPrefix = conf.get(Constants.SPLIT_KEY_PREFIXES, "");
      int splitSize = conf.getInt(Constants.SPLIT_SIZE_KEY, 1);
      cachedDefinition = new HTableDefinitionImpl(tableName, cfSpecList,
          splitPrefix, splitSize);
    }

    rowKeyFieldTypes = recordType.getRowKeySpec().getRowKeyFieldSpec();
    rowKeySeparator = recordType.getRowKeySpec().getRowKeySeparator();

    recordsPerFile = conf.getLong(Constants.RECORD_NUM_PER_FILE_KEY,
        Constants.DEFAULT_RECORD_NUM_PER_FILE);
  }

  @Override
  public Record getNext() throws IOException, ParseException {
    if (counter < recordsPerFile) {
      InnerRecord innerRecord = new InnerRecord(cachedDefinition, getRowKey(),
          getValueMap(), conf.getBoolean(Constants.WRITE_TO_WAL_KEY, false));
      counter++;
      return innerRecord;
    } else {
      return null;
    }
  }

  @Override
  public void close() throws IOException {
    // TODO Auto-generated method stub

  }

  public byte[] getRowKey() {
    ByteArray byteArray = new ByteArray();
    byte[] separatorBytes = rowKeySeparator.getBytes();
    for (InMemoryRowKeyFieldType rowKeyFieldType : rowKeyFieldTypes) {
      byte[] fieldBytes = generateBytes(rowKeyFieldType.getLength(),
          rowKeyFieldType.getFieldPattern());
      byteArray.add(fieldBytes);
      byteArray.add(separatorBytes);
    }
    return byteArray.toBytes();
  }

  public Map<byte[], Map<byte[], byte[]>> getValueMap() {
    Map<byte[], Map<byte[], byte[]>> valueMap = new HashMap<byte[], Map<byte[], byte[]>>();
    for (InMemoryColumnFamilyType cfType : cfSpecList) {
      Map<byte[], byte[]> qualifierMap = new HashMap<byte[], byte[]>();

      for (MultiInMemoryQualifierType multiInMemoryQualifierType : cfType
          .getMultiQualifierSpec()) {
        boolean useConstant = multiInMemoryQualifierType.isUseConstant();
        byte[] constantValue = null;
        if (useConstant) {
          constantValue = multiInMemoryQualifierType.getConstantValue()
              .getBytes();
        }
        for (int i = 0; i < multiInMemoryQualifierType.getQualifierNum(); i++) {
          byte[] qualValue = useConstant ? constantValue : generateBytes(
              multiInMemoryQualifierType.getLength(),
              multiInMemoryQualifierType.getFieldPattern());
          qualifierMap.put(
              new String(multiInMemoryQualifierType.getQualifierPrefix() + i)
                  .getBytes(), qualValue);
        }
      }

      for (InMemoryQualifierType qualifierType : cfType.getQualifierSpec()) {
        byte[] qualifierValue = generateBytes(qualifierType.getLength(),
            qualifierType.getFieldPattern());
        qualifierMap.put(qualifierType.getQualifierName().getBytes(),
            qualifierValue);
      }
      valueMap.put(cfType.getFamilyName().getBytes(), qualifierMap);
    }

    return valueMap;
  }

  private byte[] generateBytes(int length, FieldPattern pattern) {
    RandomByteIterator iter = new RandomByteIterator(length, pattern);
    return iter.toArray();
  }

}
