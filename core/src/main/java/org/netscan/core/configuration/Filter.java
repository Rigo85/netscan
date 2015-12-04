package org.netscan.core.configuration;

import com.google.gson.annotations.JsonAdapter;
import org.netscan.core.json.FilterJsonAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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
@JsonAdapter(FilterJsonAdapter.class)
public class Filter {
    private final List<String> filters;

    public Filter(String... args) {
        filters = Arrays.asList(args);
    }

    public Filter(Collection<? extends String> c) {
        filters = new ArrayList<>();
        filters.addAll(c);
    }

    @Override
    public String toString() {
        AtomicInteger pos = new AtomicInteger(0);
        final int size = 10;
        return IntStream.generate(() -> pos.getAndAdd(size)).limit(filters.size() / size + (filters.size() % size != 0 ? 1 : 0))
                .boxed()
                .map(i -> filters.subList(i, i + size > filters.size() ? filters.size() : i + size))
                .map(l -> l.stream().collect(Collectors.joining(", ")))
                .collect(Collectors.joining(System.getProperty("line.separator")));
    }

    public String get(int i) {
        return filters.get(i);
    }

    public Stream<String> stream() {
        return filters.stream();
    }

    public void add(String str) {
        filters.add(str);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Filter)) return false;

        Filter filter = (Filter) o;

        return !(filters != null ? !filters.equals(filter.filters) : filter.filters != null);
    }

    @Override
    public int hashCode() {
        return filters != null ? filters.hashCode() : 0;
    }
}
