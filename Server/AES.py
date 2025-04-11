import base64
from Crypto.Cipher import AES
from Crypto.Util.Padding import pad, unpad

def encrypt(text):
    key = "TheCoders12345678901234567890123"  
    cipher = AES.new(key.encode(), AES.MODE_ECB)  
    encrypted_bytes = cipher.encrypt(pad(text.encode(), AES.block_size))  
    return base64.b64encode(encrypted_bytes).decode()  
def decrypt(encrypted_text):
    cipher = AES.new(key.encode(), AES.MODE_ECB)
    key = "TheCoders12345678901234567890123"  
    decrypted_bytes = unpad(cipher.decrypt(base64.b64decode(encrypted_text)), AES.block_size)  
    return decrypted_bytes.decode()

