# Assignment Extract

Source file: `OMU-Assignment-2601.pdf`

## Scenario

Asia Pacific University has moved into a new campus and wants to implement a kiosk system that allows students and staff to order drinks and snacks. The system is intended to reduce queues at a snack booth on the 3rd floor. Initially five kiosks are planned, with possible expansion to ten.

Users browse a menu of drinks and snacks and can customize selected items. Examples include changing coffee size, sugar, milk, ice, toppings, sandwich ingredients, and dressing choices.

Payment is made using the Student/Staff AP card. The user swipes the card at the kiosk, the order cost is deducted from the card balance, and the order is sent to the snack booth preparation area.

The system should be extensible, maintainable, and designed with concurrency in mind because five kiosks are linked to a central order notification system. Product information such as prices and availability should be updated centrally from the snack booth system and then take effect across all kiosks.

## Functional Requirements

### Order Kiosk

- Identify the user and show current credit or balance.
- View drinks and snacks.
- Modify or customize drinks and snacks.
- Order drinks and snacks.
- Update the user's AP card balance by deducting the order cost.
- Update product information such as price and availability.
- Log transactions at each kiosk.

### Admin-Staff System

- Receive order notifications including what was ordered and by whom.
- Update product availability and other product information.
- Support any extra relevant functionality.

## Assignment Requirements

1. Provide a high-level use case diagram with supporting descriptions of the main use cases.
2. Design a class diagram representing the system design.
3. Create sequence diagrams for three use cases.
4. Provide an updated class diagram with two to three design patterns.
5. Provide sequence diagrams showing the interaction of the patterns.
6. Implement the patterns in Java or C++.
7. Write a brief evaluation of the solution, pattern choices, and the suitability of each pattern for object-oriented solutions.

## Report Deliverables

- Cover page.
- Table of contents.
- Overview of the scenario.
- Use case diagram with brief description.
- Class diagram without design patterns.
- Sequence diagrams with brief description.
- Brief report describing the use of design patterns.
- Refined class diagram with design patterns and brief description.
- Critical appraisal report.
- References, if appropriate.

