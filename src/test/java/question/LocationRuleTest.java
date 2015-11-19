package question;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.BeforeClass;
import org.junit.Test;
import tagging.StanfordParser;

import static org.junit.Assert.assertThat;

public class LocationRuleTest {
    private static Rule locationRule;

    @BeforeClass
    public static void init() {
        locationRule = new LocationRule();
    }

    @Test
    public void testGenerateQuestions() throws Exception {
        assertQuestionCreated("George Washington was born in Virginia", "Where was George Washington born?");
        assertQuestionCreated("The Rockets are a basketball team in Houston, Texas", "Where do the Rockets play?");
    }

    private void assertQuestionCreated(String sentence, String... question) {
        assertThat(locationRule.generateQuestions(StanfordParser.parseSentence(sentence)),
                IsIterableContainingInAnyOrder.containsInAnyOrder(question));
    }
}