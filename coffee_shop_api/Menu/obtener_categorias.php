<?php
require_once "conexion.php";
$sql = "SELECT id_categoria, nombre, orden FROM categorias ORDER BY orden";
$datos = $pdo->query($sql)->fetchAll();
responder(true, "Categorías obtenidas", $datos);
