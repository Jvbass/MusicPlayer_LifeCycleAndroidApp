package cl.jpinodev.reproductormusic

import android.media.MediaPlayer
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import cl.jpinodev.reproductormusic.AppConstant.Companion.LOG_MAIN_ACTIVITY
import cl.jpinodev.reproductormusic.AppConstant.Companion.MEDIAPLAYER_POSITION
import cl.jpinodev.reproductormusic.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private  var mediaPlayer : MediaPlayer? = null
    private lateinit var binding: ActivityMainBinding //binding es una variable que se inicializa en el método onCreate, por lo que no es necesario inicializarla aquí
    private var position: Int = 0
    private lateinit var currentSong: Song
    private var currentSongIndex: Int = 0
    private var isPlaying: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(LOG_MAIN_ACTIVITY, "Estoy en onCreate()")
        savedInstanceState?.let {
            position = it.getInt(MEDIAPLAYER_POSITION)
            currentSongIndex = it.getInt("currentSongIndex")
        }

        //setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater) //iniciamos binding dando acceso a todos los elementos del layout activity_main.xml
        setContentView(binding.root)
        //  mediaPlayer = MediaPlayer.create(this, R.raw.heavenandhell_blacksabbath)
        // mediaPlayer?.start()
        currentSong = AppConstant.Songs[currentSongIndex] //inicializamos la variable currentSong con la cancion actual de la lista
        //let funciona como un if, si savedInstanceState es diferente de null, se ejecuta el bloque de código
        savedInstanceState?.let {
            //it es una referencia al objeto savedInstanceState
            position = it.getInt(MEDIAPLAYER_POSITION)
        }
        updateUISong()

        binding.playPauseButton.setOnClickListener {
            playOrPauseMusic()
        }

        binding.playNextButton.setOnClickListener {
            playNextSong()
        }

        binding.playPreviousButton.setOnClickListener { playPreviousSong() }

        updateLyrics()
    }

    override fun onStart() {
        super.onStart()
        Log.i(LOG_MAIN_ACTIVITY, "Estoy en onStart()")
        mediaPlayer = MediaPlayer.create(this, currentSong.audioResId)
        mediaPlayer?.seekTo(position)
       /* if(isPlaying){*/
            mediaPlayer?.start()
            isPlaying = true
        updatePlayPauseButton()
      /*  } else {
            isPlaying = false
        }*/
    }

    override fun onResume() {
        super.onResume()
        Log.i(LOG_MAIN_ACTIVITY, "Estoy en onResume()")
        mediaPlayer?.seekTo(position)
        if(isPlaying){
            isPlaying = true
        } else {
            isPlaying = false
        }
    }

    override fun onPause() {
        super.onPause()
        Log.i(LOG_MAIN_ACTIVITY, "Estoy en onPause()")
        if (mediaPlayer !=null) {
            mediaPlayer?.pause()
            // con !! se asegura que mediaPlayer no sea null
            position = mediaPlayer!!.currentPosition
            isPlaying = false
        }
    }

    override fun onStop() {
        super.onStop()
        Log.i(LOG_MAIN_ACTIVITY, "Estoy en onStop()")
        if (mediaPlayer != null) {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    override fun onRestart() {
        super.onRestart()
        Log.i(LOG_MAIN_ACTIVITY, "Estoy en onRestart()")    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(LOG_MAIN_ACTIVITY, "Estoy en onDestroy()")
        mediaPlayer?.seekTo(position)
        mediaPlayer?.pause()
        mediaPlayer?.release()
        isPlaying = false
    }
//end of lifecycle methods
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //definimos la constante MEDIAPLAYER_POSITION en AppConstant para centralizar la variable en un solo lugar
        outState.putInt(MEDIAPLAYER_POSITION, position)
        outState.putInt("currentSongIndex", currentSongIndex)
    }

    private fun updateUISong(){
        binding.titleTextView.text = currentSong.title //actualizamos el título de la canción
        binding.albumCoverImageView.setImageResource(currentSong.imageResId) //actualizamos la imagen de la portada del álbum
    }

    private fun playOrPauseMusic(){
        if (isPlaying) {
            mediaPlayer?.pause()
        } else {
            mediaPlayer?.start()
        }
        isPlaying = !isPlaying
        updatePlayPauseButton()
    }

    private fun updatePlayPauseButton(){
       // binding.playPauseButton.text = if (isPlaying) "Pause" else "Play"
        binding.playPauseButton.setImageResource(if (isPlaying) R.drawable.pause else R.drawable.play)
        binding.playPauseButton.imageTintList = if (isPlaying) getColorStateList(R.color.white) else getColorStateList(R.color.red)
    }

    private fun playNextSong(){
        currentSongIndex = (currentSongIndex + 1) % AppConstant.Songs.size
        currentSong = AppConstant.Songs[currentSongIndex]
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(this, currentSong.audioResId)
        mediaPlayer?.start()
        updateUISong()//actualizamos la portada del álbum y el título de la canción
        updateLyrics()//actualizamos la letra de la canción
        isPlaying = !isPlaying
    }

    private fun playPreviousSong() {
        // Algoritmo para obtener el indice y hacer una lista circular
        //cancion anterior - tamaño lista de canciones pra que siempre sea positivo
        //% devuelve un número positivo si el dividendo es negativo
        currentSongIndex = (currentSongIndex - 1 + AppConstant.Songs.size) % AppConstant.Songs.size
        currentSong = AppConstant.Songs[currentSongIndex]
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(this, currentSong.audioResId)
        mediaPlayer?.start()
        isPlaying = true
        updateUISong()
        updateLyrics()
        isPlaying = !isPlaying
    }

    private fun updateLyrics(){
        binding.lyricsTextView?.setText(currentSong.lyricsResId)
        // binding.albumCoverImageView.setImageResource(currentSong.imageResId) //actualizamos la imagen de la portada del álbum

    }
} // end of MainActivity