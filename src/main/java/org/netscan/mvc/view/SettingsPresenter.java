package org.netscan.mvc.view;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.netscan.core.configuration.*;
import org.netscan.core.ipv4.IPAddressUtil;
import org.netscan.core.ipv4.IPv4;

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
public class SettingsPresenter {
    private final SettingsView settingsView;
    private final Configuration conf;

    public SettingsPresenter(SettingsView settingsView, Configuration conf) {
        this.settingsView = settingsView;
        this.conf = conf;

        attachEvents();
    }


    private void attachEvents() {
        updateView();

        settingsView.rangesView.addButton.setOnAction(e -> addRange());
        settingsView.rangesView.removeButton.setOnAction(e -> removeRange());

        settingsView.credentialsView.addButton.setOnAction(e -> addCredential());
        settingsView.credentialsView.removeButton.setOnAction(e -> removeCredential());

        settingsView.filtersView.addButton.setOnAction(e -> addFilter());
        settingsView.filtersView.removeButton.setOnAction(e -> removeFilter());

        settingsView.threadsView.threadCount.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            conf.setThreads(settingsView.threadsView.threadCount.getValueFactory().getValue());
            ConfigurationUtil.saveConfiguration(conf);
        });
    }

    private void removeFilter() {
        final Filter selectedItem = (Filter) settingsView.filtersView.listView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            conf.removeFilter(selectedItem);
            ConfigurationUtil.saveConfiguration(conf);
            updateView();
        }
    }

    private void addFilter() {
        Dialog<Filter> dialog = new Dialog<>();
        dialog.setTitle("Add filter");
        dialog.setResizable(true);

        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(
                getClass().getClassLoader().getResource("images/icon.png").toExternalForm()));

        Label label1 = new Label("Filter: ");

        TextField text1 = new TextField();
        text1.setPrefWidth(350);
        text1.setPromptText("*.ext [, *.ext, ...]");

        GridPane grid = new GridPane();
        grid.add(label1, 1, 1);
        grid.add(text1, 2, 1);

        grid.setHgap(10);
        grid.setVgap(10);
        dialog.getDialogPane().setContent(grid);

        ButtonType buttonTypeOk = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);

        Node addButton = dialog.getDialogPane().lookupButton(buttonTypeOk);
        addButton.setDisable(true);

        text1.textProperty().addListener((observable, oldValue, newValue) -> {
            addButton.setDisable(!newValue.trim().matches("\\s*\\*\\.[a-zA-Z0-9]{2,}(\\s*,\\s*\\*\\.[a-zA-Z0-9]{2,})*\\s*"));
        });

        dialog.setResultConverter(b -> {
            if (b == buttonTypeOk) {
                return new Filter(text1.getText().trim().replaceAll("\\s*", "").split(","));
            }
            return null;
        });

        dialog.showAndWait().ifPresent(filter -> {
            conf.addFilter(filter);
            ConfigurationUtil.saveConfiguration(conf);
            updateView();
        });
    }

    private void removeCredential() {
        final Credential selectedItem = (Credential) settingsView.credentialsView.listView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            conf.removeCredential(selectedItem);
            ConfigurationUtil.saveConfiguration(conf);
            updateView();
        }
    }

    private void addCredential() {
        Dialog<Credential> dialog = new Dialog<>();
        dialog.setTitle("Add credential");
        dialog.setResizable(true);

        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(
                getClass().getClassLoader().getResource("images/icon.png").toExternalForm()));

        Label label1 = new Label("Domain: ");
        Label label2 = new Label("Username: ");
        Label label3 = new Label("Password: ");

        TextField text1 = new TextField();
        text1.setPrefWidth(250);
        text1.setPromptText("Domain");
        text1.setText("*");

        TextField text2 = new TextField();
        text2.setPrefWidth(250);
        text2.setPromptText("Username");

        TextField text3 = new TextField();
        text3.setPrefWidth(250);
        text3.setPromptText("Password");

        GridPane grid = new GridPane();
        grid.add(label1, 1, 1);
        grid.add(text1, 2, 1);
        grid.add(label2, 1, 2);
        grid.add(text2, 2, 2);
        grid.add(label3, 1, 3);
        grid.add(text3, 2, 3);
        grid.setHgap(10);
        grid.setVgap(10);
        dialog.getDialogPane().setContent(grid);

        ButtonType buttonTypeOk = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);

        Node addButton = dialog.getDialogPane().lookupButton(buttonTypeOk);
        addButton.setDisable(true);

        text1.textProperty().addListener((observable, oldValue, newValue) -> {
            addButton.setDisable(newValue.trim().isEmpty());
        });

        text2.textProperty().addListener((observable, oldValue, newValue) -> {
            addButton.setDisable(newValue.trim().isEmpty());
        });

        text3.textProperty().addListener((observable, oldValue, newValue) -> {
            addButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.setResultConverter(b -> {
            if (b == buttonTypeOk) {
                return new Credential(text1.getText(), text2.getText(), text3.getText());
            }
            return null;
        });

        dialog.showAndWait().ifPresent(credential -> {
            conf.addCredential(credential);
            ConfigurationUtil.saveConfiguration(conf);
            updateView();
        });
    }

    @SuppressWarnings("unchecked")
    private void updateView() {
        settingsView.rangesView.listView.getItems().clear();
        settingsView.rangesView.listView.getItems().addAll(conf.getRanges());

        settingsView.credentialsView.listView.getItems().clear();
        settingsView.credentialsView.listView.getItems().addAll(conf.getCredentials());

        settingsView.filtersView.listView.getItems().clear();
        settingsView.filtersView.listView.getItems().addAll(conf.getFilters());

        settingsView.threadsView.threadCount.getValueFactory().setValue(conf.getThreads());
    }

    private void removeRange() {
        final Range selectedItem = (Range) settingsView.rangesView.listView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            conf.removeRange(selectedItem);
            ConfigurationUtil.saveConfiguration(conf);
            updateView();
        }
    }

    private void addRange() {
        Dialog<Range> dialog = new Dialog<>();
        dialog.setTitle("Add range");
        dialog.setResizable(true);

        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(
                getClass().getClassLoader().getResource("images/icon.png").toExternalForm()));

        Label label1 = new Label("Begin: ");
        Label label2 = new Label("End: ");
        TextField text1 = new TextField();
        text1.setPrefWidth(250);
        text1.setPromptText("IPv4 to begin the search");
        TextField text2 = new TextField();
        text2.setPrefWidth(250);
        text2.setPromptText("IPv4 to end the search");

        GridPane grid = new GridPane();
        grid.add(label1, 1, 1);
        grid.add(text1, 2, 1);
        grid.add(label2, 1, 2);
        grid.add(text2, 2, 2);
        grid.setHgap(10);
        grid.setVgap(10);
        dialog.getDialogPane().setContent(grid);

        ButtonType buttonTypeOk = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);

        Node addButton = dialog.getDialogPane().lookupButton(buttonTypeOk);
        addButton.setDisable(true);

        text1.textProperty().addListener((observable, oldValue, newValue) -> {
            addButton.setDisable(!IPAddressUtil.isIPv4LiteralAddress(newValue) || !IPAddressUtil.isIPv4LiteralAddress(text2.getText()));
        });

        text2.textProperty().addListener((observable, oldValue, newValue) -> {
            addButton.setDisable(!IPAddressUtil.isIPv4LiteralAddress(newValue) || !IPAddressUtil.isIPv4LiteralAddress(text1.getText()));
        });

        dialog.setResultConverter(b -> {
            if (b == buttonTypeOk) {
                return new Range(new IPv4(text1.getText()), new IPv4(text2.getText()));
            }
            return null;
        });

        dialog.showAndWait().ifPresent(range -> {
            conf.addRange(range);
            ConfigurationUtil.saveConfiguration(conf);
            updateView();
        });
    }
}
