/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cadastroserver.controller;

import cadastroserver.controller.exceptions.NonexistentEntityException;
import cadastroserver.controller.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import cadastroserver.model.MovimentoVenda;
import java.util.ArrayList;
import java.util.Collection;
import cadastroserver.model.MovimentoCompra;
import cadastroserver.model.Usuario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Madu
 */
public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) throws PreexistingEntityException, Exception {
        if (usuario.getMovimentoVendaCollection() == null) {
            usuario.setMovimentoVendaCollection(new ArrayList<MovimentoVenda>());
        }
        if (usuario.getMovimentoCompraCollection() == null) {
            usuario.setMovimentoCompraCollection(new ArrayList<MovimentoCompra>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<MovimentoVenda> attachedMovimentoVendaCollection = new ArrayList<MovimentoVenda>();
            for (MovimentoVenda movimentoVendaCollectionMovimentoVendaToAttach : usuario.getMovimentoVendaCollection()) {
                movimentoVendaCollectionMovimentoVendaToAttach = em.getReference(movimentoVendaCollectionMovimentoVendaToAttach.getClass(), movimentoVendaCollectionMovimentoVendaToAttach.getIdMovimentoVenda());
                attachedMovimentoVendaCollection.add(movimentoVendaCollectionMovimentoVendaToAttach);
            }
            usuario.setMovimentoVendaCollection(attachedMovimentoVendaCollection);
            Collection<MovimentoCompra> attachedMovimentoCompraCollection = new ArrayList<MovimentoCompra>();
            for (MovimentoCompra movimentoCompraCollectionMovimentoCompraToAttach : usuario.getMovimentoCompraCollection()) {
                movimentoCompraCollectionMovimentoCompraToAttach = em.getReference(movimentoCompraCollectionMovimentoCompraToAttach.getClass(), movimentoCompraCollectionMovimentoCompraToAttach.getIdMovimentoCompra());
                attachedMovimentoCompraCollection.add(movimentoCompraCollectionMovimentoCompraToAttach);
            }
            usuario.setMovimentoCompraCollection(attachedMovimentoCompraCollection);
            em.persist(usuario);
            for (MovimentoVenda movimentoVendaCollectionMovimentoVenda : usuario.getMovimentoVendaCollection()) {
                Usuario oldIdOperadorOfMovimentoVendaCollectionMovimentoVenda = movimentoVendaCollectionMovimentoVenda.getIdOperador();
                movimentoVendaCollectionMovimentoVenda.setIdOperador(usuario);
                movimentoVendaCollectionMovimentoVenda = em.merge(movimentoVendaCollectionMovimentoVenda);
                if (oldIdOperadorOfMovimentoVendaCollectionMovimentoVenda != null) {
                    oldIdOperadorOfMovimentoVendaCollectionMovimentoVenda.getMovimentoVendaCollection().remove(movimentoVendaCollectionMovimentoVenda);
                    oldIdOperadorOfMovimentoVendaCollectionMovimentoVenda = em.merge(oldIdOperadorOfMovimentoVendaCollectionMovimentoVenda);
                }
            }
            for (MovimentoCompra movimentoCompraCollectionMovimentoCompra : usuario.getMovimentoCompraCollection()) {
                Usuario oldIdOperadorOfMovimentoCompraCollectionMovimentoCompra = movimentoCompraCollectionMovimentoCompra.getIdOperador();
                movimentoCompraCollectionMovimentoCompra.setIdOperador(usuario);
                movimentoCompraCollectionMovimentoCompra = em.merge(movimentoCompraCollectionMovimentoCompra);
                if (oldIdOperadorOfMovimentoCompraCollectionMovimentoCompra != null) {
                    oldIdOperadorOfMovimentoCompraCollectionMovimentoCompra.getMovimentoCompraCollection().remove(movimentoCompraCollectionMovimentoCompra);
                    oldIdOperadorOfMovimentoCompraCollectionMovimentoCompra = em.merge(oldIdOperadorOfMovimentoCompraCollectionMovimentoCompra);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUsuario(usuario.getIdOperador()) != null) {
                throw new PreexistingEntityException("Usuario " + usuario + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuario usuario) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getIdOperador());
            Collection<MovimentoVenda> movimentoVendaCollectionOld = persistentUsuario.getMovimentoVendaCollection();
            Collection<MovimentoVenda> movimentoVendaCollectionNew = usuario.getMovimentoVendaCollection();
            Collection<MovimentoCompra> movimentoCompraCollectionOld = persistentUsuario.getMovimentoCompraCollection();
            Collection<MovimentoCompra> movimentoCompraCollectionNew = usuario.getMovimentoCompraCollection();
            Collection<MovimentoVenda> attachedMovimentoVendaCollectionNew = new ArrayList<MovimentoVenda>();
            for (MovimentoVenda movimentoVendaCollectionNewMovimentoVendaToAttach : movimentoVendaCollectionNew) {
                movimentoVendaCollectionNewMovimentoVendaToAttach = em.getReference(movimentoVendaCollectionNewMovimentoVendaToAttach.getClass(), movimentoVendaCollectionNewMovimentoVendaToAttach.getIdMovimentoVenda());
                attachedMovimentoVendaCollectionNew.add(movimentoVendaCollectionNewMovimentoVendaToAttach);
            }
            movimentoVendaCollectionNew = attachedMovimentoVendaCollectionNew;
            usuario.setMovimentoVendaCollection(movimentoVendaCollectionNew);
            Collection<MovimentoCompra> attachedMovimentoCompraCollectionNew = new ArrayList<MovimentoCompra>();
            for (MovimentoCompra movimentoCompraCollectionNewMovimentoCompraToAttach : movimentoCompraCollectionNew) {
                movimentoCompraCollectionNewMovimentoCompraToAttach = em.getReference(movimentoCompraCollectionNewMovimentoCompraToAttach.getClass(), movimentoCompraCollectionNewMovimentoCompraToAttach.getIdMovimentoCompra());
                attachedMovimentoCompraCollectionNew.add(movimentoCompraCollectionNewMovimentoCompraToAttach);
            }
            movimentoCompraCollectionNew = attachedMovimentoCompraCollectionNew;
            usuario.setMovimentoCompraCollection(movimentoCompraCollectionNew);
            usuario = em.merge(usuario);
            for (MovimentoVenda movimentoVendaCollectionOldMovimentoVenda : movimentoVendaCollectionOld) {
                if (!movimentoVendaCollectionNew.contains(movimentoVendaCollectionOldMovimentoVenda)) {
                    movimentoVendaCollectionOldMovimentoVenda.setIdOperador(null);
                    movimentoVendaCollectionOldMovimentoVenda = em.merge(movimentoVendaCollectionOldMovimentoVenda);
                }
            }
            for (MovimentoVenda movimentoVendaCollectionNewMovimentoVenda : movimentoVendaCollectionNew) {
                if (!movimentoVendaCollectionOld.contains(movimentoVendaCollectionNewMovimentoVenda)) {
                    Usuario oldIdOperadorOfMovimentoVendaCollectionNewMovimentoVenda = movimentoVendaCollectionNewMovimentoVenda.getIdOperador();
                    movimentoVendaCollectionNewMovimentoVenda.setIdOperador(usuario);
                    movimentoVendaCollectionNewMovimentoVenda = em.merge(movimentoVendaCollectionNewMovimentoVenda);
                    if (oldIdOperadorOfMovimentoVendaCollectionNewMovimentoVenda != null && !oldIdOperadorOfMovimentoVendaCollectionNewMovimentoVenda.equals(usuario)) {
                        oldIdOperadorOfMovimentoVendaCollectionNewMovimentoVenda.getMovimentoVendaCollection().remove(movimentoVendaCollectionNewMovimentoVenda);
                        oldIdOperadorOfMovimentoVendaCollectionNewMovimentoVenda = em.merge(oldIdOperadorOfMovimentoVendaCollectionNewMovimentoVenda);
                    }
                }
            }
            for (MovimentoCompra movimentoCompraCollectionOldMovimentoCompra : movimentoCompraCollectionOld) {
                if (!movimentoCompraCollectionNew.contains(movimentoCompraCollectionOldMovimentoCompra)) {
                    movimentoCompraCollectionOldMovimentoCompra.setIdOperador(null);
                    movimentoCompraCollectionOldMovimentoCompra = em.merge(movimentoCompraCollectionOldMovimentoCompra);
                }
            }
            for (MovimentoCompra movimentoCompraCollectionNewMovimentoCompra : movimentoCompraCollectionNew) {
                if (!movimentoCompraCollectionOld.contains(movimentoCompraCollectionNewMovimentoCompra)) {
                    Usuario oldIdOperadorOfMovimentoCompraCollectionNewMovimentoCompra = movimentoCompraCollectionNewMovimentoCompra.getIdOperador();
                    movimentoCompraCollectionNewMovimentoCompra.setIdOperador(usuario);
                    movimentoCompraCollectionNewMovimentoCompra = em.merge(movimentoCompraCollectionNewMovimentoCompra);
                    if (oldIdOperadorOfMovimentoCompraCollectionNewMovimentoCompra != null && !oldIdOperadorOfMovimentoCompraCollectionNewMovimentoCompra.equals(usuario)) {
                        oldIdOperadorOfMovimentoCompraCollectionNewMovimentoCompra.getMovimentoCompraCollection().remove(movimentoCompraCollectionNewMovimentoCompra);
                        oldIdOperadorOfMovimentoCompraCollectionNewMovimentoCompra = em.merge(oldIdOperadorOfMovimentoCompraCollectionNewMovimentoCompra);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = usuario.getIdOperador();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
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
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getIdOperador();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            Collection<MovimentoVenda> movimentoVendaCollection = usuario.getMovimentoVendaCollection();
            for (MovimentoVenda movimentoVendaCollectionMovimentoVenda : movimentoVendaCollection) {
                movimentoVendaCollectionMovimentoVenda.setIdOperador(null);
                movimentoVendaCollectionMovimentoVenda = em.merge(movimentoVendaCollectionMovimentoVenda);
            }
            Collection<MovimentoCompra> movimentoCompraCollection = usuario.getMovimentoCompraCollection();
            for (MovimentoCompra movimentoCompraCollectionMovimentoCompra : movimentoCompraCollection) {
                movimentoCompraCollectionMovimentoCompra.setIdOperador(null);
                movimentoCompraCollectionMovimentoCompra = em.merge(movimentoCompraCollectionMovimentoCompra);
            }
            em.remove(usuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
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

    public Usuario findUsuario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
