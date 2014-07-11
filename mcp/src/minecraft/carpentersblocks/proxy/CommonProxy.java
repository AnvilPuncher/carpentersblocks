package carpentersblocks.proxy;

import net.minecraftforge.common.MinecraftForge;
import carpentersblocks.util.bed.BedDesignHandler;
import carpentersblocks.util.flowerpot.FlowerPotDesignHandler;
import carpentersblocks.util.flowerpot.FlowerPotHandler;
import carpentersblocks.util.handler.EventHandler;
import carpentersblocks.util.handler.ExtendedPlantHandler;
import carpentersblocks.util.handler.OverlayHandler;
import carpentersblocks.util.handler.PatternHandler;
import carpentersblocks.util.registry.BlockRegistry;
import carpentersblocks.util.registry.FeatureRegistry;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {

    public void registerHandlers(FMLPreInitializationEvent event)
    {
        if (FeatureRegistry.enableExtendedPlantSupport) {
            FeatureRegistry.enableExtendedPlantSupport = ExtendedPlantHandler.init();
        }

        if (BlockRegistry.enableFlowerPot) {
            FlowerPotHandler.initPlantProfiles();
            FlowerPotDesignHandler.init(event);
        }

        if (FeatureRegistry.enableOverlays) {
            OverlayHandler.init();
        }

        if (FeatureRegistry.enablePatterns) {
            PatternHandler.init(event);
        }

        if (BlockRegistry.enableBed) {
            BedDesignHandler.init(event);
        }

        MinecraftForge.EVENT_BUS.register(new EventHandler());
    }

    public void registerRenderInformation(FMLPreInitializationEvent event) { }

}
