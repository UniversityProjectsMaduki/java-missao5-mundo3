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
import cadastroserver.model.MovimentoCompra;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import cadastroserver.model.PessoaJuridica;

/**
 *
 * @author Madu
 */
public class PessoaJuridicaJpaController implements Serializable {

    public PessoaJuridicaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PessoaJuridica pessoaJuridica) throws PreexistingEntityException, Exception {
        if (pessoaJuridica.getMovimentoCompraCollection() == null) {
            pessoaJuridica.setMovimentoCompraCollection(new ArrayList<MovimentoCompra>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<MovimentoCompra> attachedMovimentoCompraCollection = new ArrayList<MovimentoCompra>();
            for (MovimentoCompra movimentoCompraCollectionMovimentoCompraToAttach : pessoaJuridica.getMovimentoCompraCollection()) {
                movimentoCompraCollectionMovimentoCompraToAttach = em.getReference(movimentoCompraCollectionMovimentoCompraToAttach.getClass(), movimentoCompraCollectionMovimentoCompraToAttach.getIdMovimentoCompra());
                attachedMovimentoCompraCollection.add(movimentoCompraCollectionMovimentoCompraToAttach);
            }
            pessoaJuridica.setMovimentoCompraCollection(attachedMovimentoCompraCollection);
            em.persist(pessoaJuridica);
            for (MovimentoCompra movimentoCompraCollectionMovimentoCompra : pessoaJuridica.getMovimentoCompraCollection()) {
                PessoaJuridica oldIdFornecedorOfMovimentoCompraCollectionMovimentoCompra = movimentoCompraCollectionMovimentoCompra.getIdFornecedor();
                movimentoCompraCollectionMovimentoCompra.setIdFornecedor(pessoaJuridica);
                movimentoCompraCollectionMovimentoCompra = em.merge(movimentoCompraCollectionMovimentoCompra);
                if (oldIdFornecedorOfMovimentoCompraCollectionMovimentoCompra != null) {
                    oldIdFornecedorOfMovimentoCompraCollectionMovimentoCompra.getMovimentoCompraCollection().remove(movimentoCompraCollectionMovimentoCompra);
                    oldIdFornecedorOfMovimentoCompraCollectionMovimentoCompra = em.merge(oldIdFornecedorOfMovimentoCompraCollectionMovimentoCompra);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPessoaJuridica(pessoaJuridica.getIdPessoaJuridica()) != null) {
                throw new PreexistingEntityException("PessoaJuridica " + pessoaJuridica + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PessoaJuridica pessoaJuridica) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PessoaJuridica persistentPessoaJuridica = em.find(PessoaJuridica.class, pessoaJuridica.getIdPessoaJuridica());
            Collection<MovimentoCompra> movimentoCompraCollectionOld = persistentPessoaJuridica.getMovimentoCompraCollection();
            Collection<MovimentoCompra> movimentoCompraCollectionNew = pessoaJuridica.getMovimentoCompraCollection();
            Collection<MovimentoCompra> attachedMovimentoCompraCollectionNew = new ArrayList<MovimentoCompra>();
            for (MovimentoCompra movimentoCompraCollectionNewMovimentoCompraToAttach : movimentoCompraCollectionNew) {
                movimentoCompraCollectionNewMovimentoCompraToAttach = em.getReference(movimentoCompraCollectionNewMovimentoCompraToAttach.getClass(), movimentoCompraCollectionNewMovimentoCompraToAttach.getIdMovimentoCompra());
                attachedMovimentoCompraCollectionNew.add(movimentoCompraCollectionNewMovimentoCompraToAttach);
            }
            movimentoCompraCollectionNew = attachedMovimentoCompraCollectionNew;
            pessoaJuridica.setMovimentoCompraCollection(movimentoCompraCollectionNew);
            pessoaJuridica = em.merge(pessoaJuridica);
            for (MovimentoCompra movimentoCompraCollectionOldMovimentoCompra : movimentoCompraCollectionOld) {
                if (!movimentoCompraCollectionNew.contains(movimentoCompraCollectionOldMovimentoCompra)) {
                    movimentoCompraCollectionOldMovimentoCompra.setIdFornecedor(null);
                    movimentoCompraCollectionOldMovimentoCompra = em.merge(movimentoCompraCollectionOldMovimentoCompra);
                }
            }
            for (MovimentoCompra movimentoCompraCollectionNewMovimentoCompra : movimentoCompraCollectionNew) {
                if (!movimentoCompraCollectionOld.contains(movimentoCompraCollectionNewMovimentoCompra)) {
                    PessoaJuridica oldIdFornecedorOfMovimentoCompraCollectionNewMovimentoCompra = movimentoCompraCollectionNewMovimentoCompra.getIdFornecedor();
                    movimentoCompraCollectionNewMovimentoCompra.setIdFornecedor(pessoaJuridica);
                    movimentoCompraCollectionNewMovimentoCompra = em.merge(movimentoCompraCollectionNewMovimentoCompra);
                    if (oldIdFornecedorOfMovimentoCompraCollectionNewMovimentoCompra != null && !oldIdFornecedorOfMovimentoCompraCollectionNewMovimentoCompra.equals(pessoaJuridica)) {
                        oldIdFornecedorOfMovimentoCompraCollectionNewMovimentoCompra.getMovimentoCompraCollection().remove(movimentoCompraCollectionNewMovimentoCompra);
                        oldIdFornecedorOfMovimentoCompraCollectionNewMovimentoCompra = em.merge(oldIdFornecedorOfMovimentoCompraCollectionNewMovimentoCompra);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pessoaJuridica.getIdPessoaJuridica();
                if (findPessoaJuridica(id) == null) {
                    throw new NonexistentEntityException("The pessoaJuridica with id " + id + " no longer exists.");
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
            PessoaJuridica pessoaJuridica;
            try {
                pessoaJuridica = em.getReference(PessoaJuridica.class, id);
                pessoaJuridica.getIdPessoaJuridica();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pessoaJuridica with id " + id + " no longer exists.", enfe);
            }
            Collection<MovimentoCompra> movimentoCompraCollection = pessoaJuridica.getMovimentoCompraCollection();
            for (MovimentoCompra movimentoCompraCollectionMovimentoCompra : movimentoCompraCollection) {
                movimentoCompraCollectionMovimentoCompra.setIdFornecedor(null);
                movimentoCompraCollectionMovimentoCompra = em.merge(movimentoCompraCollectionMovimentoCompra);
            }
            em.remove(pessoaJuridica);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<PessoaJuridica> findPessoaJuridicaEntities() {
        return findPessoaJuridicaEntities(true, -1, -1);
    }

    public List<PessoaJuridica> findPessoaJuridicaEntities(int maxResults, int firstResult) {
        return findPessoaJuridicaEntities(false, maxResults, firstResult);
    }

    private List<PessoaJuridica> findPessoaJuridicaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PessoaJuridica.class));
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

    public PessoaJuridica findPessoaJuridica(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PessoaJuridica.class, id);
        } finally {
            em.close();
        }
    }

    public int getPessoaJuridicaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PessoaJuridica> rt = cq.from(PessoaJuridica.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
