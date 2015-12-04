package org.netscan.core.configuration;

import com.google.gson.annotations.JsonAdapter;
import org.netscan.core.json.CredentialJsonAdapter;

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
@JsonAdapter(CredentialJsonAdapter.class)
public class Credential {
    private final String domain;
    private final String userName;
    private final String password;

    public Credential(String domain, String userName, String password) {
        this.domain = domain;
        this.userName = userName;
        this.password = password;
    }

    public String getDomain() {
        return domain;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return String.format("%s:%s:%s", domain, userName, password);
    }
}
