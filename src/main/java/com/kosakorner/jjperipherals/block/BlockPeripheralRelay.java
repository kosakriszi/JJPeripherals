package com.kosakorner.jjperipherals.block;

import com.kosakorner.jjperipherals.tile.TilePeripheralRelay;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import openmods.block.BlockRotationMode;
import openmods.tileentity.OpenTileEntity;

public class BlockPeripheralRelay extends BlockBase implements IPeripheralProvider {

    public static class Icons {
        public static IIcon top;
        public static IIcon bottom;
        public static IIcon side;
    }

    public BlockPeripheralRelay() {
        super(Material.ground);
        setRotationMode(BlockRotationMode.SIX_DIRECTIONS);
        setPlacementMode(BlockPlacementMode.ENTITY_ANGLE);
        setRenderMode(RenderMode.BLOCK_ONLY);
    }

    @Override
    public IPeripheral getPeripheral(World world, int x, int y, int z, int side) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof OpenTileEntity) {
            ForgeDirection dir = ((OpenTileEntity) tileEntity).getRotation().getOpposite();
            if (dir.ordinal() == side) {
                return ((TilePeripheralRelay) world.getTileEntity(x, y, z));
            }
        }
        return null;
    }

    @Override
    public void registerBlockIcons(IIconRegister registry) {
        Icons.top = registry.registerIcon("jjperipherals:relay_top");
        Icons.bottom = registry.registerIcon("jjperipherals:relay_bottom");
        Icons.side = registry.registerIcon("jjperipherals:relay_side");
        setTexture(ForgeDirection.UP, Icons.top);
        setTexture(ForgeDirection.DOWN, Icons.bottom);
        setTexture(ForgeDirection.EAST, Icons.side);
        setTexture(ForgeDirection.WEST, Icons.side);
        setTexture(ForgeDirection.NORTH, Icons.side);
        setTexture(ForgeDirection.SOUTH, Icons.side);
        setDefaultTexture(Icons.side);
    }

}
