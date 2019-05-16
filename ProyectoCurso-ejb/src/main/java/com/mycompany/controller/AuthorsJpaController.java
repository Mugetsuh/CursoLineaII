/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.controller;

import com.mycompany.controller.exceptions.NonexistentEntityException;
import com.mycompany.controller.exceptions.PreexistingEntityException;
import com.mycompany.controller.exceptions.RollbackFailureException;
import com.mycompany.entity.Authors;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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
public class AuthorsJpaController implements Serializable {

    public AuthorsJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Authors authors) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (authors.getBooksList() == null) {
            authors.setBooksList(new ArrayList<Books>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Books> attachedBooksList = new ArrayList<Books>();
            for (Books booksListBooksToAttach : authors.getBooksList()) {
                booksListBooksToAttach = em.getReference(booksListBooksToAttach.getClass(), booksListBooksToAttach.getBookID());
                attachedBooksList.add(booksListBooksToAttach);
            }
            authors.setBooksList(attachedBooksList);
            em.persist(authors);
            for (Books booksListBooks : authors.getBooksList()) {
                booksListBooks.getAuthorsList().add(authors);
                booksListBooks = em.merge(booksListBooks);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findAuthors(authors.getAuthID()) != null) {
                throw new PreexistingEntityException("Authors " + authors + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Authors authors) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Authors persistentAuthors = em.find(Authors.class, authors.getAuthID());
            List<Books> booksListOld = persistentAuthors.getBooksList();
            List<Books> booksListNew = authors.getBooksList();
            List<Books> attachedBooksListNew = new ArrayList<Books>();
            for (Books booksListNewBooksToAttach : booksListNew) {
                booksListNewBooksToAttach = em.getReference(booksListNewBooksToAttach.getClass(), booksListNewBooksToAttach.getBookID());
                attachedBooksListNew.add(booksListNewBooksToAttach);
            }
            booksListNew = attachedBooksListNew;
            authors.setBooksList(booksListNew);
            authors = em.merge(authors);
            for (Books booksListOldBooks : booksListOld) {
                if (!booksListNew.contains(booksListOldBooks)) {
                    booksListOldBooks.getAuthorsList().remove(authors);
                    booksListOldBooks = em.merge(booksListOldBooks);
                }
            }
            for (Books booksListNewBooks : booksListNew) {
                if (!booksListOld.contains(booksListNewBooks)) {
                    booksListNewBooks.getAuthorsList().add(authors);
                    booksListNewBooks = em.merge(booksListNewBooks);
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
                Integer id = authors.getAuthID();
                if (findAuthors(id) == null) {
                    throw new NonexistentEntityException("The authors with id " + id + " no longer exists.");
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
            Authors authors;
            try {
                authors = em.getReference(Authors.class, id);
                authors.getAuthID();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The authors with id " + id + " no longer exists.", enfe);
            }
            List<Books> booksList = authors.getBooksList();
            for (Books booksListBooks : booksList) {
                booksListBooks.getAuthorsList().remove(authors);
                booksListBooks = em.merge(booksListBooks);
            }
            em.remove(authors);
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

    public List<Authors> findAuthorsEntities() {
        return findAuthorsEntities(true, -1, -1);
    }

    public List<Authors> findAuthorsEntities(int maxResults, int firstResult) {
        return findAuthorsEntities(false, maxResults, firstResult);
    }

    private List<Authors> findAuthorsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Authors.class));
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

    public Authors findAuthors(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Authors.class, id);
        } finally {
            em.close();
        }
    }

    public int getAuthorsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Authors> rt = cq.from(Authors.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
