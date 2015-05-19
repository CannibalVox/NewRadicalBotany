package net.technicpack.newradicalbotany.items;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
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

    public ItemRadicalFertilizer() {
        super();

        try {
            Class botaniaClientProxy = Class.forName("vazkii.botania.client.core.proxy.ClientProxy");
            this.botaniaClientProxy = NewRadicalBotany.proxy.initializeBotaniaProxy(botaniaClientProxy);
            wispFxMethod = botaniaClientProxy.getMethod("wispFX", World.class, double.class, double.class, double.class, float.class, float.class, float.class, float.class, float.class);
        } catch (ClassNotFoundException ex) {
            //Eat this- we just won't be able to create wisps
        } catch (NoSuchMethodException ex) {
            //Eat this- we just won't be able to create wisps
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
        return random.nextInt(16);
    }
}
