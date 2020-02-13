package unicus.spacegame.ui.crew;

import de.gurkenlabs.litiengine.graphics.Spritesheet;
import de.gurkenlabs.litiengine.gui.ImageComponent;
import de.gurkenlabs.litiengine.gui.ImageComponentList;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.IntConsumer;

public class CrewCardMenu extends ImageComponentList {
    private String[][] crewTextLines;
    private int currentSelectionIndex;

    private int[] crewList;
    private final List<IntConsumer> selectionChangeConsumers;

    public CrewCardMenu(double x, double y, double width, double height, int rows, int columns, int[] crewList, String[][] crewTextLines) {
        this(x, y, width, height, rows, columns, crewList, crewTextLines, null);
    }
    public CrewCardMenu(double x, double y, double width, double height, int rows, int columns, int[] crewList, String[][] crewTextLines, Spritesheet background) {
        super(x, y, width, height, rows, columns, null, background);
        this.crewTextLines = crewTextLines;
        assert (crewList.length <= columns * rows);

        this.crewList = crewList;
        this.selectionChangeConsumers = new CopyOnWriteArrayList<>();
    }

    public int getCurrentSelectionIndex() {
        return this.currentSelectionIndex;
    }
    public int getSelectedCrewID() {
        return crewList[currentSelectionIndex];
    }

    public void onChange(final IntConsumer cons) {
        this.selectionChangeConsumers.add(cons);
    }

    public void setCurrentSelectionIndex(final int currentSelectionIndex) {
        this.currentSelectionIndex = currentSelectionIndex;

        for (int i = 0; i < this.getCellComponents().size(); i++) {
            this.getCellComponents().get(this.currentSelectionIndex).setSelected(i == this.currentSelectionIndex);
        }

        this.selectionChangeConsumers.forEach(c -> c.accept(this.getCurrentSelectionIndex()));
    }

    @Override
    public void prepare() {
        super.prepare();
        int totalCells = getColumns() * getRows();
        for (int i = 0; i < totalCells; i++) {
            final ImageComponent cell = this.getCellComponents().get(i);
            int crewID = 0;
            if(i < crewList.length)
                crewID = crewList[i];
            cell.setImage(CrewCard.makeCard((int)cell.getWidth(), (int)cell.getHeight(), crewID, crewTextLines[i]));
        }
    }

    public int[] getCrewList() {
        return crewList;
    }

    public void setCrewList(int[] crewList, String[][] crewTextLines){
        assert (crewList.length <= getRows() * getColumns());
        this.crewList = crewList;
        this.crewTextLines = crewTextLines;
        if(!isSuspended())
            prepare();
    }
}
