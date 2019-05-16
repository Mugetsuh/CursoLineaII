/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.controller;

import com.mycompany.controller.exceptions.NonexistentEntityException;
import com.mycompany.controller.exceptions.RollbackFailureException;
import com.mycompany.entity.Customers;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.mycompany.entity.Orders;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.UserTransaction;

/**
 *
 * @author German
 */
public class CustomersJpaController implements Serializable {

    public CustomersJpaController() {
        this.emf = Persistence.createEntityManagerFactory("com.mycompany_ProyectoCurso-ejb_ejb_1.0-SNAPSHOTPU").createEntityManager();
    }
    private UserTransaction utx = null;
    private EntityManager emf = null;

    public EntityManager getEntityManager() {
        return emf;
    }

    public void create(Customers customers) throws RollbackFailureException, Exception {
        if (customers.getOrdersList() == null) {
            customers.setOrdersList(new ArrayList<Orders>());
        }
        //EntityManager em = null;
        try {
            /*
            utx.begin();
            em = getEntityManager();
            List<Orders> attachedOrdersList = new ArrayList<Orders>();
            for (Orders ordersListOrdersToAttach : customers.getOrdersList()) {
                ordersListOrdersToAttach = em.getReference(ordersListOrdersToAttach.getClass(), ordersListOrdersToAttach.getOrderId());
                attachedOrdersList.add(ordersListOrdersToAttach);
            }
            customers.setOrdersList(attachedOrdersList);
            em.persist(customers);
            for (Orders ordersListOrders : customers.getOrdersList()) {
                Customers oldCustomerIdOfOrdersListOrders = ordersListOrders.getCustomerId();
                ordersListOrders.setCustomerId(customers);
                ordersListOrders = em.merge(ordersListOrders);
                if (oldCustomerIdOfOrdersListOrders != null) {
                    oldCustomerIdOfOrdersListOrders.getOrdersList().remove(ordersListOrders);
                    oldCustomerIdOfOrdersListOrders = em.merge(oldCustomerIdOfOrdersListOrders);
                }
            }
            utx.commit(); */
            emf.getTransaction().begin();                        
            emf.persist(customers);
            emf.getTransaction().commit();
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (emf != null) {
                emf.close();
            }
        }
    }

    public void edit(Customers customers) throws NonexistentEntityException, RollbackFailureException, Exception {
        try {
            emf.getTransaction().begin();
            emf.merge(customers);
            emf.getTransaction().commit();
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (emf != null) {
                emf.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        try {
            emf.getTransaction().begin();
            Customers customers;
            try {
                customers = emf.getReference(Customers.class, id);
                customers.getCustomerId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The bitacora with id " + id + " no longer exists.", enfe);
            }
            emf.remove(customers);
            emf.getTransaction().commit();
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (emf != null) {
                emf.close();
            }
        }
    }

    public List<Customers> findCustomersEntities() {
        return findCustomersEntities(true, -1, -1);
    }

    public List<Customers> findCustomersEntities(int maxResults, int firstResult) {
        return findCustomersEntities(false, maxResults, firstResult);
    }

    private List<Customers> findCustomersEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Customers.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Customers findCustomers(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Customers.class, id);
        } finally {
            em.close();
        }
    }

    public int getCustomersCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Customers> rt = cq.from(Customers.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
