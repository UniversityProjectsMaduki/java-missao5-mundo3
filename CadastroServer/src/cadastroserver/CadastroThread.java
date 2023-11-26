package cadastroserver;

import cadastroserver.controller.ProdutoJpaController;
import cadastroserver.controller.UsuarioJpaController;
import cadastroserver.model.Usuario;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class CadastroThread extends Thread {
    private ProdutoJpaController produtoController;
    private UsuarioJpaController usuarioController;
    private Socket clientSocket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    public CadastroThread(ProdutoJpaController produtoController, UsuarioJpaController usuarioController, Socket clientSocket) {
        this.produtoController = produtoController;
        this.usuarioController = usuarioController;
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            inputStream = new ObjectInputStream(clientSocket.getInputStream());

            // Obter o login e a senha a partir da entrada
            String login = (String) inputStream.readObject();
            String senha = (String) inputStream.readObject();

            // Utilizar usuarioController para verificar o login
            Usuario usuario = usuarioController.findUsuario(login, senha);

            // Se não encontrar o usuário, fecha a conexão
            if (usuario == null) {
                outputStream.writeObject("Credenciais inválidas");
                clientSocket.close();
                return;
            }

            // Com o usuário válido, iniciar o loop de resposta
            while (true) {
                String comando = (String) inputStream.readObject();

                // Se o comando for 'L', utilizar produtoController para retornar o conjunto de produtos
                if ("L".equals(comando)) {
                    outputStream.writeObject(produtoController.findProdutoEntities()); // Substitua pelo método correto
                } else {
                    outputStream.writeObject("Comando não reconhecido");
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao processar a solicitação: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) outputStream.close();
                if (inputStream != null) inputStream.close();
                if (clientSocket != null) clientSocket.close();
            } catch (IOException e) { // Apenas IOException deve ser capturada aqui.
                System.err.println("Erro ao fechar recursos: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
