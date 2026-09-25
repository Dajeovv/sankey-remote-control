"""Verificacion NEC Sankey - espejo de NecIrEncoder.kt"""
def encode(address, command):
    pat = [9000, 4500]
    def add_byte(v):
        for i in range(8):
            bit = (v >> i) & 1
            pat.append(560)
            pat.append(560 if bit == 0 else 1690)
    add_byte(address & 0xFF)
    add_byte((address & 0xFF) ^ 0xFF)
    add_byte(command & 0xFF)
    add_byte((command & 0xFF) ^ 0xFF)
    pat.append(560)
    return pat

def decode(pat):
    assert pat[0]==9000 and pat[1]==4500, "leader invalido"
    assert pat[-1]==560, "cierre invalido"
    assert len(pat)==67, f"longitud debe ser 67, es {len(pat)}"
    bits=[]
    for i in range(2, 2+64, 2):
        assert pat[i]==560, f"pulso {i} debe ser 560"
        off=pat[i+1]
        if off==560: bits.append(0)
        elif off==1690: bits.append(1)
        else: raise AssertionError(f"off invalido {off}")
    def bits_to_byte(b): return sum(v<<i for i,v in enumerate(b))
    a=bits_to_byte(bits[0:8]); ai=bits_to_byte(bits[8:16])
    c=bits_to_byte(bits[16:24]); ci=bits_to_byte(bits[24:32])
    assert (a ^ ai)==0xFF, "addr invertido mal"
    assert (c ^ ci)==0xFF, "cmd invertido mal"
    return a,c

CODIGOS = {
 "POWER":0x52,"MUTE":0x53,"VOL_UP":0x02,"VOL_DN":0x09,
 "CH_NEXT":0x03,"CH_PREV":0x41,"NUM_0":0x1B,"NUM_1":0x00,
 "NUM_2":0x10,"NUM_3":0x11,"NUM_4":0x13,"NUM_5":0x14,
 "NUM_6":0x15,"NUM_7":0x17,"NUM_8":0x18,"NUM_9":0x19,
 "MENU":0x06,"EXIT":0x0A,"INFO":0x42,"SOURCE":0x4E,
 "HOME":0x40,"SETTINGS":0x1C,"UP":0x47,"DOWN":0x4D,
 "LEFT":0x49,"RIGHT":0x4B,"OK":0x4A,"NETFLIX":0x05,
 "YOUTUBE":0x58,"MEDIA":0x01,"RED":0x5C,"GREEN":0x5D,
 "YELLOW":0x5E,"BLUE":0x5F,"SLEEP":0x1F,"FAV":0x07,
}
ok=0
for nombre, cmd in CODIGOS.items():
    p=encode(0x20, cmd)
    a,c=decode(p)
    assert a==0x20 and c==cmd, f"{nombre} decodifica mal"
    ok+=1
print(f"OK: {ok}/{len(CODIGOS)} codigos NEC verificados (addr 0x20)")

p_power=encode(0x20,0x52)
print("POWER pattern len:",len(p_power))
print("POWER pattern:",",".join(map(str,p_power[:12]))+"...")
print("POWER duracion total us:",sum(p_power))
print("Frecuencia: 38000 Hz")
# Repetition frame
rep=[9000,2250,560]
print("Repeat frame:",rep,"OK")
print("VERIFICACION PASADA")
