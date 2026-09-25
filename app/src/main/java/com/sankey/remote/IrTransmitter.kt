package com.sankey.remote

import android.content.Context
import android.hardware.ConsumerIrManager

/**
 * Envoltorio del emisor IR del celular.
 * Requiere equipo con blaster IR (Xiaomi/Redmi/POCO/Huawei con IR).
 */
class IrTransmitter(context: Context) {

    private val irManager: ConsumerIrManager? =
        context.getSystemService(Context.CONSUMER_IR_SERVICE) as ConsumerIrManager?

    fun hasIrEmitter(): Boolean = irManager?.hasIrEmitter() == true

    fun supportedFrequenciesText(): String {
        val mgr = irManager ?: return "sin IR"
        return try {
            val ranges = mgr.carrierFrequencies ?: return "desconocido"
            ranges.joinToString(", ") { "${it.minFrequency}-${it.maxFrequency}Hz" }
        } catch (e: Exception) {
            "desconocido"
        }
    }

    fun supportsNecFrequency(): Boolean {
        val mgr = irManager ?: return false
        return try {
            val ranges = mgr.carrierFrequencies ?: return true
            ranges.any {
                NecIrEncoder.FREQUENCY in it.minFrequency..it.maxFrequency
            }
        } catch (e: Exception) {
            true
        }
    }

    /**
     * Transmite un comando Sankey.
     * @return null si OK, mensaje de error si falla.
     */
    fun transmit(command: Int): String? {
        val mgr = irManager ?: return "Este celular no tiene emisor infrarrojo."
        if (!mgr.hasIrEmitter()) {
            return "Este celular no tiene emisor infrarrojo (IR blaster)."
        }
        return try {
            val pattern = NecIrEncoder.encode(SankeyIrCodes.ADDRESS, command)
            mgr.transmit(NecIrEncoder.FREQUENCY, pattern)
            null
        } catch (e: Exception) {
            "Error IR: ${e.message}"
        }
    }

    fun transmitRepeatBurst(): String? {
        val mgr = irManager ?: return "Sin IR."
        return try {
            mgr.transmit(NecIrEncoder.FREQUENCY, NecIrEncoder.encodeRepeat())
            null
        } catch (e: Exception) {
            "Error IR: ${e.message}"
        }
    }
}
