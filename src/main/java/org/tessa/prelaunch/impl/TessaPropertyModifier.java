package org.tessa.prelaunch.impl;
import org.tessa.prelaunch.api.PropertyModifier;
import static org.tessa.prelaunch.TessaPreLaunch.logger;
import net.fabricmc.loader.api.FabricLoader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;
import java.util.Map;
import java.util.HashMap;
public class TessaPropertyModifier implements PropertyModifier {
    private final Properties properties;
    private final File propertiesFile;
    private static final String ERROR_TEMPLATE = "PropertyModifier Error. State -> Properties: %s \n Properties File: %s \n File Exists? : %s \n Method: %s";
    public TessaPropertyModifier() {
        this.properties = new Properties();
        this.propertiesFile = makeIfNotExists("tessa.properties");
        readProperties();
    }

    public TessaPropertyModifier(String fileName) {
        this.properties = new Properties();
        this.propertiesFile = makeIfNotExists("tessa." + fileName + ".properties");
        readProperties();
    }

    private String propertiesAsString() {
        StringBuilder builder = new StringBuilder();
        properties.forEach((k, v) -> builder.append(k).append(":").append(v).append("\n"));
        return builder.toString();
    }

    private void logError(String methodName, Exception e) {
        logger.error(String.format(ERROR_TEMPLATE,
                propertiesAsString(),
                this.propertiesFile.getAbsolutePath(),
                this.propertiesFile.exists(),
                methodName), e);
    }
    private void readProperties() {
        try (var reader = Files.newBufferedReader(this.propertiesFile.toPath())) {
            properties.load(reader);
        } catch (IOException e) {
            logError("readProperties", e);
        }
    }

    private void writeProperties() {
        try (var writer = Files.newBufferedWriter(this.propertiesFile.toPath())) {
            properties.store(writer, "Tessa Properties");
        } catch (IOException e) {
            logError("writeProperties", e);
        }
    }

    private File makeIfNotExists(String fileName) {
        File configFolder = FabricLoader.getInstance().getConfigDir().toFile();
        configFolder.mkdirs();
        File file = new File(configFolder.getAbsolutePath(), fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                logger.error(String.format(ERROR_TEMPLATE,
                        propertiesAsString(),
                        this.propertiesFile.getAbsolutePath(),
                        this.propertiesFile.exists(),
                        "makeIfNotExists"));
            }
        }
        return file;
    }

    @Override
    public String toString() {
        return String.format("PropertyModifier (key,value) pairs: %s", propertiesAsString());
    }

    @Override
    public Map<String, String> asMap() {
        HashMap<String, String> map = new HashMap<>();
        properties.forEach((k, v) -> map.put((String) k, (String) v));
        return map;
    }
    @Override
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    @Override
    public TessaPropertyModifier setProperty(String key, String value, boolean write) {
        properties.setProperty(key, value);
        if(write) writeProperties();
        return this;
    }

    @Override
    public TessaPropertyModifier unsetProperty(String key, boolean write) {
        setProperty(key, "", write);
        return this;
    }

    @Override
    public TessaPropertyModifier removeProperty(String key, boolean write) {
        properties.remove(key);
        if(write) writeProperties();
        return this;
    }

    @Override
    public TessaPropertyModifier write() {
        writeProperties();
        return this;
    }

}
