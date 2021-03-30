# Chapter 10: Using services to decouple modules

## The service architecture is made up of four parts:

  - The *service* is a class or an interface.
  - The *provider* is a concrete implementation of the service.
  - The *consumer* is any piece of code that wants to use a service.
  - The `ServiceLoader` creates and returns an instance of each provider of a given service to consumers.

Requirements and recommendations for the service type are as follows:

  - Any class or interface can be a service, but because the goal is to provide maximum flexibility to consumers and providers, it’s recommended to use interfaces (or, at the least, abstract classes).
  - Service types need to be public and in an exported package. This makes them part of their module’s public API, and they should be designed and maintained appropriately.
  - The declaration of the module defining a service contains no entry to mark a type as a service. A type becomes a service by consumers and providers using it as one.
  - Services rarely emerge randomly, but are specifically designed for their purpose. Always consider making the used type not the service but a factory for it. This makes it easier to search for a suitable implementation as well as to control when instances are created and in which state.

Requirements and recommendations for providers are as follows:

  - Modules providing services need to access the service type, so they must require the module containing it.
  - There are two ways to create a service provider: a concrete class that implements the service type and has a provider constructor (a `public`, parameterless constructor), or a type with a provider method (a `public`, `static`, parameterless method called *`provide`*) that returns an instance implementing the service type. Either way, the type must be `public`, but there’s no need to export the package containing it. On the contrary, it’s advisable not to make the providing type part of a module’s public API.
  - Modules providing services declare that by adding a `provides ${service} with ${provider}` directive to their descriptor.
  - If a modular JAR is supposed to provide services even if placed on the class path, it also needs entries in the `META-INF/services` directory. For each `provides ${service} with ${provider}` directive, there must be a plain file called `${service}` that contains one line per `${provider}` (all names must be fully qualified).

Requirements and recommendations for consumers are as follows:

  - Modules consuming services need to access the service type, so they must require the module containing it. They shouldn’t require the modules providing that service, though — on the contrary, that would be against the main reason to use services in the first place: to decouple consumers and providers.
  - There’s nothing wrong with service types and the service’s consumers living in the same module.
  - Any code can consume services regardless of its own accessibility, but the module containing it needs to declare which services it uses with a `uses` directive. This allows the module system to perform service binding efficiently and makes module declarations more explicit and readable.
  - Services are consumed by calling `ServiceLoader::load` and then iterating or streaming over the returned instances by calling either `iterate` or `stream`. It’s possible that will be providers not found, and consumers must handle that case gracefully.
  - The behavior of code that consumes services depends on global state: which provider modules are present in the module graph. This gives such code undesirable properties like making it hard to test. Service loading should be pushed into setup code that creates objects in their correct configuration (for example, the dependency injection framework).
  - The service loader instantiates providers as late as possible. Its stream method even returns a `Stream<Provider<S>>`, where `Provider::type` can be used to access the `Class` instance for the provider. This allows searching for a suitable provider by checking class-level annotations without instantiating the provider yet.

Service-loader instances aren’t thread-safe. If they're used concurrently, synchronization must be provided.

All problems during loading and instantiating providers are thrown as ServiceConfigurationError. Due to the loader’s laziness, this doesn’t happen during load, but later in iterate or stream when problematic providers are encountered. Always be sure to put the entire interaction with ServiceLoader into a try block if you want to handle errors.

Some points about module resolution and more:

  - When module resolution processes a module that declares the use of a service, all modules providing that service are resolved and thus included in the application’s module graph. This is called *service binding*, and together with the use of services in the JDK, it explains why by default even small apps use a lot of platform modules.
  - The command-line option `--limit-modules`, on the other hand, does no service binding. As a consequence, providers that aren’t transitive dependencies of the modules given to this option don’t make it into the module graph and aren’t available at run time. The option can be used to exclude services, optionally together with `--add-modules` to add some of them back.
