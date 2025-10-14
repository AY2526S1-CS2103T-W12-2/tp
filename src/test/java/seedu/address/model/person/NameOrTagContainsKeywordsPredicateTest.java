package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class NameOrTagContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList("first");
        List<String> secondPredicateKeywordList = Arrays.asList("first", "second");

        NameOrTagContainsKeywordsPredicate firstPredicate =
                new NameOrTagContainsKeywordsPredicate(firstPredicateKeywordList);
        NameOrTagContainsKeywordsPredicate secondPredicate =
                new NameOrTagContainsKeywordsPredicate(secondPredicateKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        NameOrTagContainsKeywordsPredicate firstPredicateCopy =
                new NameOrTagContainsKeywordsPredicate(firstPredicateKeywordList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different keywords -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_nameContainsKeywords_returnsTrue() {
        // One keyword matching name
        NameOrTagContainsKeywordsPredicate predicate =
                new NameOrTagContainsKeywordsPredicate(Collections.singletonList("Alice"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        // Multiple keywords, one matching name
        predicate = new NameOrTagContainsKeywordsPredicate(Arrays.asList("Alice", "Carol"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        // Mixed-case keywords matching name
        predicate = new NameOrTagContainsKeywordsPredicate(Arrays.asList("aLIce", "bOB"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));
    }

    @Test
    public void test_tagContainsKeywords_returnsTrue() {
        // One keyword matching tag
        NameOrTagContainsKeywordsPredicate predicate =
                new NameOrTagContainsKeywordsPredicate(Collections.singletonList("Customer"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").withTags("Customer").build()));

        // Multiple keywords, one matching tag
        predicate = new NameOrTagContainsKeywordsPredicate(Arrays.asList("Customer", "Staff"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").withTags("Customer").build()));

        // Mixed-case keywords matching tag
        predicate = new NameOrTagContainsKeywordsPredicate(Arrays.asList("CustOmER"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").withTags("Customer").build()));

        // Multiple tags, keyword matches one of them
        predicate = new NameOrTagContainsKeywordsPredicate(Collections.singletonList("Supplier"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob")
                .withTags("Customer", "Supplier").build()));
    }

    @Test
    public void test_nameAndTagContainsKeywords_returnsTrue() {
        // Different keywords matching name and tag respectively
        NameOrTagContainsKeywordsPredicate predicate =
                new NameOrTagContainsKeywordsPredicate(Arrays.asList("Alice", "Customer"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").withTags("Customer").build()));
    }

    @Test
    public void test_nameAndTagDoNotContainKeywords_returnsFalse() {
        // Zero keywords
        NameOrTagContainsKeywordsPredicate predicate =
                new NameOrTagContainsKeywordsPredicate(Collections.emptyList());
        assertFalse(predicate.test(new PersonBuilder().withName("Alice").withTags("Customer").build()));

        // Non-matching keyword
        predicate = new NameOrTagContainsKeywordsPredicate(Arrays.asList("Carol"));
        assertFalse(predicate.test(new PersonBuilder().withName("Alice Bob").withTags("Customer").build()));

        // Keywords match phone, email and address, but do not match name or tags
        predicate = new NameOrTagContainsKeywordsPredicate(Arrays.asList("91234567", "alice@email.com",
                "Main", "Street"));
        assertFalse(predicate.test(new PersonBuilder().withName("Alice").withPhone("91234567")
                .withEmail("alice@email.com").withAddress("Main Street").withTags("Customer").build()));
    }

    @Test
    public void toStringMethod() {
        List<String> keywords = List.of("keyword1", "keyword2");
        NameOrTagContainsKeywordsPredicate predicate = new NameOrTagContainsKeywordsPredicate(keywords);

        String expected = NameOrTagContainsKeywordsPredicate.class.getCanonicalName() + "{keywords=" + keywords + "}";
        assertEquals(expected, predicate.toString());
    }
}
