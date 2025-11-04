---
layout: default.md
title: "GhostConnect Developer Guide"
pageNav: 3
---

# GhostConnect Developer Guide

## Table of Contents

- [Acknowledgements](#acknowledgements)
- [Setting up, getting started](#setting-up-getting-started)
- [Design](#design)
    - [Architecture](#architecture)
    - [UI component](#ui-component)
    - [Logic component](#logic-component)
    - [Model component](#model-component)
    - [Storage component](#storage-component)
    - [Common classes](#common-classes)
- [Implementation](#implementation)
- [Documentation, logging, testing, configuration, dev-ops](#documentation-logging-testing-configuration-dev-ops)
- [Appendix: Requirements](#appendix-requirements)
  - [Product scope](#product-scope)
  - [User stories](#user-stories)
  - [Use cases](#use-cases)
  - [Non-Functional Requirements](#non-functional-requirements)
  - [Glossary](#glossary)
- [Appendix: Instructions for manual testing](#appendix-instructions-for-manual-testing)
  - [Launch and shutdown](#launch-and-shutdown)
  - [Deleting a person](#deleting-a-person)
  - [Saving data](#saving-data)

<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**


This project is based on [SE-EDU AddressBook Level 3](https://github.com/se-edu/addressbook-level3).
Parts of the code and documentation were adapted from it.



--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.

</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.
* To improve user experience, the parser normalises all command words to lower-case before dispatching them. This allows users to enter commands regardless of letter casing, which aims to improve user productivity.
* The original command casing is preserved in error messages and feedback to maintain consistency and UI outputs. This enhancement required no changes to individual command classes and does not affect the command's parameters or execution logic.
### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<box type="info" seamless>

**Note:** An alternative (arguably, a more OOP) model is given below. In the current implementation, each `Person` is assigned to a specific subclass(`Customer`, `Staff` or `Supplier`) upon creation, based on their category. This approach improves type safety and enables behaviour unique to each category, at the cost of minor duplication in category data.<br>

<puml src="diagrams/BetterModelClassDiagram.puml" width="450" />

</box>


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### Implementation overview

Most features follow the same end-to-end flow through the system:

1. Parse: The `Logic` layer receives raw input and delegates to `AddressBookParser`, which selects an `XYZCommandParser` to validate tokens and build an `XYZCommand`.
2. Execute: `LogicManager` executes the constructed `Command`. The command may read or mutate data via the `Model` interface.
3. Persist: Mutating commands call into `Model` which in turn persists changes through `Storage#saveAddressBook()`.
4. Respond: The command returns a `CommandResult` that encapsulates feedback text and UI flags (e.g., `showHelp`, `showPerson`, `exit`).
5. Update UI: The UI reacts to the `CommandResult` by updating panels and, when a flag is set, toggling windows (e.g., Help or View windows).

Error handling and validation happen at parse-time (missing/invalid parameters) and at execute-time (not-found targets, category mismatches, duplicates). Clear messages are returned to users without throwing unhandled exceptions into the UI.

### View Feature

The `view` command displays a read-only details window for a specific contact identified by phone number. It does not modify data or trigger storage writes.

1. User input parsing: `ViewCommandParser#parse()` tokenizes the input into an `ArgumentMultimap` and extracts `p/PHONE`.
2. Target resolution: `ViewCommand#execute()` reads `Model#getFilteredPersonList()` and locates the matching `Person` by phone.
3. UI signal: `ViewCommand` returns a `CommandResult` with a flag (e.g., `showPerson=true`) and the resolved `Person` reference.
4. UI handling: `LogicManager` returns the `CommandResult` to the UI, which opens the View window and renders the selected contact.
5. Result: A `CommandResult` message is shown; no storage calls are performed.

The sequence diagram below shows how the view operation works:

<puml src="diagrams/ViewSequenceDiagram.puml" width="700" />

#### Example usage scenario

Step 1. The user executes `view p/91234567`. The `Logic` component parses the phone and creates a `ViewCommand`.

Step 2. The command resolves the `Person` from the filtered list by phone number.

Step 3. `ViewCommand` returns a `CommandResult` with `showPerson=true` and the selected `Person`.

Step 4. The UI opens a View window owned by the main stage. The window renders category-specific fields (e.g., orders for suppliers).

Step 5. Closing the main window or executing `exit` closes all open View windows.

#### Design considerations

Aspect: How to identify the person to view

* Current choice: `p/PHONE` for unambiguous lookups across filtered lists.
    * Pros: Works independently of filtered index; stable identifier for direct commands.
    * Cons: Mixed identifier strategy vs commands that use index.
* Alternative: Use list index (e.g., `view INDEX`).
    * Pros: Consistent with `delete`/`edit`.
    * Cons: Index ambiguity under filters; requires additional flags (e.g., `/all`).

Aspect: Window lifecycle

* Current choice: Child windows are owned by the primary stage and tracked; `exit` and main-stage close will close all child windows.
* Alternative: Detached windows without ownership; requires explicit user management and risks orphan windows.

### Add Feature (Customer, Staff, Supplier)

The `add` command creates a new contact. The parser determines the category (`c/Customer`, `c/Staff`, or `c/Supplier`) and the command constructs the corresponding subtype before saving via the `Model`.

Steps common to all categories:

1. User input parsing: `AddCommandParser#parse()` tokenizes and validates fields, including the `c/CATEGORY` discriminator.
2. Person creation: `AddCommand#execute()` calls a factory (or inline constructor selection) to build the appropriate subtype (`Customer`, `Staff`, or `Supplier`).
3. Model update: `Model#addPerson(person)` persists the new contact into the in-memory list.
4. Storage: `Storage#saveAddressBook()` is invoked by the Model layer to write the updated state.
5. Result: A `CommandResult` confirms creation and echoes the new contact.

Below are the sequence diagrams per category:

#### Add Customer

<puml src="diagrams/AddCustomerSequenceDiagram.puml" width="700" />

#### Add Staff

<puml src="diagrams/AddStaffSequenceDiagram.puml" width="700" />

#### Add Supplier

<puml src="diagrams/AddSupplierSequenceDiagram.puml" width="700" />

#### Example usage scenario

Step 1. The user executes `add n/James Ho p/98765432 e/james@example.com a/123 c/Supplier`.

Step 2. `AddCommandParser` parses fields and recognizes `c/Supplier`.

Step 3. `AddCommand` constructs a `Supplier` instance and calls `Model#addPerson`.

Step 4. `Model` saves via `Storage#saveAddressBook()`.

Step 5. A success `CommandResult` is returned and the UI list updates.

#### Design considerations

Aspect: Single `add` command vs category-specific commands

* Current choice: Single `add` with `c/CATEGORY` discriminator.
    * Pros: Smaller command surface; consistent UX.
    * Cons: Parser complexity for category-specific validations.
* Alternative: `addCustomer`, `addStaff`, `addSupplier` commands.
    * Pros: Simpler parsers; clearer errors per category.
    * Cons: More commands to learn; duplication across implementations.

Aspect: Duplicate detection

* Current choice: Detect duplicates by identity fields (e.g., same phone). Rejects insertion if duplicate exists.
* Alternative: Allow duplicates and warn; defer deduplication to a later merge step.

### Add Order Feature (Supplier)

The `addOrder` command appends a new `Order` entry to a Supplier identified by phone number.

1. User input parsing: `AddOrderCommandParser#parse()` tokenizes `p/PHONE`, `i/ITEM_NAME`, `q/QUANTITY`, `u/UNIT_PRICE`, and `d/DELIVERY_DAY`.
2. Target resolution: `AddOrderCommand#execute()` locates the Supplier in `Model#getFilteredPersonList()` by phone; validates that the person is a `Supplier`.
3. Order creation: Builds a domain `Order` (and value objects) from the parsed parameters.
4. Model update: Creates an updated `Supplier` with the new order appended and calls `Model#setPerson(original, updated)`.
5. Storage: Persists via `Storage#saveAddressBook()`.
6. Result: Returns a `CommandResult` indicating the order was added.

The sequence diagram below shows how adding an order works:

<puml src="diagrams/AddOrderSequenceDiagram.puml" width="700" />

#### Example usage scenario

Step 1. The user executes `addOrder p/91234567 i/chicken q/20 u/5.60 d/every Tuesday`.

Step 2. The parser validates quantity and unit price, and resolves the supplier by phone.

Step 3. The command constructs an `Order` and updates the supplier via `Model#setPerson`.

Step 4. The model persists to storage and returns a success `CommandResult`.

#### Design considerations

Aspect: Supplier immutability and updates

* Current choice: Treat `Supplier` as immutable. Create an updated copy with appended orders and replace via `Model#setPerson`.
    * Pros: Clear change semantics.
    * Cons: Requires object copying; potential overhead for large order lists.

Aspect: Duplicate order rules

* Current choice: Prevent exact-duplicate orders (all fields equal) for the same supplier.
* Alternative: Allow duplicates but aggregate in UI; increases complexity for summaries.

Aspect: Identifier used for targeting

* Current choice: `p/PHONE` targets supplier regardless of filtered list.
* Alternative: Use list index or a unique ID.


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

Tech-savvy ghost kitchen managers who:
* handle frequent staff turnover and changing supplier contacts
* handles a customer loyalty program
* need to quickly retrieve and update contacts
* prefer typing over mouse interactions
* is reasonably comfortable using CLI apps

**Value proposition**: Our app helps ghost kitchen managers efficiently manage their diverse contact ecosystem — suppliers, customers, and staff. By offering categorisation, staff shift management, supplier orders management and customer membership management. Managers can locate, update, and organise contacts faster than with traditional GUI-driven systems, ensuring smoother operations and fewer mix-ups.


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                                  | I want to …​                                     | So that I can…​                                                                                                              |
|----------|------------------------------------------|--------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------|
| `* * *`  | new user unfamiliar with the system      | see usage instructions                           | refer to instructions when I forget how to use the App.                                                                      |
| `* * *`  | manager                                  | add a new contact                                | add entries to store contact details.                                                                                        |
| `* * *`  | manager handling many Persons            | categorise a contact (Supplier, Staff, Customer) | perform specific actions depending on the contact's category.                                                                |
| `* * *`  | manager                                  | view all contacts in the address book            | go through all entries with ease.                                                                                            |
| `* * *`  | detail-oriented manager                  | edit a contact                                   | update changed phone numbers, emails or addresses.                                                                           |
| `* * *`  | manager with many contacts               | find a contact by name                           | locate details of persons without having to go through the entire list.                                                      |
| `* * *`  | manager with many contacts               | find a contact by category                       | locate details of all persons under a specific category without having to go through the entire list.                        |
| `* * *`  | manager                                  | delete a contact                                 | remove entries that I no longer need.                                                                                        |
| `* * *`  | proactive and meticulous manager         | clear all contacts in the address book           | have access to a clean copy of the address book.                                                                             |
| `* *`    | manager processing customer transactions | add points for Customers                         | keep track of the points Customers have obtained.                                                                            |
| `* *`    | manager processing customer transactions | reduce points for Customers                      | keep track of the points Customers have redeemed.                                                                            |
| `* * `   | manager in charge of work schedule       | update shift timing for Staff                    | track who is working that day.                                                                                               |
| `* * `   | manager handling inventory               | add orders for Suppliers                         | keep track of delivery from specific suppliers.                                                                              |
| `* * `   | manager handling inventory               | update orders for Suppliers                      | update changed quantity, unit price, etc.                                                                                    |
| `* *`    | manager handling inventory               | delete orders from Suppliers                     | remove entries indicating completed or cancelled deliveries.                                                                 |
| `* *`    | manager                                  | save the data given when application closes      | retain all contact details, ensuring no information is lost and work can be resumed seamlessly when application is reopened. |

### Use cases

(For all use cases below, the **System** is the `GhostConnect` and the **Actor** is the `Manager`, unless specified otherwise)

**U1. Add a new contact**

**Actor: Manager**

**Preconditions**

* Application is running.
* Category is one of: `Customer`, `Staff`, `Supplier`.
* No existing contact with the same phone number.

**MSS**

1. Manager executes `add n/NAME p/PHONE e/EMAIL a/ADDRESS c/CATEGORY`.
2. System validates the fields and category.
3. System creates the contact and saves it to storage.
4. System displays a success message with the added contact. 
5. Use case ends.

**Extensions**

* 1a. Required fields missing or invalid.
    * 1a1. System indicates the exact invalid/missing field(s).
    * Use case ends.

* 2a. Potential duplicate detected (e.g., same phone).
    * 2a1. System informs manager of the duplicate; contact is not added.
    * Use case ends.

**U2. Search for a contact**

**Actor: Manager**

**MSS**

1. Manager executes `find KEYWORD [MORE_KEYWORDS]`.
2. System displays the list of matching contacts.
3. Manager optionally executes `view p/PHONE` to view a contact’s details. 
4. Use case ends.

**Extensions**

* 1a. No keyword provided.
    * 1a1. System indicates the correct command usage.
    * Use case ends.
* 2a. No matches found.
    * 2a1. System informs the Manager that no matches were found.
    * Use case ends.

**U3. Adding an order to a Supplier**

**Actor: Manager**

**Preconditions**

* Target contact exists and is a `Supplier`.

**MSS**

1. Manager executes `addOrder p/PHONE i/ITEM_NAME q/QUANTITY u/UNIT_PRICE [d/DELIVERY_DAY]`.
2. System validates inputs and resolves the contact by phone.
3. System appends the new order to the supplier and saves to storage.
4. System displays a success message. 
5. Use case ends.

**Extensions**

* 2a. Target contact not found.
    * 2a1. System informs the manager that no contact with the matching phone number was found.
    * Use case ends.
* 2b. Target contact is not a Supplier.
    * 2b1. System informs the manager that the contact is not a Supplier.
    * Use case ends.
* 2c. Invalid quantity or unit price format.
    * 2c1. System indicates the exact invalid field with constraints.
    * Use case ends.
* 3a. An identical order already exists for that supplier.
    * 3a1. System informs the manager; duplicate order is not created.
    * Use case ends.

**U4. Updating an order for a Supplier**

**Actor: Manager**

**Preconditions**

* Target contact exists and is a `Supplier`.
* The specified order index exists for that supplier.

**MSS**

1. Manager executes `updateOrder p/PHONE o/ORDER_INDEX [i/ITEM_NAME] [q/QUANTITY] [u/UNIT_PRICE] [d/DELIVERY_DAY]`.
2. System validates inputs, resolves the supplier by phone, and checks that the order index exists.
3. System updates the order, saves to storage, and returns a success message. 
4. Use case ends.

**Extensions**

* 2a. Target contact not found.
    * 2a1. System informs the manager that no contact with the matching phone number was found.
    * Use case ends.
* 2b. Contact is not a Supplier.
    * 2b1. System informs the manager accordingly.
    * Use case ends.
* 2c. Order index out of range.
    * 2c1. System informs the manager that the specified order cannot be found.
    * Use case ends.
* 2d. No fields to update are specified.
    * 2d1. System indicates the correct command usage and constraints.
    * Use case ends.
* 3a. The updated order would duplicate an existing order.
    * 3a1. System informs the manager; update is not applied.
    * Use case ends.

**U5. View contact details**

**Actor: Manager**

**Preconditions**

* Target contact exists.

**MSS**

1. Manager executes `view p/PHONE`.
2. System resolves the contact by phone and returns a result indicating the UI should open the View window.
3. UI opens the View window showing the contact’s details. 
4. Use case ends.

**Extensions**

* 1a. Contact not found.
    * 1a1. System informs the manager that the contact cannot be found.
    * Use case ends.


**U6. Update staff shift**

**Actor: Manager**

**Preconditions**

* Target contact exists and is a `Staff` member.

**MSS**

1. Manager executes `updateShift p/PHONE s/SHIFT` (e.g., `s/AM` or `s/PM`).
2. System validates the shift and resolves the staff by phone.
3. System updates the staff shift, saves to storage, and returns a success message. 
4. Use case ends.

**Extensions**

* 2a. Contact not found.
    * 2a1. System informs the manager that the contact cannot be found.
    * Use case ends.
* 2b. Contact is not `Staff`.
    * 2b1. System informs the manager accordingly.
    * Use case ends.
* 2c. Invalid shift value.
    * 2c1. System indicates allowed values and correct usage.
    * Use case ends.


**U7. Update customer points**

**Actor: Manager**

**Preconditions**

* Target contact exists and is a `Customer`.

**MSS**

1. Manager executes `updatePoints p/PHONE b/BILL_AMOUNT`.
2. System validates the bill amount and resolves the customer by phone.
3. System updates the customer’s points (and tier if applicable), saves to storage, and returns a success message. 
4. Use case ends.

**Extensions**

* 2a. Contact not found.
    * 2a1. System informs the manager that the contact cannot be found.
    * Use case ends.
* 2b. Contact is not `Customer`.
    * 2b1. System informs the manager accordingly.
    * Use case ends.
* 2c. Invalid bill amount.
    * 2c1. System indicates constraints (e.g., non-negative, 2 d.p.).
    * Use case ends.


**U8. Delete an order from a Supplier**

**Actor: Manager**

**Preconditions**

* Target contact exists and is a `Supplier`.
* The specified order index exists for that supplier.

**MSS**

1. Manager executes `deleteOrder p/PHONE o/ORDER_INDEX`.
2. System validates inputs, removes the order, saves to storage, and returns a success message. 
3. Use case ends.

**Extensions**

* 1a. Contact not found.
    * 1a1. System informs the manager that the contact cannot be found.
    * Use case ends.
* 1b. Contact is not `Supplier`.
    * 1b1. System informs the manager accordingly.
    * Use case ends.
* 1c. Order index out of range.
    * 1c1. System informs the manager that the order cannot be found.
    * Use case ends.


**U9. Edit a contact**

**Actor: Manager**

**Preconditions**

* The specified index exists in the currently displayed list.

**MSS**

1. Manager executes `edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [c/CATEGORY]`.
2. System validates the fields and applies the updates.
3. System saves to storage and displays a success message. 
4. Use case ends.

**Extensions**

* 1a. Index out of range.
    * 1a1. System informs the manager that the index is invalid.
    * Use case ends.
* 1b. No fields provided to update.
    * 1b1. System indicates correct usage and constraints.
    * Use case ends.
* 2a. Updated phone duplicates an existing contact.
    * 2a1. System informs the manager; edit is not applied.
    * Use case ends.


**U10. Delete a contact**

**Actor: Manager**

**Preconditions**

* The specified index exists in the currently displayed list.

**MSS**

1. Manager executes `delete INDEX`.
2. System removes the contact, saves to storage, and displays a success message. 
3. Use case ends.

**Extensions**

* 1a. Index out of range.
    * 1a1. System informs the manager that the index is invalid.
    * Use case ends.


**U11. List contacts**

**Actor: Manager**

**MSS**

1. Manager executes `list`.
2. System displays all contacts. 
3. Use case ends.

**U12. Clear all contacts**

**Actor: Manager**

**MSS**

1. Manager executes `clear`.
2. System deletes all contacts, saves to storage, and displays a success message. 
3. Use case ends.

**U13. Show help**

**Actor: Manager**

**MSS**

1. Manager executes `help`.
2. System opens the Help window and displays the command summary and examples. 
3. Use case ends.

**U14. Exit the application**

**Actor: Manager**

**MSS**

1. Manager executes `exit`.
2. System closes the Help and View windows, then closes the main window and terminates. 
3. Use case ends.

**U15. Reduce Customer Points**

**Actor: Manager**

**MSS**
1. Manager selects a customer to reduce points from.
2. Manager executes **reducePoints** and inputs details
3. System validates the inputs and locates the specified customer.
4. System deducts the specified number of points from the customer's total.
   Use case ends.

**Extensions**
* 3a. Contact not found or not a customer
*     3a1. System informs the manager that the specified contact is not a valid customer
Use case resumes at step 1 or ends
* 4a. Amount to be reduced exceeds customer's current points
*     4a1. System informs manager that insufficient points are available. 
Use case resumes at step 2 or ends.

**U16. View Customer Summary**

**Actor: Manager**

**MSS**
1. Manager executes **customerSummary**
2. System retrieves all customers from address book
3. System groups customer by their membership tier.
4. System displays the number of customers in each tier.
   Use case ends.

**Extensions**
* 2a. No customers exist in the address book.
*     2a1. System informs the manager that there are no customers to summarise.
Use case ends.

### Non-Functional Requirements

1.  Should work on any _mainstream OS_ as long as it has Java `17` or above installed.
2.  Should be able to hold up to 1000 persons without a noticeable sluggishness in performance for typical usage.
3.  A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4.  Adding or deleting contacts should complete within 1 second.
5.  All contacts must be saved to persistent storage and retrievable after application restarts.
6.  Commands should follow a predictable format (`add`, `delete`, `find`, `list`) with consistent feedback.
7.  System should alert users of invalid inputs without crashing.
8.  Clear and actionable error messages should be displayed for invalid inputs or commands.
9.  Commands and searchable fields (name) should be case-insensitive.
10. Validation rules should be in place to prevent duplicate entries.
11. Leading and trailing whitespaces should be trimmed and treated as a single spacing for storage and data operations.
12. Contact management features (add, delete, search, list) should be modular to allow scalability.
13. The project should not use any licensed image without attribution.
14. The system should ensure persistent data storage is updated immediately whenever data is added, edited or deleted.
15. The system should prevent accidental exposure or loss of contact data due to improper file handling.
16. All modules (add, delete, search, list) should be independently testable.
17. The system should maintain detailed logs of all add, delete, and edit operations for auditing and debugging.
18. Code should be documented to allow future developers to understand and maintain it.
19. Error messages should be understandable by non-technical users.

### Glossary

* **Manager**: Ghost kitchen manager that is using the application
* **Customer**: Customers who have signed up for a membership for a loyalty program
* **Fuzzy Search**: A search technique to find results approximately matching a query instead of an exact match
* **GUI-Driven Systems**: Software systems that depend on visual elements like buttons and menus for user interaction
--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for Manual Testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;testers are expected to do more *exploratory* testing.

</box>

---

### Launch and Shutdown

1. **Initial launch**

    1. Download the `.jar` file and place it in an empty folder.
    2. Double-click the `.jar` file.  
       **Expected:** The GUI appears with a set of sample contacts (customers, suppliers, staff). The window size may not be optimum.

2. **Saving window preferences**

    1. Resize the window to an optimum size. Move the window to a different location. Close the window.
    2. Re-launch the app by double-clicking the `.jar` file.  
       **Expected:** The most recent window size and location are retained.

---

### Adding a Contact

1. **Add a new customer**

    1. Prerequisites: None.
    2. Test case:
       ```
       add n/James Ho p/98765432 e/jamesho@example.com a/123, Clementi Rd, 1234665 c/Customer
       ```  
       **Expected:** A new contact named *James Ho* is added to the list. Status message confirms addition.

2. **Invalid add commands to try:**  
   `add n/James` (missing fields),  
   `add p/98765432` (missing name), etc.  
   **Expected:** Error message shown in the result display area indicating invalid command format.

---

### Editing a Contact

1. **Edit an existing contact**

    1. Prerequisites: At least one contact listed using the `list` command.
    2. Test case:
       ```
       edit 1 n/James Lee e/jameslee@example.com
       ```  
       **Expected:** Assuming no other contact uses the same name or number, contact at index 1 is updated with the new details. Status message confirms successful edit.

2. **Invalid edit commands to try:**  
   `edit`, `edit 0`, `edit n` (where *n* is larger than the list size).  
   **Expected:** Error message displayed. No contact updated.

---

### Finding and Viewing Contacts

1. **Finding contacts by name**

    1. Prerequisites: Several contacts with different names exist.
    2. Test case:
       ```
       find James Jake
       ```  
       **Expected:** Only contacts with names containing “James” or “Jake” are listed. Status message confirms success.

2. **Viewing a contact by phone**
    1. Prerequisites: Contact with the phone number exists.
    2. Test case:
  ```
  view p/98765432
  ```  
  **Expected:** Displays a pop up with detailed information for the contact with that phone number. Status message confirms success.

---

### Deleting a Contact

1. **Deleting a contact while all contacts are shown**

    1. Prerequisites: Multiple contacts in the list (`list` command used).
    2. Test case:
       ```
       delete 1
       ```  
       **Expected:** The first contact is deleted. Status message shows details of the deleted contact and success.

    3. Test case:
       ```
       delete 0
       ```  
       **Expected:** No contact deleted. Error message shown. 
   
    4. Other incorrect delete commands to try:  
       `delete`, `delete n` (where *n* exceeds list size).  
       **Expected:** Similar error behavior as above.

---

### Customer Management

1. **Updating customer loyalty points**

    1. Prerequisites: A contact with category `Customer` and valid phone number.
    2. Test case:
       ```
       updatePoints p/98765432 b/100.00
       ```  
       **Expected:** The customer’s loyalty points increase based on the truncated bill amount, unless amount causes customer to exceed maximum points of **100,000**. Customer tier is updated according to the number of points. Status message confirms update.

    3. Invalid test case:  
       `updatePoints p/123456 b/-50`  
       **Expected:** Error message displayed for invalid bill amount.

2. **Reducing customer loyalty points**
    1. Prerequisites: A contact with category `Customer` and valid phone number.
    2. Test case:
        ```
        reducePoints p/98765432 pts/500
        ```  
        **Expected:** Customer’s loyalty points are reduced and tier is updated accordingly. Unless reduce by amount exceeds maximum points of **100,000**. Status message confirms reduction.

    3. Invalid test case:  
        `reducePoints p/98765432 pts/-10`  
        **Expected:** Error message displayed.

---

### Staff Management

1. **Updating staff shift**

    1. Prerequisites: A contact with category `Staff` with valid phone number.
    2. Test case:
       ```
       updateShift p/91234567 s/PM
       ```  
       **Expected:** Staff’s shift is updated. Status message confirms successful update.

    3. Invalid test case:  
       `updateShift p/91234567 s/Midnight`  
       **Expected:** Error message displayed (invalid shift value).

---

### Supplier Management

1. **Adding a supplier order**

    1. Prerequisites: A contact with category `Supplier` with valid phone number.
    2. Test case:
       ```
       addOrder p/91234567 i/Chicken q/20 u/5.60 d/every Tuesday
       ```  
       **Expected:** A new order is added to the supplier. Order details are shown in the person card for the contact. Status message confirms successful update.

2. **Updating a supplier order**

    1. Prerequisites: A `Supplier`with an order with valid phone number.
    2. Test case:
       ```
       updateOrder p/91234567 o/1 i/Fish
       ```  
       **Expected:** The specified order is updated successfully in the supplier's record, unless the edit does not actually change anything in the order. Status message confirms successful update.

3. **Deleting a supplier order**

    1. Prerequisites: A `Supplier` with an order with valid phone number.
    2. Test case:
       ```
       deleteOrder p/91234567 o/1
       ```  
       **Expected:** The specified order is deleted from the supplier’s record. Status message confirms successful delete.

---

### Saving Data

1. **Handling missing/corrupted data files**

    1. Simulate by deleting or modifying the `data/ghostconnect.json` file.  
       **Expected:** Application starts with an empty contact list or default sample data.  
       **Error message** should be displayed in the result area, indicating corrupted or unreadable data file.

---

### Exiting the Application

1. **Exit command**

    1. Test case:
       ```
       exit
       ```  
       **Expected:** Application closes cleanly, including any pop-up windows. The latest data and window preferences are saved automatically. 

---

## **Appendix: Effort**
Compared to the baseline AddressBook-Level 3 (AB3), this project is significantly more complex. While AB3 is a single-entity (Person) application with a flat structure, GhostConnect manages multiple interrelated entity types (e.g., Customer, Supplier, Staff). This introduced a higher order of complexity in areas such as:

- Data Model: Designing a normalized and scalable relational schema.
- Logic: Implementing features that require coordination and validation across different entities (e.g., cascading deletes, status updates).
- UI: Presenting and navigating a more intricate data structure in a user-friendly way.

We estimate the overall difficulty to be 2.5x to 3x that of the AB3 project.

Challenges Faced

- Planning System Design: Planning the system design required us to rationalize the choices we made and evaluating trade-offs made to ensure scalability, efficiency and alignment of expectations within the team.
- Understanding the existing AB3 Code: Requiring understanding on the existing AB3 code requires careful analysis of its current structure and functionality and is challenging for users that do not have much experience in software engineering.
- Testing: Writing test cases involves creating comprehensive scenarios and good understanding of the code written to validate both the expected and the edge-case behaviors.
- Resolve Conflicts: Deconflicting entails merging divergent code changes while maintaining consistency and functionality.

Effort Required
- The development effort for each team member is estimated to be approximately 50 hours over the coding period.
- This effort was distributed across:
  - Design & Architecture (20%): Planning the entity relationships, command structure, and UI layout.
  - Core Implementation (50%): Coding the new entities, commands, and the primary logic.
  - Testing (20%): Writing and maintaining JUnit tests, fixing bugs, and performing user acceptance testing.
  - Documentation (10%): Writing the User Guide and Developer Guide and ensuring information written is consistent.

Achievements
- Despite the challenges, the project successfully delivered a robust and feature-rich application that surpasses the AB3 baseline. 
- Key achievements include:
  - A fully functional multi-entity management system with CRUD operations for all entities.
  - Advanced features such as search/filter across multiple fields and inter-entity linking.
  - Comprehensive test coverage ensuring reliability and ease of future maintenance.

Reused Components (Effort Saved: ~15%)
- A significant portion of development effort was saved by building upon and adapting the existing AB3 codebase and libraries.
- Components include:
  - AB3 Architecture & Boilerplate: The entire application architecture (e.g., Logic, Model, Storage, Ui component structure, command parsing pattern) was reused from AB3. This saved an immense amount of initial setup and boilerplate coding effort.
  - JavaFX Libraries: The UI leverages the JavaFX framework, reusing core components for tables, lists, and text fields. Our effort was focused on customizing and composing these components (TableView, ListView) rather than building them from scratch.
  - Third-Party Libraries:
    - Jackson Library: JSON serialization/deserialization for the storage layer was implemented using the Jackson library. Our work to adapt Jackson to our specific data model is contained primarily in the JsonAdaptedPerson, JsonAdaptedCategory, etc. classes.
    - JUnit: The testing framework was entirely reused, allowing us to focus on writing test cases instead of building a testing infrastructure.

This strategic reuse allowed the team to focus innovation on the project's unique and complex features rather than reinventing foundational elements.

## **Appendix: Planned Enhancements**
1. **Standardize all commands to use INDEX instead of PHONE for contact identification**: Currently, commands like `view`, `updatePoints`, `updateShift`, `addOrder`, `updateOrder`, and `deleteOrder` use phone numbers to identify contacts, while `edit` and `delete` use INDEX. This inconsistency can confuse users. We plan to update all category-specific commands to accept INDEX as the primary identifier (e.g., `updatePoints INDEX b/BILL_AMOUNT` instead of `updatePoints p/PHONE b/BILL_AMOUNT`). This will provide a uniform command structure across the application and allow users to work directly with the displayed list indices. The phone parameter will be deprecated in favor of INDEX for all commands.


2. **Allow all commands to access the full contact list regardless of active filters**: Currently, when users apply a `find` filter to display a subset of contacts, commands like `delete` and `edit` only operate on the filtered list indices. This can lead to confusion when users expect to operate on the full list. We plan to implement a dual-index system where commands can optionally accept a `/all` flag to operate on the full list (e.g., `delete /all 5` to delete the 5th contact in the full list even when viewing filtered results). Alternatively, we will add a command like `deleteById PHONE` to unambiguously target contacts by their unique identifier regardless of current filter state.


3. **Update UI to display real-time shift information using LocalDateTime**: The current system displays staff shift as simply "AM" or "PM" without date context. We plan to enhance the UI to use Java's LocalDateTime API to show which staff members are currently on shift today, and highlight upcoming shifts for the next 7 days. The main display will show a visual indicator (e.g., green dot) next to staff currently on duty based on the current system time and their assigned shift. For suppliers, we will similarly highlight orders scheduled for delivery today using LocalDateTime comparison, making it easier for managers to prioritize urgent deliveries at a glance.


4. **Improve phone number validation to support international formats**: Currently, phone validation only accepts numeric digits with a length of 8, which doesn't account for international formats with country codes, extensions, or special formatting (e.g., +65-1234-5678, (123) 456-7890). We plan to enhance the phone number validation regex to accept common international formats including country code prefixes (+ symbol), hyphens, spaces, and parentheses, while still maintaining uniqueness validation. Example valid formats will include: `+65 9123 4567`, `+1 (123) 456-7890`, `123-456-7890`.


5. **Support special characters in names for internationalization**: The current name validation regex `^[A-Za-z0-9 ]+(\\([A-Za-z0-9 ]+\\))?$` does not support names with special characters common in many cultures (e.g., O'Brien, José, François, 李明). We plan to expand the regex to include common name characters such as apostrophes, hyphens, accented letters, and common Unicode characters used in names worldwide: `^[\\p{L}\\p{N} '\\-]+(\\([\\p{L}\\p{N} '\\-]+\\))?$`. This will allow managers to add contacts with culturally diverse names without workarounds.


6. **Standardize numeric input handling (bill amounts, unit price, quantities)**: Current numeric validations across `updatePoints` (bill amount), and Supplier orders (`u/UNIT_PRICE`, `q/QUANTITY`) are slightly inconsistent (e.g., acceptance of `50.1` vs strict 2 d.p., scientific notation handling, large values). We plan to unify rules across all commands:
    - Accept only non-negative numbers; `q/QUANTITY` must be a positive integer (`>= 1`).
    - Accept up to 2 decimal places for monetary fields (`b/BILL_AMOUNT`, `u/UNIT_PRICE`); auto-round inputs with >2 d.p. to 2 d.p. using bankers' rounding.
    - Reject scientific notation (e.g., `1e3`), negative values, and values exceeding sane upper bounds (e.g., `b <= 1,000,000.00`, `u <= 100,000.00`).
    - Display normalized values in feedback (e.g., `5` -> `5.00`) and store them normalized in JSON to ensure consistency.
    - Provide precise error messages, e.g., "UNIT_PRICE must be a positive number with up to 2 decimal places (max 100,000.00)."
      This makes numeric handling predictable and prevents subtle data inconsistencies across features.


7. **Enhance error messages to specify exact validation failures**: Current error messages like "Invalid command format!" are too generic. For example, when adding a contact with an invalid email, the user sees a general format error rather than specifically "Email format is invalid: example.com (missing @ symbol)". We plan to implement field-specific error messages that identify exactly which parameter failed validation and why. Example: "Invalid phone number: abc123 (phone numbers must contain only digits)", "Invalid address: X (address must be between 2-100 characters)".


8. **Implement undo functionality for accidental data modifications**: Currently, there is no way to recover from accidental deletions or edits except by manually re-entering data or restoring from the JSON file. We plan to implement an `undo` command that reverses the last data-modifying operation (add, edit, delete, updatePoints, etc.). The system will maintain a history stack of up to 10 previous states. Users can execute `undo` to revert changes and `redo` to reapply them. This will appear as: "Undone: Deleted contact John Doe" with the contact restored to the list.


9. **Add batch operations for efficiency with large contact lists**: Currently, users must execute commands one at a time, which is inefficient when performing similar operations on multiple contacts (e.g., deleting multiple outdated suppliers). We plan to add batch command support where users can specify multiple indices: `delete 1,3,5-7` to delete contacts 1, 3, 5, 6, and 7 in one operation. Similarly, `updatePoints 1,2,3 b/100.00` would update points for multiple customers simultaneously. The system will display: "Updated 3 contacts: John Doe, Jane Smith, Bob Lee" to confirm the batch operation.


10. **Implement data validation on JSON file edits to prevent corruption**: Currently, if users manually edit the `addressbook.json` file and introduce invalid data (e.g., negative points, invalid shift values, duplicate phone numbers), the application either crashes or silently discards all data, resulting in data loss. We plan to implement comprehensive JSON validation on startup that: (1) identifies specific validation errors with line numbers and field names, (2) displays a detailed error report to the user, (3) offers to load the file in "safe mode" with invalid entries marked for review rather than discarding everything, (4) provides a backup recovery option. Example message: "Warning: 3 contacts have invalid data (Contact 5: negative points value, Contact 8: invalid email format). Load in safe mode to review and fix issues?"

*End of Appendix.*
