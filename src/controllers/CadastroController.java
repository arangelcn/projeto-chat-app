package controllers;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.File;
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

/**
 *
 * @author arcn
 */
public class CadastroController implements Initializable, ControladorTelas {
    
    ControleTelas controleTela;
    
    @FXML
    private AnchorPane rootLoginPane;

    @FXML
    private AnchorPane CadastroPane;

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


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        controleTela = Chat.mainController;
    }
    

    @FXML
    void btnCadastrarClicado(ActionEvent event) throws IOException {
        File arquivo = new File("src/usrinfo/usrinfo.txt");
        arquivo.createNewFile(); 
    }

    @Override
    public void setScreenParent(ControleTelas screenPage) {
        controleTela = screenPage;
    }
    
    
}
