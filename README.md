# PackerXUnpacker  
This application packs multiple files on directory into single file. Packed file is encrypted using 16 byte key and creates checksum of that file for validation. Before unpacking the packed file checksum is validated and after validation file is decrypted using 16 byte key. from the decrypted file all the packed files are unpacked.    
## Usage  
###### Compile ######  
$javac filenemae.java
###### Execute ######  
$java filenemae arg1 arg2  
. arg1 : DirectoryName
. arg2 : FileName
