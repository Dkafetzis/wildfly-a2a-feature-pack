/*
 * Copyright The WildFly Authors
 * SPDX-License-Identifier: Apache-2.0
 */
package org.wildfly.a2a.test.integration.jsonrpc;

import org.a2aproject.sdk.client.ClientBuilder;
import org.a2aproject.sdk.client.transport.jsonrpc.JSONRPCTransport;
import org.a2aproject.sdk.client.transport.jsonrpc.JSONRPCTransportConfigBuilder;
import org.a2aproject.sdk.server.apps.common.AbstractA2AServerTest;
import org.a2aproject.sdk.spec.TransportProtocol;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit5.container.annotation.ArquillianTest;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
/**
 * Integration test for A2A JSON-RPC transport on WildFly with the A2A feature-pack.
 *
 * All A2A SDK dependencies are provided by the A2A subsystem modules automatically.
 */
@ArquillianTest
@RunAsClient
public class A2AJsonRpcTestCase extends AbstractA2AServerTest {

    public A2AJsonRpcTestCase() {
        super(8080);
    }

    @Override
    protected String getTransportProtocol() {
        return TransportProtocol.JSONRPC.asString();
    }

    @Override
    protected String getTransportUrl() {
        return "http://localhost:8080";
    }

    @Override
    protected void configureTransport(ClientBuilder builder) {
        builder.withTransport(JSONRPCTransport.class, new JSONRPCTransportConfigBuilder());
    }

    @Deployment
    public static WebArchive createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "ROOT.war")
                // Test utilities from a2a-java-sdk-tests-server-common (test-jar classes)
                .addPackage(AbstractA2AServerTest.class.getPackage())
                // Test classes for this module
                .addPackage(A2AJsonRpcTestCase.class.getPackage())
                // Deployment descriptors
                .addAsManifestResource("META-INF/beans.xml", "beans.xml")
                .addAsWebInfResource("WEB-INF/web.xml", "web.xml")
                // Test properties for AgentCardProducer
                .addAsResource("a2a-requesthandler-test.properties");
    }

    /**
     * Request-scoped beans are not available on the agent executor threads when A2A is provided as a feature-pack.
     * a2a-jakarta overrides the SDK's {@code @Internal Executor} with an {@code @Alternative} producer backed by a
     * {@code ManagedExecutorService} ({@code AsyncManagedExecutorServiceProducer}). Here that producer sits in a JBoss
     * module rather than in the deployment, and beans from modules - or beans registered by a portable extension - are
     * not visible to the injection point in {@code org.a2aproject.sdk.server-common}, so the SDK's own executor is
     * always the one that wins.
     */
    @Test
    @Disabled("Request context propagation to the agent executor threads is not supported by the feature-pack")
    @Override
    public void testRequestScopedBeanAvailableOnAgentExecutorThread() {
    }

    /**
     * @see #testRequestScopedBeanAvailableOnAgentExecutorThread()
     */
    @Test
    @Disabled("Request context propagation to the agent executor threads is not supported by the feature-pack")
    @Override
    public void testRequestScopedBeanAvailableOnAgentExecutorThreadStreaming() {
    }

}
