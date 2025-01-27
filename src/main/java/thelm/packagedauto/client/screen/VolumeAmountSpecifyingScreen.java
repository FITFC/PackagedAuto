package thelm.packagedauto.client.screen;

import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import thelm.packagedauto.api.IVolumeStackWrapper;
import thelm.packagedauto.item.VolumePackageItem;
import thelm.packagedauto.menu.VolumeAmountSpecifyingMenu;
import thelm.packagedauto.network.PacketHandler;
import thelm.packagedauto.network.packet.SetItemStackPacket;

public class VolumeAmountSpecifyingScreen extends AmountSpecifyingScreen<VolumeAmountSpecifyingMenu> {

	public static final ResourceLocation BACKGROUND = new ResourceLocation("packagedauto:textures/gui/amount_specifying.png");

	private int containerSlot;
	private IVolumeStackWrapper stack;
	private int maxAmount;

	public VolumeAmountSpecifyingScreen(BaseScreen<?> parent, Inventory inventory, int containerSlot, IVolumeStackWrapper stack, int maxAmount) {
		super(parent, new VolumeAmountSpecifyingMenu(inventory, stack), inventory, new TranslatableComponent("gui.packagedauto.volume_amount_specifying"));
		imageWidth = 172;
		imageHeight = 99;
		this.containerSlot = containerSlot;
		this.stack = stack;
		this.maxAmount = maxAmount;
	}

	@Override
	protected int getDefaultAmount() {
		return stack.getAmount();
	}

	@Override
	protected int getMaxAmount() {
		return maxAmount;
	}

	@Override
	protected int[] getIncrements() {
		return new int[] {
				100, 500, 1000,
		};
	}

	@Override
	protected ResourceLocation getBackgroundTexture() {
		return BACKGROUND;
	}

	@Override
	protected void onOkButtonPressed(boolean shiftDown) {
		try {
			int amount = Mth.clamp(Integer.parseInt(amountField.getValue()), 0, maxAmount);
			IVolumeStackWrapper newStack = stack.copy();
			newStack.setAmount(amount);
			PacketHandler.INSTANCE.sendToServer(new SetItemStackPacket((short)containerSlot, VolumePackageItem.makeVolumePackage(newStack)));
			close();
		}
		catch(NumberFormatException e) {
			// NO OP
		}
	}
}
