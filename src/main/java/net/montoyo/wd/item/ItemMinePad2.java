/*
 * Copyright (C) 2018 BARBOTIN Nicolas
 */

package net.montoyo.wd.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.montoyo.wd.WebDisplays;
import net.montoyo.wd.config.CommonConfig;
import net.montoyo.wd.core.CraftComponent;
import net.montoyo.wd.net.WDNetworkRegistry;
import net.montoyo.wd.net.server_bound.C2SMessageMinepadUrl;
import net.montoyo.wd.utilities.serialization.Util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public class ItemMinePad2 extends Item implements WDItem {
    public ItemMinePad2(Properties properties) {
        super(properties
                        .stacksTo(1)
//				.tab(WebDisplays.CREATIVE_TAB)
        );
    }

    private static String getURL(ItemStack is) {
        CompoundTag tag = Util.getItemTag(is);
        if (tag == null || !tag.contains("PadURL"))
            return CommonConfig.Browser.homepage;
        else
            return tag.getStringOr("PadURL", CommonConfig.Browser.homepage);
    }

    @Override
    @Nonnull
    public InteractionResult use(Level world, Player ply, @Nonnull InteractionHand hand) {
        ItemStack is = ply.getItemInHand(hand);
        boolean ok;

        if (ply.isShiftKeyDown()) {
            if (world.isClientSide())
                WebDisplays.PROXY.displaySetPadURLGui(is, getURL(is));

            ok = true;
        } else if (Util.getItemTag(is) != null && Util.getItemTag(is).contains("PadID")) {
            if (world.isClientSide())
                WebDisplays.PROXY.openMinePadGui(Util.getUUID(Util.getItemTag(is), "PadID"));

            ok = true;
        } else {
            UUID uuid = UUID.randomUUID();
            String url = getURL(is);
            WDNetworkRegistry.INSTANCE.sendToServer(new C2SMessageMinepadUrl(uuid, url));
            CompoundTag tag = Util.getOrCreateItemTag(is);
            Util.putUUID(tag, "PadID", uuid);
            Util.setItemTag(is, tag);

            ok = true;
        }

        return ok ? InteractionResult.SUCCESS.heldItemTransformedTo(is) : InteractionResult.PASS;
    }


    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity ent) {
        if (ent.onGround() && !ent.level().isClientSide()) {
            CompoundTag tag = Util.getItemTag(ent.getItem());

            if (tag != null && tag.contains("ThrowHeight")) {
                //Delete it, it touched the ground
                double height = tag.getDoubleOr("ThrowHeight", 0.0);
                UUID thrower = null;

                if (tag.contains("ThrowerMSB") && tag.contains("ThrowerLSB"))
                    thrower = new UUID(tag.getLongOr("ThrowerMSB", 0L), tag.getLongOr("ThrowerLSB", 0L));

                if (tag.contains("PadID") || tag.contains("PadURL")) {
                    tag.remove("ThrowerMSB");
                    tag.remove("ThrowerLSB");
                    tag.remove("ThrowHeight");
                    Util.setItemTag(ent.getItem(), tag);
                } else //We can delete the whole tag
                    Util.setItemTag(ent.getItem(), null);

                if (thrower != null && height - ent.getBlockY() >= 20.0) {
                    ent.level().playSound(null, ent.getBlockX(), ent.getBlockY(), ent.getBlockZ(), SoundEvents.GLASS_BREAK, SoundSource.BLOCKS, 4.0f, 1.0f);
                    ent.level().addFreshEntity(new ItemEntity(ent.level(), ent.getBlockX(), ent.getBlockY(), ent.getBlockZ(), CraftComponent.EXTCARD.makeItemStack()));
                    ent.setRemoved(Entity.RemovalReason.CHANGED_DIMENSION);

                    Player ply = ent.level().getPlayerByUUID(thrower);
                    if (ply != null && ply instanceof ServerPlayer)
                        WebDisplays.INSTANCE.criterionPadBreak.trigger((ServerPlayer) ply);
                }
            }
        }

        return false;
    }

    @Nullable
    @Override
    public String getWikiName(@Nonnull ItemStack is) {
        return is.getItem().getName(is).getString();
    }
}
