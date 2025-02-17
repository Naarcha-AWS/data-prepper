/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.amazon.dataprepper.plugins.prepper.peerforwarder.certificate.file;

import com.amazon.dataprepper.plugins.prepper.peerforwarder.certificate.CertificateProvider;
import com.amazon.dataprepper.plugins.prepper.peerforwarder.certificate.model.Certificate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class FileCertificateProvider implements CertificateProvider {
    private final String certificateFilePath;

    public FileCertificateProvider(final String certificateFilePath) {
        this.certificateFilePath = Objects.requireNonNull(certificateFilePath);
    }

    private static final Logger LOG = LoggerFactory.getLogger(FileCertificateProvider.class);

    public Certificate getCertificate() {
        try {
            final Path certFilePath = new File(certificateFilePath).toPath();

            final byte[] bytes = Files.readAllBytes(certFilePath);

            final String certAsString = new String(bytes);

            return new Certificate(certAsString);
        } catch (final Exception ex) {
            LOG.error("Error encountered while reading the certificate.", ex);
            throw new RuntimeException(ex);
        }
    }
}
