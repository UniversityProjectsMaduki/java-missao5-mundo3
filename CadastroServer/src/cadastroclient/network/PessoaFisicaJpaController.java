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
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import cadastroserver.model.PessoaFisica;

/**
 *
 * @author Madu
 */
public class PessoaFisicaJpaController implements Serializable {

    public PessoaFisicaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PessoaFisica pessoaFisica) throws PreexistingEntityException, Exception {
        if (pessoaFisica.getMovimentoVendaCollection() == null) {
            pessoaFisica.setMovimentoVendaCollection(new ArrayList<MovimentoVenda>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<MovimentoVenda> attachedMovimentoVendaCollection = new ArrayList<MovimentoVenda>();
            for (MovimentoVenda movimentoVendaCollectionMovimentoVendaToAttach : pessoaFisica.getMovimentoVendaCollection()) {
                movimentoVendaCollectionMovimentoVendaToAttach = em.getReference(movimentoVendaCollectionMovimentoVendaToAttach.getClass(), movimentoVendaCollectionMovimentoVendaToAttach.getIdMovimentoVenda());
                attachedMovimentoVendaCollection.add(movimentoVendaCollectionMovimentoVendaToAttach);
            }
            pessoaFisica.setMovimentoVendaCollection(attachedMovimentoVendaCollection);
            em.persist(pessoaFisica);
            for (MovimentoVenda movimentoVendaCollectionMovimentoVenda : pessoaFisica.getMovimentoVendaCollection()) {
                PessoaFisica oldIdCompradorOfMovimentoVendaCollectionMovimentoVenda = movimentoVendaCollectionMovimentoVenda.getIdComprador();
                movimentoVendaCollectionMovimentoVenda.setIdComprador(pessoaFisica);
                movimentoVendaCollectionMovimentoVenda = em.merge(movimentoVendaCollectionMovimentoVenda);
                if (oldIdCompradorOfMovimentoVendaCollectionMovimentoVenda != null) {
                    oldIdCompradorOfMovimentoVendaCollectionMovimentoVenda.getMovimentoVendaCollection().remove(movimentoVendaCollectionMovimentoVenda);
                    oldIdCompradorOfMovimentoVendaCollectionMovimentoVenda = em.merge(oldIdCompradorOfMovimentoVendaCollectionMovimentoVenda);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPessoaFisica(pessoaFisica.getIdPessoaFisica()) != null) {
                throw new PreexistingEntityException("PessoaFisica " + pessoaFisica + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PessoaFisica pessoaFisica) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PessoaFisica persistentPessoaFisica = em.find(PessoaFisica.class, pessoaFisica.getIdPessoaFisica());
            Collection<MovimentoVenda> movimentoVendaCollectionOld = persistentPessoaFisica.getMovimentoVendaCollection();
            Collection<MovimentoVenda> movimentoVendaCollectionNew = pessoaFisica.getMovimentoVendaCollection();
            Collection<MovimentoVenda> attachedMovimentoVendaCollectionNew = new ArrayList<MovimentoVenda>();
            for (MovimentoVenda movimentoVendaCollectionNewMovimentoVendaToAttach : movimentoVendaCollectionNew) {
                movimentoVendaCollectionNewMovimentoVendaToAttach = em.getReference(movimentoVendaCollectionNewMovimentoVendaToAttach.getClass(), movimentoVendaCollectionNewMovimentoVendaToAttach.getIdMovimentoVenda());
                attachedMovimentoVendaCollectionNew.add(movimentoVendaCollectionNewMovimentoVendaToAttach);
            }
            movimentoVendaCollectionNew = attachedMovimentoVendaCollectionNew;
            pessoaFisica.setMovimentoVendaCollection(movimentoVendaCollectionNew);
            pessoaFisica = em.merge(pessoaFisica);
            for (MovimentoVenda movimentoVendaCollectionOldMovimentoVenda : movimentoVendaCollectionOld) {
                if (!movimentoVendaCollectionNew.contains(movimentoVendaCollectionOldMovimentoVenda)) {
                    movimentoVendaCollectionOldMovimentoVenda.setIdComprador(null);
                    movimentoVendaCollectionOldMovimentoVenda = em.merge(movimentoVendaCollectionOldMovimentoVenda);
                }
            }
            for (MovimentoVenda movimentoVendaCollectionNewMovimentoVenda : movimentoVendaCollectionNew) {
                if (!movimentoVendaCollectionOld.contains(movimentoVendaCollectionNewMovimentoVenda)) {
                    PessoaFisica oldIdCompradorOfMovimentoVendaCollectionNewMovimentoVenda = movimentoVendaCollectionNewMovimentoVenda.getIdComprador();
                    movimentoVendaCollectionNewMovimentoVenda.setIdComprador(pessoaFisica);
                    movimentoVendaCollectionNewMovimentoVenda = em.merge(movimentoVendaCollectionNewMovimentoVenda);
                    if (oldIdCompradorOfMovimentoVendaCollectionNewMovimentoVenda != null && !oldIdCompradorOfMovimentoVendaCollectionNewMovimentoVenda.equals(pessoaFisica)) {
                        oldIdCompradorOfMovimentoVendaCollectionNewMovimentoVenda.getMovimentoVendaCollection().remove(movimentoVendaCollectionNewMovimentoVenda);
                        oldIdCompradorOfMovimentoVendaCollectionNewMovimentoVenda = em.merge(oldIdCompradorOfMovimentoVendaCollectionNewMovimentoVenda);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pessoaFisica.getIdPessoaFisica();
                if (findPessoaFisica(id) == null) {
                    throw new NonexistentEntityException("The pessoaFisica with id " + id + " no longer exists.");
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
            PessoaFisica pessoaFisica;
            try {
                pessoaFisica = em.getReference(PessoaFisica.class, id);
                pessoaFisica.getIdPessoaFisica();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pessoaFisica with id " + id + " no longer exists.", enfe);
            }
            Collection<MovimentoVenda> movimentoVendaCollection = pessoaFisica.getMovimentoVendaCollection();
            for (MovimentoVenda movimentoVendaCollectionMovimentoVenda : movimentoVendaCollection) {
                movimentoVendaCollectionMovimentoVenda.setIdComprador(null);
                movimentoVendaCollectionMovimentoVenda = em.merge(movimentoVendaCollectionMovimentoVenda);
            }
            em.remove(pessoaFisica);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<PessoaFisica> findPessoaFisicaEntities() {
        return findPessoaFisicaEntities(true, -1, -1);
    }

    public List<PessoaFisica> findPessoaFisicaEntities(int maxResults, int firstResult) {
        return findPessoaFisicaEntities(false, maxResults, firstResult);
    }

    private List<PessoaFisica> findPessoaFisicaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PessoaFisica.class));
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

    public PessoaFisica findPessoaFisica(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PessoaFisica.class, id);
        } finally {
            em.close();
        }
    }

    public int getPessoaFisicaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PessoaFisica> rt = cq.from(PessoaFisica.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
