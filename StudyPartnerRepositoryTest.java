package edu.comp390.studyapp;

import org.junit.jupiter.api.*;
import java.nio.file.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class StudyPartnerRepositoryTest {
    private StudyPartnerRepository repo;

    @BeforeEach
    void setUp() throws Exception {
        Path dbFile = Files.createTempFile("study-partner-test-", ".db");
        Database db = new Database("jdbc:sqlite:" + dbFile.toAbsolutePath());
        DatabaseInitializer.initialize(db);
        repo = new StudyPartnerRepository(db);
    }

    @Test
    void avaCanFindOtherStudentsInComp390() throws Exception {
        List<Student> partners = repo.findStudyPartners(1,1);
        assertTrue(partners.stream().anyMatch(s -> s.firstName().equals("Noah")));
        assertTrue(partners.stream().anyMatch(s -> s.firstName().equals("Maya")));
    }

    @Test
    void avaDoesNotAppearInOwnResults() throws Exception {
        List<Student> partners = repo.findStudyPartners(1,1);
        assertTrue(partners.stream().noneMatch(s -> s.id() == 1));
    }

    @Test
    void newRequestIsStoredAsPending() throws Exception {
        int id = repo.createStudyRequest(1,2,1,"Study this week?");
        StudyRequest r = repo.findRequestById(id);
        assertNotNull(r);
        assertEquals(1, r.senderId());
        assertEquals(2, r.receiverId());
        assertEquals("pending", r.status());
    }
}
