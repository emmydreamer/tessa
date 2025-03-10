package org.tessa.mixin.client;
import net.minecraft.client.main.Main;
import net.minecraft.client.main.GameConfig;
import com.mojang.blaze3d.platform.DisplayData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
@Mixin(Main.class)
public abstract class ServerConnectMixin {

	@Redirect(method = "main", at = @At(value = "NEW", target = "net/minecraft/client/main/GameConfig"))
	private static GameConfig redirectGameConfigCreation(GameConfig.UserData userData, DisplayData displayData, GameConfig.FolderData folderData, GameConfig.GameData gameData, GameConfig.QuickPlayData quickPlayData) {
		File quickPlayProperties = new File("config", "tessa.quickplay.properties");
		String serverAddress = null;
		try (BufferedReader br = new BufferedReader(new FileReader(quickPlayProperties.getAbsolutePath()))){
			String line;
			while((line = br.readLine()) != null) {
				switch(line.split("=")[0]) {
					case "server_address" :
						serverAddress = line.split("=").length > 1 ? line.split("=")[1] : quickPlayData.multiplayer();
					default:
						break;
				}
			}
		} catch(IOException e) {

		}
		GameConfig.QuickPlayData modifiedQuickPlayData = new GameConfig.QuickPlayData(
				quickPlayData.path(),
				quickPlayData.singleplayer(),
				serverAddress,
				quickPlayData.realms()
		);

		// Return a new customized GameConfig
		return new GameConfig(userData, displayData, folderData, gameData, modifiedQuickPlayData);
	}
}