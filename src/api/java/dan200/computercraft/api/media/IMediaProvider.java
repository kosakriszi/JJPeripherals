/**
 * This file is part of the public ComputerCraft API - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2015. This API may be redistributed unmodified and in full only.
 * For help using the API, and posting your mods, visit the forums at computercraft.info.
 */

package dan200.computercraft.api.media;

import dan200.computercraft.api.ComputerCraftAPI;
import net.minecraft.item.ItemStack;

/**
 * This interface is used to provide IMedia implementations for ItemStack
 * @see ComputerCraftAPI#registerMediaProvider(IMediaProvider)
 */
public interface IMediaProvider
{
    /**
     * Produce an IMedia implementation from an ItemStack.
     * @see ComputerCraftAPI#registerMediaProvider(IMediaProvider)
     * @return an IMedia implementation, or null if the item is not something you wish to handle
     */
    public IMedia getMedia( ItemStack stack );
}
