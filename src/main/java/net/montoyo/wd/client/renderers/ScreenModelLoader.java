package net.montoyo.wd.client.renderers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.Identifier;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IGeometryLoader;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.function.Function;

public class ScreenModelLoader implements IGeometryLoader<ScreenModelLoader.ScreenModelGeometry> {
    public static final Identifier SCREEN_LOADER = new Identifier("webdisplays", "screen_loader");

    public static final Identifier SCREEN_SIDE = new Identifier("webdisplays", "block/screen");

    private static final Identifier[] SIDES = new Identifier[16];
    public static final Material[] MATERIALS_SIDES = new Material[16];
    
    static {
        for (int i = 0; i < SIDES.length; i++) {
            SIDES[i] = new Identifier(SCREEN_SIDE.getNamespace(), SCREEN_SIDE.getPath() + i);
            MATERIALS_SIDES[i] = ForgeHooksClient.getBlockMaterial(SIDES[i]);
        }
    }
    
    @Override
    public ScreenModelGeometry read(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return new ScreenModelGeometry();
    }

    public static class ScreenModelGeometry implements IUnbakedGeometry<ScreenModelGeometry> {
        
        @Override
        public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides, Identifier modelLocation) {
            return new ScreenBaker(modelState, spriteGetter, overrides, context.getTransforms());
        }
        
//        @Override
//        public void resolveParents(Function<Identifier, UnbakedModel> modelGetter, IGeometryBakingContext context) {
//            IUnbakedGeometry.super.resolveParents(modelGetter, context);
//        }
        
//        @Override
//        public Set<String> getConfigurableComponentNames() {
//            return IUnbakedGeometry.super.getConfigurableComponentNames();
//        }

        // TODO: ?
//        @Override
//        public Collection<Material> getMaterials(IGeometryBakingContext iGeometryBakingContext, Function<Identifier, UnbakedModel> function, Set<Pair<String, String>> set) {
//            return Arrays.asList(MATERIALS_SIDES);
//        }
    }
}


