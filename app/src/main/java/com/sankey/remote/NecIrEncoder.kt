package com.sankey.remote

/**
 * Codificador NEC para TVs SANKEY.
 *
 * Todos los Sankey usan protocolo NEC con address = 0x20.
 * Fuente: base Flipper-IRDB Sankey_TV.ir (verificada).
 *
 * Formato NEC (38 kHz):
 *  - Leader: 9000us ON, 4500us OFF
 *  - 32 bits LSB-first: addr, ~addr, cmd, ~cmd
 *    cada bit: 560us ON + (560us OFF si 0 / 1690us OFF si 1)
 *  - Cierre: 560us ON
 */
object NecIrEncoder {

    const val FREQUENCY = 38000

    private const val LEADER_ON = 9000
    private const val LEADER_OFF = 4500
    private const val BIT_ON = 560
    private const val BIT_OFF_0 = 560
    private const val BIT_OFF_1 = 1690
    private const val FINAL_BURST = 560

    fun encode(address: Int, command: Int): IntArray {
        val pattern = ArrayList<Int>(70)
        pattern.add(LEADER_ON)
        pattern.add(LEADER_OFF)
        addByte(pattern, address and 0xFF)
        addByte(pattern, (address and 0xFF) xor 0xFF)
        addByte(pattern, command and 0xFF)
        addByte(pattern, (command and 0xFF) xor 0xFF)
        pattern.add(FINAL_BURST)
        return pattern.toIntArray()
    }

    private fun addByte(pattern: ArrayList<Int>, value: Int) {
        for (i in 0 until 8) {
            val bit = (value shr i) and 0x01
            pattern.add(BIT_ON)
            if (bit == 0) pattern.add(BIT_OFF_0) else pattern.add(BIT_OFF_1)
        }
    }

    /** Trama de repeticion NEC (tecla mantenida): 9ms + 2.25ms + 560us */
    fun encodeRepeat(): IntArray = intArrayOf(9000, 2250, 560)
}
