/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import dto.Datospersonal;
import dto.Cargo;
import dto.Empresa;
import dto.Usuario;
import dto.Asistencia;
import dto.Empleado;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author USER
 */
public class EmpleadoJpaController implements Serializable {

    public EmpleadoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public EmpleadoJpaController() {
    }
    
 private EntityManagerFactory emf = Persistence.createEntityManagerFactory("com.mycompany_Asistencia03_war_1.0-SNAPSHOTPU");

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Empleado empleado) {
        if (empleado.getAsistenciaList() == null) {
            empleado.setAsistenciaList(new ArrayList<Asistencia>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Datospersonal idDatos = empleado.getIdDatos();
            if (idDatos != null) {
                idDatos = em.getReference(idDatos.getClass(), idDatos.getIdDatos());
                empleado.setIdDatos(idDatos);
            }
            Cargo idcargo = empleado.getIdcargo();
            if (idcargo != null) {
                idcargo = em.getReference(idcargo.getClass(), idcargo.getIdcargo());
                empleado.setIdcargo(idcargo);
            }
            Empresa idempresa = empleado.getIdempresa();
            if (idempresa != null) {
                idempresa = em.getReference(idempresa.getClass(), idempresa.getIdempresa());
                empleado.setIdempresa(idempresa);
            }
            Usuario usuario = empleado.getUsuario();
            if (usuario != null) {
                usuario = em.getReference(usuario.getClass(), usuario.getIdUsuario());
                empleado.setUsuario(usuario);
            }
            List<Asistencia> attachedAsistenciaList = new ArrayList<Asistencia>();
            for (Asistencia asistenciaListAsistenciaToAttach : empleado.getAsistenciaList()) {
                asistenciaListAsistenciaToAttach = em.getReference(asistenciaListAsistenciaToAttach.getClass(), asistenciaListAsistenciaToAttach.getIdAsistencia());
                attachedAsistenciaList.add(asistenciaListAsistenciaToAttach);
            }
            empleado.setAsistenciaList(attachedAsistenciaList);
            em.persist(empleado);
            if (idDatos != null) {
                idDatos.getEmpleadoList().add(empleado);
                idDatos = em.merge(idDatos);
            }
            if (idcargo != null) {
                idcargo.getEmpleadoList().add(empleado);
                idcargo = em.merge(idcargo);
            }
            if (idempresa != null) {
                idempresa.getEmpleadoList().add(empleado);
                idempresa = em.merge(idempresa);
            }
            if (usuario != null) {
                Empleado oldIdEmpleadoOfUsuario = usuario.getIdEmpleado();
                if (oldIdEmpleadoOfUsuario != null) {
                    oldIdEmpleadoOfUsuario.setUsuario(null);
                    oldIdEmpleadoOfUsuario = em.merge(oldIdEmpleadoOfUsuario);
                }
                usuario.setIdEmpleado(empleado);
                usuario = em.merge(usuario);
            }
            for (Asistencia asistenciaListAsistencia : empleado.getAsistenciaList()) {
                Empleado oldIdEmpleadoOfAsistenciaListAsistencia = asistenciaListAsistencia.getIdEmpleado();
                asistenciaListAsistencia.setIdEmpleado(empleado);
                asistenciaListAsistencia = em.merge(asistenciaListAsistencia);
                if (oldIdEmpleadoOfAsistenciaListAsistencia != null) {
                    oldIdEmpleadoOfAsistenciaListAsistencia.getAsistenciaList().remove(asistenciaListAsistencia);
                    oldIdEmpleadoOfAsistenciaListAsistencia = em.merge(oldIdEmpleadoOfAsistenciaListAsistencia);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Empleado empleado) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empleado persistentEmpleado = em.find(Empleado.class, empleado.getIdEmpleado());
            Datospersonal idDatosOld = persistentEmpleado.getIdDatos();
            Datospersonal idDatosNew = empleado.getIdDatos();
            Cargo idcargoOld = persistentEmpleado.getIdcargo();
            Cargo idcargoNew = empleado.getIdcargo();
            Empresa idempresaOld = persistentEmpleado.getIdempresa();
            Empresa idempresaNew = empleado.getIdempresa();
            Usuario usuarioOld = persistentEmpleado.getUsuario();
            Usuario usuarioNew = empleado.getUsuario();
            List<Asistencia> asistenciaListOld = persistentEmpleado.getAsistenciaList();
            List<Asistencia> asistenciaListNew = empleado.getAsistenciaList();
            List<String> illegalOrphanMessages = null;
            if (usuarioOld != null && !usuarioOld.equals(usuarioNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Usuario " + usuarioOld + " since its idEmpleado field is not nullable.");
            }
            for (Asistencia asistenciaListOldAsistencia : asistenciaListOld) {
                if (!asistenciaListNew.contains(asistenciaListOldAsistencia)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Asistencia " + asistenciaListOldAsistencia + " since its idEmpleado field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idDatosNew != null) {
                idDatosNew = em.getReference(idDatosNew.getClass(), idDatosNew.getIdDatos());
                empleado.setIdDatos(idDatosNew);
            }
            if (idcargoNew != null) {
                idcargoNew = em.getReference(idcargoNew.getClass(), idcargoNew.getIdcargo());
                empleado.setIdcargo(idcargoNew);
            }
            if (idempresaNew != null) {
                idempresaNew = em.getReference(idempresaNew.getClass(), idempresaNew.getIdempresa());
                empleado.setIdempresa(idempresaNew);
            }
            if (usuarioNew != null) {
                usuarioNew = em.getReference(usuarioNew.getClass(), usuarioNew.getIdUsuario());
                empleado.setUsuario(usuarioNew);
            }
            List<Asistencia> attachedAsistenciaListNew = new ArrayList<Asistencia>();
            for (Asistencia asistenciaListNewAsistenciaToAttach : asistenciaListNew) {
                asistenciaListNewAsistenciaToAttach = em.getReference(asistenciaListNewAsistenciaToAttach.getClass(), asistenciaListNewAsistenciaToAttach.getIdAsistencia());
                attachedAsistenciaListNew.add(asistenciaListNewAsistenciaToAttach);
            }
            asistenciaListNew = attachedAsistenciaListNew;
            empleado.setAsistenciaList(asistenciaListNew);
            empleado = em.merge(empleado);
            if (idDatosOld != null && !idDatosOld.equals(idDatosNew)) {
                idDatosOld.getEmpleadoList().remove(empleado);
                idDatosOld = em.merge(idDatosOld);
            }
            if (idDatosNew != null && !idDatosNew.equals(idDatosOld)) {
                idDatosNew.getEmpleadoList().add(empleado);
                idDatosNew = em.merge(idDatosNew);
            }
            if (idcargoOld != null && !idcargoOld.equals(idcargoNew)) {
                idcargoOld.getEmpleadoList().remove(empleado);
                idcargoOld = em.merge(idcargoOld);
            }
            if (idcargoNew != null && !idcargoNew.equals(idcargoOld)) {
                idcargoNew.getEmpleadoList().add(empleado);
                idcargoNew = em.merge(idcargoNew);
            }
            if (idempresaOld != null && !idempresaOld.equals(idempresaNew)) {
                idempresaOld.getEmpleadoList().remove(empleado);
                idempresaOld = em.merge(idempresaOld);
            }
            if (idempresaNew != null && !idempresaNew.equals(idempresaOld)) {
                idempresaNew.getEmpleadoList().add(empleado);
                idempresaNew = em.merge(idempresaNew);
            }
            if (usuarioNew != null && !usuarioNew.equals(usuarioOld)) {
                Empleado oldIdEmpleadoOfUsuario = usuarioNew.getIdEmpleado();
                if (oldIdEmpleadoOfUsuario != null) {
                    oldIdEmpleadoOfUsuario.setUsuario(null);
                    oldIdEmpleadoOfUsuario = em.merge(oldIdEmpleadoOfUsuario);
                }
                usuarioNew.setIdEmpleado(empleado);
                usuarioNew = em.merge(usuarioNew);
            }
            for (Asistencia asistenciaListNewAsistencia : asistenciaListNew) {
                if (!asistenciaListOld.contains(asistenciaListNewAsistencia)) {
                    Empleado oldIdEmpleadoOfAsistenciaListNewAsistencia = asistenciaListNewAsistencia.getIdEmpleado();
                    asistenciaListNewAsistencia.setIdEmpleado(empleado);
                    asistenciaListNewAsistencia = em.merge(asistenciaListNewAsistencia);
                    if (oldIdEmpleadoOfAsistenciaListNewAsistencia != null && !oldIdEmpleadoOfAsistenciaListNewAsistencia.equals(empleado)) {
                        oldIdEmpleadoOfAsistenciaListNewAsistencia.getAsistenciaList().remove(asistenciaListNewAsistencia);
                        oldIdEmpleadoOfAsistenciaListNewAsistencia = em.merge(oldIdEmpleadoOfAsistenciaListNewAsistencia);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = empleado.getIdEmpleado();
                if (findEmpleado(id) == null) {
                    throw new NonexistentEntityException("The empleado with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empleado empleado;
            try {
                empleado = em.getReference(Empleado.class, id);
                empleado.getIdEmpleado();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The empleado with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Usuario usuarioOrphanCheck = empleado.getUsuario();
            if (usuarioOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Empleado (" + empleado + ") cannot be destroyed since the Usuario " + usuarioOrphanCheck + " in its usuario field has a non-nullable idEmpleado field.");
            }
            List<Asistencia> asistenciaListOrphanCheck = empleado.getAsistenciaList();
            for (Asistencia asistenciaListOrphanCheckAsistencia : asistenciaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Empleado (" + empleado + ") cannot be destroyed since the Asistencia " + asistenciaListOrphanCheckAsistencia + " in its asistenciaList field has a non-nullable idEmpleado field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Datospersonal idDatos = empleado.getIdDatos();
            if (idDatos != null) {
                idDatos.getEmpleadoList().remove(empleado);
                idDatos = em.merge(idDatos);
            }
            Cargo idcargo = empleado.getIdcargo();
            if (idcargo != null) {
                idcargo.getEmpleadoList().remove(empleado);
                idcargo = em.merge(idcargo);
            }
            Empresa idempresa = empleado.getIdempresa();
            if (idempresa != null) {
                idempresa.getEmpleadoList().remove(empleado);
                idempresa = em.merge(idempresa);
            }
            em.remove(empleado);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Empleado> findEmpleadoEntities() {
        return findEmpleadoEntities(true, -1, -1);
    }

    public List<Empleado> findEmpleadoEntities(int maxResults, int firstResult) {
        return findEmpleadoEntities(false, maxResults, firstResult);
    }

    private List<Empleado> findEmpleadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Empleado.class));
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

    public Empleado findEmpleado(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Empleado.class, id);
        } finally {
            em.close();
        }
    }

    public int getEmpleadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Empleado> rt = cq.from(Empleado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
     public List<Object[]> listar() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("Empleado.listar");
            return q.getResultList();
        } finally {
            em.close();
        }
    }
      

     public static void main(String[] args) {
        EmpleadoJpaController empleado=new EmpleadoJpaController();
        List<Object[]> listar =empleado.listar();
        for(Object[] object:listar){
            System.out.println(Arrays.toString(object));
        }
    }
    
}
