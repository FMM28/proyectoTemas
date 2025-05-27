import secrets
import base64

def generar_clave_jwt_base64(longitud=64):
    """Genera una clave secreta segura en formato Base64 para JWT."""
    bytes_seguros = secrets.token_bytes(longitud)
    clave_base64 = base64.b64encode(bytes_seguros).decode('utf-8')
    return clave_base64

if __name__ == "__main__":
    clave = generar_clave_jwt_base64()
    print("Clave secreta JWT en Base64 generada:")
    print(clave)
