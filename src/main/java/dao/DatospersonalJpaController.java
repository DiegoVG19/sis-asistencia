/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import dto.Datospersonal;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import dto.Empleado;
import java.time.LocalDate;
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
public class DatospersonalJpaController implements Serializable {

    public DatospersonalJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public DatospersonalJpaController() {
    }

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("com.mycompany_Asistencia03_war_1.0-SNAPSHOTPU");

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Datospersonal datospersonal) {
        if (datospersonal.getEmpleadoList() == null) {
            datospersonal.setEmpleadoList(new ArrayList<Empleado>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Empleado> attachedEmpleadoList = new ArrayList<Empleado>();
            for (Empleado empleadoListEmpleadoToAttach : datospersonal.getEmpleadoList()) {
                empleadoListEmpleadoToAttach = em.getReference(empleadoListEmpleadoToAttach.getClass(), empleadoListEmpleadoToAttach.getIdEmpleado());
                attachedEmpleadoList.add(empleadoListEmpleadoToAttach);
            }
            datospersonal.setEmpleadoList(attachedEmpleadoList);
            em.persist(datospersonal);
            for (Empleado empleadoListEmpleado : datospersonal.getEmpleadoList()) {
                Datospersonal oldIdDatosOfEmpleadoListEmpleado = empleadoListEmpleado.getIdDatos();
                empleadoListEmpleado.setIdDatos(datospersonal);
                empleadoListEmpleado = em.merge(empleadoListEmpleado);
                if (oldIdDatosOfEmpleadoListEmpleado != null) {
                    oldIdDatosOfEmpleadoListEmpleado.getEmpleadoList().remove(empleadoListEmpleado);
                    oldIdDatosOfEmpleadoListEmpleado = em.merge(oldIdDatosOfEmpleadoListEmpleado);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Datospersonal datospersonal) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Datospersonal persistentDatospersonal = em.find(Datospersonal.class, datospersonal.getIdDatos());
            List<Empleado> empleadoListOld = persistentDatospersonal.getEmpleadoList();
            List<Empleado> empleadoListNew = datospersonal.getEmpleadoList();
            List<String> illegalOrphanMessages = null;
            for (Empleado empleadoListOldEmpleado : empleadoListOld) {
                if (!empleadoListNew.contains(empleadoListOldEmpleado)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Empleado " + empleadoListOldEmpleado + " since its idDatos field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Empleado> attachedEmpleadoListNew = new ArrayList<Empleado>();
            for (Empleado empleadoListNewEmpleadoToAttach : empleadoListNew) {
                empleadoListNewEmpleadoToAttach = em.getReference(empleadoListNewEmpleadoToAttach.getClass(), empleadoListNewEmpleadoToAttach.getIdEmpleado());
                attachedEmpleadoListNew.add(empleadoListNewEmpleadoToAttach);
            }
            empleadoListNew = attachedEmpleadoListNew;
            datospersonal.setEmpleadoList(empleadoListNew);
            datospersonal = em.merge(datospersonal);
            for (Empleado empleadoListNewEmpleado : empleadoListNew) {
                if (!empleadoListOld.contains(empleadoListNewEmpleado)) {
                    Datospersonal oldIdDatosOfEmpleadoListNewEmpleado = empleadoListNewEmpleado.getIdDatos();
                    empleadoListNewEmpleado.setIdDatos(datospersonal);
                    empleadoListNewEmpleado = em.merge(empleadoListNewEmpleado);
                    if (oldIdDatosOfEmpleadoListNewEmpleado != null && !oldIdDatosOfEmpleadoListNewEmpleado.equals(datospersonal)) {
                        oldIdDatosOfEmpleadoListNewEmpleado.getEmpleadoList().remove(empleadoListNewEmpleado);
                        oldIdDatosOfEmpleadoListNewEmpleado = em.merge(oldIdDatosOfEmpleadoListNewEmpleado);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = datospersonal.getIdDatos();
                if (findDatospersonal(id) == null) {
                    throw new NonexistentEntityException("The datospersonal with id " + id + " no longer exists.");
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
            Datospersonal datospersonal;
            try {
                datospersonal = em.getReference(Datospersonal.class, id);
                datospersonal.getIdDatos();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The datospersonal with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Empleado> empleadoListOrphanCheck = datospersonal.getEmpleadoList();
            for (Empleado empleadoListOrphanCheckEmpleado : empleadoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Datospersonal (" + datospersonal + ") cannot be destroyed since the Empleado " + empleadoListOrphanCheckEmpleado + " in its empleadoList field has a non-nullable idDatos field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(datospersonal);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Datospersonal> findDatospersonalEntities() {
        return findDatospersonalEntities(true, -1, -1);
    }

    public List<Datospersonal> findDatospersonalEntities(int maxResults, int firstResult) {
        return findDatospersonalEntities(false, maxResults, firstResult);
    }

    private List<Datospersonal> findDatospersonalEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Datospersonal.class));
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

    public Datospersonal findDatospersonal(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Datospersonal.class, id);
        } finally {
            em.close();
        }
    }

    public int getDatospersonalCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Datospersonal> rt = cq.from(Datospersonal.class);
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
            Query q = em.createNamedQuery("Datospersonal.listar");
            return q.getResultList();
        } finally {
            em.close();
        }
    }
     public boolean insertarDatosPersonal(Datospersonal datosPersonal) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(datosPersonal);
            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {

            return false;

        }
    }

    public static void main(String[] args) {
//        DatospersonalJpaController datos = new DatospersonalJpaController();
//        List<Object[]> listar = datos.listar();
//        for (Object[] object : listar) {
//            System.out.println(Arrays.toString(object));
//        }
 DatospersonalJpaController datospersonalJpaController = new DatospersonalJpaController();
        List<Object[]> lista = datospersonalJpaController.listar();
         LocalDate fecha = LocalDate.of(2023, 11, 18);
//        for (Object[] objects : lista) {
//            System.out.println(Arrays.toString(objects));
//        }
        // Crear un objeto Datospersonales para insertar
        Datospersonal  datosPersonal = new Datospersonal();
        // Establecer los valores para el objeto datos
//       datos.setIdcargo(2);
        datosPersonal.setNombPer("Liseth");
        datosPersonal.setAppPer("Chavez");
        datosPersonal.setApmaPer("Gonzales");
        datosPersonal.setDNIPers("77775555");
        datosPersonal.setFechaNac(java.sql.Date.valueOf(fecha));
        datosPersonal.setDireccion("Av.Las Malvinas");
        datosPersonal.setCelular("924401767");
        datosPersonal.setGenero("Femenino");

        // Llama al método insertarDatosPersonales y verifica si la inserción fue exitosa
        boolean insercionExitosa = datospersonalJpaController.insertarDatosPersonal(datosPersonal);

        // Verificar el resultado de la inserción
        if (insercionExitosa) {
            System.out.println("Datos personales insertados correctamente.");
        } else {
            System.out.println("Error al insertar los datos personales.");
        }

    }
}
