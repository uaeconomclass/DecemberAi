<?php
declare(strict_types=1);

const FIX_DATA_DIR = __DIR__ . '/../data';
const FIX_DB_PATH = FIX_DATA_DIR . '/decemberai.sqlite';

function fix_log(string $message, array $context = []): void
{
    $suffix = $context ? ' ' . json_encode($context, JSON_UNESCAPED_UNICODE | JSON_UNESCAPED_SLASHES) : '';
    error_log('[FIX] ' . $message . $suffix);
}

function fix_json_response(array $payload, int $status = 200): void
{
    http_response_code($status);
    header('Content-Type: application/json; charset=utf-8');
    echo json_encode($payload, JSON_UNESCAPED_UNICODE | JSON_UNESCAPED_SLASHES);
}

function fix_text_response(string $payload, int $status = 200): void
{
    http_response_code($status);
    header('Content-Type: text/plain; charset=utf-8');
    echo $payload;
}

function fix_post(string $key, ?string $default = null): ?string
{
    return isset($_POST[$key]) ? trim((string)$_POST[$key]) : $default;
}

function fix_require_post(array $keys): array
{
    $values = [];
    foreach ($keys as $key) {
        $value = fix_post($key);
        if ($value === null || $value === '') {
            throw new InvalidArgumentException("Missing required field: {$key}");
        }
        $values[$key] = $value;
    }
    return $values;
}

function fix_db(): PDO
{
    static $pdo = null;
    if ($pdo instanceof PDO) {
        return $pdo;
    }

    if (!is_dir(FIX_DATA_DIR) && !mkdir(FIX_DATA_DIR, 0777, true) && !is_dir(FIX_DATA_DIR)) {
        throw new RuntimeException('Unable to create data directory');
    }

    $pdo = new PDO('sqlite:' . FIX_DB_PATH);
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    $pdo->setAttribute(PDO::ATTR_DEFAULT_FETCH_MODE, PDO::FETCH_ASSOC);

    fix_migrate($pdo);
    fix_seed($pdo);

    return $pdo;
}

function fix_migrate(PDO $pdo): void
{
    $pdo->exec(
        'CREATE TABLE IF NOT EXISTS users (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            name TEXT NOT NULL,
            email TEXT NOT NULL UNIQUE,
            phone TEXT NOT NULL,
            password_hash TEXT NOT NULL,
            user_level TEXT NOT NULL DEFAULT "A1",
            user_skill INTEGER NOT NULL DEFAULT 0,
            openai_api_key TEXT NOT NULL DEFAULT "",
            assistant_id_for_test TEXT NOT NULL DEFAULT "asst_test_placeholder",
            actual_version_app TEXT NOT NULL DEFAULT "1",
            how_many_words_to_blocking INTEGER NOT NULL DEFAULT 60,
            created_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
            updated_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP
        )'
    );

    $pdo->exec(
        'CREATE TABLE IF NOT EXISTS lessons (
            lesson_id INTEGER PRIMARY KEY,
            title TEXT NOT NULL,
            img TEXT NOT NULL,
            level TEXT NOT NULL,
            color_fon TEXT NOT NULL,
            color_text TEXT NOT NULL,
            assistant_id TEXT NOT NULL
        )'
    );

    $pdo->exec(
        'CREATE TABLE IF NOT EXISTS practices (
            practice_id INTEGER PRIMARY KEY,
            title TEXT NOT NULL,
            img TEXT NOT NULL,
            level TEXT NOT NULL,
            color_fon TEXT NOT NULL,
            color_text TEXT NOT NULL,
            assistant_id TEXT NOT NULL
        )'
    );

    $pdo->exec(
        'CREATE TABLE IF NOT EXISTS user_completed_lessons (
            user_id INTEGER NOT NULL,
            lesson_id INTEGER NOT NULL,
            PRIMARY KEY (user_id, lesson_id)
        )'
    );

    $pdo->exec(
        'CREATE TABLE IF NOT EXISTS user_completed_practices (
            user_id INTEGER NOT NULL,
            practice_id INTEGER NOT NULL,
            PRIMARY KEY (user_id, practice_id)
        )'
    );
}

function fix_seed(PDO $pdo): void
{
    $lessonCount = (int)$pdo->query('SELECT COUNT(*) FROM lessons')->fetchColumn();
    if ($lessonCount === 0) {
        $stmt = $pdo->prepare(
            'INSERT INTO lessons (lesson_id, title, img, level, color_fon, color_text, assistant_id)
             VALUES (:id, :title, :img, :level, :fon, :text, :assistant)'
        );
        $rows = [
            [1, 'Coffee Talk', 'img_cofemania', 'A1', '#f8f1ab', '#8e3a09', 'asst_lesson_1'],
            [2, 'Friends Talk', 'img_razgovor_s_druziami', 'A2', '#d0e2c7', '#4F564C', 'asst_lesson_2'],
            [3, 'Business Intro', 'img_301', 'B1', '#424345', '#ffffff', 'asst_lesson_3'],
        ];
        foreach ($rows as [$id, $title, $img, $level, $fon, $text, $assistant]) {
            $stmt->execute([
                ':id' => $id,
                ':title' => $title,
                ':img' => $img,
                ':level' => $level,
                ':fon' => $fon,
                ':text' => $text,
                ':assistant' => $assistant,
            ]);
        }
    }

    $practiceCount = (int)$pdo->query('SELECT COUNT(*) FROM practices')->fetchColumn();
    if ($practiceCount === 0) {
        $stmt = $pdo->prepare(
            'INSERT INTO practices (practice_id, title, img, level, color_fon, color_text, assistant_id)
             VALUES (:id, :title, :img, :level, :fon, :text, :assistant)'
        );
        $rows = [
            [11, 'Daily Speaking', 'img_301', 'A1', '#f8f1ab', '#8e3a09', 'asst_practice_11'],
            [12, 'Travel Dialog', 'img_301', 'A2', '#d0e2c7', '#4F564C', 'asst_practice_12'],
            [13, 'Work Meeting', 'img_301', 'B1', '#424345', '#ffffff', 'asst_practice_13'],
        ];
        foreach ($rows as [$id, $title, $img, $level, $fon, $text, $assistant]) {
            $stmt->execute([
                ':id' => $id,
                ':title' => $title,
                ':img' => $img,
                ':level' => $level,
                ':fon' => $fon,
                ':text' => $text,
                ':assistant' => $assistant,
            ]);
        }
    }
}

function fix_find_user_by_email(PDO $pdo, string $email): ?array
{
    $stmt = $pdo->prepare('SELECT * FROM users WHERE email = :email LIMIT 1');
    $stmt->execute([':email' => $email]);
    $row = $stmt->fetch();
    return $row ?: null;
}

function fix_verify_password(string $plain, string $hash): bool
{
    if (password_verify($plain, $hash)) {
        return true;
    }
    return hash_equals($hash, $plain);
}

