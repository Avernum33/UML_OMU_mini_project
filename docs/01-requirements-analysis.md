# Requirements Analysis

## Actors

- Student or staff customer: uses the kiosk to browse, customize, order, and pay.
- Snack booth admin staff: manages product prices and availability.
- Preparation staff: receives and prepares order notifications.
- AP card service: verifies user identity and deducts credit from the card balance.

## Main Use Cases

- Identify user: read AP card details and retrieve the user's current balance.
- Browse menu: view available drinks and snacks from the synchronized catalog.
- Customize item: select supported options such as size, sugar, milk, ice, toppings, sandwich ingredients, and dressing.
- Place order: create an order containing one or more customized items.
- Pay by AP card: deduct the order total from the user's card balance.
- Notify preparation area: send confirmed order details to snack booth staff.
- Update product data: admin changes price or availability centrally.
- Synchronize kiosks: all kiosks receive the latest catalog changes.
- Log transaction: each kiosk records order and payment events for traceability.

## Non-Functional Requirements

- Maintainability: separate domain, service, pattern, and presentation responsibilities.
- Extensibility: support new drinks, snacks, customizations, and payment rules without major rewrites.
- Concurrency awareness: multiple kiosks can order while the central catalog changes.
- Consistency: product price and availability changes are made centrally and propagated.
- Traceability: transaction logging makes failures easier to isolate.

## Design Pattern Choices

- Singleton: `CentralCatalog` gives the campus-wide system one controlled source of product data.
- Observer: kiosks subscribe to catalog changes and update their local menu caches automatically.
- Factory Method: product factories isolate the creation of drinks and snacks, making future product families easier to add.

## Error Isolation Strategy

- Diagram files are separated by use case and pattern interaction.
- Java packages separate domain models, factories, observers, singleton catalog, and services.
- Transaction logging is isolated in `TransactionLogger`.
- Payment logic is isolated in `PaymentService`.
- Product synchronization is isolated in `CentralCatalog` and observer classes.

