/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelos;

import java.awt.Image;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import javafx.beans.value.ObservableListValue;

/**
 *
 * @author arcn
 */
public class Usuario implements Serializable{
    
    private static final long serialVersionUID = 2L;
    
    private int ID;
    private String nome; 
    private Status status;
    //id**
    private String email; 
    private String senha; 
    private String nomeImg;
    private String caminhoImg;
    private ArrayList<Usuario> listaAmigos;
    private ArrayList<LinkedList<Mensagem>> listaMensagem;
    
    public Usuario() {
        this.listaAmigos = new ArrayList<>();
        this.listaMensagem = new ArrayList<LinkedList<Mensagem>>();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getImagem() {
        return this.nomeImg;
    }
    
    public String getCaminhoImg() {
        return "/views/img/"+this.nomeImg+".png";
    }

    public void setImagem(String imagem) {
        this.nomeImg = imagem;
        this.caminhoImg = "/views/img/"+imagem+".png";
    }

    public ArrayList<Usuario> getListaAmigos() {
        return listaAmigos;
    }

    public void setListaAmigos(ArrayList<Usuario> listaAmigos) {
        this.listaAmigos = listaAmigos;
    }

    public ArrayList<LinkedList<Mensagem>> getListaMensagem() {
        return listaMensagem;
    }

    public void setListaMensagem(ArrayList<LinkedList<Mensagem>> listaMensagem) {
        this.listaMensagem = listaMensagem;
    }
    
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    
    
    // Metodos de um usuario: 
    
    
}
