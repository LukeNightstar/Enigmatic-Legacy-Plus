package auviotre.enigmatic.legacy.contents.attachement;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import java.util.UUID;

import net.minecraft.core.UUIDUtil;
public class EnigmaticData implements INBTSerializable<CompoundTag> {
    private @Nullable UUID engineHook;
    private int spellstoneCooldown = 0;
    private boolean magnetRingEffect = false;
    private boolean unlockedNarrator = false;
    private boolean nebulaPower = false;
    private boolean isElytraBoosting = false;
    private boolean isForbiddenCursed = false;
    private boolean isThirstForbiddenCursed = false;
    private long timeWithCurses, timeWithoutCurses;
    private int fireImmunityTimer = 0;
    private int fireImmunityTimerLast = 0;
    private int inBeaconRange = 0;
    private int witherKills = 0;
    private float etherealShield = 0;
    private long etheriumShieldTick = 0;
    private int reviveCooldown = 0;
    private int annihilatingTick = 0;
    private int[] annihilationSweepData = new int[3];

    public UUID getEngineHook() {
        return engineHook;
    }

    public void setEngineHook(UUID engineHook) {
        this.engineHook = engineHook;
    }

    public int getSpellstoneCooldown() {
        return spellstoneCooldown;
    }

    public void setSpellstoneCooldown(int spellstoneCooldown) {
        this.spellstoneCooldown = spellstoneCooldown;
    }

    public boolean isMagnetRingEnable() {
        return magnetRingEffect;
    }

    public void setMagnetRingEnable(boolean enable) {
        this.magnetRingEffect = enable;
    }

    public void toggleMagnetRingEffect() {
        this.magnetRingEffect = !magnetRingEffect;
    }

    public long getTimeWithoutCurses() {
        return this.timeWithoutCurses;
    }

    public void setTimeWithoutCurses(long time) {
        this.timeWithoutCurses = time;
    }

    public boolean getUnlockedNarrator() {
        return this.unlockedNarrator;
    }

    public void setUnlockedNarrator(Boolean unlockedNarrator) {
        this.unlockedNarrator = unlockedNarrator;
    }

    public boolean getNebulaPower() {
        return this.nebulaPower;
    }

    public void setNebulaPower(boolean power) {
        this.nebulaPower = power;
    }

    public boolean isElytraBoosting() {
        return this.isElytraBoosting;
    }

    public void setElytraBoosting(boolean boosting) {
        this.isElytraBoosting = boosting;
    }

    public boolean isForbiddenCursed() {
        return this.isForbiddenCursed;
    }

    public void setForbiddenCursed(boolean forbidden) {
        this.isForbiddenCursed = forbidden;
    }

    public boolean isThirstForbiddenCursed() {
        return this.isThirstForbiddenCursed;
    }

    public void setThirstForbiddenCursed(boolean forbidden) {
        this.isThirstForbiddenCursed = forbidden;
    }

    public boolean isInBeaconRange() {
        return this.inBeaconRange > 0;
    }

    public void setInBeaconRangeTick(int tick) {
        this.inBeaconRange = tick;
    }

    public void InBeaconRangeTick() {
        this.inBeaconRange--;
    }

    public int getWitherKills() {
        return this.witherKills;
    }

    public void setWitherKills(int amount) {
        this.witherKills = amount;
    }

    public float getEtherealShield() {
        return this.etherealShield;
    }

    public void setEtherealShield(float amount) {
        this.etherealShield = amount;
    }

    public long getEtheriumShieldTick() {
        return this.etheriumShieldTick;
    }

    public void setEtheriumShieldTick(long tick) {
        this.etheriumShieldTick = tick;
    }

    public int getFireImmunityTimer() {
        return fireImmunityTimer;
    }

    public void setFireImmunityTimer(int timer) {
        this.fireImmunityTimerLast = this.fireImmunityTimer;
        this.fireImmunityTimer = Math.clamp(timer, 0, getFireImmunityCap());
    }

    public int getFireImmunityTimerLast() {
        return this.fireImmunityTimerLast;
    }

    public int getFireImmunityCap() {
        return 30000;
    }

    public long getTimeWithCurses() {
        return this.timeWithCurses;
    }

    public void setTimeWithCurses(long time) {
        this.timeWithCurses = time;
    }

    public void incrementTimeWithoutCurses() {
        ++this.timeWithoutCurses;
    }

    public void incrementTimeWithCurses() {
        ++this.timeWithCurses;
    }

    public int getReviveCooldown() {
        return this.reviveCooldown;
    }

    public void setReviveCooldown(int reviveCooldown) {
        this.reviveCooldown = reviveCooldown;
    }

    public int getAnnihilatingTick() {
        return this.annihilatingTick;
    }

    public void setAnnihilatingTick(int tick) {
        this.annihilatingTick = tick;
    }

    public int[] getAnnihilationSweepData() {
        return this.annihilationSweepData;
    }

    public void setAnnihilationSweepData(int damage, int tick, int level) {
        this.annihilationSweepData = new int[]{damage, tick, level};
    }

    public CompoundTag save(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        if (this.engineHook != null) {
            tag.store("EngineHook", UUIDUtil.CODEC, engineHook);
        }
        tag.putBoolean("MagnetRingEffect", this.magnetRingEffect);
        tag.putBoolean("UnlockedNarrator", this.unlockedNarrator);
        tag.putBoolean("NebulaPower", this.nebulaPower);
        tag.putBoolean("ElytraBoosting", this.isElytraBoosting);
        tag.putBoolean("isForbiddenCursed", this.isForbiddenCursed);
        tag.putBoolean("isThirstForbiddenCursed", this.isThirstForbiddenCursed);
        tag.putInt("SpellstoneCooldown", this.spellstoneCooldown);
        tag.putInt("FireImmunityTimer", this.fireImmunityTimer);
        tag.putInt("FireImmunityTimerLast", this.fireImmunityTimerLast);
        tag.putInt("InBeaconRangeTick", this.inBeaconRange);
        tag.putInt("WitherKills", this.witherKills);
        tag.putFloat("EtherealShield", this.etherealShield);
        tag.putLong("EtheriumShieldTick", this.etheriumShieldTick);
        tag.putLong("timeWithCurses", this.timeWithCurses);
        tag.putLong("timeWithoutCurses", this.timeWithoutCurses);
        tag.putInt("reviveCooldown", this.reviveCooldown);
        tag.putInt("annihilatingTick", this.annihilatingTick);
        tag.putIntArray("annihilationSweepData", this.annihilationSweepData);
        return tag;
    }

    public void load(HolderLookup.Provider provider, @NotNull CompoundTag tag) {
        if (tag.contains("EngineHook")) this.engineHook = tag.read("EngineHook", UUIDUtil.CODEC).orElse(null);
        this.magnetRingEffect = tag.getBooleanOr("MagnetRingEffect", false);
        this.unlockedNarrator = tag.getBooleanOr("UnlockedNarrator", false);
        this.nebulaPower = tag.getBooleanOr("NebulaPower", false);
        this.isElytraBoosting = tag.getBooleanOr("ElytraBoosting", false);
        this.isForbiddenCursed = tag.getBooleanOr("isForbiddenCursed", false);
        this.isThirstForbiddenCursed = tag.getBooleanOr("isThirstForbiddenCursed", false);
        this.spellstoneCooldown = tag.getIntOr("SpellstoneCooldown", 0);
        this.fireImmunityTimer = tag.getIntOr("FireImmunityTimer", 0);
        this.fireImmunityTimerLast = tag.getIntOr("FireImmunityTimerLast", 0);
        this.inBeaconRange = tag.getIntOr("InBeaconRangeTick", 0);
        this.witherKills = tag.getIntOr("WitherKills", 0);
        this.etherealShield = tag.getFloatOr("EtherealShield", 0.0F);
        this.etheriumShieldTick = tag.getLongOr("EtheriumShieldTick", 0L);
        this.timeWithCurses = tag.getLongOr("timeWithCurses", 0L);
        this.timeWithoutCurses = tag.getLongOr("timeWithoutCurses", 0L);
        this.reviveCooldown = tag.getIntOr("reviveCooldown", 0);
        this.annihilatingTick = tag.getIntOr("annihilatingTick", 0);
        this.annihilationSweepData = tag.getIntArray("annihilationSweepData");
    }

    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        return save(provider);
    }

    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        load(provider, tag);
    }
}
