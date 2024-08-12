package org.simple.bankingsystem.specifications;

import org.simple.bankingsystem.enums.InclusionEnum;

public class SearchCriteria {
    private final String name;
    private final Object value;
    private InclusionEnum inclusion;

    public SearchCriteria(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public SearchCriteria(String name, Object value, InclusionEnum inclusion) {
        this.name = name;
        this.value = value;
        this.inclusion = inclusion;
    }

    public Object getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public InclusionEnum getInclusion() {
        return inclusion;
    }
}
