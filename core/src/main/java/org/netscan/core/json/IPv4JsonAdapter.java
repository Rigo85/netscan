package org.netscan.core.json;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.netscan.core.ipv4.IPv4;

import java.io.IOException;

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
public class IPv4JsonAdapter  extends TypeAdapter<IPv4>{
    @Override
    public void write(JsonWriter out, IPv4 value) throws IOException {
        out.beginObject();
        out.name("ip");
        out.value(value.toString());
        out.endObject();
    }

    @Override
    public IPv4 read(JsonReader in) throws IOException {
        in.beginObject();
        in.nextName();
        String ip = in.nextString();
        in.endObject();
        return new IPv4(ip);
    }
}
