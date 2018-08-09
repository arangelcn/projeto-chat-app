/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.MouseEvent;
import modelos.ListViewGrupo;
import modelos.ListViewUsuario;
import modelos.Usuario;

/**
 * FXML Controller class
 *
 * @author arcn
 */
public class AdicionarGrupoController implements Initializable {

    @FXML
    public JFXButton btnCriaGrupo;

    @FXML
    private MaterialDesignIconView btnClose;

    @FXML
    public JFXListView<Usuario> listViewGrupo;
    
    @FXML
    public JFXTextField txtNomeDoGrupo;
    
    private ObservableList<Usuario> listaContatosGrupo;

    @FXML
    void btnCloseClicado(MouseEvent event) {
        MensagemController.telaAddGrupo.close();
    }

    @FXML
    void btnCriaGrupoClicado(javafx.scene.input.MouseEvent event) {
        System.out.println("    ssssss");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listViewGrupo.setCellFactory(e -> {
            ListViewGrupo lv = new ListViewGrupo();
            lv.setOnMouseClicked(ev ->{
                lv.check.fire();
               
            });
            return lv;
        });
        listViewGrupo.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }    
    
    @FXML
    void listClicada(javafx.scene.input.MouseEvent event) { 
    }
    
}
