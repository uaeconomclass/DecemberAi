<?php
declare(strict_types=1);
require_once __DIR__ . '/bootstrap.php';

fix_log('register.php request start', ['ip' => $_SERVER['REMOTE_ADDR'] ?? 'unknown']);

try {
    $pdo = fix_db();
    $input = fix_require_post(['name', 'email', 'phone', 'password']);

    fix_log('register.php validating email', ['email' => $input['email']]);
    $existing = fix_find_user_by_email($pdo, $input['email']);
    if ($existing) {
        fix_log('register.php user exists', ['email' => $input['email']]);
        fix_text_response('false');
        return;
    }

    $stmt = $pdo->prepare(
        'INSERT INTO users (name, email, phone, password_hash) VALUES (:name, :email, :phone, :password_hash)'
    );
    $stmt->execute([
        ':name' => $input['name'],
        ':email' => $input['email'],
        ':phone' => $input['phone'],
        ':password_hash' => password_hash($input['password'], PASSWORD_BCRYPT),
    ]);

    fix_log('register.php success', ['email' => $input['email']]);
    fix_text_response('true');
} catch (Throwable $e) {
    fix_log('register.php failure', ['error' => $e->getMessage()]);
    fix_text_response('false', 500);
}

