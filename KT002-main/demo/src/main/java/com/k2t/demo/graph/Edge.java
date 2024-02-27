package com.k2t.demo.graph;

import javafx.scene.control.Label;
import javafx.scene.shape.Line;

public class Edge {
    private Vert beginVert;
    private Vert endVert;
    private Label weightLabel;
    private Line lineEdge;
    private int weight;

    public Edge(Vert beginVert, Vert endVert) {
        this.beginVert = beginVert;
        this.endVert = endVert;
    }

    public Edge(Vert beginVert, Vert endVert, int weight) {
        this.beginVert = beginVert;
        this.endVert = endVert;
        this.weight = weight;
    }

    public Vert getBeginVert() {
        return beginVert;
    }

    public void setBeginVert(Vert beginVert) {
        this.beginVert = beginVert;
    }

    public Vert getEndVert() {
        return endVert;
    }

    public void setEndVert(Vert endVert) {
        this.endVert = endVert;
    }

    public Label getWeightLabel() {
        return weightLabel;
    }

    public void setWeightLabel(Label weightLabel) {
        this.weightLabel = weightLabel;
    }

    public Line getLineEdge() {
        return lineEdge;
    }

    public void setLineEdge(Line lineEdge) {
        this.lineEdge = lineEdge;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
    public void updateWeight(int newWeight) {
        this.weight = newWeight;
        // Cập nhật trọng số trên Label (nếu có)
        if (weightLabel != null) {
            weightLabel.setText(String.valueOf(newWeight));
        }
    }
}
