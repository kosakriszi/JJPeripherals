package com.kosakorner.jjperipherals;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class Recipes {

    public static void register() {
        if (JJPeripherals.Blocks.peripheralRelay != null) {
            GameRegistry.addRecipe(new ShapedOreRecipe(JJPeripherals.Blocks.peripheralRelay, "srs", "sss", "sgs", 's', Blocks.stone, 'g', Items.gold_ingot, 'r', Items.redstone));
        }

    }

}
