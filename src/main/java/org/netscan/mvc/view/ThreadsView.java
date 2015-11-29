package org.netscan.mvc.view;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.GridPane;

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
public class ThreadsView extends GridPane {
    Label threadCountLabel;
    Spinner<SimpleIntegerProperty> threadCount;

    public ThreadsView(int max) {
        threadCountLabel = new Label("Max. threads");
        threadCount = new Spinner<>(1, max, 1);
        threadCount.setPrefWidth(60);

        setHgap(10);
        setVgap(10);

        setPadding(new Insets(0, 8, 8, 0));

        add(threadCountLabel, 0, 0);
        add(threadCount, 1, 0);
    }
}
