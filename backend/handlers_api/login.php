<?php
declare(strict_types=1);
require_once __DIR__ . '/bootstrap.php';

fix_log('login.php request start', ['ip' => $_SERVER['REMOTE_ADDR'] ?? 'unknown']);

try {
    $pdo = fix_db();
    $input = fix_require_post(['email', 'password']);

    $user = fix_find_user_by_email($pdo, $input['email']);
    if (!$user) {
        fix_log('login.php user not found', ['email' => $input['email']]);
        fix_text_response('false');
        return;
    }

    if (!fix_verify_password($input['password'], (string)$user['password_hash'])) {
        fix_log('login.php password mismatch', ['email' => $input['email']]);
        fix_text_response('false');
        return;
    }

    fix_log('login.php success', ['user_id' => $user['id']]);
    fix_text_response('true');
} catch (Throwable $e) {
    fix_log('login.php failure', ['error' => $e->getMessage()]);
    fix_text_response('false', 500);
}

