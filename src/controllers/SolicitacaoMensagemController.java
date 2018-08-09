/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author arcn
 */
public class SolicitacaoMensagemController implements Initializable {

    @FXML
    public Label lblNomeUsuario;

    @FXML
    public Label lblEmailUsuario;

    @FXML
    public JFXButton btnAceitar;

    @FXML
    public JFXButton btnRecusar;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
}
