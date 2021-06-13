# RelyMe
RelyMe is a secure app that helps you to store your blockchain wallet's address and private key.

# Contributors
   * Doğukan İnce
   * Muhammed Abbas
   * Ahmet Burak Karadoğan


# Youtube Link

https://www.youtube.com/watch?v=A8Ro-FRCblY

# Introduction
In the real world, we don't store our cash in plain sight. We either store it in a safe or we deposit it in a bank. In the virtual world, same rules apply. You don't want to store your private key in a text file or in a screenshot. RelyMe is your virtual safe. It stores all your wallet information with encryption in your local storage. No one except you can reach to that information.

# How to Use
  * Download "RelyMe" app in Google Play Store. 
  * For the first run, it will ask you to create a password. So create a single password to store all your wallet information in a single app.
  * After successfully creating a password, you are free to use our app.
  * You can add as many wallets as you want using the 'Encrypt' button.
  * After you create a wallet, it will add the label and encrypted version of the wallet to the list.
  * If you would like to see your 12-word phrase or your private key, press the decrypt button and enter the wallet label you want to decrypt.
# Login Options
 * Once you create your password, you can add new login options such as:
   * Face Recognition
   * Fingerprint
# Forgot Password
If you forgot your password, you can use biometrics(fingerprint or face recognition) to login and change your password from settings. If you did not activate any of those, your information cannot be reached and lost forever.

# The Encryption Algorithm We Used: AES

Advanced Encryption Standard (AES) is an encryption algorithm established by NIST in 2001 as a replacement for Data Encryption Standard (DES). The main reason for the replacement was that AES has a bigger key size which makes it exponentially more secure and is also considered to be more efficient in both software and hardware.
AES is a symmetric block cipher, which is a cipher that uses the same key for encryption and decryption. AES has three different block ciphers: 128, 192 and 256 bits. But AES performs all the computations on bytes instead of bits, meaning that the 128 bits block is treated as 16 bytes. The algorithm is iterative and is based on "substitution–permutation network", where AES uses 10, 12 and 14 for rounds of computation for 128, 192 and 256 bit keys respectively.

### Encryption Process
Each encryption round is made of 4 stages:
- Byte Substitution (SubBytes): bytes are substituted by looking up the S-box, which gives a matrix of four rows and four columns for a 16 byte input.
- Shiftrows: The four rows are shifted to the left. Any overflow gets reinserted on the end of the row.
- MixColumns: Each column gets transformed using a function that takes four bytes of one column as input and replaces them with four completely new bytes.
- Addroundkey: The result 16-byte matrix of the previous steps gets XORed.

### Decryption Process
Each decryption round does the same process of encryption but reversed.
