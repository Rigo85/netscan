package org.netscan.mvc;

import org.netscan.mvc.model.IPv4;
import org.netscan.mvc.model.Range;

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
public class Test {
    public static void main(String... args) {
//        System.out.println(new IPv4("10.8.15.19"));
//        System.out.println(new IPv4("10.8.14.19").increment(130050));

//        System.out.println(new Gson().toJson(new IPv4("10.8.14.19")));
//        System.out.println(new Gson().fromJson("{\"ip\":\"10.8.14.19\"}", IPv4.class));
        System.out.println(new Range(new IPv4("10.8.14.0"), new IPv4("10.8.14.255")));
//        System.out.println(Arrays.toString("10.8.14.19".split("\\.")));
    }
}