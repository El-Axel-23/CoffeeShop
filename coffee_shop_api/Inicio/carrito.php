<?php
// carrito.php
require_once 'config/conexion.php';
header('Content-Type: application/json');

$method = $_SERVER['REQUEST_METHOD'];
$data = json_decode(file_get_contents("php://input"), true);

if ($method === 'GET') {
    $id_usuario = $_GET['id_usuario'] ?? 1;
    
    $stmt = $pdo->prepare("
        SELECT ci.id_item, ci.cantidad, p.id_producto, p.nombre, p.precio, p.imagen_url,
        (ci.cantidad * p.precio) AS subtotal
        FROM carrito_items ci
        JOIN productos p ON ci.id_producto = p.id_producto
        WHERE ci.id_usuario = ?
    ");
    $stmt->execute([$id_usuario]);
    $items = $stmt->fetchAll();

    $total = array_sum(array_column($items, 'subtotal'));
    $cantidad_total = array_sum(array_column($items, 'cantidad'));

    echo json_encode([
        "success" => true,
        "items" => $items,
        "cantidad_badge" => $cantidad_total,
        "total_pagar" => $total
    ]);
} 
elseif ($method === 'POST') {
    $id_usuario = $data['id_usuario'] ?? 1;
    $id_producto = $data['id_producto'] ?? 0;
    $cantidad = $data['cantidad'] ?? 1;

    $check = $pdo->prepare("SELECT id_item, cantidad FROM carrito_items WHERE id_usuario = ? AND id_producto = ?");
    $check->execute([$id_usuario, $id_producto]);
    $item = $check->fetch();

    if ($item) {
        $nuevaCantidad = $item['cantidad'] + $cantidad;
        $upd = $pdo->prepare("UPDATE carrito_items SET cantidad = ? WHERE id_item = ?");
        $upd->execute([$nuevaCantidad, $item['id_item']]);
    } else {
        $ins = $pdo->prepare("INSERT INTO carrito_items (id_usuario, id_producto, cantidad) VALUES (?, ?, ?)");
        $ins->execute([$id_usuario, $id_producto, $cantidad]);
    }

    echo json_encode(["success" => true, "mensaje" => "Producto añadido al carrito correctamente"]);
}
?>