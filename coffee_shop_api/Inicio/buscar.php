<?php
// buscar.php
require_once 'config/conexion.php';
header('Content-Type: application/json');

$query = $_GET['q'] ?? '';
$id_usuario = $_GET['id_usuario'] ?? 1;

try {
    if (empty($query)) {
        $populares = $pdo->query("SELECT termino, veces FROM v_busquedas_populares LIMIT 5")->fetchAll();
        
        $recientes = $pdo->prepare("SELECT termino FROM busquedas WHERE id_usuario = ? ORDER BY fecha DESC LIMIT 5");
        $recientes->execute([$id_usuario]);
        
        echo json_encode([
            "success" => true,
            "populares" => $populares,
            "recientes" => $recientes->fetchAll()
        ]);
    } else {
        $stmtInsert = $pdo->prepare("INSERT INTO busquedas (id_usuario, termino) VALUES (?, ?)");
        $stmtInsert->execute([$id_usuario, $query]);

        $stmtProd = $pdo->prepare("
            SELECT p.*, IF(f.id_usuario IS NOT NULL, 1, 0) AS es_favorito
            FROM productos p
            LEFT JOIN favoritos f ON p.id_producto = f.id_producto AND f.id_usuario = ?
            WHERE p.nombre LIKE ? AND p.disponible = 1
        ");
        $stmtProd->execute([$id_usuario, "%$query%"]);
        
        echo json_encode([
            "success" => true,
            "resultados" => $stmtProd->fetchAll()
        ]);
    }
} catch (Exception $e) {
    echo json_encode(["success" => false, "error" => $e->getMessage()]);
}
?>