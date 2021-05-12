//package geolocation;
//
//import com.akkaserverless.javasdk.AkkaServerless;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import geolocation.domain.CustomerLocationEntity;
//import geolocation.domain.CustomerLocationView;
//
///**
// * This is the entry point into this user function.
// */
//public class GeolocationMain {
//
//    private final static Logger LOG = LoggerFactory.getLogger(GeolocationMain.class);
//
//    public static AkkaServerless geoLocationService =
//            new AkkaServerless()
//                    .registerEventSourcedEntity(
//                            CustomerLocationEntity.class,
//                            Wirelessmesh.getDescriptor().findServiceByName("WirelessMeshService"),
//                            Wirelessmeshdomain.getDescriptor()
//                    )
//                    .registerView(
//                            CustomerLocationView.class,
//                            Customerlocationview.getDescriptor()
//                                    .findServiceByName("CustomerLocationByEmailService"),
//                            "customer_locations",
//                            Wirelessmeshdomain.getDescriptor(),
//                            Customerlocationview.getDescriptor())
//                    .registerAction(
//                            new ToggleNightlightAction(),
//                            Devicecontrol.getDescriptor().findServiceByName("DeviceControlService"),
//                            Wirelessmeshdomain.getDescriptor())
//                    .registerAction(
//                            new SubscriptionAction(),
//                            Publishing.getDescriptor().findServiceByName("PublishingService"),
//                            Wirelessmeshdomain.getDescriptor()
//                    );
//
//    public static void main(String... args) throws Exception {
//        LOG.info("started");
//        wirelessMeshService.start().toCompletableFuture().get();
//    }
//}
