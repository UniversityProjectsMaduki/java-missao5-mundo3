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
@Table(name = "MovimentoVenda")
@NamedQueries({
    @NamedQuery(name = "MovimentoVenda.findAll", query = "SELECT m FROM MovimentoVenda m"),
    @NamedQuery(name = "MovimentoVenda.findByIdMovimentoVenda", query = "SELECT m FROM MovimentoVenda m WHERE m.idMovimentoVenda = :idMovimentoVenda"),
    @NamedQuery(name = "MovimentoVenda.findByQuantidade", query = "SELECT m FROM MovimentoVenda m WHERE m.quantidade = :quantidade"),
    @NamedQuery(name = "MovimentoVenda.findByPrecoUnitario", query = "SELECT m FROM MovimentoVenda m WHERE m.precoUnitario = :precoUnitario")})
public class MovimentoVenda implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idMovimentoVenda")
    private Integer idMovimentoVenda;
    @Column(name = "quantidade")
    private Integer quantidade;
    @Column(name = "precoUnitario")
    private Long precoUnitario;
    @JoinColumn(name = "idComprador", referencedColumnName = "idPessoaFisica")
    @ManyToOne
    private PessoaFisica idComprador;
    @JoinColumn(name = "idProduto", referencedColumnName = "idProduto")
    @ManyToOne
    private Produto idProduto;
    @JoinColumn(name = "idOperador", referencedColumnName = "idOperador")
    @ManyToOne
    private Usuario idOperador;

    public MovimentoVenda() {
    }

    public MovimentoVenda(Integer idMovimentoVenda) {
        this.idMovimentoVenda = idMovimentoVenda;
    }

    public Integer getIdMovimentoVenda() {
        return idMovimentoVenda;
    }

    public void setIdMovimentoVenda(Integer idMovimentoVenda) {
        this.idMovimentoVenda = idMovimentoVenda;
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

    public PessoaFisica getIdComprador() {
        return idComprador;
    }

    public void setIdComprador(PessoaFisica idComprador) {
        this.idComprador = idComprador;
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
        hash += (idMovimentoVenda != null ? idMovimentoVenda.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MovimentoVenda)) {
            return false;
        }
        MovimentoVenda other = (MovimentoVenda) object;
        if ((this.idMovimentoVenda == null && other.idMovimentoVenda != null) || (this.idMovimentoVenda != null && !this.idMovimentoVenda.equals(other.idMovimentoVenda))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.MovimentoVenda[ idMovimentoVenda=" + idMovimentoVenda + " ]";
    }
    
}
