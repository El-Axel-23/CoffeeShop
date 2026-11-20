<?php
// productos.php
require_once 'config/conexion.php';
header('Content-Type: application/json');

$accion = $_GET['accion'] ?? 'todos';
$id_usuario = $_GET['id_usuario'] ?? 1;

try {
    if ($accion === 'inicio') {
        // 1. Categorías (chips horizontales)
        $stmtCat = $pdo->query("SELECT id_categoria, nombre FROM categorias ORDER BY orden ASC");
        $categorias = $stmtCat->fetchAll();

        // 2. Cafés destacados de hoy
        $stmtDestacados = $pdo->prepare("
            SELECT p.*, 
            IF(f.id_usuario IS NOT NULL, 1, 0) AS es_favorito
            FROM productos p
            LEFT JOIN favoritos f ON p.id_producto = f.id_producto AND f.id_usuario = ?
            WHERE p.destacado = 1 AND p.disponible = 1
        ");
        $stmtDestacados->execute([$id_usuario]);
        $destacados = $stmtDestacados->fetchAll();

        // 3. Menú completo
        $stmtMenu = $pdo->prepare("
            SELECT p.*, 
            IF(f.id_usuario IS NOT NULL, 1, 0) AS es_favorito
            FROM productos p
            LEFT JOIN favoritos f ON p.id_producto = f.id_producto AND f.id_usuario = ?
            WHERE p.disponible = 1
        ");
        $stmtMenu->execute([$id_usuario]);
        $menu = $stmtMenu->fetchAll();

        echo json_encode([
            "success" => true,
            "categorias" => $categorias,
            "destacados" => $destacados,
            "menu_completo" => $menu
        ]);
    } 
    elseif ($accion === 'categoria') {
        $id_categoria = $_GET['id_categoria'] ?? 1;
        $stmt = $pdo->prepare("
            SELECT p.*, IF(f.id_usuario IS NOT NULL, 1, 0) AS es_favorito
            FROM productos p
            LEFT JOIN favoritos f ON p.id_producto = f.id_producto AND f.id_usuario = ?
            WHERE p.id_categoria = ? AND p.disponible = 1
        ");
        $stmt->execute([$id_usuario, $id_categoria]);
        echo json_encode(["success" => true, "productos" => $stmt->fetchAll()]);
    }
} catch (Exception $e) {
    echo json_encode(["success" => false, "error" => $e->getMessage()]);
}
?>