package org.netscan.core;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import org.netscan.core.configuration.Credential;
import org.netscan.core.configuration.Filter;

import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Author Rigoberto Leander Salgado Reyes <rlsalgado2006@gmail.com>
 * <p>
 * Copyright 2016 by Rigoberto Leander Salgado Reyes.
 * <p>
 * This program is licensed to you under the terms of version 3 of the
 * GNU Affero General Public License. This program is distributed WITHOUT
 * ANY EXPRESS OR IMPLIED WARRANTY, INCLUDING THOSE OF NON-INFRINGEMENT,
 * MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE. Please refer to the
 * AGPL (http:www.gnu.org/licenses/agpl-3.0.txt) for more details.
 */
class IpToSearch extends ResourceToSearch {
    final private String ip;
    final private List<Credential> credentials;

    IpToSearch(LinkedBlockingQueue<ResourceToSearch> queue, ConcurrentLinkedDeque<SmbFile> files,
               Filter filter, int timeOut, AtomicInteger counter, String ip, List<Credential> credentials,
               AtomicLong workDone, AtomicLong totalToProcess) {
        super(files, queue, filter, timeOut, counter, workDone, totalToProcess);

        this.ip = ip;
        this.credentials = credentials;
    }

    private boolean isPortsOpen() {
        return Arrays.asList(139, 445).stream().map(p -> {
            try {
                try (Socket socket = new Socket()) {
                    socket.connect(new InetSocketAddress(ip, p), timeOut);
                    return true;
                }
            } catch (Exception ex) {
                return false;
            }
        }).allMatch(isPortOpen -> isPortOpen);
    }

    @Override
    public void process() {
        if (isPortsOpen()) {
            credentials.forEach(credential -> {
                String domain = credential.getDomain().equals("*") ? ip : credential.getDomain();
                NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(domain, credential.getUserName(), credential.getPassword());
                String url = String.format("smb://%s/", ip);
                try {
                    Arrays.stream(new SmbFile(url, auth).listFiles())
                            .filter(this::isDirectory)
                            .filter(smbFile -> !smbFile.getName().equals("IPC$/"))
                            .forEach(this::spooling);
                } catch (MalformedURLException | SmbException e) {
                }
            });
        }
    }
}
