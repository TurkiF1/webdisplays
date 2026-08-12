/*
 * Copyright (C) 2018 BARBOTIN Nicolas
 */

package net.montoyo.wd.core;

import com.mojang.serialization.Codec;
import net.minecraft.advancements.criterion.ContextAwarePredicate;
import net.minecraft.advancements.criterion.SimpleCriterionTrigger;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;

import javax.annotation.Nonnull;
import java.util.Optional;

public class Criterion extends SimpleCriterionTrigger<Criterion.Instance> {
    public record Instance(Optional<ContextAwarePredicate> player) implements SimpleInstance {
        public static final Codec<Instance> CODEC = ContextAwarePredicate.CODEC
                .optionalFieldOf("player")
                .xmap(Instance::new, Instance::player)
                .codec();
    }

    private final Identifier id;

    public Criterion(@Nonnull String name) {
        id = Identifier.fromNamespaceAndPath("webdisplays", name);
    }

    public Identifier id() {
        return id;
    }

    @Override
    public Codec<Instance> codec() {
        return Instance.CODEC;
    }

    public void trigger(ServerPlayer player) {
        trigger(player, instance -> true);
    }
}
