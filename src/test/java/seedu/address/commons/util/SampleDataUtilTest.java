package seedu.address.commons.util;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.Person;
import seedu.address.model.util.SampleDataUtil;

public class SampleDataUtilTest {

    @Test
    public void getSamplePersons_coversCategoryTags() {
        Person[] people = SampleDataUtil.getSamplePersons();
        assertTrue(people.length > 0);

        for (Person p : people) {
            // assuming Person#getTags() returns a Set<Tag> and Tag#tagName is accessible or via toString()
            p.getTags().forEach(t -> {
                String v = t.toString().replace("[", "").replace("]", "");
                assertTrue(
                        v.equals("Customer") || v.equals("Supplier") || v.equals("Staff"),
                        "unexpected tag " + v
                );
            });
        }
    }
}
