package geolocation;

import com.akkaserverless.javasdk.Reply;
import com.akkaserverless.javasdk.ServiceCallRef;
import com.akkaserverless.javasdk.action.Action;
import com.akkaserverless.javasdk.action.ActionContext;
import com.akkaserverless.javasdk.action.ActionCreationContext;
import com.akkaserverless.javasdk.action.Handler;
import com.google.protobuf.Empty;

import geolocation.subscription.SubscriptionApi;

/**
 * An action to foward a subscribed pubsub event to a value entity.
 */
@Action
public class SubscriptionAction {

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
    public Reply<Empty> forwardCustomerLocationAdded(SubscriptionApi.CustomerLocationAdded in, ActionContext ctx) {
        GeolocationApi.AddCustomerLocationCommand command =
                GeolocationApi.AddCustomerLocationCommand.newBuilder()
                .setZipcode(in.getZipcode())
                .setCustomerLocationId(in.getCustomerLocationId())
                .build();

        return Reply.forward(addCustomerLocationCommandRef.createCall(command));
    }

    @Handler
    public Reply<Empty> forwardCustomerLocationRemoved(SubscriptionApi.CustomerLocationRemoved in, ActionContext ctx) {
        GeolocationApi.RemoveCustomerLocationCommand command =
                GeolocationApi.RemoveCustomerLocationCommand.newBuilder()
                        .setZipcode(in.getZipcode())
                        .setCustomerLocationId(in.getCustomerLocationId())
                        .build();

        return Reply.forward(removeCustomerLocationCommandRef.createCall(command));
    }
}
