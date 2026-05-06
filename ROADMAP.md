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
- explicit connection states in the table top bar
- visible join failures in UI instead of silent no-op behavior
- multiplayer working between `web + web` and `web + android`
- disconnect triggered when leaving the table screen, not only when the whole app dies
- shared table / round model with host, voting state, card unselect, missed-vote state, reveal, and reset flow
- snapshot-based WebSocket protocol with typed client and server messages
- lobby flow that can prefill and validate player name and table id from navigation / URL
- responsive table UI for phone and browser widths
- reusable card and player-list UI with hidden, selected, disabled, focused, hovered, revealed, and missed-vote states
- light / dark theme colors, Inter font, Material icons, and third-party asset notices
- feature-local preview fixtures for reusable UI states

The project now covers most of MVP Stage 1 and Stage 2. Stage 3 is partly complete: the table UI is much more intentional, while error, empty, and recovery flows still need attention.

## MVP Plan

### Stage 1. Model the Planning Poker domain

Goal: replace the old presence-only model with a real estimation round model.

Status:

- mostly complete

Implemented scope:

- shared table model, round model, deck definition, and round status
- player voting states and host role
- shared client-server actions for join, vote, reveal, and reset
- explicit unselect-card action
- MVP Fibonacci-style deck plus `?`
- public vote state distinguishes not voted, voted hidden, revealed, and missed vote after reveal

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
- unselect the selected card by clicking it again
- keep cards hidden until reveal
- reveal cards for the table
- show players who missed the vote after reveal
- reset the round and start the next one

Remaining notes:

- there is still only one real table behind the scenes
- create-table flow and true multi-table server state are still future work

### Stage 3. Improve UX and clarity

Goal: make the MVP pleasant to use, not just technically functional.

Status:

- in progress

Scope:

- show a clearer list of players and voting states
- improve lobby flow and table entry clarity on both Android and Web
- refine card selection UI for phone and browser layouts
- improve empty, reconnect, and error states
- remove remaining temporary / debug-like UX

Implemented scope:

- responsive table layout with different width limits for player list and card picker
- player list with current-player and host metadata
- small card indicators for hidden votes, revealed votes, missed votes, and waiting players
- large interactive planning cards with selected, disabled, hover, focus, and pressed states
- table top bar with table name and localized connection state
- localized participant count with plural resources
- dark / light theme card colors and app typography
- feature-local preview fixtures for table UI states

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
