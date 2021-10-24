package com.example.CenaClientes.repository;

import com.example.CenaClientes.classes.Filters;
import com.example.CenaClientes.entities.Client;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class ClientRepositoryCustomImpl implements  ClientRepositoryCustom{

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<Client> findClientByCriteria(Filters filters) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Client> query = cb.createQuery(Client.class);

        Root<Client> clientRoot = query.from(Client.class);

        Path<Long> typePath = clientRoot.get("type");
        Path<Long> locationPath = clientRoot.get("location");
        Path<Long> totalBalancePath = clientRoot.get("totalBalance");
        Path<Long> companyPath = clientRoot.get("company");
        Path<String> codePath = clientRoot.get("code");
        Path<Boolean> malePath = clientRoot.get("male");
        Path<Integer> idPath = clientRoot.get("id");

        List<Predicate> predicates = new ArrayList<>();
        if (filters.getTC() != null){
            predicates.add(cb.equal(typePath, filters.getTC()));
        }
        if (filters.getUG() != null){
            predicates.add(cb.equal(locationPath, filters.getUG()));
        }
        if (filters.getRI() != null){
            predicates.add(cb.greaterThan(totalBalancePath, filters.getRI()));
        }
        if (filters.getRF() != null){
            predicates.add(cb.lessThan(totalBalancePath, filters.getRF()));
        }
        predicates.add(cb.equal(malePath, filters.getMale()));
        for (Client client : filters.getUsedClients()){
            predicates.add(cb.notEqual(idPath,client.getId()));
        }

        List<Order> orderList = new ArrayList<>();
        orderList.add(cb.desc(totalBalancePath));
        orderList.add(cb.asc(codePath));

        query.select(clientRoot)
                .where(cb.and(predicates.toArray(new Predicate[predicates.size()])))
                .groupBy(companyPath)
                .having(cb.equal(cb.max(totalBalancePath),totalBalancePath))
                .orderBy(orderList);

        return entityManager.createQuery(query).setFirstResult(0).setMaxResults(filters.getLimit()).getResultList();
    }
}
