package controllers;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import main.Chat;
import conexaodb.*;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import javafx.application.Platform;
import modelos.Usuario;

/**
 *
 * @author arcn
 */
public class LoginController implements Initializable, ControladorTelas {
    
    ControleTelas controleTela;
    
    @FXML
    private AnchorPane rootLoginPane;
    
    @FXML
    private JFXButton btnLogarCadastrar;

    @FXML
    private AnchorPane CadastroPane;

    @FXML
    private AnchorPane LoginPane;
    
    @FXML
    private Label label;

    @FXML
    private JFXTextField txtEmailCadastro;

    @FXML
    private JFXPasswordField txtSenhaCad;

    @FXML
    private JFXButton btnCadastrar;

    @FXML
    private JFXTextField txtNomeCadastro;

    @FXML
    private JFXPasswordField txtConfirmSenhaCad;

    @FXML
    private JFXTextField txtLogEmail;

    @FXML
    private JFXPasswordField txtLogSenha;

    @FXML
    private JFXButton btnLogin;
    
    @FXML
    private Label lblCadastro;
    
    @FXML
    private Label lblLogin;
    
    @FXML
    private MaterialDesignIconView btnClose;

    @FXML
    private MaterialDesignIconView btnMinimizar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        controleTela = Chat.mainController;
        btnLogarCadastrar.setText("Cadastrar");
        txtEmailCadastro.setOnMouseClicked(e -> lblCadastro.setText("Cadastro"));
        txtSenhaCad.setOnMouseClicked(e -> lblCadastro.setText("Cadastro"));
        txtNomeCadastro.setOnMouseClicked(e -> lblCadastro.setText("Cadastro"));
        txtConfirmSenhaCad.setOnMouseClicked(e -> lblCadastro.setText("Cadastro"));
        txtLogEmail.setOnMouseClicked(e -> lblLogin.setText("Login"));
        txtLogSenha.setOnMouseClicked(e -> lblLogin.setText("Login"));
    }

    // eventos do painel
    @FXML
    void handleCloseAction(javafx.scene.input.MouseEvent event) {

    }
    
    @FXML
    void btnCadastrarLogarClicado(ActionEvent event) {
        if (btnLogarCadastrar.getText().equalsIgnoreCase("Cadastrar")) {
            lblCadastro.setText("Cadastro");
            txtEmailCadastro.setText("");
            txtNomeCadastro.setText("");
            txtSenhaCad.setText("");
            txtConfirmSenhaCad.setText("");
            animaLogin(1);
        } else { 
            lblLogin.setText("Login");
            animaLogin(2);
        }
    } 
    
    @FXML
    void btnLoginClicado(ActionEvent event) throws IOException {
        // Estabelece uma conexao
        ConexaoDb con = new ConexaoDb();
        Usuario usrLogin = new Usuario();
        try {
            // Recebe um usuário com base no email:
            usrLogin = con.pegaUsuario(txtLogEmail.getText());
        } catch (Exception e) {
            System.out.println("Banco fora do ar");
        }
        
        System.out.println(usrLogin.getEmail());
        System.out.println(usrLogin.getSenha());
        
        if (usrLogin != null) {
            if (usrLogin.getSenha().equals(txtLogSenha.getText())) {
                Chat.usrPrincipal = usrLogin;
                
                // Muda pra tela de mensagem:
                controleTela.carregaTela(controleTela.screenMensagemID);
                controleTela.setTela(controleTela.screenMensagemID);
            } else { 
                lblLogin.setText("Senha Incorreta");
            }
        } else { 
            lblLogin.setText("Usuário não cadastrado");
        }
    }

    @FXML
    void btnCadastrarClicado(ActionEvent event) {
        //Criando uma conexao com DB
        ConexaoDb con = new ConexaoDb();
        
        //Criando um usuario para cadastro: 
        Usuario usrCadastro = new Usuario();
        
        // Verifica se as senhas são iguais 
        if (txtSenhaCad.getText().equals(txtConfirmSenhaCad.getText())) { 
            usrCadastro.setEmail(txtEmailCadastro.getText());
            usrCadastro.setNome(txtNomeCadastro.getText());
            usrCadastro.setImagem("avatar");
            usrCadastro.setSenha(txtSenhaCad.getText());
            con.inserirUser(usrCadastro);
            lblCadastro.setText("Usuário Cadastrado");
            animaLogin(2);
        } else {
            lblCadastro.setText("As senhas não conferem!!");
        } 
    }

    public void animaLogin(int type) { 
        if (type == 1) {
            TranslateTransition translateTransition = new TranslateTransition();
            translateTransition.setFromY(this.LoginPane.getLayoutY() - 45);
            translateTransition.setToY(this.LoginPane.getLayoutY() + 600);
            translateTransition.setDuration(Duration.seconds(1.5));
            translateTransition.setNode((Node) LoginPane);
            translateTransition.play();
            translateTransition.setOnFinished(e ->{
                btnLogarCadastrar.setText("Logar");     ;
            });
        } else { 
            TranslateTransition translateTransition = new TranslateTransition();
            translateTransition.setFromY(this.LoginPane.getLayoutY() + 600);
            translateTransition.setToY(this.LoginPane.getLayoutY() - 45);
            translateTransition.setDuration(Duration.seconds(1.5));
            translateTransition.setNode((Node) LoginPane);
            translateTransition.play();
            translateTransition.setOnFinished(e ->{
                btnLogarCadastrar.setText("Cadastrar");     
            });
        }
    }

    @Override
    public void setScreenParent(ControleTelas screenPage) {
        controleTela = screenPage;
    }
    
    @FXML
    void minimizar(javafx.scene.input.MouseEvent event) {
        System.out.println("Clicou");
        Chat.stagePrincipal.setIconified(true);
    }

    @FXML
    void sair(javafx.scene.input.MouseEvent event) {
        Platform.exit();
        System.exit(0);
    }
    
    
}
