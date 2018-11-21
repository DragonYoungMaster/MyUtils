package com.tenghe.utils.tree;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import com.tenghe.utils.common.BaseConstants;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * author: teng.he
 * time: 12:22 2018/3/27
 * desc: 构建树的工具类
 */
public class TreeUtil {
  // 在内存中保存树上的所有节点（初始状态下每个节点没有children）
  private static List<TreeNode> treeNodes;

  private TreeUtil() {

  }

  /**
   * 将树上的所有节点放入到内存（初始状态下每个节点没有children）
   * @param list
   */
  private static void putTreeNodes(List<TreeNode> list){
    cleanTreeNodes();
    treeNodes = Lists.newArrayList(list);
  }

  private static void cleanTreeNodes() {
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
  private static TreeNode generateTree(String rootId){
    TreeNode root = getNodeById(rootId);
    List<TreeNode> childrenNodes = getChildrenNodesById(rootId);
    for (TreeNode item : childrenNodes) {
      TreeNode node = generateTree(item.getId());
      root.getChildren().add(node);
    }
    return root;
  }

  public static List<TreeNode> queryTreesByNodeIds(TreeNodeIdsHandler handler) {
    List<TreeNode> roots = Lists.newArrayList();
    TreeNodeIdsHandlerResult treeNodeIdsHandlerResult = handler.handle();
    List<String> rootNodeIds = treeNodeIdsHandlerResult.getRootNodeIds();
    putTreeNodes(treeNodeIdsHandlerResult.getAllTreeNodes());
    for (String nodeId : rootNodeIds) {
      TreeNode root = generateTree(nodeId);
      roots.add(root);
    }
    cleanTreeNodes();
    return roots;
  }

  public static TreeNode queryTreeByNodeId(String nodeId, TreeNodesGenerator treeNodesGenerator) {
    putTreeNodes(treeNodesGenerator.generateTreeNodes());
    TreeNode root = generateTree(nodeId);
    cleanTreeNodes();
    return root;
  }

  /**
   * 通过nodeId查询节点
   * @param nodeId
   * @return
   */
  private static TreeNode getNodeById(String nodeId){
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
  private static List<TreeNode> getChildrenNodesById(String nodeId){
    List<TreeNode> childrenNodes = Lists.newArrayList();
    for (TreeNode item : treeNodes) {
      if(nodeId.equals(item.getParentId())) {
        childrenNodes.add(item);
      }
    }
    return childrenNodes;
  }

  public static PathNode getRootPathNode(PathNode leafNode, List<TreeNode> nodes) {
    PathNode root = new PathNode(leafNode);
    TreeNode parentNode = getParentTreeNode(leafNode, nodes);
    if (parentNode == null) {
      return root;
    }

    root = new PathNode(parentNode);
    root.setNextNode(leafNode);
    return getRootPathNode(root, nodes);
  }

  public static String generatePathName(PathNode rootPathNode) {
    String path = "";
    if (rootPathNode == null) {
      return path;
    }
    path = rootPathNode.getName();

    PathNode nextPathNode = rootPathNode.getNextNode();
    while (nextPathNode != null) {
      path += BaseConstants.SLASH + nextPathNode.getName();
      nextPathNode = nextPathNode.getNextNode();
    }
    return path;
  }

  private static TreeNode getParentTreeNode(Node node, List<TreeNode> nodes) {
    TreeNode parentNode = null;
    if (node.getParentId() == null) {
      return null;
    }
    for (TreeNode item : nodes) {
      if (node.getParentId().equals(item.getId())) {
        parentNode = item;
        break;
      }
    }
    return parentNode;
  }

  /**
   * 树节点
   */
  public static class TreeNode extends Node {

    private Set<TreeNode> children = Sets.newLinkedHashSet();

    public TreeNode() {
      super();
    }

    public TreeNode(String id, String name, String type, String parentId, Integer sort) {
      super(id, name, type, parentId, sort);
    }

    public TreeNode(String id, String name, String type, String parentId, Integer sort, Map<String, Object> ext) {
      super(id, name, type, parentId, sort, ext);
    }

    public Set<TreeNode> getChildren() {
      return children;
    }

    public void setChildren(Set<TreeNode> children) {
      this.children = children;
    }
  }

  /**
   * 路径节点
   */
  public static class PathNode extends Node {
    private PathNode nextNode;//当前路径节点中的下一个节点

    public PathNode() {
      super();
    }

    public PathNode(String id, String name, String type, String parentId, Integer sort) {
      super(id, name, type, parentId, sort);
    }

    public PathNode(Node node) {
      super(node.getId(), node.getName(), node.getType(), node.getParentId(), node.getSort(),
          node.getExt());
    }

    public PathNode(PathNode pathNode) {
      super(pathNode.getId(), pathNode.getName(), pathNode.getType(), pathNode.getParentId(),
          pathNode.getSort(),pathNode.getExt());
      this.setNextNode(pathNode.getNextNode());
    }

    public PathNode getNextNode() {
      return nextNode;
    }

    public void setNextNode(PathNode nextNode) {
      this.nextNode = nextNode;
    }
  }

  public static class Node {
    private String id;
    private String name;
    private String type;
    private String parentId;
    private Integer sort;
    private Map<String, Object> ext;

    public Node() {
    }

    public Node(String id, String name, String type, String parentId, Integer sort) {
      this.id = id;
      this.name = name;
      this.type = type;
      this.parentId = parentId;
      this.sort = sort;
    }

    public Node(String id, String name, String type, String parentId, Integer sort, Map<String, Object> ext) {
      this.id = id;
      this.name = name;
      this.type = type;
      this.parentId = parentId;
      this.sort = sort;
      this.ext = ext;
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

    public Integer getSort() {
      return sort;
    }

    public void setSort(Integer sort) {
      this.sort = sort;
    }

    public Map<String, Object> getExt() {
      return ext;
    }

    public void setExt(Map<String, Object> ext) {
      this.ext = ext;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o)
        return true;
      if (!(o instanceof Node))
        return false;

      Node node = (Node) o;

      if (!getId().equals(node.getId()))
        return false;
      return getName().equals(node.getName());
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
  }
}
