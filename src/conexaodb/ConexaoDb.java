
package conexaodb;

import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.bson.Document;
import modelos.*;

public class ConexaoDb {
    
    private final String HOST = "192.168.1.60";
    private final int PORT = 27017;
    private MongoClient user;
    private final String db = "aps";
    
    private MongoDatabase dbase;
    
   public void abrirConexao(){
       user = new MongoClient(HOST,PORT);
       dbase = user.getDatabase("aps" );
       System.out.println("Banco de dados aberto");
      
   }
   public void fecharConexao(){
       user.close();
       System.out.println("Conexao fechada");
   }
   
   public void inserirUser(Usuario usuario){
       abrirConexao();
       MongoCollection<Document> coll = dbase.getCollection("user");
       Document doc = new Document("email",usuario.getEmail()).append("nome",usuario.getNome()).append("senha",usuario.getSenha());
       coll.insertOne(doc);
       
        Document whereQuery = new Document();
        whereQuery.put("email", usuario.getEmail());

        // Cria um usr para fazer o update: 
        Document doc1 = new Document();
        doc1.put("$push", new Document("contatos", new Document().append("email", ".....................").append("nome", "Adicione Novos Amigos")));
        coll.updateOne(whereQuery, doc1);
       
       fecharConexao();
   }
   
   public void inserirListaMensagem(Usuario usr){
       abrirConexao();
       MongoCollection<Document> coll = dbase.getCollection("mensagens");
       
       LocalDate data = LocalDate.now();
       int dia = data.getDayOfMonth();
       int mes = data.getMonthValue();
       int ano = data.getYear();
       String date = Integer.toString(dia) +"-"+ Integer.toString(mes) +"-"+Integer.toString(ano);
       
       Document doc = new Document("data",date).append("usuario", usr.getEmail());
       coll.insertOne(doc);
       
       fecharConexao();
   }
   
   public void inserirMensagens(Usuario usr, ArrayList<Mensagem> mensagens) { 
       abrirConexao();
       MongoCollection<Document> coll = dbase.getCollection("mensagens");
       
       LocalDate data = LocalDate.now();
       int dia = data.getDayOfMonth();
       int mes = data.getMonthValue();
       int ano = data.getYear();
       String date = Integer.toString(dia) +"-"+ Integer.toString(mes) +"-"+Integer.toString(ano);
       
       Document doc = new Document("data",date).append("usuario", usr.getEmail());
       for (Mensagem msg : mensagens) {
           // Cria um usr para fazer o update: 
           Document doc1 = new Document();
           doc1.put("$push", new Document("conversas", new Document().append("de", msg.getUsrEnvia().getEmail()).append("para", msg.getUsrRecebe().getEmail()).append("conteudo", msg.getConteudo())));
           coll.updateOne(doc, doc1);
       }
       
       fecharConexao();
   }
   
   public Usuario pegaUsuario(String email) {
       abrirConexao();
       MongoCollection<Document> coll = dbase.getCollection("user");
       BasicDBObject whereQuery = new BasicDBObject();
       whereQuery.put("email", email);
       MongoCursor<Document> cursor = coll.find(whereQuery).iterator();
       
       // Cria um usr para retornar: 
       Usuario usrDB = new Usuario(); 
       while (cursor.hasNext()) {
           Document doc = cursor.next();
           usrDB.setEmail(doc.getString("email"));
           usrDB.setNome(doc.getString("nome"));
           usrDB.setSenha(doc.getString("senha"));
           usrDB.setImagem("avatar"); // mudar aqui
       } 
       fecharConexao();
       return usrDB;
   }
   
   public HashMap pegaTodosUsuarios() {
       HashMap<String, Usuario> mapaUsuarios = new HashMap<>();
       
       abrirConexao();
       MongoCollection<Document> coll = dbase.getCollection("serverusers");
       BasicDBObject whereQuery = new BasicDBObject();
       MongoCursor<Document> cursor = coll.find().iterator();
       
       // Cria um usr para retornar: 
       Usuario usrDB; 
       while (cursor.hasNext()) {
           Document doc = cursor.next();
           usrDB = new Usuario();
           usrDB.setEmail(doc.getString("email"));
           usrDB.setNome(doc.getString("nome"));
           usrDB.setSenha(doc.getString("senha"));
           usrDB.setImagem("avatar");
           mapaUsuarios.put(usrDB.getEmail(), usrDB);
       } 
       fecharConexao();
       return mapaUsuarios;
   }
   
   public void inserirContato(Usuario usuario, Usuario usuarioAdd){
        abrirConexao();
        MongoCollection<Document> coll = dbase.getCollection("user");
        Document whereQuery = new Document();
        whereQuery.put("email", usuario.getEmail());

        // Cria um usr para fazer o update: 
        Document doc = new Document();
        doc.put("$push", new Document("contatos", new Document().append("email", usuarioAdd.getEmail()).append("nome", usuarioAdd.getNome()))); // mudar aqui
        
        coll.updateOne(whereQuery, doc);
      
       fecharConexao();
   }
   
   public ArrayList pegaTodosContatos(Usuario usuario) { 
        ArrayList<Usuario> listaUsuarios = new ArrayList<>();
       
        abrirConexao();
        MongoCollection<Document> coll = dbase.getCollection("user");
        Document whereQuery = new Document();
        whereQuery.put("email", usuario.getEmail());
        MongoCursor<Document> cursor = coll.find(whereQuery).iterator();
        
        // Cria um usr para retornar: 
       Usuario usrDB; 
       while (cursor.hasNext()) {
           List<Document> listaContatos = new ArrayList<>();
           listaContatos = (List<Document>) cursor.next().get("contatos");
           int i=0;
           for (Document contato: listaContatos) { 
               usrDB = new Usuario();
               usrDB.setEmail(listaContatos.get(i).getString("email"));
               usrDB.setNome(listaContatos.get(i).getString("nome"));
               usrDB.setImagem("avatar");
               listaUsuarios.add(usrDB);
               i++;
           }
         
       } 
       
       fecharConexao();
       return listaUsuarios;
   }
   
   //implementar
   public void setaMensagens(Usuario a, Usuario b) { 
       
   }
   
   public void alterarContato(Usuario usuario, Usuario usuarioAdd){
       abrirConexao();
        MongoCollection<Document> coll = dbase.getCollection("user");
        coll.updateOne(Filters.eq("nome", usuario.getNome()),
                                 new Document("$set", new Document("nome",usuario.getNome())));
       
       fecharConexao();
   }
   
   public void deletarContato(Usuario usuario, Usuario usuarioAdd){
       abrirConexao();
        MongoCollection<Document> coll = dbase.getCollection("user");
        coll.deleteOne(Filters.eq("nome", usuario.getNome()));
       
       fecharConexao();
   }
   
   
    
}
