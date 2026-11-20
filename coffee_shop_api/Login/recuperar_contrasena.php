
<?php
header('Content-Type: application/json; charset=utf-8');

function responder($codigo, $exito, $mensaje) {
    http_response_code($codigo);
    echo json_encode([
        'exito' => $exito,
        'mensaje' => $mensaje
    ], JSON_UNESCAPED_UNICODE);
    exit;
}

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    responder(405, false, 'Solo se permite POST.');
}

$datos = json_decode(file_get_contents('php://input'), true);
if (!is_array($datos)) responder(400, false, 'JSON no valido.');

$accion = (string)($datos['accion'] ?? '');

if ($accion !== 'solicitar' && $accion !== 'restablecer') {
    responder(400, false, 'Accion no valida.');
}

require_once __DIR__ . '/conexion.php';

if (!isset($pdo) || !($pdo instanceof PDO)) {
    responder(500, false, 'Error de conexion.');
}

try {

    if ($accion === 'solicitar') {

        $correo = strtolower(trim((string)($datos['correo'] ?? '')));

        if (!filter_var($correo, FILTER_VALIDATE_EMAIL)) {
            responder(400, false, 'Correo no valido.');
        }

        $remitente = getenv('COFFEESHOP_CORREO_REMITENTE') ?: '';

        if (!filter_var($remitente, FILTER_VALIDATE_EMAIL)) {
            responder(503, false, 'Configura el correo remitente del servidor.');
        }

        $consulta = $pdo->prepare(
            "SELECT id_usuario FROM usuarios
             WHERE correo = ?
             AND proveedor = 'local'
             AND activo = 1
             LIMIT 1"
        );

        $consulta->execute([$correo]);
        $usuario = $consulta->fetch();

        if ($usuario) {

            $codigo = bin2hex(random_bytes(32));
            $tokenHash = hash('sha256', $codigo);
            $idUsuario = (int)$usuario['id_usuario'];

            $pdo->beginTransaction();

            $pdo->prepare(
                'UPDATE recuperacion_contrasena
                 SET usado = 1
                 WHERE id_usuario = ? AND usado = 0'
            )->execute([$idUsuario]);

            $pdo->prepare(
                'INSERT INTO recuperacion_contrasena
                 (id_usuario, token, expira, usado)
                 VALUES (?, ?, DATE_ADD(NOW(), INTERVAL 15 MINUTE), 0)'
            )->execute([$idUsuario, $tokenHash]);

            $idRecuperacion = (int)$pdo->lastInsertId();

            $pdo->commit();

            $asunto = 'Recuperar contraseña - Coffee Shop';

            $mensaje = "Tu código para cambiar la contraseña es:\n\n"
                . $codigo
                . "\n\nExpira en 15 minutos.";

            $headers = 'From: ' . $remitente
                . "\r\nContent-Type: text/plain; charset=UTF-8\r\n";

            if (!mail($correo, $asunto, $mensaje, $headers)) {

                $pdo->prepare(
                    'UPDATE recuperacion_contrasena
                     SET usado = 1
                     WHERE id_recuperacion = ?'
                )->execute([$idRecuperacion]);

                error_log('No se pudo enviar el correo de recuperacion.');

                responder(
                    503,
                    false,
                    'No se pudo enviar el correo. Revisa la configuracion de correo.'
                );
            }
        }

        responder(
            200,
            true,
            'Si el correo corresponde a una cuenta local activa, recibirás un código de recuperación.'
        );
    }

    $codigo = (string)($datos['token'] ?? '');
    $nueva = (string)($datos['nueva_contrasena'] ?? '');
    $confirmacion = (string)($datos['confirmar_contrasena'] ?? '');

    if (!preg_match('/^[a-f0-9]{64}$/', $codigo)) {
        responder(400, false, 'Código no valido.');
    }

    if (strlen($nueva) < 10 || strlen($nueva) > 72) {
        responder(400, false, 'La contraseña debe tener entre 10 y 72 bytes.');
    }

    if ($nueva !== $confirmacion) {
        responder(400, false, 'Las contraseñas no coinciden.');
    }

    $pdo->beginTransaction();

    $consulta = $pdo->prepare(
        "SELECT r.id_usuario, u.contrasena_hash
         FROM recuperacion_contrasena r
         JOIN usuarios u ON u.id_usuario = r.id_usuario
         WHERE r.token = ?
           AND r.usado = 0
           AND r.expira > NOW()
           AND u.activo = 1
           AND u.proveedor = 'local'
         LIMIT 1
         FOR UPDATE"
    );

    $consulta->execute([hash('sha256', $codigo)]);
    $registro = $consulta->fetch();

    if (!$registro) {
        $pdo->rollBack();
        responder(400, false, 'Código vencido o no valido.');
    }

    if (is_string($registro['contrasena_hash']) &&
        password_verify($nueva, $registro['contrasena_hash'])) {

        $pdo->rollBack();
        responder(400, false, 'La nueva contraseña debe ser distinta de la anterior.');
    }

    $hash = password_hash($nueva, PASSWORD_DEFAULT);

    $pdo->prepare(
        'UPDATE usuarios
         SET contrasena_hash = ?
         WHERE id_usuario = ?'
    )->execute([$hash, $registro['id_usuario']]);

    $pdo->prepare(
        'UPDATE recuperacion_contrasena
         SET usado = 1
         WHERE id_usuario = ?'
    )->execute([$registro['id_usuario']]);

    $pdo->commit();

    responder(200, true, 'Contraseña actualizada correctamente.');

} catch (Throwable $e) {

    if ($pdo->inTransaction()) {
        $pdo->rollBack();
    }

    error_log('Recuperacion: ' . $e->getMessage());
    responder(500, false, 'Error del servidor.');
}
