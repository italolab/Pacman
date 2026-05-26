package italo.pacman.nucleo.logica.listener;

import italo.pacman.nucleo.to.Monstrinho;

public interface MonstrinhoListener {

	public void estadoAlterado( Monstrinho monstrinho, int estadoAnterior );
	
}
