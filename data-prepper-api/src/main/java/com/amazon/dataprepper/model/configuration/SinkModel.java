/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.amazon.dataprepper.model.configuration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Represents an extension of the {@link PluginModel} which is specific to Sink
 * plugins. This model introduces custom properties which are not available as
 * plugin settings.
 *
 * @since 2.0
 */
@JsonSerialize(using = PluginModel.PluginModelSerializer.class)
@JsonDeserialize(using = SinkModel.SinkModelDeserializer.class)
public class SinkModel extends PluginModel {

    SinkModel(final String pluginName, final List<String> routes, final Map<String, Object> pluginSettings) {
        this(pluginName, new SinkInternalJsonModel(routes, pluginSettings));
    }

    private SinkModel(final String pluginName, final SinkInternalJsonModel sinkInnerModel) {
        super(pluginName, sinkInnerModel);
    }

    /**
     * Gets the routes associated with this Sink.
     *
     * @return The collection of routes
     * @since 2.0
     */
    public Collection<String> getRoutes() {
        return this.<SinkInternalJsonModel>getInternalJsonModel().routes;
    }

    private static class SinkInternalJsonModel extends InternalJsonModel {
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        @JsonProperty("routes")
        private final List<String> routes;

        @JsonCreator
        private SinkInternalJsonModel(@JsonProperty("routes") final List<String> routes) {
            super();
            this.routes = routes != null ? routes : new ArrayList<>();
        }

        private SinkInternalJsonModel(final List<String> routes, final Map<String, Object> pluginSettings) {
            super(pluginSettings);
            this.routes = routes != null ? routes : new ArrayList<>();
        }
    }

    static class SinkModelDeserializer extends AbstractPluginModelDeserializer<SinkModel, SinkInternalJsonModel> {
        SinkModelDeserializer() {
            super(SinkModel.class, SinkInternalJsonModel.class, SinkModel::new, () -> new SinkInternalJsonModel(null));
        }
    }
}
