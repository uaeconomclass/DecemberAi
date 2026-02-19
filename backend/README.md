# Backend Recovery (WordPress-Compatible)

This folder restores the missing backend contract expected by the Android app.

## What is included

`backend/handlers_api/` endpoints:
- `register.php`
- `login.php`
- `userData.php`
- `change_password.php`
- `user_update_level.php`
- `user_update_skill.php`
- `voice_response.php` (legacy compatibility)

Shared bootstrap:
- `bootstrap.php` (SQLite connection, schema migration, seed data, helper methods)

## Why this works

The Android app calls:
`https://<domain>/handlers_api/*.php`

These scripts implement that exact path contract and return response shapes used in:
- `RegisterActivity`
- `MainActivity`
- `ChangePasswordActivity`
- `model/User`
- `model/ChatPage`

## Data store

SQLite file is created automatically on first request in:
- `../data/decemberai.sqlite` relative to `handlers_api/`
- Example in this repo layout: `backend/data/decemberai.sqlite`

Seed data for lessons/practices is inserted automatically if tables are empty.

## Quick local run

From repository root:

```bash
php -S 127.0.0.1:8080 -t backend
```

Then API URLs are available as:
- `http://127.0.0.1:8080/handlers_api/register.php`
- etc.

## Deploy on WordPress hosting

If your site root is WordPress public dir:

1. Upload folder `handlers_api` to WordPress root (same level as `wp-content`), or to any location that maps to `/handlers_api`.
2. Ensure PHP has write access to create sibling `data/` directory near `handlers_api/` (or pre-create it with proper permissions).
3. Verify endpoints with POST requests.

If backend must live directly on existing domain path:
- Put the `handlers_api` directory exactly at the path used by mobile app: `/handlers_api/`.

## Minimal smoke test (curl)

```bash
curl -X POST http://127.0.0.1:8080/handlers_api/register.php \
  -d "name=Test User&email=test@example.com&phone=0000000000&password=12345"
```

Expected response: `true`

```bash
curl -X POST http://127.0.0.1:8080/handlers_api/login.php \
  -d "email=test@example.com&password=12345"
```

Expected response: `true`

```bash
curl -X POST http://127.0.0.1:8080/handlers_api/userData.php \
  -d "email=test@example.com"
```

Expected response: JSON object with fields:
`name`, `phone`, `user_level`, `user_skill`, `openaiApiKey`, `assistantIdForTest`, `userId`, `actualVersionApp`, `howManyWordsToBlocking`, `completed_lessons`, `completed_practices`, `lessons_data`, `practices_data`.

## Notes

- Logging is added with `[FIX]` prefix via PHP `error_log`.
- This restores compatibility for the current Android client contract.
- For production, replace SQLite with MySQL/PostgreSQL and move secrets from DB fields to secure configuration.


