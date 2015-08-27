package com.kosakorner.jjperipherals.proxy;

import com.kosakorner.jjperipherals.JJPeripherals;
import cpw.mods.fml.client.registry.RenderingRegistry;
import openmods.api.IProxy;
import openmods.renderer.BlockRenderingHandler;

public class ClientProxy implements IProxy {

    @Override
    public void preInit() {

    }

    @Override
    public void init() {

    }

    @Override
    public void postInit() {

    }

    @Override
    public void registerRenderInformation() {
        JJPeripherals.renderId = RenderingRegistry.getNextAvailableRenderId();
        final BlockRenderingHandler blockRenderingHandler = new BlockRenderingHandler(JJPeripherals.renderId);
        RenderingRegistry.registerBlockHandler(blockRenderingHandler);
    }

}
