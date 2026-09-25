package com.sankey.remote

/**
 * Códigos IR originales SANKEY.
 * Protocolo: NEC, Address = 0x20, Frecuencia = 38 kHz.
 * Fuente: Flipper-IRDB / Sankey_TV.ir
 *
 * Solo Sankey. No se incluyen otras marcas a petición del usuario.
 */
object SankeyIrCodes {

    const val ADDRESS = 0x20

    // --- Potencia y sonido ---
    const val POWER = 0x52
    const val MUTE = 0x53
    const val VOL_UP = 0x02
    const val VOL_DN = 0x09

    // --- Canales ---
    const val CH_NEXT = 0x03
    const val CH_PREV = 0x41

    // --- Números ---
    const val NUM_0 = 0x1B
    const val NUM_1 = 0x00
    const val NUM_2 = 0x10
    const val NUM_3 = 0x11
    const val NUM_4 = 0x13
    const val NUM_5 = 0x14
    const val NUM_6 = 0x15
    const val NUM_7 = 0x17
    const val NUM_8 = 0x18
    const val NUM_9 = 0x19

    // --- Navegación ---
    const val MENU = 0x06
    const val EXIT = 0x0A
    const val INFO = 0x42
    const val SOURCE = 0x4E
    const val HOME = 0x40
    const val SETTINGS = 0x1C
    const val UP = 0x47
    const val DOWN = 0x4D
    const val LEFT = 0x49
    const val RIGHT = 0x4B
    const val OK = 0x4A

    // --- Smart / Media ---
    const val NETFLIX = 0x05
    const val YOUTUBE = 0x58
    const val MEDIA = 0x01
    const val EPG = 0x0D
    const val SHARE = 0x5A
    const val MOUSE_CLICK = 0x60
    const val SCREENSHOT = 0x56

    // --- Imagen / Audio / Extras ---
    const val P_MODE = 0x0B
    const val S_MODE = 0x46
    const val ASPECT = 0x0F
    const val MTS_AUDIO = 0x1D
    const val FAV = 0x07
    const val SLEEP = 0x1F
    const val FREEZE = 0x57
    const val SIZE = 0x5B
    const val INDEX = 0x44
    const val HOLD = 0x12
    const val REVEAL = 0x50
    const val SUBPAGE = 0x51
    const val SUB_TCC = 0x08
    const val TV_RADIO = 0x04
    const val T_SHIFT = 0x48
    const val REC = 0x43
    const val REPEAT = 0x4C
    const val REPEAT_AB = 0x1A
    const val TEXT_PAUSE_PLAY = 0x45

    // --- Colores ---
    const val RED = 0x5C
    const val GREEN = 0x5D
    const val YELLOW = 0x5E
    const val BLUE = 0x5F

    /** Mapa nombre -> comando, útil para depuración. */
    val ALL: Map<String, Int> = mapOf(
        "POWER" to POWER, "MUTE" to MUTE,
        "VOL_UP" to VOL_UP, "VOL_DN" to VOL_DN,
        "CH_NEXT" to CH_NEXT, "CH_PREV" to CH_PREV,
        "NUM_0" to NUM_0, "NUM_1" to NUM_1, "NUM_2" to NUM_2,
        "NUM_3" to NUM_3, "NUM_4" to NUM_4, "NUM_5" to NUM_5,
        "NUM_6" to NUM_6, "NUM_7" to NUM_7, "NUM_8" to NUM_8, "NUM_9" to NUM_9,
        "MENU" to MENU, "EXIT" to EXIT, "INFO" to INFO, "SOURCE" to SOURCE,
        "HOME" to HOME, "SETTINGS" to SETTINGS,
        "UP" to UP, "DOWN" to DOWN, "LEFT" to LEFT, "RIGHT" to RIGHT, "OK" to OK,
        "NETFLIX" to NETFLIX, "YOUTUBE" to YOUTUBE, "MEDIA" to MEDIA,
        "EPG" to EPG, "FAV" to FAV, "SLEEP" to SLEEP, "ASPECT" to ASPECT,
        "RED" to RED, "GREEN" to GREEN, "YELLOW" to YELLOW, "BLUE" to BLUE
    )
}
