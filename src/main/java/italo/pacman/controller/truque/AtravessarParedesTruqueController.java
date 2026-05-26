package italo.pacman.controller.truque;

import italo.pacman.SistemaAplic;
import italo.pacman.nucleo.to.Jogo;
import italo.pacman.nucleo.to.TruqueListener;
import italo.pacman.sound.SoundManager;

public class AtravessarParedesTruqueController implements TruqueListener {
   
    private final SistemaAplic aplic;

    public AtravessarParedesTruqueController( SistemaAplic aplic ) {
        this.aplic = aplic;
    }

    @Override
    public void executa( char[] truque ) {
        Jogo jogo = aplic.getJogo();
        aplic.getJogoManager().setAtravessarParedes( jogo, !jogo.isAtravessarParedes() );
        if ( jogo.isSetarComoNaoAtravessarParedes() ) {        	
        	aplic.getSoundManager().asyncPlay( SoundManager.NAP ); 
        } else {
        	aplic.getSoundManager().asyncPlay( SoundManager.AP ); 
        }
    }
    
}
