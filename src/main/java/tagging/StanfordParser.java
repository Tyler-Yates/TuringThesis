package tagging;

import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.parser.lexparser.*;
import edu.stanford.nlp.process.*;
import edu.stanford.nlp.trees.*;
import simplenlg.features.*;

import java.io.*;
import java.util.*;

public class StanfordParser {
    private static final String PCG_MODEL = "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz";

    private static final TokenizerFactory<CoreLabel> tokenizerFactory = PTBTokenizer.factory(
            new CoreLabelTokenFactory(), "invertible=true");
    private static final LexicalizedParser parser = LexicalizedParser.loadModel(PCG_MODEL);

    private static Tree getPosTree(String sentence) {
        final Tokenizer<CoreLabel> tokenizer = tokenizerFactory.getTokenizer(new StringReader(sentence));
        final List<CoreLabel> tokens = tokenizer.tokenize();
        return parser.apply(tokens);
    }

    private static Collection<TypedDependency> getDependencies(Tree sentenceParseTree) {
        final TreebankLanguagePack tlp = new PennTreebankLanguagePack();
        final GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
        final GrammaticalStructure gs = gsf.newGrammaticalStructure(sentenceParseTree);
        return gs.typedDependenciesCollapsed();
    }

    public static Sentence parseSentence(String sentence) {
        sentence = cleanSentence(sentence);

        final Tree posTree = getPosTree(sentence);
        return new Sentence(posTree, getDependencies(posTree),
                StanfordNamedEntityRecognizer.findNamedEntities(sentence));
    }

    private static String cleanSentence(String sentence) {
        return sentence.replaceAll("\\p{Punct}", "").replaceAll("[ ]+", " ");
    }

    public static Tense calculateTense(String clause) {
        final Tree posTree = getPosTree(clause);
        final Tree word = posTree.getLeaves().get(0);
        final String pos = word.parent(posTree).label().value().toLowerCase();
        if (pos.equals("md")) {
            return Tense.FUTURE;
        }
        if (pos.equals("vbd") || pos.equals("vbn")) {
            return Tense.PAST;
        }
        return Tense.PRESENT;
    }
}
