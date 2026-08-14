package org.wildfly.a2a.test.integration.grpc;

import jakarta.enterprise.context.ApplicationScoped;

import org.wildfly.a2a.jakarta.grpc.WildFlyGrpcHandler;

/**
 * Test class that extends WildFlyGrpcHandler to verify deployment-based gRPC handler inheritance.
 */
@ApplicationScoped
public class DeploymentGrpcBeanInitializer extends WildFlyGrpcHandler {
}
