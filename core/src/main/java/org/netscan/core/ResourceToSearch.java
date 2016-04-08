package org.netscan.core;

import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import org.netscan.core.configuration.Filter;

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
public abstract class ResourceToSearch implements Runnable {
    final Filter filter;
    final ConcurrentLinkedDeque<SmbFile> files;
    final int timeOut;
    final private LinkedBlockingQueue<ResourceToSearch> queue;
    final private AtomicInteger counter;
    final private AtomicLong workDone;
    final private AtomicLong totalToProcess;

    ResourceToSearch(ConcurrentLinkedDeque<SmbFile> files, LinkedBlockingQueue<ResourceToSearch> queue,
                     Filter filter, int timeOut, AtomicInteger counter, AtomicLong workDone, AtomicLong totalToProcess) {
        this.files = files;
        this.queue = queue;
        this.filter = filter;
        this.timeOut = timeOut;
        this.counter = counter;
        this.workDone = workDone;
        this.totalToProcess = totalToProcess;
    }

    boolean isDirectory(SmbFile smbFile) {
        try {
            return smbFile.isDirectory();
        } catch (SmbException e) {
            return false;
        }
    }

    void spooling(SmbFile smbFile) {
        try {
            queue.put(new PlaceToSearch(files, queue, filter, counter, smbFile, workDone, totalToProcess));
            totalToProcess.incrementAndGet();
        } catch (InterruptedException e) {
        }
    }

    abstract protected void process();

    @Override
    public void run() {
        counter.incrementAndGet();
        process();
        counter.decrementAndGet();
        workDone.incrementAndGet();
    }
}
