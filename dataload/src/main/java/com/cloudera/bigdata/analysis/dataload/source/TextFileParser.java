package com.cloudera.bigdata.analysis.dataload.source;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBElement;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.generated.master.snapshot_jsp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cloudera.bigdata.analysis.dataload.Constants;
import com.cloudera.bigdata.analysis.dataload.exception.ETLException;
import com.cloudera.bigdata.analysis.dataload.exception.ParseException;
import com.cloudera.bigdata.analysis.dataload.io.FileObject;
import com.cloudera.bigdata.analysis.dataload.jaxb.SchemaUnmarshaller;
import com.cloudera.bigdata.analysis.dataload.util.CommonUtils;
import com.cloudera.bigdata.analysis.generated.ColumnFamilyType;
import com.cloudera.bigdata.analysis.generated.FieldDefinition;
import com.cloudera.bigdata.analysis.generated.FieldsDefinition;
import com.cloudera.bigdata.analysis.generated.QualifierType;
import com.cloudera.bigdata.analysis.generated.RowKeyFieldType;
import com.cloudera.bigdata.analysis.generated.TxtRecordType;

/**
 * TextFileParser is used to parse the structured text files. Currently, it
 * assumes each line in the file is separated by the default "\n". There would
 * be two typical parsing style for the text file. One style is: each field is
 * represented by the fixed length; another style is: all the fields are
 * separated by a fixed separator. TextFileParser is not thread-safe.
 */
public class TextFileParser extends FileParser {
  private static final Logger LOG = LoggerFactory
      .getLogger(TextFileParser.class);

  private Configuration conf;

  private TxtRecordType recordType;
  private String rowKeyDelimiter;
  private List<RowKeyFieldType> fieldSpecList;
  private List<ColumnFamilyType> cfSpecList;

  private String cachedLine;
  private String[] fieldValues;
  private BufferedReader reader;
  private int readerBufSize = 4096;
  private boolean useHashUUIDForRowkey = false;
  private int regionQuantity = 1;;

  private HashMap<String, HTableDefinition> definitionMap;
  private HTableDefinition cachedDefinition;

  public TextFileParser() {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Creating the parser...");
    }
    definitionMap = new HashMap<String, HTableDefinition>();
  }

  /**
   * Invoked before parsing file
   */
  @Override
  public void init(InputStream is, FileObject file, Configuration configuration)
      throws IOException {
    this.conf = configuration;
    readerBufSize = conf.getInt(Constants.PARSER_READER_BUF_SIZE_KEY,
        Constants.DEFAULT_PARSER_READER_BUF_SIZE);

    // TODO: add extra char set encoding
    reader = new BufferedReader(new InputStreamReader(is), readerBufSize);
    String instanceDoc = null;
    if (Constants.DATALOAD_CLIENT_MODE.equalsIgnoreCase(conf
        .get(Constants.DATALOAD_MODE))) {
      instanceDoc = conf.get(Constants.PROPERTY_FOLDER_KEY) + "/"
          + conf.get(Constants.INSTANCE_DOC_PATH_KEY);
    } else {
      instanceDoc = conf.get(Constants.INSTANCE_DOC_PATH_KEY);
    }
    LOG.info("instanceDocPath: " + instanceDoc);
    if (instanceDoc == null) {
      throw new IOException("Cannot find instanceDoc");
    }
    recordType = ((JAXBElement<TxtRecordType>) SchemaUnmarshaller.getInstance()
        .unmarshallDocument(TxtRecordType.class, instanceDoc)).getValue();

    rowKeyDelimiter = recordType.getRowKeySpec().getRowKeyDelimiter();
    fieldSpecList = recordType.getRowKeySpec().getRowKeyFieldSpec();

    cfSpecList = recordType.getColumnFamilySpec();

    useHashUUIDForRowkey =
        conf.getBoolean(Constants.DATALOAD_HASH_UUID_ROWKEY, false);

    regionQuantity =
        conf.getInt(Constants.DATALOAD_TABLE_REGION_QUANTITY,
            Constants.DEFAULT_DATALOAD_TABLE_REGION_QUANTITY);
    if (regionQuantity <= 0) {
      ETLException.handle(Constants.DATALOAD_TABLE_REGION_QUANTITY
          + " must have the positive value.");
    }

    if (definitionMap == null) {
      definitionMap = new HashMap<String, HTableDefinition>();
    }

    String tableName = conf.get(Constants.HBASE_TARGET_TABLE_NAME);

    cachedDefinition = definitionMap.get(tableName);
    if (cachedDefinition == null) {
      String splitPrefix = conf.get(Constants.SPLIT_KEY_PREFIXES, "");
      int splitSize = conf.getInt(Constants.SPLIT_SIZE_KEY, 1);
      cachedDefinition =
          new HTableDefinitionImpl(tableName, cfSpecList,
            splitPrefix, splitSize, regionQuantity);
      definitionMap.put(tableName, cachedDefinition);
    }
  }

  @Override
  public Record getNext() throws IOException, ParseException {
    cachedLine = reader.readLine();
    Record record = null;
    if (!StringUtils.isEmpty(cachedLine)) {
      if (recordType.isUseDelimiter()) {
        String delimiter = recordType.getInputDelimiter();
        fieldValues = StringUtils.splitByWholeSeparatorPreserveAllTokens(
            cachedLine, delimiter);
        if (LOG.isDebugEnabled()) {
          LOG.debug("cachedLine : " + cachedLine);
          LOG.debug("split delimiter : " + delimiter);
          LOG.debug("fieldValues size : " + fieldValues.length);
          LOG.debug("fieldValues : " + Arrays.asList(fieldValues));
        }
      }

      if (useHashUUIDForRowkey) {
        record = new InnerRecord(cachedDefinition, CommonUtils.genRowKey(),
            getValueMap(), conf.getBoolean(Constants.WRITE_TO_WAL_KEY, false));
      } else {
        record = new InnerRecord(cachedDefinition, getRowKey(), getValueMap(),
            conf.getBoolean(Constants.WRITE_TO_WAL_KEY, false));
      }

      return record;
    }

    return null;
  }

  public byte[] getRowKey() {
    StringBuffer sb = new StringBuffer();
    for (RowKeyFieldType fieldSpec : fieldSpecList) {
      String fieldValue = null;
      if (recordType.isUseDelimiter()) {
        fieldValue = fieldValues[fieldSpec.getFieldIndex()];
      } else {
        int startPos = fieldSpec.getStartPos();
        int length = fieldSpec.getLength();
        fieldValue = cachedLine.substring(startPos, startPos + length);
      }
      sb.append(fieldValue);
      sb.append(rowKeyDelimiter);
    }

    return sb.toString().getBytes();
  }

  public Map<byte[], Map<byte[], byte[]>> getValueMap() {
    HashMap<byte[], Map<byte[], byte[]>> cfMap = new HashMap<byte[], Map<byte[], byte[]>>();
    for (ColumnFamilyType cfType : cfSpecList) {
      cfType.getFamilyName();
      HashMap<byte[], byte[]> qualifierMap = new HashMap<byte[], byte[]>();
      for (QualifierType qType : cfType.getQualifierSpec()) {
        String qValue = null;
        FieldsDefinition fields = qType.getFields();
        List<FieldDefinition> fieldList = fields.getField();
        StringBuilder sb = new StringBuilder();
        if (recordType.isUseDelimiter()) {
          for (FieldDefinition definition : fieldList) {
            sb.append(fieldValues[definition.getFieldIndex()]);
          }
        } else {
          for (FieldDefinition definition : fieldList) {
            int startPos = definition.getStartPos();
            int length = definition.getLength();
            sb.append(cachedLine.substring(startPos, startPos + length));
          }
        }
        qValue = sb.toString();
        if (LOG.isDebugEnabled()) {
          LOG.debug("qValue : fieldValues[" + qType.getFieldIndex() + "] = "
              + qValue);
        }
        qualifierMap
            .put(qType.getQualifierName().getBytes(), qValue.getBytes());
      }
      // for (MultiQualifierType qType : cfType.getMultiQualifierSpec()) {
      // StringBuffer multiQualifyValue = new StringBuffer();
      // if (recordType.isUseSeparater()) {
      // String[] indexs = StringUtils.splitByWholeSeparator(
      // qType.getFieldIndex(), ",");
      // for (int i = 0; i < indexs.length - 1; i++) {
      // multiQualifyValue.append(fieldValues[Integer.parseInt(indexs[i])]);
      // multiQualifyValue.append("|");
      // }
      // multiQualifyValue.append(fieldValues[Integer
      // .parseInt(indexs[indexs.length - 1])]);
      // if (LOG.isDebugEnabled()) {
      // LOG.debug("multiple qValue : " + multiQualifyValue);
      // }
      // } else {
      // // TODO
      // String startPos = qType.getStartPos();
      // String length = qType.getLength();
      // // TODO
      // // qValue = cachedLine.substring(startPos, startPos + length);
      // }
      // qualifierMap.put(qType.getQualifierName().getBytes(), multiQualifyValue
      // .toString().getBytes());
      // }
      cfMap.put(cfType.getFamilyName().getBytes(), qualifierMap);
    }
    return cfMap;
  }

  @Override
  public void close() throws IOException {
    // TODO Auto-generated method stub
  }
}
