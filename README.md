# Enigmatic Legacy Plus — 1.21.1 Fork (Unofficial)

> **This is an unofficial, modified version. I do not own Enigmatic Legacy.**
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
| Modifications in this fork (branch `1.21.1-fork`) | [LukeNightstar](https://github.com/LukeNightstar) |

## Modifications

This branch **modifies** Auviotre's Enigmatic Legacy Plus (1.21.1-1.1.2). The modifications are (marked
"Fork change" in the code):

- Eldritch Amulet: creatures the wearer strikes are slowed, fatigued and weakened for 3 seconds, instead of
  creatures under the wearer's gaze.
- Ring of the Seven Curses: "For the Worthy" mode no longer pulls endermen towards the bearer when
  `minecraft:enderman` is in `neutralWhiteList`; only players track time with/without curses.
- Curse/blessing checks only apply to players and are cached per player for the current tick.
- Korean translation (`ko_kr.json`), and the Eldritch Amulet tooltip updated in the other languages.
- The mod version carries a `-fork` suffix (`1.21.1-1.1.2-fork`) so the jar is not confused with the original.

Every individual change is recorded in the git history of the `1.21.1-fork` branch
(`git log main..1.21.1-fork`). The `main` branch tracks Auviotre's upstream 1.21.1 version unmodified. A Minecraft
26.1.2 port with the same modifications lives on the `26.1.2` branch.

## Requirements

- **Minecraft Version:** 1.21.1
- **NeoForge Version:** 21.1.1 or later
- **Dependencies:** Curios API 1.21.1-9.5.1 or later; Patchouli 1.21.1-93 or later; Caelus API 1.21.1-7.0.1 or later

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
