package org.netscan.mvc.model;

import javafx.concurrent.Task;
import jcifs.smb.SmbFile;
import org.netscan.core.configuration.Configuration;
import org.netscan.core.configuration.Filter;
import org.netscan.core.configuration.Range;
import org.netscan.core.ipv4.IPv4;
import org.netscan.core.ipv4.IPv4Supplier;
import org.netscan.core.workers.Worker;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.netscan.core.ipv4.IPv4Supplier.END_IP;
import static org.netscan.core.ipv4.IPv4Supplier.END_THREAD;

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
class SearchTask extends Task<List<SmbFile>> {
    private final BlockingQueue<IPv4> queue;
    private final Configuration conf;
    private final Filter filter;
    private final BlockingQueue<CountDownLatch> continueQueue;
    private final AtomicBoolean stopProducer;
    private final AtomicLong workDone;
    private final AtomicLong totalToProcess;

    SearchTask(Configuration conf, Filter filter, BlockingQueue<CountDownLatch> continueQueue) {
        this.conf = conf;
        this.filter = filter;
        queue = new ArrayBlockingQueue<>(conf.getQueueSize());
        this.continueQueue = continueQueue;
        workDone = new AtomicLong(0L);
        stopProducer = new AtomicBoolean(false);
        totalToProcess = new AtomicLong(IPCount());
    }

    private long IPCount() {
        return conf.getRanges().stream().collect(Collectors.summarizingLong(Range::ipCount)).getSum();
    }

    @Override
    protected List<SmbFile> call() throws Exception {
        startProducer();
        startConsumer(conf.getThreads());

        return Collections.emptyList();
    }

    private void startConsumer(int threads) {
        CountDownLatch barrier = new CountDownLatch(threads);

        Updater updater = new Updater(continueQueue, this);

        new Thread(updater).start();

        IntStream.range(0, threads)
                .boxed()
                .map(i -> new Thread(new Worker(barrier, queue, filter, conf, updater)))
                .forEach(Thread::start);

        try {
            barrier.await();
        } catch (InterruptedException ignored) {
        }

        updater.stop();
    }

    private void startProducer() {
        final IPv4Supplier supplier = new IPv4Supplier(conf);

        new Thread(() -> {
            IPv4 ip = supplier.get();
            while (ip != END_IP && !stopProducer.get()) {
                try {
                    queue.put(ip);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                ip = supplier.get();
            }

            if (stopProducer.get()) {
                queue.clear();
                IntStream.range(0, conf.getThreads()).forEach(value -> {
                    try {
                        queue.put(END_THREAD);
                    } catch (InterruptedException ignored) {
                    }
                });
            }
        }).start();
    }

    void update(List<SmbFile> smbFiles) {
        updateValue(smbFiles);
    }

    void updateProgress() {
        updateProgress(workDone.incrementAndGet(), totalToProcess.get());
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        stopProducer.set(true);
        return super.cancel(mayInterruptIfRunning);
    }
}
