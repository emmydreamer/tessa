package org.tessa.prelaunch.api;
import org.tessa.prelaunch.impl.TessaPropertyModifier;
import java.util.Map;
public interface PropertyModifier {

    static PropertyModifier modify(String namespace) { return new TessaPropertyModifier(namespace);}

    static PropertyModifier modifyDefault() { return new TessaPropertyModifier();}

    Map<String,String> asMap();

    String getProperty(String key, String defaultValue);

    TessaPropertyModifier setProperty(String key, String value, boolean write);

    TessaPropertyModifier unsetProperty(String key, boolean write);

    TessaPropertyModifier removeProperty(String key, boolean write);

    TessaPropertyModifier write();
}
