package com.tenghe.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;
import java.util.Set;

/**
 * author: teng.he
 * time: 12:22 2018/3/27
 * desc: 构建树的工具类
 */
public class TreeUtil {
  // 在内存中保存树上的所有节点（初始状态下每个节点没有children）
  private static List<TreeNode> treeNodes;

  private TreeUtil () {

  }

  /**
   * 将树上的所有节点放入到内存（初始状态下每个节点没有children）
   * @param list
   */
  public static void putTreeNodes(List<TreeNode> list){
    cleanTreeNodes();
    treeNodes = Lists.newArrayList(list);
  }

  public static void cleanTreeNodes() {
    if (treeNodes != null && !treeNodes.isEmpty()) {
      treeNodes.clear();
    }
  }

  /**
   * 注意：调用此方法前先调用putTreeNodes方法，构建树完成之后调用cleanTreeNodes方法清空内存
   * 递归生成以nodeId为根的树
   * @param rootId
   * @return
   */
  public static TreeNode generateTree(String rootId){
    TreeNode root = getNodeById(rootId);
    List<TreeNode> childrenNodes = getChildrenNodesById(rootId);
    for (TreeNode item : childrenNodes) {
      TreeNode node = generateTree(item.getId());
      root.getChildren().add(node);
    }
    return root;
  }

  /**
   * 通过nodeId查询节点
   * @param nodeId
   * @return
   */
  public static TreeNode getNodeById(String nodeId){
    TreeNode treeNode = new TreeNode();
    for (TreeNode item : treeNodes) {
      if (item.getId().equals(nodeId)) {
        treeNode = item;
        break;
      }
    }
    return treeNode;
  }

  /**
   * 通过nodeId查询所有的子节点
   * @param nodeId
   * @return
   */
  public static List<TreeNode> getChildrenNodesById(String nodeId){
    List<TreeNode> childrenNodes = Lists.newArrayList();
    for (TreeNode item : treeNodes) {
      if(nodeId.equals(item.getParentId())) {
        childrenNodes.add(item);
      }
    }
    return childrenNodes;
  }

  public static class TreeNode implements Comparable<TreeNode> {
    private String id;
    private String name;
    private String type;
    private String parentId;
    private Set<TreeNode> children = Sets.newTreeSet();

    public TreeNode() {
    }

    public TreeNode(String id, String name, String type, String parentId) {
      this.id = id;
      this.name = name;
      this.type = type;
      this.parentId = parentId;
    }

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getType() {
      return type;
    }

    public void setType(String type) {
      this.type = type;
    }

    public String getParentId() {
      return parentId;
    }

    public void setParentId(String parentId) {
      this.parentId = parentId;
    }

    public Set<TreeNode> getChildren() {
      return children;
    }

    public void setChildren(Set<TreeNode> children) {
      this.children = children;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o)
        return true;
      if (!(o instanceof TreeNode))
        return false;

      TreeNode treeNode = (TreeNode) o;

      if (!getId().equals(treeNode.getId()))
        return false;
      return getName().equals(treeNode.getName());
    }

    @Override
    public int hashCode() {
      int result = getId().hashCode();
      result = 31 * result + getName().hashCode();
      return result;
    }

    @Override
    public String toString() {
      return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }

    @Override
    public int compareTo(TreeNode o) {
      if (o == null) {
        return 1;
      }
      return this.getName().compareTo(o.getName());
    }
  }
}
