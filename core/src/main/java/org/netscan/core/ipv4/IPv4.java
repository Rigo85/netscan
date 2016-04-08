package org.netscan.core.ipv4;

import com.google.gson.annotations.JsonAdapter;
import org.netscan.core.json.IPv4JsonAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
@JsonAdapter(IPv4JsonAdapter.class)
public class IPv4 {
    private final List<Short> ipNumbers;
    private final long longValue;

    public IPv4(String ip) {
        this(IPAddressUtil.textToNumericFormatV4(ip));
    }

    /**
     * ipv4 = x.y.z.w
     * <p>
     * longValue x * 255^3 + y * 255^2 + z * 255^1 + w * 255^0
     */
    private IPv4(List<Short> tmp) {
        ipNumbers = tmp;

        longValue = IntStream.range(0, ipNumbers.size())
                .boxed()
                .mapToLong(i -> (long) (ipNumbers.get(i) * Math.pow(255, ipNumbers.size() - i - 1)))
                .sum();
    }

    public long toLong() {
        return longValue;
    }

    @Override
    public String toString() {
        return IntStream.range(0, ipNumbers.size())
                .boxed()
                .map(i -> String.valueOf(ipNumbers.get(i))).collect(Collectors.joining("."));
    }

    /**
     * @param inc increment for the ip address
     * @return a new IPv4
     * ipv4 = x.y.z.w
     * <p>
     * w = (w + inc) % 255;
     * z = (z + (inc + w) / 255) % 255;
     * y = (y + (z + (inc + w) / 255) / 255) % 255;
     * x = (x + (y + (z + (inc + w) / 255) / 255) / 255) % 255;
     */
    private IPv4 increment(long inc) {
        List<Short> tmp = new ArrayList<>(4);
        tmp.addAll(ipNumbers);

        long t1 = tmp.get(3) + inc;
        tmp.set(3, (short) (t1 % 255));
        long t2 = t1 / 255;
        long t3 = (tmp.get(2) + t2) / 255;
        tmp.set(2, (short) ((tmp.get(2) + t2) % 255));
        tmp.set(1, (short) ((tmp.get(1) + t3) % 255));
        tmp.set(0, (short) ((tmp.get(0) + (tmp.get(1) + t3) / 255) % 255));

        return new IPv4(tmp);
    }

    public IPv4 increment() {
        return increment(1L);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IPv4)) return false;

        IPv4 iPv4 = (IPv4) o;

        return longValue == iPv4.longValue;
    }

    @Override
    public int hashCode() {
        int result = ipNumbers != null ? ipNumbers.hashCode() : 0;
        result = 31 * result + (int) (longValue ^ (longValue >>> 32));
        return result;
    }
}
