package org.netscan.mvc.view;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.netscan.core.configuration.Filter;
import org.netscan.mvc.model.Share;

import java.time.LocalDate;

/**
 * Author Rigoberto Leander Salgado Reyes <rlsalgado2006@gmail.com>
 * <p>
 * Copyright 2015 by Rigoberto Leander Salgado Reyes.
 * <p>
 * This program is licensed to you under the terms of version 3 of the
 * GNU Affero General Public License. This program is distributed WITHOUT
 * ANY EXPRESS OR IMPLIED WARRANTY, INCLUDING THOSE OF NON-INFRINGEMENT,
 * MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE. Please refer to the
 * AGPL (http:www.gnu.org/licenses/agpl-3.0.txt) for more details.
 */
public class NetScanView extends BorderPane {
    MenuItem settings;
    MenuItem exit;
    MenuItem about;
    ComboBox<Filter> filterComboBox;
    Button searchButton;
    Button stopButton;
    TableView<Share> tableView;
    TextField searchTextField;
    MenuItem openFileManager;
    MenuItem credentials;
    ProgressBar progressBar;
    private MenuBar menuBar;
    private HBox toolBar;
    private VBox centerPanel;

    public NetScanView() {
        createMenu();
        createToolbar();
        createCenterPanel();
        setTop(menuBar);
        setCenter(centerPanel);
    }

    private void createCenterPanel() {
        openFileManager = new MenuItem("Open File Manager");
        credentials = new MenuItem("Credentials");
        ContextMenu contextMenu = new ContextMenu(openFileManager, credentials);

        tableView = new TableView<>();
        tableView.getColumns().add(getNameColumn());
        tableView.getColumns().add(getPathColumn());
        tableView.getColumns().add(getSizeColumn());
        tableView.getColumns().add(getLastModificationColumn());
        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        tableView.setContextMenu(contextMenu);

        progressBar = new ProgressBar();
        progressBar.setPrefWidth(250);
        VBox bottomPanel = new VBox(progressBar);
        VBox.setMargin(progressBar, new Insets(0, 8, 8, 8));
        setBottom(bottomPanel);

        centerPanel = new VBox(toolBar, tableView);
        VBox.setMargin(toolBar, new Insets(8, 8, 8, 8));
        VBox.setMargin(tableView, new Insets(0, 8, 8, 8));
        VBox.setVgrow(tableView, Priority.ALWAYS);
    }

    private void createToolbar() {
        Label filterLabel = new Label("Search files");

        filterComboBox = new ComboBox<>();
        filterComboBox.setPrefWidth(500);

        Label expander1 = new Label();
        expander1.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(expander1, Priority.ALWAYS);

        Label searchLabel = new Label();
        searchLabel.setGraphic(new ImageView(new Image(getClass().getClassLoader().getResource("images/filter.png").toExternalForm())));

        searchTextField = new TextField();
        searchTextField.setPromptText("Search file");
        searchTextField.setPrefWidth(250);

        searchButton = new Button(null, new ImageView(new Image(
                getClass().getClassLoader().getResource("images/search.png").toExternalForm())));

        stopButton = new Button(null, new ImageView(new Image(
                getClass().getClassLoader().getResource("images/stop.png").toExternalForm())));

        toolBar = new HBox(8, filterLabel, filterComboBox, searchButton, stopButton, expander1, searchLabel, searchTextField);

        HBox.setMargin(filterLabel, new Insets(5, 0, 5, 0));
    }

    private void createMenu() {
        settings = new MenuItem("Settings");
        exit = new MenuItem("Exit");
        Menu file = new Menu("File", null, settings, new SeparatorMenuItem(), exit);

        about = new MenuItem("About");
        Menu help = new Menu("?", null, about);

        menuBar = new MenuBar(file, help);
    }

    private TableColumn<Share, String> getNameColumn() {
        TableColumn<Share, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(300);
        return nameCol;
    }

    private TableColumn<Share, String> getPathColumn() {
        TableColumn<Share, String> pathCol = new TableColumn<>("Path");
        pathCol.setCellValueFactory(new PropertyValueFactory<>("smbPath"));
        pathCol.setPrefWidth(600);
        return pathCol;
    }

    private TableColumn<Share, Long> getSizeColumn() {
        TableColumn<Share, Long> sizeCol = new TableColumn<>("Size");
        sizeCol.setCellValueFactory(new PropertyValueFactory<>("size"));
        sizeCol.setPrefWidth(150);
        sizeCol.setCellFactory(col -> new TableCell<Share, Long>() {
            @Override
            protected void updateItem(Long item, boolean empty) {
                super.updateItem(item, empty);
                this.setText(null);
                this.setGraphic(null);
                if (!empty) {
                    this.setText(humanReadableByteCount(item, false));
                }
            }
        });
        return sizeCol;
    }

    private TableColumn<Share, LocalDate> getLastModificationColumn() {
        TableColumn<Share, LocalDate> lastMCol = new TableColumn<>("Date");
        lastMCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        lastMCol.setPrefWidth(150);
        return lastMCol;
    }

    private String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) {
            return bytes + " B";
        }
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
        return String.format("%.2f %sB", bytes / Math.pow(unit, exp), pre);
    }
}
