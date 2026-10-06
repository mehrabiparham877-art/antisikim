Smooth Anti-AFK (Fabric, Minecraft 26.2, client-only)

Keys: Z = settings screen, X = toggle on/off (rebind in Controls)

Build the jar (no PC setup needed):
1. Make a free GitHub account, create a new repo, upload ALL these files (unzipped, keep .github folder).
2. Go to the Actions tab -> "build" -> wait for green check.
3. Open the run -> Artifacts -> download "antiafk-jar". Use the file WITHOUT "-sources" in its name.
4. Put the jar in .minecraft/mods together with Fabric API (26.2).

Or locally: install JDK 25 + Gradle 9.3, then run: gradle build   (jar appears in build/libs)
