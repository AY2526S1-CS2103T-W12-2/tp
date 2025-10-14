package seedu.address.model.person;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;
import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person}'s {@code Name} or {@code Tags} matches any of the keywords given.
 */
public class NameOrTagContainsKeywordsPredicate implements Predicate<Person> {
    private final List<String> keywords;

    public NameOrTagContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Person person) {
        return keywords.stream()
                .anyMatch(keyword ->
                    StringUtil.containsWordIgnoreCase(person.getName().fullName, keyword)
                    || person.getTags().stream()
                        .anyMatch(tag -> StringUtil.containsWordIgnoreCase(tag.tagName, keyword))
                );
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof NameOrTagContainsKeywordsPredicate)) {
            return false;
        }

        NameOrTagContainsKeywordsPredicate otherPredicate = (NameOrTagContainsKeywordsPredicate) other;
        return keywords.equals(otherPredicate.keywords);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keywords", keywords).toString();
    }
}
