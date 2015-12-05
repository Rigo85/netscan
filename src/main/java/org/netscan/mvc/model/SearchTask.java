package org.netscan.mvc.model;

import javafx.concurrent.Task;
import jcifs.smb.SmbFile;
import org.netscan.core.configuration.Configuration;
import org.netscan.core.configuration.Filter;
import org.netscan.core.configuration.Range;
import org.netscan.core.ipv4.IPv4;
import org.netscan.core.ipv4.IPv4Producer;
import org.netscan.core.ipv4.IPv4Supplier;
import org.netscan.core.workers.Worker;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
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
public class SearchTask extends Task<List<SmbFile>> {
    private final BlockingQueue<IPv4> queue;
    private final Configuration conf;
    private final Filter filter;
    private final BlockingQueue<CountDownLatch> continueQueue;
    private final long ipCount;
    private long workDone;

    public SearchTask(Configuration conf, Filter filter, BlockingQueue<CountDownLatch> continueQueue) {
        this.conf = conf;
        this.filter = filter;
        queue = new ArrayBlockingQueue<>(conf.getQueueSize());
        this.continueQueue = continueQueue;
        ipCount = IPCount();
        workDone = 0L;
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
        new Thread(() -> {
            new IPv4Producer<>(queue).produce(new IPv4Supplier(conf));
        }).start();
    }

    public void update(List<SmbFile> smbFiles) {
        updateValue(smbFiles);
    }

    public void updateProgress() {
        updateProgress(++workDone, ipCount);
    }
}
