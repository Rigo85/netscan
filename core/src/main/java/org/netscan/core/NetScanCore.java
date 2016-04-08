package org.netscan.core;

import jcifs.smb.SmbFile;
import org.netscan.core.configuration.Configuration;
import org.netscan.core.configuration.Filter;
import org.netscan.core.configuration.Range;
import org.netscan.core.ipv4.IPv4;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
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
    final private AtomicBoolean stop;
    final private Filter filter;
    final private ConcurrentLinkedDeque<SmbFile> files;
    final private LinkedBlockingQueue<ResourceToSearch> queue;
    final private ExecutorService threadPool;
    final private AtomicInteger counter;
    final private Configuration configuration;
    final private ResourceToSearch KILL;
    final private AtomicLong workDone;
    final private AtomicLong totalToProcess;

    public NetScanCore(LinkedBlockingQueue<ResourceToSearch> queue, ConcurrentLinkedDeque<SmbFile> files,
                       Configuration configuration, Filter filter, AtomicInteger counter, AtomicBoolean stop,
                       AtomicLong workDone, AtomicLong totalToProcess) {
        this.filter = filter;
        this.configuration = configuration;
        this.threadPool = Executors.newFixedThreadPool(configuration.getThreads());
        this.files = files;
        this.queue = queue;
        this.counter = counter;
        this.stop = stop;
        this.workDone = workDone;
        this.totalToProcess = totalToProcess;
        this.KILL = new ResourceToSearch(files, queue, filter, configuration.getTimeOut(),
                counter, workDone, totalToProcess) {
            @Override
            protected void process() {

            }
        };
    }

    public void produceFiles() {
        //todo test inside the thread.
        feedQueue();

        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                    if ((counter.get() == 0 && queue.isEmpty()) || stop.get()) {
                        if (!stop.get()) {
                            threadPool.shutdown();
                        } else {
                            threadPool.awaitTermination(1, TimeUnit.MILLISECONDS);
                            queue.clear();
                        }
                        queue.put(KILL);
                        return;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        threadPool.submit(() -> {
            while (true) {
                try {
                    ResourceToSearch resourceToSearch = queue.take();
                    if (resourceToSearch == KILL) {
                        return;
                    }
                    threadPool.execute(resourceToSearch);
                } catch (InterruptedException e) {
                }
            }
        });
    }

    private Stream<String> getIps(Range range) {
        List<String> ipStrings = new LinkedList<>();
        IPv4 ip = range.beginIP;
        final long end = range.endIP.toLong();

        while (ip.toLong() <= end) {
            ipStrings.add(ip.toString());
            ip = ip.increment();
        }

        return ipStrings.stream();
    }

    private void spooling(String ip) {
        try {
            queue.put(new IpToSearch(queue, files, filter, configuration.getTimeOut(), counter,
                    ip, configuration.getCredentials(), workDone, totalToProcess));
            totalToProcess.incrementAndGet();
        } catch (InterruptedException e) {
        }
    }

    private void feedQueue() {
        configuration.getRanges()
                .stream()
                .flatMap(this::getIps)
                .forEach(this::spooling);
    }
}
