package org.netscan.mvc.view;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.netscan.mvc.model.Filter;

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
    private final NetScanView netScanView;

    public NetScanPresenter(NetScanView netScanView) {

        this.netScanView = netScanView;

        attachEvents();
    }

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
        netScanView.filterComboBox.getItems().addAll(new Filter("*.mpg", "*.avi"));
    }

    private void settingsAction() {
        SettingsView settingsView = new SettingsView();
        new SettingsPresenter(settingsView);

        Dialog dialog = new Dialog();
        dialog.setTitle("Settings");
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setResizable(true);

        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(
                getClass().getClassLoader().getResource("images/icon.png").toExternalForm()));

        ButtonType reloadButtonType = new ButtonType("Reload", ButtonBar.ButtonData.OTHER);
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OTHER);

        dialog.getDialogPane().getButtonTypes().addAll(reloadButtonType, saveButtonType, ButtonType.CLOSE);
        dialog.getDialogPane().setContent(settingsView);
        dialog.getDialogPane().setPrefWidth(500);

        dialog.getDialogPane().getScene().addEventFilter(KeyEvent.ANY, e -> {
            if (e.getCode() == KeyCode.ESCAPE) e.consume();
        });

        dialog.showAndWait();
    }


}
