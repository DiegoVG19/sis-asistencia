/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dao.exceptions.NonexistentEntityException;
import dto.Asistencia;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import dto.Empleado;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;

/**
 *
 * @author redcr
 */
public class AsistenciaJpaController implements Serializable {

    public AsistenciaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("com.mycompany_Asistencia03_war_1.0-SNAPSHOTPU");

    public AsistenciaJpaController() {
    }
    
    

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Asistencia asistencia) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empleado idEmpleado = asistencia.getIdEmpleado();
            if (idEmpleado != null) {
                idEmpleado = em.getReference(idEmpleado.getClass(), idEmpleado.getIdEmpleado());
                asistencia.setIdEmpleado(idEmpleado);
            }
            em.persist(asistencia);
            if (idEmpleado != null) {
                idEmpleado.getAsistenciaList().add(asistencia);
                idEmpleado = em.merge(idEmpleado);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Asistencia asistencia) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Asistencia persistentAsistencia = em.find(Asistencia.class, asistencia.getIdAsistencia());
            Empleado idEmpleadoOld = persistentAsistencia.getIdEmpleado();
            Empleado idEmpleadoNew = asistencia.getIdEmpleado();
            if (idEmpleadoNew != null) {
                idEmpleadoNew = em.getReference(idEmpleadoNew.getClass(), idEmpleadoNew.getIdEmpleado());
                asistencia.setIdEmpleado(idEmpleadoNew);
            }
            asistencia = em.merge(asistencia);
            if (idEmpleadoOld != null && !idEmpleadoOld.equals(idEmpleadoNew)) {
                idEmpleadoOld.getAsistenciaList().remove(asistencia);
                idEmpleadoOld = em.merge(idEmpleadoOld);
            }
            if (idEmpleadoNew != null && !idEmpleadoNew.equals(idEmpleadoOld)) {
                idEmpleadoNew.getAsistenciaList().add(asistencia);
                idEmpleadoNew = em.merge(idEmpleadoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = asistencia.getIdAsistencia();
                if (findAsistencia(id) == null) {
                    throw new NonexistentEntityException("The asistencia with id " + id + " no longer exists.");
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
            Asistencia asistencia;
            try {
                asistencia = em.getReference(Asistencia.class, id);
                asistencia.getIdAsistencia();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The asistencia with id " + id + " no longer exists.", enfe);
            }
            Empleado idEmpleado = asistencia.getIdEmpleado();
            if (idEmpleado != null) {
                idEmpleado.getAsistenciaList().remove(asistencia);
                idEmpleado = em.merge(idEmpleado);
            }
            em.remove(asistencia);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Asistencia> findAsistenciaEntities() {
        return findAsistenciaEntities(true, -1, -1);
    }

    public List<Asistencia> findAsistenciaEntities(int maxResults, int firstResult) {
        return findAsistenciaEntities(false, maxResults, firstResult);
    }

    private List<Asistencia> findAsistenciaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Asistencia.class));
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

    public Asistencia findAsistencia(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Asistencia.class, id);
        } finally {
            em.close();
        }
    }
    
   public List<Asistencia> findAsistenciaByEmpleadoYFecha(int idEmpleado, Date fecha) {
    EntityManager em = getEntityManager();
    try {
        Calendar startOfDay = Calendar.getInstance();
        startOfDay.setTime(fecha);
        startOfDay.set(Calendar.HOUR_OF_DAY, 0);
        startOfDay.set(Calendar.MINUTE, 0);
        startOfDay.set(Calendar.SECOND, 0);

        Calendar endOfDay = Calendar.getInstance();
        endOfDay.setTime(fecha);
        endOfDay.set(Calendar.HOUR_OF_DAY, 23);
        endOfDay.set(Calendar.MINUTE, 59);
        endOfDay.set(Calendar.SECOND, 59);

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Asistencia> cq = cb.createQuery(Asistencia.class);
        Root<Asistencia> asistencia = cq.from(Asistencia.class);

        cq.select(asistencia)
            .where(
                cb.equal(asistencia.get("idEmpleado"), idEmpleado),
                cb.between(asistencia.get("entrada").as(Date.class), 
                           startOfDay.getTime(), 
                           endOfDay.getTime())
            );

        TypedQuery<Asistencia> query = em.createQuery(cq);
        return query.getResultList();
    } finally {
        em.close();
    }
}



    public int getAsistenciaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Asistencia> rt = cq.from(Asistencia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public Asistencia findAsistenciaByEmpleado(int idEmpleado) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Asistencia> cq = cb.createQuery(Asistencia.class);
            Root<Asistencia> asistencia = cq.from(Asistencia.class);

            cq.select(asistencia)
                .where(cb.equal(asistencia.get("idEmpleado"), idEmpleado))
                .orderBy(cb.desc(asistencia.get("entrada"))); // Si hay múltiples entradas, obtener la más reciente

            TypedQuery<Asistencia> query = em.createQuery(cq);
            query.setMaxResults(1); // Obtener solo un resultado (la entrada más reciente)
            
            List<Asistencia> result = query.getResultList();
            
            if (!result.isEmpty()) {
                return result.get(0);
            } else {
                return null; // No se encontraron entradas para el empleado
            }
        } finally {
            em.close();
        }
    }
    
}
