<?php

include("../config/conexion.php");
include("../config/respuesta.php");

$datos = json_decode(file_get_contents("php://input"), true);

$id_usuario = 1;//utilizo este nnumero para relizar pruebas

if (!isset($datos["nombre_completo"]) || !isset($datos["correo"]) || !isset($datos["telefono"])) {
    responder("error", "Faltan datos para actualizar el perfil");
}

$nombre = trim($datos["nombre_completo"]);
$correo = trim($datos["correo"]);
$telefono = trim($datos["telefono"]);

if ($nombre == "" || $correo == "") {
    responder("error", "El nombre y el correo son obligatorios");
}

if (!filter_var($correo, FILTER_VALIDATE_EMAIL)) {
    responder("error", "El correo electronico no es valido");
}

$sql = "UPDATE usuarios SET nombre_completo = ?, correo = ?, telefono = ? WHERE id_usuario = ?";

$stmt = $conexion->prepare($sql);

$stmt->bind_param("sssi", $nombre, $correo, $telefono, $id_usuario);

if ($stmt->execute()) {
    responder("success", "Perfil actualizado correctamente");
} else {
    responder("error", "No se pudo actualizar el perfil");
}

?>