package moe.inx.simple_durability_tooltip.mixin;

import java.util.function.Consumer;

import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.network.chat.Component;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

	@Shadow
	public abstract boolean isDamaged();

	@Shadow
	public abstract int getMaxDamage();

	@Shadow
	public abstract int getDamageValue();

	@Inject(
		method = "addDetailsToTooltip",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/TooltipFlag;isAdvanced()Z")
	)
	public void simple_durability_tooltip$getTooltip(Item.TooltipContext context, TooltipDisplay display, @Nullable Player player, TooltipFlag tooltipFlag,
			Consumer<Component> builder, CallbackInfo ci) {
		if (!tooltipFlag.isAdvanced() && this.isDamaged() && display.shows(DataComponents.DAMAGE)) {
			builder.accept(Component.translatable("item.durability",this.getMaxDamage() - this.getDamageValue(),this.getMaxDamage()));
		}
	}
}
