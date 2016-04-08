package org.netscan.core.configuration;

import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.List;

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
public class Configuration {
    @SerializedName("threads")
    private int threads;

    @SerializedName("timeout")
    private int timeOut;

    @SerializedName("queuesize")
    private int queueSize;

    @SerializedName("credentials")
    private List<Credential> credentials;

    @SerializedName("ranges")
    private List<Range> ranges;

    @SerializedName("filters")
    private List<Filter> filters;

    Configuration() {
        credentials = Collections.emptyList();
        ranges = Collections.emptyList();
        filters = Collections.emptyList();
        threads = 1;
    }

    public List<Credential> getCredentials() {
        return credentials;
    }

    public void setCredentials(List<Credential> credentials) {
        this.credentials = credentials != null ? credentials : Collections.emptyList();
    }

    public List<Range> getRanges() {
        return ranges;
    }

    public void setRanges(List<Range> ranges) {
        this.ranges = ranges != null ? ranges : Collections.emptyList();
    }

    public List<Filter> getFilters() {
        return filters;
    }

    public void setFilters(List<Filter> filters) {
        this.filters = filters != null ? filters : Collections.emptyList();
    }

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads > 50 ? 50 : (threads < 1 ? 1 : threads);
    }

    public int getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut < 50 ? 200 : timeOut;
    }

    public int getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize < 10 ? 10 : queueSize;
    }

    public void addRange(Range range) {
        ranges.add(range);
    }

    public void removeRange(Range range) {
        ranges.remove(range);
    }

    public void addCredential(Credential credential) {
        credentials.add(credential);
    }

    public void removeCredential(Credential credential) {
        credentials.remove(credential);
    }

    public void addFilter(Filter filter) {
        filters.add(filter);
    }

    public void removeFilter(Filter filter) {
        filters.remove(filter);
    }
}
