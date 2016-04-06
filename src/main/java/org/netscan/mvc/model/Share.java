package org.netscan.mvc.model;

import javafx.beans.property.SimpleLongProperty;
import javafx.concurrent.Task;
import jcifs.smb.NtlmPasswordAuthentication;

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
public class Share extends Task<SimpleLongProperty> {
    private final String name;
    private final String smbPath;
    private final long size;
    private final LocalDate date;
    private final NtlmPasswordAuthentication auth;

    public Share(String name, String smbPath, long size, LocalDate date, NtlmPasswordAuthentication auth) {
        this.name = name;
        this.smbPath = smbPath;
        this.size = size;
        this.date = date;
        this.auth = auth;
    }

    @Override
    protected SimpleLongProperty call() throws Exception {
        return null;
    }

    private String getName() {
        return name;
    }

    public String getSmbPath() {
        return smbPath;
    }

    private long getSize() {
        return size;
    }

    private LocalDate getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Share{" +
                "name='" + name + '\'' +
                ", smbPath='" + smbPath + '\'' +
                ", size=" + size +
                ", date=" + date +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Share)) return false;

        Share share = (Share) o;

        return getSize() == share.getSize() &&
                !(getName() != null ? !getName().equals(share.getName()) : share.getName() != null) &&
                !(getSmbPath() != null ? !getSmbPath().equals(share.getSmbPath()) : share.getSmbPath() != null) &&
                !(getDate() != null ? !getDate().equals(share.getDate()) : share.getDate() != null);
    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (getSmbPath() != null ? getSmbPath().hashCode() : 0);
        result = 31 * result + (int) (getSize() ^ (getSize() >>> 32));
        result = 31 * result + (getDate() != null ? getDate().hashCode() : 0);
        return result;
    }

    public NtlmPasswordAuthentication getAuth() {
        return auth;
    }
}
