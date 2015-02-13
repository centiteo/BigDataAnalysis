package com.intel.bigdata.analysis.dataload.extract;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.cloudera.bigdata.analysis.util.Util;
import com.intel.bigdata.analysis.dataload.Constants;
import com.intel.bigdata.analysis.dataload.exception.ETLException;
import com.intel.bigdata.analysis.dataload.util.CommonUtils;

public class HBaseTargetFieldSpec {
	private Properties props;
	private String hbaseGeneratedHfilesOutputPath;
	private String hbaseTargetTableName;
	private boolean hbaseTargetWriteToWAL = false;
	private String hbaseTargetTableSplitKeySpec;
	private String hbaseTargetTableCellSpec;
	private Map<String, String> targetTableCellMap;

	public String getHbaseGeneratedHfilesOutputPath() {
		return hbaseGeneratedHfilesOutputPath;
	}

	public String getHbaseTargetTableName() {
		return hbaseTargetTableName;
	}

	public boolean isHbaseTargetWriteToWAL() {
		return hbaseTargetWriteToWAL;
	}

	public String getHbaseTargetTableSplitKeySpec() {
		return hbaseTargetTableSplitKeySpec;
	}

	public String getHbaseTargetTableCellSpec() {
		return hbaseTargetTableCellSpec;
	}

	public Map<String, String> getTargetTableCellMap() {
		return targetTableCellMap;
	}

	public HBaseTargetFieldSpec(Properties props) {
		this.props = props;
		readAndCheckHBaseTargetFieldSpecs();
	}

	private void readAndCheckHBaseTargetFieldSpecs() {
		this.targetTableCellMap = new HashMap<String, String>();

		hbaseGeneratedHfilesOutputPath = props
				.getProperty(Constants.HBASE_GENERATED_HFILES_OUTPUT_PATH);

		hbaseTargetTableName = props
				.getProperty(Constants.HBASE_TARGET_TABLE_NAME);

		hbaseTargetWriteToWAL = (!props
				.containsKey(Constants.HBASE_TARGET_WRITE_TO_WAL_FLAG) ? Constants.DEFALUT_HBASE_TARGET_WRITE_TO_WAL_FLAG
				: Boolean.parseBoolean(props
						.getProperty(Constants.HBASE_TARGET_WRITE_TO_WAL_FLAG)));

		hbaseTargetTableSplitKeySpec = props
				.getProperty(Constants.HBASE_TARGET_TABLE_SPLIT_KEY_SPEC);

		hbaseTargetTableCellSpec = props
				.getProperty(Constants.HBASE_TARGET_TABLE_CELL_SPEC);

		if (Util.checkIsEmpty(hbaseGeneratedHfilesOutputPath)) {
			ETLException.handle("Can't find properties "
					+ Constants.HBASE_GENERATED_HFILES_OUTPUT_PATH);
		}
		
		if(Util.checkIsEmpty(hbaseTargetTableName)){
			ETLException.handle("Can't find properties "
					+ Constants.HBASE_TARGET_TABLE_NAME);
		}

		String[] targetTableCellKeys = hbaseTargetTableCellSpec.trim().split(
				Constants.CELL_SPLIT_CHARACTER);

		// if it uses CustomizedHBaseRowConverter
		for (String targetTableCellKey : targetTableCellKeys) {
			if (!props.containsKey(targetTableCellKey)) {
				ETLException
						.handle("Can't find properties for target table cell key \""
								+ targetTableCellKey + "\"");
			}

			String targetTableCellValue = props.getProperty(targetTableCellKey)
					.trim();
			if (targetTableCellValue.isEmpty()) {
				ETLException.handle("Empty value for target table cell key \""
						+ targetTableCellKey + "\"");
			}
			targetTableCellMap.put(targetTableCellKey, targetTableCellValue);
		}

		if (!targetTableCellMap.containsKey(Constants.ROW_KEY_NAME)) {
			ETLException
					.handle("Can't find 'rowkey' property in target table cell key");
		}
		if (targetTableCellMap.size() != targetTableCellKeys.length) {
			ETLException
					.handle("Mis-matched target table cell spec and cell mapping!");
		}

	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("hbaseGeneratedHfilesOutputPath:")
				.append(hbaseGeneratedHfilesOutputPath).append("\n");
		sb.append("hbaseTargetTableName:").append(hbaseTargetTableName)
				.append("\n");
		sb.append("hbaseTargetWriteToWAL:").append(hbaseTargetWriteToWAL)
				.append("\n");
		sb.append("hbaseTargetTableSplitKeySpec:")
				.append(hbaseTargetTableSplitKeySpec).append("\n");
		sb.append("hbaseTargetTableCellSpec:").append(hbaseTargetTableCellSpec)
				.append("\n");
		sb.append("targetTableCellMap:").append(targetTableCellMap)
				.append("\n");
		sb.append(
				"target table cell getStringFromMap:"
						+ "\n"
						+ CommonUtils.getStringFromMap(targetTableCellMap, "|",
								"=")).append("\n");
		sb.append("target table cell getMapFromString:"
				+ "\n"
				+ CommonUtils.getMapFromString(CommonUtils.getStringFromMap(
						targetTableCellMap, "|", "="), "|", "="));

		return sb.toString();
	}

	public static void main(String[] args) throws Exception {
		Properties props = new Properties();
		props.load(new FileInputStream("etl-hbase2hbase-conf.properties"));
		HBaseTargetFieldSpec htfs = new HBaseTargetFieldSpec(props);
		System.out.println(htfs.toString());
	}
}
