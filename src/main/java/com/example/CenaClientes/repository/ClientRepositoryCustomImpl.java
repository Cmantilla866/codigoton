package com.example.CenaClientes.repository;

import com.example.CenaClientes.entities.Account;
import com.example.CenaClientes.entities.Client;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class ClientRepositoryCustomImpl implements  ClientRepositoryCustom{

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<Client> findClientByCriteria(Integer TC, String UG, String RI, String RF) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Client> query = cb.createQuery(Client.class);

        Root<Client> clientRoot = query.from(Client.class);

        Path<String> typePath = clientRoot.get("type");
        Path<String> locationPath = clientRoot.get("location");
        Path<String> totalBalancePath = clientRoot.get("totalBalance");

        List<Predicate> predicates = new ArrayList<>();
        if (TC != null){
            predicates.add(cb.equal(typePath,TC));
        }
        if (UG != null){
            predicates.add(cb.like(locationPath,UG));
        }
        if (RI != null){
            predicates.add(cb.greaterThan(totalBalancePath,RI));
        }
        if (RF != null){
            predicates.add(cb.lessThan(totalBalancePath,RF));
        }

        query.select(clientRoot).where(cb.and(predicates.toArray(new Predicate[predicates.size()])));

        return entityManager.createQuery(query).getResultList();
    }
}
