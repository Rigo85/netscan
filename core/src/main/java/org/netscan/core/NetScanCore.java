package org.netscan.core;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import org.netscan.core.configuration.Credential;
import org.netscan.core.configuration.Filter;
import org.netscan.core.ipv4.IPv4;

import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
public class NetScanCore {
    private final IPv4 ip;
    private final Filter filter;
    private final List<Credential> credentials;
    private final int timeOut;

    public NetScanCore(IPv4 ip, Filter filter, List<Credential> credentials, int timeOut) {
        this.ip = ip;
        this.filter = filter;
        this.credentials = credentials;
        this.timeOut = timeOut;
    }

    private boolean isPortsOpen() {
        return Arrays.asList(139, 445).stream().map(p -> {
            try {
                try (Socket socket = new Socket()) {
                    socket.connect(new InetSocketAddress(ip.toString(), p), timeOut);
                    return true;
                }
            } catch (Exception ex) {
                return false;
            }
        }).allMatch(isPortOpen -> isPortOpen);
    }

    public List<SmbFile> produceFiles() {
        List<SmbFile> result = new LinkedList<>();

        if (isPortsOpen()) {
            result.addAll(credentials.stream().flatMap(c -> {
                String domain = c.getDomain().equals("*") ? ip.toString() : c.getDomain();
                NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(domain, c.getUserName(), c.getPassword());
                String url = String.format("smb://%s/", ip);
                List<SmbFile> files;

                try {
                    files = walk(new SmbFile(url, auth));
                } catch (MalformedURLException ignored) {
                    files = Collections.emptyList();
                }

                return files.stream();
            }).collect(Collectors.toList()));
        }

        return result;
    }

    private List<SmbFile> walk(SmbFile sf) {
        List<SmbFile> result = new LinkedList<>();

        try {
            if (sf.isFile() && (filter.get(0).equals("*.*") ||
                    filter.stream().anyMatch(f -> sf.getName().endsWith(f.substring(1))))) {
                result.add(sf);
            } else if (sf.isDirectory()) {
                result.addAll(Stream.of(sf.listFiles()).flatMap(s -> walk(s).stream()).collect(Collectors.toList()));
            }
        } catch (Exception ignored) {
        }

        return result;
    }
}
