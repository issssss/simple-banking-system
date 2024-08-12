package org.simple.bankingsystem.specifications;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.simple.bankingsystem.entities.Transaction;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collection;

public class TransactionSpecification implements Specification<Transaction> {

    private final SearchCriteria searchCriteria;

    public TransactionSpecification(SearchCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    @Override
    public Predicate toPredicate(Root<Transaction> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

        if (searchCriteria == null)
        {
            return null;
        }
       if (root.get(searchCriteria.getName()).getJavaType() == String.class) {
                return criteriaBuilder.like(
                        root.get(searchCriteria.getName()), "%" + searchCriteria.getValue() + "%");
       }
       else if (searchCriteria.getValue() instanceof Collection<?>)
       {
           return root.get(searchCriteria.getName()).in((Collection<?>) searchCriteria.getValue());
       }
       else {
           return criteriaBuilder.equal(root.get(searchCriteria.getName()), searchCriteria.getValue());
       }
    }
}
