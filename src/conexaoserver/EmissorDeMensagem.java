
package conexaoserver;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import modelos.Mensagem;
import modelos.Usuario;


public class EmissorDeMensagem {
    
    private ObjectOutputStream saida;

    public EmissorDeMensagem(ObjectOutputStream saida){
        this.saida = saida;
    }
    
    public void enviaMensagem(Mensagem mensagem) throws IOException {
        this.saida.writeObject(mensagem);
        this.saida.flush();
    }
    
    
}
