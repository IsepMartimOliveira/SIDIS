package repository;

import com.example.psoft_22_23_project.plansmanagement.model.*;
import com.example.psoft_22_23_project.plansmanagement.services.PlansManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PlansRepositoryTest {

    @Mock
    private PlansManager plansRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    private Plans createPlan(String name) {
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
        activeStatus.setActive(true);

        Promoted promotedStatus = new Promoted();
        promotedStatus.setPromoted(true);

        return new Plans(planName, description, numberOfMinutes, maximumNumberOfUsers,
                musicCollection, musicSuggestion, annualFee, monthlyFee, activeStatus, promotedStatus);
    }
    @Test
    void testFindByName() throws IOException, URISyntaxException, InterruptedException {
        // Mock the repository method
        Plans plan = createPlan("Plan Name");
        when(plansRepository.findByName("Plan Name")).thenReturn(Optional.of(plan));

        // Call the method
        Optional<Plans> result = plansRepository.findByName("Plan Name");

        // Verify the result
        assertEquals(Optional.of(plan), result);
        verify(plansRepository).findByName("Plan Name");
    }

    @Test
    void testFindByActive() throws IOException, URISyntaxException, InterruptedException {
        Plans plan = createPlan("Plan Name");
        when(plansRepository.findByName("Plan Name")).thenReturn(Optional.of(plan));
        when(plansRepository.save(Mockito.any(Plans.class))).thenReturn(plan);
        plansRepository.save(plan);
        Iterable<Plans> result = plansRepository.findByActive_Active(true);
        assertEquals(plan, result);
        verify(plansRepository).findByActive_Active(true);
    }



}
