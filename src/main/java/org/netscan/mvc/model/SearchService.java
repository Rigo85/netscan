package org.netscan.mvc.model;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import jcifs.smb.SmbFile;
import org.netscan.core.configuration.Configuration;
import org.netscan.core.configuration.Filter;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

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
public class SearchService extends Service<List<SmbFile>> {

    private final Configuration conf;
    private final BlockingQueue<CountDownLatch> continueQueue;
    private Filter filter;

    public SearchService(Configuration conf) {
        this.conf = conf;
        filter = null;
        continueQueue = new LinkedBlockingQueue<>();
    }

    @Override
    protected Task<List<SmbFile>> createTask() {
        return new SearchTask(conf, filter, continueQueue);
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public void canContinue() {
        final CountDownLatch peek = continueQueue.peek();
        if (peek != null) {
            peek.countDown();
        }
    }
}
