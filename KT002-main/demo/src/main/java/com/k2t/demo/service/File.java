package com.k2t.demo.service;

import com.k2t.demo.controllers.HomeController;
import com.k2t.demo.graph.Edge;
import com.k2t.demo.graph.GraphicsGraph;
import com.k2t.demo.graph.Vert;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;


import java.io.*;

public class File {
    public static GraphicsGraph graph;
    private static HomeController homeController;

    /***************************************************
                    SAVE GRAPH SECTION
     ***************************************************/

    /**
     * Hiển thị cửa sổ chọn nơi lưu file
     */
    public static void showSaveWindow(MouseEvent event) {
        /*
        Mở hộp thoại để lưu tập tin
         */
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Lưu đồ thị");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Graph File (*.qbit)", "*.qbit"));

        java.io.File selectedFile; // tập tin đã chọn để lưu
        selectedFile = fileChooser.showSaveDialog(((Node)event.getSource()).getScene().getWindow());

        // Nếu tập tin được khởi tạo đúng thì tiến hành ghi nội dung vào tập tin và lưu
        if (selectedFile != null) {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(selectedFile));

                // Số lượng đỉnh
                writer.write(String.valueOf(graph.getVerts().size()) + "\t");
                // Số lượng cung
                writer.write(String.valueOf(graph.getEdges().size()));
                writer.write("\n");

                // Từng đỉnh có trong đồ thị
                for (Vert vert : graph.getVerts()) {
                    // Nhãn của đỉnh
                    writer.write(vert.getName() + "\t");
                    // Tọa độ của đỉnh
                    writer.write(String.valueOf(vert.getLabel().getLayoutX())
                            + "\t" + String.valueOf(vert.getLabel().getLayoutY()));
                    writer.write("\n");
                }

                // Từng cung có trong đồ thị
                for (Edge edge : graph.getEdges()) {
                    // Đỉnh bắt đầu - Đỉnh kết thúc
                    writer.write(edge.getBeginVert().getName()+ "\t" + edge.getEndVert().getName() + "\t");
                    // Trọng số cung
                    writer.write(String.valueOf(edge.getWeight()));
                    writer.write("\n");
                }

                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /***************************************************
                    OPEN GRAPH SECTION
     ***************************************************/

    /*
     Mở hộp thoại để mở tập tin
     */
    public  static void showOpenWindow(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Mở đồ thị");
        java.io.File selectedFile; // tập tin đã chọn để mở
        selectedFile = fileChooser.showOpenDialog(((Node)event.getSource()).getScene().getWindow());

        if (selectedFile != null) {
            try {
                graph.reset();

                BufferedReader reader = new BufferedReader(new FileReader(selectedFile));
                String line;
                int vertNumber = 0;
                int edgeNumber = 0;
                int i = 0;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split("\t");
                    if (i == 0) {
                        vertNumber = Integer.parseInt(parts[0].trim());
                        edgeNumber = Integer.parseInt(parts[1].trim());

                    }
                    else if (i >= 1 && i <= vertNumber) {
                        graph.getVerts().add(readVertFromFile(parts));
                    }
                    else {
                        readEdgeFromFile(parts);
                    }
                    i++;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    /**
     * Đọc và xử lý đỉnh từ tập tin
     *
     * @param parts các thành phần của dòng đã chia theo kí tự tab
     */
    public static Vert readVertFromFile(String[] parts) {
        /*
        Khởi tạo và đặt thuộc tính cho label
         */

        Label vertexLabel = new Label(parts[0]);
        vertexLabel.setFont(new Font("System Bold", 24));
        vertexLabel.setStyle("-fx-label-padding: 15; -fx-background-radius: 100; -fx-border-radius: 100; -fx-background-color: white; -fx-border-color: black; -fx-min-height: 60; -fx-min-width: 60; -fx-max-height: 60; -fx-max-width: 60; -fx-alignment: CENTER;");
        vertexLabel.setLayoutX(Double.parseDouble(parts[1]));
        vertexLabel.setLayoutY(Double.parseDouble(parts[2]));

        /*
        Khởi tạo và đặt thuộc tính cho vert
         */
        Vert vertWithPosition = new Vert();
        vertWithPosition.setLabel(vertexLabel);
        vertWithPosition.setVisited(false);
        // Thêm số vào usedNumbers
        int vertNumber = Integer.parseInt(parts[0]);
        return vertWithPosition;
    }

    /**
     * Đọc và xử lý cung từ tập tin
     *
     * @param parts các thành phần của dòng đã chia theo kí tự tab
     */
    public static void readEdgeFromFile(String[] parts) {
        Vert beginVert = graph.findVertByName(parts[0]);
        Vert endVert = graph.findVertByName(parts[1]);

        if (beginVert != null && endVert != null) {
            graph.addEdge(beginVert, endVert, Integer.parseInt(parts[2]));

            // Gọi lại hàm thiết lập sự kiện cho Label trọng số của cung mới thêm vào
            if (!graph.getEdges().isEmpty()) {
                Edge newEdge = graph.getEdges().get(graph.getEdges().size() - 1);
//                if (newEdge.getWeightLabel() != null) {
//                   addEventForEdgeLabel(newEdge.getWeightLabel());
//                }
            }
        }
    }
}