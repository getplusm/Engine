package t.me.p1azmer.engine.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.generator.WorldInfo;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import t.me.p1azmer.engine.Version;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@UtilityClass
public class BukkitThing {

    @NotNull
    public List<String> worldNames() {
        return Bukkit.getServer().getWorlds().stream().map(WorldInfo::getName).toList();
    }

    @Nullable
    public <T extends Keyed> T fromRegistry(@NotNull Registry<T> registry, @NotNull String key) {
        try {
            NamespacedKey namespacedKey = NamespacedKey.minecraft(key.toLowerCase());
            return registry.get(namespacedKey);
        } catch (IllegalArgumentException exception) {
            return null;
        }
    }

    @NotNull
    public <T extends Keyed> Set<T> allFromRegistry(@NotNull Registry<T> registry) {
        if (Version.isBehind(Version.V1_20_R2)) {
            return StreamSupport.stream(registry.spliterator(), false).collect(Collectors.toSet());
        }
        return registry.stream().collect(Collectors.toSet());
    }

    @NotNull
    public <T extends Keyed> List<String> getNames(@NotNull Registry<T> registry) {
        if (Version.isBehind(Version.V1_20_R2)) {
            return StreamSupport.stream(registry.spliterator(), false).map(BukkitThing::toString).toList();
        }
        return registry.stream().map(BukkitThing::toString).toList();
    }

    @NotNull
    public String toString(@NotNull Keyed keyed) {
        return keyed.getKey().getKey();
    }

    @Nullable
    public Material getMaterial(@NotNull String name) {
        return fromRegistry(Registry.MATERIAL, name);
    }

    @NotNull
    public Set<Material> getMaterials() {
        return allFromRegistry(Registry.MATERIAL);
    }

    @NotNull
    public Set<Enchantment> getEnchantments() {
        return allFromRegistry(Registry.ENCHANTMENT);
    }

    @Nullable
    public Enchantment getEnchantment(@NotNull String name) {
        return fromRegistry(Registry.ENCHANTMENT, name);
    }

    @Nullable
    public EntityType getEntityType(@NotNull String name) {
        return fromRegistry(Registry.ENTITY_TYPE, name);
    }

    @Nullable
    public Attribute getAttribute(@NotNull String name) {
        return fromRegistry(Registry.ATTRIBUTE, name);
    }

    @Nullable
    public PotionType getPotionType(@NotNull String name) {
        return fromRegistry(Registry.POTION, name);
    }

    @Nullable
    public Sound getSound(@NotNull String name) {
        return fromRegistry(Registry.SOUNDS, name);
    }

    @Nullable
    public Particle getParticle(@NotNull String name) {
        return fromRegistry(Registry.PARTICLE_TYPE, name);
    }
}
