package me.mahmutkocas.pixelmon.gtsemc;

import me.mahmutkocas.pixelmon.gtsemc.shop.Shop;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.*;
import org.apache.logging.log4j.Logger;

@Mod(modid = GtsEmcMain.MODID, name = GtsEmcMain.NAME, version = GtsEmcMain.VERSION, dependencies = "after:pixelmon;after:projecte", acceptableRemoteVersions = "*")
public class GtsEmcMain
{
    public static final String MODID = "gtsemc";
    public static final String NAME = "ProjectE GTS Currency";
    public static final String VERSION = "1.0";
    public static MinecraftServer server;
    public static Shop shop;
    private static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        System.out.println("GTS EMC INIT");
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(Shop.onJoin.class);
    }

    @EventHandler
    public void postInit(FMLServerStartingEvent event) {
        server = event.getServer();
        shop = new Shop();
        event.registerServerCommand(new Command());
    }
}
