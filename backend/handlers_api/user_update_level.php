<?php
declare(strict_types=1);
require_once __DIR__ . '/bootstrap.php';

fix_log('user_update_level.php request start');

try {
    $pdo = fix_db();
    $input = fix_require_post(['email', 'userLevel']);

    $user = fix_find_user_by_email($pdo, $input['email']);
    if (!$user) {
        fix_log('user_update_level.php user not found', ['email' => $input['email']]);
        fix_text_response('false');
        return;
    }

    $stmt = $pdo->prepare('UPDATE users SET user_level = :level, updated_at = CURRENT_TIMESTAMP WHERE id = :id');
    $stmt->execute([
        ':level' => $input['userLevel'],
        ':id' => $user['id'],
    ]);

    fix_log('user_update_level.php success', ['user_id' => $user['id'], 'level' => $input['userLevel']]);
    fix_text_response('true');
} catch (Throwable $e) {
    fix_log('user_update_level.php failure', ['error' => $e->getMessage()]);
    fix_text_response('false', 500);
}

