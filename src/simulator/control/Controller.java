package simulator.control;

import org.json.JSONArray;
import org.json.JSONObject;
import simulator.model.*;
import simulator.view.SimpleObjectViewer;
import simulator.view.SimpleObjectViewer.ObjInfo;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    private final Simulator _sim;

    public Controller(Simulator sim) {
        this._sim = sim;
    }

    /**
     * Loads simulation data from the provided JSON object.
     *
     * @param data JSON object containing simulation data
     */
    public void load_data(JSONObject data) {
        if (data.has("regions")) {
            JSONArray regions = data.optJSONArray("regions");
            load(regions);
        }

        JSONArray animals = data.getJSONArray("animals");
        for (int i = 0; i < animals.length(); i++) {
            JSONObject animalSpec = animals.getJSONObject(i);
            int amount = animalSpec.getInt("amount");
            JSONObject spec = animalSpec.getJSONObject("spec");
            for (int j = 0; j < amount; j++) {
                _sim.add_animal(spec);
            }
        }
    }

    private void load(JSONArray regions) {
        for (int i = 0; i < regions.length(); i++) {
            JSONObject regionSpec = regions.getJSONObject(i);
            JSONArray rowArray = regionSpec.getJSONArray("row");
            JSONArray colArray = regionSpec.getJSONArray("col");
            JSONObject spec = regionSpec.getJSONObject("spec");
            if (rowArray != null && colArray != null) {
                json_to_sim_converter(rowArray, colArray, spec);
            } else {
                int row = regionSpec.getInt("row");
                int col = regionSpec.getInt("col");
                _sim.set_region(row, col, spec);
            }
        }
    }

    /**
     * Runs the simulation for the specified duration.
     *
     * @param t   Duration of the simulation
     * @param dt  Time step for the simulation
     * @param sv  Indicates whether to display the simulation visually
     * @param out Output stream to write simulation results
     */
    public void run(double t, double dt, boolean sv, OutputStream out) {
        JSONObject init_state = _sim.as_JSON();
        JSONObject final_state;
        SimpleObjectViewer view = null;
        if (sv) {
            MapInfo m = _sim.get_map_info();
            view = new SimpleObjectViewer("ECOSYSTEM", m.get_width(), m.get_height(), m.get_cols(), m.get_rows());
            view.update(to_animals_info(_sim.getAnimals()), _sim.get_time(), dt);

        }

        while (_sim.get_time() < t) {
            _sim.advance(dt);
            if (sv) {
                view.update(to_animals_info(_sim.getAnimals()), _sim.get_time(), dt);
            }
        }
        final_state = _sim.as_JSON();
        JSONObject output = new JSONObject();
        output.put("in", init_state);
        output.put("out", final_state);

        try {
            out.write(output.toString(2).getBytes());
        } catch (Exception e) {
            System.err.println("Error while writing the output file: " + e.getLocalizedMessage());
        }

        if (sv)
            view.close();
    }

    /**
     * Converts list of animal information to list of object information.
     *
     * @param animals List of animal information
     * @return List of object information
     */
    private List<ObjInfo> to_animals_info(List<? extends AnimalInfo> animals) {
        List<ObjInfo> ol = new ArrayList<>(animals.size());
        for (AnimalInfo animal : animals) {
            ol.add(new ObjInfo(animal.get_genetic_code(), (int) animal.get_position().getX(), (int) animal.get_position().getY(), (int) Math.round(animal.get_age()) + 2));
        }
        return ol;
    }

    public void reset(int cols, int rows, int width, int height) {
        _sim.reset(cols, rows, width, height);
    }
    public void set_regions(JSONObject rs) {
        JSONArray regionsArray = rs.getJSONArray("regions");
        for (int i = 0; i < regionsArray.length(); i++) {
            JSONObject regionObject = regionsArray.getJSONObject(i);
            JSONArray rowArray = regionObject.getJSONArray("row");
            JSONArray colArray = regionObject.getJSONArray("col");
            JSONObject spec = regionObject.getJSONObject("spec");

            if (spec.getString("type").equals("dynamic")) {
            } else {
                if (rowArray.length() == 1 && colArray.length() == 1) {
                    int row = rowArray.getInt(0);
                    int col = colArray.getInt(0);
                    _sim.set_region(row, col, spec);
                } else {
                    json_to_sim_converter(rowArray, colArray, spec);
                }
            }
        }
    }

    private void json_to_sim_converter(JSONArray rowArray, JSONArray colArray, JSONObject spec) {
        for (int j = 0; j < rowArray.length(); j++) {
            for (int k = 0; k < colArray.length(); k++) {
                int row = rowArray.getInt(j);
                int col = colArray.getInt(k);
                _sim.set_region(row, col, spec);
            }
        }
    }


    public void advance(double dt) {
        _sim.advance(dt);
    }

    public void addObserver(EcoSysObserver o) {
        _sim.addObserver(o);
    }

    public void removeObserver(EcoSysObserver o){
        _sim.removeObserver(o);
    }

    public List<String> getAnimalSpecies() {
        List<String> species = new ArrayList<>();
        List<Animal> animals = _sim.getAnimals();
        for (AnimalInfo animal : animals) {
            String speciesName = animal.get_genetic_code();
            if (!species.contains(speciesName)) {
                species.add(speciesName);
            }
        }
        return species;
    }
}

