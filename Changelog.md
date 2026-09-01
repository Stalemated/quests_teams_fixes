# Changelog

## 1.0.1+1.20.1

### New features

- Made the FTB mods as well as Architectury soft dependent
  - This allows for correct crash handling for modpacks downloaded from Modrinth, where the FTB mods are not present
- Added S-Lib as a dependency

## 1.0.0+1.20.1

### Features

- Fixed quests soft locking in FTB Quests when tasks are completed before dependencies are unlocked
- The mod retroactively fixes existing gray checkmark soft locks on player join.
- Disabled the /ftbquests command
- Disabled FTB sidebar buttons
- Disabled FTB party creation by other APIs/mods in favor of other team solutions (like OPAC)