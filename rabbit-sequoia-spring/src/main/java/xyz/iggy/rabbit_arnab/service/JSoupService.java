package xyz.iggy.rabbit_arnab.service;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Component
@Slf4j
public class JSoupService implements CommandLineRunner {
    public void doJSoup() throws IOException {
        Document doc = Jsoup.connect("https://jobs.sequoiacap.com/jobs?skills=Spring+Boot/").get();
        log.info(doc.title());
        log.info("printing doc\n{}",doc);
        Elements newsHeadlines = doc.select("job-list-job");
        Elements divJobListJob = doc.select("job-list-job");
        Element e_divJobListJob = doc.select("div.job-list-job").first();

        log.info("divJobListJob size == {}", divJobListJob.size());
        log.info("e_divJobListJob == {}", e_divJobListJob);
        for (Element headline : newsHeadlines) {
            log.info("%s\n\t%s", headline.attr("title"), headline.absUrl("href"));
        }
    }

    @Override
    public void run(String... args) throws Exception {
//        doJSoup();
    }
}
