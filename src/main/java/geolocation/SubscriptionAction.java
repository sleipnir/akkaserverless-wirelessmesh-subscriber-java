package geolocation;

import com.akkaserverless.javasdk.Reply;
import com.akkaserverless.javasdk.ServiceCallRef;
import com.akkaserverless.javasdk.action.Action;
import com.akkaserverless.javasdk.action.ActionContext;
import com.akkaserverless.javasdk.action.ActionCreationContext;
import com.akkaserverless.javasdk.action.Handler;
import com.google.protobuf.Any;
import com.google.protobuf.Empty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wirelessmeshdomain.Wirelessmeshdomain;

/**
 * An action to foward a subscribed pubsub event to a value entity.
 */
@Action
public class SubscriptionAction {

    private static final Logger LOG = LoggerFactory.getLogger(SubscriptionAction.class);

    private static final String GEOLOCATION_SERVICE = "geolocation.GeolocationService";

    private final ServiceCallRef<GeolocationApi.AddCustomerLocationCommand> addCustomerLocationCommandRef;

    private final ServiceCallRef<GeolocationApi.RemoveCustomerLocationCommand> removeCustomerLocationCommandRef;

    public SubscriptionAction(ActionCreationContext context) {
        addCustomerLocationCommandRef = context
                .serviceCallFactory()
                        .lookup(GEOLOCATION_SERVICE, "AddCustomerLocation", GeolocationApi.AddCustomerLocationCommand.class);

        removeCustomerLocationCommandRef = context
                .serviceCallFactory()
                .lookup(GEOLOCATION_SERVICE, "RemoveCustomerLocation", GeolocationApi.RemoveCustomerLocationCommand.class);
    }

    @Handler
    public Reply<Empty> forwardCustomerLocationAdded(Wirelessmeshdomain.CustomerLocationAdded in, ActionContext ctx) {
        LOG.info("forwardCustomerLocationAdded::Received: '{}'", in);

        GeolocationApi.AddCustomerLocationCommand command =
                GeolocationApi.AddCustomerLocationCommand.newBuilder()
                .setZipcode(in.getZipcode())
                .setCustomerLocationId(in.getCustomerLocationId())
                .build();

        return Reply.forward(addCustomerLocationCommandRef.createCall(command));
    }

    @Handler
    public Reply<Empty> forwardCustomerLocationRemoved(Wirelessmeshdomain.CustomerLocationRemoved in, ActionContext ctx) {
        LOG.info("forwardCustomerLocationRemoved::Received: '{}'", in);

        GeolocationApi.RemoveCustomerLocationCommand command =
                GeolocationApi.RemoveCustomerLocationCommand.newBuilder()
                        .setZipcode(in.getZipcode())
                        .setCustomerLocationId(in.getCustomerLocationId())
                        .build();

        return Reply.forward(removeCustomerLocationCommandRef.createCall(command));
    }

    @Handler
    public Empty catchOthers(Any in) {
        return Empty.getDefaultInstance();
    }
}
