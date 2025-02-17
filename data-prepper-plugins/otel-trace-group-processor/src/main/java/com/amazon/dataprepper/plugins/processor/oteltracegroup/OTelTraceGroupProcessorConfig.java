/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.amazon.dataprepper.plugins.processor.oteltracegroup;

import com.amazon.dataprepper.model.configuration.PluginSetting;
import org.opensearch.dataprepper.plugins.sink.opensearch.ConnectionConfiguration;
import org.opensearch.dataprepper.plugins.sink.opensearch.index.IndexConstants;
import org.opensearch.dataprepper.plugins.sink.opensearch.index.IndexType;

public class OTelTraceGroupProcessorConfig {
    protected static final String TRACE_ID_FIELD = "traceId";
    protected static final String SPAN_ID_FIELD = "spanId";
    protected static final String PARENT_SPAN_ID_FIELD = "parentSpanId";
    protected static final String RAW_INDEX_ALIAS = IndexConstants.TYPE_TO_DEFAULT_ALIAS.get(IndexType.TRACE_ANALYTICS_RAW);
    protected static final String STRICT_DATE_TIME = "strict_date_time";

    private final ConnectionConfiguration esConnectionConfig;

    public ConnectionConfiguration getEsConnectionConfig() {
        return esConnectionConfig;
    }

    private OTelTraceGroupProcessorConfig(final ConnectionConfiguration esConnectionConfig) {
        this.esConnectionConfig = esConnectionConfig;
    }

    public static OTelTraceGroupProcessorConfig buildConfig(final PluginSetting pluginSetting) {
        final ConnectionConfiguration esConnectionConfig = ConnectionConfiguration.readConnectionConfiguration(pluginSetting);
        return new OTelTraceGroupProcessorConfig(esConnectionConfig);
    }
}
