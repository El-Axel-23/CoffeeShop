<?php
require_once "conexion.php";

$entrada = json_decode(file_get_contents("php://input"), true);
if (!$entrada) responder(false, "JSON inválido");

$idUsuario = intval($entrada["id_usuario"] ?? 0);
$tipoEntrega = $entrada["tipo_entrega"] ?? "";
$idDireccion = isset($entrada["id_direccion"]) ? intval($entrada["id_direccion"]) : null;
$items = $entrada["items"] ?? [];

if ($idUsuario <= 0 || !in_array($tipoEntrega, ["en_cafeteria","domicilio"], true) || !is_array($items) || count($items) === 0) {
    responder(false, "Datos del pedido incompletos");
}
if ($tipoEntrega === "domicilio" && !$idDireccion) responder(false, "Selecciona una dirección para el domicilio");

try {
    $pdo->beginTransaction();

    // Verifica que el usuario exista y esté activo.
    $u = $pdo->prepare("SELECT id_usuario FROM usuarios WHERE id_usuario = ? AND activo = 1");
    $u->execute([$idUsuario]);
    if (!$u->fetch()) throw new Exception("Usuario no válido");

    if ($idDireccion) {
        $d = $pdo->prepare("SELECT id_direccion FROM direcciones WHERE id_direccion = ? AND id_usuario = ?");
        $d->execute([$idDireccion, $idUsuario]);
        if (!$d->fetch()) throw new Exception("La dirección no pertenece al usuario");
    }

    $subtotal = 0;
    $lineas = [];
    foreach ($items as $item) {
        $idProducto = intval($item["id_producto"] ?? 0);
        $cantidad = intval($item["cantidad"] ?? 0);
        $idsOpciones = $item["opciones"] ?? [];
        if ($idProducto <= 0 || $cantidad <= 0 || $cantidad > 30 || !is_array($idsOpciones)) {
            throw new Exception("Producto, cantidad u opciones incorrectos");
        }

        $p = $pdo->prepare("SELECT id_producto, nombre, precio FROM productos WHERE id_producto = ? AND disponible = 1");
        $p->execute([$idProducto]);
        $producto = $p->fetch();
        if (!$producto) throw new Exception("Uno de los productos no está disponible");

        $precioBase = (float)$producto["precio"];
        $precioOpciones = 0;
        $opcionesValidas = [];
        foreach ($idsOpciones as $idOpcion) {
            $idOpcion = intval($idOpcion);
            $o = $pdo->prepare(
                "SELECT o.id_opcion, o.nombre, o.precio_extra
                 FROM opciones o
                 INNER JOIN producto_opciones po ON po.id_opcion = o.id_opcion
                 WHERE o.id_opcion = ? AND po.id_producto = ?"
            );
            $o->execute([$idOpcion, $idProducto]);
            $op = $o->fetch();
            if (!$op) throw new Exception("Opción no válida para un producto");
            $precioOpciones += (float)$op["precio_extra"];
            $opcionesValidas[] = $op;
        }
        $precioUnitario = $precioBase + $precioOpciones;
        $totalLinea = $precioUnitario * $cantidad;
        $subtotal += $totalLinea;
        $lineas[] = [
            "producto" => $producto, "cantidad" => $cantidad,
            "precio_unitario" => $precioUnitario, "subtotal" => $totalLinea,
            "opciones" => $opcionesValidas
        ];
    }

    $conf = $pdo->prepare("SELECT valor FROM configuracion_sistema WHERE clave = 'impuesto_porcentaje'");
    $conf->execute();
    $porcentaje = (float)($conf->fetchColumn() ?: 0);
    $impuestos = round($subtotal * $porcentaje / 100, 2);
    $total = $subtotal + $impuestos;

    $metodoPago = $tipoEntrega === "domicilio" ? "efectivo_domicilio" : "efectivo_cafeteria";
    $pedido = $pdo->prepare(
        "INSERT INTO pedidos (id_usuario,tipo_entrega,metodo_pago,id_direccion,subtotal,impuestos,total,estado)
         VALUES (?,?,?,?,?,?,?,'pendiente')"
    );
    $pedido->execute([$idUsuario,$tipoEntrega,$metodoPago,$idDireccion,$subtotal,$impuestos,$total]);
    $idPedido = (int)$pdo->lastInsertId();

    $detalle = $pdo->prepare(
        "INSERT INTO pedido_detalle (id_pedido,id_producto,nombre_producto,precio_unitario,cantidad,subtotal)
         VALUES (?,?,?,?,?,?)"
    );
    $detalleOpcion = $pdo->prepare(
        "INSERT INTO pedido_detalle_opciones (id_detalle,id_opcion,precio_extra) VALUES (?,?,?)"
    );

    foreach ($lineas as $linea) {
        $detalle->execute([
            $idPedido, $linea["producto"]["id_producto"], $linea["producto"]["nombre"],
            $linea["precio_unitario"], $linea["cantidad"], $linea["subtotal"]
        ]);
        $idDetalle = (int)$pdo->lastInsertId();
        foreach ($linea["opciones"] as $op) {
            $detalleOpcion->execute([$idDetalle,$op["id_opcion"],$op["precio_extra"]]);
        }
    }

    $n = $pdo->prepare("INSERT INTO notificaciones (id_usuario,id_pedido,titulo) VALUES (?,?,?)");
    $n->execute([$idUsuario,$idPedido,"Tu pedido #$idPedido fue recibido"]);

    $pdo->commit();
    responder(true, "Pedido registrado", [
        "id_pedido" => $idPedido, "subtotal" => $subtotal,
        "impuestos" => $impuestos, "total" => $total
    ]);
} catch (Throwable $e) {
    if ($pdo->inTransaction()) $pdo->rollBack();
    responder(false, $e->getMessage());
}
