package com.cloudera.bigdata.analysis.dataload.transform;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cloudera.bigdata.analysis.dataload.Constants;
import com.cloudera.bigdata.analysis.dataload.exception.ETLException;

public class TargetTableSpec extends HBaseTableSpec {
  ColumnSpec rowKeySpec;
  Map<String, ColumnSpec> columnMap;

  public TargetTableSpec(String tableName, String rowSpecString,
      String splitKeySpec) throws Exception {
    super(tableName, rowSpecString, splitKeySpec);
  }

  public ColumnSpec getRowKeySpec() {
    return rowKeySpec;
  }

  public Map<String, ColumnSpec> getColumnMap() {
    return columnMap;
  }

  public String toString() {
    return tableName + "-" + rowKeySpec.toString() + "-"
        + columnMap.values().toString();
  }

  @Override
  protected void initializeFieldSpecs() throws ETLException {

    String normalized = StringUtils.deleteWhitespace(rowSpecString);
    String[] columnSpecs = StringUtils.splitByWholeSeparatorPreserveAllTokens(
        normalized, Constants.COLUMN_ENTRY_SEPARATOR);
    if (columnSpecs.length <= 1)
      throw new ETLException("Invalid htable spec: No column found in "
          + rowSpecString);

    ColumnSpec column = null;
    columnMap = new HashMap<String, ColumnSpec>();

    // decode columnSpec with "|" and "="
    for (String columnSpec : columnSpecs) {
      String[] ss = StringUtils.splitByWholeSeparatorPreserveAllTokens(
          columnSpec, Constants.COLUMN_KEY_VALUE_SEPARATOR);
      if (ss.length != 2)
        throw new ETLException("Invalid htable spec: Invalid column spec "
            + columnSpec);

      column = new ColumnSpec(ss[0], ss[1]);

      System.out.println(column.toString());
      if (ss[0].equalsIgnoreCase(Constants.ROW_KEY_NAME)) {
        rowKeySpec = column;
        continue;
      }
      columnMap.put(ss[0], column);
    }

    if (rowKeySpec == null)
      throw new ETLException("No rowKey specification is found!");
  }
}
