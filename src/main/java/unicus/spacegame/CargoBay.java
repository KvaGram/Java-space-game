package unicus.spacegame;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class CargoBay {
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
                cargo.AddCargo(add_amount, type);
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

    public CargoBay() { //constructor
        cargos = new CargoModule[CARGO_MODULES_COUNT];
        for (int i=0; i<cargos.length; i++) {
            cargos[i] = new CargoModule(this, i);
        }
    }
    public void reorderCargo(String sortpattern) {
        if (sortpattern == "Compact") {
            System.out.println("Compacting cargo...");
        } else if (sortpattern == "Type") {
            System.out.println("Sorting by type...");
        } else if (sortpattern == "Spread") {
            System.out.println("Spreading it all around...");
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
    private final static String[] CARGO_NAMES = {CARGO_FOOD, CARGO_WATER, CARGO_FUEL, CARGO_OXY, CARGO_SPARES, CARGO_SHINY};
    //final static int CARGO_TYPECOUNT = CARGO_NAMES.length;
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
    public void AddCargo(int amount, String type) {
        if (total_fullness + amount > CARGO_CAPACITY_PER_MODULE) {
            //decide how to complain on capacity failure. return type bool?
        } else {
            total_fullness += amount;
            contents.put(type, amount+contents.get(type));
        }
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
        System.out.println("");
    }
}