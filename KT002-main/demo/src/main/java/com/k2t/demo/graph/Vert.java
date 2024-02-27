package com.k2t.demo.graph;

import javafx.scene.control.Label;

public class Vert {
    private Label label;
    private boolean isVisited;

    /**
     * Construct mặc nhiên
     */
    public Vert() {
        this.label = new Label();
        this.isVisited = false;
    }

    /**
     * Sao chép cạn cho vert và mặc định chưa được duyệt
     * @param label
     */
    public Vert(Label label) {
        this.label = label;
        this.isVisited = false;
    }

    /**
     * Sao chép sâu cho vert
     * @param vert
     */
    public Vert(Vert vert) {
        this.label = vert.label;
        this.isVisited = vert.isVisited;
    }

    /**
     * Lấy control Label của đỉnh
     * @return Label của đỉnh
     */
    public Label getLabel() {
        return label;
    }

    /**
     * Đặt lấy Label cho đỉnh
     * @param label
     */
    public void setLabel(Label label) {
        this.label = label;
    }

    /**
     * Kiểm tra đỉnh đã được duyệt chưa?
     * @return True nếu đã duyệt, False nếu chưa
     */
    public boolean isVisited() {
        return isVisited;
    }

    /**
     * Đặt trạng thái duyệt cho đỉnh
     * @param visited
     */
    public void setVisited(boolean visited) {
        isVisited = visited;
    }

    /**
     * Lấy nhãn tên của đỉnh
     * @return nhãn của đỉnh.
     */
    public String getName() {
        return label.getText().trim();
    }

    public String toString() {
        return "Đỉnh " + getName();
    }
}
