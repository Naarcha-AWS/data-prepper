/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.amazon.dataprepper.plugins.source.ownership;

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

class NoOwnershipBucketOwnerProviderTest {
    private NoOwnershipBucketOwnerProvider createObjectUnderTest() {
        return new NoOwnershipBucketOwnerProvider();
    }

    @Test
    void getBucketOwner_returns_empty() {
        final Optional<String> optionalOwner = createObjectUnderTest().getBucketOwner(UUID.randomUUID().toString());

        assertThat(optionalOwner, notNullValue());
        assertThat(optionalOwner.isPresent(), equalTo(false));
    }
}