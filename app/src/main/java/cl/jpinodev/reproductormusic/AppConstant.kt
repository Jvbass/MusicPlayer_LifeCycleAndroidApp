package cl.jpinodev.reproductormusic

data class Song (
    val title: String,
    val audioResId: Int,
    val imageResId: Int
){}

class AppConstant {
    // companion object es un objeto que se puede acceder sin instanciar la clase
    companion object {
        const val LOG_MAIN_ACTIVITY = "MainActivityReproductor"
        const val MEDIAPLAYER_POSITION = "mpPosition"

        val Songs = listOf(
            Song("Ride the Lightning", R.raw.m_ridethelightning, R.drawable.ridethelightning_metallica),
            Song("Heaven and Hell", R.raw.heavenandhell_blacksabbath, R.drawable.heaven_and_hell_blacksabbath),
            Song("Painkiller", R.raw.painkiller_judaspriest, R.drawable.painkiller_judaspriest)
        )
    }
}