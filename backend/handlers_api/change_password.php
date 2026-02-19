<?php
declare(strict_types=1);
require_once __DIR__ . '/bootstrap.php';

fix_log('change_password.php request start');

try {
    $pdo = fix_db();
    $input = fix_require_post(['email', 'password', 'newPassword']);

    $user = fix_find_user_by_email($pdo, $input['email']);
    if (!$user || !fix_verify_password($input['password'], (string)$user['password_hash'])) {
        fix_log('change_password.php auth failed', ['email' => $input['email']]);
        fix_text_response('false');
        return;
    }

    $stmt = $pdo->prepare('UPDATE users SET password_hash = :password_hash, updated_at = CURRENT_TIMESTAMP WHERE id = :id');
    $stmt->execute([
        ':password_hash' => password_hash($input['newPassword'], PASSWORD_BCRYPT),
        ':id' => $user['id'],
    ]);

    fix_log('change_password.php success', ['user_id' => $user['id']]);
    fix_text_response('true');
} catch (Throwable $e) {
    fix_log('change_password.php failure', ['error' => $e->getMessage()]);
    fix_text_response('false', 500);
}

