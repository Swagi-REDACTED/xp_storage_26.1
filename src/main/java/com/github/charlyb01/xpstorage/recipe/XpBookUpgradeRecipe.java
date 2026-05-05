package com.github.charlyb01.xpstorage.recipe;
 
import com.github.charlyb01.xpstorage.item.XpBook;
import com.github.charlyb01.xpstorage.component.BookData;
import com.github.charlyb01.xpstorage.component.MyComponents;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
 
import java.util.List;
import java.util.Optional;
 
public class XpBookUpgradeRecipe implements SmithingRecipe {
    final Optional<Ingredient> template;
    final Optional<Ingredient> base;
    final Optional<Ingredient> addition;
    private final int baseLevel;
    private final int resultCapacity;
    private final int resultXpFromUsing;
    private final int resultXpFromBrewing;
    private final int resultBarColor;
    @Nullable
    private PlacementInfo placementInfo;
 
    public XpBookUpgradeRecipe(Optional<Ingredient> template, Optional<Ingredient> base, Optional<Ingredient> addition,
                               int baseLevel, int resultCapacity, int resultXpFromUsing, int resultXpFromBrewing,
                               int resultBarColor) {
        this.template = template;
        this.base = base;
        this.addition = addition;
        this.baseLevel = baseLevel;
        this.resultCapacity = resultCapacity;
        this.resultXpFromUsing = resultXpFromUsing;
        this.resultXpFromBrewing = resultXpFromBrewing;
        this.resultBarColor = resultBarColor;
    }
 
    @Override
    public boolean matches(SmithingRecipeInput smithingRecipeInput, Level world) {
        return this.testBase(smithingRecipeInput.base())
                && Ingredient.testOptionalIngredient(this.templateIngredient(), smithingRecipeInput.template())
                && Ingredient.testOptionalIngredient(this.additionIngredient(), smithingRecipeInput.addition());
    }
 
    @Override
    public ItemStack assemble(SmithingRecipeInput input) {
        ItemStack base = input.base();
        int level = XpBook.getBookLevel(base);
        ItemStack result = base.copyWithCount(1);
        result.set(MyComponents.BOOK_COMPONENT, new BookData(level + 1, this.resultCapacity,
                this.resultXpFromUsing, this.resultXpFromBrewing, this.resultBarColor));
        return result;
    }
 
    public boolean testBase(ItemStack stack) {
        if (!this.baseIngredient().test(stack)) return false;
 
        int level = stack.getOrDefault(MyComponents.BOOK_COMPONENT, BookData.getDefault()).level();
        return level == this.baseLevel;
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
    public Optional<Ingredient> templateIngredient() {
        return this.template;
    }
 
    @Override
    public Ingredient baseIngredient() {
        return this.base.orElseGet(Ingredient::of);
    }
 
    @Override
    public Optional<Ingredient> additionIngredient() {
        return this.addition;
    }
 
    @Override
    public PlacementInfo placementInfo() {
        if (this.placementInfo == null) {
            this.placementInfo = PlacementInfo.createFromOptionals(List.of(this.template, this.base, this.addition));
        }
 
        return this.placementInfo;
    }
 
    @Override
    public RecipeSerializer<XpBookUpgradeRecipe> getSerializer() {
        return SERIALIZER;
    }
 
    public static final RecipeSerializer<XpBookUpgradeRecipe> SERIALIZER = new RecipeSerializer<>(
            RecordCodecBuilder.mapCodec(
                    instance -> instance.group(
                                    Ingredient.CODEC.optionalFieldOf("template").forGetter(recipe -> recipe.template),
                                    Ingredient.CODEC.optionalFieldOf("base").forGetter(recipe -> recipe.base),
                                    Ingredient.CODEC.optionalFieldOf("addition").forGetter(recipe -> recipe.addition),
                                    Codec.INT.fieldOf("baseLevel").forGetter(recipe -> recipe.baseLevel),
                                    Codec.INT.fieldOf("resultCapacity").forGetter(recipe -> recipe.resultCapacity),
                                    Codec.INT.fieldOf("resultXpFromUsing").forGetter(recipe -> recipe.resultXpFromUsing),
                                    Codec.INT.fieldOf("resultXpFromBrewing").forGetter(recipe -> recipe.resultXpFromBrewing),
                                    Codec.INT.fieldOf("resultBarColor").forGetter(recipe -> recipe.resultBarColor)
                            )
                            .apply(instance, XpBookUpgradeRecipe::new)
            ),
            StreamCodec.composite(
                    Ingredient.OPTIONAL_CONTENTS_STREAM_CODEC,
                    recipe -> recipe.template,
                    Ingredient.OPTIONAL_CONTENTS_STREAM_CODEC,
                    recipe -> recipe.base,
                    Ingredient.OPTIONAL_CONTENTS_STREAM_CODEC,
                    recipe -> recipe.addition,
                    ByteBufCodecs.VAR_INT,
                    recipe -> recipe.baseLevel,
                    ByteBufCodecs.VAR_INT,
                    recipe -> recipe.resultCapacity,
                    ByteBufCodecs.VAR_INT,
                    recipe -> recipe.resultXpFromUsing,
                    ByteBufCodecs.VAR_INT,
                    recipe -> recipe.resultXpFromBrewing,
                    ByteBufCodecs.VAR_INT,
                    recipe -> recipe.resultBarColor,
                    XpBookUpgradeRecipe::new
            )
    );
}
