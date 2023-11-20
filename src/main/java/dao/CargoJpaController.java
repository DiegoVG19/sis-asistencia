/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import dto.Cargo;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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
public class CargoJpaController implements Serializable {

    public CargoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public CargoJpaController() {
    }

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("com.mycompany_Asistencia03_war_1.0-SNAPSHOTPU");

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cargo cargo) {
        if (cargo.getEmpleadoList() == null) {
            cargo.setEmpleadoList(new ArrayList<Empleado>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Empleado> attachedEmpleadoList = new ArrayList<Empleado>();
            for (Empleado empleadoListEmpleadoToAttach : cargo.getEmpleadoList()) {
                empleadoListEmpleadoToAttach = em.getReference(empleadoListEmpleadoToAttach.getClass(), empleadoListEmpleadoToAttach.getIdEmpleado());
                attachedEmpleadoList.add(empleadoListEmpleadoToAttach);
            }
            cargo.setEmpleadoList(attachedEmpleadoList);
            em.persist(cargo);
            for (Empleado empleadoListEmpleado : cargo.getEmpleadoList()) {
                Cargo oldIdcargoOfEmpleadoListEmpleado = empleadoListEmpleado.getIdcargo();
                empleadoListEmpleado.setIdcargo(cargo);
                empleadoListEmpleado = em.merge(empleadoListEmpleado);
                if (oldIdcargoOfEmpleadoListEmpleado != null) {
                    oldIdcargoOfEmpleadoListEmpleado.getEmpleadoList().remove(empleadoListEmpleado);
                    oldIdcargoOfEmpleadoListEmpleado = em.merge(oldIdcargoOfEmpleadoListEmpleado);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cargo cargo) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cargo persistentCargo = em.find(Cargo.class, cargo.getIdcargo());
            List<Empleado> empleadoListOld = persistentCargo.getEmpleadoList();
            List<Empleado> empleadoListNew = cargo.getEmpleadoList();
            List<String> illegalOrphanMessages = null;
            for (Empleado empleadoListOldEmpleado : empleadoListOld) {
                if (!empleadoListNew.contains(empleadoListOldEmpleado)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Empleado " + empleadoListOldEmpleado + " since its idcargo field is not nullable.");
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
            cargo.setEmpleadoList(empleadoListNew);
            cargo = em.merge(cargo);
            for (Empleado empleadoListNewEmpleado : empleadoListNew) {
                if (!empleadoListOld.contains(empleadoListNewEmpleado)) {
                    Cargo oldIdcargoOfEmpleadoListNewEmpleado = empleadoListNewEmpleado.getIdcargo();
                    empleadoListNewEmpleado.setIdcargo(cargo);
                    empleadoListNewEmpleado = em.merge(empleadoListNewEmpleado);
                    if (oldIdcargoOfEmpleadoListNewEmpleado != null && !oldIdcargoOfEmpleadoListNewEmpleado.equals(cargo)) {
                        oldIdcargoOfEmpleadoListNewEmpleado.getEmpleadoList().remove(empleadoListNewEmpleado);
                        oldIdcargoOfEmpleadoListNewEmpleado = em.merge(oldIdcargoOfEmpleadoListNewEmpleado);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = cargo.getIdcargo();
                if (findCargo(id) == null) {
                    throw new NonexistentEntityException("The cargo with id " + id + " no longer exists.");
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
            Cargo cargo;
            try {
                cargo = em.getReference(Cargo.class, id);
                cargo.getIdcargo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cargo with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Empleado> empleadoListOrphanCheck = cargo.getEmpleadoList();
            for (Empleado empleadoListOrphanCheckEmpleado : empleadoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cargo (" + cargo + ") cannot be destroyed since the Empleado " + empleadoListOrphanCheckEmpleado + " in its empleadoList field has a non-nullable idcargo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(cargo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Cargo> findCargoEntities() {
        return findCargoEntities(true, -1, -1);
    }

    public List<Cargo> findCargoEntities(int maxResults, int firstResult) {
        return findCargoEntities(false, maxResults, firstResult);
    }

    private List<Cargo> findCargoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cargo.class));
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

    public Cargo findCargo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cargo.class, id);
        } finally {
            em.close();
        }
    }

    public int getCargoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cargo> rt = cq.from(Cargo.class);
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
            Query q = em.createNamedQuery("Cargo.listar");
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
     public boolean insertarCargo(Cargo cargo) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(cargo);
            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {

            return false;

        }
    }

    public static void main(String[] args) {
//        CargoJpaController cargo = new CargoJpaController();
//        List<Object[]> listar = cargo.listar();
//        for (Object[] object : listar) {
//            System.out.println(Arrays.toString(object));
//        }
        CargoJpaController cargoJpaController = new CargoJpaController();
        List<Object[]> lista = cargoJpaController.listar();
//        for (Object[] objects : lista) {
//            System.out.println(Arrays.toString(objects));
//        }
        // Crear un objeto Datospersonales para insertar
        Cargo datos = new Cargo();
        // Establecer los valores para el objeto datos
//       datos.setIdcargo(2);
        datos.setNombre("Logistica");

        // Llama al método insertarDatosPersonales y verifica si la inserción fue exitosa
        boolean insercionExitosa = cargoJpaController.insertarCargo(datos);

        // Verificar el resultado de la inserción
        if (insercionExitosa) {
            System.out.println("Datos personales insertados correctamente.");
        } else {
            System.out.println("Error al insertar los datos personales.");
        }

    }
}
