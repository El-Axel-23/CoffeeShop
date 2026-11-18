<?php

include("../config/conexion.php");
include("../config/respuesta.php");

$id_usuario = 1; //utilizo este nnumero para relizar pruebas

$sql = "SELECT id_usuario, nombre_completo, correo, telefono FROM usuarios WHERE id_usuario = ?";

$stmt = $conexion->prepare($sql);

$stmt->bind_param("i", $id_usuario);

$stmt->execute();

$resultado = $stmt->get_result();

if ($resultado->num_rows == 0) {
    responder("error", "No se encontro el usuario");
}

$usuario = $resultado->fetch_assoc();

responder("success", "Perfil obtenido correctamente", $usuario);

?>