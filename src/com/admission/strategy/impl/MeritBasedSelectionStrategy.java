package com.admission.strategy.impl;

import com.admission.model.Application;
import com.admission.strategy.SelectionStrategy;

import java.util.List;

public class MeritBasedSelectionStrategy implements SelectionStrategy {

    @Override
    public List<Application> select(List<Application> applications, int seatCount) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
