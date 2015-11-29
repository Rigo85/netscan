package org.netscan.mvc.view;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.netscan.mvc.model.Filter;
import org.netscan.mvc.model.Share;

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
    MenuBar menuBar;
    Menu file;
    MenuItem settings;
    MenuItem exit;
    Menu help;
    MenuItem about;
    HBox toolBar;
    Label filterLabel;
    ComboBox<Filter> filterComboBox;
    Button searchButton;
    TableView<Share> tableView;
    VBox centerPanel;

    public NetScanView() {
        createMenu();
        createToolbar();
        createCenterPanel();
        setTop(menuBar);
        setCenter(centerPanel);
    }

    private void createCenterPanel() {
        tableView = new TableView<>();
        centerPanel = new VBox(toolBar, tableView);
        VBox.setMargin(toolBar, new Insets(8, 8, 8, 8));
        VBox.setMargin(tableView, new Insets(0, 8, 8, 8));
        VBox.setVgrow(tableView, Priority.ALWAYS);
    }

    private void createToolbar() {
        filterLabel = new Label("Search files");
        filterComboBox = new ComboBox<>();
        filterComboBox.setPrefWidth(500);
        searchButton = new Button(null, new ImageView(new Image(
                getClass().getClassLoader().getResource("images/search.png").toExternalForm())));
        toolBar = new HBox(8, filterLabel, filterComboBox, searchButton);
        HBox.setMargin(filterLabel, new Insets(5, 0, 5, 0));
    }

    private void createMenu() {
        settings = new MenuItem("Settings");
        exit = new MenuItem("Exit");
        file = new Menu("File", null, settings, new SeparatorMenuItem(), exit);

        about = new MenuItem("About");
        help = new Menu("?", null, about);

        menuBar = new MenuBar(file, help);
    }
}
