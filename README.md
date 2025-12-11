## Who's That Mon?

Whos That Mon? is an Android Kotlin app where the players have to identify Pokémon from its partial reveals and silhouettes. In this README file is the
developer guide, release history, project report, and the deployment notes, everything to the instructors, contributors, and maintainers need.

## Table of contents 

- About the app
- Purpose and Target Audience
- Key Features
- Design and Architecture Considerations
   - High level architecture                                                                                                                           
   - Data and offline strategy
   - Security and privacy
   - Localization and accessibility
   - UI/UX choices
   - Project structure
   - How we used GitHub(isssues,branching and PRs)
   - Tests
   - Release Notes (detailed prototype to current)
   - Contribution guidelines
---

## About the App

Who's That Mon? is a small mobile educational quiz game. The players are shown an outline/silhouetted Pokémon, and they must type in the correct name, once
the correct name is inserted/answered, and the Pokémon is unlocked in the local Pokedex.

---

## Purpose and Target Audience 

- Casual Players: light, single player mobile game experience
- Educational: recognition/memory practice through the gameplay
- Course deliverable: it demonstrates Android app design, authentication, data persistence, and testing.
- Low connectivity users: offline first design allows players with no internet access to play  

---

## Key Features

- Local Pokedex with unlocked Pokémon
- Offline first seeding of master list ( the first 151 monsters)
- Silhouette based guessing game
- Firebase email/password authentication and optional biometrics for quick login
- Account settings: notifications, authentication and biometrics
- Clear architecture enabling testability
- Unit testing for core logic
- Notifications helper for local notification

---

## Design and Architecture Consideration

  ### High level architecture
  
  - Layered Architecture
      - UI: Activities such as the MainActivity, PokedexActivity and Account screens
      - ViewModels: UI state and business logic orchestration
      - Data layer: Room entities + DAOs, plus network client wrapper
      - Repository: single source of truth, network clients and abstract local DB
  - ViewModelFactory for dependency provisioning (testability)
  - Coroutines + Kotlin for asynchronous work

### Data and offline strategy

  - Room Local DB holds Pokémon master list and UnlockedPokemon records.
  - On the first run the repository seeds the DB with the first 151 Pokémon which are fetched from the PokeAPI clients.
  - Gameplay uses the local DB for the main flows network used for the optional detailed information.

  Advantages: 
   - Offline gameplay
   - Fast startup after seeding
   - simplier testing of the core flows

### Security and Privacy
 - Biometric preference is stored with the EncryptedSharedPreference - there are no raw credentials saved in the repo
 - No API keys or sensitive tokens committed.
 - Firebase Auth for user identity, only UID used locally when it's necessary    

### Localization and accessibility
 - String resources used for localization.
 - Runtime language selector (English, Afrikaans, Zulu, Xhosa).
 - UI design with clear contrasts of colours and accessible controls.

### UI/UX choices 
 - Bottom navigation for quick access to Home, Pokedex and Profile.
 - Minimalist layout emphasizing the outline/silhouette of the Pokémon and guess input
 - Toasts and meaningful visual feedback for the input validation and game events.

---

## Project structure (high level)

- app/
  - src/main/java/com/poe/whosthatmon/
    - data/db/ - Room entities such as UnlockedPokemon, AppDatabase and Pokemon.kts
    - data/repository/ - PokemonRepository.kt
    - data/network/ - PokeApiClient.kt wrapper
    - ui/ - Activities, ViewModels, helpers (NotificationHelper, LoginActivity, RegisterActivity)
    - ui/pokedex/ - PokedexActivity, Adapter, ViewModel
  - scr/test/ - JVM unit test (ExampleUnitTest)
  - build.gradle.kts, gradle.properties, gradle wrapper files

---

## How we use GitHub 

Branching and workflow
 - Default branch: master
 - Feature branches: features
 - BugFix branches: fix

Issues and Projects
 - We used GitHub Issues to track bugs, enhancements and tasks
 - GitHub Project (Kanban) for milestone tracking

Code review expectations
 - Run the unit test before opening PR
 - Keep the whole PR description clear and list all the steps to test

---

## Developer setup to build and run 

prerequisites

- Android Studio
- JDK 11+
- Android Studio SDK platform matching compileSDKVersion
- Github/Git

Quick start

1. Clone: https://github.com/VCCT-PROG7314-2025-G3/Whos-That-Mon.git
2. Open in Android Studio and allow for the Gradle to sync
3. Build and run on the emulator or device
4. To generate an APK from the CLI: ./gradlew :app:assembleDebug
   APK: app/build/outputs/apk/debug/app-debug.apk

---

## Tests - what are all included and how to run 

Unit test (JVM test)
- app/scr/test/java/com/poe/whosthatmon/ExampleUnittest.kts

Run test
- run test from Android Studio by right clicking test folder then run

Instrumented test:
- For the UI and the Room integration tests, create "androidTest" instrumentation tests 

---


## Release notes, the history since the prototype

All of the updates are recorded here; each one of the versions highlights the features/innovations added.

Version 1 - Prototype (Initial classroom prototype)
 - There is minimal outline/silhouette view
 - There is basic scoring loop and nest button
 - There is manual text input for answers

Version 2 - Authentication + Biometric 
 - A Firebase Authentication was added (email and password)
 - An optional biometric quick login was intergrated into the login flow
 - A biometric preference persisted using an encrypted storage (EncryptedSharedPreferences)

Version 3
- There is a full local seeding of master list ( the first 151 Pokémon) enabling for offline gameplay play
- Implemented Room DB and Pokemon entity
- Repository pattern introduced to decouple the UI from the data sources
- Pokedex screen listing unlocked the Pokémon 

Version 4 
- The account settings screen with toggle controls for the Biomtrics, Sync and Notifications
- NotificationHelper was added to handle local notifications
- The runtime language selector supports English, Afrikaans, Zulu and Xhosa

Version 5
- Refactored code to better/improve the testability
- Local only tests to enable the CI unit testing without an emulator present
- Added unit tests for the repository DAO NotificationHelper and interactions

Version 6
- UX clean/polish: better/improve the navigation and form validation
- Better/stronger error handling in the repository network operations
- CI/CD: added a CI recommendations and a sample workflow snippet
- Accessibility and localization polish
- Documentation: A comprehensive README and release history

## Innovative Features in the release cycle 

1. Offline first seeding of the first 151 Pokémon
   - Why is it innovative: unlike the other apps that require a network connection for every launch, this application that we have created
     populates the device with the full master list which enables instant, offline gameplay for students and other users in low or no connection
     envioronemts
   - Implementation notes: the repository checks the DB count, if it's empty it will pull entries and inserts, gameplay reads from the local Romm DB.

2. Biometrics enabled a streamlined login with encrypted preference storage
   - Why is it innovative: it improves the user experience while maintaining strong security for a biometric preference. The biometric opt in flag
     is stored securely allowing for quick login while avoiding persistent plain text secrets  

3. Test first improvements and dependency decoupling
   - Why is it innovative: The refactoring to make the repositories and helpers testable allows for automated verification and gives instructors a clear path
     To demonstrate unit testing in android projects 


