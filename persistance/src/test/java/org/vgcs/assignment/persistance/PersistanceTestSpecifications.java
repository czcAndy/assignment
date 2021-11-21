package org.vgcs.assignment.persistance;

import org.junit.jupiter.api.Test;

public interface PersistanceTestSpecifications {
    @Test
    void findById_OK_test();

    @Test
    void findById_not_OK_test();
}
