/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cadastroclient.network;

import cadastroclient.network.exceptions.NonexistentEntityException;
import cadastroclient.network.exceptions.PreexistingEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import cadastroserver.model.MovimentoVenda;
import cadastroserver.model.PessoaFisica;
import cadastroserver.model.Produto;
import cadastroserver.model.Usuario;

/**
 *
 * @author Madu
 */
public class MovimentoVendaJpaController implements Serializable {

    public MovimentoVendaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(MovimentoVenda movimentoVenda) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PessoaFisica idComprador = movimentoVenda.getIdComprador();
            if (idComprador != null) {
                idComprador = em.getReference(idComprador.getClass(), idComprador.getIdPessoaFisica());
                movimentoVenda.setIdComprador(idComprador);
            }
            Produto idProduto = movimentoVenda.getIdProduto();
            if (idProduto != null) {
                idProduto = em.getReference(idProduto.getClass(), idProduto.getIdProduto());
                movimentoVenda.setIdProduto(idProduto);
            }
            Usuario idOperador = movimentoVenda.getIdOperador();
            if (idOperador != null) {
                idOperador = em.getReference(idOperador.getClass(), idOperador.getIdOperador());
                movimentoVenda.setIdOperador(idOperador);
            }
            em.persist(movimentoVenda);
            if (idComprador != null) {
                idComprador.getMovimentoVendaCollection().add(movimentoVenda);
                idComprador = em.merge(idComprador);
            }
            if (idProduto != null) {
                idProduto.getMovimentoVendaCollection().add(movimentoVenda);
                idProduto = em.merge(idProduto);
            }
            if (idOperador != null) {
                idOperador.getMovimentoVendaCollection().add(movimentoVenda);
                idOperador = em.merge(idOperador);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findMovimentoVenda(movimentoVenda.getIdMovimentoVenda()) != null) {
                throw new PreexistingEntityException("MovimentoVenda " + movimentoVenda + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(MovimentoVenda movimentoVenda) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MovimentoVenda persistentMovimentoVenda = em.find(MovimentoVenda.class, movimentoVenda.getIdMovimentoVenda());
            PessoaFisica idCompradorOld = persistentMovimentoVenda.getIdComprador();
            PessoaFisica idCompradorNew = movimentoVenda.getIdComprador();
            Produto idProdutoOld = persistentMovimentoVenda.getIdProduto();
            Produto idProdutoNew = movimentoVenda.getIdProduto();
            Usuario idOperadorOld = persistentMovimentoVenda.getIdOperador();
            Usuario idOperadorNew = movimentoVenda.getIdOperador();
            if (idCompradorNew != null) {
                idCompradorNew = em.getReference(idCompradorNew.getClass(), idCompradorNew.getIdPessoaFisica());
                movimentoVenda.setIdComprador(idCompradorNew);
            }
            if (idProdutoNew != null) {
                idProdutoNew = em.getReference(idProdutoNew.getClass(), idProdutoNew.getIdProduto());
                movimentoVenda.setIdProduto(idProdutoNew);
            }
            if (idOperadorNew != null) {
                idOperadorNew = em.getReference(idOperadorNew.getClass(), idOperadorNew.getIdOperador());
                movimentoVenda.setIdOperador(idOperadorNew);
            }
            movimentoVenda = em.merge(movimentoVenda);
            if (idCompradorOld != null && !idCompradorOld.equals(idCompradorNew)) {
                idCompradorOld.getMovimentoVendaCollection().remove(movimentoVenda);
                idCompradorOld = em.merge(idCompradorOld);
            }
            if (idCompradorNew != null && !idCompradorNew.equals(idCompradorOld)) {
                idCompradorNew.getMovimentoVendaCollection().add(movimentoVenda);
                idCompradorNew = em.merge(idCompradorNew);
            }
            if (idProdutoOld != null && !idProdutoOld.equals(idProdutoNew)) {
                idProdutoOld.getMovimentoVendaCollection().remove(movimentoVenda);
                idProdutoOld = em.merge(idProdutoOld);
            }
            if (idProdutoNew != null && !idProdutoNew.equals(idProdutoOld)) {
                idProdutoNew.getMovimentoVendaCollection().add(movimentoVenda);
                idProdutoNew = em.merge(idProdutoNew);
            }
            if (idOperadorOld != null && !idOperadorOld.equals(idOperadorNew)) {
                idOperadorOld.getMovimentoVendaCollection().remove(movimentoVenda);
                idOperadorOld = em.merge(idOperadorOld);
            }
            if (idOperadorNew != null && !idOperadorNew.equals(idOperadorOld)) {
                idOperadorNew.getMovimentoVendaCollection().add(movimentoVenda);
                idOperadorNew = em.merge(idOperadorNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = movimentoVenda.getIdMovimentoVenda();
                if (findMovimentoVenda(id) == null) {
                    throw new NonexistentEntityException("The movimentoVenda with id " + id + " no longer exists.");
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
            MovimentoVenda movimentoVenda;
            try {
                movimentoVenda = em.getReference(MovimentoVenda.class, id);
                movimentoVenda.getIdMovimentoVenda();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The movimentoVenda with id " + id + " no longer exists.", enfe);
            }
            PessoaFisica idComprador = movimentoVenda.getIdComprador();
            if (idComprador != null) {
                idComprador.getMovimentoVendaCollection().remove(movimentoVenda);
                idComprador = em.merge(idComprador);
            }
            Produto idProduto = movimentoVenda.getIdProduto();
            if (idProduto != null) {
                idProduto.getMovimentoVendaCollection().remove(movimentoVenda);
                idProduto = em.merge(idProduto);
            }
            Usuario idOperador = movimentoVenda.getIdOperador();
            if (idOperador != null) {
                idOperador.getMovimentoVendaCollection().remove(movimentoVenda);
                idOperador = em.merge(idOperador);
            }
            em.remove(movimentoVenda);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<MovimentoVenda> findMovimentoVendaEntities() {
        return findMovimentoVendaEntities(true, -1, -1);
    }

    public List<MovimentoVenda> findMovimentoVendaEntities(int maxResults, int firstResult) {
        return findMovimentoVendaEntities(false, maxResults, firstResult);
    }

    private List<MovimentoVenda> findMovimentoVendaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(MovimentoVenda.class));
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

    public MovimentoVenda findMovimentoVenda(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(MovimentoVenda.class, id);
        } finally {
            em.close();
        }
    }

    public int getMovimentoVendaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<MovimentoVenda> rt = cq.from(MovimentoVenda.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
