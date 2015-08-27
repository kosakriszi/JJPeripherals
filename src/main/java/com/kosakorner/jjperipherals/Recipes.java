package com.kosakorner.jjperipherals;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.List;

public class Recipes {

    public static void register() {
        @SuppressWarnings("unchecked")
        final List<IRecipe> recipeList = CraftingManager.getInstance().getRecipeList();

        if (JJPeripherals.Blocks.peripheralRelay != null) {
            recipeList.add(new ShapedOreRecipe(JJPeripherals.Blocks.peripheralRelay, "srs", "sss", "sgs", 's', Blocks.stone, 'g', Items.gold_ingot, 'r', Items.redstone));
        }

    }

}
