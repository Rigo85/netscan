package org.netscan.mvc.view;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

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
public class SpinButton extends HBox {
    TextField valueTextField;
    SimpleIntegerProperty min;
    SimpleIntegerProperty max;
    Pagination pagination;

    public SpinButton(int min, int max) {
        this(new SimpleIntegerProperty(min), new SimpleIntegerProperty(max));
    }

    public SpinButton(SimpleIntegerProperty min, SimpleIntegerProperty max) {
        this.min = min;
        this.max = max;

        valueTextField = new TextField();
        valueTextField.setEditable(false);

        pagination = new Pagination(max.get() - min.get(), min.get());
        getChildren().addAll(valueTextField, pagination);
    }
}
