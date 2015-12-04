package org.netscan.core.ipv4;

import java.util.concurrent.BlockingQueue;
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
public class IPv4Consumer<T> {
    private final BlockingQueue<T> queue;

    public IPv4Consumer(BlockingQueue<T> queue) {
        this.queue = queue;
    }

    public void consume(Consumer<T> consumer) {
        try {
            final T take = queue.take();
            consumer.accept(take);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
