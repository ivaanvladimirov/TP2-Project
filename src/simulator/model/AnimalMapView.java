package simulator.model;

import java.util.List;
import java.util.function.Predicate;

public interface AnimalMapView {
    public List<Animal> get_animals_in_range(Animal e, Predicate<Animal> filter);
    public int get_cols();
    public int get_rows();
    public int get_width();
    public int get_height();
    public int get_region_width();
    public int get_region_height();

}
