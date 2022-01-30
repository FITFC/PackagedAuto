package thelm.packagedauto.inventory;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.IItemHandlerModifiable;
import thelm.packagedauto.block.entity.PackagerExtensionBlockEntity;

public class PackagerExtensionItemHandler extends BaseItemHandler<PackagerExtensionBlockEntity> {

	public PackagerExtensionItemHandler(PackagerExtensionBlockEntity blockEntity) {
		super(blockEntity, 11);
	}

	@Override
	protected void onContentsChanged(int slot) {
		if(slot < 9 && !blockEntity.getLevel().isClientSide) {
			if(blockEntity.isWorking && !getStackInSlot(slot).isEmpty()) {
				if(blockEntity.isWorking && (getStackInSlot(slot).isEmpty() || !blockEntity.isInputValid())) {
					blockEntity.endProcess();
				}
			}
		}
	}

	@Override
	public boolean isItemValid(int index, ItemStack stack) {
		switch(index) {
		case 9: return false;
		case 10: return stack.getCapability(CapabilityEnergy.ENERGY, null).isPresent();
		default: return blockEntity.isWorking ? !getStackInSlot(index).isEmpty() : true;
		}
	}

	@Override
	public IItemHandlerModifiable getWrapperForDirection(Direction side) {
		return wrapperMap.computeIfAbsent(side, s->new PackagerExtensionItemHandlerWrapper(this, s));
	}

	@Override
	public int get(int id) {
		switch(id) {
		case 0: return blockEntity.remainingProgress;
		case 1: return blockEntity.isWorking ? 1 : 0;
		default: return 0;
		}
	}

	@Override
	public void set(int id, int value) {
		switch(id) {
		case 0:
			blockEntity.remainingProgress = value;
			break;
		case 1:
			blockEntity.isWorking = value != 0;
			break;
		}
	}

	@Override
	public int getCount() {
		return 2;
	}
}
