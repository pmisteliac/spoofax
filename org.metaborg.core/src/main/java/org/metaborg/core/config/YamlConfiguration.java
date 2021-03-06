package org.metaborg.core.config;

import org.apache.commons.configuration2.HierarchicalConfiguration;
import org.apache.commons.configuration2.tree.ImmutableNode;

import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.virtlink.commons.configuration2.jackson.JacksonConfiguration;

/**
 * Configuration that uses YAML files.
 */
public class YamlConfiguration extends JacksonConfiguration {
    /**
     * Initializes a new instance of the {@link YamlConfiguration} class.
     */
    public YamlConfiguration() {
        this(null);
    }

    /**
     * Initializes a new instance of the {@link YamlConfiguration} class.
     *
     * @param config
     *            The configuration whose nodes to copy.
     */
    public YamlConfiguration(HierarchicalConfiguration<ImmutableNode> config) {
        super(new YAMLFactory(), config);
    }
}
