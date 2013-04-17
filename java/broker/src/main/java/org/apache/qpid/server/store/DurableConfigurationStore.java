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
package org.apache.qpid.server.store;

import org.apache.commons.configuration.Configuration;

import org.apache.qpid.AMQStoreException;
import org.apache.qpid.framing.FieldTable;
import org.apache.qpid.server.binding.Binding;
import org.apache.qpid.server.exchange.Exchange;
import org.apache.qpid.server.queue.AMQQueue;

public interface DurableConfigurationStore
{

    public static interface Source
    {
        DurableConfigurationStore getMessageStore();
    }

    /**
     * Called after instantiation in order to configure the message store. A particular implementation can define
     * whatever parameters it wants.
     *
     * @param name             The name to be used by this store
     * @param recoveryHandler  Handler to be called as the store recovers on start up
     * @param config           The apache commons configuration object.
     * @throws Exception If any error occurs that means the store is unable to configure itself.
     */
    void configureConfigStore(String name,
                              ConfigurationRecoveryHandler recoveryHandler,
                              Configuration config) throws Exception;
    /**
     * Makes the specified exchange persistent.
     *
     * @param exchange The exchange to persist.
     *
     * @throws AMQStoreException If the operation fails for any reason.
     */
    void createExchange(Exchange exchange) throws AMQStoreException;

    /**
     * Removes the specified persistent exchange.
     *
     * @param exchange The exchange to remove.
     *
     * @throws AMQStoreException If the operation fails for any reason.
     */
    void removeExchange(Exchange exchange) throws AMQStoreException;

    /**
     * Store the queue binding.
     *
     * @param binding queue binding
     *
     * @throws AMQStoreException if the operation fails for any reason.
     */
    void bindQueue(Binding binding) throws AMQStoreException;

    /**
     * Removes queue binding
     *
     * @param binding queue binding to remove
     *
     * @throws AMQStoreException If the operation fails for any reason.
     */
    void unbindQueue(Binding binding) throws AMQStoreException;

    /**
     * Makes the specified queue persistent.
     *
     * @param queue The queue to store.
     *
     * @throws AMQStoreException If the operation fails for any reason.
     */
    void createQueue(AMQQueue queue) throws AMQStoreException;

    /**
     * Makes the specified queue persistent.
     *
     * @param queue The queue to store.
     * @param arguments The additional arguments to the binding
     *
     * @throws AMQStoreException If the operation fails for any reason.
     */
    void createQueue(AMQQueue queue, FieldTable arguments) throws AMQStoreException;

    /**
     * Removes the specified queue from the persistent store.
     *
     * @param queue The queue to remove.
     * 
     * @throws AMQStoreException If the operation fails for any reason.
     */
    void removeQueue(AMQQueue queue) throws AMQStoreException;
    
    /**
     * Updates the specified queue in the persistent store, IF it is already present. If the queue
     * is not present in the store, it will not be added.
     *
     * @param queue The queue to update the entry for.
     * @throws AMQStoreException If the operation fails for any reason.
     */
    void updateQueue(AMQQueue queue) throws AMQStoreException;

}