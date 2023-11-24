/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cadastroserver.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Madu
 */
@Entity
@Table(name = "MovimentoCompra")
@NamedQueries({
    @NamedQuery(name = "MovimentoCompra.findAll", query = "SELECT m FROM MovimentoCompra m"),
    @NamedQuery(name = "MovimentoCompra.findByIdMovimentoCompra", query = "SELECT m FROM MovimentoCompra m WHERE m.idMovimentoCompra = :idMovimentoCompra"),
    @NamedQuery(name = "MovimentoCompra.findByQuantidade", query = "SELECT m FROM MovimentoCompra m WHERE m.quantidade = :quantidade"),
    @NamedQuery(name = "MovimentoCompra.findByPrecoUnitario", query = "SELECT m FROM MovimentoCompra m WHERE m.precoUnitario = :precoUnitario")})
public class MovimentoCompra implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idMovimentoCompra")
    private Integer idMovimentoCompra;
    @Column(name = "quantidade")
    private Integer quantidade;
    @Column(name = "precoUnitario")
    private Long precoUnitario;
    @JoinColumn(name = "idFornecedor", referencedColumnName = "idPessoaJuridica")
    @ManyToOne
    private PessoaJuridica idFornecedor;
    @JoinColumn(name = "idProduto", referencedColumnName = "idProduto")
    @ManyToOne
    private Produto idProduto;
    @JoinColumn(name = "idOperador", referencedColumnName = "idOperador")
    @ManyToOne
    private Usuario idOperador;

    public MovimentoCompra() {
    }

    public MovimentoCompra(Integer idMovimentoCompra) {
        this.idMovimentoCompra = idMovimentoCompra;
    }

    public Integer getIdMovimentoCompra() {
        return idMovimentoCompra;
    }

    public void setIdMovimentoCompra(Integer idMovimentoCompra) {
        this.idMovimentoCompra = idMovimentoCompra;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Long getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(Long precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public PessoaJuridica getIdFornecedor() {
        return idFornecedor;
    }

    public void setIdFornecedor(PessoaJuridica idFornecedor) {
        this.idFornecedor = idFornecedor;
    }

    public Produto getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(Produto idProduto) {
        this.idProduto = idProduto;
    }

    public Usuario getIdOperador() {
        return idOperador;
    }

    public void setIdOperador(Usuario idOperador) {
        this.idOperador = idOperador;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMovimentoCompra != null ? idMovimentoCompra.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MovimentoCompra)) {
            return false;
        }
        MovimentoCompra other = (MovimentoCompra) object;
        if ((this.idMovimentoCompra == null && other.idMovimentoCompra != null) || (this.idMovimentoCompra != null && !this.idMovimentoCompra.equals(other.idMovimentoCompra))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.MovimentoCompra[ idMovimentoCompra=" + idMovimentoCompra + " ]";
    }
    
}
