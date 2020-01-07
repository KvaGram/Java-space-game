package unicus.spacegame;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class CargoBay {
    private static CargoBay instance;
    public static CargoBay getInstance() {
        return instance;
    }

    private final static int CARGO_MODULES_COUNT = 12;
    CargoModule[] cargos;
    public static void main(String[] args) {
        //Create window
        JFrame frame = new JFrame("Chat Frame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 400);
        JTextArea j_statustextarea = new JTextArea("Cargo Bay Demonstration. \n");
        JPanel j_buttonpanel = new JPanel();

        //Create cargo
        CargoBay allcargo = new CargoBay(); //possibly: pass textarea argument to print out there?
        allcargo.createStartCargo();

        //Populate buttons
        JButton b_sort_compact = new JButton("Compact cargo");
        JButton b_sort_spread = new JButton("Spread cargo");
        JButton b_sort_type = new JButton("Sort cargo");
        JButton b_list = new JButton("List cargo");
        j_buttonpanel.add(b_sort_compact);
        j_buttonpanel.add(b_sort_spread);
        j_buttonpanel.add(b_sort_type);
        j_buttonpanel.add(b_list);
        b_sort_compact.addActionListener(arg0 -> allcargo.reorderCargo("Compact"));
        b_sort_spread.addActionListener(arg0 -> allcargo.reorderCargo("Spread"));
        b_sort_type.addActionListener(arg0 -> allcargo.reorderCargo("Type"));
        b_list.addActionListener(arg0 -> allcargo.ListCargo());
        JButton b_addwater = new JButton("Add 5 waters");
        JButton b_removewater = new JButton("Remove 5 waters");
        j_buttonpanel.add(b_addwater);
        j_buttonpanel.add(b_removewater);
        b_addwater.addActionListener(arg0 -> allcargo.AddCargo(5, "Water"));
        b_removewater.addActionListener(arg0 -> allcargo.RemoveCargo(5, "Water"));

        frame.getContentPane().add(BorderLayout.SOUTH, j_buttonpanel);
        frame.getContentPane().add(BorderLayout.CENTER, j_statustextarea);
        frame.setVisible(true);
    }

    private boolean AddCargo(int amount, String type) {
        int remaining_amount = amount;
        for (CargoModule cargo : cargos) { //for each cargo module
            if (cargo.total_fullness < CargoModule.CARGO_CAPACITY_PER_MODULE) { //check if that module has space
                int add_amount = Math.min(remaining_amount, CargoModule.CARGO_CAPACITY_PER_MODULE - cargo.total_fullness);
                remaining_amount -= add_amount;
                cargo.AddCargo(add_amount, type, false);
            }
        }
        if (remaining_amount != 0) { System.out.println("Couldn't fit it all.");}
        return (remaining_amount == 0);
    }
    private boolean RemoveCargo(int amount, String type) {
        int remaining_amount = amount;
        for (CargoModule cargo : cargos) {
            int moduleAmount = cargo.contents.get(type);
            if (moduleAmount > 0) {
                int remove_amount = Math.min(remaining_amount, moduleAmount);
                remaining_amount -= remove_amount;
                cargo.total_fullness -= remove_amount;
                cargo.contents.put(type, (cargo.contents.get(type)-remove_amount));
            }
        }
        if (remaining_amount != 0) { System.out.println("Didn't have that much.");}
        return (remaining_amount == 0);
    }
    private void ListCargo() {
        for (CargoModule cargo : cargos) {
            cargo.PrintCargo();
        }
    }
    private int[] CountTotalCargo() {
        int[] result = new int[CargoModule.CARGO_TYPECOUNT];
        for (CargoModule cargo : cargos) {
            int[] temp = cargo.CountLocalCargo();
            for (int i=0; i<result.length; i++) {
                result[i] += temp[i];
            }
        }
        return result;
    }

    public CargoBay() { //constructor
        cargos = new CargoModule[CARGO_MODULES_COUNT];
        for (int i=0; i<cargos.length; i++) {
            cargos[i] = new CargoModule(this, i);
        }
        instance = this;
    }
    public void reorderCargo(String sortpattern) {
        switch (sortpattern) {
            case "Compact":
                System.out.println("Compacting cargo...");
                reorderCargoCompactly();
                break;
            case "Type":
                System.out.println("Sorting by type...");
                reorderCargoByType();
                break;
            case "Spread":
                System.out.println("Spreading it all around...");
                reorderCargoSpread();
                break;
        }
    }
    public void reorderCargoCompactly() {
        //see how much cargo we have
        int[] cargoAmounts = CountTotalCargo();
        //remove it all
        for (CargoModule m: cargos) {
            for (String cargotype: CargoModule.CARGO_NAMES) {
                m.RemoveCargo(m.contents.get(cargotype), cargotype, true);
            }
        }
        //put it back in order
        int j = 0;
        for (int i=0; i<cargoAmounts.length; i++) {
            while (cargoAmounts[i] > 0 && j < cargos.length) {
                int chunk = Math.min(cargoAmounts[i], cargos[j].getSpace());
                if (chunk == 0) { j++; }
                else {
                    cargos[j].AddCargo(chunk, CargoModule.CARGO_NAMES[i], false);
                    cargoAmounts[i] -= chunk;
                }
            }
        }
    }
    public void reorderCargoByType() {
        int[] cargoAmounts = CountTotalCargo();
        int roundUpSum = 0;
        int cm = CargoModule.CARGO_CAPACITY_PER_MODULE;
        //Determine if we have space to sort things separately
        for (int cargoAmount : cargoAmounts) {
            roundUpSum += (cargoAmount + cm - 1) / cm;
        }
        if (roundUpSum > cargos.length) {
            System.out.println("Wasn't space to sort completely separately. Falling back to compact sort.");
            reorderCargoCompactly();
        } else {
            for (CargoModule m: cargos) {
                for (String cargotype : CargoModule.CARGO_NAMES) {
                    m.RemoveCargo(m.contents.get(cargotype), cargotype, true);
                }
            }
            for (int i=0; i<cargoAmounts.length; i++) {
                for (int j=0; j<cargos.length; j++) {
                    if (cargoAmounts[i] != 0 && cargos[j].getSpace() == 60) {
                        int chunk = Math.min(cargoAmounts[i], cm);
                        cargoAmounts[i] -= chunk;
                        cargos[j].AddCargo(chunk, CargoModule.CARGO_NAMES[i], false);
                    }
                }
            }
        }
    }
    public void reorderCargoSpread() {
        //first carry out old cargo
        int[] cargoAmounts = CountTotalCargo();
        for (CargoModule m: cargos) {
            for (String cargotype: CargoModule.CARGO_NAMES) {
                m.RemoveCargo(m.contents.get(cargotype), cargotype, true);
            }
        }
        //then put back in new order
        int totalCargo = 0;
        for (int c: cargoAmounts) {totalCargo += c;}
        int i=0;
        while (totalCargo > 0) {
            for (CargoModule unit: cargos) {
                while (i < cargoAmounts.length && cargoAmounts[i] == 0) {
                    i++;
                }
                if (i < cargoAmounts.length) {
                    totalCargo -= 1;
                    cargoAmounts[i] -= 1;
                    unit.AddCargo(1, CargoModule.CARGO_NAMES[i], true);
                }
            }
        }
    }
    public void createStartCargo() {
        AddCargo(12, "Fuel");
        AddCargo(8, "Oxygen");
        AddCargo( 2, "Water");
    }
}


class CargoModule {
    private final static String CARGO_FOOD = "Food";
    private final static String CARGO_WATER = "Water";
    private final static String CARGO_FUEL = "Fuel";
    private final static String CARGO_OXY = "Oxygen";
    private final static String CARGO_SPARES = "Spare parts";
    private final static String CARGO_SHINY = "Shinyium";
    protected final static String[] CARGO_NAMES = {CARGO_FOOD, CARGO_WATER, CARGO_FUEL, CARGO_OXY, CARGO_SPARES, CARGO_SHINY}; //TODO replace with enum
    final static int CARGO_TYPECOUNT = CARGO_NAMES.length;
    final static int CARGO_CAPACITY_PER_MODULE = 60;
    int total_fullness;
    String moduleID;
    HashMap<String, Integer> contents;

    public CargoModule(CargoBay cb, int id) { //constructor
        total_fullness = 0;
        contents = new HashMap<>();
        for (String s: CARGO_NAMES) {
            contents.put(s, 0);
        }
        CargoBay master = cb;
        moduleID = (String) ("Module "+(id+1));
    }
    public boolean AddCargo(int amount, String type, boolean force) {
        if (total_fullness + amount > CARGO_CAPACITY_PER_MODULE) {
            if (force) {
                int free_space = CARGO_CAPACITY_PER_MODULE - total_fullness;
                total_fullness += free_space;
                contents.put(type, free_space);
            }
            return false;
        } else {
            total_fullness += amount;
            contents.put(type, contents.get(type)+amount);
        }
        return true;
    }
    public boolean RemoveCargo(int amount, String type, boolean force) {
        if (contents.get(type) < amount) {
            if (force) {
                total_fullness -= contents.get(type);
                contents.put(type, 0);
            }
            return false;
        } else {
            total_fullness -= amount;
            contents.put(type, contents.get(type)-amount);
        }
        return true;
    }
    public int[] CountLocalCargo() {
        int[] result = new int[CARGO_TYPECOUNT];
        for (int i=0; i<CARGO_TYPECOUNT; i++) {
            result[i] = contents.get(CARGO_NAMES[i]);
        }
        return result;
    }

    public void PrintCargo() {
        System.out.print(moduleID + " contains: ");
        boolean maybeEmpty = true;
        for (String s: CARGO_NAMES) {
            if (contents.get(s) > 0) {
                maybeEmpty = false;
                System.out.print(contents.get(s) + " " +s+ ". ");
            }
        }
        if (maybeEmpty) {  System.out.print("Nothing."); }
        System.out.println();
    }

    public int getSpace() {
        return (CARGO_CAPACITY_PER_MODULE - total_fullness);
    }
}

enum CargoTypes {
    FOOD, WATER, FUEL, OXYGEN, PARTS, SHINYIUM;
}

class BasicCargoCollection implements CargoCollection {
    CargoTypes ctype;
    int numCargo;
    BasicCargoCollection(CargoTypes ctype) {
        this.ctype=ctype;
        numCargo=0;
    }
    @Override
    public int getCargoUnits() {
        return numCargo;
    }

    @Override
    public boolean canMerge(CargoCollection other) {
        BasicCargoCollection bOther = (BasicCargoCollection) other;
        return (this.ctype == bOther.ctype);
    }

    @Override
    public boolean doMerge(CargoCollection other) {
        BasicCargoCollection bOther = (BasicCargoCollection) other;
        assert (this.ctype == bOther.ctype);
        this.numCargo += bOther.numCargo;
        bOther.numCargo = 0;
        return false;
    }
}

//Option one: Separate classes for each type of content. WaterCollection, FoodCollection, FuelCollection...
//Option two: Is anything like 'instanceof this' legal so that classes can inherit polymorphic code?
//Option three: Get an eval() function into java somehow.
//Option four: generic class, multiple instances, each constructed with an ID for what it's a collection of
