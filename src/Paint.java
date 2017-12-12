import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.canvas.paint.Painter;

import java.awt.*;

public class Paint implements Painter{

    private HelloAlcher hostClass;

    Paint(HelloAlcher hostClass){
        this.hostClass = hostClass;
    }

    @Override
    public void onPaint(Graphics2D graphics2D) {
        if(hostClass != null){
            graphics2D.setColor(Color.MAGENTA);
            long startTime = hostClass.getStartTime();
            long runTime = System.currentTimeMillis() - startTime;

            int gainedXp = hostClass.getExperienceTracker().getGainedXP(Skill.MAGIC);
            int XPH = hostClass.getExperienceTracker().getGainedXPPerHour(Skill.MAGIC);

            graphics2D.drawString("runtime: " + formatTime(runTime), 340, 330);
            graphics2D.drawString("gainedXP " + formatValue(gainedXp) + " XPH: " + formatValue(XPH), 240, 295 );
        }

    }

    private String formatTime(final long ms){
        long s = ms / 1000, m = s / 60, h = m / 60;
        s %= 60; m %= 60; h %= 24;
        return String.format("%02d:%02d:%02d", h, m, s);
    }

    private String formatValue(final long l) {
        return (l > 1_000_000) ? String.format("%.2fm", ((double) l / 1_000_000))
                : (l > 1000) ? String.format("%.1fk", ((double) l / 1000))
                : l + "";
    }
}
