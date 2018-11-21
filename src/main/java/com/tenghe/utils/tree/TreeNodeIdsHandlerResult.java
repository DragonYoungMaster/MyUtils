package com.tenghe.utils.tree;

import java.util.List;

/**
 * author: teng.he
 * time: 18:36 2018/6/25
 * desc:
 */
public class TreeNodeIdsHandlerResult {
  private List<String> rootNodeIds;
  private List<TreeUtil.TreeNode> allTreeNodes;

  public List<String> getRootNodeIds() {
    return rootNodeIds;
  }

  public void setRootNodeIds(List<String> rootNodeIds) {
    this.rootNodeIds = rootNodeIds;
  }

  public List<TreeUtil.TreeNode> getAllTreeNodes() {
    return allTreeNodes;
  }

  public void setAllTreeNodes(List<TreeUtil.TreeNode> allTreeNodes) {
    this.allTreeNodes = allTreeNodes;
  }
}
