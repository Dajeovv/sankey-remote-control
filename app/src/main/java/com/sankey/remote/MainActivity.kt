package com.sankey.remote

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var ir: IrTransmitter
    private lateinit var tvStatus: TextView
    private lateinit var tvLastCode: TextView
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ir = IrTransmitter(this)
        tvStatus = findViewById(R.id.tvStatus)
        tvLastCode = findViewById(R.id.tvLastCode)

        checkIrHardware()

        // --- Fila superior ---
        bind(R.id.btnPower, SankeyIrCodes.POWER, "Power")
        bind(R.id.btnMute, SankeyIrCodes.MUTE, "Mute")
        bind(R.id.btnSource, SankeyIrCodes.SOURCE, "Source/Input")
        bind(R.id.btnSleep, SankeyIrCodes.SLEEP, "Sleep")

        // --- Números ---
        bind(R.id.btnNum1, SankeyIrCodes.NUM_1, "1")
        bind(R.id.btnNum2, SankeyIrCodes.NUM_2, "2")
        bind(R.id.btnNum3, SankeyIrCodes.NUM_3, "3")
        bind(R.id.btnNum4, SankeyIrCodes.NUM_4, "4")
        bind(R.id.btnNum5, SankeyIrCodes.NUM_5, "5")
        bind(R.id.btnNum6, SankeyIrCodes.NUM_6, "6")
        bind(R.id.btnNum7, SankeyIrCodes.NUM_7, "7")
        bind(R.id.btnNum8, SankeyIrCodes.NUM_8, "8")
        bind(R.id.btnNum9, SankeyIrCodes.NUM_9, "9")
        bind(R.id.btnFav, SankeyIrCodes.FAV, "Fav")
        bind(R.id.btnNum0, SankeyIrCodes.NUM_0, "0")
        bind(R.id.btnInfo, SankeyIrCodes.INFO, "Info")

        // --- Volumen / Canal (con repetición al mantener) ---
        bindHold(R.id.btnVolUp, SankeyIrCodes.VOL_UP, "Vol+")
        bindHold(R.id.btnVolDown, SankeyIrCodes.VOL_DN, "Vol-")
        bindHold(R.id.btnChUp, SankeyIrCodes.CH_NEXT, "CH+")
        bindHold(R.id.btnChDown, SankeyIrCodes.CH_PREV, "CH-")

        // --- Navegación ---
        bind(R.id.btnUp, SankeyIrCodes.UP, "Arriba")
        bind(R.id.btnDown, SankeyIrCodes.DOWN, "Abajo")
        bind(R.id.btnLeft, SankeyIrCodes.LEFT, "Izquierda")
        bind(R.id.btnRight, SankeyIrCodes.RIGHT, "Derecha")
        bind(R.id.btnOk, SankeyIrCodes.OK, "OK")
        bind(R.id.btnMenu, SankeyIrCodes.MENU, "Menú")
        bind(R.id.btnExit, SankeyIrCodes.EXIT, "Salir")
        bind(R.id.btnHome, SankeyIrCodes.HOME, "Home")
        bind(R.id.btnSettings, SankeyIrCodes.SETTINGS, "Ajustes")

        // --- Smart ---
        bind(R.id.btnNetflix, SankeyIrCodes.NETFLIX, "Netflix")
        bind(R.id.btnYoutube, SankeyIrCodes.YOUTUBE, "YouTube")
        bind(R.id.btnMedia, SankeyIrCodes.MEDIA, "Media")
        bind(R.id.btnEpg, SankeyIrCodes.EPG, "EPG/Guía")

        // --- Imagen / extras ---
        bind(R.id.btnAspect, SankeyIrCodes.ASPECT, "Aspecto")
        bind(R.id.btnMts, SankeyIrCodes.MTS_AUDIO, "MTS/Audio")
        bind(R.id.btnPmode, SankeyIrCodes.P_MODE, "P-Mode")

        // --- Colores ---
        bind(R.id.btnRed, SankeyIrCodes.RED, "Rojo")
        bind(R.id.btnGreen, SankeyIrCodes.GREEN, "Verde")
        bind(R.id.btnYellow, SankeyIrCodes.YELLOW, "Amarillo")
        bind(R.id.btnBlue, SankeyIrCodes.BLUE, "Azul")
    }

    private fun checkIrHardware() {
        if (ir.hasIrEmitter()) {
            val okFreq = ir.supportsNecFrequency()
            tvStatus.text = if (okFreq) {
                "● IR listo — apunta al TV Sankey"
            } else {
                "● IR detectado (frec: ${ir.supportedFrequenciesText()})"
            }
            tvStatus.setTextColor(0xFF4CAF50.toInt())
        } else {
            tvStatus.text = "● SIN emisor IR — este celular no puede emitir infrarrojos. " +
                    "Necesitas un Xiaomi/Redmi/POCO/Huawei con IR blaster."
            tvStatus.setTextColor(0xFFF44336.toInt())
            Toast.makeText(
                this,
                "Sin IR blaster: la app solo funciona en celulares con infrarrojo.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun bind(buttonId: Int, command: Int, label: String) {
        findViewById<Button>(buttonId).setOnClickListener {
            sendIr(command, label)
        }
    }

    /**
     * Botón con repetición: un toque = 1 envío,
     * mantener presionado = reenvía cada 120 ms (ideal Vol/CH).
     */
    @SuppressLint("ClickableViewAccessibility")
    private fun bindHold(buttonId: Int, command: Int, label: String) {
        val btn = findViewById<Button>(buttonId)
        var repeating = false
        val repeatRunnable = object : Runnable {
            override fun run() {
                if (repeating) {
                    sendIr(command, label, silent = true)
                    handler.postDelayed(this, 120)
                }
            }
        }
        btn.setOnClickListener { sendIr(command, label) }
        btn.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    repeating = true
                    handler.postDelayed(repeatRunnable, 400)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    repeating = false
                    handler.removeCallbacks(repeatRunnable)
                }
            }
            false
        }
    }

    private fun sendIr(command: Int, label: String, silent: Boolean = false) {
        val error = ir.transmit(command)
        if (error != null) {
            tvLastCode.text = "Error: $error"
            if (!silent) Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        } else {
            tvLastCode.text = "Enviado: $label (NEC 0x20 / 0x${command.toString(16).uppercase().padStart(2, '0')})"
        }
    }
}
