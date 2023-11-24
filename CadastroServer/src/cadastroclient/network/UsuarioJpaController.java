package cadastroclient.network;

import cadastroclient.network.exceptions.NonexistentEntityException;
import cadastroclient.network.exceptions.PreexistingEntityException;
import cadastroserver.model.MovimentoCompra;
import cadastroserver.model.MovimentoVenda;
import cadastroserver.model.Usuario;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UsuarioJpaController implements Serializable {

    private EntityManagerFactory emf = null;

    public UsuarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public Usuario findUsuario(String login, String senha) {
    EntityManager em = getEntityManager();
    try {
        return em.createQuery(
                "SELECT u FROM Usuario u WHERE u.login = :login AND u.senha = :senha", Usuario.class)
                .setParameter("login", login)
                .setParameter("senha", senha)
                .getSingleResult();
    } catch (NoResultException e) {
        return null;
    } finally {
        em.close();
    }
}


    public void create(Usuario usuario) throws PreexistingEntityException, Exception {
        if (usuario.getMovimentoVendaCollection() == null) {
            usuario.setMovimentoVendaCollection(new ArrayList<>());
        }
        if (usuario.getMovimentoCompraCollection() == null) {
            usuario.setMovimentoCompraCollection(new ArrayList<>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<MovimentoVenda> attachedMovimentoVendaCollection = new ArrayList<>();
            for (MovimentoVenda movimentoVenda : usuario.getMovimentoVendaCollection()) {
                movimentoVenda = em.getReference(MovimentoVenda.class, movimentoVenda.getIdMovimentoVenda());
                attachedMovimentoVendaCollection.add(movimentoVenda);
            }
            usuario.setMovimentoVendaCollection(attachedMovimentoVendaCollection);
            Collection<MovimentoCompra> attachedMovimentoCompraCollection = new ArrayList<>();
            for (MovimentoCompra movimentoCompra : usuario.getMovimentoCompraCollection()) {
                movimentoCompra = em.getReference(MovimentoCompra.class, movimentoCompra.getIdMovimentoCompra());
                attachedMovimentoCompraCollection.add(movimentoCompra);
            }
            usuario.setMovimentoCompraCollection(attachedMovimentoCompraCollection);
            em.persist(usuario);
            for (MovimentoVenda movimentoVenda : usuario.getMovimentoVendaCollection()) {
                Usuario oldIdOperadorOfMovimentoVendaCollectionMovimentoVenda = movimentoVenda.getIdOperador();
                movimentoVenda.setIdOperador(usuario);
                movimentoVenda = em.merge(movimentoVenda);
                if (oldIdOperadorOfMovimentoVendaCollectionMovimentoVenda != null) {
                    oldIdOperadorOfMovimentoVendaCollectionMovimentoVenda.getMovimentoVendaCollection().remove(movimentoVenda);
                    oldIdOperadorOfMovimentoVendaCollectionMovimentoVenda = em.merge(oldIdOperadorOfMovimentoVendaCollectionMovimentoVenda);
                }
            }
            for (MovimentoCompra movimentoCompra : usuario.getMovimentoCompraCollection()) {
                Usuario oldIdOperadorOfMovimentoCompraCollectionMovimentoCompra = movimentoCompra.getIdOperador();
                movimentoCompra.setIdOperador(usuario);
                movimentoCompra = em.merge(movimentoCompra);
                if (oldIdOperadorOfMovimentoCompraCollectionMovimentoCompra != null) {
                    oldIdOperadorOfMovimentoCompraCollectionMovimentoCompra.getMovimentoCompraCollection().remove(movimentoCompra);
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
            Collection<MovimentoVenda> attachedMovimentoVendaCollectionNew = new ArrayList<>();
            for (MovimentoVenda movimentoVenda : movimentoVendaCollectionNew) {
                movimentoVenda = em.getReference(MovimentoVenda.class, movimentoVenda.getIdMovimentoVenda());
                attachedMovimentoVendaCollectionNew.add(movimentoVenda);
            }
            movimentoVendaCollectionNew = attachedMovimentoVendaCollectionNew;
            usuario.setMovimentoVendaCollection(movimentoVendaCollectionNew);
            Collection<MovimentoCompra> attachedMovimentoCompraCollectionNew = new ArrayList<>();
            for (MovimentoCompra movimentoCompra : movimentoCompraCollectionNew) {
                movimentoCompra = em.getReference(MovimentoCompra.class, movimentoCompra.getIdMovimentoCompra());
                attachedMovimentoCompraCollectionNew.add(movimentoCompra);
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
            for (MovimentoVenda movimentoVenda : movimentoVendaCollection) {
                movimentoVenda.setIdOperador(null);
                movimentoVenda = em.merge(movimentoVenda);
            }
            Collection<MovimentoCompra> movimentoCompraCollection = usuario.getMovimentoCompraCollection();
            for (MovimentoCompra movimentoCompra : movimentoCompraCollection) {
                movimentoCompra.setIdOperador(null);
                movimentoCompra = em.merge(movimentoCompra);
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
