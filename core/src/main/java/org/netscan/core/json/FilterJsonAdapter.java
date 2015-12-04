package org.netscan.core.json;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.netscan.core.configuration.Filter;

import java.io.IOException;
import java.util.stream.Collectors;
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
public class FilterJsonAdapter extends TypeAdapter<Filter> {
    @Override
    public void write(JsonWriter out, Filter value) throws IOException {
        out.beginObject();
        out.name("filter");
        out.value(value.toString());
        out.endObject();
    }

    @Override
    public Filter read(JsonReader in) throws IOException {
        in.beginObject();
        in.nextName();
        String[] filter = in.nextString().replaceAll("\\s*", "").split(",");
        in.endObject();

        return new Filter(Stream.of(filter).collect(Collectors.toList()));
    }
}
