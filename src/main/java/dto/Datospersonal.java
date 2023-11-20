/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author USER
 */
@Entity
@Table(name = "datospersonal")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Datospersonal.findAll", query = "SELECT d FROM Datospersonal d"),
    @NamedQuery(name = "Datospersonal.findByIdDatos", query = "SELECT d FROM Datospersonal d WHERE d.idDatos = :idDatos"),
    @NamedQuery(name = "Datospersonal.findByDNIPers", query = "SELECT d FROM Datospersonal d WHERE d.dNIPers = :dNIPers"),
    @NamedQuery(name = "Datospersonal.findByNombPer", query = "SELECT d FROM Datospersonal d WHERE d.nombPer = :nombPer"),
    @NamedQuery(name = "Datospersonal.findByAppPer", query = "SELECT d FROM Datospersonal d WHERE d.appPer = :appPer"),
    @NamedQuery(name = "Datospersonal.findByApmaPer", query = "SELECT d FROM Datospersonal d WHERE d.apmaPer = :apmaPer"),
    @NamedQuery(name = "Datospersonal.findByFechaNac", query = "SELECT d FROM Datospersonal d WHERE d.fechaNac = :fechaNac"),
    @NamedQuery(name = "Datospersonal.listar", query = "SELECT d.idDatos,d.nombPer,d.appPer,d.apmaPer,d.dNIPers,d.fechaNac ,d.direccion,"
            + "d.celular,d.genero FROM Datospersonal d"),
    @NamedQuery(name = "Datospersonal.findByDireccion", query = "SELECT d FROM Datospersonal d WHERE d.direccion = :direccion"),
    @NamedQuery(name = "Datospersonal.findByCelular", query = "SELECT d FROM Datospersonal d WHERE d.celular = :celular"),
    @NamedQuery(name = "Datospersonal.findByGenero", query = "SELECT d FROM Datospersonal d WHERE d.genero = :genero")})
public class Datospersonal implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IdDatos")
    private Integer idDatos;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "DNIPers")
    private String dNIPers;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nombPer")
    private String nombPer;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "appPer")
    private String appPer;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "apmaPer")
    private String apmaPer;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fechaNac")
    @Temporal(TemporalType.DATE)
    private Date fechaNac;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "direccion")
    private String direccion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 9)
    @Column(name = "celular")
    private String celular;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "genero")
    private String genero;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idDatos")
    private List<Empleado> empleadoList;

    public Datospersonal() {
    }

    public Datospersonal(Integer idDatos) {
        this.idDatos = idDatos;
    }

    public Datospersonal(Integer idDatos, String dNIPers, String nombPer, String appPer, String apmaPer, Date fechaNac, String direccion, String celular, String genero) {
        this.idDatos = idDatos;
        this.dNIPers = dNIPers;
        this.nombPer = nombPer;
        this.appPer = appPer;
        this.apmaPer = apmaPer;
        this.fechaNac = fechaNac;
        this.direccion = direccion;
        this.celular = celular;
        this.genero = genero;
    }

    public Integer getIdDatos() {
        return idDatos;
    }

    public void setIdDatos(Integer idDatos) {
        this.idDatos = idDatos;
    }

    public String getDNIPers() {
        return dNIPers;
    }

    public void setDNIPers(String dNIPers) {
        this.dNIPers = dNIPers;
    }

    public String getNombPer() {
        return nombPer;
    }

    public void setNombPer(String nombPer) {
        this.nombPer = nombPer;
    }

    public String getAppPer() {
        return appPer;
    }

    public void setAppPer(String appPer) {
        this.appPer = appPer;
    }

    public String getApmaPer() {
        return apmaPer;
    }

    public void setApmaPer(String apmaPer) {
        this.apmaPer = apmaPer;
    }

    public Date getFechaNac() {
        return fechaNac;
    }

    public void setFechaNac(Date fechaNac) {
        this.fechaNac = fechaNac;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    @XmlTransient
    public List<Empleado> getEmpleadoList() {
        return empleadoList;
    }

    public void setEmpleadoList(List<Empleado> empleadoList) {
        this.empleadoList = empleadoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDatos != null ? idDatos.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Datospersonal)) {
            return false;
        }
        Datospersonal other = (Datospersonal) object;
        if ((this.idDatos == null && other.idDatos != null) || (this.idDatos != null && !this.idDatos.equals(other.idDatos))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.Datospersonal[ idDatos=" + idDatos + " ]";
    }
    
}
