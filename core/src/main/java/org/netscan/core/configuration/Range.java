package org.netscan.core.configuration;

import com.google.gson.annotations.SerializedName;
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
public class Range {
    @SerializedName("beginIP")
    public IPv4 beginIP;

    @SerializedName("endIP")
    public IPv4 endIP;

    public Range() {
    }

    public Range(IPv4 beginIP, IPv4 endIP) {
        this.beginIP = beginIP;
        this.endIP = endIP;
    }

    public void setBeginIP(IPv4 beginIP) {
        this.beginIP = beginIP;
    }

    public void setEndIP(IPv4 endIP) {
        this.endIP = endIP;
    }

    @Override
    public String toString() {
        return String.format("%s - %s", beginIP, endIP);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Range)) return false;

        Range range = (Range) o;

        return beginIP != null ? beginIP.equals(range.beginIP) : range.beginIP == null &&
                (endIP != null ? endIP.equals(range.endIP) : range.endIP == null);
    }

    @Override
    public int hashCode() {
        int result = beginIP != null ? beginIP.hashCode() : 0;
        result = 31 * result + (endIP != null ? endIP.hashCode() : 0);
        return result;
    }
}
