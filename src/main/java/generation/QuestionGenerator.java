package generation;

import simplenlg.features.Feature;
import simplenlg.features.InterrogativeType;
import simplenlg.framework.NLGFactory;
import simplenlg.lexicon.Lexicon;
import simplenlg.phrasespec.NPPhraseSpec;
import simplenlg.phrasespec.SPhraseSpec;
import simplenlg.phrasespec.VPPhraseSpec;
import simplenlg.realiser.english.Realiser;

public class QuestionGenerator {
    private static final Lexicon lexicon = Lexicon.getDefaultLexicon();
    private static final NLGFactory nlgFactory = new NLGFactory(lexicon);
    private static final Realiser realiser = new Realiser(lexicon);

    public static String generateLocationQuestion(String vp, String subject) {
        final VPPhraseSpec vpPhraseSpec = nlgFactory.createVerbPhrase(vp);
        final NPPhraseSpec npPhraseSpec = nlgFactory.createNounPhrase(subject);

        final SPhraseSpec sPhraseSpec = nlgFactory.createClause(npPhraseSpec, vpPhraseSpec);
        sPhraseSpec.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHERE);

        return realiser.realiseSentence(sPhraseSpec);
    }
}
