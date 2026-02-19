<?php
declare(strict_types=1);
require_once __DIR__ . '/bootstrap.php';

fix_log('upload_user_voice.php request start');

try {
    if (!isset($_FILES['file']) || !is_array($_FILES['file'])) {
        fix_log('upload_user_voice.php missing file');
        fix_json_response(['error' => 'missing_file'], 400);
        return;
    }

    $tmpPath = (string)$_FILES['file']['tmp_name'];
    if ($tmpPath === '' || !is_uploaded_file($tmpPath)) {
        fix_log('upload_user_voice.php invalid upload payload');
        fix_json_response(['error' => 'invalid_upload'], 400);
        return;
    }

    // We keep this endpoint for old clients; new client uses OpenAI directly.
    fix_log('upload_user_voice.php compatibility response');
    fix_json_response([
        'text' => 'Voice upload received. This endpoint is in compatibility mode.',
    ]);
} catch (Throwable $e) {
    fix_log('upload_user_voice.php failure', ['error' => $e->getMessage()]);
    fix_json_response(['error' => 'internal_error'], 500);
}

