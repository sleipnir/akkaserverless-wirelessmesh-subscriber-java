package geolocation;

import com.akkaserverless.javasdk.AkkaServerless;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import geolocation.subscription.SubscriptionApi;

/**
 * This is the entry point into this user function.
 */
public class GeolocationMain {

    private final static Logger LOG = LoggerFactory.getLogger(GeolocationMain.class);

    public static final AkkaServerless SERVICE =
            new AkkaServerless()
                    .registerValueEntity(
                            GeoLocationEntity.class,
                            GeolocationApi.getDescriptor().findServiceByName("GeolocationService"))
                    .registerAction(
                            SubscriptionAction.class,
                            SubscriptionApi.getDescriptor().findServiceByName("SubscriptionService"));

    public static void main(String... args) throws Exception {
        LOG.info("started");
        SERVICE.start().toCompletableFuture().get();
    }
}
