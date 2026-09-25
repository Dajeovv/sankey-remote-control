# Sankey Remote IR — Control para TV Sankey por infrarrojos

App Android nativa (Kotlin) que convierte un celular con **IR blaster** en control remoto para **televisores SANKEY**.
Solo Sankey, protocolo **NEC address 0x20, 38 kHz**. Códigos verificados desde base Flipper-IRDB `Sankey_TV.ir`.

## 1. Requisito indispensable

> **Solo funciona en celulares CON emisor infrarrojo (IR blaster).**
> Ejemplos que SÍ tienen: Xiaomi / Redmi / POCO (Redmi Note 8-13, POCO X3-F5), Huawei P30/Mate 20 con IR, Honor con IR.
> **NO funciona en:** Samsung Galaxy S/A (sin IR desde 2016), iPhone, Motorola, Pixel, ni la mayoría de gama alta reciente.
> La app detecta al abrir si hay IR y avisa en rojo si no hay.

No necesita WiFi, Bluetooth ni internet. Es IR directo, hay que apuntar al TV.

## 2. Qué incluye

- `MainActivity.kt` — UI + envío IR + repetición al mantener Vol/CH
- `SankeyIrCodes.kt` — 50+ códigos Sankey (Power 0x52, Mute 0x53, Vol+ 0x02, etc.)
- `NecIrEncoder.kt` — convierte NEC a patrón `ConsumerIrManager.transmit(38000, pattern)`
- `IrTransmitter.kt` — chequeo `hasIrEmitter()` y frecuencias
- `activity_main.xml` — control completo: Power, números 0-9, Vol/CH, D-pad OK, Menú, Input, Netflix, YouTube, colores R/G/Y/B, Sleep, etc.
- `verificar_codigos.py` — prueba que los 36 códigos principales codifican/decodifican NEC correctamente

## 3. Cómo compilar el APK (5 min)

1. Instala **Android Studio Ladybug o superior** + JDK 17.
2. En Android Studio: **Open → selecciona la carpeta `SankeyRemote`** (la que contiene `settings.gradle`).
3. Deja que Gradle sincronice (descarga `appcompat`, `material`).
4. Conecta un celular con IR en modo depuración USB, o usa **Build → Build APK(s)**.
5. El APK sale en `app/build/outputs/apk/debug/app-debug.apk`. Pásalo al celular e instálalo.
   - O directo: **Run ▶** con el celular conectado.

Datos Gradle: `minSdk 24` (Android 7.0+), `targetSdk 34`, Kotlin 1.9.22, AGP 8.2.2.

## 4. Cómo usar

1. Abre **Sankey Remote IR**.
2. Si dice `● IR listo` en verde, apunta la parte superior del celular al TV Sankey (alcance 5-8 m).
3. Pulsa Power. Debe responder igual que el control original.
4. Vol/CH: un toque = un paso, mantener = repite.
5. Abajo ves `Enviado: Power (NEC 0x20 / 0x52)` como confirmación.

## 5. Compatibilidad Sankey

Probado contra mapa NEC 0x20. Cubre series 19D/24D/32D/39D/42D/50D (ej. 32D3270, 39D3320, 42D10). Si tu Sankey es Smart con Netflix/YouTube, esos botones usan 0x05 / 0x58.

Tabla resumida (hex):

| Botón | CMD | Botón | CMD |
|---|---|---|---|
| Power | 52 | Mute | 53 |
| Vol+ | 02 | Vol- | 09 |
| CH+ | 03 | CH- | 41 |
| 1-9,0 | 00,10,11,13,14,15,17,18,19,1B | OK | 4A |
| Up/Down/Left/Right | 47/4D/49/4B | Menú/Exit | 06/0A |
| Source | 4E | Home/Settings | 40/1C |
| Netflix/YouTube | 05/58 | Rojo/Verde/Amarillo/Azul | 5C/5D/5E/5F |

Lista completa en `SankeyIrCodes.kt`.

## 6. Solución de problemas

- **"Sin emisor IR"**: ese celular no sirve para IR. Prueba con otro Xiaomi con IR arriba (agujerito negro).
- **No responde**: acerca a 2 m, quita funda gruesa, apunta directo, cambia pilas del TV? (no), prueba Power + Source. Verifica que sea Sankey original (no Roku stick).
- **Vol no repite**: mantén 0.5 s, la app reenvía cada 120 ms.
- **Error de compilación `mipmap`**: ya está corregido a icono del sistema, no necesitas agregar iconos.

## 7. Verificación técnica

```bash
python verificar_codigos.py
# OK: 36/36 códigos NEC verificados (addr 0x20)
# POWER pattern len: 67
# POWER duración total us: 67980
```

Cada trama = leader 9000,4500 + 32 bits (560 + 560/1690) + 560 final, a 38000 Hz. Longitud siempre 67 enteros.

---
Hecho para controlar **solo Sankey por IR**. Sin cuentas, sin ads, sin internet.
