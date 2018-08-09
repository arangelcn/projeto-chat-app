/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conexaoserver;

import java.io.ObjectInputStream;

/**
 *
 * @author arcn
 */
public abstract class ReceptorDeMensagem implements Runnable{
    
    private ObjectInputStream entrada;
    
    public ReceptorDeMensagem(ObjectInputStream entrada) {
        this.entrada = entrada;
    }

    public ObjectInputStream getEntrada() {
        return entrada;
    }

    public void setEntrada(ObjectInputStream entrada) {
        this.entrada = entrada;
    }

    @Override
    public abstract void run();
}
