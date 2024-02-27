package com.k2t.demo.service;

import com.k2t.demo.graph.Edge;
import com.k2t.demo.graph.UndirectGraph;
import com.k2t.demo.graph.Vert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SpanningTree extends UndirectGraph {
    @Override
    protected Label createVertLabel(MouseEvent mouseEvent) {
        Label vertexLabel = new Label(name.pop());
        vertexLabel.setFont(new Font("System Bold", 24));
        vertexLabel.setStyle("-fx-label-padding: 15; -fx-background-radius: 100; -fx-border-radius: 100; -fx-background-color: white; -fx-border-color: black; -fx-min-height: 60; -fx-min-width: 60; -fx-max-height: 60; -fx-max-width: 60; -fx-alignment: CENTER;");
        vertexLabel.setLayoutX(mouseEvent.getX() - 30);
        vertexLabel.setLayoutY(mouseEvent.getY() - 30);
        return vertexLabel;
    }

    @Override
    public void resetStyle() {
        for(Vert vert : verts) {
            vert.getLabel().setStyle("-fx-label-padding: 15; -fx-background-radius: 100; -fx-border-radius: 100; -fx-background-color: white; -fx-border-color: black; -fx-min-height: 60; -fx-min-width: 60; -fx-max-height: 60; -fx-max-width: 60; -fx-alignment: CENTER;");
        }
        for(Edge edge : edges) {
            edge.getLineEdge().setStyle("-fx-stroke: rgb(0, 0, 0);");
        }
    }

    public List<Edge> minSpanningTree() {
        List<Edge> result = new ArrayList<>();
        List<Edge> sortedEdges = new ArrayList<>(edges);
        sortedEdges.sort(Comparator.comparingDouble(Edge::getWeight));

        int[] parent = new int[verts.size()];
        for (int i = 0; i < verts.size(); i++) {
            parent[i] = i;
        }

        for (Edge edge : sortedEdges) {
            int x = find(parent, verts.indexOf(edge.getBeginVert()));
            int y = find(parent, verts.indexOf(edge.getEndVert()));

            if (x != y) {
                result.add(edge);
                union(parent, x, y);
            }
        }

        drawAllVertColor(colors.get(10));
        drawEdgesColor(result, colors.get(10));
        return result;
    }

    private int find(int[] parent, int i) {
        if (parent[i] == i) {
            return i;
        }
        return find(parent, parent[i]);
    }

    private void union(int[] parent, int x, int y) {
        int rootX = find(parent, x);
        int rootY = find(parent, y);
        parent[rootX] = rootY;
    }

}
