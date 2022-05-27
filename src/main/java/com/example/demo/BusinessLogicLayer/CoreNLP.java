package com.example.demo.BusinessLogicLayer;


import com.example.demo.ControlLayer.AppController;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

//import static com.example.demo.SpringCrudPocApplication.pipeline;
@Component
public class CoreNLP {
    private static final Logger log = LoggerFactory.getLogger(CoreNLP.class);
    public static StanfordCoreNLP pipeline;
    public static Properties props;

    public static void main(String[] args) throws IOException {
        try {
            init();
        } catch (Exception g) {
//g.printStackTrace();
        }
        Set<String> dummy = new HashSet<>();
        dummy.add("John");
        dummy.add("Ricky Martin");

        CoreNLP_NameValidation(dummy);
    }

    public static void init() {

        log.info("Loading CoreNLP ....");
        props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma, ner");
        pipeline = new StanfordCoreNLP(props);
    }


    public static HashMap<String, String> CoreNLP_NameValidation(Set<String> Name) {
        log.info("CoreNLP Name Validation Module");
        HashMap<String, String> validatedNames = new HashMap<>();
        for (String TestCase : Name) {
            String deciderFlag = "false";
            int truecount = 0;
            int falsecount = 0;
            String final_word = "";
            {
                edu.stanford.nlp.pipeline.Annotation document = NER.pipeline.process(TestCase);
                List<CoreLabel> tokens = document.get(CoreAnnotations.TokensAnnotation.class);
                for (CoreLabel token : tokens) {
//    String lemma = token.get(CoreAnnotations.LemmaAnnotation.class);
                    String word = token.get(CoreAnnotations.TextAnnotation.class);
//    String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                    String ner = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
                    if (ner.contains("PERSON")) {
                        final_word += word + " ";
                        truecount++;
                    } else {
                        falsecount++;
                    }
                }
                if (truecount >= falsecount) {
                    deciderFlag = "true";
                } else {
                    deciderFlag = "false";
                }
            }
            validatedNames.put(final_word.replaceAll("(?sim)\\s+", " ").trim(), deciderFlag);
        }
        return validatedNames;
    }

}


