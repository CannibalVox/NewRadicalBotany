package net.technicpack.newradicalbotany.coremod;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

import java.util.Map;

public class NewRadicalBotanyCoremod implements IFMLLoadingPlugin {
    @Override
    public String[] getASMTransformerClass() {
        return new String[] { "net.technicpack.newradicalbotany.coremod.NewRadicalBotanyClassTransformer" };
    }

    @Override
    public String getModContainerClass() {
        return "net.technicpack.newradicalbotany.NewRadicalBotany";
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {

    }

    @Override
    public String getAccessTransformerClass() {
        return "net.technicpack.newradicalbotany.coremod.NewRadicalBotanyAccessTransformer";
    }
}
