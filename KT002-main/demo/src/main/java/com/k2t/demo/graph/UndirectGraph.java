package com.k2t.demo.graph;

import java.util.ArrayList;
import java.util.List;

/**
 * UnredirectGraph - Đồ thị có hướng làm việc với giao diện với javafx
 * @author Nguyễn Ngọc Truyện
 * @version 1.0
 * @since  1.0
 */
public abstract class UndirectGraph extends GraphicsGraph {

    @Override
    public Edge findEdgeByVerts(Vert vert1, Vert vert2) {
        for (Edge edge : edges) {
            if ((edge.getBeginVert().equals(vert1) && edge.getEndVert().equals(vert2)) ||
                    (edge.getBeginVert().equals(vert2) && edge.getEndVert().equals(vert1))) {
                return edge;
            }
        }
        return null;
    }

    @Override
    public List<Edge> findEdgesByVert(Vert vert) {
        List<Edge> resultEdges = new ArrayList<>();
        for(int i = 0; i < edges.size(); i++) {
            if(edges.get(i).getBeginVert() == vert)
                resultEdges.add(edges.get(i));
        }
        return resultEdges;
    }

}
