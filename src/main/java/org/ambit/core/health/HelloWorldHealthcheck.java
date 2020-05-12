package org.ambit.core.health;

import org.killbill.billing.osgi.api.Healthcheck;
import org.killbill.billing.tenant.api.Tenant;

import javax.annotation.Nullable;
import java.util.Map;

public class HelloWorldHealthcheck implements Healthcheck {

    @Override
    public Healthcheck.HealthStatus getHealthStatus(@Nullable final Tenant tenant, @Nullable final Map properties) {
        return HealthStatus.healthy();
    }
}