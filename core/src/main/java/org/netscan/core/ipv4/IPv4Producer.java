package org.netscan.core.ipv4;

import java.util.concurrent.BlockingQueue;
import java.util.function.Supplier;

import static org.netscan.core.ipv4.IPv4Supplier.*;

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
public class IPv4Producer<T> {
    private final BlockingQueue<T> queue;

    public IPv4Producer(BlockingQueue<T> queue) {
        this.queue = queue;
    }

    public void produce(Supplier<T> supplier) {
        T ip = supplier.get();

        while (ip != END_IP) {
            try {
                queue.put(ip);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            ip = supplier.get();
        }
    }
}
