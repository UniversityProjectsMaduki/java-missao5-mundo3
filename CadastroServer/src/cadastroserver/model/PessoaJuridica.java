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
@Table(name = "PessoaJuridica")
@NamedQueries({
    @NamedQuery(name = "PessoaJuridica.findAll", query = "SELECT p FROM PessoaJuridica p"),
    @NamedQuery(name = "PessoaJuridica.findByIdPessoaJuridica", query = "SELECT p FROM PessoaJuridica p WHERE p.idPessoaJuridica = :idPessoaJuridica"),
    @NamedQuery(name = "PessoaJuridica.findByCnpj", query = "SELECT p FROM PessoaJuridica p WHERE p.cnpj = :cnpj")})
public class PessoaJuridica implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idPessoaJuridica")
    private Integer idPessoaJuridica;
    @Column(name = "cnpj")
    private String cnpj;
    @OneToMany(mappedBy = "idFornecedor")
    private Collection<MovimentoCompra> movimentoCompraCollection;

    public PessoaJuridica() {
    }

    public PessoaJuridica(Integer idPessoaJuridica) {
        this.idPessoaJuridica = idPessoaJuridica;
    }

    public Integer getIdPessoaJuridica() {
        return idPessoaJuridica;
    }

    public void setIdPessoaJuridica(Integer idPessoaJuridica) {
        this.idPessoaJuridica = idPessoaJuridica;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
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
        hash += (idPessoaJuridica != null ? idPessoaJuridica.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PessoaJuridica)) {
            return false;
        }
        PessoaJuridica other = (PessoaJuridica) object;
        if ((this.idPessoaJuridica == null && other.idPessoaJuridica != null) || (this.idPessoaJuridica != null && !this.idPessoaJuridica.equals(other.idPessoaJuridica))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.PessoaJuridica[ idPessoaJuridica=" + idPessoaJuridica + " ]";
    }
    
}
