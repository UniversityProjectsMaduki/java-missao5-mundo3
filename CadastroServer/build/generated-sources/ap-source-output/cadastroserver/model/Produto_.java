package cadastroserver.model;

import cadastroserver.model.MovimentoCompra;
import cadastroserver.model.MovimentoVenda;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2023-11-24T01:46:37", comments="EclipseLink-2.7.12.v20230209-rNA")
@StaticMetamodel(Produto.class)
public class Produto_ { 

    public static volatile SingularAttribute<Produto, Long> precoVenda;
    public static volatile SingularAttribute<Produto, Integer> idProduto;
    public static volatile CollectionAttribute<Produto, MovimentoVenda> movimentoVendaCollection;
    public static volatile SingularAttribute<Produto, String> nome;
    public static volatile CollectionAttribute<Produto, MovimentoCompra> movimentoCompraCollection;
    public static volatile SingularAttribute<Produto, Integer> quantidade;

}