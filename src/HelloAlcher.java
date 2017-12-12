import org.osbot.rs07.api.Magic;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.api.ui.Spells;
import org.osbot.rs07.script.MethodProvider;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

import java.awt.Point;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@ScriptManifest(author = "PayPalMeRSGP", name = "Hello World Alcher", info = "Basically an Autoclicker", version = 0.3, logo = "")
public class HelloAlcher extends Script {

    //statistics variables for normal dist
    private static final double RS_GAME_TICK_MS = 603;
    private static final double BETWEEN_ALCH_STDDEV_MS = 20;
    private static final double MID_CAST_MEAN_MS = 215;
    private static final double MID_CAST_STDDEV_MS = 600;

    //for small mouse move after a random number
    private static final Point LOWER_BOTTOM_LEFT_BOUND = new Point(709,336);
    private static final Point UPPER_TOP_RIGHT_BOUND = new Point(720,321);
    private int randomMouseMoveCountdown;

    //Variables for Paint
    private long startTime;



    private Magic spellChecker;
    @Override
    public final void onStart() {
        startTime = System.currentTimeMillis();
        getExperienceTracker().start(Skill.MAGIC);
        getBot().addPainter(new Paint(HelloAlcher.this));
        this.spellChecker = getMagic();
        this.randomMouseMoveCountdown = ThreadLocalRandom.current().nextInt(750, 1001);
    }

    @Override
    public final int onLoop() throws InterruptedException{

        return castHighAlch();
    }

    private int castHighAlch() throws InterruptedException{
        if(playerCanCastHighAlch()){
            if(randomMouseMove()){
                log("random mouse triggered");
            };
            spellChecker.castSpell(Spells.NormalSpells.HIGH_LEVEL_ALCHEMY);
            MethodProvider.sleep(randomNormalDist(MID_CAST_MEAN_MS, BETWEEN_ALCH_STDDEV_MS, true)); //double click interval between clicking high alch and casting spell on item
            if(spellChecker.isSpellSelected()){
                getInventory().interact("Cast", "Magic longbow");
            }
            return (int) randomNormalDist(RS_GAME_TICK_MS * 5, MID_CAST_STDDEV_MS, false); //alch takes 5 game ticks before next alch can be cast
        }
        else{
            stop();
            return ThreadLocalRandom.current().nextInt(1000,5000);
        }
    }

    private boolean randomMouseMove(){
        this.randomMouseMoveCountdown--;
        if(this.randomMouseMoveCountdown <= 0){
            this.randomMouseMoveCountdown = ThreadLocalRandom.current().nextInt(750, 1001);
            int randX = ThreadLocalRandom.current().nextInt(LOWER_BOTTOM_LEFT_BOUND.x, UPPER_TOP_RIGHT_BOUND.x);
            int randY = ThreadLocalRandom.current().nextInt(LOWER_BOTTOM_LEFT_BOUND.y, UPPER_TOP_RIGHT_BOUND.y);
            getMouse().move(randX, randY);
            return true;
        }
        return false;
    }

    private boolean playerCanCastHighAlch() throws InterruptedException {
        return spellChecker != null && spellChecker.canCast(Spells.NormalSpells.HIGH_LEVEL_ALCHEMY, false);
    }

    private long randomNormalDist(double mean, double stddev, boolean midCast){
        long debug = (long) ((new Random().nextGaussian() * stddev + mean));
        if(midCast){
            log("double click interval: "+debug);
        }
        else{
            log("midcast interval: "+debug);
        }

        return Math.abs(debug);
    }

    public long getStartTime() {
        return startTime;
    }

}
