package thelm.packagedauto.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import thelm.packagedauto.PackagedAuto;
import thelm.packagedauto.block.entity.EncoderBlockEntity;

public class EncoderBlock extends BaseBlock {

	public static final EncoderBlock INSTANCE = new EncoderBlock();
	public static final Item ITEM_INSTANCE = new BlockItem(INSTANCE, new Item.Properties().tab(PackagedAuto.CREATIVE_TAB)).setRegistryName("packagedauto:encoder");

	protected EncoderBlock() {
		super(BlockBehaviour.Properties.of(Material.METAL).strength(15F, 25F).sound(SoundType.METAL));
		setRegistryName("packagedauto:encoder");
	}

	@Override
	public EncoderBlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return EncoderBlockEntity.TYPE_INSTANCE.create(pos, state);
	}
}
