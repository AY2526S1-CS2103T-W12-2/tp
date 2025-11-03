---
layout: default.md
title: "Developer Guide"
pageNav: 3
---

# AB-3 Developer Guide

<details>
    <summary> Table of Contents </summary>

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

</details>

<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

_{ list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well }_
This project is based on [SE-EDU AddressBook Level 3](https://github.com/se-edu/addressbook-level3).
Parts of the code and documentation were adapted from it.
Fuzzy search functionality was inspired by the [Levenshtein Distance algorithm](https://en.wikipedia.org/wiki/Levenshtein_distance).


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

**Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

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

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

<puml src="diagrams/UndoRedoState0.puml" alt="UndoRedoState0" />

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

<puml src="diagrams/UndoRedoState1.puml" alt="UndoRedoState1" />

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

<puml src="diagrams/UndoRedoState2.puml" alt="UndoRedoState2" />

<box type="info" seamless>

**Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</box>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

<puml src="diagrams/UndoRedoState3.puml" alt="UndoRedoState3" />


<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</box>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

<puml src="diagrams/UndoSequenceDiagram-Logic.puml" alt="UndoSequenceDiagram-Logic" />

<box type="info" seamless>

**Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</box>

Similarly, how an undo operation goes through the `Model` component is shown below:

<puml src="diagrams/UndoSequenceDiagram-Model.puml" alt="UndoSequenceDiagram-Model" />

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</box>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

<puml src="diagrams/UndoRedoState4.puml" alt="UndoRedoState4" />

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

<puml src="diagrams/UndoRedoState5.puml" alt="UndoRedoState5" />

The following activity diagram summarizes what happens when a user executes a new command:

<puml src="diagrams/CommitActivityDiagram.puml" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_


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
* oversee multiple delivery-only brands and suppliers
* handle frequent staff turnover and changing supplier contacts
* need to quickly retrieve and update contacts
* prefer typing over mouse interactions
* is reasonably comfortable using CLI apps

**Value proposition**: Our app helps ghost kitchen managers efficiently manage their diverse contact ecosystem — suppliers, delivery riders, staff, and partner brands. By offering categorisation, relationship links, fuzzy search, tagging, and archiving, it streamlines contact management. Managers can locate, update, and organise contacts faster than with traditional GUI-driven systems, ensuring smoother operations and fewer mix-ups.


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                                       | I want to …​                                          | So that I can…​                                                        |
|---------|-----------------------------------------------|-------------------------------------------------------|------------------------------------------------------------------------|
| `* * *` | new user                                      | see usage instructions                                | refer to instructions when I forget how to use the App                 |
| `* * *` | manager                                       | add a new contact                                     |                                                                        |
| `* * *` | manager                                       | delete a contact                                      | remove entries that I no longer need                                   |
| `* * *` | manager                                       | find a contact by name                                | locate details of persons without having to go through the entire list |
| `* * *` | manager                                       | edit a contact                                        | update changed phone numbers, emails or addresses                      |
| `* * *` | manager                                       | categorise a contact (Supplier, Staff, Customer)      | filter contacts by groups                                              |
| `* * * ` | manager                                       | add orders for Suppliers                              | keep track of delivery from specific suppliers.                        |
| `* * *` | manager                                       | update orders for Suppliers                           | update changed quantity, unit price, etc                               |
| `* * `  | manager                                       | track customer spending                               | update their membership tier                                           |
| `* * `  | manager                                       | update staff shift timing                             | track who is working that day                                          |
| `* *`   | manager                                       | delete orders from specific suppliers                 | remove entries indicating completed or cancelled deliveries            |
| `* *`   | manager                                       | mark a staff contact as inactive (e.g., on leave)     | I don’t accidentally assign tasks to them.                             |
| `*`     | manager                                       | record staff shifts with their contacts               | reach out to the right staff on duty                                   |
| `* `    | manager                                       | attach notes to a contact                             | remember context like “delivers only on weekends”                      |
| `*`     | manager                                       | search for staff by their shift timings               | See all at once the staff that should be on duty                       |
| `*`     | manager with many persons in the address book | sort persons by name                                  | locate a person easily                                                 |
| `*`     | manager                                       | view usage analytics (e.g., most contacted suppliers) | optimise operations with insights                                      |

### Use cases

(For all use cases below, the **System** is the `AddressBook` and the **Actor** is the `user`, unless specified otherwise) <br>

Note: For all commands, if input format or required prefixes are missing, the system will reject the command and display the error message indicating the correct format. This validation is handled uniformly across all commands. <br>

**U1. Add a new contact**

**Actor: Manager**

**MSS**

1. Manager selects **Add Contact**
2. System prompts for contact details
3. Manager provides details and confirms.
4. System records the contact

    Use case ends.

**Extensions**

* 3a.Required fields missing.
    * 3a1. System indicates missing field
    * Use case resumes at step 2

* 3b. Potential duplicate detected.

    * 3b1. System informs manager of duplicate.
    * Use case resumes at step 2 or ends(if cancelled)

**U2. Search for a contact**

**Actor: Manager**

**MSS**

1. Manager initiates **Search Contact**
2. System prompts for search keyword
3. Manager enters search keyword
4. System displays list of matching contacts
5. Manager selects a contact to view details

    Use case ends.

**Extensions**

* 2a. No keyword provided
    * 2a1. System prompts for input.
    * Use case resumes at step 2.
* 3a. No matches found
    * 3a1. System informs Manager that no matches were found.
    * Use case ends.

**U3. Adding Orders to Supplier**

**Actor: Manager**

**MSS**

1. Manager selects contact.
2. Manager initiates **Add Order**.
3. Manager keys in order details, such as Item Name, Quantity, Unit Price and Delivery Day.
4. System adds order into the specified supplier's list of orders.

   Use case ends.

**Extensions**

* 1a. Target contact found is not a Supplier
    * 1a1. System informs manager that contact found is not a Supplier.
    * Use case ends.
* 3a. Order with identical values already exists
    * 3a1. System informs manager of the duplicate order, duplicate order is not created.
    * Use case ends.

**U4. Updating Order for Supplier**

**Actor: Manager**

**MSS**

1. Manager selects contact and order to be updated.
2. Manager initiates **Update Order**
3. Manager keys in the information to be updated for the order, such as Item Quantity, Item Name, etc
4. System updates the corresponding order in the specified supplier's order list.

   Use case ends.

**Extensions**

* 1a. Target contact not found
    * 1a1. System informs manager that no contact with the matching phone number was found.
    * Use case ends.
* 3a. Order with identical values already exists
    * 3a1. System informs manager of the duplicate order, duplicate order is not created.
    * Use case ends.

**U5. View contacts by category**

**Actor: Manager**

**MSS**

1. Manager selects **View by category**
2. System displays available categories
3. Manager chooses a category (e.g. Supplier & Staff)
4. System lists all contacts under category

   Use case ends.

**Extensions**

* 3a. No contacts in selected category
    * 3a1. System informs manager that no contacts were found.
    * Use case resumes at step 3 or ends.
* 4a. Large number of contacts to display.
    * 4a1. System displays contacts efficiently, allowing all results to be viewed without truncation.
    * Use case ends.

**U6. Edit an existing contact**

**Actor: Manager**

**MSS**

1. Manager selects contact to be edited.
2. Manager initiates **edit contact** and inputs details 
3. System validates inputs and updates corresponding fields of contact.
4. System displays success message. 

   Use case ends.

**Extensions**

* 3a. Contact does not exist in address book.
   * 3a1. System informs manager that contact cannot be found.
  
   Use case resumes at step 2 or ends.
* 3b. Input is invalid.
    * 3b1. System informs manager that input is invalid.

  Use case resumes at step 2 or ends.

**U7. List all contacts**

**Actor: Manager**

**MSS**

1. Manager executes list
2. System display all contacts in address book.
Use case ends

**Extensions**
None

**U8. Delete a contact**
**Actor: Manager** 
**MSS**

1. Manager chooses contact to be deleted.
2. Manager executes **delete** command and inputs details.
3. System validates inputs and removes the specified contact.
4. System displays the confirmation message.
Use case ends.
**Extensions**
* 1a. Chosen contact does not exist.
*     1a1. System informs manager that chosen contact is invalid.
Use case resumes at step 1 or ends. 

**U9. Clear all contacts**
**Actor: Manager**
**MSS**
1. Manager executes clear
2. System removes all contacts.
3. System displays confirmation message.
Use case ends.

**Extensions**
None

**U10. Exit the application**
**Actor: Manager**
**MSS**
1. Manager executes exit.
2. System saves all data and terminates.
Use case ends.

**U11. Update Customer Points**
**Actor: Manager**
**MSS**
1. Manager chooses customer to add points.
2. Manager executes **updatePoints** and inputs details 
3. System validates inputs, locates the customer and adds corresponding points. 

**Extension** 
* 1a. Contact is not a customer
*     1a1. System informs manager that chosen contact is not customer.
Use case resumes at step 1 or ends.

**U12. Update Staff Shift**
**Actor: Manager**
**MSS**
1. Manager chooses staff to update shift.
2. Manager executes **updateShift** and enters input.
3. System validates inputs, locates the staff and updates their shift.
4. System displays confirmation message.
Use case ends. 

**Extensions**
* 3a. Contact chosen is not a staff
*     3a1. System informs manager that chosen contact is not staff
Use case continues at step 1 or ends
*3a. Shift value is invalid
*     3a1. System informs manager that shift value is invalid
Use case continues at step 2 or ends. 

**U13. Delete an order**
**Actor: Manager**
**MSS**

1. Manager chooses supplier and the specified order from that supplier.
2. Manager executes **deleteOrder** and inputs details
3. System validates inputs, deletes the specified order from that supplier
4. System displays a confirmation message.
Use case ends.

**Extensions**
* 3a. Contact chosen is not a supplier
*     3a1. System informs manager that chosen contact is not a supplier.
Use case continues at step 1 or ends.
* 3b. Order chosen does not exist.
*     3b1. System informs manager that chosen order does not exist.
Use case continues at step 1 or ends. 

    

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
* **Archiving**: The process of storing data in an organized manner for long-term retention
--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases …​ }_

### Deleting a person

1. Deleting a person while all persons are being shown

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   1. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete 0`<br>
      Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

1. _{ more test cases …​ }_

### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_

## **Appendix: Effort**
wip

## **Appendix: Planned Enhancements**
1. Adding a command to deduct and update number of leaves for a staff.
2. Improving shift system to track shifts in the current month.
3. Improving membership system to implement discounts for customers in tiers.
