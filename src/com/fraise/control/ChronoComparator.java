package com.fraise.control;

import java.util.Comparator;

import com.fraise.domain.PoetryTweet;

public class ChronoComparator implements Comparator<PoetryTweet> {

    @Override
    public int compare(PoetryTweet o1, PoetryTweet o2) {
        return o2.getAuthoredOn().compareTo(o1.getAuthoredOn());
    }

}
