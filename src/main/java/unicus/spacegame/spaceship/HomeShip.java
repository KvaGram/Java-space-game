package unicus.spacegame.spaceship;

import unicus.spacegame.CargoCollection;
import unicus.spacegame.CargoContainer;
import unicus.spacegame.crew.*;

import java.util.*;

/** The HomeShip class is an singleton data structure representing the sections, frames, modules and weapons installed.
 *  Anything related to the above is accessed though this class.
 *
 *  The primary data structure is the modules object.
 *  It is a hash-table of Ship-locations.
 *
 *
 * The datastructure representing a spaceship.
 * The spaceship has a length of sections (at least 2).
 * Each section has a number of modules, depending on the SectionType.
 */
public class HomeShip {

    /** Number of modules each non-special section has.*/
    private final static int MODULES_PER_SECTION = 6;
    /**total amount of module objects allowed per section (this includes the section object)
     * Used for calculating the hash value for ShipLoc.*/
    private final static int MAX_MODULE_OBJECTS = MODULES_PER_SECTION + 1;

    private static HomeShip instance;

    public static HomeShip getInstance() {
        return instance;
    }

    private final int headLocation;
    private final int middleLength;
    private final int tailLocation;
    private final int fullLength;


    public Hashtable<ShipLoc, AbstractShipModule> modules;

    public static int getHeadLocation() {
        return instance.headLocation;
    }

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
    public class ShipLoc {
        //section, module.
        int s, m;

        ShipLoc(int s, int m){
            this.s = s; this.m = m;
        }

        public boolean isValidSection() {return s >= 0 && s <= tailLocation;}
        public boolean isValidModule() {
            if (!isValidSection() || m < 0)
                return false;
            return modules.containsKey(this);
        }

        /**
         * @return If this is the head section.
         */
        public boolean isHead(){return s == headLocation;} //Note: headLocation is always 0

        /**
         * @return If this is the tail section.
         */
        public boolean isTail(){return s == tailLocation;}


        public AbstractShipModule getModule() {
            if(isValidModule())
                return modules.get(this);
            return null;
        }
        public AbstractShipSection getSection() {
            if (isValidSection()) {
                return (AbstractShipSection) new ShipLoc(s, 0).getModule();
            }
            return null;
        }
        public ShipLoc[] getModuleLocList() {
            if(!isValidSection())
                return null;

            ShipLoc[] ret = new ShipLoc[MODULES_PER_SECTION];
            for (int i = 0; i < MODULES_PER_SECTION; i++) {
                ret[i] = new ShipLoc(s, i + 1); //offset by 1, because the SectionObject occupies index 0.
            }
            return ret;
        }

        /**
         * One Ship location equals another
         * when they have the same section and module value.
         * @param obj object to compare.
         * @return Whatever the two locations are the same.
         */
        @Override
        public boolean equals(Object obj) {
            if (! (obj instanceof ShipLoc))
                return false;
            ShipLoc other = (ShipLoc)obj;

            return other.s == s && other.m == m;
        }

        /**
         * The hash value for a ship location, is the section number, multiplied by the maximum number of
         * module objects (this includes the section object), plus the module index.
         * @return a unique indexed value of the ship location (given valid values for section and module).
         */
        @Override
        public int hashCode() {
            int h = (MAX_MODULE_OBJECTS * s) + m;
            return h;
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

        /**
         * Rolls the module-index.
         * Should not be used for locations meant to represent a section index.
         * @param num number of modules to roll.
         * @return A new ShipLoc linking to the new module index.
         */
        public ShipLoc rollModule(int num) {
            int newM = m + num; //add the roll amount.
            /*
            The following rolls newW to be within the [1, MAX_MODULE_OBJECTS] range
             note: MODULES_PER_SECTION is expected to be 6,
                  and MAX_MODULE_OBJECTS is expected to be 7 ( MODULES_PER_SECTION + 1).
            */
            while (newM >= MAX_MODULE_OBJECTS)
                newM -= MODULES_PER_SECTION;
            while (newM < 1) //Note: index 0 is reserved for the section-object.
                newM += MODULES_PER_SECTION;

            return new ShipLoc(s, newM);
        }
        public ShipLoc nextModule(){return rollModule(1);}
        public ShipLoc prevModule(){return rollModule(-1);}

        /**
         * Moves the section-index.
         * @param num number of sections to move.
         * @return A new ShipLoc linking to the new section index.
         */
        public ShipLoc moveSection(int num) {
            // Clamp newS to [headLocation, tailLocation]
            // Where headLocation is always 0, and tailLocation is the last section index.
            int newS = Math.max(headLocation, Math.min(tailLocation, s + num));
            return new ShipLoc(newS, m);
        }
        public ShipLoc nextSection() {return moveSection(1);}
        public ShipLoc prevSection() {return moveSection(-1);}



    }

    /**
     *
     * @param s section index. exceptions: 0 refer to the head section, {@code length} refer to the tail section.
     * @param m module index. exceptions: 0 refer to the section itself.
     * @return
     */
    public ShipLoc getShipLoc(int s, int m) {
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
        this.fullLength = middleLength + 2; //the full length of the ship

        modules = new Hashtable<>();
        instance = this;

        HeadSection head = new HeadSection(new ShipLoc(headLocation, 0));
        MainBridge bridge = new MainBridge(new ShipLoc(headLocation, 1));
        modules.put(head.loc, head);

        int i, j;

        // Fill first sector with reference to bridge.
        for(i = 1; i <= MODULES_PER_SECTION; i++)
            modules.put(new ShipLoc(headLocation,i), bridge);

        TailSection tail = new TailSection(new ShipLoc(tailLocation, 0));
        Engineering engineering = new Engineering(new ShipLoc(tailLocation, 1));
        modules.put(tail.loc, tail);

        // Fill last sector with reference to engineering.
        for(i = 1; i <= MODULES_PER_SECTION; i++)
            modules.put(new ShipLoc(tailLocation,i), engineering);

        //Fill sectors in the middle with stripped frame sections with empty modules
        for(i = 1; i < tailLocation; i++) {
            ShipLoc loc = new ShipLoc(i, 0);
            modules.put(loc, new StrippedFrame(loc));
            for(j = 1; j < MAX_MODULE_OBJECTS; j++) {
                loc = new ShipLoc(i, j);
                modules.put(loc, new NullModule(loc));
            }
        }
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
        if (index < 1 || index > getMiddleLength()) {
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

        destroySection(index);
        modules.replace(sectionLoc, newSection);
        for (ShipLoc loc : sectionLoc.getModuleLocList()) {
            forceBuildModule(loc, ModuleType.Empty);
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
        modules.replace(loc, newModule);

        return newModule;
    }

    /** prepares a section for destruction.
     * @param index
     */
    private void destroySection(int index) {
        ShipLoc[] moduleLocList = getShipLoc(index, 0).getModuleLocList();
        for (ShipLoc loc : moduleLocList) {
            destroyModule(loc.getModule());
        }
    }


    /** Prepares a module or section object for destruction
     * This puts the reclaimed resources back in storage, displaces any crew and cargo that might be there
     * and removes any dependent jobs.
     * @param m
     */
    private void destroyModule(AbstractShipModule m) {
        if (m == null)
            return;
        for (abstractShipComponent c : m.getComponents()) {
            //This stores or destroys the components.
            c.onDestroy();
            if(m instanceof Workplace) {
                Workplace w = (Workplace) m;
                SpaceCrew.getInstance().removeJobs(w.getDependentJobs());
            }
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
     * @param middleLength The number of sections in the middle of the HomeShip.
     * @param full How much of the potential space will be filled with cargo (range 0, 1)
     * @return A Spaceship
     */
    private static HomeShip GenerateStart1(Random rand, int middleLength, float full){
        //middle length MUST be at least 2.
        if (middleLength < 2)
            middleLength = 2;

        HomeShip ship = new HomeShip(middleLength);


        AbstractShipSection wheelSection = ship.forceBuildSection(1, SectionType.Wheel);
        ship.forceBuildModule(ship.getShipLoc(1,2), ModuleType.Habitat);
        ship.forceBuildModule(ship.getShipLoc(1,5), ModuleType.Habitat);


        int totModuleSpace = (MODULES_PER_SECTION * (middleLength-1));
        int numCargoModules = 0;
        int targetCargoModules = Math.round( (float)totModuleSpace * full);

        System.out.println("Total module space: " + totModuleSpace + ", target cargo modules: " + targetCargoModules);

        for(int i = 2; i < ship.tailLocation; i++){
            //If none of the modules are used, can targetFilled still be reached?
            boolean canBeEmpty = (numCargoModules + MODULES_PER_SECTION * (middleLength - i - 1)) > targetCargoModules;
            float sectionEmptyChance = rand.nextFloat();
            System.out.println("Chance section is empty: " + sectionEmptyChance + " can be empty: " + canBeEmpty);
            if(canBeEmpty && sectionEmptyChance < 0.3f){
                ship.forceBuildSection(i, SectionType.None);
            } else {
                ship.forceBuildSection(i, SectionType.Normal);
                for(int j = 1; j < MODULES_PER_SECTION+1; j++)
                {
                    ShipLoc loc = ship.getShipLoc(i, j);
                    ModuleType type;
                    float moduleEmptyChance = rand.nextFloat();
                    System.out.println("Chance module is empty: " + moduleEmptyChance + " can be empty: " + canBeEmpty);
                    if( numCargoModules >= targetCargoModules || (canBeEmpty && moduleEmptyChance < 0.6f)){
                        type = ModuleType.Empty;
                    } else {
                        type = ModuleType.Cargo;
                        numCargoModules++;
                    }
                    ship.forceBuildModule(loc, type);
                }
            }
        }
        return ship;
    }
    public boolean canBuildSection(ShipLoc shipLoc, SectionType typeToBuild, StringBuffer message) {
        if (!shipLoc.isValidSection()) {
            message.append("Illegal selection! How did you manage this? HOW!? (this is a bug, please report it)");
            return false;
        }
        //TODO: get material cost of construction.
        ArrayList<CargoCollection> cost = new ArrayList<>();
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
        //Check section for compatibility with moduleType.
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
        ArrayList<CargoCollection> cost = new ArrayList<>();
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
        //TODO: check for housing assignments
        //ArrayList<HousingAssignment> housingToMove = new ArrayList<>();
        //if(module instanceof HousingPlace) {
        //
        //}
        ArrayList<CargoCollection> cargoToMove = new ArrayList<>(module.getCargoOnDestruction());

        //Add this location to the locked modules.
        // This is used when checking if cargo,
        // recycled resources and displaced crew can be relocated.
        lockedModules.add(shipLoc);

        int numCargo = cargoToMove.size(); //STUB - TODO: should report the total cargo units
        //int numPeople = housingToMove.size();
        //if( !checkCanHouseCrew(housingToMove, lockedModules)) {
        //    message.append("Cannot remove module. There is not enough crew-quarters to move all ");
        //    message.append(numPeople + " crewmen. Please construct more habitats.");
        //    return false;
        //}
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

        /* Lists of cargo and housing assignments that will be displaced/lost if section is removed.
         */
        ArrayList<CargoCollection> cargoToMove = new ArrayList<>();
        ArrayList<HousingAssignment> housingToMove = new ArrayList<>();

        //TODO: add resources stripped from section-frame to cargo.
        //TODO: add weapon-components dismantled to cargo.

        ShipLoc[] sModules = shipLoc.getModuleLocList();
        for (int i = 0, moduleLength = sModules.length; i < moduleLength; i++) {
            AbstractShipModule m = sModules[i].getModule();
            cargoToMove.addAll(m.getCargoOnDestruction());
            //TODO: add check for housing assignments
            //if (m instanceof Habitat) {
            //    Habitat h = (Habitat) m;
            //    Collections.addAll(housingToMove, h.getHousingAssignments());
            //}
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
    private boolean checkCanHouseCrew(ArrayList<HousingAssignment> toMove) {
        return checkCanHouseCrew(toMove, new ArrayList<ShipLoc>());
    }
    private boolean checkStoreCargo(ArrayList<CargoCollection> toStore) {
        return checkStoreCargo(toStore, new ArrayList<ShipLoc>());
    }
    //endregion

    // STUB. TODO: check if crew can be housed in available housing space (except in modules in the ignore list).
    private boolean checkCanHouseCrew(ArrayList<HousingAssignment> toMove, ArrayList<ShipLoc> ignoreList) {
        return true;
    }
    // STUB. TODO: check if cargo can be stored in available space (except in modules in the ignore list).
    private boolean checkStoreCargo(ArrayList<CargoCollection> toStore, ArrayList<ShipLoc> ignoreList) {
        return true;
    }
    //STUB! Todo: check if player can afford the cost.
    private boolean CanAfford(ArrayList<CargoCollection> cost) {
        return true;
    }

    public ArrayList<ShipLoc> getLockedModules() {
        ArrayList<ShipLoc> ret = new ArrayList<>();
        for (RefitTask task : getTaskchain()) {
            Collections.addAll(ret, task.targets);
        }
        return ret;
    }


    private ArrayList<RefitTask> taskchain;


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


    /**
     * Test-creates a spaceship, then prints the structure to console.
     * @param args
     */
    public static void main(String[] args) {
        HomeShip ship = HomeShip.GenerateStart1(new Random(0), 2, 10, 0.0f, 1.0f);
        System.out.println(ship.toString());
    }

    public AbstractShipModule[] getModules() {
        return (AbstractShipModule[]) modules.values().toArray();
    }

    public ArrayList<RefitTask> getTaskchain() {
        return taskchain;
    }

    public static int getMiddleLength() {
        return instance.tailLocation;
    }
    public static int getTailLocation() {
        return instance.tailLocation;
    }
    public static int getFullLength() {
        return instance.fullLength;
    }
}


/* STUB section. All classes below must at some point be expanded and moved.*/
class ConstructionTask {
    private int labourCost;
    private String description;

    public ConstructionTask(int labourCost, String description) {

        this.labourCost = labourCost;
        this.description = description;
    }
}
abstract class SpecialSection extends AbstractShipSection {

    public SpecialSection(HomeShip.ShipLoc loc) {
        super(loc, SectionType.Special);
    }


    /** No modules can be built on a special section!
     * It can only contain pre-built modules.
     * @return an empty list.
     */
    @Override
    public AbstractShipModule[] GetModuleTypes() {
        return new AbstractShipModule[0];
    }

    /**
     * No modules can be built on a special section!
     * It can only contain pre-built modules.
     * @param typeToBuild
     * @param message
     * @return false, always.
     */
    @Override
    public boolean canBuildModule(ModuleType typeToBuild, StringBuffer message) {
        message.append("You cannot build on a special section!");
        return false;
    }

    @Override
    public int getNumComponents() {
        return 0;
    }

    @Override
    public abstractShipComponent[] getComponents() {
        return new abstractShipComponent[0];
    }

    /**
     *  Special modules always exist in weightless conditions.
     * @return always false
     */
    @Override
    public boolean useGravity() {
        return false;
    }

    @Override
    public Collection<CargoCollection> getCargoOnDestruction() {
        return CargoContainer.Null.getCollection();
    }

    /**
     * The head section cannot be destroyed.
     */
    @Override
    public void onDestroy() {

    }
}
class HeadSection extends SpecialSection {

    public HeadSection(HomeShip.ShipLoc loc) {
        super(loc);
    }

    @Override
    public String GetName() {
        return "(STUB) Head Section";
    }
}
class TailSection extends SpecialSection {
    public TailSection(HomeShip.ShipLoc loc) {
        super(loc);
    }

    @Override
    public String GetName() {
        return "(STUB) Tail Section";
    }
}
abstract class SpecialModule extends AbstractShipModule {

    protected SpecialModule(HomeShip.ShipLoc loc) {
        super(ShipPartType.Module, loc);
    }

    @Override
    public int getNumComponents() {
        return 0;
    }

    @Override
    public abstractShipComponent[] getComponents() {
        return new abstractShipComponent[0];
    }

    @Override
    public ModuleType getModuleType() {
        return ModuleType.Special;
    }

    /**
     * Special modules cannot be built manually, and so does not need to bother with gravity.
     * @return
     */
    @Override
    public boolean useGravity() {
        return false;
    }

    @Override
    public Collection<CargoCollection> getCargoOnDestruction() {
        return CargoContainer.Null.getCollection();
    }

    /**
     * Special modules cannot be destroyed!
     */
    @Override
    public void onDestroy() {

    }
}
class MainBridge extends SpecialModule {

    protected MainBridge(HomeShip.ShipLoc loc) {
        super(loc);
    }

    @Override
    public String GetName() {
        return "(STUB) Main bridge";
    }
}
class Engineering extends SpecialModule {

    protected Engineering(HomeShip.ShipLoc loc) {
        super(loc);
    }

    @Override
    public String GetName() {
        return "(STUB) Engineering";
    }
}