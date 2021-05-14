package geolocation;

import com.akkaserverless.javasdk.EntityId;
import com.akkaserverless.javasdk.valueentity.CommandContext;
import com.akkaserverless.javasdk.valueentity.CommandHandler;
import com.akkaserverless.javasdk.valueentity.ValueEntity;
import com.google.protobuf.Empty;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ValueEntity(entityType = "geolocation-entity")
public class GeoLocationEntity {

    private final String entityId;

    public GeoLocationEntity(@EntityId String entityId) {
        this.entityId = entityId;
    }

    @CommandHandler
    public Empty addCustomerLocation(GeolocationApi.AddCustomerLocationCommand addCustomerLocationCommand,
                                     CommandContext<GeolocationApi.GeolocationState> ctx) {

        if (!isZipcode(addCustomerLocationCommand.getZipcode())) {
            ctx.fail("invalid zipcode");
        }
        else if (!isAlphaNumeric(addCustomerLocationCommand.getCustomerLocationId())) {
            ctx.fail("customer-location-id must be alphanumeric");
        }
        else {
            GeolocationApi.GeolocationState state = ctx.getState().orElse(GeolocationApi.GeolocationState.newBuilder().build());
            List<GeolocationApi.CustomerLocation> customerLocations = state.getCustomerLocationsList();
            customerLocations.add(GeolocationApi.CustomerLocation.newBuilder()
                    .setCustomerLocationId(addCustomerLocationCommand.getCustomerLocationId())
                    .setZipcode(addCustomerLocationCommand.getZipcode())
                    .build());

            GeolocationApi.GeolocationState updated = GeolocationApi.GeolocationState.newBuilder()
                    .setZipcode(addCustomerLocationCommand.getZipcode())
                    .addAllCustomerLocations(customerLocations)
                    .build();

            ctx.updateState(updated);
        }

        return Empty.getDefaultInstance();
    }

    @CommandHandler
    public Empty removeCustomerLocation(GeolocationApi.RemoveCustomerLocationCommand removeCustomerLocationCommand,
                                     CommandContext<GeolocationApi.GeolocationState> ctx) {

        if (ctx.getState().isEmpty()) {
            ctx.fail("geolocation does not exist");
        }
        else {
            GeolocationApi.GeolocationState state = ctx.getState().get();

            if (!findCustomerLocation(removeCustomerLocationCommand.getCustomerLocationId(), state.getCustomerLocationsList()).isPresent()) {
                ctx.fail("CustomerLocation does not exist");
            } else {
                List<GeolocationApi.CustomerLocation> customerLocations = state.getCustomerLocationsList().stream()
                        .filter(c -> !c.getCustomerLocationId().equals(removeCustomerLocationCommand.getCustomerLocationId()))
                        .collect(Collectors.toList());

                GeolocationApi.GeolocationState updated = GeolocationApi.GeolocationState.newBuilder()
                        .setZipcode(removeCustomerLocationCommand.getZipcode())
                        .addAllCustomerLocations(customerLocations)
                        .build();

                ctx.updateState(updated);
            }
        }

        return Empty.getDefaultInstance();
    }

    @CommandHandler
    public GeolocationApi.GeolocationState getGeolocation(GeolocationApi.GetGeolocationCommand getGeolocationCommand,
                                        CommandContext<GeolocationApi.GeolocationState> ctx) {

        if (ctx.getState().isEmpty()) {
            ctx.fail("geolocation does not exist");
        }

        GeolocationApi.GeolocationState state = ctx.getState().orElse(GeolocationApi.GeolocationState.newBuilder().build());
        return state;
    }

    /**
     * Helper function to find a customer location in collection.
     */
    private Optional<GeolocationApi.CustomerLocation> findCustomerLocation(String customerLocationId, List<GeolocationApi.CustomerLocation> customerLocations) {
        return customerLocations.stream()
                .filter(c -> c.getCustomerLocationId().equals(customerLocationId))
                .findFirst();
    }

    /**
     * Helper validation function for alphanumeric attributes.
     */
    private boolean isAlphaNumeric(String id) {
        return id.matches("^[a-zA-Z0-9_-]*$");
    }

    /**
     * Helper function to validate zipcode.
     */
    private boolean isZipcode(String zipcode) {
        return zipcode.matches("^[0-9]{5}(?:-[0-9]{4})?$");
    }
}
