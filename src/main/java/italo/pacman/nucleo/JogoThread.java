package italo.pacman.nucleo;

import italo.pacman.SistemaAplic;
import italo.pacman.nucleo.to.Fase;
import italo.pacman.nucleo.to.Jogo;
import italo.pacman.nucleo.to.Monstrinho;
import italo.pacman.nucleo.to.Pacman;
import italo.pacman.nucleo.to.Truque;
import italo.pacman.sound.SoundManager;

public class JogoThread extends Thread {
    
    private final int PONTUACAO_BOLINHA = 10;
    private final int PONTUACAO_BOLINHA_GRANDE = 50;
    
    private final SistemaAplic aplic;

    public JogoThread(SistemaAplic aplic) {
        this.aplic = aplic;
    }
    
    @Override
    public void run() {        
        Jogo jogo = aplic.getJogo();
                
        while( !jogo.isFim() ) {           
            int frequencia = 0;
            Fase fase;
            do {
                fase = aplic.getJogoManager().getFaseCorrente( jogo );                
                if ( !jogo.isPausa() ) {
                    Pacman pacman = fase.getPacman();
                    if ( frequencia % pacman.getExecFrequencia() == 0 )
                        this.pacmanAlgoritmo( fase.getPacman(), frequencia );

                    for( Monstrinho monstrinho : fase.getMonstrinhos() ) {
                        int freq = aplic.getMonstrinhoManager().getFrequenciaMonstrinho( monstrinho );
                        if ( frequencia % freq == 0 )
                            this.monstrinhoAlgoritmo( monstrinho, frequencia );
                    }     
                    
                    aplic.getJogoManager().verificaSeVenceu( jogo, fase ); 
                }
                
                if ( frequencia % 9 == 0 || fase.isVenceu() || fase.isPerdeu() || jogo.isPausa() )
                    aplic.getGUI().getPainelDesenho().repaint();
                
                aplic.getJogoManager().delay( 1 ); 
                                
                if ( !jogo.isPausa() )
                    frequencia++;
                
                if ( jogo.getTruques() != null )
                    for( Truque truque : jogo.getTruques() )
                        aplic.getTruqueManager().processaTruqueAtivado( truque );                                    
            } while( !jogo.isFim() && !fase.isVenceu() && !fase.isPerdeu() );
            
            if ( !jogo.isFim() ) {                
                if ( fase.isPerdeu() ) {
                    if ( jogo.getNVidas() > 0 ) {
                        jogo.setNVidas( jogo.getNVidas() - 1 ); 
                        aplic.getSoundManager().syncPlay( SoundManager.PERDEU ); 
                        fase.reinicia(); 
                    } else {
                        aplic.getSoundManager().syncPlay( SoundManager.GAMEOVER );
                        jogo.inicializa( aplic.getJogoConfigInicial() );
                    }
                } else {
                    if ( jogo.getFases().length-1 > jogo.getFaseCorrente() ) {
                    	aplic.getSoundManager().syncPlay( SoundManager.PASSOU );
                        aplic.getJogoManager().proximaFase( jogo );
                    } else {
                    	aplic.getSoundManager().syncPlay( SoundManager.ZEROU ); 
                        jogo.inicializa( aplic.getJogoConfigInicial() );
                    }
                }
            }
        }                
    }
    
    public void pacmanAlgoritmo( Pacman pacman, int frequencia ) {
        Jogo jogo = aplic.getJogo();
        Fase fase = aplic.getJogoManager().getFaseCorrente( jogo ); 

        aplic.getJogoManager().processaNaoAtravessarParedes( jogo, fase, pacman );
        aplic.getMoveManager().move( pacman, fase, jogo );
                
        int result = aplic.getPacmanManager().comeBolinha( fase, pacman );
        if ( result == Fase.BOLINHA ) {
            aplic.getJogoManager().incPontos( jogo, PONTUACAO_BOLINHA ); 
            aplic.getSoundManager().asyncPlay( SoundManager.COMEU );             
        } else if ( result == Fase.BOLINHA_GRANDE ) {
            aplic.getJogoManager().incPontos( jogo, PONTUACAO_BOLINHA_GRANDE ); 
            aplic.getSoundManager().asyncPlay( SoundManager.COMEU_BG ); 
            aplic.getMonstrinhoManager().pacmanComeuBolinhaGrande( fase, frequencia );
        }
        
        if ( frequencia % 9 == 0 )
            aplic.getPacmanManager().calculaNovoBocaFator( pacman ); 
    }
    
    public void monstrinhoAlgoritmo( Monstrinho monstrinho, int frequencia ) {
        Jogo jogo = aplic.getJogo();
        Fase fase = aplic.getJogoManager().getFaseCorrente( aplic.getJogo() ); 
        Pacman pacman = fase.getPacman();
        
        double pacmanDist = aplic.getMatematicaUtil().distancia( monstrinho, pacman );        
        
        aplic.getMonstrinhoManager().processaEstado( monstrinho, fase, frequencia, pacmanDist );        
        aplic.getMonstrinhoManager().alteraAnguloOlhoMonstrinho( monstrinho );                        
        aplic.getMonstrinhoManager().move( monstrinho, fase, jogo, frequencia, pacmanDist );
    }
    
}
