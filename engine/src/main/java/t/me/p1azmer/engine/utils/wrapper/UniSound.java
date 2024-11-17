package t.me.p1azmer.engine.utils.wrapper;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import t.me.p1azmer.engine.api.config.JOption;
import t.me.p1azmer.engine.api.config.JYML;

import java.util.Optional;

public class UniSound {

    private final String soundName;
    private final Sound soundType;
    private final float volume;
    private final float pitch;

    public UniSound(@NotNull String soundName, @Nullable Sound soundType, float volume, float pitch) {
        this.soundName = soundName;
        this.soundType = soundType;
        this.volume = volume;
        this.pitch = pitch;
    }

    @NotNull
    public static UniSound of(@NotNull Sound sound) {
        return of(sound, 0.8F);
    }

    @NotNull
    public static UniSound of(@NotNull Sound sound, float volume) {
        return of(sound, volume, 1F);
    }

    @NotNull
    public static UniSound of(@NotNull Sound sound, float volume, float pitch) {
        return new UniSound(sound.name(), sound, volume, pitch);
    }

    @NotNull
    public static UniSound read(@NotNull JYML cfg, @NotNull String path) {
        String soundName = JOption.create(path + ".Name", "null",
                "Sound name. You can use Spigot sound names, or ones from your resource pack.",
                "Spigot Sounds: https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Sound.html"
        ).read(cfg);

        float volume = JOption.create(path + ".Volume", 0.8D,
                "Sound volume. From 0.0 to 1.0."
        ).read(cfg).floatValue();

        float pitch = JOption.create(path + ".Pitch", 1D,
                "Sound speed. From 0.5 to 2.0"
        ).read(cfg).floatValue();

        Sound soundType = Optional.of(Sound.valueOf(soundName)).orElse(null);

        return new UniSound(soundName, soundType, volume, pitch);
    }

    public void write(@NotNull JYML cfg, @NotNull String path) {
        cfg.set(path + ".Name", this.getSoundName());
        cfg.set(path + ".Volume", this.getVolume());
        cfg.set(path + ".Pitch", this.getPitch());
    }

    public boolean isEmpty() {
        return this.getVolume() <= 0F || this.getSoundName().isEmpty();
    }

    public void play(@NotNull Player player) {
        if (this.isEmpty()) return;

        Location location = player.getLocation();
        if (this.getSoundType() == null) {
            player.playSound(location, this.getSoundName(), this.getVolume(), this.getPitch());
        } else {
            player.playSound(location, this.getSoundType(), this.getVolume(), this.getPitch());
        }
    }

    public void play(@NotNull Location location) {
        if (this.isEmpty()) return;

        World world = location.getWorld();
        if (world == null) return;

        if (this.getSoundType() == null) {
            world.playSound(location, this.getSoundName(), this.getVolume(), this.getPitch());
        } else {
            world.playSound(location, this.getSoundType(), this.getVolume(), this.getPitch());
        }
    }

    @NotNull
    public String getSoundName() {
        return soundName;
    }

    @Nullable
    public Sound getSoundType() {
        return soundType;
    }

    public float getVolume() {
        return volume;
    }

    public float getPitch() {
        return pitch;
    }
}