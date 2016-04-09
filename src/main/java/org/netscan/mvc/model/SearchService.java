package org.netscan.mvc.model;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import jcifs.smb.SmbFile;
import org.netscan.core.configuration.Configuration;
import org.netscan.core.configuration.Filter;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;

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
public class SearchService extends Service<SmbFile> {
    final private Configuration configuration;
    final private CyclicBarrier barrier;
    private Filter filter;
    final private AtomicBoolean stop;

    public SearchService(Configuration configuration, CyclicBarrier barrier, AtomicBoolean stop) {
        this.configuration = configuration;
        filter = null;
        this.barrier = barrier;
        this.stop = stop;
    }

    @Override
    protected Task<SmbFile> createTask() {
        return new SearchTask(filter, configuration, barrier, stop);
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }
}
