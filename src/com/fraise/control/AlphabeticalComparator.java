package com.fraise.control;

import java.util.Comparator;

import com.fraise.domain.PoetryTweet;

public class AlphabeticalComparator implements Comparator<PoetryTweet> {

    @Override
    public int compare(PoetryTweet o1, PoetryTweet o2) {
        return o1.getText().compareToIgnoreCase(o2.getText());
    }

}
