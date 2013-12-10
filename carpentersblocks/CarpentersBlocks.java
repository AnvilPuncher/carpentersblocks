package carpentersblocks;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.Configuration;
import carpentersblocks.proxy.CommonProxy;
import carpentersblocks.util.CarpentersBlocksTab;
import carpentersblocks.util.ModLogger;
import carpentersblocks.util.handler.TileEntityHandler;
import carpentersblocks.util.registry.BlockRegistry;
import carpentersblocks.util.registry.FeatureRegistry;
import carpentersblocks.util.registry.ItemRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

@Mod(
		modid = "CarpentersBlocks",
		name = "Carpenter's Blocks",
		version = "v1.9.7"
		)
@NetworkMod(
		clientSideRequired = true,
		serverSideRequired = false
		)
public class CarpentersBlocks
{

	@Instance("CarpentersBlocks")
	public static CarpentersBlocks instance;

	@SidedProxy(clientSide = "carpentersblocks.proxy.ClientProxy", serverSide = "carpentersblocks.proxy.CommonProxy")
	public static CommonProxy proxy;

	public static CreativeTabs tabCarpentersBlocks = new CarpentersBlocksTab("carpentersBlocks");

	@PreInit
	public void preInit(FMLPreInitializationEvent event)
	{
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());

		config.load();
		FeatureRegistry.initFeatures(event, config);
		BlockRegistry.initBlocks(event, config);
		ItemRegistry.initItems(event, config);
		config.save();

		ModLogger.init();

		proxy.registerHandlers(event);
		proxy.registerRenderInformation(event);
	}

	@Init
	public void init(FMLInitializationEvent event)
	{
		TileEntityHandler.registerTileEntities();
		BlockRegistry.registerBlocks();
		ItemRegistry.registerItems();
	}

}
