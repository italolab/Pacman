package italo.pacman.sound;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class Audio {

	private String path;

	private Player player = null;
	private boolean playing = false;
	
	private byte[] dados = null;

	public Audio(String path) {
		super();
		this.path = path;
		this.player = null;
		this.playing = false;
	}
	
	public void load() throws AudioLoadingException {
		InputStream in = SoundManager.class.getResourceAsStream( path );
		if ( in == null )
			throw new AudioLoadingException( "Audio não encontrado para leitura: "+path );			
			
		try {
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			byte[] data = new byte[4096];
			int bytesRead;
			while ( ( bytesRead = in.read( data ) ) != -1 )
				buffer.write( data, 0, bytesRead );

			dados = buffer.toByteArray();
		} catch ( IOException e ) {
			throw new AudioLoadingException( "Falha na leitura do audio: "+path );
		}
	}
	
	public void play( boolean async ) {	
		if ( dados == null ) {
			System.err.println( "Tentativa de tocar audio não carregado: "+path );
			return;
		}
				
		if ( async ) {
			new Thread( () -> {	
				syncPlay();
			} ).start();	
		} else {
			syncPlay();
		}
	}
	
	private void syncPlay() {
		try {
			ByteArrayInputStream in = new ByteArrayInputStream( dados );
			player = new Player( in );
			
			playing = true;
			player.play();			
			playing = false;
		} catch (JavaLayerException e) {
			System.err.println( "Falha na execução do audio: "+path ); 
		}
	}
	
	public void finishIfPlaying() {
		if ( playing && player != null ) {
			player.close();
		}
	}
		
}
