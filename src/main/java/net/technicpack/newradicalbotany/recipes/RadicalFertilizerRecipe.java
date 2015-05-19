package net.technicpack.newradicalbotany.recipes;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.technicpack.newradicalbotany.NewRadicalBotany;

import java.util.ArrayList;
import java.util.Collections;

public class RadicalFertilizerRecipe implements IRecipe {

    private Item botaniaDye;
    private ItemStack bonemeal;
    private ItemStack outputFertilizer;

    public RadicalFertilizerRecipe() {
        this.botaniaDye = (Item)Item.itemRegistry.getObject("Botania:dye");
        this.bonemeal = new ItemStack(Items.dye, 1, 15);
        this.outputFertilizer = new ItemStack(NewRadicalBotany.fertilizer, 1);
    }

    @Override
    public boolean matches(InventoryCrafting inventory, World p_77569_2_) {
        boolean foundBonemeal = false;
        int foundBotaniaDyes = 0;
        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (stack == null)
                continue;

            if (!foundBonemeal && stack.getItem() == bonemeal.getItem() && stack.getItemDamage() == bonemeal.getItemDamage()) {
                foundBonemeal = true;
                continue;
            } else if (foundBotaniaDyes < 4 && stack.getItem() == botaniaDye) {
                foundBotaniaDyes++;
                continue;
            }

            return false;
        }

        return foundBonemeal && foundBotaniaDyes == 4;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventory) {
        ItemStack output = outputFertilizer.copy();

        NBTTagCompound compound = new NBTTagCompound();
        ArrayList<Integer> flowerList = new ArrayList<Integer>(4);

        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (stack == null || stack.getItem() != botaniaDye)
                continue;

            int flower = stack.getItemDamage();
            if (!flowerList.contains(flower))
                flowerList.add(flower);
        }

        Collections.sort(flowerList);

        int[] finalList = new int[flowerList.size()];

        for (int i = 0; i < finalList.length; i++) {
            finalList[i] = flowerList.get(i);
        }

        compound.setIntArray("Flowers", finalList);

        output.setTagCompound(compound);
        return output;
    }

    @Override
    public int getRecipeSize() {
        return 5;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return outputFertilizer;
    }
}
