# Roadmap

## Current State

The project now has:

- `androidApp`, `composeApp`, `shared`, and `server` modules
- a working Android app entry module separated from KMP library modules
- updated Kotlin / Compose Multiplatform / Ktor / AndroidX dependency set that builds again
- browser navigation binding updated to current Compose Multiplatform API
- platform-aware backend WebSocket configuration for Android emulator and Web
- localized UI strings moved into Compose Multiplatform resources for core lobby/table screens
- typed validation and server error handling instead of raw UI strings in key flows
- explicit connection states in the table screen
- visible join failures in UI instead of silent no-op behavior
- shared-session multiplayer working between `web + web` and `web + android`
- disconnect triggered when leaving the table screen, not only when the whole app dies

The project still does not implement actual Planning Poker gameplay. The next product goal after the clean baseline commit is to move from shared session presence to real round logic.

## MVP Plan

### Stage 1. Model the Planning Poker domain

Goal: replace the current `GameState(players)` model with a real estimation round model.

Scope:

- introduce table model, round model, deck model, and round status
- add player states such as joined, voted, revealed, and observer
- extend client-server messages to support game actions
- define the MVP deck, for example Fibonacci plus `?`

Definition of done:

- `shared` contains the full MVP contract
- client and server use the same models without local workarounds

### Stage 2. Implement MVP gameplay

Goal: deliver a complete Planning Poker round.

Scope:

- create or choose a table
- join a specific table
- select a card as a player
- keep cards hidden until reveal
- reveal cards as the host
- reset the round

Definition of done:

- 2+ clients can join the same session
- every participant can vote
- reveal shows the result to everyone
- reset clears the votes and starts the next round

### Stage 3. Improve UX and clarity

Goal: make the MVP pleasant to use, not just technically functional.

Scope:

- show a readable list of players and voting statuses
- build a card selection UI that works on phone and browser layouts
- improve error, empty, and reconnect states
- remove temporary debug-like controls from the table screen

Definition of done:

- a user can understand what to do next without explanation
- the main flow does not depend on temporary debug buttons

### Stage 4. Add tests and stabilization

Goal: protect the most important behavior from regressions.

Scope:

- unit tests for validation and game rules
- server tests for WebSocket message handling
- ViewModel tests for the main app flows

Definition of done:

- key game rules are covered by tests
- main protocol errors are detected automatically

## Suggested Next Tasks After Initial Commit

1. Replace `GameState(players)` with a shared table / round model in `shared`.
2. Define MVP actions such as vote, reveal, and reset in shared client-server contracts.
3. Refactor server `Game` from participant registry into real table-round logic.
4. Rebuild `TableViewModel` around full gameplay state instead of only connection + player list.
5. Replace temporary table UI with player cards, voting states, and host actions.
6. Add first automated tests for shared rules and server protocol handling.

## Post-MVP Growth Plan

### Version 1.1

- multiple tables with room codes
- host / moderator privileges
- rename and rejoin flow
- better reconnect handling

### Version 1.2

- round history
- helper statistics such as average, median, and spread
- observer mode without voting
- configurable card decks

### Version 1.3

- session persistence
- backend deployment
- lightweight onboarding for sharing the app with a team
- telemetry and error reporting

## Things To Skip For Now

- user accounts and authentication
- a database
- complex permissions and roles beyond MVP needs
- a large design system
- offline mode

These can be useful later, but they would slow down delivery of the first complete version.
