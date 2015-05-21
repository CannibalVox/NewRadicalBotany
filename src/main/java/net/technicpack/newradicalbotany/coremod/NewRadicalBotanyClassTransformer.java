package net.technicpack.newradicalbotany.coremod;

import static org.objectweb.asm.Opcodes.*;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

public class NewRadicalBotanyClassTransformer implements IClassTransformer {
    @Override
    public byte[] transform(String s, String s1, byte[] bytes) {
        if (bytes == null)
            return null;

        if (s1.equals("vazkii.botania.common.block.subtile.SubTilePureDaisy")) {
            ClassReader reader = new ClassReader(bytes);
            ClassNode node = new ClassNode(ASM5);
            reader.accept(node, 0);

            l: {
                for (MethodNode method : node.methods) {
                    if (method.name.equals("onUpdate")) {
                        for (int i = 0; i < method.instructions.size(); i++) {
                            AbstractInsnNode instruction = method.instructions.get(i);
                            if (instruction.getOpcode() == RETURN) {
                                method.instructions.insertBefore(instruction, new VarInsnNode(ALOAD, 0));
                                method.instructions.insertBefore(instruction, new FieldInsnNode(GETFIELD, "vazkii/botania/api/subtile/SubTileEntity", "supertile", "Lnet/minecraft/tileentity/TileEntity;"));
                                MethodInsnNode methodInstruction = new MethodInsnNode(INVOKESTATIC, "net/technicpack/newradicalbotany/coremodsupport/BiomeCleanseSupport", "cleanseBiomeAroundTile", "(Lnet/minecraft/tileentity/TileEntity;)V", false);
                                method.instructions.insertBefore(instruction, methodInstruction);
                                break;
                            }
                        }
                    }
                }

                ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
                node.accept(writer);
                bytes = writer.toByteArray();
            }
        }

        return bytes;
    }
}
