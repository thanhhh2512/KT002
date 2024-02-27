package com.k2t.demo.graph;

import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.util.Pair;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * Graph - Đồ thị làm việc với giao diện với javafx
 * @author Nguyễn Ngọc Truyện
 * @version 1.0
 * @since  1.0
 */
public abstract class GraphicsGraph {
    protected List<Edge> edges;
    protected List<Vert> verts;

    public Stack<String> name;

    public List<String> colors;

    /**
     * Phương thức xây dựng mặc nhiên
     */
    public GraphicsGraph() {
        edges = new ArrayList<>();
        verts = new ArrayList<>();
        name = new Stack<>();
        colors = new ArrayList<>();

        for(int i = 1000; i >= 1; i--) {
            name.push(String.valueOf(i));

            if(i % 25== 0)
                colors.add("rgb(" + Math.random() * 10000 % 226 +  "," + (i + 6) % 226 + "," + (i + 27) % 226 + ");" );
        }
    }

    /**
     * Mặc định giá trị của phương thức
     */
    public void reset() {
        edges.clear();
        verts.clear();
        name.clear();
        colors.clear();

        for(int i = 1000; i >= 1; i--) {
            name.push(String.valueOf(i));

            if(i % 25== 0)
                colors.add("rgb(" + Math.random() * 10000 % 226 +  "," + (i + 6) % 226 + "," + (i + 27) % 226 + ");" );
        }
    }

    /**
     * Lấy danh sách cung
     * @return danh sách cung
     */
    public List<Edge> getEdges() {
        return edges;
    }

    /**
     * Lấy danh sách đỉnh
     * @return danh sách đỉnh
     */
    public List<Vert> getVerts() {
        return verts;
    }


    /**
     * Giải thuật duyệt đồ thị theo chiều sâu
     *
     * @return Danh sách các đỉnh theo thứ tự duyệt
     */
    public List<Vert> dfs(Vert startVert) {
        List<Vert> result = new ArrayList<>();
        Stack<Vert> stack = new Stack<>();

        if (!verts.isEmpty()) {
            stack.push(startVert);
        }

        while (!stack.isEmpty()) {
            Vert currentVert = stack.pop();

            if (!currentVert.isVisited()) {
                currentVert.setVisited(true);
                result.add(currentVert);

                List<Vert> neighbors = getUnvisitedNeighbors(currentVert);
                for (Vert neighbor : neighbors) {
                    stack.push(neighbor);
                }
            }
        }

        return result;
    }

    /**
     * Giải thuật tìm bộ phận liên thông
     *
     * @return Cấu trúc Map bao gồm key: số bộ phần liên thông, value: danh sách từng bộ phận
     */
    public Pair<Integer, List<List<Edge>>> connectivity() {
        //Mỗi bộ phận liên thông biểu diễn một danh sách cung thuộc bộ phần đó
        resetVisitedStatus();
        List<List<Edge>> connect = new ArrayList<>();
        int componentNumber = 0;

        for (Vert vert : verts) {
            if (!vert.isVisited()) {
                String choiceColor = colors.get(componentNumber++);

                connect.add(drawVertsColor(dfs(vert), choiceColor));
            }
        }
        Pair<Integer, List<List<Edge>>> result = new Pair<>(componentNumber, connect);

        resetVisitedStatus();
        return result;
    }

    private List<Vert> getUnvisitedNeighbors(Vert vert) {
        List<Vert> neighbors = new ArrayList<>();
        for (Edge edge : edges) {
            if (edge.getBeginVert().equals(vert) && !edge.getEndVert().isVisited()) {
                neighbors.add(edge.getEndVert());
            } else if (edge.getEndVert().equals(vert) && !edge.getBeginVert().isVisited()) {
                neighbors.add(edge.getBeginVert());
            }
        }
        return neighbors;
    }


    /**
     * Thêm đỉnh
     * @param mouseEvent sự kiện chuột
     * @return label của đỉnh vừa được thêm
     */
    public Label addVert(MouseEvent mouseEvent) {

        // Tạo control label đỉnh
        Label vertexLabel = createVertLabel(mouseEvent);

        /**
         * Tạo đỉnh và thêm đỉnh vào danh sách
         */
        Vert vert = new Vert();
        vert.setLabel(vertexLabel);
        vert.setVisited(false);
        this.verts.add(vert);
        return vertexLabel;
    }

    /**
     * Tạo label cho đỉnh
     * @param mouseEvent Sự kiện chuột
     * @return label tương ứng
     */
    protected abstract Label createVertLabel(MouseEvent mouseEvent);

    /**
     * Xóa đỉnh
     * @param vert đỉnh cần xóa
     */
    public void deleteVert(Vert vert) {
        String vertName = vert.getName();
        name.push(vertName);
        verts.remove(vert);
    }

    /**
     * Tìm đỉnh dựa trên control Label
     * @param label
     * @return đỉnh tương ứng
     */
    public Vert findVertByLabel(Label label) {
        for(int i = 0; i < verts.size(); i++)
            if(verts.get(i).getLabel() == label)
                return verts.get(i);
        return null;
    }

    /**
     * Tìm đỉnh dựa trên tên của đỉnh
     * @param name tên
     * @return đỉnh tuơng ứng
     */
    public Vert findVertByName(String name) {
        for(int i = 0; i < verts.size(); i++)
            if(verts.get(i).getName().compareTo(name) == 0)
                return verts.get(i);
        return null;
    }

    /**
     * Vẽ màu tất cả đỉnh
     * @param choiceColor màu được chọn
     */
    protected void drawAllVertColor(String choiceColor) {
        for(Vert vert : verts) {
            vert.getLabel().setStyle(vert.getLabel().getStyle() + "-fx-background-color: "+ choiceColor + "-fx-text-fill: white;");
        }
    }

    /**
     * Tô màu các đỉnh
     * @param vertexes danh sách đỉnh
     * @param choiceColor màu sắc cần tô
     * @return danh sách cung đã tô
     */
    private List<Edge> drawVertsColor(List<Vert> vertexes, String choiceColor) {
        List<Edge> resultEdges = new ArrayList<>();
        for(Vert vert : vertexes) {

            // Tô màu đỉnh
            vert.getLabel().setStyle(vert.getLabel().getStyle() + "-fx-background-color: "+ choiceColor + "-fx-text-fill: white;");

            /*
             * Tô màu các cung liên thuộc
             */
            List<Edge> edgeList = findEdgesByVert(vert);
            resultEdges.addAll(drawEdgesColor(edgeList, choiceColor));

        }
        return resultEdges;
    }
    /**
     * Thêm cung
     * @param beginVert đỉnh thứ nhất
     * @param endVert đỉnh thứ hai
     * @param weight trọng số cung
     * @return đỉnh được tạo
     */
    public Edge addEdge(Vert beginVert, Vert endVert, int weight) {
        /*
         * Tính toán các giá trị để hai đầu của cung là hai điểm gần nhau nhất nằm trên hai cung tròn
         */
        double radius = 30;
        double startLineX = beginVert.getLabel().getLayoutX() + radius;
        double startLineY = beginVert.getLabel().getLayoutY() + radius;
        double endLineX = endVert.getLabel().getLayoutX() + radius;
        double endLineY = endVert.getLabel().getLayoutY() + radius;
        double angle = Math.atan2(endLineY - startLineY, endLineX - startLineX);
        double startXOnCircle = startLineX + radius * Math.cos(angle);
        double startYOnCircle = startLineY + radius * Math.sin(angle);
        double endXOnCircle = endLineX - radius * Math.cos(angle);
        double endYOnCircle = endLineY - radius * Math.sin(angle);

        /*
         * Tạo đường biểu diễn cung
         */
        Line edgeLine = new Line(startXOnCircle, startYOnCircle, endXOnCircle, endYOnCircle);
        edgeLine.setStrokeWidth(3);

        /*
         * Tạo label để biểu diễn trọng số
         */
        Label weightLabel = new Label(String.valueOf(weight));
        weightLabel.setFont(new Font("System", 24));
        weightLabel.setLayoutX((startXOnCircle + endXOnCircle) / 2);
        weightLabel.setLayoutY((startYOnCircle + endYOnCircle) / 2);

        /*
         * Tạo và thêm Edge vào danh sách
         */
        Edge edge = new Edge(beginVert, endVert, weight);
        edge.setLineEdge(edgeLine);
        edge.setWeightLabel(weightLabel);
        edges.add(edge);

        return edge;
    }

    /**
     * Xóa cung dựa vào line
     * @param line
     */
    public void deleteEdge(Line line) {
        edges.remove(findEdgeByLine(line));
    }

    /**
     * Tìm cung dựa vào hai đỉnh
     * @param vert1 đỉnh thứ nhất
     * @param vert2 đỉnh thứ hai
     * @return cung tương ứng
     */
    public abstract Edge findEdgeByVerts(Vert vert1, Vert vert2);


    /**
     * Tìm cung dựa trên line
     * @param line
     * @return Cung cần tìm
     */
    public Edge findEdgeByLine(Line line) {
        for(int i = 0; i < edges.size(); i++)
            if(edges.get(i).getLineEdge() == line)
                return edges.get(i);
        return null;
    }

    /**
     * Tô màu các cung
     * @param edgeList danh sách cung
     * @param choiceColor màu cần tô
     * @return danh sách cung đã tô
     */
    protected List<Edge> drawEdgesColor(List<Edge> edgeList, String choiceColor) {
        List<Edge> resultEdges = new ArrayList<>();
        for(Edge edge : edgeList) {
            if(!resultEdges.contains(edge)) {
                resultEdges.add(edge);

                edge.getLineEdge().setStyle("-fx-stroke: " + choiceColor);
            }
        }
        return resultEdges;
    }

    /**
     * Tìm cung liên thuộc với đỉnh
     * @param vert đỉnh đầu vào
     * @return danh sách cung
     */
    public abstract List<Edge> findEdgesByVert(Vert vert);

    /**
     * Tìm tất cả các cung có nối với đỉnh
     * @param vert đỉnh
     * @return danh sách cung
     */
    public List<Edge> findAllEdgeWithVert(Vert vert){
        List<Edge> resultEdges = new ArrayList<>();
        for(int i = 0; i < edges.size(); i++) {
            if(edges.get(i).getBeginVert() == vert || edges.get(i).getEndVert() == vert)
                resultEdges.add(edges.get(i));
        }
        return resultEdges;
    }


    /**
     * Cài mặc định trạng thái duyệt
     */
    private void resetVisitedStatus() {
        for (Vert vert : verts) {
            vert.setVisited(false);
        }
    }

    /**
     * Cài mặc đỉnh style của đỉnh và cung
     */
    public abstract void resetStyle();
}
