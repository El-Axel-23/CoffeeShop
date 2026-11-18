<?php

require_once("../config/conexion.php");
require_once("../config/respuesta.php");

try {

    $sql = "SELECT id_pregunta, pregunta, respuesta FROM preguntas_frecuentes ORDER BY orden ASC";

    $resultado = $conexion->query($sql);

    $preguntas = [];

    while ($fila = $resultado->fetch_assoc()) {
        $preguntas[] = $fila;
    }

    responder("success", "Preguntas frecuentes obtenidas correctamente", $preguntas);

} catch (Exception $e) {

    responder("error", "No se pudieron obtener las preguntas frecuentes", null);

}

?>