<?php
declare(strict_types=1);
require_once __DIR__ . '/bootstrap.php';

fix_log('voice_response.php request start');

try {
    $rawBody = file_get_contents('php://input') ?: '';
    $payload = json_decode($rawBody, true);
    $text = is_array($payload) ? trim((string)($payload['text'] ?? '')) : '';

    if ($text === '') {
        fix_log('voice_response.php empty text');
        fix_json_response(['error' => 'empty_text'], 400);
        return;
    }

    // Legacy endpoint kept for compatibility with old app builds.
    fix_log('voice_response.php accepted legacy call');
    fix_json_response(['error' => 'legacy_endpoint_not_used_use_openai_audio_speech']);
} catch (Throwable $e) {
    fix_log('voice_response.php failure', ['error' => $e->getMessage()]);
    fix_json_response(['error' => 'internal_error'], 500);
}

