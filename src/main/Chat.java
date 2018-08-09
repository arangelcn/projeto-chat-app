/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import controllers.ControleTelas;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import modelos.Usuario;

/**
 *
 * @author arcn
 */
public class Chat extends Application {
    
    // Define um Usuario principal do Chat: 
    public static Usuario usrPrincipal;
    
    
    // Inicializa a classe controleTelas da classe principal
    public static ControleTelas mainController;
    
    // Inicializa os componentes visuais da classe principal
    public static Stage stagePrincipal;
    public static Stage stageMensagem;
    
      
    @Override
    public void start(Stage primaryStage) throws Exception {
        
        String caminho;
        
        // Instancia o controle 
        mainController = new ControleTelas();
        
        // Instancia o usuario principal: 
        usrPrincipal = new Usuario();
           
        // Seta o caminho para o Cadastro:
        caminho = mainController.screenLoginID;
        mainController.carregaTela(mainController.screenLoginID);
        
        // Instancia a tela principal (Login)
        stagePrincipal = primaryStage;
         
        mainController.setTela(caminho);
        
        String os = System.getProperty("os.name");
        System.out.println(os);

    }
      
}
