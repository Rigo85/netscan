package org.netscan.mvc.model;

import javafx.concurrent.Task;
import jcifs.smb.SmbFile;
import org.netscan.core.NetScanCore;
import org.netscan.core.ResourceToSearch;
import org.netscan.core.configuration.Configuration;
import org.netscan.core.configuration.Filter;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

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
class SearchTask extends Task<SmbFile> {
    final private Filter filter;
    final private ConcurrentLinkedDeque<SmbFile> files;
    final private LinkedBlockingQueue<ResourceToSearch> queue;
    final private AtomicInteger counter;
    final private Configuration configuration;
    final private AtomicBoolean stop;
    final private CyclicBarrier barrier;
    final private AtomicLong workDone;
    final private AtomicLong totalToProcess;

    SearchTask(Filter filter, Configuration configuration, CyclicBarrier barrier, AtomicBoolean stop) {
        this.filter = filter;
        this.files = new ConcurrentLinkedDeque<>();
        this.queue = new LinkedBlockingQueue<>();
        this.counter = new AtomicInteger(0);
        this.configuration = configuration;
        this.stop = stop;
        this.barrier = barrier;
        this.workDone = new AtomicLong(0);
        this.totalToProcess = new AtomicLong(0);
    }

    @Override
    protected SmbFile call() throws Exception {
        new NetScanCore(queue, files, configuration, filter, counter, stop, workDone, totalToProcess).produceFiles();

        final Thread updater = new Thread(() -> {
            while (true) {
                updateProgress(workDone.get(), totalToProcess.get());
                updateMessage(String.format("%d processed resources of %d | remaining %d files to display",
                        workDone.get(), totalToProcess.get(), files.size()));
                try {
                    if ((counter.get() == 0 && queue.isEmpty() && files.isEmpty()) || stop.get()) return;
                    if (files.isEmpty()) {
                        Thread.sleep(1000);
                    } else {
                        updateValue(files.pop());
                        barrier.await();
                    }
                } catch (InterruptedException | BrokenBarrierException e) {
                }
            }
        });
        updater.start();
        updater.join();

        return null;
    }
}
