package com.fraise.web;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.fraise.control.PoetryFeederManager;
import com.fraise.domain.Cinquain;
import com.fraise.domain.CinquainLine;
import com.fraise.domain.Language;
import com.fraise.domain.Paging;
import com.fraise.domain.Poem;
import com.fraise.domain.Poems;
import com.fraise.domain.PoetryTweet;
import com.fraise.domain.SortOrder;

@Controller
public class PoemController {

    private static final Logger log = Logger.getLogger(PoemController.class.getName());

    @Autowired
    private PoetryFeederManager poetryFeederManager;
    private int poemSize = 14;
    private int numberOfPoems = 3;

    // @RequestMapping(value = "/setupPF", method = RequestMethod.GET)
    // public String setupPF() {
    // poetryFeederManager.retrieveAndPersistNewPoetryLines();
    // return "setupComplete";
    // }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(
            @RequestParam(value = "sort", required = false, defaultValue = "chronological") String sortOrder,
            ModelMap model, HttpServletRequest request) {
        return getMostRecentPoem(sortOrder, model, request);
    }

    @RequestMapping(value = "/recent", method = RequestMethod.GET)
    public String getMostRecentPoem(
            @RequestParam(value = "sort", required = false, defaultValue = "chronological") String sortOrder,
            ModelMap model, HttpServletRequest request) {
        Paging paging = new Paging(poemSize);

        Language language = getLanguageFromSession(request);

        System.out.println("PoemController.getMostRecentPoem()");
        System.out.println("sortBy:" + sortOrder);
        List<PoetryTweet> recent = poetryFeederManager.getRecentSortedBy(SortOrder.valueOf(sortOrder.toUpperCase()),
                paging, language);

        poetryFeederManager.getRecentSortedBy(SortOrder.CHRONOLOGICAL, paging, language);
        model.addAttribute("recentList", recent);
        return "recent";
    }

    @RequestMapping(value = "/archive", method = RequestMethod.GET)
    public String getArchivedPoems(
            @RequestParam(value = "sort", required = false, defaultValue = "chronological") String sortOrder,
            @RequestParam(value = "bookmark", required = false, defaultValue = "1") String bookmarkString,
            ModelMap model, HttpServletRequest request) {
        Paging paging = new Paging(poemSize, parseBookmark(bookmarkString).getTime());

        Language language = getLanguageFromSession(request);

        List<Poem> archivedPoems = null;
        try {
            archivedPoems = poetryFeederManager.getArchivedPoems(numberOfPoems, paging, SortOrder.valueOf(sortOrder
                    .toUpperCase()), language);

            // TODO dirty hack to prevent empty pages being returned.
            if (null == archivedPoems || archivedPoems.size() == 0) {
                paging.setBookmark(parseBookmark("1").getTime());
                archivedPoems = poetryFeederManager.getArchivedPoems(numberOfPoems, paging, SortOrder.valueOf(sortOrder
                        .toUpperCase()), language);
            }
        } catch (Exception e) {
            log.info("Exception retrieving archived poems:" + e.getMessage());
        }
        model.addAttribute("archivedPoems", archivedPoems);

        model.addAttribute("bookmarkNext", checkForNext(archivedPoems));
        model.addAttribute("currentBookmark", bookmarkString);
        return "archive";
    }

    @RequestMapping(value = "/archive/next", method = RequestMethod.GET)
    public @ResponseBody
    Poems getNextArchived(
            @RequestParam(value = "sort", required = false, defaultValue = "chronological") String sortOrder,
            @RequestParam(value = "bookmark", required = false, defaultValue = "1") String bookmarkString,
            ModelMap model, HttpServletRequest request) {
        Paging paging = new Paging(poemSize, parseBookmark(bookmarkString).getTime());
        log.info("Bookmark:" + bookmarkString);
        Language language = getLanguageFromSession(request);

        List<Poem> archivedPoems = poetryFeederManager.getArchivedPoems(numberOfPoems, paging, SortOrder
                .valueOf(sortOrder.toUpperCase()), language);
        Poems poems = new Poems(archivedPoems);
        return poems;
    }

    @RequestMapping(value = "/cinquain", method = RequestMethod.GET)
    public String getCinquain(ModelMap model, HttpServletRequest request) {
        Paging paging = new Paging(5);

        Language language = getLanguageFromSession(request);

        List<Cinquain> cinquains = poetryFeederManager.getCinquant(3, paging, language);
        model.addAttribute("cinquains", cinquains);

        return "cinquain";
    }

    @RequestMapping(value = "/about", method = RequestMethod.GET)
    public String about() {
        return "about";
    }

    @RequestMapping(value = "/write", method = RequestMethod.GET)
    public String write() {
        return "write";
    }

    private Language getLanguageFromSession(HttpServletRequest request) {
        LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);

        Language language = Language.EN;
        if (localeResolver != null) {
            // get current locale
            Locale locale = localeResolver.resolveLocale(request);

            if (locale != null) {

                System.out.println("locale::" + locale);

                language = Language.valueOf(locale.getLanguage());
            }
        }
        return language;
    }

    private Calendar parseBookmark(String bookmarkString) {
        long bookmark = 1;
        try {
            bookmark = Long.valueOf(bookmarkString).longValue();
        } catch (Exception e) {
            // leave it.
        }
        Calendar cal = new GregorianCalendar(2009, 0, 1);
        cal.setTimeInMillis(bookmark);
        log.info("bookmark calendar: " + cal);
        return cal;
    }

    private String checkForNext(List<Poem> archivedPoems) {
        String timeString = "1";
        if (null != archivedPoems && archivedPoems.size() == numberOfPoems) {
            Poem lastPoem = archivedPoems.get(archivedPoems.size() - 1);
            long time = lastPoem.getBookmark().getTime();
            timeString = String.valueOf(time);
        }
        return timeString;
    }

    @RequestMapping(value = "/refresh", method = RequestMethod.GET)
    public String refreshSource() {
        log.info("refreshing poetryfeeder");
        poetryFeederManager.retrieveAndPersistNewPoetryLines();
        return "refresh";
    }

    @RequestMapping(value = "/refreshCinquains", method = RequestMethod.GET)
    public String refreshCinquains() {
        log.info("refreshing cinquains");
        poetryFeederManager.collateAndPersistCinquain();
        return "refresh";
    }

    @RequestMapping(value = "/refreshFollowers", method = RequestMethod.GET)
    public String refreshFollowers() {
        log.info("refreshing followers");
        poetryFeederManager.refreshFollowers();
        return "refresh";
    }

    //
    // @RequestMapping(value = "/setup", method = RequestMethod.GET)
    // public String setup(@RequestParam(value = "pwd", required = false,
    // defaultValue = "bladyblah") String pwd) {
    // if (pwd.equals("dg793sdg457hdfDFGW35as5wfsDSASD")) {
    // poetryFeederManager.setup();
    // }
    // return "about";
    // }

    @RequestMapping(value = "/cleanup", method = RequestMethod.GET)
    public String setup(@RequestParam(value = "pwd", required = false, defaultValue = "bladyblah") String pwd,
            @RequestParam(value = "byName", required = false, defaultValue = "nonenone") String byName) {
        if (pwd.equals("dg793sdg457hdfDFGW35as5wfsDSASD")) {
            poetryFeederManager.cleanup(byName);
        }
        return "about";
    }

    public void setPoetryFeederManager(PoetryFeederManager poetryFeederManager) {
        this.poetryFeederManager = poetryFeederManager;
    }

    public void setPoemSize(int poemSize) {
        this.poemSize = poemSize;
    }

    public void setNumberOfPoems(int numberOfPoems) {
        this.numberOfPoems = numberOfPoems;
    }

}
