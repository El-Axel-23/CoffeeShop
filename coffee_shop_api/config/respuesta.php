<?php

header("Content-Type: application/json; charset=UTF-8");

function responder($status, $message, $data = null) {

    echo json_encode(["status" => $status, "message" => $message, "data" => $data], JSON_UNESCAPED_UNICODE);

    exit;
}

?>
