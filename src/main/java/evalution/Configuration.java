package evalution;

import org.cfg4j.provider.ConfigurationProvider;
import org.cfg4j.provider.ConfigurationProviderBuilder;
import org.cfg4j.source.ConfigurationSource;
import org.cfg4j.source.compose.FallbackConfigurationSource;
import org.cfg4j.source.context.filesprovider.ConfigFilesProvider;
import org.cfg4j.source.files.FilesConfigurationSource;
import org.cfg4j.source.system.EnvironmentVariablesConfigurationSource;

import java.io.File;
import java.util.Arrays;

public class Configuration {
    private static Configuration ourInstance = new Configuration();

    public ConfigurationProvider getConfig() {
        return config;
    }

    private ConfigurationProvider config;

    public static Configuration getInstance() {
        return ourInstance;
    }

    private Configuration() {
        ConfigFilesProvider filesProvider = () -> Arrays.asList(new File("secrets.yaml").toPath().toAbsolutePath());
        ConfigurationSource fileConfig = new FilesConfigurationSource(filesProvider);
        ConfigurationSource source = new EnvironmentVariablesConfigurationSource();
        FallbackConfigurationSource fallbackConfig = new FallbackConfigurationSource(fileConfig, source);
        config = new ConfigurationProviderBuilder()
                .withConfigurationSource(fallbackConfig)
                .build();
    }
}
