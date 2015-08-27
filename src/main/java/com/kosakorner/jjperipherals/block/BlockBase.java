package com.kosakorner.jjperipherals.block;

import com.kosakorner.jjperipherals.JJPeripherals;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import openmods.block.OpenBlock;

public class BlockBase extends OpenBlock {

    public BlockBase(Material material) {
        super(material);
        this.setCreativeTab(JJPeripherals.tabJJPeripherals);
    }

    @Override
    public int getRenderType() {
        return JJPeripherals.renderId;
    }

    @Override
    protected Object getModInstance() {
        return JJPeripherals.instance;
    }

}
