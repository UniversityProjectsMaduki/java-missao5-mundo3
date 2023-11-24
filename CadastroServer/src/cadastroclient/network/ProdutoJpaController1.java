/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cadastroclient.network;

import cadastroclient.network.exceptions.NonexistentEntityException;
import cadastroclient.network.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import cadastroserver.model.MovimentoVenda;
import java.util.ArrayList;
import java.util.Collection;
import cadastroserver.model.MovimentoCompra;
import cadastroserver.model.Produto;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Madu
 */
public class ProdutoJpaController1 implements Serializable {

    public ProdutoJpaController1(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Produto produto) throws PreexistingEntityException, Exception {
        if (produto.getMovimentoVendaCollection() == null) {
            produto.setMovimentoVendaCollection(new ArrayList<MovimentoVenda>());
        }
        if (produto.getMovimentoCompraCollection() == null) {
            produto.setMovimentoCompraCollection(new ArrayList<MovimentoCompra>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<MovimentoVenda> attachedMovimentoVendaCollection = new ArrayList<MovimentoVenda>();
            for (MovimentoVenda movimentoVendaCollectionMovimentoVendaToAttach : produto.getMovimentoVendaCollection()) {
                movimentoVendaCollectionMovimentoVendaToAttach = em.getReference(movimentoVendaCollectionMovimentoVendaToAttach.getClass(), movimentoVendaCollectionMovimentoVendaToAttach.getIdMovimentoVenda());
                attachedMovimentoVendaCollection.add(movimentoVendaCollectionMovimentoVendaToAttach);
            }
            produto.setMovimentoVendaCollection(attachedMovimentoVendaCollection);
            Collection<MovimentoCompra> attachedMovimentoCompraCollection = new ArrayList<MovimentoCompra>();
            for (MovimentoCompra movimentoCompraCollectionMovimentoCompraToAttach : produto.getMovimentoCompraCollection()) {
                movimentoCompraCollectionMovimentoCompraToAttach = em.getReference(movimentoCompraCollectionMovimentoCompraToAttach.getClass(), movimentoCompraCollectionMovimentoCompraToAttach.getIdMovimentoCompra());
                attachedMovimentoCompraCollection.add(movimentoCompraCollectionMovimentoCompraToAttach);
            }
            produto.setMovimentoCompraCollection(attachedMovimentoCompraCollection);
            em.persist(produto);
            for (MovimentoVenda movimentoVendaCollectionMovimentoVenda : produto.getMovimentoVendaCollection()) {
                Produto oldIdProdutoOfMovimentoVendaCollectionMovimentoVenda = movimentoVendaCollectionMovimentoVenda.getIdProduto();
                movimentoVendaCollectionMovimentoVenda.setIdProduto(produto);
                movimentoVendaCollectionMovimentoVenda = em.merge(movimentoVendaCollectionMovimentoVenda);
                if (oldIdProdutoOfMovimentoVendaCollectionMovimentoVenda != null) {
                    oldIdProdutoOfMovimentoVendaCollectionMovimentoVenda.getMovimentoVendaCollection().remove(movimentoVendaCollectionMovimentoVenda);
                    oldIdProdutoOfMovimentoVendaCollectionMovimentoVenda = em.merge(oldIdProdutoOfMovimentoVendaCollectionMovimentoVenda);
                }
            }
            for (MovimentoCompra movimentoCompraCollectionMovimentoCompra : produto.getMovimentoCompraCollection()) {
                Produto oldIdProdutoOfMovimentoCompraCollectionMovimentoCompra = movimentoCompraCollectionMovimentoCompra.getIdProduto();
                movimentoCompraCollectionMovimentoCompra.setIdProduto(produto);
                movimentoCompraCollectionMovimentoCompra = em.merge(movimentoCompraCollectionMovimentoCompra);
                if (oldIdProdutoOfMovimentoCompraCollectionMovimentoCompra != null) {
                    oldIdProdutoOfMovimentoCompraCollectionMovimentoCompra.getMovimentoCompraCollection().remove(movimentoCompraCollectionMovimentoCompra);
                    oldIdProdutoOfMovimentoCompraCollectionMovimentoCompra = em.merge(oldIdProdutoOfMovimentoCompraCollectionMovimentoCompra);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findProduto(produto.getIdProduto()) != null) {
                throw new PreexistingEntityException("Produto " + produto + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Produto produto) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Produto persistentProduto = em.find(Produto.class, produto.getIdProduto());
            Collection<MovimentoVenda> movimentoVendaCollectionOld = persistentProduto.getMovimentoVendaCollection();
            Collection<MovimentoVenda> movimentoVendaCollectionNew = produto.getMovimentoVendaCollection();
            Collection<MovimentoCompra> movimentoCompraCollectionOld = persistentProduto.getMovimentoCompraCollection();
            Collection<MovimentoCompra> movimentoCompraCollectionNew = produto.getMovimentoCompraCollection();
            Collection<MovimentoVenda> attachedMovimentoVendaCollectionNew = new ArrayList<MovimentoVenda>();
            for (MovimentoVenda movimentoVendaCollectionNewMovimentoVendaToAttach : movimentoVendaCollectionNew) {
                movimentoVendaCollectionNewMovimentoVendaToAttach = em.getReference(movimentoVendaCollectionNewMovimentoVendaToAttach.getClass(), movimentoVendaCollectionNewMovimentoVendaToAttach.getIdMovimentoVenda());
                attachedMovimentoVendaCollectionNew.add(movimentoVendaCollectionNewMovimentoVendaToAttach);
            }
            movimentoVendaCollectionNew = attachedMovimentoVendaCollectionNew;
            produto.setMovimentoVendaCollection(movimentoVendaCollectionNew);
            Collection<MovimentoCompra> attachedMovimentoCompraCollectionNew = new ArrayList<MovimentoCompra>();
            for (MovimentoCompra movimentoCompraCollectionNewMovimentoCompraToAttach : movimentoCompraCollectionNew) {
                movimentoCompraCollectionNewMovimentoCompraToAttach = em.getReference(movimentoCompraCollectionNewMovimentoCompraToAttach.getClass(), movimentoCompraCollectionNewMovimentoCompraToAttach.getIdMovimentoCompra());
                attachedMovimentoCompraCollectionNew.add(movimentoCompraCollectionNewMovimentoCompraToAttach);
            }
            movimentoCompraCollectionNew = attachedMovimentoCompraCollectionNew;
            produto.setMovimentoCompraCollection(movimentoCompraCollectionNew);
            produto = em.merge(produto);
            for (MovimentoVenda movimentoVendaCollectionOldMovimentoVenda : movimentoVendaCollectionOld) {
                if (!movimentoVendaCollectionNew.contains(movimentoVendaCollectionOldMovimentoVenda)) {
                    movimentoVendaCollectionOldMovimentoVenda.setIdProduto(null);
                    movimentoVendaCollectionOldMovimentoVenda = em.merge(movimentoVendaCollectionOldMovimentoVenda);
                }
            }
            for (MovimentoVenda movimentoVendaCollectionNewMovimentoVenda : movimentoVendaCollectionNew) {
                if (!movimentoVendaCollectionOld.contains(movimentoVendaCollectionNewMovimentoVenda)) {
                    Produto oldIdProdutoOfMovimentoVendaCollectionNewMovimentoVenda = movimentoVendaCollectionNewMovimentoVenda.getIdProduto();
                    movimentoVendaCollectionNewMovimentoVenda.setIdProduto(produto);
                    movimentoVendaCollectionNewMovimentoVenda = em.merge(movimentoVendaCollectionNewMovimentoVenda);
                    if (oldIdProdutoOfMovimentoVendaCollectionNewMovimentoVenda != null && !oldIdProdutoOfMovimentoVendaCollectionNewMovimentoVenda.equals(produto)) {
                        oldIdProdutoOfMovimentoVendaCollectionNewMovimentoVenda.getMovimentoVendaCollection().remove(movimentoVendaCollectionNewMovimentoVenda);
                        oldIdProdutoOfMovimentoVendaCollectionNewMovimentoVenda = em.merge(oldIdProdutoOfMovimentoVendaCollectionNewMovimentoVenda);
                    }
                }
            }
            for (MovimentoCompra movimentoCompraCollectionOldMovimentoCompra : movimentoCompraCollectionOld) {
                if (!movimentoCompraCollectionNew.contains(movimentoCompraCollectionOldMovimentoCompra)) {
                    movimentoCompraCollectionOldMovimentoCompra.setIdProduto(null);
                    movimentoCompraCollectionOldMovimentoCompra = em.merge(movimentoCompraCollectionOldMovimentoCompra);
                }
            }
            for (MovimentoCompra movimentoCompraCollectionNewMovimentoCompra : movimentoCompraCollectionNew) {
                if (!movimentoCompraCollectionOld.contains(movimentoCompraCollectionNewMovimentoCompra)) {
                    Produto oldIdProdutoOfMovimentoCompraCollectionNewMovimentoCompra = movimentoCompraCollectionNewMovimentoCompra.getIdProduto();
                    movimentoCompraCollectionNewMovimentoCompra.setIdProduto(produto);
                    movimentoCompraCollectionNewMovimentoCompra = em.merge(movimentoCompraCollectionNewMovimentoCompra);
                    if (oldIdProdutoOfMovimentoCompraCollectionNewMovimentoCompra != null && !oldIdProdutoOfMovimentoCompraCollectionNewMovimentoCompra.equals(produto)) {
                        oldIdProdutoOfMovimentoCompraCollectionNewMovimentoCompra.getMovimentoCompraCollection().remove(movimentoCompraCollectionNewMovimentoCompra);
                        oldIdProdutoOfMovimentoCompraCollectionNewMovimentoCompra = em.merge(oldIdProdutoOfMovimentoCompraCollectionNewMovimentoCompra);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = produto.getIdProduto();
                if (findProduto(id) == null) {
                    throw new NonexistentEntityException("The produto with id " + id + " no longer exists.");
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
            Produto produto;
            try {
                produto = em.getReference(Produto.class, id);
                produto.getIdProduto();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The produto with id " + id + " no longer exists.", enfe);
            }
            Collection<MovimentoVenda> movimentoVendaCollection = produto.getMovimentoVendaCollection();
            for (MovimentoVenda movimentoVendaCollectionMovimentoVenda : movimentoVendaCollection) {
                movimentoVendaCollectionMovimentoVenda.setIdProduto(null);
                movimentoVendaCollectionMovimentoVenda = em.merge(movimentoVendaCollectionMovimentoVenda);
            }
            Collection<MovimentoCompra> movimentoCompraCollection = produto.getMovimentoCompraCollection();
            for (MovimentoCompra movimentoCompraCollectionMovimentoCompra : movimentoCompraCollection) {
                movimentoCompraCollectionMovimentoCompra.setIdProduto(null);
                movimentoCompraCollectionMovimentoCompra = em.merge(movimentoCompraCollectionMovimentoCompra);
            }
            em.remove(produto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Produto> findProdutoEntities() {
        return findProdutoEntities(true, -1, -1);
    }

    public List<Produto> findProdutoEntities(int maxResults, int firstResult) {
        return findProdutoEntities(false, maxResults, firstResult);
    }

    private List<Produto> findProdutoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Produto.class));
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

    public Produto findProduto(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Produto.class, id);
        } finally {
            em.close();
        }
    }

    public int getProdutoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Produto> rt = cq.from(Produto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
