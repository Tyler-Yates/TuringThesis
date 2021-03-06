package util;

import com.google.common.collect.ImmutableSet;
import edu.stanford.nlp.simple.Sentence;
import edu.stanford.nlp.trees.Tree;

import java.util.Set;

public class NerUtil {
    private static final Set<String> PERSON_WORDS = ImmutableSet.of("he", "she", "him", "her");

    /**
     * Returns whether the given string contains a word that is tagged as a PERSON.
     *
     * @param np the given string
     * @return {@code true} if the given string contains a word tagged as a PERSON
     */
    public static boolean isPerson(String np) {
        final Sentence sentence = new Sentence(np);
        for (String label : sentence.nerTags()) {
            if (label.equalsIgnoreCase("person")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns whether the word at the given index in the given sentence represents a person.
     *
     * @param sentence the given sentence
     * @param index    the given index
     * @return whether the word at the given index in the given sentence represents a person
     */
    public static boolean isPerson(Sentence sentence, int index) {
        final String word = sentence.word(index);
        if (PERSON_WORDS.contains(word.toLowerCase())) {
            return true;
        }
        return sentence.nerTag(index).equalsIgnoreCase("person");
    }

    /**
     * Returns whether the word at the given index in the given sentence represents a location.
     *
     * @param sentence the given sentence
     * @param index    the given index
     * @return whether the word at the given index in the given sentence represents a location
     */
    public static boolean isLocation(Sentence sentence, int index) {
        return sentence.nerTag(index).equalsIgnoreCase("location");
    }

    /**
     * Returns whether the word at the given index in the given sentence represents a date or time.
     *
     * @param sentence the given sentence
     * @param index    the given index
     * @return whether the word at the given index in the given sentence represents a date or time
     */
    public static boolean isDateOrTime(Sentence sentence, int index) {
        return sentence.nerTag(index).equalsIgnoreCase("date") || sentence.nerTag(index).equalsIgnoreCase("time");
    }

    /**
     * Returns whether the head word of the given tree in the given sentence represents a person.
     *
     * @param root the root of the phrase structure tree
     * @param tree the given tree
     * @return whether whether the head word of the given tree in the given sentence represents a person
     */
    public static boolean headOfTreeIsPerson(Sentence sentence, Tree root, Tree tree) {
        final int leafIndex = TreeUtil.getLeafIndex(root, TreeUtil.findHead(tree).getLeaves().get(0));
        if (leafIndex == -1) {
            System.err.println("----------------------------------------");
            System.err.printf("Could not find index for Tree [%s]\n", tree);
            System.err.printf("In Sentence [%s]\n", root);
            System.err.println("----------------------------------------");
            return false;
        }
        return isPerson(sentence, leafIndex);
    }

    /**
     * Returns whether the head word of the given tree in the given sentence represents a location.
     *
     * @param root the root of the phrase structure tree
     * @param tree the given tree
     * @return whether whether the head word of the given tree in the given sentence represents a location
     */
    public static boolean headOfTreeIsLocation(Sentence sentence, Tree root, Tree tree) {
        final int leafIndex = TreeUtil.getLeafIndex(root, TreeUtil.findHead(tree).getLeaves().get(0));
        if (leafIndex == -1) {
            System.err.println("----------------------------------------");
            System.err.printf("Could not find index for Tree [%s]\n", tree);
            System.err.printf("In Sentence [%s]\n", root);
            System.err.println("----------------------------------------");
            return false;
        }
        return isLocation(sentence, leafIndex);
    }

    /**
     * Returns whether the head word of the given tree in the given sentence represents a time or date.
     *
     * @param root the root of the phrase structure tree
     * @param tree the given tree
     * @return whether whether the head word of the given tree in the given sentence represents a time or date
     */
    public static boolean headOfTreeIsTime(Sentence sentence, Tree root, Tree tree) {
        final int leafIndex = TreeUtil.getLeafIndex(root, TreeUtil.findHead(tree).getLeaves().get(0));
        if (leafIndex == -1) {
            System.err.println("----------------------------------------");
            System.err.printf("Could not find index for Tree [%s]\n", tree);
            System.err.printf("In Sentence [%s]\n", root);
            System.err.println("----------------------------------------");
            return false;
        }
        return isDateOrTime(sentence, leafIndex);
    }

    /**
     * Returns either 'who' or 'what' based on the head word of the given tree.
     *
     * @param sentence the sentence containing the given tree
     * @param root     the root of the given sentence
     * @param tree     the given tree
     * @return 'who' if the head word is a person, 'what' otherwise
     */
    public static String getWhFromHead(Sentence sentence, Tree root, Tree tree) {
        if (headOfTreeIsPerson(sentence, root, tree)) {
            return "who";
        }
        return "what";
    }
}
