package com.fraise.control;

import java.util.Collections;
import java.util.List;

import com.fraise.domain.Poem;
import com.fraise.domain.PoetryTweet;
import com.fraise.domain.SortOrder;

public class PoetrySorterImpl implements PoetrySorter {

    @Override
    public List<PoetryTweet> sortPoem(List<PoetryTweet> poem, SortOrder sortOrder) {
        if (sortOrder.equals(SortOrder.ALPHABETICAL)) {
            Collections.sort(poem, new AlphabeticalComparator());
        } else if (sortOrder.equals(SortOrder.AUTHOR)) {
            Collections.sort(poem, new AuthorComparator());
        } else if (sortOrder.equals(SortOrder.CHRONOLOGICAL)) {
            Collections.sort(poem, new ChronoComparator());
        }
        return poem;
    }

    @Override
    public List<Poem> sortPoems(List<Poem> poems, SortOrder sortOrder) {
        for (Poem poem : poems) {
            sortPoem(poem.getLines(), sortOrder);
        }
        return poems;
    }

}
