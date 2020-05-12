package org.ambit.core;

import org.ambit.HelloWorldServlet;
import org.ambit.api.HelloWorldPaymentPluginApi;
import org.ambit.core.health.HelloWorldHealthcheck;
import org.ambit.core.health.HelloWorldHealthcheckServlet;
import org.killbill.billing.osgi.api.Healthcheck;
import org.killbill.billing.osgi.api.OSGIPluginProperties;
import org.killbill.billing.osgi.libs.killbill.KillbillActivatorBase;
import org.killbill.billing.osgi.libs.killbill.OSGIKillbillEventDispatcher;
import org.killbill.billing.payment.plugin.api.PaymentPluginApi;
import org.killbill.billing.plugin.api.notification.PluginConfigurationEventHandler;
import org.killbill.billing.plugin.core.config.PluginEnvironmentConfig;
import org.killbill.billing.plugin.core.resources.jooby.PluginApp;
import org.killbill.billing.plugin.core.resources.jooby.PluginAppBuilder;
import org.osgi.framework.BundleContext;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServlet;
import java.util.Hashtable;
import java.util.Properties;

import org.ambit.HelloWorldListener;

public class HelloWorldActivator extends KillbillActivatorBase {

    public static final String PLUGIN_NAME = "hello-world-payment-plugin";

    private HelloWorldConfigurationHandler helloWorldConfigurationHandler;
    private OSGIKillbillEventDispatcher.OSGIKillbillEventHandler killbillEventHandler;

    @Override
    public void start(final BundleContext context) throws Exception {
        super.start(context);

        final String region = PluginEnvironmentConfig.getRegion(configProperties.getProperties());

        helloWorldConfigurationHandler = new HelloWorldConfigurationHandler(region, PLUGIN_NAME, killbillAPI, logService);
        final Properties globalConfiguration = helloWorldConfigurationHandler.createConfigurable(configProperties.getProperties());
        helloWorldConfigurationHandler.setDefaultConfigurable(globalConfiguration);

        //Optional handlers, but helps flesh out the foundation.
        killbillEventHandler = new HelloWorldListener(killbillAPI);

        final PaymentPluginApi paymentPluginApi = new HelloWorldPaymentPluginApi(configProperties.getProperties());
        registerPaymentPluginApi(context, paymentPluginApi);

        final Healthcheck healthcheck = new HelloWorldHealthcheck();
        registerHealthcheck(context, healthcheck);

        final PluginApp pluginApp = new PluginAppBuilder(PLUGIN_NAME,
                killbillAPI,
                logService,
                dataSource,
                super.clock,
                configProperties).withRouteClass(HelloWorldServlet.class)
                .withRouteClass(HelloWorldHealthcheckServlet.class)
                .withService(healthcheck)
                .build();

        final HttpServlet httpServlet = PluginApp.createServlet(pluginApp);
        registerServlet(context, httpServlet);

        registerHandlers();
    }

    @Override
    public void stop(final BundleContext context) throws Exception {
        super.stop(context);
    }

    private void registerHandlers() {
        final PluginConfigurationEventHandler configHandler = new PluginConfigurationEventHandler(helloWorldConfigurationHandler);

        dispatcher.registerEventHandlers(configHandler,
                (OSGIKillbillEventDispatcher.OSGIFrameworkEventHandler) () -> dispatcher.registerEventHandlers(killbillEventHandler));
    }

    private void registerServlet(final BundleContext context, final Servlet servlet) {
        final Hashtable<String, String> props = new Hashtable<String, String>();
        props.put(OSGIPluginProperties.PLUGIN_NAME_PROP, PLUGIN_NAME);
        registrar.registerService(context, Servlet.class, servlet, props);
    }

    private void registerPaymentPluginApi(final BundleContext context, final PaymentPluginApi api) {
        final Hashtable<String, String> props = new Hashtable<String, String>();
        props.put(OSGIPluginProperties.PLUGIN_NAME_PROP, PLUGIN_NAME);
        registrar.registerService(context, PaymentPluginApi.class, api, props);
    }

    private void registerHealthcheck(final BundleContext context, final Healthcheck healthcheck) {
        final Hashtable<String, String> props = new Hashtable<String, String>();
        props.put(OSGIPluginProperties.PLUGIN_NAME_PROP, PLUGIN_NAME);
        registrar.registerService(context, Healthcheck.class, healthcheck, props);
    }
}