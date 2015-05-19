package net.technicpack.newradicalbotany;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.technicpack.newradicalbotany.items.ItemRadicalFertilizer;

@Mod(modid = NewRadicalBotany.MODID, version = NewRadicalBotany.VERSION, dependencies = "required-after:Botania")
public class NewRadicalBotany {
    public static final String MODID = "newradicalbotany";
    public static final String VERSION = "GRADLE:VERSION-GRADLE:BUILD";

    @Mod.Instance
    public static NewRadicalBotany instance;

    @SidedProxy(clientSide = "net.technicpack.newradicalbotany.ClientProxy", serverSide = "net.technicpack.newradicalbotany.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Item fertilizer = new ItemRadicalFertilizer().setTextureName("fertilizer").setUnlocalizedName("fertilizer").setCreativeTab(CreativeTabs.tabMisc);
        GameRegistry.registerItem(fertilizer, "fertilizer");
    }
}
