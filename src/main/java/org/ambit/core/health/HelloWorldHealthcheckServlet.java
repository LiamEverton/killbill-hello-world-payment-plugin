package org.ambit.core.health;

import java.util.Map;
import java.util.Optional;

import org.jooby.Result;
import org.jooby.mvc.GET;
import org.jooby.mvc.Local;
import org.jooby.mvc.Path;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.killbill.billing.osgi.api.Healthcheck;
import org.killbill.billing.plugin.core.resources.PluginHealthcheck;
import org.killbill.billing.tenant.api.Tenant;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
@Path("/healthcheck")
/**
 * https://killbill.github.io/slate/#admin-operational-apis
 */
public class HelloWorldHealthcheckServlet extends PluginHealthcheck implements Healthcheck {

    private final HelloWorldHealthcheck healthcheck;

    @Inject
    public HelloWorldHealthcheckServlet(final HelloWorldHealthcheck healthcheck) {
        this.healthcheck = healthcheck;
    }

    @GET
    public Result check(@Local @Named("killbill_tenant") final Optional<Tenant> tenant) throws JsonProcessingException {
        return check(healthcheck, tenant.orElse(null), null);
    }

    @Override
    public HealthStatus getHealthStatus(Tenant tenant, Map map) {
        return HealthStatus.healthy("OK");
    }
}