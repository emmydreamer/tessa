package org.tessa.prelaunch.api;
import org.tessa.prelaunch.impl.TessaQuickPlay;

public interface QuickPlay {
    static QuickPlay play() { return new TessaQuickPlay();}
    QuickPlay setServerAddress(String serverAddress);
    QuickPlay unsetServerAddress();
    QuickPlay removeServerAddressKey(boolean writeToFile);
}
