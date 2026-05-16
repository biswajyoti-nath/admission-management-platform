package com.admission.strategy.impl;

import com.admission.model.Application;
import com.admission.model.ApplicationStatus;
import com.admission.strategy.SelectionStrategy;

import java.util.List;

public class MeritBasedSelectionStrategy implements SelectionStrategy {

    @Override
    public List<Application> select(List<Application> applications, int seatCount) {
        applications.sort((a, b) -> Double.compare(b.getScore(), a.getScore()));
        for (int i = 0; i < applications.size(); i++) {
            if (i < seatCount) {
                applications.get(i).setStatus(ApplicationStatus.SELECTED);
            } else {
                applications.get(i).setStatus(ApplicationStatus.REJECTED);
            }
        }
        return applications;
    }
}
