# Quests & Teams Fixes

A utility mod designed to fix some issues in FTB Quests and FTB Teams, including soft locking in bigger modpacks. Supports both **Forge** and **Fabric**.

## What does this mod fix exactly?

When using the "Flexible" progression mode in FTB Quests, quests can become soft-locked when all tasks are completed before the quest's dependencies are unlocked, displaying the quest being completed at 100% but having a gray checkmark.

## Fixes

- If unlocking a quest checks that a dependent quest already has 100% of its tasks completed, the mod forces the dependent to complete instantly, preventing the soft lock from ever happening.
- For players who are already stuck with gray checkmarks before installing this mod, the fix is applied retroactively on player join.
- This also disables the /ftbquests command, as well as the sidebar button and party creation by 3rd party APIs and mods, for modpacks that use another provider for teams (like OPAC)


## Compiling from Source
```bash
git clone https://github.com/Stalemated/quests_teams_fixes.git
cd quests_teams_fixes
# Build the mod
gradlew.bat build # (Windows)
./gradlew build   # (Linux/macOS)
```

