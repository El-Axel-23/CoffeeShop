
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

$nombre = trim((string)($datos['nombre_completo'] ?? ''));
$correo = strtolower(trim((string)($datos['correo'] ?? '')));
$clave = (string)($datos['contrasena'] ?? '');
$confirmacion = (string)($datos['confirmar_contrasena'] ?? '');
$terminos = ($datos['acepto_terminos'] ?? false) === true;

if ($nombre === '' || strlen($nombre) > 120) {
    responder(400, false, 'Nombre no valido.');
}

if (!filter_var($correo, FILTER_VALIDATE_EMAIL) || strlen($correo) > 120) {
    responder(400, false, 'Correo no valido.');
}

if (strlen($clave) < 10 || strlen($clave) > 72) {
    responder(400, false, 'La contraseña debe tener entre 10 y 72 bytes.');
}

if ($clave !== $confirmacion) {
    responder(400, false, 'Las contraseñas no coinciden.');
}

if (!$terminos) {
    responder(400, false, 'Debes aceptar los terminos y condiciones.');
}

require_once __DIR__ . '/conexion.php';

if (!isset($pdo) || !($pdo instanceof PDO)) {
    responder(500, false, 'Error de conexion.');
}

try {
    $consulta = $pdo->prepare(
        'SELECT id_usuario FROM usuarios WHERE correo = ? LIMIT 1'
    );

    $consulta->execute([$correo]);

    if ($consulta->fetch()) {
        responder(409, false, 'Este correo ya esta registrado.');
    }

    $hash = password_hash($clave, PASSWORD_DEFAULT);

    $insertar = $pdo->prepare(
        "INSERT INTO usuarios
         (nombre_completo, correo, contrasena_hash,
          proveedor, acepto_terminos, rol, activo)
         VALUES (?, ?, ?, 'local', 1, 'cliente', 1)"
    );

    $insertar->execute([$nombre, $correo, $hash]);

    responder(201, true, 'Cuenta creada correctamente.', [
        'usuario' => [
            'id_usuario' => (int)$pdo->lastInsertId(),
            'nombre_completo' => $nombre,
            'correo' => $correo,
            'rol' => 'cliente'
        ]
    ]);

} catch (PDOException $e) {
    if ($e->getCode() === '23000') {
        responder(409, false, 'Este correo ya esta registrado.');
    }

    error_log('Registro: ' . $e->getMessage());
    responder(500, false, 'Error del servidor.');
}
