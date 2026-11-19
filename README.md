# No Force Pack – Fabric Mod für Minecraft 1.21.1

Ermöglicht es, **erzwungene Server-Ressourcenpakete** aus dem Ressourcenpaket-HUD zu entfernen.

## Was macht die Mod?

Normalerweise zeigt Minecraft Server-Ressourcenpakete im Ressourcenpaket-Bildschirm als **gesperrt** an – der Entfernen-Button fehlt oder ist deaktiviert. Diese Mod:

1. **Entsperrt den Entfernen-Button** im Ressourcenpaket-HUD für alle Packs (via Mixin)
2. **Tastenkürzel (Standard: R)** – entfernt das Server-Pack sofort per Tastendruck im Spiel
3. Bestätigung per **Action-Bar-Nachricht** im Spiel

## Installation

1. [Fabric Loader](https://fabricmc.net/use/) installieren (1.21.1)
2. [Fabric API](https://modrinth.com/mod/fabric-api) herunterladen
3. Diese Mod-JAR in den `mods`-Ordner legen

## Kompilieren

```bash
./gradlew build
```

Die fertige JAR liegt dann in `build/libs/noforcepack-1.0.0.jar`.

## Tastenbelegung ändern

Einstellungen → Steuerung → **No Force Pack** → „Server-Ressourcenpaket entfernen"

## Wie es funktioniert

- **Mixin** auf `PackListWidget$ResourcePackEntry#isRequired()` → gibt immer `false` zurück, sodass der Entfernen-Button erscheint
- **`ServerPackRemover`** sucht Packs mit dem Präfix `server/` oder `serverresourcepack` und entfernt sie aus der aktiven Liste, danach `reloadResources()`
