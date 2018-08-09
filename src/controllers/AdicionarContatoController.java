/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author arcn
 */
public class AdicionarContatoController implements Initializable {
    
    @FXML
    public JFXTextField txtEmailAmigo;

    @FXML
    public JFXButton btnEnviaSolicitacao;
    
    @FXML
    public MaterialDesignIconView btnClose;

    @FXML
    void btnCloseClicado(javafx.scene.input.MouseEvent event) {
        MensagemController.telaAddContato.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
}
