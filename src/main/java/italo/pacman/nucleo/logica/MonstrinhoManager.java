package italo.pacman.nucleo.logica;

import italo.pacman.nucleo.logica.aestrela.PortaCelulaTiposCondicao;
import italo.pacman.nucleo.logica.listener.MonstrinhoListener;
import italo.pacman.nucleo.logica.util.MatematicaUtil;
import italo.pacman.nucleo.logica.util.TodosUtils;
import italo.pacman.nucleo.to.Fase;
import italo.pacman.nucleo.to.Jogo;
import italo.pacman.nucleo.to.Monstrinho;
import italo.pacman.nucleo.to.Pacman;
import italo.pacman.nucleo.to.Personagem;

public class MonstrinhoManager {
    
    private final TodosUtils utils;
    private final TodosManagers managers;

    private final PortaCelulaTiposCondicao portaCTipoCondicao = new PortaCelulaTiposCondicao();
    
    private MonstrinhoListener monstrinhoListener = null;
    
    public MonstrinhoManager( TodosManagers managers, TodosUtils utils ) {
        this.utils = utils;
        this.managers = managers;
    }       
       
    public void move( Monstrinho monstrinho, Fase fase, Jogo jogo, int frequencia, double pacmanDist ) {
        MoveManager moveManager = managers.getMoveManager();
        
        int estado = monstrinho.getEstado();
        
        if ( estado == Monstrinho.MUITO_FRACO ) {
            this.configuraDirecaoMonstrinhoMuitoFraco( monstrinho, fase, frequencia );
        } else if ( estado == Monstrinho.NORMAL ) {
            moveManager.configuraNovaDirPorRaio( monstrinho, fase, pacmanDist );
        }
        
        boolean moveu = moveManager.move( monstrinho, fase, jogo );
        if ( !moveu ) {
            this.sorteiaNovaDirecao( monstrinho );
            moveManager.move( monstrinho, fase, jogo );
        }               
    }
    
    public void configuraDirecaoMonstrinhoMuitoFraco( Monstrinho monstrinho, Fase fase, int frequencia ) {
        AEstrelaManager aestrelaManager = managers.getAEstrelaManager();
        
        int estadoMuitoFracoDif = frequencia - monstrinho.getControleFrequenciaMuitoFraco();
        int tempoParaCasinha = monstrinho.getTempoEstadoMuitoFracoParaCasinha();
        if ( estadoMuitoFracoDif > tempoParaCasinha ) {                        
            int casinhaX = fase.getCasinhaPortaX();
            int casinhaY = fase.getCasinhaPortaY();
            boolean entrou = this.tentaEntrarNaCasa( fase, monstrinho );
            if ( !entrou )
                aestrelaManager.configuraNovaDirMaisProxima( monstrinho, fase, casinhaX, casinhaY, portaCTipoCondicao );
        }
    }
    
    public void processaEstado( Monstrinho monstrinho, Fase fase, int frequencia, double pacmanDist ) {                                
        int cw = fase.getCelulaLargura();
        int ch = fase.getCelulaAltura();
        int min = Math.min( cw, ch );
        
        int estado = monstrinho.getEstado();                               
        
        if ( pacmanDist <= min ) {
            if ( estado == Monstrinho.FRACO || estado == Monstrinho.MELHORANDO ) {
                monstrinho.setEstado( Monstrinho.MUITO_FRACO ); 
                monstrinho.setControleFrequenciaMuitoFraco( frequencia );
                
                if ( monstrinhoListener != null )
                	monstrinhoListener.estadoAlterado( monstrinho, estado );
            } else {
                fase.setPerdeu( estado == Monstrinho.NORMAL ); 
            }        
        } else {
            int estadoFracoDif = frequencia - monstrinho.getControleFrequenciaFraco();
            int tempoEstadoFraco = monstrinho.getTempoEstadoFraco();
            switch ( estado ) {
                case Monstrinho.NORMAL:
                    monstrinho.setConfigNovaDirDistanteFlag( true ); 
                    break;
                case Monstrinho.FRACO:
                    if ( estadoFracoDif > tempoEstadoFraco/2 ) {
                        monstrinho.setEstado( Monstrinho.MELHORANDO );
                        
                        if ( monstrinhoListener != null )
                        	monstrinhoListener.estadoAlterado( monstrinho, estado );
                    }
                    break;
                case Monstrinho.MELHORANDO:
                    if ( estadoFracoDif > tempoEstadoFraco ) {
                        monstrinho.setEstado( Monstrinho.NORMAL );
                        
                        if ( monstrinhoListener != null )
                        	monstrinhoListener.estadoAlterado( monstrinho, estado );
                    }
                    break;
            }
        }
    }
    
    public void alteraAnguloOlhoMonstrinho( Monstrinho monstrinho ) {
        monstrinho.setOlhoAngulo( monstrinho.getOlhoAngulo() + Math.PI / 12 );
    }
    
    public int getFrequenciaMonstrinho( Monstrinho monstrinho ) {
        switch( monstrinho.getEstado() ) {
            case Monstrinho.NORMAL:      return monstrinho.getExecFrequencia();
            case Monstrinho.FRACO : 
            case Monstrinho.MELHORANDO:  return monstrinho.getFrequenciaFraco();
            case Monstrinho.MUITO_FRACO: return monstrinho.getFrequenciaMuitoFraco();
        }
        return monstrinho.getExecFrequencia();
    }
    
    public boolean tentaEntrarNaCasa( Fase fase, Monstrinho monstrinho ) {
        int cw = fase.getCelulaLargura();
        int ch = fase.getCelulaAltura();        
        int[][] paredes = fase.getParedes();
        
        int x = monstrinho.getX();
        int y = monstrinho.getY();                
        
        if ( (x-(cw/2)) % cw == 0 && (y-(ch/2)) % ch == 0 ) {            
            int xx = ( x - (cw/2) ) / cw;
            int yy = ( y - (ch/2) ) / ch;                                 
                                    
            if ( paredes[ yy ][ xx ] == Fase.CELULA_CASA ) {              
                monstrinho.setEstado( Monstrinho.NORMAL );
                if ( paredes[ yy-1 ][ xx ] == Fase.CELULA_PORTA_H ) {
                    monstrinho.setNovaDirecao( Personagem.CIMA );
                } else if ( paredes[ yy+1 ][ xx ] == Fase.CELULA_PORTA_H ) {
                    monstrinho.setNovaDirecao( Personagem.BAIXO );
                } else if ( paredes[ yy ][ xx-1 ] == Fase.CELULA_PORTA_V ) {
                    monstrinho.setNovaDirecao( Personagem.TRAZ );
                } else if ( paredes[ yy ][ xx+1 ] == Fase.CELULA_PORTA_V ) {
                    monstrinho.setNovaDirecao( Personagem.FRENTE );
                }
                return true;
            }
        }
        return false;
    }
    
    public void configuraNovaDirMaisDistante( Monstrinho monstrinho, Pacman pacman, Fase fase ) {
        MatematicaUtil matematicaUtil = utils.getMatematicaUtil();
        
        int cw = fase.getCelulaLargura();
        int ch = fase.getCelulaAltura();
        
        int x1 = monstrinho.getX() / cw;
        int y1 = monstrinho.getY() / ch;
        int x2 = pacman.getX() / cw;
        int y2 = pacman.getY() / ch;
                        
        int[][] paredes = fase.getParedes();
        
        int[][] dirs = {
            { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 }
        };
        
        int[] direcoes = { 
            Personagem.TRAZ, Personagem.FRENTE,
            Personagem.CIMA, Personagem.BAIXO
        };
        
        double r = matematicaUtil.distancia( x1, y1, x2, y2 );
        
        int novaDir = monstrinho.getNovaDirecao();
                
        for( int i = 0; i < dirs.length; i++ ) {
            int dx = dirs[i][0];
            int dy = dirs[i][1];
            int x = x1 + dx;
            int y = y1 + dy;
            if ( x >= 0 && x < paredes[0].length && y >= 0 && y < paredes.length ) {
                int dir = direcoes[ i ];
                if ( paredes[ y ][ x ] == Fase.CELULA_LIVRE ) {
                    double r2 = matematicaUtil.distancia( x, y, x2, y2 );
                    if ( r2 > r ) {                        
                        novaDir = dir;
                        r = r2;
                    }
                }
            }        
        }
         
        monstrinho.setNovaDirecao( novaDir ); 
    }
        
    public void sorteiaNovaDirecao( Monstrinho monstrinho ) {
        double v = (int)( Math.random() * 2.0d );
        if ( v == 2 )
            v--;
        
        int atualDir = monstrinho.getDirecaoAtual();
        if ( atualDir == Personagem.FRENTE || atualDir == Personagem.TRAZ ) {
            monstrinho.setNovaDirecao( v == 0 ? Personagem.CIMA : Personagem.BAIXO );
        } else {
            monstrinho.setNovaDirecao( v == 0 ? Personagem.FRENTE : Personagem.TRAZ );
        }        
    }
    
    public void pacmanComeuBolinhaGrande( Fase fase, int frequencia ) {
        for( Monstrinho m : fase.getMonstrinhos() ) {
            if ( m.getEstado() == Monstrinho.NORMAL ) {
                m.setEstado( Monstrinho.FRACO );       
                m.setControleFrequenciaFraco( frequencia ); 
                
                if ( monstrinhoListener != null )
                	monstrinhoListener.estadoAlterado( m, Monstrinho.NORMAL );
            }
        }
    }
            
    public void setMonstrinhoListener( MonstrinhoListener listener ) {
    	this.monstrinhoListener = listener;
    }
    
}
