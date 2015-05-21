package net.technicpack.newradicalbotany.coremod;

import cpw.mods.fml.common.asm.transformers.AccessTransformer;
import net.technicpack.newradicalbotany.NewRadicalBotany;

import java.io.IOException;

public class NewRadicalBotanyAccessTransformer extends AccessTransformer {
    public NewRadicalBotanyAccessTransformer() throws IOException {
        super("newradicalbotany_at.cfg");
    }
}
