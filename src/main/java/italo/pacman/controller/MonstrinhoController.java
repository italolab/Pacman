package italo.pacman.controller;

import italo.pacman.SistemaAplic;
import italo.pacman.nucleo.logica.listener.MonstrinhoListener;
import italo.pacman.nucleo.to.Monstrinho;
import italo.pacman.sound.SoundManager;

public class MonstrinhoController implements MonstrinhoListener {

	private final SistemaAplic aplic;
	
	public MonstrinhoController( SistemaAplic aplic ) {
		this.aplic = aplic;
	}
 	
	@Override
	public void estadoAlterado( Monstrinho monstrinho, int estadoAnterior ) {
		if ( monstrinho.getEstado() == Monstrinho.MUITO_FRACO )
			aplic.getSoundManager().asyncPlay( SoundManager.OOPS ); 
	}	
	
}
