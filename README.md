# DecemberAi

Android-приложение (Java, XML, Gradle) для разговорной практики с AI-ассистентом, пользовательскими уроками/практиками и базовой системой аккаунта.

## Что есть в проекте
- Регистрация и вход пользователя (`RegisterActivity`)
- Авто-логин через `SharedPreferences` (`MainActivity`)
- Главный экран с подборкой уроков (`HomeFragment`)
- Экран практик (`PracticeFragment`)
- Профиль пользователя и смена пароля (`AccountFragment`, `ChangePasswordActivity`)
- Входной тест уровня (`TesterUserActivity`)
- Голосовой чат с OpenAI Assistants API (`model/ChatPage`)
- Экран завершения диалога с обновлением прогресса (`PageFinishChat`)

## Технологический стек
- Язык: Java 8
- Android SDK: `compileSdk 33`, `targetSdk 33`, `minSdk 21`
- UI: AndroidX, Material Components, ConstraintLayout
- Сеть: `HttpURLConnection`, `OkHttp 4.12.0`
- Медиа/ресурсы: Glide, MediaRecorder, большое количество drawable-ассетов
- Сборка: AGP `8.2.0`, Gradle wrapper

## Архитектура (фактически)
Проект построен как single-module Android app (`:app`) с активити + фрагментами и глобальным in-memory стейтом в `User`.

### Ключевые компоненты
- `MainActivity`
  - Проверяет локальную авторизацию (`email/password` в `UserPreferences`)
  - Тянет профиль и контент пользователя с backend
  - Инициализирует bottom navigation и фрагменты
- `model/User`
  - Глобальный статический стейт приложения (уровень, скилл, списки уроков/практик, api key)
  - Методы обновления уровня/скилла на backend
- `model/ChatPage`
  - Запись голоса, upload файла, transcription
  - Вызовы OpenAI Assistants API (`threads`, `messages`, `runs`, poll статуса)
  - Озвучка ответа (`/v1/audio/speech`)
- `adapter/*`
  - RecyclerView-адаптеры уроков, практик, home и сообщений

## User flow
1. Старт приложения -> `MainActivity`
2. Если нет валидных учетных данных -> `RegisterActivity`
3. После логина/регистрации -> загрузка пользовательских данных и учебного контента
4. Если первый запуск -> `TesterUserActivity`
5. Пользователь открывает урок/практику -> `ChatPage`
6. По завершению диалога -> `PageFinishChat`, обновление уровня/скилла

## Сетевые интеграции
### Backend (`yourtalker.com`)
Используются endpoint'ы:
- `handlers_api/register.php`
- `handlers_api/login.php`
- `handlers_api/userData.php`
- `handlers_api/change_password.php`
- `handlers_api/user_update_level.php`
- `handlers_api/user_update_skill.php`
- `handlers_api/voice_response.php`
- `handlers_api/upload_user_voice.php`

### OpenAI API
В `ChatPage` используются:
- `POST /v1/threads`
- `POST /v1/threads/{threadId}/messages`
- `POST /v1/threads/{threadId}/runs`
- `GET /v1/threads/{threadId}/runs/{runId}`
- `GET /v1/threads/{threadId}/messages`
- `POST /v1/audio/transcriptions`
- `POST /v1/audio/speech`

Ключ OpenAI приходит с backend в `userData.php` и сохраняется в `User.openaiApiKey`.

## Локальное хранение
`SharedPreferences("UserPreferences")`:
- `email`, `password`
- `userViewPageTesterUserActivity`
- `errorUserUpdateSkill`, `errorUserUpdateSchetchikSlov`

## Как запустить
### Android Studio
1. Открыть папку проекта.
2. Дождаться sync Gradle.
3. Запустить `app` на эмуляторе/устройстве (Android 5.0+).

### CLI
```bash
./gradlew assembleDebug
```
(на Windows: `gradlew.bat assembleDebug`)

## Текущее состояние кода
Проект рабочий по сценарию MVP, но с выраженным technical debt.

### Основные риски
- Сильная связность и глобальный mutable state (`User` static fields)
- Сетевая логика размазана по Activity/Fragment
- Дублирование HTTP-кода
- Хранение пароля в `SharedPreferences` в открытом виде
- Закомментированные/устаревшие куски логики
- Часть UI/комментариев и строк имеет проблемы с кодировкой

### Что стоит сделать первым
1. Вынести сетевой слой в отдельные репозитории/сервисы.
2. Перейти на безопасное хранение токенов/сессии (EncryptedSharedPreferences).
3. Убрать хранение plaintext-пароля.
4. Ввести ViewModel + state holder (минимум для `MainActivity` и `ChatPage`).
5. Добавить базовые unit/instrumentation тесты на auth и chat flow.

## Структура каталогов
- `app/src/main/java/com/example/decemberai/` - Activity/Fragment/adapter/model
- `app/src/main/res/layout/` - XML разметки экранов и элементов списков
- `app/src/main/res/drawable/` - визуальные ассеты
- `app/src/main/res/values*` - строки/темы/локализация

## Примечание
Документация отражает текущее фактическое состояние репозитория и может отличаться от изначального архитектурного замысла.


