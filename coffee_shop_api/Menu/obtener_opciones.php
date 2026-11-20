<?php
require_once "conexion.php";
$idProducto = isset($_GET["id_producto"]) ? intval($_GET["id_producto"]) : 0;
if ($idProducto <= 0) responder(false, "Falta id_producto");
$sql = "SELECT o.id_opcion, o.tipo, o.nombre, o.precio_extra
        FROM opciones o
        INNER JOIN producto_opciones po ON po.id_opcion = o.id_opcion
        WHERE po.id_producto = :id
        ORDER BY FIELD(o.tipo,'tamano','leche','extra','preparacion'), o.id_opcion";
$stmt = $pdo->prepare($sql);
$stmt->execute([":id" => $idProducto]);
responder(true, "Opciones obtenidas", $stmt->fetchAll());
