/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dao.exceptions.NonexistentEntityException;
import dao.exceptions.PreexistingEntityException;
import dto.TablaAsistencia;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author redcr
 */
public class TablaAsistenciaJpaController implements Serializable {

    public TablaAsistenciaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("com.mycompany_Asistencia03_war_1.0-SNAPSHOTPU");

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public TablaAsistenciaJpaController() {
    }
    
    

    public void create(TablaAsistencia tablaAsistencia) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(tablaAsistencia);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTablaAsistencia(tablaAsistencia.getIdAsistencia()) != null) {
                throw new PreexistingEntityException("TablaAsistencia " + tablaAsistencia + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TablaAsistencia tablaAsistencia) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            tablaAsistencia = em.merge(tablaAsistencia);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tablaAsistencia.getIdAsistencia();
                if (findTablaAsistencia(id) == null) {
                    throw new NonexistentEntityException("The tablaAsistencia with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TablaAsistencia tablaAsistencia;
            try {
                tablaAsistencia = em.getReference(TablaAsistencia.class, id);
                tablaAsistencia.getIdAsistencia();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tablaAsistencia with id " + id + " no longer exists.", enfe);
            }
            em.remove(tablaAsistencia);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TablaAsistencia> findTablaAsistenciaEntities() {
        return findTablaAsistenciaEntities(true, -1, -1);
    }

    public List<TablaAsistencia> findTablaAsistenciaEntities(int maxResults, int firstResult) {
        return findTablaAsistenciaEntities(false, maxResults, firstResult);
    }

    private List<TablaAsistencia> findTablaAsistenciaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TablaAsistencia.class));
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

    public TablaAsistencia findTablaAsistencia(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TablaAsistencia.class, id);
        } finally {
            em.close();
        }
    }

    public int getTablaAsistenciaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TablaAsistencia> rt = cq.from(TablaAsistencia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
