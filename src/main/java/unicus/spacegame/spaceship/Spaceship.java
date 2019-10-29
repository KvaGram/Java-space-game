package unicus.spacegame.spaceship;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * The datastructure representing a spaceship.
 * The spaceship has a length of sections (at least 2).
 * Each section has a number of modules, depending on the SectionType.
 */
public class Spaceship {
    public int length;
    //lists the type of sections currently installed. 0 is near bridge, other end near engineering.
    public SectionType[] sectionTypes;
    public ShipModule[][] modules;

    /**
     * Creates a length long spaceship, naked down to the framework.
     * Not meant to be used directly. Use one of the Generate functions instead.
     * @param length
     */
    public Spaceship(int length)
    {
        this.length = length;
        sectionTypes = new SectionType[length];
        modules = new ShipModule[length][0];
        for (int i = 0; i < length; i++)
        {
            //None-type sections
            sectionTypes[i] = SectionType.None;
            modules[i] = new ShipModule[0];
        }
    }

    /**
     * Replaces a section of the spaceship.
     * Warning: this WILL replace the existing modules in it with empty modules, without asking!
     * Planned: function to check if replacing a section should be allowed (by gameplay rules)
     *
     * @param index section index of module to replace.
     * @param sectionType The new section type.
     */
    public void BuildSection(int index, SectionType sectionType)
    {
        // ( index >= 0 && index < length);
        int sLength = sectionType.getNumModules();
        sectionTypes[index] = sectionType;
        modules[index] = new ShipModule[sLength];
        for(int i = 0; i < sLength; i++){
            modules[index][i] = new ShipModule(sectionType);
        }
    }

    /**
     * Replaces a module of the spaceship.
     * Warning: this WILL replace the existing module, without asking!
     * Planned: function to check if replacing a module should be allowed (by gameplay rules)
     *
     * @param sIndex The section index
     * @param mIndex The module index (of section)
     * @param mType The new module type.
     */
    public void BuildModule(int sIndex, int mIndex, ModuleType mType){
        modules[sIndex][mIndex] = new ShipModule(sectionTypes[sIndex], mType);
    }

    /**
     * Generates a new spaceship, with adjustable range of specification.
     * @param rand The instance of Random to use.
     * @param minLength minimal length of the ship
     * @param maxLength maximum length of the ship
     * @param minFull minimal cargo to spawn with (range 0, 1)
     * @param maxFull maximum cargo to spawn with (range 0, 1)
     * @return A Spaceship
     */
    static public Spaceship GenerateStart1(Random rand, int minLength, int maxLength, float minFull, float maxFull){
        int length = rand.nextInt(maxLength - minLength) + minLength;
        float fullRange = maxFull - minFull;
        float full = rand.nextFloat() * fullRange + minFull;
        return GenerateStart1(rand, length, full);
    }

    /**
     * Generates a new spaceship, with some fixed specification
     * @param rand The instance of Random to use.
     * @param length The length of the Spaceship
     * @param full How much of the potential space will be filled with cargo (range 0, 1)
     * @return A Spaceship
     */
    private static Spaceship GenerateStart1(Random rand, int length, float full){
        //length MUST be at least 2.
        if (length < 2)
            length = 2;

        Spaceship ship = new Spaceship(length);
        //center of the wheel section hosts the first hab module
        int habstart = SectionType.Wheel.getNumModules() / 2;
        ship.BuildSection(0, SectionType.Wheel);
        ship.BuildModule(0, habstart, ModuleType.Habitat);


        int normSize = SectionType.Normal.getNumModules();
        int totCargoSpace = (normSize * (length-1));
        int usedCargoSpace = 0;
        int targetFilled = Math.round( (float)totCargoSpace * full);

        System.out.println("Total cargo space: " + totCargoSpace + ", target cargo: " + targetFilled);

        for(int i = 1; i < length; i++){
            //If none of the modules are used, can targetFilled still be reached?
            boolean canBeEmpty = (usedCargoSpace + normSize * (length - i - 1)) > targetFilled;
            float sectionEmptyChance = rand.nextFloat();
            System.out.println("Chance section is empty: " + sectionEmptyChance + " can be empty: " + canBeEmpty);
            if(canBeEmpty && sectionEmptyChance < 0.3f){
                ship.BuildSection(i, SectionType.None);
            } else {
                ship.BuildSection(i, SectionType.Normal);
                for(int j = 0; j < normSize; j++)
                {
                    ModuleType type;
                    float moduleEmptyChance = rand.nextFloat();
                    System.out.println("Chance module is empty: " + moduleEmptyChance + " can be empty: " + canBeEmpty);
                    if( usedCargoSpace >= targetFilled || (canBeEmpty && moduleEmptyChance < 0.6f)){
                        type = ModuleType.Empty;
                    } else {
                        type = ModuleType.Cargo;
                        usedCargoSpace++;
                    }
                    ship.BuildModule(i, j, type);
                }
            }



        }
        return ship;
    }

    /**
     * Test-creates a spaceship, then prints the structure to console.
     * @param args
     */
    public static void main(String[] args) {
        Spaceship ship = Spaceship.GenerateStart1(new Random(0), 2, 10, 0.0f, 1.0f);
        System.out.println(ship.toString());
    }

    public ArrayList<Integer> GetBuildableModules(Point loc) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        if(loc.y < 0 || loc.x < 0)
            return list;
        else {
            SectionType sectionType = sectionTypes[loc.x];
            ShipModule module = modules[loc.x][loc.y];
            ModuleType[] mTypes = ModuleType.values();

            for (int i = 0; i < mTypes.length; i++)
            {
                if(mTypes[i] == module.moduleType)
                    continue; //Do not include existing type.
                if(mTypes[i].getNeedGravity() && !sectionType.getHasGravity())
                    continue; //Do not include gravity modules for non-gravity section
                list.add(i);
            }
            return list;
        }

    }

    public ArrayList<Integer> GetBuildableSections(Point loc) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        if(loc.x < 0)
            return list;
        SectionType sectionType = sectionTypes[loc.x];
        SectionType[] sTypes    = SectionType.values();
        for (int i = 0; i < sTypes.length; i++){
            if(sTypes[i] == sectionType)
                continue; //Do not include existing type.
            list.add(i);
        }
        return list;
    }
}
