package net.technicpack.newradicalbotany;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.technicpack.newradicalbotany.items.ItemRadicalFertilizer;
import net.technicpack.newradicalbotany.recipes.RadicalFertilizerRecipe;

@Mod(modid = NewRadicalBotany.MODID, version = NewRadicalBotany.VERSION, dependencies = "required-after:Botania")
public class NewRadicalBotany {
    public static final String MODID = "newradicalbotany";
    public static final String VERSION = "GRADLE:VERSION-GRADLE:BUILD";

    public static Item fertilizer;

    @Mod.Instance
    public static NewRadicalBotany instance;

    @SidedProxy(clientSide = "net.technicpack.newradicalbotany.ClientProxy", serverSide = "net.technicpack.newradicalbotany.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        fertilizer = new ItemRadicalFertilizer().setTextureName("fertilizer").setUnlocalizedName("fertilizer").setCreativeTab(CreativeTabs.tabMisc);
        GameRegistry.registerItem(fertilizer, "fertilizer");
        GameRegistry.addRecipe(new RadicalFertilizerRecipe());
    }
}
