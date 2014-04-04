import java.awt.*;

public class AcornBooty extends Booty {
    public AcornBooty(Catamaran a, Image img, int x, int y)
    {
        super(a, img, x, y);
        width = 40;
        height = 40;
    }

    public void paint(Graphics g)
    {
        g.drawImage(treasure, locx-app.vleft, locy, width, height, null);
        g.drawRect(locx-app.vleft, locy, width, height);
    }
}
