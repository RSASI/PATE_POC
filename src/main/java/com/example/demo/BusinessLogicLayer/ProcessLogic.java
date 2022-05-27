package com.example.demo.BusinessLogicLayer;

import com.example.demo.EntityLayer.Entitymodel;
import edu.stanford.nlp.ling.tokensregex.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ProcessLogic {
    private static final Logger log = LoggerFactory.getLogger(ProcessLogic.class);

    @Autowired
    public NER nerFlow;
    @Autowired
    public CoreNLP crnlp;

    public List<Entitymodel> process(List<Entitymodel> ent) throws IOException, ParseException, InterruptedException {
        List<Entitymodel> ModelDataList = new ArrayList<>();
        List<Entitymodel> entities = ent;
        for (String lurl : entities.stream().map(x -> x.getLandingUrl()).collect(Collectors.toList())) {
            String FinalNameList = "";
            String durl = entities.stream().filter(x -> x.getLandingUrl().equalsIgnoreCase(lurl)).map(x -> x.getDomainUrl()).findAny().get();
            String PageSource = Optional.ofNullable(getContent(lurl)).toString();
            if (!(PageSource.isEmpty() || PageSource == null)) {
                String CleansedPS = CleanContent(PageSource);
                Set<String> NerNames = nerFlow.NERNameIdentification(CleansedPS);
                FinalNameList = NerNames.stream().map(x -> x.trim()).sorted().collect(Collectors.joining("|"));
                FinalNameList = FinalNameList.replaceAll("(?sim)^\\s*\\|\\s*", "").replaceAll("(?sim)\\|\\s*$", "").trim();
            }
            Entitymodel finalEntityMode = new Entitymodel();
            finalEntityMode.setLandingUrl(lurl);
            finalEntityMode.setDomainUrl(durl);
            finalEntityMode.setPersonName(FinalNameList);
            try {
                ModelDataList.add(finalEntityMode);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return ModelDataList;
    }

    private static String CleanContent(String pageSource) {

        String normContent = pageSource;
        normContent = normContent.replaceAll("(?sim)<script[^<>]*>.*?</script>", "")
                .replaceAll("(?si)<iframe[^<>]*?>.*?</iframe>", "")
                .replaceAll("(?si)<iframe[^<>]*?>", "")
                .replaceAll("(?si)<img[^<>]*?>|</img>", "")
                .replaceAll("(?si)<style[^<>]*?>.*?</style>", "")
                .replaceAll("(?si)<noscript[^<>]*?>.*?</noscript>", "")
                .replaceAll("<noscript/>", "")
                .replaceAll("(?sim)<head>.*?</head>", "")
                .replaceAll("(?sim)<!--[^<>]*>", "")
                .replaceAll("(?sim)<!--.*?-->", "")
                .replaceAll("(?sim)\\s\\s+", "\\\n")
                .replaceAll("(?sim)&nbsp;", " ")
                .replaceAll("(?sim)&amp;", "&");

        return normContent;

    }

    public static String getContent(String url) {
        log.info("Page Source Extraction Module ....");
        String docStr = "";
        int timeout = 1000;
        Document document = null;
        try {
            document = Jsoup.connect(url).userAgent("Mozilla/5.0 (X11; Linux i586; rv:31.0) Gecko/20100101 Firefox/31.0").followRedirects(true).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (document != null) {
            docStr = document.toString();
        } else {
            docStr = "";
        }

        return docStr;
    }


}
