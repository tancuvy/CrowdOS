package cn.crowdos.kernel;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.image.Kernel;

import static org.junit.jupiter.api.Assertions.*;

class CrowdKernelTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void version() {
        System.out.println(CrowdKernel.version());
        assertEquals(1, 1);
    }
}