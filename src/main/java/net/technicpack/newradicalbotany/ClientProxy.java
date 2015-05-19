package net.technicpack.newradicalbotany;

public class ClientProxy extends CommonProxy {
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
