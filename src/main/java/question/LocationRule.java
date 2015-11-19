package question;

import com.google.common.base.Joiner;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TypedDependency;
import generation.QuestionGenerator;
import tagging.NamedEntity;
import tagging.Sentence;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LocationRule implements Rule {

    @Override
    public Set<String> generateQuestions(Sentence sentence) {
        final Set<String> questions = new HashSet<>();
        System.out.println("Starting location scanning\n-----------------------------------");
        System.out.println("Sentence: '" + sentence.getString() + "'");
        processTree(sentence.getPosTree(), sentence, questions);
        System.out.println("-----------------------------------\nEnding location scanning");
        return questions;
    }

    private void processTree(Tree tree, Sentence sentence, Set<String> questions) {
        if (tree.label().value().equals("PP")) {
            System.out.println("Found a PP");
            validatePP(tree, sentence, questions);
            System.out.println();
        }
        for (final Tree child : tree.getChildrenAsList()) {
            processTree(child, sentence, questions);
        }
    }

    private void validatePP(Tree pp, Sentence sentence, Set<String> questions) {
        // Check the preposition of the PP
        if (pp.firstChild().firstChild().value().equals("in")) {
            System.out.println("PP starts with 'in'");
            final String phrase = Joiner.on(' ').join(pp.getChild(1).getLeaves());
            // Check the NP part of the PP to see if it is a location
            if (sentence.getNamedEntities().get(phrase) == NamedEntity.LOCATION) {
                System.out.println("NP is a location");
                // Check that the PP is contained within a VP
                final Tree ppParent = pp.parent(sentence.getPosTree());
                if (ppParent.label().value().equalsIgnoreCase("vp")) {
                    System.out.println("PP contained within VP");
                    constructQuestion(ppParent, sentence, questions);
                }
            }
        }
    }

    private void constructQuestion(Tree vp, Sentence sentence, Set<String> questions) {
        String verb = vp.firstChild().firstChild().value();

        final Tree verbTree = vp.getLeaves().get(0);
        final List<TypedDependency> verbDependencies = sentence.getDependenciesForLeaf(verbTree);
        String subject = "";
        for (final TypedDependency typedDependency : verbDependencies) {
            if (typedDependency.reln().getLongName().toLowerCase().contains("subj")) {
                subject = sentence.getNp(typedDependency.dep());
            }
            if (typedDependency.reln().getLongName().toLowerCase().contains("aux")) {
                verb = typedDependency.dep().originalText() + " " + verb;
            }
        }
        final String question = QuestionGenerator.generateLocationQuestion(verb, subject);
        System.out.println("Question generated: " + question);
        questions.add(question);
    }
}
