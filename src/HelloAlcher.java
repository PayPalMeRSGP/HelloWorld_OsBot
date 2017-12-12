import org.osbot.rs07.api.Magic;
import org.osbot.rs07.api.ui.Spells;
import org.osbot.rs07.script.MethodProvider;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;
import java.util.Random;

@ScriptManifest(author = "PayPalMeRSGP", name = "Hello World Alcher", info = "Basically an Autoclicker", version = 0.1, logo = "")
public class HelloAlcher extends Script {

    private static final double GAME_TICK_SEC = 0.603;

    private Magic spellChecker;
    @Override
    public final void onStart() {
        spellChecker = getMagic();

    }

    @Override
    public final int onLoop() throws InterruptedException{
        if(playerCanCastHighAlch()){
            return castHighAlch();
        }
        stop();
        return -1;
    }

    private int castHighAlch() throws InterruptedException{
        if(playerCanCastHighAlch()){
            spellChecker.castSpell(Spells.NormalSpells.HIGH_LEVEL_ALCHEMY);
            MethodProvider.sleep(randomNormalDist(0.35, 0.05, true)); //double click interval between clicking high alch and casting spell on item
            if(spellChecker.isSpellSelected()){
                getInventory().interact("Cast", "Magic longbow");
            }
        }

        return (int) randomNormalDist(GAME_TICK_SEC * 5, 0.45, false); //alch takes 5 game ticks before next alch can be cast
    }

    private boolean playerCanCastHighAlch() throws InterruptedException {
        return spellChecker != null && spellChecker.canCast(Spells.NormalSpells.HIGH_LEVEL_ALCHEMY);
    }

    private long randomNormalDist(double mean, double stddev, boolean midCast){
        long debug = (long) ((new Random().nextGaussian() * stddev + mean) * 1000);
        log(debug);
        return debug;
    }




}
