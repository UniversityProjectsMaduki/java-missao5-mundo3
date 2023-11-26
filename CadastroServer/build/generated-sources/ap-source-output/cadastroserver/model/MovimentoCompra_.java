package cadastroserver.model;

import cadastroserver.model.PessoaJuridica;
import cadastroserver.model.Produto;
import cadastroserver.model.Usuario;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2023-11-24T03:32:21", comments="EclipseLink-2.7.12.v20230209-rNA")
@StaticMetamodel(MovimentoCompra.class)
public class MovimentoCompra_ { 

    public static volatile SingularAttribute<MovimentoCompra, Long> precoUnitario;
    public static volatile SingularAttribute<MovimentoCompra, Usuario> idOperador;
    public static volatile SingularAttribute<MovimentoCompra, PessoaJuridica> idFornecedor;
    public static volatile SingularAttribute<MovimentoCompra, Produto> idProduto;
    public static volatile SingularAttribute<MovimentoCompra, Integer> idMovimentoCompra;
    public static volatile SingularAttribute<MovimentoCompra, Integer> quantidade;

}