# Enigmatic Legacy Plus — Minecraft 26.1.2 Port (Unofficial)

> **This is an unofficial, modified port. I do not own Enigmatic Legacy.**
>
> **Enigmatic Legacy** and all of its code, assets and binaries are the property of their original creator,
> **[Aizistral](https://aizistral.com/)**.
> Official distribution source: **[Aizistral-Studios/Enigmatic-Legacy](https://github.com/Aizistral-Studios/Enigmatic-Legacy)**
>
> This project is based on **[Enigmatic Legacy Plus](https://github.com/Auviotre/Enigmatic-Legacy-Plus)** by
> **[Auviotre](https://github.com/Auviotre)**, an unofficial port of Enigmatic Legacy to Minecraft 1.21.1.

## Credits

| Role | Who |
|---|---|
| Original author and owner of Enigmatic Legacy | [Aizistral](https://aizistral.com/) — [Enigmatic-Legacy](https://github.com/Aizistral-Studios/Enigmatic-Legacy) |
| Unofficial 1.21.1 port (Enigmatic Legacy Plus) | [Auviotre](https://github.com/Auviotre) — [Enigmatic-Legacy-Plus](https://github.com/Auviotre/Enigmatic-Legacy-Plus) |
| Unofficial 26.1.2 port (this repository, branch `26.1.2`) | [LukeNightstar](https://github.com/LukeNightstar) |

## Modifications

This repository **modifies** Auviotre's Enigmatic Legacy Plus (1.21.1-1.1.2). The modifications are:

- Porting the mod from Minecraft 1.21.1 / NeoForge 21.1 to **Minecraft 26.1.2 / NeoForge 26.1.2**.
- Updating dependencies to their 26.1.2 versions (Curios, Patchouli, JEI and compat mods).
- Replacing the **Caelus API** dependency, which is not available for 26.1.2: curio elytra glide through the vanilla
  `GLIDER` component plus a small mixin, and the cape is hidden through a NeoForge render state modifier.
- Any code, asset or data changes required by the above. Notable ones:
  - Wandering trader trades are data driven (`data/enigmaticlegacyplus/villager_trade`) instead of code.
  - Item model overrides are converted to 26.1.2 item model definitions (`assets/enigmaticlegacyplus/items`).
  - The screen darkening shader of the Annihilating Sword / Violence Scroll is replaced by a dark screen overlay,
    because 26.1.2 post effects can no longer take per-frame values.
  - Cursed/blessed/eldritch tooltip colors are tooltip sprites generated from the vanilla tooltip shapes.
  - The Infernal Shield's instant block and axe immunity are expressed through the `BLOCKS_ATTACKS` component.
  - Spellstone table recipes are synced to clients so JEI can show them.
  - The Antique Bag inventory uses NeoForge's transfer API (`ItemStacksResourceHandler`) instead of the removed-soon
    `ItemStackHandler`; bags saved in the old format are still read. Curios, JEI and screen calls deprecated for
    removal are replaced with their current equivalents.
  - The Annihilating Sword keeps an empty `ATTRIBUTE_MODIFIERS` component (its stats are computed per stack), so mods
    that read that component from every item, such as Better Combat, do not crash.
- The data generators (`auviotre.enigmatic.legacy.data`) are not ported and are excluded from the build; the
  generated data that was already committed is used as is.

Every individual change is recorded in the git history of the `26.1.2` branch
(`git log main..26.1.2`). The `main` branch tracks Auviotre's upstream 1.21.1 version unmodified.

## Status

**Work in progress.** The mod builds, and a dedicated server starts and generates a world without errors.
In-game testing (client and gameplay) is still ongoing.

- **Minecraft Version:** 26.1.2
- **NeoForge Version:** 26.1.2
- **Dependencies:** Curios API 15.0.0+26.1.2; Patchouli 26.1-94 (beta)

## License

This project is distributed under the **Enigmatic Legacy License, II Edition**, the license of the original work.
The full, unmodified license text is in [LICENSE.md](LICENSE.md).

In accordance with section IV.3 of that license:

- I do not own Enigmatic Legacy; its owner is [Aizistral](https://aizistral.com/).
- This project is a modified version, and its modifications are listed above.
- This project is open source. Its source may be viewed, redistributed and modified, and any project that copies,
  modifies, incorporates, depends on or extends it must be open source under the same terms.

---

*Original description:* This mod is designed to bring new magical-themed content to the game, mainly of utilitary
nature. New trinkets and powerful relics are restlessly waiting to contribute to your creational and/or destructional
purposes, whatever moral is standing behind them, if any at all...
