# OMU Mini Project Report

## Cover Page

Project title: APU Campus Wide Drinks and Snack Ordering Kiosk System  
Module: Object Modelling with UML  
Coursework weight: 30%  
Implementation language: Java  
Diagram format: PlantUML  
Group members: Dubernet Mathieu, Cavaignac Romain, Haegeman Victor  

## Table of Contents

1. Scenario overview
2. Use case diagram and descriptions
3. Initial class diagram
4. Sequence diagrams for main use cases
5. Design pattern report
6. Refined class diagram with design patterns
7. Pattern interaction sequence diagrams
8. Critical appraisal
9. References

## 1. Scenario Overview

Asia Pacific University requires a campus-wide ordering system for drinks and snacks. Students and staff use kiosks to browse available menu items, customize drinks or snacks, place orders, and pay through their AP card balance. Confirmed orders are sent to the snack booth preparation area.

The system begins with five kiosks but should support future expansion. Product data such as price and availability must be maintained centrally by snack booth staff and synchronized across all kiosks. The design therefore prioritizes maintainability, extensibility, and concurrency awareness.

## 2. Use Case Diagram and Descriptions

Diagram: [`diagrams/plantuml/01-use-case.puml`](../diagrams/plantuml/01-use-case.puml)

Rendered diagram gallery: [`docs/all-diagrams.md`](all-diagrams.md)

### Main Actors

- Student or staff customer: browses, customizes, orders, and pays.
- Snack booth admin staff: updates menu data such as prices and availability.
- Preparation staff: receives confirmed orders.
- AP card service: identifies users and supports balance deduction.

### Main Use Cases

- Identify user: the customer swipes an AP card and the system retrieves identity and balance.
- Browse drinks and snacks: the kiosk displays currently available products.
- Customize item: the customer chooses supported options such as drink size, sugar, milk, ice, toppings, or sandwich ingredients.
- Place order: the kiosk creates an order from selected items.
- Pay by AP card: the system deducts the order total from the card balance.
- Deduct card balance: the AP card service updates the user's remaining credit after successful payment.
- Notify preparation area: confirmed orders are sent to snack booth staff.
- Log transaction: the kiosk records the payment and order event.
- Update product price or availability: admin staff centrally changes menu data.
- Synchronize kiosks: all kiosk menu caches receive the latest catalog state.
- View incoming orders: preparation staff view confirmed orders and customization details.

## 3. Initial Class Diagram

Diagram: [`diagrams/plantuml/02-class-initial.puml`](../diagrams/plantuml/02-class-initial.puml)

The initial design separates the core business objects from services. Domain classes such as `User`, `APCard`, `Product`, `Drink`, `Snack`, `Order`, `OrderItem`, `Customization`, and `Transaction` represent the main business concepts. Service classes such as `Kiosk`, `ProductCatalog`, `OrderService`, `PaymentService`, `TransactionLogger`, `OrderNotificationSystem`, and `AdminStaffSystem` coordinate the use cases.

This version intentionally avoids design patterns so the base requirements can be understood before refinement.

## 4. Sequence Diagrams for Main Use Cases

### Browse and Customize Item

Diagram: [`diagrams/plantuml/03-sequence-browse-customize.puml`](../diagrams/plantuml/03-sequence-browse-customize.puml)

The customer identifies themself, requests the menu, selects a product, and adds customizations. The kiosk retrieves available products from its local menu cache and updates the draft order with the customized item subtotal.

### Place Order and Pay

Diagram: [`diagrams/plantuml/04-sequence-place-order-payment.puml`](../diagrams/plantuml/04-sequence-place-order-payment.puml)

The customer confirms the order, the order total is calculated, payment is charged to the AP card, the transaction is logged, and the snack booth display is notified.

### Admin Product Update

Diagram: [`diagrams/plantuml/05-sequence-admin-update.puml`](../diagrams/plantuml/05-sequence-admin-update.puml)

Admin staff update product price or availability through the admin system. The central catalog stores the new product snapshot and pushes the update to kiosk menu caches.

## 5. Design Pattern Report

### Singleton: Central Catalog

The central catalog is modelled as a Singleton because product price and availability should have one authoritative source. This helps prevent five kiosks from maintaining conflicting product states. The Java implementation uses double-checked locking and thread-safe collections to support concurrent kiosk access.

Suitability: Singleton is suitable for shared infrastructure-like state, but it must be used carefully because global state can make testing and replacement harder. The design limits the Singleton to catalog coordination only.

### Observer: Kiosk Menu Synchronization

The Observer pattern allows each kiosk menu cache to subscribe to catalog changes. When admin staff update a product, the central catalog publishes an event and each kiosk cache refreshes its local snapshot.

Suitability: Observer is suitable because kiosks should react automatically to product changes without the admin system needing to know every kiosk directly. It supports extension when more kiosks are added.

### Factory Method: Product Creation

The Factory Method pattern separates drink and snack creation from the admin workflow. `DrinkFactory` and `SnackFactory` validate and create the correct product subtype from a `ProductRequest`.

Suitability: Factory Method is suitable because the system is expected to grow with more product types and customization rules. Creation logic can evolve without rewriting the admin system.

## 6. Refined Class Diagram with Design Patterns

Diagram: [`diagrams/plantuml/06-class-refined-patterns.puml`](../diagrams/plantuml/06-class-refined-patterns.puml)

The refined design introduces:

- `CentralCatalog` as the Singleton source of product data.
- `CatalogObserver`, `CatalogEvent`, and `KioskMenuCache` for Observer-based synchronization.
- `ProductFactory`, `DrinkFactory`, `SnackFactory`, `ProductFactoryProvider`, and `ProductRequest` for Factory Method creation.

The Java code mirrors this structure under:

- `src/main/java/omu/patterns/singleton`
- `src/main/java/omu/patterns/observer`
- `src/main/java/omu/patterns/factory`

## 7. Pattern Interaction Sequence Diagrams

### Observer Catalog Synchronization

Diagram: [`diagrams/plantuml/07-sequence-observer-catalog-sync.puml`](../diagrams/plantuml/07-sequence-observer-catalog-sync.puml)

Admin staff change product availability. `CentralCatalog` publishes a `CatalogEvent`, and each `KioskMenuCache` updates itself.

### Factory Product Creation

Diagram: [`diagrams/plantuml/08-sequence-factory-product-creation.puml`](../diagrams/plantuml/08-sequence-factory-product-creation.puml)

The admin system requests the correct factory for a product type, creates the product, and saves it to the central catalog.

### Singleton Catalog Access

Diagram: [`diagrams/plantuml/09-sequence-singleton-catalog-access.puml`](../diagrams/plantuml/09-sequence-singleton-catalog-access.puml)

Multiple kiosks access the same central catalog instance, ensuring consistent product data.

## 8. Critical Appraisal

The design addresses the main assignment requirements and keeps responsibilities separated. The domain model is small and readable, while services coordinate use cases without embedding too much object creation or synchronization logic.

The biggest strength is maintainability. Product creation, payment, transaction logging, notification, and catalog synchronization are isolated in separate classes. If a problem occurs, it can be traced to a focused module.

The main limitation is that the implementation is skeletal. A production system would need persistent storage, authentication against a real AP card service, stronger error recovery, retry logic for snack booth notifications, and a richer user interface.

Concurrency is considered through thread-safe card deduction, central catalog locks, concurrent maps, and copy-on-write observer lists. However, a real deployment would also need database transactions and distributed consistency controls across kiosk machines.

The selected patterns are appropriate for the scenario. Singleton centralizes catalog state, Observer synchronizes kiosks, and Factory Method supports future product expansion. The design avoids applying patterns where simple services are sufficient.

The solution is intentionally balanced for an UML and design-pattern mini project. It is stronger than a purely diagram-based submission because it includes a Java skeleton that maps to the refined design, but it does not pretend to be a complete production kiosk platform.

## 9. References

- Gamma, E., Helm, R., Johnson, R., and Vlissides, J. Design Patterns: Elements of Reusable Object-Oriented Software.
- Oracle Java documentation for collections, concurrency utilities, and `BigDecimal`.
- Assignment brief: `OMU-Assignment-2601.pdf`.
