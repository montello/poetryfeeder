package com.fraise.domain;

import java.util.List;

public class Poems {

    private List<Poem> poems;

    public Poems(List<Poem> poems) {
        this.poems = poems;
    }

    public List<Poem> getPoems() {
        return poems;
    }

    public void setPoems(List<Poem> poems) {
        this.poems = poems;
    }

}
