package wang.wangby.utils.tree;

public interface NodeVistor {
    //访问某个节点
    boolean vister(TreeNode node,int layer);
}
