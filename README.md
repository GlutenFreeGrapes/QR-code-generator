# GlutenFreeGrapes's Java QR Code Generator
 A Java QR code generator that I designed in a month. 
 Spent too much time on error correction because of the fact that I spent 2-3 weeks learning to understand Reed-Solomon error correction, Galois field arithmetic, modulo 2 addition, multiplication, division, etc. Galois Field of 256 polynomial multiplication is eventually what I settled on. 
 Scrolling through the Java file you will notice that there are some extremely long lists. Two of those are because I really do not feel like generating entire lists for the GF(256) finite field and alpha notation for it. 
 
 The encoding is byte-encoding, meaning that the only characters that can be encoded are in the charset UTF-8. 
 
 I originally planned to add a "Restart" button, but I can't seem to find how to do it. 
 
 Made with what a Java CompSci Foundations and AP Computer Science and Data Structures could teach me, as well as plain ol' Google. 
 
 Here is the QR code generating tutorial that I referenced: 
 https://www.thonky.com/qr-code-tutorial/
 
 ISO Standards that were used: 
 - ISO/IEC 18004:2015,     CHF 198 = 222.52 USD in 2021            Information technology — Automatic identification and data capture techniques — QR Code bar code symbology specification
 
 About error correction: 
 - Error correction is frequently used in order to correct for things like mistransribing pixels and all. If the data has been mistranscribed in some way, or the QR code has been torn or is dirty, error correction can help and ensure that the QR code and its data will be read just fine. 
 - General rule of thumb: lower error correction means more data can be stored in a smaller sized QR code, while a higher error correction means that if the QR code gets very dirty, the QR code can still be read. 
 
 So how do you use this program?
 1. Run the Java program "QR.java", making sure that the images folder is downloaded as well. 
 2. Give the program the text that you would like to convert into a QR code, maximum length is 2953 characters. 
 3. Chose your error correction level from the dropdown menu. 
 4. A small panel should pop up, giving you a QR code and its information. There is a button at the bottom that allows the used to either save the QR code as a PNG file, and another one to quit. 
 
 Share this with friends, make sure to credit me, feel free to make edits for added efficiency. 
