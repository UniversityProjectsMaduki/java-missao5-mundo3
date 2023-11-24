package cadastroserver.model;

import cadastroserver.model.MovimentoVenda;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2023-11-24T01:46:37", comments="EclipseLink-2.7.12.v20230209-rNA")
@StaticMetamodel(PessoaFisica.class)
public class PessoaFisica_ { 

    public static volatile CollectionAttribute<PessoaFisica, MovimentoVenda> movimentoVendaCollection;
    public static volatile SingularAttribute<PessoaFisica, Integer> idPessoaFisica;
    public static volatile SingularAttribute<PessoaFisica, String> cpf;

}