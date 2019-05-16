/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.controller;

import com.mycompany.controller.exceptions.NonexistentEntityException;
import com.mycompany.controller.exceptions.PreexistingEntityException;
import com.mycompany.controller.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.mycompany.entity.Authors;
import com.mycompany.entity.Books;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author German
 */
public class BooksJpaController implements Serializable {

    public BooksJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Books books) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (books.getAuthorsList() == null) {
            books.setAuthorsList(new ArrayList<Authors>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Authors> attachedAuthorsList = new ArrayList<Authors>();
            for (Authors authorsListAuthorsToAttach : books.getAuthorsList()) {
                authorsListAuthorsToAttach = em.getReference(authorsListAuthorsToAttach.getClass(), authorsListAuthorsToAttach.getAuthID());
                attachedAuthorsList.add(authorsListAuthorsToAttach);
            }
            books.setAuthorsList(attachedAuthorsList);
            em.persist(books);
            for (Authors authorsListAuthors : books.getAuthorsList()) {
                authorsListAuthors.getBooksList().add(books);
                authorsListAuthors = em.merge(authorsListAuthors);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findBooks(books.getBookID()) != null) {
                throw new PreexistingEntityException("Books " + books + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Books books) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Books persistentBooks = em.find(Books.class, books.getBookID());
            List<Authors> authorsListOld = persistentBooks.getAuthorsList();
            List<Authors> authorsListNew = books.getAuthorsList();
            List<Authors> attachedAuthorsListNew = new ArrayList<Authors>();
            for (Authors authorsListNewAuthorsToAttach : authorsListNew) {
                authorsListNewAuthorsToAttach = em.getReference(authorsListNewAuthorsToAttach.getClass(), authorsListNewAuthorsToAttach.getAuthID());
                attachedAuthorsListNew.add(authorsListNewAuthorsToAttach);
            }
            authorsListNew = attachedAuthorsListNew;
            books.setAuthorsList(authorsListNew);
            books = em.merge(books);
            for (Authors authorsListOldAuthors : authorsListOld) {
                if (!authorsListNew.contains(authorsListOldAuthors)) {
                    authorsListOldAuthors.getBooksList().remove(books);
                    authorsListOldAuthors = em.merge(authorsListOldAuthors);
                }
            }
            for (Authors authorsListNewAuthors : authorsListNew) {
                if (!authorsListOld.contains(authorsListNewAuthors)) {
                    authorsListNewAuthors.getBooksList().add(books);
                    authorsListNewAuthors = em.merge(authorsListNewAuthors);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = books.getBookID();
                if (findBooks(id) == null) {
                    throw new NonexistentEntityException("The books with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Books books;
            try {
                books = em.getReference(Books.class, id);
                books.getBookID();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The books with id " + id + " no longer exists.", enfe);
            }
            List<Authors> authorsList = books.getAuthorsList();
            for (Authors authorsListAuthors : authorsList) {
                authorsListAuthors.getBooksList().remove(books);
                authorsListAuthors = em.merge(authorsListAuthors);
            }
            em.remove(books);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Books> findBooksEntities() {
        return findBooksEntities(true, -1, -1);
    }

    public List<Books> findBooksEntities(int maxResults, int firstResult) {
        return findBooksEntities(false, maxResults, firstResult);
    }

    private List<Books> findBooksEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Books.class));
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

    public Books findBooks(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Books.class, id);
        } finally {
            em.close();
        }
    }

    public int getBooksCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Books> rt = cq.from(Books.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
