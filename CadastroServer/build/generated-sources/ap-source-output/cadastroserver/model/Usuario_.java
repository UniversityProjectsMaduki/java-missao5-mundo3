package cadastroserver.model;

import cadastroserver.model.MovimentoCompra;
import cadastroserver.model.MovimentoVenda;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2023-11-24T03:32:21", comments="EclipseLink-2.7.12.v20230209-rNA")
@StaticMetamodel(Usuario.class)
public class Usuario_ { 

    public static volatile SingularAttribute<Usuario, Integer> idOperador;
    public static volatile SingularAttribute<Usuario, String> senha;
    public static volatile CollectionAttribute<Usuario, MovimentoVenda> movimentoVendaCollection;
    public static volatile SingularAttribute<Usuario, String> nome;
    public static volatile CollectionAttribute<Usuario, MovimentoCompra> movimentoCompraCollection;

}