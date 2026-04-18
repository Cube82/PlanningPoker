# AGENTS.md

## Project Goal

Planning Poker is a Kotlin Multiplatform app for Scrum estimation. The project currently consists of:

- `composeApp` - shared Compose Multiplatform client for Android and Web/Wasm
- `shared` - shared models and communication contracts
- `server` - Ktor backend with WebSockets

The short-term priority is to deliver a small, working MVP first, and only then expand the domain model and UX.

## Current Repository State

The project already has a working multiplayer baseline:

- lobby screen with player name validation
- navigation from lobby to table screen
- WebSocket communication between client and server
- server-side player registration
- shared `GameState` broadcast to connected clients
- typed shared server error contracts in `shared`
- explicit connection state in table UI
- visible join failures instead of silent no-op behavior
- leave-table behavior when the table screen is dismissed

Main gaps compared to the final Planning Poker MVP:

- no estimation round model yet
- no card selection, reveal, or reset flow yet
- no host / moderator round actions yet
- no support for multiple independent tables yet
- no automated tests for domain logic or protocol behavior yet

## Architecture

### `composeApp`

- shared UI and presentation code live in `composeApp/src/commonMain`
- platform-specific client implementations live in `androidMain` and `wasmJsMain`
- dependency injection uses Koin
- current screen flow is `LobbyScreen` -> `TableScreen`

### `shared`

- contains serializable models using `kotlinx.serialization`
- this is the right place for WebSocket contracts, domain types, and shared protocol-level logic

### `server`

- backend is built with Ktor + WebSockets
- game state is kept in memory inside `Game`
- current implementation manages player presence and shared state broadcast

## Current Technical Risks

- `GameState` is still too small for real Planning Poker gameplay
- `GameClientImpl` still owns a single active WebSocket session, which may complicate reconnect and tests later
- disconnect and reconnect behavior is improved, but still lightly tested and mostly verified manually
- some localization files still use temporary ASCII-safe Polish text because of previous encoding issues
- server logic and protocol flow still have no automated tests

## Working Rules For This Repo

- Prefer incremental changes that end in a working vertical slice.
- Keep domain logic outside Composables whenever possible.
- Evolve WebSocket contracts in `shared` first, then implement client and server.
- For domain features, update models and tests first, then UI.
- Do not add new dependencies without a clear reason.
- If functionality is shared by client and server, check whether it should live in `shared` first.

## Preferred Implementation Workflow

1. Define use case and domain state.
2. Extend shared models in `shared`.
3. Implement backend logic in `server`.
4. Add message handling in `composeApp:data`.
5. Wire the ViewModel.
6. Refine Compose UI last.
7. Manually verify Android + Web/Wasm end to end.

## MVP Standard

The first usable version of the app should allow:

- joining a room with a player name
- multiple participants joining the same table
- each player selecting one estimation card
- cards staying hidden until reveal
- reveal triggered by the host
- round reset and next-round start

Everything beyond that is product growth, not an MVP blocker.

## Useful Commands

- Android app: `./gradlew :androidApp:assembleDebug`
- Web/Wasm dev: `./gradlew :composeApp:wasmJsBrowserDevelopmentRun`
- Shared checks: `./gradlew :shared:check`
- Server run: `./gradlew :server:run`
- Full build: `./gradlew build`

## Direction After MVP

- multiple tables and join-by-room-code flow
- host / Scrum Master role
- round history and helper statistics
- reconnect after connection loss
- local or remote session persistence
- better design and mobile/web responsiveness
- automated tests for protocol and game logic

## How To Make Decisions

If there is a choice between:

- shipping the MVP faster
- and building a more generic architecture

prefer the first option, as long as it does not block a sensible refactor later.

Working product first. Generalization later.
