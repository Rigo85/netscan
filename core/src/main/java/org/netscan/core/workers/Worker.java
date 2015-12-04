package org.netscan.core.workers;

import jcifs.smb.SmbFile;
import org.netscan.core.NetScanCore;
import org.netscan.core.configuration.Configuration;
import org.netscan.core.configuration.Filter;
import org.netscan.core.ipv4.IPv4;
import org.netscan.core.ipv4.IPv4Consumer;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

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
public class Worker implements Runnable {

    private final CountDownLatch master;
    private final BlockingQueue<IPv4> queue;
    private final Filter filter;
    private final Configuration conf;
    private final Consumer<List<SmbFile>> consumer;
    private final AtomicBoolean isDone;

    public Worker(CountDownLatch master, BlockingQueue<IPv4> queue, Filter filter, Configuration conf,
                  Consumer<List<SmbFile>> consumer) {
        this.master = master;
        this.queue = queue;
        this.filter = filter;
        this.conf = conf;
        this.consumer = consumer;
        isDone = new AtomicBoolean(false);
    }

    @Override
    public void run() {
        final IPv4Consumer<IPv4> iPv4Consumer = new IPv4Consumer<>(queue);

        while (!isDone.get()) {
            iPv4Consumer.consume(ip -> {
                if (ip == END_THREAD)
                    isDone.set(true);
                else
                    consumer.accept(new NetScanCore(ip, filter, conf.getCredentials(), conf.getTimeOut()).produceFiles());
            });
        }

        master.countDown();
    }
}
