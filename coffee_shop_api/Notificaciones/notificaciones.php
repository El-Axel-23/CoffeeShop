<?php
// notificaciones.php
require_once 'config/conexion.php';
header('Content-Type: application/json');

$id_usuario = $_GET['id_usuario'] ?? 1;

try {
    $stmt = $pdo->prepare("
        SELECT id_notificacion, titulo, leida, fecha 
        FROM notificaciones 
        WHERE id_usuario = ? 
        ORDER BY fecha DESC
    ");
    $stmt->execute([$id_usuario]);
    
    echo json_encode([
        "success" => true,
        "notificaciones" => $stmt->fetchAll()
    ]);
} catch (Exception $e) {
    echo json_encode(["success" => false, "error" => $e->getMessage()]);
}
?>