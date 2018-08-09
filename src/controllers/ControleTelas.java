/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import com.sun.glass.events.WindowEvent;
import static java.lang.System.out;
import java.util.HashMap;
import java.util.Map;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.Chat;

/**
 *
 * @author arcn
 */
public class ControleTelas extends StackPane {
    
    private HashMap<String, Scene> telas;
    
    // Inicializa os identificadores dos componentes visuais
    public String screenLoginID = "Login";
    public String screenLoginFile = "/views/Login.fxml";
    public String screenCadastroID = "Cadastro"; 
    public String screenCadastroFile = "/views/Cadastro.fxml";
    public String screenMensagemID = "Mensagem";
    public String screenMensagemFile = "/views/InterfacePrincipal.fxml";

    
    public ControleTelas() {
        super();
        this.telas = new HashMap<>();
    }

    //
    public void addTela(String name, Scene screen) {
        telas.put(name, screen);
    }

    //
    public Scene getTela(String name) {
        return telas.get(name);
    }

    //
    public boolean carregaTela(String name) {
        try {     
            
            String caminho = this.screenLoginFile;
            
            if (name.equalsIgnoreCase("Mensagem")) caminho = this.screenMensagemFile;
            if (name.equalsIgnoreCase("Cadastro")) caminho = this.screenCadastroFile;
 
            FXMLLoader carregador = new FXMLLoader(getClass().getResource(caminho));
            Parent carregaFXML = (Parent) carregador.load();
            ControladorTelas meuControlador = (ControladorTelas) carregador.getController();
            meuControlador.setScreenParent(this);
            Scene cena = new Scene(carregaFXML);
            addTela(name, cena);
            return true;
        } catch (Exception e) {
            out.println(e.getMessage());
            return false;
        }
    }

    //
    public boolean setTela(String name) {
        if (telas.get(name) != null) {
            if (Chat.stagePrincipal.isShowing())
                Chat.stagePrincipal.close();//Fecha o Stage exibido 
            Chat.stagePrincipal = new Stage(); //Instancia um novo stage
            if (name.equals(this.screenLoginID)) { 
                Chat.stagePrincipal.initStyle(StageStyle.UNDECORATED);
                Chat.stagePrincipal.setResizable(true); // Torna impossivel mudar o tam da tela
            } else { 
                Chat.stagePrincipal.setOnCloseRequest(e->e.consume());
            }
            Chat.stagePrincipal.setScene(telas.get(name)); //Pega a Scene necessári
            Chat.stagePrincipal.centerOnScreen(); //Coloca no centro da tela
            Chat.stagePrincipal.show(); // Exibe 
                
            return true;
        } else {
            out.println("screen hasn't been loaded!!! \n");
            return false;
        }
        
    }

    //Metodo para remover uma tela qualquer
    public boolean unloadScreen(String name) {
        if (telas.remove(name) == null) {
            out.println("Screen didn't exist");
            return false;
        } else {
            return true;
        }
    }
}