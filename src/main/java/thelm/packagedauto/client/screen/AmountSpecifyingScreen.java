package thelm.packagedauto.client.screen;

import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;
import thelm.packagedauto.menu.BaseMenu;

// Code from Refined Storage
public abstract class AmountSpecifyingScreen<C extends BaseMenu<?>> extends BaseScreen<C> {

	private BaseScreen<?> parent;

	protected EditBox amountField;
	protected Button okButton;
	protected Button cancelButton;

	public AmountSpecifyingScreen(BaseScreen<?> parent, C menu, Inventory inventory, Component title) {
		super(menu, inventory, title);
		this.parent = parent;
	}

	protected abstract int[] getIncrements();

	protected abstract int getMaxAmount();

	protected abstract int getDefaultAmount();

	protected Pair<Integer, Integer> getAmountPos() {
		return Pair.of(9, 51);
	}

	protected abstract Pair<Integer, Integer> getOkCancelPos();

	protected abstract int getOkCancelButtonWidth();

	@Override
	public void init() {
		super.init();
		Pair<Integer, Integer> pos = getOkCancelPos();

		okButton = addButton(leftPos+pos.getLeft(), topPos+pos.getRight(), getOkCancelButtonWidth(), 20, new TranslatableComponent("misc.packagedauto.set"), true, true, btn->onOkButtonPressed(hasShiftDown()));
		cancelButton = addButton(leftPos+pos.getLeft(), topPos+pos.getRight()+24, getOkCancelButtonWidth(), 20, new TranslatableComponent("gui.cancel"), true, true, btn->close());

		amountField = new EditBox(font, leftPos+getAmountPos().getLeft(), topPos+getAmountPos().getRight(), 69 - 6, font.lineHeight, TextComponent.EMPTY);
		amountField.setBordered(false);
		amountField.setVisible(true);
		amountField.setValue(String.valueOf(getDefaultAmount()));
		amountField.setTextColor(0xFFFFFF);
		amountField.setCanLoseFocus(false);
		amountField.changeFocus(true);

		addRenderableWidget(amountField);
		setFocused(amountField);

		int[] increments = getIncrements();

		int xx = 7;
		int width = 30;
		for(int i = 0; i < 3; ++i) {
			int increment = increments[i];
			String text = "+" + increment;
			addButton(leftPos+xx, topPos+20, width, 20, new TextComponent(text), true, true, btn->onIncrementButtonClicked(increment));
			xx += width+3;
		}

		xx = 7;
		for(int i = 0; i < 3; ++i) {
			int increment = increments[i];
			String text = "-" + increment;
			addButton(leftPos+xx, topPos+imageHeight - 20 - 7, width, 20, new TextComponent(text), true, true, btn->onIncrementButtonClicked(-increment));
			xx += width+3;
		}
	}

	@Override
	protected void renderBgAdditional(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
		amountField.renderButton(poseStack, 0, 0, 0F);
	}

	@Override
	protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
		font.draw(poseStack, getTitle().getString(), 7, 7, 0x404040);
		super.renderLabels(poseStack, mouseX, mouseY);
	}

	@Override
	public boolean keyPressed(int key, int scanCode, int modifiers) {
		if(key == GLFW.GLFW_KEY_ESCAPE) {
			close();
			return true;
		}
		if((key == GLFW.GLFW_KEY_ENTER || key == GLFW.GLFW_KEY_KP_ENTER) && amountField.isFocused()) {
			onOkButtonPressed(hasShiftDown());
			return true;
		}
		if(amountField.keyPressed(key, scanCode, modifiers)) {
			return true;
		}
		return super.keyPressed(key, scanCode, modifiers);
	}

	private void onIncrementButtonClicked(int increment) {
		int oldAmount = 0;
		try {
			oldAmount = Integer.parseInt(amountField.getValue());
		}
		catch(NumberFormatException e) {
			// NO OP
		}
		int newAmount = Math.min(Math.max(oldAmount+increment, 0), getMaxAmount());
		amountField.setValue(String.valueOf(newAmount));
	}

	protected abstract void onOkButtonPressed(boolean shiftDown);

	public void close() {
		minecraft.setScreen(parent);
	}

	public BaseScreen<?> getParent() {
		return parent;
	}
}