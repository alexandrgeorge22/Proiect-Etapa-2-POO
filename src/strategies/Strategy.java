package strategies;

import fileio.Distributor;
import fileio.Producer;

import java.util.List;

public interface Strategy {
    void applyStrategy(final Distributor distributor, final List<Producer> producers);
}
