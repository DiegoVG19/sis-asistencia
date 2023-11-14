/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author redcr
 */
@Entity
@Table(name = "tabla_asistencia")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TablaAsistencia.findAll", query = "SELECT t FROM TablaAsistencia t"),
    @NamedQuery(name = "TablaAsistencia.findByIdAsistencia", query = "SELECT t FROM TablaAsistencia t WHERE t.idAsistencia = :idAsistencia"),
    @NamedQuery(name = "TablaAsistencia.findByEmpleado", query = "SELECT t FROM TablaAsistencia t WHERE t.empleado = :empleado"),
    @NamedQuery(name = "TablaAsistencia.findByAsistencia", query = "SELECT t FROM TablaAsistencia t WHERE t.asistencia = :asistencia")})
public class TablaAsistencia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_asistencia")
    private Integer idAsistencia;
    @Size(max = 255)
    @Column(name = "empleado")
    private String empleado;
    @Column(name = "asistencia")
    private Character asistencia;

    public TablaAsistencia() {
    }

    public TablaAsistencia(Integer idAsistencia) {
        this.idAsistencia = idAsistencia;
    }

    public Integer getIdAsistencia() {
        return idAsistencia;
    }

    public void setIdAsistencia(Integer idAsistencia) {
        this.idAsistencia = idAsistencia;
    }

    public String getEmpleado() {
        return empleado;
    }

    public void setEmpleado(String empleado) {
        this.empleado = empleado;
    }

    public Character getAsistencia() {
        return asistencia;
    }

    public void setAsistencia(Character asistencia) {
        this.asistencia = asistencia;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAsistencia != null ? idAsistencia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TablaAsistencia)) {
            return false;
        }
        TablaAsistencia other = (TablaAsistencia) object;
        if ((this.idAsistencia == null && other.idAsistencia != null) || (this.idAsistencia != null && !this.idAsistencia.equals(other.idAsistencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.TablaAsistencia[ idAsistencia=" + idAsistencia + " ]";
    }
    
}
