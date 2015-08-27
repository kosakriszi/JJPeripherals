package com.kosakorner.jjperipherals.tile;

import dan200.computercraft.api.peripheral.IPeripheral;
import openmods.api.INeighbourAwareTile;
import openmods.tileentity.OpenTileEntity;

public abstract class TilePeripheralBase extends OpenTileEntity implements IPeripheral {

    protected String peripheralName;

    public TilePeripheralBase(String name) {
        peripheralName = name;
    }

    @Override
    public String getType() {
        return peripheralName;
    }

    @Override
    public boolean equals(IPeripheral other) {
        return other == this;
    }

}
