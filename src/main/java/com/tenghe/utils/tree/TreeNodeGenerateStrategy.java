package com.tenghe.utils.tree;

/**
 * author: teng.he
 * time: 17:38 2018/5/3
 * desc:
 */
public interface TreeNodeGenerateStrategy<T> {
  TreeUtil.TreeNode generateTreeNode(T t);
}
