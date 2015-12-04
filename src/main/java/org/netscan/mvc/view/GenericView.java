package org.netscan.mvc.view;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

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
public class GenericView extends BorderPane {
    final ListView listView;
    final VBox buttonBar;
    final Button addButton;
    final Button removeButton;

    public GenericView(String title) {
        listView = new ListView<>();
        listView.setMaxHeight(100);
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        addButton = new Button("Add");
        addButton.setMaxWidth(Double.MAX_VALUE);
        VBox.setMargin(addButton, new Insets(0, 0, 8, 8));

        removeButton = new Button("Remove");
        removeButton.setMaxWidth(Double.MAX_VALUE);
        VBox.setMargin(removeButton, new Insets(0, 0, 8, 8));

        buttonBar = new VBox(5, addButton, removeButton);

        setTop(new Label(title));

        setCenter(listView);
        setRight(buttonBar);
    }
}