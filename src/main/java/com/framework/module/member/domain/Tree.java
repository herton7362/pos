package com.framework.module.member.domain;

import java.util.*;

/**
 * <p>Description: </p>
 *
 * @Auther: 张庆贺
 * @Date: 2018/6/29 10:13
 */
public class Tree {
    private Member member;
    private List<Tree> children = new ArrayList<Tree>();
    private Double transactionAmount = 0d;
    private Map<String,Integer> childLevelList = new HashMap<>();

    public Tree(Member member) {
        this.member = member;
    }

    /**
     * 添加子树
     *
     * @param tree 子树
     */
    public void addNode(Tree tree) {
        children.add(tree);
    }

    /**
     * 置空树
     */
    public void clearTree() {
        member = null;
        children.clear();
    }

    /**
     * 求树的深度
     * 这方法还有点问题，有待完善
     *
     * @param tree
     * @return
     */
    private int dept(Tree tree) {
        if (tree.isEmpty()) {
            return 0;
        } else if (tree.isLeaf()) {
            return 1;
        } else {
            int n = children.size();
            int[] a = new int[n];
            for (int i = 0; i < n; i++) {
                if (children.get(i).isEmpty()) {
                    a[i] = 0 + 1;
                } else {
                    a[i] = dept(children.get(i)) + 1;
                }
            }
            Arrays.sort(a);
            return a[n - 1];
        }
    }

    public Double getTransactionAmount() {
        return transactionAmount;
    }

    /**
     * 增加收益金额
     * @param d 本次增加的收益金额
     * @return 增加后的收益金额
     */
    public Double addTransactionAmount(double d) {
        transactionAmount += d;
        return transactionAmount;
    }

    public Map<String, Integer> getChildLevelMap() {
        return childLevelList;
    }

    /**
     * 返回递i个子树
     *
     * @param i
     * @return
     */
    public Tree getChild(int i) {
        return children.get(i);
    }

    /**
     * 求第一个孩子 结点
     *
     * @return
     */
    public Tree getFirstChild() {
        return children.get(0);

    }

    /**
     * 求最后 一个孩子结点
     *
     * @return
     */
    public Tree getLastChild() {
        return children.get(children.size() - 1);
    }

    public List<Tree> getChildren() {
        return children;
    }

    /**
     * 获得根结点的数据
     *
     * @return
     */
    public Member getRootData() {
        return member;
    }

    /**
     * 判断是否为空树
     *
     * @return 如果为空，返回true,否则返回false
     */
    public boolean isEmpty() {
        if (children.isEmpty() && member == null)
            return true;
        return false;
    }

    /**
     * 判断是否为叶子结点
     *
     * @return
     */
    public boolean isLeaf() {
        if (children.isEmpty())
            return true;
        return false;
    }

    /**
     * 获得树根
     *
     * @return 树的根
     */
    public Tree root() {
        return this;
    }

    /**
     * 设置根结点的数据
     */
    public void setRootData(Member member) {
        this.member = member;
    }

    /**
     * 求结点数
     * 这方法还有点问题，有待完善
     *
     * @return 结点的个数
     */
    public int size() {
        return size(this);
    }

    /**
     * 求结点数
     * 这方法还有点问题，有待完善
     *
     * @param tree
     * @return
     */
    private int size(Tree tree) {
        if (tree.isEmpty()) {
            return 0;
        } else if (tree.isLeaf()) {
            return 1;
        } else {
            int count = 1;
            int n = children.size();
            for (int i = 0; i < n; i++) {
                if (!children.get(i).isEmpty()) {
                    count += size(children.get(i));
                }
            }
            return count;
        }
    }
}
