package italo.pacman.sound;

public class SoundManager {

	public final static Audio COMEU = new Audio( "/audio/comeu.mp3" );
	public final static Audio COMEU_BG = new Audio( "/audio/comeubg.mp3" );
	public final static Audio PERDEU = new Audio( "/audio/perdeu.mp3" );
	public final static Audio PASSOU = new Audio( "/audio/passou.mp3" );
	public final static Audio ZEROU = new Audio( "/audio/zerou.mp3" );
	public final static Audio OOPS = new Audio( "/audio/oops.mp3" );
	public final static Audio GAMEOVER = new Audio( "/audio/gameover.mp3" );
	public final static Audio AP = new Audio( "/audio/atravessarparedes.mp3" );
	public final static Audio NAP = new Audio( "/audio/naoatravessarparedes.mp3" );
				
	private Audio[] AUDIOS = {
		COMEU,
		COMEU_BG,
		PERDEU,
		PASSOU,
		ZEROU,
		GAMEOVER,
		OOPS,
		AP,
		NAP
	};
	
	public void load() {
		try {
			for( Audio audio : AUDIOS )
				audio.load();
		} catch ( AudioLoadingException e ) {
			System.err.println( e.getMessage() );
		}
	}
			
	public void syncPlay( Audio audio ) {		
		audio.play( false );
	}
	
	public void asyncPlay( Audio audio ) {
		audio.play( true ); 
	}
	
}
