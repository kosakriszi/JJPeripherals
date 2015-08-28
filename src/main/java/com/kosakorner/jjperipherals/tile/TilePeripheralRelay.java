package com.kosakorner.jjperipherals.tile;

import com.kosakorner.jjperipherals.JJPeripherals;
import com.kosakorner.jjperipherals.WrappedPeripheral;
import com.kosakorner.jjperipherals.util.StringUtil;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import openmods.api.INeighbourAwareTile;
import openmods.reflection.MethodAccess;
import openmods.reflection.ReflectionHelper;

import java.util.*;
import java.util.regex.Pattern;

public class TilePeripheralRelay extends TilePeripheralBase implements INeighbourAwareTile {

    private static class Access {
        public final Class<?> ccClass = ReflectionHelper.getClass("dan200.computercraft.ComputerCraft");

        public final MethodAccess.Function5<IPeripheral, World, Integer, Integer, Integer, Integer> getPeripheralAt =
                MethodAccess.create(IPeripheral.class, ccClass, World.class, int.class, int.class, int.class, int.class, "getPeripheralAt");
    }

    private final static Access access = new Access();

    private IPeripheral attachedPeripheral;

    private String name = "peripheral_relay";
    private List<String> groups = new ArrayList<String>() {{
        add("null");
    }};

    public TilePeripheralRelay() {
        super("peripheral_relay");
    }

    @Override
    public void initialize() {
        onNeighbourChanged(getBlockType());
    }

    @Override
    public String[] getMethodNames() {
        return new String[]{"getName", "setName", "getGroups", "setGroups", "addGroup", "removeGroup", "getAttachedType", "getAttachedMethods", "forward"};
    }

    @Override
    public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
        switch (method) {
            case 0:
                return new Object[]{name};
            case 1:
                if (arguments.length == 1 && arguments[0] instanceof String) {
                    name = (String) arguments[0];
                    return new Object[]{true};
                }
                return new Object[]{false};
            case 2:
                Map<Object, Object> table = new HashMap<Object, Object>();
                for (int i = 0; i < groups.size(); i++) {
                    table.put(i + 1, groups.get(i));
                }
                return new Object[]{table};
            case 3:
                if (arguments.length >= 1) {
                    List<String> newGroups = new ArrayList<String>();
                    for (Object arg : arguments) {
                        if (arg instanceof String) {
                            newGroups.add((String) arg);
                        }
                    }
                    groups = newGroups;
                    return new Object[]{true};
                }
                return new Object[]{"Usage: setGroups(\"group1\", [\"group2\"]..."};
            case 4:
                if (arguments.length == 1 && arguments[0] instanceof String) {
                    boolean success = groups.add((String) arguments[0]);
                    return new Object[]{success};
                }
                return new Object[]{false};
            case 5:
                if (arguments.length == 1 && arguments[0] instanceof String) {
                    boolean success = groups.remove((String) arguments[0]);
                    return new Object[]{success};
                }
                return new Object[]{false};
            case 6:
                if (attachedPeripheral != null) {
                    return new Object[]{attachedPeripheral.getType()};
                }
                return new Object[]{"No peripheral attached!"};
            case 7:
                if (attachedPeripheral != null) {
                    String[] methods = attachedPeripheral.getMethodNames();
                    table = new HashMap<Object, Object>();
                    for (int i = 0; i < methods.length; i++) {
                        table.put(i + 1, methods[i]);
                    }
                    return new Object[]{table};
                }
                return new Object[]{"No peripheral attached!"};
            case 8:
                if (attachedPeripheral != null) {
                    if (arguments.length >= 1) {
                        String[] methods = attachedPeripheral.getMethodNames();
                        System.out.println(arguments[0].getClass().getCanonicalName());
                        int index = Arrays.asList(methods).indexOf((String) arguments[0]);
                        return attachedPeripheral.callMethod(computer, context, index, Arrays.copyOfRange(arguments, 1, arguments.length));
                    }
                    return new Object[]{"Usage: forward(\"methodName\", [\"args1\"]...])"};
                }
                return new Object[]{"No peripheral attached!"};
        }
        return null;
    }

    @Override
    public void attach(IComputerAccess computer) {
        computer.mount("rom/jjperipherals", JJPeripherals.apiMount);
    }

    @Override
    public void detach(IComputerAccess computer) {
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setString("name", name);
        tag.setString("groups", StringUtil.joinString(groups, "|", 0));
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        if (tag.hasKey("groups")) {
            Collections.addAll(groups, tag.getString("groups").split(Pattern.quote("|")));
        }
        if (tag.hasKey("name")) {
            name = tag.getString("name");
        }
    }

    @Override
    public void onNeighbourChanged(Block block) {
        if (!worldObj.isRemote) {
            final ForgeDirection rotation = getRotation();

            final int targetX = xCoord + rotation.offsetX;
            final int targetY = yCoord + rotation.offsetY;
            final int targetZ = zCoord + rotation.offsetZ;

            IPeripheral peripheral = access.getPeripheralAt.call(null, worldObj, targetX, targetY, targetZ, 0);
            if (peripheral != null) {
                attachedPeripheral = new WrappedPeripheral(peripheral);
            } else {
                attachedPeripheral = null;
            }
        }
    }

}
