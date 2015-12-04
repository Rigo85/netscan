package org.netscan.mvc.view;

import javafx.geometry.Insets;
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
public class SettingsView extends VBox {
    final ThreadsView threadsView;
    final GenericView rangesView;
    final GenericView credentialsView;
    final GenericView filtersView;

    public SettingsView() {
        threadsView = new ThreadsView(50);

        rangesView = new GenericView("Ranges");
        VBox.setMargin(rangesView, new Insets(0, 0, 8, 0));

        credentialsView = new GenericView("Credentials");
        VBox.setMargin(credentialsView, new Insets(0, 0, 8, 0));

        filtersView = new GenericView("Filters");
        VBox.setMargin(filtersView, new Insets(0, 0, 8, 0));

        getChildren().addAll(threadsView, rangesView, credentialsView, filtersView);
    }
}
