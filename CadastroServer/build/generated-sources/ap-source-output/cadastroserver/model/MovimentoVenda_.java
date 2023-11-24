package cadastroserver.model;

import cadastroserver.model.PessoaFisica;
import cadastroserver.model.Produto;
import cadastroserver.model.Usuario;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2023-11-24T01:46:37", comments="EclipseLink-2.7.12.v20230209-rNA")
@StaticMetamodel(MovimentoVenda.class)
public class MovimentoVenda_ { 

    public static volatile SingularAttribute<MovimentoVenda, Long> precoUnitario;
    public static volatile SingularAttribute<MovimentoVenda, Usuario> idOperador;
    public static volatile SingularAttribute<MovimentoVenda, Produto> idProduto;
    public static volatile SingularAttribute<MovimentoVenda, Integer> idMovimentoVenda;
    public static volatile SingularAttribute<MovimentoVenda, Integer> quantidade;
    public static volatile SingularAttribute<MovimentoVenda, PessoaFisica> idComprador;

}