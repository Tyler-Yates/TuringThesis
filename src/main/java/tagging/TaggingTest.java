package tagging;

import com.google.common.base.Joiner;
import edu.stanford.nlp.trees.Tree;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

public class TaggingTest {
    final static StanfordParser parser = new StanfordParser();

    public static void main(String[] args) {
        checkArgument(args.length > 0, "Sentence must have at least one word");

        // Wait for the English model to load in order to keep debug text out of the results
        try {
            Thread.sleep(500L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        final String sentence = Joiner.on(' ').join(args);
        System.out.println("Sentence being analyzed: " + sentence + "\n---------------------------");

        final Tree tree = parser.parse(sentence).getChild(0);
        final List<Tree> trees = tree.getChildrenAsList();
        for (final Tree part : trees) {
            System.out.print(part.label());
            System.out.println(part);
        }
    }
}