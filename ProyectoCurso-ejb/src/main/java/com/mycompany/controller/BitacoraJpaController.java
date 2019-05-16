/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.controller;

import com.mycompany.controller.exceptions.NonexistentEntityException;
import com.mycompany.controller.exceptions.RollbackFailureException;
import com.mycompany.entity.Bitacora;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;

/**
 *
 * @author German
 */
public class BitacoraJpaController implements Serializable {

    public BitacoraJpaController() {
            this.emf = Persistence.createEntityManagerFactory("com.mycompany_ProyectoCurso-ejb_ejb_1.0-SNAPSHOTPU").createEntityManager();
    }
    private UserTransaction utx = null;
    private EntityManager emf = null;

    public EntityManager getEntityManager() {
        return emf;
    }

    public void create(Bitacora bitacora) throws RollbackFailureException, Exception {      
        try {
            emf.getTransaction().begin();                        
            emf.persist(bitacora);
            emf.getTransaction().commit();
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (emf != null) {
                emf.close();
            }
        }
    }

    public void edit(Bitacora bitacora) throws NonexistentEntityException, RollbackFailureException, Exception {
        try {
            emf.getTransaction().begin();
            emf.merge(bitacora);
            emf.getTransaction().commit();
        } catch (Exception ex){
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
            Bitacora bitacora;
            try {
                bitacora = emf.getReference(Bitacora.class, id);
                bitacora.getIdBitacora();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The bitacora with id " + id + " no longer exists.", enfe);
            }
            emf.remove(bitacora);
            emf.getTransaction().commit();
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (emf != null) {
                emf.close();
            }
        }
    }

    public List<Bitacora> findBitacoraEntities() {
        return findBitacoraEntities(true, -1, -1);
    }

    public List<Bitacora> findBitacoraEntities(int maxResults, int firstResult) {
        return findBitacoraEntities(false, maxResults, firstResult);
    }

    private List<Bitacora> findBitacoraEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Bitacora.class));
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

    public Bitacora findBitacora(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Bitacora.class, id);
        } finally {
            em.close();
        }
    }

    public int getBitacoraCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Bitacora> rt = cq.from(Bitacora.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
