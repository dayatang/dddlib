package org.dayatang.observer.domain;

import org.dayatang.observer.AbstractSubjectEntity;

public class Baby extends AbstractSubjectEntity {

    /**
     *
     */
    private static final long serialVersionUID = -7609647418687945848L;

    @Override
    public String getSubjectKey() {
        return "BABY-SUBJECT";
    }

    @Override
    public String[] businessKeys() {
        return new String[]{};
    }

    public void cry() {
        notifyObservers();
    }

}
