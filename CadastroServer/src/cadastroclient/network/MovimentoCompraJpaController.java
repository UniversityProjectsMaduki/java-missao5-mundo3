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
import cadastroserver.model.MovimentoCompra;
import cadastroserver.model.PessoaJuridica;
import cadastroserver.model.Produto;
import cadastroserver.model.Usuario;


public class MovimentoCompraJpaController implements Serializable {

    public MovimentoCompraJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(MovimentoCompra movimentoCompra) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PessoaJuridica idFornecedor = movimentoCompra.getIdFornecedor();
            if (idFornecedor != null) {
                idFornecedor = em.getReference(idFornecedor.getClass(), idFornecedor.getIdPessoaJuridica());
                movimentoCompra.setIdFornecedor(idFornecedor);
            }
            Produto idProduto = movimentoCompra.getIdProduto();
            if (idProduto != null) {
                idProduto = em.getReference(idProduto.getClass(), idProduto.getIdProduto());
                movimentoCompra.setIdProduto(idProduto);
            }
            Usuario idOperador = movimentoCompra.getIdOperador();
            if (idOperador != null) {
                idOperador = em.getReference(idOperador.getClass(), idOperador.getIdOperador());
                movimentoCompra.setIdOperador(idOperador);
            }
            em.persist(movimentoCompra);
            if (idFornecedor != null) {
                idFornecedor.getMovimentoCompraCollection().add(movimentoCompra);
                idFornecedor = em.merge(idFornecedor);
            }
            if (idProduto != null) {
                idProduto.getMovimentoCompraCollection().add(movimentoCompra);
                idProduto = em.merge(idProduto);
            }
            if (idOperador != null) {
                idOperador.getMovimentoCompraCollection().add(movimentoCompra);
                idOperador = em.merge(idOperador);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findMovimentoCompra(movimentoCompra.getIdMovimentoCompra()) != null) {
                throw new PreexistingEntityException("MovimentoCompra " + movimentoCompra + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(MovimentoCompra movimentoCompra) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MovimentoCompra persistentMovimentoCompra = em.find(MovimentoCompra.class, movimentoCompra.getIdMovimentoCompra());
            PessoaJuridica idFornecedorOld = persistentMovimentoCompra.getIdFornecedor();
            PessoaJuridica idFornecedorNew = movimentoCompra.getIdFornecedor();
            Produto idProdutoOld = persistentMovimentoCompra.getIdProduto();
            Produto idProdutoNew = movimentoCompra.getIdProduto();
            Usuario idOperadorOld = persistentMovimentoCompra.getIdOperador();
            Usuario idOperadorNew = movimentoCompra.getIdOperador();
            if (idFornecedorNew != null) {
                idFornecedorNew = em.getReference(idFornecedorNew.getClass(), idFornecedorNew.getIdPessoaJuridica());
                movimentoCompra.setIdFornecedor(idFornecedorNew);
            }
            if (idProdutoNew != null) {
                idProdutoNew = em.getReference(idProdutoNew.getClass(), idProdutoNew.getIdProduto());
                movimentoCompra.setIdProduto(idProdutoNew);
            }
            if (idOperadorNew != null) {
                idOperadorNew = em.getReference(idOperadorNew.getClass(), idOperadorNew.getIdOperador());
                movimentoCompra.setIdOperador(idOperadorNew);
            }
            movimentoCompra = em.merge(movimentoCompra);
            if (idFornecedorOld != null && !idFornecedorOld.equals(idFornecedorNew)) {
                idFornecedorOld.getMovimentoCompraCollection().remove(movimentoCompra);
                idFornecedorOld = em.merge(idFornecedorOld);
            }
            if (idFornecedorNew != null && !idFornecedorNew.equals(idFornecedorOld)) {
                idFornecedorNew.getMovimentoCompraCollection().add(movimentoCompra);
                idFornecedorNew = em.merge(idFornecedorNew);
            }
            if (idProdutoOld != null && !idProdutoOld.equals(idProdutoNew)) {
                idProdutoOld.getMovimentoCompraCollection().remove(movimentoCompra);
                idProdutoOld = em.merge(idProdutoOld);
            }
            if (idProdutoNew != null && !idProdutoNew.equals(idProdutoOld)) {
                idProdutoNew.getMovimentoCompraCollection().add(movimentoCompra);
                idProdutoNew = em.merge(idProdutoNew);
            }
            if (idOperadorOld != null && !idOperadorOld.equals(idOperadorNew)) {
                idOperadorOld.getMovimentoCompraCollection().remove(movimentoCompra);
                idOperadorOld = em.merge(idOperadorOld);
            }
            if (idOperadorNew != null && !idOperadorNew.equals(idOperadorOld)) {
                idOperadorNew.getMovimentoCompraCollection().add(movimentoCompra);
                idOperadorNew = em.merge(idOperadorNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = movimentoCompra.getIdMovimentoCompra();
                if (findMovimentoCompra(id) == null) {
                    throw new NonexistentEntityException("The movimentoCompra with id " + id + " no longer exists.");
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
            MovimentoCompra movimentoCompra;
            try {
                movimentoCompra = em.getReference(MovimentoCompra.class, id);
                movimentoCompra.getIdMovimentoCompra();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The movimentoCompra with id " + id + " no longer exists.", enfe);
            }
            PessoaJuridica idFornecedor = movimentoCompra.getIdFornecedor();
            if (idFornecedor != null) {
                idFornecedor.getMovimentoCompraCollection().remove(movimentoCompra);
                idFornecedor = em.merge(idFornecedor);
            }
            Produto idProduto = movimentoCompra.getIdProduto();
            if (idProduto != null) {
                idProduto.getMovimentoCompraCollection().remove(movimentoCompra);
                idProduto = em.merge(idProduto);
            }
            Usuario idOperador = movimentoCompra.getIdOperador();
            if (idOperador != null) {
                idOperador.getMovimentoCompraCollection().remove(movimentoCompra);
                idOperador = em.merge(idOperador);
            }
            em.remove(movimentoCompra);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<MovimentoCompra> findMovimentoCompraEntities() {
        return findMovimentoCompraEntities(true, -1, -1);
    }

    public List<MovimentoCompra> findMovimentoCompraEntities(int maxResults, int firstResult) {
        return findMovimentoCompraEntities(false, maxResults, firstResult);
    }

    private List<MovimentoCompra> findMovimentoCompraEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(MovimentoCompra.class));
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

    public MovimentoCompra findMovimentoCompra(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(MovimentoCompra.class, id);
        } finally {
            em.close();
        }
    }

    public int getMovimentoCompraCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<MovimentoCompra> rt = cq.from(MovimentoCompra.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
