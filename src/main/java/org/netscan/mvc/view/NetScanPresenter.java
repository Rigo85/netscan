package org.netscan.mvc.view;


import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Worker;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import org.netscan.core.configuration.Configuration;
import org.netscan.core.configuration.ConfigurationUtil;
import org.netscan.core.configuration.Filter;
import org.netscan.mvc.model.SearchService;
import org.netscan.mvc.model.Share;

import java.nio.file.Paths;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;


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
public class NetScanPresenter {
    final SettingsView settingsView;
    final SearchService searchService;
    private final NetScanView netScanView;
    private final Configuration conf;
    private final ObservableList<Share> list;

    public NetScanPresenter(NetScanView netScanView) {
        this.netScanView = netScanView;

        conf = ConfigurationUtil.loadConfiguration();

        settingsView = new SettingsView();
        new SettingsPresenter(settingsView, conf);

        searchService = new SearchService(conf);

        list = FXCollections.observableArrayList();

        attachEvents();
    }

    @SuppressWarnings("unchecked")
    private void attachEvents() {
        netScanView.settings.setOnAction(e -> settingsAction());

        netScanView.about.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.OK);
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            alert.setResizable(true);
            alert.getDialogPane().setPrefSize(600, 300);
            stage.getIcons().add(new Image(getClass().getClassLoader().getResource("images/icon.png").toExternalForm()));
            alert.setHeaderText("NetScan v0.1");
            alert.setContentText("Author Rigoberto Leander Salgado Reyes <rlsalgado2006@gmail.com>" +
                    "\n\n" +
                    "Copyright 2015 by Rigoberto Leander Salgado Reyes." +
                    "\n" +
                    "This program is licensed to you under the terms of version 3 of the\n" +
                    "GNU Affero General Public License. This program is distributed WITHOUT\n" +
                    "ANY EXPRESS OR IMPLIED WARRANTY, INCLUDING THOSE OF NON-INFRINGEMENT,\n" +
                    "MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE. Please refer to the\n" +
                    "AGPL (http:www.gnu.org/licenses/agpl-3.0.txt) for more details.");
            alert.setTitle("About Dialog");
            alert.showAndWait();
        });

        netScanView.exit.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit?", ButtonType.YES, ButtonType.CANCEL);
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(getClass().getClassLoader().getResource("images/icon.png").toExternalForm()));
            alert.setTitle("Confirmation Dialog");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.YES) Platform.exit();
        });

        netScanView.filterComboBox.itemsProperty().bind(settingsView.filtersView.listView.itemsProperty());

        netScanView.searchButton.setOnAction(e -> searchEvent());
        netScanView.searchButton.disableProperty().bind(searchService.stateProperty().isEqualTo(Worker.State.RUNNING));

        netScanView.stopButton.setOnAction(e -> searchService.cancel());
        netScanView.stopButton.disableProperty().bind(searchService.stateProperty().isNotEqualTo(Worker.State.RUNNING));

        netScanView.searchTextField.disableProperty().bind(searchService.stateProperty().isEqualTo(Worker.State.RUNNING));
        netScanView.searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                final FilteredList<Share> filtered =
                        list.filtered(dt -> dt.getSmbPath().toLowerCase().contains(newValue.trim().toLowerCase()));
                netScanView.tableView.setItems(filtered);
            } else {
                netScanView.tableView.setItems(list);
            }
        });

        if (System.getProperty("os.name").contains("Windows")) {
            netScanView.openFileManager.setDisable(true);
        } else {
            netScanView.openFileManager.setOnAction(e -> openFileManagerAction());
        }

        netScanView.credentials.setOnAction(e -> credentialsAction());

        netScanView.progressBar.progressProperty().bind(searchService.progressProperty());
    }

    private void credentialsAction() {
        final Share share = netScanView.tableView.getSelectionModel().getSelectedItem();

        if (share != null) {
            final NtlmPasswordAuthentication auth = share.getAuth();

            Dialog dialog = new Dialog();
            dialog.setTitle("Credentials");
            dialog.setResizable(true);

            Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(
                    getClass().getClassLoader().getResource("images/icon.png").toExternalForm()));

            Label label1 = new Label("Domain: ");
            Label label2 = new Label("Username: ");
            Label label3 = new Label("Password: ");

            TextField text1 = new TextField();
            text1.setPrefWidth(250);
            text1.setText(auth.getDomain());
            text1.setEditable(false);

            TextField text2 = new TextField();
            text2.setPrefWidth(250);
            text2.setText(auth.getUsername());
            text2.setEditable(false);

            TextField text3 = new TextField();
            text3.setPrefWidth(250);
            text3.setText(auth.getPassword());
            text3.setEditable(false);

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

            dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

            dialog.showAndWait();
        }
    }

    private void openFileManagerAction() {
        final Share share = netScanView.tableView.getSelectionModel().getSelectedItem();

        if (share != null) {
            String path = Paths.get(share.getSmbPath()).getParent().toString().replace("smb:/", "smb://");
            String fManager = "xdg-open";
            String comm = String.format("%s %s", fManager, path);

            Platform.runLater(() -> {
                try {
                    Process process = Runtime.getRuntime().exec(comm);
                    while (process.exitValue() != 0) {
                        process = Runtime.getRuntime().exec(comm);
                    }
                } catch (Exception ignored) {
                }
            });
        }
    }

    private void searchEvent() {
        netScanView.searchTextField.setText("");
        netScanView.tableView.getItems().clear();
        final Filter filter = netScanView.filterComboBox.getValue();
        if (!conf.getCredentials().isEmpty() && filter != null && !conf.getRanges().isEmpty()) {
            if (searchService.getState() == Worker.State.READY) {
                searchService.valueProperty().addListener((observable, oldValue, smbFiles) -> {
                    if (smbFiles != null && !smbFiles.isEmpty()) {
                        smbFiles.stream().forEach(sf -> {
                            try {
                                final Share share = new Share(sf.getName(), sf.getCanonicalPath(), sf.length(),
                                        new Date(sf.getDate()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                                        (NtlmPasswordAuthentication) sf.getPrincipal());
                                netScanView.tableView.getItems().add(share);
                            } catch (SmbException ignored) {
                            }
                        });

                        searchService.canContinue();
                    }
                });
            }

            if (searchService.getState() == Worker.State.SUCCEEDED ||
                    searchService.getState() == Worker.State.FAILED ||
                    searchService.getState() == Worker.State.CANCELLED) searchService.reset();

            searchService.setFilter(filter);
            searchService.setOnSucceeded(e -> onSuccessSearch());
            searchService.setOnCancelled(e -> onCancelSearch());
            searchService.start();
        }
    }

    private void onCancelSearch() {
        list.clear();
        list.addAll(netScanView.tableView.getItems());
    }

    private void onSuccessSearch() {
        list.clear();
        list.addAll(netScanView.tableView.getItems());
    }

    private void settingsAction() {
        Dialog dialog = new Dialog();
        dialog.setTitle("Settings");
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setResizable(true);

        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(
                getClass().getClassLoader().getResource("images/icon.png").toExternalForm()));

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);

        dialog.getDialogPane().setContent(settingsView);
        dialog.getDialogPane().setPrefWidth(500);

        dialog.getDialogPane().getScene().addEventFilter(KeyEvent.ANY, e -> {
            if (e.getCode() == KeyCode.ESCAPE) e.consume();
        });

        dialog.showAndWait();
    }
}
