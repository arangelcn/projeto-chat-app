/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelos;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

/**
 *
 * @author arcn
 */
public class Mensagem implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    private Usuario usrEnvia; 
    private Usuario usrRecebe;
    private Date data;
    private String conteudo;
    private Image msgImagem; 
    private String status; // enviada, recebida, nao enviada;
    private ArrayList<Usuario> grupo;
    private byte[] arquivo;
    private String nomeArquivo;
    private String formatoArquivo;
    private String protocolo; //mensagem, conexao, arquivo, solicitacao, solicitacaoOK, solicitacaoNOK, grupocriado
    
    public Mensagem(Usuario envia, Usuario recebe, String conteudo) {
        this.protocolo = "mensagem";
        this.formatoArquivo = "";
        this.grupo = new ArrayList<>();
        this.arquivo = null;
        this.usrEnvia = envia;
        this.usrRecebe = recebe;
        this.conteudo = conteudo;
        msgImagem = null;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }
    

    public String getFormatoArquivo() {
        return formatoArquivo;
    }

    public void setFormatoArquivo(String formatoArquivo) {
        this.formatoArquivo = formatoArquivo;
    }
    
    

    public String getProtocolo() {
        return protocolo;
    }

    public void setProtocolo(String protocolo) {
        this.protocolo = protocolo;
    }
    
    public void setGrupo(ArrayList<Usuario> grupo) { 
        this.grupo = grupo; 
    }
    
    public ArrayList<Usuario> getGrupo() { 
        return this.grupo;
    }
    
    public void setFile(byte[] arquivo, int tam) { 
        this.arquivo = new byte[tam];
        this.arquivo = arquivo;
    }
    
    public byte[] getFile(){ 
        return this.arquivo;
    }
    
    public Image getMsgImagem() { 
        return this.msgImagem;
    }
    
    public void setMsgImagem(Image img) { 
        this.msgImagem = img;
    }

    public Usuario getUsrEnvia() {
        return usrEnvia;
    }

    public void setUsrEnvia(Usuario usrEnvia) {
        this.usrEnvia = usrEnvia;
    }

    public Usuario getUsrRecebe() {
        return usrRecebe;
    }

    public void setUsrRecebe(Usuario usrRecebe) {
        this.usrRecebe = usrRecebe;
    }
    
    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }
    
}
