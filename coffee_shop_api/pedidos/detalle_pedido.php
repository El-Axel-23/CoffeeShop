<?php
require_once "conexion.php";
$idPedido = intval($_GET["id_pedido"] ?? 0);
$idUsuario = intval($_GET["id_usuario"] ?? 0);
if ($idPedido <= 0 || $idUsuario <= 0) responder(false, "Faltan parámetros");
$cab = $pdo->prepare(
    "SELECT id_pedido,tipo_entrega,metodo_pago,subtotal,impuestos,total,estado,fecha
     FROM pedidos WHERE id_pedido=? AND id_usuario=?"
);
$cab->execute([$idPedido,$idUsuario]);
$pedido = $cab->fetch();
if (!$pedido) responder(false, "Pedido no encontrado");

$det = $pdo->prepare(
    "SELECT id_detalle,nombre_producto,precio_unitario,cantidad,subtotal
     FROM pedido_detalle WHERE id_pedido=?"
);
$det->execute([$idPedido]);
$pedido["productos"] = $det->fetchAll();
foreach ($pedido["productos"] as &$producto) {
    $op = $pdo->prepare(
        "SELECT o.nombre, pdo.precio_extra FROM pedido_detalle_opciones pdo
         INNER JOIN opciones o ON o.id_opcion=pdo.id_opcion WHERE pdo.id_detalle=?"
    );
    $op->execute([$producto["id_detalle"]]);
    $producto["opciones"] = $op->fetchAll();
}
unset($producto);
responder(true, "Detalle del pedido", $pedido);
