
<?php
$host = "localhost";
$db = "u906011863_madison";
$user = "u906011863_madison";
$pass = "t>hv@Sm6";

try {
    $pdo = new PDO(
        "mysql:host=$host;dbname=$db;charset=utf8mb4",
        $user,
        $pass
    );

    $pdo->setAttribute(PDO::ATTR_EMULATE_PREPARES, false);
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    $pdo->setAttribute(PDO::ATTR_DEFAULT_FETCH_MODE, PDO::FETCH_ASSOC);

} catch (PDOException $e) {
    error_log("Error PDO: " . $e->getMessage());

    header("Content-Type: application/json; charset=UTF-8");
    http_response_code(500);

    echo json_encode([
        "exito" => false,
        "mensaje" => "Error de conexión."
    ]);
    exit;
}
?>
