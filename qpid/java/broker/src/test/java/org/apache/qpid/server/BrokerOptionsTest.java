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
package org.apache.qpid.server;

import java.io.File;
import java.util.Map;

import org.apache.qpid.test.utils.QpidTestCase;

public class BrokerOptionsTest extends QpidTestCase
{
    private BrokerOptions _options;

    protected void setUp() throws Exception
    {
        super.setUp();
        _options = new BrokerOptions();
    }

    public void testDefaultConfigurationStoreType()
    {
        assertEquals("json", _options.getConfigurationStoreType());
    }

    public void testOverriddenConfigurationStoreType()
    {
        _options.setConfigurationStoreType("dby");
        assertEquals("dby", _options.getConfigurationStoreType());
    }

    public void testDefaultConfigurationStoreLocationWithQpidWork()
    {
        String qpidWork = "/test/value";
        setTestSystemProperty("QPID_WORK", qpidWork);

        String expectedPath = new File(qpidWork, BrokerOptions.DEFAULT_CONFIG_NAME_PREFIX + "." + BrokerOptions.DEFAULT_STORE_TYPE).getAbsolutePath();
        assertEquals (expectedPath, _options.getConfigurationStoreLocation());
    }

    public void testDefaultConfigurationStoreLocationWithoutQpidWork()
    {
        setTestSystemProperty("QPID_WORK", null);
        String userDir = System.getProperty("user.dir");

        String expectedPath = new File(userDir, "work/" + BrokerOptions.DEFAULT_CONFIG_NAME_PREFIX + "." + BrokerOptions.DEFAULT_STORE_TYPE).getAbsolutePath();
        assertEquals (expectedPath, _options.getConfigurationStoreLocation());
    }

    public void testDefaultConfigurationStoreLocationWithQpidWorkAndDifferentStoreType()
    {
        String qpidWork = "/test/value";
        setTestSystemProperty("QPID_WORK", qpidWork);

        String storeType = "dby";
        _options.setConfigurationStoreType(storeType);

        String expectedPath = new File(qpidWork, BrokerOptions.DEFAULT_CONFIG_NAME_PREFIX + "." + storeType).getAbsolutePath();
        assertEquals (expectedPath, _options.getConfigurationStoreLocation());
    }

    public void testOverriddenConfigurationStoreLocation()
    {
        final String testConfigFile = "/my/test/store-location.dby";
        _options.setConfigurationStoreLocation(testConfigFile);
        assertEquals(testConfigFile, _options.getConfigurationStoreLocation());
    }

    public void testDefaultLogConfigFile()
    {
        assertNull(_options.getLogConfigFile());
    }

    public void testOverriddenLogConfigFile()
    {
        final String testLogConfigFile = "etc/mytestlog4j.xml";
        _options.setLogConfigFile(testLogConfigFile);
        assertEquals(testLogConfigFile, _options.getLogConfigFile());
    }

    public void testDefaultLogWatchFrequency()
    {
        assertEquals(0L, _options.getLogWatchFrequency());
    }

    public void testOverridenLogWatchFrequency()
    {
        final int myFreq = 10 * 1000;
        
        _options.setLogWatchFrequency(myFreq);
        assertEquals(myFreq, _options.getLogWatchFrequency());
    }

    public void testDefaultInitialConfigurationLocation()
    {
        assertEquals(BrokerOptions.DEFAULT_INITIAL_CONFIG_LOCATION, _options.getInitialConfigurationLocation());
    }

    public void testOverriddenInitialConfigurationLocation()
    {
        final String testConfigFile = "etc/mytestconfig.json";
        _options.setInitialConfigurationLocation(testConfigFile);
        assertEquals(testConfigFile, _options.getInitialConfigurationLocation());
    }

    public void testDefaultManagementMode()
    {
        assertEquals(false, _options.isManagementMode());
    }

    public void testOverriddenDefaultManagementMode()
    {
        _options.setManagementMode(true);
        assertEquals(true, _options.isManagementMode());
    }

    public void testDefaultManagementModeQuiesceVirtualHosts()
    {
        assertEquals(false, _options.isManagementModeQuiesceVirtualHosts());
    }

    public void testOverriddenDefaultManagementModeQuiesceVirtualHosts()
    {
        _options.setManagementModeQuiesceVirtualHosts(true);
        assertEquals(true, _options.isManagementModeQuiesceVirtualHosts());
    }

    public void testDefaultManagementModeRmiPort()
    {
        assertEquals(0, _options.getManagementModeRmiPort());
    }

    public void testOverriddenDefaultManagementModeRmiPort()
    {
        _options.setManagementModeRmiPort(5555);
        assertEquals(5555, _options.getManagementModeRmiPort());
    }

    public void testDefaultManagementModeConnectorPort()
    {
        assertEquals(0, _options.getManagementModeConnectorPort());
    }

    public void testOverriddenDefaultManagementModeConnectorPort()
    {
        _options.setManagementModeConnectorPort(5555);
        assertEquals(5555, _options.getManagementModeConnectorPort());
    }

    public void testDefaultManagementModeHttpPort()
    {
        assertEquals(0, _options.getManagementModeHttpPort());
    }

    public void testOverriddenDefaultManagementModeHttpPort()
    {
        _options.setManagementModeHttpPort(5555);
        assertEquals(5555, _options.getManagementModeHttpPort());
    }

    public void testDefaultWorkDirWithQpidWork()
    {
        String qpidWork = "/test/value";
        setTestSystemProperty("QPID_WORK", qpidWork);

        String expectedPath = new File(qpidWork).getAbsolutePath();
        assertEquals (expectedPath, _options.getWorkDir());
    }

    public void testDefaultWorkDirWithoutQpidWork()
    {
        setTestSystemProperty("QPID_WORK", null);
        String userDir = System.getProperty("user.dir");

        String expectedPath = new File(userDir, "work").getAbsolutePath();
        assertEquals (expectedPath, _options.getWorkDir());
    }

    public void testOverriddenWorkDir()
    {
        final String testWorkDir = "/my/test/work/dir";
        _options.setWorkDir(testWorkDir);
        assertEquals(testWorkDir, _options.getWorkDir());
    }

    public void testDefaultSkipLoggingConfiguration()
    {
        assertFalse(_options.isSkipLoggingConfiguration());
    }

    public void testOverriddenSkipLoggingConfiguration()
    {
        _options.setSkipLoggingConfiguration(true);
        assertTrue(_options.isSkipLoggingConfiguration());
    }

    public void testDefaultOverwriteConfigurationStore()
    {
        assertFalse(_options.isOverwriteConfigurationStore());
    }

    public void testOverriddenOverwriteConfigurationStore()
    {
        _options.setOverwriteConfigurationStore(true);
        assertTrue(_options.isOverwriteConfigurationStore());
    }

    public void testManagementModePassword()
    {
        _options.setManagementModePassword("test");
        assertEquals("Unexpected management mode password", "test", _options.getManagementModePassword());
    }

    public void testGetDefaultConfigProperties()
    {
        Map<String,String> props = _options.getConfigProperties();

        assertEquals("unexpected number of entries", 4, props.keySet().size());

        assertEquals(BrokerOptions.DEFAULT_AMQP_PORT_NUMBER, props.get(BrokerOptions.QPID_AMQP_PORT));
        assertEquals(BrokerOptions.DEFAULT_HTTP_PORT_NUMBER, props.get(BrokerOptions.QPID_HTTP_PORT));
        assertEquals(BrokerOptions.DEFAULT_RMI_PORT_NUMBER, props.get(BrokerOptions.QPID_RMI_PORT));
        assertEquals(BrokerOptions.DEFAULT_JMX_PORT_NUMBER, props.get(BrokerOptions.QPID_JMX_PORT));
    }

    public void testSetDefaultConfigProperties()
    {
        String oldPort = BrokerOptions.DEFAULT_AMQP_PORT_NUMBER;
        String newPort = "12345";

        _options.setConfigProperty(BrokerOptions.QPID_AMQP_PORT, newPort);
        Map<String,String> props = _options.getConfigProperties();
        assertEquals("unexpected number of entries", 4, props.keySet().size());
        assertEquals(newPort, props.get(BrokerOptions.QPID_AMQP_PORT));
        assertEquals(BrokerOptions.DEFAULT_HTTP_PORT_NUMBER, props.get(BrokerOptions.QPID_HTTP_PORT));
        assertEquals(BrokerOptions.DEFAULT_RMI_PORT_NUMBER, props.get(BrokerOptions.QPID_RMI_PORT));
        assertEquals(BrokerOptions.DEFAULT_JMX_PORT_NUMBER, props.get(BrokerOptions.QPID_JMX_PORT));

        _options.setConfigProperty(BrokerOptions.QPID_AMQP_PORT, null);
        props = _options.getConfigProperties();
        assertEquals("unexpected number of entries", 4, props.keySet().size());
        assertEquals(oldPort, props.get(BrokerOptions.QPID_AMQP_PORT));
        assertEquals(BrokerOptions.DEFAULT_HTTP_PORT_NUMBER, props.get(BrokerOptions.QPID_HTTP_PORT));
        assertEquals(BrokerOptions.DEFAULT_RMI_PORT_NUMBER, props.get(BrokerOptions.QPID_RMI_PORT));
        assertEquals(BrokerOptions.DEFAULT_JMX_PORT_NUMBER, props.get(BrokerOptions.QPID_JMX_PORT));

        _options.setConfigProperty("name", "value");
        props = _options.getConfigProperties();
        assertEquals("unexpected number of entries", 5, props.keySet().size());
        assertEquals(oldPort, props.get(BrokerOptions.QPID_AMQP_PORT));
        assertEquals(BrokerOptions.DEFAULT_HTTP_PORT_NUMBER, props.get(BrokerOptions.QPID_HTTP_PORT));
        assertEquals(BrokerOptions.DEFAULT_RMI_PORT_NUMBER, props.get(BrokerOptions.QPID_RMI_PORT));
        assertEquals(BrokerOptions.DEFAULT_JMX_PORT_NUMBER, props.get(BrokerOptions.QPID_JMX_PORT));
        assertEquals("value", props.get("name"));
    }
}