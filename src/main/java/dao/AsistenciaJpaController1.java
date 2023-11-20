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
import dto.Turno;
import dto.Empleado;
import dto.Estado;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author USER
 */
public class AsistenciaJpaController1 implements Serializable {

    public AsistenciaJpaController1(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public AsistenciaJpaController1() {
    }
    
 private EntityManagerFactory emf = Persistence.createEntityManagerFactory("com.mycompany_Asistencia03_war_1.0-SNAPSHOTPU");

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Asistencia asistencia) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Turno idTurno = asistencia.getIdTurno();
            if (idTurno != null) {
                idTurno = em.getReference(idTurno.getClass(), idTurno.getIdTurno());
                asistencia.setIdTurno(idTurno);
            }
            Empleado idEmpleado = asistencia.getIdEmpleado();
            if (idEmpleado != null) {
                idEmpleado = em.getReference(idEmpleado.getClass(), idEmpleado.getIdEmpleado());
                asistencia.setIdEmpleado(idEmpleado);
            }
            Estado idEstado = asistencia.getIdEstado();
            if (idEstado != null) {
                idEstado = em.getReference(idEstado.getClass(), idEstado.getIdEstado());
                asistencia.setIdEstado(idEstado);
            }
            em.persist(asistencia);
            if (idTurno != null) {
                idTurno.getAsistenciaList().add(asistencia);
                idTurno = em.merge(idTurno);
            }
            if (idEmpleado != null) {
                idEmpleado.getAsistenciaList().add(asistencia);
                idEmpleado = em.merge(idEmpleado);
            }
            if (idEstado != null) {
                idEstado.getAsistenciaList().add(asistencia);
                idEstado = em.merge(idEstado);
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
            Turno idTurnoOld = persistentAsistencia.getIdTurno();
            Turno idTurnoNew = asistencia.getIdTurno();
            Empleado idEmpleadoOld = persistentAsistencia.getIdEmpleado();
            Empleado idEmpleadoNew = asistencia.getIdEmpleado();
            Estado idEstadoOld = persistentAsistencia.getIdEstado();
            Estado idEstadoNew = asistencia.getIdEstado();
            if (idTurnoNew != null) {
                idTurnoNew = em.getReference(idTurnoNew.getClass(), idTurnoNew.getIdTurno());
                asistencia.setIdTurno(idTurnoNew);
            }
            if (idEmpleadoNew != null) {
                idEmpleadoNew = em.getReference(idEmpleadoNew.getClass(), idEmpleadoNew.getIdEmpleado());
                asistencia.setIdEmpleado(idEmpleadoNew);
            }
            if (idEstadoNew != null) {
                idEstadoNew = em.getReference(idEstadoNew.getClass(), idEstadoNew.getIdEstado());
                asistencia.setIdEstado(idEstadoNew);
            }
            asistencia = em.merge(asistencia);
            if (idTurnoOld != null && !idTurnoOld.equals(idTurnoNew)) {
                idTurnoOld.getAsistenciaList().remove(asistencia);
                idTurnoOld = em.merge(idTurnoOld);
            }
            if (idTurnoNew != null && !idTurnoNew.equals(idTurnoOld)) {
                idTurnoNew.getAsistenciaList().add(asistencia);
                idTurnoNew = em.merge(idTurnoNew);
            }
            if (idEmpleadoOld != null && !idEmpleadoOld.equals(idEmpleadoNew)) {
                idEmpleadoOld.getAsistenciaList().remove(asistencia);
                idEmpleadoOld = em.merge(idEmpleadoOld);
            }
            if (idEmpleadoNew != null && !idEmpleadoNew.equals(idEmpleadoOld)) {
                idEmpleadoNew.getAsistenciaList().add(asistencia);
                idEmpleadoNew = em.merge(idEmpleadoNew);
            }
            if (idEstadoOld != null && !idEstadoOld.equals(idEstadoNew)) {
                idEstadoOld.getAsistenciaList().remove(asistencia);
                idEstadoOld = em.merge(idEstadoOld);
            }
            if (idEstadoNew != null && !idEstadoNew.equals(idEstadoOld)) {
                idEstadoNew.getAsistenciaList().add(asistencia);
                idEstadoNew = em.merge(idEstadoNew);
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
            Turno idTurno = asistencia.getIdTurno();
            if (idTurno != null) {
                idTurno.getAsistenciaList().remove(asistencia);
                idTurno = em.merge(idTurno);
            }
            Empleado idEmpleado = asistencia.getIdEmpleado();
            if (idEmpleado != null) {
                idEmpleado.getAsistenciaList().remove(asistencia);
                idEmpleado = em.merge(idEmpleado);
            }
            Estado idEstado = asistencia.getIdEstado();
            if (idEstado != null) {
                idEstado.getAsistenciaList().remove(asistencia);
                idEstado = em.merge(idEstado);
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
    
}
