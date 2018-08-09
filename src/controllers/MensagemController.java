/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import com.sun.glass.events.WindowEvent;
import conexaodb.ConexaoDb;
import conexaoserver.EmissorDeMensagem;
import conexaoserver.ReceptorDeMensagem;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javax.swing.JFileChooser;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkEvent.EventType;
import main.Chat;
import modelos.ListViewMensagem;
import modelos.ListViewUsuario;
import modelos.Mensagem;
import modelos.Usuario;

/**
 * FXML Controller class
 *
 * @author arcn
 */
public class MensagemController implements Initializable, ControladorTelas {
    
    private final String host = "192.168.1.60";
    
    private Thread pilha;
    
    public ControleTelas controleTela;
    
    public Usuario usrPrincipal;
    public Usuario usrChat;
    
    // Setando a lista de mensagens e de contatos
    private ObservableList<Usuario> listaContatos;
    private ObservableList<Mensagem> listaMensagens;
    private ObservableList<Mensagem> listaMensagemUsuario;
    
    // Setando a relação dos contatos com as mensagens
    private HashMap<String, ObservableList<Mensagem>> tabelaMensagens;
    private HashMap<String, ObservableList<Usuario>> tabelaUsuariosGrupo;
                  //nome      //usuarios no grupo
                  //grupo
    
    // Setando atributos para conversa com servidor
    private Socket socket;
    private ObjectOutputStream saida;
    private ObjectInputStream entrada; 
    private EmissorDeMensagem emissor;
    private ReceptorDeMensagem receptorMensagem;
    
    
    // Stage para addContato:
    public static Stage telaAddContato;
    public static Stage telaAddGrupo;
    
    public static boolean isRunning;
    
    // Atributos para link com GUI
    @FXML
    private AnchorPane painelRootMensagens;
    
    @FXML
    private MaterialDesignIconView btnLogout;
    
    @FXML
    private JFXButton btnAddContato;
    
    @FXML
    private Pane painelMensagem;

    @FXML
    private Label lblNomeUsr;

    @FXML
    private JFXListView<Usuario> listViewUsuarios;
    
    @FXML
    private JFXListView<Mensagem> listViewMensagens;

    @FXML
    private JFXToggleButton toglleOnOff;

    @FXML
    private JFXButton btnEnviarMensagem;

    @FXML
    private JFXTextField txtMensagem;
    
    @FXML
    private FontAwesomeIconView iconEnviarMensagem;
    
    @FXML
    private ImageView imgUsuario;
    
    @FXML
    private JFXButton btnNovoGrupo;
    
     @FXML
    private FontAwesomeIconView btnEnviaImg;

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        isRunning = true;
        
        // Setando Usuario do Chat
        usrPrincipal = Chat.usrPrincipal;
        
        System.out.println(usrPrincipal.getNome()); 
        lblNomeUsr.setText(usrPrincipal.getNome()); // Seta o nome do usuario no label
        
        // Inicializando a tabela de mensagens
        tabelaMensagens = new HashMap<>();
        tabelaUsuariosGrupo = new HashMap<>();
        
        // Pegando os contatos 
        ConexaoDb conexaoDb = new ConexaoDb(); 
        ArrayList<Usuario> contatos = new ArrayList<>();
        contatos = conexaoDb.pegaTodosContatos(usrPrincipal);
        
        // Lista de teste
        listaContatos = FXCollections.observableArrayList();
         if (contatos.size() > 1) { 
            for (int i=1; i < contatos.size(); i++) { 
                listaContatos.add(contatos.get(i));
            }
         } else {
            for (Usuario usr: contatos) { 
                listaContatos.add(usr);
            }
         }
        
        // Inicia a conversa com o usuario da posicao 0 da lista 
        usrChat = listaContatos.get(0);
        // Instanciacao de todas as mensagens de usuarios no mapa
        for (Usuario u: listaContatos) { 
            tabelaMensagens.put(u.getEmail(), FXCollections.observableArrayList());
        }
        
        listViewUsuarios.setItems(listaContatos);
        listViewMensagens.setItems(tabelaMensagens.get(usrChat.getEmail()));
        
        listViewUsuarios.setCellFactory(e -> {
            return new ListViewUsuario();
        });
        listViewMensagens.setCellFactory(e -> {
            return new ListViewMensagem();
        });
        
        // Inicia conexao com o servidor e instancia a thread do receptor
        iniciaConexaoServidor();
        
        // Envia um sinal para o servidor de que o usuario está online
        enviarMensagemOnline();
       
    }
    

    @FXML
    void enviarMensagem(javafx.scene.input.MouseEvent event) throws IOException {
        if (!usrChat.getEmail().equals("grupo")) { 
            System.out.println("Clicou!");
            Mensagem msg = new Mensagem(usrPrincipal, usrChat, txtMensagem.getText());
            msg.setProtocolo("mensagem"); // Cria uma nova mensagem para o usuario
            emissor.enviaMensagem(msg);  // Envia a mensagem para o servidor
            txtMensagem.setText("");     // Limpa o txtField da mensagem

            tabelaMensagens.get(usrChat.getEmail()).add(msg); // Adiciona a mensagem no hashMap         
        } else { 
            Mensagem msg = null;
            for (Usuario usuario : tabelaUsuariosGrupo.get(usrChat.getNome())) {
                msg = new Mensagem(usrPrincipal, usuario, txtMensagem.getText()); // Cria uma nova mensagem para o usuario
                emissor.enviaMensagem(msg);  // Envia a mensagem para o servidor
            }
            tabelaMensagens.get(usrChat.getNome()).add(msg);
            txtMensagem.setText(""); 
        }
    }
    
    @FXML
    void enviaMensagemClicado(javafx.scene.input.KeyEvent event) throws IOException {
        if (event.getCode().equals(KeyCode.ENTER)) {
            if (!usrChat.getEmail().equals("grupo")) { 
                System.out.println("Clicou!");
                Mensagem msg = new Mensagem(usrPrincipal, usrChat, txtMensagem.getText());
                msg.setProtocolo("mensagem"); // Cria uma nova mensagem para o usuario
                emissor.enviaMensagem(msg);  // Envia a mensagem para o servidor
                txtMensagem.setText("");     // Limpa o txtField da mensagem

                tabelaMensagens.get(usrChat.getEmail()).add(msg); // Adiciona a mensagem no hashMap         
            } else { 
                Mensagem msg = null;
                for (Usuario usuario : tabelaUsuariosGrupo.get(usrChat.getNome())) {
                    msg = new Mensagem(usrPrincipal, usuario, txtMensagem.getText()); // Cria uma nova mensagem para o usuario
                    emissor.enviaMensagem(msg);  // Envia a mensagem para o servidor
                }
                tabelaMensagens.get(usrChat.getNome()).add(msg);
                txtMensagem.setText(""); 
            }
        }
    }
    
    @FXML
    void btnAddContatoClicado(javafx.scene.input.MouseEvent event) throws IOException { 
        enviaSolicitacao();
    }
    
    @FXML
    void listViewUserClicada(MouseEvent event) {
        
        if (listViewUsuarios.getSelectionModel().getSelectedItem() != null && !(listViewUsuarios.getSelectionModel().getSelectedItem().getEmail().equals("grupo"))) { 
            System.out.println("Clicou");
            if (!usrChat.equals(listViewUsuarios.getSelectionModel().getSelectedItem())){ //limpa o chat

                // Altera o usuario do chat
                usrChat = listViewUsuarios.getSelectionModel().getSelectedItem();

                // Pega outra lista de mensagem:
                listViewMensagens.setItems(tabelaMensagens.get(usrChat.getEmail()));
            }
        } else  { 
                 // Altera o usuario do chat
                usrChat = listViewUsuarios.getSelectionModel().getSelectedItem();

                // Pega outra lista de mensagem:
                listViewMensagens.setItems(tabelaMensagens.get(usrChat.getNome()));
        }
       
    }

    @Override
    public void setScreenParent(ControleTelas screenPage) {
        controleTela = screenPage;
    }
    
    // Metodo de iniciar conexao com o servidor: 
    public void iniciaConexaoServidor() { 
         try {
             
            socket = new Socket(host, 5051);
            
            if (socket != null) { 
                saida = new ObjectOutputStream(socket.getOutputStream());
                entrada = new ObjectInputStream(socket.getInputStream());

                emissor = new EmissorDeMensagem(saida); 
                
                // Thread de recebimento de mensagem
                receptorMensagem = new ReceptorDeMensagem(entrada) {
                    @Override
                    public void run() {
                        long inicio = System.currentTimeMillis();
                        while (isRunning) {
                            if (this.getEntrada() != null) { 
                                try {
                                    Mensagem mensagem = (Mensagem) this.getEntrada().readObject();
                                    System.out.println("Recebendo mensagem de "+mensagem.getUsrEnvia().getEmail()+", mensagem: "+mensagem.getConteudo());
                                    trataRecebimentoMensagem(mensagem);
                                } catch (IOException ex) {
                                    Logger.getLogger(MensagemController.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (ClassNotFoundException ex) {
                                    Logger.getLogger(MensagemController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                           
                        }
                    }
                };
                
                pilha = new Thread(receptorMensagem);
                pilha.start();
            } else  { 
                System.out.println("Servidor Offline");
            } 
        } catch (IOException ex) {
            System.out.println("Servidor Offline");
            System.out.println(ex);
        }
    }
    
    // Modificar o método para que envie um sinal de que um usuário está online:
    public void enviarMensagemOnline() {
        Usuario usrTeste = new Usuario();
        usrTeste.setEmail("conexao@gmail.com");
        usrTeste.setNome("conexao");
        usrTeste.setImagem("avatar");
        
        Mensagem m = new Mensagem(usrPrincipal, usrTeste, "conexao");
        m.setProtocolo("conexao");
        try {
            emissor.enviaMensagem(m);
        } catch (IOException ex) {
            Logger.getLogger(MensagemController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // Trata Recebimento Mensagem
    public void trataRecebimentoMensagem(Mensagem mensagem) { 
        if (!(mensagem.getUsrEnvia().getEmail().equals(usrPrincipal.getEmail()))) {
            if (mensagem.getProtocolo().equals("solicitacao") && !listaContatos.contains(mensagem.getUsrEnvia())) {
                // Recebimento de solicitacao de amizade
                Platform.runLater(() -> {
                    try {
                        recebeSolicitacao(mensagem); 
                    } catch (IOException ex) {
                        Logger.getLogger(MensagemController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
            } else if ((mensagem.getProtocolo().equals("solicitacaoOK"))) {// Metodo para atualizar a GUI com as mensagens recebidas do servidor
                // Solicitacao confirmada, adiciona o usuário na lista de amizades
                Platform.runLater(() -> {
                    if (listaContatos.get(0).getNome().equals("Adicione Novos Amigos")) { 
                        listaContatos.remove(0);
                    }
                    listaContatos.add(mensagem.getUsrEnvia());
                    tabelaMensagens.put(mensagem.getUsrEnvia().getEmail(), FXCollections.observableArrayList());
                    addAmigoBD(mensagem.getUsrEnvia());
                });
            } else if ((mensagem.getProtocolo().equals("arquivo"))) {
                // Codigo para a criacao do grupo do lado de quem recebe a mensagem
                Platform.runLater(() -> {
                    System.out.println("Você recebeu um arquivo!");
                    
                    // Abre um directoryChooser para escolher o local onde o arquivo será salvo
                    DirectoryChooser dc = new DirectoryChooser(); 
                    dc.setTitle("Escolha um diretório para salvar o arquivo");
                    File caminho =  dc.showDialog(painelRootMensagens.getScene().getWindow());
                    File arquivoSalvar = null;
                    // Pega o nome do sistema operacional
                    String os = System.getProperty("os.name");
                    if (!os.equals("Linux")) { 
                        arquivoSalvar = new File(caminho + "\\" + mensagem.getNomeArquivo());
                    } else { 
                        arquivoSalvar = new File(caminho + "/" + mensagem.getNomeArquivo());
                    }
                    try {
                        //System.out.println(os);
                        converteByteArrayToFile(mensagem.getFile(), arquivoSalvar);
                    } catch (IOException ex) {
                        Logger.getLogger(MensagemController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    System.out.println("Caminho dir: " +caminho.toString());
                    //System.out.println("Nome arquivo: " +mensagem.getFile().getName());
                    mensagem.setConteudo("Arquivo "+mensagem.getNomeArquivo()+" salvo em: "+caminho.toString());
                    tabelaMensagens.get(mensagem.getUsrEnvia().getEmail()).add(mensagem);
                   
                });
            } else if ((mensagem.getProtocolo().equals("grupocriado")) && ! listaContatos.contains(mensagem.getUsrEnvia())) {
                // Codigo para a criacao do grupo do lado de quem recebe a mensagem
                Platform.runLater(() -> {
                    // OK 
                    listaContatos.add(mensagem.getUsrEnvia()); //usrEnvia == GRUPO
                    // OK
                    tabelaMensagens.put(mensagem.getUsrEnvia().getNome(), FXCollections.observableArrayList());
                    
                    ObservableList<Usuario> lista = FXCollections.observableArrayList();
                    for (Usuario u: mensagem.getGrupo()) { 
                        lista.add(u);
                    }
                    
                    tabelaUsuariosGrupo.put(mensagem.getUsrEnvia().getNome(), lista);
                    //addGrupoAmigoBD(mensagem.getUsrEnvia());
                });
            } else if ((mensagem.getProtocolo().equals("solicitacaoNOK"))) { 
                // Metodo para atualizar a GUI com as mensagens recebidas do servidor
               // Platform.runLater(() -> {
                    //tabelaMensagens.get(mensagem.getUsrEnvia().getEmail()).add(mensagem);
                //});
            } else if ((mensagem.getProtocolo().equals("mensagem"))) { 
                // Metodo para atualizar a GUI com as mensagens recebidas do servidor
                Platform.runLater(() -> {
                    tabelaMensagens.get(mensagem.getUsrEnvia().getEmail()).add(mensagem);
                });
            } else if ((mensagem.getProtocolo().equals("conexao"))) { 
                // Metodo para atualizar a GUI com as mensagens recebidas do servidor
                Platform.runLater(() -> {
                    if (listaContatos.contains(mensagem.getUsrEnvia())) { 
                        listaContatos.get(listaContatos.indexOf(mensagem.getUsrEnvia())).setNome(mensagem.getUsrEnvia() + "(online)");
                        listViewUsuarios.refresh();
                    }
                    tabelaMensagens.get(mensagem.getUsrEnvia().getEmail()).add(mensagem);
                });
            } 
        }
    }
    
    // Metodo que envia uma solicitação de amizade
    public void enviaSolicitacao() throws IOException { 
        // Carregando a tela para adicionar amigos: 
        telaAddContato = new Stage();
        FXMLLoader carregador = new FXMLLoader(getClass().getResource("/views/AdicionarContato.fxml"));
        Parent carregaFXML = (Parent) carregador.load();
        AdicionarContatoController meuControlador = carregador.getController();
        Scene cena = new Scene(carregaFXML);
        telaAddContato.initStyle(StageStyle.UNDECORATED);
        telaAddContato.centerOnScreen();
        telaAddContato.setScene(cena);
        telaAddContato.show();
        
        // Setando evento do Envia Solicitação: 
        meuControlador.btnEnviaSolicitacao.setOnMouseClicked(e -> { 
            try {
                String email = meuControlador.txtEmailAmigo.getText();
                Usuario solicita = new Usuario();
                solicita.setEmail(email);
                solicita.setNome("Solicitado");
                solicita.setImagem("avatar");
                Mensagem mgs = new Mensagem(usrPrincipal, solicita, "solicitacao amizade");
                mgs.setProtocolo("solicitacao");
                emissor.enviaMensagem(mgs);
            } catch (IOException ex) {
                Logger.getLogger(MensagemController.class.getName()).log(Level.SEVERE, null, ex);
            } finally { 
                telaAddContato.close();
            }
        });
    }
    
    public void addAmigoBD(Usuario usrAdd){ 
        conexaodb.ConexaoDb conexaoDb = new ConexaoDb();
        conexaoDb.inserirContato(usrPrincipal, usrAdd);
    }
    
    // Metodo que trata o recebimento de uma solicitacao de amizade
    public void recebeSolicitacao(Mensagem m) throws IOException { 
        // Carregando a tela para adicionar amigos: 
        telaAddContato = new Stage();
        FXMLLoader carregador = new FXMLLoader(getClass().getResource("/views/SolicitacaoMensagem.fxml"));
        Parent carregaFXML = (Parent) carregador.load();
        SolicitacaoMensagemController meuControlador = carregador.getController();
        Scene cena = new Scene(carregaFXML);
        telaAddContato.initStyle(StageStyle.UNDECORATED);
        telaAddContato.centerOnScreen();
        telaAddContato.setScene(cena);
        telaAddContato.show();
        
        // Setando as informaçoes da solicitacao: 
        meuControlador.lblNomeUsuario.setText(m.getUsrEnvia().getNome());
        meuControlador.lblEmailUsuario.setText(m.getUsrEnvia().getEmail());
        
        meuControlador.btnAceitar.setOnMouseClicked( e ->  { 
            Platform.runLater( () -> {
                listaContatos.add(m.getUsrEnvia());
                tabelaMensagens.put(m.getUsrEnvia().getEmail(), FXCollections.observableArrayList());
                addAmigoBD(m.getUsrEnvia());
            });
            Mensagem msg = new Mensagem(usrPrincipal, m.getUsrEnvia(), "solicitacao aceita");
            msg.setProtocolo("solicitacaoOK");
            telaAddContato.close();
            try {
                emissor.enviaMensagem(msg);
            } catch (IOException ex) {
                System.out.println(ex);
            }
        });
        
        meuControlador.btnRecusar.setOnMouseClicked( e ->  {
            Mensagem msg = new Mensagem(usrPrincipal, m.getUsrEnvia(), "solicitacao nao aceita");
            msg.setProtocolo("solicitacaoNOK");
            try {
                emissor.enviaMensagem(msg);
            } catch (IOException ex) {
                System.out.println(ex);
            }
            telaAddContato.close();
        });
        
        
    }
    
    
    //Distribuir a lista de emails junto com o id do grupo criado 
    //Se os usuarios do grupos nao tiverem todos do grupo, colocar na lista de amizades
    // 
    
    @FXML
    void btnNovoGrupoClicado(javafx.scene.input.MouseEvent event) throws IOException {
        if (listaContatos.size() > 2) { 
            
            // Cria a tela para gerar o grupo
            telaAddGrupo = new Stage(); 
            // Carregando a tela para adicionar amigos:
            FXMLLoader carregador = new FXMLLoader(getClass().getResource("/views/AdicionarGrupo.fxml"));
            Parent carregaFXML = (Parent) carregador.load();
            AdicionarGrupoController meuControlador = carregador.getController();
            Scene cena = new Scene(carregaFXML);
            telaAddGrupo.initStyle(StageStyle.UNDECORATED);
            telaAddGrupo.centerOnScreen();
            telaAddGrupo.setScene(cena);
            telaAddGrupo.show();
            
            // Trata os eventos da tela
            
            for (Usuario u : listaContatos) { 
                if (!u.getEmail().equals("grupo")) { 
                     meuControlador.listViewGrupo.getItems().add(u);
                }
            }
            
            meuControlador.btnCriaGrupo.setOnMouseClicked(e -> {
                
                ArrayList<Usuario> listaGrupo = new ArrayList<>();
                String nomeGrupo = meuControlador.txtNomeDoGrupo.getText(); 
                System.out.println(nomeGrupo);
                Usuario grupo = new Usuario();
                grupo.setEmail("grupo");
                grupo.setNome(nomeGrupo);
                grupo.setImagem("avatar");
                
                listaGrupo.add(usrPrincipal);
                
                // DEfine a hash da tabela de usuarios de um grupo
                //espelhada na tabela de mensagens: 
                tabelaUsuariosGrupo.put(nomeGrupo, FXCollections.observableArrayList());
                
                for (Usuario u : meuControlador.listViewGrupo.getSelectionModel().getSelectedItems()) { 
                    listaGrupo.add(u);
                }
                System.out.println(listaGrupo.toString());
                
                Mensagem msg = new Mensagem(grupo, usrPrincipal, "nvgrupocriado");
                msg.setGrupo(listaGrupo);
                
                for (Usuario usr: listaGrupo) {
                    msg.setUsrRecebe(usr);
                    System.out.println(usr.getNome());
                    tabelaUsuariosGrupo.get(grupo.getNome()).add(usr);
                    try {
                        emissor.enviaMensagem(msg);
                    } catch (IOException ex) {
                        Logger.getLogger(MensagemController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
                listaContatos.add(grupo);
                tabelaMensagens.put(nomeGrupo, FXCollections.observableArrayList());
                
                meuControlador.btnCloseClicado(event);
                
            });
            
            
        }
    }
    
    // Atualizar imagem do usuario
    
    
    @FXML
    void btnEnviaImgClicado(javafx.scene.input.MouseEvent event) throws IOException {
        //System.out.println("Clicou");
        final FileChooser chooser = new FileChooser();
        chooser.setTitle("Escolha o Arquivo");
        File caminho = chooser.showOpenDialog(painelRootMensagens.getScene().getWindow());
        File arquivo = new File(caminho.getAbsolutePath());
        String nomeArquivo = arquivo.getName();
        System.out.println("Nome do arquivo: " + nomeArquivo);
        String extensao = nomeArquivo.substring(nomeArquivo.lastIndexOf("."), nomeArquivo.length());
        System.out.println("Extensao " +extensao);
        Mensagem msg = new Mensagem(usrPrincipal, usrChat, "enviando arquivo");
        msg.setProtocolo("arquivo");
        byte[] arq = getBytes(arquivo);
        msg.setNomeArquivo(nomeArquivo);
        msg.setFile(arq, arq.length);
        Mensagem msgArquivoEnv = new  Mensagem(usrPrincipal, usrChat, "Enviando "+msg.getNomeArquivo());
        tabelaMensagens.get(usrChat.getEmail()).add(msgArquivoEnv);
        emissor.enviaMensagem(msg);
    }
    
    //transforma arquivos em bytes
    public byte[] getBytes(File file) {
        int len = (int) file.length();
        byte[] sendBuf = new byte[len];
        FileInputStream inFile = null;
        try {
            inFile = new FileInputStream(file);
            inFile.read(sendBuf, 0, len);
        } catch (FileNotFoundException fnfex) {
        } catch (IOException ioex) {
        }
        return sendBuf;
    }
    
    //transforma byte[] em arquivo
    public void converteByteArrayToFile(byte[] bytes, File arquivo) throws FileNotFoundException, IOException{
        FileOutputStream fos = new FileOutputStream(arquivo);
        fos.write(bytes);
        fos.close();
    }
     
    // Metodos para minimizar e sair do programa
    
    @FXML
    void sair(javafx.scene.input.MouseEvent event) {
        
        System.out.println("Fechando");
        isRunning = false;
        if (!pilha.isInterrupted()) { 
            pilha.interrupt();
        }
        System.exit(0);
    }
    
    void fechar() {
        System.out.println("Fechando");
        isRunning = false;
        if (!pilha.isInterrupted()) { 
            pilha.interrupt();
        }
        System.exit(0);
    
    }
    
    @FXML
    void minimizar(javafx.scene.input.MouseEvent event) {
        Chat.stagePrincipal.setIconified(true);
    }
    
    @FXML
    void logout(javafx.scene.input.MouseEvent event) {
        System.out.println("Fechando");
        isRunning = false;
        if (!pilha.isInterrupted()) { 
            pilha.interrupt();
        }
        controleTela.setTela(controleTela.screenLoginID);
    }
    
}
