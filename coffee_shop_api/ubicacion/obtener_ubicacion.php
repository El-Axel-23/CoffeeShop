<?php

require_once("../config/conexion.php");
require_once("../config/respuesta.php");

try {

    $sql = "SELECT nombre, direccion, latitud, longitud, telefono, correo, dias_atencion, hora_apertura, hora_cierre FROM cafeteria_info";

    $resultado = $conexion->query($sql);

    $ubicaciones = [];

    while ($fila = $resultado->fetch_assoc()) {
        $ubicaciones[] = $fila;
    }

    responder("success", "Informacion de la cafeteria obtenida correctamente", $ubicaciones);

} catch (Exception $e) {

    responder("error", "No se pudo obtener la ubicacion", null);

}

?>