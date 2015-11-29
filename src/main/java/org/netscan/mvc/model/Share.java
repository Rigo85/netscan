package org.netscan.mvc.model;

import javafx.beans.property.SimpleLongProperty;
import javafx.concurrent.Task;

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
public class Share extends Task<SimpleLongProperty>{
    private final String name;
    private final String smbPath;
    private final long size;
    private final LocalDate date;

    public Share(String name, String smbPath, long size, LocalDate date) {
        this.name = name;
        this.smbPath = smbPath;
        this.size = size;
        this.date = date;
    }

    @Override
    protected SimpleLongProperty call() throws Exception {
        return null;
    }

    public String getName() {
        return name;
    }

    public String getSmbPath() {
        return smbPath;
    }

    public long getSize() {
        return size;
    }

    public LocalDate getDate() {
        return date;
    }
}
