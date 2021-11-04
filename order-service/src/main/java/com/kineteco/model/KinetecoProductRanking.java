package com.kineteco.model;

import org.jboss.logging.Logger;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class KinetecoProductRanking {
    private static final Logger LOGGER = Logger.getLogger(KinetecoProductRanking.class);

    private final int max;

    private final Comparator<ProductOrderStats> comparator = Comparator.comparingInt(s -> -1 * s.units);

    private final LinkedList<ProductOrderStats> ranking = new LinkedList<>();

    public KinetecoProductRanking(int size) {
        max = size;
    }

    public Iterable<ProductOrderStats> onNewStat(ProductOrderStats productOrderStats) {
        LOGGER.debugf("on new stat for product %s", productOrderStats.sku);
        // Remove stats if already present,
        ranking.removeIf(s -> s.sku.equalsIgnoreCase(productOrderStats.sku));
        // Add  stats
        ranking.add(productOrderStats);
        // Sort
        ranking.sort(comparator);

        // Drop on overflow
        if (ranking.size() > max) {
            ranking.remove(ranking.getLast());
        }

        return Collections.unmodifiableList(ranking);
    }
}
