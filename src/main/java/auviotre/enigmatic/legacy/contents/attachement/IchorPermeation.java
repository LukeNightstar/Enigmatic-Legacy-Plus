package auviotre.enigmatic.legacy.contents.attachement;

import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;

public class IchorPermeation implements ValueIOSerializable {
    private boolean infected = false;

    public boolean isInfected() {
        return this.infected;
    }

    public void setInfected(boolean infected) {
        this.infected = infected;
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("IchorPermeation", this.infected);
        return tag;
    }

    public void load(@NotNull CompoundTag tag) {
        this.infected = tag.getBooleanOr("IchorPermeation", false);
    }

    @Override
    public void serialize(ValueOutput output) {
        output.putBoolean("IchorPermeation", this.infected);
    }

    @Override
    public void deserialize(ValueInput input) {
        this.infected = input.getBooleanOr("IchorPermeation", false);
    }
}
