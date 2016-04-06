package org.netscan.core.ipv4;

import org.netscan.core.configuration.Configuration;
import org.netscan.core.configuration.Range;

import java.util.Iterator;
import java.util.function.Supplier;

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
public class IPv4Supplier implements Supplier<IPv4> {

    public static IPv4 END_THREAD = new IPv4("0.0.0.0");
    public static IPv4 END_IP = new IPv4("0.0.0.0");
    private final Iterator<Range> rangeIt;
    private int countDown;
    private Range range = null;
    private IPv4 ip = null;

    public IPv4Supplier(Configuration conf) {
        rangeIt = conf.getRanges().iterator();
        if (rangeIt.hasNext()) range = rangeIt.next();
        if (range != null) ip = range.beginIP;
        countDown = conf.getThreads();
    }

    @Override
    public IPv4 get() {
        IPv4 result = END_IP;

        if (range != null) {
            if (ip.toLong() <= range.endIP.toLong()) {
                result = ip;
                ip = ip.increment();
            } else if (rangeIt.hasNext()) {
                range = rangeIt.next();
                result = range.beginIP;
                ip = result.increment();
            }
        }

        if (result == END_IP && countDown > 0) {
            result = END_THREAD;
            countDown--;
        }

        return result;
    }
}
