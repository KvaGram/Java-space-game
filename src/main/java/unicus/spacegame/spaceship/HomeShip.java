package unicus.spacegame.spaceship;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * The datastructure representing a spaceship.
 * The spaceship has a length of sections (at least 2).
 * Each section has a number of modules, depending on the SectionType.
 */
public class HomeShip {

    private static HomeShip instance;
    public static HomeShip getInstance() {
        return instance;
    }

    public final int headLocation;
    public int middleLength;
    public final int tailLocation;
    //lists the type of sections currently installed. 0 is near bridge, other end near engineering.
    //public SectionType[] sectionTypes; //<- to remove
    public AbstractShipModule[][] modules;



    //public ShipWeapon[][] weaponTypes; //<- to remove

    /* TODO: static special modules

    head:
        Bridge
        Command-center
    tail:
        Engineering
        Forge
        Hangar
    */



    /**
     * Ship location, inner class.
     * Used to store a location of a section or module
     *
     * A section-value of 0 points to the head end of the ship.
     * A section-value of {@link this.length} points to the tail end of the ship.
     * A section-value not inside the above two is invalid, and points to nothing.
     *
     * A module-value of 0 refer to the section itself.
     * A module-value above number of modules in the section or -1 and below are invalid, and does not point to any module or component.
     *
     */

    //NOTE TO SELF: removing component from ShipLoc. Complete the new module array structure.
    public class ShipLoc {
        //section, module, component.
        int s, m;

        ShipLoc(int s, int m){
            this.s = s; this.m = m;
        }

        public boolean isValidSection() {return s >= 0 && s < modules.length;}
        public boolean isValidModule() {
            if (!isValidSection() || m < 0)
                return false;
            return m < modules[s].length;
        }

        /**
         * @return If this is the head section.
         */
        public boolean isHead(){return s == 0;}

        /**
         * @return If this is the tail section.
         */
        public boolean isTail(){return s == middleLength;}


        public AbstractShipModule getModule() {
            if(isValidModule())
                return modules[s][m];
            return null;
        }
        public AbstractShipSection getSection() {
            if (isValidSection())
                return (AbstractShipSection) modules[s][0];
            return null;
        }
        public ShipLoc[] getModuleLocList() {
            if(!isValidSection())
                return null;
            int len = getSection().getNumModules();
            ShipLoc[] ret = new ShipLoc[len];
            for (int i = 1; i < len; i++) {
                ret[i] = new ShipLoc(s, i);
            }
            return ret;
        }

        @Override
        public boolean equals(Object obj) {
            if (! (obj instanceof ShipLoc))
                return false;
            ShipLoc other = (ShipLoc)obj;

            return other.s == s && other.m == m;
        }

        public int getM() {
            return m;
        }

        public int getS() {
            return s;
        }

        @Override
        public String toString() {
            return "(Section " + s + ", Module " + m + ")";
        }
    }

    /**
     *
     * @param s section index. exceptions: 0 refer to the head section, {@code length} refer to the tail section.
     * @param m module index. exceptions: 0 refer to the section itself.
     * @return
     */
    public ShipLoc getShipLoc(int s, int m){
        return new ShipLoc(s, m);
    }

    /**
     * Creates a length long spaceship, naked down to the framework.
     * Not meant to be used directly. Use one of the Generate functions instead.
     * @param middleLength
     */
    public HomeShip(int middleLength)
    {
        this.headLocation = 0; //It's always 0, but hey, now the code might be more readable.
        this.middleLength = middleLength;
        this.tailLocation = 1 + middleLength; //The tail's index is right after the middle sections.


        //sectionTypes = new SectionType[length];
        modules = new AbstractShipModule[middleLength +2][1];

        int i = 0;
        //TODO: add head section
        //placeholder head section
        modules[i][0] = new StrippedFrame(new ShipLoc(i, 0));
        i++;
        for (; i < middleLength + 1; i++)
        {
            //Stripped sections
            modules[i][0] = new StrippedFrame(new ShipLoc(i, 0));
        }
        //TODO add tail section
        //placeholder tail section
        modules[i][0] = new StrippedFrame(new ShipLoc(i, 0));

        instance = this;
    }

    /**
     * Replaces a section of the spaceship.
     * Warning: this WILL replace (destroy!) the existing section and modules,
     * including the components in it, with the new section and empty modules, without asking!
     *
     * @param index section index of module to replace.
     * @param sectionType The new section type.
     *
     * @return the new section-frame
     */
    private AbstractShipSection forceBuildSection(int index, SectionType sectionType)
    {
        if (index < 1 || index > middleLength) {
            throw new IllegalArgumentException("Whoops.. that location is invalid for construction.\n" +
                    "Someone did a programming woopsie, because of that, the game will now quit.");
        }
        AbstractShipSection newSection;
        ShipLoc sectionLoc = new ShipLoc(index, 0);
        switch (sectionType) {
            //TODO: add classes for missing types.
            case Normal:
                newSection = new BasicFrame(sectionLoc);
                break;
            case Wheel:
                newSection = new WheelFrame(sectionLoc);
                break;
            case GravityPlated:
                newSection = new GravityFrame(sectionLoc);
                break;
            case None:
            default:
                newSection = new StrippedFrame(sectionLoc);
        }

        // ( index >= 0 && index < length);
        int sLength = newSection.getNumModules();
        //sectionTypes[index] = sectionType;
        destroySection(index);
        modules[index] = new AbstractShipModule[sLength +1];
        modules[index][0] = newSection;
        for(int i = 1; i < sLength+1; i++){
            modules[index][i] = new NullModule(new ShipLoc(index, i));
        }
        return newSection;
    }

    /**
     * Replaces a module of the spaceship.
     * Warning: this WILL replace (destroy!) the existing module, including any components, without asking!
     *
     * @param loc The location to build the module in.
     * @param moduleType The new module type.
     *
     * @return The new module
     */
    private AbstractShipModule forceBuildModule(ShipLoc loc, ModuleType moduleType){
        if (!loc.isValidModule())
            throw new IllegalArgumentException("Whoops.. that location is invalid for construction.\n" +
                    "Someone did a programming woopsie, because of that, the game will now quit.");
        AbstractShipModule newModule;
        switch (moduleType) {
            case Cargo:
                newModule = new CargoModule(loc);
                break;
            //TODO: add classes for missing types.
            case Habitat:
                newModule = new HabitatModule(loc);
                break;
            case Empty:
            default:
                newModule = new NullModule(loc);
                break;
        }
        destroyModule(loc.getModule());
        modules[loc.s][loc.m] = newModule;

        return newModule;
    }

    /** prepares a section for destruction.
     * @param index
     */
    private void destroySection(int index) {
        for (AbstractShipModule m : modules[index]) {
            destroyModule(m);
        }
    }


    /** Prepares a module or section object for destruction
     * This puts the reclaimed resources back in storage and displaces any crew and cargo that might be there.
     * @param m
     */
    private void destroyModule(AbstractShipModule m) {
        for (abstractShipComponent c : m.getComponents()) {
            //This stores or destroys the components.
            c.onDestroy();
        }
        m.onDestroy();
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
    static public HomeShip GenerateStart1(Random rand, int minLength, int maxLength, float minFull, float maxFull){
        int length = rand.nextInt(maxLength - minLength) + minLength;
        float fullRange = maxFull - minFull;
        float full = rand.nextFloat() * fullRange + minFull;
        return GenerateStart1(rand, length, full);
    }

    /**
     * Generates a new spaceship with a set number of cargobays that are full
     * @param rand The instance of Random to use.
     * @param length The number of sections in the middle of the HomeShip.
     * @param full How much of the potential space will be filled with cargo (range 0, 1)
     * @return A Spaceship
     */
    private static HomeShip GenerateStart1(Random rand, int length, float full){
        //length MUST be at least 2.
        if (length < 2)
            length = 2;

        HomeShip ship = new HomeShip(length);
        //center of the wheel section hosts the first hab module
        AbstractShipSection wheelSection = ship.forceBuildSection(1, SectionType.Wheel);

        int habstart = wheelSection.getNumModules()/2 + 1;
        ship.forceBuildModule(ship.getShipLoc(1,habstart), ModuleType.Habitat);


        int normSize = 6;//SectionType.Normal.getNumModules();
        int totCargoSpace = (normSize * (length-1));
        int usedCargoSpace = 0;
        int targetFilled = Math.round( (float)totCargoSpace * full);

        System.out.println("Total cargo space: " + totCargoSpace + ", target cargo: " + targetFilled);

        for(int i = 2; i < ship.modules.length-1; i++){
            //If none of the modules are used, can targetFilled still be reached?
            boolean canBeEmpty = (usedCargoSpace + normSize * (length - i - 1)) > targetFilled;
            float sectionEmptyChance = rand.nextFloat();
            System.out.println("Chance section is empty: " + sectionEmptyChance + " can be empty: " + canBeEmpty);
            if(canBeEmpty && sectionEmptyChance < 0.3f){
                ship.forceBuildSection(i, SectionType.None);
            } else {
                ship.forceBuildSection(i, SectionType.Normal);
                for(int j = 1; j < normSize+1; j++)
                {
                    ShipLoc loc = ship.getShipLoc(i, j);
                    ModuleType type;
                    float moduleEmptyChance = rand.nextFloat();
                    System.out.println("Chance module is empty: " + moduleEmptyChance + " can be empty: " + canBeEmpty);
                    if( usedCargoSpace >= targetFilled || (canBeEmpty && moduleEmptyChance < 0.6f)){
                        type = ModuleType.Empty;
                    } else {
                        type = ModuleType.Cargo;
                        usedCargoSpace++;
                    }
                    ship.forceBuildModule(loc, type);
                }
            }



        }
        return ship;
    }

/*
    public ArrayList<Integer> GetBuildableModules(Point loc) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        if(loc.y < 0 || loc.x < 0)
            return list;
        else {
            SectionType sectionType = sectionTypes[loc.x];
            AbstractShipModule module = modules[loc.x][loc.y];
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

*/
    public boolean canBuildSection(ShipLoc shipLoc, SectionType typeToBuild, StringBuffer message) {
        if (!shipLoc.isValidSection()) {
            message.append("Illegal selection! How did you manage this? HOW!? (this is a bug, please report it)");
            return false;
        }
        /*
        //TODO: find a more elegant check for this, or remove
        if(shipLoc.getSection().getClass() == NullSection.class) {
            message.append("Cannot build section-frame. You need to strip off the old one first.");
            return false;
        }
         */

        //TODO: get material cost of construction.
        ArrayList<CargoPlaceholder> cost = new ArrayList<>();
        if(! CanAfford(cost)) {
            message.append("You cannot afford X resources :-(");
            return false;
        }
        message.append("You can build this section-frame. It will cost X resources.");
        return true;
    }

    public boolean canBuildModule(ShipLoc loc, ModuleType typeToBuild, StringBuffer message) {
        if (loc.isValidModule()){
            message.append("Illegal selection! How did you manage this? HOW!? (this is a bug, please report it)");
            return false;
        }
        //Check section for compadibility with moduleType.
        if (!loc.getSection().canBuildModule(typeToBuild, message)) {
            message.append("\nModule cannot be built.");
            return false;
        }

        if (canRemoveModule(loc, message)) {
            message.append("\n");
        }
        else {
            message.append("\nModule cannot be built.");
            return false;
        }
        //TODO: get material cost of construction.
        ArrayList<CargoPlaceholder> cost = new ArrayList<>();
        if(! CanAfford(cost)) {
            message.append("You cannot afford X resources :-(");
            return false;
        }
        message.append("You can build this module. It will cost X resources.");
        return true;

    }
    public boolean canRemoveModule(ShipLoc shipLoc, StringBuffer message) {
        if (shipLoc.isValidModule()){
            message.append("Illegal selection! How did you manage this? HOW!? (this is a bug, please report it)");
            return false;
        }

        ArrayList<ShipLoc> lockedModules = getLockedModules();
        if(lockedModules.contains(shipLoc)) {
            message.append("Your crew is already busy at work here.");
            return false;
        }

        AbstractShipModule module = shipLoc.getModule();
        //TODO: find more elegant way for checking this.
        if (module.getClass() == NullModule.class) {
            message.append("There is no module to remove.");
            return false;
        }

        //placeholder cargo objects
        ArrayList<CargoPlaceholder> cargoToMove = new ArrayList<>();
        //placeholder crew housing assignment
        ArrayList<HousingPlaceholder> housingToMove = new ArrayList<>();

        if(module instanceof Habitat) {
            Habitat hModule = (Habitat) module;
            Collections.addAll(housingToMove, hModule.getHousingAssignments());
        }
        Collections.addAll(cargoToMove, module.getCargoOnDestruction());

        //Add this location to the locked modules.
        // This is used when checking if cargo,
        // recycled resources and displaced crew can be relocated.
        lockedModules.add(shipLoc);

        int numCargo = cargoToMove.size(); //STUB - TODO: should report the total cargo units
        int numPeople = housingToMove.size();
        if( !checkCanHouseCrew(housingToMove, lockedModules)) {
            message.append("Cannot remove module. There is not enough crew-quarters to move all ");
            message.append(numPeople + " crewmen. Please construct more habitats.");
            return false;
        }
        if( !checkStoreCargo(cargoToMove, lockedModules)) {
            message.append("Cannot remove module. There is not enough space to store all ");
            message.append(numCargo + " cargo units");
            return false;
        }

        message.append("You can remove this module. You will move and reclaim x resources and displace x crew-members");
        return true;
    }
    public boolean canRemoveSection(ShipLoc shipLoc, StringBuffer message) {
        if (!shipLoc.isValidSection()) {
            message.append("Illegal selection! How did you manage this? HOW!? (this is a bug, please report it)");
            return false;
        }

        ArrayList<ShipLoc> lockedModules = getLockedModules();
        for (ShipLoc l : lockedModules)
            if (l.s == shipLoc.s) {
                message.append("Your crew is already busy working in this section. You cannot remove it.");
                return false;
            }
        //TODO: find a more elegant way on checking for this
        if(shipLoc.getSection().getClass() == StrippedFrame.class) {
            message.append("This section is already stripped.");
            return false;
        }

        //placeholder cargo objects
        ArrayList<CargoPlaceholder> cargoToMove = new ArrayList<>();
        //placeholder crew housing assignment
        ArrayList<HousingPlaceholder> housingToMove = new ArrayList<>();

        //TODO: add resources stripped from section-frame to cargo.
        //TODO: add weapon-components dismantled to cargo.

        ShipLoc[] sModules = shipLoc.getModuleLocList();
        for (int i = 0, moduleLength = sModules.length; i < moduleLength; i++) {
            AbstractShipModule m = sModules[i].getModule();
            Collections.addAll(cargoToMove, m.getCargoOnDestruction());
            if (m instanceof Habitat) {
                Habitat h = (Habitat) m;
                Collections.addAll(housingToMove, h.getHousingAssignments());
            }
        }

        //Adds this section's modules to the locked modules list.
        // This is used when checking if cargo,
        // recycled resources and displaced crew can be relocated.
        Collections.addAll(lockedModules, shipLoc.getModuleLocList());

        int numCargo = cargoToMove.size(); //STUB - should report the total cargo units
        int numPeople = housingToMove.size();
        if( !checkCanHouseCrew(housingToMove, lockedModules)) {
            message.append("Cannot strip section. There is not enough crew-quarters to move all ");
            message.append(numPeople + " crewmen. Please construct more habitats.");
            return false;
        }
        if( !checkStoreCargo(cargoToMove, lockedModules)) {
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
    private boolean checkCanHouseCrew(ArrayList<HousingPlaceholder> toMove) {
        return checkCanHouseCrew(toMove, new ArrayList<ShipLoc>());
    }
    private boolean checkStoreCargo(ArrayList<CargoPlaceholder> toStore) {
        return checkStoreCargo(toStore, new ArrayList<ShipLoc>());
    }
    //endregion

    // STUB. TODO: check if crew can be housed in available housing space (except in modules in the ignore list).
    private boolean checkCanHouseCrew(ArrayList<HousingPlaceholder> toMove, ArrayList<ShipLoc> ignoreList) {
        return true;
    }
    // STUB. TODO: check if cargo can be stored in available space (except in modules in the ignore list).
    private boolean checkStoreCargo(ArrayList<CargoPlaceholder> toStore, ArrayList<ShipLoc> ignoreList) {
        return true;
    }
    //STUB! Todo: check if player can afford the cost.
    private boolean CanAfford(ArrayList<CargoPlaceholder> cost) {
        return true;
    }

    public ArrayList<ShipLoc> getLockedModules() {
        ArrayList<ShipLoc> ret = new ArrayList<>();
        for (RefitTask task : taskchain) {
            Collections.addAll(ret, task.targets);
        }
        return ret;
    }


    public ArrayList<RefitTask> taskchain;


    //STUB TODO: integrate with the job system
    public void cancelAllRefitTasks(){
        taskchain = new ArrayList<>();
    }


    //TODO: move RefitTaskChain and RefitTask out of the class
    /**
     * The refit-task is a task that may show up for the construction job.
     */
    abstract class RefitTask extends ConstructionTask {
        protected RefitType refitType;
        protected ShipLoc[] targets;

        public RefitTask(int labourCost, String description, RefitType refitType, ShipLoc[] targets) {
            super(labourCost, description);
            this.refitType = refitType;
            this.targets = targets;
        }
        public RefitTask(int labourCost, String description, RefitType refitType, ShipLoc target) {
            super(labourCost, description);
            this.refitType = refitType;
            this.targets = new ShipLoc[]{target};
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

        public ShipLoc[] getTargets() {
            return targets;
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
        HomeShip ship = HomeShip.GenerateStart1(new Random(0), 2, 10, 0.0f, 1.0f);
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

class ShipSection {
    SectionType type;

}

//STUB
class ShipWeapon {

}

//Placeholder classes. TODO: write these classes (duh..)
@SuppressWarnings({"All"})
class CargoPlaceholder{}
@SuppressWarnings({"All"})
class HousingPlaceholder{}
//NOTE: why o' why complain about placeholders, Intellij.
// you are needlessly breaking my workflow.
