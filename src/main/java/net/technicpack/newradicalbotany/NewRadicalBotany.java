package net.technicpack.newradicalbotany;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import cpw.mods.fml.common.*;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.versioning.DefaultArtifactVersion;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.technicpack.newradicalbotany.coremod.NewRadicalBotanyResourcePack;
import net.technicpack.newradicalbotany.items.ItemRadicalFertilizer;
import net.technicpack.newradicalbotany.recipes.RadicalFertilizerRecipe;

import java.io.File;

public class NewRadicalBotany extends DummyModContainer {
    public static final String MODID = "newradicalbotany";
    public static final String VERSION = "GRADLE:VERSION";

    public static Item fertilizer;

    @Mod.Instance
    public static NewRadicalBotany instance;

    @SidedProxy(clientSide = "net.technicpack.newradicalbotany.ClientProxy", serverSide = "net.technicpack.newradicalbotany.CommonProxy")
    public static CommonProxy proxy;

    public NewRadicalBotany() {
        super(new ModMetadata());

        ModMetadata metadata = getMetadata();
        metadata.modId = NewRadicalBotany.MODID;
        metadata.version = NewRadicalBotany.VERSION;
        metadata.name = "New Radical Botany";
        metadata.authorList = ImmutableList.of("Cannibalvox");
        metadata.url = "http://www.technicpack.net/";
        metadata.credits = "Developed by Technic";
        metadata.description = "Can't forget, you only get what you give.";
        metadata.requiredMods.add(new DefaultArtifactVersion("Botania", false));
        metadata.dependencies.add(new DefaultArtifactVersion("Botania", false));
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller) {
        bus.register(this);
        return true;
    }

    @Override
    public Class<?> getCustomResourcePackClass() { return NewRadicalBotanyResourcePack.class; }

    @Subscribe
    public void preInit(FMLPreInitializationEvent event) {
        if (FMLCommonHandler.instance().getSide().isClient())
            proxy = new ClientProxy();
        else
            proxy = new CommonProxy();

        fertilizer = new ItemRadicalFertilizer().setTextureName("fertilizer").setUnlocalizedName("fertilizer").setCreativeTab(CreativeTabs.tabMisc);
        GameRegistry.registerItem(fertilizer, "fertilizer");
        GameRegistry.addRecipe(new RadicalFertilizerRecipe());
    }
}
