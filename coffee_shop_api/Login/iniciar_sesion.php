
<?php
header('Content-Type: application/json; charset=utf-8');

function responder($codigo, $exito, $mensaje, $extra = []) {
    http_response_code($codigo);
    echo json_encode(array_merge([
        'exito' => $exito,
        'mensaje' => $mensaje
    ], $extra), JSON_UNESCAPED_UNICODE);
    exit;
}

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    responder(405, false, 'Solo se permite POST.');
}

$datos = json_decode(file_get_contents('php://input'), true);
if (!is_array($datos)) responder(400, false, 'JSON no valido.');

$correo = strtolower(trim((string)($datos['correo'] ?? '')));
$clave = (string)($datos['contrasena'] ?? '');

if (!filter_var($correo, FILTER_VALIDATE_EMAIL) || $clave === '') {
    responder(400, false, 'Correo y contraseña obligatorios.');
}

require_once __DIR__ . '/conexion.php';

if (!isset($pdo) || !($pdo instanceof PDO)) {
    responder(500, false, 'Error de conexion.');
}

try {
    $consulta = $pdo->prepare(
        'SELECT id_usuario, nombre_completo, correo,
                contrasena_hash, proveedor, rol, activo
         FROM usuarios
         WHERE correo = ?
         LIMIT 1'
    );

    $consulta->execute([$correo]);
    $usuario = $consulta->fetch();

    if (!$usuario ||
        $usuario['proveedor'] !== 'local' ||
        !is_string($usuario['contrasena_hash']) ||
        !password_verify($clave, $usuario['contrasena_hash'])) {

        responder(401, false, 'Correo o contraseña incorrectos.');
    }

    if ((int)$usuario['activo'] !== 1) {
        responder(403, false, 'Cuenta desactivada.');
    }

    responder(200, true, 'Credenciales correctas.', [
        'usuario' => [
            'id_usuario' => (int)$usuario['id_usuario'],
            'nombre_completo' => $usuario['nombre_completo'],
            'correo' => $usuario['correo'],
            'rol' => $usuario['rol']
        ]
    ]);

} catch (PDOException $e) {
    error_log('Login: ' . $e->getMessage());
    responder(500, false, 'Error del servidor.');
}
