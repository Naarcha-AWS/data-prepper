/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.dataprepper.peerforwarder.discovery;

import org.opensearch.dataprepper.peerforwarder.PeerForwarderConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StaticPeerListProviderCreationTest {

    private static final String ENDPOINT = "ENDPOINT";
    private static final String INVALID_ENDPOINT = "INVALID_ENDPOINT_";

    private PeerForwarderConfiguration peerForwarderConfiguration;

    @BeforeEach
    void setup() {
        peerForwarderConfiguration = mock(PeerForwarderConfiguration.class);
    }

    @Test
    void testCreateProviderStaticInstanceNoEndpoints() {
        when(peerForwarderConfiguration.getStaticEndpoints()).thenReturn(null);

        assertThrows(NullPointerException.class,
                () -> StaticPeerListProvider.createPeerListProvider(peerForwarderConfiguration));
    }

    @Test
    void testCreateProviderStaticInstanceWithEndpoints() {
        when(peerForwarderConfiguration.getStaticEndpoints()).thenReturn(Collections.singletonList(ENDPOINT));

        PeerListProvider result = StaticPeerListProvider.createPeerListProvider(peerForwarderConfiguration);

        assertThat(result, instanceOf(StaticPeerListProvider.class));
        assertEquals(1, result.getPeerList().size());
        assertTrue(result.getPeerList().contains(ENDPOINT));
    }

    @Test
    void testCreateProviderStaticInstanceWithInvalidEndpoints() {
        when(peerForwarderConfiguration.getStaticEndpoints()).thenReturn(Arrays.asList(ENDPOINT, INVALID_ENDPOINT));

        assertThrows(IllegalStateException.class,
                () -> StaticPeerListProvider.createPeerListProvider(peerForwarderConfiguration));
    }

}