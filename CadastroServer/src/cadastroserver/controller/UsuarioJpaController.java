package cadastroserver.controller;

import cadastroserver.controller.exceptions.NonexistentEntityException;
import cadastroserver.controller.exceptions.PreexistingEntityException;
import cadastroserver.model.Usuario;
import cadastroserver.model.MovimentoCompra;
import cadastroserver.model.MovimentoVenda;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class UsuarioJpaController implements Serializable {

    private EntityManagerFactory emf;

    public UsuarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) throws PreexistingEntityException, Exception {
        if (usuario.getMovimentoCompraCollection() == null) {
            usuario.setMovimentoCompraCollection(new ArrayList<>());
        }
        if (usuario.getMovimentoVendaCollection() == null) {
            usuario.setMovimentoVendaCollection(new ArrayList<>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();

            usuario = em.merge(usuario);

            for (MovimentoCompra movimentoCompra : usuario.getMovimentoCompraCollection()) {
                Usuario oldUsuarioOfMovimentoCompra = movimentoCompra.getUsuario();
                movimentoCompra.setUsuario(usuario);
                movimentoCompra = em.merge(movimentoCompra);
                if (oldUsuarioOfMovimentoCompra != null) {
                    oldUsuarioOfMovimentoCompra.getMovimentoCompraCollection().remove(movimentoCompra);
                    em.merge(oldUsuarioOfMovimentoCompra);
                }
            }

            for (MovimentoVenda movimentoVenda : usuario.getMovimentoVendaCollection()) {
                Usuario oldUsuarioOfMovimentoVenda = movimentoVenda.getUsuario();
                movimentoVenda.setUsuario(usuario);
                movimentoVenda = em.merge(movimentoVenda);
                if (oldUsuarioOfMovimentoVenda != null) {
                    oldUsuarioOfMovimentoVenda.getMovimentoVendaCollection().remove(movimentoVenda);
                    em.merge(oldUsuarioOfMovimentoVenda);
                }
            }

            em.persist(usuario);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUsuarioByLogin(usuario.getNome(), usuario.getSenha()) != null) {
                throw new PreexistingEntityException("Usuário " + usuario.getNome() + " já existe.", ex);
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

            Usuario persistentUsuario = em.find(Usuario.class, usuario.getUsuario());
            List<MovimentoVenda> movimentoVendaCollectionOld = new ArrayList<>(persistentUsuario.getMovimentoVendaCollection());
            List<MovimentoVenda> movimentoVendaCollectionNew = new ArrayList<>(usuario.getMovimentoVendaCollection());
            List<MovimentoCompra> movimentoCompraCollectionOld = new ArrayList<>(persistentUsuario.getMovimentoCompraCollection());
            List<MovimentoCompra> movimentoCompraCollectionNew = new ArrayList<>(usuario.getMovimentoCompraCollection());

            // Atualizar as referências para movimentos de venda
            for (MovimentoVenda movimentoVenda : movimentoVendaCollectionNew) {
                movimentoVenda.setUsuario(usuario);
                movimentoVenda = em.merge(movimentoVenda);
            }

            // Atualizar as referências para movimentos de compra
            for (MovimentoCompra movimentoCompra : movimentoCompraCollectionNew) {
                movimentoCompra.setUsuario(usuario);
                movimentoCompra = em.merge(movimentoCompra);
            }

            usuario = em.merge(usuario);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = usuario.getUsuario();
                if (findUsuarioById(id) == null) {
                    throw new NonexistentEntityException("O usuário com id " + id + " não existe mais.");
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
                usuario.getUsuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("O usuário com id " + id + " não existe mais.", enfe);
            }

            // Remover movimentos de venda e compra associados ao usuário
            for (MovimentoVenda movimentoVenda : usuario.getMovimentoVendaCollection()) {
                movimentoVenda.setUsuario(null);
                em.merge(movimentoVenda);
            }
            for (MovimentoCompra movimentoCompra : usuario.getMovimentoCompraCollection()) {
                movimentoCompra.setUsuario(null);
                em.merge(movimentoCompra);
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

    public Usuario findUsuarioById(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public Usuario findUsuario(String login, String senha) {
        EntityManager em = getEntityManager();
        try {
            // Substitua a consulta abaixo pela lógica apropriada
            TypedQuery<Usuario> query = em.createQuery("SELECT u FROM Usuario u WHERE u.login = :login AND u.senha = :senha", Usuario.class);
            query.setParameter("login", login);
            query.setParameter("senha", senha);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery<Long> cq = em.getCriteriaBuilder().createQuery(Long.class);
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    public Usuario findUsuarioByLogin(String login, String senha) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Usuario> query = em.createQuery(
                    "SELECT u FROM Usuario u WHERE u.login = :login AND u.senha = :senha",
                    Usuario.class
            );
            query.setParameter("login", login);
            query.setParameter("senha", senha);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

}
