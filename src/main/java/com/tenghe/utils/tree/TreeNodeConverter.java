package com.tenghe.utils.tree;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * author: teng.he
 * time: 17:28 2018/5/3
 * desc: 树节点转换器
 */
public class TreeNodeConverter {
  private TreeNodeGenerateStrategy treeNodeGenerateStrategy;

  private TreeNodeConverter(TreeNodeGenerateStrategy treeNodeGenerateStrategy) {
    this.treeNodeGenerateStrategy = treeNodeGenerateStrategy;
  }

  public static TreeNodeConverter buildTreeNodeConverter(TreeNodeGenerateStrategy treeNodeGenerateStrategy) {
    return new TreeNodeConverter(treeNodeGenerateStrategy);
  }

  public <T> List<TreeUtil.TreeNode> convert(List<T> list) {
    List<TreeUtil.TreeNode> treeNodes = Lists.newArrayList();
    if (list == null || list.isEmpty()) {
      return treeNodes;
    }
    for (T t : list) {
      treeNodes.add(treeNodeGenerateStrategy.generateTreeNode(t));
    }
    return treeNodes;
  }
}
