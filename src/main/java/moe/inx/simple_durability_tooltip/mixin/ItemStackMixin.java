package moe.inx.simple_durability_tooltip.mixin;

import java.util.List;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.component.TranslatableComponent;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

	@Shadow
	abstract boolean isDamaged();

	@Shadow
	abstract int getMaxDamage();

	@Shadow
	abstract int getDamage();

	@Inject(method = "getTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/item/TooltipContext;isAdvanced()Z", ordinal = 2), locals = LocalCapture.CAPTURE_FAILSOFT)
	public void simple_durability_tooltip$getTooltip(@Nullable PlayerEntity player, TooltipContext context,
			CallbackInfoReturnable<List<Text>> ci, List<Text> list) {
		if (!context.isAdvanced()) {
			if (this.isDamaged()) {
				try {
					list.add(Text.translatable("item.durability", this.getMaxDamage() - this.getDamage(),
							this.getMaxDamage()));
				} catch (NoSuchMethodError e) {
					list.add((Text) new TranslatableComponent("item.durability", this.getMaxDamage() - this.getDamage(),
							this.getMaxDamage()));
				}
			}
		}
	}
}
