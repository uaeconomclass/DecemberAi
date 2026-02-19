<?php
declare(strict_types=1);
require_once __DIR__ . '/bootstrap.php';

fix_log('user_update_skill.php request start');

try {
    $pdo = fix_db();
    $input = fix_require_post(['email', 'id_chat', 'schetchik_slov']);

    $user = fix_find_user_by_email($pdo, $input['email']);
    if (!$user) {
        fix_log('user_update_skill.php user not found', ['email' => $input['email']]);
        fix_text_response('false');
        return;
    }

    $chatId = (int)$input['id_chat'];
    $skillDelta = max(0, (int)$input['schetchik_slov']);

    $updateSkill = $pdo->prepare('UPDATE users SET user_skill = user_skill + :delta, updated_at = CURRENT_TIMESTAMP WHERE id = :id');
    $updateSkill->execute([
        ':delta' => $skillDelta,
        ':id' => $user['id'],
    ]);

    $stmtLesson = $pdo->prepare('SELECT COUNT(*) FROM lessons WHERE lesson_id = :id');
    $stmtLesson->execute([':id' => $chatId]);
    $isLesson = ((int)$stmtLesson->fetchColumn()) > 0;

    if ($isLesson) {
        $upsertLesson = $pdo->prepare(
            'INSERT OR IGNORE INTO user_completed_lessons (user_id, lesson_id) VALUES (:user_id, :lesson_id)'
        );
        $upsertLesson->execute([
            ':user_id' => $user['id'],
            ':lesson_id' => $chatId,
        ]);
    } else {
        $upsertPractice = $pdo->prepare(
            'INSERT OR IGNORE INTO user_completed_practices (user_id, practice_id) VALUES (:user_id, :practice_id)'
        );
        $upsertPractice->execute([
            ':user_id' => $user['id'],
            ':practice_id' => $chatId,
        ]);
    }

    fix_log('user_update_skill.php success', [
        'user_id' => $user['id'],
        'chat_id' => $chatId,
        'skill_delta' => $skillDelta,
        'is_lesson' => $isLesson,
    ]);
    fix_text_response('true');
} catch (Throwable $e) {
    fix_log('user_update_skill.php failure', ['error' => $e->getMessage()]);
    fix_text_response('false', 500);
}
