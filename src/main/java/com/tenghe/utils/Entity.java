package com.tenghe.utils;

import java.io.Serializable;

/**
 * Created by zhengzhigang on 2018/3/24.
 */
public interface Entity extends Serializable {
  String getId();
  void setId(String id);
}
