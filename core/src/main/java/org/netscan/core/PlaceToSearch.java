package org.netscan.core;

import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import org.netscan.core.configuration.Filter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;


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
class PlaceToSearch extends ResourceToSearch {
    private final SmbFile smbFile;

    PlaceToSearch(ConcurrentLinkedDeque<SmbFile> files, LinkedBlockingQueue<ResourceToSearch> queue,
                  Filter filter, AtomicInteger counter, SmbFile smbFile,
                  AtomicLong workDone, AtomicLong totalToProcess) {
        super(files, queue, filter, 0, counter, workDone, totalToProcess);

        this.smbFile = smbFile;
    }

    @Override
    public void process() {
        try {
            final Map<Boolean, List<SmbFile>> groups = Arrays.stream(smbFile.listFiles())
                    .collect(Collectors.partitioningBy(this::isDirectory));

            boolean greedyFilter = filter.get(0).equals("*.*");

            files.addAll(groups.get(Boolean.FALSE)
                    .stream()
                    .filter(sf -> greedyFilter || filter.stream().anyMatch(f -> sf.getName().endsWith(f.substring(1))))
                    .collect(Collectors.toList()));

            groups.get(Boolean.TRUE).stream().forEach(this::spooling);

        } catch (SmbException e) {
        }
    }
}
