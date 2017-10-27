package com.tenghe.utils.model;

/**
 * author: teng.he
 * time: 10:54 2017/9/1
 * desc: 一条数据记录的结果
 */
public class DataCheckResult {
  private boolean isValid;
  private String errMsg;

  public boolean isValid() {
    return isValid;
  }

  public void setValid(boolean valid) {
    isValid = valid;
  }

  public String getErrMsg() {
    return errMsg;
  }

  public void setErrMsg(String errMsg) {
    this.errMsg = errMsg;
  }

  @Override
  public String toString() {
    return "DataCheckResult{" +
        "isValid=" + isValid +
        ", errMsg='" + errMsg + '\'' +
        '}' ;
  }
}
