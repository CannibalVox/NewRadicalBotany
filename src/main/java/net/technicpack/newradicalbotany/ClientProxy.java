package net.technicpack.newradicalbotany;

public class ClientProxy extends CommonProxy {
    @Override
    public Class getBotaniaClientProxyClass() throws ClassNotFoundException {
        return Class.forName("vazkii.botania.client.core.proxy.ClientProxy");
    }

    @Override
    public Object initializeBotaniaProxy(Class proxyClass) {
        try {
            return proxyClass.newInstance();
        } catch (InstantiationException ex) {
            return null;
        } catch (IllegalAccessException ex) {
            return null;
        }
    }
}
