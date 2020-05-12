package org.ambit.core;

import org.killbill.billing.osgi.libs.killbill.OSGIKillbillAPI;
import org.killbill.billing.osgi.libs.killbill.OSGIKillbillLogService;
import org.killbill.billing.plugin.api.notification.PluginTenantConfigurableConfigurationHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class HelloWorldConfigurationHandler extends PluginTenantConfigurableConfigurationHandler<Properties> {

    private static final Logger logger = LoggerFactory.getLogger(HelloWorldConfigurationHandler.class);

    private final String region;

    public HelloWorldConfigurationHandler(final String region,
                                          final String pluginName,
                                          final OSGIKillbillAPI osgiKillbillAPI,
                                          final OSGIKillbillLogService osgiKillbillLogService) {
        super(pluginName, osgiKillbillAPI, osgiKillbillLogService);

        this.region = region;
    }

    @Override
    protected Properties createConfigurable(final Properties properties) {
        return properties;
    }
}