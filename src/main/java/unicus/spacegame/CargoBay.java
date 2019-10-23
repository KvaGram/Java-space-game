package unicus.spacegame;
import javax.swing.*;
import java.awt.*;
//I don't know why I need to import these next two separately
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;

public class CargoBay {
    final static int CARGO_MODULES_COUNT = 12;
    CargoModule[] cargos;
    public static void main(String[] args) {
        //Create window
        JFrame frame = new JFrame("Chat Frame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 400);
        JTextArea j_statustextarea = new JTextArea("Cargo Bay Demonstration. \n");
        JPanel j_buttonpanel = new JPanel();

        //Create cargo
        CargoBay allcargo = new CargoBay(j_statustextarea); //let printout know about textarea
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
        b_sort_compact.addActionListener(arg0 -> allcargo.SortCargo("Compact"));
        b_sort_spread.addActionListener(arg0 -> allcargo.SortCargo("Spread"));
        b_sort_type.addActionListener(arg0 -> allcargo.SortCargo("Type"));
        b_list.addActionListener(arg0 -> allcargo.ListCargo());
        JButton b_addwater = new JButton("Add 5 waters");
        JButton b_removewater = new JButton("Remove 5 waters");
        j_buttonpanel.add(b_addwater);
        j_buttonpanel.add(b_removewater);

        frame.getContentPane().add(BorderLayout.SOUTH, j_buttonpanel);
        frame.getContentPane().add(BorderLayout.CENTER, j_statustextarea);
        frame.setVisible(true);
    }

    private void ListCargo() {
        for (int i=0; i<cargos.length; i++) {
            cargos[i].PrintCargo();
        }
    }

    public CargoBay(JTextArea text) {
        cargos = new CargoModule[CARGO_MODULES_COUNT];
        for (int i=0; i<cargos.length; i++) {
            cargos[i] = new CargoModule(this);
        }
    }
    public void SortCargo(String sortpattern) {
        if (sortpattern == "Compact") {
            System.out.println("Compacting cargo...");
        } else if (sortpattern == "Type") {
            System.out.println("Sorting by type...");
        } else if (sortpattern == "Spread") {
            System.out.println("Spreading it all around...");
        }
    }
    public void createStartCargo() {
        assert true;
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
    private enum CARGO_TYPES {CARGO_FOOD, CARGO_WATER, CARGO_FUEL, CARGO_OXY, CARGO_SPARES, CARGO_SHINY}
    final static int CARGO_TYPECOUNT = CARGO_NAMES.length;
    final static int CARGO_CAPACITY_PER_MODULE = 600;
    private int fullness;
    private int[] amount_per_cargo;

    public CargoModule(CargoBay cb) { //constructor
        fullness = 0;
        amount_per_cargo = new int[CARGO_TYPECOUNT];
        CargoBay master = cb;
    }
    public void AddCargo(int amount, String type) {
        if (fullness + amount > CARGO_CAPACITY_PER_MODULE) {
            //decide how to complain on capacity failure. return type bool?
        } else {
            fullness += amount;
            amount_per_cargo[CARGO_NAMES.indexOf(type)] += amount; //Fucking java, what's this called
        }
    }

    public void PrintCargo() {
        for (int i=0; i<CARGO_TYPES; i++) {
            if (amount_per_cargo[i] > 0) {
                System.out.print(CARGO_NAMES[i] + ":" + Arrays.toString(amount_per_cargo) + " ");
            }
        }
    }
}