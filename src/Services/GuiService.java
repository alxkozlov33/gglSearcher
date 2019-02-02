package Services;


import GUI.*;

public class GuiService {
    private Bootstrapper bootstrapper;
    public GuiService(Bootstrapper frame) {
        bootstrapper = frame;
    }

    public Bootstrapper getBootstrapper() {
        return bootstrapper;
    }
}
