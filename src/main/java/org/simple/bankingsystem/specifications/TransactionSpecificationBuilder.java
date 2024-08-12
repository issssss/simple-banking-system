package org.simple.bankingsystem.specifications;

import org.simple.bankingsystem.entities.Transaction;
import org.simple.bankingsystem.enums.InclusionEnum;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class TransactionSpecificationBuilder {

    private final List<SearchCriteria> searchCriteria;

    public TransactionSpecificationBuilder(List<SearchCriteria> searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    public Specification<Transaction> build() {
        if (searchCriteria == null || searchCriteria.isEmpty()) {
            return new TransactionSpecification(null);
        }
        Specification<Transaction> result = new TransactionSpecification(searchCriteria.get(0));
        for (SearchCriteria searchCriterion : searchCriteria) {
            if (searchCriteria.indexOf(searchCriterion) == 0) {
                continue;
            }
            if (searchCriterion.getInclusion() != null
                    && searchCriterion.getInclusion().equals(InclusionEnum.OR))
            {
                result = result.or(new TransactionSpecification(searchCriterion));
            }
            else
            {
                result = result.and(new TransactionSpecification(searchCriterion));
            }
        }
        return result;
    }
}
