# Validation Checklist

Use this checklist to isolate issues quickly.

## UML

- `01-use-case.puml`: confirms all required actors and main use cases are present.
- `02-class-initial.puml`: checks the base system model before patterns.
- `03-sequence-browse-customize.puml`: checks browsing and customization flow.
- `04-sequence-place-order-payment.puml`: checks order confirmation, payment, notification, and logging.
- `05-sequence-admin-update.puml`: checks central product update and kiosk synchronization.
- `06-class-refined-patterns.puml`: checks Singleton, Observer, and Factory Method integration.
- `07-sequence-observer-catalog-sync.puml`: checks Observer interaction.
- `08-sequence-factory-product-creation.puml`: checks Factory Method interaction.
- `09-sequence-singleton-catalog-access.puml`: checks Singleton catalog access.

## Code

- Domain errors: inspect `src/main/java/omu/domain`.
- Product creation errors: inspect `src/main/java/omu/patterns/factory`.
- Synchronization errors: inspect `src/main/java/omu/patterns/singleton` and `src/main/java/omu/patterns/observer`.
- Payment errors: inspect `src/main/java/omu/service/PaymentService.java`.
- Notification errors: inspect `src/main/java/omu/service/SnackBoothOrderDisplay.java`.
- End-to-end scenario errors: inspect `src/test/java/omu/PatternScenarioTest.java`.

