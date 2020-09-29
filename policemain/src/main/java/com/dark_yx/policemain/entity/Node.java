package com.dark_yx.policemain.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WANGJIE on 2015-7-13.
 */
public class Node {

    private int id;
    private int pId;
    private String label;
    private int icon;
    private boolean isExpand = false;
    private int level;
    private Node parent;
    private int userId;
    private List<Node> children = new ArrayList<Node>();

    public Node() {

    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Node(int id, int pId, String label, int userId) {
        this.id = id;
        this.pId = pId;
        this.label = label;
        this.userId = userId;
    }

    @Override
    public String toString() {
        return userId + "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getpId() {
        return pId;
    }

    public void setpId(int pId) {
        this.pId = pId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setIsExpand(boolean isExpand) {
        this.isExpand = isExpand;
        if (!isExpand) {
            for (Node node : children) {
                node.setIsExpand(false);
            }
        }
    }

    public int getLevel() {
        return parent == null ? 0 : parent.getLevel() + 1;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public List<Node> getChildren() {
        return children;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }

    public boolean isRoot() {

        return parent == null;
    }

    public boolean isParentExpand() {
        if (parent == null) {
            return false;
        }
        return parent.isExpand();
    }

    public boolean isLeaf() {

        return children.size() == 0;
    }

}
