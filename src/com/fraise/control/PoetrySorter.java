package com.fraise.control;

import java.util.List;

import com.fraise.domain.Poem;
import com.fraise.domain.PoetryTweet;
import com.fraise.domain.SortOrder;

public interface PoetrySorter {

    List<PoetryTweet> sortPoem(List<PoetryTweet> poem, SortOrder sortOrder);

    List<Poem> sortPoems(List<Poem> poems, SortOrder sortOrder);

}
