<?php
// favoritos.php
require_once 'config/conexion.php';
header('Content-Type: application/json');

$method = $_SERVER['REQUEST_METHOD'];
$data = json_decode(file_get_contents("php://input"), true);

if ($method === 'GET') {
    $id_usuario = $_GET['id_usuario'] ?? 1;
    $stmt = $pdo->prepare("
        SELECT p.*, 1 AS es_favorito 
        FROM favoritos f 
        JOIN productos p ON f.id_producto = p.id_producto 
        WHERE f.id_usuario = ?
    ");
    $stmt->execute([$id_usuario]);
    echo json_encode(["success" => true, "favoritos" => $stmt->fetchAll()]);
} 
elseif ($method === 'POST') {
    $id_usuario = $data['id_usuario'] ?? 1;
    $id_producto = $data['id_producto'] ?? 0;

    $check = $pdo->prepare("SELECT * FROM favoritos WHERE id_usuario = ? AND id_producto = ?");
    $check->execute([$id_usuario, $id_producto]);

    if ($check->rowCount() > 0) {
        $del = $pdo->prepare("DELETE FROM favoritos WHERE id_usuario = ? AND id_producto = ?");
        $del->execute([$id_usuario, $id_producto]);
        echo json_encode(["success" => true, "accion" => "eliminado"]);
    } else {
        $ins = $pdo->prepare("INSERT INTO favoritos (id_usuario, id_producto) VALUES (?, ?)");
        $ins->execute([$id_usuario, $id_producto]);
        echo json_encode(["success" => true, "accion" => "agregado"]);
    }
}
?>