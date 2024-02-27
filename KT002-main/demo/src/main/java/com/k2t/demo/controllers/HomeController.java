package com.k2t.demo.controllers;

import com.k2t.demo.service.*;
import com.k2t.demo.graph.Edge;
import com.k2t.demo.graph.Vert;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

import java.net.URL;
import java.util.*;

public class HomeController implements Initializable {
    /************************
     * CÁC THUỘC TÍNH CONTROL
     ************************/
    @FXML
    private ImageView vertButton;
    @FXML
    private ImageView edgeButton;
    @FXML
    private ImageView moveButton;
    @FXML
    private ImageView deleteButton;
    @FXML
    private ImageView searchButton;
    @FXML
    private ImageView spanningTreeButton;
    @FXML
    private ImageView connectivityGraphButton;
    @FXML
    private Pane mainPane;
    @FXML
    private Label infoLabel;
    @FXML
    private Canvas canvas;

    /**********************
     *  CÁC THUỘC TÍNH KHÁC
     *********************/
    private SpanningTree graph;
    private boolean isEnableAddVert;
    private boolean isEnableAddEdge;
    private boolean isEnableMove;
    private boolean isEnableDelete;
    private boolean isEnableSearch;
    private boolean isEnableSpanning;
    private boolean isEnableConnectivity;

    private Line edgeInProcess;
    private Label selectedVertLabel;


    /**
     * Phương thức khởi tạo scene
     *
     * @param url            url
     * @param resourceBundle resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        graph = new SpanningTree();
        isEnableDelete = false;
        isEnableAddEdge = false;
        isEnableAddVert = false;
        isEnableConnectivity = false;
        isEnableMove = false;
        isEnableSearch = false;
        isEnableSpanning = false;
    }

    /*****************************
     * CÁC PHƯƠNG THỨC BẮT SỰ KIỆN
     *****************************/

    /**
     * Bắt sự kiện khi nhấn vào nút vẽ đỉnh
     *
     * @param event
     */
    @FXML
    public void clickedVertButton(MouseEvent event) {
        isEnableAddVert = !isEnableAddVert;
        setStateButton(vertButton, isEnableAddVert);
    }

    /**
     * Bắt sự kiện khi nhấn vào nút vẽ cung
     *
     * @param event
     */
    @FXML
    public void clickedEdgeButton(MouseEvent event) {
        isEnableAddEdge = !isEnableAddEdge;
        setStateButton(edgeButton, isEnableAddEdge);
    }

    /**
     * Bắt sự kiện khi nhấn vào nút di chuyển
     *
     * @param event
     */
    @FXML
    public void clickedMoveButton(MouseEvent event) {
        isEnableMove = !isEnableMove;
        setStateButton(moveButton, isEnableMove);
    }

    /**
     * Bắt sự kiện khi nhấn chuột vào nút cài lại
     *
     * @param event sự kiện tương ứng
     */
    @FXML
    public void clickedResetButton(MouseEvent event) {
        resetGraph();
    }

    /**
     * Bắt sự kiện khi nhấn vào nút xóa
     *
     * @param event
     */
    @FXML
    public void clickedDeleteButton(MouseEvent event) {
        isEnableDelete = !isEnableDelete;
        setStateButton(deleteButton, isEnableDelete);
    }

    /**
     * Bắt sự kiện khi nhấn vào nút lưu
     *
     * @param event
     */
    @FXML
    public void clickedSaveButton(MouseEvent event) {
        File.graph = this.graph;
        File.showSaveWindow(event);
    }

    /**
     * Bắt sự kiện khi nhấn vào nút mở tập tin
     *
     * @param event
     */
    @FXML
    public void clickedOpenButton(MouseEvent event) {
        mainPane.getChildren().clear();
        mainPane.getChildren().add(canvas);

        File.graph = this.graph;
        File.showOpenWindow(event);

        drawAllFromFile(graph.getVerts(), graph.getEdges());

    }

    /**
     * Bắt sự kiện khi nhấn vào nút duyệt đồ thị
     *
     * @param event
     */
    @FXML
    public void clickedSearchButton(MouseEvent event) {
        isEnableSearch = !isEnableSearch;
        setStateButton(searchButton, isEnableSearch);

        depthFirstSearch();
    }


    /**
     * Bắt sự kiện khi nhấn vào nút tìm cây khung nhỏ nhất
     *
     * @param event
     */
    @FXML
    public void clickedSpanningTreeButton(MouseEvent event) {
        isEnableSpanning = !isEnableSpanning;
        setStateButton(spanningTreeButton, isEnableSpanning);

        minSpanningTree();
    }

    /**
     * Bắt sự kiện khi nhấn vào nút xác định bộ phận liên thông
     *
     * @param event
     */
    @FXML
    public void clickedConnectivityGraphButton(MouseEvent event) {
        isEnableConnectivity = !isEnableConnectivity;
        setStateButton(connectivityGraphButton, isEnableConnectivity);

        connectivityGraph();
    }


    /**
     * Bắt sự kiện khi nhấn chuột vào khung vẽ
     *
     * @param event
     */
    @FXML
    public void clickedCanvas(MouseEvent event) {
        if (isEnableAddVert)
            addVert(event);
    }

    /**
     * Bắt sự kiện khi nhấn chuột vào đỉnh cần xóa
     *
     * @param event
     */
    private void clickedDeleteVert(MouseEvent event) {
        if (isEnableDelete)
            deleteVert((Label) event.getSource());
    }

    /**
     * Bắt sự kiện ấn chuột vào đỉnh
     *
     * @param event
     */
    private void pressedVert(MouseEvent event) {
        if (isEnableMove)
            startMoveVert(event);
        if (isEnableAddEdge)
            startDrawEdge(event);
    }

    /**
     * Bắt sự kiện kéo chuột
     *
     * @param event
     */
    private void draggedVert(MouseEvent event) {
        if (isEnableAddEdge && selectedVertLabel != null)
            drawEdge(event);
        if (isEnableMove && selectedVertLabel != null)
            moveVert(event);
    }

    /**
     * Bắt sự kiện thả chuột
     *
     * @param event
     */
    private void releasedVert(MouseEvent event) {
        if (isEnableAddEdge && selectedVertLabel != null)
            endDrawEdge(event);
        if (isEnableMove && selectedVertLabel != null)
            endMoveVert(event);
    }

    /**
     * Trả về đỉnh khi có tọa độ tại vị trí chuột
     *
     * @param event
     * @return
     */
    private Label releaseVertexLabel(MouseEvent event) {
        for (int i = 0; i < graph.getVerts().size(); i++) {
            Label vertex = graph.getVerts().get(i).getLabel();
            Bounds bounds = vertex.getBoundsInParent();
            if (bounds.contains(event.getX() + selectedVertLabel.getLayoutX(), event.getY() + selectedVertLabel.getLayoutY()))
                return graph.getVerts().get(i).getLabel();
        }
        return null;
    }

    /**
     * Bắt sự kiện click cung cần xóa
     *
     * @param event
     */
    private void clickedEdge(MouseEvent event) {
        if (isEnableDelete)
            deleteEdge((Line) event.getSource());
    }

    /**
     * Bắt sự kiện click vao trong so can sua
     *
     * @param mouseEvent
     */
    private void clickedLabelEdge(MouseEvent mouseEvent) {
        updateWeight((Label) mouseEvent.getSource());
        System.out.println("Mouse clicked on edge label");
    }
    /************************
     * XỬ LÝ CHI TIẾT SỰ KIỆN
     ************************/

    /**
     * Đặt trạng thái của nút
     *
     * @param button nút cần đặt trạng thái
     * @param state  trạng thái cần đặt
     */
    private void setStateButton(ImageView button, boolean state) {
        isEnableDelete = false;
        setImageStyle(deleteButton, false);
        isEnableAddEdge = false;
        setImageStyle(edgeButton, false);
        isEnableAddVert = false;
        setImageStyle(vertButton, false);
        isEnableConnectivity = false;
        setImageStyle(connectivityGraphButton, false);
        isEnableMove = false;
        setImageStyle(moveButton, false);
        isEnableSearch = false;
        setImageStyle(searchButton, false);
        isEnableSpanning = false;
        setImageStyle(spanningTreeButton, false);

        if (button == vertButton)
            isEnableAddVert = state;
        if (button == edgeButton)
            isEnableAddEdge = state;
        if (button == moveButton)
            isEnableMove = state;
        if (button == deleteButton)
            isEnableDelete = state;
        if (button == searchButton)
            isEnableSearch = state;
        if (button == connectivityGraphButton)
            isEnableConnectivity = state;
        if (button == spanningTreeButton)
            isEnableSpanning = state;

        setImageStyle(button, state);
    }

    public void addEventForEdgeLabel(Label label) {
        label.setOnMouseClicked(this::clickedLabelEdge);
    }

    /**
     * Thay đổi ảnh của nút theo trạng thái
     *
     * @param button nút cần thay đổi
     * @param state  trạng thái nút
     */
    private void setImageStyle(ImageView button, boolean state) {
        if (state) {
            String url = button.getImage().getUrl().replaceAll("1.png", "2.png");
            button.setImage(new Image(url));
        } else {
            String url = button.getImage().getUrl().replaceAll("2.png", "1.png");
            button.setImage(new Image(url));
        }
    }
    /**
     * Xử lý thêm đỉnh
     *
     * @param event
     */
    private void addVert(MouseEvent event) {
        // Kiểm tra xem đỉnh có nằm ngoài biên của mainPane không
        if (isNodeOutsideBounds(event) == true) {
            // Hiển thị hộp thoại lỗi nếu đỉnh nằm ngoài biên
            showErrorDialog("Đỉnh phải nằm trong vùng vẽ.");
        } else {
            Label vertexLabel = graph.addVert(event);
            addEventForVertLabel(vertexLabel);

            // Thêm đỉnh vào mainPanel
            mainPane.getChildren().add(vertexLabel);
        }
    }
    private boolean isNodeOutsideBounds(MouseEvent event) {
        if(event.getX() >= 30 && event.getY() >= 30 && event.getX() <= 1030 && event.getY() <= 495 ) {
            return false;
        }
        else {
            return true;
        }
    }
    private void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Lỗi");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }
    /**
     * Xóa đỉnh dựa trên label
     *
     * @param source
     */
    private void deleteVert(Label source) {
        Vert deletedVert = graph.findVertByLabel(source);
        if (deletedVert != null) {
            // Xóa đỉnh từ đồ thị và danh sách đỉnh đã xóa
            List<Edge> edges = graph.findAllEdgeWithVert(deletedVert);
            for (Edge edge : edges) {
                deleteEdge(edge.getLineEdge());
            }
            // Xóa đỉnh khỏi mainPane
            mainPane.getChildren().remove(source);
            graph.deleteVert(deletedVert);
        }
    }

    /**
     * Bắt đầu vẽ cung
     *
     * @param event
     */
    private void startDrawEdge(MouseEvent event) {
        selectedVertLabel = (Label) event.getSource();
        /**
         * Tiến hành khởi tạo cung tạm và thêm vào panel
         */
        double startLineX = selectedVertLabel.getLayoutX() + selectedVertLabel.getWidth() / 2;
        double startLineY = selectedVertLabel.getLayoutY() + selectedVertLabel.getWidth() / 2;

        edgeInProcess = new Line(startLineX, startLineY,
                startLineX, startLineY);
        mainPane.getChildren().add(edgeInProcess);
    }

    /**
     * Tiến hành vẽ cung
     *
     * @param event
     */
    private void drawEdge(MouseEvent event) {
        double radius = selectedVertLabel.getWidth() / 2;

        double startLineX = selectedVertLabel.getLayoutX() + radius;
        double startLineY = selectedVertLabel.getLayoutY() + radius;
        double endLineX = selectedVertLabel.getLayoutX() + event.getX();
        double endLineY = selectedVertLabel.getLayoutY() + event.getY();

        double angle = Math.atan2(endLineY - startLineY, endLineX - startLineX);

        double startXOnCircle = startLineX + radius * Math.cos(angle);
        double startYOnCircle = startLineY + radius * Math.sin(angle);

        edgeInProcess.setStartX(startXOnCircle);
        edgeInProcess.setStartY(startYOnCircle);

        edgeInProcess.setEndX(endLineX);
        edgeInProcess.setEndY(endLineY);
    }

    /**
     * Kết thúc vẽ cung
     *
     * @param event
     */
    private void endDrawEdge(MouseEvent event) {
        Label releaseVertexLabel = releaseVertexLabel(event);

        if (releaseVertexLabel != null && releaseVertexLabel != selectedVertLabel) {
            Vert beginVert = graph.findVertByLabel(selectedVertLabel);
            Vert endVert = graph.findVertByLabel(releaseVertexLabel);

            Edge edge = graph.findEdgeByVerts(beginVert, endVert);

            if (edge == null)
                showInputWeightDialog(beginVert, endVert, "Nhập trọng số cung");
            else {
                showDialog(Alert.AlertType.WARNING, "Cảnh bao", null, "Cung đã tồn tại");
            }
        }
        selectedVertLabel = null;
        mainPane.getChildren().remove(edgeInProcess);
        edgeInProcess = null;
    }

    /**
     * Hiển thị thông báo alert
     *
     * @param type        loại thông báo
     * @param title       tiêu đề
     * @param header      đầu đề
     * @param contentText nội dung
     */
    private void showDialog(Alert.AlertType type, String title, String header, String contentText) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(contentText);

        alert.showAndWait();
    }

    /**
     * Nhập trọng số qua dialog
     *
     * @param beginVert đỉnh thứ nhất
     * @param endVert   đỉnh thứ hai
     * @param message   tin
     */
    private void showInputWeightDialog(Vert beginVert, Vert endVert, String message) {
        TextInputDialog dialog = new TextInputDialog("1");
        dialog.setTitle("Nhập trọng số cung: ");
        dialog.setHeaderText(null);
        dialog.setContentText(message);

        // Lấy giá trị nhập từ dialog
        dialog.showAndWait().ifPresent(weight -> {
            try {
                int parsedWeight = Integer.parseInt(weight);
                if (parsedWeight > 0) {
                    addEdge(beginVert, endVert, parsedWeight);
                } else {
                    showInputWeightDialog(beginVert, endVert, "Vui lòng nhập số lớn hơn 0");
                }
            } catch (NumberFormatException e) {
                showInputWeightDialog(beginVert, endVert, "Vui lòng nhập số");
            }
        });
    }

    /**
     * Thêm cung
     *
     * @param beginVert đỉnh thứ nhất
     * @param endVert   đỉnh thứ hai
     * @param weight    trọng số
     */
    private void addEdge(Vert beginVert, Vert endVert, int weight) {
        Edge edge = graph.addEdge(beginVert, endVert, weight);

        /**
         * Thêm sự kiện cho edge
         * line cung và label trong số vào mainPane;
         */
        edge.getLineEdge().setOnMouseClicked(this::clickedEdge);
        edge.getWeightLabel().setOnMouseClicked(this::clickedLabelEdge);

        mainPane.getChildren().addAll(edge.getLineEdge(), edge.getWeightLabel());
    }

    /**
     * Xóa cung dựa vào line
     *
     * @param edgeLine
     */
    private void deleteEdge(Line edgeLine) {
        Edge temp = graph.findEdgeByLine(edgeLine);
        graph.deleteEdge(edgeLine);
        mainPane.getChildren().removeAll(temp.getLineEdge(), temp.getWeightLabel());

    }

    /**
     * Bắt đầu di chuyển đỉnh
     *
     * @param event
     */
    private void startMoveVert(MouseEvent event) {
        selectedVertLabel = (Label) event.getSource();
    }

    /**
     * Tiến hành di chuyển đỉnh
     *
     * @param event
     */
    private void moveVert(MouseEvent event) {

        double newX = event.getX() + selectedVertLabel.getLayoutX() - selectedVertLabel.getWidth() / 2;
        double newY = event.getY() + selectedVertLabel.getLayoutY() - selectedVertLabel.getHeight() / 2;

        double canvasWidth = canvas.getWidth();
        double canvasHeight = canvas.getHeight();

        double vertWidth = selectedVertLabel.getWidth();
        double vertHeight = selectedVertLabel.getHeight();

        if (newX >= 0 && newY >= 0 && newX + vertWidth <= canvasWidth && newY + vertHeight <= canvasHeight) {
            // Di chuyển đỉnh
            selectedVertLabel.setLayoutX(newX);
            selectedVertLabel.setLayoutY(newY);

            List<Edge> edges = graph.findAllEdgeWithVert((graph.findVertByLabel(selectedVertLabel)));

            // Di chuyển các cung nối tới đỉnh
            for (Edge edge : edges) {
                Vert fixedVert = null;
                Line line = edge.getLineEdge();

                if (selectedVertLabel == edge.getBeginVert().getLabel())
                    fixedVert = edge.getEndVert();
                else
                    fixedVert = edge.getBeginVert();

                double radius = selectedVertLabel.getWidth() / 2;

                double startLineX = fixedVert.getLabel().getLayoutX() + radius;
                double startLineY = fixedVert.getLabel().getLayoutY() + radius;
                double endLineX = selectedVertLabel.getLayoutX() + event.getX();
                double endLineY = selectedVertLabel.getLayoutY() + event.getY();

                double angle = Math.atan2(endLineY - startLineY, endLineX - startLineX);

                double startXOnCircle = startLineX + radius * Math.cos(angle);
                double startYOnCircle = startLineY + radius * Math.sin(angle);
                double endXOnCircle = endLineX - radius * Math.cos(angle);
                double endYOnCircle = endLineY - radius * Math.sin(angle);

                line.setStartX(startXOnCircle);
                line.setStartY(startYOnCircle);

                line.setEndX(endXOnCircle);
                line.setEndY(endYOnCircle);

                edge.getWeightLabel().setLayoutX((line.getStartX() + line.getEndX()) / 2);
                edge.getWeightLabel().setLayoutY((line.getStartY() + line.getEndY()) / 2);
            }

        }
    }

    /**
     * Kết thúc di chuyển
     *
     * @param event
     */
    private void endMoveVert(MouseEvent event) {
        selectedVertLabel = null;
    }


    /**
     * Xử lý sự kiện duyệt đồ thị theo chiều sâu
     */
    private void depthFirstSearch() {
        if (isEnableSearch) {
            ChoiceDialog<Vert> dialog = new ChoiceDialog<>();
            dialog.setTitle("Duyệt đồ thị theo chiều sâu");
            dialog.setHeaderText("Chọn đỉnh: ");
            dialog.getItems().addAll(graph.getVerts());

            dialog.showAndWait();

            Vert startVert = dialog.getSelectedItem();
            if (startVert != null) {
                List<Vert> result = graph.dfs(startVert);
                infoLabel.setText("Kết quả duyệt đồ thị là:\n");
                for (Vert vert : result) {
                    infoLabel.setText(infoLabel.getText() + " " + vert.getName());
                }
            }
        } else {
            infoLabel.setText("");
        }
    }

    /**
     * Xử lý tìm các bộ phận liên thông
     */
    private void connectivityGraph() {
        if (isEnableConnectivity)
            infoLabel.setText("Số bộ phận liên thông:\n" + graph.connectivity().getKey());
        else {
            infoLabel.setText("");
            graph.resetStyle();
        }

    }

    /**
     * Xử lý cho sự kiện xác định cây khung tối thiểu
     */
    private void minSpanningTree() {
        if (isEnableSpanning) {
            if (graph.connectivity().getKey() == 1)
                graph.minSpanningTree();
            else {
                showDialog(Alert.AlertType.ERROR, "Đồ thị liên thông", "Lỗi ràng buộc", "Đang có hơn 1 đồ thị liên thông");
            }
        } else {
            graph.resetStyle();
        }
    }

    /**
     * Xử lý  vẽ đỉnh từ file
     *
     * @param vertListWithPosition danh sách đỉnh
     */
    public void drawAllFromFile(List<Vert> vertListWithPosition, List<Edge> edgeList) {
        for (Vert vert : vertListWithPosition) {
            graph.name.remove(vert.getLabel().getText());
            //Thêm sự kiện cho label của đỉnh
            addEventForVertLabel(vert.getLabel());

            mainPane.getChildren().add(vert.getLabel());
        }
        for (Edge edge : edgeList) {

            //Thêm sự kiện cho line của cung
            edge.getLineEdge().setOnMouseClicked(this::clickedEdge);
            //Them su kien cho label cung
            edge.getWeightLabel().setOnMouseClicked(this::clickedLabelEdge);
            mainPane.getChildren().addAll(edge.getLineEdge(), edge.getWeightLabel());
        }
    }


    /**
     * Thêm sự kiện cho label của vert
     *
     * @param vertLabel Label của vert tương ứng
     */
    private void addEventForVertLabel(Label vertLabel) {
        vertLabel.setOnMouseClicked(this::clickedDeleteVert);
        vertLabel.setOnMousePressed(this::pressedVert);
        vertLabel.setOnMouseDragged(this::draggedVert);
        vertLabel.setOnMouseReleased(this::releasedVert);
    }

    /**
     * Thay doi trong so cung
     *
     * @param source
     */
    private void updateWeight(Label source) {
        Edge edge = findEdgeByLabel(source);
        if (edge != null) {
            TextInputDialog dialog = new TextInputDialog(String.valueOf(edge.getWeight()));
            dialog.setTitle("Nhập trọng số cung: ");
            dialog.setHeaderText(null);
            dialog.setContentText("Nhập trọng số mới:");

            // Lấy giá trị nhập từ dialog
            dialog.showAndWait().ifPresent(weight -> {
                try {
                    int parsedWeight = Integer.parseInt(weight);
                    edge.updateWeight(parsedWeight);
                    edge.getWeightLabel().setText(weight);
                    // Kiểm tra số âm
                    if (parsedWeight <= 0) {
                        // Hiển thị thông báo nếu nhập số âm
                        updateWeight(source);
                    }
                } catch (NumberFormatException e) {
                    // Xử lý nếu người dùng nhập không phải số
                    updateWeight(source);
                }
            });
        }
    }

    /**
     * Tìm cung dựa trên Label của bạn
     *
     * @param label
     * @return đối tượng Edge hoặc null nếu không tìm thấy
     */
    private Edge findEdgeByLabel(Label label) {
        for (Edge edge : graph.getEdges()) {
            if (edge.getWeightLabel() == label) {
                return edge;
            }
        }
        return null;
    }

    /**
     * Xử lý sự kiện cài lại đồ thị
     */
    private void resetGraph() {
        // Xóa tất cả các đỉnh và cung
        graph.getVerts().clear();
        graph.getEdges().clear();
        mainPane.getChildren().clear();
        mainPane.getChildren().add(canvas);
        graph.reset();
    }
}
