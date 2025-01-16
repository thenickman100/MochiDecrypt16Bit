# MochiDecrypt16Bit
### This code is based on work done by [jindrapetrik/jpexs-decompiler](https://github.com/jindrapetrik/jpexs-decompiler) and [MegaHakkero/unmochify](https://github.com/MegaHakkero/unmochify).
#### Unmochify handles 32Bit decryption, but is not suited to handle the 16Bit decryption that some swf files require.  I created this solution to handle the 16Bit files.

## Build:
#### To compile, type the following into the terminal:
```
javac MochiDecrypt16Bit.java
```

## Decryption:
#### This program takes a .bin file as input, usually titled "7_mochicrypt.Payload.bin".  It will output a file called "7_mochicrypt.Payload.swf".  The payload bin file can be acquired using JPEXS.

#### To run decryption, type the following into the terminal:
```
java MochiDecrypt16Bit ./7_mochicrypt.Payload.bin
```