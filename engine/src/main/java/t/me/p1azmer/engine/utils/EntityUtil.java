package t.me.p1azmer.engine.utils;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import t.me.p1azmer.engine.NexEngine;
import t.me.p1azmer.engine.Version;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@UtilityClass
public class EntityUtil {
    public static final EquipmentSlot[] EQUIPMENT_SLOTS = {
            EquipmentSlot.HAND, EquipmentSlot.OFF_HAND, EquipmentSlot.HEAD,
            EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET
    };

    private static AtomicInteger entityCounter;

    public boolean loadEntityCounter(@NotNull NexEngine core) {
        Class<?> entityClass = Reflex.getClass("net.minecraft.world.entity", "Entity");
        if (entityClass == null) {
            core.error("Could not find NMS Entity class!");
            return false;
        }

        String fieldName = "c";
        if (Version.isAtLeast(Version.V1_19_R3) && Version.isBehind(Version.MC_1_20_6)) {
            fieldName = "d";
        }

        Object object = Reflex.getFieldValue(entityClass, fieldName);
        if (!(object instanceof AtomicInteger atomicInteger)) {
            if (object == null) {
                core.error("Could not find entity counter field!");
            } else
                core.error("Field '" + fieldName + "' in " + entityClass.getName() + " class is " +
                        object.getClass().getName() + " (expected AtomicInteger)");
            return false;
        }

        entityCounter = atomicInteger;
        return true;
    }

    public int nextEntityId() {
        return entityCounter.incrementAndGet();
    }

    public double getAttribute(@NotNull LivingEntity entity, @NotNull Attribute attribute) {
        AttributeInstance instance = entity.getAttribute(attribute);
        return instance == null ? 0D : instance.getValue();
    }

    public double getAttributeBase(@NotNull LivingEntity entity, @NotNull Attribute attribute) {
        AttributeInstance instance = entity.getAttribute(attribute);
        return instance == null ? 0D : instance.getBaseValue();
    }

    @NotNull
    public Map<EquipmentSlot, ItemStack> getEquippedItems(@NotNull LivingEntity entity) {
        return getEquippedItems(entity, EQUIPMENT_SLOTS);
    }

    @NotNull
    public Map<EquipmentSlot, ItemStack> getEquippedItems(@NotNull LivingEntity entity, @NotNull EquipmentSlot... slots) {
        EntityEquipment equipment = entity.getEquipment();
        if (equipment == null) return Collections.emptyMap();

        Map<EquipmentSlot, ItemStack> map = new HashMap<>();
        for (EquipmentSlot slot : slots) {
            if (slot.name().equalsIgnoreCase("BODY")) continue; // from 1.20.6

            map.put(slot, equipment.getItem(slot));
        }
        return map;
    }

    @NotNull
    public Map<EquipmentSlot, ItemStack> getEquippedHands(@NotNull LivingEntity entity) {
        return getEquippedItems(entity, EquipmentSlot.HAND, EquipmentSlot.OFF_HAND);
    }

    @NotNull
    public Map<EquipmentSlot, ItemStack> getEquippedArmor(@NotNull LivingEntity entity) {
        return getEquippedItems(entity, EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.CHEST, EquipmentSlot.HEAD);
    }

    @Nullable
    public BlockFace getDirection(@NotNull Entity entity) {
        float yaw = Math.round(entity.getLocation().getYaw() / 90F);

        if ((yaw == -4.0F) || (yaw == 0.0F) || (yaw == 4.0F)) {
            return BlockFace.SOUTH;
        }
        if ((yaw == -1.0F) || (yaw == 3.0F)) {
            return BlockFace.EAST;
        }
        if ((yaw == -2.0F) || (yaw == 2.0F)) {
            return BlockFace.NORTH;
        }
        if ((yaw == -3.0F) || (yaw == 1.0F)) {
            return BlockFace.WEST;
        }
        return null;
    }
}