
import View.Frame;
import View.Panel;


public class Program {
    
    Frame frame;
    Panel panel;

    int width, height;

    public Program() {

        width = 1280;
        height = 720;

        panel = new Panel(width, height);
        frame = new Frame(panel); 

    }

}
