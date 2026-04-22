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
- multiplayer working between `web + web` and `web + android`
- disconnect triggered when leaving the table screen, not only when the whole app dies
- shared table / round model with host, voting state, reveal, and reset flow
- snapshot-based WebSocket protocol with typed client and server messages
- lobby flow that can prefill and validate player name and table id from navigation / URL

The project now covers most of MVP Stage 1 and Stage 2. The next product goal is Stage 3: improve UI / UX clarity and make the MVP feel intentional instead of merely functional.

## MVP Plan

### Stage 1. Model the Planning Poker domain

Goal: replace the old presence-only model with a real estimation round model.

Status:

- mostly complete

Implemented scope:

- shared table model, round model, deck definition, and round status
- player voting states and host role
- shared client-server actions for join, vote, reveal, and reset
- MVP Fibonacci-style deck plus `?`

Remaining notes:

- observer mode is still intentionally deferred
- table configuration is still minimal

### Stage 2. Implement MVP gameplay

Goal: deliver a complete Planning Poker round.

Status:

- mostly complete

Implemented scope:

- join a specific table
- select a card as a player
- keep cards hidden until reveal
- reveal cards for the table
- reset the round and start the next one

Remaining notes:

- there is still only one real table behind the scenes
- create-table flow and true multi-table server state are still future work

### Stage 3. Improve UX and clarity

Goal: make the MVP pleasant to use, not just technically functional.

Scope:

- show a clearer list of players and voting states
- improve lobby flow and table entry clarity on both Android and Web
- refine card selection UI for phone and browser layouts
- improve empty, reconnect, and error states
- remove remaining temporary / debug-like UX

Definition of done:

- a user can understand what to do next without explanation
- main flow feels coherent on both Android and Web
- table entry via direct URL behaves predictably

## Post-MVP Growth Plan

### Stabilization

- unit tests for validation and game rules
- server tests for WebSocket message handling
- ViewModel tests for the main app flows
- regression protection for key protocol errors and game rules

### Version 1.1

- multiple tables with room codes
- host / moderator privileges beyond current MVP defaults
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
