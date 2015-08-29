package com.kosakorner.jjperipherals;

import com.kosakorner.jjperipherals.tile.TilePeripheralRelay;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent;
import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.filesystem.IMount;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import com.kosakorner.jjperipherals.block.BlockPeripheralRelay;
import com.kosakorner.jjperipherals.proxy.CommonProxy;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import openmods.api.IProxy;
import openmods.config.BlockInstances;
import openmods.config.game.ModStartupHelper;
import openmods.config.game.RegisterBlock;
import openmods.config.properties.ConfigProcessing;
import org.apache.commons.lang3.ObjectUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;

@Mod(modid = JJPeripherals.MODID, name = JJPeripherals.DISPLAY, version = JJPeripherals.VERSION, dependencies = "required-after:OpenMods;after:ComputerCraft")
public class JJPeripherals {

    public static final String MODID = "jjperipherals";
    public static final String DISPLAY = "JJPeripherals";
    public static final String VERSION = "1.0.6";

    @Mod.Instance(MODID)
    public static JJPeripherals instance;

    @SidedProxy(clientSide = "com.kosakorner.jjperipherals.proxy.ClientProxy", serverSide = "com.kosakorner.jjperipherals.proxy.CommonProxy")
    public static IProxy proxy;

    public static CreativeTabs tabJJPeripherals = new CreativeTabs("tabJJPeripherals") {
        @Override
        public Item getTabIconItem() {
            return ObjectUtils.firstNonNull(Item.getItemFromBlock(Blocks.peripheralRelay), Items.potato);
        }
    };

    private final ModStartupHelper startupHelper = new ModStartupHelper(MODID);

    public static int renderId;
    public static IMount apiMount;

    public static class Blocks implements BlockInstances {
        @RegisterBlock(name = "peripheral_relay", tileEntity = TilePeripheralRelay.class)
        public static BlockPeripheralRelay peripheralRelay;
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        startupHelper.registerBlocksHolder(JJPeripherals.Blocks.class);

        startupHelper.preInit(event.getSuggestedConfigurationFile());
        apiMount = new ApiMount(new File(event.getSuggestedConfigurationFile().getParentFile(), "/JJPeripherals"));

        Recipes.register();

        proxy.preInit();
    }

    @EventHandler
    public void handleRenames(FMLMissingMappingsEvent event) {
        startupHelper.handleRenames(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();
        proxy.registerRenderInformation();

        ComputerCraftAPI.registerPeripheralProvider(JJPeripherals.Blocks.peripheralRelay);
    }

}
