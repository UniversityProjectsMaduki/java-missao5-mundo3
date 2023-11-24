/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cadastroserver.model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Madu
 */
@Entity
@Table(name = "Usuario")
@NamedQueries({
    @NamedQuery(name = "Usuario.findAll", query = "SELECT u FROM Usuario u"),
    @NamedQuery(name = "Usuario.findByIdOperador", query = "SELECT u FROM Usuario u WHERE u.idOperador = :idOperador"),
    @NamedQuery(name = "Usuario.findByNome", query = "SELECT u FROM Usuario u WHERE u.nome = :nome"),
    @NamedQuery(name = "Usuario.findBySenha", query = "SELECT u FROM Usuario u WHERE u.senha = :senha")})
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idOperador")
    private Integer idOperador;
    @Column(name = "nome")
    private String nome;
    @Column(name = "senha")
    private String senha;
    @OneToMany(mappedBy = "idOperador")
    private Collection<MovimentoVenda> movimentoVendaCollection;
    @OneToMany(mappedBy = "idOperador")
    private Collection<MovimentoCompra> movimentoCompraCollection;

    public Usuario() {
    }

    public Usuario(Integer idOperador) {
        this.idOperador = idOperador;
    }

    public Integer getIdOperador() {
        return idOperador;
    }

    public void setIdOperador(Integer idOperador) {
        this.idOperador = idOperador;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Collection<MovimentoVenda> getMovimentoVendaCollection() {
        return movimentoVendaCollection;
    }

    public void setMovimentoVendaCollection(Collection<MovimentoVenda> movimentoVendaCollection) {
        this.movimentoVendaCollection = movimentoVendaCollection;
    }

    public Collection<MovimentoCompra> getMovimentoCompraCollection() {
        return movimentoCompraCollection;
    }

    public void setMovimentoCompraCollection(Collection<MovimentoCompra> movimentoCompraCollection) {
        this.movimentoCompraCollection = movimentoCompraCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idOperador != null ? idOperador.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.idOperador == null && other.idOperador != null) || (this.idOperador != null && !this.idOperador.equals(other.idOperador))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Usuario[ idOperador=" + idOperador + " ]";
    }
    
}
