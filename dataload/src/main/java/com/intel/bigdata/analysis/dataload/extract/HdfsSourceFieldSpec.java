package com.intel.bigdata.analysis.dataload.extract;

import java.io.FileInputStream;
import java.util.Properties;

import com.cloudera.bigdata.analysis.util.Util;
import com.intel.bigdata.analysis.dataload.Constants;
import com.intel.bigdata.analysis.dataload.exception.ETLException;

public class HdfsSourceFieldSpec {
	private Properties props = null;
	private String idpHBaseMasterIpaddress = null;
	private String hdfsSourceFileInputPath;
	private String hdfsSourceFileEncoding;
	private String hdfsSourceFileRecordFieldsDelimiter;
	private String hdfsSourceFileRecordFieldsNumber;
	private String hdfsSourceFileRecordFieldTypeInt;

	public String getIdpHBaseMasterIpaddress() {
		return idpHBaseMasterIpaddress;
	}

	public String getHdfsSourceFileInputPath() {
		return hdfsSourceFileInputPath;
	}

	public String getHdfsSourceFileEncoding() {
		return hdfsSourceFileEncoding;
	}

	public String getHdfsSourceFileRecordFieldsDelimiter() {
		return hdfsSourceFileRecordFieldsDelimiter;
	}

	public String getHdfsSourceFileRecordFieldsNumber() {
		return hdfsSourceFileRecordFieldsNumber;
	}

	public String getHdfsSourceFileRecordFieldTypeInt() {
		return hdfsSourceFileRecordFieldTypeInt;
	}

	public HdfsSourceFieldSpec(Properties props) {
		this.props = props;
		readAndCheckHdfsSourceFieldSpec();
	}

	private void readAndCheckHdfsSourceFieldSpec() {
		idpHBaseMasterIpaddress = props.getProperty(Constants.IDP_HBASE_MASTER_IPADDRESS);
		
		hdfsSourceFileInputPath = props.getProperty(Constants.HDFS_SOURCE_FILE_INPUT_PATH);
		
		hdfsSourceFileEncoding = (!props
				.containsKey(Constants.HDFS_SOURCE_FILE_ENCODING) ? Constants.DEFAULT_HDFS_SOURCE_FILE_ENCODING
				: props.getProperty(Constants.HDFS_SOURCE_FILE_ENCODING));
		
		hdfsSourceFileRecordFieldsDelimiter = props.getProperty(Constants.HDFS_SOURCE_FILE_RECORD_FIELDS_DELIMITER);
		
		hdfsSourceFileRecordFieldsNumber = (!props
				.containsKey(Constants.HDFS_SOURCE_FILE_RECORD_FIELDS_NUMBER) ? hdfsSourceFileRecordFieldsNumber
				: props.getProperty(Constants.HDFS_SOURCE_FILE_RECORD_FIELDS_NUMBER));
		
		hdfsSourceFileRecordFieldTypeInt = props.getProperty(Constants.HDFS_SOURCE_FILE_RECORD_FIELDS_TYPE_INT);
		
		if(Util.checkIsEmpty(hdfsSourceFileInputPath)){
			ETLException.handle("Can't find correct value for \""
					+ Constants.HDFS_SOURCE_FILE_INPUT_PATH + "\"");
		}
		
		if(Util.checkIsEmpty(hdfsSourceFileRecordFieldsDelimiter)){
			ETLException.handle("Can't find correct value for \""
					+ Constants.HDFS_SOURCE_FILE_RECORD_FIELDS_DELIMITER);
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("idpHBaseMasterIpaddress:").append(idpHBaseMasterIpaddress)
				.append("\n");
		sb.append("hdfsSourceFileInputPath:").append(hdfsSourceFileInputPath)
				.append("\n");
		sb.append("hdfsSourceFileEncoding:").append(hdfsSourceFileEncoding)
				.append("\n");
		sb.append("hdfsSourceFileRecordFieldsDelimiter:")
				.append(hdfsSourceFileRecordFieldsDelimiter).append("\n");
		sb.append("hdfsSourceFileRecordFieldsNumber:")
				.append(hdfsSourceFileRecordFieldsNumber).append("\n");
		sb.append("hdfsSourceFileRecordFieldTypeInt:").append(
				hdfsSourceFileRecordFieldTypeInt);

		return sb.toString();
	}

	public static void main(String[] args) throws Exception {
		Properties props = new Properties();
		props.load(new FileInputStream("etl-hdfs2hbase-conf.properties"));
		HdfsSourceFieldSpec hsfs = new HdfsSourceFieldSpec(props);
		System.out.println(hsfs.toString());
	}
}
