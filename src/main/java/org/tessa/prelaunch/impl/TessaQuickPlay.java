package org.tessa.prelaunch.impl;

import org.tessa.prelaunch.api.QuickPlay;
import org.tessa.prelaunch.api.PropertyModifier;

public class TessaQuickPlay implements QuickPlay {

    private final PropertyModifier properties;

    public TessaQuickPlay() {
        properties = PropertyModifier.modify("quickplay");
    }


    @Override
    public QuickPlay setServerAddress(String serverAddress) {
        properties.setProperty("server_address", serverAddress, true);
        return this;
    }

    @Override
    public QuickPlay unsetServerAddress() {
        properties.unsetProperty("server_address", true);
        return this;
    }

    @Override
    public QuickPlay removeServerAddressKey(boolean writeToFile) {
        return this;
    }



}
