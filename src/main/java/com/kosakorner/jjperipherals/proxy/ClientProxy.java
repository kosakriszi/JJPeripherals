package com.kosakorner.jjperipherals.proxy;

import com.kosakorner.jjperipherals.JJPeripherals;
import cpw.mods.fml.client.registry.RenderingRegistry;
import openmods.renderer.BlockRenderingHandler;

public class ClientProxy extends CommonProxy {

    public ClientProxy() {
        super();
        JJPeripherals.renderId = RenderingRegistry.getNextAvailableRenderId();
        final BlockRenderingHandler blockRenderingHandler = new BlockRenderingHandler(JJPeripherals.renderId);
        RenderingRegistry.registerBlockHandler(blockRenderingHandler);
    }

}
