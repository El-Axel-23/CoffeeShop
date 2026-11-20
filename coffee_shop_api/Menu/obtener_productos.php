<?php
require_once "conexion.php";
$idCategoria = isset($_GET["id_categoria"]) ? intval($_GET["id_categoria"]) : 0;
$sql = "SELECT p.id_producto, p.id_categoria, c.nombre AS categoria,
               p.nombre, p.descripcion, p.precio, p.imagen_url,
               p.destacado, p.disponible
        FROM productos p
        INNER JOIN categorias c ON c.id_categoria = p.id_categoria
        WHERE p.disponible = 1";
if ($idCategoria > 0) {
    $sql .= " AND p.id_categoria = :id_categoria";
}
$sql .= " ORDER BY p.destacado DESC, p.nombre";
$stmt = $pdo->prepare($sql);
if ($idCategoria > 0) $stmt->bindValue(":id_categoria", $idCategoria, PDO::PARAM_INT);
$stmt->execute();
responder(true, "Productos obtenidos", $stmt->fetchAll());
