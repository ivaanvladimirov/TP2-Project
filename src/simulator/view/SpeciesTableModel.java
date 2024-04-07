package simulator.view;

import simulator.control.Controller;
import simulator.model.*;

import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SpeciesTableModel extends AbstractTableModel implements EcoSysObserver {
    private static final long serialVersionUID = 1L;
    private Controller _ctrl;
    private List<AnimalInfo> _animals;
    private List<String> _columns;
    private List<List<Object>> _data;

    SpeciesTableModel(Controller ctrl) {
        // TODO initialise the corresponding data structures
        _ctrl = ctrl;
        _columns = new ArrayList<>();
        _data = new ArrayList<>();
        _animals = new ArrayList<>();
        _ctrl.addObserver(this);
    }
    // TODO the rest of the methods go here...


    @Override
    public int getRowCount() {
        return 0;
    }

    @Override
    public int getColumnCount() {
        return 0;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return null;
    }

    @Override
    public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {

    }

    @Override
    public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {

    }

    @Override
    public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {

    }

    @Override
    public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {

    }

    @Override
    public void open(Component parent) {

    }

    @Override
    public void onAvanced(double currentTime, RegionManager regionManager, ArrayList<Animal> animals, double dt) {

    }
}
