/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.dataprepper.peerforwarder.exception;

/**
 * This exception is thrown when processor which implements
 * {@link com.amazon.dataprepper.model.peerforwarder.RequiresPeerForwarding} interface returns an empty set of identification keys.
 *
 * @since 2.0
 */
public class EmptyPeerForwarderPluginIdentificationKeysException extends RuntimeException {

    public EmptyPeerForwarderPluginIdentificationKeysException(final String errorMessage) {
        super(errorMessage);
    }
}
