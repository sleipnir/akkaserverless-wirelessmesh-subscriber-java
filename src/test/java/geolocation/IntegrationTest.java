///**
// * THIS TEST WILL NOT WORK WITH ANY LIBRARY GREATER THAN BETA.3!!!!!!!
// */
//
//package geolocation;
//
//import java.util.concurrent.ExecutionException;
//
//import com.akkaserverless.javasdk.AkkaServerless;
//import com.akkaserverless.javasdk.testkit.junit.AkkaServerlessTestkitResource;
//import org.junit.ClassRule;
//import org.junit.Test;
//import static org.junit.Assert.assertEquals;
//
//public class IntegrationTest {
//
//    private static final AkkaServerless GEOLOCATION = GeolocationMain.SERVICE;
//
//    @ClassRule
//    public static final AkkaServerlessTestkitResource testkit = new AkkaServerlessTestkitResource(GEOLOCATION);
//
//    private final GeolocationServiceClient client;
//
//    String zipcode = "07701";
//
//    public IntegrationTest() {
//        this.client = GeolocationServiceClient.create(testkit.getGrpcClientSettings(), testkit.getActorSystem());
//    }
//
//    @Test
//    public void addCustomerLocationTest() throws ExecutionException, InterruptedException {
//        String customerLocationId = "customerId1";
//
//        client.addCustomerLocation(GeolocationApi.AddCustomerLocationCommand.newBuilder()
//                .setCustomerLocationId(customerLocationId)
//                .setZipcode(zipcode)
//                .build())
//                .toCompletableFuture().get();
//
//        GeolocationApi.GeolocationState geolocationState = client.getGeolocation(
//                GeolocationApi.GetGeolocationCommand.newBuilder()
//                        .setZipcode("07760")
//                        .build()).toCompletableFuture().get();
//
//        System.out.println(geolocationState);
//        assertEquals(zipcode, geolocationState.getZipcode());
//        assertEquals(geolocationState.getCustomerLocationsList().size(), 1);
//    }
//}
