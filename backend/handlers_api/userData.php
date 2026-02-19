<?php
declare(strict_types=1);
require_once __DIR__ . '/bootstrap.php';

fix_log('userData.php request start');

try {
    $pdo = fix_db();
    $input = fix_require_post(['email']);
    $user = fix_find_user_by_email($pdo, $input['email']);

    if (!$user) {
        fix_log('userData.php user not found', ['email' => $input['email']]);
        fix_json_response(['error' => 'user_not_found'], 404);
        return;
    }

    $completedLessonsStmt = $pdo->prepare('SELECT lesson_id FROM user_completed_lessons WHERE user_id = :id ORDER BY lesson_id ASC');
    $completedLessonsStmt->execute([':id' => $user['id']]);
    $completedLessons = array_map('intval', array_column($completedLessonsStmt->fetchAll(), 'lesson_id'));

    $completedPracticesStmt = $pdo->prepare('SELECT practice_id FROM user_completed_practices WHERE user_id = :id ORDER BY practice_id ASC');
    $completedPracticesStmt->execute([':id' => $user['id']]);
    $completedPractices = array_map('intval', array_column($completedPracticesStmt->fetchAll(), 'practice_id'));

    $lessonsRaw = $pdo->query('SELECT lesson_id, title, img, level, color_fon, color_text, assistant_id FROM lessons ORDER BY lesson_id ASC')->fetchAll();
    $lessonsData = array_map(static function (array $row): array {
        return [
            'lessonId' => (int)$row['lesson_id'],
            'title' => (string)$row['title'],
            'img' => (string)$row['img'],
            'level' => (string)$row['level'],
            'colorFon' => (string)$row['color_fon'],
            'colorText' => (string)$row['color_text'],
            'assistantId' => (string)$row['assistant_id'],
        ];
    }, $lessonsRaw);

    $practicesRaw = $pdo->query('SELECT practice_id, title, img, level, color_fon, color_text, assistant_id FROM practices ORDER BY practice_id ASC')->fetchAll();
    $practicesData = array_map(static function (array $row): array {
        return [
            'practiceId' => (int)$row['practice_id'],
            'title' => (string)$row['title'],
            'img' => (string)$row['img'],
            'level' => (string)$row['level'],
            'colorFon' => (string)$row['color_fon'],
            'colorText' => (string)$row['color_text'],
            'assistantId' => (string)$row['assistant_id'],
        ];
    }, $practicesRaw);

    $response = [
        'name' => (string)$user['name'],
        'phone' => (string)$user['phone'],
        'user_level' => (string)$user['user_level'],
        'user_skill' => (int)$user['user_skill'],
        'openaiApiKey' => (string)$user['openai_api_key'],
        'assistantIdForTest' => (string)$user['assistant_id_for_test'],
        'userId' => (int)$user['id'],
        'actualVersionApp' => (string)$user['actual_version_app'],
        'howManyWordsToBlocking' => (int)$user['how_many_words_to_blocking'],
        'completed_lessons' => $completedLessons,
        'completed_practices' => $completedPractices,
        'lessons_data' => $lessonsData,
        'practices_data' => $practicesData,
    ];

    fix_log('userData.php success', ['user_id' => $user['id']]);
    fix_json_response($response);
} catch (Throwable $e) {
    fix_log('userData.php failure', ['error' => $e->getMessage()]);
    fix_json_response(['error' => 'internal_error'], 500);
}

