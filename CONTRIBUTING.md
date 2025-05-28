# Contributing Guide - Android Internship 2025 - Final Project

## 🔀 Branching Strategy

We use the following main branches:

| Branch    | Purpose                          |
|-----------|----------------------------------|
| `main`    | Production-ready, stable release |
| `develop` | Ongoing integration and staging  |

### ✅ Branch Naming Convention

```bash
<type>/<feature-name>/<short-description>
# or if not divided according to the module/feature:
<type>/<short-description>
```

#### Branch Types:
| Type       | Use case                           | Example Branch Name                     |
|------------|------------------------------------|-----------------------------------------|
| `feature`  | New feature                        | `feature/auth/login-screen`             |
| `bugfix`   | Fixing a bug                       | `bugfix/player/fix-crash-on-pause`      |
| `refactor` | Code improvement, no logic changes | `refactor/home/cleanup-fragment-logic`  |
| `setup`    | Initial setup/configuration        | `setup/firebase-init-config`            |
| `docs`     | Documentation updates              | `docs/update-pr-template`               |
| `test`     | Adding/updating tests              | `test/payment/add-viewmodel-unit-tests` |
| `hotfix`   | Emergency fix from `main`          | `hotfix/auth/urgent-token-expiry-fix`   |

### 📦 Development Workflow

1. Branch off from develop
2. Work on your branch
3. Commit with proper message format
4. Push and create a PR to develop
5. Wait for review and merge

### ✍️ Commit Message Convention

```bass
<type>: <short summary>
```

| Type       | Meaning                                             | Example                                     |
|------------|-----------------------------------------------------|---------------------------------------------|
| `feat`     | A new feature                                       | `feat: add user profile screen`             |
| `fix`      | A bug fix                                           | `fix: crash on payment method selection`    |
| `refactor` | Code change that doesn’t fix a bug or add a feature | `refactor: move logic to use-case`          |
| `chore`    | Maintenance tasks (e.g., configs, deps)             | `chore: update Gradle wrapper`              |
| `docs`     | Documentation-only changes                          | `docs: update README with usage guide`      |
| `test`     | Adding or updating tests                            | `test: add unit tests for PaymentViewModel` |
| `style`    | Code formatting, white-space, lint fixes            | `style: reformat with ktlint`               |
| `perf`     | Code that improves performance                      | `perf: optimize RecyclerView scroll`        |
| `build`    | Changes to build system/config files                | `build: add proguard rules for release`     |
| `ci`       | CI configuration and scripts                        | `ci: add GitHub Actions workflow`           |
| `revert`   | Reverts a previous commit                           | `revert: fix login crash`                   |
