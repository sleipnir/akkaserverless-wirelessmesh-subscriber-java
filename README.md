# Akka Serverless - Wireless Mesh Subscriber Example App

A Java-based example app for [Akka Serverless](https://developer.lightbend.com/docs/akka-serverless/)

Features include:

* A pubsub subscription application that creates a value entity that counts customer locations per zipcode. The publisher application is found [here](https://github.com/lightbend-labs/akkaserverless-wirelessmesh-java)

## What is this example?

To help you get started with Akka Serverless, we've built some example apps that showcase the capabilities of the platform. This example application mimics a company that uses Akka Serverless to keep track of the wireless mesh devices their customers have installed and the devices connected to the meshes. This application is interested in how many customer locations are within any given zipcode.

## Prerequisites

To build and deploy this example application, you'll need to have:

* An [Akka Serverless account](https://docs.cloudstate.com/getting-started/lightbend-account.html)
* Java 11 or higher installed
* Maven 3.x or higher installed
* The Docker CLI installed
* A [service account](https://cloud.google.com/docs/authentication/production) that can connect to your Google Cloud Pubsub
* A Pub/Sub topic called 'wirelessmesh' under your own Google Cloud account, following the read me of the the [publisher application](https://github.com/lightbend-labs/akkaserverless-wirelessmesh-java)

## Build, Deploy, and Test

### Prepare your Google Could Pubsub

Create a Pub/Sub topic called 'wirelessmesh' under your own Google Cloud account.

To connect Akka Serverless to your Google Cloud Pub/Sub you must authenticate using a service account. To create your [service account](https://cloud.google.com/docs/authentication/production#cloud-console). After creating your service account, you need to download the service account key as a JSON file.

Now use the [akkasls](https://developer.lightbend.com/docs/akka-serverless/getting-started/set-up-development-env.html) command-line tool to give Akka Serverless access to your broker:

```
akkasls project set broker --broker-service gcp-pubsub --gcp-key-file testing-pubsub-310212-fec7d0612927.json
```
### Build your container

To build your own container, run `mvn -Dnamespace=<namespace> clean install`, substituting `<namespace>` for a docker registry namespace you have write access to (for example your dockerhub username).

This command will create a new Docker image.

The result of the command should be:

```bash
...
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  01:31 min
[INFO] Finished at: 2021-01-20T16:20:29-08:00
[INFO] ------------------------------------------------------------------------
```

### Deploy your container

To deploy the container as a service in Akka Serverless, you'll need to:

1. Push the container to a container registry: `docker push -t <registry url>/<registry namespace>/akkaserverless-wirelessmesh-subscriber-java:latest`
1. Deploy the service in Akka Serverless: `akkasls svc deploy wirelessmesh <registry url>/<registry username>/akkaserverless-wirelessmesh-subscriber-java:latest`

_The above command will deploy your container to your default project with the name `wirelessmesh`. If you want to have a different name, you can change that._

### Testing your service

* Assuming you have deployed to akkaserverless and exposed your service to 'winter-mountain-2372.us-east1.apps.akkaserverless.com'...

Using Curl (use a real zipcode instead of 11111):
```
curl https://${AS_HOST}/geolocation/get-geolocation?zipcode=11111
```
* You should see a json response containing your customer locations within that zipcode.

## Contributing

We welcome all contributions! [Pull requests](https://github.com/lightbend-labs/akkaserverless-wirelessmesh-java/pulls) are the preferred way to share your contributions. For major changes, please open [an issue](https://github.com/lightbend-labs/akkaserverless-wirelessmesh-java/issues) first to discuss what you would like to change.

## Support

This project is provided on an as-is basis and is not covered by the Lightbend Support policy.

## License

See the [LICENSE](./LICENSE).
