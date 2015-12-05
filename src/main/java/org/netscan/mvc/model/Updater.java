package org.netscan.mvc.model;

import jcifs.smb.SmbFile;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

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
public class Updater implements Runnable, Consumer<List<SmbFile>> {

    private final BlockingQueue<CountDownLatch> continueQueue;
    private final SearchTask searchTask;
    final BlockingQueue<List<SmbFile>> queue;
    private boolean stop;

    public Updater(BlockingQueue<CountDownLatch> continueQueue, SearchTask searchTask) {
        this.continueQueue = continueQueue;
        this.searchTask = searchTask;
        queue = new LinkedBlockingQueue<>();
        stop = false;
    }

    @Override
    synchronized public void accept(List<SmbFile> smbFiles) {
        searchTask.updateProgress();
        if (!smbFiles.isEmpty()) {
            queue.add(smbFiles);
            try {
                continueQueue.put(new CountDownLatch(1));
            } catch (InterruptedException ignored) {
            }
        }
    }

    @Override
    public void run() {
        while (!stop || queue.peek() != null) {
            if (queue.peek() != null) {
                try {
                    searchTask.update(queue.take());

                    final CountDownLatch peek = continueQueue.peek();
                    if (peek != null) {
                        peek.await();
                    }

                    continueQueue.take();
                } catch (InterruptedException ignored) {
                }
            }
        }
    }

    public void stop() {
        stop = true;
    }
}
