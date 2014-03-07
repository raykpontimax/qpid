/*
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */
package org.apache.qpid.server.management.plugin.servlet;

import org.apache.qpid.server.security.auth.SocketConnectionPrincipal;

import javax.servlet.ServletRequest;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class ServletConnectionPrincipal implements SocketConnectionPrincipal
{
    private final InetSocketAddress _address;

    public ServletConnectionPrincipal(ServletRequest request)
    {
        _address = new InetSocketAddress(request.getRemoteHost(), request.getRemotePort());
    }

    @Override
    public SocketAddress getRemoteAddress()
    {
        return _address;
    }

    @Override
    public String getName()
    {
        return _address.toString();
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        final ServletConnectionPrincipal that = (ServletConnectionPrincipal) o;

        if (!_address.equals(that._address))
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        return _address.hashCode();
    }
}