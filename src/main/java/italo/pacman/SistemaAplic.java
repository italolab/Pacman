package italo.pacman;

import italo.pacman.controller.JanelaController;
import italo.pacman.controller.MonstrinhoController;
import italo.pacman.controller.truque.AtravessarParedesTruqueController;
import italo.pacman.controller.truque.GrupoTruqueController;
import italo.pacman.controller.truque.PularFaseTruqueController;
import italo.pacman.controller.truque.VidasTruqueController;
import italo.pacman.desenho.JogoDesenho;
import italo.pacman.gui.GUI;
import italo.pacman.gui.principal.JanelaGUIListener;
import italo.pacman.nucleo.JogoThread;
import italo.pacman.nucleo.logica.AEstrelaManager;
import italo.pacman.nucleo.logica.FaseManager;
import italo.pacman.nucleo.logica.JogoManager;
import italo.pacman.nucleo.logica.MonstrinhoManager;
import italo.pacman.nucleo.logica.MoveManager;
import italo.pacman.nucleo.logica.PacmanManager;
import italo.pacman.nucleo.logica.TodosManagers;
import italo.pacman.nucleo.logica.TruqueManager;
import italo.pacman.nucleo.logica.ini.GrupoTruqueListener;
import italo.pacman.nucleo.logica.ini.JogoInicializador;
import italo.pacman.nucleo.logica.util.ArrayUtil;
import italo.pacman.nucleo.logica.util.MatematicaUtil;
import italo.pacman.nucleo.logica.util.TodosUtils;
import italo.pacman.nucleo.to.Jogo;
import italo.pacman.nucleo.to.TruqueListener;
import italo.pacman.sound.SoundManager;

public class SistemaAplic implements TodosManagers, TodosUtils {
    
    private final GUI gui = new GUI();
    private final SoundManager soundManager = new SoundManager();
    
    private final JogoInicializador inicializador = new JogoInicializador( this );
    
    private final ArrayUtil arrayUtil = new ArrayUtil();
    private final MatematicaUtil matematicaUtil = new MatematicaUtil();
    
    private final JogoManager jogoManager = new JogoManager( this ); 
    private final FaseManager faseManager = new FaseManager();
    private final MoveManager moveManager = new MoveManager( this );
    private final PacmanManager pacmanManager = new PacmanManager();
    private final MonstrinhoManager monstrinhoManager = new MonstrinhoManager( this, this );
    private final AEstrelaManager aestrelaManager = new AEstrelaManager();
    private final TruqueManager truqueManager = new TruqueManager();        
    
    private final Jogo jogo = new Jogo();
    private Jogo jogoConfigInicial;      
    
    private final JanelaGUIListener janelaController = new JanelaController( this );
    private final TruqueListener pularFaseTruqueController = new PularFaseTruqueController( this );
    private final TruqueListener vidasTruqueController = new VidasTruqueController( this );
    private final TruqueListener atravessarParedesTruqueController = new AtravessarParedesTruqueController( this ); 
    private final GrupoTruqueListener grupoTruqueController = new GrupoTruqueController( this );
    
    private final MonstrinhoController monstrinhoController = new MonstrinhoController( this );
    
    private final JogoThread jogoThread = new JogoThread( this );
    
    public void executa() {
        jogoConfigInicial = inicializador.inicializa( gui.getPainelDesenho(), grupoTruqueController );
        
        jogo.inicializa( jogoConfigInicial ); 
        
        soundManager.load();
				        
        monstrinhoManager.setMonstrinhoListener( monstrinhoController );  
        
        gui.getJanelaGUI().setJanelaGUIListener( janelaController );        
        gui.getPainelDesenho().setDesenho( new JogoDesenho( this ) ); 
        
        jogoThread.start();
    }
    
    public GUI getGUI() {
        return gui;
    }
    
    public SoundManager getSoundManager() {
    	return soundManager;
    }

    public JogoInicializador getInicializador() {
        return inicializador;
    }
    
    public Jogo getJogo() {
        return jogo;
    }
        
    public Jogo getJogoConfigInicial() {
        return jogoConfigInicial;
    } 

    public TruqueListener getPularFaseTruqueController() {
        return pularFaseTruqueController;
    }

    public TruqueListener getVidasTruqueController() {
        return vidasTruqueController;
    }

    public GrupoTruqueListener getGrupoTruqueController() {
        return grupoTruqueController;
    }

    public TruqueListener getAtravessarParedesTruqueController() {
        return atravessarParedesTruqueController;
    }
    
    @Override
    public JogoManager getJogoManager() {
        return jogoManager;
    }

    @Override
    public TruqueManager getTruqueManager() {
        return truqueManager;
    }

    @Override
    public MoveManager getMoveManager() {
        return moveManager;
    }

    @Override
    public FaseManager getFaseManager() {
        return faseManager;
    }

    @Override
    public PacmanManager getPacmanManager() {
        return pacmanManager;
    }

    @Override
    public MonstrinhoManager getMonstrinhoManager() {
        return monstrinhoManager;
    }

    @Override
    public AEstrelaManager getAEstrelaManager() {
        return aestrelaManager;
    }

    @Override
    public MatematicaUtil getMatematicaUtil() {
        return matematicaUtil;
    }

    @Override
    public ArrayUtil getArrayUtil() {
        return arrayUtil;
    }
    
}
