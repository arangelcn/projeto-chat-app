/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelos;

import com.jfoenix.controls.JFXListCell;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author arcn
 */
public class ListViewMensagem extends JFXListCell<Mensagem> {
    
    private VBox painel;
    private HBox hbox;
    private VBox vBox;
    private Label nomeUsuario;
    private Label msgUsuario;
    private ImageView imagemUsuario;
     private MaterialDesignIconView icon;
    
    public ListViewMensagem() {
        icon = new MaterialDesignIconView(MaterialDesignIcon.ACCOUNT);
        painel = new VBox();
        hbox = new HBox(); 
        vBox = new VBox();
        nomeUsuario = new Label("Nome Aqui"); 
        msgUsuario = new Label("Email aqui");
        imagemUsuario = new ImageView();
    }
    
    @Override
    public void updateItem(Mensagem item, boolean empty) {
        super.updateItem(item, empty);
        
        // Novos itens:
        painel = new VBox();
        hbox = new HBox(); 
        vBox = new VBox();
        nomeUsuario = new Label("Nome Aqui"); 
        msgUsuario = new Label("Email aqui");
        imagemUsuario = new ImageView();
        icon = new MaterialDesignIconView(MaterialDesignIcon.ACCOUNT);
        icon.setFill(javafx.scene.paint.Color.WHITE);
        icon.setSize("27");
        
        hbox.setSpacing(25);
        hbox.setAlignment(Pos.CENTER_LEFT);
        if (item != null && !empty) {
            nomeUsuario.setText(item.getUsrEnvia().getNome() +": ");
            msgUsuario.setText(item.getConteudo());
            //final Image image = new Image(item.getUsrEnvia().getCaminhoImg());
            //if (image != null) {
            //    imagemUsuario.setImage(image);
            //    imagemUsuario.fitWidthProperty().set(30);
            //    imagemUsuario.fitHeightProperty().set(30);
            //    hbox.getChildren().add(imagemUsuario);
            //}
            hbox.getChildren().add(icon);
            vBox.getChildren().add(nomeUsuario);
            vBox.getChildren().add(msgUsuario);
            hbox.getChildren().add(vBox);
            setGraphic(hbox);
        } else {
            setGraphic(null);
        }
    }
    
    
}
