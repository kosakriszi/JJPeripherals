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
        add("default");
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
        try {
            switch (method) {
                // getName
                case 0:
                    return new Object[]{name};
                // setName
                case 1:
                    if (arguments.length == 1 && arguments[0] instanceof String) {
                        name = (String) arguments[0];
                        return new Object[]{true};
                    }
                    return new Object[]{false};
                // getGroups
                case 2:
                    Map<Object, Object> table = new HashMap<Object, Object>();
                    for (int i = 0; i < groups.size(); i++) {
                        table.put(i + 1, groups.get(i));
                    }
                    return new Object[]{table};
                // setGroups
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
                // addGroup
                case 4:
                    if (arguments.length == 1 && arguments[0] instanceof String) {
                        boolean success = groups.add((String) arguments[0]);
                        return new Object[]{success};
                    }
                    return new Object[]{false};
                // removeGroup
                case 5:
                    if (arguments.length == 1 && arguments[0] instanceof String) {
                        boolean success = groups.remove((String) arguments[0]);
                        return new Object[]{success};
                    }
                    return new Object[]{false};
                // getAttachedType
                case 6:
                    if (attachedPeripheral != null) {
                        return new Object[]{attachedPeripheral.getType()};
                    }
                    return new Object[]{"No peripheral attached!"};
                // getAttachedMethods
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
                // forward
                case 8:
                    if (attachedPeripheral != null) {
                        if (arguments.length >= 1) {
                            String[] methods = attachedPeripheral.getMethodNames();

                            String methodName = (String) arguments[0];
                            Object[] args = Arrays.copyOfRange(arguments, 1, arguments.length);
                            int index = Arrays.asList(methods).indexOf(methodName);
                            return attachedPeripheral.callMethod(computer, context, index, args);
                        }
                        return new Object[]{"Usage: forward(\"methodName\", [\"args1\"]...])"};
                    }
                    return new Object[]{"No peripheral attached!"};
            }
        }
        catch (Exception e) {
            // Wrap this entire thing so people can report actual code bugs.
            e.printStackTrace();
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
            // This really shouldn't be necessary, but APPARENTLY it is
            String[] readGroups = tag.getString("groups").split(Pattern.quote("|"));
            for (String group : readGroups) {
                if (!groups.contains(group)) {
                    groups.add(group);
                }
            }
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
