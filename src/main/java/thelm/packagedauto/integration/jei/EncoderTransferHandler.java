package thelm.packagedauto.integration.jei;


import java.util.List;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandlerHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import thelm.packagedauto.api.IPackageRecipeType;
import thelm.packagedauto.menu.EncoderMenu;
import thelm.packagedauto.network.PacketHandler;
import thelm.packagedauto.network.packet.SetRecipePacket;

public class EncoderTransferHandler implements IRecipeTransferHandler<EncoderMenu, Object> {

	private final IRecipeTransferHandlerHelper transferHelper;

	public EncoderTransferHandler(IRecipeTransferHandlerHelper transferHelper) {
		this.transferHelper = transferHelper;
	}

	@Override
	public Class<EncoderMenu> getContainerClass() {
		return EncoderMenu.class;
	}

	@Override
	public Class<Object> getRecipeClass() {
		return Object.class;
	}

	@Override
	public IRecipeTransferError transferRecipe(EncoderMenu menu, Object recipe, IRecipeSlotsView recipeSlots, Player player, boolean maxTransfer, boolean doTransfer) {
		List<ResourceLocation> categories = PackagedAutoJEIPlugin.jeiRuntime.getRecipeManager().createRecipeCategoryLookup().get().
				map(c->c.getRecipeType()).filter(t->t.getRecipeClass().isAssignableFrom(recipe.getClass())).
				map(t->t.getUid()).toList();
		if(categories.isEmpty()) {
			return transferHelper.createInternalError();
		}
		IPackageRecipeType recipeType = menu.patternItemHandler.recipeType;
		if(!categories.stream().anyMatch(recipeType.getJEICategories()::contains)) {
			return transferHelper.createInternalError();
		}
		Int2ObjectMap<ItemStack> map = recipeType.getRecipeTransferMap(new RecipeSlotsViewWrapper(recipe, recipeSlots));
		if(map == null || map.isEmpty()) {
			return transferHelper.createInternalError();
		}
		if(!doTransfer) {
			return null;
		}
		PacketHandler.INSTANCE.sendToServer(new SetRecipePacket(map));
		return null;
	}
}
