<?php
require_once "conexion.php";
$idUsuario = intval($_GET["id_usuario"] ?? 0);
if ($idUsuario <= 0) responder(false, "Falta id_usuario");
$sql = "SELECT id_pedido, tipo_entrega, metodo_pago, subtotal, impuestos, total, estado, fecha
        FROM pedidos WHERE id_usuario = ? ORDER BY fecha DESC";
$stmt = $pdo->prepare($sql);
$stmt->execute([$idUsuario]);
responder(true, "Pedidos obtenidos", $stmt->fetchAll());
