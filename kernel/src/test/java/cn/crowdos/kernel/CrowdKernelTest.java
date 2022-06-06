package cn.crowdos.kernel;

import cn.crowdos.kernel.system.SystemResourceCollection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CrowdKernelTest {

    CrowdKernel single;
    @BeforeEach
    void setUp() {
        single = Kernel.getKernel();
    }

    @AfterEach
    void tearDown() {
        Kernel.shutdown();
    }

    @Test
    void debug(){

    }
    @Test
    void getKernel() {
        CrowdKernel kernel = Kernel.getKernel();
        assertSame(kernel, single);
    }

    @Test
    void testVersion() {
        System.out.println(Kernel.version());
    }

    @Test
    void testInitial() {
        single.initial();
        assertTrue(single.isInitialed());
    }

    @Test
    void testInitial1() {
    }

    @Test
    void getSystemResourceCollection() {
        SystemResourceCollection systemResourceCollection = null;
        single.initial();
        systemResourceCollection = single.getSystemResourceCollection();
        assertNotNull(systemResourceCollection);
    }

    @Test
    void isInitialed() {
        System.out.println(single.isInitialed());
        assertFalse(single.isInitialed());
        single.initial();
        assertTrue(single.isInitialed());
    }

    @Test
    void submitTask() {
    }

    @Test
    void getTasks() {
        single.initial();
        single.getTasks();
    }

    @Test
    void getTasksIter() {
    }

    @Test
    void getTaskAssignmentScheme() {
    }

    @Test
    void getTaskRecommendationScheme() {
    }

    @Test
    void getTaskParticipantSelectionResult() {
    }

    @Test
    void getParticipants() {
    }

    @Test
    void getParticipantsIter() {
    }
}