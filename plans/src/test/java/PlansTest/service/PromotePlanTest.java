package PlansTest.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.psoft_22_23_project.plansmanagement.model.*;
import com.example.psoft_22_23_project.plansmanagement.repositories.PlansRepository;
import com.example.psoft_22_23_project.plansmanagement.services.PlansServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.when;

public class PromotePlanTest {

    @Mock
    private PlansRepository plansRepository;

    @InjectMocks
    private PlansServiceImpl plansService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    private Plans createPlan(String name, boolean active, boolean promoted) {
        Name planName = new Name();
        planName.setName(name);

        Description description = new Description();
        description.setDescription("Plan Description");

        NumberOfMinutes numberOfMinutes = new NumberOfMinutes();
        numberOfMinutes.setNumberOfMinutes("100");

        MaximumNumberOfUsers maximumNumberOfUsers = new MaximumNumberOfUsers();
        maximumNumberOfUsers.setMaximumNumberOfUsers(5);

        MusicCollection musicCollection = new MusicCollection();
        musicCollection.setMusicCollection(10);

        MusicSuggestion musicSuggestion = new MusicSuggestion();
        musicSuggestion.setMusicSuggestion("personalized");

        AnnualFee annualFee = new AnnualFee();
        annualFee.setAnnualFee(100.00);

        MonthlyFee monthlyFee = new MonthlyFee();
        monthlyFee.setMonthlyFee(10.00);

        Active activeStatus = new Active();
        activeStatus.setActive(active);

        Promoted promotedStatus = new Promoted();
        promotedStatus.setPromoted(promoted);

        return new Plans(planName, description, numberOfMinutes, maximumNumberOfUsers,
                musicCollection, musicSuggestion, annualFee, monthlyFee, activeStatus, promotedStatus);
    }

    private void setupMockRepository(String planName, Plans plan) {
        when(plansRepository.findByName_Name(planName)).thenReturn(Optional.of(plan));
        when(plansRepository.save(plan)).thenReturn(plan); // Mock the save operation
    }

    @Test
    public void promoteTest() throws IOException, URISyntaxException, InterruptedException {
        String planName = "Plan Name";
        Plans plan = createPlan(planName, true, false);
        setupMockRepository(planName, plan);

        PromotionResult result = plansService.promote(planName, 0L);

        assertNotNull(result.getNewPromotedPlan());
        assertTrue(result.getNewPromotedPlan().getPromoted().getPromoted());
        assertNull(result.getPreviousPromotedPlan());
        verify(plansRepository).save(plan);
    }


    @Test
    public void promoteTest_InactivePlan() {
        String planName = "Inactive Plan";
        Plans plan = createPlan(planName, false, false);
        setupMockRepository(planName, plan);

        assertThrows(IllegalArgumentException.class, () -> plansService.promote(planName, 0L));
    }

    @Test
    public void promoteTest_AlreadyPromotedPlan() {
        String planName = "Promoted Plan";
        Plans plan = createPlan(planName, true, true);
        setupMockRepository(planName, plan);

        assertThrows(IllegalArgumentException.class, () -> plansService.promote(planName, 0L));
    }

    @Test
    public void promoteTest_ExistingPromotedPlan() throws IOException, URISyntaxException, InterruptedException {
        String planName = "Plan Name";
        Plans plan = createPlan(planName, true, false);
        setupMockRepository(planName, plan);

        Plans existingPromotedPlan = createPlan("Existing Promoted Plan", true, true);
        when(plansRepository.findByPromoted_Promoted(true)).thenReturn(Optional.of(existingPromotedPlan));

        PromotionResult result = plansService.promote(planName, 0L);

        assertTrue(result.getNewPromotedPlan().getPromoted().getPromoted());
        assertEquals(existingPromotedPlan, result.getPreviousPromotedPlan());
        verify(plansRepository).save(plan);
        verify(plansRepository).save(existingPromotedPlan);
    }

}
