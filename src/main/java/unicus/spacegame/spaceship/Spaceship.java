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
    private void forceBuildSection(int index, SectionType sectionType)
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
    private void forceBuildModule(int sIndex, int mIndex, ModuleType mType){
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
        ship.forceBuildSection(0, SectionType.Wheel);
        ship.forceBuildModule(0, habstart, ModuleType.Habitat);


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
                ship.forceBuildSection(i, SectionType.None);
            } else {
                ship.forceBuildSection(i, SectionType.Normal);
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
                    ship.forceBuildModule(i, j, type);
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
        //TODO: check if any previous task in the chain conflicts with this.
        for (int i = 0; i < taskchain.size(); i++) {

        }
        ShipModule module = modules[sectionID][moduleID];
        if (module.moduleType == ModuleType.Empty) {
            message.append("There is no module to remove.");
            return false;
        }

        //placeholder cargo objects
        ArrayList<Object> cargoToMove = new ArrayList<>();
        //placeholder crew housing assignment
        ArrayList<Object> housingToMove = new ArrayList<>();

        if(module instanceof Habitat) {
            Habitat hModule = (Habitat) module;
            housingToMove.add(hModule.getHousingAssiments());
        }
        cargoToMove.add(module.getCargoOnDestruction());

        int numCargo = cargoToMove.size(); //STUB - should report the total cargo units
        int numPeople = housingToMove.size();
        if( !CheckCanHouseCrew(housingToMove, module)) {
            message.append("Cannot remove module. There is not enough crew-quarters to move all ");
            message.append(numPeople + " crewmen. Please construct more habitats.");
            return false;
        }
        if( !CheckStoreCargo(cargoToMove, module)) {
            message.append("Cannot remove module. There is not enough space to store all ");
            message.append(numCargo + " cargo units");
            return false;
        }

        return false;
    }
    public boolean CanRemoveSection(int sectionID, StringBuffer message) {
        if (!validateSectionID(sectionID)) {
            message.append("Illegal selection! How did you manage this? HOW!? (this is a bug, please report it)");
            return false;
        }
        //TODO: check if any previous task in the chain conflicts with this.
        for (int i = 0; i < taskchain.size(); i++) {

        }

        if(sectionTypes[sectionID] == SectionType.None) {
            message.append("This section is already stripped.");
            return false;
        }

        //placeholder cargo objects
        ArrayList<Object> cargoToMove = new ArrayList<>();
        //placeholder crew housing assignment
        ArrayList<Object> housingToMove = new ArrayList<>();

        //TODO: add resources stripped from section-frame to cargo.
        //TODO: add weapon-components dismantled to cargo.

        ShipModule[] moduleList = modules[sectionID];
        for (int i = 0, moduleLength = moduleList.length; i < moduleLength; i++) {
            cargoToMove.add(moduleList[i].getCargoOnDestruction());
            if (moduleList[i] instanceof Habitat) {
                Habitat h = (Habitat) moduleList[i];
                housingToMove.add(h.getHousingAssiments());
            }
        }
        int numCargo = cargoToMove.size(); //STUB - should report the total cargo units
        int numPeople = housingToMove.size();
        if( !CheckCanHouseCrew(housingToMove, moduleList)) {
            message.append("Cannot strip section. There is not enough crew-quarters to move all ");
            message.append(numPeople + " crewmen. Please construct more habitats.");
            return false;
        }
        if( !CheckStoreCargo(cargoToMove, moduleList)) {
            message.append("Cannot strip section. There is not enough space to store all ");
            message.append(numCargo + " cargo units");
            return false;
        }
        message.append("You can strip this section. This will dismantle all modules and weapons installed on it.");
        message.append("\nYou will strip and move a total of ");
        message.append(numCargo + " units of cargo");
        if (numPeople > 0)
            message.append("/n" + numPeople + " crewmen will have to be moved.");
        return true;
    }

    //region checkStore/re-house shortcuts
    private boolean CheckCanHouseCrew(ArrayList<Object> toMove, ShipModule ignoreModule) {
        return CheckStoreCargo(toMove, new ShipModule[] {ignoreModule});
    }
    private boolean CheckStoreCargo (ArrayList<Object> toStore, ShipModule ignoreModule) {
        return CheckStoreCargo(toStore, new ShipModule[] {ignoreModule});
    }
    private boolean CheckCanHouseCrew(ArrayList<Object> toMove) {
        return CheckStoreCargo(toMove, new ShipModule[0]);
    }
    private boolean CheckStoreCargo (ArrayList<Object> toStore) {
        return CheckStoreCargo(toStore, new ShipModule[0]);
    }
    //endregion

    // STUB. TODO: check if crew can be housed in available housing space (except in modules in the ignore list).
    private boolean CheckCanHouseCrew(ArrayList<Object> toMove, ShipModule[] ignoreList) {
        return true;
    }
    // STUB. TODO: check if cargo can be stored in available space (except in modules in the ignore list).
    private boolean CheckStoreCargo (ArrayList<Object> toStore, ShipModule[] ignoreList) {
        return true;
    }


    public ArrayList<RefitTask> taskchain;


    //STUB TODO: integrate with the job system
    public void cancelAllRefitTasks(){
        taskchain = new ArrayList<>();
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

    //TODO: move RefitTaskChain and RefitTask out of the class
    class RefitTaskChain {
        public String message;
        ArrayList<RefitTask> chain;
        public RefitTaskChain() {
            message = "";
            chain = new ArrayList<>();
        }
    }

    /**
     * The refit-task is a task that may show up for the construction job.
     */
    abstract class RefitTask extends ConstructionTask {
        protected RefitType refitType;
        protected int sectionLocation;
        protected int moduleLocation;

        public RefitTask(int labourCost, String description, RefitType refitType, int sectionLocation, int moduleLocation) {
            super(labourCost, description);
            this.refitType = refitType;
            this.sectionLocation = sectionLocation;
            this.moduleLocation = moduleLocation;
        }

        /** TODO: move to bottom-most super-class for tasks.
         * Runs when finishing up the job, the construction job is finished.
         * Some related events could trigger.
         * @return whatever the task was successfully completed.
         */
        abstract boolean onFinish();

        /**
         *
         * @return whatever the task can be removed.
         */
        abstract boolean onRemove();

        public int getSectionLocation() {
            return sectionLocation;
        }

        public int getModuleLocation() {
            return moduleLocation;
        }

        public RefitType getRefitType() {
            return refitType;
        }
    }
    enum RefitType{build, remove}











/*    public CanBuildResult canBuildWeapon(int sectionID, int slotID, WeaponType type) {
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



    }*/
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


//STUB
class ConstructionTask {
    private int labourCost;
    private String description;

    public ConstructionTask(int labourCost, String description) {

        this.labourCost = labourCost;
        this.description = description;
    }
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