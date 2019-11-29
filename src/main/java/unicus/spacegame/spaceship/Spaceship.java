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
    public ShipWeapon[][] weaponTypes;

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

    public boolean CanRemoveModule(int sectionID, int moduleID, StringBuffer message) {
        if (validateModuleSlot(sectionID, moduleID)){
            message.append("Illegal selection! How did you manage this? HOW!? (this is a bug, please report it)");
            return false;
        }
        return false;
    }
    public boolean CanRemoveSection(int sectionID, StringBuffer message) {
        if (!validateSectionID(sectionID)) {
            message.append("Illegal selection! How did you manage this? HOW!? (this is a bug, please report it)");
            return false;
        }
        if(sectionTypes[sectionID] == SectionType.None) {
            message.append("This section is already stripped.");
            return false;
        }
        int failedModules = 0;
        int emptyModules = 0;
        ShipModule[] moduleList = modules[sectionID];
        for (int i = 0, moduleLength = moduleList.length; i < moduleLength; i++) {
            if(moduleList[i].moduleType == ModuleType.Empty) {
                emptyModules++;
                continue;
            }
            StringBuffer sub = new StringBuffer();
            if(!CanRemoveModule(sectionID, i, sub)){
                failedModules++;
            }
            message.append("\n module number " + i + sub);
        }
        if(failedModules > 0) {
            message.insert(0, "This section cannot be stripped." +
                    "\n " + failedModules + " modules cannot be removed.");
            return false;
        }
        if(emptyModules == moduleList.length) {
            message.insert(0, "This section be be stripped. You would reclaim X resources");
            return true;
        }
        message.insert(0, "This section be be stripped. You would reclaim X resources." +
                "\n WARNING: This will also remove the modules and weapons installed on it.");
        return true;
    }









    public ArrayList<RefitTaskChain> taskchains;

    public void resetRefitTasks(){
        taskchains = new ArrayList<>();
    }
    public void applyRefitTasks(){

    }


    private boolean validateSectionID(int id) {return id < 0 || id > sectionTypes.length;}
    private boolean validateWeaponSlot(int sID, int wID) {
        if (!validateSectionID(sID))
            return false;
        //This is a STUB
        return wID > 0 && wID < 12;
    }
    private boolean validateModuleSlot(int sID, int mID) {
        if (!validateSectionID(sID))
            return false;
        return mID > 0 && mID < sectionTypes[sID].getNumModules();
    }



    public RefitTaskChain tryBuildModule(int sectionID, int moduleSlot, ModuleType type, RefitTaskChain chain) {

    }












    public CanBuildResult canBuildWeapon(int sectionID, int slotID, WeaponType type) {
        CanBuildResult result = new CanBuildResult();
        if (!validateWeaponSlot(sectionID, slotID)) {
            result.possible = false;
            result.message = "Invalid selection.";
            return result;
        }

        //STUB. TODO: check if player can afford to construct this.
        result.possible = true;
        result.message = String.format("A test-weapon will be built on section %1$s's weapon slot number %2$s.", sectionID, slotID);
        return result;
    }
    public CanBuildResult canBuildModule(int sectionID, int moduleID, ModuleType type) {
        CanBuildResult result = new CanBuildResult();
        if (validateModuleSlot(sectionID, moduleID)) {
            result.possible = false;
            result.message = "Invalid selection.";
            return result;
        }
        ShipModule module = modules[sectionID][moduleID];
        //This is a STUB - No care is made for cargo or crew quarters yet

        if (module.moduleType == ModuleType.Empty) {
            if(type == ModuleType.Empty) {
                result.possible = true;
                result.message = "";
                return result;
            }
            //STUB. TODO: check if player can afford to construct this.
            result.possible = true;
            result.message = String.format("A %1$s module will be constructed at section 2$s's module slot number 3$s", type, sectionID, moduleID);

        }


        if(type == ModuleType.Empty) {
            result.possible = true;
            if (module.moduleType == ModuleType.Empty) {
                result.message = "";
            }
            result.message = "Module at ";
        }



    }



    /** --- end of refit section ----**/










    /**
     * Test-creates a spaceship, then prints the structure to console.
     * @param args
     */
    public static void main(String[] args) {
        Spaceship ship = Spaceship.GenerateStart1(new Random(0), 2, 10, 0.0f, 1.0f);
        System.out.println(ship.toString());
    }
}
class RefitTaskChain {
    public boolean possible;
    public String message;
    ArrayList<RefitTask> chain;
    public RefitTaskChain() {
        possible = false;
        message = "";
        chain = new ArrayList<>();
    }

    //todo list of material cost / gain

}
class RefitTask extends ConstructionTask {
    private RefitType refitType;
    private int[] args;

    public RefitTask(int labourCost, RefitType refitType, int... args) {
        super(labourCost);

        this.refitType = refitType;
        this.args = args;
    }
}

//STUB
class ConstructionTask {
    private int labourCost;

    public ConstructionTask(int labourCost) {

        this.labourCost = labourCost;
    }
}
enum RefitType {
    BuildWeapon, BuildModule, BuildSectionFrame, moveModule
}

//STUB
enum WeaponType {
    None, TestGun;
    public static WeaponType fromInt(int id){ return values()[id]; }
    public static int toInt(WeaponType type) {return type.toInt(); }
    public int toInt() {return ordinal();}

}

//STUB
class ShipWeapon {

}