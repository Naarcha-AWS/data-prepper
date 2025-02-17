/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.dataprepper.peerforwarder.discovery;

import org.opensearch.dataprepper.peerforwarder.PeerForwarderConfiguration;
import com.google.common.base.Preconditions;
import com.linecorp.armeria.client.Endpoint;
import com.linecorp.armeria.client.endpoint.dns.DnsAddressEndpointGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class DnsPeerListProvider implements PeerListProvider {

    private static final Logger LOG = LoggerFactory.getLogger(DnsPeerListProvider.class);
    private static final int MIN_TTL = 10;
    private static final int MAX_TTL = 20;

    private final DnsAddressEndpointGroup endpointGroup;

    public DnsPeerListProvider(final DnsAddressEndpointGroup endpointGroup) {
        Objects.requireNonNull(endpointGroup);

        this.endpointGroup = endpointGroup;

        try {
            endpointGroup.whenReady().get();
            LOG.info("Found endpoints: {}", String.join(",", endpointGroup.endpoints().toString()));
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Caught exception while querying DNS", e);
        }

    }

    static DnsPeerListProvider createPeerListProvider(PeerForwarderConfiguration peerForwarderConfiguration) {
        final String domainName = peerForwarderConfiguration.getDomainName();
        Objects.requireNonNull(domainName, "Missing domain_name configuration value");
        Preconditions.checkState(DiscoveryUtils.validateEndpoint(domainName), "Invalid domain name: %s", domainName);

        final DnsAddressEndpointGroup endpointGroup = DnsAddressEndpointGroup.builder(domainName)
                .ttl(MIN_TTL, MAX_TTL)
                .build();

        return new DnsPeerListProvider(endpointGroup);
    }

    @Override
    public List<String> getPeerList() {
        return endpointGroup.endpoints()
                .stream()
                .map(Endpoint::ipAddr)
                .collect(Collectors.toList());
    }

    @Override
    public void addListener(final Consumer<? super List<Endpoint>> listener) {
        endpointGroup.addListener(listener);
    }

    @Override
    public void removeListener(final Consumer<?> listener) {
        endpointGroup.removeListener(listener);
    }
}
