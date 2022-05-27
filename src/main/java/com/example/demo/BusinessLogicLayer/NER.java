package com.example.demo.BusinessLogicLayer;


import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.tokensregex.parser.ParseException;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class NER {

    private static final Logger log = LoggerFactory.getLogger(NER.class);
    @Autowired
    public static CoreNLP crnlp;
    public static String serializedClassifier = "classifiers/english.all.3class.distsim.crf.ser.gz";
    public static AbstractSequenceClassifier classifier = CRFClassifier.getClassifierNoExceptions(serializedClassifier);
    public static StanfordCoreNLP pipeline;
    public static Properties props;

    public static void main(String[] args) throws Exception {
//            init(serializedClassifier);
//            Set<String> test=NERNameIdentification("John & Peter working along with Mitchelle.");
//            test.stream().forEach(x-> System.out.println(x));
//
    }

    public static void init(String serializedClassifier)
            throws Exception {
        classifier = CRFClassifier.getClassifierNoExceptions(serializedClassifier);
        System.out.println("Loading CoreNLP ....");
        //      model = new POSModelLoader().load(new File(MethodRepository.opennlpLocation));
        props = new Properties();
//            props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref, sentiment");
        props.put("annotators", "tokenize, ssplit, pos, lemma, ner");
//    props.put("annotators", "tokenize, ssplit, ner");
        pipeline = new StanfordCoreNLP(props);
        log.info("NER Module Initiated Successfully...");

    }

    public static Set<String> NERNameIdentification(String Input) throws IOException, InterruptedException, ParseException {
        log.info("Name Identification Module...");
        Thread.sleep(1000);
        Input = Input.replaceAll("(?sim)\\s+", " ");
        String output = classifier.classifyWithInlineXML(Input);
        String matchPtrn = "<PERSON>([^<>]*)</PERSON>";
        String person = "";
        Set<String> NamesFound = new HashSet<String>();
        Set<String> NameList = RegexMatcherList(output, matchPtrn, 1);

        NamesFound = NameList.stream().filter(word -> !word.trim().isEmpty()).filter(word -> word.length() < 30).map(x -> x).collect(Collectors.toSet());

        return NamesFound;

    }

    public static Set<String> RegexMatcherList(String text, String pts, int grp) {
        Set<String> matchedData = new TreeSet<>();
        Pattern regex = Pattern.compile(String.valueOf(pts),
                                        Pattern.CANON_EQ | Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.MULTILINE);
        Matcher regexMatcher = regex.matcher(text);
        while (regexMatcher.find()) {
            matchedData.add(regexMatcher.group(grp).trim());
        }
        return matchedData;
    }


}




