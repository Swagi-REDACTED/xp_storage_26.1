package com.github.charlyb01.xpstorage.recipe;
 
import com.github.charlyb01.xpstorage.component.BookData;
import com.github.charlyb01.xpstorage.component.MyComponents;
import com.github.charlyb01.xpstorage.component.XpAmountData;
import com.github.charlyb01.xpstorage.item.ItemRegistry;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
 
public class XpBookDeprecateRecipe implements CraftingRecipe {
    final Ingredient base;
 
    private static final int BASE_LEVEL = 15;
    private static final BookData UPGRADE_1 = new BookData(1, 30, 90, 3, Integer.parseInt("a1fbe8", 16));
    private static final BookData UPGRADE_2 = new BookData(2, 50, 95, 5, Integer.parseInt("5a575a", 16));
    private static final BookData UPGRADE_3 = new BookData(3, 100, 100, 10, Integer.parseInt("e0e277", 16));
 
    public XpBookDeprecateRecipe(Ingredient base) {
        this.base = base;
    }
 
    @Override
    public boolean matches(CraftingInput input, Level world) {
        if (input.ingredientCount() != 1) return false;
 
        ItemStack stack = ItemStack.EMPTY;
        for (int i = 0; i < input.size(); i++) {
            if (!input.getItem(i).isEmpty()) {
                stack = input.getItem(i);
                break;
            }
        }
        XpAmountData xpAmountData = stack.getOrDefault(MyComponents.XP_COMPONENT, XpAmountData.EMPTY);
        return xpAmountData.level() > BASE_LEVEL;
    }
 
    @Override
    public ItemStack assemble(CraftingInput input) {
        ItemStack inputStack = ItemStack.EMPTY;
        for (int i = 0; i < input.size(); i++) {
            if (!input.getItem(i).isEmpty()) {
                inputStack = input.getItem(i);
                break;
            }
        }
        ItemStack result = new ItemStack(ItemRegistry.XP_BOOK);
 
        XpAmountData xpAmountData = inputStack.getOrDefault(MyComponents.XP_COMPONENT, XpAmountData.EMPTY);
        int level = xpAmountData.level();
        result.set(MyComponents.XP_COMPONENT, xpAmountData);
 
        if (level > UPGRADE_3.capacity()) {
            result.set(MyComponents.BOOK_COMPONENT, new BookData(4, level, 100, 10, Integer.parseInt("ffffff", 16)));
        } else if (level > UPGRADE_2.capacity() || inputStack.is(ItemRegistry.XP_BOOK3)) {
            result.set(MyComponents.BOOK_COMPONENT, UPGRADE_3);
        } else if (level > UPGRADE_1.capacity() || inputStack.is(ItemRegistry.XP_BOOK2)) {
            result.set(MyComponents.BOOK_COMPONENT, UPGRADE_2);
        } else {
            result.set(MyComponents.BOOK_COMPONENT, UPGRADE_1);
        }
 
        return result;
    }
 
    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }
 
    @Override
    public String group() {
        return "";
    }
 
    @Override
    public boolean showNotification() {
        return true;
    }
 
    @Override
    public RecipeSerializer<XpBookDeprecateRecipe> getSerializer() {
        return SERIALIZER;
    }
 
    @Override
    public CraftingBookCategory category() {
        return CraftingBookCategory.MISC;
    }
 
    public static final RecipeSerializer<XpBookDeprecateRecipe> SERIALIZER = new RecipeSerializer<>(
            RecordCodecBuilder.mapCodec(
                    instance -> instance.group(
                                    Ingredient.CODEC.fieldOf("base").forGetter(recipe -> recipe.base)
                            )
                            .apply(instance, XpBookDeprecateRecipe::new)
            ),
            StreamCodec.composite(
                    Ingredient.CONTENTS_STREAM_CODEC,
                    recipe -> recipe.base,
                    XpBookDeprecateRecipe::new
            )
    );
}
