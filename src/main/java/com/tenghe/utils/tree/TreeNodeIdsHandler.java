package com.tenghe.utils.tree;

import com.google.common.collect.Lists;

import com.tenghe.utils.BaseEntity;

import java.util.List;

/**
 * author: teng.he
 * time: 18:35 2018/6/25
 * desc:
 */
public interface TreeNodeIdsHandler<T extends BaseEntity> {

  //获取待处理根节点列表
  List<String> getRootNodeIds();

  //处理原始的根节点列表
  List<String> getRootNodeIdsForQueryTrees(List<String> rootNodeIds);

  List<T> getChildrenTree(String nodeId);

  TreeNodeGenerateStrategy<T> getTreeNodeGenerateStrategy();

  default TreeNodeIdsHandlerResult handle() {
    TreeNodeIdsHandlerResult treeNodeIdsHandlerResult = new TreeNodeIdsHandlerResult();
    List<String> rootNodeIds = getRootNodeIdsForQueryTrees(getRootNodeIds());
    treeNodeIdsHandlerResult.setRootNodeIds(rootNodeIds);
    treeNodeIdsHandlerResult.setAllTreeNodes(getTreeNodes(rootNodeIds));
    return treeNodeIdsHandlerResult;
  }

  default List<TreeUtil.TreeNode> getTreeNodes(List<String> rootNodeIds){
    List<T> entities = Lists.newArrayList();
    for (String nodeId : rootNodeIds) {
      entities.addAll(getChildrenTree(nodeId));
    }

    return TreeNodeConverter.buildTreeNodeConverter(getTreeNodeGenerateStrategy()).convert(entities);
  }
}
