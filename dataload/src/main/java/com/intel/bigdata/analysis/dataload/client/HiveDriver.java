package com.intel.bigdata.analysis.dataload.client;

import com.intel.bigdata.analysis.dataload.hive.HiveCommandExecutor;

public class HiveDriver implements Worker {
  private String configFile;

  public HiveDriver(String configFile) {
    this.configFile = configFile;
  }

  @Override
  public void execute() throws Exception {
    HiveCommandExecutor hce = new HiveCommandExecutor(configFile);
    hce.executeHiveCommands();
  }
}
