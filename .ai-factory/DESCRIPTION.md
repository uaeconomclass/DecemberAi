# Project: DecemberAi

## Overview
DecemberAi is a native Android mobile app built with Java and Gradle. The app contains authentication screens, tab/fragments-based navigation, chat flows, lessons/practice modules, and integrations with remote APIs.

## Core Features
- User entry flow: login/register, profile/account, password change
- Learning modules: lessons and practice sections with list adapters
- Chat flow: chat page and message rendering with API request manager
- Rich Android UI resources: custom drawables, animations, and localized strings (`values-ru`, `values-uk`)

## Tech Stack
- **Language:** Java 8 (Android)
- **Build system:** Gradle (Android plugin 8.2.0)
- **Framework/UI:** Android SDK, AndroidX, Material Components
- **Networking:** OkHttp 4.12.0
- **Image loading:** Glide 4.12.0
- **Tests:** JUnit 4, AndroidX instrumentation tests

## Architecture Notes
- Single Android application module (`:app`)
- UI organized around Activities + Fragments
- Data/view binding is adapter-driven for list views (`adapter/*`)
- API interaction encapsulated in model-level manager class (`ApiRequestManager`)

## Non-Functional Requirements
- Logging: centralize through Android `Log` wrappers and disable verbose logs in release
- Error handling: map network failures to user-friendly UI states
- Security:
  - Avoid deprecated storage permissions for new Android targets
  - Keep API keys/tokens out of source (use `local.properties` / CI secrets)
  - Validate all remote payloads before rendering in UI
