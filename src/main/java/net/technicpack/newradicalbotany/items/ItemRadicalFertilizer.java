package net.technicpack.newradicalbotany.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.technicpack.newradicalbotany.NewRadicalBotany;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ItemRadicalFertilizer extends Item {

    private Object botaniaClientProxy = null;
    private Method wispFxMethod = null;

    private IIcon[] icons = new IIcon[4];

    public ItemRadicalFertilizer() {
        super();

        try {
            Class botaniaClientProxy = NewRadicalBotany.proxy.getBotaniaClientProxyClass();
            if (botaniaClientProxy != null)
                this.botaniaClientProxy = NewRadicalBotany.proxy.initializeBotaniaProxy(botaniaClientProxy);
            if (this.botaniaClientProxy != null)
                wispFxMethod = botaniaClientProxy.getMethod("wispFX", World.class, double.class, double.class, double.class, float.class, float.class, float.class, float.class, float.class);
        } catch (ClassNotFoundException ex) {
            //Eat this- we just won't be able to create wisps
        } catch (NoSuchMethodException ex) {
            //Eat this- we just won't be able to create wisps
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister register) {
        for (int i = 0; i < 4; i++) {
            icons[i] = register.registerIcon("newradicalbotany:fertilizer"+Integer.toString(i));
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getRenderPasses(int metadata)
    {
        return 4;
    }

    /**
     * Gets an icon index based on an item's damage value and the given render pass
     */
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamageForRenderPass(int p_77618_1_, int p_77618_2_)
    {
        return p_77618_2_ >= 4 ? icons[0] : icons[p_77618_2_];
    }

    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack itemStack, int pass)
    {
        int[] flowers = getFlowerArray(itemStack);

        if (flowers != null && flowers.length > 0 && flowers.length <= 4) {
            pass %= flowers.length;
            return MapColor.getMapColorForBlockColored(flowers[pass]).colorValue;
        }

        switch (pass) {
            case 1:
                return 0x3A7C24;
            case 2:
                return 0x9D3E12;
            case 3:
                return 0x9D92A8;
            default:
                return 0x82D775;
        }
    }

    private static final StringBuilder allFlowersBuilder = new StringBuilder();
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List info, boolean advanced) {
        info.add("");
        int[] flowers = getFlowerArray(stack);
        if (flowers == null || flowers.length == 0) {
            String allFlowers = StatCollector.translateToLocal("gui.newradicalbotany.fertilizer.all");
            allFlowersBuilder.delete(0, allFlowersBuilder.length());
            for (int i = 0; i < allFlowers.length(); i++) {
                int color = player.worldObj.rand.nextInt(15)+1;
                allFlowersBuilder.append("\u00a7");
                allFlowersBuilder.append(Integer.toHexString(color));
                allFlowersBuilder.append(allFlowers.charAt(i));
            }
            info.add(allFlowersBuilder.toString());
            return;
        }

        info.add(StatCollector.translateToLocal("gui.newradicalbotany.fertilizer.some"));
        for (int i = 0; i < flowers.length; i++) {
            info.add(
                " \u00a79* \u00a7" +
                Integer.toHexString(blockColorToTextColor(flowers[i])) +
                StatCollector.translateToLocal("tile.botania:flower"+Integer.toString(flowers[i])+".name")
            );
        }
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        final int range = 3;
        final Block botaniaFlower = (Block)Block.getBlockFromName("Botania:flower");

        if (botaniaFlower == null)
            return false;

        if (!world.isRemote) {
            List<ChunkCoordinates> validCoords = new ArrayList<ChunkCoordinates>();

            for (int i = -range - 1; i < range; i++) {
                for (int j = -range - 1; j < range; j++) {
                    for (int k = 2; k >= -2; k--) {
                        int newX = x + i + 1;
                        int newY = y + k + 1;
                        int newZ = z + j + 1;
                        if (world.isAirBlock(newX, newY, newZ) && (!world.provider.hasNoSky || y < 255) && botaniaFlower.canBlockStay(world, newX, newY, newZ))
                            validCoords.add(new ChunkCoordinates(newX, newY, newZ));
                    }
                }
            }

            int flowerCount = Math.min(validCoords.size(), world.rand.nextBoolean() ? 3 : 4);
            for (int i = 0; i < flowerCount; i++) {
                ChunkCoordinates coords = validCoords.get(world.rand.nextInt(validCoords.size()));
                validCoords.remove(coords);
                world.setBlock(coords.posX, coords.posY, coords.posZ, botaniaFlower, getFlowerColor(itemStack, world.rand), 3);
            }

            itemStack.stackSize--;
        } else {
            if (wispFxMethod != null && botaniaClientProxy != null) {
                for (int i = 0; i < 15; i++) {
                    double fxX = x - range + world.rand.nextInt(range * 2 + 1) + Math.random();
                    double fxY = y + 1;
                    double fxZ = z - range + world.rand.nextInt(range * 2 + 1) + Math.random();
                    float red = (float) Math.random();
                    float green = (float) Math.random();
                    float blue = (float) Math.random();

                    try {
                        wispFxMethod.invoke(botaniaClientProxy, world, fxX, fxY, fxZ, red, green, blue, 0.15f + (float) Math.random() * 0.25f, -(float) Math.random() * 0.1f - 0.05f);
                    } catch (InvocationTargetException ex) {
                        ex.printStackTrace();
                    } catch (IllegalAccessException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }

        return true;
    }

    private int getFlowerColor(ItemStack stack, Random random) {
        int[] flowers = getFlowerArray(stack);

        if (flowers != null && flowers.length != 0) {
            return flowers[random.nextInt(flowers.length)];
        }
        return random.nextInt(16);
    }

    private int[] getFlowerArray(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();

        if (tag != null && tag.hasKey("Flowers", 11))
            return tag.getIntArray("Flowers");

        return null;
    }

    private int blockColorToTextColor(int color) {
        switch(color) {
            case 0:
                return 15;
            case 1:
                return 6;
            case 2:
                return 13;
            case 3:
                return 9;
            case 4:
                return 14;
            case 5:
                return 10;
            case 6:
                return 13;
            case 7:
                return 8;
            case 8:
                return 7;
            case 9:
                return 11;
            case 10:
                return 5;
            case 11:
                return 1;
            case 12:
                return 6;
            case 13:
                return 2;
            case 14:
                return 12;
            case 15:
                return 0;
            default:
                return 7;
        }
    }
}
